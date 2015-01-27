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

import es.caib.ripea.core.api.dto.IntegracioAccioDto;
import es.caib.ripea.core.api.dto.IntegracioDto;
import es.caib.ripea.core.api.exception.IntegracioNotFoundException;
import es.caib.ripea.core.api.service.IntegracioService;

/**
 * Implementació de EntitatService com a EJB que empra una clase
 * delegada per accedir a la funcionalitat del servei.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class IntegracioServiceBean implements IntegracioService {

	@Autowired
	IntegracioService delegate;



	@Override
	@RolesAllowed("IPA_SUPER")
	public List<IntegracioDto> findAll() {
		return delegate.findAll();
	}

	@Override
	@RolesAllowed("IPA_SUPER")
	public List<IntegracioAccioDto> findDarreresAccionsByIntegracio(
			String codi) throws IntegracioNotFoundException {
		return delegate.findDarreresAccionsByIntegracio(codi);
	}

}
