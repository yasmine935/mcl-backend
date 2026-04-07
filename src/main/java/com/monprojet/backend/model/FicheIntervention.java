package com.monprojet.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fiches_intervention")
public class FicheIntervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numProjet;
    private String client;
    private String adresse;
    private String contact;
    private LocalDate dateIntervention;
    private String heureDebut;
    private String heureFin;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String statut; // EN_COURS, COMPLETEE

    private LocalDateTime dateCreation;

    @ManyToOne
    @JoinColumn(name = "technicien_id")
    private Utilisateur technicien;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Utilisateur manager;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumProjet() { return numProjet; }
    public void setNumProjet(String numProjet) { this.numProjet = numProjet; }
    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public LocalDate getDateIntervention() { return dateIntervention; }
    public void setDateIntervention(LocalDate dateIntervention) { this.dateIntervention = dateIntervention; }
    public String getHeureDebut() { return heureDebut; }
    public void setHeureDebut(String heureDebut) { this.heureDebut = heureDebut; }
    public String getHeureFin() { return heureFin; }
    public void setHeureFin(String heureFin) { this.heureFin = heureFin; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public Utilisateur getTechnicien() { return technicien; }
    public void setTechnicien(Utilisateur technicien) { this.technicien = technicien; }
    public Utilisateur getManager() { return manager; }
    public void setManager(Utilisateur manager) { this.manager = manager; }
}