/**
 * 
 */
package es.caib.ripea.plugin.caib.unitat;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	public List<UnitatOrganitzativa> findAmbPare(String pareCodi) throws SistemaExternException {
		try {
			List<UnitatOrganitzativa> unitats = new ArrayList<UnitatOrganitzativa>();
			UnitatOrganitzativa unitatOrganitzativaArrel = findAmbCodi(pareCodi);
			unitats.add(unitatOrganitzativaArrel);
			List<UnidadTF> unidades = getObtenerUnidadesService().obtenerArbolUnidades(
					pareCodi,
					null,
					null);//df.format(new Date()));
			if (unidades != null) {
				for (UnidadTF unidad: unidades) {
					if ("V".equalsIgnoreCase(unidad.getCodigoEstadoEntidad())) {
						UnitatOrganitzativa unitat = new UnitatOrganitzativa(
								unidad.getCodigo(),
								unidad.getDenominacion(),
								unidad.getCodigo(), // CifNif
								unidad.getFechaAltaOficial(),
								unidad.getCodigoEstadoEntidad(),
								unidad.getCodUnidadSuperior(),
								unidad.getCodUnidadRaiz());
						
						unitats.add(unitat);
					}
				}
			}
			return unitats;
		} catch (Exception ex) {
			LOGGER.error(
					"No s'han pogut consultar les unitats organitzatives (" +
					"pareCodi=" + pareCodi + ")",
					ex);
			throw new SistemaExternException(
					"No s'han pogut consultar les unitats organitzatives (" +
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
				unitat = new UnitatOrganitzativa(
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
						//getAdressa(unidad.getCodigoTipoVia(), unidad.getNombreVia(), unidad.getNumVia()));
						unidad.getCodigoTipoVia(), 
						unidad.getNombreVia(), 
						unidad.getNumVia());
			} else {
				LOGGER.error(
						"La unitat organitzativa no està vigent (" +
						"codi=" + codi + ")");
				throw new SistemaExternException(
						"La unitat organitzativa no està vigent (" +
						"codi=" + codi + ")");
			}
			return unitat;
		} catch (SistemaExternException ex) {
			throw ex;
		} catch (Exception ex) {
			LOGGER.error(
					"No s'ha pogut consultar la unitat organitzativa (" +
					"codi=" + codi + ")",
					ex);
			throw new SistemaExternException(
					"No s'ha pogut consultar la unitat organitzativa (" +
					"codi=" + codi + ")",
					ex);
		}
	}

	@Override
	public List<UnitatOrganitzativa> cercaUnitats(
			String codiUnitat, 
			String denominacioUnitat,
			Long codiNivellAdministracio, 
			Long codiComunitat, 
			Boolean ambOficines, 
			Boolean esUnitatArrel,
			Long codiProvincia, 
			String codiLocalitat) throws SistemaExternException {
		LOGGER.debug("Cercant tots els paisos");
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
			LOGGER.error("Error al obtenir les províncies de la font externa", ex);
			return null;
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
		Integer connectTimeout = getServiceConnectTimeout();
		if (connectTimeout != null) {
			bp.getRequestContext().put(
					"com.sun.xml.internal.ws.connect.timeout",
					connectTimeout);
		}
		Integer requestTimeout = getServiceRequestTimeout();
		if (requestTimeout != null) {
			bp.getRequestContext().put(
					"com.sun.xml.internal.ws.request.timeout",
					requestTimeout);
		}
		return client;
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
	private Integer getServiceConnectTimeout() {
		String key = "es.caib.ripea.plugin.unitats.organitzatives.dir3.service.connect.timeout";
		if (PropertiesHelper.getProperties().getProperty(key) != null)
			return PropertiesHelper.getProperties().getAsInt(key);
		else
			return null;
	}
	private Integer getServiceRequestTimeout() {
		String key = "es.caib.ripea.plugin.unitats.organitzatives.dir3.service.request.timeout";
		if (PropertiesHelper.getProperties().getProperty(key) != null)
			return PropertiesHelper.getProperties().getAsInt(key);
		else
			return null;
	}
	private String getServiceCercaUrl() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.unitats.cerca.dir3.service.url");
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(UnitatsOrganitzativesPluginDir3.class);

}
