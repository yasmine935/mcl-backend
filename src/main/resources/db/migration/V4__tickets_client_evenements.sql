-- Timeline des tickets client : historique des statuts + commentaires/échanges.
CREATE TABLE `tickets_client_evenements` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ticket_id` bigint(20) DEFAULT NULL,
  `auteur_id` bigint(20) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `message` text DEFAULT NULL,
  `ancien_statut` varchar(255) DEFAULT NULL,
  `nouveau_statut` varchar(255) DEFAULT NULL,
  `date_creation` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_tce_ticket` (`ticket_id`),
  CONSTRAINT `fk_tce_ticket` FOREIGN KEY (`ticket_id`) REFERENCES `tickets_client` (`id`),
  CONSTRAINT `fk_tce_auteur` FOREIGN KEY (`auteur_id`) REFERENCES `utilisateurs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
