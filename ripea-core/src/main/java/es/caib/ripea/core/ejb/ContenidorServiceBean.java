/**
 * 
 */
package es.caib.ripea.core.ejb;

import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import es.caib.ripea.core.api.dto.ContenidorDto;
import es.caib.ripea.core.api.dto.ContenidorFiltreDto;
import es.caib.ripea.core.api.dto.ContenidorLogDto;
import es.caib.ripea.core.api.dto.ContenidorMovimentDto;
import es.caib.ripea.core.api.dto.DadaDto;
import es.caib.ripea.core.api.dto.EscriptoriDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.dto.ValidacioErrorDto;
import es.caib.ripea.core.api.exception.BustiaNotFoundException;
import es.caib.ripea.core.api.exception.ContenidorNomDuplicatException;
import es.caib.ripea.core.api.exception.ContenidorNotFoundException;
import es.caib.ripea.core.api.exception.DadaNotFoundException;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.MetaDadaNotFoundException;
import es.caib.ripea.core.api.exception.MultiplicitatException;
import es.caib.ripea.core.api.exception.UsuariNotFoundException;
import es.caib.ripea.core.api.service.ContenidorService;

/**
 * Implementació de ContenidorService com a EJB que empra una clase
 * delegada per accedir a la funcionalitat del servei.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class ContenidorServiceBean implements ContenidorService {

	@Autowired
	ContenidorService delegate;



	@Override
	@RolesAllowed("tothom")
	public ContenidorDto rename(
			Long entitatId,
			Long contenidorId,
			String nom) throws EntitatNotFoundException, ContenidorNotFoundException {
		return delegate.rename(
				entitatId,
				contenidorId,
				nom);
	}

	@Override
	@RolesAllowed("tothom")
	public DadaDto dadaCreate(
			Long entitatId,
			Long contenidorId,
			Long metaDadaId,
			Object valor) throws EntitatNotFoundException, ContenidorNotFoundException, MetaDadaNotFoundException, MultiplicitatException {
		return delegate.dadaCreate(
				entitatId,
				contenidorId,
				metaDadaId,
				valor);
	}

	@Override
	@RolesAllowed("tothom")
	public DadaDto dadaUpdate(
			Long entitatId,
			Long contenidorId,
			Long dadaId,
			Object valor) throws EntitatNotFoundException, ContenidorNotFoundException, DadaNotFoundException {
		return delegate.dadaUpdate(entitatId, contenidorId, dadaId, valor);
	}

	@Override
	@RolesAllowed("tothom")
	public DadaDto dadaDelete(
			Long entitatId,
			Long contenidorId,
			Long dadaId) throws EntitatNotFoundException, ContenidorNotFoundException, DadaNotFoundException {
		return delegate.dadaDelete(entitatId, contenidorId, dadaId);
	}

	@Override
	@RolesAllowed("tothom")
	public DadaDto dadaFindById(
			Long entitatId,
			Long contenidorId,
			Long dadaId) throws EntitatNotFoundException, ContenidorNotFoundException {
		return delegate.dadaFindById(entitatId, contenidorId, dadaId);
	}

	@Override
	@RolesAllowed("tothom")
	public ContenidorDto deleteReversible(
			Long entitatId,
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException {
		return delegate.deleteReversible(entitatId, contenidorId);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public ContenidorDto deleteDefinitiu(
			Long entitatId,
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException {
		return delegate.deleteDefinitiu(entitatId, contenidorId);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public ContenidorDto undelete(
			Long entitatId,
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException {
		return delegate.undelete(entitatId, contenidorId);
	}

	@Override
	@RolesAllowed("tothom")
	public ContenidorDto move(
			Long entitatId,
			Long contenidorOrigenId,
			Long contenidorDestiId) throws EntitatNotFoundException, ContenidorNotFoundException, ContenidorNomDuplicatException {
		return delegate.move(entitatId, contenidorOrigenId, contenidorDestiId);
	}

	@Override
	@RolesAllowed("tothom")
	public ContenidorDto copy(
			Long entitatId,
			Long contenidorOrigenId,
			Long contenidorDestiId,
			boolean recursiu) throws EntitatNotFoundException, ContenidorNotFoundException, ContenidorNomDuplicatException {
		return delegate.copy(entitatId, contenidorOrigenId, contenidorDestiId, recursiu);
	}

	@Override
	@RolesAllowed("tothom")
	public ContenidorDto send(
			Long entitatId,
			Long contenidorId,
			Long bustiaId,
			String comentari) throws EntitatNotFoundException, ContenidorNotFoundException, BustiaNotFoundException {
		return delegate.send(entitatId, contenidorId, bustiaId, comentari);
	}

	@Override
	@RolesAllowed("tothom")
	public ContenidorDto receive(
			Long entitatId,
			Long bustiaId,
			Long contenidorOrigenId,
			Long contenidorDestiId) throws EntitatNotFoundException, BustiaNotFoundException, ContenidorNotFoundException {
		return delegate.receive(
				entitatId,
				bustiaId,
				contenidorOrigenId,
				contenidorDestiId);
	}

	@Override
	@RolesAllowed("tothom")
	public EscriptoriDto getEscriptoriPerUsuariActual(
			Long entitatId) throws EntitatNotFoundException, UsuariNotFoundException {
		return delegate.getEscriptoriPerUsuariActual(entitatId);
	}

	@Override
	@RolesAllowed("tothom")
	public ContenidorDto getContenidorSenseContingut(
			Long entitatId,
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException {
		return delegate.getContenidorSenseContingut(entitatId, contenidorId);
	}

	@Override
	@RolesAllowed("tothom")
	public ContenidorDto getContenidorAmbContingut(
			Long entitatId,
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException {
		return delegate.getContenidorAmbContingut(entitatId, contenidorId);
	}

	@Override
	@RolesAllowed("tothom")
	public ContenidorDto getContenidorAmbContingutPerPath(
			Long entitatId,
			String path) throws EntitatNotFoundException, ContenidorNotFoundException {
		return delegate.getContenidorAmbContingutPerPath(entitatId, path);
	}

	@Override
	@RolesAllowed("tothom")
	public List<ValidacioErrorDto> findErrorsValidacio(
			Long entitatId,
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException {
		return delegate.findErrorsValidacio(entitatId, contenidorId);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public List<ContenidorLogDto> findLogsPerContenidorAdmin(
			Long entitatId,
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException {
		return delegate.findLogsPerContenidorAdmin(entitatId, contenidorId);
	}

	@Override
	@RolesAllowed("tothom")
	public List<ContenidorLogDto> findLogsPerContenidorUser(
			Long entitatId,
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException {
		return delegate.findLogsPerContenidorUser(entitatId, contenidorId);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public List<ContenidorMovimentDto> findMovimentsPerContenidorAdmin(
			Long entitatId,
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException {
		return delegate.findMovimentsPerContenidorAdmin(entitatId, contenidorId);
	}

	@Override
	@RolesAllowed("tothom")
	public List<ContenidorMovimentDto> findMovimentsPerContenidorUser(
			Long entitatId,
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException {
		return delegate.findMovimentsPerContenidorUser(entitatId, contenidorId);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public PaginaDto<ContenidorDto> findAdmin(
			Long entitatId,
			ContenidorFiltreDto filtre,
			PaginacioParamsDto paginacioParams) throws EntitatNotFoundException {
		return delegate.findAdmin(
				entitatId,
				filtre,
				paginacioParams);
	}

	@Override
	@RolesAllowed("IPA_ADMIN")
	public PaginaDto<ContenidorDto> findEsborrats(
			Long entitatId,
			String nom,
			String usuariCodi,
			Date dataInici,
			Date dataFi,
			PaginacioParamsDto paginacioParams) throws EntitatNotFoundException, ContenidorNotFoundException {
		return delegate.findEsborrats(
				entitatId,
				nom,
				usuariCodi,
				dataInici,
				dataFi,
				paginacioParams);
	}

}
