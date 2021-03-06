/**
 * 
 */
package es.caib.ripea.core.service.ws.bustia;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebService;

import org.jboss.annotation.security.SecurityDomain;
import org.jboss.wsf.spi.annotation.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

/**
 * Implementació dels mètodes per al servei de bústies de RIPEA.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Stateless
@WebService(
		name = "Bustia",
		serviceName = "BustiaService",
		portName = "BustiaServicePort",
		targetNamespace = "http://www.caib.es/ripea/ws/bustia")
@WebContext(
		contextRoot = "/ripea/ws",
		urlPattern = "/bustia",
		authMethod = "WSBASIC",
		transportGuarantee = "NONE",
		secureWSDLAccess = false)
@RolesAllowed({"IPA_BSTWS"})
@SecurityDomain("seycon")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class BustiaWsBean implements BustiaWs {

	@Autowired
	private BustiaWsImpl delegate;

	@Override
	public void enviarUnitatAdministrativa(
			Long entitatId,
			String unitatCodi,
			BustiaEnviamentTipus tipus,
			String expedientRef,
			String documentRef,
			AnotacionRegistro anotacio) {
		delegate.enviarUnitatAdministrativa(
				entitatId,
				unitatCodi,
				tipus,
				expedientRef,
				documentRef,
				anotacio);
	}

}
