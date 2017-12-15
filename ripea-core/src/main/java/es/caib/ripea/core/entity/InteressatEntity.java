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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.ForeignKey;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.api.dto.InteressatDocumentTipusEnumDto;
import es.caib.ripea.core.api.dto.InteressatIdiomaEnumDto;
import es.caib.ripea.core.audit.RipeaAuditable;
import es.caib.ripea.plugin.notificacio.NotificacioEntregaPostalViaTipusEnum;

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

	@Column(name = "document_tipus", length = 1)
	@Enumerated(EnumType.STRING)
	protected InteressatDocumentTipusEnumDto documentTipus;
	@Column(name = "document_num", length = 17)
	protected String documentNum;
	@Column(name = "email", length = 160)
	protected String email;
	@Column(name = "telefon", length = 20)
	protected String telefon;
	@Column(name = "observacions", length = 160)
	protected String observacions;
	@Column(name = "not_idioma", length = 2)
	@Enumerated(EnumType.STRING)
	protected InteressatIdiomaEnumDto preferenciaIdioma;
	@Column(name = "not_autoritzat")
	protected boolean notificacioAutoritzat;

	@Column(name = "es_representant")
	protected boolean esRepresentant;

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
	
	
	@Column(name = "dom_tipus", length = 30)
	protected String domiciliTipusEnum;
	@Column(name = "dom_apartat", length = 10)
	protected String domiciliApartatCorreus;
	@Column(name = "dom_bloc", length = 50)
	protected String domiciliBloc;
	@Column(name = "dom_cie")
	protected Integer domiciliCie;
	@Column(name = "dom_codi_postal", length = 10)
	protected String domiciliCodiPostal;
	@Column(name = "dom_complem", length = 250)
	protected String domiciliComplement;
	@Column(name = "dom_escala", length = 50)
	protected String domiciliEscala;
	@Column(name = "dom_linea1", length = 50)
	protected String domiciliLinea1;
	@Column(name = "dom_linea2", length = 50)
	protected String domiciliLinea2;
	@Column(name = "dom_mun_codine", length = 5)
	protected String domiciliMunicipiCodiIne;
	@Column(name = "dom_num_tipus", length = 30)
	protected String domiciliNumeracioTipus;
	@Column(name = "dom_num_num", length = 10)
	protected String domiciliNumeracioNumero;
	@Column(name = "dom_pai_codiso", length = 3)
	protected String domiciliPaisCodiIso; // ISO-3166
	@Column(name = "dom_planta", length = 50)
	protected String domiciliPlanta;
	@Column(name = "dom_poblacio", length = 30)
	protected String domiciliPoblacio;
	@Column(name = "dom_porta", length = 50)
	protected String domiciliPorta;
	@Column(name = "dom_portal", length = 50)
	protected String domiciliPortal;
	@Column(name = "dom_prv_codi", length = 2)
	protected String domiciliProvinciaCodi;
	@Column(name = "dom_num_puntkm", length = 10)
	protected String domiciliNumeracioPuntKm;
	@Column(name = "dom_via_nom", length = 100)
	protected String domiciliViaNom;
	@Column(name = "dom_via_tipus")
	protected NotificacioEntregaPostalViaTipusEnum domiciliViaTipus;
	
	
	public void updateAdresa(
			String domiciliTipusEnum,
			String domiciliApartatCorreus,
			String domiciliBloc,
			Integer domiciliCie,
			String domiciliCodiPostal,
			String domiciliComplement,
			String domiciliEscala,
			String domiciliLinea1,
			String domiciliLinea2,
			String domiciliMunicipiCodiIne,
			String domiciliNumeracioTipus,
			String domiciliNumeracioNumero,
			String domiciliPaisCodiIso,
			String domiciliPlanta,
			String domiciliPoblacio,
			String domiciliPorta,
			String domiciliPortal,
			String domiciliProvinciaCodi,
			String domiciliNumeracioPuntKm,
			String domiciliViaNom,
			NotificacioEntregaPostalViaTipusEnum domiciliViaTipus) {
		this.domiciliTipusEnum = domiciliTipusEnum;
		this.domiciliApartatCorreus = domiciliApartatCorreus;
		this.domiciliBloc = domiciliBloc;
		this.domiciliCie = domiciliCie;
		this.domiciliCodiPostal = domiciliCodiPostal;
		this.domiciliComplement = domiciliComplement;
		this.domiciliEscala = domiciliEscala;
		this.domiciliLinea1 = domiciliLinea1;
		this.domiciliLinea2 = domiciliLinea2;
		this.domiciliMunicipiCodiIne = domiciliMunicipiCodiIne;
		this.domiciliNumeracioTipus = domiciliNumeracioTipus;
		this.domiciliNumeracioNumero = domiciliNumeracioNumero;
		this.domiciliPaisCodiIso = domiciliPaisCodiIso;
		this.domiciliPlanta = domiciliPlanta;
		this.domiciliPoblacio = domiciliPoblacio;
		this.domiciliPorta = domiciliPorta;
		this.domiciliPortal = domiciliPortal;
		this.domiciliProvinciaCodi = domiciliProvinciaCodi;
		this.domiciliNumeracioPuntKm = domiciliNumeracioPuntKm;
		this.domiciliViaNom = domiciliViaNom;
		this.domiciliViaTipus = domiciliViaTipus;
	}
	
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

	public InteressatIdiomaEnumDto getPreferenciaIdioma() {
		return preferenciaIdioma;
	}
	public void setPreferenciaIdioma(InteressatIdiomaEnumDto preferenciaIdioma) {
		this.preferenciaIdioma = preferenciaIdioma;
	}

	public boolean isNotificacioAutoritzat() {
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
	
	
	
	public String getDomiciliTipusEnum() {
		return domiciliTipusEnum;
	}

	public void setDomiciliTipusEnum(String domiciliTipusEnum) {
		this.domiciliTipusEnum = domiciliTipusEnum;
	}

	public String getDomiciliNumeracioTipus() {
		return domiciliNumeracioTipus;
	}

	public void setDomiciliNumeracioTipus(String domiciliNumeracioTipus) {
		this.domiciliNumeracioTipus = domiciliNumeracioTipus;
	}

	public String getDomiciliApartatCorreus() {
		return domiciliApartatCorreus;
	}
	public void setDomiciliApartatCorreus(String domiciliApartatCorreus) {
		this.domiciliApartatCorreus = domiciliApartatCorreus;
	}
	
	public String getDomiciliBloc() {
		return domiciliBloc;
	}
	public void setDomiciliBloc(String domiciliBloc) {
		this.domiciliBloc = domiciliBloc;
	}
	
	public Integer getDomiciliCie() {
		return domiciliCie;
	}
	public void setDomiciliCie(Integer domiciliCie) {
		this.domiciliCie = domiciliCie;
	}
	
	public String getDomiciliCodiPostal() {
		return domiciliCodiPostal;
	}
	public void setDomiciliCodiPostal(String domiciliCodiPostal) {
		this.domiciliCodiPostal = domiciliCodiPostal;
	}
	
	public String getDomiciliComplement() {
		return domiciliComplement;
	}
	public void setDomiciliComplement(String domiciliComplement) {
		this.domiciliComplement = domiciliComplement;
	}
	
	public String getDomiciliEscala() {
		return domiciliEscala;
	}
	public void setDomiciliEscala(String domiciliEscala) {
		this.domiciliEscala = domiciliEscala;
	}
	
	public String getDomiciliLinea1() {
		return domiciliLinea1;
	}
	public void setDomiciliLinea1(String domiciliLinea1) {
		this.domiciliLinea1 = domiciliLinea1;
	}
	
	public String getDomiciliLinea2() {
		return domiciliLinea2;
	}
	public void setDomiciliLinea2(String domiciliLinea2) {
		this.domiciliLinea2 = domiciliLinea2;
	}
	
	public String getDomiciliMunicipiCodiIne() {
		return domiciliMunicipiCodiIne;
	}
	public void setDomiciliMunicipiCodiIne(String domiciliMunicipiCodiIne) {
		this.domiciliMunicipiCodiIne = domiciliMunicipiCodiIne;
	}
	
	public String getDomiciliNumeracioNumero() {
		return domiciliNumeracioNumero;
	}
	public void setDomiciliNumeracioNumero(String domiciliNumeracioNumero) {
		this.domiciliNumeracioNumero = domiciliNumeracioNumero;
	}
	
	public String getDomiciliPaisCodiIso() {
		return domiciliPaisCodiIso;
	}
	public void setDomiciliPaisCodiIso(String domiciliPaisCodiIso) {
		this.domiciliPaisCodiIso = domiciliPaisCodiIso;
	}
	
	public String getDomiciliPlanta() {
		return domiciliPlanta;
	}
	public void setDomiciliPlanta(String domiciliPlanta) {
		this.domiciliPlanta = domiciliPlanta;
	}
	
	public String getDomiciliPoblacio() {
		return domiciliPoblacio;
	}
	public void setDomiciliPoblacio(String domiciliPoblacio) {
		this.domiciliPoblacio = domiciliPoblacio;
	}
	
	public String getDomiciliPorta() {
		return domiciliPorta;
	}
	public void setDomiciliPorta(String domiciliPorta) {
		this.domiciliPorta = domiciliPorta;
	}
	
	public String getDomiciliPortal() {
		return domiciliPortal;
	}
	public void setDomiciliPortal(String domiciliPortal) {
		this.domiciliPortal = domiciliPortal;
	}
	
	public String getDomiciliProvinciaCodi() {
		return domiciliProvinciaCodi;
	}
	public void setDomiciliProvinciaCodi(String domiciliProvinciaCodi) {
		this.domiciliProvinciaCodi = domiciliProvinciaCodi;
	}
	
	public String getDomiciliNumeracioPuntKm() {
		return domiciliNumeracioPuntKm;
	}
	public void setDomiciliNumeracioPuntKm(String domiciliNumeracioPuntKm) {
		this.domiciliNumeracioPuntKm = domiciliNumeracioPuntKm;
	}
	
	public String getDomiciliViaNom() {
		return domiciliViaNom;
	}
	public void setDomiciliViaNom(String domiciliViaNom) {
		this.domiciliViaNom = domiciliViaNom;
	}
	
	public NotificacioEntregaPostalViaTipusEnum getDomiciliViaTipus() {
		return domiciliViaTipus;
	}
	public void setDomiciliViaTipus(NotificacioEntregaPostalViaTipusEnum domiciliViaTipus) {
		this.domiciliViaTipus = domiciliViaTipus;
	}
	
	public void setNotificacioAutoritzat(boolean notificacioAutoritzat) {
		this.notificacioAutoritzat = notificacioAutoritzat;
	}
	public void setEsRepresentant(boolean esRepresentant) {
		this.esRepresentant = esRepresentant;
	}
	
	public void setRepresentantId(Long representantId) {
		this.representantId = representantId;
	}
	public void setRepresentantIdentificador(Long representantIdentificador) {
		this.representantIdentificador = representantIdentificador;
	}
	
	
	public abstract String getIdentificador();
	
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

	public boolean isEsRepresentant() {
		return esRepresentant;
	}
	public void setEsRepresentant(Boolean esRepresentant) {
		this.esRepresentant = esRepresentant;
	}
	
	private static final long serialVersionUID = -2299453443943600172L;

}
