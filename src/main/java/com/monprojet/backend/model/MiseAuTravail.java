package com.monprojet.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mises_au_travail")
public class MiseAuTravail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private String chantierZone;
    private String date;
    private String heure;
    private String entreprisesIntervenantes;
    private String responsableIntervention;

    // A. Analyse
    @Column(columnDefinition = "TEXT")
    private String descriptionActivite;
    private Boolean modesOperatoiresValides;
    private Boolean coActiviteIdentifiee;
    private String coActivitePrecision;
    private Boolean permisSpecifiques;
    private String permisSpecifiquesPrecision;

    // B. Risques
    private Boolean risquesEnvironnement;
    private Boolean risquesTache;
    private Boolean protectionsCollectives;
    private Boolean epiSpecifiques;

    // C. Urgence
    private Boolean moyensAlerte;
    private Boolean secours;
    private String nomSST;
    private Boolean evacuation;

    private String statut; // Brouillon, Validee, Archivee
    private LocalDateTime dateCreation;

    @ManyToOne
    @JoinColumn(name = "cree_par_id")
    private Utilisateur creePar;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getChantierZone() { return chantierZone; }
    public void setChantierZone(String v) { this.chantierZone = v; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getHeure() { return heure; }
    public void setHeure(String heure) { this.heure = heure; }
    public String getEntreprisesIntervenantes() { return entreprisesIntervenantes; }
    public void setEntreprisesIntervenantes(String v) { this.entreprisesIntervenantes = v; }
    public String getResponsableIntervention() { return responsableIntervention; }
    public void setResponsableIntervention(String v) { this.responsableIntervention = v; }
    public String getDescriptionActivite() { return descriptionActivite; }
    public void setDescriptionActivite(String v) { this.descriptionActivite = v; }
    public Boolean getModesOperatoiresValides() { return modesOperatoiresValides; }
    public void setModesOperatoiresValides(Boolean v) { this.modesOperatoiresValides = v; }
    public Boolean getCoActiviteIdentifiee() { return coActiviteIdentifiee; }
    public void setCoActiviteIdentifiee(Boolean v) { this.coActiviteIdentifiee = v; }
    public String getCoActivitePrecision() { return coActivitePrecision; }
    public void setCoActivitePrecision(String v) { this.coActivitePrecision = v; }
    public Boolean getPermisSpecifiques() { return permisSpecifiques; }
    public void setPermisSpecifiques(Boolean v) { this.permisSpecifiques = v; }
    public String getPermisSpecifiquesPrecision() { return permisSpecifiquesPrecision; }
    public void setPermisSpecifiquesPrecision(String v) { this.permisSpecifiquesPrecision = v; }
    public Boolean getRisquesEnvironnement() { return risquesEnvironnement; }
    public void setRisquesEnvironnement(Boolean v) { this.risquesEnvironnement = v; }
    public Boolean getRisquesTache() { return risquesTache; }
    public void setRisquesTache(Boolean v) { this.risquesTache = v; }
    public Boolean getProtectionsCollectives() { return protectionsCollectives; }
    public void setProtectionsCollectives(Boolean v) { this.protectionsCollectives = v; }
    public Boolean getEpiSpecifiques() { return epiSpecifiques; }
    public void setEpiSpecifiques(Boolean v) { this.epiSpecifiques = v; }
    public Boolean getMoyensAlerte() { return moyensAlerte; }
    public void setMoyensAlerte(Boolean v) { this.moyensAlerte = v; }
    public Boolean getSecours() { return secours; }
    public void setSecours(Boolean v) { this.secours = v; }
    public String getNomSST() { return nomSST; }
    public void setNomSST(String nomSST) { this.nomSST = nomSST; }
    public Boolean getEvacuation() { return evacuation; }
    public void setEvacuation(Boolean v) { this.evacuation = v; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime v) { this.dateCreation = v; }
    public Utilisateur getCreePar() { return creePar; }
    public void setCreePar(Utilisateur creePar) { this.creePar = creePar; }
}