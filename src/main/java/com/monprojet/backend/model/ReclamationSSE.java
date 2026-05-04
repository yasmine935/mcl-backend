package com.monprojet.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    private String gravite;

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

    // Photos stockées en base
    @Column(columnDefinition = "LONGTEXT")
    private String photosJson;

    @Transient
    private List<Map<String, String>> photos;

    private String statut;

    private LocalDateTime dateCreation;

    @ManyToOne
    @JoinColumn(name = "technicien_id")
    private Utilisateur technicien;

    // ══ Conversion automatique photos ↔ JSON ══

    @PrePersist
    @PreUpdate
    public void convertPhotosToJson() {
        if (photos != null && !photos.isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                this.photosJson = mapper.writeValueAsString(photos);
            } catch (Exception e) {
                this.photosJson = "[]";
            }
        }
    }

    @PostLoad
    public void convertJsonToPhotos() {
        if (photosJson != null && !photosJson.isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                this.photos = mapper.readValue(photosJson,
                        new TypeReference<List<Map<String, String>>>(){});
            } catch (Exception e) {
                this.photos = new ArrayList<>();
            }
        } else {
            this.photos = new ArrayList<>();
        }
    }

    // ══ Getters / Setters ══

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

    public String getPhotosJson() { return photosJson; }
    public void setPhotosJson(String photosJson) { this.photosJson = photosJson; }

    public List<Map<String, String>> getPhotos() { return photos; }
    public void setPhotos(List<Map<String, String>> photos) { this.photos = photos; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime v) { this.dateCreation = v; }

    public Utilisateur getTechnicien() { return technicien; }
    public void setTechnicien(Utilisateur technicien) { this.technicien = technicien; }
}
