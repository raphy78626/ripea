/**
 * 
 */
package es.caib.ripea.plugin.caib.unitat;

import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.ripea.plugin.SistemaExternException;
import es.caib.ripea.plugin.unitat.UnitatOrganitzativa;
import es.caib.ripea.plugin.unitat.UnitatsOrganitzativesPlugin;

/**
 * Implementació de proves del plugin d'unitats organitzatives.
 * La estructura d'unitats és la següent:
 *   arrel: Limit Tecnologies (00000000T)
 *   filla: Departament de programari (12345678Z)
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class UnitatsOrganitzativesPluginMock implements UnitatsOrganitzativesPlugin {

	private static final String CODI_UNITAT_ARREL = "00000000T";
	private static final String CODI_UNITAT_FILLA = "12345678Z";

	private List<UnitatOrganitzativa> unitats;
	
	
	@Override
	public UnitatOrganitzativa findUnidad(String pareCodi, Timestamp fechaActualizacion, Timestamp fechaSincronizacion) throws MalformedURLException{
		return new UnitatOrganitzativa("E00003601", "Ministerio de Fomento","EA9999999", "E00003601","V", null);
	}
	
	@Override
	public List<UnitatOrganitzativa> findAmbPare(String pareCodi, Timestamp fechaActualizacion, Timestamp fechaSincronizacion) throws SistemaExternException{
		
		List<UnitatOrganitzativa> unitats = new ArrayList<>();
		unitats.add(new UnitatOrganitzativa("EA0004099", "Adif-Alta Velocidad","E00003601", "E00003601","T", new ArrayList<>(Arrays.asList("E00125501", "EA0004100"))));
		unitats.add(new UnitatOrganitzativa("E00125501", "Administracion del Estado en el Exterior - Representacion Permanente de Transportes","E00003601", "E00003601","V", null));
		unitats.add(new UnitatOrganitzativa("EA0004100", "AENA Aeropuertos, S.A.","E00003601", "E00003601","V", null));
		unitats.add(new UnitatOrganitzativa("E04865601", "Agencia Estatal de Seguridad Aerea","E00003601", "E00003601","T", new ArrayList<>(Arrays.asList("EA0004101"))));
		unitats.add(new UnitatOrganitzativa("E04978901", "Agencia Estatal de Seguridad Ferroviaria","E04590204", "E00003601","T", new ArrayList<>(Arrays.asList("EA0004101"))));
		unitats.add(new UnitatOrganitzativa("EA0004101", "AENA Desarrollo Internacional, S.A.","E00003601", "E00003601","V",null));
		unitats.add(new UnitatOrganitzativa("EA0004518", "Autoridad Portuaria de Baleares","E00003601", "E00003601","T", new ArrayList<>(Arrays.asList("EA0008120"))));
		unitats.add(new UnitatOrganitzativa("EA0008120", "Autoridad Portuaria de Gijón","E00003601", "E00003601","V", null));
		
		return unitats;
	}





	/*@Override
	public List<UnitatOrganitzativaD3> cercaUnitatsD3(
			String codiUnitat, 
			String denominacioUnitat,
			Long codiNivellAdministracio, 
			Long codiComunitat, 
			Boolean ambOficines, 
			Boolean esUnitatArrel,
			Long codiProvincia, 
			String codiLocalitat) throws SistemaExternException {
		throw new SistemaExternException("Mètode no implementat");
	}*/

	private List<UnitatOrganitzativa> getUnitats() {
		if (unitats == null) {
			unitats = new ArrayList<UnitatOrganitzativa>();
			UnitatOrganitzativa pare = new UnitatOrganitzativa();
			pare.setCodi(CODI_UNITAT_ARREL);
			pare.setDenominacio("Límit Tecnologies");
			unitats.add(pare);
			UnitatOrganitzativa fill = new UnitatOrganitzativa();
			fill.setCodi(CODI_UNITAT_FILLA);
			fill.setDenominacio("Departament de programari");
			fill.setCodiUnitatArrel(CODI_UNITAT_ARREL);
			fill.setCodiUnitatSuperior(CODI_UNITAT_ARREL);
			unitats.add(fill);
		}
		return unitats;
	}

	@Override
	public List<UnitatOrganitzativa> cercaUnitats(String codiUnitat, String denominacioUnitat,
			Long codiNivellAdministracio, Long codiComunitat, Boolean ambOficines, Boolean esUnitatArrel,
			Long codiProvincia, String codiLocalitat) throws SistemaExternException {
		// TODO Auto-generated method stub
		return null;
	}

}
