package com.monprojet.backend.repository;

import com.monprojet.backend.model.TicketClient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketClientRepository extends JpaRepository<TicketClient, Long> {
    // Cloisonnement : les tickets d'un client donné.
    List<TicketClient> findByClientIdOrderByDateCreationDesc(Long clientId);
    List<TicketClient> findAllByOrderByDateCreationDesc();
}
