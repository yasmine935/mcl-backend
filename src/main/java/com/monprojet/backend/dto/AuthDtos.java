package com.monprojet.backend.dto;

import com.monprojet.backend.model.Utilisateur;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** DTOs du flux d'authentification — remplacent les entités JPA et les Map en entrée/sortie. */
public final class AuthDtos {

    private AuthDtos() {}

    public record LoginRequest(
            @NotBlank(message = "username obligatoire") String username,
            @NotBlank(message = "password obligatoire") String password) {}

    /** Le mot de passe de l'entité est en WRITE_ONLY : il n'apparaît jamais dans la réponse. */
    public record LoginResponse(String token, Utilisateur user) {}

    public record ForgotPasswordRequest(
            @NotBlank(message = "username obligatoire") String username) {}

    public record ChangePasswordRequest(
            @NotBlank(message = "mot de passe actuel obligatoire") String currentPassword,
            @NotBlank @Size(min = 8, message = "le nouveau mot de passe doit contenir au moins 8 caractères")
            String newPassword) {}

    public record AdminResetRequest(
            @NotBlank @Size(min = 8, message = "le mot de passe doit contenir au moins 8 caractères")
            String newPassword) {}
}
