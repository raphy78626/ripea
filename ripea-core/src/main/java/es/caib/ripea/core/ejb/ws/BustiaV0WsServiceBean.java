/**
 * 
 */
package es.caib.ripea.core.ejb.ws;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebService;

import org.jboss.annotation.security.SecurityDomain;
import org.jboss.wsf.spi.annotation.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import es.caib.ripea.core.api.service.ws.BustiaV0WsService;
import es.caib.ripea.core.helper.UsuariHelper;
import es.caib.ripea.core.service.ws.bustia.BustiaV0WsServiceImpl;

/**
 * Implementació dels mètodes per al servei de bústies de RIPEA.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Stateless
@WebService(
		name = "BustiaV0",
		serviceName = "BustiaV0Service",
		portName = "BustiaV0ServicePort",
		targetNamespace = "http://www.caib.es/ripea/ws/v0/bustia")
@WebContext(
		contextRoot = "/ripea/ws",
		urlPattern = "/v0/bustia",
		authMethod = "WSBASIC",
		transportGuarantee = "NONE",
		secureWSDLAccess = false)
@RolesAllowed({"IPA_BSTWS"})
@SecurityDomain("seycon")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class BustiaV0WsServiceBean implements BustiaV0WsService {

	@Autowired
	private BustiaV0WsServiceImpl delegate;

	@Resource
	private SessionContext sessionContext;
	@Autowired
	private UsuariHelper usuariHelper;



	@Override
	public void enviarContingut(
			String entitat,
			String unitatAdministrativa,
			BustiaContingutTipus tipus,
			String referencia) {
		usuariHelper.generarUsuariAutenticatEjb(
				sessionContext,
				true);
		delegate.enviarContingut(
				entitat,
				unitatAdministrativa,
				tipus,
				referencia);
	}

}
