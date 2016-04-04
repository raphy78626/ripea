
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
  SIGNATURA_TIPMIME       VARCHAR2(64),
  FIRMA_PFIRMA            NUMBER(1),
  PORTAFIRMES_DOCTIP      VARCHAR2(64),
  PORTAFIRMES_FLUXID      VARCHAR2(64),
  PORTAFIRMES_RESPONS     VARCHAR2(512),
  PORTAFIRMES_FLUXTIP     VARCHAR2(256),
  CUSTODIA_POLITICA       VARCHAR2(64),
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
  ID            NUMBER(19)                      NOT NULL,
  OBERT         NUMBER(1),
  TANCAT_MOTIU  VARCHAR2(1024),
  ANIO          NUMBER (10),
  SEQUENCIA     NUMBER (19),
  ARXIU_ID      NUMBER(19)                      NOT NULL
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
  CREATEDDATE          TIMESTAMP(6),
  LASTMODIFIEDDATE     TIMESTAMP(6),
  APLICACIO_CODI       VARCHAR2(80),
  APLICACIO_VERSIO     VARCHAR2(60),
  ASSUMPTE_CODI        VARCHAR2(64),
  ASSUMPTE_NUMEXP      VARCHAR2(320),
  ASSUMPTE_REF         VARCHAR2(64),
  ASSUMPTE_TIPUS       VARCHAR2(64)             NOT NULL,
  DATA                 TIMESTAMP(6),
  DATA_PROCESSAT       TIMESTAMP(6),
  DOCUMENTACIO_FIS     VARCHAR2(4),
  EXPOSA               VARCHAR2(4000),
  EXTRACTE             VARCHAR2(960),
  IDENTIFICADOR        VARCHAR2(1020),
  IDIOMA               VARCHAR2(8)              NOT NULL,
  LLIBRE               VARCHAR2(16)             NOT NULL,
  MOTIU_REBUIG         VARCHAR2(4000),
  NUMERO               NUMBER(10)               NOT NULL,
  OBSERVACIONS         VARCHAR2(200),
  OFICINA              VARCHAR2(84)             NOT NULL,
  SOLICITA             VARCHAR2(4000),
  TIPUS                VARCHAR2(4)              NOT NULL,
  TRANSPORT_NUM        VARCHAR2(80),
  TRANSPORT_TIPUS      VARCHAR2(8),
  USUARI_CONTACTE      VARCHAR2(640),
  USUARI_NOM           VARCHAR2(320)            NOT NULL,
  VERSION              NUMBER(19)               NOT NULL,
  CREATEDBY_CODI       VARCHAR2(256),
  LASTMODIFIEDBY_CODI  VARCHAR2(256),
  CONTENIDOR_ID        NUMBER(19)
);


CREATE TABLE IPA_REGISTRE_ANNEX
(
  ID                   NUMBER(19)               NOT NULL,
  CREATEDDATE          TIMESTAMP(6),
  LASTMODIFIEDDATE     TIMESTAMP(6),
  DATA_CAPTURA         TIMESTAMP(6)             NOT NULL,
  FIRMA_MODE           NUMBER(10),
  FITXER_NOM           VARCHAR2(320)            NOT NULL,
  FITXER_TAMANY        NUMBER(10)               NOT NULL,
  FITXER_MIME          VARCHAR2(120),
  NTI_TIPO             VARCHAR2(16)             NOT NULL,
  OBSERVACIONS         VARCHAR2(200),
  ORIGEN               VARCHAR2(4),
  TIPUS                VARCHAR2(1020)           NOT NULL,
  TITOL                VARCHAR2(800)            NOT NULL,
  VALIDESA             VARCHAR2(1020),
  VERSION              NUMBER(19)               NOT NULL,
  CREATEDBY_CODI       VARCHAR2(256),
  LASTMODIFIEDBY_CODI  VARCHAR2(256),
  REGISTRE_ID          NUMBER(19)               NOT NULL
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
