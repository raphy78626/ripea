/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.entity.UsuariEntity;
import es.caib.ripea.core.repository.UsuariRepository;


/**
 * Helper per a operacions amb usuaris.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class UsuariHelper {
 
	@Resource
	private UsuariRepository usuariRepository;



	public Authentication generarUsuariAutenticat(
			String usuariCodi,
			boolean establirComAUsuariActual) {
		List<GrantedAuthority> authorities = null;
		Authentication auth = new DadesUsuariAuthenticationToken(
				usuariCodi,
			authorities);
		if (establirComAUsuariActual)
			SecurityContextHolder.getContext().setAuthentication(auth);
		return auth;
	}

	public class DadesUsuariAuthenticationToken extends AbstractAuthenticationToken {
		String principal;
		public DadesUsuariAuthenticationToken(
				String usuariCodi,
				Collection<GrantedAuthority> authorities) {
			super(authorities);
			principal = usuariCodi;
		}
		@Override
		public Object getCredentials() {
			return principal;
		}
		@Override
		public Object getPrincipal() {
			return principal;
		}
		private static final long serialVersionUID = 5974089352023050267L;
	}

	public UsuariEntity getUsuariAutenticat() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null)
			return null;
		UsuariEntity usuari = usuariRepository.findOne(auth.getName());
		if (usuari == null) {
			logger.error("No s'ha trobat l'usuari (codi=" + auth.getName() + ")");
			throw new NotFoundException(
					auth.getName(),
					UsuariEntity.class);
		}
		return usuari;
	}



	private static final Logger logger = LoggerFactory.getLogger(UsuariHelper.class);

}
