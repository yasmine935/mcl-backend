-- Ajoute le pourcentage d'avancement (0-100) saisi manuellement sur un
-- projet, affiché dans la liste "Gestion des Projets" à la place de la
-- priorité. Nullable pour ne pas casser les projets déjà existants ;
-- traité comme 0 côté application quand absent.
ALTER TABLE `taches` ADD COLUMN `avancement` int DEFAULT NULL;
