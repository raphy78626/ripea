/**
 * 
 */
package es.caib.ripea.core.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.ForeignKey;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.audit.RipeaAuditable;

/**
 * Classe del model de dades que representa una versió d'un document.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(	name = "ipa_document_versio",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {
						"document_id",
						"versio"})})
@EntityListeners(AuditingEntityListener.class)
public class DocumentVersioEntity extends RipeaAuditable<Long> {

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "document_id")
	@ForeignKey(name = "ipa_document_docversio_fk")
	private DocumentEntity document;
	@Column(name = "versio", nullable = false)
	private int versio;
	@Column(name = "arxiu_nom", length = 256, nullable = false)
	private String arxiuNom;
	@Column(name = "arxiu_content_type", length = 256, nullable = false)
	private String arxiuContentType;
	@Column(name = "arxiu_content_length", nullable = false)
	private long arxiuContentLength;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "arxiu_contingut")
	private byte[] arxiuContingut;
	@Column(name = "arxiu_uuid", length = 36)
	private String arxiuUuid;
	@Version
	private long version = 0;



	public DocumentEntity getDocument() {
		return document;
	}
	public int getVersio() {
		return versio;
	}
	public String getArxiuNom() {
		return arxiuNom;
	}
	public String getArxiuContentType() {
		return arxiuContentType;
	}
	public long getArxiuContentLength() {
		return arxiuContentLength;
	}
	public byte[] getArxiuContingut() {
		return arxiuContingut;
	}
	public String getArxiuUuid() {
		return arxiuUuid;
	}

	public void updateArxiu(
			String arxiuNom,
			String arxiuContentType,
			byte[] arxiuContingut) {
		this.arxiuNom = arxiuNom;
		this.arxiuContentType = arxiuContentType;
		this.arxiuContingut = arxiuContingut;
		this.arxiuContentLength = arxiuContingut.length;
	}

	public void updateArxiuUuid(
			String arxiuUuid) {
		this.arxiuUuid = arxiuUuid;
	}

	public static Builder getBuilder(
			DocumentEntity document,
			int versio,
			String arxiuNom,
			String arxiuContentType,
			byte[] arxiuContingut) {
		return new Builder(
				document,
				versio,
				arxiuNom,
				arxiuContentType,
				arxiuContingut);
	}
	public static Builder getBuilder(
			DocumentEntity document,
			int versio,
			String arxiuNom,
			String arxiuContentType,
			long arxiuContentLength) {
		return new Builder(
				document,
				versio,
				arxiuNom,
				arxiuContentType,
				arxiuContentLength);
	}

	public static class Builder {
		DocumentVersioEntity built;
		Builder(
				DocumentEntity document,
				int versio,
				String arxiuNom,
				String arxiuContentType,
				byte[] arxiuContingut) {
			built = new DocumentVersioEntity();
			built.document = document;
			built.versio = versio;
			built.arxiuNom = arxiuNom;
			built.arxiuContentType = arxiuContentType;
			built.arxiuContingut = arxiuContingut;
			built.arxiuContentLength = arxiuContingut.length;
		}
		Builder(
				DocumentEntity document,
				int versio,
				String arxiuNom,
				String arxiuContentType,
				long arxiuContentLength) {
			built = new DocumentVersioEntity();
			built.document = document;
			built.versio = versio;
			built.arxiuNom = arxiuNom;
			built.arxiuContentType = arxiuContentType;
			built.arxiuContentLength = arxiuContentLength;
		}
		public DocumentVersioEntity build() {
			return built;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((document == null) ? 0 : document.hashCode());
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
		DocumentVersioEntity other = (DocumentVersioEntity) obj;
		if (document == null) {
			if (other.document != null)
				return false;
		} else if (!document.equals(other.document))
			return false;
		if (versio != other.versio)
			return false;
		return true;
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
