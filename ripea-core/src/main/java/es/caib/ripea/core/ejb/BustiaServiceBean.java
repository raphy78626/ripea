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
import es.caib.ripea.core.api.exception.BustiaNotFoundException;
import es.caib.ripea.core.api.exception.ContenidorNotFoundException;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.UnitatNotFoundException;
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
			BustiaDto bustia) throws EntitatNotFoundException, UnitatNotFoundException {
		return delegate.create(entitatId, bustia);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public BustiaDto update(
			Long entitatId,
			BustiaDto bustia) throws EntitatNotFoundException, BustiaNotFoundException {
		return delegate.update(entitatId, bustia);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public BustiaDto updateActiva(
			Long entitatId,
			Long id,
			boolean activa) throws EntitatNotFoundException, BustiaNotFoundException {
		return delegate.updateActiva(entitatId, id, activa);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public BustiaDto delete(
			Long entitatId,
			Long id) throws EntitatNotFoundException, BustiaNotFoundException {
		return delegate.delete(entitatId, id);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public BustiaDto marcarPerDefecte(
			Long entitatId,
			Long id) throws EntitatNotFoundException, BustiaNotFoundException {
		return delegate.marcarPerDefecte(entitatId, id);
	}

	@Override
	@RolesAllowed({"IPA_ADMIN", "tothom"})
	public BustiaDto findById(
			Long entitatId,
			Long id) throws EntitatNotFoundException {
		return delegate.findById(entitatId, id);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public List<BustiaDto> findByUnitatCodiAdmin(
			Long entitatId,
			String unitatCodi) throws EntitatNotFoundException, UnitatNotFoundException {
		return delegate.findByUnitatCodiAdmin(entitatId, unitatCodi);
	}

	@Override
	@RolesAllowed("tothom")
	public List<BustiaDto> findByUnitatCodiUsuari(
			Long entitatId,
			String unitatCodi) throws EntitatNotFoundException, UnitatNotFoundException {
		return delegate.findByUnitatCodiUsuari(entitatId, unitatCodi);
	}

	@Override
	@RolesAllowed("tothom")
	public List<BustiaDto> findByEntitatAndActivaTrue(
			Long entitatId) throws EntitatNotFoundException {
		return delegate.findByEntitatAndActivaTrue(entitatId);
	}

	@Override
	@RolesAllowed("tothom")
	public List<BustiaDto> findPermesesPerUsuari(
			Long entitatId) throws EntitatNotFoundException {
		return delegate.findPermesesPerUsuari(entitatId);
	}

	@Override
	@RolesAllowed("tothom")
	public List<ContenidorDto> findContingutPendent(
			Long entitatId,
			Long id) throws EntitatNotFoundException, BustiaNotFoundException {
		return delegate.findContingutPendent(entitatId, id);
	}

	@Override
	@RolesAllowed("tothom")
	public List<RegistreAnotacioDto> findRegistrePendent(
			Long entitatId,
			Long id) throws EntitatNotFoundException, BustiaNotFoundException {
		return delegate.findRegistrePendent(entitatId, id);
	}

	@Override
	@RolesAllowed("tothom")
	public RegistreAnotacioDto findOneRegistrePendent(
			Long entitatId,
			Long id,
			Long registreId) throws EntitatNotFoundException, BustiaNotFoundException {
		return delegate.findOneRegistrePendent(
				entitatId,
				id,
				registreId);
	}

	@Override
	@RolesAllowed("tothom")
	public long countElementsPendentsBustiesAll(
			Long entitatId) throws EntitatNotFoundException {
		return delegate.countElementsPendentsBustiesAll(entitatId);
	}

	@Override
	@RolesAllowed("tothom")
	public void refreshCountElementsPendentsBustiesAll(
			Long entitatId) throws EntitatNotFoundException {
		delegate.refreshCountElementsPendentsBustiesAll(entitatId);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public void updatePermis(
			Long entitatId,
			Long id,
			PermisDto permis) throws EntitatNotFoundException, BustiaNotFoundException {
		delegate.updatePermis(entitatId, id, permis);
	}	

	@Override
	@RolesAllowed("IPA_ADMIN")
	public void deletePermis(
			Long entitatId,
			Long id,
			Long permisId) throws EntitatNotFoundException, BustiaNotFoundException {
		delegate.deletePermis(entitatId, id, permisId);
	}

	@Override
	@RolesAllowed({"IPA_ADMIN", "tothom"})
	public ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzatives(
			Long entitatId,
			boolean nomesBustiesPermeses,
			boolean comptarElementsPendents) throws EntitatNotFoundException {
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
			String comentari) throws EntitatNotFoundException, BustiaNotFoundException, ContenidorNotFoundException {
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
			String comentari) throws EntitatNotFoundException, BustiaNotFoundException, ContenidorNotFoundException {
		delegate.forwardRegistre(
				entitatId,
				id,
				registreId,
				bustiaDestiId,
				comentari);
	}

}
