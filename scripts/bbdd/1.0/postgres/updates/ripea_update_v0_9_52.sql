-----------------------------------------------------------------
-- #159 Distribució asíncrona
-----------------------------------------------------------------

ALTER TABLE IPA_REGISTRE_ANNEX ADD GESDOC_DOC_ID character varying(50);
ALTER TABLE IPA_REGISTRE_ANNEX_FIRMA ADD GESDOC_FIR_ID character varying(50);

UPDATE IPA_REGISTRE SET PROCES_ESTAT = PROCESSAT WHERE PROCES_ESTAT = PENDENT;
UPDATE IPA_REGISTRE SET PROCES_INTENTS = 50;
UPDATE IPA_REGISTRE SET PROCES_DATA = DATA WHERE PROCES_DATA IS NULL;