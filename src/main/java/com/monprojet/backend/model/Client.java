package com.monprojet.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String codeClient;
    private String adresse;
    private String contact;
    private String email;
    private Boolean actif = true;
    private String desactivePar;
    private String dateDesactivation;
    private String responsableCommercial;
    private String typeClient = "Client";
    private String interlocuteurAchat;
    private String interlocuteurFacture;
    private String interlocuteurCommercial;
    private String interlocuteurAutres;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getCodeClient() { return codeClient; }
    public void setCodeClient(String codeClient) { this.codeClient = codeClient; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
    public String getDesactivePar() { return desactivePar; }
    public void setDesactivePar(String desactivePar) { this.desactivePar = desactivePar; }
    public String getDateDesactivation() { return dateDesactivation; }
    public void setDateDesactivation(String dateDesactivation) { this.dateDesactivation = dateDesactivation; }
    public String getResponsableCommercial() { return responsableCommercial; }
    public void setResponsableCommercial(String responsableCommercial) { this.responsableCommercial = responsableCommercial; }
    public String getTypeClient() { return typeClient; }
    public void setTypeClient(String typeClient) { this.typeClient = typeClient; }
    public String getInterlocuteurAchat() { return interlocuteurAchat; }
    public void setInterlocuteurAchat(String v) { this.interlocuteurAchat = v; }
    public String getInterlocuteurFacture() { return interlocuteurFacture; }
    public void setInterlocuteurFacture(String v) { this.interlocuteurFacture = v; }
    public String getInterlocuteurCommercial() { return interlocuteurCommercial; }
    public void setInterlocuteurCommercial(String v) { this.interlocuteurCommercial = v; }
    public String getInterlocuteurAutres() { return interlocuteurAutres; }
    public void setInterlocuteurAutres(String v) { this.interlocuteurAutres = v; }
}
