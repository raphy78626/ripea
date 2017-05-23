/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;

/**
 * Informació d'un contingut de document emmagatzemat amb el plugin d'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuPluginDocumentContingutDto implements Serializable {

	private String tipus;
	private String contentType;

	public String getTipus() {
		return tipus;
	}
	public void setTipus(String tipus) {
		this.tipus = tipus;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	private static final long serialVersionUID = -2124829280908976623L;

}
