package com.monprojet.backend.repository;
import com.monprojet.backend.model.Tache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
public interface TacheRepository extends JpaRepository<Tache, Long>, JpaSpecificationExecutor<Tache>, TacheResumeRepository {
    List<Tache> findByUtilisateurId(Long userId);
    List<Tache> findByStatut(String statut);
}