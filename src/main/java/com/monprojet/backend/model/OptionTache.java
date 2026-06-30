package com.monprojet.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "options_taches")
public class OptionTache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private CategorieTache categorie;

    private String nom;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public CategorieTache getCategorie() { return categorie; }
    public void setCategorie(CategorieTache categorie) { this.categorie = categorie; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}
