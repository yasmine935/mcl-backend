package com.monprojet.backend.repository;

import com.monprojet.backend.model.Visiteur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisiteurRepository extends JpaRepository<Visiteur, Long> {
}
