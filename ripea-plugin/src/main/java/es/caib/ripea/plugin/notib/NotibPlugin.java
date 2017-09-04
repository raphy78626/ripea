package es.caib.ripea.plugin.notib;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import es.caib.notib.rest.client.NotibRestException;
import es.caib.notib.ws.notificacio.Notificacio_Type;
import es.caib.ripea.plugin.SistemaExternException;

/**
 * Plugin per a la comunicació amb el ciutadà.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface NotibPlugin {

	/**
	 * Envia una notificació telemàtica mitjançant l'aplicació NOTIB.
	 * 
	 * @param expedientIdentificador
	 * @param unitatAdministrativa
	 * @param registreOficinaCodi
	 * @param registreOrganCodi
	 * @param destinatari
	 * @param representat
	 * @param assumpteTipus
	 * @param oficiTitol
	 * @param oficiText
	 * @param avisTitol
	 * @param avisText
	 * @param avisTextSms
	 * @param confirmarRecepcio
	 * @param annexos
	 * @return les dades de la notificació creada
	 * @throws SistemaExternException
	 *             Si hi ha hagut algun problema per a crear la notificació
	 */
	public NotibNotificacioResultat notificacioCrear(
			String cifEntitat,
			String enviamentTipus,
			String concepte,
			String procedimentCodiSia,
			String documentArxiuNom,
			byte[] documentContingut,
			NotibPersona destinatari,
			NotibPersona representat,
			String seuExpedientSerieDocumental,
			String seuExpedientUnitatOrganitzativa,
			String seuExpedientIdentificadorEni,
			String seuExpedientTitol,
			String seuRegistreOficina,
			String seuRegistreLlibre,
			String seuIdioma,
			String seuAvisTitol,
			String seuAvisText,
			String seuAvisTextMobil,
			String seuOficiTitol,
			String seuOficiText,
			boolean confirmarRecepcio) throws SistemaExternException;


	public Notificacio_Type notificacioFindPerReferencia(String referencia) throws JsonParseException, JsonMappingException, NotibRestException, IOException;
}
