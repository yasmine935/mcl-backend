package com.monprojet.backend.repository;
import com.monprojet.backend.model.Tache;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TacheRepository extends JpaRepository<Tache, Long> {
    List<Tache> findByUtilisateurId(Long userId);
    List<Tache> findByStatut(String statut);
}