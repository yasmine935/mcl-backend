package com.monprojet.backend.service;

import com.monprojet.backend.dto.AuthDtos.LoginRequest;
import com.monprojet.backend.dto.AuthDtos.LoginResponse;
import com.monprojet.backend.model.Utilisateur;
import com.monprojet.backend.repository.UtilisateurRepository;
import com.monprojet.backend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UtilisateurRepository utilisateurRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /** Renvoie le jeton et l'utilisateur si les identifiants sont valides, sinon empty. */
    public Optional<LoginResponse> authentifier(LoginRequest requete) {
        Optional<Utilisateur> opt = utilisateurRepository.findByUsername(requete.username());
        if (opt.isEmpty()) return Optional.empty();

        Utilisateur user = opt.get();
        if (Boolean.FALSE.equals(user.getActif())) return Optional.empty();
        if (user.getPassword() == null
                || !passwordEncoder.matches(requete.password(), user.getPassword())) {
            return Optional.empty();
        }
        return Optional.of(new LoginResponse(jwtService.genererToken(user), user));
    }
}
