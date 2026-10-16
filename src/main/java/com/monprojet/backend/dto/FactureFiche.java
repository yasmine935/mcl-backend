package com.monprojet.backend.dto;

/**
 * Facturation d'une fiche d'intervention, telle qu'affichée dans le détail d'un
 * projet : uniquement les champs utiles (pas de photos/signatures en base64).
 */
public record FactureFiche(
        Long id,
        String dateFacturation,
        String numeroFacture,
        String montantFacture,
        String statut) {
}
