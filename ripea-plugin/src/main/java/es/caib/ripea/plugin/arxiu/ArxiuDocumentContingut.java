/**
 * 
 */
package es.caib.ripea.plugin.arxiu;

/**
 * Estructura que representa un contingut d'un document de l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuDocumentContingut {

	private String contentType;
	private byte[] contingut;

	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public byte[] getContingut() {
		return contingut;
	}
	public void setContingut(byte[] contingut) {
		this.contingut = contingut;
	}

}
