package com.monprojet.backend.controller;

import com.monprojet.backend.model.FicheIntervention;
import com.monprojet.backend.model.Utilisateur;
import com.monprojet.backend.repository.FicheInterventionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fiches-intervention")
@CrossOrigin(origins = "http://localhost:4200")
public class FicheInterventionController {

    @Autowired
    private FicheInterventionRepository ficheRepository;

    @GetMapping
    public List<FicheIntervention> getAll() { return ficheRepository.findAll(); }

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

    @PostMapping
    public FicheIntervention create(@RequestBody FicheIntervention fiche) {
        return ficheRepository.save(fiche);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FicheIntervention> update(@PathVariable Long id, @RequestBody FicheIntervention updated) {
        return ficheRepository.findById(id).map(fiche -> {
            fiche.setNumProjet(updated.getNumProjet());
            fiche.setClient(updated.getClient());
            fiche.setAdresse(updated.getAdresse());
            fiche.setContact(updated.getContact());
            fiche.setDateIntervention(updated.getDateIntervention());
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
    @PutMapping("/{id}/valider")
    public ResponseEntity<FicheIntervention> validerFiche(@PathVariable Long id, @RequestBody Map<String, Object> data) {
        return ficheRepository.findById(id).map(fiche -> {
            fiche.setStatut("VALIDEE");
            if (data.get("approuvePar") != null) fiche.setApprouvePar((String) data.get("approuvePar"));
            if (data.get("dateApprobation") != null) fiche.setDateApprobation((String) data.get("dateApprobation"));
            return ResponseEntity.ok(ficheRepository.save(fiche));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ KIA (Technicien Supérieur) confirme le travail avant la validation finale
    @PutMapping("/{id}/confirmer-kia")
    public ResponseEntity<FicheIntervention> confirmerKia(@PathVariable Long id, @RequestBody Map<String, Object> data) {
        return ficheRepository.findById(id).map(fiche -> {
            fiche.setStatut("VALIDEE_KIA");
            if (data.get("confirmePar") != null) fiche.setConfirmeParKia((String) data.get("confirmePar"));
            fiche.setDateConfirmationKia(java.time.LocalDateTime.now().toString());
            return ResponseEntity.ok(ficheRepository.save(fiche));
        }).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (ficheRepository.existsById(id)) { ficheRepository.deleteById(id); return ResponseEntity.ok().build(); }
        return ResponseEntity.notFound().build();
    }
}