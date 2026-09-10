-- Table du service de ticketing client (cahier des charges Dashboard Client).
CREATE TABLE `tickets_client` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `numero` varchar(255) DEFAULT NULL,
  `titre` varchar(255) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `categorie` varchar(255) DEFAULT NULL,
  `priorite` varchar(255) DEFAULT NULL,
  `statut` varchar(255) DEFAULT NULL,
  `date_creation` datetime(6) DEFAULT NULL,
  `date_maj` datetime(6) DEFAULT NULL,
  `date_traitement` datetime(6) DEFAULT NULL,
  `commentaire_validation` text DEFAULT NULL,
  `client_id` bigint(20) DEFAULT NULL,
  `traite_par_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_tickets_client_client` (`client_id`),
  KEY `idx_tickets_client_statut` (`statut`),
  CONSTRAINT `fk_tc_client` FOREIGN KEY (`client_id`) REFERENCES `utilisateurs` (`id`),
  CONSTRAINT `fk_tc_traite_par` FOREIGN KEY (`traite_par_id`) REFERENCES `utilisateurs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
