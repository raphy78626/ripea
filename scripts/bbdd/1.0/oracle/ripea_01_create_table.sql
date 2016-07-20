
CREATE TABLE IPA_USUARI
(
  CODI          VARCHAR2(64)                    NOT NULL,
  INICIALITZAT  NUMBER(1),
  NIF           VARCHAR2(9)                     NOT NULL,
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
  FIRMA_APPLET            NUMBER(1),
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
  ID             NUMBER(19)                     NOT NULL,
  PARE_ID        NUMBER(19),
  CLASSIFICACIO  VARCHAR2(30)
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
  DTYPE                VARCHAR2(256)            NOT NULL,
  ID                   NUMBER(19)               NOT NULL,
  CREATEDDATE          TIMESTAMP(6),
  LASTMODIFIEDDATE     TIMESTAMP(6),
  NOM                  VARCHAR2(256)            NOT NULL,
  VERSION              NUMBER(19)               NOT NULL,
  IDENTIFICADOR        VARCHAR2(9),
  LLINATGES            VARCHAR2(256),
  NIF                  VARCHAR2(9),
  CREATEDBY_CODI       VARCHAR2(64),
  LASTMODIFIEDBY_CODI  VARCHAR2(64),
  ENTITAT_ID           NUMBER(19)               NOT NULL
);


CREATE TABLE IPA_ARXIU
(
  ID                   NUMBER(19)               NOT NULL,
  ACTIU                NUMBER(1),
  UNITAT_CODI          VARCHAR2(9)              NOT NULL
);


CREATE TABLE IPA_CONTENIDOR
(
  ID                   NUMBER(19)               NOT NULL,
  CREATEDDATE          TIMESTAMP(6),
  LASTMODIFIEDDATE     TIMESTAMP(6),
  ESBORRAT             NUMBER(10),
  NOM                  VARCHAR2(256)            NOT NULL,
  TIPUS_CONT           NUMBER(10)               NOT NULL,
  VERSION              NUMBER(19)               NOT NULL,
  CREATEDBY_CODI       VARCHAR2(64),
  LASTMODIFIEDBY_CODI  VARCHAR2(64),
  CONTMOV_ID           NUMBER(19),
  ENTITAT_ID           NUMBER(19)               NOT NULL,
  PARE_ID              NUMBER(19)
);


CREATE TABLE IPA_CONT_MOV
(
  ID                   NUMBER(19)               NOT NULL,
  CREATEDDATE          TIMESTAMP(6),
  LASTMODIFIEDDATE     TIMESTAMP(6),
  COMENTARI            VARCHAR2(256),
  DATA                 TIMESTAMP(6),
  VERSION              NUMBER(19)               NOT NULL,
  CREATEDBY_CODI       VARCHAR2(64),
  LASTMODIFIEDBY_CODI  VARCHAR2(64),
  CONTENIDOR_ID        NUMBER(19)               NOT NULL,
  ORIGEN_ID            NUMBER(19),
  DESTI_ID             NUMBER(19)               NOT NULL,
  REMITENT_ID          VARCHAR2(64)
);


CREATE TABLE IPA_CONT_LOG
(
  ID                   NUMBER(19)                     NOT NULL,
  CONTENIDOR_ID        NUMBER(19)                     NOT NULL,
  DATA                 TIMESTAMP(6)                   NOT NULL,
  USUARI_ID            VARCHAR2 (255)                 NOT NULL,
  PARE_ID              NUMBER (19),
  CONTENIDOR_MOV_ID    NUMBER (19),
  OBJECTE_ID           NUMBER (19),
  OBJECTE_LOG_TIPUS    NUMBER (10),
  OBJECTE_TIPUS        NUMBER (10),
  PARAM1               VARCHAR2 (256),
  PARAM2               VARCHAR2 (256),
  TIPUS                NUMBER (10),
  VERSION              NUMBER (19)
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
  TIPUS  VARCHAR2(1020)                         NOT NULL,
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
  OBERT              NUMBER(1),
  TANCAT_MOTIU       VARCHAR2(1024),
  ANIO               NUMBER (10),
  SEQUENCIA          NUMBER (19),
  ARXIU_ID           NUMBER(19)                 NOT NULL,
  SISTRA_CLAU        VARCHAR2(100),
  SISTRA_PUBLICAT    NUMBER(1),
  SISTRA_UNITAT_ADM  NUMBER(10)
);


CREATE TABLE IPA_DOCUMENT
(
  ID              NUMBER(19)                    NOT NULL,
  DARRERA_VERSIO  NUMBER(10)                    NOT NULL,
  DATA            TIMESTAMP(6),
  EXPEDIENT_ID    NUMBER(19)
);


CREATE TABLE IPA_DOCUMENT_VERSIO
(
  ID                    NUMBER(19)              NOT NULL,
  CREATEDDATE           TIMESTAMP(6),
  LASTMODIFIEDDATE      TIMESTAMP(6),
  ARXIU_NOM             VARCHAR2(256)           NOT NULL,
  ARXIU_CONTENT_TYPE    VARCHAR2(256)           NOT NULL,
  ARXIU_CONTENT_LENGTH  NUMBER(19)              NOT NULL,
  ARXIU_CONTINGUT       BLOB,
  ARXIU_GESDOC_ID       VARCHAR2(100),
  VERSIO                NUMBER(10)              NOT NULL,
  VERSION               NUMBER(19)              NOT NULL,
  CREATEDBY_CODI        VARCHAR2(64),
  LASTMODIFIEDBY_CODI   VARCHAR2(64),
  DOCUMENT_ID           NUMBER(19)              NOT NULL,
  CUSTODIAT             NUMBER(1),
  CUSTODIA_ID           VARCHAR2(256),
  CUSTODIA_URL          VARCHAR2(1024)
);


CREATE TABLE IPA_DOCUMENT_PFIRMES
(
  ID                    NUMBER(19)              NOT NULL,
  CREATEDDATE           TIMESTAMP(6),
  LASTMODIFIEDDATE      TIMESTAMP(6),
  PFIRMES_ID            NUMBER(19)              NOT NULL,
  VERSIO                NUMBER(10)              NOT NULL,
  VERSION               NUMBER(19)              NOT NULL,
  CREATEDBY_CODI        VARCHAR2(64),
  LASTMODIFIEDBY_CODI   VARCHAR2(64),
  DOCUMENT_ID           NUMBER(19)              NOT NULL,
  DATA_CADUCITAT        DATE,
  MOTIU                 VARCHAR2(256)           NOT NULL,
  PFIRMES_ESTAT         VARCHAR2(255)           NOT NULL,
  PRIORITAT             VARCHAR2(255)           NOT NULL,
  CALLBACK_DARRER       TIMESTAMP(6),
  CALLBACK_COUNT        NUMBER(10),
  ERROR_DESC            VARCHAR2(1024)
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
  DATA_PROCESSAT       TIMESTAMP(6),
  MOTIU_REBUIG         VARCHAR2(4000),
  CONTENIDOR_ID        NUMBER(19)               NOT NULL,
  VERSION              NUMBER(19)               NOT NULL,
  CREATEDDATE          TIMESTAMP(6),
  CREATEDBY_CODI       VARCHAR2(256),
  LASTMODIFIEDDATE     TIMESTAMP(6),
  LASTMODIFIEDBY_CODI  VARCHAR2(256)
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


CREATE TABLE IPA_REGISTRE_MOV
(
   ID                    NUMBER(19)             NOT NULL,
   CREATEDDATE           TIMESTAMP(6),
   LASTMODIFIEDDATE      TIMESTAMP(6),
   COMENTARI             VARCHAR2(256),
   DATA                  TIMESTAMP(6)           NOT NULL,
   VERSION               NUMBER(19),
   CREATEDBY_CODI        VARCHAR2(64),
   LASTMODIFIEDBY_CODI   VARCHAR2(64),
   DESTI_ID              NUMBER(19)             NOT NULL,
   ORIGEN_ID             NUMBER(19),
   REGISTRE_ID           NUMBER(19)             NOT NULL,
   REMITENT_ID           VARCHAR2 (64)
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