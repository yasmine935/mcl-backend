package com.monprojet.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages_aby")
public class MessageAby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String expediteur;
    private String destinataire;
    private String sujet;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    private LocalDateTime dateEnvoi;
    private boolean lu = false;

    @Column(columnDefinition = "TEXT")
    private String reponse;

    private LocalDateTime dateReponse;
    private String repondantNom;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getExpediteur() { return expediteur; }
    public void setExpediteur(String expediteur) { this.expediteur = expediteur; }
    public String getDestinataire() { return destinataire; }
    public void setDestinataire(String destinataire) { this.destinataire = destinataire; }
    public String getSujet() { return sujet; }
    public void setSujet(String sujet) { this.sujet = sujet; }
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }
    public boolean isLu() { return lu; }
    public void setLu(boolean lu) { this.lu = lu; }
    public String getReponse() { return reponse; }
    public void setReponse(String reponse) { this.reponse = reponse; }
    public LocalDateTime getDateReponse() { return dateReponse; }
    public void setDateReponse(LocalDateTime dateReponse) { this.dateReponse = dateReponse; }
    public String getRepondantNom() { return repondantNom; }
    public void setRepondantNom(String repondantNom) { this.repondantNom = repondantNom; }
}
