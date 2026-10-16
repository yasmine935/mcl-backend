package com.monprojet.backend.repository;
import com.monprojet.backend.dto.FactureFiche;
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
    @Query(value = "SELECT * FROM fiches_intervention WHERE technicien_id = :id OR FIND_IN_SET(:id, technicien_ids) > 0", nativeQuery = true)
    List<FicheIntervention> findByTechnicienPrincipalOrMulti(@Param("id") Long id);

    // Fiches facturées d'un projet (lien par numProjet = titre du projet) : seuls les
    // champs de facturation sont lus, jamais les colonnes LONGTEXT (photos, signatures...).
    @Query("SELECT new com.monprojet.backend.dto.FactureFiche(f.id, f.dateFacturation, f.numeroFacture, f.montantFacture, f.statut) "
         + "FROM FicheIntervention f WHERE f.numProjet = :numProjet "
         + "AND ((f.numeroFacture IS NOT NULL AND f.numeroFacture <> '') OR (f.montantFacture IS NOT NULL AND f.montantFacture <> '')) "
         + "ORDER BY f.dateFacturation, f.id")
    List<FactureFiche> findFacturesByNumProjet(@Param("numProjet") String numProjet);
}
