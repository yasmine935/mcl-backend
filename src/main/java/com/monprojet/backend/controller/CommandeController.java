package com.monprojet.backend.controller;

import com.monprojet.backend.model.Commande;
import com.monprojet.backend.repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    @Autowired
    private CommandeRepository commandeRepository;

    @GetMapping
    public List<Commande> getAll() {
        return commandeRepository.findAll();
    }

    @GetMapping("/utilisateur/{id}")
    public List<Commande> getByUtilisateur(@PathVariable Long id) {
        return commandeRepository.findByUtilisateurId(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Commande> getById(@PathVariable Long id) {
        return commandeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Commande> create(@RequestBody Commande commande) {
        commande.setStatut("EN_ATTENTE");
        commande.setDateCreation(LocalDateTime.now());
        long count = commandeRepository.count();
        commande.setNumero("CMD-" + String.format("%04d", count + 1));
        return ResponseEntity.ok(commandeRepository.save(commande));
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<Commande> updateStatut(
            @PathVariable Long id, @RequestParam String statut) {
        return commandeRepository.findById(id).map(cmd -> {
            cmd.setStatut(statut);
            return ResponseEntity.ok(commandeRepository.save(cmd));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Commande> update(@PathVariable Long id, @RequestBody Commande commande) {
        return commandeRepository.findById(id).map(existing -> {
            existing.setFournisseur(commande.getFournisseur());
            existing.setMontant(commande.getMontant());
            existing.setStatut(commande.getStatut());
            existing.setDescription(commande.getDescription());
            return ResponseEntity.ok(commandeRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        commandeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}