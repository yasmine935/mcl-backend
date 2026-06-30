package com.monprojet.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "conges")
public class Conge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String type;
    private String motif;
    private String statut = "EN_ATTENTE"; // EN_ATTENTE, VALIDE_KIA, APPROUVE, REFUSE
    private Boolean valideParKia = false; // trace: reste true même après la décision finale de Ferid
    private String periode; // null = journée complète, MATIN ou APRES_MIDI = demi-journée

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public Boolean getValideParKia() { return valideParKia; }
    public void setValideParKia(Boolean valideParKia) { this.valideParKia = valideParKia; }
    public String getPeriode() { return periode; }
    public void setPeriode(String periode) { this.periode = periode; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
}