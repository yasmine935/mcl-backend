package com.monprojet.backend.config;

import com.monprojet.backend.model.Utilisateur;
import com.monprojet.backend.repository.UtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Migration ponctuelle : remplace les anciens rôles-prénoms par des rôles
 * fonctionnels. Idempotente — un rôle déjà fonctionnel n'est jamais retouché.
 */
@Component
public class RoleMigration implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(RoleMigration.class);

    private static final Map<String, String> ANCIEN_VERS_NOUVEAU = Map.ofEntries(
            Map.entry("FERID", "ADMINISTRATEUR"),
            Map.entry("ESSAN", "DIRECTION"),
            Map.entry("KARINE", "RH"),
            Map.entry("NACCERA", "COMPTABILITE"),
            Map.entry("ABY", "SUPPLY_CHAIN"),
            Map.entry("HAIDEH", "ADMINISTRATIF"),
            Map.entry("AYDEH", "ADMINISTRATIF"),   // ancienne faute de frappe encore possible en base
            Map.entry("AURELIEN", "MANAGER"),
            Map.entry("ODILE", "MANAGER"),
            Map.entry("KIA", "TECHNICIEN_SUP"),
            Map.entry("UN", "TECHNICIEN")
    );

    private final UtilisateurRepository utilisateurRepository;

    public RoleMigration(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public void run(String... args) {
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        int migres = 0;
        for (Utilisateur u : utilisateurs) {
            String nouveau = u.getRole() == null ? null : ANCIEN_VERS_NOUVEAU.get(u.getRole());
            if (nouveau != null) {
                log.info("Migration rôle : {} {} → {}", u.getUsername(), u.getRole(), nouveau);
                u.setRole(nouveau);
                utilisateurRepository.save(u);
                migres++;
            }
        }
        if (migres > 0) {
            log.info("Migration rôles : {} compte(s) migré(s) vers les rôles fonctionnels", migres);
        }
    }
}
