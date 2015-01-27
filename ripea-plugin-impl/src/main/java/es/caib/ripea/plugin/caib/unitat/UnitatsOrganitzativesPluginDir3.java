/**
 * 
 */
package es.caib.ripea.plugin.caib.unitat;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.BindingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.caib.dir3caib.ObtenerUnidades;
import es.caib.dir3caib.ObtenerUnidadesService;
import es.caib.dir3caib.UnidadTF;
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
			List<UnidadTF> listaUnidades= getObtenerUnidadesService().obtenerArbolUnidades(pareCodi, null);//df.format(new Date()));
			if (listaUnidades != null) {
				for (UnidadTF unidad: listaUnidades) {
					if ("V".equalsIgnoreCase(unidad.getCodigoEstadoEntidad())) {
						UnitatOrganitzativa unitat = new UnitatOrganitzativa(
								unidad.getCodigo(),
								unidad.getDenominacion(),
								unidad.getCodigo(), // CifNif
								unidad.getFechaAltaOficial() != null ? unidad.getFechaAltaOficial().toGregorianCalendar().getTime() : null,
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
			UnidadTF unidad = getObtenerUnidadesService().obtenerUnidad(codi, null);
			if (unidad != null && "V".equalsIgnoreCase(unidad.getCodigoEstadoEntidad())) {
				unitat = new UnitatOrganitzativa(
						unidad.getCodigo(),
						unidad.getDenominacion(),
						unidad.getCodigo(), // CifNif
						unidad.getFechaAltaOficial() != null ? unidad.getFechaAltaOficial().toGregorianCalendar().getTime() : null,
						unidad.getCodigoEstadoEntidad(),
						unidad.getCodUnidadSuperior(),
						unidad.getCodUnidadRaiz());
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
	
	private ObtenerUnidades getObtenerUnidadesService() throws Exception {
		ObtenerUnidades client = null;
		URL url = new URL(getServiceUrl() + "?wsdl");
		ObtenerUnidadesService service = new ObtenerUnidadesService(url);
		client = service.getObtenerUnidades();
		BindingProvider bp = (BindingProvider)client;
		bp.getRequestContext().put(
				BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				getServiceUrl());
				//"http://10.215.9.169/dir3caib/ws/ObtenerUnidadesImpl");
		return client;
	}

	private String getServiceUrl() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.unitat.dir3.url");
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(UnitatsOrganitzativesPluginDir3.class);

}
