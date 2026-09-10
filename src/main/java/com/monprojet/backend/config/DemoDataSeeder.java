package com.monprojet.backend.config;

import com.monprojet.backend.model.TicketClient;
import com.monprojet.backend.model.Utilisateur;
import com.monprojet.backend.repository.TicketClientRepository;
import com.monprojet.backend.repository.UtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Crée des comptes de test — UNIQUEMENT sous les profils "demo"/"seed" (bases jetables).
 * Ne s'active jamais en prod. Mot de passe commun : demo1234.
 */
@Profile({"demo", "seed"})
@Component
public class DemoDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataSeeder.class);
    private static final String MOT_DE_PASSE = "demo1234";

    private final UtilisateurRepository repo;
    private final TicketClientRepository ticketClientRepo;
    private final PasswordEncoder passwordEncoder;

    public DemoDataSeeder(UtilisateurRepository repo, TicketClientRepository ticketClientRepo,
                          PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.ticketClientRepo = ticketClientRepo;
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
            {"odile", "Odile", "MANAGER"},
            {"kia", "Kia", "TECHNICIEN_SUP"},
            {"tech", "Technicien", "TECHNICIEN"},
            // Comptes clients externes (dashboard client)
            {"clientdupont", "Dupont", "CLIENT"},
            {"clientmartin", "Martin", "CLIENT"},
        };

        Utilisateur dupont = null;
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
            u = repo.save(u);
            if ("clientdupont".equals(c[0])) dupont = u;
        }

        // Quelques tickets client de démonstration, adressés aux valideurs (à prendre).
        if (dupont != null) {
            creerTicket(dupont, "Imprimante en panne", "L'imprimante du 2e étage n'imprime plus.", "Matériel", "Haute");
            creerTicket(dupont, "Accès VPN", "Impossible de me connecter au VPN depuis lundi.", "Réseau", "Moyenne");
        }

        log.info("DEMO : {} comptes créés (mot de passe : {}) dont 2 comptes CLIENT (clientdupont, clientmartin)",
                comptes.length, MOT_DE_PASSE);
    }

    private void creerTicket(Utilisateur client, String titre, String description, String categorie, String priorite) {
        TicketClient t = new TicketClient();
        t.setNumero("TC-" + String.format("%04d", ticketClientRepo.count() + 1));
        t.setTitre(titre);
        t.setDescription(description);
        t.setCategorie(categorie);
        t.setPriorite(priorite);
        t.setStatut(TicketClient.EN_ATTENTE_VALIDATION);
        t.setClient(client);
        t.setDateCreation(LocalDateTime.now());
        t.setDateMaj(LocalDateTime.now());
        ticketClientRepo.save(t);
    }
}
