package com.monprojet.backend.controller;

import com.monprojet.backend.model.MessageAby;
import com.monprojet.backend.model.MessageAbyReply;
import com.monprojet.backend.repository.MessageAbyRepository;
import com.monprojet.backend.repository.MessageAbyReplyRepository;
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

    @Autowired
    private MessageAbyReplyRepository replyRepo;

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

    // ── Thread de réponses (multi-tours) ──

    @GetMapping("/{id}/replies")
    public List<MessageAbyReply> getReplies(@PathVariable Long id) {
        return replyRepo.findByMessageIdOrderByDateEnvoiAsc(id);
    }

    @PostMapping("/{id}/replies")
    public MessageAbyReply addReply(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return repo.findById(id).map(msg -> {
            MessageAbyReply reply = new MessageAbyReply();
            reply.setMessage(msg);
            reply.setAuteur(body.get("auteur"));
            reply.setAuteurRole(body.get("auteurRole")); // "ABY" ou "MCL"
            reply.setContenu(body.get("contenu"));
            reply.setDateEnvoi(LocalDateTime.now());
            return replyRepo.save(reply);
        }).orElseThrow();
    }
}
