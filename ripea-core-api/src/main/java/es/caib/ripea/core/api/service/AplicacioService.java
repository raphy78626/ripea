/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.ExcepcioLogDto;
import es.caib.ripea.core.api.dto.IntegracioAccioDto;
import es.caib.ripea.core.api.dto.IntegracioDto;
import es.caib.ripea.core.api.dto.UsuariDto;
import es.caib.ripea.core.api.exception.NotFoundException;

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
	@PreAuthorize("hasRole('IPA_SUPER') or hasRole('IPA_ADMIN') or hasRole('tothom')")
	public String getVersioActual();

	/**
	 * Processa l'autenticació d'un usuari.
	 * 
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'usuari amb el codi de l'usuari autenticat.
	 */
	@PreAuthorize("hasRole('IPA_SUPER') or hasRole('IPA_ADMIN') or hasRole('tothom')")
	public void processarAutenticacioUsuari() throws NotFoundException;

	/**
	 * Obté l'usuari actual.
	 * 
	 * @return L'usuari actual.
	 */
	@PreAuthorize("hasRole('IPA_SUPER') or hasRole('IPA_ADMIN') or hasRole('tothom')")
	public UsuariDto getUsuariActual();

	/**
	 * Obté un usuari donat el seu codi.
	 * 
	 * @param codi
	 *            Codi de l'usuari a cercar.
	 * @return L'usuari obtingut o null si no s'ha trobat.
	 */
	@PreAuthorize("hasRole('IPA_SUPER') or hasRole('IPA_ADMIN') or hasRole('tothom')")
	public UsuariDto findUsuariAmbCodi(String codi);

	/**
	 * Consulta els usuaris donat un text.
	 * 
	 * @param text
	 *            Text per a fer la consulta.
	 * @return La llista d'usuaris.
	 */
	@PreAuthorize("hasRole('IPA_SUPER') or hasRole('IPA_ADMIN') or hasRole('tothom')")
	public List<UsuariDto> findUsuariAmbText(String text);

	/**
	 * Obté les integracions disponibles.
	 * 
	 * @return La llista d'integracions.
	 */
	@PreAuthorize("hasRole('IPA_SUPER')")
	public List<IntegracioDto> integracioFindAll();

	/**
	 * Obté la llista de les darreres accions realitzades a una integració.
	 * 
	 * @param codi
	 *             Codi de la integració.
	 * @return La llista amb les darreres accions.
	 * @throws NotFoundException
	 *             Si no s'ha trobat la integració amb el codi especificat.
	 */
	@PreAuthorize("hasRole('IPA_SUPER')")
	public List<IntegracioAccioDto> integracioFindDarreresAccionsByCodi(
			String codi) throws NotFoundException;

	/**
	 * Emmagatzema una excepció llençada per un servei.
	 * 
	 * @param exception
	 *             L'excepció a emmagatzemar.
	 */
	public void excepcioSave(Throwable exception);

	/**
	 * Consulta la informació d'una excepció donat el seu índex.
	 * 
	 * @param index
	 *             L'index de l'excepció.
	 * @return L'excepció.
	 */
	@PreAuthorize("hasRole('IPA_SUPER')")
	public ExcepcioLogDto excepcioFindOne(Long index);

	/**
	 * Retorna una llista amb les darreres excepcions emmagatzemades.
	 * 
	 * @return La llista amb les darreres excepcions.
	 */
	@PreAuthorize("hasRole('IPA_SUPER')")
	public List<ExcepcioLogDto> excepcioFindAll();

	/**
	 * Retorna una llista amb els diferents rols els quals
	 * tenen assignat algun permis.
	 * 
	 * @return La llista amb els rols.
	 */
	public List<String> permisosFindRolsDistinctAll();

	/**
	 * Retorna el valor d'un paràmetre de configuració de l'aplicació.
	 * 
	 * @param property
	 *             El codi del paràmetre
	 * @return el valor del paràmetre
	 */
	@PreAuthorize("hasRole('IPA_SUPER') or hasRole('IPA_ADMIN') or hasRole('tothom')")
	public String propertyGet(String property);

	/**
	 * Retorna el valor d'un paràmetre de configuració de l'aplicació.
	 * 
	 * @param prefix
	 *             El prefix dels paràmetres a consultar
	 * @return un Map amb els paràmetres trobats.
	 */
	@PreAuthorize("hasRole('IPA_SUPER') or hasRole('IPA_ADMIN') or hasRole('tothom')")
	public Map<String, String> propertyFindByPrefix(String prefix);

}