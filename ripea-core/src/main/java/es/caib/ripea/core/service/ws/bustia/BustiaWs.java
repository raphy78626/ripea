/**
 * 
 */
package es.caib.ripea.core.service.ws.bustia;

import javax.jws.WebParam;
import javax.jws.WebService;


/**
 * Declaració dels mètodes per al servei de bústia de RIPEA.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@WebService(
		name = "Bustia",
		serviceName = "BustiaService",
		portName = "BustiaServicePort",
		targetNamespace = "http://www.caib.es/ripea/ws/bustia")
public interface BustiaWs {

	public enum BustiaEnviamentTipus {
		EXPEDIENT,
		DOCUMENT,
		REGISTRE
	}

	/**
	 * Envia un contingut a la bústia per defecte de la unitat administrativa.
	 * 
	 * @param anotacio Dades de l'anotació.
	 */
	//@PreAuthorize("hasRole('ROLE_BSTWS')")
	public void enviarUnitatAdministrativa(
			@WebParam(name="entitatId") Long entitatId,
			@WebParam(name="unitatCodi") String unitatCodi,
			@WebParam(name="tipus") BustiaEnviamentTipus tipus,
			@WebParam(name="expedientRef") String expedientRef,
			@WebParam(name="documentRef") String documentRef,
			@WebParam(name="anotacio") AnotacionRegistro anotacio);

}
