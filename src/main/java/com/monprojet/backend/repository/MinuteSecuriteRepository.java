package com.monprojet.backend.repository;

import com.monprojet.backend.model.MinuteSecurite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MinuteSecuriteRepository extends JpaRepository<MinuteSecurite, Long> {
    List<MinuteSecurite> findByTechnicienId(Long technicienId);
    List<MinuteSecurite> findByStatut(String statut);
    List<MinuteSecurite> findAllByOrderByDateCreationDesc();
}