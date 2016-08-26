/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.MunicipiDto;
import es.caib.ripea.core.api.dto.PaisDto;
import es.caib.ripea.core.api.dto.ProvinciaDto;

/**
 * Mètodes per a obtenir dades de fonts externes.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface DadesExternesService {

	/**
	 * Retorna el llistat de tots els països.
	 * 
	 * @return el llistat de països.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<PaisDto> findPaisos();
	
	/**
	 * Retorna el llistat de totes les províncies.
	 * 
	 * @return el llistat de províncies.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<ProvinciaDto> findProvincies();
	
	/**
	 * Retorna el llistat de totes les províncies d'una comunitat.
	 * 
	 * @return el llistat de províncies.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<ProvinciaDto> findProvinciesPerComunitat(String comunitatCodi);

	/**
	 * Retorna el llistat dels municipis d'una província.
	 * 
	 * @param provinciaCodi
	 *            El codi de la província.
	 * @return el llistat de municipis.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<MunicipiDto> findMunicipisPerProvincia(String provinciaCodi);

}
