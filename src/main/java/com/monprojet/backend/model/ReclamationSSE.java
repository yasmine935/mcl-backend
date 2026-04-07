package com.monprojet.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reclamations_sse")
public class ReclamationSSE {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;

    // Type de signalement
    private Boolean typeSituationDangereuse;
    private Boolean typePresquAccident;
    private Boolean typeSuggestion;

    // Description
    private String date;
    private String heure;
    private String lieu;
    private String entrepriseConcernee;

    @Column(columnDefinition = "TEXT")
    private String descriptionFaits;

    // Gravité
    private String gravite; // Faible, Moyen, Grave

    @Column(columnDefinition = "TEXT")
    private String mesureImmediate;

    // Partie encadrement
    @Column(columnDefinition = "TEXT")
    private String analyseCauses;

    @Column(columnDefinition = "TEXT")
    private String actionCorrective;

    private String responsableAction;
    private String echeance;
    private Boolean informationDeclarant;

    private String statut; // EN_ATTENTE, EN_COURS, TRAITEE

    private LocalDateTime dateCreation;

    @ManyToOne
    @JoinColumn(name = "technicien_id")
    private Utilisateur technicien;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public Boolean getTypeSituationDangereuse() { return typeSituationDangereuse; }
    public void setTypeSituationDangereuse(Boolean v) { this.typeSituationDangereuse = v; }
    public Boolean getTypePresquAccident() { return typePresquAccident; }
    public void setTypePresquAccident(Boolean v) { this.typePresquAccident = v; }
    public Boolean getTypeSuggestion() { return typeSuggestion; }
    public void setTypeSuggestion(Boolean v) { this.typeSuggestion = v; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getHeure() { return heure; }
    public void setHeure(String heure) { this.heure = heure; }
    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }
    public String getEntrepriseConcernee() { return entrepriseConcernee; }
    public void setEntrepriseConcernee(String v) { this.entrepriseConcernee = v; }
    public String getDescriptionFaits() { return descriptionFaits; }
    public void setDescriptionFaits(String v) { this.descriptionFaits = v; }
    public String getGravite() { return gravite; }
    public void setGravite(String gravite) { this.gravite = gravite; }
    public String getMesureImmediate() { return mesureImmediate; }
    public void setMesureImmediate(String v) { this.mesureImmediate = v; }
    public String getAnalyseCauses() { return analyseCauses; }
    public void setAnalyseCauses(String v) { this.analyseCauses = v; }
    public String getActionCorrective() { return actionCorrective; }
    public void setActionCorrective(String v) { this.actionCorrective = v; }
    public String getResponsableAction() { return responsableAction; }
    public void setResponsableAction(String v) { this.responsableAction = v; }
    public String getEcheance() { return echeance; }
    public void setEcheance(String echeance) { this.echeance = echeance; }
    public Boolean getInformationDeclarant() { return informationDeclarant; }
    public void setInformationDeclarant(Boolean v) { this.informationDeclarant = v; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime v) { this.dateCreation = v; }
    public Utilisateur getTechnicien() { return technicien; }
    public void setTechnicien(Utilisateur technicien) { this.technicien = technicien; }
}