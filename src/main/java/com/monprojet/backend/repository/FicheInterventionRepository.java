package com.monprojet.backend.repository;
import com.monprojet.backend.model.FicheIntervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface FicheInterventionRepository extends JpaRepository<FicheIntervention, Long> {
    List<FicheIntervention> findByTechnicienId(Long technicienId);
    List<FicheIntervention> findByManagerId(Long managerId);
    List<FicheIntervention> findByStatut(String statut);

    // Retourne toutes les fiches où le technicien est assigné (principal OU dans la liste multi)
    @Query(value = "SELECT * FROM fiche_intervention WHERE technicien_id = :id OR FIND_IN_SET(:id, technicien_ids) > 0", nativeQuery = true)
    List<FicheIntervention> findByTechnicienPrincipalOrMulti(@Param("id") Long id);
}
