package com.monprojet.backend.controller;

import com.monprojet.backend.model.Voiture;
import com.monprojet.backend.repository.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/voitures")
public class VoitureController {

    @Autowired
    private VoitureRepository voitureRepository;

    // GET ALL
    @GetMapping
    public List<Voiture> getAll() {
        return voitureRepository.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Voiture> getById(@PathVariable Long id) {
        return voitureRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET BY STATUT
    @GetMapping("/statut/{statut}")
    public List<Voiture> getByStatut(@PathVariable String statut) {
        return voitureRepository.findByStatut(statut);
    }

    // POST
    @PreAuthorize("hasAnyAuthority('RH','ADMINISTRATEUR')")
    @PostMapping
    public Voiture create(@RequestBody Voiture voiture) {
        return voitureRepository.save(voiture);
    }

    // PUT
    @PreAuthorize("hasAnyAuthority('RH','ADMINISTRATEUR')")
    @PutMapping("/{id}")
    public ResponseEntity<Voiture> update(@PathVariable Long id, @RequestBody Voiture voiture) {
        return voitureRepository.findById(id).map(existing -> {
            existing.setImmatriculation(voiture.getImmatriculation());
            existing.setMarque(voiture.getMarque());
            existing.setModele(voiture.getModele());
            existing.setAnnee(voiture.getAnnee());
            existing.setKilometrage(voiture.getKilometrage());
            existing.setStatut(voiture.getStatut());
            existing.setConducteur(voiture.getConducteur());
            existing.setProchainControle(voiture.getProchainControle());
            existing.setAssurance(voiture.getAssurance());
            existing.setDateExpirationAssurance(voiture.getDateExpirationAssurance());
            existing.setCarburant(voiture.getCarburant());
            return ResponseEntity.ok(voitureRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // PUT STATUT
    @PreAuthorize("hasAnyAuthority('RH','ADMINISTRATEUR')")
    @PutMapping("/{id}/statut")
    public ResponseEntity<Voiture> updateStatut(@PathVariable Long id, @RequestParam String statut) {
        return voitureRepository.findById(id).map(v -> {
            v.setStatut(statut);
            return ResponseEntity.ok(voitureRepository.save(v));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @PreAuthorize("hasAnyAuthority('RH','ADMINISTRATEUR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (voitureRepository.existsById(id)) {
            voitureRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}