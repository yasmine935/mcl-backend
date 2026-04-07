package com.monprojet.backend.controller;

import com.monprojet.backend.model.PlanningNote;
import com.monprojet.backend.repository.PlanningNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/planning")
public class PlanningNoteController {

    @Autowired
    private PlanningNoteRepository planningNoteRepository;

    // GET toutes les notes
    @GetMapping
    public List<PlanningNote> getAll() {
        return planningNoteRepository.findAll();
    }

    // GET notes d'un utilisateur
    @GetMapping("/utilisateur/{id}")
    public List<PlanningNote> getByUtilisateur(@PathVariable Long id) {
        return planningNoteRepository.findByUtilisateurId(id);
    }

    // GET notes par plage de dates
    @GetMapping("/periode")
    public List<PlanningNote> getByPeriode(
            @RequestParam String debut,
            @RequestParam String fin) {
        return planningNoteRepository.findByDateBetween(
                LocalDate.parse(debut),
                LocalDate.parse(fin));
    }

    // GET ou créer note pour un utilisateur + date
    @GetMapping("/utilisateur/{id}/date/{date}")
    public ResponseEntity<PlanningNote> getByUserAndDate(
            @PathVariable Long id,
            @PathVariable String date) {
        return planningNoteRepository
                .findByUtilisateurIdAndDate(id, LocalDate.parse(date))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST ou PUT sauvegarder une note (upsert)
    @PostMapping("/sauvegarder")
    public ResponseEntity<PlanningNote> sauvegarder(@RequestBody PlanningNote planningNote) {
        // Si une note existe déjà pour cet utilisateur et cette date, on la met à jour
        if (planningNote.getUtilisateur() != null && planningNote.getDate() != null) {
            planningNoteRepository
                    .findByUtilisateurIdAndDate(
                            planningNote.getUtilisateur().getId(),
                            planningNote.getDate())
                    .ifPresent(existing -> planningNote.setId(existing.getId()));
        }
        return ResponseEntity.ok(planningNoteRepository.save(planningNote));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        planningNoteRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}