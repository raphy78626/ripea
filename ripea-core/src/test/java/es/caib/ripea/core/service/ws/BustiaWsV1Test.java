/**
 * 
 */
package es.caib.ripea.core.service.ws;

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

import es.caib.ripea.core.api.registre.RegistreAnotacio;
import es.caib.ripea.core.api.registre.RegistreInteressat;
import es.caib.ripea.core.api.service.ws.BustiaV1WsService;

/**
 * Classe de proves pel registre versió 1.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class BustiaWsV1Test {

	private static final String ENDPOINT_ADDRESS = "http://localhost:8080/ripea/ws/v1/bustia";

	public static void main(String[] args) {
		try {
			new BustiaWsV1Test().provaEnviamentContingut();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void provaEnviamentContingut() throws Exception {
		
		// Anotació de registre
		Date ara = new Date();
		String numeroIdentificador = new SimpleDateFormat("yyMMddHHmm").format(ara);
		RegistreAnotacio anotacio = new RegistreAnotacio();
		anotacio.setNumero(Integer.valueOf(numeroIdentificador));
		anotacio.setData(ara);
		anotacio.setIdentificador(numeroIdentificador);
		anotacio.setExtracte("Extracte " + numeroIdentificador);
		anotacio.setOficinaCodi("0");
		anotacio.setAssumpteTipusCodi("0");
		anotacio.setIdiomaCodi("CA");
		anotacio.setEntitatCodi("?");
		anotacio.setLlibreCodi("?");
		
		// Envia l'anotació de registre.
		getBustiaService().enviarAnotacioRegistreEntrada(
				"A04019299",
				"A04019299",
				anotacio);
	}

	private BustiaV1WsService getBustiaService() throws Exception {
		URL url = new URL(ENDPOINT_ADDRESS + "?wsdl");
		QName qname = new QName(
				"http://www.caib.es/ripea/ws/v1/bustia",
				"BustiaV1Service");
		Service service = Service.create(url, qname);
		BustiaV1WsService bustiaWs = service.getPort(BustiaV1WsService.class);
		BindingProvider bp = (BindingProvider)bustiaWs;
		@SuppressWarnings("rawtypes")
		List<Handler> handlerChain = new ArrayList<Handler>();
		handlerChain.add(new LogMessageHandler());
		bp.getBinding().setHandlerChain(handlerChain);

		return bustiaWs;
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
