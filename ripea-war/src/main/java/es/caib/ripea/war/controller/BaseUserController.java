/**
 * 
 */
package es.caib.ripea.war.controller;

import javax.servlet.http.HttpServletRequest;

import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.war.helper.EntitatHelper;


/**
 * Controlador base que implementa funcionalitats comunes per
 * als controladors de les accions de l'administrador.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class BaseUserController extends BaseController {

	public EntitatDto getEntitatActualComprovantPermisos(
			HttpServletRequest request) {
		EntitatDto entitat = EntitatHelper.getEntitatActual(request);
		if (entitat == null)
			throw new SecurityException("No te cap entitat assignada");
		if (!entitat.isUsuariActualRead())
			throw new SecurityException("No te permisos per accedir a aquesta entitat com a usuari");
		return entitat;
	}

}
