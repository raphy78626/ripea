/**
 * 
 */
package es.caib.ripea.core.ejb;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebService;

import org.jboss.wsf.spi.annotation.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import es.caib.ripea.core.service.ws.callback.CallbackRequest;
import es.caib.ripea.core.service.ws.callback.CallbackResponse;
import es.caib.ripea.core.service.ws.callback.MCGDws;
import es.caib.ripea.core.service.ws.callback.MCGDwsImpl;

/**
 * Implementació dels mètodes per al servei de recepció de
 * callbacks de portafirmes.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Stateless
@WebService(
		name = "PortaFIBCallBackWs",
		serviceName = "PortaFIBCallBackWsService",
		portName = "PortaFIBCallBackWsServicePort",
		targetNamespace = "http://v1.server.callback.ws.portafib.caib.es/")
@WebContext(
		contextRoot = "/ripea/ws",
		urlPattern = "/PortaFIBCallBackWs",
		//authMethod = "WSBASIC",
		transportGuarantee = "NONE",
		secureWSDLAccess = false)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class PortaFIBCallbackWsBean implements MCGDws {

	@Autowired
	private MCGDwsImpl delegate;

	@Override
	public CallbackResponse callback(CallbackRequest callbackRequest) {
		return delegate.callback(callbackRequest);
	}

}
