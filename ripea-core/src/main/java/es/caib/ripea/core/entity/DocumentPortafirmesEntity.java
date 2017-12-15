/**
 * 
 */
package es.caib.ripea.core.entity;

import java.util.Date;

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
import es.caib.ripea.core.api.dto.MetaDocumentFirmaFluxTipusEnumDto;
import es.caib.ripea.core.api.dto.PortafirmesPrioritatEnumDto;

/**
 * Classe del model de dades que representa un enviament d'una versió
 * d'un document al portafirmes.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class DocumentPortafirmesEntity extends DocumentEnviamentEntity {

	private static final int ERROR_DESC_TAMANY = 2048;

	@Column(name = "pfirmes_priori")
	@Enumerated(EnumType.STRING)
	private PortafirmesPrioritatEnumDto prioritat;
	@Temporal(TemporalType.DATE)
	@Column(name = "pfirmes_datcad")
	private Date dataCaducitat;
	@Column(name = "pfirmes_doctip", length = 64)
	private String documentTipus;
	@Column(name = "pfirmes_responsables", length = 1024)
	private String responsables;
	@Enumerated(EnumType.STRING)
	@Column(name = "pfirmes_fluxtip")
	private MetaDocumentFirmaFluxTipusEnumDto fluxTipus;
	@Column(name = "pfirmes_fluxid", length = 64)
	private String fluxId;
	@Column(name = "pfirmes_id", length = 64, unique = true)
	private String portafirmesId;
	@Column(name = "enviam_data")
	@Temporal(TemporalType.TIMESTAMP)
	private Date enviamentData;
	@Column(name = "enviam_count")
	private Integer enviamentCount;
	@Column(name = "enviam_error")
	private boolean enviamentError;
	@Column(name = "enviam_error_desc", length = ERROR_DESC_TAMANY)
	private String enviamentErrorDescripcio;
	@Column(name = "proces_data")
	@Temporal(TemporalType.TIMESTAMP)
	private Date processamentData;
	@Column(name = "proces_count")
	private Integer processamentCount;
	@Column(name = "proces_error")
	private boolean processamentError;
	@Column(name = "proces_error_desc", length = ERROR_DESC_TAMANY)
	private String processamentErrorDescripcio;



	public PortafirmesPrioritatEnumDto getPrioritat() {
		return prioritat;
	}
	public Date getDataCaducitat() {
		return dataCaducitat;
	}
	public String getDocumentTipus() {
		return documentTipus;
	}
	public String[] getResponsables() {
		if (responsables == null)
			return null;
		return responsables.split(",");
	}
	public MetaDocumentFirmaFluxTipusEnumDto getFluxTipus() {
		return fluxTipus;
	}
	public String getFluxId() {
		return fluxId;
	}
	public String getPortafirmesId() {
		return portafirmesId;
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

	public void updateEnviament(
			boolean enviamentCountIncrementar,
			boolean enviamentError,
			String enviamentErrorDescripcio,
			String portafirmesId) {
		this.enviamentData = new Date();
		if (enviamentCountIncrementar) {
			if (this.enviamentCount == null)
				this.enviamentCount = 1;
			else
				this.enviamentCount += 1;
		}
		this.enviamentError = enviamentError;
		if (enviamentErrorDescripcio != null) {
			this.enviamentErrorDescripcio = enviamentErrorDescripcio.substring(0, ERROR_DESC_TAMANY - 10);
		}
		if (enviamentError) {
			estat = DocumentEnviamentEstatEnumDto.ENVIAT_ERROR;
		} else {
			this.portafirmesId = portafirmesId;
			estat = DocumentEnviamentEstatEnumDto.ENVIAT_OK;
		}
	}
	public void updateProcessament(
			boolean processamentCountIncrementar,
			boolean processamentError,
			String processamentErrorDescripcio,
			boolean rebutjat) {
		this.processamentData = new Date();
		if (processamentCountIncrementar) {
			if (this.processamentCount == null)
				this.processamentCount = 1;
			else
				this.processamentCount += 1;
		}
		this.processamentError = processamentError;
		if (processamentErrorDescripcio != null) {
			this.processamentErrorDescripcio = processamentErrorDescripcio.substring(0, ERROR_DESC_TAMANY - 10);
		}
		if (processamentError) {
			estat = DocumentEnviamentEstatEnumDto.PROCESSAT_ERROR;
		} else {
			if (rebutjat) {
				estat = DocumentEnviamentEstatEnumDto.PROCESSAT_REBUTJAT;
			} else {
				estat = DocumentEnviamentEstatEnumDto.PROCESSAT_OK;
			}
		}
	}
	public void updateCancelacio() {
		estat = DocumentEnviamentEstatEnumDto.CANCELAT;
	}

	public static Builder getBuilder(
			DocumentEntity document,
			DocumentEnviamentEstatEnumDto estat,
			String assumpte,
			Date dataEnviament,
			PortafirmesPrioritatEnumDto prioritat,
			Date dataCaducitat,
			String documentTipus,
			String[] responsables,
			MetaDocumentFirmaFluxTipusEnumDto fluxTipus,
			String fluxId) {
		return new Builder(
				document,
				estat,
				assumpte,
				dataEnviament,
				prioritat,
				dataCaducitat,
				documentTipus,
				responsables,
				fluxTipus,
				fluxId);
	}

	public static class Builder {
		DocumentPortafirmesEntity built;
		Builder(
				DocumentEntity document,
				DocumentEnviamentEstatEnumDto estat,
				String assumpte,
				Date dataEnviament,
				PortafirmesPrioritatEnumDto prioritat,
				Date dataCaducitat,
				String documentTipus,
				String[] responsables,
				MetaDocumentFirmaFluxTipusEnumDto fluxTipus,
				String fluxId) {
			built = new DocumentPortafirmesEntity();
			built.document = document;
			built.expedient = document.getExpedient();
			built.estat = estat;
			built.assumpte = assumpte;
			built.dataEnviament = dataEnviament;
			built.prioritat = prioritat;
			built.dataCaducitat = dataCaducitat;
			built.documentTipus = documentTipus;
			built.responsables = getResponsablesFromArray(responsables);
			built.fluxTipus = fluxTipus;
			built.fluxId = fluxId;
			
		}
		public DocumentPortafirmesEntity build() {
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
		result = prime * result + ((dataCaducitat == null) ? 0 : dataCaducitat.hashCode());
		result = prime * result + ((documentTipus == null) ? 0 : documentTipus.hashCode());
		result = prime * result + ((fluxId == null) ? 0 : fluxId.hashCode());
		result = prime * result + ((fluxTipus == null) ? 0 : fluxTipus.hashCode());
		result = prime * result + ((prioritat == null) ? 0 : prioritat.hashCode());
		result = prime * result + ((responsables == null) ? 0 : responsables.hashCode());
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
		DocumentPortafirmesEntity other = (DocumentPortafirmesEntity) obj;
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
		if (dataCaducitat == null) {
			if (other.dataCaducitat != null)
				return false;
		} else if (!dataCaducitat.equals(other.dataCaducitat))
			return false;
		if (documentTipus == null) {
			if (other.documentTipus != null)
				return false;
		} else if (!documentTipus.equals(other.documentTipus))
			return false;
		if (fluxId == null) {
			if (other.fluxId != null)
				return false;
		} else if (!fluxId.equals(other.fluxId))
			return false;
		if (fluxTipus != other.fluxTipus)
			return false;
		if (prioritat != other.prioritat)
			return false;
		if (responsables == null) {
			if (other.responsables != null)
				return false;
		} else if (!responsables.equals(other.responsables))
			return false;
		return true;
	}
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	private static String getResponsablesFromArray(String[] responsables) {
		StringBuilder responsablesStr = new StringBuilder();
		for (String responsable: responsables) {
			if (responsablesStr.length() > 0)
				responsablesStr.append(",");
			responsablesStr.append(responsable);
		}
		return responsablesStr.toString();
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
