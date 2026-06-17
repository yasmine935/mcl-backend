package com.monprojet.backend.controller;

import com.monprojet.backend.model.MessageAby;
import com.monprojet.backend.repository.MessageAbyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages-aby")
@CrossOrigin(origins = "*")
public class MessageAbyController {

    @Autowired
    private MessageAbyRepository repo;

    @GetMapping
    public List<MessageAby> getAll() {
        return repo.findAllByOrderByDateEnvoiDesc();
    }

    @GetMapping("/non-lus")
    public List<MessageAby> getNonLus() {
        return repo.findByLuFalseOrderByDateEnvoiDesc();
    }

    @GetMapping("/expediteur/{exp}")
    public List<MessageAby> getByExpediteur(@PathVariable String exp) {
        return repo.findByExpediteurOrderByDateEnvoiDesc(exp);
    }

    @PostMapping
    public MessageAby envoyer(@RequestBody MessageAby msg) {
        msg.setDateEnvoi(LocalDateTime.now());
        msg.setLu(false);
        return repo.save(msg);
    }

    @PutMapping("/{id}/lu")
    public void marquerLu(@PathVariable Long id) {
        repo.findById(id).ifPresent(m -> {
            m.setLu(true);
            repo.save(m);
        });
    }

    @PutMapping("/{id}/repondre")
    public MessageAby repondre(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return repo.findById(id).map(m -> {
            m.setReponse(body.get("reponse"));
            m.setRepondantNom(body.get("repondantNom"));
            m.setDateReponse(LocalDateTime.now());
            m.setLu(true);
            return repo.save(m);
        }).orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
