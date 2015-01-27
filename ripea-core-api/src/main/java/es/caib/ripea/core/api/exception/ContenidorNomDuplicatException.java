/**
 * 
 */
package es.caib.ripea.core.api.exception;

/**
 * Excepció que es llança quan ja existeix un altre contenidor amb el mateix nom.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@SuppressWarnings("serial")
public class ContenidorNomDuplicatException extends RuntimeException {

	public ContenidorNomDuplicatException() {
		super();
	}

	public ContenidorNomDuplicatException(Throwable cause) {
		super(cause);
	}

	public ContenidorNomDuplicatException(String message) {
		super(message);
	}

	public ContenidorNomDuplicatException(String message, Throwable cause) {
		super(message, cause);
	}

}
