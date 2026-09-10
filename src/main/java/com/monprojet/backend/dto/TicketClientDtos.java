package com.monprojet.backend.dto;

import jakarta.validation.constraints.NotBlank;

/** DTOs du service de ticketing client. */
public final class TicketClientDtos {

    private TicketClientDtos() {}

    public record CreationRequest(
            @NotBlank(message = "titre obligatoire") String titre,
            @NotBlank(message = "description obligatoire") String description,
            String categorie,
            String priorite) {}

    /** Décision d'un valideur (validation, rejet) avec commentaire optionnel. */
    public record DecisionRequest(String commentaire) {}

    /** Ajout d'un commentaire/échange sur le ticket. */
    public record CommentaireRequest(
            @NotBlank(message = "commentaire vide") String contenu) {}
}
