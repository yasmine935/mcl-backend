package com.monprojet.backend.service;

import com.monprojet.backend.dto.TicketClientDtos.CreationRequest;
import com.monprojet.backend.model.TicketClient;
import com.monprojet.backend.model.Utilisateur;
import com.monprojet.backend.repository.TicketClientRepository;
import com.monprojet.backend.repository.UtilisateurRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TicketClientService {

    /** Rôles habilités à recevoir et traiter les tickets client (les « 3 valideurs »). */
    public static final Set<String> ROLES_VALIDEUR = Set.of("MANAGER", "ADMINISTRATEUR");

    public enum Refus { INTROUVABLE, NON_AUTORISE, DEJA_PRIS, TRANSITION_INVALIDE }

    /** Résultat d'une opération : soit un ticket, soit un motif de refus. */
    public record Resultat(TicketClient ticket, Refus refus) {
        static Resultat ok(TicketClient t) { return new Resultat(t, null); }
        static Resultat ko(Refus r) { return new Resultat(null, r); }
        public boolean estOk() { return refus == null; }
    }

    private final TicketClientRepository repo;
    private final UtilisateurRepository utilisateurRepository;

    public TicketClientService(TicketClientRepository repo, UtilisateurRepository utilisateurRepository) {
        this.repo = repo;
        this.utilisateurRepository = utilisateurRepository;
    }

    private boolean estValideur(Authentication auth) {
        return auth != null && auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).anyMatch(ROLES_VALIDEUR::contains);
    }

    /** Création par le client connecté — il devient propriétaire du ticket. */
    @Transactional
    public TicketClient creer(CreationRequest req, String username) {
        Utilisateur client = utilisateurRepository.findByUsername(username).orElse(null);
        TicketClient t = new TicketClient();
        t.setTitre(req.titre());
        t.setDescription(req.description());
        t.setCategorie(req.categorie());
        t.setPriorite(req.priorite() != null ? req.priorite() : "Moyenne");
        t.setStatut(TicketClient.EN_ATTENTE_VALIDATION);
        t.setClient(client);
        t.setDateCreation(LocalDateTime.now());
        t.setDateMaj(LocalDateTime.now());
        long count = repo.count();
        t.setNumero("TC-" + String.format("%04d", count + 1));
        return repo.save(t);
    }

    /** Liste cloisonnée : un valideur voit tout, un client ne voit que ses tickets. */
    public List<TicketClient> lister(String username, Authentication auth) {
        if (estValideur(auth)) {
            return repo.findAllByOrderByDateCreationDesc();
        }
        Utilisateur client = utilisateurRepository.findByUsername(username).orElse(null);
        if (client == null) return List.of();
        return repo.findByClientIdOrderByDateCreationDesc(client.getId());
    }

    /** Accès à un ticket : le valideur ou le client propriétaire uniquement. */
    public Resultat parId(Long id, String username, Authentication auth) {
        Optional<TicketClient> opt = repo.findById(id);
        if (opt.isEmpty()) return Resultat.ko(Refus.INTROUVABLE);
        TicketClient t = opt.get();
        if (!peutVoir(t, username, auth)) return Resultat.ko(Refus.NON_AUTORISE);
        return Resultat.ok(t);
    }

    private boolean peutVoir(TicketClient t, String username, Authentication auth) {
        if (estValideur(auth)) return true;
        return t.getClient() != null && username != null
                && username.equals(t.getClient().getUsername());
    }

    /**
     * Prise en charge d'une décision par un valideur. Le PREMIER valideur qui agit
     * devient traitePar ; un autre valideur ne peut plus décider ensuite (DEJA_PRIS),
     * mais tous continuent de voir qui l'a pris.
     */
    @Transactional
    public Resultat decider(Long id, String nouveauStatut, String commentaire,
                            String username, Authentication auth) {
        if (!estValideur(auth)) return Resultat.ko(Refus.NON_AUTORISE);
        Optional<TicketClient> opt = repo.findById(id);
        if (opt.isEmpty()) return Resultat.ko(Refus.INTROUVABLE);
        TicketClient t = opt.get();

        Utilisateur acteur = utilisateurRepository.findByUsername(username).orElse(null);
        // Un ticket déjà pris par un AUTRE valideur ne peut plus être traité par un tiers.
        if (t.getTraitePar() != null && acteur != null
                && !t.getTraitePar().getId().equals(acteur.getId())) {
            return Resultat.ko(Refus.DEJA_PRIS);
        }
        if (!transitionAutorisee(t.getStatut(), nouveauStatut)) {
            return Resultat.ko(Refus.TRANSITION_INVALIDE);
        }

        t.setStatut(nouveauStatut);
        if (t.getTraitePar() == null) {
            t.setTraitePar(acteur);
            t.setDateTraitement(LocalDateTime.now());
        }
        if (commentaire != null && !commentaire.isBlank()) {
            t.setCommentaireValidation(commentaire);
        }
        t.setDateMaj(LocalDateTime.now());
        return Resultat.ok(repo.save(t));
    }

    /** Transitions autorisées du cycle de vie. */
    private boolean transitionAutorisee(String actuel, String cible) {
        return switch (cible) {
            case TicketClient.VALIDE, TicketClient.REJETE ->
                    TicketClient.EN_ATTENTE_VALIDATION.equals(actuel) || TicketClient.NOUVEAU.equals(actuel);
            case TicketClient.EN_COURS -> TicketClient.VALIDE.equals(actuel);
            case TicketClient.RESOLU -> TicketClient.EN_COURS.equals(actuel);
            case TicketClient.CLOTURE -> TicketClient.RESOLU.equals(actuel);
            default -> false;
        };
    }
}
