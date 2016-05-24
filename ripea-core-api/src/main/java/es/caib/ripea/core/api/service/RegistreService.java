/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.RegistreAnotacioDto;
import es.caib.ripea.core.api.dto.RegistreMovimentDto;
import es.caib.ripea.core.api.exception.NotFoundException;

/**
 * Declaració dels mètodes per a gestionar les anotacions
 * de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface RegistreService {

	/**
	 * Retorna la informació d'una anotació de registre situada dins un contenidor.
	 * 
	 * @param entitatId
	 *            Atribut id de l'entitat.
	 * @param contenidorId
	 *            Atribut id del contenidor a on està situada l'anotació.
	 * @param registreId
	 *            Atribut id del l'anotació que es vol consultarcontenidor a on està situada l'anotació.
	 * @return els detalls de l'anotació.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public RegistreAnotacioDto findOne(
			Long entitatId,
			Long contenidorId,
			Long registreId) throws NotFoundException;

	/**
	 * Retorna una llista dels moviments d'una anotació de registre.
	 * 
	 * @param entitatId
	 *            Id de l'entitat.
	 * @param registreId
	 *            Atribut id de l'anotació de registre a afegir.
	 * @return la llista de moviments.
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public List<RegistreMovimentDto> findMoviments(
			Long entitatId,
			Long registreId) throws NotFoundException;

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
	 * @throws NotFoundException
	 *             Si no s'ha trobat l'objecte amb l'id especificat.
	 */
	@PreAuthorize("hasRole('tothom')")
	public void rebutjar(
			Long entitatId,
			Long bustiaId,
			Long registreId,
			String motiu) throws NotFoundException;

}
