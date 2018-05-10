-----------------------------------------------------------------
-- 	#155: Poder ordenar bústies per nom d'unitat organitzat
-----------------------------------------------------------------

CREATE TABLE IPA_UNITAT_ORGANITZATIVA
(
  ID           				BIGINT              					NOT NULL,
  CODI 						character varying(9) 					NOT NULL,
  DENOMINACIO 				character varying(500) 					NOT NULL,
  NIF_CIF 					character varying(9),
  CODI_UNITAT_SUPERIOR 		character varying(9),
  CODI_UNITAT_ARREL 		character varying(9),
  DATA_CREACIO_OFICIAL 		timestamp without time zone,
  DATA_SUPRESSIO_OFICIAL 	timestamp without time zone,
  DATA_EXTINCIO_FUNCIONAL 	timestamp without time zone,
  DATA_ANULACIO 	 		timestamp without time zone,
  ESTAT 					character varying(1),
  CODI_PAIS 				character varying(3),
  CODI_COMUNITAT 			character varying(1),
  CODI_PROVINCIA 			character varying(1),
  CODI_POSTAL 				character varying(5),
  NOM_LOCALITAT 			character varying(50),
  LOCALITAT 				character varying(40),
  ADRESSA 					character varying(70),
  TIPUS_VIA 	 			bigint,
  NOM_VIA 					character varying(200),
  NUM_VIA 					character varying(100),
  OBSOLETE					boolean,

  CREATEDDATE          		timestamp without time zone,
  CREATEDBY_CODI       		character varying(256),
  LASTMODIFIEDDATE     		timestamp without time zone,
  LASTMODIFIEDBY_CODI  		character varying(256)
);

ALTER TABLE ONLY IPA_UNITAT_ORGANITZATIVA ADD CONSTRAINT IPA_UNITAT_ORGANITZATIVA_PK PRIMARY KEY (ID);

ALTER TABLE IPA_BUSTIA ADD COLUMN UNITAT_ID BIGINT;

ALTER TABLE IPA_BUSTIA ADD CONSTRAINT IPA_UNITAT_BUSTIA_FK FOREIGN KEY (UNITAT_ID) REFERENCES IPA_UNITAT_ORGANITZATIVA (ID);