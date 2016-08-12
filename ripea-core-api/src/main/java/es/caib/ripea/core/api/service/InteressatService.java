/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.InteressatAdministracioDto;
import es.caib.ripea.core.api.dto.InteressatPersonaFisicaDto;
import es.caib.ripea.core.api.dto.InteressatPersonaJuridicaDto;
import es.caib.ripea.core.api.dto.InteressatDto;
import es.caib.ripea.core.api.exception.NotFoundException;

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
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public InteressatDto create(
			Long entitatId,
			Long expedientId,
			InteressatDto interessat) throws NotFoundException;

	/**
	 * Associa un interessat existent a un expedient.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'expedient.
	 * @param expedientId
	 *            Atribut id de l'expedient al qual s'associarà l'interessat.
	 * @param id
	 *            Atribut id de l'interessat que es vol afegir.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public void addToExpedient(
			Long entitatId,
			Long expedientId,
			Long id) throws NotFoundException;

	/**
	 * Deslliga un interessat existent d'un expedient.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'expedient.
	 * @param expedientId
	 *            Atribut id de l'expedient al qual s'associarà l'interessat.
	 * @param id
	 *            Atribut id de l'interessat que es vol afegir.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public void removeFromExpedient(
			Long entitatId,
			Long expedientId,
			Long id) throws NotFoundException;

	/**
	 * Consulta l'interessat donat el seu id.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'interessat.
	 * @param id
	 *            Atribut id de l'interessat que es vol trobar.
	 * @return l'interessat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public InteressatDto findById(
			Long entitatId,
			Long id) throws NotFoundException;

	/**
	 * Consulta dels interessats associats a un expedient.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'interessat.
	 * @param expedientId
	 *            Atribut id de l'interessat que es vol trobar.
	 * @return Els insteressats associats a l'expedient.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<InteressatDto> findByExpedient(
			Long entitatId,
			Long expedientId) throws NotFoundException;

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
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<InteressatPersonaFisicaDto> findByFiltrePersonaFisica(
			Long entitatId,
			String documentNum,
			String nom,
			String llinatge1,
			String llinatge2) throws NotFoundException;
	
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
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<InteressatPersonaJuridicaDto> findByFiltrePersonaJuridica(
			Long entitatId,
			String documentNum,
			String raoSocial) throws NotFoundException;
	

	/**
	 * Consulta els interessats per nom i identificador.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'interessat.
	 * @param organCodi
	 *            codi de l'organ de l'administració per a la consulta.
	 * @return La llista d'interessats trobats.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<InteressatAdministracioDto> findByFiltreAdministracio(
			Long entitatId,
			String organCodi) throws NotFoundException;

}
