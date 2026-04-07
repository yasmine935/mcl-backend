package com.monprojet.backend.repository;

import com.monprojet.backend.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatut(String statut);
    List<Ticket> findAllByOrderByDateCreationDesc();
}