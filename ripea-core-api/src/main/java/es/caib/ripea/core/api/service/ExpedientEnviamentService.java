/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.Date;
import java.util.List;

import es.caib.notib.ws.notificacio.NotificacioEstatEnum;
import es.caib.ripea.core.api.dto.DocumentEnviamentDto;
import es.caib.ripea.core.api.dto.DocumentNotificacioDto;
import es.caib.ripea.core.api.dto.DocumentPublicacioDto;
import es.caib.ripea.core.api.exception.NotFoundException;

/**
 * Declaració dels mètodes per a gestionar enviaments dels expedients.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface ExpedientEnviamentService {

	/**
	 * Crea una notificació d'un document de l'expedient a un ciutadà.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param documentId
	 *            Atribut id del document.
	 * @param interessatId
	 *            Atribut id de l'interessat destinatari de la notificació.
	 * @param notificacio
	 *            Dades de la notificació.
	 * @return La notificació creada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	public DocumentNotificacioDto notificacioCreate(
			Long entitatId,
			Long documentId,
			Long interessatId,
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
	 *            L'atribut id de la notificació.
	 * @return La notificació modificada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	public DocumentNotificacioDto notificacioDelete(
			Long entitatId,
			Long expedientId,
			Long notificacioId) throws NotFoundException;

	/**
	 * Reintenta l'enviament d'una notificació electrònica feta amb anterioritat.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param expedientId
	 *            Atribut id de l'expedient.
	 * @param notificacioId
	 *            L'atribut id de la notificació.
	 * @return true si s'ha pogut enviar sense errors o false en cas contrari.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	public boolean notificacioRetry(
			Long entitatId,
			Long expedientId,
			Long notificacioId) throws NotFoundException;

	/**
	 * Consulta una notificació de l'expedient a un ciutadà.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param expedientId
	 *            Atribut id de l'expedient.
	 * @param notificacioId
	 *            L'atribut id de la notificació.
	 * @return La notificació trobada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	public DocumentNotificacioDto notificacioFindAmbId(
			Long entitatId,
			Long expedientId,
			Long notificacioId) throws NotFoundException;

	/**
	 * Processa les notificacions pendents de forma periòdica.
	 */
	public void notificacioProcessarPendents();

	/**
	 * Crea una publicació d'un document de l'expedient a un butlletí oficial.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param documentId
	 *            Atribut id del document.
	 * @param publicacio
	 *            Dades de la publicació.
	 * @return La publicació creada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	public DocumentPublicacioDto publicacioCreate(
			Long entitatId,
			Long documentId,
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
	 * Consulta una publicació de l'expedient a un butlletí oficial.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param expedientId
	 *            Atribut id de l'expedient.
	 * @param publicacioId
	 *            L'atribut id de la publicació.
	 * @return La publicació trobada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	public DocumentPublicacioDto publicacioFindAmbId(
			Long entitatId,
			Long expedientId,
			Long publicacioId) throws NotFoundException;

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
	public List<DocumentEnviamentDto> enviamentFindAmbExpedient(
			Long entitatId,
			Long expedientId) throws NotFoundException;

	
	/**
	 * Modifica una notificació d'un document de l'expedient
	 *  a partir d'una referencia d'enviament
	 * 
	 * @param referencia
	 *            Referència de l'enviament.
	 * @param estat
	 *            Estat de la notificació.
	 * @param data
	 *            Data del canvi d'estat.
	 * @return notificacio modificada
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	public DocumentNotificacioDto notificacioUpdatePerReferencia(
			String referencia,
			NotificacioEstatEnum estat,
			Date data) throws NotFoundException;
}
