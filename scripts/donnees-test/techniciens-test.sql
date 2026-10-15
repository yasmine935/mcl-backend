-- ─────────────────────────────────────────────────────────────────────────────
-- Données de TEST : 5 techniciens supplémentaires (base LOCALE uniquement).
-- Mot de passe : celui du compte de démo « tech » (demo1234), recopié tel quel.
-- Emails en @test.local → suppression : voir techniciens-test-suppression.sql
-- ─────────────────────────────────────────────────────────────────────────────

INSERT INTO utilisateurs
  (username, prenom, nom, email, telephone, poste, departement, date_embauche,
   role, actif, premier_connexion, password)
SELECT t.username, t.prenom, t.nom, CONCAT(t.username, '@test.local'), t.telephone,
       'Technicien', t.departement, t.date_embauche,
       'TECHNICIEN', b'1', b'0',
       (SELECT password FROM utilisateurs WHERE username = 'tech')
FROM (
  SELECT 'lucas.bernard'  AS username, 'Lucas'  AS prenom, 'Bernard' AS nom, '06 11 22 33 01' AS telephone, 'Maintenance'   AS departement, DATE '2023-03-01' AS date_embauche
  UNION ALL SELECT 'emma.petit',     'Emma',   'Petit',   '06 11 22 33 02', 'Électricité',   DATE '2022-09-15'
  UNION ALL SELECT 'hugo.moreau',    'Hugo',   'Moreau',  '06 11 22 33 03', 'Climatisation', DATE '2024-01-08'
  UNION ALL SELECT 'lea.garcia',     'Léa',    'Garcia',  '06 11 22 33 04', 'Maintenance',   DATE '2021-06-21'
  UNION ALL SELECT 'nathan.roux',    'Nathan', 'Roux',    '06 11 22 33 05', 'Plomberie',     DATE '2025-02-03'
) t
WHERE NOT EXISTS (SELECT 1 FROM utilisateurs u WHERE u.username = t.username);

SELECT id, username, prenom, nom, departement, role FROM utilisateurs WHERE email LIKE '%@test.local';
