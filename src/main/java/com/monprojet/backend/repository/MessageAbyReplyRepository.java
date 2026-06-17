package com.monprojet.backend.repository;

import com.monprojet.backend.model.MessageAbyReply;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageAbyReplyRepository extends JpaRepository<MessageAbyReply, Long> {
    List<MessageAbyReply> findByMessageIdOrderByDateEnvoiAsc(Long messageId);
}
