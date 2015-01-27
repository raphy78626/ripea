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
import es.caib.ripea.core.api.exception.BustiaNotFoundException;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.ExpedientNotFoundException;
import es.caib.ripea.core.api.exception.RegistreNotFoundException;
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
			RegistreAnotacioDto registre) throws EntitatNotFoundException, BustiaNotFoundException {
		delegate.create(entitatId, registre);
	}

	@Override
	@RolesAllowed("tothom")
	public void afegirAExpedient(
			Long entitatId,
			Long expedientId,
			Long registreId) throws EntitatNotFoundException, ExpedientNotFoundException, RegistreNotFoundException {
		delegate.afegirAExpedient(entitatId, expedientId, registreId);
	}

	@Override
	public void rebutjar(
			Long entitatId,
			Long bustiaId,
			Long registreId,
			String motiu) throws EntitatNotFoundException, BustiaNotFoundException, RegistreNotFoundException {
		delegate.rebutjar(entitatId, bustiaId, registreId, motiu);
	}

}
