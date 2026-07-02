package com.monprojet.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "journaux_travail")
public class JournalTravail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    private LocalDate dateJournee;

    @Column(columnDefinition = "TEXT")
    private String notesOriginales;

    @Column(columnDefinition = "TEXT")
    private String rapportGenere;

    private LocalDateTime dateCreation;

    @PrePersist
    public void prePersist() {
        if (this.dateCreation == null) this.dateCreation = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
    public LocalDate getDateJournee() { return dateJournee; }
    public void setDateJournee(LocalDate dateJournee) { this.dateJournee = dateJournee; }
    public String getNotesOriginales() { return notesOriginales; }
    public void setNotesOriginales(String notesOriginales) { this.notesOriginales = notesOriginales; }
    public String getRapportGenere() { return rapportGenere; }
    public void setRapportGenere(String rapportGenere) { this.rapportGenere = rapportGenere; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}
