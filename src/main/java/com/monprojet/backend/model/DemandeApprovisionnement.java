package com.monprojet.backend.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "demandes_approvisionnement")
public class DemandeApprovisionnement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private String dateCreation;
    private String demandeur;
    private Long demandeurId;
    private String dateAttendue;
    private String department;
    private String nomProjet;
    private String codeAffaire;
    private String responsableProjet;
    private String commandeClientRecue;
    private String montantTotal;
    private String quotationsObtenues;
    private String devisFileJson;
    private String commentaires;
    private String statut;
    private String traitePar;
    private String dateTraitement;

    @Column(columnDefinition = "LONGTEXT")
    private String lignesJson;

    private LocalDateTime dateSoumission;

    @Transient
    private List<Map<String, Object>> lignes;

    @Transient
    private Map<String, String> devisFile;

    @PrePersist
    @PreUpdate
    public void convertToJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (lignes != null) lignesJson = mapper.writeValueAsString(lignes);
            if (devisFile != null) devisFileJson = mapper.writeValueAsString(devisFile);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @PostLoad
    public void convertFromJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (lignesJson != null && !lignesJson.isEmpty())
                lignes = mapper.readValue(lignesJson, new TypeReference<List<Map<String, Object>>>(){});
            else lignes = new ArrayList<>();
            if (devisFileJson != null && !devisFileJson.isEmpty())
                devisFile = mapper.readValue(devisFileJson, new TypeReference<Map<String, String>>(){});
        } catch (Exception e) { lignes = new ArrayList<>(); }
    }

    // ── Getters & Setters ──
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getDateCreation() { return dateCreation; }
    public void setDateCreation(String dateCreation) { this.dateCreation = dateCreation; }
    public String getDemandeur() { return demandeur; }
    public void setDemandeur(String demandeur) { this.demandeur = demandeur; }
    public Long getDemandeurId() { return demandeurId; }
    public void setDemandeurId(Long demandeurId) { this.demandeurId = demandeurId; }
    public String getDateAttendue() { return dateAttendue; }
    public void setDateAttendue(String dateAttendue) { this.dateAttendue = dateAttendue; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getNomProjet() { return nomProjet; }
    public void setNomProjet(String nomProjet) { this.nomProjet = nomProjet; }
    public String getCodeAffaire() { return codeAffaire; }
    public void setCodeAffaire(String codeAffaire) { this.codeAffaire = codeAffaire; }
    public String getResponsableProjet() { return responsableProjet; }
    public void setResponsableProjet(String responsableProjet) { this.responsableProjet = responsableProjet; }
    public String getCommandeClientRecue() { return commandeClientRecue; }
    public void setCommandeClientRecue(String commandeClientRecue) { this.commandeClientRecue = commandeClientRecue; }
    public String getMontantTotal() { return montantTotal; }
    public void setMontantTotal(String montantTotal) { this.montantTotal = montantTotal; }
    public String getQuotationsObtenues() { return quotationsObtenues; }
    public void setQuotationsObtenues(String quotationsObtenues) { this.quotationsObtenues = quotationsObtenues; }
    public String getCommentaires() { return commentaires; }
    public void setCommentaires(String commentaires) { this.commentaires = commentaires; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getTraitePar() { return traitePar; }
    public void setTraitePar(String traitePar) { this.traitePar = traitePar; }
    public String getDateTraitement() { return dateTraitement; }
    public void setDateTraitement(String dateTraitement) { this.dateTraitement = dateTraitement; }
    public LocalDateTime getDateSoumission() { return dateSoumission; }
    public void setDateSoumission(LocalDateTime dateSoumission) { this.dateSoumission = dateSoumission; }
    public List<Map<String, Object>> getLignes() { return lignes; }
    public void setLignes(List<Map<String, Object>> lignes) { this.lignes = lignes; }
    public Map<String, String> getDevisFile() { return devisFile; }
    public void setDevisFile(Map<String, String> devisFile) { this.devisFile = devisFile; }
    public String getLignesJson() { return lignesJson; }
    public void setLignesJson(String lignesJson) { this.lignesJson = lignesJson; }
    public String getDevisFileJson() { return devisFileJson; }
    public void setDevisFileJson(String devisFileJson) { this.devisFileJson = devisFileJson; }
}
