/**
 * 
 */
package es.caib.ripea.core.api.service;

/**
 * Declaració dels mètodes per a gestionar continguts.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface SegonPlaService {

	/**
	 * Comprovar si hi ha execucions massives pendents 
	 * d'executar-se
	 */
//	@PreAuthorize("hasRole('tothom')")
	public void comprovarExecucionsMassives();
	
}
