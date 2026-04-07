package com.monprojet.backend.controller;

import com.monprojet.backend.model.ReclamationSSE;
import com.monprojet.backend.repository.ReclamationSSERepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reclamations-sse")
public class ReclamationSSEController {

    @Autowired
    private ReclamationSSERepository reclamationRepository;

    // GET toutes les remontées (managers)
    @GetMapping
    public List<ReclamationSSE> getAll() {
        return reclamationRepository.findAll();
    }

    // GET remontées d'un technicien
    @GetMapping("/technicien/{id}")
    public List<ReclamationSSE> getByTechnicien(@PathVariable Long id) {
        return reclamationRepository.findByTechnicienId(id);
    }

    // GET par statut
    @GetMapping("/statut/{statut}")
    public List<ReclamationSSE> getByStatut(@PathVariable String statut) {
        return reclamationRepository.findByStatut(statut);
    }

    // GET une remontée
    @GetMapping("/{id}")
    public ResponseEntity<ReclamationSSE> getById(@PathVariable Long id) {
        return reclamationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST créer (technicien)
    @PostMapping
    public ResponseEntity<ReclamationSSE> create(@RequestBody ReclamationSSE rec) {
        rec.setStatut("EN_ATTENTE");
        rec.setDateCreation(LocalDateTime.now());
        long count = reclamationRepository.count();
        rec.setNumero("SSE-" + String.format("%04d", count + 1));
        return ResponseEntity.ok(reclamationRepository.save(rec));
    }

    // PUT changer statut (manager)
    @PutMapping("/{id}/statut")
    public ResponseEntity<ReclamationSSE> updateStatut(
            @PathVariable Long id, @RequestParam String statut) {
        return reclamationRepository.findById(id).map(rec -> {
            rec.setStatut(statut);
            return ResponseEntity.ok(reclamationRepository.save(rec));
        }).orElse(ResponseEntity.notFound().build());
    }

    // PUT remplir partie encadrement (manager)
    @PutMapping("/{id}/encadrement")
    public ResponseEntity<ReclamationSSE> updateEncadrement(
            @PathVariable Long id, @RequestBody ReclamationSSE data) {
        return reclamationRepository.findById(id).map(rec -> {
            rec.setAnalyseCauses(data.getAnalyseCauses());
            rec.setActionCorrective(data.getActionCorrective());
            rec.setResponsableAction(data.getResponsableAction());
            rec.setEcheance(data.getEcheance());
            rec.setInformationDeclarant(data.getInformationDeclarant());
            return ResponseEntity.ok(reclamationRepository.save(rec));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        reclamationRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}