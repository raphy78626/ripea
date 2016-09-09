/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.InteressatAdministracioDto;
import es.caib.ripea.core.api.dto.InteressatPersonaFisicaDto;
import es.caib.ripea.core.api.dto.InteressatPersonaJuridicaDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
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
	 * Crea un nou interessat i l'associa a un expedient.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'expedient.
	 * @param expedientId
	 *            Atribut id de l'expedient al qual s'associarà l'interessat.
	 * @param representant
	 *            Dades del representant que es vol crear.
	 * @return L'interessat creat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public InteressatDto create(
			Long id, 
			Long expedientId, 
			Long interessatId,
			InteressatDto representant) throws NotFoundException;
	
	/**
	 * Modifica un representant associat a un interessat.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'interessat.
	 * @param expedientId
	 *            Atribut id de l'expedient al qual s'associarà l'interessat.
	 * @param interessatId
	 *            Atribut id de linteressat al qual s'associarà l'interessat.
	 * 
	 * @return L'interessat modificat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public InteressatDto update(
			Long entitatId,
			Long expedientId,
			InteressatDto interessat) throws NotFoundException;

	/**
	 * Modifica un representant associat a un interessat.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el representant.
	 * @param expedientId
	 *            Atribut id de l'expedient al qual s'associarà el representant.
	 * @param interessatId
	 *            Atribut id de linteressat al qual s'associarà el representant.
	 * @param representant
	 *            Dades del representant que es vol modificar.
	 * @return L'interessat modificat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public InteressatDto update(
			Long id, 
			Long expedientId, 
			Long interessatId, 
			InteressatDto representant);
	
	/**
	 * elimina un interessat existent en un expedient.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'expedient.
	 * @param expedientId
	 *            Atribut id de l'expedient al qual s'associarà l'interessat.
	 * @param interessatId
	 *            Atribut id de l'interessat que es vol afegir.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public void delete(
			Long entitatId,
			Long expedientId,
			Long interessatId);
	
	/**
	 * elimina un interessat existent en un expedient.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el representant.
	 * @param expedientId
	 *            Atribut id de l'expedient al qual s'associarà el representant.
	 * @param interessatId
	 *            Atribut id de l'interessat al qual pertany el representant.
	 * @param representantId
	 *            Atribut id del representant que es vol esborrar.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public void delete(
			Long id, 
			Long expedientId, 
			Long interessatId, 
			Long representantId);

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
	 * Consulta el representant donat el seu id i interessat.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'interessat.
	 * @param interessatId
	 *            Atribut id de l'interessat a la qual pertany el representant.
	 * @param id
	 *            Atribut id del representant que es vol trobar.
	 * @return l'interessat.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public InteressatDto findRepresentantById(
			Long entitatId,
			Long interessatId,
			Long id);
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
	 * Consulta el nombre d'interessats associats a un expedient.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany l'interessat.
	 * @param expedientId
	 *            Atribut id de l'interessat que es vol trobar.
	 * @return El nombre d'insteressats associats a l'expedient.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public Long countByExpedient(
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

	/**
	 * Consulta les unitats organitzatives de la entitat.
	 * 
	 * @param entitatCodi
	 *            Atribut codi de l'entitat a la qual pertany l'interessat.
	 * @return La llista d'unitats organitzatives trobades.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<UnitatOrganitzativaDto> findUnitatsOrganitzativesByEntitat(String entitatCodi);

	/**
	 * Consulta una unitat organitzativa per codi.
	 * 
	 *            Atribut codi de l'unitat.
	 * @return La unitats organitzativa trobada.
	 * @param codi
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	public UnitatOrganitzativaDto findUnitatsOrganitzativesByCodi(String codi);

}
