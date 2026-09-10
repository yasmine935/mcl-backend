package com.monprojet.backend.security;

import com.monprojet.backend.config.SecurityBeansConfig;
import com.monprojet.backend.config.SecurityConfig;
import com.monprojet.backend.controller.AuthController;
import com.monprojet.backend.controller.PasswordResetController;
import com.monprojet.backend.controller.TicketController;
import com.monprojet.backend.controller.UtilisateurController;
import com.monprojet.backend.model.Ticket;
import com.monprojet.backend.model.Utilisateur;
import com.monprojet.backend.repository.ResetPasswordRequestRepository;
import com.monprojet.backend.repository.TicketRepository;
import com.monprojet.backend.repository.UtilisateurRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Vérifie les règles de sécurité HTTP (authentification JWT + autorisations par rôle)
 * sans base de données : les repositories sont mockés.
 */
@WebMvcTest(controllers = {
        AuthController.class,
        PasswordResetController.class,
        UtilisateurController.class,
        TicketController.class,
        com.monprojet.backend.controller.CongeController.class
})
@Import({SecurityConfig.class, SecurityBeansConfig.class, JwtService.class, JwtAuthFilter.class,
        com.monprojet.backend.exception.GlobalExceptionHandler.class,
        com.monprojet.backend.service.AuthService.class,
        com.monprojet.backend.service.PasswordResetService.class,
        com.monprojet.backend.service.UtilisateurService.class,
        com.monprojet.backend.service.TicketService.class})
@TestPropertySource(properties = {
        "app.jwt.secret=cle-de-test-suffisamment-longue-0123456789",
        "app.jwt.expiration-hours=1"
})
class SecuriteEndpointsTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;

    @MockBean private UtilisateurRepository utilisateurRepository;
    @MockBean private ResetPasswordRequestRepository resetRepo;
    @MockBean private TicketRepository ticketRepository;
    @MockBean private com.monprojet.backend.repository.CongeRepository congeRepository;

    private String jeton(String role) {
        Utilisateur u = new Utilisateur();
        u.setId(1L);
        u.setUsername("test-" + role.toLowerCase());
        u.setRole(role);
        return "Bearer " + jwtService.genererToken(u);
    }

    // --- Authentification obligatoire ---

    @Test
    void sansJetonLesEndpointsProtegesRendent401() throws Exception {
        mockMvc.perform(get("/api/utilisateurs")).andExpect(status().isUnauthorized());
        mockMvc.perform(put("/api/tickets/1/statut").param("statut", "EN_COURS"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/api/tickets/1")).andExpect(status().isUnauthorized());
        mockMvc.perform(put("/api/auth/change-password").contentType("application/json")
                        .content("{\"currentPassword\":\"a\",\"newPassword\":\"nouveau\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void avecJetonValideLaLectureEstAutorisee() throws Exception {
        when(utilisateurRepository.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/utilisateurs").header("Authorization", jeton("TECHNICIEN")))
                .andExpect(status().isOk());
    }

    // --- Écrans publics ---

    @Test
    void leDepotDeTicketResteAccessibleSansJeton() throws Exception {
        when(ticketRepository.count()).thenReturn(0L);
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));
        // Création publique conservée
        mockMvc.perform(post("/api/tickets").contentType("application/json")
                        .content("{\"nom\":\"Dupont\",\"descriptionPanne\":\"Écran HS\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void laListeDesTicketsNEstPlusPublique() throws Exception {
        // La liste expose des données personnelles → 401 sans jeton
        mockMvc.perform(get("/api/tickets")).andExpect(status().isUnauthorized());
    }

    @Test
    void unTechnicienNePeutPasSupprimerLesCongesDAutrui() throws Exception {
        // Cas emblématique du ré-audit : DELETE /api/conges réservé au propriétaire ou à la gestion.
        // Le congé appartient à "essan", supprimé par un technicien "test-technicien" → 403.
        com.monprojet.backend.model.Utilisateur proprio = new com.monprojet.backend.model.Utilisateur();
        proprio.setUsername("essan");
        com.monprojet.backend.model.Conge conge = new com.monprojet.backend.model.Conge();
        conge.setUtilisateur(proprio);
        when(congeRepository.findById(1L)).thenReturn(Optional.of(conge));

        mockMvc.perform(delete("/api/conges/1").header("Authorization", jeton("TECHNICIEN")))
                .andExpect(status().isForbidden());
    }

    @Test
    void laValidationRejetteLesEntreesIncompletes() throws Exception {
        // Ticket public sans champs essentiels → 400 (et pas de stack trace)
        mockMvc.perform(post("/api/tickets").contentType("application/json").content("{}"))
                .andExpect(status().isBadRequest());

        // Login sans mot de passe → 400 de validation
        mockMvc.perform(post("/api/auth/login").contentType("application/json")
                        .content("{\"username\":\"jean\"}"))
                .andExpect(status().isBadRequest());

        // Nouveau mot de passe trop court (< 8) → 400
        mockMvc.perform(put("/api/auth/change-password").header("Authorization", jeton("TECHNICIEN"))
                        .contentType("application/json")
                        .content("{\"currentPassword\":\"a\",\"newPassword\":\"court\"}"))
                .andExpect(status().isBadRequest());
    }

    // --- Autorisations par rôle : gestion des employés ---

    @Test
    void unTechnicienNePeutPasCreerNiModifierUnEmploye() throws Exception {
        mockMvc.perform(post("/api/utilisateurs").header("Authorization", jeton("TECHNICIEN"))
                        .contentType("application/json").content("{\"role\":\"FERID\"}"))
                .andExpect(status().isForbidden());
        mockMvc.perform(put("/api/utilisateurs/1").header("Authorization", jeton("TECHNICIEN"))
                        .contentType("application/json").content("{\"role\":\"FERID\"}"))
                .andExpect(status().isForbidden());
        mockMvc.perform(put("/api/utilisateurs/1/desactiver").param("desactivePar", "x")
                        .header("Authorization", jeton("TECHNICIEN")))
                .andExpect(status().isForbidden());
    }

    @Test
    void lesRHPeuventCreerUnEmployeEtSonMotDePasseEstHache() throws Exception {
        when(utilisateurRepository.save(any(Utilisateur.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/api/utilisateurs").header("Authorization", jeton("RH"))
                        .contentType("application/json")
                        .content("{\"username\":\"nouveau\",\"password\":\"secret123\",\"role\":\"TECHNICIEN\"}"))
                .andExpect(status().isOk());

        ArgumentCaptor<Utilisateur> capture = ArgumentCaptor.forClass(Utilisateur.class);
        verify(utilisateurRepository).save(capture.capture());
        String enregistre = capture.getValue().getPassword();
        assertNotEquals("secret123", enregistre);
        assertTrue(enregistre.startsWith("$2"));
        assertTrue(passwordEncoder.matches("secret123", enregistre));
    }

    @Test
    void lesDemandesDeResetSontReserveesALAdmin() throws Exception {
        mockMvc.perform(get("/api/auth/reset-requests").header("Authorization", jeton("TECHNICIEN")))
                .andExpect(status().isForbidden());

        when(resetRepo.findByStatut("EN_ATTENTE")).thenReturn(List.of());
        mockMvc.perform(get("/api/auth/reset-requests").header("Authorization", jeton("ADMINISTRATEUR")))
                .andExpect(status().isOk());
    }

    // --- Login ---

    private Utilisateur compte(String username, String motDePasseClair, boolean actif) {
        Utilisateur u = new Utilisateur();
        u.setId(5L);
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(motDePasseClair));
        u.setRole("TECHNICIEN");
        u.setActif(actif);
        return u;
    }

    @Test
    void leLoginRendUnJetonEtNExposeJamaisLeMotDePasse() throws Exception {
        when(utilisateurRepository.findByUsername("jean"))
                .thenReturn(Optional.of(compte("jean", "secret123", true)));

        mockMvc.perform(post("/api/auth/login").contentType("application/json")
                        .content("{\"username\":\"jean\",\"password\":\"secret123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.user.username").value("jean"))
                .andExpect(jsonPath("$.user.password").doesNotExist());
    }

    @Test
    void leLoginRefuseMauvaisMotDePasseEtCompteDesactive() throws Exception {
        when(utilisateurRepository.findByUsername("jean"))
                .thenReturn(Optional.of(compte("jean", "secret123", true)));
        mockMvc.perform(post("/api/auth/login").contentType("application/json")
                        .content("{\"username\":\"jean\",\"password\":\"mauvais\"}"))
                .andExpect(status().isUnauthorized());

        when(utilisateurRepository.findByUsername("parti"))
                .thenReturn(Optional.of(compte("parti", "secret123", false)));
        mockMvc.perform(post("/api/auth/login").contentType("application/json")
                        .content("{\"username\":\"parti\",\"password\":\"secret123\"}"))
                .andExpect(status().isUnauthorized());
    }

    // --- Changement de mot de passe ---

    @Test
    void changePasswordCibleLePorteurDuJetonEtExigeLeMotDePasseActuel() throws Exception {
        Utilisateur moi = compte("test-technicien", "actuel123", true);
        when(utilisateurRepository.findByUsername("test-technicien")).thenReturn(Optional.of(moi));
        when(utilisateurRepository.save(any(Utilisateur.class))).thenAnswer(inv -> inv.getArgument(0));

        // Mauvais mot de passe actuel → refusé
        mockMvc.perform(put("/api/auth/change-password").header("Authorization", jeton("TECHNICIEN"))
                        .contentType("application/json")
                        .content("{\"currentPassword\":\"mauvais\",\"newPassword\":\"nouveau123\"}"))
                .andExpect(status().isUnauthorized());

        // Bon mot de passe actuel → accepté, nouveau mot de passe haché
        mockMvc.perform(put("/api/auth/change-password").header("Authorization", jeton("TECHNICIEN"))
                        .contentType("application/json")
                        .content("{\"currentPassword\":\"actuel123\",\"newPassword\":\"nouveau123\"}"))
                .andExpect(status().isOk());

        assertTrue(passwordEncoder.matches("nouveau123", moi.getPassword()));
        assertFalse(moi.isPremierConnexion());
    }
}
