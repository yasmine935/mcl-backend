package com.monprojet.backend.service;

import com.monprojet.backend.model.Ticket;
import com.monprojet.backend.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> tous() {
        return ticketRepository.findAllByOrderByDateCreationDesc();
    }

    public List<Ticket> parStatut(String statut) {
        return ticketRepository.findByStatut(statut);
    }

    public Optional<Ticket> parId(Long id) {
        return ticketRepository.findById(id);
    }

    /**
     * Création via l'écran public : validation des champs essentiels côté serveur.
     * synchronized : évite que deux créations simultanées obtiennent le même numéro
     * (le compteur repose sur count() — correct tant qu'il n'y a qu'une instance).
     */
    @Transactional
    public synchronized Ticket creer(Ticket ticket) {
        if (estVide(ticket.getNom()) || estVide(ticket.getDescriptionPanne())) {
            throw new IllegalArgumentException("Nom et description de la panne sont obligatoires");
        }
        ticket.setStatut("EN_ATTENTE");
        ticket.setDateCreation(LocalDateTime.now());
        long count = ticketRepository.count();
        ticket.setNumero("TKT-" + String.format("%04d", count + 1));
        return ticketRepository.save(ticket);
    }

    @Transactional
    public Optional<Ticket> changerStatut(Long id, String statut, String valideePar) {
        return ticketRepository.findById(id).map(ticket -> {
            ticket.setStatut(statut);
            if ("VALIDEE".equals(statut)) {
                ticket.setDateValidation(LocalDateTime.now());
                ticket.setValideePar(valideePar);
            }
            return ticketRepository.save(ticket);
        });
    }

    @Transactional
    public Optional<Ticket> mettreAJour(Long id, Ticket ticket) {
        return ticketRepository.findById(id).map(existing -> {
            existing.setStatut(ticket.getStatut());
            existing.setAssigne(ticket.getAssigne());
            existing.setPriorite(ticket.getPriorite());
            existing.setEcheance(ticket.getEcheance());
            existing.setValideePar(ticket.getValideePar());
            if ("VALIDEE".equals(ticket.getStatut()) && existing.getDateValidation() == null) {
                existing.setDateValidation(LocalDateTime.now());
            }
            return ticketRepository.save(existing);
        });
    }

    @Transactional
    public boolean supprimer(Long id) {
        if (!ticketRepository.existsById(id)) return false;
        ticketRepository.deleteById(id);
        return true;
    }

    private boolean estVide(String s) {
        return s == null || s.isBlank();
    }
}
