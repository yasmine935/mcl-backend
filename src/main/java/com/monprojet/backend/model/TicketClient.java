package com.monprojet.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Ticket déposé par un client externe (compte de rôle CLIENT), distinct du Ticket
 * de la borne d'accueil. Adressé au groupe des valideurs (MANAGER + ADMINISTRATEUR) :
 * le premier qui le prend devient traitePar, visible de tous les valideurs.
 */
@Entity
@Table(name = "tickets_client")
public class TicketClient {

    // Cycle de vie (cf. cahier des charges)
    public static final String NOUVEAU = "NOUVEAU";
    public static final String EN_ATTENTE_VALIDATION = "EN_ATTENTE_VALIDATION";
    public static final String VALIDE = "VALIDE";
    public static final String REJETE = "REJETE";
    public static final String EN_COURS = "EN_COURS";
    public static final String RESOLU = "RESOLU";
    public static final String CLOTURE = "CLOTURE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private String titre;
    private String categorie;
    private String statut = EN_ATTENTE_VALIDATION;

    // ── Demande d'intervention (coordonnées du demandeur — peut différer du
    // titulaire du compte client si plusieurs personnes partagent un même accès) ──
    private String adresseSite;
    private String nomDemandeur;
    private String prenomDemandeur;
    private String telephoneDemandeur;
    private String emailDemandeur;

    // ── Lieu d'intervention ──
    private String lieuSite;
    private String nomSalle;
    private String etage;

    @Column(columnDefinition = "TEXT")
    private String informationsAdditionnelles;

    // ── Nature de la panne ──
    private String typeMateriel;
    private String marque;
    private String reference;
    private String numeroSerie;
    private Boolean sousGarantie;
    private String criticite;

    @Column(columnDefinition = "TEXT")
    private String descriptionPanne;

    private LocalDateTime dateCreation;
    private LocalDateTime dateMaj;
    private LocalDateTime dateTraitement;

    @Column(columnDefinition = "TEXT")
    private String commentaireValidation;

    // Le client (compte de rôle CLIENT) propriétaire du ticket — sert au cloisonnement.
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Utilisateur client;

    // Le valideur qui a pris le ticket en premier (null = encore à prendre par l'un des 3).
    @ManyToOne
    @JoinColumn(name = "traite_par_id")
    private Utilisateur traitePar;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public String getAdresseSite() { return adresseSite; }
    public void setAdresseSite(String adresseSite) { this.adresseSite = adresseSite; }
    public String getNomDemandeur() { return nomDemandeur; }
    public void setNomDemandeur(String nomDemandeur) { this.nomDemandeur = nomDemandeur; }
    public String getPrenomDemandeur() { return prenomDemandeur; }
    public void setPrenomDemandeur(String prenomDemandeur) { this.prenomDemandeur = prenomDemandeur; }
    public String getTelephoneDemandeur() { return telephoneDemandeur; }
    public void setTelephoneDemandeur(String telephoneDemandeur) { this.telephoneDemandeur = telephoneDemandeur; }
    public String getEmailDemandeur() { return emailDemandeur; }
    public void setEmailDemandeur(String emailDemandeur) { this.emailDemandeur = emailDemandeur; }
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
    public String getNumeroSerie() { return numeroSerie; }
    public void setNumeroSerie(String numeroSerie) { this.numeroSerie = numeroSerie; }
    public Boolean getSousGarantie() { return sousGarantie; }
    public void setSousGarantie(Boolean sousGarantie) { this.sousGarantie = sousGarantie; }
    public String getCriticite() { return criticite; }
    public void setCriticite(String criticite) { this.criticite = criticite; }
    public String getDescriptionPanne() { return descriptionPanne; }
    public void setDescriptionPanne(String descriptionPanne) { this.descriptionPanne = descriptionPanne; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public LocalDateTime getDateMaj() { return dateMaj; }
    public void setDateMaj(LocalDateTime dateMaj) { this.dateMaj = dateMaj; }
    public LocalDateTime getDateTraitement() { return dateTraitement; }
    public void setDateTraitement(LocalDateTime dateTraitement) { this.dateTraitement = dateTraitement; }
    public String getCommentaireValidation() { return commentaireValidation; }
    public void setCommentaireValidation(String commentaireValidation) { this.commentaireValidation = commentaireValidation; }
    public Utilisateur getClient() { return client; }
    public void setClient(Utilisateur client) { this.client = client; }
    public Utilisateur getTraitePar() { return traitePar; }
    public void setTraitePar(Utilisateur traitePar) { this.traitePar = traitePar; }
}
