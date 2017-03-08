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
public class ArxiuNotFoundException extends ArxiuException {

	public ArxiuNotFoundException(
			String metode,
			int httpStatus,
			String arxiuCodi,
			String arxiuDescripcio) {
		super(	metode,
				httpStatus,
				arxiuCodi,
				arxiuDescripcio);
	}

}
