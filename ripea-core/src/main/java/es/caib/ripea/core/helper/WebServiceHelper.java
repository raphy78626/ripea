/**
 * 
 */
package es.caib.ripea.core.helper;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.caib.loginModule.auth.ControladorSesion;
import es.caib.loginModule.client.AuthorizationToken;


/**
 * Helper per a accedir a serveis web.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class WebServiceHelper {

	static final boolean ENTORN_PRODUCCIO = false;

	public <T> T generarClient(
			URL url,
			QName serviceQname,
			QName portQname,
			Class<T> portClass,
			String userName,
			String password) throws Exception {
		Service service = Service.create(url, serviceQname);
		T port = service.getPort(portQname, portClass);
		if (userName != null) {
			BindingProvider bp = (BindingProvider)port;
			if (ENTORN_PRODUCCIO) {
				ControladorSesion controlador = new ControladorSesion();
				controlador.autenticar(userName, password);
				AuthorizationToken token = controlador.getToken();
				bp.getRequestContext().put(
						BindingProvider.USERNAME_PROPERTY,
						token.getUser());
				bp.getRequestContext().put(
						BindingProvider.PASSWORD_PROPERTY,
						token.getPassword());
			} else {
				bp.getRequestContext().put(
						BindingProvider.USERNAME_PROPERTY,
						userName);
				bp.getRequestContext().put(
						BindingProvider.PASSWORD_PROPERTY,
						password);
			}
			bp.getBinding().getHandlerChain().add(new SOAPLoggingHandler());
		}
		return port;
	}



	private class SOAPLoggingHandler implements SOAPHandler<SOAPMessageContext> {
		public Set<QName> getHeaders() {
			return null;
		}
		public boolean handleMessage(SOAPMessageContext smc) {
			logToSystemOut(smc);
			return true;
		}
		public boolean handleFault(SOAPMessageContext smc) {
			logToSystemOut(smc);
			return true;
		}
		public void close(MessageContext messageContext) {
		}
		private void logToSystemOut(SOAPMessageContext smc) {
			if (logger.isDebugEnabled()) {
				StringBuilder sb = new StringBuilder();
				Boolean outboundProperty = (Boolean)smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
				if (outboundProperty.booleanValue())
					sb.append("Missarge sortint: ");
				else
					sb.append("Missarge entrant: ");
				SOAPMessage message = smc.getMessage();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				try {
					message.writeTo(baos);
					sb.append(baos.toString());
				} catch (Exception ex) {
					sb.append("Error al imprimir el missatge XML: " + ex.getMessage());
				}
				logger.debug(sb.toString());
			}
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(WebServiceHelper.class);

}
