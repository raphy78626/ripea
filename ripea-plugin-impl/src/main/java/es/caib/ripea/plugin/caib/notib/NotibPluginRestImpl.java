/**
 * 
 */
package es.caib.ripea.plugin.caib.notib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import es.caib.notib.rest.client.NotibRestClient;
import es.caib.notib.rest.client.NotibRestException;
import es.caib.notib.ws.notificacio.NotificaEnviamentTipusEnumDto;
import es.caib.notib.ws.notificacio.NotificacioDestinatari;
import es.caib.notib.ws.notificacio.NotificacioEstatEnumDto;
import es.caib.notib.ws.notificacio.NotificacioSeuEstatEnumDto;
import es.caib.notib.ws.notificacio.Notificacio_Type;
import es.caib.notib.ws.notificacio.ServeiTipusEnum;
import es.caib.ripea.plugin.SistemaExternException;
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

	public Notificacio_Type notificacioFindPerReferencia(String referencia) throws JsonParseException, JsonMappingException, NotibRestException, IOException {
		Notificacio_Type notificacio = NotibRestClient.consulta(
				getBaseUrl(), 
				getUsername(), 
				getPassword(), 
				referencia);
		
		return notificacio;
	}
	
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
			boolean confirmarRecepcio) throws SistemaExternException {
		
		try {
			
			GregorianCalendar c = new GregorianCalendar();
			c.setTime(new Date(0));
			XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			
			/*instanciem una notificacio*/
			Notificacio_Type notificacio = new Notificacio_Type();
			notificacio.setCifEntitat(cifEntitat);
			notificacio.setEnviamentTipus(NotificaEnviamentTipusEnumDto.valueOf(enviamentTipus));
			notificacio.setEnviamentDataProgramada(date);
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
			notificacio.setSeuRegistreLlibre(seuRegistreLlibre);
			notificacio.setSeuRegistreOficina(seuRegistreOficina);
			notificacio.setSeuIdioma(seuIdioma);
			notificacio.setSeuAvisTitol(seuAvisTitol);
			notificacio.setSeuAvisText(seuAvisText);
			notificacio.setSeuAvisTextMobil(seuAvisTextMobil);
			notificacio.setSeuOficiTitol(seuOficiTitol);
			notificacio.setSeuOficiText(seuOficiText);
			notificacio.setEstat(NotificacioEstatEnumDto.PENDENT);
			notificacio.getDestinataris().addAll(getDestinataris(destinatari, representat));
			/////////////////////////////
			
			
			/**Usam client api notib**/
			List<String> referencies = null;
			referencies = NotibRestClient.alta(
					getBaseUrl(), 
					getUsername(), 
					getPassword(), 
					notificacio);
			
			/*************************/
			
			if (referencies != null && referencies.size() > 0) {
				NotibNotificacioResultat resultat = new NotibNotificacioResultat();
				resultat.setReferenciaData(new Date());
				resultat.setReferencia(referencies.get(0));
				return resultat;
			} else {
				throw new SistemaExternException();
			}
		} catch (Exception ex) {
			throw new SistemaExternException("",ex);
		}
	}

	private List<NotificacioDestinatari>getDestinataris(NotibPersona destinatari, NotibPersona representat) {
		List<NotificacioDestinatari> destinataris = new ArrayList<NotificacioDestinatari>();
		
		NotificacioDestinatari destinatariFinal = new NotificacioDestinatari();
		
		destinatariFinal.setDestinatariNif(destinatari.getNif());
		destinatariFinal.setDehNif(destinatari.getNif());
		destinatariFinal.setDestinatariNom(destinatari.getNom());
		destinatariFinal.setDestinatariLlinatge1(destinatari.getLlinatge1());
		destinatariFinal.setDestinatariLlinatge2(destinatari.getLlinatge2());
		destinatariFinal.setDomiciliPaisCodiIso(destinatari.getPaisCodi());
		destinatariFinal.setDomiciliPaisNom(destinatari.getPaisNom());
		destinatariFinal.setDomiciliProvinciaCodi(destinatari.getProvinciaCodi());
		destinatariFinal.setDomiciliProvinciaNom(destinatari.getProvinciaNom());
		destinatariFinal.setDomiciliMunicipiCodiIne(destinatari.getMunicipiCodi());
		destinatariFinal.setDomiciliMunicipiNom(destinatari.getMunicipiNom());
		destinatariFinal.setDestinatariEmail(destinatari.getEmail());
		destinatariFinal.setServeiTipus(ServeiTipusEnum.NORMAL);
		destinatariFinal.setSeuEstat(NotificacioSeuEstatEnumDto.PENDENT);
//		destinatariFinal.setDomiciliTipus(DomiciliTipusEnum.CONCRET);
//		destinatariFinal.setDomiciliConcretTipus(DomiciliConcretTipusEnum.NACIONAL);
//		destinatariFinal.setDomiciliNumeracioTipus(DomiciliNumeracioTipusEnum.NUMERO);
		
		if (representat != null) {
			destinatariFinal.setTitularNif(representat.getNif());
			destinatariFinal.setTitularNom(representat.getNom());
			destinatariFinal.setTitularLlinatge1(representat.getLlinatge1());
			destinatariFinal.setTitularLlinatge2(representat.getLlinatge2());
			destinatariFinal.setTitularEmail(representat.getEmail());
		}
		
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
