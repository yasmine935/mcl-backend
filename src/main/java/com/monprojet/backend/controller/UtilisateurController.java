package com.monprojet.backend.controller;

import com.monprojet.backend.model.Utilisateur;
import com.monprojet.backend.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurRepository repo;

    // GET tous les employés
    @GetMapping
    public List<Utilisateur> getAll() {
        return repo.findAll();
    }

    // GET un employé par ID
    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST créer un employé
    @PostMapping
    public Utilisateur create(@RequestBody Utilisateur u) {
        return repo.save(u);
    }

    // PUT modifier un employé
    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> update(@PathVariable Long id, @RequestBody Utilisateur u) {
        return repo.findById(id).map(existing -> {
            existing.setNom(u.getNom());
            existing.setPrenom(u.getPrenom());
            existing.setEmail(u.getEmail());
            existing.setTelephone(u.getTelephone());
            existing.setPoste(u.getPoste());
            existing.setDepartement(u.getDepartement());
            existing.setUsername(u.getUsername());
            existing.setRole(u.getRole());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE supprimer un employé
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}