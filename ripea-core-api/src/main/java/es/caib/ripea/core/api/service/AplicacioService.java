/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.UsuariDto;
import es.caib.ripea.core.api.exception.UsuariNotFoundException;

/**
 * Declaració dels mètodes comuns de l'aplicació.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface AplicacioService {

	/**
	 * Obté la versió actual de l'aplicació.
	 * 
	 * @return La versió actual.
	 */
	@PreAuthorize("hasRole('ROLE_SUPER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public String getVersioActual();

	/**
	 * Processa l'autenticació d'un usuari.
	 * 
	 * @throws UsuariNotFoundException
	 *             Si l'usuari no es troba al plugin d'usuaris.
	 */
	@PreAuthorize("hasRole('ROLE_SUPER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public void processarAutenticacioUsuari() throws UsuariNotFoundException;

	/**
	 * Obté l'usuari actual.
	 * 
	 * @return L'usuari actual.
	 */
	@PreAuthorize("hasRole('ROLE_SUPER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public UsuariDto getUsuariActual();

	/**
	 * Obté un usuari donat el seu codi.
	 * 
	 * @param codi
	 *            codi de l'usuari a cercar.
	 * @return L'usuari obtingut o null si no s'ha trobat.
	 */
	@PreAuthorize("hasRole('ROLE_SUPER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public UsuariDto findUsuariAmbCodi(String codi);

	/**
	 * Consulta els usuaris donat un text.
	 * 
	 * @param text
	 *            text per a fer la consulta.
	 * @return La llista d'usuaris.
	 */
	@PreAuthorize("hasRole('ROLE_SUPER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public List<UsuariDto> findUsuariAmbText(String text);

}
