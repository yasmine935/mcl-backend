-- Ajoute le numéro de série du matériel sur le ticket (section "Nature de la
-- Panne" du formulaire de création). Champ optionnel : le numéro de série
-- n'est pas toujours connu/disponible au moment de la déclaration.
ALTER TABLE `tickets` ADD COLUMN `numero_serie` varchar(255) DEFAULT NULL;
