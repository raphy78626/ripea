/**
 * 
 */
package es.caib.ripea.core.api.service;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.RegistreAnotacioDto;
import es.caib.ripea.core.api.exception.BustiaNotFoundException;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.ExpedientNotFoundException;
import es.caib.ripea.core.api.exception.RegistreNotFoundException;

/**
 * Declaració dels mètodes per a gestionar les anotacions
 * de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface RegistreService {

	/**
	 * Crea un nou registre.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param registre
	 *            Informació de l'anotacio a crear.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws BustiaNotFoundException
	 *             Si no s'ha trobat la bústia destinatària per l'anotació.
	 */
	@PreAuthorize("hasRole('ROLE_REGWS')")
	public void create(
			Long entitatId,
			RegistreAnotacioDto registre) throws EntitatNotFoundException, BustiaNotFoundException;

	/**
	 * Rebutja un registre situat dins una bústia.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param bustia
	 *            Atribut id de la bústia.
	 * @param registreId
	 *            Atribut id de l'anotació de registre a afegir.
	 * @param motiu
	 *            Motiu del rebuig.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws BustiaNotFoundException
	 *             Si no s'ha trobat l'expedient amb l'id especificat.
	 * @throws RegistreNotFoundException
	 *             Si no s'ha trobat l'anotació de registre  amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public void rebutjar(
			Long entitatId,
			Long bustiaId,
			Long registreId,
			String motiu) throws EntitatNotFoundException, BustiaNotFoundException, RegistreNotFoundException;

	/**
	 * Afegeix una anotació de registre a un expedient existent.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'expedient.
	 * @param expedientId
	 *            Atribut id de l'expedient.
	 * @param registreId
	 *            Atribut id de l'anotació de registre a afegir.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws ExpedientNotFoundException
	 *             Si no s'ha trobat l'expedient amb l'id especificat.
	 * @throws RegistreNotFoundException
	 *             Si no s'ha trobat l'anotació de registre  amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public void afegirAExpedient(
			Long entitatId,
			Long expedientId,
			Long registreId) throws EntitatNotFoundException, ExpedientNotFoundException, RegistreNotFoundException;

}
