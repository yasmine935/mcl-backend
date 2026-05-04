// DemandeApprovisionnementRepository.java
package com.monprojet.backend.repository;
import com.monprojet.backend.model.DemandeApprovisionnement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DemandeApprovisionnementRepository extends JpaRepository<DemandeApprovisionnement, Long> {
    List<DemandeApprovisionnement> findByDemandeurIdOrderByDateSoumissionDesc(Long demandeurId);
}
