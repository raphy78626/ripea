/**
 * 
 */
package es.caib.ripea.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.api.dto.ContingutTipusEnumDto;
import es.caib.ripea.core.api.dto.DocumentTipusEnumDto;

/**
 * Classe del model de dades que representa un document.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(name = "ipa_document")
@EntityListeners(AuditingEntityListener.class)
public class DocumentEntity extends NodeEntity {

	@Column(name = "tipus", nullable = false)
	protected DocumentTipusEnumDto documentTipus;
	@Column(name = "ubicacio", length = 255)
	protected String ubicacio;
	@ManyToOne(optional = true)
	@JoinColumn(name = "expedient_id")
	@ForeignKey(name = "ipa_expedient_document_fk")
	protected ExpedientEntity expedient;
	@Column(name = "data")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date data;
	@Column(name = "custodiat")
	private boolean custodiat;
	@Column(name = "custodia_data")
	@Temporal(TemporalType.TIMESTAMP)
	private Date custodiaData;
	@Column(name = "custodia_id", length = 256)
	private String custodiaId;
	@Column(name = "custodia_url", length = 1024)
	private String custodiaUrl;
	@ManyToOne(optional = true)
	@JoinColumn(name = "versio_darrera_id")
	@ForeignKey(name = "ipa_verdarrera_document_fk")
	protected DocumentVersioEntity versioDarrera;



	public DocumentTipusEnumDto getDocumentTipus() {
		return documentTipus;
	}
	public String getUbicacio() {
		return ubicacio;
	}
	public ExpedientEntity getExpedient() {
		return expedient;
	}
	public Date getData() {
		return data;
	}
	public DocumentVersioEntity getVersioDarrera() {
		return versioDarrera;
	}
	public boolean isCustodiat() {
		return custodiat;
	}
	public Date getCustodiaData() {
		return custodiaData;
	}
	public String getCustodiaId() {
		return custodiaId;
	}
	public String getCustodiaUrl() {
		return custodiaUrl;
	}

	public MetaDocumentEntity getMetaDocument() {
		return (MetaDocumentEntity)getMetaNode();
	}

	public void update(
			MetaDocumentEntity metaDocument,
			String nom,
			Date data,
			String ubicacio) {
		this.metaNode = metaDocument;
		this.nom = nom;
		this.data = data;
		this.ubicacio = ubicacio;
	}
	public void updateVersioDarrera(
			DocumentVersioEntity versioDarrera) {
		this.versioDarrera = versioDarrera;
	}

	public void updateCustodiaUrl(
			String custodiaUrl) {
		this.custodiaUrl = custodiaUrl;
	}
	public void updateCustodiaEstat(
			boolean custodiat,
			Date custodiaData,
			String custodiaId) {
		this.custodiat = custodiat;
		this.custodiaData = custodiaData;
		this.custodiaId = custodiaId;
	}

	public static Builder getBuilder(
			DocumentTipusEnumDto documentTipus,
			String nom,
			Date data,
			ExpedientEntity expedient,
			MetaNodeEntity metaNode,
			ContingutEntity pare,
			EntitatEntity entitat) {
		return new Builder(
				documentTipus,
				nom,
				data,
				expedient,
				metaNode,
				pare,
				entitat);
	}
	public static class Builder {
		DocumentEntity built;
		Builder(
				DocumentTipusEnumDto documentTipus,
				String nom,
				Date data,
				ExpedientEntity expedient,
				MetaNodeEntity metaNode,
				ContingutEntity pare,
				EntitatEntity entitat) {
			built = new DocumentEntity();
			built.documentTipus = documentTipus;
			built.nom = nom;
			built.data = data;
			built.expedient = expedient;
			built.metaNode = metaNode;
			built.pare = pare;
			built.entitat = entitat;
			built.tipus = ContingutTipusEnumDto.DOCUMENT;
		}
		public Builder ubicacio(String ubicacio) {
			built.ubicacio = ubicacio;
			return this;
		}
		public DocumentEntity build() {
			return built;
		}
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
