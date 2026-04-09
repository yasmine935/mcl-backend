package com.monprojet.backend.repository;

import com.monprojet.backend.model.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoitureRepository extends JpaRepository<Voiture, Long> {
    List<Voiture> findByStatut(String statut);
    List<Voiture> findByConducteur(String conducteur);
}