delete from ipa_expedient_interessat;
delete from ipa_interessat;
delete from ipa_cont_log;
delete from ipa_document_notifc;
delete from ipa_document_pfirmes;
delete from ipa_document_versio;
delete from ipa_document;
delete from ipa_expedient_avis;
delete from ipa_expedient;
delete from ipa_carpeta;
delete from ipa_dada;
delete from ipa_node;
delete from ipa_escriptori;
update ipa_contenidor set contmov_id=null;
delete from ipa_cont_mov;
delete from ipa_registre_annex;
delete from ipa_registre_inter;
delete from ipa_registre_mov;
delete from ipa_registre;
delete from ipa_metaexpedient_arxiu;
delete from ipa_arxiu;
delete from ipa_bustia;
update ipa_contenidor set pare_id = null;
delete from ipa_contenidor;

delete from ipa_metacarpeta;
delete from ipa_metaexpedient_metadocument;
delete from ipa_metaexp_seq;
delete from ipa_metaexpedient;
delete from ipa_metadocument;
delete from ipa_metanode_metadada;
delete from ipa_metanode;
delete from ipa_metadada;

delete from ipa_entitat;
delete from ipa_usuari;

delete from ipa_acl_entry;
delete from ipa_acl_object_identity;
delete from ipa_acl_class;
delete from ipa_acl_sid;