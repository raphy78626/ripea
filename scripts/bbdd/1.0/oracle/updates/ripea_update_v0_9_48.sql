-----------------------------------------------------------------
-- #159 Distribució asíncrona
-----------------------------------------------------------------

ALTER TABLE IPA_REGISTRE ADD DATA_DIST_ASINC TIMESTAMP(6);
ALTER TABLE IPA_REGISTRE ADD REINTENTS_DIST_ASINC NUMBER(4);

UPDATE IPA_REGISTRE SET DATA_DIST_ASINC = DATA WHERE DATA IS NOT NULL;
UPDATE IPA_REGISTRE SET REINTENTS_DIST_ASINC = 0;
