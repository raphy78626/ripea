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
import es.caib.ripea.core.api.dto.BustiaContingutPendentDto;
import es.caib.ripea.core.api.dto.BustiaDto;
import es.caib.ripea.core.api.dto.ContingutDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.registre.RegistreAnotacio;
import es.caib.ripea.core.api.registre.RegistreTipusEnum;
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
	public List<BustiaDto> findAmbUnitatCodiAdmin(
			Long entitatId,
			String unitatCodi) {
		return delegate.findAmbUnitatCodiAdmin(entitatId, unitatCodi);
	}

	@Override
	@RolesAllowed({"IPA_ADMIN", "tothom"})
	public List<BustiaDto> findActivesAmbEntitat(
			Long entitatId) {
		return delegate.findActivesAmbEntitat(entitatId);
	}

	@Override
	@RolesAllowed("tothom")
	public PaginaDto<BustiaDto> findPermesesPerUsuari(
			Long entitatId,
			PaginacioParamsDto paginacioParams) {
		return delegate.findPermesesPerUsuari(
				entitatId,
				paginacioParams);
	}

	@Override
	@RolesAllowed("tothom")
	public ContingutDto enviarContingut(
			Long entitatId,
			Long bustiaId,
			Long contingutId,
			String comentari) {
		return delegate.enviarContingut(
				entitatId,
				bustiaId,
				contingutId,
				comentari);
	}

	@Override
	@RolesAllowed("IPA_BSTWS")
	public void registreAnotacioCrear(
			String entitatCodi,
			RegistreTipusEnum tipus,
			String unitatAdministrativa,
			RegistreAnotacio anotacio) {
		delegate.registreAnotacioCrear(
				entitatCodi,
				tipus,
				unitatAdministrativa,
				anotacio);
	}

	@Override
	@RolesAllowed("IPA_BSTWS")
	public void registreAnotacioCrear(
			String entitatCodi,
			String referencia) {
		delegate.registreAnotacioCrear(
				entitatCodi,
				referencia);
	}

	@Override
	@RolesAllowed("tothom")
	public PaginaDto<BustiaContingutPendentDto> contingutPendentFindByBustiaId(
			Long entitatId,
			Long bustiaId,
			PaginacioParamsDto paginacioParams) {
		return delegate.contingutPendentFindByBustiaId(
				entitatId,
				bustiaId,
				paginacioParams);
	}

	@Override
	@RolesAllowed("tothom")
	public BustiaContingutPendentDto contingutPendentFindOne(
			Long entitatId,
			Long bustiaId,
			Long contingutId) {
		return delegate.contingutPendentFindOne(
				entitatId,
				bustiaId,
				contingutId);
	}

	@Override
	@RolesAllowed("tothom")
	public long contingutPendentBustiesAllCount(
			Long entitatId) {
		return delegate.contingutPendentBustiesAllCount(entitatId);
	}

	@Override
	@RolesAllowed("tothom")
	public void contingutPendentReenviar(
			Long entitatId,
			Long bustiaOrigenId,
			Long bustiaDestiId,
			Long contingutId,
			String comentari) throws NotFoundException {
		delegate.contingutPendentReenviar(
				entitatId,
				bustiaOrigenId,
				bustiaDestiId,
				contingutId,
				comentari);
	}

	@Override
	@RolesAllowed({"IPA_ADMIN", "tothom"})
	public ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzatives(
			Long entitatId,
			boolean nomesBusties,
			boolean nomesBustiesPermeses,
			boolean comptarElementsPendents) {
		return delegate.findArbreUnitatsOrganitzatives(
				entitatId,
				nomesBusties,
				nomesBustiesPermeses,
				comptarElementsPendents);
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

}
