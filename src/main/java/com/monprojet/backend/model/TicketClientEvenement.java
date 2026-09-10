package com.monprojet.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Événement de la timeline d'un ticket client : création, changement de statut
 * ou commentaire. Couvre à la fois l'« historique horodaté » et les
 * « échanges client ↔ valideur » du cahier des charges.
 */
@Entity
@Table(name = "tickets_client_evenements")
public class TicketClientEvenement {

    public static final String CREATION = "CREATION";
    public static final String STATUT = "STATUT";
    public static final String COMMENTAIRE = "COMMENTAIRE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Le ticket est connu par l'URL ; inutile (et verbeux) de le re-sérialiser dans chaque événement.
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private TicketClient ticket;

    @ManyToOne
    @JoinColumn(name = "auteur_id")
    private Utilisateur auteur;

    private String type;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String ancienStatut;
    private String nouveauStatut;

    private LocalDateTime dateCreation;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TicketClient getTicket() { return ticket; }
    public void setTicket(TicketClient ticket) { this.ticket = ticket; }
    public Utilisateur getAuteur() { return auteur; }
    public void setAuteur(Utilisateur auteur) { this.auteur = auteur; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getAncienStatut() { return ancienStatut; }
    public void setAncienStatut(String ancienStatut) { this.ancienStatut = ancienStatut; }
    public String getNouveauStatut() { return nouveauStatut; }
    public void setNouveauStatut(String nouveauStatut) { this.nouveauStatut = nouveauStatut; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}
