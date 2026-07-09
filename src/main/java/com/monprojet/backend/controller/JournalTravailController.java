package com.monprojet.backend.controller;

import com.monprojet.backend.model.JournalTravail;
import com.monprojet.backend.model.Utilisateur;
import com.monprojet.backend.repository.JournalTravailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/journal")
@CrossOrigin(origins = "*")
public class JournalTravailController {

    @Autowired
    private JournalTravailRepository journalRepository;

    @Value("${anthropic.api.key:}")
    private String anthropicApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String ANTHROPIC_URL = "https://api.anthropic.com/v1/messages";

    // ✅ Génération IA du rapport à partir des notes brutes
    @PostMapping("/generer")
    public ResponseEntity<Map<String, String>> genererRapport(@RequestBody Map<String, String> body) {
        String notes = body.get("notes");
        if (notes == null || notes.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("erreur", "Notes vides"));
        }
        // Mode démo : si aucune clé API n'est configurée, on génère un rapport simulé
        if (anthropicApiKey == null || anthropicApiKey.isBlank()) {
            return ResponseEntity.ok(Map.of("rapport", genererRapportSimule(notes)));
        }

        String prompt = """
                Tu es un assistant rédacteur pour une société d'installation et de maintenance audiovisuelle.
                Un technicien t'a laissé des notes informelles sur sa journée de travail.
                Rédige un rapport journalier professionnel et structuré en français, en suivant ce plan :
                1. Résumé de la journée
                2. Interventions réalisées (avec détails techniques)
                3. Matériels utilisés / installés
                4. Problèmes rencontrés et solutions apportées (si applicable)
                5. Points de suivi / prochaines étapes

                Le ton doit être professionnel, clair et précis. Utilise des paragraphes lisibles.

                Notes brutes du technicien :
                %s
                """.formatted(notes);

        Map<String, Object> requestBody = Map.of(
            "model", "claude-opus-4-8",
            "max_tokens", 1500,
            "messages", List.of(Map.of("role", "user", "content", prompt))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", anthropicApiKey);
        headers.set("anthropic-version", "2023-06-01");

        try {
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(ANTHROPIC_URL, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<?> content = (List<?>) response.getBody().get("content");
                if (content != null && !content.isEmpty()) {
                    Map<?, ?> firstBlock = (Map<?, ?>) content.get(0);
                    String texte = (String) firstBlock.get("text");
                    return ResponseEntity.ok(Map.of("rapport", texte));
                }
            }
            return ResponseEntity.status(500).body(Map.of("erreur", "Réponse inattendue de l'IA"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erreur", "Erreur lors de l'appel à l'IA : " + e.getMessage()));
        }
    }

    // ✅ Tous les journaux (vue managers : KIA, Ferid, Karine)
    @GetMapping
    public List<JournalTravail> getAll() {
        return journalRepository.findAllByOrderByDateJourneeDesc();
    }

    // ✅ Sauvegarder un journal de travail
    @PostMapping
    public ResponseEntity<JournalTravail> sauvegarder(@RequestBody Map<String, Object> body) {
        JournalTravail journal = new JournalTravail();
        if (body.get("userId") != null) {
            Utilisateur u = new Utilisateur();
            u.setId(Long.valueOf(body.get("userId").toString()));
            journal.setUtilisateur(u);
        }
        if (body.get("dateJournee") != null) {
            journal.setDateJournee(LocalDate.parse(body.get("dateJournee").toString()));
        }
        journal.setNotesOriginales(body.getOrDefault("notesOriginales", "").toString());
        journal.setRapportGenere(body.getOrDefault("rapportGenere", "").toString());
        if (body.get("signature") != null) journal.setSignature(body.get("signature").toString());
        return ResponseEntity.ok(journalRepository.save(journal));
    }

    // ✅ Lister les journaux d'un employé
    @GetMapping("/employe/{userId}")
    public List<JournalTravail> getByEmploye(@PathVariable Long userId) {
        return journalRepository.findByUtilisateurIdOrderByDateJourneeDesc(userId);
    }

    // ✅ Supprimer un journal
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        journalRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // Génération simulée pour la démo (sans clé API)
    private String genererRapportSimule(String notes) {
        String date = java.time.LocalDate.now()
            .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return String.format("""
            RAPPORT JOURNALIER — %s
            Généré automatiquement à partir des notes du technicien
            ─────────────────────────────────────────────────────

            1. RÉSUMÉ DE LA JOURNÉE
            ───────────────────────
            La journée s'est déroulée conformément au planning prévu. Le technicien a mené à bien l'ensemble des interventions assignées et a résolu les points techniques rencontrés dans les délais impartis.

            2. INTERVENTIONS RÉALISÉES
            ──────────────────────────
            Notes terrain du technicien :
            « %s »

            L'ensemble des tâches ont été exécutées selon les procédures internes MCL Solutions. Les installations ont été réalisées dans le respect des normes techniques en vigueur.

            3. MATÉRIELS UTILISÉS / INSTALLÉS
            ────────────────────────────────────
            Le matériel mobilisé au cours de cette journée a été sélectionné en adéquation avec les besoins identifiés lors du diagnostic préalable. Tout le matériel utilisé a été inventorié et remis en stock ou laissé sur site selon les instructions du responsable.

            4. PROBLÈMES RENCONTRÉS ET SOLUTIONS APPORTÉES
            ─────────────────────────────────────────────────
            Les difficultés rencontrées ont été traitées sur le moment par le technicien, qui a appliqué les procédures adaptées. Aucun incident majeur n'a nécessité d'escalade vers le responsable technique.

            5. POINTS DE SUIVI / PROCHAINES ÉTAPES
            ─────────────────────────────────────────
            Un compte-rendu complet sera transmis au responsable de projet. Les actions restantes seront planifiées lors du prochain point d'équipe. Le technicien reste disponible pour toute question complémentaire relative aux interventions de cette journée.

            ─────────────────────────────────────────────────────
            Rapport établi le %s — MCL Solutions
            """, date, notes.trim(), date);
    }
}
