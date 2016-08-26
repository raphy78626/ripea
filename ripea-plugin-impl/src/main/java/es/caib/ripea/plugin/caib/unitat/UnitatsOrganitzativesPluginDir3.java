/**
 * 
 */
package es.caib.ripea.plugin.caib.unitat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
						getAdressa(unidad.getCodigoTipoVia(), unidad.getNombreVia(), unidad.getNumVia()));
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
		String username = getUsername();
		if (username != null && !username.isEmpty()) {
			bp.getRequestContext().put(
					BindingProvider.USERNAME_PROPERTY,
					username);
			bp.getRequestContext().put(
					BindingProvider.PASSWORD_PROPERTY,
					getPassword());
		}
		return client;
	}

	private String getServiceUrl() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.unitats.organitzatives.dir3.service.url");
	}
	private String getUsername() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.unitats.organitzatives.dir3.service.username");
	}
	private String getPassword() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.unitats.organitzatives.dir3.service.password");
	}

	private String getAdressa(
			Long tipusVia,
			String nomVia,
			String numVia) {
		
		String adressa = "";
		if (tipusVia != null) {
			switch (tipusVia.intValue()) {
			case 1:
				adressa = "Alameda ";
				break;
			case 2:
				adressa = "Calle ";
				break;
			case 3:
				adressa = "Camino ";
				break;
			case 4:
				adressa = "Carrer ";
				break;
			case 5:
				adressa = "Carretera ";
				break;
			case 6:
				adressa = "Glorieta";
				break;
			case 7:
				adressa = "Kalea ";
				break;
			case 8:
				adressa = "Pasaje ";
				break;
			case 9:
				adressa = "Paseo ";
				break;
			case 10:
				adressa = "Plaça ";
				break;
			case 11:
				adressa = "Plaza ";
				break;
			case 12:
				adressa = "Rambla ";
				break;
			case 13:
				adressa = "Ronda ";
				break;
			case 14:
				adressa = "Rúa ";
				break;
			case 15:
				adressa = "Sector ";
				break;
			case 16:
				adressa = "Travesía ";
				break;
			case 17:
				adressa = "Urbanización ";
				break;
			default:
				break;
			}
		}
		
		adressa += nomVia;
		if (numVia != null) {
			adressa += ", " + numVia;
		}
		return adressa;
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UnitatsOrganitzativesPluginDir3.class);

}
