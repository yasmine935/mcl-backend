package com.monprojet.backend.controller;

import com.monprojet.backend.model.Conge;
import com.monprojet.backend.repository.CongeRepository;
import com.monprojet.backend.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/conges")
public class CongeController {

    @Autowired
    private CongeRepository congeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    // GET tous les congés (Admin)
    @GetMapping
    public List<Conge> getAll() {
        return congeRepository.findAll();
    }

    // GET congés d'un employé
    @GetMapping("/employe/{userId}")
    public List<Conge> getByEmploye(@PathVariable Long userId) {
        return congeRepository.findByUtilisateurId(userId);
    }

    // POST déposer une demande
    @PostMapping
    public ResponseEntity<Conge> create(@RequestBody Conge conge) {
        conge.setStatut("EN_ATTENTE");
        return ResponseEntity.ok(congeRepository.save(conge));
    }

    // PUT changer le statut (Admin)
    @PutMapping("/{id}/statut")
    public ResponseEntity<Conge> updateStatut(
            @PathVariable Long id,
            @RequestParam String statut) {
        return congeRepository.findById(id).map(conge -> {
            conge.setStatut(statut);
            return ResponseEntity.ok(congeRepository.save(conge));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE supprimer
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        congeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}