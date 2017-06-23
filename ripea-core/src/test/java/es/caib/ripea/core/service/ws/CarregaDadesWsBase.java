/**
 * 
 */
package es.caib.ripea.core.service.ws;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.namespace.QName;

import es.caib.ripea.core.api.service.ws.RipeaCarregaTestWsService;
import es.caib.ripea.core.helper.WsClientHelper;

/**
 * Classe pel servei web de carrega de dades per proves de rendiment.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class CarregaDadesWsBase {

	protected Properties properties;
	

	protected void carregaProperties() {
		// Llegim el fitxer de properties
		properties = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("src/test/resources/es/caib/ripea/core/test.properties");

			// load a properties file
			properties.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected RipeaCarregaTestWsService getCarregaDadesService() throws Exception {
		return new WsClientHelper<RipeaCarregaTestWsService>().generarClientWs(
				getClass().getResource("/es/caib/ripea/core/service/ws/carregatest/ripeaCarregaTest.wsdl"),
				properties.getProperty("es.caib.ripea.performance.endpoint.address"),
				new QName(
						"http://www.caib.es/ripea/ws/ripeacarregatest",
						"RipeaCarregaTest"),
				properties.getProperty("es.caib.ripea.performance.ws.user"),
				properties.getProperty("es.caib.ripea.performance.ws.password"),
				null,
				RipeaCarregaTestWsService.class);
	}

}
