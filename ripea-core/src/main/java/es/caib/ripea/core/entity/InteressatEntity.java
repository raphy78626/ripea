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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.ForeignKey;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.api.dto.IndiomaEnumDto;
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
						"expedient_id",
						"document_num",
						"nom",
						"llinatge1",
						"llinatge2",
						"rao_social",
						"organ_codi"})})
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@EntityListeners(AuditingEntityListener.class)
public abstract class InteressatEntity extends RipeaAuditable<Long> {

//	CAMP					TIPUS INTERESSAT	DESCRIPCIÓ
//	------------------------------------------------------------------------------------------------------------------------------------
//	tipus: 					COMÚ				tipus d’interessat (persona física, persona jurídica o administració pública)
//	documentTipus: 			COMÚ				tipus de document d’identitat.
//	documentNum: 			COMÚ				número del document d’identitat.
//	nom: 					FÍSICA				nom de l’interessat.
//	llinatge1: 				FÍSICA				primer llinatge de l’interessat.
//	llinatge2: 				FÍSICA				segon llinatge de l’interessat.
//	raoSocial: 				JURÍDICA			nom de l’empresa en cas de persona jurídica.
//	organCodi: 				ADMINISTRACIÓ		codi DIR3 de l’òrgan en cas de que l’interessat sigui del tipus administració pública.
//	país: 					COMÚ				país de l’interessat.
//	provincia: 				COMÚ				província de l’interessat.
//	municipi: 				COMÚ				municipi de l’interessat.
//	adresa: 				COMÚ				adreça de l’interessat.
//	codiPostal: 			COMÚ				codi postal de l’interessat.
//	email: 					COMÚ				adreça electonica de contacte.
//	telefon: 				COMÚ				telèfon de l’interessat
//	observacions: 			COMÚ				observacions de l’interessat.
//	notificacioIdioma: 		COMÚ				per emmagatzemar l’idioma desitjat per a les notificacions.
//	NotificacioAutoritzat: 	COMÚ				per indicar si l’interessat ha autoritzat la recepció de notificacions en format electrònic.
	
	@Column(name = "document_tipus", length = 40)
	@Enumerated(EnumType.STRING)
	protected InteressatDocumentTipusEnumDto documentTipus;
	@Column(name = "document_num", length = 17)
	protected String documentNum;
	@Column(name = "pais", length = 4)
	protected String pais;
	@Column(name = "provincia", length = 2)
	protected String provincia;
	@Column(name = "municipi", length = 5)
	protected String municipi;
	@Column(name = "adresa", length = 160)
	protected String adresa;
	@Column(name = "codi_postal", length = 5)
	protected String codiPostal;
	@Column(name = "email", length = 160)
	protected String email;
	@Column(name = "telefon", length = 20)
	protected String telefon;
	@Column(name = "observacions", length = 160)
	protected String observacions;
	@Column(name = "not_idioma", length = 2)
	@Enumerated(EnumType.STRING)
	protected IndiomaEnumDto notificacioIdioma;
	@Column(name = "not_autoritzat")
	protected Boolean notificacioAutoritzat;
	
	@Column(name = "identificador", length = 80)
	protected String identificador;
	@Column(name = "es_representant")
	protected Boolean esRepresentant;
	
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
	
	@Transient
	private Long representantId;
	@Transient
	private Long representantIdentificador;

	public InteressatDocumentTipusEnumDto getDocumentTipus() {
		return documentTipus;
	}
	public void setDocumentTipus(InteressatDocumentTipusEnumDto documentTipus) {
		this.documentTipus = documentTipus;
	}

	public String getDocumentNum() {
		return documentNum;
	}
	public void setDocumentNum(String documentNum) {
		this.documentNum = documentNum;
	}

	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getMunicipi() {
		return municipi;
	}
	public void setMunicipi(String municipi) {
		this.municipi = municipi;
	}

	public String getAdresa() {
		return adresa;
	}
	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getCodiPostal() {
		return codiPostal;
	}
	public void setCodiPostal(String codiPostal) {
		this.codiPostal = codiPostal;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefon() {
		return telefon;
	}
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getObservacions() {
		return observacions;
	}
	public void setObservacions(String observacions) {
		this.observacions = observacions;
	}

	public IndiomaEnumDto getNotificacioIdioma() {
		return notificacioIdioma;
	}
	public void setNotificacioIdioma(IndiomaEnumDto notificacioIdioma) {
		this.notificacioIdioma = notificacioIdioma;
	}

	public Boolean getNotificacioAutoritzat() {
		return notificacioAutoritzat;
	}
	public void setNotificacioAutoritzat(Boolean notificacioAutoritzat) {
		this.notificacioAutoritzat = notificacioAutoritzat;
	}

	public ExpedientEntity getExpedient() {
		return expedient;
	}
	public void setExpedient(ExpedientEntity expedient) {
		this.expedient = expedient;
	}

	public InteressatEntity getRepresentant() {
		return representant;
	}
	public void setRepresentant(InteressatEntity representant) {
		this.representant = representant;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
	
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	
	public Long getRepresentantId() {
		Long representantId = null;
		if (representant != null) {
			representantId = representant.getId();
		}
		return representantId;
	}
	public String getRepresentantIdentificador() {
		String representantIdentificador = "";
		if (representant != null) {
			representantIdentificador = representant.getIdentificador();
		}
		return representantIdentificador;
	}

	public Boolean getEsRepresentant() {
		return esRepresentant;
	}
	public void setEsRepresentant(Boolean esRepresentant) {
		this.esRepresentant = esRepresentant;
	}
	
	@PreUpdate
	@PrePersist
	public abstract void updateIdentificador();
	
	
//	public void setIdentificador(InteressatEntity interessat) {
//		this.updateIdentificador();
//	}

	private static final long serialVersionUID = -2299453443943600172L;

}
