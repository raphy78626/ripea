/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.MetaDadaDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.MetaDadaNotFoundException;
import es.caib.ripea.core.api.exception.NodeNotFoundException;

/**
 * Declaració dels mètodes per a la gestió de meta-dades.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface MetaDadaService {

	/**
	 * Crea una nova meta-dada.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param metaDada
	 *            Informació de la meta-dada a crear.
	 * @return La MetaDada creada.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public MetaDadaDto create(
			Long entitatId,
			MetaDadaDto metaDada) throws EntitatNotFoundException;

	/**
	 * Actualitza la informació de la meta-dada que tengui el mateix
	 * id que l'especificat per paràmetre.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param metaDada
	 *            Informació de la meta-dada a modificar.
	 * @return La meta-dada modificada.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws MetaDadaNotFoundException
	 *             Si no s'ha trobat cap meta-dada amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public MetaDadaDto update(
			Long entitatId,
			MetaDadaDto metaDada) throws EntitatNotFoundException, MetaDadaNotFoundException;

	/**
	 * Marca com a activa/inactiva la meta-dada amb el mateix id
	 * que l'especificat.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Atribut id de la meta-dada a esborrar.
	 * @param activa
	 *            true si la meta-dada es vol activar o false en cas
	 *            contrari.
	 * @return La meta-dada modificada.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws MetaDadaNotFoundException
	 *             Si no s'ha trobat cap meta-dada amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public MetaDadaDto updateActiva(
			Long entitatId,
			Long id,
			boolean activa) throws EntitatNotFoundException, MetaDadaNotFoundException;

	/**
	 * Esborra la meta-dada amb el mateix id que l'especificat.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Atribut id de la meta-dada a esborrar.
	 * @return La meta-dada esborrada.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws MetaDadaNotFoundException
	 *             Si no s'ha trobat cap meta-dada amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public MetaDadaDto delete(
			Long entitatId,
			Long id) throws EntitatNotFoundException, MetaDadaNotFoundException;

	/**
	 * Consulta una meta-dada donat el seu id.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param id
	 *            Atribut id de la meta-dada a trobar.
	 * @return La meta-dada amb l'id especificat o null si no s'ha trobat.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public MetaDadaDto findById(
			Long entitatId,
			Long id) throws EntitatNotFoundException;

	/**
	 * Consulta una meta-dada donat el seu codi.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param codi
	 *            Atribut codi de la meta-dada a trobar.
	 * @return La meta-dada amb el codi especificat o null si no s'ha trobat.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public MetaDadaDto findByEntitatCodi(
			Long entitatId,
			String codi) throws EntitatNotFoundException;

	/**
	 * Llistat paginat amb totes les meta-dades.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param paginacioParams
	 *            Paràmetres per a dur a terme la paginació del resultats.
	 * @return La pàgina de meta-dades.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public PaginaDto<MetaDadaDto> findAllByEntitatPaginat(
			Long entitatId,
			PaginacioParamsDto paginacioParams) throws EntitatNotFoundException;

	/**
	 * Llistat amb les meta-dades actives per a una entitat.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param incloureGlobalsExpedient
	 *            Indica si s'han de retornar les meta-dades globals per expedient.
	 * @param incloureGlobalsDocument
	 *            Indica si s'han de mostrar les meta-dades globals per document.
	 * @return La pàgina de meta-dades.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<MetaDadaDto> findActiveByEntitat(
			Long entitatId,
			boolean incloureGlobalsExpedient,
			boolean incloureGlobalsDocument) throws EntitatNotFoundException;

	/**
	 * Llistat amb les meta-dades actives donat un meta-node.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param nodeId
	 *            Id del node.
	 * @return
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws NodeNotFoundException
	 *             Si no s'ha trobat cap node amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<MetaDadaDto> findByNodePerCreacio(
			Long entitatId,
			Long nodeId) throws EntitatNotFoundException, NodeNotFoundException;

}
