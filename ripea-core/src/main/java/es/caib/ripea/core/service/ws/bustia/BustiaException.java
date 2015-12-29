/**
 * 
 */
package es.caib.ripea.core.service.ws.bustia;

/**
 * Excepció que es llança per a mostrar els errors en el
 * servei web de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@SuppressWarnings("serial")
public class BustiaException extends RuntimeException {

	public BustiaException() {
		super();
	}

	public BustiaException(Throwable cause) {
		super(cause);
	}

	public BustiaException(String message) {
		super(message);
	}

	public BustiaException(String message, Throwable cause) {
		super(message, cause);
	}

}
