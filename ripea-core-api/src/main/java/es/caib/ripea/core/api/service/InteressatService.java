/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.InteressatAdministracioDto;
import es.caib.ripea.core.api.dto.InteressatCiutadaDto;
import es.caib.ripea.core.api.dto.InteressatDto;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.ExpedientNotFoundException;
import es.caib.ripea.core.api.exception.InteressatNotFoundException;

/**
 * Declaració dels mètodes per a gestionar contenidors.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface InteressatService {

	/**
	 * Crea un nou interessat i l'associa a un expedient.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'expedient.
	 * @param expedientId
	 *            Atribut id de l'expedient al qual s'associarà l'interessat.
	 * @param interessat
	 *            Dades de l'interessat que es vol crear.
	 * @return L'interessat creat.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws ExpedientNotFoundException
	 *             Si no s'ha trobat l'expedient amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public InteressatDto create(
			Long entitatId,
			Long expedientId,
			InteressatDto interessat) throws EntitatNotFoundException, ExpedientNotFoundException;

	/**
	 * Associa un interessat existent a un expedient.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'expedient.
	 * @param expedientId
	 *            Atribut id de l'expedient al qual s'associarà l'interessat.
	 * @param id
	 *            Atribut id de l'interessat que es vol afegir.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws ExpedientNotFoundException
	 *             Si no s'ha trobat l'expedient amb l'id especificat.
	 * @throws InteressatNotFoundException
	 *             Si no s'ha trobat l'interessat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public void addToExpedient(
			Long entitatId,
			Long expedientId,
			Long id) throws EntitatNotFoundException, ExpedientNotFoundException, InteressatNotFoundException;

	/**
	 * Deslliga un interessat existent d'un expedient.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'expedient.
	 * @param expedientId
	 *            Atribut id de l'expedient al qual s'associarà l'interessat.
	 * @param id
	 *            Atribut id de l'interessat que es vol afegir.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws ExpedientNotFoundException
	 *             Si no s'ha trobat l'expedient amb l'id especificat.
	 * @throws InteressatNotFoundException
	 *             Si no s'ha trobat l'interessat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public void removeFromExpedient(
			Long entitatId,
			Long expedientId,
			Long id) throws EntitatNotFoundException, ExpedientNotFoundException, InteressatNotFoundException;

	/**
	 * Consulta l'interessat donat el seu id.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'interessat.
	 * @param id
	 *            Atribut id de l'interessat que es vol trobar.
	 * @return l'interessat.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public InteressatDto findById(
			Long entitatId,
			Long id) throws EntitatNotFoundException;

	/**
	 * Consulta dels interessats associats a un expedient.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'interessat.
	 * @param expedientId
	 *            Atribut id de l'interessat que es vol trobar.
	 * @return Els insteressats associats a l'expedient.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws ExpedientNotFoundException
	 *             Si no s'ha trobat cap expedient amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<InteressatDto> findByExpedient(
			Long entitatId,
			Long expedientId) throws EntitatNotFoundException, ExpedientNotFoundException;

	/**
	 * Consulta els interessats per nom i identificador.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'interessat.
	 * @param nom
	 *            Nom de l'interessat per a la consulta.
	 * @param nif
	 *            NIF de l'interessat per a la consulta.
	 * @param llinatges
	 *            Llinatges de l'interessat per a la consulta.
	 * @return La llista d'interessats trobats.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<InteressatCiutadaDto> findByFiltreCiutada(
			Long entitatId,
			String nom,
			String nif,
			String llinatges) throws EntitatNotFoundException;

	/**
	 * Consulta els interessats per nom i identificador.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'interessat.
	 * @param nom
	 *            Nom de l'interessat per a la consulta.
	 * @param identificador
	 *            Identificador de l'interessat per a la consulta.
	 * @return La llista d'interessats trobats.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<InteressatAdministracioDto> findByFiltreAdministracio(
			Long entitatId,
			String nom,
			String identificador) throws EntitatNotFoundException;

}
