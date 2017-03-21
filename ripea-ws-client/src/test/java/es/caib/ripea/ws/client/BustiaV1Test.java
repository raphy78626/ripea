/**
 * 
 */
package es.caib.ripea.ws.client;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import es.caib.ripea.ws.v1.bustia.BustiaV1;
import es.caib.ripea.ws.v1.bustia.BustiaV1Service;
import es.caib.ripea.ws.v1.bustia.RegistreAnnex;
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
	public void test() throws DatatypeConfigurationException, IOException {
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
        anotacio.setIdentificador("15/10/2015");
        
        File file = new File("c:/Feina/RIPEA/annexos/annex1.pdf");
        byte[] encodedContingut = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        RegistreAnnex annex1 = new RegistreAnnex();
        annex1.setTitol("annexproves1");
        annex1.setFitxerNom(file.getName());
        annex1.setFitxerTipusMime(FilenameUtils.getExtension(file.getName()));
        annex1.setFitxerContingutBase64(new String(encodedContingut));
        annex1.setFitxerTamany((int)(file.length()));
        annex1.setDataCaptura(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
        
        annex1.setOrigenCiutadaAdmin("0");
        annex1.setNtiTipusDocument("TD01");
        annex1.setSicresTipusDocument("01");
        
        
        File file2 = new File("c:/Feina/RIPEA/annexos/annex2.pdf");
        byte[] encodedContingut2 = Base64.encodeBase64(FileUtils.readFileToByteArray(file2));
        RegistreAnnex annex2 = new RegistreAnnex();
        annex2.setTitol("annexproves2");
        annex2.setFitxerNom(file2.getName());
        annex2.setFitxerTipusMime(FilenameUtils.getExtension(file2.getName()));
        annex2.setFitxerContingutBase64(new String(encodedContingut2));
        annex2.setFitxerTamany((int)(file.length()));
        annex2.setDataCaptura(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
        
        annex2.setOrigenCiutadaAdmin("1");
        annex2.setNtiTipusDocument("TD02");
        annex2.setSicresTipusDocument("02");
        
        anotacio.getAnnexos().add(annex1);
        anotacio.getAnnexos().add(annex2);
        
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
