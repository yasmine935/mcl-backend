-- Ajoute la date de facturation et le numero de facture, saisis par le
-- manager lors de la validation finale d'une fiche d'intervention
-- (remplace le bouton "Valider" par "A facturer").
ALTER TABLE `fiches_intervention` ADD COLUMN `date_facturation` varchar(255) DEFAULT NULL;
ALTER TABLE `fiches_intervention` ADD COLUMN `numero_facture` varchar(255) DEFAULT NULL;
