/**
 * 
 */
package es.caib.ripea.ws.client;

import java.net.MalformedURLException;

import javax.xml.namespace.QName;

import es.caib.ripea.ws.v1.bustia.BustiaV1;

/**
 * Utilitat per a instanciar clients per al servei d'enviament
 * de contingut a bústies.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class BustiaV1WsClientFactory {

	public static BustiaV1 getWsClient(
			String url,
			String userName,
			String password) throws MalformedURLException {
		return new WsClientHelper<BustiaV1>().generarClientWs(
				url,
				new QName(
						"http://www.caib.es/ripea/ws/v1/bustia",
						"BustiaV1Service"),
				userName,
				password,
				BustiaV1.class);
	}

}
