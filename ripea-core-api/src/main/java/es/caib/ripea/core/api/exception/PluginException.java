/**
 * 
 */
package es.caib.ripea.core.api.exception;

/**
 * Excepció que es llança quan es dona un error a un plugin.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@SuppressWarnings("serial")
public class PluginException extends RuntimeException {

	public PluginException() {
		super();
	}

	public PluginException(Throwable cause) {
		super(cause);
	}

	public PluginException(String message) {
		super(message);
	}

	public PluginException(String message, Throwable cause) {
		super(message, cause);
	}

}
