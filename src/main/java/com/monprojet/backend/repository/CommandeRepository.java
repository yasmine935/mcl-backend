package com.monprojet.backend.repository;
import com.monprojet.backend.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByUtilisateurId(Long userId);
    List<Commande> findByStatut(String statut);
}