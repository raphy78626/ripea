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
	
}
