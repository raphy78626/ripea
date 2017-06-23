/**
 * 
 */
package es.caib.ripea.core.api.service;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.ExecucioMassivaDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ValidationException;

/**
 * Declaració dels mètodes per a gestionar documents.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface ExecucioMassivaService {
	
	/**
	 * Crea una nova execució massiva
	 * 
	 * @param dto
	 *            Dto amb la informació de l'execució massiva a programar
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 * @throws ValidationException
	 *             Si el nom del contenidor conté caràcters invàlids.
	 */
	@PreAuthorize("hasRole('tothom')")
	public void crearExecucioMassiva(ExecucioMassivaDto dto) throws NotFoundException, ValidationException;
	
}
