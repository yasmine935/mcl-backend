package com.monprojet.backend.controller;

import com.monprojet.backend.model.Tache;
import com.monprojet.backend.repository.TacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/taches")
public class TacheController {

    @Autowired
    private TacheRepository tacheRepository;

    @GetMapping
    public List<Tache> getAll() {
        return tacheRepository.findAll();
    }

    /**
     * Liste paginée (écran "Gestion des Projets") : charge 20 projets à la fois
     * au lieu de tout charger d'un coup (findAll() devenait lent, certains
     * projets embarquent des fichiers en base64 dans "fichiers", potentiellement
     * volumineux). Filtres optionnels appliqués côté base, pas en mémoire.
     */
    @GetMapping("/page")
    public Page<Tache> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) String priorite,
            @RequestParam(required = false) String client,
            @RequestParam(required = false) String recherche) {

        Specification<Tache> spec = Specification.where(null);
        if (statut != null && !statut.isBlank()) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("statut"), statut));
        }
        if (priorite != null && !priorite.isBlank()) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("priorite"), priorite));
        }
        if (client != null && !client.isBlank()) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("client"), client));
        }
        if (recherche != null && !recherche.isBlank()) {
            String like = "%" + recherche.toLowerCase() + "%";
            spec = spec.and((root, q, cb) -> cb.or(
                    cb.like(cb.lower(root.get("titre")), like),
                    cb.like(cb.lower(root.get("client")), like),
                    cb.like(cb.lower(root.get("description")), like),
                    cb.like(cb.lower(root.get("clientFinal")), like)
            ));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreation"));
        return tacheRepository.findAll(spec, pageable);
    }

    @GetMapping("/utilisateur/{id}")
    public List<Tache> getByUtilisateur(@PathVariable Long id) {
        return tacheRepository.findByUtilisateurId(id);
    }

    @GetMapping("/statut/{statut}")
    public List<Tache> getByStatut(@PathVariable String statut) {
        return tacheRepository.findByStatut(statut);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tache> getById(@PathVariable Long id) {
        return tacheRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','TECHNICIEN_SUP','SUPPLY_CHAIN','ADMINISTRATEUR')")
    @PostMapping
    public ResponseEntity<Tache> create(@RequestBody Tache tache) {
        tache.setStatut("A_FAIRE");
        tache.setDateCreation(LocalDateTime.now());
        return ResponseEntity.ok(tacheRepository.save(tache));
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','TECHNICIEN_SUP','SUPPLY_CHAIN','ADMINISTRATEUR')")
    @PutMapping("/{id}/statut")
    public ResponseEntity<Tache> updateStatut(
            @PathVariable Long id, @RequestParam String statut) {
        return tacheRepository.findById(id).map(tache -> {
            tache.setStatut(statut);
            return ResponseEntity.ok(tacheRepository.save(tache));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','TECHNICIEN_SUP','SUPPLY_CHAIN','ADMINISTRATEUR')")
    @PutMapping("/{id}")
    public ResponseEntity<Tache> update(@PathVariable Long id, @RequestBody Tache tache) {
        return tacheRepository.findById(id).map(existing -> {
            existing.setTitre(tache.getTitre());
            existing.setDescription(tache.getDescription());
            existing.setPriorite(tache.getPriorite());
            existing.setStatut(tache.getStatut());
            existing.setAvancement(tache.getAvancement());
            existing.setDateEcheance(tache.getDateEcheance());
            existing.setClient(tache.getClient());
            existing.setClientFinal(tache.getClientFinal());
            existing.setAdresse(tache.getAdresse());
            existing.setChiffreAffaire(tache.getChiffreAffaire());
            existing.setNumDevis(tache.getNumDevis());
            existing.setCaDevis(tache.getCaDevis());
            existing.setAssignes(tache.getAssignes());
            existing.setEtapes(tache.getEtapes());
            existing.setFichiers(tache.getFichiers());
            if (tache.getUtilisateur() != null) existing.setUtilisateur(tache.getUtilisateur());
            return ResponseEntity.ok(tacheRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','TECHNICIEN_SUP','SUPPLY_CHAIN','ADMINISTRATEUR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        tacheRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}