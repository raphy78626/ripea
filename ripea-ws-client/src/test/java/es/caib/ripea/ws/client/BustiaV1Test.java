/**
 * 
 */
package es.caib.ripea.ws.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.junit.Test;

import es.caib.ripea.ws.v1.bustia.BustiaV1;
import es.caib.ripea.ws.v1.bustia.BustiaV1Service;
import es.caib.ripea.ws.v1.bustia.RegistreAnotacio;

/**
 * Client de test per al servei bustia de RIPEA.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class BustiaV1Test {

	private static final String ENDPOINT_ADDRESS = "http://localhost:8080/ripea/ws/v1/bustia";
	private static final String USERNAME = null;
	private static final String PASSWORD = null;


	@Test
	public void test() throws MalformedURLException, DatatypeConfigurationException {
		RegistreAnotacio anotacio = new RegistreAnotacio(); 
		anotacio.setAplicacioCodi("CLIENT_TEST");
		anotacio.setAplicacioVersio("2");
		anotacio.setAssumpteCodi("CodA");
        anotacio.setAssumpteDescripcio("Descripcio CodA");
        anotacio.setAssumpteTipusCodi("TD01");
        anotacio.setAssumpteDescripcio("Assumpte de proves");
        anotacio.setUsuariCodi("u104848");
        anotacio.setUsuariNom("VHZ");
        anotacio.setData(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
        anotacio.setExtracte("Prova2");
        anotacio.setOficinaCodi("10");
        anotacio.setOficinaDescripcio("Oficina de proves");
        anotacio.setEntitatCodi("codientitat");
        anotacio.setEntitatDescripcio("Descripció entitat");
        anotacio.setLlibreCodi("L01");
        anotacio.setLlibreDescripcio("llibre descripció");
        anotacio.setNumero(28);
        anotacio.setIdiomaCodi("1");
        anotacio.setIdiomaDescripcio("Català");
        anotacio.setIdentificador("9/10/2015");
		getBustiaServicePort().enviarAnotacioRegistreEntrada(
				"A04003003", // "entitatCodi",
				"A04013499", // "unitatAdministrativaCodi",
				anotacio);
	}



	private BustiaV1 getBustiaServicePort() throws MalformedURLException {
		URL url = new URL(ENDPOINT_ADDRESS + "?wsdl");
		BustiaV1 bustia = new BustiaV1Service(url).getBustiaV1ServicePort();
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
