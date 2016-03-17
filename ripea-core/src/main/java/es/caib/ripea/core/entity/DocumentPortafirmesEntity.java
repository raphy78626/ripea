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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.ForeignKey;

import es.caib.ripea.core.api.dto.PortafirmesEstatEnumDto;
import es.caib.ripea.core.audit.RipeaAuditable;
import es.caib.ripea.core.audit.RipeaAuditingEntityListener;

/**
 * Classe del model de dades que representa un enviament d'una versió
 * d'un document al portafirmes.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table( name = "ipa_document_pfirmes",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {
						"document_id",
						"versio",
						"pfirmes_id"})})
@EntityListeners(RipeaAuditingEntityListener.class)
public class DocumentPortafirmesEntity extends RipeaAuditable<Long> {

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "document_id")
	@ForeignKey(name = "ipa_document_docpfir_fk")
	private DocumentEntity document;
	@Column(name = "versio", nullable = false)
	private int versio;
	@Column(name = "motiu", length = 256, nullable = false)
	protected String motiu;
	@Column(name = "prioritat", nullable = false)
	@Enumerated(EnumType.STRING)
	protected DocumentPortafirmesPrioritatEnum prioritat;
	@Temporal(TemporalType.DATE)
	@Column(name = "data_caducitat")
	protected Date dataCaducitat;
	@Column(name = "pfirmes_id", nullable = false, unique = true)
	private long portafirmesId;
	@Column(name = "pfirmes_estat", nullable = false)
	@Enumerated(EnumType.STRING)
	private PortafirmesEstatEnumDto portafirmesEstat;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "callback_darrer")
	private Date callbackDarrer;
	@Column(name = "callback_count")
	private int callbackCount;
	@Column(name = "error_desc", length = 1024)
	private String errorDescripcio;
	@Version
	private long version = 0;



	public DocumentEntity getDocument() {
		return document;
	}
	public int getVersio() {
		return versio;
	}
	public String getMotiu() {
		return motiu;
	}
	public DocumentPortafirmesPrioritatEnum getPrioritat() {
		return prioritat;
	}
	public Date getDataCaducitat() {
		return dataCaducitat;
	}
	public long getPortafirmesId() {
		return portafirmesId;
	}
	public PortafirmesEstatEnumDto getPortafirmesEstat() {
		return portafirmesEstat;
	}
	public Date getCallbackDarrer() {
		return callbackDarrer;
	}
	public int getCallbackCount() {
		return callbackCount;
	}
	public String getErrorDescripcio() {
		return errorDescripcio;
	}

	public void updateEstat(
			PortafirmesEstatEnumDto portafirmesEstat) {
		this.portafirmesEstat = portafirmesEstat;
	}
	public void updateError(
			String errorDescripcio) {
		this.errorDescripcio = errorDescripcio;
	}
	public void updateNouCallback() {
		this.callbackDarrer = new Date();
		this.callbackCount++;
	}

	/**
	 * Obté el Builder per a crear objectes de tipus document-portafirmes.
	 * 
	 * @param document
	 *            El document relacionat amb aquest enviament.
	 * @param versio
	 *            La versió del document.
	 * @param motiu
	 *            El motiu per l'enviament.
	 * @param prioritat
	 *            La prioritat per l'enviament.
	 * @param dataCaducitat
	 *            La data de caducitat per l'enviament.
	 * @param portafirmesId
	 *            La identificació de l'enviament a portafirmes.
	 * @param portafirmesEstat
	 *            L'estat de l'enviament a portafirmes.
	 * @return Una nova instància del Builder.
	 */
	public static Builder getBuilder(
			DocumentEntity document,
			int versio,
			String motiu,
			DocumentPortafirmesPrioritatEnum prioritat,
			Date dataCaducitat,
			long portafirmesId,
			PortafirmesEstatEnumDto portafirmesEstat) {
		return new Builder(
				document,
				versio,
				motiu,
				prioritat,
				dataCaducitat,
				portafirmesId,
				portafirmesEstat);
	}

	/**
	 * Builder per a crear noves instàncies d'aquesta classe.
	 * 
	 * @author Limit Tecnologies <limit@limit.es>
	 */
	public static class Builder {
		DocumentPortafirmesEntity built;
		Builder(
				DocumentEntity document,
				int versio,
				String motiu,
				DocumentPortafirmesPrioritatEnum prioritat,
				Date dataCaducitat,
				long portafirmesId,
				PortafirmesEstatEnumDto portafirmesEstat) {
			built = new DocumentPortafirmesEntity();
			built.document = document;
			built.versio = versio;
			built.motiu = motiu;
			built.prioritat = prioritat;
			built.dataCaducitat = dataCaducitat;
			built.portafirmesId = portafirmesId;
			built.portafirmesEstat = portafirmesEstat;
		}
		public DocumentPortafirmesEntity build() {
			return built;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((document == null) ? 0 : document.hashCode());
		result = prime * result
				+ (int) (portafirmesId ^ (portafirmesId >>> 32));
		result = prime * result + versio;
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
		if (document == null) {
			if (other.document != null)
				return false;
		} else if (!document.equals(other.document))
			return false;
		if (portafirmesId != other.portafirmesId)
			return false;
		if (versio != other.versio)
			return false;
		return true;
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
