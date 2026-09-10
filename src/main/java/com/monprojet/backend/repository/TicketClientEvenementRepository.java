package com.monprojet.backend.repository;

import com.monprojet.backend.model.TicketClientEvenement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketClientEvenementRepository extends JpaRepository<TicketClientEvenement, Long> {
    List<TicketClientEvenement> findByTicketIdOrderByDateCreationAsc(Long ticketId);
}
