package com.monprojet.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private String site;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String lieuSite;
    private String nomSalle;
    private String etage;

    @Column(columnDefinition = "TEXT")
    private String informationsAdditionnelles;

    private String typeMateriel;
    private String marque;
    private String reference;
    private Boolean sousGarantie;
    private String criticite;

    @Column(columnDefinition = "TEXT")
    private String descriptionPanne;

    private String priorite;
    private String echeance;
    private String statut; // EN_ATTENTE, EN_COURS, VALIDEE

    private LocalDateTime dateCreation;
    private LocalDateTime dateValidation;

    @ManyToOne
    @JoinColumn(name = "assigne_id")
    private Utilisateur assigne;

    @ManyToOne
    @JoinColumn(name = "demandeur_id")
    private Utilisateur demandeur;

    private String valideePar;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getSite() { return site; }
    public void setSite(String site) { this.site = site; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getLieuSite() { return lieuSite; }
    public void setLieuSite(String lieuSite) { this.lieuSite = lieuSite; }
    public String getNomSalle() { return nomSalle; }
    public void setNomSalle(String nomSalle) { this.nomSalle = nomSalle; }
    public String getEtage() { return etage; }
    public void setEtage(String etage) { this.etage = etage; }
    public String getInformationsAdditionnelles() { return informationsAdditionnelles; }
    public void setInformationsAdditionnelles(String v) { this.informationsAdditionnelles = v; }
    public String getTypeMateriel() { return typeMateriel; }
    public void setTypeMateriel(String typeMateriel) { this.typeMateriel = typeMateriel; }
    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public Boolean getSousGarantie() { return sousGarantie; }
    public void setSousGarantie(Boolean sousGarantie) { this.sousGarantie = sousGarantie; }
    public String getCriticite() { return criticite; }
    public void setCriticite(String criticite) { this.criticite = criticite; }
    public String getDescriptionPanne() { return descriptionPanne; }
    public void setDescriptionPanne(String descriptionPanne) { this.descriptionPanne = descriptionPanne; }
    public String getPriorite() { return priorite; }
    public void setPriorite(String priorite) { this.priorite = priorite; }
    public String getEcheance() { return echeance; }
    public void setEcheance(String echeance) { this.echeance = echeance; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public LocalDateTime getDateValidation() { return dateValidation; }
    public void setDateValidation(LocalDateTime dateValidation) { this.dateValidation = dateValidation; }
    public Utilisateur getAssigne() { return assigne; }
    public void setAssigne(Utilisateur assigne) { this.assigne = assigne; }
    public Utilisateur getDemandeur() { return demandeur; }
    public void setDemandeur(Utilisateur demandeur) { this.demandeur = demandeur; }
    public String getValideePar() { return valideePar; }
    public void setValideePar(String valideePar) { this.valideePar = valideePar; }
}