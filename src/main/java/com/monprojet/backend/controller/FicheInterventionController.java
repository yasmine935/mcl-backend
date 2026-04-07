package com.monprojet.backend.controller;

import com.monprojet.backend.model.FicheIntervention;
import com.monprojet.backend.repository.FicheInterventionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/fiches-intervention")
public class FicheInterventionController {

    @Autowired
    private FicheInterventionRepository ficheRepository;

    // GET toutes les fiches
    @GetMapping
    public List<FicheIntervention> getAll() {
        return ficheRepository.findAll();
    }

    // GET fiches d'un technicien
    @GetMapping("/technicien/{id}")
    public List<FicheIntervention> getByTechnicien(@PathVariable Long id) {
        return ficheRepository.findByTechnicienId(id);
    }

    // GET fiches d'un manager
    @GetMapping("/manager/{id}")
    public List<FicheIntervention> getByManager(@PathVariable Long id) {
        return ficheRepository.findByManagerId(id);
    }

    // GET par statut
    @GetMapping("/statut/{statut}")
    public List<FicheIntervention> getByStatut(@PathVariable String statut) {
        return ficheRepository.findByStatut(statut);
    }

    // GET une fiche
    @GetMapping("/{id}")
    public ResponseEntity<FicheIntervention> getById(@PathVariable Long id) {
        return ficheRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST créer une fiche
    @PostMapping
    public ResponseEntity<FicheIntervention> create(@RequestBody FicheIntervention fiche) {
        fiche.setStatut("EN_COURS");
        fiche.setDateCreation(LocalDateTime.now());
        return ResponseEntity.ok(ficheRepository.save(fiche));
    }

    // PUT modifier une fiche
    @PutMapping("/{id}")
    public ResponseEntity<FicheIntervention> update(
            @PathVariable Long id,
            @RequestBody FicheIntervention fiche) {
        return ficheRepository.findById(id).map(existing -> {
            existing.setNumProjet(fiche.getNumProjet());
            existing.setClient(fiche.getClient());
            existing.setAdresse(fiche.getAdresse());
            existing.setContact(fiche.getContact());
            existing.setDateIntervention(fiche.getDateIntervention());
            existing.setHeureDebut(fiche.getHeureDebut());
            existing.setHeureFin(fiche.getHeureFin());
            existing.setDescription(fiche.getDescription());
            existing.setStatut(fiche.getStatut());
            existing.setTechnicien(fiche.getTechnicien());
            return ResponseEntity.ok(ficheRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // PUT changer statut
    @PutMapping("/{id}/statut")
    public ResponseEntity<FicheIntervention> updateStatut(
            @PathVariable Long id,
            @RequestParam String statut) {
        return ficheRepository.findById(id).map(fiche -> {
            fiche.setStatut(statut);
            return ResponseEntity.ok(ficheRepository.save(fiche));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        ficheRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}