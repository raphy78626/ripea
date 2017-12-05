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

import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.RegistreAnnexDetallDto;
import es.caib.ripea.core.api.dto.RegistreAnotacioDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.registre.RegistreProcesEstatEnum;
import es.caib.ripea.core.api.registre.RegistreProcesEstatSistraEnum;
import es.caib.ripea.core.api.service.RegistreService;

/**
 * Implementació de RegistreService com a EJB que empra una clase
 * delegada per accedir a la funcionalitat del servei.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class RegistreServiceBean implements RegistreService {

	@Autowired
	RegistreService delegate;



	@Override
	@RolesAllowed("tothom")
	public RegistreAnotacioDto findOne(
			Long entitatId,
			Long contenidorId,
			Long registreId) {
		return delegate.findOne(
				entitatId,
				contenidorId,
				registreId);
	}

	@Override
	@RolesAllowed("tothom")
	public void rebutjar(
			Long entitatId,
			Long bustiaId,
			Long registreId,
			String motiu) {
		delegate.rebutjar(entitatId, bustiaId, registreId, motiu);
	}

	@Override
	public void reglaAplicarPendents() {
		delegate.reglaAplicarPendents();
	}

	@Override
	public void reglaAplicarPendentsBackofficeSistra() {
		delegate.reglaAplicarPendents();
	}
	
	@Override
	@RolesAllowed("IPA_ADMIN")
	public boolean reglaReintentarAdmin(
			Long entitatId,
			Long bustiaId,
			Long registreId) {
		return delegate.reglaReintentarAdmin(
				entitatId,
				bustiaId,
				registreId);
	}

	@Override
	@RolesAllowed("tothom")
	public boolean reglaReintentarUser(
			Long entitatId,
			Long bustiaId,
			Long registreId) {
		return delegate.reglaReintentarUser(
				entitatId,
				bustiaId,
				registreId);
	}

	@Override
	@RolesAllowed("tothom")
	public List<RegistreAnnexDetallDto> getAnnexosAmbArxiu(
			Long entitatId, 
			Long contingutId, 
			Long registreId)
			throws NotFoundException {
		return delegate.getAnnexosAmbArxiu(
				entitatId, 
				contingutId, 
				registreId);
	}

	@Override
	@RolesAllowed("tothom")
	public FitxerDto getArxiuAnnex(Long annexId) throws NotFoundException {
		return delegate.getArxiuAnnex(annexId);
	}
	
	@Override
	@RolesAllowed("tothom")
	public FitxerDto getAnnexFirmaContingut(Long annexId,
			int indexFirma) throws NotFoundException {
		return delegate.getAnnexFirmaContingut(annexId, indexFirma);
	}

	@Override
	@RolesAllowed("tothom")
	public RegistreAnotacioDto findAmbIdentificador(String identificador) {
		return delegate.findAmbIdentificador(identificador);
	}

	@Override
	@RolesAllowed("tothom")
	public void updateProces(Long registreId, RegistreProcesEstatEnum procesEstat,
			RegistreProcesEstatSistraEnum procesEstatSistra, String resultadoProcesamiento) {
		delegate.updateProces(registreId, procesEstat, procesEstatSistra, resultadoProcesamiento);
	}

	@Override
	@RolesAllowed("tothom")
	public List<String> findPerBackofficeSistra(String identificadorProcediment, String identificadorTramit,
			RegistreProcesEstatSistraEnum procesEstatSistra, Date desdeDate, Date finsDate) {
		return delegate.findPerBackofficeSistra(identificadorProcediment, identificadorTramit, procesEstatSistra, desdeDate, finsDate);
	}

}
