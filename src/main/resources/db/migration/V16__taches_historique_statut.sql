-- Historique des changements de statut d'un projet (JSON : [{ancien, statut, at, par}]),
-- alimenté par le serveur à chaque changement — trace affichée dans « Activité récente ».
-- Les étapes d'avancement (colonne etapes) sont désormais synchronisées sur le statut.
ALTER TABLE `taches` ADD COLUMN `historique_statut` TEXT DEFAULT NULL;
