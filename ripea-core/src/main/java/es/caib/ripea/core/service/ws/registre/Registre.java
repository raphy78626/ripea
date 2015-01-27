/**
 * 
 */
package es.caib.ripea.core.service.ws.registre;

import javax.jws.WebService;


/**
 * Declaració dels mètodes per al servei de registre de RIPEA.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@WebService(
		name = "Registre",
		serviceName = "RegistreService",
		portName = "RegistreServicePort",
		targetNamespace = "http://www.caib.es/ripea/ws/registre")
public interface Registre {

	/**
	 * Avisa d'una nova anotació de registre.
	 * 
	 * @param anotacio Dades de l'anotació.
	 * @return true si s'ha processat correctament o false en cas contrari.
	 */
	//@PreAuthorize("hasRole('ROLE_REGWS')")
	public boolean avisAnotacio(
			AnotacionRegistro anotacio);

}
