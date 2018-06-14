/**
 * 
 */
package es.caib.ripea.plugin.caib.unitat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWsService;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.ripea.plugin.SistemaExternException;
import es.caib.ripea.plugin.unitat.UnitatOrganitzativa;
import es.caib.ripea.plugin.unitat.UnitatsOrganitzativesPlugin;
import es.caib.ripea.plugin.utils.PropertiesHelper;

/**
 * Implementació de proves del plugin d'unitats organitzatives.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class UnitatsOrganitzativesPluginDir3 implements UnitatsOrganitzativesPlugin {

	
	@Override
	public UnitatOrganitzativa findUnidad(
			String pareCodi, 
			Timestamp fechaActualizacion, 
			Timestamp fechaSincronizacion) throws MalformedURLException {

		UnidadTF unidad = getObtenerUnidadesService().obtenerUnidad(
				pareCodi, 
				fechaActualizacion, 
				fechaSincronizacion);
		if (unidad != null) {
			return toUnitatOrganitzativa(unidad);
		} else {
			return null;
		}

	}
	
	@Override
	public List<UnitatOrganitzativa> findAmbPare(
			String pareCodi,
			Timestamp fechaActualizacion,
			Timestamp fechaSincronizacion) throws SistemaExternException {
		try {
			List<UnitatOrganitzativa> unitatOrganitzativa = new ArrayList<UnitatOrganitzativa>();
			List<UnidadTF> arbol = getObtenerUnidadesService().obtenerArbolUnidades(
					pareCodi,
					fechaActualizacion,
					fechaSincronizacion);
			
			System.out.println("TF");
			for(UnidadTF unidadTF: arbol){
				System.out.println("codi: " + unidadTF.getCodigo() + ", parent: "+unidadTF.getCodUnidadSuperior()+", estat: "+unidadTF.getCodigoEstadoEntidad());
				
				unitatOrganitzativa.add(toUnitatOrganitzativa(unidadTF));
			}
			return unitatOrganitzativa;
		} catch (Exception ex) {
			throw new SistemaExternException(
					"No s'han pogut consultar les unitats organitzatives via WS (" +
					"pareCodi=" + pareCodi + ")",
					ex);
		}
	}
	


	public List<UnitatOrganitzativa> cercaUnitats(
			String codi, 
			String denominacio,
			Long nivellAdministracio, 
			Long comunitatAutonoma, 
			Boolean ambOficines, 
			Boolean esUnitatArrel,
			Long provincia, 
			String municipi) throws SistemaExternException {
		try {
			URL url = new URL(getServiceCercaUrl()
					+ "?codigo=" + codi
					+ "&denominacion=" + denominacio
					+ "&codNivelAdministracion=" + (nivellAdministracio != null ? nivellAdministracio : "-1")
					+ "&codComunidadAutonoma=" + (comunitatAutonoma != null ? comunitatAutonoma : "-1")
					+ "&conOficinas=" + (ambOficines != null && ambOficines ? "true" : "false")
					+ "&unidadRaiz=" + (esUnitatArrel != null && esUnitatArrel ? "true" : "false")
					+ "&provincia="+ (provincia != null ? provincia : "-1")
					+ "&localidad=" + (municipi != null ? municipi : "-1"));
			HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
			httpConnection.setRequestMethod("GET");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			List<UnitatOrganitzativa> unitats = mapper.readValue(
					httpConnection.getInputStream(), 
					TypeFactory.defaultInstance().constructCollectionType(
							List.class,  
							UnitatOrganitzativa.class));
			Collections.sort(unitats);
			return unitats;
		} catch (Exception ex) {
			throw new SistemaExternException(
					"No s'han pogut consultar les unitats organitzatives via REST (" +
					"codi=" + codi + ", " +
					"denominacio=" + denominacio + ", " +
					"nivellAdministracio=" + nivellAdministracio + ", " +
					"comunitatAutonoma=" + comunitatAutonoma + ", " +
					"ambOficines=" + ambOficines + ", " +
					"esUnitatArrel=" + esUnitatArrel + ", " +
					"provincia=" + provincia + ", " +
					"municipi=" + municipi + ")",
					ex);
		}
	}



	private Dir3CaibObtenerUnidadesWs getObtenerUnidadesService() throws MalformedURLException {
		Dir3CaibObtenerUnidadesWs client = null;
		URL url = new URL(getServiceUrl() + "?wsdl");
		Dir3CaibObtenerUnidadesWsService service = new Dir3CaibObtenerUnidadesWsService(
				url,
				new QName(
						"http://unidad.ws.dir3caib.caib.es/",
						"Dir3CaibObtenerUnidadesWsService"));
		client = service.getDir3CaibObtenerUnidadesWs();
		BindingProvider bp = (BindingProvider)client;
		bp.getRequestContext().put(
				BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				getServiceUrl());
		String username = getServiceUsername();
		if (username != null && !username.isEmpty()) {
			bp.getRequestContext().put(
					BindingProvider.USERNAME_PROPERTY,
					username);
			bp.getRequestContext().put(
					BindingProvider.PASSWORD_PROPERTY,
					getServicePassword());
		}
		if (isLogMissatgesActiu()) {
			@SuppressWarnings("rawtypes")
			List<Handler> handlerChain = new ArrayList<Handler>();
			handlerChain.add(new LogMessageHandler());
			bp.getBinding().setHandlerChain(handlerChain);
		}
		Integer connectTimeout = getServiceTimeout();
		if (connectTimeout != null) {
			bp.getRequestContext().put(
					"org.jboss.ws.timeout",
					connectTimeout);
		}
		return client;
	}

	private UnitatOrganitzativa toUnitatOrganitzativa(UnidadTF unidad) {
		UnitatOrganitzativa unitat = new UnitatOrganitzativa(
				unidad.getCodigo(),
				unidad.getDenominacion(),
				unidad.getCodigo(), // CifNif
				unidad.getFechaAltaOficial(),
				unidad.getCodigoEstadoEntidad(),
				unidad.getCodUnidadSuperior(),
				unidad.getCodUnidadRaiz(),
				unidad.getCodigoAmbPais(),
				unidad.getCodAmbComunidad(),
				unidad.getCodAmbProvincia(),
				unidad.getCodPostal(),
				unidad.getDescripcionLocalidad(),
				unidad.getCodigoTipoVia(), 
				unidad.getNombreVia(), 
				unidad.getNumVia(),
				unidad.getHistoricosUO());
		return unitat;
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
	
	private String getServiceUrl() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.unitats.organitzatives.dir3.service.url");
	}
	private String getServiceUsername() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.unitats.organitzatives.dir3.service.username");
	}
	private String getServicePassword() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.unitats.organitzatives.dir3.service.password");
	}
	private boolean isLogMissatgesActiu() {
		return PropertiesHelper.getProperties().getAsBoolean(
				"es.caib.ripea.plugin.unitats.organitzatives.dir3.service.log.actiu");
	}
	private Integer getServiceTimeout() {
		String key = "es.caib.ripea.plugin.unitats.organitzatives.dir3.service.timeout";
		if (PropertiesHelper.getProperties().getProperty(key) != null)
			return PropertiesHelper.getProperties().getAsInt(key);
		else
			return null;
	}
	private String getServiceCercaUrl() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.unitats.cerca.dir3.service.url");
	}

}
