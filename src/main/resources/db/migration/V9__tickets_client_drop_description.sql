-- Retire "description" (champ generique), redondant avec "description_panne"
-- (bloc Nature de la Panne) qui joue desormais ce role seul.
ALTER TABLE `tickets_client` DROP COLUMN `description`;
