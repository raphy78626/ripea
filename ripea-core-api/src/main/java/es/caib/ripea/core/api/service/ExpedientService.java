/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.ContenidorDto;
import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.dto.ExpedientFiltreDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.exception.NomInvalidException;
import es.caib.ripea.core.api.exception.NotFoundException;

/**
 * Declaració dels mètodes per a gestionar contenidors.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface ExpedientService {

	/**
	 * Crea un nou expedient a dins un contenidor.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'expedient.
	 * @param contenidorId
	 *            Atribut id del contenidor dins el qual es vol crear l'expedient.
	 * @param metaExpedientId
	 *            Atribut id del meta-expedient a partir del qual es vol crear l'expedient.
	 * @param arxiuId
	 *            Atribut id de l'arxiu dins el qual es vol crear l'expedient.
	 * @param any
	 *            Any de l'expedient que es vol crear. Si és null l'expedient es crearà
	 *            a dins l'any actual.
	 * @param nom
	 *            Nom de l'expedient que es vol crear.
	 * @param contingutId
	 *            Atribut id del contingut que es vol associar amb l'expedient.
	 * @param registreId
	 *            Atribut id de l'anotació de registre que es vol associar amb l'expedient.
	 * @return L'expedient creat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws NomInvalidException
	 *             Si el nom del contenidor conté caràcters invàlids.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public ExpedientDto create(
			Long entitatId,
			Long contenidorId,
			Long metaExpedientId,
			Long arxiuId,
			Integer any,
			String nom,
			Long contingutId,
			Long registreId) throws NotFoundException, NomInvalidException;

	/**
	 * Modifica un expedient.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'expedient.
	 * @param id
	 *            Atribut id de l'expedient que es vol modificar.
	 * @param arxiuId
	 *            Atribut id de l'arxiu.
	 * @param metaExpedientId
	 *            Atribut id del meta-expedient.
	 * @param nom
	 *            Nom de l'expedient.
	 * @return L'expedient modificat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws NomInvalidException
	 *             Si el nom del contenidor conté caràcters invàlids.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public ExpedientDto update(
			Long entitatId,
			Long id,
			Long arxiuId,
			Long metaExpedientId,
			String nom) throws NotFoundException, NomInvalidException;

	/**
	 * Esborra un expedient.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'expedient.
	 * @param id
	 *            Atribut id de l'expedient que es vol esborrar.
	 * @return L'expedient a esborrar.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public ExpedientDto delete(
			Long entitatId,
			Long id) throws NotFoundException;

	/**
	 * Consulta un expedient donat el seu id.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'expedient.
	 * @param id
	 *            Atribut id de l'expedient que es vol trobar.
	 * @return L'expedient.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public ExpedientDto findById(
			Long entitatId,
			Long id) throws NotFoundException;

	/**
	 * Consulta els expedients donat un arxiu.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param arxiuId
	 *            Atribut id de l'arxiu.
	 * @param filtre
	 *            Filtre per a la consulta.
	 * @param paginacioParams
	 *            Paràmetres per a dur a terme la paginació del resultats.
	 * @return La pàgina amb els expedients trobats.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public PaginaDto<ExpedientDto> findPaginatAdmin(
			Long entitatId,
			ExpedientFiltreDto filtre,
			PaginacioParamsDto paginacioParams) throws NotFoundException;

	/**
	 * Consulta els expedients donat un arxiu.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param arxiuId
	 *            Atribut id de l'arxiu.
	 * @param filtre
	 *            Filtre per a la consulta.
	 * @param paginacioParams
	 *            Paràmetres per a dur a terme la paginació del resultats.
	 * @return La pàgina amb els expedients trobats.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public PaginaDto<ExpedientDto> findPaginatUser(
			Long entitatId,
			ExpedientFiltreDto filtre,
			PaginacioParamsDto paginacioParams) throws NotFoundException;

	/**
	 * Consulta el contingut de la carpeta de nouvinguts de l'expedient.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'expedient.
	 * @param id
	 *            Atribut id de l'expedient.
	 * @return El contingut de la carpeta nouvinguts.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<ContenidorDto> getContingutCarpetaNouvinguts(
			Long entitatId,
			Long id) throws NotFoundException;

	/**
	 * Posa un expedient a l'escriptori de l'usuari actual.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'expedient.
	 * @param arxiuId
	 *            Atribut id de l'arxiu al qual pertany l'expedient.
	 * @param id
	 *            Atribut id de l'expedient.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public void agafarUser(
			Long entitatId,
			Long arxiuId,
			Long id) throws NotFoundException;

	/**
	 * Posa un expedient a l'escriptori de l'usuari seleccionat.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'expedient.
	 * @param arxiuId
	 *            Atribut id de l'arxiu al qual pertany l'expedient.
	 * @param id
	 *            Atribut id de l'expedient.
	 * @param usuariCodi
	 *            Codi de l'usuari al qual es vol enviar l'expedient.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void agafarAdmin(
			Long entitatId,
			Long arxiuId,
			Long id,
			String usuariCodi) throws NotFoundException;

	/**
	 * Allibera un expedient agafat per l'usuari actual.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'expedient.
	 * @param id
	 *            Atribut id de l'expedient.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public void alliberarUser(
			Long entitatId,
			Long id) throws NotFoundException;

	/**
	 * Allibera un expedient agafat per qualsevol usuari.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'expedient.
	 * @param id
	 *            Atribut id de l'expedient.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void alliberarAdmin(
			Long entitatId,
			Long id) throws NotFoundException;

	/**
	 * Finalitza un expedient.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'expedient.
	 * @param id
	 *            Atribut id de l'expedient.
	 * @param motiu
	 *            Motiu de la finalització de l'expedient.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public void finalitzar(
			Long entitatId,
			Long id,
			String motiu) throws NotFoundException;

	/**
	 * Acumula les dades d'un expedient a dins un altre.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'expedient.
	 * @param id
	 *            Atribut id de l'expedient destí de l'acumulació.
	 * @param acumulatId
	 *            Atribut id de l'expedient que conté els elements que es volen
	 *            moure a dins l'expedient destí.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public void acumular(
			Long entitatId,
			Long id,
			Long acumulatId) throws NotFoundException;

}
