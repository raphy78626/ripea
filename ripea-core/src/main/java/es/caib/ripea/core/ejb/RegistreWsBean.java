/**
 * 
 */
package es.caib.ripea.core.ejb;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebService;

import org.jboss.annotation.security.SecurityDomain;
import org.jboss.wsf.spi.annotation.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import es.caib.ripea.core.service.ws.registre.AnotacionRegistro;
import es.caib.ripea.core.service.ws.registre.Registre;
import es.caib.ripea.core.service.ws.registre.RegistreImpl;

/**
 * Implementació dels mètodes per al servei de notificacions de
 * registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Stateless
@WebService(
		name = "Registre",
		serviceName = "RegistreService",
		portName = "RegistreServicePort",
		targetNamespace = "http://www.caib.es/ripea/ws/registre")
@WebContext(
		contextRoot = "/ripea/ws",
		urlPattern = "/registre",
		authMethod = "WSBASIC",
		transportGuarantee = "NONE",
		secureWSDLAccess = false)
@RolesAllowed({"IPA_REGWS"})
@SecurityDomain("seycon")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class RegistreWsBean implements Registre {

	@Autowired
	private RegistreImpl delegate;

	@Override
	public boolean avisAnotacio(AnotacionRegistro anotacio) {
		return delegate.avisAnotacio(anotacio);
	}

}
