package com.monprojet.backend.service;

import com.monprojet.backend.model.ResetPasswordRequest;
import com.monprojet.backend.model.Utilisateur;
import com.monprojet.backend.repository.ResetPasswordRequestRepository;
import com.monprojet.backend.repository.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PasswordResetService {

    /** Résultat d'une opération : ok, ou refus avec statut à traduire côté contrôleur. */
    public enum Resultat { OK, INTROUVABLE, DEJA_TRAITEE, MAUVAIS_MOT_DE_PASSE }

    private final ResetPasswordRequestRepository resetRepo;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(ResetPasswordRequestRepository resetRepo,
                                UtilisateurRepository utilisateurRepository,
                                PasswordEncoder passwordEncoder) {
        this.resetRepo = resetRepo;
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Resultat creerDemande(String username) {
        if (utilisateurRepository.findByUsername(username).isEmpty()) {
            return Resultat.INTROUVABLE;
        }
        ResetPasswordRequest req = new ResetPasswordRequest();
        req.setUsername(username);
        req.setStatut("EN_ATTENTE");
        req.setDatedemande(LocalDateTime.now().toString());
        resetRepo.save(req);
        return Resultat.OK;
    }

    public List<ResetPasswordRequest> demandesEnAttente() {
        return resetRepo.findByStatut("EN_ATTENTE");
    }

    /** Reset par l'admin : mot de passe temporaire, à changer à la prochaine connexion. */
    @Transactional
    public Resultat reinitialiser(Long demandeId, String nouveauMotDePasse) {
        Optional<ResetPasswordRequest> optReq = resetRepo.findById(demandeId);
        if (optReq.isEmpty()) return Resultat.INTROUVABLE;

        ResetPasswordRequest req = optReq.get();
        if (!"EN_ATTENTE".equals(req.getStatut())) return Resultat.DEJA_TRAITEE;

        Optional<Utilisateur> optUser = utilisateurRepository.findByUsername(req.getUsername());
        if (optUser.isEmpty()) return Resultat.INTROUVABLE;

        Utilisateur u = optUser.get();
        u.setPassword(passwordEncoder.encode(nouveauMotDePasse));
        u.setPremierConnexion(true);
        utilisateurRepository.save(u);

        req.setStatut("TRAITE");
        resetRepo.save(req);
        return Resultat.OK;
    }

    /** Changement par l'utilisateur lui-même : exige la preuve du mot de passe actuel. */
    @Transactional
    public Resultat changerMotDePasse(String username, String motDePasseActuel, String nouveau) {
        Optional<Utilisateur> opt = utilisateurRepository.findByUsername(username);
        if (opt.isEmpty()) return Resultat.INTROUVABLE;

        Utilisateur u = opt.get();
        if (u.getPassword() == null
                || !passwordEncoder.matches(motDePasseActuel, u.getPassword())) {
            return Resultat.MAUVAIS_MOT_DE_PASSE;
        }
        u.setPassword(passwordEncoder.encode(nouveau));
        u.setPremierConnexion(false);
        utilisateurRepository.save(u);
        return Resultat.OK;
    }
}
