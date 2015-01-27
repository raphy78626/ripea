/**
 * 
 */
package es.caib.ripea.core.audit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import es.caib.ripea.core.entity.UsuariEntity;
import es.caib.ripea.core.repository.UsuariRepository;

/**
 * Especifica els mètodes que s'han d'emprar per obtenir i modificar la
 * informació relativa a una entitat que està emmagatzemada a dins la base de
 * dades.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class BasicAuditorAware implements AuditorAware<UsuariEntity> {

	private static final BasicAuditorAware _inst = new BasicAuditorAware();

	public static BasicAuditorAware getInstance() {
		return _inst;
	}

	@Resource
	private UsuariRepository usuariRepository;

	@Override
	public UsuariEntity getCurrentAuditor() {
		String auditorActual = getCurrentUserName();
		LOGGER.debug("Obtenint l'usuari auditor per a l'usuari (codi=" + getCurrentUserName() + ")");
		if (auditorActual == null) {
			LOGGER.debug("Auditor actual: null");
			return null;
		} else {
			UsuariEntity usuari = usuariRepository.findOne(auditorActual);
			return usuari;
		}
	}

	private String getCurrentUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return (auth != null) ? auth.getName() : null;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(BasicAuditorAware.class);

}
