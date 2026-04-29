package com.monprojet.backend.repository;

import com.monprojet.backend.model.ResetPasswordRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResetPasswordRequestRepository extends JpaRepository<ResetPasswordRequest, Long> {
    List<ResetPasswordRequest> findByStatut(String statut);
}
