-- Retire "priorite", redondante avec "criticite" (les deux representaient la
-- meme notion de severite sur le ticket client, avec deux echelles differentes).
-- Criticite est desormais le seul champ de severite pour les tickets clients.
ALTER TABLE `tickets_client` DROP COLUMN `priorite`;
