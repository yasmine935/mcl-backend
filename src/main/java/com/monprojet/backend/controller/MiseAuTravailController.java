package com.monprojet.backend.controller;

import com.monprojet.backend.model.MiseAuTravail;
import com.monprojet.backend.repository.MiseAuTravailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/mises-au-travail")
public class MiseAuTravailController {

    @Autowired
    private MiseAuTravailRepository miseAuTravailRepository;

    @GetMapping
    public List<MiseAuTravail> getAll() {
        return miseAuTravailRepository.findAll();
    }

    @GetMapping("/utilisateur/{id}")
    public List<MiseAuTravail> getByUtilisateur(@PathVariable Long id) {
        return miseAuTravailRepository.findByCreePar_Id(id);
    }

    @GetMapping("/statut/{statut}")
    public List<MiseAuTravail> getByStatut(@PathVariable String statut) {
        return miseAuTravailRepository.findByStatut(statut);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MiseAuTravail> getById(@PathVariable Long id) {
        return miseAuTravailRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MiseAuTravail> create(@RequestBody MiseAuTravail mat) {
        mat.setDateCreation(LocalDateTime.now());
        if (mat.getStatut() == null) mat.setStatut("Brouillon");
        long count = miseAuTravailRepository.count();
        mat.setNumero("MAT-" + String.format("%04d", count + 1));
        return ResponseEntity.ok(miseAuTravailRepository.save(mat));
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<MiseAuTravail> updateStatut(
            @PathVariable Long id, @RequestParam String statut) {
        return miseAuTravailRepository.findById(id).map(mat -> {
            mat.setStatut(statut);
            return ResponseEntity.ok(miseAuTravailRepository.save(mat));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MiseAuTravail> update(
            @PathVariable Long id, @RequestBody MiseAuTravail data) {
        return miseAuTravailRepository.findById(id).map(existing -> {
            existing.setChantierZone(data.getChantierZone());
            existing.setDate(data.getDate());
            existing.setHeure(data.getHeure());
            existing.setEntreprisesIntervenantes(data.getEntreprisesIntervenantes());
            existing.setResponsableIntervention(data.getResponsableIntervention());
            existing.setDescriptionActivite(data.getDescriptionActivite());
            existing.setModesOperatoiresValides(data.getModesOperatoiresValides());
            existing.setCoActiviteIdentifiee(data.getCoActiviteIdentifiee());
            existing.setCoActivitePrecision(data.getCoActivitePrecision());
            existing.setPermisSpecifiques(data.getPermisSpecifiques());
            existing.setPermisSpecifiquesPrecision(data.getPermisSpecifiquesPrecision());
            existing.setRisquesEnvironnement(data.getRisquesEnvironnement());
            existing.setRisquesTache(data.getRisquesTache());
            existing.setProtectionsCollectives(data.getProtectionsCollectives());
            existing.setEpiSpecifiques(data.getEpiSpecifiques());
            existing.setMoyensAlerte(data.getMoyensAlerte());
            existing.setSecours(data.getSecours());
            existing.setNomSST(data.getNomSST());
            existing.setEvacuation(data.getEvacuation());
            existing.setStatut(data.getStatut());
            return ResponseEntity.ok(miseAuTravailRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        miseAuTravailRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}