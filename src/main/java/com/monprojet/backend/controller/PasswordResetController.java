package com.monprojet.backend.controller;

import com.monprojet.backend.dto.AuthDtos.AdminResetRequest;
import com.monprojet.backend.dto.AuthDtos.ChangePasswordRequest;
import com.monprojet.backend.dto.AuthDtos.ForgotPasswordRequest;
import com.monprojet.backend.model.ResetPasswordRequest;
import com.monprojet.backend.service.PasswordResetService;
import com.monprojet.backend.service.PasswordResetService.Resultat;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    private final PasswordResetService service;

    public PasswordResetController(PasswordResetService service) {
        this.service = service;
    }

    // Demande mot de passe oublié (public)
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest requete) {
        return service.creerDemande(requete.username()) == Resultat.OK
                ? ResponseEntity.ok("Demande envoyée")
                : ResponseEntity.badRequest().body("Utilisateur introuvable");
    }

    // Liste des demandes — réservé à l'admin (SecurityConfig)
    @GetMapping("/reset-requests")
    public List<ResetPasswordRequest> getRequests() {
        return service.demandesEnAttente();
    }

    // Reset admin : mot de passe temporaire, à changer à la connexion — réservé à l'admin
    @PutMapping("/reset-requests/{id}/reset")
    public ResponseEntity<?> resetPassword(@PathVariable Long id,
                                           @Valid @RequestBody AdminResetRequest requete) {
        return switch (service.reinitialiser(id, requete.newPassword())) {
            case OK -> ResponseEntity.ok("Mot de passe réinitialisé");
            case DEJA_TRAITEE -> ResponseEntity.badRequest().body("Demande déjà traitée");
            default -> ResponseEntity.notFound().build();
        };
    }

    // Changer son propre mot de passe — la cible est le porteur du jeton
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest requete,
                                            Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(401).build();
        }
        return switch (service.changerMotDePasse(principal.getName(),
                requete.currentPassword(), requete.newPassword())) {
            case OK -> ResponseEntity.ok("ok");
            case MAUVAIS_MOT_DE_PASSE -> ResponseEntity.status(401).body("Mot de passe actuel incorrect");
            default -> ResponseEntity.notFound().build();
        };
    }
}
