package com.monprojet.backend.controller;

import com.monprojet.backend.dto.FactureFiche;
import com.monprojet.backend.model.FicheIntervention;
import com.monprojet.backend.model.Tache;
import com.monprojet.backend.model.Utilisateur;
import com.monprojet.backend.repository.FicheInterventionRepository;
import com.monprojet.backend.repository.TacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/fiches-intervention")
public class FicheInterventionController {

    @Autowired
    private FicheInterventionRepository ficheRepository;

    @Autowired
    private TacheRepository tacheRepository;

    /** Une fiche doit appartenir à un projet réel (plus de saisie libre du nom). */
    private static final String PROJET_OBLIGATOIRE = "Choisissez un projet existant pour cette fiche d'intervention.";

    /** Projet référencé par la fiche reçue, s'il existe. */
    private Optional<Tache> projetDe(FicheIntervention fiche) {
        return fiche.getProjetId() == null ? Optional.empty() : tacheRepository.findById(fiche.getProjetId());
    }

    @GetMapping
    public List<FicheIntervention> getAll() { return ficheRepository.findAll(); }

    /** Factures des fiches d'un projet (détail du projet : tableau + reste à facturer). */
    @GetMapping("/factures")
    public List<FactureFiche> getFacturesProjet(@RequestParam Long projetId) {
        return ficheRepository.findFacturesByProjetId(projetId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FicheIntervention> getById(@PathVariable Long id) {
        return ficheRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/technicien/{technicienId}")
    public List<FicheIntervention> getByTechnicien(@PathVariable Long technicienId) {
        try {
            return ficheRepository.findByTechnicienPrincipalOrMulti(technicienId);
        } catch (Exception e) {
            return ficheRepository.findByTechnicienId(technicienId);
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','TECHNICIEN_SUP','DIRECTION','ADMINISTRATEUR')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody FicheIntervention fiche) {
        Optional<Tache> projet = projetDe(fiche);
        if (projet.isEmpty()) return ResponseEntity.badRequest().body(Map.of("message", PROJET_OBLIGATOIRE));
        fiche.setNumProjet(projet.get().getTitre());   // libellé = titre du projet, pas le texte reçu
        return ResponseEntity.ok(ficheRepository.save(fiche));
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','TECHNICIEN_SUP','DIRECTION','ADMINISTRATEUR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody FicheIntervention updated) {
        Optional<Tache> projet = projetDe(updated);
        if (projet.isEmpty()) return ResponseEntity.badRequest().body(Map.of("message", PROJET_OBLIGATOIRE));
        return ficheRepository.findById(id).<ResponseEntity<?>>map(fiche -> {
            fiche.setProjetId(projet.get().getId());
            fiche.setNumProjet(projet.get().getTitre());
            fiche.setClient(updated.getClient());
            fiche.setAdresse(updated.getAdresse());
            fiche.setContact(updated.getContact());
            fiche.setDateIntervention(updated.getDateIntervention());
            fiche.setDateFin(updated.getDateFin());
            // Absent de la requête = non géré par l'appelant : on garde la valeur en base
            if (updated.getMaterielsHorsStandard() != null) fiche.setMaterielsHorsStandard(updated.getMaterielsHorsStandard());
            fiche.setDescription(updated.getDescription());
            fiche.setTaches(updated.getTaches());
            fiche.setDocumentsImportes(updated.getDocumentsImportes());
            fiche.setStatut(updated.getStatut());
            fiche.setHeureDebut(updated.getHeureDebut());
            fiche.setHeureFin(updated.getHeureFin());
            if (updated.getTechnicien() != null) {
                Utilisateur tech = new Utilisateur();
                tech.setId(updated.getTechnicien().getId());
                fiche.setTechnicien(tech);
            }
            fiche.setTechnicienIds(updated.getTechnicienIds());
            fiche.setTechnicienNoms(updated.getTechnicienNoms());
            fiche.setNomContactSite(updated.getNomContactSite());
            fiche.setTelContactSite(updated.getTelContactSite());
            // Rubrique Facturation (les factures importées sont dans documentsImportes, type "Facture")
            fiche.setNumeroFacture(updated.getNumeroFacture());
            fiche.setDateFacturation(updated.getDateFacturation());
            fiche.setMontantFacture(updated.getMontantFacture());
            fiche.setRefSage(updated.getRefSage());
            return ResponseEntity.ok(ficheRepository.save(fiche));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/taches")
    public ResponseEntity<FicheIntervention> saveTaches(@PathVariable Long id, @RequestBody Map<String, Object> data) {
        return ficheRepository.findById(id).map(fiche -> {
            if (data.get("taches") != null) fiche.setTaches((String) data.get("taches"));
            return ResponseEntity.ok(ficheRepository.save(fiche));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','TECHNICIEN_SUP','DIRECTION','ADMINISTRATEUR')")
    @PutMapping("/{id}/statut")
    public ResponseEntity<FicheIntervention> updateStatut(@PathVariable Long id, @RequestParam String statut) {
        return ficheRepository.findById(id).map(fiche -> {
            fiche.setStatut(statut);
            return ResponseEntity.ok(ficheRepository.save(fiche));
        }).orElse(ResponseEntity.notFound().build());
    }

    private String str(Map<String, Object> data, String key) {
        Object val = data.get(key);
        return val != null ? (String) val : null;
    }

    // ✅ Technicien complète la fiche avec signatures
    @PutMapping("/{id}/completer")
    public ResponseEntity<FicheIntervention> completerFiche(@PathVariable Long id, @RequestBody Map<String, Object> data) {
        return ficheRepository.findById(id).map(fiche -> {
            fiche.setStatut("COMPLETEE");
            appliquerDonneesCompletion(fiche, data);
            return ResponseEntity.ok(ficheRepository.save(fiche));
        }).orElse(ResponseEntity.notFound().build());
    }

    private void appliquerDonneesCompletion(FicheIntervention fiche, Map<String, Object> data) {
        if (str(data, "signatureTechnicien") != null) fiche.setSignatureTechnicien(str(data, "signatureTechnicien"));
        if (str(data, "signatureClient") != null) fiche.setSignatureClient(str(data, "signatureClient"));
        if (str(data, "nomClientSigne") != null) fiche.setNomClientSigne(str(data, "nomClientSigne"));
        if (str(data, "heureDebut") != null) fiche.setHeureDebut(str(data, "heureDebut"));
        if (str(data, "heureFin") != null) fiche.setHeureFin(str(data, "heureFin"));
        if (str(data, "intervenants") != null) fiche.setIntervenants(str(data, "intervenants"));
        if (str(data, "dateCompletion") != null) fiche.setDateCompletion(str(data, "dateCompletion"));
        if (str(data, "materielsHorsStandard") != null) fiche.setMaterielsHorsStandard(str(data, "materielsHorsStandard"));
        if (str(data, "photos") != null) fiche.setPhotos(str(data, "photos"));
    }
    @PreAuthorize("hasAnyAuthority('MANAGER','TECHNICIEN_SUP','DIRECTION','ADMINISTRATEUR')")
    @PutMapping("/{id}/valider")
    public ResponseEntity<FicheIntervention> validerFiche(@PathVariable Long id, @RequestBody Map<String, Object> data) {
        return ficheRepository.findById(id).map(fiche -> {
            fiche.setStatut("VALIDEE");
            if (data.get("approuvePar") != null) fiche.setApprouvePar((String) data.get("approuvePar"));
            if (data.get("dateApprobation") != null) fiche.setDateApprobation((String) data.get("dateApprobation"));
            if (data.get("dateFacturation") != null) fiche.setDateFacturation((String) data.get("dateFacturation"));
            if (data.get("numeroFacture") != null) fiche.setNumeroFacture((String) data.get("numeroFacture"));
            return ResponseEntity.ok(ficheRepository.save(fiche));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ KIA (Technicien Supérieur) confirme le travail avant la validation finale
    @PreAuthorize("hasAnyAuthority('TECHNICIEN_SUP','ADMINISTRATEUR')")
    @PutMapping("/{id}/confirmer-kia")
    public ResponseEntity<FicheIntervention> confirmerKia(@PathVariable Long id, @RequestBody Map<String, Object> data) {
        return ficheRepository.findById(id).map(fiche -> {
            fiche.setStatut("VALIDEE_KIA");
            if (data.get("confirmePar") != null) fiche.setConfirmeParKia((String) data.get("confirmePar"));
            fiche.setDateConfirmationKia(java.time.LocalDateTime.now().toString());
            return ResponseEntity.ok(ficheRepository.save(fiche));
        }).orElse(ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasAnyAuthority('MANAGER','TECHNICIEN_SUP','DIRECTION','ADMINISTRATEUR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (ficheRepository.existsById(id)) { ficheRepository.deleteById(id); return ResponseEntity.ok().build(); }
        return ResponseEntity.notFound().build();
    }
}