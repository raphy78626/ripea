/**
 * 
 */
package es.caib.ripea.core.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.ForeignKey;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.api.dto.DocumentEnviamentEstatEnumDto;
import es.caib.ripea.core.audit.RipeaAuditable;

/**
 * Classe del model de dades que representa un enviament d'un document
 * i controla l'estat en el qual es troba.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table( name = "ipa_document_enviament",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {
						"expedient_id",
						"document_id",
						"assumpte",
						"data_enviament",
						"dtype",
						"tipus",
						"destin_doctip",
						"destinatari_nif"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@EntityListeners(AuditingEntityListener.class)
public abstract class DocumentEnviamentEntity extends RipeaAuditable<Long> {

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "expedient_id")
	@ForeignKey(name = "ipa_expedient_docenv_fk")
	protected ExpedientEntity expedient;
	@Column(name = "estat", nullable = false)
	@Enumerated(EnumType.STRING)
	protected DocumentEnviamentEstatEnumDto estat;
	@Column(name = "assumpte", length = 256, nullable = false)
	protected String assumpte;
	@Column(name = "data_enviament", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	protected Date dataEnviament;
	@Column(name = "observacions", length = 256)
	protected String observacions;
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "document_id")
	@ForeignKey(name = "ipa_document_docenv_fk")
	protected DocumentEntity document;
	@ManyToMany(
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY)
	@JoinTable(
			name = "ipa_document_enviament_doc",
			joinColumns = {@JoinColumn(name = "document_enviament_id")},
			inverseJoinColumns = {@JoinColumn(name = "document_id")})
	protected List<DocumentEntity> annexos = new ArrayList<DocumentEntity>();
	@Version
	private long version = 0;



	public ExpedientEntity getExpedient() {
		return expedient;
	}
	public DocumentEnviamentEstatEnumDto getEstat() {
		return estat;
	}
	public String getAssumpte() {
		return assumpte;
	}
	public Date getDataEnviament() {
		return dataEnviament;
	}
	public String getObservacions() {
		return observacions;
	}
	public DocumentEntity getDocument() {
		return document;
	}
	public List<DocumentEntity> getAnnexos() {
		return annexos;
	}

	public void addAnnex(DocumentEntity annex) {
		annexos.add(annex);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((assumpte == null) ? 0 : assumpte.hashCode());
		result = prime * result + ((dataEnviament == null) ? 0 : dataEnviament.hashCode());
		result = prime * result + ((document == null) ? 0 : document.hashCode());
		result = prime * result + ((expedient == null) ? 0 : expedient.hashCode());
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
		DocumentEnviamentEntity other = (DocumentEnviamentEntity) obj;
		if (assumpte == null) {
			if (other.assumpte != null)
				return false;
		} else if (!assumpte.equals(other.assumpte))
			return false;
		if (dataEnviament == null) {
			if (other.dataEnviament != null)
				return false;
		} else if (!dataEnviament.equals(other.dataEnviament))
			return false;
		if (document == null) {
			if (other.document != null)
				return false;
		} else if (!document.equals(other.document))
			return false;
		if (expedient == null) {
			if (other.expedient != null)
				return false;
		} else if (!expedient.equals(other.expedient))
			return false;
		return true;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
