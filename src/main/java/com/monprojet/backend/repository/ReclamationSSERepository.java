package com.monprojet.backend.repository;
import com.monprojet.backend.model.ReclamationSSE;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ReclamationSSERepository extends JpaRepository<ReclamationSSE, Long> {
    List<ReclamationSSE> findByTechnicienId(Long technicienId);
    List<ReclamationSSE> findByStatut(String statut);
}