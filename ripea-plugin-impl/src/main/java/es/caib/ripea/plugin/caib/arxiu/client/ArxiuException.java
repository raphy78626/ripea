/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu.client;

/**
 * Excepció que es produeix al accedir als serveis REST de
 * l'arxiu digital.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@SuppressWarnings("serial")
public class ArxiuException extends RuntimeException {

	private String metode;
	private int httpStatus;
	private String arxiuCodi;
	private String arxiuDescripcio;

	public ArxiuException(
			String metode,
			String message) {
		super(message);
		this.metode = metode;
	}

	public ArxiuException(
			String metode,
			String message,
			Throwable cause) {
		super(message, cause);
		this.metode = metode;
	}

	public ArxiuException(
			String metode,
			int httpStatus,
			String arxiuCodi,
			String arxiuDescripcio) {
		super("[HTTP_" + httpStatus + ", " + arxiuCodi + "] " + arxiuDescripcio);
		this.metode = metode;
		this.arxiuCodi = arxiuCodi;
		this.arxiuDescripcio = arxiuDescripcio;
	}

	public String getMetode() {
		return metode;
	}
	public int getHttpStatus() {
		return httpStatus;
	}
	public String getArxiuCodi() {
		return arxiuCodi;
	}
	public String getArxiuDescripcio() {
		return arxiuDescripcio;
	}

}
