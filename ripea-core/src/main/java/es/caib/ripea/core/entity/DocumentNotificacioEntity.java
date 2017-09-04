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

import es.caib.ripea.core.api.dto.DocumentEnviamentEstatDetallatEnumDto;
import es.caib.ripea.core.api.dto.DocumentEnviamentEstatEnumDto;
import es.caib.ripea.core.api.dto.DocumentNotificacioEnviamentTipusEnumDto;
import es.caib.ripea.core.api.dto.DocumentNotificacioTipusEnumDto;
import es.caib.ripea.core.api.dto.InteressatDocumentTipusEnumDto;
import es.caib.ripea.core.api.dto.InteressatIdiomaEnumDto;

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
	@Column(name = "tipus_enviament")
	@Enumerated(EnumType.STRING)
	private DocumentNotificacioEnviamentTipusEnumDto tipusEnviament;
	@Column(name = "data_recepcio")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataRecepcio;
	@Column(name = "registre_num", length = 100)
	private String registreNumero;
	@Column(name = "destin_doctip")
	private InteressatDocumentTipusEnumDto destinatariDocumentTipus;
	@Column(name = "destin_docnum", length = 17)
	private String destinatariDocumentNum;
	@Column(name = "destin_nom", length = 30)
	private String destinatariNom;
	@Column(name = "destin_lling1", length = 30)
	private String destinatariLlinatge1;
	@Column(name = "destin_lling2", length = 30)
	private String destinatariLlinatge2;
	@Column(name = "destin_paicod", length = 4)
	private String destinatariPaisCodi;
	@Column(name = "destin_prvcod", length = 2)
	private String destinatariProvinciaCodi;
	@Column(name = "destin_muncod", length = 5)
	private String destinatariMunicipiCodi;
	@Column(name = "destin_email", length = 160)
	private String destinatariEmail;
	@Column(name = "destin_repres")
	private boolean destinatariRepresentant;
	@Column(name = "unitat_adm", length = 9)
	private String unitatAdministrativa;
	@Column(name = "organ_codi", length = 9)
	private String organCodi;
	@Column(name = "llibre_codi", length = 4)
	private String llibreCodi;
	@Column(name = "avis_titol", length = 256)
	private String avisTitol;
	@Column(name = "avis_text", length = 1024)
	private String avisText;
	@Column(name = "avis_textsms", length = 200)
	private String avisTextSms;
	@Column(name = "ofici_titol", length = 1024)
	private String oficiTitol;
	@Column(name = "ofici_text", length = 256)
	private String oficiText;
	@Column(name = "idioma", length = 2)
	@Enumerated(EnumType.STRING)
	private InteressatIdiomaEnumDto idioma;
	@Column(name = "enviam_data")
	@Temporal(TemporalType.TIMESTAMP)
	private Date enviamentData;
	@Column(name = "enviam_count")
	private Integer enviamentCount;
	@Column(name = "enviam_error")
	private boolean enviamentError;
	@Column(name = "enviam_error_desc", length = 2048)
	private String enviamentErrorDescripcio;
	@Column(name = "proces_data")
	@Temporal(TemporalType.TIMESTAMP)
	private Date processamentData;
	@Column(name = "proces_count")
	private Integer processamentCount;
	@Column(name = "proces_error")
	private boolean processamentError;
	@Column(name = "proces_error_desc", length = 2048)
	private String processamentErrorDescripcio;
	@Column(name = "concepte", length = 256)
	private String concepte;
	@Column(name = "referencia", length = 64)
	private String referencia;
	@Column(name = "estat_detallat", nullable = true)
	@Enumerated(EnumType.STRING)
	protected DocumentEnviamentEstatDetallatEnumDto estatDetallat;


	public DocumentNotificacioTipusEnumDto getTipus() {
		return tipus;
	}
	public Date getDataRecepcio() {
		return dataRecepcio;
	}
	public String getRegistreNumero() {
		return registreNumero;
	}
	public InteressatDocumentTipusEnumDto getDestinatariDocumentTipus() {
		return destinatariDocumentTipus;
	}
	public String getDestinatariDocumentNum() {
		return destinatariDocumentNum;
	}
	public String getDestinatariEmail() {
		return destinatariEmail;
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
	public String getDestinatariPaisCodi() {
		return destinatariPaisCodi;
	}
	public String getDestinatariProvinciaCodi() {
		return destinatariProvinciaCodi;
	}
	public String getDestinatariMunicipiCodi() {
		return destinatariMunicipiCodi;
	}
	public boolean isDestinatariRepresentant() {
		return destinatariRepresentant;
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
	public String getAvisTitol() {
		return avisTitol;
	}
	public String getAvisText() {
		return avisText;
	}
	public String getAvisTextSms() {
		return avisTextSms;
	}
	public String getOficiTitol() {
		return oficiTitol;
	}
	public String getOficiText() {
		return oficiText;
	}
	public InteressatIdiomaEnumDto getIdioma() {
		return idioma;
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

	public DocumentNotificacioEnviamentTipusEnumDto getTipusEnviament() {
		return tipusEnviament;
	}
	public String getConcepte() {
		return concepte;
	}
	public String getReferencia() {
		return referencia;
	}
	public DocumentEnviamentEstatDetallatEnumDto getEstatDetallat() {
		return estatDetallat;
	}
	public void update(
			DocumentEnviamentEstatEnumDto estat,
			String assumpte,
			String observacions,
			InteressatDocumentTipusEnumDto destinatariDocumentTipus,
			String destinatariDocumentNum,
			String destinatariNom,
			String destinatariLlinatge1,
			String destinatariLlinatge2,
			boolean destinatariRepresentant,
			String unitatAdministrativa,
			String organCodi,
			String llibreCodi,
			String avisTitol,
			String avisText,
			String avisTextSms,
			String oficiTitol,
			String oficiText,
			InteressatIdiomaEnumDto idioma) {
		this.estat = estat;
		this.assumpte = assumpte;
		this.observacions = observacions;
		this.destinatariDocumentTipus = destinatariDocumentTipus;
		this.destinatariDocumentNum = destinatariDocumentNum;
		this.destinatariNom = destinatariNom;
		this.destinatariLlinatge1 = destinatariLlinatge1;
		this.destinatariLlinatge2 = destinatariLlinatge2;
		this.destinatariRepresentant = destinatariRepresentant;
		this.unitatAdministrativa = unitatAdministrativa;
		this.organCodi = organCodi;
		this.llibreCodi = llibreCodi;
		this.avisTitol = avisTitol;
		this.avisText = avisText;
		this.avisTextSms = avisTextSms;
		this.oficiTitol = oficiTitol;
		this.oficiText = oficiText;
		this.idioma = idioma;
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
			this.registreNumero = registreNumero;
			estat = DocumentEnviamentEstatEnumDto.ENVIAT_OK;
		}
	}
	
	public void updateReferenciaEnviament(
			boolean enviamentCountIncrementar,
			boolean enviamentError,
			String enviamentErrorDescripcio,
			String referencia) {
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
			this.referencia = referencia;
			estat = DocumentEnviamentEstatEnumDto.ENVIAT_OK;
		}
	}
	
	public void updateNotificacioEstat(
			boolean processamentCountIncrementar,
			DocumentEnviamentEstatEnumDto estat,
			DocumentEnviamentEstatDetallatEnumDto estatDetallat,
			Date dataRecepcio) {
		this.processamentData = new Date();
		if (processamentCountIncrementar) {
			if (this.processamentCount == null)
				this.processamentCount = 1;
			else
				this.processamentCount += 1;
		}
		
		this.estat = estat;
		this.estatDetallat = estatDetallat;
		
		if (DocumentEnviamentEstatEnumDto.PROCESSAT_OK == estat) {
			this.processamentError = true;
			this.dataRecepcio = dataRecepcio;
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

	public static Builder getBuilder(
			ExpedientEntity expedient,
			DocumentEnviamentEstatEnumDto estat,
			String assumpte,
			Date dataEnviament,
			DocumentEntity document,
			DocumentNotificacioTipusEnumDto tipus,
			DocumentNotificacioEnviamentTipusEnumDto tipusEnviament,
			InteressatDocumentTipusEnumDto destinatariDocumentTipus,
			String destinatariDocumentNum,
			String destinatariNom,
			String destinatariLlinatge1,
			String destinatariLlinatge2,
			String destinatariPaisCodi,
			String destinatariProvinciaCodi,
			String destinatariMunicipiCodi,
			boolean destinatariRepresentant,
			InteressatIdiomaEnumDto idioma,
			String concepte) {
		return new Builder(
				expedient,
				estat,
				assumpte,
				dataEnviament,
				document,
				tipus,
				tipusEnviament,
				destinatariDocumentTipus,
				destinatariDocumentNum,
				destinatariNom,
				destinatariLlinatge1,
				destinatariLlinatge2,
				destinatariPaisCodi,
				destinatariProvinciaCodi,
				destinatariMunicipiCodi,
				destinatariRepresentant,
				idioma,
				concepte);
	}

	public static class Builder {
		DocumentNotificacioEntity built;
		Builder(
				ExpedientEntity expedient,
				DocumentEnviamentEstatEnumDto estat,
				String assumpte,
				Date dataEnviament,
				DocumentEntity document,
				DocumentNotificacioTipusEnumDto tipus,
				DocumentNotificacioEnviamentTipusEnumDto tipusEnviament,
				InteressatDocumentTipusEnumDto destinatariDocumentTipus,
				String destinatariDocumentNum,
				String destinatariNom,
				String destinatariLlinatge1,
				String destinatariLlinatge2,
				String destinatariPaisCodi,
				String destinatariProvinciaCodi,
				String destinatariMunicipiCodi,
				boolean destinatariRepresentant,
				InteressatIdiomaEnumDto idioma,
				String concepte) {
			built = new DocumentNotificacioEntity();
			built.expedient = expedient;
			built.estat = estat;
			built.assumpte = assumpte;
			built.dataEnviament = dataEnviament;
			built.document = document;
			built.tipus = tipus;
			built.tipusEnviament = tipusEnviament;
			built.destinatariDocumentTipus = destinatariDocumentTipus;
			built.destinatariDocumentNum = destinatariDocumentNum;
			built.destinatariNom = destinatariNom;
			built.destinatariLlinatge1 = destinatariLlinatge1;
			built.destinatariLlinatge2 = destinatariLlinatge2;
			built.destinatariPaisCodi = destinatariPaisCodi;
			built.destinatariProvinciaCodi = destinatariProvinciaCodi;
			built.destinatariMunicipiCodi = destinatariMunicipiCodi;
			built.destinatariRepresentant = destinatariRepresentant;
			built.idioma = idioma;
			built.concepte = concepte;
		}
		public Builder annexos(List<DocumentEntity> annexos) {
			built.annexos = annexos;
			return this;
		}
		public Builder observacions(String observacions) {
			built.observacions = observacions;
			return this;
		}
		public Builder destinatariEmail(String destinatariEmail) {
			built.destinatariEmail = destinatariEmail;
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
		public Builder avisTitol(String avisTitol) {
			built.avisTitol = avisTitol;
			return this;
		}
		public Builder avisText(String avisText) {
			built.avisText = avisText;
			return this;
		}
		public Builder avisTextSms(String avisTextSms) {
			built.avisTextSms = avisTextSms;
			return this;
		}
		public Builder oficiTitol(String oficiTitol) {
			built.oficiTitol = oficiTitol;
			return this;
		}
		public Builder oficiText(String oficiText) {
			built.oficiText = oficiText;
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
		result = prime * result + ((destinatariDocumentNum == null) ? 0 : destinatariDocumentNum.hashCode());
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
		if (destinatariDocumentNum == null) {
			if (other.destinatariDocumentNum != null)
				return false;
		} else if (!destinatariDocumentNum.equals(other.destinatariDocumentNum))
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
