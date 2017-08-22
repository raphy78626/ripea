/**
 * 
 */
package es.caib.ripea.plugin.caib.notib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import es.caib.notib.ws.notificacio.NotificaEnviamentTipusEnumDto;
import es.caib.notib.ws.notificacio.NotificacioDestinatari;
import es.caib.notib.ws.notificacio.NotificacioEntity;
import es.caib.notib.ws.notificacio.NotificacioEstatEnumDto;
import es.caib.ripea.plugin.SistemaExternException;
import es.caib.ripea.plugin.notib.NotibDocument;
import es.caib.ripea.plugin.notib.NotibNotificacioResultat;
import es.caib.ripea.plugin.notib.NotibPersona;
import es.caib.ripea.plugin.notib.NotibPlugin;
import es.caib.ripea.plugin.utils.PropertiesHelper;

/**
 * Implementació de del plugin de comunicació amb el notib
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class NotibPluginRestImpl implements NotibPlugin {

	@Override
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
			boolean confirmarRecepcio,
			List<NotibDocument> annexos) throws SistemaExternException {
		
		try {
			
			/*instanciem una notificacio*/
			NotificacioEntity notificacio = new NotificacioEntity();
			notificacio.setCifEntitat(cifEntitat);
			notificacio.setEnviamentTipus(NotificaEnviamentTipusEnumDto.valueOf(enviamentTipus));
			notificacio.setEnviamentDataProgramada(new Date());
			notificacio.setConcepte(concepte);
			
			notificacio.setPagadorCorreusCodiDir3(null);
			notificacio.setPagadorCorreusContracteNum(null);
			notificacio.setPagadorCorreusCodiClientFacturacio(null);
			notificacio.setPagadorCorreusDataVigencia(null);
			notificacio.setPagadorCieCodiDir3(null);
			notificacio.setPagadorCieDataVigencia(null);
			
			notificacio.setProcedimentCodiSia(procedimentCodiSia);
			notificacio.setProcedimentDescripcioSia(null);
			notificacio.setDocumentArxiuNom(documentArxiuNom);
			notificacio.setDocumentContingutBase64(Base64.encodeBase64String(documentContingut));
			notificacio.setDocumentSha1(null);
			notificacio.setDocumentNormalitzat(true);
			notificacio.setDocumentGenerarCsv(true);
			notificacio.setSeuExpedientSerieDocumental(seuExpedientSerieDocumental);
			notificacio.setSeuExpedientUnitatOrganitzativa(seuExpedientUnitatOrganitzativa);
			notificacio.setSeuExpedientIdentificadorEni(seuExpedientIdentificadorEni);
			notificacio.setSeuExpedientTitol(seuExpedientTitol);
			notificacio.setSeuRegistreOficina(seuRegistreOficina);
			notificacio.setSeuRegistreLlibre(seuRegistreLlibre);
			notificacio.setSeuIdioma(seuIdioma);
			notificacio.setSeuAvisTitol(seuAvisTitol);
			notificacio.setSeuAvisText(seuAvisText);
			notificacio.setSeuAvisTextMobil(seuAvisTextMobil);
			notificacio.setSeuOficiTitol(seuOficiTitol);
			notificacio.setSeuOficiText(seuOficiText);
			notificacio.setEstat(NotificacioEstatEnumDto.ENVIADA_NOTIFICA);
			notificacio.setDestinataris(getDestinataris(destinatari, representat));
			/////////////////////////////
			
			Client jerseyClient = new Client();
			ObjectMapper mapper  = new ObjectMapper();
			
			String user = getUsername();
			String pass = getPassword();
			String urlAmbMetode = getBaseUrl() + "/api/services/altaEnviament";
			String body = mapper.writeValueAsString(notificacio);
			
			jerseyClient.addFilter( new HTTPBasicAuthFilter(user, pass) );
			ClientResponse response = jerseyClient.
					resource(urlAmbMetode).
					type("application/json").
					post(ClientResponse.class, body);
			
			System.out.println( response.getStatus() );
			System.out.println( response.getEntity(String.class) );
			
			NotibNotificacioResultat resultat = new NotibNotificacioResultat();
			return resultat;
		} catch (Exception ex) {
			throw new SistemaExternException("",ex);
		}
	}

	private List<NotificacioDestinatari>getDestinataris(NotibPersona destinatari, NotibPersona representat) {
		List<NotificacioDestinatari> destinataris = new ArrayList<NotificacioDestinatari>();
		
		NotificacioDestinatari destinatariFinal = new NotificacioDestinatari();
		
		destinatariFinal.setDestinatariNif(destinatari.getNif());
		destinatariFinal.setDestinatariNom(destinatari.getNom());
		destinatariFinal.setDestinatariLlinatges(destinatari.getLlinatges());
		destinatariFinal.setDomiciliPaisCodiIso(destinatari.getPaisCodi());
		destinatariFinal.setDomiciliPaisNom(destinatari.getPaisNom());
		destinatariFinal.setDomiciliProvinciaCodi(destinatari.getProvinciaCodi());
		destinatariFinal.setDomiciliProvinciaNom(destinatari.getProvinciaNom());
		destinatariFinal.setDomiciliMunicipiCodiIne(destinatari.getMunicipiCodi());
		destinatariFinal.setDomiciliMunicipiNom(destinatari.getMunicipiNom());
		
		destinatariFinal.setTitularNif(representat.getNif());
		destinatariFinal.setTitularNom(representat.getNom());
		destinatariFinal.setTitularLlinatges(representat.getLlinatges());
		
		destinataris.add(destinatariFinal);
		
		return destinataris;
	}


	private String getBaseUrl() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.notib.base.url");
	}
	private String getUsername() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.notib.username");
	}
	private String getPassword() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.notib.password");
	}
	private boolean isLogMissatgesActiu() {
		return PropertiesHelper.getProperties().getAsBoolean(
				"es.caib.ripea.plugin.notib.log.actiu");
	}
	private String getAssumpteTipus() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.notib.assumpte.tipus");
	}

	private class LogMessageHandler implements SOAPHandler<SOAPMessageContext> {
		public boolean handleMessage(SOAPMessageContext messageContext) {
			log(messageContext);
			return true;
		}
		public Set<QName> getHeaders() {
			return Collections.emptySet();
		}
		public boolean handleFault(SOAPMessageContext messageContext) {
			log(messageContext);
			return true;
		}
		public void close(MessageContext context) {
		}
		private void log(SOAPMessageContext messageContext) {
			SOAPMessage msg = messageContext.getMessage();
			try {
				Boolean outboundProperty = (Boolean)messageContext.get(
						MessageContext.MESSAGE_OUTBOUND_PROPERTY);
				if (outboundProperty)
					System.out.print("Missatge SOAP petició: ");
				else
					System.out.print("Missatge SOAP resposta: ");
				msg.writeTo(System.out);
				System.out.println();
			} catch (SOAPException ex) {
				Logger.getLogger(LogMessageHandler.class.getName()).log(
						Level.SEVERE,
						null,
						ex);
			} catch (IOException ex) {
				Logger.getLogger(LogMessageHandler.class.getName()).log(
						Level.SEVERE,
						null,
						ex);
			}
		}
	}

}
