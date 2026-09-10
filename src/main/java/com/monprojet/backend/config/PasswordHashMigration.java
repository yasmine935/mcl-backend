package com.monprojet.backend.config;

import com.monprojet.backend.model.Utilisateur;
import com.monprojet.backend.repository.UtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Migration ponctuelle : hache en BCrypt les mots de passe encore stockés en clair.
 * Idempotent — un hash BCrypt existant (préfixe $2a$/$2b$/$2y$) n'est jamais re-haché.
 */
@Profile("!schemagen")
@Component
public class PasswordHashMigration implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(PasswordHashMigration.class);

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordHashMigration(UtilisateurRepository utilisateurRepository,
                                 PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        int migres = 0;
        for (Utilisateur u : utilisateurs) {
            String p = u.getPassword();
            if (p != null && !p.isBlank() && !estHashBcrypt(p)) {
                u.setPassword(passwordEncoder.encode(p));
                utilisateurRepository.save(u);
                migres++;
            }
        }
        if (migres > 0) {
            log.info("Migration mots de passe : {} compte(s) migré(s) vers BCrypt", migres);
        }
    }

    private boolean estHashBcrypt(String p) {
        return p.startsWith("$2a$") || p.startsWith("$2b$") || p.startsWith("$2y$");
    }
}
