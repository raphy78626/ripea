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

import es.caib.ripea.core.api.dto.ArbreDto;
import es.caib.ripea.core.api.dto.ArxiuDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.exception.ArxiuNotFoundException;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.UnitatNotFoundException;
import es.caib.ripea.core.api.service.ArxiuService;

/**
 * Implementació de ArxiuService com a EJB que empra una clase
 * delegada per accedir a la funcionalitat del servei.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class ArxiuServiceBean implements ArxiuService {

	@Autowired
	ArxiuService delegate;



	@Override
	@RolesAllowed("IPA_ADMIN")
	public ArxiuDto create(
			Long entitatId,
			ArxiuDto arxiu) throws EntitatNotFoundException, UnitatNotFoundException {
		return delegate.create(entitatId, arxiu);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public ArxiuDto update(
			Long entitatId,
			ArxiuDto arxiu) throws EntitatNotFoundException, ArxiuNotFoundException {
		return delegate.update(entitatId, arxiu);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public ArxiuDto updateActiu(
			Long entitatId,
			Long id,
			boolean actiu) throws EntitatNotFoundException, ArxiuNotFoundException {
		return delegate.updateActiu(entitatId, id, actiu);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public ArxiuDto delete(
			Long entitatId,
			Long id) throws EntitatNotFoundException, ArxiuNotFoundException {
		return delegate.delete(entitatId, id);
	}

	@Override
	@RolesAllowed({"IPA_ADMIN", "tothom"})
	public ArxiuDto findById(
			Long entitatId,
			Long id) throws EntitatNotFoundException {
		return delegate.findById(entitatId, id);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public List<ArxiuDto> findByUnitatCodiAdmin(
			Long entitatId,
			String unitatCodi) throws EntitatNotFoundException, ArxiuNotFoundException {
		return delegate.findByUnitatCodiAdmin(entitatId, unitatCodi);
	}

	@Override
	@RolesAllowed("tothom")
	public List<ArxiuDto> findByUnitatCodiUsuari(
			Long entitatId,
			String unitatCodi) throws EntitatNotFoundException, ArxiuNotFoundException {
		return delegate.findByUnitatCodiUsuari(entitatId, unitatCodi);
	}

	@Override
	@RolesAllowed("tothom")
	public List<ArxiuDto> findPermesosPerUsuari(
			Long entitatId) throws EntitatNotFoundException {
		return delegate.findPermesosPerUsuari(entitatId);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public void updatePermis(
			Long entitatId,
			Long id,
			PermisDto permis) throws EntitatNotFoundException, ArxiuNotFoundException {
		delegate.updatePermis(entitatId, id, permis);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public void deletePermis(
			Long entitatId,
			Long id,
			Long permisId) throws EntitatNotFoundException, ArxiuNotFoundException {
		delegate.deletePermis(entitatId, id, permisId);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzativesAdmin(
			Long entitatId) throws EntitatNotFoundException {
		return delegate.findArbreUnitatsOrganitzativesAdmin(entitatId);
	}

	@Override
	@RolesAllowed("tothom")
	public ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzativesUser(
			Long entitatId) throws EntitatNotFoundException {
		return delegate.findArbreUnitatsOrganitzativesUser(entitatId);
	}

}
