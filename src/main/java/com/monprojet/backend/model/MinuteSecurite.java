package com.monprojet.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "minutes_securite")
public class MinuteSecurite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tacheAEffectuer;

    // 5 questions OUI/NON
    private Boolean competencesHabilitations;
    private Boolean outilsEquipements;
    private Boolean environnementSecurise;
    private Boolean modeOperatoire;
    private Boolean saitQuoiFaireUrgence;

    // Risques et prevention
    @Column(columnDefinition = "TEXT")
    private String risquesSpecifiques;

    @Column(columnDefinition = "TEXT")
    private String mesurePreventionImmediate;

    // Validation
    private String nomSignataire;
    private String signature;
    private LocalDateTime dateCreation;

    // Lien technicien
    @ManyToOne
    @JoinColumn(name = "technicien_id")
    private Utilisateur technicien;

    private String statut; // SOUMIS, LU

    @PrePersist
    public void prePersist() {
        this.dateCreation = LocalDateTime.now();
        if (this.statut == null) this.statut = "SOUMIS";
    }

    // GETTERS & SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTacheAEffectuer() { return tacheAEffectuer; }
    public void setTacheAEffectuer(String tacheAEffectuer) { this.tacheAEffectuer = tacheAEffectuer; }

    public Boolean getCompetencesHabilitations() { return competencesHabilitations; }
    public void setCompetencesHabilitations(Boolean competencesHabilitations) { this.competencesHabilitations = competencesHabilitations; }

    public Boolean getOutilsEquipements() { return outilsEquipements; }
    public void setOutilsEquipements(Boolean outilsEquipements) { this.outilsEquipements = outilsEquipements; }

    public Boolean getEnvironnementSecurise() { return environnementSecurise; }
    public void setEnvironnementSecurise(Boolean environnementSecurise) { this.environnementSecurise = environnementSecurise; }

    public Boolean getModeOperatoire() { return modeOperatoire; }
    public void setModeOperatoire(Boolean modeOperatoire) { this.modeOperatoire = modeOperatoire; }

    public Boolean getSaitQuoiFaireUrgence() { return saitQuoiFaireUrgence; }
    public void setSaitQuoiFaireUrgence(Boolean saitQuoiFaireUrgence) { this.saitQuoiFaireUrgence = saitQuoiFaireUrgence; }

    public String getRisquesSpecifiques() { return risquesSpecifiques; }
    public void setRisquesSpecifiques(String risquesSpecifiques) { this.risquesSpecifiques = risquesSpecifiques; }

    public String getMesurePreventionImmediate() { return mesurePreventionImmediate; }
    public void setMesurePreventionImmediate(String mesurePreventionImmediate) { this.mesurePreventionImmediate = mesurePreventionImmediate; }

    public String getNomSignataire() { return nomSignataire; }
    public void setNomSignataire(String nomSignataire) { this.nomSignataire = nomSignataire; }

    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public Utilisateur getTechnicien() { return technicien; }
    public void setTechnicien(Utilisateur technicien) { this.technicien = technicien; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}