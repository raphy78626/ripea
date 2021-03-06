/*CREATE INDEX IPA_USUARI_PK_I ON IPA_USUARI(CODI);
CREATE INDEX IPA_USUARI_NIF_UK_I ON IPA_USUARI(NIF);

CREATE INDEX IPA_ENTITAT_PK_I ON IPA_ENTITAT(ID);
CREATE INDEX IPA_ENTITAT_CODI_UK_I ON IPA_ENTITAT(CODI);

CREATE INDEX IPA_METANODE_PK_I ON IPA_METANODE(ID);
CREATE INDEX IPA_METANODE_MULT_UK_I ON IPA_METANODE(ENTITAT_ID, CODI, TIPUS);

CREATE INDEX IPA_METADADA_PK_I ON IPA_METADADA(ID);
CREATE INDEX IPA_METADADA_ENTI_CODI_UK_I ON IPA_METADADA(ENTITAT_ID, CODI);

 CREATE INDEX IPA_METADOCUMENT_PK_I ON IPA_METADOCUMENT(ID);

CREATE INDEX IPA_METAEXPEDIENT_PK_I ON IPA_METAEXPEDIENT(ID);

CREATE INDEX IPA_METAEXP_SEQ_PK_I ON IPA_METAEXP_SEQ(ID);
CREATE INDEX IPA_METAEXP_SEQ_MULT_UK_I ON IPA_METAEXP_SEQ(ANIO, META_EXPEDIENT_ID);

CREATE INDEX IPA_INTERESSAT_PK_I ON IPA_INTERESSAT(ID);
CREATE INDEX IPA_INTERESSAT_MULT_UK_I ON IPA_INTERESSAT(ENTITAT_ID, NIF, NOM, LLINATGES, IDENTIFICADOR);

CREATE INDEX IPA_ARXIU_PK_I ON IPA_ARXIU(ID);

CREATE INDEX IPA_CONTENIDOR_PK_I ON IPA_CONTENIDOR(ID);
CREATE INDEX IPA_CONTENIDOR_MULT_UK_I ON IPA_CONTENIDOR(NOM, PARE_ID, TIPUS_CONT, ESBORRAT);

CREATE INDEX IPA_CONTMOV_PK_I ON IPA_CONT_MOV(ID);
CREATE INDEX IPA_CONTMOV_MULT_UK_I ON IPA_CONT_MOV(CONTENIDOR_ID, DESTI_ID, DATA);

CREATE INDEX IPA_ESCRIPTORI_PK_I ON IPA_ESCRIPTORI(ID);

CREATE INDEX IPA_MEXP_MDOC_PK_I ON IPA_METAEXPEDIENT_METADOCUMENT(ID);
CREATE INDEX IPA_MEXP_MDOC_MULT_UK_I ON IPA_METAEXPEDIENT_METADOCUMENT(METAEXPEDIENT_ID, METADOCUMENT_ID);

CREATE INDEX IPA_NODE_PK_I ON IPA_NODE(ID);

CREATE INDEX IPA_MNODE_MDADA_PK_I ON IPA_METANODE_METADADA(ID);
CREATE INDEX IPA_MNODE_MDADA_MNOD_MDAD_UK_I ON IPA_METANODE_METADADA(METANODE_ID, METADADA_ID);

CREATE INDEX IPA_CARPETA_PK_I ON IPA_CARPETA(ID);

CREATE INDEX IPA_DADA_PK_I ON IPA_DADA(ID);
CREATE INDEX IPA_DADA_MULT_UK_I ON IPA_DADA(METADADA_ID, NODE_ID, ORDRE);

CREATE INDEX IPA_EXPEDIENT_PK_I ON IPA_EXPEDIENT(ID);

CREATE INDEX IPA_DOCUMENT_PK_I ON IPA_DOCUMENT(ID);

CREATE INDEX IPA_DOC_VER_PK_I ON IPA_DOCUMENT_VERSIO(ID);
CREATE INDEX IPA_DOC_VER_DOC_VER_UK_I ON IPA_DOCUMENT_VERSIO(DOCUMENT_ID, VERSIO);

CREATE INDEX IPA_EXP_INT_PK_I ON IPA_EXPEDIENT_INTERESSAT(EXPEDIENT_ID, INTERESSAT_ID);

CREATE INDEX IPA_BUSTIA_PK_I ON IPA_BUSTIA(ID);

CREATE INDEX IPA_CONT_LOG_PK_I ON IPA_CONT_LOG(ID);

CREATE INDEX IPA_NODE_LOG_PK_I ON IPA_NODE_LOG(ID);

CREATE INDEX IPA_METACARPETA_PK_I ON IPA_METACARPETA(ID);

CREATE INDEX IPA_MDAD_MNOD_PK_I ON IPA_METADADA_METANODE(ID);
CREATE INDEX IPA_MDAD_MNOD_MULT_PK_I ON IPA_METADADA_METANODE(METAEXPEDIENT_ID, METADOCUMENT_ID);

CREATE INDEX IPA_REGISTRE_PK_I ON IPA_REGISTRE(ID);
CREATE INDEX IPA_REG_MULT_UK_I ON IPA_REGISTRE(ENTITAT_ID, ACCIO, TIPUS, ENTITAT_CODI, NUMERO, DATA);

CREATE INDEX IPA_REG_DOC_PK_I ON IPA_REGISTRE_DOC(ID);
CREATE INDEX IPA_REG_DOC_MULT_UK_I ON IPA_REGISTRE_DOC(REGISTRE_ID, IDENTIFICADOR);

CREATE INDEX IPA_REG_INT_PK_I ON IPA_REGISTRE_INTER(ID);
CREATE INDEX IPA_REG_INT_MULT_UK_I ON IPA_REGISTRE_INTER(REGISTRE_ID, DOC_TIPUS, DOC_NUM, NOM, LLINATGE1, LLINATGE2, RAO_SOCIAL);*/




CREATE INDEX IPA_USUCRE_ENTITAT_FK_I ON IPA_ENTITAT(CREATEDBY_CODI);
CREATE INDEX IPA_USUMOD_ENTITAT_FK_I ON IPA_ENTITAT(LASTMODIFIEDBY_CODI);

CREATE INDEX IPA_ENTITAT_METANODE_FK_I ON IPA_METANODE(ENTITAT_ID);
CREATE INDEX IPA_USUCRE_METANODE_FK_I ON IPA_METANODE(CREATEDBY_CODI);
CREATE INDEX IPA_USUMOD_METANODE_FK_I ON IPA_METANODE(LASTMODIFIEDBY_CODI);

CREATE INDEX IPA_ENTITAT_METADADA_FK_I ON IPA_METADADA(ENTITAT_ID);
CREATE INDEX IPA_USUCRE_METADADA_FK_I ON IPA_METADADA(CREATEDBY_CODI);
CREATE INDEX IPA_USUMOD_METADADA_FK_I ON IPA_METADADA(LASTMODIFIEDBY_CODI);

--CREATE INDEX IPA_METANODE_METADOC_FK_I ON IPA_METADOCUMENT(ID);

CREATE INDEX IPA_PARE_METAEXP_FK_I ON IPA_METAEXPEDIENT(PARE_ID);
--CREATE INDEX IPA_METANODE_METAEXP_FK_I ON IPA_METAEXPEDIENT(ID);

CREATE INDEX IPA_METAEXP_METAEXPSEQ_FK_I ON IPA_METAEXP_SEQ(META_EXPEDIENT_ID);
CREATE INDEX IPA_USUCRE_METAEXPSEQ_FK_I ON IPA_METAEXP_SEQ(CREATEDBY_CODI);
CREATE INDEX IPA_USUMOD_METAEXPSEQ_FK_I ON IPA_METAEXP_SEQ(LASTMODIFIEDBY_CODI);

CREATE INDEX IPA_ENTITAT_INTERESSAT_FK_I ON IPA_INTERESSAT(ENTITAT_ID);
CREATE INDEX IPA_USUCRE_INTERESSAT_FK_I ON IPA_INTERESSAT(CREATEDBY_CODI);
CREATE INDEX IPA_USUMOD_INTERESSAT_FK_I ON IPA_INTERESSAT(LASTMODIFIEDBY_CODI);

--CREATE INDEX IPA_CONTENIDOR_ARXIU_FK_I ON IPA_ARXIU(ID);

CREATE INDEX IPA_CONTMOV_CONTENIDOR_FK_I ON IPA_CONTENIDOR(CONTMOV_ID);
CREATE INDEX IPA_ENTITAT_CONTENIDOR_FK_I ON IPA_CONTENIDOR(ENTITAT_ID);
CREATE INDEX IPA_USUCRE_CONTENIDOR_FK_I ON IPA_CONTENIDOR(CREATEDBY_CODI);
CREATE INDEX IPA_PARE_CONTENIDOR_FK_I ON IPA_CONTENIDOR(PARE_ID);
CREATE INDEX IPA_USUMOD_CONTENIDOR_FK_I ON IPA_CONTENIDOR(LASTMODIFIEDBY_CODI);

CREATE INDEX IPA_CONTENIDOR_CONTMOV_FK_I ON IPA_CONT_MOV(CONTENIDOR_ID);
CREATE INDEX IPA_ORIGEN_CONTMOV_FK_I ON IPA_CONT_MOV(ORIGEN_ID);
CREATE INDEX IPA_DESTI_CONTMOV_FK_I ON IPA_CONT_MOV(DESTI_ID);
CREATE INDEX IPA_REMITENT_CONTMOV_FK_I ON IPA_CONT_MOV(REMITENT_ID);
CREATE INDEX IPA_USUCRE_CONTMOV_FK_I ON IPA_CONT_MOV(CREATEDBY_CODI);
CREATE INDEX IPA_USUMOD_CONTMOV_FK_I ON IPA_CONT_MOV(LASTMODIFIEDBY_CODI);

CREATE INDEX IPA_CONT_CONTLOG_I ON IPA_CONT_LOG(CONTENIDOR_ID);

CREATE INDEX IPA_USUARI_ESCRIPTORI_FK_I ON IPA_ESCRIPTORI(USUARI_ID);
--CREATE INDEX IPA_CONTENIDOR_ESCRIPTORI_FK_I ON IPA_ESCRIPTORI(ID);

CREATE INDEX IPA_USUCRE_METAEXPDOC_FK_I ON IPA_METAEXPEDIENT_METADOCUMENT(CREATEDBY_CODI);
CREATE INDEX IPA_METAEXP_METAEXPDOC_FK_I ON IPA_METAEXPEDIENT_METADOCUMENT(METAEXPEDIENT_ID);
CREATE INDEX IPA_USUMOD_METAEXPDOC_FK_I ON IPA_METAEXPEDIENT_METADOCUMENT(LASTMODIFIEDBY_CODI);
CREATE INDEX IPA_METADOC_METAEXPDOC_FK_I ON IPA_METAEXPEDIENT_METADOCUMENT(METADOCUMENT_ID);

--CREATE INDEX IPA_CONTENIDOR_NODE_FK_I ON IPA_NODE(ID);
CREATE INDEX IPA_METANODE_NODE_FK_I ON IPA_NODE(METANODE_ID);

CREATE INDEX IPA_METADADA_METADADANODE_FK_I ON IPA_METANODE_METADADA(METADADA_ID);
CREATE INDEX IPA_USUCRE_METADADANODE_FK_I ON IPA_METANODE_METADADA(CREATEDBY_CODI);
CREATE INDEX IPA_USUMOD_METADADANODE_FK_I ON IPA_METANODE_METADADA(LASTMODIFIEDBY_CODI);
CREATE INDEX IPA_METANODE_METADADANODE_FK_I ON IPA_METANODE_METADADA(METANODE_ID);

--CREATE INDEX IPA_CONTENIDOR_CARPETA_FK_I ON IPA_CARPETA(ID);

CREATE INDEX IPA_METADADA_DADA_FK_I ON IPA_DADA(METADADA_ID);
CREATE INDEX IPA_USUCRE_DADA_FK_I ON IPA_DADA(CREATEDBY_CODI);
CREATE INDEX IPA_USUMOD_DADA_FK_I ON IPA_DADA(LASTMODIFIEDBY_CODI);
CREATE INDEX IPA_NODE_DADA_FK_I ON IPA_DADA(NODE_ID);

CREATE INDEX IPA_ARXIU_EXPEDIENT_FK_I ON IPA_EXPEDIENT(ARXIU_ID);
--CREATE INDEX IPA_NODE_EXPEDIENT_FK_I ON IPA_EXPEDIENT(ID);

--CREATE INDEX IPA_NODE_DOCUMENT_FK_I ON IPA_DOCUMENT(ID);
CREATE INDEX IPA_EXPEDIENT_DOCUMENT_FK_I ON IPA_DOCUMENT(EXPEDIENT_ID);

CREATE INDEX IPA_USUCRE_DOCVERSIO_FK_I ON IPA_DOCUMENT_VERSIO(CREATEDBY_CODI);
CREATE INDEX IPA_USUMOD_DOCVERSIO_FK_I ON IPA_DOCUMENT_VERSIO(LASTMODIFIEDBY_CODI);
CREATE INDEX IPA_DOCUMENT_DOCVERSIO_FK_I ON IPA_DOCUMENT_VERSIO(DOCUMENT_ID);

CREATE INDEX IPA_INTERESSAT_EXPINTER_FK_I ON IPA_EXPEDIENT_INTERESSAT(INTERESSAT_ID);
CREATE INDEX IPA_EXPEDIENT_EXPINTER_FK_I ON IPA_EXPEDIENT_INTERESSAT(EXPEDIENT_ID);

--CREATE INDEX IPA_CONTENIDOR_BUSTIA_FK_I ON IPA_BUSTIA(ID);

CREATE INDEX IPA_ENTITAT_REGISTRE_FK_I ON IPA_REGISTRE(ENTITAT_ID);
CREATE INDEX IPA_USUCRE_REGISTRE_FK_I ON IPA_REGISTRE(CREATEDBY_CODI);
CREATE INDEX IPA_USUMOD_REGISTRE_FK_I ON IPA_REGISTRE(LASTMODIFIEDBY_CODI);
CREATE INDEX IPA_CONTENIDOR_REGISTRE_FK_I ON IPA_REGISTRE(CONTENIDOR_ID);

CREATE INDEX IPA_USUCRE_REGDOC_FK_I ON IPA_REGISTRE_DOC(CREATEDBY_CODI);
CREATE INDEX IPA_REGISTRE_REGDOC_FK_I ON IPA_REGISTRE_DOC(REGISTRE_ID);
CREATE INDEX IPA_USUMOD_REGDOC_FK_I ON IPA_REGISTRE_DOC(LASTMODIFIEDBY_CODI);

CREATE INDEX IPA_USUCRE_REGINT_FK_I ON IPA_REGISTRE_INTER(CREATEDBY_CODI);
CREATE INDEX IPA_REGISTRE_REGINT_FK_I ON IPA_REGISTRE_INTER(REGISTRE_ID);
CREATE INDEX IPA_USUMOD_REGINT_FK_I ON IPA_REGISTRE_INTER(LASTMODIFIEDBY_CODI);




/*CREATE INDEX IPA_ACL_CLASS_PK_I ON IPA_ACL_CLASS(ID);
CREATE INDEX IPA_ACL_CLASS_CLASS_UK_I ON IPA_ACL_CLASS(CLASS);

CREATE INDEX IPA_ACL_ENTRY_PK_I ON IPA_ACL_ENTRY(ID);
CREATE INDEX IPA_ACL_ENTRY_IDENT_ORDER_UK_I ON IPA_ACL_ENTRY(ACL_OBJECT_IDENTITY, ACE_ORDER);

CREATE INDEX IPA_ACL_OID_PK_I ON IPA_ACL_OBJECT_IDENTITY(ID);
CREATE INDEX IPA_ACL_IOD_CLASS_IDENTITY_UK_I ON IPA_ACL_OBJECT_IDENTITY(OBJECT_ID_CLASS, OBJECT_ID_IDENTITY);

CREATE INDEX IPA_ACL_SID_PK_I ON IPA_ACL_SID(ID);
CREATE INDEX IPA_ACL_SID_PRINCIPAL_SID_UK_I ON IPA_ACL_SID(SID, PRINCIPAL);*/

CREATE INDEX IPA_ACL_OID_ENTRY_FK_I ON IPA_ACL_ENTRY(ACL_OBJECT_IDENTITY);
CREATE INDEX IPA_ACL_SID_ENTRY_FK_I ON IPA_ACL_ENTRY(SID);

CREATE INDEX IPA_ACL_CLASS_OID_FK_I ON IPA_ACL_OBJECT_IDENTITY(OBJECT_ID_CLASS);
CREATE INDEX IPA_ACL_PARENT_OID_FK_I ON IPA_ACL_OBJECT_IDENTITY(PARENT_OBJECT);

CREATE INDEX IPA_ACL_SID_OID_FK_I ON IPA_ACL_OBJECT_IDENTITY(OWNER_SID);
