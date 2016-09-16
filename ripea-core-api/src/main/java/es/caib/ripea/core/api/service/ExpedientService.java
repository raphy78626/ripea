/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.BustiaContingutPendentTipusEnumDto;
import es.caib.ripea.core.api.dto.DocumentEnviamentDto;
import es.caib.ripea.core.api.dto.DocumentNotificacioDto;
import es.caib.ripea.core.api.dto.DocumentPublicacioDto;
import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.dto.ExpedientFiltreDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ValidationException;

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
	 * @param pareId
	 *            Atribut id del contenidor a dins el qual es vol crear l'expedient.
	 * @param metaExpedientId
	 *            Atribut id del meta-expedient a partir del qual es vol crear l'expedient.
	 * @param arxiuId
	 *            Atribut id de l'arxiu dins el qual es vol crear l'expedient.
	 * @param any
	 *            Any de l'expedient que es vol crear. Si és null l'expedient es crearà
	 *            a dins l'any actual.
	 * @param nom
	 *            Nom de l'expedient que es vol crear.
	 * @param contingutTipus
	 *            Tipus de contingut que es vol associar amb l'expedient.
	 * @param contingutId
	 *            Atribut id del contingut que es vol associar amb l'expedient.
	 * @return L'expedient creat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws NomInvalidException
	 *             Si el nom del contenidor conté caràcters invàlids.
	 */
	@PreAuthorize("hasRole('tothom')")
	public ExpedientDto create(
			Long entitatId,
			Long pareId,
			Long metaExpedientId,
			Long arxiuId,
			Integer any,
			String nom,
			BustiaContingutPendentTipusEnumDto contingutTipus,
			Long contingutId) throws NotFoundException, ValidationException;

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
	@PreAuthorize("hasRole('tothom')")
	public ExpedientDto update(
			Long entitatId,
			Long id,
			Long arxiuId,
			Long metaExpedientId,
			String nom) throws NotFoundException, ValidationException;

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
	@PreAuthorize("hasRole('tothom')")
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
	@PreAuthorize("hasRole('tothom')")
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
	@PreAuthorize("hasRole('IPA_ADMIN')")
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
	@PreAuthorize("hasRole('tothom')")
	public PaginaDto<ExpedientDto> findPaginatUser(
			Long entitatId,
			ExpedientFiltreDto filtre,
			PaginacioParamsDto paginacioParams) throws NotFoundException;

	/**
	 * Posa un expedient a l'escriptori de l'usuari actual.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'expedient.
	 * @param id
	 *            Atribut id de l'expedient.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public void agafarUser(
			Long entitatId,
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
	@PreAuthorize("hasRole('IPA_ADMIN')")
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
	@PreAuthorize("hasRole('tothom')")
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
	@PreAuthorize("hasRole('IPA_ADMIN')")
	public void alliberarAdmin(
			Long entitatId,
			Long id) throws NotFoundException;

	/**
	 * Tanca la tramitació d'un expedient.
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
	@PreAuthorize("hasRole('tothom')")
	public void tancar(
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
	@PreAuthorize("hasRole('tothom')")
	public void acumular(
			Long entitatId,
			Long id,
			Long acumulatId) throws NotFoundException;

	/**
	 * Afegeix un contingut d'una bústia a un expedient.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'expedient.
	 * @param id
	 *            Atribut id de l'expedient destí de l'acumulació.
	 * @param bustiaId
	 *            Atribut id de la bústia a on es troba el contingut.
	 * @param contingutTipus
	 *            Tipus de contingut que es vol associar amb l'expedient.
	 * @param contingutId
	 *            Atribut id del contingut que es vol associar amb l'expedient.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	public void afegirContingutBustia(
			Long entitatId,
			Long id,
			Long bustiaId,
			BustiaContingutPendentTipusEnumDto contingutTipus,
			Long contingutId) throws NotFoundException;

	/**
	 * Relaciona l'expedient amb un altre.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param expedientId
	 *            Atribut id de l'expedient.
	 * @param relacionatId
	 *            Atribut id de l'expedient amb que es relacionarà.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public void relacioCreate(
			Long entitatId,
			Long expedientId,
			Long relacionatId) throws NotFoundException;

	/**
	 * Esborra una relació de l'expedient.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param expedientId
	 *            Atribut id de l'expedient.
	 * @param relacionatId
	 *            Atribut id de l'expedient relacionat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public boolean relacioDelete(
			Long entitatId,
			Long expedientId,
			Long relacionatId) throws NotFoundException;

	/**
	 * Retorna la llista d'expedients relacionats amb l'expedient
	 * especificat.
	 * 
	 * @param entitatId 
	 * @param expedientId
	 *            Atribut id de l'expedient que es vol consultar.
	 * @return La llista d'expedients relacionats.
	 */
	public List<ExpedientDto> relacioFindAmbExpedient(
			Long entitatId, 
			Long expedientId);

	/**
	 * Crea una notificació d'un document de l'expedient a un ciutadà.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param expedientId
	 *            Atribut id de l'expedient.
	 * @param notificacio
	 *            Dades de la notificació.
	 * @return La notificació creada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	public DocumentNotificacioDto notificacioCreate(
			Long entitatId,
			Long expedientId,
			DocumentNotificacioDto notificacio) throws NotFoundException;

	/**
	 * Modifica una notificació d'un document de l'expedient a un ciutadà.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param expedientId
	 *            Atribut id de l'expedient.
	 * @param notificacio
	 *            Dades de la notificació.
	 * @return La notificació modificada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	public DocumentNotificacioDto notificacioUpdate(
			Long entitatId,
			Long expedientId,
			DocumentNotificacioDto notificacio) throws NotFoundException;

	/**
	 * Esborra una notificació d'un document de l'expedient a un ciutadà.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param expedientId
	 *            Atribut id de l'expedient.
	 * @param notificacioId
	 *            L'atribut id de la notificacio.
	 * @return La notificació modificada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	public DocumentNotificacioDto notificacioDelete(
			Long entitatId,
			Long expedientId,
			Long notificacioId) throws NotFoundException;

	/**
	 * Retorna una llista de notificacions associades a un expedient.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param expedientId
	 *            Atribut id de l'expedient.
	 * @return La llista de notificacions.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	public List<DocumentEnviamentDto> notificacioFindByExpedientId(
			Long entitatId,
			Long expedientId) throws NotFoundException;

	/**
	 * Crea una publicació d'un document de l'expedient a un butlletí oficial.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param expedientId
	 *            Atribut id de l'expedient.
	 * @param publicacio
	 *            Dades de la publicació.
	 * @return La publicació creada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	public DocumentPublicacioDto publicacioCreate(
			Long entitatId,
			Long expedientId,
			DocumentPublicacioDto publicacio) throws NotFoundException;

	/**
	 * Modifica una publicació d'un document de l'expedient a un butlletí oficial.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param expedientId
	 *            Atribut id de l'expedient.
	 * @param publicacio
	 *            Dades de la publicació.
	 * @return La notificació modificada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	public DocumentPublicacioDto publicacioUpdate(
			Long entitatId,
			Long expedientId,
			DocumentPublicacioDto publicacio) throws NotFoundException;

	/**
	 * Esborra una publicació d'un document de l'expedient a un butlletí oficial.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param expedientId
	 *            Atribut id de l'expedient.
	 * @param publicacioId
	 *            L'atribut id de la publicació.
	 * @return La publicació modificada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	public DocumentPublicacioDto publicacioDelete(
			Long entitatId,
			Long expedientId,
			Long publicacioId) throws NotFoundException;

	/**
	 * Retorna una llista de publicacions associades a un expedient.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param expedientId
	 *            Atribut id de l'expedient.
	 * @return La llista de publicacions.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	public List<DocumentPublicacioDto> publicacioFindByExpedientId(
			Long entitatId,
			Long expedientId) throws NotFoundException;

}
