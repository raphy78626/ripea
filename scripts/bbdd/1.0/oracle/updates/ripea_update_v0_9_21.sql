
------
--- #95 Mostrar data i oficina d'origen al detall de l'anotació
------
-- Modificació de la taula de registres per tenir informació la data i oficina d'origen
-- Data origen
ALTER TABLE IPA_REGISTRE ADD (DATA_ORIG TIMESTAMP(6));
-- Codi de l'oficina d'origen
ALTER TABLE IPA_REGISTRE ADD (OFICINA_ORIG_CODI VARCHAR2(21 CHAR));
-- Descripció de l'oficina d'origen
ALTER TABLE IPA_REGISTRE ADD (OFICINA_ORIG_DESC VARCHAR2(100 CHAR));


------
--- #97 Reordenar informació detall anotació
------
-- Afegeix un camp per guardar el nom de la unitat administrativa corresponent al codi dir3
ALTER TABLE IPA_REGISTRE ADD (UNITAT_ADM_DESC VARCHAR2(100 CHAR));
