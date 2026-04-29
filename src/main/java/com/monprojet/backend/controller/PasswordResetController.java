package com.monprojet.backend.controller;

import com.monprojet.backend.model.ResetPasswordRequest;
import com.monprojet.backend.model.Utilisateur;
import com.monprojet.backend.repository.ResetPasswordRequestRepository;
import com.monprojet.backend.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class PasswordResetController {

    @Autowired
    private ResetPasswordRequestRepository resetRepo;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    // Demande mot de passe oublié
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        Optional<Utilisateur> opt = utilisateurRepository.findByUsername(username);
        if (opt.isEmpty()) return ResponseEntity.badRequest().body("Utilisateur introuvable");

        ResetPasswordRequest req = new ResetPasswordRequest();
        req.setUsername(username);
        req.setStatut("EN_ATTENTE");
        req.setDatedemande(LocalDateTime.now().toString());
        resetRepo.save(req);

        return ResponseEntity.ok("Demande envoyée");
    }

    // Liste des demandes (pour admin)
    @GetMapping("/reset-requests")
    public List<ResetPasswordRequest> getRequests() {
        return resetRepo.findByStatut("EN_ATTENTE");
    }

    // Admin réinitialise le mot de passe
    @PutMapping("/reset-requests/{id}/reset")
    public ResponseEntity<?> resetPassword(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String newPassword = body.get("newPassword");
        Optional<ResetPasswordRequest> optReq = resetRepo.findById(id);
        if (optReq.isEmpty()) return ResponseEntity.notFound().build();

        ResetPasswordRequest req = optReq.get();
        Optional<Utilisateur> optUser = utilisateurRepository.findByUsername(req.getUsername());
        if (optUser.isEmpty()) return ResponseEntity.notFound().build();

        Utilisateur u = optUser.get();
        u.setPassword(newPassword);
        utilisateurRepository.save(u);

        req.setStatut("TRAITE");
        resetRepo.save(req);

        return ResponseEntity.ok("Mot de passe réinitialisé");
    }

    // Première connexion — changer son propre mot de passe
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String newPassword = body.get("newPassword");

        Optional<Utilisateur> opt = utilisateurRepository.findByUsername(username);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Utilisateur u = opt.get();
        u.setPassword(newPassword);
        u.setPremierConnexion(false);
        utilisateurRepository.save(u);

        return ResponseEntity.ok("ok");
    }
}
