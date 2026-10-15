-- Commentaire libre saisi sur un projet (section "Informations Principales"
-- de Gestion des Projets). Nullable : les projets existants n'en ont pas.
ALTER TABLE `taches` ADD COLUMN `commentaire` TEXT DEFAULT NULL;
