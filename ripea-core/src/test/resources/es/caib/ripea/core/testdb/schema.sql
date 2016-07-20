create table public.ipa_arxiu (actiu bit, unitat_codi varchar(9) not null, id bigint not null, primary key (id))
create table public.ipa_bustia (activa bit, per_defecte bit, unitat_codi varchar(9) not null, id bigint not null, primary key (id))
create table public.ipa_carpeta (tipus varchar(255) not null, id bigint not null, primary key (id))
create table public.ipa_cont_log (id bigint generated by default as identity (start with 1), contenidor_id bigint not null, data timestamp not null, objecte_class varchar(256), objecte_id bigint, param1 varchar(256), param2 varchar(256), subtipus integer, tipus integer not null, usuari_id varchar(255) not null, version bigint not null, primary key (id))
create table public.ipa_cont_mov (id bigint generated by default as identity (start with 1), createdDate timestamp, lastModifiedDate timestamp, comentari varchar(256), data timestamp not null, version bigint not null, createdBy_codi varchar(64), lastModifiedBy_codi varchar(64), contenidor_id bigint not null, desti_id bigint not null, origen_id bigint, remitent_id varchar(64), primary key (id), unique (contenidor_id, desti_id, data))
create table public.ipa_contenidor (id bigint generated by default as identity (start with 1), createdDate timestamp, lastModifiedDate timestamp, esborrat integer, nom varchar(256) not null, tipus_cont integer not null, version bigint not null, createdBy_codi varchar(64), lastModifiedBy_codi varchar(64), contmov_id bigint, entitat_id bigint not null, pare_id bigint, primary key (id), unique (nom, pare_id, tipus_cont, esborrat))
create table public.ipa_dada (id bigint generated by default as identity (start with 1), createdDate timestamp, lastModifiedDate timestamp, ordre integer, valor varchar(256) not null, version bigint not null, createdBy_codi varchar(64), lastModifiedBy_codi varchar(64), metadada_id bigint not null, node_id bigint not null, primary key (id), unique (metadada_id, node_id, ordre))
create table public.ipa_document (darrera_versio integer not null, data timestamp, id bigint not null, expedient_id bigint, primary key (id))
create table public.ipa_document_versio (id bigint generated by default as identity (start with 1), createdDate timestamp, lastModifiedDate timestamp, arxiu_content_length bigint not null, arxiu_content_type varchar(256) not null, arxiu_contingut blob, arxiu_gesdoc_id varchar(100), arxiu_nom varchar(256) not null, versio integer not null, version bigint not null, createdBy_codi varchar(64), lastModifiedBy_codi varchar(64), document_id bigint not null, primary key (id), unique (document_id, versio))
create table public.ipa_entitat (id bigint generated by default as identity (start with 1), createdDate timestamp, lastModifiedDate timestamp, activa bit, cif varchar(9) not null, codi varchar(64) not null, descripcio varchar(1024), nom varchar(256) not null, unitat_arrel varchar(9) not null, version bigint not null, createdBy_codi varchar(64), lastModifiedBy_codi varchar(64), primary key (id), unique (codi))
create table public.ipa_escriptori (id bigint not null, usuari_id varchar(64) not null, primary key (id))
create table public.ipa_expedient (anio integer, obert bit, sequencia bigint, tancat_motiu varchar(1024), id bigint not null, arxiu_id bigint not null, primary key (id))
create table public.ipa_expedient_interessat (expedient_id bigint not null, interessat_id bigint not null, primary key (expedient_id, interessat_id))
create table public.ipa_interessat (DTYPE varchar(31) not null, id bigint generated by default as identity (start with 1), createdDate timestamp, lastModifiedDate timestamp, nom varchar(256) not null, version bigint not null, identificador varchar(9), llinatges varchar(256), nif varchar(9), createdBy_codi varchar(64), lastModifiedBy_codi varchar(64), entitat_id bigint not null, primary key (id), unique (entitat_id, nif, nom, llinatges, identificador))
create table public.ipa_metadada (id bigint generated by default as identity (start with 1), createdDate timestamp, lastModifiedDate timestamp, activa bit, codi varchar(64) not null, descripcio varchar(1024), global_document bit, global_expedient bit, global_multiplicitat varchar(255), global_readonly bit, nom varchar(256) not null, tipus varchar(255) not null, version bigint not null, createdBy_codi varchar(64), lastModifiedBy_codi varchar(64), entitat_id bigint not null, primary key (id), unique (entitat_id, codi))
create table public.ipa_metadocument (custodia_politica varchar(64), global_expedient bit, global_multiplicitat varchar(255), global_readonly bit, plantilla_content_type varchar(256), plantilla_contingut blob, plantilla_nom varchar(256), portafirmes_doctip varchar(64), signatura_tipmime varchar(64), id bigint not null, primary key (id))
create table public.ipa_metaexp_seq (id bigint generated by default as identity (start with 1), createdDate timestamp, lastModifiedDate timestamp, anio integer, valor bigint, createdBy_codi varchar(64), lastModifiedBy_codi varchar(64), meta_expedient_id bigint, primary key (id), unique (anio, meta_expedient_id))
create table public.ipa_metaexpedient (classificacio varchar(30), id bigint not null, pare_id bigint, primary key (id))
create table public.ipa_metaexpedient_metadocument (id bigint generated by default as identity (start with 1), createdDate timestamp, lastModifiedDate timestamp, multiplicitat varchar(255), ordre integer not null, readonly bit, createdBy_codi varchar(64), lastModifiedBy_codi varchar(64), metadocument_id bigint not null, metaexpedient_id bigint not null, primary key (id), unique (metaexpedient_id, metadocument_id))
create table public.ipa_metanode (id bigint generated by default as identity (start with 1), createdDate timestamp, lastModifiedDate timestamp, actiu bit, codi varchar(64) not null, descripcio varchar(1024), nom varchar(256) not null, tipus varchar(255) not null, version bigint not null, createdBy_codi varchar(64), lastModifiedBy_codi varchar(64), entitat_id bigint not null, primary key (id), unique (entitat_id, codi, tipus))
create table public.ipa_metanode_metadada (id bigint generated by default as identity (start with 1), createdDate timestamp, lastModifiedDate timestamp, multiplicitat varchar(255) not null, ordre integer not null, readonly bit, createdBy_codi varchar(64), lastModifiedBy_codi varchar(64), metadada_id bigint not null, metanode_id bigint not null, primary key (id), unique (metanode_id, metadada_id))
create table public.ipa_node (id bigint not null, metanode_id bigint, primary key (id))
create table public.ipa_registre (id bigint generated by default as identity (start with 1), createdDate timestamp, lastModifiedDate timestamp, accio integer not null, aplicacio_emissora varchar(4), assumpte_codi varchar(16), assumpte_numexp varchar(80), assumpte_referencia varchar(16), assumpte_resum varchar(240) not null, data timestamp not null, documentacio_fis integer, entitat_codi varchar(21) not null, entitat_nom varchar(80), motiu_rebuig varchar(1024), numero varchar(20) not null, observacions varchar(50), prova bit, tipus integer not null, transport_num varchar(20), transport_tipus integer, usuari_contacte varchar(160), usuari_nom varchar(80), version bigint not null, createdBy_codi varchar(64), lastModifiedBy_codi varchar(64), contenidor_id bigint, entitat_id bigint not null, primary key (id), unique (entitat_id, accio, tipus, entitat_codi, numero, data))
create table public.ipa_registre_doc (id bigint generated by default as identity (start with 1), createdDate timestamp, lastModifiedDate timestamp, fitxer_nom varchar(80) not null, gesdoc_id varchar(100), identificador varchar(50) not null, docfirmat_id varchar(50), observacions varchar(50), tipus integer not null, validesa integer, version bigint not null, createdBy_codi varchar(64), lastModifiedBy_codi varchar(64), registre_id bigint not null, primary key (id), unique (registre_id, identificador))
create table public.ipa_registre_inter (id bigint generated by default as identity (start with 1), createdDate timestamp, lastModifiedDate timestamp, adresa varchar(160), canal_pref integer, codi_postal varchar(5), doc_num varchar(17), doc_tipus integer, email varchar(160), email_hab varchar(160), llinatge1 varchar(30), llinatge2 varchar(30), municipi varchar(5), nom varchar(30), observacions varchar(160), pais varchar(4), provincia varchar(2), rao_social varchar(80), rep_adresa varchar(160), rep_canal_pref integer, rep_codi_postal varchar(5), rep_doc_num varchar(17), rep_doc_tipus integer, rep_email varchar(160), rep_email_hab varchar(160), rep_llinatge1 varchar(30), rep_llinatge2 varchar(30), rep_municipi varchar(5), rep_nom varchar(30), rep_pais varchar(4), rep_provincia varchar(2), rep_rao_social varchar(80), rep_telefon varchar(20), telefon varchar(20), version bigint not null, createdBy_codi varchar(64), lastModifiedBy_codi varchar(64), registre_id bigint not null, primary key (id), unique (registre_id, doc_tipus, doc_num, nom, llinatge1, llinatge2, rao_social))
create table public.ipa_usuari (codi varchar(64) not null, email varchar(200), inicialitzat bit, nif varchar(9) not null, nom varchar(200), version bigint not null, primary key (codi), unique (nif))
alter table public.ipa_arxiu add constraint FKF57A896EB22417E3 foreign key (id) references public.ipa_contenidor
alter table public.ipa_bustia add constraint FKBBB3A5D1B22417E3 foreign key (id) references public.ipa_contenidor
alter table public.ipa_carpeta add constraint FKCD766DD1B22417E3 foreign key (id) references public.ipa_contenidor
alter table public.ipa_cont_mov add constraint public.ipa_contenidor_contmov_fk foreign key (contenidor_id) references public.ipa_contenidor
alter table public.ipa_cont_mov add constraint FKBF50B30CF10A12AA foreign key (lastModifiedBy_codi) references public.ipa_usuari
alter table public.ipa_cont_mov add constraint public.ipa_origen_contmov_fk foreign key (origen_id) references public.ipa_contenidor
alter table public.ipa_cont_mov add constraint FKBF50B30CE9BD5701 foreign key (createdBy_codi) references public.ipa_usuari
alter table public.ipa_cont_mov add constraint public.ipa_remitent_contmov_fk foreign key (remitent_id) references public.ipa_usuari
alter table public.ipa_cont_mov add constraint public.ipa_desti_contmov_fk foreign key (desti_id) references public.ipa_contenidor
alter table public.ipa_contenidor add constraint public.ipa_contmov_contenidor_fk foreign key (contmov_id) references public.ipa_cont_mov
alter table public.ipa_contenidor add constraint FK38385B5EF10A12AA foreign key (lastModifiedBy_codi) references public.ipa_usuari
alter table public.ipa_contenidor add constraint public.ipa_pare_contenidor_fk foreign key (pare_id) references public.ipa_contenidor
alter table public.ipa_contenidor add constraint FK38385B5EE9BD5701 foreign key (createdBy_codi) references public.ipa_usuari
alter table public.ipa_contenidor add constraint public.ipa_entitat_contenidor_fk foreign key (entitat_id) references public.ipa_entitat
alter table public.ipa_dada add constraint public.ipa_node_dada_fk foreign key (node_id) references public.ipa_node
alter table public.ipa_dada add constraint FK6B050EBFF10A12AA foreign key (lastModifiedBy_codi) references public.ipa_usuari
alter table public.ipa_dada add constraint FK6B050EBFE9BD5701 foreign key (createdBy_codi) references public.ipa_usuari
alter table public.ipa_dada add constraint public.ipa_metadada_dada_fk foreign key (metadada_id) references public.ipa_metadada
alter table public.ipa_document add constraint public.ipa_expedient_document_fk foreign key (expedient_id) references public.ipa_expedient
alter table public.ipa_document add constraint FK1480E7A06B8AA20C foreign key (id) references public.ipa_node
alter table public.ipa_document_versio add constraint public.ipa_document_docversio_fk foreign key (document_id) references public.ipa_document
alter table public.ipa_document_versio add constraint FK6849EFD5F10A12AA foreign key (lastModifiedBy_codi) references public.ipa_usuari
alter table public.ipa_document_versio add constraint FK6849EFD5E9BD5701 foreign key (createdBy_codi) references public.ipa_usuari
alter table public.ipa_entitat add constraint FK4D8B1AE4F10A12AA foreign key (lastModifiedBy_codi) references public.ipa_usuari
alter table public.ipa_entitat add constraint FK4D8B1AE4E9BD5701 foreign key (createdBy_codi) references public.ipa_usuari
alter table public.ipa_escriptori add constraint FK985850BBB22417E3 foreign key (id) references public.ipa_contenidor
alter table public.ipa_escriptori add constraint public.ipa_usuari_escriptori_fk foreign key (usuari_id) references public.ipa_usuari
alter table public.ipa_expedient add constraint FK4AB163796B8AA20C foreign key (id) references public.ipa_node
alter table public.ipa_expedient add constraint public.ipa_arxiu_expedient_fk foreign key (arxiu_id) references public.ipa_arxiu
alter table public.ipa_expedient_interessat add constraint FK42AC9A2CD06A191 foreign key (expedient_id) references public.ipa_expedient
alter table public.ipa_expedient_interessat add constraint FK42AC9A29BC5B6A9 foreign key (interessat_id) references public.ipa_interessat
alter table public.ipa_interessat add constraint FKC92B1661F10A12AA foreign key (lastModifiedBy_codi) references public.ipa_usuari
alter table public.ipa_interessat add constraint FKC92B1661E9BD5701 foreign key (createdBy_codi) references public.ipa_usuari
alter table public.ipa_interessat add constraint public.ipa_entitat_interessat_fk foreign key (entitat_id) references public.ipa_entitat
alter table public.ipa_metadada add constraint FKC6518FC4F10A12AA foreign key (lastModifiedBy_codi) references public.ipa_usuari
alter table public.ipa_metadada add constraint FKC6518FC4E9BD5701 foreign key (createdBy_codi) references public.ipa_usuari
alter table public.ipa_metadada add constraint public.ipa_entitat_metadada_fk foreign key (entitat_id) references public.ipa_entitat
alter table public.ipa_metadocument add constraint FK3EB5E2546857BF1 foreign key (id) references public.ipa_metanode
alter table public.ipa_metaexp_seq add constraint FKEA38C8F3F10A12AA foreign key (lastModifiedBy_codi) references public.ipa_usuari
alter table public.ipa_metaexp_seq add constraint FKEA38C8F3E9BD5701 foreign key (createdBy_codi) references public.ipa_usuari
alter table public.ipa_metaexp_seq add constraint public.ipa_metaexp_metaexpseq_fk foreign key (meta_expedient_id) references public.ipa_metaexpedient
alter table public.ipa_metaexpedient add constraint FK4895BD9446857BF1 foreign key (id) references public.ipa_metanode
alter table public.ipa_metaexpedient add constraint public.ipa_pare_metaexp_fk foreign key (pare_id) references public.ipa_metaexpedient
alter table public.ipa_metaexpedient_metadocument add constraint public.ipa_metadoc_metaexpdoc_fk foreign key (metadocument_id) references public.ipa_metadocument
alter table public.ipa_metaexpedient_metadocument add constraint FKFDAD6EEBF10A12AA foreign key (lastModifiedBy_codi) references public.ipa_usuari
alter table public.ipa_metaexpedient_metadocument add constraint public.ipa_metaexp_metaexpdoc_fk foreign key (metaexpedient_id) references public.ipa_metaexpedient
alter table public.ipa_metaexpedient_metadocument add constraint FKFDAD6EEBE9BD5701 foreign key (createdBy_codi) references public.ipa_usuari
alter table public.ipa_metanode add constraint FKC656500CF10A12AA foreign key (lastModifiedBy_codi) references public.ipa_usuari
alter table public.ipa_metanode add constraint FKC656500CE9BD5701 foreign key (createdBy_codi) references public.ipa_usuari
alter table public.ipa_metanode add constraint public.ipa_entitat_metanode_fk foreign key (entitat_id) references public.ipa_entitat
alter table public.ipa_metanode_metadada add constraint public.ipa_metanode_metadadanode_fk foreign key (metanode_id) references public.ipa_metanode
alter table public.ipa_metanode_metadada add constraint FK4BC7A812F10A12AA foreign key (lastModifiedBy_codi) references public.ipa_usuari
alter table public.ipa_metanode_metadada add constraint FK4BC7A812E9BD5701 foreign key (createdBy_codi) references public.ipa_usuari
alter table public.ipa_metanode_metadada add constraint public.ipa_metadada_metadadanode_fk foreign key (metadada_id) references public.ipa_metadada
alter table public.ipa_node add constraint public.ipa_metanode_node_fk foreign key (metanode_id) references public.ipa_metanode
alter table public.ipa_node add constraint FK6B09CF07B22417E3 foreign key (id) references public.ipa_contenidor
alter table public.ipa_registre add constraint public.ipa_contenidor_registre_fk foreign key (contenidor_id) references public.ipa_contenidor
alter table public.ipa_registre add constraint FKB80046AEF10A12AA foreign key (lastModifiedBy_codi) references public.ipa_usuari
alter table public.ipa_registre add constraint FKB80046AEE9BD5701 foreign key (createdBy_codi) references public.ipa_usuari
alter table public.ipa_registre add constraint public.ipa_entitat_registre_fk foreign key (entitat_id) references public.ipa_entitat
alter table public.ipa_registre_doc add constraint FK9C2DF427F10A12AA foreign key (lastModifiedBy_codi) references public.ipa_usuari
alter table public.ipa_registre_doc add constraint public.ipa_document_registre_fk foreign key (registre_id) references public.ipa_registre
alter table public.ipa_registre_doc add constraint FK9C2DF427E9BD5701 foreign key (createdBy_codi) references public.ipa_usuari
alter table public.ipa_registre_inter add constraint FK48C7D40BF10A12AA foreign key (lastModifiedBy_codi) references public.ipa_usuari
alter table public.ipa_registre_inter add constraint public.ipa_interessat_registre_fk foreign key (registre_id) references public.ipa_registre
alter table public.ipa_registre_inter add constraint FK48C7D40BE9BD5701 foreign key (createdBy_codi) references public.ipa_usuari
create table public.ipa_acl_sid (id bigint generated by default as identity(start with 100) not null primary key,principal boolean not null,sid varchar_ignorecase(100) not null,constraint public.ipa_sid_uk unique(sid,principal))
create table public.ipa_acl_class (id bigint generated by default as identity(start with 100) not null primary key,class varchar_ignorecase(100) not null,constraint public.ipa_class_uk unique(class))
create table public.ipa_acl_object_identity (id bigint generated by default as identity(start with 100) not null primary key,object_id_class bigint not null,object_id_identity bigint not null,parent_object bigint,owner_sid bigint not null,entries_inheriting boolean not null,constraint public.ipa_oid_uk unique(object_id_class,object_id_identity),constraint public.ipa_oid_oid_fk foreign key(parent_object)references public.ipa_acl_object_identity(id),constraint public.ipa_class_oid_fk foreign key(object_id_class)references public.ipa_acl_class(id),constraint public.ipa_sid_oid_fk foreign key(owner_sid)references public.ipa_acl_sid(id))
create table public.ipa_acl_entry (id bigint generated by default as identity(start with 100) not null primary key,acl_object_identity bigint not null,ace_order int not null,sid bigint not null,mask integer not null,granting boolean not null,audit_success boolean not null,audit_failure boolean not null,constraint public.ipa_entry_uk unique(acl_object_identity,ace_order),constraint public.ipa_oid_entry_fk foreign key(acl_object_identity) references public.ipa_acl_object_identity(id),constraint public.ipa_sid_entry_fk foreign key(sid) references public.ipa_acl_sid(id))