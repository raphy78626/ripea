/**
 * 
 */
package es.caib.ripea.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.ForeignKey;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.api.dto.InteressatDocumentTipusEnumDto;
import es.caib.ripea.core.audit.RipeaAuditable;

/**
 * Classe del model de dades que representa un interessat.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(	name = "ipa_interessat",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {
						"entitat_id",
						"nif",
						"nom",
						"llinatges",
						"identificador"})})
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@EntityListeners(AuditingEntityListener.class)
public abstract class InteressatEntity extends RipeaAuditable<Long> {

//	tipus: 					tipus d’interessat (persona física, persona jurídica o administració pública)
//	documentTipus: 			tipus de document d’identitat.
//	documentNum: 			número del document d’identitat.
//	nom: 					nom de l’interessat.
//	llinatge1: 				primer llinatge de l’interessat.
//	llinatge2: 				segon llinatge de l’interessat.
//	raoSocial: 				nom de l’empresa en cas de persona jurídica.
//	organCodi: 				codi DIR3 de l’òrgan en cas de que l’interessat sigui del tipus administració pública.
//	país: 					país de l’interessat.
//	provincia: 				província de l’interessat.
//	municipi: 				municipi de l’interessat.
//	adresa: 				adreça de l’interessat.
//	codiPostal: 			codi postal de l’interessat.
//	email: 					adreça electonica de contacte.
//	telefon: 				telèfon de l’interessat
//	observacions: 			observacions de l’interessat.
//	notificacioIdioma: 		per emmagatzemar l’idioma desitjat per a les notificacions.
//	NotificacioAutoritzat: 	per indicar si l’interessat ha autoritzat la recepció de notificacions en format electrònic.
	
	@Column(name = "document_tipus", length = 256, nullable = false)
	@Enumerated(EnumType.STRING)
	InteressatDocumentTipusEnumDto documentTipus;
	@Column(name = "document_num", length = 17)
	protected String documentNum;
	@Column(name = "document_num", length = 17)
	protected String documentNum;
	@Column(name = "document_num", length = 17)
	protected String documentNum;
	@Column(name = "document_num", length = 17)
	protected String documentNum;
	@Column(name = "document_num", length = 17)
	protected String documentNum;
	@Column(name = "document_num", length = 17)
	protected String documentNum;
	@Column(name = "document_num", length = 17)
	protected String documentNum;
	@Column(name = "document_num", length = 17)
	protected String documentNum;
	@Column(name = "document_num", length = 17)
	protected String documentNum;
	@Column(name = "document_num", length = 17)
	protected String documentNum;
	@Column(name = "document_num", length = 17)
	protected String documentNum;
	
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "entitat_id")
	@ForeignKey(name = "ipa_entitat_interessat_fk")
	protected EntitatEntity entitat;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "expedient_id")
	@ForeignKey(name = "ipa_expedient_interessat_fk")
	protected ExpedientEntity expedient;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "representant_id")
	@ForeignKey(name = "ipa_interessat_interessat_fk")
	protected InteressatEntity representant;
	@Version
	private long version = 0;



	public String getNom() {
		return nom;
	}
	public EntitatEntity getEntitat() {
		return entitat;
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
