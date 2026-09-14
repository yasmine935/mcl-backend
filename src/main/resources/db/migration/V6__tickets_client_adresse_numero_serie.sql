-- Ajoute l'adresse du site et le numéro de série du matériel sur les tickets
-- clients (formulaire "Créer un nouveau ticket" du dashboard client).
-- adresse_site : obligatoire côté formulaire, mais colonne nullable pour ne
-- pas casser les tickets déjà existants créés avant ce champ.
ALTER TABLE `tickets_client` ADD COLUMN `adresse_site` varchar(255) DEFAULT NULL;
ALTER TABLE `tickets_client` ADD COLUMN `numero_serie` varchar(255) DEFAULT NULL;
