/**
 * 
 */
package es.caib.ripea.war.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.war.helper.EntitatHelper;
import es.caib.ripea.war.helper.RolHelper;

/**
 * Controlador per a la pàgina inicial (index).
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
public class IndexController {

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String get(
			HttpServletRequest request) {
		if (RolHelper.isUsuariActualSuperusuari(request)) {
			return "redirect:entitat";
		} else {
			EntitatDto entitat = EntitatHelper.getEntitatActual(request);
			if (entitat == null)
				throw new SecurityException("No te cap entitat assignada");
			if (RolHelper.isUsuariActualAdministrador(request)) {
				return "redirect:metaExpedient";
			} else if (RolHelper.isUsuariActualUsuari(request)) {
				return "redirect:escriptori";
			} else {
				return "index";
			}
		}
	}

}
