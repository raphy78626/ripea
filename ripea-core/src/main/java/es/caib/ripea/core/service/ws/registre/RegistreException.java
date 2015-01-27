/**
 * 
 */
package es.caib.ripea.core.service.ws.registre;

/**
 * Excepció que es llança per a mostrar els errors en el
 * servei web de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@SuppressWarnings("serial")
public class RegistreException extends RuntimeException {

	public RegistreException() {
		super();
	}

	public RegistreException(Throwable cause) {
		super(cause);
	}

	public RegistreException(String message) {
		super(message);
	}

	public RegistreException(String message, Throwable cause) {
		super(message, cause);
	}

}
