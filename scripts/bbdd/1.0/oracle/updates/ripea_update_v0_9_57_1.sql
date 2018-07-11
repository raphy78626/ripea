-----------------------------------------------------------------
-- 	#155: Poder ordenar bústies per nom d'unitat organitzat
-----------------------------------------------------------------
-----------------------------------------------------------------
-- 	#165: Gestió i Sincronització de UO
-----------------------------------------------------------------

CREATE TABLE IPA_UNITAT_ORGANITZATIVA (
  ID           				NUMBER(19)          NOT NULL,
  CODI 						VARCHAR2(9) 		NOT NULL,
  DENOMINACIO 				VARCHAR2(300) 		NOT NULL,
  NIF_CIF 					VARCHAR2(9),
  CODI_UNITAT_SUPERIOR 		VARCHAR2(9),
  CODI_UNITAT_ARREL 		VARCHAR2(9),
  DATA_CREACIO_OFICIAL 		TIMESTAMP(6),
  DATA_SUPRESSIO_OFICIAL 	TIMESTAMP(6),
  DATA_EXTINCIO_FUNCIONAL 	TIMESTAMP(6),
  DATA_ANULACIO 	 		TIMESTAMP(6),
  ESTAT 					VARCHAR2(1),
  CODI_PAIS 				VARCHAR2(3),
  CODI_COMUNITAT 			VARCHAR2(2),
  CODI_PROVINCIA 			VARCHAR2(2),
  CODI_POSTAL 				VARCHAR2(5),
  NOM_LOCALITAT 			VARCHAR2(50),
  LOCALITAT 				VARCHAR2(40),
  ADRESSA 					VARCHAR2(70),
  TIPUS_VIA 	 			NUMBER(19),
  NOM_VIA 					VARCHAR2(200),
  NUM_VIA 					VARCHAR2(100),
  TIPUS_TRANSICIO 		    NUMBER(10),

  CREATEDDATE          		TIMESTAMP(6),
  CREATEDBY_CODI       		VARCHAR2(256),
  LASTMODIFIEDDATE     		TIMESTAMP(6),
  LASTMODIFIEDBY_CODI  		VARCHAR2(256)
);
ALTER TABLE IPA_UNITAT_ORGANITZATIVA ADD (
  CONSTRAINT IPA_UNITAT_ORGANITZATIVA_PK PRIMARY KEY (ID));
  
 
ALTER TABLE IPA_BUSTIA ADD UNITAT_ID NUMBER(19);
ALTER TABLE IPA_BUSTIA ADD (
  CONSTRAINT IPA_UNITAT_BUSTIA_FK FOREIGN KEY (UNITAT_ID) 
    REFERENCES IPA_UNITAT_ORGANITZATIVA (ID));
    
    
ALTER TABLE IPA_REGLA ADD UNITAT_ID NUMBER(19);
ALTER TABLE IPA_REGLA ADD (
  CONSTRAINT IPA_UNITAT_REGLA_FK FOREIGN KEY (UNITAT_ID) 
    REFERENCES IPA_UNITAT_ORGANITZATIVA (ID));
    
    
CREATE TABLE IPA_UO_SINC_REL (
  ANTIGA_UO         			NUMBER(19)          NOT NULL,
  NOVA_UO           			NUMBER(19)          NOT NULL
);
ALTER TABLE IPA_UO_SINC_REL ADD (
  CONSTRAINT IPA_UNITAT_ANTIGA_FK FOREIGN KEY (ANTIGA_UO) 
    REFERENCES IPA_UNITAT_ORGANITZATIVA (ID));
ALTER TABLE IPA_UO_SINC_REL ADD (
  CONSTRAINT IPA_UNITAT_NOVA_FK FOREIGN KEY (NOVA_UO) 
    REFERENCES IPA_UNITAT_ORGANITZATIVA (ID));
ALTER TABLE IPA_UO_SINC_REL ADD CONSTRAINT IPA_UO_SINC_REL_MULT_UK UNIQUE (ANTIGA_UO, NOVA_UO);


ALTER TABLE IPA_ENTITAT 
	ADD FECHA_ACTUALIZACION TIMESTAMP(6) 
	ADD FECHA_SINCRONIZACION TIMESTAMP(6);  
	