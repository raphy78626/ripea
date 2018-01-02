/**
 * 
 */
package es.caib.ripea.core.api.exception;

/**
 * Excepció que es produeix al accedir a un sistema extern.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@SuppressWarnings("serial")
public class PropietatNotFoundException extends RuntimeException {

	private String propietatCodi;

	public PropietatNotFoundException(
			String propietatCodi,
			String message) {
		super(message);
		this.propietatCodi = propietatCodi;
	}

	public PropietatNotFoundException(
			String propietatCodi,
			String message,
			Throwable cause) {
		super(message, cause);
		this.propietatCodi = propietatCodi;
	}

	public String getPropietatCodi() {
		return propietatCodi;
	}

}
