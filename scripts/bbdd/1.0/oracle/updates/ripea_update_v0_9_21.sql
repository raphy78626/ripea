
------
--- #94 Canviar el tipus de la propietat numero de RegistreAnotacio a String. Corregir detall
------
-- Crea la columna auxiliar
-- Modificació del manteniment de regles
ALTER TABLE IPA_REGISTRE ADD NUMERO_temp VARCHAR2(100 CHAR);
-- Copia els valors a la nova columna
UPDATE IPA_REGISTRE SET NUMERO_temp = TO_CHAR(NUMERO);
-- Esborra la columna antiga
ALTER TABLE IPA_REGISTRE DROP COLUMN NUMERO;
-- Reanomena la columna auxiliar com a la columna vàlida
ALTER TABLE IPA_REGISTRE RENAME COLUMN NUMERO_temp TO NUMERO;
-- Afegeix la restricció de not null
ALTER TABLE IPA_REGISTRE MODIFY NUMERO NOT NULL;
