package com.monprojet.backend.controller;

import com.monprojet.backend.model.Stock;
import com.monprojet.backend.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    @Autowired
    private StockRepository stockRepository;

    @GetMapping
    public List<Stock> getAll() {
        return stockRepository.findAll();
    }

    @GetMapping("/utilisateur/{id}")
    public List<Stock> getByUtilisateur(@PathVariable Long id) {
        return stockRepository.findByUtilisateurId(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> getById(@PathVariable Long id) {
        return stockRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Stock> create(@RequestBody Stock stock) {
        stock.setDateCreation(LocalDateTime.now());
        stock.setDateMaj(LocalDateTime.now());
        return ResponseEntity.ok(stockRepository.save(stock));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> update(@PathVariable Long id, @RequestBody Stock stock) {
        return stockRepository.findById(id).map(existing -> {
            existing.setReference(stock.getReference());
            existing.setNom(stock.getNom());
            existing.setDescription(stock.getDescription());
            existing.setQuantite(stock.getQuantite());
            existing.setPrixUnitaire(stock.getPrixUnitaire());
            existing.setCategorie(stock.getCategorie());
            existing.setFournisseur(stock.getFournisseur());
            existing.setDateMaj(LocalDateTime.now());
            return ResponseEntity.ok(stockRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        stockRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}