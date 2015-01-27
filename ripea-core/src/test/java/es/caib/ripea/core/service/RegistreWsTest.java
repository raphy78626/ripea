/**
 * 
 */
package es.caib.ripea.core.service;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.caib.ripea.core.service.ws.registre.AnotacionRegistro;
import es.caib.ripea.core.service.ws.registre.Asunto;
import es.caib.ripea.core.service.ws.registre.Control;
import es.caib.ripea.core.service.ws.registre.Origen;
import es.caib.ripea.core.service.ws.registre.Registre;

/**
 * Classe de proves pel registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class RegistreWsTest {

	private static final String ENDPOINT_ADDRESS = "http://10.35.3.243:8380/ripea/ws/registre";
	private static final String USERNAME = "tomeud";
	private static final String PASSWORD = "tomeud";

	public static void main(String[] args) {
		try {
			boolean processat = new RegistreWsTest().provaAvisAnotacio();
			System.out.println(">>> Processat: " + processat);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private boolean provaAvisAnotacio() throws Exception {
		AnotacionRegistro anotacio = new AnotacionRegistro();
		anotacio.setAccion("0");
		Origen origen = new Origen();
		origen.setCodigoEntidadRegistralOrigen("LIM000001");
		origen.setNumeroRegistroEntrada("" + System.currentTimeMillis() + "/2014");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		origen.setFechaHoraEntrada(sdf.format(new Date()));
		anotacio.setOrigen(origen);
		Asunto asunto = new Asunto();
		asunto.setResumen("Prova d'anotació " + System.currentTimeMillis());
		anotacio.setAsunto(asunto);
		Control control = new Control();
		control.setTipoRegistro("0");
		control.setIndicadorPrueba("1");
		anotacio.setControl(control);
		return getRegistreService().avisAnotacio(anotacio);
	}

	private Registre getRegistreService() throws Exception {
		URL url = new URL(ENDPOINT_ADDRESS + "?wsdl");
		QName qname = new QName(
				"http://www.caib.es/ripea/ws/registre",
				"RegistreService");
		Service service = Service.create(url, qname);
		Registre registre = service.getPort(Registre.class);
		BindingProvider bp = (BindingProvider)registre;
		@SuppressWarnings("rawtypes")
		List<Handler> handlerChain = new ArrayList<Handler>();
		handlerChain.add(new LogMessageHandler());
		bp.getBinding().setHandlerChain(handlerChain);
		bp.getRequestContext().put(
				BindingProvider.USERNAME_PROPERTY,
				USERNAME);
		bp.getRequestContext().put(
				BindingProvider.PASSWORD_PROPERTY,
				PASSWORD);
		return registre;
	}

	private class LogMessageHandler implements SOAPHandler<SOAPMessageContext> {
		public boolean handleMessage(SOAPMessageContext messageContext) {
			log(messageContext);
			return true;
		}
		public Set<QName> getHeaders() {
			return Collections.emptySet();
		}
		public boolean handleFault(SOAPMessageContext messageContext) {
			log(messageContext);
			return true;
		}
		public void close(MessageContext context) {
		}
		private void log(SOAPMessageContext messageContext) {
			SOAPMessage msg = messageContext.getMessage();
			try {
				Boolean outboundProperty = (Boolean)messageContext.get(
						MessageContext.MESSAGE_OUTBOUND_PROPERTY);
				if (outboundProperty)
					System.out.print("Missatge SOAP petició: ");
				else
					System.out.print("Missatge SOAP resposta: ");
				msg.writeTo(System.out);
				System.out.println();
			} catch (SOAPException ex) {
				Logger.getLogger(LogMessageHandler.class.getName()).log(
						Level.SEVERE,
						null,
						ex);
			} catch (IOException ex) {
				Logger.getLogger(LogMessageHandler.class.getName()).log(
						Level.SEVERE,
						null,
						ex);
			}
		}
	}

}
