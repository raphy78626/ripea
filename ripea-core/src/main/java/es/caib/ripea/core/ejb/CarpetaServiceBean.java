/**
 * 
 */
package es.caib.ripea.core.ejb;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import es.caib.ripea.core.api.dto.CarpetaDto;
import es.caib.ripea.core.api.dto.CarpetaTipusEnumDto;
import es.caib.ripea.core.api.exception.CarpetaNotFoundException;
import es.caib.ripea.core.api.exception.ContenidorNotFoundException;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.NomInvalidException;
import es.caib.ripea.core.api.service.CarpetaService;

/**
 * Implementació de ContenidorService com a EJB que empra una clase
 * delegada per accedir a la funcionalitat del servei.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class CarpetaServiceBean implements CarpetaService {

	@Autowired
	CarpetaService delegate;



	@Override
	@RolesAllowed("tothom")
	public CarpetaDto create(
			Long entitatId,
			Long contenidorId,
			String nom,
			CarpetaTipusEnumDto tipus) throws EntitatNotFoundException, ContenidorNotFoundException, NomInvalidException {
		return delegate.create(
				entitatId,
				contenidorId,
				nom,
				tipus);
	}

	@Override
	@RolesAllowed("tothom")
	public CarpetaDto update(
			Long entitatId,
			Long id,
			String nom,
			CarpetaTipusEnumDto tipus) throws EntitatNotFoundException, CarpetaNotFoundException, NomInvalidException {
		return delegate.update(
				entitatId,
				id,
				nom,
				tipus);
	}

	@Override
	@RolesAllowed("tothom")
	public CarpetaDto delete(
			Long entitatId,
			Long id) throws EntitatNotFoundException, CarpetaNotFoundException {
		return delegate.delete(entitatId, id);
	}

	@Override
	@RolesAllowed("tothom")
	public CarpetaDto findById(
			Long entitatId,
			Long id) throws EntitatNotFoundException {
		return delegate.findById(entitatId, id);
	}

}
