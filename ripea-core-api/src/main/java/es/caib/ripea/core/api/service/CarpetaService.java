/**
 * 
 */
package es.caib.ripea.core.api.service;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.CarpetaDto;
import es.caib.ripea.core.api.dto.CarpetaTipusEnumDto;
import es.caib.ripea.core.api.exception.CarpetaNotFoundException;
import es.caib.ripea.core.api.exception.ContenidorNotFoundException;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.NomInvalidException;

/**
 * Declaració dels mètodes per a gestionar contenidors.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface CarpetaService {

	/**
	 * Crea una nova carpeta a dins un contenidor.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param contenidorId
	 *            Atribut id del contenidor dins el qual es vol crear la carpeta.
	 * @param nom
	 *            Nom de la carpeta que es vol crear.
	 * @param tipus
	 *            Tipus de la carpeta.
	 * @return La carpeta creada.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws ContenidorNotFoundException
	 *             Si no s'ha trobat el contenidor amb l'id especificat.
	 * @throws MetaCarpetaNotFoundException
	 *             Si no s'ha trobat el meta-expedient amb l'id especificat.
	 * @throws NomInvalidException
	 *             Si el nom del contenidor conté caràcters invàlids.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public CarpetaDto create(
			Long entitatId,
			Long contenidorId,
			String nom,
			CarpetaTipusEnumDto tipus) throws EntitatNotFoundException, ContenidorNotFoundException, NomInvalidException;

	/**
	 * Modifica una carpeta.
	 * 
	 * @param entitatId
	 *            Atribut id de la carpeta a la qual pertany el contenidor.
	 * @param id
	 *            Atribut id de la carpeta que es vol modificar.
	 * @param nom
	 *            Nom de la carpeta.
	 * @param tipus
	 *            Tipus de la carpeta.
	 * @return La carpeta modificada.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws CarpetaNotFoundException
	 *             Si no s'ha trobat la carpeta amb l'id especificat.
	 * @throws NomInvalidException
	 *             Si el nom del contenidor conté caràcters invàlids.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public CarpetaDto update(
			Long entitatId,
			Long id,
			String nom,
			CarpetaTipusEnumDto tipus) throws EntitatNotFoundException, CarpetaNotFoundException, NomInvalidException;

	/**
	 * Esborra una carpeta.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param id
	 *           Atribut id de la carpeta que es vol esborrar.
	 * @return La carpeta esborrada.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws CarpetaNotFoundException
	 *             Si no s'ha trobat la carpeta amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public CarpetaDto delete(
			Long entitatId,
			Long id) throws EntitatNotFoundException, CarpetaNotFoundException;

	/**
	 * Consulta una carpeta donat el seu id.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param id
	 *            Atribut id de la carpeta que es vol trobar.
	 * @return La carpeta.
	 * @throws EntitatNotFoundException
	 *             Si no s'ha trobat cap entitat amb l'id especificat.
	 * @throws CarpetaNotFoundException
	 *             Si no s'ha trobat la carpeta amb l'id especificat.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	public CarpetaDto findById(
			Long entitatId,
			Long id) throws EntitatNotFoundException, CarpetaNotFoundException;

}
