package com.monprojet.backend.controller;

import com.monprojet.backend.model.Utilisateur;
import com.monprojet.backend.service.UtilisateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService service;

    public UtilisateurController(UtilisateurService service) {
        this.service = service;
    }

    // GET tous les employés
    @GetMapping
    public List<Utilisateur> getAll() {
        return service.tous();
    }

    // GET un employé par ID
    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getById(@PathVariable Long id) {
        return service.parId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST créer un employé — réservé à l'administrateur et aux RH
    @PreAuthorize("hasAnyAuthority('ADMINISTRATEUR', 'RH')")
    @PostMapping
    public Utilisateur creer(@RequestBody Utilisateur utilisateur) {
        return service.creer(utilisateur);
    }

    // PUT modifier un employé (dont le rôle) — réservé à l'administrateur et aux RH,
    // sinon n'importe quel compte pourrait s'attribuer le rôle admin
    @PreAuthorize("hasAnyAuthority('ADMINISTRATEUR', 'RH')")
    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> update(@PathVariable Long id, @RequestBody Utilisateur u) {
        return service.mettreAJour(id, u)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Désactiver un employé (soft delete) — réservé à l'administrateur et aux RH
    @PreAuthorize("hasAnyAuthority('ADMINISTRATEUR', 'RH')")
    @PutMapping("/{id}/desactiver")
    public ResponseEntity<Utilisateur> desactiver(@PathVariable Long id,
                                                  @RequestParam String desactivePar) {
        return service.desactiver(id, desactivePar)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Réactiver un employé — réservé à l'administrateur et aux RH
    @PreAuthorize("hasAnyAuthority('ADMINISTRATEUR', 'RH')")
    @PutMapping("/{id}/reactiver")
    public ResponseEntity<Utilisateur> reactiver(@PathVariable Long id) {
        return service.reactiver(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
