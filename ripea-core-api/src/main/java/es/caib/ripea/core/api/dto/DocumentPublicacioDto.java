/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.Date;

/**
 * Informació d'una publicació d'un document a un butlletí oficial.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class DocumentPublicacioDto extends DocumentEnviamentDto {

	
	private DocumentPublicacioTipusEnumDto tipus;
	private Date dataPublicacio;
	
	

	public DocumentPublicacioTipusEnumDto getTipus() {
		return tipus;
	}
	public void setTipus(DocumentPublicacioTipusEnumDto tipus) {
		this.tipus = tipus;
	}
	public Date getDataPublicacio() {
		return dataPublicacio;
	}
	public void setDataPublicacio(Date dataPublicacio) {
		this.dataPublicacio = dataPublicacio;
	}

	private static final long serialVersionUID = -139254994389509932L;

}
