/**
 * 
 */
package es.caib.ripea.core.ejb;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import es.caib.ripea.core.api.dto.ExecucioMassivaDto;
import es.caib.ripea.core.api.exception.ExecucioMassivaException;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.service.ExecucioMassivaService;

/**
 * Implementació de BustiaService com a EJB que empra una clase
 * delegada per accedir a la funcionalitat del servei.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class ExecucioMassivaServiceBean implements ExecucioMassivaService {

	@Autowired
	ExecucioMassivaService delegate;

	@Override
	@RolesAllowed("tothom")
	public void crearExecucioMassiva(ExecucioMassivaDto dto) throws NotFoundException, ValidationException {
		delegate.crearExecucioMassiva(dto);
	}

	@Override
	@RolesAllowed("tothom")
	public Long getExecucionsMassivesActiva(Long ultimaExecucioMassiva) {
		return delegate.getExecucionsMassivesActiva(ultimaExecucioMassiva);
	}

	@Override
	@RolesAllowed("tothom")
	public void executarExecucioMassiva(Long cmasiu_id) throws NotFoundException, ValidationException {
		delegate.executarExecucioMassiva(cmasiu_id);
	}

	@Override
	@RolesAllowed("tothom")
	public void generaInformeError(Long emc_id, String error) throws NotFoundException {
		delegate.generaInformeError(emc_id, error);
	}

	@Override
	@RolesAllowed("tothom")
	public void actualitzaUltimaOperacio(Long ome_id) throws NotFoundException, ExecucioMassivaException {
		delegate.actualitzaUltimaOperacio(ome_id);
	}
	
}
