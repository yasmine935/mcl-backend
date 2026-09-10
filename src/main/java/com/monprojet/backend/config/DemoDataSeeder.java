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
 * Crée des comptes de test — UNIQUEMENT sous le profil "demo" (base H2 jetable).
 * Ne s'active jamais en prod (profil "local"). Mot de passe commun : demo1234.
 */
@Profile("demo")
@Component
public class DemoDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataSeeder.class);
    private static final String MOT_DE_PASSE = "demo1234";

    private final UtilisateurRepository repo;
    private final PasswordEncoder passwordEncoder;

    public DemoDataSeeder(UtilisateurRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (repo.count() > 0) return;

        String[][] comptes = {
            {"ferid", "Ferid", "ADMINISTRATEUR"},
            {"essan", "Essan", "DIRECTION"},
            {"karine", "Karine", "RH"},
            {"naccera", "Naccera", "COMPTABILITE"},
            {"aby", "Aby", "SUPPLY_CHAIN"},
            {"haideh", "Haideh", "ADMINISTRATIF"},
            {"aurelien", "Aurelien", "MANAGER"},
            {"kia", "Kia", "TECHNICIEN_SUP"},
            {"tech", "Technicien", "TECHNICIEN"},
        };

        for (String[] c : comptes) {
            Utilisateur u = new Utilisateur();
            u.setUsername(c[0]);
            u.setNom(c[1]);
            u.setPrenom("Demo");
            u.setEmail(c[0] + "@demo.local");
            u.setRole(c[2]);
            u.setActif(true);
            u.setPremierConnexion(false);
            u.setPassword(passwordEncoder.encode(MOT_DE_PASSE));
            repo.save(u);
        }
        log.info("DEMO : {} comptes de test créés (mot de passe : {})", comptes.length, MOT_DE_PASSE);
        log.info("DEMO : connectez-vous avec ferid / demo1234 (ADMINISTRATEUR), tech / demo1234 (TECHNICIEN), etc.");
    }
}
