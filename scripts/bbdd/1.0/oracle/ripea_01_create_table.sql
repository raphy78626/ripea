
CREATE TABLE IPA_USUARI
(
  CODI          VARCHAR2(64)                    NOT NULL,
  INICIALITZAT  NUMBER(1),
  NIF           VARCHAR2(9),
  NOM           VARCHAR2(200),
  EMAIL         VARCHAR2(200),
  VERSION       NUMBER(19)                      NOT NULL
);


CREATE TABLE IPA_ENTITAT
(
  ID                   NUMBER(19)               NOT NULL,
  CODI                 VARCHAR2(64)             NOT NULL,
  NOM                  VARCHAR2(256)            NOT NULL,
  DESCRIPCIO           VARCHAR2(1024),
  CIF                  VARCHAR2(9)              NOT NULL,
  UNITAT_ARREL         VARCHAR2(9)              NOT NULL,
  ACTIVA               NUMBER(1),
  VERSION              NUMBER(19)               NOT NULL,
  CREATEDBY_CODI       VARCHAR2(64),
  CREATEDDATE          TIMESTAMP(6),
  LASTMODIFIEDBY_CODI  VARCHAR2(64),
  LASTMODIFIEDDATE     TIMESTAMP(6)
);


CREATE TABLE IPA_METANODE
(
  ID                   NUMBER(19)               NOT NULL,
  CODI                 VARCHAR2(256)            NOT NULL,
  NOM                  VARCHAR2(256)            NOT NULL,
  DESCRIPCIO           VARCHAR2(1024),
  TIPUS                VARCHAR2(256)            NOT NULL,
  ENTITAT_ID           NUMBER(19)               NOT NULL,
  ACTIU                NUMBER(1),
  VERSION              NUMBER(19)               NOT NULL,
  CREATEDBY_CODI       VARCHAR2(64),
  CREATEDDATE          TIMESTAMP(6),
  LASTMODIFIEDBY_CODI  VARCHAR2(64),
  LASTMODIFIEDDATE     TIMESTAMP(6)
);


CREATE TABLE IPA_METADOCUMENT
(
  ID                      NUMBER(19)            NOT NULL,
  GLOBAL_EXPEDIENT        NUMBER(1),
  GLOBAL_MULTIPLICITAT    VARCHAR2(256),
  GLOBAL_READONLY         NUMBER(1),
  FIRMA_PFIRMA            NUMBER(1),
  PORTAFIRMES_DOCTIP      VARCHAR2(64),
  PORTAFIRMES_FLUXID      VARCHAR2(64),
  PORTAFIRMES_RESPONS     VARCHAR2(512),
  PORTAFIRMES_FLUXTIP     VARCHAR2(256),
  PORTAFIRMES_CUSTIP      VARCHAR2(64),
  FIRMA_PASSARELA         NUMBER(1),
  PASSARELA_CUSTIP        VARCHAR2(64),
  PLANTILLA_NOM           VARCHAR2(256),
  PLANTILLA_CONTENT_TYPE  VARCHAR2(256),
  PLANTILLA_CONTINGUT     BLOB
);


CREATE TABLE IPA_METADADA
(
  ID                    NUMBER(19)              NOT NULL,
  CREATEDDATE           TIMESTAMP(6),
  LASTMODIFIEDDATE      TIMESTAMP(6),
  ACTIVA                NUMBER(1),
  CODI                  VARCHAR2(64)            NOT NULL,
  DESCRIPCIO            VARCHAR2(1024),
  GLOBAL_CARPETA        NUMBER(1),
  GLOBAL_DOCUMENT       NUMBER(1),
  GLOBAL_EXPEDIENT      NUMBER(1),
  GLOBAL_MULTIPLICITAT  VARCHAR2(256),
  GLOBAL_READONLY       NUMBER(1),
  NOM                   VARCHAR2(256)           NOT NULL,
  TIPUS                 VARCHAR2(256)           NOT NULL,
  VERSION               NUMBER(19)              NOT NULL,
  CREATEDBY_CODI        VARCHAR2(64),
  LASTMODIFIEDBY_CODI   VARCHAR2(64),
  ENTITAT_ID            NUMBER(19)              NOT NULL
);


CREATE TABLE IPA_METAEXPEDIENT
(
  ID                 NUMBER(19)                 NOT NULL,
  PARE_ID            NUMBER(19),
  CLASIF_SIA         VARCHAR2(6)                NOT NULL,
  CLASIF_DOC         VARCHAR2(30)               NOT NULL,
  UNITAT_ADM         VARCHAR2(9),
  NOTIF_ACTIVA       NUMBER(1)                  NOT NULL,
  NOTIF_UNITAT_ADM   VARCHAR2(9),
  NOTIF_ORGAN_CODI   VARCHAR2(9),
  NOTIF_LLIBRE_CODI  VARCHAR2(4),
  NOTIF_AVIS_TITOL   VARCHAR2(256),
  NOTIF_AVIS_TEXT    VARCHAR2(1024),
  NOTIF_AVIS_TEXTSMS VARCHAR2(200),
  NOTIF_OFICI_TITOL  VARCHAR2(256),
  NOTIF_OFICI_TEXT   VARCHAR2(1024)
);


CREATE TABLE IPA_METAEXPEDIENT_ARXIU
(
  METAEXPEDIENT_ID  NUMBER(19)                  NOT NULL,
  ARXIU_ID          NUMBER(19)                  NOT NULL
);


CREATE TABLE IPA_METAEXP_SEQ
(
  ID                   NUMBER(19)               NOT NULL,
  ANIO                 NUMBER(10),
  VALOR                NUMBER(19),
  CREATEDDATE          TIMESTAMP(6),
  LASTMODIFIEDDATE     TIMESTAMP(6),
  CREATEDBY_CODI       VARCHAR2(64),
  LASTMODIFIEDBY_CODI  VARCHAR2(64),
  META_EXPEDIENT_ID    NUMBER(19)               NOT NULL
);


CREATE TABLE IPA_INTERESSAT
(
  ID                   NUMBER(19)               NOT NULL,
  DTYPE                VARCHAR2(256)            NOT NULL,
  NOM                  VARCHAR2(30)             NOT NULL,
  LLINATGE1            VARCHAR2(30),
  LLINATGE2            VARCHAR2(30),
  DOCUMENT_TIPUS       VARCHAR2(40)             NOT NULL,
  DOCUMENT_NUM         VARCHAR2(17)             NOT NULL,
  PAIS                 VARCHAR2(4),
  PROVINCIA            VARCHAR2(2),
  MUNICIPI             VARCHAR2(5),
  ADRESA               VARCHAR2(160),
  CODI_POSTAL          VARCHAR2(5),
  EMAIL                VARCHAR2(160),
  TELEFON              VARCHAR2(20),
  OBSERVACIONS         VARCHAR2(160),
  ORGAN_CODI           VARCHAR2(9),
  ORGAN_NOM		       VARCHAR2(80),
  RAO_SOCIAL           VARCHAR2(80),
  NOT_IDIOMA           VARCHAR2(2),
  NOT_AUTORITZAT       NUMBER(1)                NOT NULL,
  ES_REPRESENTANT      NUMBER(1)                NOT NULL,
  REPRESENTANT_ID      NUMBER(19),
  EXPEDIENT_ID         NUMBER(19)               NOT NULL,
  CREATEDDATE          TIMESTAMP(6),
  LASTMODIFIEDDATE     TIMESTAMP(6),
  CREATEDBY_CODI       VARCHAR2(64),
  LASTMODIFIEDBY_CODI  VARCHAR2(64),
  VERSION              NUMBER(19)               NOT NULL
);


CREATE TABLE IPA_ARXIU
(
  ID                   NUMBER(19)               NOT NULL,
  ACTIU                NUMBER(1),
  UNITAT_CODI          VARCHAR2(9)              NOT NULL
);


CREATE TABLE IPA_CONTINGUT
(
  ID                   NUMBER(19)               NOT NULL,
  NOM                  VARCHAR2(1024)           NOT NULL,
  TIPUS                NUMBER(10)               NOT NULL,
  PARE_ID              NUMBER(19),
  ESBORRAT             NUMBER(10),
  ARXIU_UUID           VARCHAR2(36),
  ARXIU_DATA_ACT       TIMESTAMP(6),
  CONTMOV_ID           NUMBER(19),
  ENTITAT_ID           NUMBER(19)               NOT NULL,
  CREATEDDATE          TIMESTAMP(6),
  LASTMODIFIEDDATE     TIMESTAMP(6),
  CREATEDBY_CODI       VARCHAR2(64),
  LASTMODIFIEDBY_CODI  VARCHAR2(64),
  VERSION              NUMBER(19)               NOT NULL
);


CREATE TABLE IPA_CONT_MOV
(
  ID                   NUMBER(19)               NOT NULL,
  CONTINGUT_ID         NUMBER(19)               NOT NULL,
  ORIGEN_ID            NUMBER(19),
  DESTI_ID             NUMBER(19)               NOT NULL,
  REMITENT_CODI        VARCHAR2(64),
  COMENTARI            VARCHAR2(256),
  CREATEDDATE          TIMESTAMP(6),
  LASTMODIFIEDDATE     TIMESTAMP(6),
  CREATEDBY_CODI       VARCHAR2(64),
  LASTMODIFIEDBY_CODI  VARCHAR2(64)
);


CREATE TABLE IPA_CONT_LOG
(
  ID                   NUMBER(19)               NOT NULL,
  TIPUS                NUMBER(10)               NOT NULL,
  CONTINGUT_ID         NUMBER(19)               NOT NULL,
  PARE_ID              NUMBER(19),
  CONTMOV_ID           NUMBER(19),
  OBJECTE_ID           VARCHAR2(256),
  OBJECTE_LOG_TIPUS    NUMBER(10),
  OBJECTE_TIPUS        NUMBER(10),
  PARAM1               VARCHAR2(256),
  PARAM2               VARCHAR2(256),
  CREATEDDATE          TIMESTAMP(6),
  LASTMODIFIEDDATE     TIMESTAMP(6),
  CREATEDBY_CODI       VARCHAR2(64),
  LASTMODIFIEDBY_CODI  VARCHAR2(64)
);


CREATE TABLE IPA_ESCRIPTORI
(
  ID         NUMBER(19)                         NOT NULL,
  USUARI_ID  VARCHAR2(64)                       NOT NULL
);


CREATE TABLE IPA_METAEXPEDIENT_METADOCUMENT
(
  ID                   NUMBER(19)               NOT NULL,
  CREATEDDATE          TIMESTAMP(6),
  LASTMODIFIEDDATE     TIMESTAMP(6),
  MULTIPLICITAT        VARCHAR2(256),
  ORDRE                NUMBER(10)               NOT NULL,
  CREATEDBY_CODI       VARCHAR2(64),
  LASTMODIFIEDBY_CODI  VARCHAR2(64),
  METADOCUMENT_ID      NUMBER(19)               NOT NULL,
  METAEXPEDIENT_ID     NUMBER(19)               NOT NULL,
  READONLY             NUMBER(1)
);


CREATE TABLE IPA_NODE
(
  ID                  NUMBER(19)                NOT NULL,
  METANODE_ID         NUMBER(19)
);


CREATE TABLE IPA_METANODE_METADADA
(
  ID                   NUMBER(19)               NOT NULL,
  CREATEDDATE          TIMESTAMP(6),
  LASTMODIFIEDDATE     TIMESTAMP(6),
  MULTIPLICITAT        VARCHAR2(256),
  ORDRE                NUMBER(10)               NOT NULL,
  CREATEDBY_CODI       VARCHAR2(64),
  LASTMODIFIEDBY_CODI  VARCHAR2(64),
  METADADA_ID          NUMBER(19)               NOT NULL,
  METANODE_ID          NUMBER(19)               NOT NULL,
  READONLY             NUMBER(1)
);


CREATE TABLE IPA_CARPETA
(
  ID     NUMBER(19)                             NOT NULL
);


CREATE TABLE IPA_DADA
(
  ID                   NUMBER(19)               NOT NULL,
  CREATEDDATE          TIMESTAMP(6),
  LASTMODIFIEDDATE     TIMESTAMP(6),
  ORDRE                NUMBER(10),
  VALOR                VARCHAR2(256)            NOT NULL,
  VERSION              NUMBER(19)               NOT NULL,
  CREATEDBY_CODI       VARCHAR2(64),
  LASTMODIFIEDBY_CODI  VARCHAR2(64),
  METADADA_ID          NUMBER(19)               NOT NULL,
  NODE_ID              NUMBER(19)               NOT NULL
);


CREATE TABLE IPA_EXPEDIENT
(
  ID                 NUMBER(19)                 NOT NULL,
  ESTAT              NUMBER(10)                 NOT NULL,
  TANCAT_DATA        TIMESTAMP(6),
  TANCAT_MOTIU       VARCHAR2(1024),
  ANIO               NUMBER(10)                 NOT NULL,
  SEQUENCIA          NUMBER(19)                 NOT NULL,
  ARXIU_ID           NUMBER(19)                 NOT NULL,
  NTI_VERSION        VARCHAR2(5)                NOT NULL,
  NTI_IDENTIF        VARCHAR2(52)               NOT NULL,
  NTI_ORGANO         VARCHAR2(9)                NOT NULL,
  NTI_FECHA_APE      TIMESTAMP(6)               NOT NULL,
  NTI_CLASIF_SIA     VARCHAR2(6)                NOT NULL,
  SISTRA_BANTEL_NUM  VARCHAR2(16),
  SISTRA_PUBLICAT    NUMBER(1),
  SISTRA_UNITAT_ADM  VARCHAR2(9),
  SISTRA_CLAU        VARCHAR2(100)
);


CREATE TABLE IPA_EXPEDIENT_REL
(
  EXPEDIENT_ID       NUMBER(19)                 NOT NULL,
  EXPEDIENT_REL_ID   NUMBER(19)                 NOT NULL
);


CREATE TABLE IPA_DOCUMENT
(
  ID                   NUMBER(19)               NOT NULL,
  TIPUS                NUMBER(10)               NOT NULL,
  ESTAT                NUMBER(10)               NOT NULL,
  UBICACIO             VARCHAR2(255),
  DATA                 TIMESTAMP(6)             NOT NULL,
  DATA_CAPTURA         TIMESTAMP(6)             NOT NULL,
  EXPEDIENT_ID         NUMBER(19),
  CUSTODIA_DATA        TIMESTAMP(6),
  CUSTODIA_ID          VARCHAR2(256),
  CUSTODIA_CSV         VARCHAR2(256),
  FITXER_NOM           VARCHAR2(256),
  FITXER_CONTENT_TYPE  VARCHAR2(256),
  FITXER_CONTINGUT     BLOB,
  VERSIO_DARRERA       VARCHAR2(32),
  VERSIO_COUNT         NUMBER(10)               NOT NULL,
  NTI_VERSION          VARCHAR2(5)              NOT NULL,
  NTI_IDENTIF          VARCHAR2(48)             NOT NULL,
  NTI_ORGANO           VARCHAR2(9)              NOT NULL,
  NTI_ORIGEN           VARCHAR2(2)              NOT NULL,
  NTI_ESTELA           VARCHAR2(4)              NOT NULL,
  NTI_TIPDOC           VARCHAR2(4)              NOT NULL,
  NTI_IDORIG           VARCHAR2(48),
  NTI_TIPFIR           VARCHAR2(4),
  NTI_CSV              VARCHAR2(256),
  NTI_CSVREG           VARCHAR2(512)
);


CREATE TABLE IPA_DOCUMENT_ENVIAMENT
(
  ID                   NUMBER(19)               NOT NULL,
  DTYPE                VARCHAR2(32)             NOT NULL,
  ASSUMPTE             VARCHAR2(256)            NOT NULL,
  ESTAT                VARCHAR2(255)            NOT NULL,
  DATA_ENVIAMENT       TIMESTAMP(6)             NOT NULL,
  OBSERVACIONS         VARCHAR2(256),
  DATA_PUBLICACIO      TIMESTAMP(6),
  TIPUS                VARCHAR2(255),
  DATA_RECEPCIO        TIMESTAMP(6),
  REGISTRE_NUM         VARCHAR2(100),
  DESTIN_DOCNUM        VARCHAR2(17),
  DESTIN_DOCTIP        NUMBER(10),
  DESTIN_EMAIL         VARCHAR2(160),
  DESTIN_LLING1        VARCHAR2(30),
  DESTIN_LLING2        VARCHAR2(30),
  DESTIN_NOM           VARCHAR2(30),
  DESTIN_PAICOD        VARCHAR2(4),
  DESTIN_PRVCOD        VARCHAR2(2),
  DESTIN_MUNCOD        VARCHAR2(5),
  DESTIN_REPRES        NUMBER(1),
  UNITAT_ADM           VARCHAR2(9),
  ORGAN_CODI           VARCHAR2(9),
  LLIBRE_CODI          VARCHAR2(4),
  AVIS_TITOL           VARCHAR2(256),
  AVIS_TEXT            VARCHAR2(1024),
  AVIS_TEXTSMS         VARCHAR2(200),
  OFICI_TITOL          VARCHAR2(256),
  OFICI_TEXT           VARCHAR2(1024),
  IDIOMA               VARCHAR2(2),
  PFIRMES_PRIORI       VARCHAR2(64),
  PFIRMES_DATCAD       TIMESTAMP(6),
  PFIRMES_DOCTIP       VARCHAR2(64),
  PFIRMES_RESPONSABLES VARCHAR2(1024),
  PFIRMES_FLUXTIP      VARCHAR2(64),
  PFIRMES_FLUXID       VARCHAR2(64),
  PFIRMES_ID           VARCHAR2(64),
  ENVIAM_DATA          TIMESTAMP(6),
  ENVIAM_COUNT         NUMBER(10),
  ENVIAM_ERROR         NUMBER(1),
  ENVIAM_ERROR_DESC    VARCHAR2(2048),
  PROCES_DATA          TIMESTAMP(6),
  PROCES_COUNT         NUMBER(10),
  PROCES_ERROR         NUMBER(1),
  PROCES_ERROR_DESC    VARCHAR2(2048),
  DOCUMENT_ID          NUMBER(19)               NOT NULL,
  EXPEDIENT_ID         NUMBER(19)               NOT NULL,
  CREATEDDATE          TIMESTAMP(6),
  LASTMODIFIEDDATE     TIMESTAMP(6),
  CREATEDBY_CODI       VARCHAR2(256),
  LASTMODIFIEDBY_CODI  VARCHAR2(256),
  VERSION              NUMBER(19)               NOT NULL
);


CREATE TABLE IPA_DOCUMENT_ENVIAMENT_DOC (
  DOCUMENT_ENVIAMENT_ID NUMBER(19)              NOT NULL,
  DOCUMENT_ID           NUMBER(19)              NOT NULL
);


CREATE TABLE IPA_EXPEDIENT_INTERESSAT
(
  EXPEDIENT_ID   NUMBER(19)                     NOT NULL,
  INTERESSAT_ID  NUMBER(19)                     NOT NULL
);


CREATE TABLE IPA_BUSTIA
(
  ID           NUMBER(19)                       NOT NULL,
  UNITAT_CODI  VARCHAR2(9)                      NOT NULL,
  PER_DEFECTE  NUMBER(1),
  ACTIVA       NUMBER(1)
);


CREATE TABLE IPA_REGISTRE
(
  ID                   NUMBER(19)               NOT NULL,
  TIPUS                VARCHAR2(1)              NOT NULL,
  UNITAT_ADM           VARCHAR2(21)             NOT NULL,
  NUMERO               NUMBER(10)               NOT NULL,
  DATA                 TIMESTAMP(6)             NOT NULL,
  IDENTIFICADOR        VARCHAR2(100)            NOT NULL,
  ENTITAT_CODI         VARCHAR2(21)             NOT NULL,
  ENTITAT_DESC         VARCHAR2(100),
  OFICINA_CODI         VARCHAR2(21)             NOT NULL,
  OFICINA_DESC         VARCHAR2(100),
  LLIBRE_CODI          VARCHAR2(4)              NOT NULL,
  LLIBRE_DESC          VARCHAR2(100),
  EXTRACTE             VARCHAR2(240),
  ASSUMPTE_TIPUS_CODI  VARCHAR2(16)             NOT NULL,
  ASSUMPTE_TIPUS_DESC  VARCHAR2(100),
  ASSUMPTE_CODI        VARCHAR2(16),
  ASSUMPTE_DESC        VARCHAR2(100),
  REFERENCIA           VARCHAR2(16),
  EXPEDIENT_NUM        VARCHAR2(80),
  IDIOMA_CODI          VARCHAR2(2)              NOT NULL,
  IDIOMA_DESC          VARCHAR2(100),
  TRANSPORT_TIPUS_CODI VARCHAR2(2),
  TRANSPORT_TIPUS_DESC VARCHAR2(100),
  TRANSPORT_NUM        VARCHAR2(20),
  USUARI_CODI          VARCHAR2(20),
  USUARI_NOM           VARCHAR2(80),
  USUARI_CONTACTE      VARCHAR2(160),
  APLICACIO_CODI       VARCHAR2(20),
  APLICACIO_VERSIO     VARCHAR2(15),
  DOCFIS_CODI          VARCHAR2(1),
  DOCFIS_DESC          VARCHAR2(100),
  OBSERVACIONS         VARCHAR2(50),
  EXPOSA               VARCHAR2(4000),
  SOLICITA             VARCHAR2(4000),
  MOTIU_REBUIG         VARCHAR2(4000),
  PROCES_DATA          TIMESTAMP(6),
  PROCES_ESTAT         VARCHAR2(16)             NOT NULL,
  PROCES_INTENTS       NUMBER(10),
  PROCES_ERROR         VARCHAR2(1024),
  REGLA_ID             NUMBER(19),
  CREATEDDATE          TIMESTAMP(6),
  CREATEDBY_CODI       VARCHAR2(256),
  LASTMODIFIEDDATE     TIMESTAMP(6),
  LASTMODIFIEDBY_CODI  VARCHAR2(256),
  PROCES_ESTAT_SISTRA  VARCHAR2(16 CHAR),
  SISTRA_ID_TRAM 	   VARCHAR2(20 CHAR),
  SISTRA_ID_PROC 	   VARCHAR2(100 CHAR),
  DATA_ORIG            TIMESTAMP(6),
  OFICINA_ORIG_CODI    VARCHAR2(21),
  OFICINA_ORIG_DESC    VARCHAR2(100)
);


CREATE TABLE IPA_REGISTRE_ANNEX
(
  ID                   NUMBER(19)               NOT NULL,
  TITOL                VARCHAR2(200)            NOT NULL,
  FITXER_NOM           VARCHAR2(80)             NOT NULL,
  FITXER_TAMANY        NUMBER(10)               NOT NULL,
  FITXER_MIME          VARCHAR2(30),
  FITXER_GESDOC_ID     VARCHAR2(100)            NOT NULL,
  DATA_CAPTURA         TIMESTAMP(6)             NOT NULL,
  LOCALITZACIO         VARCHAR2(80),
  ORIGEN_CIUADM        VARCHAR2(1)              NOT NULL,
  NTI_TIPUS_DOC        VARCHAR2(4)              NOT NULL,
  SICRES_TIPUS_DOC     VARCHAR2(2)              NOT NULL,
  NTI_ELABORACIO_ESTAT VARCHAR2(2),
  OBSERVACIONS         VARCHAR2(50),
  FIRMA_MODE           NUMBER(10),
  FIRMA_FITXER_NOM     VARCHAR2(80),
  FIRMA_FITXER_TAMANY  NUMBER(10),
  FIRMA_FITXER_MIME    VARCHAR2(30),
  FIRMA_FITXER_GESDOC  VARCHAR2(100),
  FIRMA_CSV            VARCHAR2(50),
  TIMESTAMP            VARCHAR2(100),
  VALIDACIO_OCSP       VARCHAR2(100),
  REGISTRE_ID          NUMBER(19)               NOT NULL,
  VERSION              NUMBER(19)               NOT NULL,
  CREATEDDATE          TIMESTAMP(6),
  CREATEDBY_CODI       VARCHAR2(256),
  LASTMODIFIEDDATE     TIMESTAMP(6),
  LASTMODIFIEDBY_CODI  VARCHAR2(256)
);


CREATE TABLE IPA_REGISTRE_INTER
(
  ID                   NUMBER(19)               NOT NULL,
  CREATEDDATE          TIMESTAMP(6),
  LASTMODIFIEDDATE     TIMESTAMP(6),
  ADRESA               VARCHAR2(640),
  CANAL_PREF           VARCHAR2(8),
  CODI_POSTAL          VARCHAR2(20),
  DOC_NUM              VARCHAR2(68),
  DOC_TIPUS            VARCHAR2(4),
  EMAIL                VARCHAR2(640),
  EMAIL_HAB            VARCHAR2(640),
  LLINATGE1            VARCHAR2(120),
  LLINATGE2            VARCHAR2(120),
  MUNICIPI             VARCHAR2(20),
  NOM                  VARCHAR2(120),
  OBSERVACIONS         VARCHAR2(640),
  PAIS                 VARCHAR2(16),
  PROVINCIA            VARCHAR2(8),
  RAO_SOCIAL           VARCHAR2(320),
  TELEFON              VARCHAR2(80),
  TIPUS                VARCHAR2(4)              NOT NULL,
  VERSION              NUMBER(19)               NOT NULL,
  CREATEDBY_CODI       VARCHAR2(256),
  LASTMODIFIEDBY_CODI  VARCHAR2(256),
  REGISTRE_ID          NUMBER(19)               NOT NULL,
  REPRESENTANT_ID      NUMBER(19),
  REPRESENTAT_ID       NUMBER(19)
);


CREATE TABLE IPA_REGLA
(
  ID                   NUMBER(19)               NOT NULL,
  CREATEDDATE          TIMESTAMP(6),
  LASTMODIFIEDDATE     TIMESTAMP(6),
  ACTIVA               NUMBER(1),
  ASSUMPTE_CODI        VARCHAR2(16)             NOT NULL,
  DESCRIPCIO           VARCHAR2(1024),
  NOM                  VARCHAR2(256)            NOT NULL,
  ORDRE                NUMBER(10)               NOT NULL,
  TIPUS                VARCHAR2(32)             NOT NULL,
  UNITAT_CODI          VARCHAR2(9),
  VERSION              NUMBER(19)               NOT NULL,
  CREATEDBY_CODI       VARCHAR2(256),
  LASTMODIFIEDBY_CODI  VARCHAR2(256),
  ENTITAT_ID           NUMBER(19)               NOT NULL,
  CONTRASENYA          VARCHAR2(64),
  TIPUS_BACKOFFICE     VARCHAR2(255),
  INTENTS              NUMBER(10),
  TEMPS_ENTRE_INTENTS  NUMBER(10),
  URL                  VARCHAR2(256),
  USUARI               VARCHAR2(64),
  ARXIU_ID             NUMBER(19),
  BUSTIA_ID            NUMBER(19),
  METAEXPEDIENT_ID     NUMBER(19)
);


CREATE TABLE IPA_ACL_CLASS
(
  ID     NUMBER(19)                             NOT NULL,
  CLASS  VARCHAR2(100)                          NOT NULL
);


CREATE TABLE IPA_ACL_SID
(
  ID         NUMBER(19)                         NOT NULL,
  PRINCIPAL  NUMBER(1)                          NOT NULL,
  SID        VARCHAR2(100)                      NOT NULL
);


CREATE TABLE IPA_ACL_ENTRY
(
  ID                   NUMBER(19)               NOT NULL,
  ACL_OBJECT_IDENTITY  NUMBER(19)               NOT NULL,
  ACE_ORDER            NUMBER(19)               NOT NULL,
  SID                  NUMBER(19)               NOT NULL,
  MASK                 NUMBER(19)               NOT NULL,
  GRANTING             NUMBER(1)                NOT NULL,
  AUDIT_SUCCESS        NUMBER(1)                NOT NULL,
  AUDIT_FAILURE        NUMBER(1)                NOT NULL
);


CREATE TABLE IPA_ACL_OBJECT_IDENTITY
(
  ID                  NUMBER(19)                NOT NULL,
  OBJECT_ID_CLASS     NUMBER(19)                NOT NULL,
  OBJECT_ID_IDENTITY  NUMBER(19)                NOT NULL,
  PARENT_OBJECT       NUMBER(19),
  OWNER_SID           NUMBER(19)                NOT NULL,
  ENTRIES_INHERITING  NUMBER(1)                 NOT NULL
);

CREATE TABLE IPA_CONT_COMMENT
(
  ID                   NUMBER(19)               NOT NULL,
  CONTINGUT_ID         NUMBER(19) 				NOT NULL,
  TEXT				   VARCHAR2 (1024),
  CREATEDDATE          TIMESTAMP(6),
  LASTMODIFIEDDATE     TIMESTAMP(6),
  CREATEDBY_CODI       VARCHAR2(64),
  LASTMODIFIEDBY_CODI  VARCHAR2(64)
);

CREATE TABLE IPA_EXECUCIO_MASSIVA
(
  ID                   NUMBER(19)		NOT NULL,
  TIPUS                VARCHAR2(255)	NOT NULL,
  DATA_INICI	       TIMESTAMP(6),
  DATA_FI              TIMESTAMP(6),
  PFIRMES_MOTIU		   VARCHAR2(256),
  PFIRMES_PRIORI	   VARCHAR2(255),
  PFIRMES_DATCAD	   TIMESTAMP(6),
  ENVIAR_CORREU		   NUMBER(1),
  ENTITAT_ID		   NUMBER(19)		NOT NULL,
  CREATEDBY_CODI       VARCHAR2(64),
  CREATEDDATE          TIMESTAMP(6),
  LASTMODIFIEDBY_CODI  VARCHAR2(64),
  LASTMODIFIEDDATE     TIMESTAMP(6)
);

CREATE TABLE IPA_MASSIVA_CONTINGUT
(
  ID                   NUMBER(19)		NOT NULL,
  EXECUCIO_MASSIVA_ID  NUMBER(19),
  CONTINGUT_ID  	   NUMBER(19),
  DATA_INICI	       TIMESTAMP(6),
  DATA_FI              TIMESTAMP(6),
  ESTAT				   VARCHAR2(255),
  ERROR				   VARCHAR2(2046),
  ORDRE				   NUMBER(19),
  CREATEDBY_CODI       VARCHAR2(64),
  CREATEDDATE          TIMESTAMP(6),
  LASTMODIFIEDBY_CODI  VARCHAR2(64),
  LASTMODIFIEDDATE     TIMESTAMP(6)
);