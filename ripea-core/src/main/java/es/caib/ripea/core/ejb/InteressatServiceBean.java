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
import es.caib.ripea.core.api.dto.InteressatPersonaFisicaDto;
import es.caib.ripea.core.api.dto.InteressatPersonaJuridicaDto;
import es.caib.ripea.core.api.dto.InteressatDto;
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
			InteressatDto interessat) {
		return delegate.create(entitatId, expedientId, interessat);
	}

	@Override
	@RolesAllowed("tothom")
	public void addToExpedient(
			Long entitatId,
			Long expedientId, Long id) {
		delegate.addToExpedient(entitatId, expedientId, id);
	}

	@Override
	@RolesAllowed("tothom")
	public void removeFromExpedient(
			Long entitatId,
			Long expedientId,
			Long id) {
		delegate.removeFromExpedient(entitatId, expedientId, id);
	}

	@Override
	@RolesAllowed("tothom")
	public InteressatDto findById(
			Long entitatId,
			Long id) {
		return delegate.findById(entitatId, id);
	}

	@Override
	@RolesAllowed("tothom")
	public List<InteressatDto> findByExpedient(
			Long entitatId,
			Long expedientId) {
		return delegate.findByExpedient(entitatId, expedientId);
	}

	@Override
	@RolesAllowed("tothom")
	public List<InteressatPersonaFisicaDto> findByFiltrePersonaFisica(
			Long entitatId,
			String documentNum,
			String nom,
			String llinatge1,
			String llinatge2) {
		return delegate.findByFiltrePersonaFisica(
				entitatId,
				documentNum,
				nom,
				llinatge1,
				llinatge2);
	}
	
	@Override
	@RolesAllowed("tothom")
	public List<InteressatPersonaJuridicaDto> findByFiltrePersonaJuridica(
			Long entitatId,
			String documentNum,
			String raoSocial) {
		return delegate.findByFiltrePersonaJuridica(
				entitatId, 
				documentNum, 
				raoSocial);
	}
	
	@Override
	@RolesAllowed("tothom")
	public List<InteressatAdministracioDto> findByFiltreAdministracio(
			Long entitatId,
			String organCodi) {
		return delegate.findByFiltreAdministracio(
				entitatId,
				organCodi);
	}

}
