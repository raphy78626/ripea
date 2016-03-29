package es.caib.ripea.plugin.registre;

import es.caib.ripea.plugin.SistemaExternException;

/**
 * Plugin per a la integració amb el registre d'entrades i sortides.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface RegistrePlugin {

	/**
	 * Consulta la informació d'una anotació d'entrada al registre.
	 * 
	 * @param identificador
	 *            L'identificador de l'anotació a consultar.
	 * @return La informació de l'anotació.
	 * @throws SistemaExternException
	 *            Si hi ha hagut algun problema per dur a terme l'acció.
	 */
	public RegistreAnotacio entradaConsultar(
			String identificador) throws SistemaExternException;

}
