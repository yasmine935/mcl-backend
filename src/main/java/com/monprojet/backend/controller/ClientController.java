package com.monprojet.backend.controller;

import com.monprojet.backend.model.Client;
import com.monprojet.backend.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "http://localhost:4200")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @PostMapping
    public Client create(@RequestBody Client client) {
        client.setId(null);
        return clientRepository.save(client);
    }

    @PutMapping("/{id}")
    public Client update(@PathVariable Long id, @RequestBody Client client) {
        client.setId(id);
        return clientRepository.save(client);
    }

    @PutMapping("/{id}/desactiver")
    public ResponseEntity<Client> desactiver(@PathVariable Long id,
                                             @RequestParam String desactivePar) {
        return clientRepository.findById(id).map(client -> {
            client.setActif(false);
            client.setDesactivePar(desactivePar);
            client.setDateDesactivation(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            );
            return ResponseEntity.ok(clientRepository.save(client));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/reactiver")
    public ResponseEntity<Client> reactiver(@PathVariable Long id) {
        return clientRepository.findById(id).map(client -> {
            client.setActif(true);
            client.setDesactivePar(null);
            client.setDateDesactivation(null);
            return ResponseEntity.ok(clientRepository.save(client));
        }).orElse(ResponseEntity.notFound().build());
    }
}
