/**
 * 
 */
package es.caib.ripea.war.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.EscriptoriDto;
import es.caib.ripea.core.api.service.ContenidorService;

/**
 * Controlador per a l'escriptori.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/escriptori")
public class EscriptoriController extends BaseUserController {
	
	@Autowired
	ContenidorService contenidorService;

	@RequestMapping(method = RequestMethod.GET)
	public String get(
			HttpServletRequest request) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		EscriptoriDto escriptori = contenidorService.getEscriptoriPerUsuariActual(entitatActual.getId());
		return "redirect:contenidor/" + escriptori.getId();
	}

}
