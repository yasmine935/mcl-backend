-- Supprime les techniciens de TEST créés par techniciens-test.sql (emails @test.local).
-- Base LOCALE uniquement. Échoue si des fiches/plannings y sont encore rattachés (clés étrangères).
DELETE FROM utilisateurs WHERE email LIKE '%@test.local' AND role = 'TECHNICIEN';
