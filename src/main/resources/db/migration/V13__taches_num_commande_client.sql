-- Numéro de commande du client (rubrique "Commande" de la fiche projet),
-- distinct du N° Commande interne. Nullable : les projets existants n'en ont pas.
ALTER TABLE `taches` ADD COLUMN `num_commande_client` varchar(255) DEFAULT NULL;
