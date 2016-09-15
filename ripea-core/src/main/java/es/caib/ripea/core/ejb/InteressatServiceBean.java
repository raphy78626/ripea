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
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
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
	public InteressatDto create(
			Long entitatId,
			Long expedientId,
			Long interessatId,
			InteressatDto representant) {
		return delegate.create(entitatId, expedientId, interessatId, representant);
	}

	@Override
	@RolesAllowed("tothom")
	public InteressatDto update(
			Long entitatId,
			Long expedientId,
			InteressatDto interessat) {
		return delegate.update(entitatId, expedientId, interessat);
	}
	
	@Override
	@RolesAllowed("tothom")
	public InteressatDto update(
			Long entitatId,
			Long expedientId,
			Long interessatId,
			InteressatDto representant) {
		return delegate.update(entitatId, expedientId, interessatId, representant);
	}
	
	@Override
	@RolesAllowed("tothom")
	public void delete(
			Long entitatId,
			Long expedientId,
			Long interessatId) {
		delegate.delete(entitatId, expedientId, interessatId);
	}
	
	@Override
	@RolesAllowed("tothom")
	public void delete(
			Long entitatId,
			Long expedientId,
			Long interessatId,
			Long representantId) {
		delegate.delete(entitatId, expedientId, interessatId, representantId);
	}
	
	@Override
	@RolesAllowed("tothom")
	public InteressatDto findById(
			Long id) {
		return delegate.findById(id);
	}

	@Override
	@RolesAllowed("tothom")
	public InteressatDto findRepresentantById(
			Long interessatId, 
			Long id) {
		return delegate.findRepresentantById(interessatId, id);
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
	public Long countByExpedient(
			Long entitatId,
			Long expedientId) {
		return delegate.countByExpedient(entitatId, expedientId);
	}

	@Override
	@RolesAllowed("tothom")
	public List<InteressatPersonaFisicaDto> findByFiltrePersonaFisica(
			String documentNum,
			String nom,
			String llinatge1,
			String llinatge2) {
		return delegate.findByFiltrePersonaFisica(
				documentNum,
				nom,
				llinatge1,
				llinatge2);
	}
	
	@Override
	@RolesAllowed("tothom")
	public List<InteressatPersonaJuridicaDto> findByFiltrePersonaJuridica(
			String documentNum,
			String raoSocial) {
		return delegate.findByFiltrePersonaJuridica(
				documentNum, 
				raoSocial);
	}
	
	@Override
	@RolesAllowed("tothom")
	public List<InteressatAdministracioDto> findByFiltreAdministracio(
			String organCodi) {
		return delegate.findByFiltreAdministracio(
				organCodi);
	}

	@Override
	@RolesAllowed("tothom")
	public List<UnitatOrganitzativaDto> findUnitatsOrganitzativesByEntitat(String entitatCodi) {
		return delegate.findUnitatsOrganitzativesByEntitat(entitatCodi);
	}

	@Override
	@RolesAllowed("tothom")
	public UnitatOrganitzativaDto findUnitatsOrganitzativesByCodi(String codi) {
		return delegate.findUnitatsOrganitzativesByCodi(codi);
	}

}
