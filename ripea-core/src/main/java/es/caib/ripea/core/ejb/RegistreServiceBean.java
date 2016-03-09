/**
 * 
 */
package es.caib.ripea.core.ejb;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import es.caib.ripea.core.api.dto.RegistreAnotacioDto;
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
	@RolesAllowed("IPA_REGWS")
	public void create(
			Long entitatId,
			RegistreAnotacioDto registre) {
		delegate.create(entitatId, registre);
	}

	@Override
	@RolesAllowed("tothom")
	public void afegirAExpedient(
			Long entitatId,
			Long expedientId,
			Long registreId) {
		delegate.afegirAExpedient(entitatId, expedientId, registreId);
	}

	@Override
	public void rebutjar(
			Long entitatId,
			Long bustiaId,
			Long registreId,
			String motiu) {
		delegate.rebutjar(entitatId, bustiaId, registreId, motiu);
	}

}
