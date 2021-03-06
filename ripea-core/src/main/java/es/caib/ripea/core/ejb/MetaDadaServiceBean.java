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

import es.caib.ripea.core.api.dto.MetaDadaDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.MetaDadaNotFoundException;
import es.caib.ripea.core.api.exception.NodeNotFoundException;
import es.caib.ripea.core.api.service.MetaDadaService;

/**
 * Implementació de MetaDadaService com a EJB que empra una clase
 * delegada per accedir a la funcionalitat del servei.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class MetaDadaServiceBean implements MetaDadaService {

	@Autowired
	MetaDadaService delegate;



	@Override
	@RolesAllowed("IPA_ADMIN")
	public MetaDadaDto create(
			Long entitatId,
			MetaDadaDto metaDada) {
		return delegate.create(entitatId, metaDada);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public MetaDadaDto update(
			Long entitatId,
			MetaDadaDto metaDada) throws EntitatNotFoundException, MetaDadaNotFoundException {
		return delegate.update(entitatId, metaDada);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public MetaDadaDto updateActiva(
			Long entitatId,
			Long id,
			boolean activa) throws EntitatNotFoundException, MetaDadaNotFoundException {
		return delegate.updateActiva(entitatId, id, activa);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public MetaDadaDto delete(
			Long entitatId,
			Long metaDadaId) throws EntitatNotFoundException, MetaDadaNotFoundException {
		return delegate.delete(entitatId, metaDadaId);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public MetaDadaDto findById(
			Long entitatId,
			Long id) throws EntitatNotFoundException {
		return delegate.findById(
				entitatId,
				id);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public MetaDadaDto findByEntitatCodi(
			Long entitatId,
			String codi) throws EntitatNotFoundException {
		return delegate.findByEntitatCodi(entitatId, codi);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public PaginaDto<MetaDadaDto> findAllByEntitatPaginat(
			Long entitatId,
			PaginacioParamsDto paginacioParams) throws EntitatNotFoundException {
		return delegate.findAllByEntitatPaginat(entitatId, paginacioParams);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public List<MetaDadaDto> findActiveByEntitat(
			Long entitatId,
			boolean incloureGlobalsExpedient,
			boolean incloureGlobalsDocument) throws EntitatNotFoundException {
		return delegate.findActiveByEntitat(
				entitatId,
				incloureGlobalsExpedient,
				incloureGlobalsDocument);
	}

	@Override
	@RolesAllowed("tothom")
	public List<MetaDadaDto> findByNodePerCreacio(
			Long entitatId,
			Long nodeId) throws EntitatNotFoundException, NodeNotFoundException {
		return delegate.findByNodePerCreacio(entitatId, nodeId);
	}

}
