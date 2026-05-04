package com.monprojet.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "articles_attente")
public class ArticleAttente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long utilisateurId;
    private String marque;
    private String designation;
    private String refFournisseur;
    private String nomFournisseur;
    private String refMCL;
    private Integer quantite;
    private String dateAjout;

    // ── Getters & Setters ──
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(Long utilisateurId) { this.utilisateurId = utilisateurId; }
    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public String getRefFournisseur() { return refFournisseur; }
    public void setRefFournisseur(String refFournisseur) { this.refFournisseur = refFournisseur; }
    public String getNomFournisseur() { return nomFournisseur; }
    public void setNomFournisseur(String nomFournisseur) { this.nomFournisseur = nomFournisseur; }
    public String getRefMCL() { return refMCL; }
    public void setRefMCL(String refMCL) { this.refMCL = refMCL; }
    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }
    public String getDateAjout() { return dateAjout; }
    public void setDateAjout(String dateAjout) { this.dateAjout = dateAjout; }
}
