package com.monprojet.backend.controller;

import com.monprojet.backend.model.Utilisateur;
import com.monprojet.backend.repository.UtilisateurRepository;
import com.monprojet.backend.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UtilisateurRepository utilisateurRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Utilisateur loginRequest) {
        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.status(401).body("Username ou mot de passe incorrect");
        }

        Optional<Utilisateur> opt = utilisateurRepository.findByUsername(loginRequest.getUsername());
        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body("Username ou mot de passe incorrect");
        }

        Utilisateur user = opt.get();
        if (Boolean.FALSE.equals(user.getActif())) {
            return ResponseEntity.status(401).body("Compte désactivé");
        }
        if (user.getPassword() == null
                || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Username ou mot de passe incorrect");
        }

        return ResponseEntity.ok(Map.of(
                "token", jwtService.genererToken(user),
                "user", user
        ));
    }
}
