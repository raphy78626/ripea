/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.LocalitatDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.dto.ProvinciaRw3Dto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaD3Dto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.dto.UnitatsFiltreDto;
import es.caib.ripea.core.api.exception.NotFoundException;

/**
 * Declaració dels mètodes per a gestionar continguts.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface UnitatsOrganitzativesService {

	/**
	 * Canvia el nom del contingut.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contingut.
	 * @param contingutId
	 *            Atribut id del contingut del qual es vol consultar el contingut.
	 * @param nom
	 *            El nom que es vol posar al contingut.
	 * @return El contingut modificat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public UnitatOrganitzativaDto findUnitatOrganitzativaByCodi(
			String codi) throws NotFoundException;
	
	@PreAuthorize("hasRole('tothom')")
	public PaginaDto<UnitatOrganitzativaD3Dto> findUnitatsOrganitzativesPerDatatable(
			UnitatsFiltreDto filtre,
			PaginacioParamsDto paginacioParams) throws NotFoundException;

	@PreAuthorize("hasRole('tothom')")
	public List<LocalitatDto> findLocalitatsPerProvincia(
			String codiProvincia) throws NotFoundException;
	
	@PreAuthorize("hasRole('tothom')")
	public List<ProvinciaRw3Dto> findProvinciesPerComunitat(
			String codiComunitat) throws NotFoundException;
	
}
