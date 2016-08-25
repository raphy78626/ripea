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

import es.caib.ripea.core.api.dto.BustiaContingutPendentTipusEnumDto;
import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.dto.ExpedientFiltreDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.service.ExpedientService;

/**
 * Implementació de ContenidorService com a EJB que empra una clase
 * delegada per accedir a la funcionalitat del servei.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class ExpedientServiceBean implements ExpedientService {

	@Autowired
	ExpedientService delegate;

	@Override
	@RolesAllowed("tothom")
	public ExpedientDto create(
			Long entitatId,
			Long contenidorId,
			Long metaExpedientId,
			Long arxiuId,
			Integer any,
			String nom,
			BustiaContingutPendentTipusEnumDto contingutTipus,
			Long contingutId) {
		return delegate.create(
				entitatId,
				contenidorId,
				metaExpedientId,
				arxiuId,
				any,
				nom,
				contingutTipus,
				contingutId);
	}

	@Override
	@RolesAllowed("tothom")
	public ExpedientDto update(
			Long entitatId,
			Long expedientId,
			Long arxiuId,
			Long metaExpedientId,
			String nom) {
		return delegate.update(
				entitatId,
				expedientId,
				arxiuId,
				metaExpedientId,
				nom);
	}

	@Override
	@RolesAllowed("tothom")
	public ExpedientDto delete(
			Long entitatId,
			Long expedientId) {
		return delegate.delete(entitatId, expedientId);
	}

	@Override
	@RolesAllowed("tothom")
	public ExpedientDto findById(
			Long entitatId,
			Long id) {
		return delegate.findById(entitatId, id);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public PaginaDto<ExpedientDto> findPaginatAdmin(
			Long entitatId,
			ExpedientFiltreDto filtre,
			PaginacioParamsDto paginacioParams) {
		return delegate.findPaginatAdmin(entitatId, filtre, paginacioParams);
	}

	@Override
	@RolesAllowed("tothom")
	public PaginaDto<ExpedientDto> findPaginatUser(
			Long entitatId,
			ExpedientFiltreDto filtre,
			PaginacioParamsDto paginacioParams) {
		return delegate.findPaginatUser(entitatId, filtre, paginacioParams);
	}

	@Override
	@RolesAllowed("tothom")
	public void agafarUser(
			Long entitatId,
			Long id) {
		delegate.agafarUser(entitatId, id);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public void agafarAdmin(
			Long entitatId,
			Long arxiuId,
			Long id,
			String usuariCodi) {
		delegate.agafarAdmin(entitatId, arxiuId, id, usuariCodi);
	}

	@Override
	@RolesAllowed("tothom")
	public void alliberarUser(
			Long entitatId,
			Long id) {
		delegate.alliberarUser(entitatId, id);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public void alliberarAdmin(
			Long entitatId,
			Long id) {
		delegate.alliberarAdmin(entitatId, id);
	}

	@Override
	@RolesAllowed("tothom")
	public void tancar(
			Long entitatId,
			Long id,
			String motiu) {
		delegate.tancar(entitatId, id, motiu);
	}

	@Override
	@RolesAllowed("tothom")
	public void acumular(
			Long entitatId,
			Long id,
			Long acumulatId) {
		delegate.acumular(
				entitatId,
				id,
				acumulatId);
	}

	@Override
	@RolesAllowed("tothom")
	public void afegirContingutBustia(
			Long entitatId,
			Long id,
			Long bustiaId,
			BustiaContingutPendentTipusEnumDto contingutTipus,
			Long contingutId) {
		delegate.afegirContingutBustia(
				entitatId,
				id,
				bustiaId,
				contingutTipus,
				contingutId);
	}

	@Override
	@RolesAllowed("tothom")
	public void relacionar(
			Long entitatId,
			Long id,
			Long acumulatId) {
		delegate.relacionar(
				entitatId,
				id,
				acumulatId);
	}

	@Override
	@RolesAllowed("tothom")
	public List<ExpedientDto> relacioFindAmbExpedient(
			Long entitatId, 
			Long expedientId) {
		return delegate.relacioFindAmbExpedient(entitatId, expedientId);
	}

	@Override
	@RolesAllowed("tothom")
	public boolean relacioDelete(
			Long entitatId, 
			Long expedientId, 
			Long relacionatId) throws NotFoundException {
		return delegate.relacioDelete(entitatId, expedientId, relacionatId);
	}

}
