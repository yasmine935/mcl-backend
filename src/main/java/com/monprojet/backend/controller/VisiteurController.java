package com.monprojet.backend.controller;

import com.monprojet.backend.model.Visiteur;
import com.monprojet.backend.repository.VisiteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visiteurs")
public class VisiteurController {

    @Autowired
    private VisiteurRepository visiteurRepository;

    @GetMapping
    public List<Visiteur> getAll() {
        return visiteurRepository.findAll();
    }

    @PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    @PostMapping
    public Visiteur create(@RequestBody Visiteur visiteur) {
        return visiteurRepository.save(visiteur);
    }

    @PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (visiteurRepository.existsById(id)) {
            visiteurRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
