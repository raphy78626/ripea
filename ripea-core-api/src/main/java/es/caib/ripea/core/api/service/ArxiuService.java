/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.ArbreDto;
import es.caib.ripea.core.api.dto.ArxiuDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.exception.ArxiuNotFoundException;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.UnitatNotFoundException;

/**
 * Declaració dels mètodes per a gestionar arxius.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface ArxiuService {

	/**
	 * Crea un nou arxiu.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param arxiu
	 *            Informació de l'arxiu a crear.
	 * @return L'arxiu creat.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws UnitatNotFoundException
	 *             Si no s'ha trobat cap unitat amb el codi especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ArxiuDto create(
			Long entitatId,
			ArxiuDto arxiu) throws EntitatNotFoundException, UnitatNotFoundException;

	/**
	 * Actualitza la informació de l'arxiu.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param arxiu
	 *            Informació de l'arxiu a modificar.
	 * @return L'arxiu modificat.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws ArxiuNotFoundException
	 *             Si no s'ha trobat cap arxiu amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ArxiuDto update(
			Long entitatId,
			ArxiuDto arxiu) throws EntitatNotFoundException, ArxiuNotFoundException;

	/**
	 * Activa o desactiva un arxiu.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Id de l'arxiu a modificar.
	 * @param actiu
	 *            Indica si activar o desactivar l'arxiu.
	 * @return L'arxiu modificat.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws ArxiuNotFoundException
	 *             Si no s'ha trobat cap arxiu amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ArxiuDto updateActiu(
			Long entitatId,
			Long id,
			boolean actiu) throws EntitatNotFoundException, ArxiuNotFoundException;

	/**
	 * Esborra l'arxiu amb l'id especificat.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Atribut id de l'arxiu a esborrar.
	 * @return L'arxiu esborrat.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws ArxiuNotFoundException
	 *             Si no s'ha trobat cap arxiu amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ArxiuDto delete(
			Long entitatId,
			Long id) throws EntitatNotFoundException, ArxiuNotFoundException;

	/**
	 * Consulta un arxiu donat el seu id.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Atribut id de l'arxiu a trobar.
	 * @return L'arxiu amb l'id especificat o null si no s'ha trobat.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public ArxiuDto findById(
			Long entitatId,
			Long id) throws EntitatNotFoundException;

	/**
	 * Consulta els arxius donat el codi d'unitat.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param unitatCodi
	 *            Atribut unitatCodi dels arxius a trobar.
	 * @return Els arxius amb la unitat especificada.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws UnitatNotFoundException
	 *             Si no s'ha trobat cap unitat amb el codi especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<ArxiuDto> findByUnitatCodiAdmin(
			Long entitatId,
			String unitatCodi) throws EntitatNotFoundException, UnitatNotFoundException;
	
	/**
	 * Consulta els arxius donat el codi d'unitat.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param unitatCodi
	 *            Atribut unitatCodi dels arxius a trobar.
	 * @return Els arxius amb la unitat especificada.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws UnitatNotFoundException
	 *             Si no s'ha trobat cap unitat amb el codi especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<ArxiuDto> findByUnitatCodiUsuari(
			Long entitatId,
			String unitatCodi) throws EntitatNotFoundException, UnitatNotFoundException;

	/**
	 * Consulta els arxius de l'entitat amb permisos d'accés per a
	 * l'usuari actual.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @return Els arxius permesos.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<ArxiuDto> findPermesosPerUsuari(
			Long entitatId) throws EntitatNotFoundException;

	/**
	 * Modifica els permisos d'un usuari o d'un rol per a accedir a un arxiu.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Atribut id de l'arxiu del qual es modificar el permís.
	 * @param permis
	 *            El permís que es vol modificar.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws ArxiuNotFoundException
	 *             Si no s'ha trobat cap arxiu amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void updatePermis(
			Long entitatId,
			Long id,
			PermisDto permis) throws EntitatNotFoundException, ArxiuNotFoundException;

	/**
	 * Esborra els permisos d'un usuari o d'un rol per a accedir a un arxiu.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Atribut id de l'arxiu del qual es vol modificar el permís.
	 * @param permisId
	 *            Atribut id del permís que es vol esborrar.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws ArxiuNotFoundException
	 *             Si no s'ha trobat cap arxiu amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deletePermis(
			Long entitatId,
			Long id,
			Long permisId) throws EntitatNotFoundException, ArxiuNotFoundException;

	/**
	 * Consulta l'arbre de les unitats organitzatives per a mostrar els
	 * arxius a un usuari administrador.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @return L'arbre de les unitats organitzatives.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzativesAdmin(
			Long entitatId) throws EntitatNotFoundException;

	/**
	 * Consulta l'arbre de les unitats organitzatives per a mostrar els
	 * arxius a un usuari normal.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @return L'arbre de les unitats organitzatives.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzativesUser(
			Long entitatId) throws EntitatNotFoundException;

}
