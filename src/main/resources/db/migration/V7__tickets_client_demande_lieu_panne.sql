-- Étend le ticket client avec les 3 blocs du formulaire "Créer un nouveau
-- ticket" : Demande d'intervention (coordonnées du demandeur), Lieu
-- d'intervention, Nature de la panne. Toutes les colonnes sont nullable car
-- les tickets déjà existants n'ont pas ces informations.

-- Demande d'intervention (coordonnées du demandeur, distinctes du compte
-- client qui peut être partagé par plusieurs personnes).
ALTER TABLE `tickets_client` ADD COLUMN `nom_demandeur` varchar(255) DEFAULT NULL;
ALTER TABLE `tickets_client` ADD COLUMN `prenom_demandeur` varchar(255) DEFAULT NULL;
ALTER TABLE `tickets_client` ADD COLUMN `telephone_demandeur` varchar(255) DEFAULT NULL;
ALTER TABLE `tickets_client` ADD COLUMN `email_demandeur` varchar(255) DEFAULT NULL;

-- Lieu d'intervention
ALTER TABLE `tickets_client` ADD COLUMN `lieu_site` varchar(255) DEFAULT NULL;
ALTER TABLE `tickets_client` ADD COLUMN `nom_salle` varchar(255) DEFAULT NULL;
ALTER TABLE `tickets_client` ADD COLUMN `etage` varchar(255) DEFAULT NULL;
ALTER TABLE `tickets_client` ADD COLUMN `informations_additionnelles` text DEFAULT NULL;

-- Nature de la panne
ALTER TABLE `tickets_client` ADD COLUMN `type_materiel` varchar(255) DEFAULT NULL;
ALTER TABLE `tickets_client` ADD COLUMN `marque` varchar(255) DEFAULT NULL;
ALTER TABLE `tickets_client` ADD COLUMN `reference` varchar(255) DEFAULT NULL;
ALTER TABLE `tickets_client` ADD COLUMN `sous_garantie` bit(1) DEFAULT NULL;
ALTER TABLE `tickets_client` ADD COLUMN `criticite` varchar(255) DEFAULT NULL;
ALTER TABLE `tickets_client` ADD COLUMN `description_panne` text DEFAULT NULL;
