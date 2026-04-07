package com.monprojet.backend.controller;

import com.monprojet.backend.model.Tache;
import com.monprojet.backend.repository.TacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/taches")
public class TacheController {

    @Autowired
    private TacheRepository tacheRepository;

    @GetMapping
    public List<Tache> getAll() {
        return tacheRepository.findAll();
    }

    @GetMapping("/utilisateur/{id}")
    public List<Tache> getByUtilisateur(@PathVariable Long id) {
        return tacheRepository.findByUtilisateurId(id);
    }

    @GetMapping("/statut/{statut}")
    public List<Tache> getByStatut(@PathVariable String statut) {
        return tacheRepository.findByStatut(statut);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tache> getById(@PathVariable Long id) {
        return tacheRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tache> create(@RequestBody Tache tache) {
        tache.setStatut("A_FAIRE");
        tache.setDateCreation(LocalDateTime.now());
        return ResponseEntity.ok(tacheRepository.save(tache));
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<Tache> updateStatut(
            @PathVariable Long id, @RequestParam String statut) {
        return tacheRepository.findById(id).map(tache -> {
            tache.setStatut(statut);
            return ResponseEntity.ok(tacheRepository.save(tache));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tache> update(@PathVariable Long id, @RequestBody Tache tache) {
        return tacheRepository.findById(id).map(existing -> {
            existing.setTitre(tache.getTitre());
            existing.setDescription(tache.getDescription());
            existing.setPriorite(tache.getPriorite());
            existing.setStatut(tache.getStatut());
            existing.setDateEcheance(tache.getDateEcheance());
            existing.setUtilisateur(tache.getUtilisateur());
            return ResponseEntity.ok(tacheRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        tacheRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}