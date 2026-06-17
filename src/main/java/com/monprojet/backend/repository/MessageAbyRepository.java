package com.monprojet.backend.repository;

import com.monprojet.backend.model.MessageAby;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageAbyRepository extends JpaRepository<MessageAby, Long> {
    List<MessageAby> findByExpediteurOrderByDateEnvoiDesc(String expediteur);
    List<MessageAby> findAllByOrderByDateEnvoiDesc();
    List<MessageAby> findByLuFalseOrderByDateEnvoiDesc();
}
