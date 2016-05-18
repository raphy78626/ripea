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

import es.caib.ripea.core.api.registre.RegistreAnotacio;
import es.caib.ripea.core.api.service.ws.BustiaV1WsService;

/**
 * Controlador per a les peticions al servei REST d'enviament
 * de contingut a una bústia.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/rest/v1/bustia")
public class BustiaV1WsController {

	@Autowired
	private BustiaV1WsService bustiaWs;

	@RequestMapping(method = RequestMethod.GET)
	public String get() {
		return "restBustia";
	}

	@RequestMapping(value = "/enviarDocument", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void enviarDocumentPost(
			@RequestParam("entitat") String entitat,
			@RequestParam("unitatAdministrativa") String unitatAdministrativa,
			@RequestParam("referencia") String referencia) {
		bustiaWs.enviarDocument(
				entitat,
				unitatAdministrativa,
				referencia);
	}

	@RequestMapping(value = "/enviarExpedient", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void enviarExpedientPost(
			@RequestParam("entitat") String entitat,
			@RequestParam("unitatAdministrativa") String unitatAdministrativa,
			@RequestParam("referencia") String referencia) {
		bustiaWs.enviarExpedient(
				entitat,
				unitatAdministrativa,
				referencia);
	}

	@RequestMapping(value = "/enviarAnotacioRegistreEntrada", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void enviarAnotacioRegistreEntradaPost(
			@RequestParam("entitat") String entitat,
			@RequestParam("unitatAdministrativa") String unitatAdministrativa,
			@RequestParam("referencia") String referencia) {
		// TODO emplenar RegistroEntradaWs
		bustiaWs.enviarAnotacioRegistreEntrada(
				entitat,
				unitatAdministrativa,
				new RegistreAnotacio());
	}

}
