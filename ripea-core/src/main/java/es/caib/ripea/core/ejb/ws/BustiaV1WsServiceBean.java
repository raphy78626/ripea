/**
 * 
 */
package es.caib.ripea.core.ejb.ws;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebService;

import org.jboss.annotation.security.SecurityDomain;
import org.jboss.wsf.spi.annotation.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import es.caib.regweb3.ws.api.v3.RegistroEntradaWs;
import es.caib.ripea.core.api.service.ws.BustiaV1WsService;
import es.caib.ripea.core.service.ws.bustia.BustiaV1WsServiceImpl;

/**
 * Implementació dels mètodes per al servei de bústies de RIPEA.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Stateless
@WebService(
		name = "BustiaV1",
		serviceName = "BustiaV1Service",
		portName = "BustiaV1ServicePort",
		targetNamespace = "http://www.caib.es/ripea/ws/v1/bustia")
@WebContext(
		contextRoot = "/ripea/ws",
		urlPattern = "/v1/bustia",
		authMethod = "WSBASIC",
		transportGuarantee = "NONE",
		secureWSDLAccess = false)
@RolesAllowed({"IPA_BSTWS"})
@SecurityDomain("seycon")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class BustiaV1WsServiceBean implements BustiaV1WsService {

	@Autowired
	private BustiaV1WsServiceImpl delegate;

	@Override
	public void enviarAnotacioRegistreEntrada(
			String entitat,
			String unitatAdministrativa,
			RegistroEntradaWs registroEntrada) {
		delegate.enviarAnotacioRegistreEntrada(
				entitat,
				unitatAdministrativa,
				registroEntrada);
		
	}

	@Override
	public void enviarDocument(
			String entitat,
			String unitatAdministrativa,
			String referenciaDocument) {
		delegate.enviarDocument(
				entitat,
				unitatAdministrativa,
				referenciaDocument);
	}

	@Override
	public void enviarExpedient(
			String entitat,
			String unitatAdministrativa,
			String referenciaExpedient) {
		delegate.enviarExpedient(
				entitat,
				unitatAdministrativa,
				referenciaExpedient);
	}

}
