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

import es.caib.ripea.core.api.dto.UsuariDto;
import es.caib.ripea.core.api.service.AplicacioService;

/**
 * Implementació de AplicacioService com a EJB que empra una clase
 * delegada per accedir a la funcionalitat del servei.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class AplicacioServiceBean implements AplicacioService {

	@Autowired
	AplicacioService delegate;



	@Override
	@RolesAllowed({"IPA_SUPER", "IPA_ADMIN", "tothom"})
	public String getVersioActual() {
		return delegate.getVersioActual();
	}
	
	@Override
	@RolesAllowed({"IPA_SUPER", "IPA_ADMIN", "tothom"})
	public void processarAutenticacioUsuari() {
		delegate.processarAutenticacioUsuari();
	}

	@Override
	@RolesAllowed({"IPA_SUPER", "IPA_ADMIN", "tothom"})
	public UsuariDto getUsuariActual() {
		return delegate.getUsuariActual();
	}

	@Override
	@RolesAllowed({"IPA_SUPER", "IPA_ADMIN", "tothom"})
	public UsuariDto findUsuariAmbCodi(String codi) {
		return delegate.findUsuariAmbCodi(codi);
	}

	@Override
	@RolesAllowed({"IPA_SUPER", "IPA_ADMIN", "tothom"})
	public List<UsuariDto> findUsuariAmbText(String text) {
		return delegate.findUsuariAmbText(text);
	}

}
