package com.monprojet.backend.repository;

import com.monprojet.backend.model.JournalTravail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JournalTravailRepository extends JpaRepository<JournalTravail, Long> {
    List<JournalTravail> findByUtilisateurIdOrderByDateJourneeDesc(Long utilisateurId);
    List<JournalTravail> findAllByOrderByDateJourneeDesc();
}
