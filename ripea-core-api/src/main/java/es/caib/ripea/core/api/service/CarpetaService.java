/**
 * 
 */
package es.caib.ripea.core.api.service;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.CarpetaDto;
import es.caib.ripea.core.api.dto.CarpetaTipusEnumDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ValidationException;

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
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws ValidationException
	 *             Si el nom del contenidor conté caràcters invàlids.
	 */
	@PreAuthorize("hasRole('tothom')")
	public CarpetaDto create(
			Long entitatId,
			Long contenidorId,
			String nom,
			CarpetaTipusEnumDto tipus) throws NotFoundException, ValidationException;

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
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws ValidationException
	 *             Si el nom del contenidor conté caràcters invàlids.
	 */
	@PreAuthorize("hasRole('tothom')")
	public CarpetaDto update(
			Long entitatId,
			Long id,
			String nom,
			CarpetaTipusEnumDto tipus) throws NotFoundException, ValidationException;

	/**
	 * Esborra una carpeta.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param id
	 *           Atribut id de la carpeta que es vol esborrar.
	 * @return La carpeta esborrada.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public CarpetaDto delete(
			Long entitatId,
			Long id) throws NotFoundException;

	/**
	 * Consulta una carpeta donat el seu id.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat a la qual pertany el contenidor.
	 * @param id
	 *            Atribut id de la carpeta que es vol trobar.
	 * @return La carpeta.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public CarpetaDto findById(
			Long entitatId,
			Long id) throws NotFoundException;

}
