package com.monprojet.backend.repository;
import com.monprojet.backend.model.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface FactureRepository extends JpaRepository<Facture, Long> {
    List<Facture> findByUtilisateurId(Long userId);
    List<Facture> findByStatut(String statut);
}