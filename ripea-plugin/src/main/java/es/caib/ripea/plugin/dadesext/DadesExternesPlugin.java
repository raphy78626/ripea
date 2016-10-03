/**
 * 
 */
package es.caib.ripea.plugin.dadesext;

import java.util.List;

import es.caib.ripea.plugin.SistemaExternException;


/**
 * Interfície per consultar dades externes.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface DadesExternesPlugin {

	/**
	 * Consulta la llista de paisos disponibles.
	 * 
	 * @return la llista de paisos.
	 * @throws SistemaExternException
	 *             Si hi ha hagut algun problema per dur a terme l'acció.
	 */
	public List<Pais> paisFindAll() throws SistemaExternException;

	/**
	 * Consulta la llista de províncies disponibles.
	 * 
	 * @return la llista de províncies.
	 * @throws SistemaExternException
	 *             Si hi ha hagut algun problema per dur a terme l'acció.
	 */
	public List<Provincia> provinciaFindAll() throws SistemaExternException;

	/**
	 * Consulta la llista de províncies d'una comunitat autònoma.
	 * 
	 * @param comunitatCodi
	 *            El codi de la comunitat autònoma.
	 * @return la llista de províncies.
	 * @throws SistemaExternException
	 *             Si hi ha hagut algun problema per dur a terme l'acció.
	 */
	public List<Provincia> provinciaFindByComunitat(
			String comunitatCodi) throws SistemaExternException;

	/**
	 * Consulta la llista de municipis d'una província.
	 * 
	 * @param provinciaCodi
	 *            El codi de la província.
	 * @return la llista de municipis.
	 * @throws SistemaExternException
	 *             Si hi ha hagut algun problema per dur a terme l'acció.
	 */
	public List<Municipi> municipiFindByProvincia(
			String provinciaCodi) throws SistemaExternException;

}
