/**
 * 
 */
package es.caib.ripea.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.LocalitatDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.dto.ProvinciaRw3Dto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaD3Dto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.dto.UnitatsFiltreDto;
import es.caib.ripea.core.api.service.UnitatsOrganitzativesService;
import es.caib.ripea.core.helper.PluginHelper;
import es.caib.ripea.core.helper.UnitatOrganitzativaHelper;

/**
 * Implementació del servei de gestió d'entitats.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class UnitatsOrganitzativesServiceImpl implements UnitatsOrganitzativesService {
	
	@Resource
	private UnitatOrganitzativaHelper unitatOrganitzativaHelper;
	@Resource
	private PluginHelper pluginHelper;
	
	@Transactional
	@Override
	public UnitatOrganitzativaDto findUnitatOrganitzativaByCodi(String codi) {
		UnitatOrganitzativaDto result = null;
		try {
			result = unitatOrganitzativaHelper.findPerCodi(codi);
		} catch(Exception ex) {
			result = new UnitatOrganitzativaDto();
		}
		return result;
	}
	
	@Transactional
	@Override
	public PaginaDto<UnitatOrganitzativaD3Dto> findUnitatsOrganitzativesPerDatatable(
			UnitatsFiltreDto filtre,
			PaginacioParamsDto paginacioParams) {
		return unitatOrganitzativaHelper.findUnitatsOrganitzativesPerDatatable(
				filtre, 
				null);
	}
	
	@Transactional
	@Override
	public List<LocalitatDto> findLocalitatsPerProvincia(String codiProvincia) {
		List<LocalitatDto> localitats = new ArrayList<LocalitatDto>();
		
		if (codiProvincia != null && !codiProvincia.isEmpty())
			localitats = pluginHelper.findLocalitatsPerProvincia(codiProvincia);
		
		return localitats;
	}

	@Transactional
	@Override
	public List<ProvinciaRw3Dto> findProvinciesPerComunitat(String codiComunitat) {
		List<ProvinciaRw3Dto> provincies = new ArrayList<ProvinciaRw3Dto>();
		
		if (codiComunitat != null && !codiComunitat.isEmpty())
			provincies = pluginHelper.findProvinciesPerComunitat(codiComunitat);
		
		return provincies;
	}
}
