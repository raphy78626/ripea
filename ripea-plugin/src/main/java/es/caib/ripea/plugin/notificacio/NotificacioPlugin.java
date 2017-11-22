package es.caib.ripea.plugin.notificacio;

import java.util.Calendar;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import es.caib.ripea.plugin.SistemaExternException;

/**
 * Plugin per a la comunicació amb el ciutadà.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface NotificacioPlugin {

	/**
	 * Envia una notificació telemàtica al Notib.
	 * 
	 * @param caducitat
	 * @param concepte
	 * @param descripcio
	 * @param document
	 * @param emisorDir3Codi
	 * @param enviamentDataProgramada
	 * @param enviamentTipus
	 * @param enviament
	 * @param pagadorPostal
	 * @param parametresSeu
	 * @return el número de registre de la notificació creada
	 * @throws SistemaExternException
	 *             Si hi ha hagut algun problema per a crear la notificació
	 */
	public List<String> notificacioAlta(
			XMLGregorianCalendar caducitat,
			String concepte,
			String descripcio,
			NotificacioDocument document,
			String emisorDir3Codi,
			XMLGregorianCalendar enviamentDataProgramada,
			NotificacioEnviamentTipusEnum enviamentTipus,
			NotificacioEnviament[] enviaments,
			NotificacioPagadorCie pagadorCie,
			NotificacioPagadorPostal pagadorPostal,
			NotificacioParametresSeu parametresSeu,
			String procedimentCodi,
		    Integer retard
			) throws SistemaExternException;

	/**
	 * Obté l'estat d'una notificació telemàtica enviada a Notib.
	 * 
	 * @param registreNumero
	 * @return la informació sobre l'estat de la notificació
	 * @throws SistemaExternException
	 *             Si hi ha hagut algun problema per a obtenir l'estat de la notificació
	 */
	public NotificacioInformacioEnviament notificacioConsultaEstat(
			String registreNumero) throws SistemaExternException;

}
