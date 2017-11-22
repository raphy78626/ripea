/**
 * 
 */
package es.caib.ripea.plugin.caib.notificacio;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import es.caib.notib.client.NotificacioRestClient;
import es.caib.notib.client.NotificacioRestClientFactory;
import es.caib.notib.ws.notificacio.Certificacio;
import es.caib.notib.ws.notificacio.Document;
import es.caib.notib.ws.notificacio.EntregaDeh;
import es.caib.notib.ws.notificacio.EntregaPostal;
import es.caib.notib.ws.notificacio.EntregaPostalTipusEnum;
import es.caib.notib.ws.notificacio.EntregaPostalViaTipusEnum;
import es.caib.notib.ws.notificacio.Enviament;
import es.caib.notib.ws.notificacio.EnviamentTipusEnum;
import es.caib.notib.ws.notificacio.InformacioEnviament;
import es.caib.notib.ws.notificacio.Notificacio;
import es.caib.notib.ws.notificacio.PagadorCie;
import es.caib.notib.ws.notificacio.PagadorPostal;
import es.caib.notib.ws.notificacio.ParametresSeu;
import es.caib.notib.ws.notificacio.Persona;
import es.caib.notib.ws.notificacio.ServeiTipusEnum;
import es.caib.ripea.plugin.SistemaExternException;
import es.caib.ripea.plugin.caib.helper.ConversioHelper;
import es.caib.ripea.plugin.notificacio.NotificacioCertificacio;
import es.caib.ripea.plugin.notificacio.NotificacioDocument;
import es.caib.ripea.plugin.notificacio.NotificacioEntregaDeh;
import es.caib.ripea.plugin.notificacio.NotificacioEntregaPostal;
import es.caib.ripea.plugin.notificacio.NotificacioEntregaPostalTipusEnum;
import es.caib.ripea.plugin.notificacio.NotificacioEntregaPostalViaTipusEnum;
import es.caib.ripea.plugin.notificacio.NotificacioEnviament;
import es.caib.ripea.plugin.notificacio.NotificacioEnviamentEstatEnum;
import es.caib.ripea.plugin.notificacio.NotificacioEnviamentTipusEnum;
import es.caib.ripea.plugin.notificacio.NotificacioInformacioEnviament;
import es.caib.ripea.plugin.notificacio.NotificacioPagadorCie;
import es.caib.ripea.plugin.notificacio.NotificacioPagadorPostal;
import es.caib.ripea.plugin.notificacio.NotificacioParametresSeu;
import es.caib.ripea.plugin.notificacio.NotificacioPersona;
import es.caib.ripea.plugin.notificacio.NotificacioPlugin;
import es.caib.ripea.plugin.utils.PropertiesHelper;

/**
 * Implementació de del plugin de comunicació amb el ciutadà
 * emprant SISTRA.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class NotificacioPluginNotib implements NotificacioPlugin {
	
	private final static String KEY_BASE_URL = "es.caib.ripea.plugin.ciutada.notib.base.url";
	private final static String KEY_USERNAME = "es.caib.ripea.plugin.ciutada.notib.username";
	private final static String KEY_PASSWORD = "es.caib.ripea.plugin.ciutada.notib.password";
	
	private String getBaseUrl() throws SistemaExternException {
		String base_url = PropertiesHelper.getProperties().getProperty(KEY_BASE_URL);
		if(base_url != null && !base_url.equals(""))
			throw new SistemaExternException("No s'ha configurat la propietat: " + KEY_BASE_URL);
		return base_url;
	}
	private String getUsername() throws SistemaExternException {
		String username = PropertiesHelper.getProperties().getProperty(KEY_USERNAME);
		if(username != null && !username.equals(""))
			throw new SistemaExternException("No s'ha configurat la propietat: " + KEY_USERNAME);
		return username;
	}
	private String getPassword() throws SistemaExternException {
		String password = PropertiesHelper.getProperties().getProperty(KEY_PASSWORD);
		if(password != null && !password.equals(""))
			throw new SistemaExternException("No s'ha configurat la propietat: " + KEY_PASSWORD);
		return password;
	}
	
	private NotificacioRestClient notificacioRestClient;
	
	private NotificacioRestClient getNotificacioRestClient() throws SistemaExternException {
		if(notificacioRestClient == null) {
			notificacioRestClient = NotificacioRestClientFactory.getRestClient(
					getBaseUrl(),
					getUsername(),
					getPassword());
		}
		return notificacioRestClient;
	}
	
	
	@Override
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
		    Integer retard) throws SistemaExternException {
		Notificacio notificacio = new Notificacio();
		notificacio.setCaducitat(caducitat);
		notificacio.setConcepte(concepte);
		notificacio.setDescripcio(descripcio);
		notificacio.setDocument(
				ConversioHelper.convertir(document, Document.class));
		notificacio.setEmisorDir3Codi(emisorDir3Codi);
		notificacio.setEnviamentDataProgramada(enviamentDataProgramada);
		notificacio.setEnviamentTipus(
				EnviamentTipusEnum.fromValue(enviamentTipus.name()));
		
		for(NotificacioEnviament e : enviaments) {
			Enviament enviament = new Enviament();
			Persona[] persones = new Persona[e.getDestinataris().length];
			for(NotificacioPersona persona : e.getDestinataris())
				enviament.getDestinataris().add(
						ConversioHelper.convertir(persona, Persona.class));
			EntregaDeh entregaDeh = new EntregaDeh();
			entregaDeh.setObligat(enviament.getEntregaDeh().isObligat());
			entregaDeh.setProcedimentCodi(enviament.getEntregaDeh().getProcedimentCodi());
			enviament.setEntregaDeh(
					ConversioHelper.convertir(e.getEntregaDeh(), EntregaDeh.class));
			enviament.setEntregaPostal(
					ConversioHelper.convertir(e.getEntregaPostal(), EntregaPostal.class));
			enviament.getEntregaPostal().setTipus(
					EntregaPostalTipusEnum.fromValue(e.getEntregaPostal().getTipus().name()));
			enviament.getEntregaPostal().setViaTipus(
					EntregaPostalViaTipusEnum.fromValue(e.getEntregaPostal().getViaTipus().name()));
			enviament.setReferencia(e.getReferencia());
			enviament.setServeiTipus(
					ServeiTipusEnum.fromValue(e.getServeiTipus().name()));
			enviament.setTitular(
					ConversioHelper.convertir(e.getTitular(), Persona.class));
	        notificacio.getEnviaments().add(enviament);
		}
		
		notificacio.setPagadorCie(
				ConversioHelper.convertir(pagadorCie, PagadorCie.class));
		notificacio.setPagadorPostal(
				ConversioHelper.convertir(pagadorPostal, PagadorPostal.class));
		notificacio.setParametresSeu(
				ConversioHelper.convertir(parametresSeu, ParametresSeu.class));
		notificacio.setProcedimentCodi(procedimentCodi);
		notificacio.setRetard(retard);
		
		List<String> results = null;
		try {
			results = getNotificacioRestClient().alta(notificacio);
		} catch (Exception e) {
			throw new SistemaExternException("Error al cridar a la funció REST de alta del Notib", e);
		}
		return results;
	}
	
	@Override
	public NotificacioInformacioEnviament notificacioConsultaEstat(
			String registreNumero) throws SistemaExternException {
		
		InformacioEnviament ie = null;
		try {
			ie = getNotificacioRestClient().consulta(registreNumero);
		} catch (Exception e) {
			throw new SistemaExternException("Error al cridar a la funció REST de consulta d'estad del Notib", e);
		}
		
		NotificacioEntregaPostal entregaPostal = ConversioHelper.convertir(
				ie.getEntregaPostal(),
				NotificacioEntregaPostal.class);
		entregaPostal.setTipus(
				NotificacioEntregaPostalTipusEnum.valueOf(
						ie.getEntregaPostal().getTipus().name()));
		entregaPostal.setViaTipus(
				NotificacioEntregaPostalViaTipusEnum.valueOf(
						ie.getEntregaPostal().getViaTipus().name()));
		
		return new NotificacioInformacioEnviament(
				ConversioHelper.convertir(ie.getCertificacio(), NotificacioCertificacio.class),
				ie.getConcepte(),
				ie.getDataCaducitat(),
				ie.getDataCreacio(),
				ie.getDataPostaDisposicio(),
				ie.getDescripcio(),
				ie.getDestiDir3Codi(),
				ie.getDestiDir3Descripcio(),
				ConversioHelper.convertirList(ie.getDestinataris(), NotificacioPersona.class),
				ie.getEmisorArrelDir3Codi(),
				ie.getEmisorArrelDir3Descripcio(),
				ie.getEmisorDir3Codi(),
				ie.getEmisorDir3Descripcio(),
				ConversioHelper.convertir(ie.getEntregaDeh(), NotificacioEntregaDeh.class),
				entregaPostal,
				NotificacioEnviamentTipusEnum.valueOf(ie.getEnviamentTipus().name()),
				NotificacioEnviamentEstatEnum.valueOf(ie.getEstat().name()),
				ie.getEstatData(),
				ie.getIdentificador(),
				ie.isNotificaError(),
				ie.getNotificaErrorData(),
				ie.getNotificaErrorDescripcio(),
				NotificacioEnviamentEstatEnum.valueOf(ie.getNotificaEstat().name()),
				ie.getNotificaEstatData(),
				ie.getProcedimentCodi(),
				ie.getProcedimentDescripcio(),
				ie.getReferencia(),
				ie.getRetard(),
				ie.isSeuError(),
				ie.getSeuErrorData(),
				ie.getSeuErrorDescripcio(),
				NotificacioEnviamentEstatEnum.valueOf(ie.getSeuEstat().name()),
				ie.getSeuEstatData(),
				ConversioHelper.convertir(ie.getTitular(), NotificacioPersona.class));
	}
	
}
