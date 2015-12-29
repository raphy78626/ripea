/**
 * 
 */
package es.caib.ripea.ws.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.junit.Test;

import es.caib.ripea.ws.bustia.Bustia;
import es.caib.ripea.ws.bustia.BustiaEnviamentTipus;
import es.caib.ripea.ws.bustia.BustiaService;

/**
 * Client de test per al servei bustia de RIPEA.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class BustiaTest {

	private static final String ENDPOINT_ADDRESS = "http://localhost:8080/ripea/ws/bustia";
	private static final String USERNAME = null;
	private static final String PASSWORD = null;


	@Test
	public void test() throws MalformedURLException {
		getBustiaServicePort().enviarUnitatAdministrativa(
				new Long(1),
				"unitatCodi",
				BustiaEnviamentTipus.EXPEDIENT,
				"expedientRef",
				"documentRef",
				null);
	}



	private Bustia getBustiaServicePort() throws MalformedURLException {
		URL url = new URL(ENDPOINT_ADDRESS + "?wsdl");
		Bustia bustia = new BustiaService(url).getBustiaServicePort();
		@SuppressWarnings("rawtypes")
		List<Handler> handlerChain = new ArrayList<Handler>();
		handlerChain.add(new LogMessageHandler());
		if (USERNAME != null) {
			BindingProvider bp = (BindingProvider)bustia;
			bp.getBinding().setHandlerChain(handlerChain);
			bp.getRequestContext().put(
					BindingProvider.USERNAME_PROPERTY,
					USERNAME);
			bp.getRequestContext().put(
					BindingProvider.PASSWORD_PROPERTY,
					PASSWORD);
		}
		return bustia;
	}

}
