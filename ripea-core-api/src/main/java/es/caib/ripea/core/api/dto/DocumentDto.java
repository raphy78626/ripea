/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.Date;

/**
 * Informació d'un document.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class DocumentDto extends NodeDto {

	protected DocumentTipusEnumDto documentTipus;
	protected DocumentEstatEnumDto estat;
	protected String ubicacio;
	private Date data;
	private Date custodiaData;
	private String custodiaId;
	private String custodiaUrl;
	private DocumentVersioDto versioDarrera;

	public DocumentTipusEnumDto getDocumentTipus() {
		return documentTipus;
	}
	public void setDocumentTipus(DocumentTipusEnumDto documentTipus) {
		this.documentTipus = documentTipus;
	}
	public DocumentEstatEnumDto getEstat() {
		return estat;
	}
	public void setEstat(DocumentEstatEnumDto estat) {
		this.estat = estat;
	}
	public String getUbicacio() {
		return ubicacio;
	}
	public void setUbicacio(String ubicacio) {
		this.ubicacio = ubicacio;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public Date getCustodiaData() {
		return custodiaData;
	}
	public void setCustodiaData(Date custodiaData) {
		this.custodiaData = custodiaData;
	}
	public String getCustodiaId() {
		return custodiaId;
	}
	public void setCustodiaId(String custodiaId) {
		this.custodiaId = custodiaId;
	}
	public String getCustodiaUrl() {
		return custodiaUrl;
	}
	public void setCustodiaUrl(String custodiaUrl) {
		this.custodiaUrl = custodiaUrl;
	}
	public DocumentVersioDto getVersioDarrera() {
		return versioDarrera;
	}
	public void setVersioDarrera(DocumentVersioDto versioDarrera) {
		this.versioDarrera = versioDarrera;
	}
	public MetaDocumentDto getMetaDocument() {
		return (MetaDocumentDto)getMetaNode();
	}

	protected DocumentDto copiarContenidor(ContingutDto original) {
		DocumentDto copia = new DocumentDto();
		copia.setId(original.getId());
		copia.setNom(original.getNom());
		return copia;
	}

	private static final long serialVersionUID = -139254994389509932L;

}
