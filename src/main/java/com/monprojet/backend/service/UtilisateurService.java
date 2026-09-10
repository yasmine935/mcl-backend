package com.monprojet.backend.service;

import com.monprojet.backend.model.Utilisateur;
import com.monprojet.backend.repository.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    private final UtilisateurRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurService(UtilisateurRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Utilisateur> tous() {
        return repo.findAll();
    }

    public Optional<Utilisateur> parId(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public Utilisateur creer(Utilisateur utilisateur) {
        utilisateur.setPremierConnexion(true);
        if (utilisateur.getPassword() != null && !utilisateur.getPassword().isBlank()) {
            utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        }
        return repo.save(utilisateur);
    }

    /** Mise à jour partielle du profil — ne touche jamais au mot de passe. */
    @Transactional
    public Optional<Utilisateur> mettreAJour(Long id, Utilisateur u) {
        return repo.findById(id).map(existing -> {
            existing.setNom(u.getNom());
            existing.setPrenom(u.getPrenom());
            existing.setEmail(u.getEmail());
            existing.setTelephone(u.getTelephone());
            existing.setPoste(u.getPoste());
            existing.setDepartement(u.getDepartement());
            existing.setUsername(u.getUsername());
            existing.setRole(u.getRole());
            return repo.save(existing);
        });
    }

    @Transactional
    public Optional<Utilisateur> desactiver(Long id, String desactivePar) {
        return repo.findById(id).map(u -> {
            u.setActif(false);
            u.setDesactivePar(desactivePar);
            u.setDateDesactivation(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            return repo.save(u);
        });
    }

    @Transactional
    public Optional<Utilisateur> reactiver(Long id) {
        return repo.findById(id).map(u -> {
            u.setActif(true);
            u.setDesactivePar(null);
            u.setDateDesactivation(null);
            return repo.save(u);
        });
    }
}
