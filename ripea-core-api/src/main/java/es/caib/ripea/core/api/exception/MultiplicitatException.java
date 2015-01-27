/**
 * 
 */
package es.caib.ripea.core.api.exception;

/**
 * Excepció que es llança quan es volen afegir a un node més dades d'un
 * mateix tipus que les permeses.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@SuppressWarnings("serial")
public class MultiplicitatException extends RuntimeException {

	public MultiplicitatException() {
		super();
	}

	public MultiplicitatException(Throwable cause) {
		super(cause);
	}

	public MultiplicitatException(String message) {
		super(message);
	}

	public MultiplicitatException(String message, Throwable cause) {
		super(message, cause);
	}

}
