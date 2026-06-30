package com.monprojet.backend.controller;

import com.monprojet.backend.model.CategorieTache;
import com.monprojet.backend.model.OptionTache;
import com.monprojet.backend.repository.CategorieTacheRepository;
import com.monprojet.backend.repository.OptionTacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories-taches")
@CrossOrigin(origins = "*")
public class CategorieTacheController {

    @Autowired
    private CategorieTacheRepository categorieRepo;

    @Autowired
    private OptionTacheRepository optionRepo;

    @GetMapping
    public List<Map<String, Object>> getAll() {
        return categorieRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @PostMapping
    public Map<String, Object> creer(@RequestBody Map<String, String> body) {
        CategorieTache categorie = new CategorieTache();
        categorie.setNom(body.get("nom"));
        categorieRepo.save(categorie);
        return toDto(categorie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> modifier(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return categorieRepo.findById(id).map(categorie -> {
            categorie.setNom(body.get("nom"));
            categorieRepo.save(categorie);
            return ResponseEntity.ok(toDto(categorie));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        optionRepo.deleteAll(optionRepo.findByCategorieId(id));
        categorieRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/options")
    public ResponseEntity<Map<String, Object>> ajouterOption(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return categorieRepo.findById(id).map(categorie -> {
            OptionTache option = new OptionTache();
            option.setCategorie(categorie);
            option.setNom(body.get("nom"));
            optionRepo.save(option);
            return ResponseEntity.ok(toDto(categorie));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/options/{optionId}")
    public ResponseEntity<Void> modifierOption(@PathVariable Long optionId, @RequestBody Map<String, String> body) {
        return optionRepo.findById(optionId).map(option -> {
            option.setNom(body.get("nom"));
            optionRepo.save(option);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/options/{optionId}")
    public ResponseEntity<Void> supprimerOption(@PathVariable Long optionId) {
        optionRepo.deleteById(optionId);
        return ResponseEntity.ok().build();
    }

    private Map<String, Object> toDto(CategorieTache categorie) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", categorie.getId());
        dto.put("nom", categorie.getNom());
        dto.put("options", optionRepo.findByCategorieId(categorie.getId()).stream()
                .map(o -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", o.getId());
                    m.put("nom", o.getNom());
                    return m;
                })
                .collect(Collectors.toList()));
        return dto;
    }
}
