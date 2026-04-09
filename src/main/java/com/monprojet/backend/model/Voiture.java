package com.monprojet.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "voitures")
public class Voiture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String immatriculation;
    private String marque;
    private String modele;
    private String annee;
    private String kilometrage;
    private String statut; // Disponible, En service, En maintenance, Hors service
    private String conducteur;
    private LocalDate prochainControle;
    private String assurance;
    private LocalDate dateExpirationAssurance;
    private String carburant; // Essence, Diesel, Electrique, Hybride

    // GETTERS & SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImmatriculation() { return immatriculation; }
    public void setImmatriculation(String immatriculation) { this.immatriculation = immatriculation; }

    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }

    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }

    public String getAnnee() { return annee; }
    public void setAnnee(String annee) { this.annee = annee; }

    public String getKilometrage() { return kilometrage; }
    public void setKilometrage(String kilometrage) { this.kilometrage = kilometrage; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getConducteur() { return conducteur; }
    public void setConducteur(String conducteur) { this.conducteur = conducteur; }

    public LocalDate getProchainControle() { return prochainControle; }
    public void setProchainControle(LocalDate prochainControle) { this.prochainControle = prochainControle; }

    public String getAssurance() { return assurance; }
    public void setAssurance(String assurance) { this.assurance = assurance; }

    public LocalDate getDateExpirationAssurance() { return dateExpirationAssurance; }
    public void setDateExpirationAssurance(LocalDate dateExpirationAssurance) { this.dateExpirationAssurance = dateExpirationAssurance; }

    public String getCarburant() { return carburant; }
    public void setCarburant(String carburant) { this.carburant = carburant; }
}