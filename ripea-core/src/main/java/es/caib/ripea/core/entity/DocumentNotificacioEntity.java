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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.api.dto.DocumentEnviamentEstatEnumDto;
import es.caib.ripea.core.api.dto.DocumentNotificacioTipusEnumDto;

/**
 * Classe del model de dades que representa una notificació d'un document
 * a un dels interessats d'un expedient.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class DocumentNotificacioEntity extends DocumentEnviamentEntity {

	@Column(name = "tipus", nullable = false)
	@Enumerated(EnumType.STRING)
	private DocumentNotificacioTipusEnumDto tipus;
	@Column(name = "data_recepcio")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataRecepcio;
	@Column(name = "registre_num", length = 100)
	private String registreNumero;
	@Column(name = "destin_doctip", length = 1)
	private String destinatariDocumentTipus;
	@Column(name = "destin_docnum", length = 17)
	private String destinatariDocumentNum;
	@Column(name = "destin_nom", length = 30, nullable = false)
	private String destinatariNom;
	@Column(name = "destin_lling1", length = 30, nullable = false)
	private String destinatariLlinatge1;
	@Column(name = "destin_lling2", length = 30)
	private String destinatariLlinatge2;
	@Column(name = "destin_email", length = 160)
	private String destinatariEmail;
	@Column(name = "destin_repres")
	private boolean destinatariRepresentant;



	public DocumentNotificacioTipusEnumDto getTipus() {
		return tipus;
	}
	public Date getDataRecepcio() {
		return dataRecepcio;
	}
	public String getRegistreNumero() {
		return registreNumero;
	}
	public String getDestinatariDocumentTipus() {
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
	public boolean isDestinatariRepresentant() {
		return destinatariRepresentant;
	}

	public void update(
			String assumpte,
			String observacions,
			String destinatariDocumentTipus,
			String destinatariDocumentNum,
			String destinatariNom,
			String destinatariLlinatge1,
			String destinatariLlinatge2,
			boolean destinatariRepresentant) {
		this.assumpte = assumpte;
		this.observacions = observacions;
		this.destinatariDocumentTipus = destinatariDocumentTipus;
		this.destinatariDocumentNum = destinatariDocumentNum;
		this.destinatariNom = destinatariNom;
		this.destinatariLlinatge1 = destinatariLlinatge1;
		this.destinatariLlinatge2 = destinatariLlinatge2;
		this.destinatariRepresentant = destinatariRepresentant;
	}

	public static Builder getBuilder(
			ExpedientEntity expedient,
			DocumentEnviamentEstatEnumDto estat,
			String assumpte,
			Date dataEnviament,
			String sistemaExternId,
			DocumentEntity document,
			DocumentNotificacioTipusEnumDto tipus,
			String destinatariDocumentTipus,
			String destinatariDocumentNum,
			String destinatariNom,
			String destinatariLlinatge1,
			String destinatariLlinatge2,
			boolean destinatariRepresentant) {
		return new Builder(
				expedient,
				estat,
				assumpte,
				dataEnviament,
				sistemaExternId,
				document,
				tipus,
				destinatariDocumentTipus,
				destinatariDocumentNum,
				destinatariNom,
				destinatariLlinatge1,
				destinatariLlinatge2,
				destinatariRepresentant);
	}

	public static class Builder {
		DocumentNotificacioEntity built;
		Builder(
				ExpedientEntity expedient,
				DocumentEnviamentEstatEnumDto estat,
				String assumpte,
				Date dataEnviament,
				String sistemaExternId,
				DocumentEntity document,
				DocumentNotificacioTipusEnumDto tipus,
				String destinatariDocumentTipus,
				String destinatariDocumentNum,
				String destinatariNom,
				String destinatariLlinatge1,
				String destinatariLlinatge2,
				boolean destinatariRepresentant) {
			built = new DocumentNotificacioEntity();
			built.expedient = expedient;
			built.estat = estat;
			built.assumpte = assumpte;
			built.dataEnviament = dataEnviament;
			built.sistemaExternId = sistemaExternId;
			built.document = document;
			built.tipus = tipus;
			built.destinatariDocumentTipus = destinatariDocumentTipus;
			built.destinatariDocumentNum = destinatariDocumentNum;
			built.destinatariNom = destinatariNom;
			built.destinatariLlinatge1 = destinatariLlinatge1;
			built.destinatariLlinatge2 = destinatariLlinatge2;
			built.destinatariRepresentant = destinatariRepresentant;
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
		public DocumentNotificacioEntity build() {
			return built;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((destinatariDocumentNum == null) ? 0 : destinatariDocumentNum.hashCode());
		result = prime * result + ((destinatariDocumentTipus == null) ? 0 : destinatariDocumentTipus.hashCode());
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
		DocumentNotificacioEntity other = (DocumentNotificacioEntity) obj;
		if (destinatariDocumentNum == null) {
			if (other.destinatariDocumentNum != null)
				return false;
		} else if (!destinatariDocumentNum.equals(other.destinatariDocumentNum))
			return false;
		if (destinatariDocumentTipus == null) {
			if (other.destinatariDocumentTipus != null)
				return false;
		} else if (!destinatariDocumentTipus.equals(other.destinatariDocumentTipus))
			return false;
		if (tipus != other.tipus)
			return false;
		return true;
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
