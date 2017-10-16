/**
 * 
 */
package es.caib.ripea.core.service.ws;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.caib.ripea.core.api.registre.RegistreAnotacio;
import es.caib.ripea.core.api.registre.RegistreInteressat;

/**
 * Classe de proves pel registre versió 1.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class BustiaWsV1Test extends BustiaBaseTest {


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
		anotacio.setNumero(String.valueOf(ara.getTime()));
		anotacio.setData(ara);
		anotacio.setIdentificador(numeroIdentificador);
		anotacio.setExtracte("Extracte " + numeroIdentificador);
		anotacio.setOficinaCodi("0");
		anotacio.setAssumpteTipusCodi("0");
		anotacio.setIdiomaCodi("CA");
		anotacio.setEntitatCodi("?");
		anotacio.setLlibreCodi("?");
		
		anotacio.setInteressats(new ArrayList<RegistreInteressat>());
		// Afegeix interessats
		for (int i=0; i<2; i++)
			anotacio.getInteressats().add(getInteressat(i));
		
		// Envia l'anotació de registre.
		getBustiaService().enviarAnotacioRegistreEntrada(
				"A04019299",
				"A04019299",
				anotacio);
	}
}
