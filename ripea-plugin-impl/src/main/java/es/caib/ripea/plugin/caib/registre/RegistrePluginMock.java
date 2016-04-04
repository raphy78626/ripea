/**
 * 
 */
package es.caib.ripea.plugin.caib.registre;

import es.caib.ripea.plugin.SistemaExternException;
import es.caib.ripea.plugin.registre.RegistreAnotacio;
import es.caib.ripea.plugin.registre.RegistrePlugin;

/**
 * Implementació de proves del plugin de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class RegistrePluginMock implements RegistrePlugin {

	@Override
	public RegistreAnotacio entradaConsultar(
			String identificador,
			String entitat) throws SistemaExternException {
		return null;
	}

	@Override
	public RegistreAnotacio sortidaConsultar(
			String identificador,
			String entitat) throws SistemaExternException {
		return null;
	}

}
