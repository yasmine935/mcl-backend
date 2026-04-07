package com.monprojet.backend.repository;
import com.monprojet.backend.model.FicheIntervention;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface FicheInterventionRepository extends JpaRepository<FicheIntervention, Long> {
    List<FicheIntervention> findByTechnicienId(Long technicienId);
    List<FicheIntervention> findByManagerId(Long managerId);
    List<FicheIntervention> findByStatut(String statut);
}