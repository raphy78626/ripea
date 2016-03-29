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

	public enum BustiaContingutTipus {
		EXPEDIENT,
		DOCUMENT,
		REGISTRE_ENTRADA
	}

	/**
	 * Envia un contingut a la bústia per defecte de la unitat administrativa.
	 * 
	 * @param anotacio Dades de l'anotació.
	 */
	//@PreAuthorize("hasRole('ROLE_BSTWS')")
	public void enviarContingut(
			@WebParam(name="entitat") String entitat,
			@WebParam(name="unitatAdministrativa") String unitatAdministrativa,
			@WebParam(name="tipus") BustiaContingutTipus tipus,
			@WebParam(name="referencia") String referencia);

}
