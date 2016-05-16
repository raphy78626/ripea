/**
 * 
 */
package es.caib.ripea.core.api.service.ws;

import javax.jws.WebParam;
import javax.jws.WebService;


/**
 * Declaració dels mètodes per al servei d'enviament de contingut a
 * bústies de RIPEA v0.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@WebService(
		name = "BustiaV0",
		serviceName = "BustiaV0Service",
		portName = "BustiaV0ServicePort",
		targetNamespace = "http://www.caib.es/ripea/ws/v0/bustia")
public interface BustiaV0WsService {

	public enum BustiaContingutTipus {
		EXPEDIENT,
		DOCUMENT,
		REGISTRE_ENTRADA
	}

	/**
	 * Envia un contingut a la bústia per defecte de la unitat administrativa.
	 * 
	 * @param entitat
	 *            Codi de l'entitat.
	 * @param unitatAdministrativa
	 *            Codi de la unitat administrativa.
	 * @param tipus
	 *            Codi de l'usuari a cercar.
	 * @param referencia
	 *            Referència per a consultar el contingut i posar-lo dins la bústia.
	 */
	public void enviarContingut(
			@WebParam(name="entitat") String entitat,
			@WebParam(name="unitatAdministrativa") String unitatAdministrativa,
			@WebParam(name="tipus") BustiaContingutTipus tipus,
			@WebParam(name="referencia") String referencia);

}
