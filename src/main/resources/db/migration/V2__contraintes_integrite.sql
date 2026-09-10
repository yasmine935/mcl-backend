-- Contraintes d'intégrité absentes du schéma généré par Hibernate.
-- NB : sur une base existante contenant déjà des données, la contrainte UNIQUE
-- échouera s'il y a des doublons de username — les dédoublonner au préalable.

-- Un nom d'utilisateur doit être unique (empêche deux comptes homonymes,
-- rend findByUsername déterministe).
ALTER TABLE utilisateurs
    ADD CONSTRAINT uk_utilisateurs_username UNIQUE (username);

-- Index sur les colonnes de statut, très filtrées par l'application.
CREATE INDEX idx_conges_statut ON conges (statut);
CREATE INDEX idx_tickets_statut ON tickets (statut);
CREATE INDEX idx_reclamations_statut ON reclamations_sse (statut);
CREATE INDEX idx_commandes_statut ON commandes (statut);

-- Index sur les clés étrangères fréquemment jointes / filtrées.
CREATE INDEX idx_conges_utilisateur ON conges (utilisateur_id);
