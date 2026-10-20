package com.monprojet.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monprojet.backend.model.Tache;
import com.monprojet.backend.repository.UtilisateurRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Étapes d'avancement d'un projet, pilotées par son statut.
 *
 * À chaque changement de statut : les étapes jusqu'à celle du nouveau statut sont
 * cochées (date = date du changement, les étapes sautées sont marquées « auto »),
 * les suivantes sont décochées, et le changement est inscrit dans l'historique.
 * « Perdu » n'a pas d'étape : les étapes restent telles quelles, seul l'historique bouge.
 * « Validation Resp » n'est jamais cochée ni comptée (notion de responsable non implémentée).
 */
@Service
public class ProjetEtapesService {

    public static final List<String> ETAPES = List.of(
            "Qualification", "Devis", "Validation Resp", "Bon de commande", "Réalisation", "Clôture");
    public static final String ETAPE_IGNOREE = "Validation Resp";

    /** Statut affiché → étape correspondante. */
    private static final Map<String, String> ETAPE_DU_STATUT = Map.of(
            "Qualification", "Qualification",
            "Devis", "Devis",
            "Commande", "Bon de commande",
            "En cours", "Réalisation",
            "Réalisé", "Clôture");

    /** Anciens codes/libellés encore en base → statut affiché (miroir de statuts-projet.ts). */
    private static final Map<String, String> ANCIENS_STATUTS = Map.ofEntries(
            Map.entry("A_FAIRE", "Qualification"), Map.entry("EN_COURS", "En cours"),
            Map.entry("TERMINEE", "Réalisé"), Map.entry("En Qualification", "Qualification"),
            Map.entry("En Attente", "Qualification"), Map.entry("Fait", "Réalisé"),
            Map.entry("Validation Resp", "Devis"), Map.entry("Bon de commande", "Commande"),
            Map.entry("Réalisation", "Réalisé"), Map.entry("Clôture", "Réalisé"));
    private static final List<String> STATUTS = List.of("Qualification", "Devis", "Commande", "En cours", "Réalisé", "Perdu");

    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final ZoneId FUSEAU = ZoneId.of("Europe/Paris");

    private final ObjectMapper objectMapper;
    private final UtilisateurRepository utilisateurRepository;

    public ProjetEtapesService(ObjectMapper objectMapper, UtilisateurRepository utilisateurRepository) {
        this.objectMapper = objectMapper;
        this.utilisateurRepository = utilisateurRepository;
    }

    /** Ramène un statut brut à l'un des 6 statuts affichés (inconnu/vide → Qualification). */
    public static String normaliser(String statutBrut) {
        if (statutBrut != null && STATUTS.contains(statutBrut)) return statutBrut;
        return statutBrut == null ? "Qualification" : ANCIENS_STATUTS.getOrDefault(statutBrut, "Qualification");
    }

    /**
     * À appeler après avoir posé le nouveau statut sur le projet. Ne fait rien si le
     * statut (normalisé) n'a pas changé — sauf pour un projet neuf (ancienStatut null),
     * dont les étapes sont initialisées.
     */
    public void appliquerChangementStatut(Tache projet, String ancienStatut) {
        String nouveau = normaliser(projet.getStatut());
        boolean creation = ancienStatut == null;
        if (!creation && Objects.equals(normaliser(ancienStatut), nouveau)) return;

        String maintenant = ZonedDateTime.now(FUSEAU).format(FORMAT_DATE);
        String par = utilisateurCourant();

        List<Map<String, Object>> etapes = lireEtapes(projet.getEtapes());
        String etapeCible = ETAPE_DU_STATUT.get(nouveau);   // null pour « Perdu »
        if (etapeCible != null) {
            int cible = ETAPES.indexOf(etapeCible);
            for (int i = 0; i < etapes.size(); i++) {
                Map<String, Object> etape = etapes.get(i);
                if (ETAPE_IGNOREE.equals(etape.get("nom"))) {
                    vider(etape);
                } else if (i <= cible) {
                    if (!Boolean.TRUE.equals(etape.get("done"))) {
                        etape.put("done", true);
                        etape.put("doneAt", maintenant);
                        etape.put("doneBy", par);
                        etape.put("auto", i < cible);   // étape sautée, cochée par le changement de statut
                    }
                } else {
                    vider(etape);
                }
            }
        }
        projet.setEtapes(ecrire(etapes));

        List<Map<String, Object>> historique = lireListe(projet.getHistoriqueStatut());
        Map<String, Object> entree = new LinkedHashMap<>();
        entree.put("ancien", creation ? null : normaliser(ancienStatut));
        entree.put("statut", nouveau);
        entree.put("at", maintenant);
        entree.put("par", par);
        historique.add(entree);
        projet.setHistoriqueStatut(ecrire(historique));
    }

    private static void vider(Map<String, Object> etape) {
        etape.put("done", false);
        etape.put("doneAt", "");
        etape.put("doneBy", "");
        etape.remove("auto");
    }

    /** Étapes en base, complétées/réordonnées selon la liste officielle. */
    private List<Map<String, Object>> lireEtapes(String json) {
        List<Map<String, Object>> enBase = lireListe(json);
        List<Map<String, Object>> etapes = new ArrayList<>();
        for (String nom : ETAPES) {
            Map<String, Object> etape = enBase.stream().filter(e -> nom.equals(e.get("nom"))).findFirst()
                    .orElseGet(() -> new LinkedHashMap<>(Map.of("nom", nom, "done", false, "doneAt", "", "doneBy", "")));
            etapes.add(new LinkedHashMap<>(etape));
        }
        return etapes;
    }

    private List<Map<String, Object>> lireListe(String json) {
        if (json == null || json.isBlank()) return new ArrayList<>();
        try {
            return new ArrayList<>(objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {}));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private String ecrire(Object valeur) {
        try {
            return objectMapper.writeValueAsString(valeur);
        } catch (Exception e) {
            throw new IllegalStateException("Sérialisation JSON impossible", e);
        }
    }

    /** « Prénom Nom » de l'utilisateur connecté (à défaut son identifiant). */
    private String utilisateurCourant() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) return "";
        return utilisateurRepository.findByUsername(auth.getName())
                .map(u -> ((u.getPrenom() != null ? u.getPrenom() : "") + " " + (u.getNom() != null ? u.getNom() : "")).trim())
                .filter(s -> !s.isEmpty())
                .orElse(auth.getName());
    }
}
