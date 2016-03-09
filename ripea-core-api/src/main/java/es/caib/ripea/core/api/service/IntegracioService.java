/**
 * 
 */
package es.caib.ripea.core.api.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import es.caib.ripea.core.api.dto.IntegracioAccioDto;
import es.caib.ripea.core.api.dto.IntegracioDto;
import es.caib.ripea.core.api.exception.NotFoundException;

/**
 * Declaració dels mètodes per al seguiment de la comunicació
 * entre RIPEA i les diferents integracions.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface IntegracioService {

	/**
	 * Obté les integracions disponibles.
	 * 
	 * @return La llista d'integracions.
	 */
	@PreAuthorize("hasRole('ROLE_SUPER')")
	public List<IntegracioDto> findAll();

	/**
	 * Obté la llista de les darreres accions realitzades a una integració.
	 * 
	 * @param codi
	 *             Codi de la integració.
	 * @return La llista amb les darreres accions.
	 * @throws NotFoundException
	 *             Si no s'ha trobat la integració amb el codi especificat.
	 */
	@PreAuthorize("hasRole('ROLE_SUPER')")
	public List<IntegracioAccioDto> findDarreresAccionsByIntegracio(
			String codi) throws NotFoundException;

}
