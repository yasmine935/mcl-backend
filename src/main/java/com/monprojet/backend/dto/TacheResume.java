package com.monprojet.backend.dto;

import com.monprojet.backend.model.Utilisateur;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Projet tel que renvoyé par les listes : mêmes champs JSON que Tache, SAUF
 * "fichiers" (pièces jointes en base64, jusqu'à plusieurs Mo par projet), qui
 * n'est jamais lu en base pour une liste. Le détail complet s'obtient via
 * GET /api/taches/{id}.
 */
public record TacheResume(
        Long id,
        String titre,
        String description,
        String priorite,
        String statut,
        Integer avancement,
        LocalDate dateEcheance,
        LocalDateTime dateCreation,
        String client,
        String clientFinal,
        String adresse,
        String chiffreAffaire,
        String numDevis,
        String caDevis,
        String assignes,
        String etapes,
        Utilisateur utilisateur) {
}
