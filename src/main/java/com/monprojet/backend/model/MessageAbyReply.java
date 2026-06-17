package com.monprojet.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages_aby_replies")
public class MessageAbyReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private MessageAby message;

    private String auteur;
    private String auteurRole; // "ABY" ou "MCL"

    @Column(columnDefinition = "TEXT")
    private String contenu;

    private LocalDateTime dateEnvoi;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public MessageAby getMessage() { return message; }
    public void setMessage(MessageAby message) { this.message = message; }
    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    public String getAuteurRole() { return auteurRole; }
    public void setAuteurRole(String auteurRole) { this.auteurRole = auteurRole; }
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }
}
