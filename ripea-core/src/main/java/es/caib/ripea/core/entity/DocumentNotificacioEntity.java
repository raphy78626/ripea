/**
 * 
 */
package es.caib.ripea.core.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.api.dto.DocumentEnviamentEstatEnumDto;
import es.caib.ripea.core.api.dto.DocumentNotificacioTipusEnumDto;
import es.caib.ripea.core.api.dto.InteressatDocumentTipusEnumDto;
import es.caib.ripea.core.api.dto.InteressatIdiomaEnumDto;
import es.caib.ripea.plugin.notificacio.NotificacioEnviamentTipusEnum;

/**
 * Classe del model de dades que representa una notificació d'un document
 * a un dels interessats d'un expedient.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class DocumentNotificacioEntity extends DocumentEnviamentEntity {	
	
	@Column(name = "tipus")
	@Enumerated(EnumType.STRING)
	private DocumentNotificacioTipusEnumDto tipus;
	@Column(name = "unitat_adm", length = 9)
	private String unitatAdministrativa;
	@Column(name = "organ_codi", length = 9)
	private String organCodi;
	@Column(name = "llibre_codi", length = 4)
	private String llibreCodi;
	
	@Column(name = "caducitat")
	@Temporal(TemporalType.TIMESTAMP)
	private Date caducitat;
	@Column(name = "concepte", length = 50, nullable = false)
	private String concepte;
	@Column(name = "descripcio", length = 100)
	private String descripcio;
	@Column(name = "emisor_dir3codi", length = 9, nullable = false)
	private String emisorDir3Codi;
	@Column(name = "env_data_prog")
	@Temporal(TemporalType.TIMESTAMP)
	private Date enviamentDataProgramada;
	@Column(name = "env_tipus", nullable = false)
	@Enumerated(EnumType.STRING)
	private NotificacioEnviamentTipusEnum enviamentTipus;
	
	@Column(name = "destin_doctip")
	private InteressatDocumentTipusEnumDto destinatariDocumentTipus;
	@Column(name = "destinatari_nom", length = 100)
	private String destinatariNom;
	@Column(name = "destinatari_llinatge1", length = 100)
	private String destinatariLlinatge1;
	@Column(name = "destinatari_llinatge2", length = 100)
	private String destinatariLlinatge2;
	@Column(name = "destinatari_nif", length = 9)
	private String destinatariNif;
	@Column(name = "destinatari_telefon", length = 16)
	private String destinatariTelefon;
	@Column(name = "destinatari_email", length = 100)
	private String destinatariEmail;
	@Column(name = "destin_repres")
	private boolean destinatariRepresentant;
	@Column(name = "destin_paicod", length = 4)
	private String destinatariPaisCodi;
	@Column(name = "destin_prvcod", length = 2)
	private String destinatariProvinciaCodi;
	@Column(name = "destin_muncod", length = 5)
	private String destinatariMunicipiCodi;
	
	@Column(name = "deh_obligat")
	private boolean dehObligat;
	@Column(name = "deh_nif", length = 9)
	private String dehNif;
	
    //private EntregaPostal entregaPostal;
	
	@Column(name = "notifica_ref", length = 20)
	private String notificaReferencia;
	@Column(name = "servei_tipus")
	private ServeiTipusEnum serveiTipus;
    
    @Column(name = "titular_nom", length = 100)
	private String titularNom;
	@Column(name = "titular_llinatge1", length = 100)
	private String titularLlinatge1;
	@Column(name = "titular_llinatge2", length = 100)
	private String titularLlinatge2;
	@Column(name = "titular_nif", length = 9, nullable = false)
	private String titularNif;
	@Column(name = "titular_telefon", length = 16)
	private String titularTelefon;
	@Column(name = "titular_email", length = 100)
	private String titularEmail;
	
	@Column(name = "pagcie_dir3", length = 9)
	private String pagadorCieCodiDir3;
	@Column(name = "pagcie_data_vig")
	@Temporal(TemporalType.TIMESTAMP)
	private Date pagadorCieDataVigencia;
	
	@Column(name = "pagcor_dir3", length = 9)
	private String pagadorCorreusCodiDir3;
	@Column(name = "pagcor_numcont", length = 20)
	private String pagadorCorreusContracteNum;
	@Column(name = "pagcor_codi_client", length = 20)
	private String pagadorCorreusCodiClientFacturacio;
	@Column(name = "pagcor_data_vig")
	@Temporal(TemporalType.TIMESTAMP)
	private Date pagadorCorreusDataVigencia;
	
	@Column(name = "seu_exp_serdoc", length = 10, nullable = false)
	private String seuExpedientSerieDocumental;
	@Column(name = "seu_exp_uniorg", length = 10, nullable = false)
	private String seuExpedientUnitatOrganitzativa;
	@Column(name = "seu_exp_ideni", length = 52, nullable = false)
	private String seuExpedientIdentificadorEni;
	@Column(name = "seu_exp_titol", length = 256, nullable = false)
	private String seuExpedientTitol;
	@Column(name = "seu_reg_oficina", length = 256, nullable = false)
	private String seuRegistreOficina;
	@Column(name = "seu_reg_llibre", length = 256, nullable = false)
	private String seuRegistreLlibre;
	@Column(name = "idioma", length = 2)
	@Enumerated(EnumType.STRING)
	private InteressatIdiomaEnumDto seuIdioma;
	@Column(name = "seu_avis_titol", length = 256, nullable = false)
	private String seuAvisTitol;
	@Column(name = "seu_avis_text", length = 256, nullable = false)
	private String seuAvisText;
	@Column(name = "seu_avis_mobil", length = 256)
	private String seuAvisTextMobil;
	@Column(name = "seu_ofici_titol", length = 256, nullable = false)
	private String seuOficiTitol;
	@Column(name = "seu_ofici_text", length = 256, nullable = false)
	private String seuOficiText;
	
	@Column(name = "proc_codi_sia", length = 6, nullable = false)
	private String procedimentCodiSia;
	@Column(name = "retard_postal")
	private Integer retardPostal;
	
	@Column(name = "enviam_data")
	@Temporal(TemporalType.TIMESTAMP)
	private Date enviamentData;
	@Column(name = "enviam_count")
	private Integer enviamentCount;
	@Column(name = "enviam_error")
	private boolean enviamentError;
	@Column(name = "enviam_error_desc", length = 2048)
	private String enviamentErrorDescripcio;
	
	@Column(name = "data_recepcio")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataRecepcio;
	@Column(name = "proces_data")
	@Temporal(TemporalType.TIMESTAMP)
	private Date processamentData;
	@Column(name = "proces_count")
	private Integer processamentCount;
	@Column(name = "proces_error")
	private boolean processamentError;
	@Column(name = "proces_error_desc", length = 2048)
	private String processamentErrorDescripcio;
	
	@Column(name = "doc_hash", length = 40)//, nullable = false)
	private String documentHash;
	@Column(name = "doc_normalitzat")//, nullable = false)
	private boolean documentNormalitzat;
	@Column(name = "doc_gen_csv")//, nullable = false)
	private boolean documentGenerarCsv;
	
	
	public DocumentNotificacioTipusEnumDto getTipus() {
		return tipus;
	}
	public String getUnitatAdministrativa() {
		return unitatAdministrativa;
	}
	public String getOrganCodi() {
		return organCodi;
	}
	public String getLlibreCodi() {
		return llibreCodi;
	}

	public Date getCaducitat() {
		return caducitat;
	}
	public String getConcepte() {
		return concepte;
	}
	public String getDescripcio() {
		return descripcio;
	}
	public String getEmisorDir3Codi() {
		return emisorDir3Codi;
	}
	public Date getEnviamentDataProgramada() {
		return enviamentDataProgramada;
	}
	public NotificacioEnviamentTipusEnum getEnviamentTipus() {
		return enviamentTipus;
	}
	public InteressatDocumentTipusEnumDto getDestinatariDocumentTipus() {
		return destinatariDocumentTipus;
	}
	
	public String getDestinatariNom() {
		return destinatariNom;
	}
	public String getDestinatariLlinatge1() {
		return destinatariLlinatge1;
	}
	public String getDestinatariLlinatge2() {
		return destinatariLlinatge2;
	}
	public String getDestinatariNif() {
		return destinatariNif;
	}
	public String getDestinatariTelefon() {
		return destinatariTelefon;
	}
	public String getDestinatariEmail() {
		return destinatariEmail;
	}
	public boolean isDestinatariRepresentant() {
		return destinatariRepresentant;
	}
	public String getDestinatariPaisCodi() {
		return destinatariPaisCodi;
	}
	public String getDestinatariProvinciaCodi() {
		return destinatariProvinciaCodi;
	}
	public String getDestinatariMunicipiCodi() {
		return destinatariMunicipiCodi;
	}
	
	public boolean getDehObligat() {
		return dehObligat;
	}
	public String getDehNif() {
		return dehNif;
	}
	
	public String getNotificaReferencia() {
		return notificaReferencia;
	}
	public ServeiTipusEnum getServeiTipus() {
		return serveiTipus;
	}
	
	public String getTitularNom() {
		return titularNom;
	}
	public String getTitularLlinatge1() {
		return titularLlinatge1;
	}
	public String getTitularLlinatge2() {
		return titularLlinatge2;
	}
	public String getTitularNif() {
		return titularNif;
	}
	public String getTitularTelefon() {
		return titularTelefon;
	}
	public String getTitularEmail() {
		return titularEmail;
	}
	
	public String getPagadorCieCodiDir3() {
		return pagadorCieCodiDir3;
	}
	public Date getPagadorCieDataVigencia() {
		return pagadorCieDataVigencia;
	}
	
	public String getPagadorCorreusCodiDir3() {
		return pagadorCorreusCodiDir3;
	}
	public String getPagadorCorreusContracteNum() {
		return pagadorCorreusContracteNum;
	}
	public String getPagadorCorreusCodiClientFacturacio() {
		return pagadorCorreusCodiClientFacturacio;
	}
	public Date getPagadorCorreusDataVigencia() {
		return pagadorCorreusDataVigencia;
	}
	
	public String getSeuExpedientSerieDocumental() {
		return seuExpedientSerieDocumental;
	}
	public String getSeuExpedientUnitatOrganitzativa() {
		return seuExpedientUnitatOrganitzativa;
	}
	public String getSeuExpedientIdentificadorEni() {
		return seuExpedientIdentificadorEni;
	}
	public String getSeuExpedientTitol() {
		return seuExpedientTitol;
	}
	public String getSeuRegistreOficina() {
		return seuRegistreOficina;
	}
	public String getSeuRegistreLlibre() {
		return seuRegistreLlibre;
	}
	public InteressatIdiomaEnumDto getSeuIdioma() {
		return seuIdioma;
	}
	public String getSeuAvisTitol() {
		return seuAvisTitol;
	}
	public String getSeuAvisText() {
		return seuAvisText;
	}
	public String getSeuAvisTextMobil() {
		return seuAvisTextMobil;
	}
	public String getSeuOficiTitol() {
		return seuOficiTitol;
	}
	public String getSeuOficiText() {
		return seuOficiText;
	}
	
	public String getProcedimentCodiSia() {
		return procedimentCodiSia;
	}
	public Integer getRetardPostal() {
		return retardPostal;
	}
	
	public Date getEnviamentData() {
		return enviamentData;
	}
	public Integer getEnviamentCount() {
		return enviamentCount;
	}
	public boolean isEnviamentError() {
		return enviamentError;
	}
	public String getEnviamentErrorDescripcio() {
		return enviamentErrorDescripcio;
	}
	
	public Date getDataRecepcio() {
		return dataRecepcio;
	}
	public Date getProcessamentData() {
		return processamentData;
	}
	public Integer getProcessamentCount() {
		return processamentCount;
	}
	public boolean isProcessamentError() {
		return processamentError;
	}
	public String getProcessamentErrorDescripcio() {
		return processamentErrorDescripcio;
	}
	
	public String getDocumentHash() {
		return documentHash;
	}
	public boolean isDocumentNormalitzat() {
		return documentNormalitzat;
	}
	public boolean isDocumentGenerarCsv() {
		return documentGenerarCsv;
	}
	
	
	
	public void update(
			String observacions,
			DocumentNotificacioTipusEnumDto tipus,
			String unitatAdministrativa,
			String organCodi,
			String llibreCodi,
			String assumpte,
			Date caducitat,
			String concepte,
			String descripcio,
			String emisorDir3Codi,
			Date enviamentDataProgramada,
			NotificacioEnviamentTipusEnum enviamentTipus,
			String procedimentCodiSia,
			Integer retardPostal) {
		this.observacions = observacions;
		this.tipus = tipus;
		this.unitatAdministrativa = unitatAdministrativa;
		this.organCodi = organCodi;
		this.llibreCodi = llibreCodi;
		this.assumpte = assumpte;
		this.caducitat = caducitat;
		this.concepte = concepte;
		this.descripcio = descripcio;
		this.emisorDir3Codi = emisorDir3Codi;
		this.enviamentDataProgramada = enviamentDataProgramada;
		this.enviamentTipus = enviamentTipus;
		this.procedimentCodiSia = procedimentCodiSia;
		this.retardPostal = retardPostal;
	}
	public void updateDestinatari(
			InteressatDocumentTipusEnumDto destinatariDocumentTipus,
			String destinatariNom,
			String destinatariLlinatge1,
			String destinatariLlinatge2,
			String destinatariNif,
			String destinatariTelefon,
			String destinatariEmail,
			boolean destinatariRepresentant,
			String destinatariPaisCodi,
			String destinatariProvinciaCodi,
			String destinatariMunicipiCodi) {
		this.destinatariDocumentTipus = destinatariDocumentTipus;
		this.destinatariNom = destinatariNom;
		this.destinatariLlinatge1 = destinatariLlinatge1;
		this.destinatariLlinatge2 = destinatariLlinatge2;
		this.destinatariNif = destinatariNif;
		this.destinatariTelefon = destinatariTelefon;
		this.destinatariEmail = destinatariEmail;
		this.destinatariRepresentant = destinatariRepresentant;
		this.destinatariPaisCodi = destinatariPaisCodi;
		this.destinatariProvinciaCodi = destinatariProvinciaCodi;
		this.destinatariMunicipiCodi = destinatariMunicipiCodi;
	}
	public void updateDeh(
			boolean dehObligat,
			String dehNif) {
		this.dehObligat = dehObligat;
		this.dehNif = dehNif;
	}
	public void updateNotifica(
			String notificaReferencia,
			ServeiTipusEnum serveiTipus) {
		this.notificaReferencia = notificaReferencia;
		this.serveiTipus = serveiTipus;
	}
	public void updateTitular(
			String titularNom,
			String titularLlinatge1,
			String titularLlinatge2,
			String titularNif,
			String titularTelefon,
			String titularEmail) {
		this.titularNom = titularNom;
		this.titularLlinatge1 = titularLlinatge1;
		this.titularLlinatge2 = titularLlinatge2;
		this.titularNif = titularNif;
		this.titularTelefon = titularTelefon;
		this.titularEmail = titularEmail;
	}
	public void updatePagadorCie(
			String codiDir3,
			Date dataVigencia) {
		this.pagadorCieCodiDir3 = codiDir3;
		this.pagadorCieDataVigencia = dataVigencia;
	}
	public void updatePagadorCorreus(
			String codiDir3,
			String contracteNum,
			String codiClientFacturacio,
			Date dataVigencia) {
		this.pagadorCorreusCodiDir3 = codiDir3;
		this.pagadorCorreusContracteNum = contracteNum;
		this.pagadorCorreusCodiClientFacturacio = codiClientFacturacio;
		this.pagadorCorreusDataVigencia = dataVigencia;
	}
	public void updateSeu(
			String expedientSerieDocumental,
			String expedientUnitatOrganitzativa,
			String expedientIdentificadorEni,
			String expedientTitol,
			String registreOficina,
			String registreLlibre,
			InteressatIdiomaEnumDto idioma,
			String avisTitol,
			String avisText,
			String avisTextMobil,
			String oficiTitol,
			String oficiText) {
		this.seuExpedientSerieDocumental = expedientSerieDocumental;
		this.seuExpedientUnitatOrganitzativa = expedientUnitatOrganitzativa;
		this.seuExpedientIdentificadorEni = expedientIdentificadorEni;
		this.seuExpedientTitol = expedientTitol;
		this.seuRegistreOficina = registreOficina;
		this.seuRegistreLlibre = registreLlibre;
		this.seuIdioma = idioma;
		this.seuAvisTitol = avisTitol;
		this.seuAvisText = avisText;
		this.seuAvisTextMobil = avisTextMobil;
		this.seuOficiTitol = oficiTitol;
		this.seuOficiText = oficiText;
	}
	public void updateEnviament(
			boolean enviamentCountIncrementar,
			boolean enviamentError,
			String enviamentErrorDescripcio,
			String registreNumero) {
		this.enviamentData = new Date();
		if (enviamentCountIncrementar) {
			if (this.enviamentCount == null)
				this.enviamentCount = 1;
			else
				this.enviamentCount += 1;
		}
		this.enviamentError = enviamentError;
		if (enviamentErrorDescripcio != null) {
			this.enviamentErrorDescripcio = enviamentErrorDescripcio.substring(0, 2048);
		}
		if (enviamentError) {
			estat = DocumentEnviamentEstatEnumDto.ENVIAT_ERROR;
		} else {
			this.notificaReferencia = registreNumero;
			estat = DocumentEnviamentEstatEnumDto.ENVIAT_OK;
		}
	}
	public void updateProcessament(
			boolean processamentCountIncrementar,
			boolean processamentError,
			String processamentErrorDescripcio,
			Date dataRecepcio,
			boolean entregadaORebutjada) {
		this.processamentData = new Date();
		if (processamentCountIncrementar) {
			if (this.processamentCount == null)
				this.processamentCount = 1;
			else
				this.processamentCount += 1;
		}
		this.processamentError = processamentError;
		if (enviamentErrorDescripcio != null) {
			this.processamentErrorDescripcio = processamentErrorDescripcio.substring(0, 2048);
		}
		if (processamentError) {
			estat = DocumentEnviamentEstatEnumDto.PROCESSAT_ERROR;
		} else {
			if (entregadaORebutjada) {
				if (dataRecepcio != null) {
					this.dataRecepcio = dataRecepcio;
					estat = DocumentEnviamentEstatEnumDto.PROCESSAT_OK;
				} else {
					estat = DocumentEnviamentEstatEnumDto.PROCESSAT_REBUTJAT;
				}
			}
		}
	}
	public void updateDocument(
			String documentHash,
			boolean documentNormalitzat,
			boolean documentGenerarCsv) {
		this.documentHash = documentHash;
		this.documentNormalitzat = documentNormalitzat;
		this.documentGenerarCsv = documentGenerarCsv;
	}
	
	
	public static Builder getBuilder(
			DocumentNotificacioTipusEnumDto tipus,
			ExpedientEntity expedient,
			DocumentEnviamentEstatEnumDto estat,
			String assumpte,
			Date dataEnviament,
			DocumentEntity document,
			Date caducitat,
			String concepte,
			String descripcio,
			String emisorDir3Codi,
			Date enviamentDataProgramada,
			NotificacioEnviamentTipusEnum enviamentTipus,
			String procedimentCodiSia,
			Integer retardPostal) {
		return new Builder(
				tipus,
				expedient,
				estat,
				assumpte,
				dataEnviament,
				document,
				caducitat,
				concepte,
				descripcio,
				emisorDir3Codi,
				enviamentDataProgramada,
				enviamentTipus,
				procedimentCodiSia,
				retardPostal);
	}

	public static class Builder {
		DocumentNotificacioEntity built;
		Builder(
				DocumentNotificacioTipusEnumDto tipus,
				ExpedientEntity expedient,
				DocumentEnviamentEstatEnumDto estat,
				String assumpte,
				Date dataEnviament,
				DocumentEntity document,
				Date caducitat,
				String concepte,
				String descripcio,
				String emisorDir3Codi,
				Date enviamentDataProgramada,
				NotificacioEnviamentTipusEnum enviamentTipus,
				String procedimentCodiSia,
				Integer retardPostal) {
			built = new DocumentNotificacioEntity();
			built.tipus = tipus;
			built.expedient = expedient;
			built.estat = estat;
			built.assumpte = assumpte;
			built.dataEnviament = dataEnviament;
			built.document = document;
			built.caducitat = caducitat;
			built.concepte = concepte;
			built.descripcio = descripcio;
			built.emisorDir3Codi = emisorDir3Codi;
			built.enviamentDataProgramada = enviamentDataProgramada;
			built.enviamentTipus = enviamentTipus;
			built.procedimentCodiSia = procedimentCodiSia;
			built.retardPostal = retardPostal;
		}
		public Builder annexos(List<DocumentEntity> annexos) {
			built.annexos = annexos;
			return this;
		}
		public Builder observacions(String observacions) {
			built.observacions = observacions;
			return this;
		}
		public Builder unitatAdministrativa(String unitatAdministrativa) {
			built.unitatAdministrativa = unitatAdministrativa;
			return this;
		}
		public Builder organCodi(String organCodi) {
			built.organCodi = organCodi;
			return this;
		}
		public Builder llibreCodi(String llibreCodi) {
			built.llibreCodi = llibreCodi;
			return this;
		}
		public Builder destinatariDocumentTipus(InteressatDocumentTipusEnumDto destinatariDocumentTipus) {
			built.destinatariDocumentTipus = destinatariDocumentTipus;
			return this;
		}
		public Builder destinatariNom(String destinatariNom) {
			built.destinatariNom = destinatariNom;
			return this;
		}
		public Builder destinatariLlinatge1(String destinatariLlinatge1) {
			built.destinatariLlinatge1 = destinatariLlinatge1;
			return this;
		}
		public Builder destinatariLlinatge2(String destinatariLlinatge2) {
			built.destinatariLlinatge2 = destinatariLlinatge2;
			return this;
		}
		public Builder destinatariNif(String destinatariNif) {
			built.destinatariNif = destinatariNif;
			return this;
		}
		public Builder destinatariTelefon(String destinatariTelefon) {
			built.destinatariTelefon = destinatariTelefon;
			return this;
		}
		public Builder destinatariEmail(String destinatariEmail) {
			built.destinatariEmail = destinatariEmail;
			return this;
		}
		public Builder destinatariRepresentant(boolean destinatariRepresentant) {
			built.destinatariRepresentant = destinatariRepresentant;
			return this;
		}
		public Builder destinatariPaisCodi(String destinatariPaisCodi) {
			built.destinatariPaisCodi = destinatariPaisCodi;
			return this;
		}
		public Builder destinatariProvinciaCodi(String destinatariProvinciaCodi) {
			built.destinatariProvinciaCodi = destinatariProvinciaCodi;
			return this;
		}
		public Builder destinatariMunicipiCodi(String destinatariMunicipiCodi) {
			built.destinatariMunicipiCodi = destinatariMunicipiCodi;
			return this;
		}
		public Builder dehObligat(boolean dehObligat) {
			built.dehObligat = dehObligat;
			return this;
		}
		public Builder dehNif(String dehNif) {
			built.dehNif = dehNif;
			return this;
		}
		public Builder notificaReferencia(String notificaReferencia) {
			built.notificaReferencia = notificaReferencia;
			return this;
		}
		public Builder serveiTipus(ServeiTipusEnum serveiTipus) {
			built.serveiTipus = serveiTipus;
			return this;
		}
		public Builder titularNom(String titularNom) {
			built.titularNom = titularNom;
			return this;
		}
		public Builder titularLlinatge1(String titularLlinatge1) {
			built.titularLlinatge1 = titularLlinatge1;
			return this;
		}
		public Builder titularLlinatge2(String titularLlinatge2) {
			built.titularLlinatge2 = titularLlinatge2;
			return this;
		}
		public Builder titularNif(String titularNif) {
			built.titularNif = titularNif;
			return this;
		}
		public Builder titularTelefon(String titularTelefon) {
			built.titularTelefon = titularTelefon;
			return this;
		}
		public Builder titularEmail(String titularEmail) {
			built.titularEmail = titularEmail;
			return this;
		}
		public Builder pagadorCieCodiDir3(String pagadorCieCodiDir3) {
			built.pagadorCieCodiDir3 = pagadorCieCodiDir3;
			return this;
		}
		public Builder pagadorCieDataVigencia(Date pagadorCieDataVigencia) {
			built.pagadorCieDataVigencia = pagadorCieDataVigencia;
			return this;
		}
		public Builder pagadorCorreusCodiDir3(String pagadorCorreusCodiDir3) {
			built.pagadorCorreusCodiDir3 = pagadorCorreusCodiDir3;
			return this;
		}
		public Builder pagadorCorreusContracteNum(String pagadorCorreusContracteNum) {
			built.pagadorCorreusContracteNum = pagadorCorreusContracteNum;
			return this;
		}
		public Builder pagadorCorreusCodiClientFacturacio(String pagadorCorreusCodiClientFacturacio) {
			built.pagadorCorreusCodiClientFacturacio = pagadorCorreusCodiClientFacturacio;
			return this;
		}
		public Builder pagadorCorreusDataVigencia(Date pagadorCorreusDataVigencia) {
			built.pagadorCorreusDataVigencia = pagadorCorreusDataVigencia;
			return this;
		}
		public Builder seuExpedientSerieDocumental(String seuExpedientSerieDocumental) {
			built.seuExpedientSerieDocumental = seuExpedientSerieDocumental;
			return this;
		}
		public Builder seuExpedientUnitatOrganitzativa(String seuExpedientUnitatOrganitzativa) {
			built.seuExpedientUnitatOrganitzativa = seuExpedientUnitatOrganitzativa;
			return this;
		}
		public Builder seuExpedientIdentificadorEni(String seuExpedientIdentificadorEni) {
			built.seuExpedientIdentificadorEni = seuExpedientIdentificadorEni;
			return this;
		}
		public Builder seuExpedientTitol(String seuExpedientTitol) {
			built.seuExpedientTitol = seuExpedientTitol;
			return this;
		}
		public Builder seuRegistreOficina(String seuRegistreOficina) {
			built.seuRegistreOficina = seuRegistreOficina;
			return this;
		}
		public Builder seuRegistreLlibre(String seuRegistreLlibre) {
			built.seuRegistreLlibre = seuRegistreLlibre;
			return this;
		}
		public Builder seuIdioma(InteressatIdiomaEnumDto seuIdioma) {
			built.seuIdioma = seuIdioma;
			return this;
		}
		public Builder seuAvisTitol(String seuAvisTitol) {
			built.seuAvisTitol = seuAvisTitol;
			return this;
		}
		public Builder seuAvisText(String seuAvisText) {
			built.seuAvisText = seuAvisText;
			return this;
		}
		public Builder seuAvisTextMobil(String seuAvisTextMobil) {
			built.seuAvisTextMobil = seuAvisTextMobil;
			return this;
		}
		public Builder seuOficiTitol(String seuOficiTitol) {
			built.seuOficiTitol = seuOficiTitol;
			return this;
		}
		public Builder seuOficiText(String seuOficiText) {
			built.seuOficiText = seuOficiText;
			return this;
		}
		public Builder documentHash(String documentHash) {
			built.documentHash = documentHash;
			return this;
		}
		public Builder documentNormalitzat(boolean documentNormalitzat) {
			built.documentNormalitzat = documentNormalitzat;
			return this;
		}
		public Builder documentGenerarCsv(boolean documentGenerarCsv) {
			built.documentGenerarCsv = documentGenerarCsv;
			return this;
		}
		
		public DocumentNotificacioEntity build() {
			return built;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((expedient == null) ? 0 : expedient.hashCode());
		result = prime * result + ((document == null) ? 0 : document.hashCode());
		result = prime * result + ((dataEnviament == null) ? 0 : dataEnviament.hashCode());
		result = prime * result + ((destinatariNif == null) ? 0 : destinatariNif.hashCode());
		result = prime * result + ((destinatariDocumentTipus == null) ? 0 : destinatariDocumentTipus.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentNotificacioEntity other = (DocumentNotificacioEntity) obj;
		if (expedient == null) {
			if (other.expedient != null)
				return false;
		} else if (!expedient.equals(other.expedient))
			return false;
		if (document == null) {
			if (other.document != null)
				return false;
		} else if (!document.equals(other.document))
			return false;
		if (dataEnviament == null) {
			if (other.dataEnviament != null)
				return false;
		} else if (!dataEnviament.equals(other.dataEnviament))
			return false;
		if (destinatariNif == null) {
			if (other.destinatariNif != null)
				return false;
		} else if (!destinatariNif.equals(other.destinatariNif))
			return false;
		if (destinatariDocumentTipus != other.destinatariDocumentTipus)
			return false;
		return true;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
