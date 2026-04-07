package com.monprojet.backend.controller;

import com.monprojet.backend.model.Ticket;
import com.monprojet.backend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    // GET tous les tickets
    @GetMapping
    public List<Ticket> getAll() {
        return ticketRepository.findAllByOrderByDateCreationDesc();
    }

    // GET tickets par statut
    @GetMapping("/statut/{statut}")
    public List<Ticket> getByStatut(@PathVariable String statut) {
        return ticketRepository.findByStatut(statut);
    }

    // GET un ticket
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getById(@PathVariable Long id) {
        return ticketRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST créer un ticket (client)
    @PostMapping
    public ResponseEntity<Ticket> create(@RequestBody Ticket ticket) {
        ticket.setStatut("EN_ATTENTE");
        ticket.setDateCreation(LocalDateTime.now());
        // Auto-numérotation
        long count = ticketRepository.count();
        ticket.setNumero("TKT-" + String.format("%04d", count + 1));
        return ResponseEntity.ok(ticketRepository.save(ticket));
    }

    // PUT changer statut
    @PutMapping("/{id}/statut")
    public ResponseEntity<Ticket> updateStatut(
            @PathVariable Long id,
            @RequestParam String statut,
            @RequestParam(required = false) String valideePar) {
        return ticketRepository.findById(id).map(ticket -> {
            ticket.setStatut(statut);
            if ("VALIDEE".equals(statut)) {
                ticket.setDateValidation(LocalDateTime.now());
                ticket.setValideePar(valideePar);
            }
            return ResponseEntity.ok(ticketRepository.save(ticket));
        }).orElse(ResponseEntity.notFound().build());
    }

    // PUT modifier un ticket
    @PutMapping("/{id}")
    public ResponseEntity<Ticket> update(@PathVariable Long id, @RequestBody Ticket ticket) {
        return ticketRepository.findById(id).map(existing -> {
            existing.setStatut(ticket.getStatut());
            existing.setAssigne(ticket.getAssigne());
            existing.setPriorite(ticket.getPriorite());
            existing.setEcheance(ticket.getEcheance());
            existing.setValideePar(ticket.getValideePar());
            if ("VALIDEE".equals(ticket.getStatut()) && existing.getDateValidation() == null) {
                existing.setDateValidation(LocalDateTime.now());
            }
            return ResponseEntity.ok(ticketRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE supprimer
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        ticketRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}