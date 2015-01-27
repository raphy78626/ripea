/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;

/**
 * Declaració dels mètodes per a la gestió d'entitats.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface EntitatService {

	/**
	 * Crea una nova entitat.
	 * 
	 * @param entitat
	 *            Informació de l'entitat a crear.
	 * @return L'Entitat creada.
	 */
	@PreAuthorize("hasRole('ROLE_SUPER')")
	public EntitatDto create(EntitatDto entitat);

	/**
	 * Actualitza la informació de l'entitat que tengui el mateix
	 * id que l'especificat per paràmetre.
	 * 
	 * @param entitat
	 *            Informació de l'entitat a modificar.
	 * @return L'entitat modificada.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_SUPER')")
	public EntitatDto update(
			EntitatDto entitat) throws EntitatNotFoundException;

	/**
	 * Marca l'entitat amb l'id especificat com a activa/inactiva.
	 * 
	 * @param id
	 *            Atribut id de l'entitat a esborrar.
	 * @param activa
	 *            true si es vol activar o false en cas contrari.
	 * @return L'entitat modificada.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_SUPER')")
	public EntitatDto updateActiva(
			Long id,
			boolean activa) throws EntitatNotFoundException;

	/**
	 * Esborra l'entitat amb el mateix id que l'especificat.
	 * 
	 * @param id
	 *            Atribut id de l'entitat a esborrar.
	 * @return L'entitat esborrada.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap Entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_SUPER')")
	public EntitatDto delete(
			Long id) throws EntitatNotFoundException;

	/**
	 * Consulta una entitat donat el seu id.
	 * 
	 * @param id
	 *            Atribut id de l'entitat a trobar.
	 * @return L'entitat amb l'id especificat o null si no s'ha trobat.
	 */
	@PreAuthorize("hasRole('ROLE_SUPER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public EntitatDto findById(Long id);

	/**
	 * Consulta una entitat donat el seu codi.
	 * 
	 * @param codi
	 *            Atribut codi de l'entitat a trobar.
	 * @return L'entitat amb el codi especificat o null si no s'ha trobat.
	 */
	@PreAuthorize("hasRole('ROLE_SUPER')")
	public EntitatDto findByCodi(String codi);

	/**
	 * Llistat amb totes les entitats paginades.
	 * 
	 * @param paginacioParams
	 *            Paràmetres per a dur a terme la paginació del resultats.
	 * @return La pàgina d'Entitats.
	 */
	@PreAuthorize("hasRole('ROLE_SUPER')")
	public PaginaDto<EntitatDto> findAllPaginat(PaginacioParamsDto paginacioParams);

	/**
	 * Llistat amb les entitats accessibles per a l'usuari actual.
	 * 
	 * @return El llistat d'entitats.
	 */
	@PreAuthorize("hasRole('ROLE_SUPER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public List<EntitatDto> findAccessiblesUsuariActual();

	/**
	 * Consulta els permisos de l'entitat com a superusuari.
	 * 
	 * @param id
	 *            Atribut id de l'entitat a la qual es volen consultar els permisos.
	 * @return El llistat de permisos.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_SUPER')")
	public List<PermisDto> findPermisSuper(
			Long id) throws EntitatNotFoundException;

	/**
	 * Modifica els permisos d'un usuari o d'un rol per a una entitat com a
	 * superusuari.
	 * 
	 * @param id
	 *            Atribut id de l'entitat de la qual es modificar el permís.
	 * @param permis
	 *            El permís que es vol modificar.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_SUPER')")
	public void updatePermisSuper(
			Long id,
			PermisDto permis) throws EntitatNotFoundException;

	/**
	 * Esborra els permisos d'un usuari o d'un rol per a una entitat com a
	 * superusuari.
	 * 
	 * @param id
	 *            Atribut id de l'entitat de la qual es vol modificar el permís.
	 * @param permisId
	 *            Atribut id del permís que es vol esborrar.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_SUPER')")
	public void deletePermisSuper(
			Long id,
			Long permisId) throws EntitatNotFoundException;

	/**
	 * Consulta els permisos de l'entitat com a administrador.
	 * 
	 * @param id
	 *            Atribut id de l'entitat a la qual es volen consultar els permisos.
	 * @return El llistat de permisos.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<PermisDto> findPermisAdmin(
			Long id) throws EntitatNotFoundException;

	/**
	 * Modifica els permisos d'un usuari o d'un rol per a una entitat com a
	 * administrador de l'entitat.
	 * 
	 * @param id
	 *            Atribut id de l'entitat de la qual es vol modificar el permís.
	 * @param permis
	 *            El permís que es vol modificar.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void updatePermisAdmin(
			Long id,
			PermisDto permis) throws EntitatNotFoundException;

	/**
	 * Esborra els permisos d'un usuari o d'un rol per a una entitat com a
	 * administrador de l'entitat.
	 * 
	 * @param id
	 *            Atribut id de l'entitat de la qual es vol modificar el permís.
	 * @param permisId
	 *            Atribut id del permís que es vol esborrar.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deletePermisAdmin(
			Long id,
			Long permisId) throws EntitatNotFoundException;


}
