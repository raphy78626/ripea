
CREATE TABLE IPA_ALERTA
(
  ID                   BIGINT                          NOT NULL,
  TEXT                 character varying(256)          NOT NULL,
  ERROR                character varying(2048),
  LLEGIDA              boolean                         NOT NULL,
  CONTINGUT_ID         bigint,
  CREATEDBY_CODI       character varying(64),
  CREATEDDATE          timestamp without time zone,
  LASTMODIFIEDBY_CODI  character varying(64),
  LASTMODIFIEDDATE     timestamp without time zone
);

CREATE TABLE IPA_USUARI
(
  CODI          character varying(64)           NOT NULL,
  INICIALITZAT  boolean,
  NIF           character varying(9)            NOT NULL,
  NOM           character varying(200),
  EMAIL         character varying(200),
  VERSION       bigint                          NOT NULL
);


CREATE TABLE IPA_ENTITAT
(
  ID                   BIGINT                   NOT NULL,
  CODI                 character varying(64)    NOT NULL,
  NOM                  character varying(256)   NOT NULL,
  DESCRIPCIO           character varying(1024),
  CIF                  character varying(9)     NOT NULL,
  UNITAT_ARREL         character varying(9)     NOT NULL,
  ACTIVA               boolean,
  VERSION              bigint                   NOT NULL,
  CREATEDBY_CODI       character varying(64),
  CREATEDDATE          timestamp without time zone,
  LASTMODIFIEDBY_CODI  character varying(64),
  LASTMODIFIEDDATE     timestamp without time zone
);


CREATE TABLE IPA_METANODE
(
  ID                   BIGINT                  NOT NULL,
  CODI                 character varying(256)  NOT NULL,
  NOM                  character varying(256)  NOT NULL,
  DESCRIPCIO           character varying(1024),
  TIPUS                character varying(256)  NOT NULL,
  ENTITAT_ID           bigint                  NOT NULL,
  ACTIU                boolean,
  VERSION              bigint                  NOT NULL,
  CREATEDBY_CODI       character varying(64),
  CREATEDDATE          timestamp without time zone,
  LASTMODIFIEDBY_CODI  character varying(64),
  LASTMODIFIEDDATE     timestamp without time zone
);


CREATE TABLE IPA_METADOCUMENT
(
  ID                      BIGINT               NOT NULL,
  GLOBAL_EXPEDIENT        boolean,
  GLOBAL_MULTIPLICITAT    character varying(256),
  GLOBAL_READONLY         boolean,
  FIRMA_PFIRMA            boolean,
  PORTAFIRMES_DOCTIP      character varying(64),
  PORTAFIRMES_FLUXID      character varying(64),
  PORTAFIRMES_RESPONS     character varying(512),
  PORTAFIRMES_FLUXTIP     character varying(256),
  PORTAFIRMES_CUSTIP      character varying(64),
  FIRMA_PASSARELA         boolean,
  PASSARELA_CUSTIP        character varying(64),
  PLANTILLA_NOM           character varying(256),
  PLANTILLA_CONTENT_TYPE  character varying(256),
  PLANTILLA_CONTINGUT     bytea
);


CREATE TABLE IPA_METADADA
(
  ID                    BIGINT                 NOT NULL,
  CREATEDDATE           timestamp without time zone,
  LASTMODIFIEDDATE      timestamp without time zone,
  ACTIVA                boolean,
  CODI                  character varying(64)   NOT NULL,
  DESCRIPCIO            character varying(1024),
  GLOBAL_CARPETA        boolean,
  GLOBAL_DOCUMENT       boolean,
  GLOBAL_EXPEDIENT      boolean,
  GLOBAL_MULTIPLICITAT  character varying(256),
  GLOBAL_READONLY       boolean,
  NOM                   character varying(256)   NOT NULL,
  TIPUS                 character varying(256)  NOT NULL,
  VERSION               bigint                   NOT NULL,
  CREATEDBY_CODI        character varying(64),
  LASTMODIFIEDBY_CODI   character varying(64),
  ENTITAT_ID            bigint                   NOT NULL
);


CREATE TABLE IPA_METAEXPEDIENT
(
  ID                 BIGINT                     NOT NULL,
  PARE_ID            bigint,
  CLASIF_SIA         character varying(6)       NOT NULL,
  CLASIF_DOC         character varying(30)      NOT NULL,
  UNITAT_ADM         character varying(9),
  UNITAT_ADM_DESC    character varying(10),
  NOTIF_ACTIVA       boolean                    NOT NULL,
  NOTIF_UNITAT_ADM   character varying(9),
  NOTIF_ORGAN_CODI   character varying(9),
  NOTIF_LLIBRE_CODI  character varying(4),
  NOTIF_AVIS_TITOL   character varying(256),
  NOTIF_AVIS_TEXT    character varying(1024),
  NOTIF_AVIS_TEXTSMS character varying(200),
  NOTIF_OFICI_TITOL  character varying(256),
  NOTIF_OFICI_TEXT   character varying(1024)
);


CREATE TABLE IPA_METAEXPEDIENT_ARXIU
(
  METAEXPEDIENT_ID  BIGINT                      NOT NULL,
  ARXIU_ID          bigint                      NOT NULL
);


CREATE TABLE IPA_METAEXP_SEQ
(
  ID                   BIGINT                    NOT NULL,
  ANIO                 integer,
  VALOR                bigint,
  CREATEDDATE          timestamp without time zone,
  LASTMODIFIEDDATE     timestamp without time zone,
  CREATEDBY_CODI       character varying(64),
  LASTMODIFIEDBY_CODI  character varying(64),
  META_EXPEDIENT_ID    bigint                    NOT NULL
);

CREATE TABLE IPA_INTERESSAT
(
  ID                   BIGINT                   NOT NULL,
  DTYPE                character varying(256)   NOT NULL,
  NOM                  character varying(30)    NOT NULL,
  LLINATGE1            character varying(30),
  LLINATGE2            character varying(30),
  DOCUMENT_TIPUS       character varying(40)    NOT NULL,
  DOCUMENT_NUM         character varying(17)    NOT NULL,
  PAIS                 character varying(4),
  PROVINCIA            character varying(2),
  MUNICIPI             character varying(5),
  ADRESA               character varying(160),
  CODI_POSTAL          character varying(5),
  EMAIL                character varying(160),
  TELEFON              character varying(20),
  OBSERVACIONS         character varying(160),
  ORGAN_CODI           character varying(9),
  ORGAN_NOM	       character varying(80),
  RAO_SOCIAL           character varying(80),
  NOT_IDIOMA           character varying(2),
  NOT_AUTORITZAT       boolean                  NOT NULL,
  ES_REPRESENTANT      boolean                  NOT NULL,
  REPRESENTANT_ID      bigint,
  EXPEDIENT_ID         bigint                   NOT NULL,
  CREATEDDATE          timestamp without time zone,
  LASTMODIFIEDDATE     timestamp without time zone,
  CREATEDBY_CODI       character varying(64),
  LASTMODIFIEDBY_CODI  character varying(64),
  VERSION              bigint               NOT NULL
);


CREATE TABLE IPA_ARXIU
(
  ID                   BIGINT                    NOT NULL,
  ACTIU                boolean,
  UNITAT_CODI          character varying(9)      NOT NULL
);


CREATE TABLE IPA_CONTINGUT
(
  ID                   BIGINT                   NOT NULL,
  NOM                  character varying(1024)  NOT NULL,
  TIPUS                integer                  NOT NULL,
  PARE_ID              bigint,
  ESBORRAT             integer,
  ARXIU_UUID           character varying(36),
  ARXIU_DATA_ACT       timestamp without time zone,
  CONTMOV_ID           bigint,
  ENTITAT_ID           bigint                   NOT NULL,
  CREATEDDATE          timestamp without time zone,
  LASTMODIFIEDDATE     timestamp without time zone,
  CREATEDBY_CODI       character varying(64),
  LASTMODIFIEDBY_CODI  character varying(64),
  VERSION              bigint                   NOT NULL
);


CREATE TABLE IPA_CONT_MOV
(
  ID                   BIGINT                   NOT NULL,
  CONTINGUT_ID         bigint                   NOT NULL,
  ORIGEN_ID            bigint,
  DESTI_ID             bigint                   NOT NULL,
  REMITENT_CODI        character varying(64),
  COMENTARI            character varying(256),
  CREATEDDATE          timestamp without time zone,
  LASTMODIFIEDDATE     timestamp without time zone,
  CREATEDBY_CODI       character varying(64),
  LASTMODIFIEDBY_CODI  character varying(64)
);


CREATE TABLE IPA_CONT_LOG
(
  ID                   BIGINT                   NOT NULL,
  TIPUS                integer                  NOT NULL,
  CONTINGUT_ID         bigint                   NOT NULL,
  PARE_ID              bigint,
  CONTMOV_ID           bigint,
  OBJECTE_ID           character varying(256),
  OBJECTE_LOG_TIPUS    integer,
  OBJECTE_TIPUS        integer,
  PARAM1               character varying(256),
  PARAM2               character varying(256),
  CREATEDDATE          timestamp without time zone,
  LASTMODIFIEDDATE     timestamp without time zone,
  CREATEDBY_CODI       character varying(64),
  LASTMODIFIEDBY_CODI  character varying(64)
);


CREATE TABLE IPA_ESCRIPTORI
(
  ID         BIGINT                               NOT NULL,
  USUARI_ID  character varying(64)                NOT NULL
);


CREATE TABLE IPA_METAEXPEDIENT_METADOCUMENT
(
  ID                   BIGINT                     NOT NULL,
  CREATEDDATE          timestamp without time zone,
  LASTMODIFIEDDATE     timestamp without time zone,
  MULTIPLICITAT        character varying(256),
  ORDRE                integer                 NOT NULL,
  CREATEDBY_CODI       character varying(64),
  LASTMODIFIEDBY_CODI  character varying(64),
  METADOCUMENT_ID      bigint                     NOT NULL,
  METAEXPEDIENT_ID     bigint                     NOT NULL,
  READONLY             boolean
);


CREATE TABLE IPA_NODE
(
  ID                  BIGINT                      NOT NULL,
  METANODE_ID         bigint
);


CREATE TABLE IPA_METANODE_METADADA
(
  ID                   BIGINT                     NOT NULL,
  CREATEDDATE          timestamp without time zone,
  LASTMODIFIEDDATE     timestamp without time zone,
  MULTIPLICITAT        character varying(256),
  ORDRE                integer                 NOT NULL,
  CREATEDBY_CODI       character varying(64),
  LASTMODIFIEDBY_CODI  character varying(64),
  METADADA_ID          bigint                     NOT NULL,
  METANODE_ID          bigint                     NOT NULL,
  READONLY             boolean
);


CREATE TABLE IPA_CARPETA
(
  ID     BIGINT                                   NOT NULL
);


CREATE TABLE IPA_DADA
(
  ID                   BIGINT                     NOT NULL,
  CREATEDDATE          timestamp without time zone,
  LASTMODIFIEDDATE     timestamp without time zone,
  ORDRE                integer,
  VALOR                character varying(256)     NOT NULL,
  VERSION              bigint                     NOT NULL,
  CREATEDBY_CODI       character varying(64),
  LASTMODIFIEDBY_CODI  character varying(64),
  METADADA_ID          bigint                     NOT NULL,
  NODE_ID              bigint                     NOT NULL
);


CREATE TABLE IPA_EXPEDIENT
(
  ID                 BIGINT                     NOT NULL,
  ESTAT              integer                    NOT NULL,
  TANCAT_DATA        timestamp without time zone,
  TANCAT_MOTIU       character varying(1024),
  ANIO               integer                    NOT NULL,
  SEQUENCIA          bigint                     NOT NULL,
  CODI               character varying(256)     NOT NULL,
  ARXIU_ID           bigint                     NOT NULL,
  NTI_VERSION        character varying(5)       NOT NULL,
  NTI_IDENTIF        character varying(52)      NOT NULL,
  NTI_ORGANO         character varying(9)       NOT NULL,
  NTI_FECHA_APE      timestamp without time zone NOT NULL,
  NTI_CLASIF_SIA     character varying(6)       NOT NULL,
  SISTRA_BANTEL_NUM  character varying(16),
  SISTRA_PUBLICAT    boolean,
  SISTRA_UNITAT_ADM  character varying(9),
  SISTRA_CLAU        character varying(100),
  AGAFAT_PER_CODI    character varying(64)
);


CREATE TABLE IPA_EXPEDIENT_REL
(
  EXPEDIENT_ID       BIGINT                     NOT NULL,
  EXPEDIENT_REL_ID   bigint                     NOT NULL
);


CREATE TABLE IPA_DOCUMENT
(
  ID                   BIGINT                   NOT NULL,
  TIPUS                integer                  NOT NULL,
  ESTAT                integer                  NOT NULL,
  UBICACIO             character varying(255),
  DATA                 timestamp without time zone NOT NULL,
  DATA_CAPTURA         timestamp without time zone NOT NULL,
  EXPEDIENT_ID         bigint,
  CUSTODIA_DATA        timestamp without time zone,
  CUSTODIA_ID          character varying(256),
  CUSTODIA_CSV         character varying(256),
  FITXER_NOM           character varying(256),
  FITXER_CONTENT_TYPE  character varying(256),
  FITXER_CONTINGUT     bytea,
  VERSIO_DARRERA       character varying(32),
  VERSIO_COUNT         integer                  NOT NULL,
  NTI_VERSION          character varying(5)     NOT NULL,
  NTI_IDENTIF          character varying(48)    NOT NULL,
  NTI_ORGANO           character varying(9)     NOT NULL,
  NTI_ORIGEN           character varying(2)     NOT NULL,
  NTI_ESTELA           character varying(4)     NOT NULL,
  NTI_TIPDOC           character varying(4)     NOT NULL,
  NTI_IDORIG           character varying(48),
  NTI_TIPFIR           character varying(4),
  NTI_CSV              character varying(256),
  NTI_CSVREG           character varying(512)
);


CREATE TABLE IPA_DOCUMENT_ENVIAMENT
(
  ID                   BIGINT                   NOT NULL,
  DTYPE                character varying(32)    NOT NULL,
  ASSUMPTE             character varying(256)   NOT NULL,
  ESTAT                character varying(255)   NOT NULL,
  DATA_ENVIAMENT       timestamp without time zone NOT NULL,
  OBSERVACIONS         character varying(256),
  DATA_PUBLICACIO      timestamp without time zone,
  TIPUS                character varying(255),
  DATA_RECEPCIO        timestamp without time zone,
  REGISTRE_NUM         character varying(100),
  DESTIN_DOCNUM        character varying(17),
  DESTIN_DOCTIP        integer,
  DESTIN_EMAIL         character varying(160),
  DESTIN_LLING1        character varying(30),
  DESTIN_LLING2        character varying(30),
  DESTIN_NOM           character varying(30),
  DESTIN_PAICOD        character varying(4),
  DESTIN_PRVCOD        character varying(2),
  DESTIN_MUNCOD        character varying(5),
  DESTIN_REPRES        boolean,
  UNITAT_ADM           character varying(9),
  ORGAN_CODI           character varying(9),
  LLIBRE_CODI          character varying(4),
  AVIS_TITOL           character varying(256),
  AVIS_TEXT            character varying(1024),
  AVIS_TEXTSMS         character varying(200),
  OFICI_TITOL          character varying(256),
  OFICI_TEXT           character varying(1024),
  IDIOMA               character varying(2),
  PFIRMES_PRIORI       character varying(64),
  PFIRMES_DATCAD       timestamp without time zone,
  PFIRMES_DOCTIP       character varying(64),
  PFIRMES_RESPONSABLES character varying(1024),
  PFIRMES_FLUXTIP      character varying(64),
  PFIRMES_FLUXID       character varying(64),
  PFIRMES_ID           character varying(64),
  ENVIAM_DATA          timestamp without time zone,
  ENVIAM_COUNT         integer,
  ENVIAM_ERROR         boolean,
  ENVIAM_ERROR_DESC    character varying(2048),
  PROCES_DATA          timestamp without time zone,
  PROCES_COUNT         integer,
  PROCES_ERROR         boolean,
  PROCES_ERROR_DESC    character varying(2048),
  DOCUMENT_ID          bigint                  NOT NULL,
  EXPEDIENT_ID         bigint                  NOT NULL,
  CREATEDDATE          timestamp without time zone,
  LASTMODIFIEDDATE     timestamp without time zone,
  CREATEDBY_CODI       character varying(256),
  LASTMODIFIEDBY_CODI  character varying(256),
  VERSION              bigint                  NOT NULL
);


CREATE TABLE IPA_DOCUMENT_ENVIAMENT_DOC (
  DOCUMENT_ENVIAMENT_ID BIGINT                  NOT NULL,
  DOCUMENT_ID           bigint                  NOT NULL
);


CREATE TABLE IPA_EXPEDIENT_INTERESSAT
(
  EXPEDIENT_ID   BIGINT                         NOT NULL,
  INTERESSAT_ID  bigint                         NOT NULL
);


CREATE TABLE IPA_BUSTIA
(
  ID           BIGINT                           NOT NULL,
  UNITAT_CODI  character varying(9)             NOT NULL,
  PER_DEFECTE  boolean,
  ACTIVA       boolean
);


CREATE TABLE IPA_REGISTRE
(
  ID                   BIGINT                   NOT NULL,
  TIPUS                character varying(1)     NOT NULL,
  UNITAT_ADM           character varying(21)    NOT NULL,
  UNITAT_ADM_DESC      character varying(100),
  NUMERO               character varying(100)   NOT NULL,
  DATA                 timestamp without time zone NOT NULL,
  IDENTIFICADOR        character varying(100)   NOT NULL,
  ENTITAT_CODI         character varying(21)    NOT NULL,
  ENTITAT_DESC         character varying(100),
  OFICINA_CODI         character varying(21)    NOT NULL,
  OFICINA_DESC         character varying(100),
  LLIBRE_CODI          character varying(4)     NOT NULL,
  LLIBRE_DESC          character varying(100),
  EXTRACTE             character varying(240),
  ASSUMPTE_TIPUS_CODI  character varying(16)    NOT NULL,
  ASSUMPTE_TIPUS_DESC  character varying(100),
  ASSUMPTE_CODI        character varying(16),
  ASSUMPTE_DESC        character varying(100),
  REFERENCIA           character varying(16),
  EXPEDIENT_NUM        character varying(80),
  NUM_ORIG 			   character varying(80),
  IDIOMA_CODI          character varying(2)     NOT NULL,
  IDIOMA_DESC          character varying(100),
  TRANSPORT_TIPUS_CODI character varying(2),
  TRANSPORT_TIPUS_DESC character varying(100),
  TRANSPORT_NUM        character varying(20),
  USUARI_CODI          character varying(20),
  USUARI_NOM           character varying(80),
  USUARI_CONTACTE      character varying(160),
  APLICACIO_CODI       character varying(20),
  APLICACIO_VERSIO     character varying(15),
  DOCFIS_CODI          character varying(1),
  DOCFIS_DESC          character varying(100),
  OBSERVACIONS         character varying(50),
  EXPOSA               character varying(4000),
  SOLICITA             character varying(4000),
  MOTIU_REBUIG         character varying(4000),
  PROCES_DATA          timestamp without time zone,
  PROCES_ESTAT         character varying(16)    NOT NULL,
  PROCES_INTENTS       integer,
  PROCES_ERROR         character varying(1024),
  REGLA_ID             bigint,
  CREATEDDATE          timestamp without time zone,
  CREATEDBY_CODI       character varying(256),
  LASTMODIFIEDDATE     timestamp without time zone,
  LASTMODIFIEDBY_CODI  character varying(256),
  PROCES_ESTAT_SISTRA  character varying(16),
  SISTRA_ID_TRAM       character varying(20),
  SISTRA_ID_PROC       character varying(100),
  DATA_ORIG            timestamp without time zone,
  OFICINA_ORIG_CODI    character varying(21),
  OFICINA_ORIG_DESC    character varying(100),
  JUSTIFICANT_ARXIU_UUID character varying(100)
);


CREATE TABLE IPA_REGISTRE_ANNEX
(
  ID                   BIGINT                   NOT NULL,
  TITOL                character varying(200)   NOT NULL,
  FITXER_NOM           character varying(80)    NOT NULL,
  FITXER_TAMANY        integer                  NOT NULL,
  FITXER_MIME          character varying(30),
  FITXER_GESDOC_ID     character varying(100)   NOT NULL,
  DATA_CAPTURA         timestamp without time zone NOT NULL,
  LOCALITZACIO         character varying(80),
  ORIGEN_CIUADM        character varying(1)     NOT NULL,
  NTI_TIPUS_DOC        character varying(4)     NOT NULL,
  SICRES_TIPUS_DOC     character varying(2)     NOT NULL,
  NTI_ELABORACIO_ESTAT character varying(2),
  OBSERVACIONS         character varying(50),
  FIRMA_MODE           integer,
  FIRMA_FITXER_NOM     character varying(80),
  FIRMA_FITXER_TAMANY  integer,
  FIRMA_FITXER_MIME    character varying(30),
  FIRMA_FITXER_GESDOC  character varying(100),
  FIRMA_CSV            character varying(50),
  TIMESTAMP            character varying(100),
  VALIDACIO_OCSP       character varying(100),
  REGISTRE_ID          bigint                   NOT NULL,
  VERSION              bigint                   NOT NULL,
  CREATEDDATE          timestamp without time zone,
  CREATEDBY_CODI       character varying(256),
  LASTMODIFIEDDATE     timestamp without time zone,
  LASTMODIFIEDBY_CODI  character varying(256)
);

CREATE TABLE IPA_REGISTRE_ANNEX_FIRMA
(
  ID                   BIGINT                   	NOT NULL,
  TIPUS		           character varying(30),
  PERFIL    	       character varying(30),
  FITXER_NOM           character varying(80),
  TIPUS_MIME           character varying(30),
  CSV_REGULACIO		   character varying(640),
  ANNEX_ID             bigint(19)               	NOT NULL,
  CREATEDDATE          timestamp without time zone,
  CREATEDBY_CODI       character varying(256),
  LASTMODIFIEDDATE     timestamp without time zone,
  LASTMODIFIEDBY_CODI  character varying(256)
);


CREATE TABLE IPA_REGISTRE_INTER
(
  ID                   BIGINT                   NOT NULL,
  CREATEDDATE          timestamp without time zone,
  LASTMODIFIEDDATE     timestamp without time zone,
  ADRESA               character varying(640),
  CANAL_PREF           character varying(8),
  CODI_POSTAL          character varying(20),
  DOC_NUM              character varying(68),
  DOC_TIPUS            character varying(4),
  EMAIL                character varying(640),
  EMAIL_HAB            character varying(640),
  LLINATGE1            character varying(120),
  LLINATGE2            character varying(120),
  MUNICIPI             character varying(20),
  NOM                  character varying(120),
  OBSERVACIONS         character varying(640),
  PAIS                 character varying(16),
  PROVINCIA            character varying(8),
  RAO_SOCIAL           character varying(320),
  TELEFON              character varying(80),
  TIPUS                character varying(4)     NOT NULL,
  VERSION              bigint                   NOT NULL,
  CREATEDBY_CODI       character varying(256),
  LASTMODIFIEDBY_CODI  character varying(256),
  REGISTRE_ID          bigint                   NOT NULL,
  REPRESENTANT_ID      bigint,
  REPRESENTAT_ID       bigint
);


CREATE TABLE IPA_REGLA
(
  ID                   BIGINT                   NOT NULL,
  CREATEDDATE          timestamp without time zone,
  LASTMODIFIEDDATE     timestamp without time zone,
  ACTIVA               boolean,
  ASSUMPTE_CODI        character varying(16)    NOT NULL,
  DESCRIPCIO           character varying(1024),
  NOM                  character varying(256)   NOT NULL,
  ORDRE                integer                  NOT NULL,
  TIPUS                character varying(32)    NOT NULL,
  UNITAT_CODI          character varying(9),
  VERSION              bigint                   NOT NULL,
  CREATEDBY_CODI       character varying(256),
  LASTMODIFIEDBY_CODI  character varying(256),
  ENTITAT_ID           bigint                   NOT NULL,
  CONTRASENYA          character varying(64),
  TIPUS_BACKOFFICE     character varying(255),
  INTENTS              integer,
  TEMPS_ENTRE_INTENTS  integer,
  URL                  character varying(256),
  USUARI               character varying(64),
  ARXIU_ID             bigint,
  BUSTIA_ID            bigint,
  METAEXPEDIENT_ID     bigint
);


CREATE TABLE IPA_ACL_CLASS
(
  ID     BIGSERIAL                              NOT NULL,
  CLASS  character varying(100)                 NOT NULL
);


CREATE TABLE IPA_ACL_SID
(
  ID         BIGSERIAL                          NOT NULL,
  PRINCIPAL  boolean                            NOT NULL,
  SID        character varying(100)             NOT NULL
);


CREATE TABLE IPA_ACL_ENTRY
(
  ID                   BIGSERIAL                NOT NULL,
  ACL_OBJECT_IDENTITY  bigint                   NOT NULL,
  ACE_ORDER            bigint                   NOT NULL,
  SID                  bigint                   NOT NULL,
  MASK                 bigint                   NOT NULL,
  GRANTING             boolean                  NOT NULL,
  AUDIT_SUCCESS        boolean                  NOT NULL,
  AUDIT_FAILURE        boolean                  NOT NULL
);


CREATE TABLE IPA_ACL_OBJECT_IDENTITY
(
  ID                  BIGSERIAL                 NOT NULL,
  OBJECT_ID_CLASS     bigint                    NOT NULL,
  OBJECT_ID_IDENTITY  bigint                    NOT NULL,
  PARENT_OBJECT       bigint,
  OWNER_SID           bigint                    NOT NULL,
  ENTRIES_INHERITING  boolean                   NOT NULL
);

CREATE TABLE IPA_CONT_COMMENT
(
  ID                   BIGINT                   NOT NULL,
  CONTINGUT_ID         bigint 		        NOT NULL,
  TEXT		       character varying (1024),
  CREATEDDATE          timestamp without time zone,
  LASTMODIFIEDDATE     timestamp without time zone,
  CREATEDBY_CODI       character varying(64),
  LASTMODIFIEDBY_CODI  character varying(64)
);

CREATE TABLE IPA_EXECUCIO_MASSIVA
(
  ID                   BIGINT   		NOT NULL,
  TIPUS                character varying(255)   NOT NULL,
  DATA_INICI	       timestamp without time zone,
  DATA_FI              timestamp without time zone,
  PFIRMES_MOTIU	       character varying(256),
  PFIRMES_PRIORI       character varying(255),
  PFIRMES_DATCAD       timestamp without time zone,
  ENVIAR_CORREU	       boolean,
  ENTITAT_ID	       bigint		NOT NULL,
  CREATEDBY_CODI       character varying(64),
  CREATEDDATE          timestamp without time zone,
  LASTMODIFIEDBY_CODI  character varying(64),
  LASTMODIFIEDDATE     timestamp without time zone
);

CREATE TABLE IPA_MASSIVA_CONTINGUT
(
  ID                   BIGINT                   NOT NULL,
  EXECUCIO_MASSIVA_ID  bigint,
  CONTINGUT_ID         bigint,
  DATA_INICI	       timestamp without time zone,
  DATA_FI              timestamp without time zone,
  ESTAT	               character varying(255),
  ERROR	               character varying(2046),
  ORDRE	               bigint,
  CREATEDBY_CODI       character varying(64),
  CREATEDDATE          timestamp without time zone,
  LASTMODIFIEDBY_CODI  character varying(64),
  LASTMODIFIEDDATE     timestamp without time zone
);
