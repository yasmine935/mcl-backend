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

    @Column(columnDefinition = "TEXT")
    private String description;

    private String categorie;
    private String priorite;
    private String statut = EN_ATTENTE_VALIDATION;

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
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public String getPriorite() { return priorite; }
    public void setPriorite(String priorite) { this.priorite = priorite; }
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
