-- Lien réel fiche d'intervention → projet (table taches), à la place du simple
-- nom saisi dans num_projet (texte libre : fautes de frappe, renommages, doublons).
-- num_projet est conservé comme libellé affiché, tenu à jour par l'application.

ALTER TABLE `fiches_intervention` ADD COLUMN `projet_id` bigint(20) DEFAULT NULL;

-- Suppression d'un projet : ses fiches ne sont pas supprimées, elles redeviennent sans projet.
ALTER TABLE `fiches_intervention`
  ADD CONSTRAINT `fk_fiches_intervention_projet`
  FOREIGN KEY (`projet_id`) REFERENCES `taches` (`id`) ON DELETE SET NULL;

-- Reprise de l'existant : rattachement par le nom, uniquement quand ce nom désigne
-- un seul projet (collation insensible à la casse). Les fiches dont le nom ne
-- correspond à aucun projet — ou à plusieurs — gardent projet_id = NULL
-- (« Projet introuvable » à l'écran, cf. scripts/diagnostic/fiches-orphelines.sql).
UPDATE `fiches_intervention` f
JOIN (
  SELECT MIN(`id`) AS `id`, `titre`
  FROM `taches`
  WHERE `titre` IS NOT NULL AND TRIM(`titre`) <> ''
  GROUP BY `titre`
  HAVING COUNT(*) = 1
) t ON t.`titre` = f.`num_projet`
SET f.`projet_id` = t.`id`;
