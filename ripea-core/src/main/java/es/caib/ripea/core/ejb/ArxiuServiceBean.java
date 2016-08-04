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
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.exception.NotFoundException;
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
			ArxiuDto arxiu) {
		return delegate.create(entitatId, arxiu);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public ArxiuDto update(
			Long entitatId,
			ArxiuDto arxiu) {
		return delegate.update(entitatId, arxiu);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public ArxiuDto updateActiu(
			Long entitatId,
			Long id,
			boolean actiu) {
		return delegate.updateActiu(entitatId, id, actiu);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public ArxiuDto delete(
			Long entitatId,
			Long id) {
		return delegate.delete(entitatId, id);
	}

	@Override
	@RolesAllowed({"IPA_ADMIN", "tothom"})
	public ArxiuDto findById(
			Long entitatId,
			Long id) {
		return delegate.findById(entitatId, id);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public List<ArxiuDto> findByUnitatCodiAdmin(
			Long entitatId,
			String unitatCodi) {
		return delegate.findByUnitatCodiAdmin(entitatId, unitatCodi);
	}

	@Override
	@RolesAllowed("tothom")
	public List<ArxiuDto> findByUnitatCodiUsuari(
			Long entitatId,
			String unitatCodi) {
		return delegate.findByUnitatCodiUsuari(entitatId, unitatCodi);
	}

	@Override
	@RolesAllowed("tothom")
	public List<ArxiuDto> findPermesosPerUsuariIMetaExpedient(Long entitatId, Long metaExpedientId)
			throws NotFoundException {
		return delegate.findPermesosPerUsuariIMetaExpedient(entitatId, metaExpedientId);
	}

	@Override
	@RolesAllowed("tothom")
	public List<ArxiuDto> findPermesosPerUsuari(Long entitatId)
			throws NotFoundException {
		return delegate.findPermesosPerUsuari(entitatId);
	}

	@Override
	@RolesAllowed("tothom")
	public ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzativesUser(
			Long entitatId) {
		return delegate.findArbreUnitatsOrganitzativesUser(entitatId);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public void addMetaExpedient(
			Long entitatId, 
			Long id, 
			Long metaExpedientId) {
		delegate.addMetaExpedient(entitatId, id, metaExpedientId);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public void removeMetaExpedient(
			Long entitatId, 
			Long id, 
			Long metaExpedientId) {
		delegate.removeMetaExpedient(entitatId, id, metaExpedientId);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzativesAdmin(
			Long entitatId,
			boolean nomesAmbArxiusPermesos,
			boolean nomesQueContinguinArxius) throws NotFoundException {
		return delegate.findArbreUnitatsOrganitzativesAdmin(entitatId, nomesAmbArxiusPermesos,nomesQueContinguinArxius);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public List<ArxiuDto> findAmbMetaExpedient(
			Long entitatId,
			Long metaExpedientId) throws NotFoundException {
		return delegate.findAmbMetaExpedient(
				entitatId,
				metaExpedientId);
	}

	@Override
	@RolesAllowed("tothom")
	public List<ArxiuDto> findAmbMetaExpedientPerCreacio(
			Long entitatId, 
			Long metaExpedientId) {
		return delegate.findAmbMetaExpedientPerCreacio(
				entitatId,
				metaExpedientId);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public List<ArxiuDto> findActiusAmbEntitat(
			Long entitatId) throws NotFoundException {
		return delegate.findActiusAmbEntitat(entitatId);
	}

}
