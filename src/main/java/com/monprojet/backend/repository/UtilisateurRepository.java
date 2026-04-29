package com.monprojet.backend.repository;

import com.monprojet.backend.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByUsernameAndPassword(String username, String password);
    Optional<Utilisateur> findByUsername(String username);
}