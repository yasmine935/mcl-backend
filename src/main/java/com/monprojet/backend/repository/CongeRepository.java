package com.monprojet.backend.repository;

import com.monprojet.backend.model.Conge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CongeRepository extends JpaRepository<Conge, Long> {
    List<Conge> findByUtilisateurId(Long utilisateurId);
}