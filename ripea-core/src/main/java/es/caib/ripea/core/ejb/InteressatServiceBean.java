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

import es.caib.ripea.core.api.dto.InteressatAdministracioDto;
import es.caib.ripea.core.api.dto.InteressatCiutadaDto;
import es.caib.ripea.core.api.dto.InteressatDto;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.ExpedientNotFoundException;
import es.caib.ripea.core.api.exception.InteressatNotFoundException;
import es.caib.ripea.core.api.service.InteressatService;

/**
 * Implementació de InteressatService com a EJB que empra una clase
 * delegada per accedir a la funcionalitat del servei.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class InteressatServiceBean implements InteressatService {

	@Autowired
	InteressatService delegate;



	@Override
	@RolesAllowed("tothom")
	public InteressatDto create(
			Long entitatId,
			Long expedientId,
			InteressatDto interessat) throws EntitatNotFoundException, ExpedientNotFoundException {
		return delegate.create(entitatId, expedientId, interessat);
	}

	@Override
	@RolesAllowed("tothom")
	public void addToExpedient(
			Long entitatId,
			Long expedientId, Long id) throws EntitatNotFoundException, ExpedientNotFoundException, InteressatNotFoundException {
		delegate.addToExpedient(entitatId, expedientId, id);
	}

	@Override
	@RolesAllowed("tothom")
	public void removeFromExpedient(
			Long entitatId,
			Long expedientId,
			Long id) throws EntitatNotFoundException, ExpedientNotFoundException, InteressatNotFoundException {
		delegate.removeFromExpedient(entitatId, expedientId, id);
	}

	@Override
	@RolesAllowed("tothom")
	public InteressatDto findById(
			Long entitatId,
			Long id) throws EntitatNotFoundException {
		return delegate.findById(entitatId, id);
	}

	@Override
	@RolesAllowed("tothom")
	public List<InteressatDto> findByExpedient(
			Long entitatId,
			Long expedientId) throws EntitatNotFoundException, ExpedientNotFoundException {
		return delegate.findByExpedient(entitatId, expedientId);
	}

	@Override
	@RolesAllowed("tothom")
	public List<InteressatCiutadaDto> findByFiltreCiutada(
			Long entitatId,
			String nom,
			String nif,
			String llinatges) {
		return delegate.findByFiltreCiutada(
				entitatId,
				nom,
				nif,
				llinatges);
	}

	@Override
	@RolesAllowed("tothom")
	public List<InteressatAdministracioDto> findByFiltreAdministracio(
			Long entitatId,
			String nom,
			String identificador) throws EntitatNotFoundException {
		return delegate.findByFiltreAdministracio(
				entitatId,
				nom,
				identificador);
	}

}
