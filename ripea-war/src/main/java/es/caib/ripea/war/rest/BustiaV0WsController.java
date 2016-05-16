/**
 * 
 */
package es.caib.ripea.war.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.caib.ripea.core.api.service.ws.BustiaV0WsService;
import es.caib.ripea.core.api.service.ws.BustiaV0WsService.BustiaContingutTipus;

/**
 * Controlador per a les peticions al servei REST d'enviament
 * de contingut a una bústia.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/rest/v0/bustia")
public class BustiaV0WsController {

	@Autowired
	private BustiaV0WsService bustiaWs;

	@RequestMapping(method = RequestMethod.GET)
	public String get() {
		return "restBustia";
	}

	@RequestMapping(value = "/enviarContingut", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void enviarContingutPost(
			@RequestParam("entitat") String entitat,
			@RequestParam("unitatAdministrativa") String unitatAdministrativa,
			@RequestParam("tipus") BustiaContingutTipus tipus,
			@RequestParam("referencia") String referencia) {
		bustiaWs.enviarContingut(
				entitat,
				unitatAdministrativa,
				tipus,
				referencia);
	}

}
