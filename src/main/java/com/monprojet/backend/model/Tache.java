package com.monprojet.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "taches")
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String priorite;
    private String statut;
    private LocalDate dateEcheance;
    private LocalDateTime dateCreation;

    private String client;
    private String clientFinal;
    private String chiffreAffaire;
    private String numDevis;

    @Column(columnDefinition = "TEXT")
    private String assignes;

    @Column(columnDefinition = "TEXT")
    private String etapes;

    @Column(columnDefinition = "LONGTEXT")
    private String fichiers;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPriorite() { return priorite; }
    public void setPriorite(String priorite) { this.priorite = priorite; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public LocalDate getDateEcheance() { return dateEcheance; }
    public void setDateEcheance(LocalDate dateEcheance) { this.dateEcheance = dateEcheance; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }
    public String getClientFinal() { return clientFinal; }
    public void setClientFinal(String clientFinal) { this.clientFinal = clientFinal; }
    public String getChiffreAffaire() { return chiffreAffaire; }
    public void setChiffreAffaire(String chiffreAffaire) { this.chiffreAffaire = chiffreAffaire; }
    public String getNumDevis() { return numDevis; }
    public void setNumDevis(String numDevis) { this.numDevis = numDevis; }
    public String getAssignes() { return assignes; }
    public void setAssignes(String assignes) { this.assignes = assignes; }
    public String getEtapes() { return etapes; }
    public void setEtapes(String etapes) { this.etapes = etapes; }
    public String getFichiers() { return fichiers; }
    public void setFichiers(String fichiers) { this.fichiers = fichiers; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
}