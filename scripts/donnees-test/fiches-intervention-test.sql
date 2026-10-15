-- ─────────────────────────────────────────────────────────────────────────────
-- Données de TEST : 90 fiches d'intervention réparties sur les 12 derniers mois
-- (pour le graphique « Interventions » du dashboard admin : 1 / 3 / 6 / 12 mois).
--
-- ⚠ Base LOCALE uniquement — ce n'est PAS une migration Flyway.
-- Toutes les fiches créées ont une description commençant par « [TEST] ».
-- Suppression : voir fiches-intervention-test-suppression.sql
--
-- Exécution :
--   mariadb --host=127.0.0.1 --user=mcl --password=... --skip-ssl mon_projet < fiches-intervention-test.sql
-- ─────────────────────────────────────────────────────────────────────────────

INSERT INTO fiches_intervention
  (num_projet, client, adresse, contact, nom_contact_site, tel_contact_site,
   date_intervention, date_fin, date_creation, heure_debut, heure_fin,
   statut, description, taches, documents_importes,
   technicien_id, technicien_ids, technicien_noms, manager_id,
   confirme_par_kia, date_confirmation_kia, approuve_par, date_approbation,
   date_facturation, numero_facture)
WITH RECURSIVE seq AS (
  SELECT 1 AS n UNION ALL SELECT n + 1 FROM seq WHERE n < 90
),
base AS (
  -- Répartition pseudo-aléatoire (déterministe) sur 0..364 jours en arrière,
  -- avec un renfort sur le mois en cours pour la vue « 1 mois »
  -- et un lot de 11 à 45 jours (→ fiches « à facturer », statut VALIDEE_KIA).
  SELECT n,
         CASE WHEN n % 6 = 0 THEN (n * 7) % (DAYOFMONTH(CURDATE()))
              WHEN n % 7 = 0 THEN 11 + (n * 3) % 35
              ELSE (n * n * 13 + n * 5) % 365 END AS age
  FROM seq
)
SELECT
  CONCAT('Projet ', 1 + n % 8),
  ELT(1 + n % 8, 'THEO', 'Vega Industries', 'Mercure Systemes', 'Orion Technologies',
      'Canis Logistique', 'Lyra Consulting', 'Draco Batiment', 'Phenix Energie'),
  CONCAT(10 + n, ' rue de Test, 750', LPAD(1 + n % 20, 2, '0'), ' Paris'),
  'Contact Test',
  CONCAT('Contact site ', n),
  CONCAT('06 00 00 ', LPAD(n, 2, '0'), ' 00'),
  DATE_SUB(CURDATE(), INTERVAL age DAY),
  DATE_SUB(CURDATE(), INTERVAL age DAY),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL age + 2 DAY), '09:00:00'),
  ELT(1 + n % 3, '08:30', '10:00', '14:00'),
  ELT(1 + n % 3, '12:00', '12:30', '17:30'),
  -- Statut cohérent avec l'ancienneté
  CASE
    WHEN age > 60 THEN 'VALIDEE'
    -- Confirmées par le Technicien Supérieur → en attente de facturation
    WHEN age > 10 THEN IF(n % 4 = 0, 'COMPLETEE', 'VALIDEE_KIA')
    ELSE IF(n % 3 = 0, 'COMPLETEE', 'EN_COURS')
  END,
  CONCAT('[TEST] ', ELT(1 + n % 4, 'Installation poste de travail', 'Maintenance préventive',
                         'Dépannage réseau', 'Câblage baie')),
  '[]',
  '[]',
  10, '10', '["Demo Technicien"]', 7,
  IF(age > 60 OR (age > 10 AND n % 4 <> 0), 'Demo Kia', NULL),
  IF(age > 60 OR (age > 10 AND n % 4 <> 0), DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL age - 1 DAY), '%Y-%m-%dT10:00:00'), NULL),
  IF(age > 60, 'Demo Aurelien', NULL),
  IF(age > 60, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL age - 3 DAY), '%Y-%m-%dT10:00:00'), NULL),
  IF(age > 60, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL age - 3 DAY), '%Y-%m-%d'), NULL),
  IF(age > 60, CONCAT('FAC-TEST-', LPAD(n, 4, '0')), NULL)
FROM base;

-- Contrôle : nombre de fiches de test par mois
SELECT DATE_FORMAT(date_intervention, '%Y-%m') AS mois, COUNT(*) AS nb
FROM fiches_intervention
WHERE description LIKE '[TEST]%'
GROUP BY mois ORDER BY mois;
