package com.monprojet.backend.controller;

import com.monprojet.backend.model.Conge;
import com.monprojet.backend.repository.CongeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/conges")
@CrossOrigin(origins = "http://localhost:4200")
public class CongeController {

    @Autowired
    private CongeRepository congeRepository;

    // GET ALL
    @GetMapping
    public List<Conge> getAll() {
        return congeRepository.findAll();
    }

    // GET by employe
    @GetMapping("/employe/{userId}")
    public List<Conge> getByEmploye(@PathVariable Long userId) {
        return congeRepository.findByUtilisateurId(userId);
    }

    // POST
    @PostMapping
    public Conge create(@RequestBody Conge conge) {
        if (conge.getStatut() == null || conge.getStatut().isBlank()) {
            conge.setStatut("EN_ATTENTE");
        }
        return congeRepository.save(conge);
    }

    // PUT (modification de la demande par son auteur)
    @PutMapping("/{id}")
    public ResponseEntity<Conge> update(@PathVariable Long id, @RequestBody Conge congeModifie) {
        return congeRepository.findById(id).map(conge -> {
            conge.setDateDebut(congeModifie.getDateDebut());
            conge.setDateFin(congeModifie.getDateFin());
            conge.setType(congeModifie.getType());
            conge.setMotif(congeModifie.getMotif());
            conge.setPeriode(congeModifie.getPeriode());
            return ResponseEntity.ok(congeRepository.save(conge));
        }).orElse(ResponseEntity.notFound().build());
    }

    // PUT statut
    @PutMapping("/{id}/statut")
    public ResponseEntity<Conge> updateStatut(@PathVariable Long id,
                                              @RequestParam String statut) {
        return congeRepository.findById(id).map(conge -> {
            conge.setStatut(statut);
            if ("VALIDE_KIA".equals(statut)) {
                conge.setValideParKia(true);
            }
            return ResponseEntity.ok(congeRepository.save(conge));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (congeRepository.existsById(id)) {
            congeRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ✅ GET SOLDE CONGES
    @GetMapping("/solde/{userId}")
    public ResponseEntity<Map<String, Object>> getSoldeConges(@PathVariable Long userId) {
        int anneeEnCours = LocalDate.now().getYear();

        List<Conge> congesApprouves = congeRepository.findByUtilisateurId(userId)
                .stream()
                .filter(c -> "APPROUVE".equals(c.getStatut()))
                .filter(c -> c.getDateDebut() != null && c.getDateDebut().getYear() == anneeEnCours)
                .collect(Collectors.toList());

        double joursAnnuelPris = congesApprouves.stream()
                .filter(c -> "ANNUEL".equals(c.getType()))
                .mapToDouble(c -> calculerJours(c.getDateDebut(), c.getDateFin(), c.getPeriode()))
                .sum();

        double joursRTTPris = congesApprouves.stream()
                .filter(c -> "RTT".equals(c.getType()))
                .mapToDouble(c -> calculerJours(c.getDateDebut(), c.getDateFin(), c.getPeriode()))
                .sum();

        double joursMaladiePris = congesApprouves.stream()
                .filter(c -> "MALADIE".equals(c.getType()))
                .mapToDouble(c -> calculerJours(c.getDateDebut(), c.getDateFin(), c.getPeriode()))
                .sum();

        int soldeAnnuelTotal = 25;
        int soldeRTTTotal = 12;

        Map<String, Object> solde = new HashMap<>();
        solde.put("soldeAnnuelTotal", soldeAnnuelTotal);
        solde.put("joursAnnuelPris", joursAnnuelPris);
        solde.put("soldeAnnuelRestant", soldeAnnuelTotal - joursAnnuelPris);
        solde.put("soldeRTTTotal", soldeRTTTotal);
        solde.put("joursRTTPris", joursRTTPris);
        solde.put("soldeRTTRestant", soldeRTTTotal - joursRTTPris);
        solde.put("joursMaladiePris", joursMaladiePris);
        solde.put("annee", anneeEnCours);

        return ResponseEntity.ok(solde);
    }

    private double calculerJours(LocalDate debut, LocalDate fin, String periode) {
        if (debut == null || fin == null) return 0;
        if (debut.equals(fin) && ("MATIN".equals(periode) || "APRES_MIDI".equals(periode))) {
            return 0.5;
        }
        return ChronoUnit.DAYS.between(debut, fin) + 1;
    }
}