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

import es.caib.ripea.core.api.service.ws.BustiaWs;
import es.caib.ripea.core.api.service.ws.BustiaWs.BustiaContingutTipus;

/**
 * Controlador per a les peticions al servei REST d'enviament
 * de contingut a una bústia.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/rest/bustia")
public class BustiaWsController {

	@Autowired
	private BustiaWs bustiaWs;

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
