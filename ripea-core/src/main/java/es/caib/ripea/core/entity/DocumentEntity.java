/**
 * 
 */
package es.caib.ripea.core.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.api.dto.ContingutTipusEnumDto;
import es.caib.ripea.core.api.dto.DocumentEstatEnumDto;
import es.caib.ripea.core.api.dto.DocumentNtiEstadoElaboracionEnumDto;
import es.caib.ripea.core.api.dto.DocumentNtiOrigenEnumDto;
import es.caib.ripea.core.api.dto.DocumentNtiTipoDocumentalEnumDto;
import es.caib.ripea.core.api.dto.DocumentNtiTipoFirmaEnumDto;
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
	private DocumentTipusEnumDto documentTipus;
	@Column(name = "estat", nullable = false)
	private DocumentEstatEnumDto estat;
	@Column(name = "ubicacio", length = 255)
	private String ubicacio;
	@ManyToOne(optional = true)
	@JoinColumn(name = "expedient_id")
	@ForeignKey(name = "ipa_expedient_document_fk")
	private ExpedientEntity expedient;
	@Column(name = "data", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_captura", nullable = false)
	private Date dataCaptura;
	@Column(name = "custodia_data")
	@Temporal(TemporalType.TIMESTAMP)
	private Date custodiaData;
	@Column(name = "custodia_id", length = 256)
	private String custodiaId;
	@Column(name = "custodia_url", length = 1024)
	private String custodiaUrl;
	@Column(name = "nti_version", length = 5, nullable = false)
	private String ntiVersion;
	@Column(name = "nti_identif", length = 48, nullable = false)
	private String ntiIdentificador;
	@Column(name = "nti_organo", length = 9, nullable = false)
	private String ntiOrgano;
	@Column(name = "nti_origen", length = 2, nullable = false)
	@Enumerated(EnumType.STRING)
	private DocumentNtiOrigenEnumDto ntiOrigen;
	@Column(name = "nti_estela", length = 4, nullable = false)
	@Enumerated(EnumType.STRING)
	private DocumentNtiEstadoElaboracionEnumDto ntiEstadoElaboracion;
	@Column(name = "nti_tipdoc", length = 4, nullable = false)
	@Enumerated(EnumType.STRING)
	private DocumentNtiTipoDocumentalEnumDto ntiTipoDocumental;
	@Column(name = "nti_idorig", length = 48, nullable = false)
	private String ntiIdDocumentoOrigen;
	@Column(name = "nti_tipfir", length = 4)
	@Enumerated(EnumType.STRING)
	private DocumentNtiTipoFirmaEnumDto ntiTipoFirma;
	@Column(name = "nti_csv", length = 256)
	private String ntiCsv;
	@Column(name = "nti_csvreg", length = 512)
	private String ntiCsvRegulacion;
	@ManyToOne(optional = true)
	@JoinColumn(name = "versio_darrera_id")
	@ForeignKey(name = "ipa_verdarrera_document_fk")
	private DocumentVersioEntity versioDarrera;
	@OneToMany(
			mappedBy = "document",
			fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private List<DocumentVersioEntity> versions;



	public DocumentTipusEnumDto getDocumentTipus() {
		return documentTipus;
	}
	public DocumentEstatEnumDto getEstat() {
		return estat;
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
	public Date getDataCaptura() {
		return dataCaptura;
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
	public String getNtiVersion() {
		return ntiVersion;
	}
	public String getNtiIdentificador() {
		return ntiIdentificador;
	}
	public String getNtiOrgano() {
		return ntiOrgano;
	}
	public DocumentNtiOrigenEnumDto getNtiOrigen() {
		return ntiOrigen;
	}
	public DocumentNtiEstadoElaboracionEnumDto getNtiEstadoElaboracion() {
		return ntiEstadoElaboracion;
	}
	public DocumentNtiTipoDocumentalEnumDto getNtiTipoDocumental() {
		return ntiTipoDocumental;
	}
	public String getNtiIdDocumentoOrigen() {
		return ntiIdDocumentoOrigen;
	}
	public DocumentNtiTipoFirmaEnumDto getNtiTipoFirma() {
		return ntiTipoFirma;
	}
	public String getNtiCsv() {
		return ntiCsv;
	}
	public String getNtiCsvRegulacion() {
		return ntiCsvRegulacion;
	}
	public DocumentVersioEntity getVersioDarrera() {
		return versioDarrera;
	}

	public MetaDocumentEntity getMetaDocument() {
		return (MetaDocumentEntity)getMetaNode();
	}

	public void update(
			MetaDocumentEntity metaDocument,
			String nom,
			Date data,
			String ubicacio,
			Date dataCaptura,
			String ntiOrgano,
			DocumentNtiOrigenEnumDto ntiOrigen,
			DocumentNtiEstadoElaboracionEnumDto ntiEstadoElaboracion,
			DocumentNtiTipoDocumentalEnumDto ntiTipoDocumental,
			String ntiIdDocumentoOrigen,
			DocumentNtiTipoFirmaEnumDto ntiTipoFirma,
			String ntiCsv,
			String ntiCsvRegulacion) {
		this.metaNode = metaDocument;
		this.nom = nom;
		this.data = data;
		this.ubicacio = ubicacio;
		this.dataCaptura = dataCaptura;
		this.ntiOrgano = ntiOrgano;
		this.ntiOrigen = ntiOrigen;
		this.ntiEstadoElaboracion = ntiEstadoElaboracion;
		this.ntiTipoDocumental = ntiTipoDocumental;
		this.ntiIdDocumentoOrigen = ntiIdDocumentoOrigen;
		this.ntiTipoFirma = ntiTipoFirma;
		this.ntiCsv = ntiCsv;
		this.ntiCsvRegulacion = ntiCsvRegulacion;
	}
	public void updateNtiIdentificador(
			String ntiIdentificador) {
		this.ntiIdentificador = ntiIdentificador;
	}
	public void updateEstat(
			DocumentEstatEnumDto estat) {
		this.estat = estat;
	}
	public void updateVersioDarrera(
			DocumentVersioEntity versioDarrera) {
		this.versioDarrera = versioDarrera;
	}

	public void updateInformacioCustodia(
			Date custodiaData,
			String custodiaId,
			String custodiaUrl) {
		this.custodiaData = custodiaData;
		this.custodiaId = custodiaId;
		this.custodiaUrl = custodiaUrl;
	}

	public static Builder getBuilder(
			DocumentTipusEnumDto documentTipus,
			DocumentEstatEnumDto estat,
			String nom,
			Date data,
			Date dataCaptura,
			String ntiVersion,
			String ntiOrgano,
			DocumentNtiOrigenEnumDto ntiOrigen,
			DocumentNtiEstadoElaboracionEnumDto ntiEstadoElaboracion,
			DocumentNtiTipoDocumentalEnumDto ntiTipoDocumental,
			ExpedientEntity expedient,
			MetaNodeEntity metaNode,
			ContingutEntity pare,
			EntitatEntity entitat) {
		return new Builder(
				documentTipus,
				estat,
				nom,
				data,
				dataCaptura,
				ntiVersion,
				ntiOrgano,
				ntiOrigen,
				ntiEstadoElaboracion,
				ntiTipoDocumental,
				expedient,
				metaNode,
				pare,
				entitat);
	}
	public static class Builder {
		DocumentEntity built;
		Builder(
				DocumentTipusEnumDto documentTipus,
				DocumentEstatEnumDto estat,
				String nom,
				Date data,
				Date dataCaptura,
				String ntiVersion,
				String ntiOrgano,
				DocumentNtiOrigenEnumDto ntiOrigen,
				DocumentNtiEstadoElaboracionEnumDto ntiEstadoElaboracion,
				DocumentNtiTipoDocumentalEnumDto ntiTipoDocumental,
				ExpedientEntity expedient,
				MetaNodeEntity metaNode,
				ContingutEntity pare,
				EntitatEntity entitat) {
			built = new DocumentEntity();
			built.documentTipus = documentTipus;
			built.estat = estat;
			built.nom = nom;
			built.data = data;
			built.dataCaptura = dataCaptura;
			built.ntiVersion = ntiVersion;
			built.ntiIdentificador = new Long(System.currentTimeMillis()).toString();
			built.ntiOrgano = ntiOrgano;
			built.ntiOrigen = ntiOrigen;
			built.ntiEstadoElaboracion = ntiEstadoElaboracion;
			built.ntiTipoDocumental = ntiTipoDocumental;
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
		public Builder ntiIdDocumentoOrigen(String ntiIdDocumentoOrigen) {
			built.ntiIdDocumentoOrigen = ntiIdDocumentoOrigen;
			return this;
		}
		public Builder ntiTipoFirma(DocumentNtiTipoFirmaEnumDto ntiTipoFirma) {
			built.ntiTipoFirma = ntiTipoFirma;
			return this;
		}
		public Builder ntiCsv(String ntiCsv) {
			built.ntiCsv = ntiCsv;
			return this;
		}
		public Builder ntiCsvRegulacion(String ntiCsvRegulacion) {
			built.ntiCsvRegulacion = ntiCsvRegulacion;
			return this;
		}
		public DocumentEntity build() {
			return built;
		}
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
