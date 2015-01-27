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

import es.caib.ripea.core.api.dto.ContenidorDto;
import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.dto.ExpedientFiltreDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.exception.ArxiuNotFoundException;
import es.caib.ripea.core.api.exception.ContenidorNotFoundException;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.ExpedientNotFoundException;
import es.caib.ripea.core.api.exception.MetaExpedientNotFoundException;
import es.caib.ripea.core.api.exception.NomInvalidException;
import es.caib.ripea.core.api.exception.UsuariNotFoundException;
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
			Long contingutId,
			Long registreId) throws EntitatNotFoundException, ContenidorNotFoundException, MetaExpedientNotFoundException, ArxiuNotFoundException, NomInvalidException {
		return delegate.create(
				entitatId,
				contenidorId,
				metaExpedientId,
				arxiuId,
				any,
				nom,
				contingutId,
				registreId);
	}

	@Override
	@RolesAllowed("tothom")
	public ExpedientDto update(
			Long entitatId,
			Long expedientId,
			Long arxiuId,
			Long metaExpedientId,
			String nom) throws EntitatNotFoundException, ExpedientNotFoundException, ArxiuNotFoundException, NomInvalidException {
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
			Long expedientId) throws EntitatNotFoundException, ExpedientNotFoundException {
		return delegate.delete(entitatId, expedientId);
	}

	@Override
	@RolesAllowed("tothom")
	public ExpedientDto findById(
			Long entitatId,
			Long id) throws EntitatNotFoundException {
		return delegate.findById(entitatId, id);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public PaginaDto<ExpedientDto> findPaginatAdmin(
			Long entitatId,
			ExpedientFiltreDto filtre,
			PaginacioParamsDto paginacioParams) throws EntitatNotFoundException, ArxiuNotFoundException {
		return delegate.findPaginatAdmin(entitatId, filtre, paginacioParams);
	}

	@Override
	@RolesAllowed("tothom")
	public PaginaDto<ExpedientDto> findPaginatUser(
			Long entitatId,
			ExpedientFiltreDto filtre,
			PaginacioParamsDto paginacioParams) throws EntitatNotFoundException, ArxiuNotFoundException {
		return delegate.findPaginatUser(entitatId, filtre, paginacioParams);
	}

	@Override
	@RolesAllowed("tothom")
	public List<ContenidorDto> getContingutCarpetaNouvinguts(
			Long entitatId,
			Long id) throws EntitatNotFoundException, ExpedientNotFoundException {
		return delegate.getContingutCarpetaNouvinguts(
				entitatId,
				id);
	}

	@Override
	@RolesAllowed("tothom")
	public void agafarUser(
			Long entitatId,
			Long arxiuId,
			Long id) throws EntitatNotFoundException, ArxiuNotFoundException, ExpedientNotFoundException {
		delegate.agafarUser(entitatId, arxiuId, id);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public void agafarAdmin(
			Long entitatId,
			Long arxiuId,
			Long id,
			String usuariCodi) throws EntitatNotFoundException, ArxiuNotFoundException, ExpedientNotFoundException, UsuariNotFoundException {
		delegate.agafarAdmin(entitatId, arxiuId, id, usuariCodi);
	}

	@Override
	@RolesAllowed("tothom")
	public void alliberarUser(
			Long entitatId,
			Long id) throws EntitatNotFoundException, ExpedientNotFoundException {
		delegate.alliberarUser(entitatId, id);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public void alliberarAdmin(
			Long entitatId,
			Long id) throws EntitatNotFoundException, ExpedientNotFoundException {
		delegate.alliberarAdmin(entitatId, id);
	}

	@Override
	@RolesAllowed("tothom")
	public void finalitzar(
			Long entitatId,
			Long id,
			String motiu) throws EntitatNotFoundException, ExpedientNotFoundException {
		delegate.finalitzar(entitatId, id, motiu);
	}

	@Override
	@RolesAllowed("tothom")
	public void acumular(
			Long entitatId,
			Long id,
			Long acumulatId) throws EntitatNotFoundException, ExpedientNotFoundException {
		delegate.acumular(entitatId, id, acumulatId);
	}

}
