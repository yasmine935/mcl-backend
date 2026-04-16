package com.monprojet.backend.controller;

import com.monprojet.backend.model.MinuteSecurite;
import com.monprojet.backend.repository.MinuteSecuriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/minutes-securite")
@CrossOrigin(origins = "http://localhost:4200")
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
    @PostMapping
    public MinuteSecurite create(@RequestBody MinuteSecurite minute) {
        return minuteSecuriteRepository.save(minute);
    }

    // PUT - Marquer comme lu
    @PutMapping("/{id}/lu")
    public ResponseEntity<MinuteSecurite> marquerLu(@PathVariable Long id) {
        return minuteSecuriteRepository.findById(id).map(m -> {
            m.setStatut("LU");
            return ResponseEntity.ok(minuteSecuriteRepository.save(m));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (minuteSecuriteRepository.existsById(id)) {
            minuteSecuriteRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}