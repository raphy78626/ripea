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

	private Date data;
	private DocumentVersioDto darreraVersio;

	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public DocumentVersioDto getDarreraVersio() {
		return darreraVersio;
	}
	public void setDarreraVersio(DocumentVersioDto darreraVersio) {
		this.darreraVersio = darreraVersio;
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
