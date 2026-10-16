-- Rubrique "Facturation" de la fiche d'intervention : montant facturé et
-- référence Sage (en plus de date_facturation / numero_facture, cf. V11).
-- Nullable : les fiches existantes n'en ont pas.
ALTER TABLE `fiches_intervention` ADD COLUMN `montant_facture` varchar(255) DEFAULT NULL;
ALTER TABLE `fiches_intervention` ADD COLUMN `ref_sage` varchar(255) DEFAULT NULL;
