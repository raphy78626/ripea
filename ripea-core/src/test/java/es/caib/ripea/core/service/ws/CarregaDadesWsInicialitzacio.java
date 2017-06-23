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
public class CarregaDadesWsInicialitzacio extends CarregaDadesWsBase {
	
	public static void main(String[] args) {
		try {
			CarregaDadesWsInicialitzacio carrega = new CarregaDadesWsInicialitzacio();
			carrega.carregaProperties();
			carrega.inicialitza();
//			System.out.println(">>> Resposta: " + resposta);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void inicialitza() throws Exception {
		
		RipeaCarregaTestWsService clientCarregaDades = getCarregaDadesService();
		
		
		// Cream una entitat per a les proves de rendiment
		Long entitatId = clientCarregaDades.crearEntitat(
				properties.getProperty("es.caib.ripea.performance.entitat.codi"), 
				properties.getProperty("es.caib.ripea.performance.entitat.nom"), 
				"Entitat per a executar proves de rendiment", 
				"D28953958", 
				"A04003003");
		
		// Assignam permisos al usuaris per a executar les accions a l'entitat
		clientCarregaDades.assignaPermisEntitat(
				entitatId, 
				properties.getProperty("es.caib.ripea.performance.usuari.codi"), 
				true, 
				true, 
				true, 
				true, 
				true, 
				true);
		
//		clientCarregaDades.assignaPermisEntitat(
//				entitatId, 
//				properties.getProperty("es.caib.ripea.performance.administrador.codi"), 
//				true, 
//				true, 
//				true, 
//				true, 
//				true, 
//				true);
		
		// Cream un metaExpedient
		Long metaExpadientId = clientCarregaDades.crearMetaExpedient(
				entitatId, 
				properties.getProperty("es.caib.ripea.performance.metaexpedient.codi"), 
				properties.getProperty("es.caib.ripea.performance.metaexpedient.nom"), 
				"Tipus d'expedient per a executar proves de rendiment",
				"00000", 
				"00000", 
				null, 
				false, 
				null, 
				null, 
				null, 
				null, 
				null, 
				null, 
				null, 
				null);

		// Cream la metadada1 de l'expedient
		//Long expMetadada1 = 
		clientCarregaDades.crearExpedientMetadata(
				entitatId, 
				metaExpadientId, 
				properties.getProperty("es.caib.ripea.performance.metadadaexpedient.1.codi"), 
				properties.getProperty("es.caib.ripea.performance.metadadaexpedient.1.nom"), 
				"Metadada 1 del tipus d'expedient per a executar proves de rendiment", 
				properties.getProperty("es.caib.ripea.performance.metadadaexpedient.1.tipus"), 
				false, 
				false, 
				"M_1", 
				false, 
				"M_1", 
				false);
		
		// Cream la metadada2 de l'expedient
		//Long expMetadada2 = 
		clientCarregaDades.crearExpedientMetadata(
				entitatId, 
				metaExpadientId, 
				properties.getProperty("es.caib.ripea.performance.metadadaexpedient.2.codi"), 
				properties.getProperty("es.caib.ripea.performance.metadadaexpedient.2.nom"),
				"Metadada 2 del tipus d'expedient per a executar proves de rendiment", 
				properties.getProperty("es.caib.ripea.performance.metadadaexpedient.2.tipus"), 
				false, 
				false, 
				"M_1", 
				false, 
				"M_1", 
				false);

		// Cream un metaDocument
		Long metaDocumentId = clientCarregaDades.crearDocumentTipus(
				entitatId, 
				properties.getProperty("es.caib.ripea.performance.metadocument.codi"), 
				properties.getProperty("es.caib.ripea.performance.metadocument.nom"), 
				"Tipus de document per a executar proves de rendiment",
				false, 
				"M_1", 
				false, 
				true, 
				"1", 
				null, 
				new String[] {properties.getProperty("es.caib.ripea.performance.usuari.codi")}, 
				"SERIE", 
				null, 
				true, 
				null, 
				null, 
				"application/octet-stream", 
				null);
		
		// Cream la metadada1 del document
		//Long docMetadada1 = 
		clientCarregaDades.crearDocumentMetadata(
				entitatId, 
				metaDocumentId, 
				properties.getProperty("es.caib.ripea.performance.metadadadocument.1.codi"), 
				properties.getProperty("es.caib.ripea.performance.metadadadocument.1.nom"), 
				"Metadada 1 del tipus de document per a executar proves de rendiment", 
				properties.getProperty("es.caib.ripea.performance.metadadadocument.1.tipus"), 
				false, 
				false, 
				"M_1", 
				false, 
				"M_1", 
				false);
		
		// Cream la metadada2 del document
		//Long docMetadada2 = 
		clientCarregaDades.crearDocumentMetadata(
				entitatId, 
				metaDocumentId, 
				properties.getProperty("es.caib.ripea.performance.metadadadocument.2.codi"), 
				properties.getProperty("es.caib.ripea.performance.metadadadocument.2.nom"), 
				"Metadada 2 del tipus de document per a executar proves de rendiment", 
				properties.getProperty("es.caib.ripea.performance.metadadadocument.2.tipus"), 
				false, 
				false, 
				"M_1", 
				false, 
				"M_1", 
				false);
		
		// Cream un arxiu
		//Long arxiuId = 
		clientCarregaDades.crearArxiu(
				entitatId, 
				properties.getProperty("es.caib.ripea.performance.arxiu.nom"), 
				properties.getProperty("es.caib.ripea.performance.arxiu.unitat.codi"));
		
	}

}
