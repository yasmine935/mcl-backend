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
        return ficheRepository.findByTechnicienId(technicienId);
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
            fiche.setStatut(updated.getStatut());
            fiche.setHeureDebut(updated.getHeureDebut());
            fiche.setHeureFin(updated.getHeureFin());
            if (updated.getTechnicien() != null) {
                Utilisateur tech = new Utilisateur();
                tech.setId(updated.getTechnicien().getId());
                fiche.setTechnicien(tech);
            }
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

    // ✅ Technicien complète la fiche avec signatures
    @PutMapping("/{id}/completer")
    public ResponseEntity<FicheIntervention> completerFiche(@PathVariable Long id, @RequestBody Map<String, Object> data) {
        return ficheRepository.findById(id).map(fiche -> {
            fiche.setStatut("COMPLETEE");
            if (data.get("signatureTechnicien") != null) fiche.setSignatureTechnicien((String) data.get("signatureTechnicien"));
            if (data.get("signatureClient") != null) fiche.setSignatureClient((String) data.get("signatureClient"));
            if (data.get("nomClientSigne") != null) fiche.setNomClientSigne((String) data.get("nomClientSigne"));
            if (data.get("heureDebut") != null) fiche.setHeureDebut((String) data.get("heureDebut"));
            if (data.get("heureFin") != null) fiche.setHeureFin((String) data.get("heureFin"));
            if (data.get("intervenants") != null) fiche.setIntervenants((String) data.get("intervenants"));
            if (data.get("dateCompletion") != null) fiche.setDateCompletion((String) data.get("dateCompletion"));
            return ResponseEntity.ok(ficheRepository.save(fiche));
        }).orElse(ResponseEntity.notFound().build());
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (ficheRepository.existsById(id)) { ficheRepository.deleteById(id); return ResponseEntity.ok().build(); }
        return ResponseEntity.notFound().build();
    }
}