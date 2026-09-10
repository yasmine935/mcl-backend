package com.monprojet.backend.controller;

import com.monprojet.backend.model.Ticket;
import com.monprojet.backend.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    // GET tous les tickets (public : affiché sur l'écran de création)
    @GetMapping
    public List<Ticket> getAll() {
        return service.tous();
    }

    // GET tickets par statut
    @GetMapping("/statut/{statut}")
    public List<Ticket> getByStatut(@PathVariable String statut) {
        return service.parStatut(statut);
    }

    // GET un ticket
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getById(@PathVariable Long id) {
        return service.parId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST créer un ticket (public — validation des champs essentiels dans le service)
    @PostMapping
    public ResponseEntity<Ticket> create(@RequestBody Ticket ticket) {
        return ResponseEntity.ok(service.creer(ticket));
    }

    // PUT changer statut
    @PutMapping("/{id}/statut")
    public ResponseEntity<Ticket> updateStatut(
            @PathVariable Long id,
            @RequestParam String statut,
            @RequestParam(required = false) String valideePar) {
        return service.changerStatut(id, statut, valideePar)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT modifier un ticket
    @PutMapping("/{id}")
    public ResponseEntity<Ticket> update(@PathVariable Long id, @RequestBody Ticket ticket) {
        return service.mettreAJour(id, ticket)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE supprimer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.supprimer(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
