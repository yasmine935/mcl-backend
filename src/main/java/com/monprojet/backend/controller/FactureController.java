package com.monprojet.backend.controller;

import com.monprojet.backend.model.Facture;
import com.monprojet.backend.repository.FactureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/factures")
public class FactureController {

    @Autowired
    private FactureRepository factureRepository;

    @GetMapping
    public List<Facture> getAll() {
        return factureRepository.findAll();
    }

    @GetMapping("/utilisateur/{id}")
    public List<Facture> getByUtilisateur(@PathVariable Long id) {
        return factureRepository.findByUtilisateurId(id);
    }

    @GetMapping("/statut/{statut}")
    public List<Facture> getByStatut(@PathVariable String statut) {
        return factureRepository.findByStatut(statut);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facture> getById(@PathVariable Long id) {
        return factureRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Facture> create(@RequestBody Facture facture) {
        facture.setStatut("EN_ATTENTE");
        facture.setDateCreation(LocalDateTime.now());
        long count = factureRepository.count();
        facture.setNumero("FAC-" + String.format("%04d", count + 1));
        return ResponseEntity.ok(factureRepository.save(facture));
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<Facture> updateStatut(
            @PathVariable Long id, @RequestParam String statut) {
        return factureRepository.findById(id).map(facture -> {
            facture.setStatut(statut);
            return ResponseEntity.ok(factureRepository.save(facture));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Facture> update(@PathVariable Long id, @RequestBody Facture facture) {
        return factureRepository.findById(id).map(existing -> {
            existing.setClient(facture.getClient());
            existing.setMontantHT(facture.getMontantHT());
            existing.setTva(facture.getTva());
            existing.setStatut(facture.getStatut());
            existing.setDateFacture(facture.getDateFacture());
            return ResponseEntity.ok(factureRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        factureRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}