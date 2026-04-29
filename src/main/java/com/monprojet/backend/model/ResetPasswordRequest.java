package com.monprojet.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "reset_password_requests")
public class ResetPasswordRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String statut;
    private String datedemande;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getDatedemande() { return datedemande; }
    public void setDatedemande(String datedemande) { this.datedemande = datedemande; }
}
