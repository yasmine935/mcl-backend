package com.monprojet.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categories_taches")
public class CategorieTache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}
