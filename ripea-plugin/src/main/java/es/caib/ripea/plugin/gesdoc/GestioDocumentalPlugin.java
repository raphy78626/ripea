/**
 * 
 */
package es.caib.ripea.plugin.gesdoc;

import es.caib.ripea.plugin.SistemaExternException;


/**
 * Interfície per accedir al gestor documental.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface GestioDocumentalPlugin {

	/**
	 * Puja un document a la gestió documental.
	 * 
	 * @param fileName
	 *            Nom de l'arxiu.
	 * @param content
	 *            Contingut de l'arxiu.
	 * @return L'identificador del document.
	 * @throws SistemaExternException
	 *             Si hi ha hagut algun problema per dur a terme l'acció.
	 */
	public String create(
			GestioDocumentalArxiu arxiu) throws SistemaExternException;

	/**
	 * Actualitza un document ja existent a la gestió documental.
	 * 
	 * @param id
	 *            Identificador del document.
	 * @param fileName
	 *            Nom de l'arxiu.
	 * @param content
	 *            Contingut de l'arxiu.
	 * @throws SistemaExternException
	 *             Si hi ha hagut algun problema per dur a terme l'acció.
	 */
	public void update(
			String id,
			GestioDocumentalArxiu arxiu) throws SistemaExternException;

	/**
	 * Esborra un document ja existent a la gestió documental.
	 * 
	 * @param id
	 *            Identificador del document.
	 * @throws SistemaExternException
	 *             Si hi ha hagut algun problema per dur a terme l'acció.
	 */
	public void delete(String id) throws SistemaExternException;

	/**
	 * Obté un document de la gestió documental.
	 * 
	 * @param id
	 *            Identificador del document.
	 * @return La informació del document.
	 * @throws SistemaExternException
	 *             Si hi ha hagut algun problema per dur a terme l'acció.
	 */
	public GestioDocumentalArxiu get(String id) throws SistemaExternException;

}
