-----------------------------------------------------------------
-- #159 Distribució asíncrona
-----------------------------------------------------------------
ALTER TABLE IPA_REGISTRE ADD DATA_DIST_ASINC timestamp without time zone;
ALTER TABLE IPA_REGISTRE ADD REINTENTS_DIST_ASINC INTEGER;
UPDATE IPA_REGISTRE SET DATA_DIST_ASINC = DATA WHERE DATA IS NOT NULL;
UPDATE IPA_REGISTRE SET REINTENTS_DIST_ASINC = 0;