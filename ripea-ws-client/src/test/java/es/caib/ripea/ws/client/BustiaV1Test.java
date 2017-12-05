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
import java.util.Random;

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
import es.caib.ripea.ws.v1.bustia.Firma;
import es.caib.ripea.ws.v1.bustia.RegistreAnnex;
import es.caib.ripea.ws.v1.bustia.RegistreAnotacio;

/**
 * Client de test per al servei bustia de RIPEA.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class BustiaV1Test {

	private static final String ENDPOINT_ADDRESS = "http://localhost:8080/ripea/ws/v1/bustia";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "admin15";



	@Test
	public void test() throws DatatypeConfigurationException, IOException {
		
		Random generator = new Random(); 
		int randomNumber = generator.nextInt(9999) + 1;
		
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
        anotacio.setNumero(String.valueOf(randomNumber));
        anotacio.setIdiomaCodi("1");
        anotacio.setIdiomaDescripcio("Català");
        anotacio.setIdentificador("15/10/2015");
        anotacio.setExpedientNumero(String.valueOf(randomNumber));
        
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
        annex1.setNtiElaboracioEstat("EE01");
        afegirFirmes(annex1);
        
        
        File file2 = new File("c:/Feina/RIPEA/annexos/annex2.pdf");
        byte[] encodedContingut2 = Base64.encodeBase64(FileUtils.readFileToByteArray(file2));
        RegistreAnnex annex2 = new RegistreAnnex();
        annex2.setTitol("annexproves2");
        annex2.setFitxerNom(file2.getName());
        annex2.setFitxerTipusMime(FilenameUtils.getExtension(file2.getName()));
        annex2.setFitxerContingutBase64(new String(encodedContingut2));
        annex2.setFitxerTamany((int)(file2.length()));
        annex2.setDataCaptura(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
        annex2.setOrigenCiutadaAdmin("1");
        annex2.setNtiTipusDocument("TD02");
        annex2.setSicresTipusDocument("02");
        annex2.setNtiElaboracioEstat("EE01");
        
        anotacio.getAnnexos().add(annex1);
        anotacio.getAnnexos().add(annex2);
        
        File file3 = new File("c:/Feina/RIPEA/annexos/justificant.pdf");
        byte[] encodedContingut3 = Base64.encodeBase64(FileUtils.readFileToByteArray(file3));
        RegistreAnnex justificant = new RegistreAnnex();
        justificant.setTitol("justificant");
        justificant.setFitxerNom(file3.getName());
        justificant.setFitxerTipusMime(FilenameUtils.getExtension(file3.getName()));
        justificant.setFitxerContingutBase64(new String(encodedContingut3));
        justificant.setFitxerTamany((int)(file3.length()));
        justificant.setDataCaptura(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
        justificant.setOrigenCiutadaAdmin("1");
        justificant.setNtiTipusDocument("TD02");
        justificant.setSicresTipusDocument("02");
        justificant.setNtiElaboracioEstat("EE01");
        
        anotacio.setJustificant(justificant);
        
		getBustiaServicePort().enviarAnotacioRegistreEntrada(
				"A04003003", // "entitatCodi",
				"A04019299", // "unitatAdministrativaCodi",
				anotacio);
	}

	private void afegirFirmes(RegistreAnnex annex) throws IOException {
		Firma firma = new Firma();
		File firmaFile = new File("c:/Feina/RIPEA/annexos/signatura.pdf");
        byte[] firmaContingut = Base64.encodeBase64(FileUtils.readFileToByteArray(firmaFile));
        firma.setTipus("TF05");
        firma.setPerfil("BES");
        firma.setContingut(firmaContingut);
        firma.setFitxerNom("signatura.pdf");
        firma.setTipusMime("application/pdf");
		
		Firma firma2 = new Firma();
		File firmaFile2 = new File("c:/Feina/RIPEA/annexos/signatura2.pdf");
        byte[] firmaContingut2 = Base64.encodeBase64(FileUtils.readFileToByteArray(firmaFile2));
		firma2.setTipus("TF05");
		firma2.setPerfil("BES");
		firma2.setContingut(firmaContingut2);
		firma2.setFitxerNom("signatura2.pdf");
		firma2.setTipusMime("application/pdf");
		
		annex.getFirmes().add(firma);
		annex.getFirmes().add(firma2);
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
