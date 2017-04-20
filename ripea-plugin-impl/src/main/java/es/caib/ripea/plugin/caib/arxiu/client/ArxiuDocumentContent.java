/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu.client;

/**
 * Estructura que representa un contingut d'un document de l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuDocumentContent {

	protected String name;
	private ArxiuDocumentContentTypeEnum type;
	private String contentType;
	private String contentBase64;
	private String encoding;

	public ArxiuDocumentContentTypeEnum getType() {
		return type;
	}
	public void setType(ArxiuDocumentContentTypeEnum type) {
		this.type = type;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getContentBase64() {
		return contentBase64;
	}
	public void setContentBase64(String contentBase64) {
		this.contentBase64 = contentBase64;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

}
