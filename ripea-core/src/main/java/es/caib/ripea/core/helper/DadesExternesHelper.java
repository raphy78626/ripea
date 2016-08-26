/**
 * 
 */
package es.caib.ripea.core.helper;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import es.caib.ripea.core.api.dto.MunicipiDto;
import es.caib.ripea.core.api.dto.PaisDto;
import es.caib.ripea.core.api.dto.ProvinciaDto;

/**
 * Implementació dels mètodes per obtenir dades de fonts
 * externes.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class DadesExternesHelper {

	public List<PaisDto> findPaisos() {
		LOGGER.debug("Cercant tots els paisos");
		try {
			URL url = new URL(getDadesComunesBaseUrl() + "/services/paisos/format/JSON/idioma/ca");
			HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
			httpConnection.setRequestMethod("GET");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);

			ObjectMapper mapper = new ObjectMapper();
			List<PaisDto> paisos = mapper.readValue(
					httpConnection.getInputStream(), 
					TypeFactory.defaultInstance().constructCollectionType(
							List.class,  
							PaisDto.class));
			Collections.sort(paisos);
			return paisos;
			
		} catch (Exception ex) {
			LOGGER.error("Error al obtenir les províncies de la font externa", ex);
			return null;
		}
	}
	
	public List<ProvinciaDto> findProvincies() {
		LOGGER.debug("Cercant totes les províncies");
		try {
			URL url = new URL(getDadesComunesBaseUrl() + "/services/provincies/format/JSON/idioma/ca");
			HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
			httpConnection.setRequestMethod("GET");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			
			ObjectMapper mapper = new ObjectMapper();
			List<ProvinciaDto> provincies = mapper.readValue(
					httpConnection.getInputStream(), 
					TypeFactory.defaultInstance().constructCollectionType(
							List.class,  
							ProvinciaDto.class));
			Collections.sort(provincies);
			return provincies;
			
		} catch (Exception ex) {
			LOGGER.error("Error al obtenir les províncies de la font externa", ex);
			return null;
		}
	}
	
	public List<ProvinciaDto> findProvinciesPerComunitat(String comunitatCodi) {
		LOGGER.debug("Cercant totes les províncies de la comunitat (comunitatCodi=" + comunitatCodi + ")");
		try {
			URL url = new URL(getDadesComunesBaseUrl() + "/services/provincies/" + comunitatCodi + "/format/JSON/idioma/ca");
			HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
			httpConnection.setRequestMethod("GET");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			
			ObjectMapper mapper = new ObjectMapper();
			
			List<ProvinciaDto> provincies = mapper.readValue(
					httpConnection.getInputStream(),
					TypeFactory.defaultInstance().constructCollectionType(
							List.class,
							ProvinciaDto.class));
			Collections.sort(provincies);
			return provincies;
			
		} catch (Exception ex) {
			LOGGER.error("Error al obtenir les províncies de la font externa", ex);
			return null;
		}
	}

	public List<MunicipiDto> findMunicipisPerProvincia(String provinciaCodi) {
		LOGGER.debug("Cercant els municipis de la província (provinciaCodi=" + provinciaCodi + ")");
		try {
			URL url = new URL(getDadesComunesBaseUrl() + "/services/municipis/" + provinciaCodi + "/format/JSON");
			HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
			httpConnection.setRequestMethod("GET");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			
			ObjectMapper mapper = new ObjectMapper();
			
			List<MunicipiDto> municipis = mapper.readValue(
					httpConnection.getInputStream(),
					TypeFactory.defaultInstance().constructCollectionType(
							List.class,
							MunicipiDto.class));
			Collections.sort(municipis);
			return municipis;
		} catch (Exception ex) {
			LOGGER.error("Error al obtenir les províncies de la font externa", ex);
			return null;
		}
	}

	private String getDadesComunesBaseUrl() {
		String baseUrl = PropertiesHelper.getProperties().getProperty("es.caib.ripea.dadescomunes.base.url");
		if (baseUrl != null && baseUrl.length() > 0) {
			return baseUrl;
		} else {
			return "https://proves.caib.es/dadescomunsfront";
		}
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(DadesExternesHelper.class);

}
