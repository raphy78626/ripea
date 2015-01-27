/**
 * 
 */
package es.caib.ripea.core.api.exception;

/**
 * Excepció que es llança quan el nom del contenidor conté caràcters
 * invàlids.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@SuppressWarnings("serial")
public class NomInvalidException extends RuntimeException {

	public NomInvalidException() {
		super();
	}

	public NomInvalidException(Throwable cause) {
		super(cause);
	}

	public NomInvalidException(String message) {
		super(message);
	}

	public NomInvalidException(String message, Throwable cause) {
		super(message, cause);
	}

}
