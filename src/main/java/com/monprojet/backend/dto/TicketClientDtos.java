package com.monprojet.backend.dto;

import jakarta.validation.constraints.NotBlank;

/** DTOs du service de ticketing client. */
public final class TicketClientDtos {

    private TicketClientDtos() {}

    public record CreationRequest(
            @NotBlank(message = "titre obligatoire") String titre,
            String categorie,
            // Demande d'intervention
            @NotBlank(message = "adresse site obligatoire") String adresseSite,
            @NotBlank(message = "nom du demandeur obligatoire") String nomDemandeur,
            @NotBlank(message = "prénom du demandeur obligatoire") String prenomDemandeur,
            @NotBlank(message = "téléphone du demandeur obligatoire") String telephoneDemandeur,
            @NotBlank(message = "email du demandeur obligatoire") String emailDemandeur,
            // Lieu d'intervention
            @NotBlank(message = "site obligatoire") String lieuSite,
            String nomSalle,
            String etage,
            String informationsAdditionnelles,
            // Nature de la panne
            @NotBlank(message = "type de matériel obligatoire") String typeMateriel,
            @NotBlank(message = "marque obligatoire") String marque,
            @NotBlank(message = "référence obligatoire") String reference,
            String numeroSerie,
            Boolean sousGarantie,
            String criticite,
            @NotBlank(message = "description de la panne obligatoire") String descriptionPanne) {}

    /** Décision d'un valideur (validation, rejet) avec commentaire optionnel. */
    public record DecisionRequest(String commentaire) {}

    /** Ajout d'un commentaire/échange sur le ticket. */
    public record CommentaireRequest(
            @NotBlank(message = "commentaire vide") String contenu) {}
}
