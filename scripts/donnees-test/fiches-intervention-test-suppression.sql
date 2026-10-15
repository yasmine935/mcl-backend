-- Supprime les fiches d'intervention de TEST créées par fiches-intervention-test.sql
-- (reconnues à leur description « [TEST] … »). Base LOCALE uniquement.
DELETE FROM fiches_intervention WHERE description LIKE '[TEST]%';
