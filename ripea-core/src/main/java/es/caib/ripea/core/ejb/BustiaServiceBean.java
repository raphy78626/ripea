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
import es.caib.ripea.core.api.dto.BustiaDto;
import es.caib.ripea.core.api.dto.ContenidorDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.dto.RegistreAnotacioDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.service.BustiaService;

/**
 * Implementació de BustiaService com a EJB que empra una clase
 * delegada per accedir a la funcionalitat del servei.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class BustiaServiceBean implements BustiaService {

	@Autowired
	BustiaService delegate;



	@Override
	@RolesAllowed("IPA_ADMIN")
	public BustiaDto create(
			Long entitatId,
			BustiaDto bustia) {
		return delegate.create(entitatId, bustia);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public BustiaDto update(
			Long entitatId,
			BustiaDto bustia) {
		return delegate.update(entitatId, bustia);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public BustiaDto updateActiva(
			Long entitatId,
			Long id,
			boolean activa) {
		return delegate.updateActiva(entitatId, id, activa);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public BustiaDto delete(
			Long entitatId,
			Long id) {
		return delegate.delete(entitatId, id);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public BustiaDto marcarPerDefecte(
			Long entitatId,
			Long id) {
		return delegate.marcarPerDefecte(entitatId, id);
	}

	@Override
	@RolesAllowed({"IPA_ADMIN", "tothom"})
	public BustiaDto findById(
			Long entitatId,
			Long id) {
		return delegate.findById(entitatId, id);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public List<BustiaDto> findByUnitatCodiAdmin(
			Long entitatId,
			String unitatCodi) {
		return delegate.findByUnitatCodiAdmin(entitatId, unitatCodi);
	}

	@Override
	@RolesAllowed("tothom")
	public List<BustiaDto> findByUnitatCodiUsuari(
			Long entitatId,
			String unitatCodi) {
		return delegate.findByUnitatCodiUsuari(entitatId, unitatCodi);
	}

	@Override
	@RolesAllowed("tothom")
	public List<BustiaDto> findByEntitatAndActivaTrue(
			Long entitatId) {
		return delegate.findByEntitatAndActivaTrue(entitatId);
	}

	@Override
	@RolesAllowed("tothom")
	public List<BustiaDto> findPermesesPerUsuari(
			Long entitatId) {
		return delegate.findPermesesPerUsuari(entitatId);
	}

	@Override
	@RolesAllowed("tothom")
	public List<ContenidorDto> findContingutPendent(
			Long entitatId,
			Long id) {
		return delegate.findContingutPendent(entitatId, id);
	}

	@Override
	@RolesAllowed("tothom")
	public List<RegistreAnotacioDto> findRegistrePendent(
			Long entitatId,
			Long id) {
		return delegate.findRegistrePendent(entitatId, id);
	}

	@Override
	@RolesAllowed("tothom")
	public RegistreAnotacioDto findOneRegistrePendent(
			Long entitatId,
			Long id,
			Long registreId) {
		return delegate.findOneRegistrePendent(
				entitatId,
				id,
				registreId);
	}

	@Override
	@RolesAllowed("tothom")
	public long countElementsPendentsBustiesAll(
			Long entitatId) {
		return delegate.countElementsPendentsBustiesAll(entitatId);
	}

	@Override
	@RolesAllowed("tothom")
	public void refreshCountElementsPendentsBustiesAll(
			Long entitatId) {
		delegate.refreshCountElementsPendentsBustiesAll(entitatId);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public void updatePermis(
			Long entitatId,
			Long id,
			PermisDto permis) {
		delegate.updatePermis(entitatId, id, permis);
	}	

	@Override
	@RolesAllowed("IPA_ADMIN")
	public void deletePermis(
			Long entitatId,
			Long id,
			Long permisId) {
		delegate.deletePermis(entitatId, id, permisId);
	}

	@Override
	@RolesAllowed({"IPA_ADMIN", "tothom"})
	public ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzatives(
			Long entitatId,
			boolean nomesBustiesPermeses,
			boolean comptarElementsPendents) {
		return delegate.findArbreUnitatsOrganitzatives(
				entitatId,
				nomesBustiesPermeses,
				comptarElementsPendents);
	}

	@Override
	@RolesAllowed("tothom")
	public void forwardContenidor(
			Long entitatId,
			Long id,
			Long contenidorId,
			Long bustiaDestiId,
			String comentari) {
		delegate.forwardContenidor(
				entitatId,
				id,
				contenidorId,
				bustiaDestiId,
				comentari);
	}

	@Override
	@RolesAllowed("tothom")
	public void forwardRegistre(
			Long entitatId,
			Long id,
			Long registreId,
			Long bustiaDestiId,
			String comentari) {
		delegate.forwardRegistre(
				entitatId,
				id,
				registreId,
				bustiaDestiId,
				comentari);
	}

}
