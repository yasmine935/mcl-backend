package com.monprojet.backend.controller;

import com.monprojet.backend.model.Utilisateur;
import com.monprojet.backend.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    // POST créer un employé — réservé à l'admin et aux RH
    // (rôles-prénoms conservés jusqu'à la refonte en rôles fonctionnels)
    @PreAuthorize("hasAnyAuthority('FERID', 'KARINE')")
    @PostMapping
    public Utilisateur creer(@RequestBody Utilisateur utilisateur) {
        utilisateur.setPremierConnexion(true);
        if (utilisateur.getPassword() != null && !utilisateur.getPassword().isBlank()) {
            utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        }
        return repo.save(utilisateur);
    }
    // PUT modifier un employé (dont le rôle) — réservé à l'admin et aux RH,
    // sinon n'importe quel compte pourrait s'attribuer le rôle admin
    @PreAuthorize("hasAnyAuthority('FERID', 'KARINE')")
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

    // Désactiver un employé (soft delete) — réservé à l'admin et aux RH
    @PreAuthorize("hasAnyAuthority('FERID', 'KARINE')")
    @PutMapping("/{id}/desactiver")
    public ResponseEntity<Utilisateur> desactiver(@PathVariable Long id,
                                                  @RequestParam String desactivePar) {
        return repo.findById(id).map(u -> {
            u.setActif(false);
            u.setDesactivePar(desactivePar);
            u.setDateDesactivation(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            );
            return ResponseEntity.ok(repo.save(u));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Réactiver un employé — réservé à l'admin et aux RH
    @PreAuthorize("hasAnyAuthority('FERID', 'KARINE')")
    @PutMapping("/{id}/reactiver")
    public ResponseEntity<Utilisateur> reactiver(@PathVariable Long id) {
        return repo.findById(id).map(u -> {
            u.setActif(true);
            u.setDesactivePar(null);
            u.setDateDesactivation(null);
            return ResponseEntity.ok(repo.save(u));
        }).orElse(ResponseEntity.notFound().build());
    }
}