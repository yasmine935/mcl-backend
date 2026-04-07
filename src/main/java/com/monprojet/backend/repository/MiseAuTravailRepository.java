package com.monprojet.backend.repository;
import com.monprojet.backend.model.MiseAuTravail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface MiseAuTravailRepository extends JpaRepository<MiseAuTravail, Long> {
    List<MiseAuTravail> findByCreePar_Id(Long userId);
    List<MiseAuTravail> findByStatut(String statut);
}