/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.ArbreDto;
import es.caib.ripea.core.api.dto.ArxiuDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.exception.NotFoundException;

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
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public ArxiuDto create(
			Long entitatId,
			ArxiuDto arxiu) throws NotFoundException;

	/**
	 * Actualitza la informació de l'arxiu.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param arxiu
	 *            Informació de l'arxiu a modificar.
	 * @return L'arxiu modificat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public ArxiuDto update(
			Long entitatId,
			ArxiuDto arxiu) throws NotFoundException;

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
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public ArxiuDto updateActiu(
			Long entitatId,
			Long id,
			boolean actiu) throws NotFoundException;

	/**
	 * Esborra l'arxiu amb l'id especificat.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Atribut id de l'arxiu a esborrar.
	 * @return L'arxiu esborrat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public ArxiuDto delete(
			Long entitatId,
			Long id) throws NotFoundException;

	/**
	 * Consulta un arxiu donat el seu id.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Atribut id de l'arxiu a trobar.
	 * @return L'arxiu amb l'id especificat o null si no s'ha trobat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN') or hasRole('tothom')")
	public ArxiuDto findById(
			Long entitatId,
			Long id) throws NotFoundException;

	/**
	 * Consulta els arxius donat el codi d'unitat.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param unitatCodi
	 *            Atribut unitatCodi dels arxius a trobar.
	 * @return Els arxius amb la unitat especificada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public List<ArxiuDto> findByUnitatCodiAdmin(
			Long entitatId,
			String unitatCodi) throws NotFoundException;
	
	/**
	 * Consulta els arxius donat el codi d'unitat.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param unitatCodi
	 *            Atribut unitatCodi dels arxius a trobar.
	 * @return Els arxius amb la unitat especificada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<ArxiuDto> findByUnitatCodiUsuari(
			Long entitatId,
			String unitatCodi) throws NotFoundException;

	/**
	 * Consulta els arxius de l'entitat amb permisos d'accés per a
	 * l'usuari actual i un meta-expedient donat.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @return Els arxius permesos.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<ArxiuDto> findPermesosPerUsuariIMetaExpedient(
			Long entitatId,
			Long metaExpedientId) throws NotFoundException;

	/**
	 * Consulta els arxius de l'entitat amb permisos d'accés per a
	 * l'usuari actual.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @return Els arxius permesos.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<ArxiuDto> findPermesosPerUsuari(Long entitatId);

	/**
	 * Consulta l'arbre de les unitats organitzatives per a mostrar els
	 * arxius a un usuari administrador.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @return L'arbre de les unitats organitzatives.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzativesAdmin(
			Long entitatId,
			boolean nomesAmbArxiusPermesos,
			boolean nomesQueContinguinArxius) throws NotFoundException;

	/**
	 * Consulta l'arbre de les unitats organitzatives per a mostrar els
	 * arxius a un usuari normal.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @return L'arbre de les unitats organitzatives.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzativesUser(
			Long entitatId) throws NotFoundException;

	/**
	 * Afegeix el meta-expedient en la relació amb l'arxiu si aquest no existia.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Atribut id de l'arxiu al qual es vol afegir el meta-expedient.
	 * @param metaExpedientId
	 *            El meta-exedient que es vol relacionar.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public void addMetaExpedient(
			Long entitatId,
			Long id,
			Long metaExpedientId) throws NotFoundException;

	/**
	 * Esborra la relació de l'arxiu amb el meta-expedient.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param id
	 *            Atribut id de l'arxiu del qual es vol esborrar la relació amb el meta-expedient.
	 * @param metaExpedientId
	 *            Atribut id del meta-expedient del que es vol esborrar la relació.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public void removeMetaExpedient(
			Long entitatId,
			Long id,
			Long metaExpedientId) throws NotFoundException;

	/**
	 * Consulta la llista d'arxius del meta-expedient.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param id
	 *            Atribut id de l'arxiu.
	 * @return la llista d'arxius.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public List<ArxiuDto> findAmbMetaExpedient(
			Long entitatId, 
			Long metaExpedientId) throws NotFoundException;

	/**
	 * Consulta la llista d'arxius del meta-expedient per a la creació d'expedients.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param id
	 *            Atribut id de l'arxiu.
	 * @return la llista d'arxius.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<ArxiuDto> findAmbMetaExpedientPerCreacio(
			Long entitatId, 
			Long metaExpedientId) throws NotFoundException;

	/**
	 * Consulta la llista d'arxius de l'entitat.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @return la llista d'arxius.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public List<ArxiuDto> findActiusAmbEntitat(
			Long entitatId) throws NotFoundException;
}
