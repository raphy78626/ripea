/**
 * 
 */
package es.caib.ripea.core.ejb;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import es.caib.ripea.core.api.dto.LocalitatDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.dto.ProvinciaRw3Dto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaD3Dto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.dto.UnitatsFiltreDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.service.UnitatsOrganitzativesService;

/**
 * Implementació de UnitatsOrganitzativesService com a EJB que empra una clase
 * delegada per accedir a la funcionalitat del servei.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class UnitatsOrganitzativesServiceBean implements UnitatsOrganitzativesService {

	@Autowired
	UnitatsOrganitzativesService delegate;

	@Override
	@RolesAllowed("tothom")
	public UnitatOrganitzativaDto findUnitatOrganitzativaByCodi(String codi) throws NotFoundException {
		return delegate.findUnitatOrganitzativaByCodi(codi);
	}

	@Override
	@RolesAllowed("tothom")
	public PaginaDto<UnitatOrganitzativaD3Dto> findUnitatsOrganitzativesPerDatatable(UnitatsFiltreDto filtre,
			PaginacioParamsDto paginacioParams) throws NotFoundException {
		return delegate.findUnitatsOrganitzativesPerDatatable(filtre, paginacioParams);
	}

	@Override
	@RolesAllowed("tothom")
	public List<LocalitatDto> findLocalitatsPerProvincia(String codiProvincia) throws NotFoundException {
		return delegate.findLocalitatsPerProvincia(codiProvincia);
	}

	@Override
	@RolesAllowed("tothom")
	public List<ProvinciaRw3Dto> findProvinciesPerComunitat(String codiComunitat) throws NotFoundException {
		return delegate.findProvinciesPerComunitat(codiComunitat);
	}
	
}
