package com.monprojet.backend.controller;

import com.monprojet.backend.model.MinuteSecurite;
import com.monprojet.backend.repository.MinuteSecuriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/minutes-securite")
public class MinuteSecuriteController {

    @Autowired
    private MinuteSecuriteRepository minuteSecuriteRepository;

    // GET ALL (pour managers)
    @GetMapping
    public List<MinuteSecurite> getAll() {
        return minuteSecuriteRepository.findAllByOrderByDateCreationDesc();
    }

    // GET par technicien
    @GetMapping("/technicien/{technicienId}")
    public List<MinuteSecurite> getByTechnicien(@PathVariable Long technicienId) {
        return minuteSecuriteRepository.findByTechnicienId(technicienId);
    }

    // POST - Technicien soumet
    @PreAuthorize("hasAnyAuthority('TECHNICIEN','TECHNICIEN_SUP','ADMINISTRATEUR')")
    @PostMapping
    public MinuteSecurite create(@RequestBody MinuteSecurite minute) {
        return minuteSecuriteRepository.save(minute);
    }

    // PUT - Marquer comme lu
    @PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    @PutMapping("/{id}/lu")
    public ResponseEntity<MinuteSecurite> marquerLu(@PathVariable Long id) {
        return minuteSecuriteRepository.findById(id).map(m -> {
            m.setStatut("LU");
            return ResponseEntity.ok(minuteSecuriteRepository.save(m));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (minuteSecuriteRepository.existsById(id)) {
            minuteSecuriteRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}