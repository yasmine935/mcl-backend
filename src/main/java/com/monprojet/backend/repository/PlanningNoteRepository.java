package com.monprojet.backend.repository;
import com.monprojet.backend.model.PlanningNote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
public interface PlanningNoteRepository extends JpaRepository<PlanningNote, Long> {
    List<PlanningNote> findByUtilisateurId(Long userId);
    List<PlanningNote> findByDateBetween(LocalDate debut, LocalDate fin);
    Optional<PlanningNote> findByUtilisateurIdAndDate(Long userId, LocalDate date);
}