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

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.api.dto.DocumentEnviamentEstatEnumDto;
import es.caib.ripea.core.api.dto.DocumentPublicacioTipusEnumDto;

/**
 * Classe del model de dades que representa una publicació d'un document
 * d'un expedient a una butlletí oficial.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class DocumentPublicacioEntity extends DocumentEnviamentEntity {

	@Column(name = "tipus", nullable = false)
	@Enumerated(EnumType.STRING)
	private DocumentPublicacioTipusEnumDto tipus;
	@Column(name = "data_publicacio")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataPublicacio;



	public DocumentPublicacioTipusEnumDto getTipus() {
		return tipus;
	}
	public Date getDataPublicacio() {
		return dataPublicacio;
	}

	public void update(
			String assumpte,
			String observacions,
			DocumentPublicacioTipusEnumDto tipus,
			Date dataPublicacio) {
		this.assumpte = assumpte;
		this.observacions = observacions;
		this.tipus = tipus;
		this.dataPublicacio = dataPublicacio;
	}

	public static Builder getBuilder(
			ExpedientEntity expedient,
			DocumentEnviamentEstatEnumDto estat,
			String assumpte,
			Date dataEnviament,
			DocumentEntity document,
			DocumentPublicacioTipusEnumDto tipus) {
		return new Builder(
				expedient,
				estat,
				assumpte,
				dataEnviament,
				document,
				tipus);
	}

	public static class Builder {
		DocumentPublicacioEntity built;
		Builder(
				ExpedientEntity expedient,
				DocumentEnviamentEstatEnumDto estat,
				String assumpte,
				Date dataEnviament,
				DocumentEntity document,
				DocumentPublicacioTipusEnumDto tipus) {
			built = new DocumentPublicacioEntity();
			built.expedient = expedient;
			built.estat = estat;
			built.assumpte = assumpte;
			built.dataEnviament = dataEnviament;
			built.document = document;
			built.tipus = tipus;
		}
		public Builder observacions(String observacions) {
			built.observacions = observacions;
			return this;
		}
		public DocumentPublicacioEntity build() {
			return built;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((dataPublicacio == null) ? 0 : dataPublicacio.hashCode());
		result = prime * result + ((tipus == null) ? 0 : tipus.hashCode());
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
		DocumentPublicacioEntity other = (DocumentPublicacioEntity) obj;
		if (dataPublicacio == null) {
			if (other.dataPublicacio != null)
				return false;
		} else if (!dataPublicacio.equals(other.dataPublicacio))
			return false;
		if (tipus != other.tipus)
			return false;
		return true;
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
