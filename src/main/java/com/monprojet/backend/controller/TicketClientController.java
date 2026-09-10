package com.monprojet.backend.controller;

import com.monprojet.backend.dto.TicketClientDtos.CreationRequest;
import com.monprojet.backend.dto.TicketClientDtos.DecisionRequest;
import com.monprojet.backend.model.TicketClient;
import com.monprojet.backend.service.TicketClientService;
import com.monprojet.backend.service.TicketClientService.Refus;
import com.monprojet.backend.service.TicketClientService.Resultat;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tickets-client")
public class TicketClientController {

    private final TicketClientService service;

    public TicketClientController(TicketClientService service) {
        this.service = service;
    }

    // Création : réservée aux comptes clients.
    @PreAuthorize("hasAuthority('CLIENT')")
    @PostMapping
    public TicketClient creer(@Valid @RequestBody CreationRequest req, Principal principal) {
        return service.creer(req, principal.getName());
    }

    // Liste : cloisonnée (le client ne voit que ses tickets ; les valideurs voient tout).
    @GetMapping
    public List<TicketClient> lister(Principal principal, Authentication auth) {
        return service.lister(principal.getName(), auth);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> parId(@PathVariable Long id, Principal principal, Authentication auth) {
        return traduire(service.parId(id, principal.getName(), auth));
    }

    // Décisions de valideur — le service vérifie le rôle et le « premier qui prend ».
    @PutMapping("/{id}/valider")
    public ResponseEntity<?> valider(@PathVariable Long id, @RequestBody(required = false) DecisionRequest d,
                                     Principal principal, Authentication auth) {
        return traduire(service.decider(id, TicketClient.VALIDE, commentaire(d), principal.getName(), auth));
    }

    @PutMapping("/{id}/rejeter")
    public ResponseEntity<?> rejeter(@PathVariable Long id, @RequestBody(required = false) DecisionRequest d,
                                     Principal principal, Authentication auth) {
        return traduire(service.decider(id, TicketClient.REJETE, commentaire(d), principal.getName(), auth));
    }

    @PutMapping("/{id}/prendre-en-charge")
    public ResponseEntity<?> prendreEnCharge(@PathVariable Long id, Principal principal, Authentication auth) {
        return traduire(service.decider(id, TicketClient.EN_COURS, null, principal.getName(), auth));
    }

    @PutMapping("/{id}/resoudre")
    public ResponseEntity<?> resoudre(@PathVariable Long id, Principal principal, Authentication auth) {
        return traduire(service.decider(id, TicketClient.RESOLU, null, principal.getName(), auth));
    }

    @PutMapping("/{id}/cloturer")
    public ResponseEntity<?> cloturer(@PathVariable Long id, Principal principal, Authentication auth) {
        return traduire(service.decider(id, TicketClient.CLOTURE, null, principal.getName(), auth));
    }

    private String commentaire(DecisionRequest d) {
        return d == null ? null : d.commentaire();
    }

    private ResponseEntity<?> traduire(Resultat r) {
        if (r.estOk()) return ResponseEntity.ok(r.ticket());
        Refus refus = r.refus();
        return switch (refus) {
            case INTROUVABLE -> ResponseEntity.notFound().build();
            case NON_AUTORISE -> ResponseEntity.status(403).body("Accès refusé");
            case DEJA_PRIS -> ResponseEntity.status(409).body("Ticket déjà pris en charge par un autre valideur");
            case TRANSITION_INVALIDE -> ResponseEntity.badRequest().body("Transition de statut non autorisée");
        };
    }
}
