/**
 * 
 */
package es.caib.ripea.plugin.caib.unitat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWsService;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.ripea.plugin.SistemaExternException;
import es.caib.ripea.plugin.unitat.Localitat;
import es.caib.ripea.plugin.unitat.ProvinciaRw3;
import es.caib.ripea.plugin.unitat.UnitatOrganitzativa;
import es.caib.ripea.plugin.unitat.UnitatOrganitzativaD3;
import es.caib.ripea.plugin.unitat.UnitatsOrganitzativesPlugin;
import es.caib.ripea.plugin.utils.PropertiesHelper;

/**
 * Implementació de proves del plugin d'unitats organitzatives.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class UnitatsOrganitzativesPluginDir3 implements UnitatsOrganitzativesPlugin {

	@Override
	public List<UnitatOrganitzativa> findAmbPare(String pareCodi) throws SistemaExternException {
		try {
			List<UnitatOrganitzativa> unitats = new ArrayList<UnitatOrganitzativa>();
			UnidadTF unidadPare = getObtenerUnidadesService().obtenerUnidad(
					pareCodi,
					null,
					null);
			List<UnidadTF> unidades = getObtenerUnidadesService().obtenerArbolUnidades(
					pareCodi,
					null,
					null);//df.format(new Date()));
			unidades.add(0, unidadPare);
			if (unidades != null) {
				for (UnidadTF unidad: unidades) {
					if ("V".equalsIgnoreCase(unidad.getCodigoEstadoEntidad())) {
						unitats.add(toUnitatOrganitzativa(unidad));
						/*unitats.add(new UnitatOrganitzativa(
								unidad.getCodigo(),
								unidad.getDenominacion(),
								unidad.getCodigo(), // CifNif
								unidad.getFechaAltaOficial(),
								unidad.getCodigoEstadoEntidad(),
								unidad.getCodUnidadSuperior(),
								unidad.getCodUnidadRaiz()));*/
					}
				}
			}
			return unitats;
		} catch (Exception ex) {
			throw new SistemaExternException(
					"No s'han pogut consultar les unitats organitzatives via WS (" +
					"pareCodi=" + pareCodi + ")",
					ex);
		}
	}
	
	@Override
	public UnitatOrganitzativa findAmbCodi(String codi) throws SistemaExternException {
		try {
			UnitatOrganitzativa unitat = null;
			UnidadTF unidad = getObtenerUnidadesService().obtenerUnidad(
					codi,
					null,
					null);
			if (unidad != null && "V".equalsIgnoreCase(unidad.getCodigoEstadoEntidad())) {
				unitat = toUnitatOrganitzativa(unidad);
			} else {
				throw new SistemaExternException(
						"La unitat organitzativa no està vigent (" +
						"codi=" + codi + ")");
			}
			return unitat;
		} catch (SistemaExternException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SistemaExternException(
					"No s'ha pogut consultar la unitat organitzativa (" +
					"codi=" + codi + ")",
					ex);
		}
	}

	@Override
	public List<UnitatOrganitzativaD3> cercaUnitatsD3(
			String codiUnitat, 
			String denominacioUnitat,
			Long codiNivellAdministracio, 
			Long codiComunitat, 
			Boolean ambOficines, 
			Boolean esUnitatArrel,
			Long codiProvincia, 
			String codiLocalitat) throws SistemaExternException {
		try {
			URL url = new URL(getServiceCercaUrl()
					+ "?codigo=" + codiUnitat
					+ "&denominacion=" + denominacioUnitat
					+ "&codNivelAdministracion=" + (codiNivellAdministracio != null ? codiNivellAdministracio : "-1")
					+ "&codComunidadAutonoma=" + (codiComunitat != null ? codiComunitat : "-1")
					+ "&conOficinas=" + (ambOficines != null && ambOficines ? "true" : "false")
					+ "&unidadRaiz=" + (esUnitatArrel != null && esUnitatArrel ? "true" : "false")
					+ "&provincia="+ (codiProvincia != null ? codiProvincia : "-1")
					+ "&localidad=" + (codiLocalitat != null ? codiLocalitat : "-1"));
			HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
			httpConnection.setRequestMethod("GET");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			List<UnitatOrganitzativaD3> unitats = mapper.readValue(
					httpConnection.getInputStream(), 
					TypeFactory.defaultInstance().constructCollectionType(
							List.class,  
							UnitatOrganitzativaD3.class));
			Collections.sort(unitats);
			return unitats;
		} catch (Exception ex) {
			throw new SistemaExternException(
					"No s'han pogut consultar les unitats organitzatives via REST (" +
					"codiUnitat=" + codiUnitat + ", " +
					"denominacioUnitat=" + denominacioUnitat + ", " +
					"codiNivellAdministracio=" + codiNivellAdministracio + ", " +
					"codiComunitat=" + codiComunitat + ", " +
					"ambOficines=" + ambOficines + ", " +
					"esUnitatArrel=" + esUnitatArrel + ", " +
					"codiProvincia=" + codiProvincia + ", " +
					"codiLocalitat=" + codiLocalitat + ")",
					ex);
		}
	}
	
	public List<UnitatOrganitzativa> cercaUnitats(
			String codiUnitat, 
			String denominacioUnitat,
			Long codiNivellAdministracio, 
			Long codiComunitat, 
			Boolean ambOficines, 
			Boolean esUnitatArrel,
			Long codiProvincia, 
			String codiLocalitat) throws SistemaExternException {
		try {
			URL url = new URL(getServiceCercaUrl()
					+ "?codigo=" + codiUnitat
					+ "&denominacion=" + denominacioUnitat
					+ "&codNivelAdministracion=" + (codiNivellAdministracio != null ? codiNivellAdministracio : "-1")
					+ "&codComunidadAutonoma=" + (codiComunitat != null ? codiComunitat : "-1")
					+ "&conOficinas=" + (ambOficines != null && ambOficines ? "true" : "false")
					+ "&unidadRaiz=" + (esUnitatArrel != null && esUnitatArrel ? "true" : "false")
					+ "&provincia="+ (codiProvincia != null ? codiProvincia : "-1")
					+ "&localidad=" + (codiLocalitat != null ? codiLocalitat : "-1"));
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
					"codiUnitat=" + codiUnitat + ", " +
					"denominacioUnitat=" + denominacioUnitat + ", " +
					"codiNivellAdministracio=" + codiNivellAdministracio + ", " +
					"codiComunitat=" + codiComunitat + ", " +
					"ambOficines=" + ambOficines + ", " +
					"esUnitatArrel=" + esUnitatArrel + ", " +
					"codiProvincia=" + codiProvincia + ", " +
					"codiLocalitat=" + codiLocalitat + ")",
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
	
	public List<Localitat> getLocalitatsPerProvincia(
			Long codiProvincia) throws SistemaExternException {
		try {
			
			String usuari = getRegWeb3RestServiceUser();
			String contrasenya = getRegWeb3RestServicePassword();
			
			String url = getRegWeb3RestServiceUrl() + "/obtenerLocalidadesProvincia?id=" + codiProvincia;
			
			Response responseGet = Jsoup.connect(url)
					.method(Method.GET)
					.execute();
			Response responseLogin = Jsoup.
				connect("https://proves.caib.es/regweb3/rest/j_security_check").
				data("j_username", usuari).
				data("j_password", contrasenya).
				cookies(responseGet.cookies()).
				ignoreContentType(true).
				method(Method.POST).
				execute();
				
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			List<Localitat> localitats = mapper.readValue(
					responseLogin.body(), 
					TypeFactory.defaultInstance().constructCollectionType(
							List.class,  
							Localitat.class));
			return localitats;
		} catch (Exception ex) {
			throw new SistemaExternException(
					"No s'han pogut consultar les localitats via REST (" +
					"codiProvincia=" + codiProvincia + ")",
					ex);
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
	private String getRegWeb3RestServiceUrl() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.regweb3.rest.base.url");
	}
	private String getRegWeb3RestServiceUser() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.regweb3.rest.user");
	}
	private String getRegWeb3RestServicePassword() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.regweb3.rest.password");
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
				unidad.getNumVia());
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

	@Override
	public List<ProvinciaRw3> getProvinciaPerComunitat(Long codiComunitat) throws SistemaExternException {
		try {
			
			String usuari = getRegWeb3RestServiceUser();
			String contrasenya = getRegWeb3RestServicePassword();
			
			String url = getRegWeb3RestServiceUrl() + "/obtenerProvincias?id=" + codiComunitat;
			
			Response responseGet = Jsoup.connect(url)
					.method(Method.GET)
					.execute();
			Response responseLogin = Jsoup.
				connect("https://proves.caib.es/regweb3/rest/j_security_check").
				data("j_username", usuari).
				data("j_password", contrasenya).
				cookies(responseGet.cookies()).
				ignoreContentType(true).
				method(Method.POST).
				execute();
				
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			List<ProvinciaRw3> provincies = mapper.readValue(
					responseLogin.body(), 
					TypeFactory.defaultInstance().constructCollectionType(
							List.class,  
							ProvinciaRw3.class));
			return provincies;
		} catch (Exception ex) {
			throw new SistemaExternException(
					"No s'han pogut consultar les provincies via REST (" +
					"codiComunitat=" + codiComunitat + ")",
					ex);
		}
	}
}
