package com.monprojet.backend.controller;

import com.monprojet.backend.model.ArticleAttente;
import com.monprojet.backend.model.DemandeApprovisionnement;
import com.monprojet.backend.repository.ArticleAttenteRepository;
import com.monprojet.backend.repository.DemandeApprovisionnementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApprovisionnementController {

    @Autowired
    private DemandeApprovisionnementRepository demandeRepo;

    @Autowired
    private ArticleAttenteRepository attenteRepo;

    // ── DEMANDES ──

    @GetMapping("/demandes-appro")
    public List<DemandeApprovisionnement> getAll() {
        return demandeRepo.findAll();
    }

    @GetMapping("/demandes-appro/demandeur/{id}")
    public List<DemandeApprovisionnement> getByDemandeur(@PathVariable Long id) {
        return demandeRepo.findByDemandeurIdOrderByDateSoumissionDesc(id);
    }

    @PostMapping("/demandes-appro")
    public DemandeApprovisionnement create(@RequestBody DemandeApprovisionnement demande) {
        demande.setDateSoumission(LocalDateTime.now());
        if (demande.getStatut() == null) demande.setStatut("En attente");
        return demandeRepo.save(demande);
    }

    @PutMapping("/demandes-appro/{id}/statut")
    public DemandeApprovisionnement changerStatut(
            @PathVariable Long id,
            @RequestParam String statut,
            @RequestParam(required = false) String traitePar) {
        DemandeApprovisionnement d = demandeRepo.findById(id).orElseThrow();
        d.setStatut(statut);
        if (traitePar != null) d.setTraitePar(traitePar);
        d.setDateTraitement(java.time.LocalDate.now().toString());
        return demandeRepo.save(d);
    }

    @DeleteMapping("/demandes-appro/{id}")
    public void delete(@PathVariable Long id) {
        demandeRepo.deleteById(id);
    }

    // ── LISTE D'ATTENTE ──

    @GetMapping("/articles-attente/utilisateur/{id}")
    public List<ArticleAttente> getAttente(@PathVariable Long id) {
        return attenteRepo.findByUtilisateurId(id);
    }

    @PostMapping("/articles-attente")
    public ArticleAttente addAttente(@RequestBody ArticleAttente article) {
        return attenteRepo.save(article);
    }

    @DeleteMapping("/articles-attente/{id}")
    public void deleteAttente(@PathVariable Long id) {
        attenteRepo.deleteById(id);
    }
}
