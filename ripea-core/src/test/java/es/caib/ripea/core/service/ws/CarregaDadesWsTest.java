/**
 * 
 */
package es.caib.ripea.core.service.ws;

import es.caib.ripea.core.api.service.ws.RipeaCarregaTestWsService;

/**
 * Classe de proves pel servei web de callback de portafirmes MCGDws.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class CarregaDadesWsTest extends CarregaDadesWsBase {

	public static void main(String[] args) {
		try {
			new CarregaDadesWsTest().carrega();
//			System.out.println(">>> Resposta: " + resposta);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void carrega() throws Exception {
		
		RipeaCarregaTestWsService clientCarregaDades = getCarregaDadesService();
		
		for (int i = 0; i < 100; i++) {
//			clientCarregaDades.crearExpedient(
//					entitatId, 
//					pareId, 
//					metaExpedientCodi, 
//					arxiuId, 
//					any, 
//					nom, 
//					expedientMetadata1Codi, 
//					expedientMetadada1Valor, 
//					expedientMetadata2Codi, 
//					expedientMetadada2Valor, 
//					documentTipusCodi, 
//					documentTipus, 
//					fitxerNomOriginal, 
//					fitxerContentType, 
//					fitxerContingut, 
//					documentTitol, 
//					documentData, 
//					documentUbicacio, 
//					documentNtiOrgan, 
//					documentNtiOrigen, 
//					documentNtiEstat, 
//					documentNtiTipusDoc, 
//					documentMetadada1Codi, 
//					documentMetadada1Valor, 
//					documentMetadada2Codi, 
//					documentMetadada2Valor);
		}
		
	}

}
