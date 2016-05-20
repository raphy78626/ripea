/**
 * 
 */
package es.caib.ripea.core.api.service.ws;

import javax.jws.WebParam;
import javax.jws.WebService;

import es.caib.regweb3.ws.api.v3.RegistroEntradaWs;


/**
 * Declaració dels mètodes per al servei d'enviament de contingut a
 * bústies de RIPEA v1.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@WebService(
		name = "BustiaV1",
		serviceName = "BustiaV1Service",
		portName = "BustiaV1ServicePort",
		targetNamespace = "http://www.caib.es/ripea/ws/v1/bustia")
public interface BustiaV1WsService {

	/**
	 * Envia una anotació de registre d'entrada a la bústia per defecte
	 * associada amb la unitat administrativa.
	 * 
	 * @param entitat
	 *            Codi de l'entitat.
	 * @param unitatAdministrativa
	 *            Codi de la unitat administrativa.
	 * @param registreEntrada
	 *            Dades de l'anotació al registre d'entrada.
	 */
	public void enviarAnotacioRegistreEntrada(
			@WebParam(name="entitat") String entitat,
			@WebParam(name="unitatAdministrativa") String unitatAdministrativa,
			@WebParam(name="registreEntrada") RegistroEntradaWs registroEntrada);

	/**
	 * Envia una anotació de registre d'entrada a la bústia per defecte
	 * associada amb la unitat administrativa.
	 * 
	 * @param entitat
	 *            Codi de l'entitat.
	 * @param unitatAdministrativa
	 *            Codi de la unitat administrativa.
	 * @param referenciaDocument
	 *            Referència per a consultar el document.
	 */
	public void enviarDocument(
			@WebParam(name="entitat") String entitat,
			@WebParam(name="unitatAdministrativa") String unitatAdministrativa,
			@WebParam(name="referenciaDocument") String referenciaDocument);

	/**
	 * Envia una anotació de registre d'entrada a la bústia per defecte
	 * associada amb la unitat administrativa.
	 * 
	 * @param entitat
	 *            Codi de l'entitat.
	 * @param unitatAdministrativa
	 *            Codi de la unitat administrativa.
	 * @param referenciaDocument
	 *            Referència per a consultar l'expedient.
	 */
	public void enviarExpedient(
			@WebParam(name="entitat") String entitat,
			@WebParam(name="unitatAdministrativa") String unitatAdministrativa,
			@WebParam(name="referenciaExpedient") String referenciaExpedient);

}