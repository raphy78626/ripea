/**
 * 
 */
package es.caib.ripea.war.rest;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.caib.ripea.core.api.dto.DocumentNotificacioDto;
import es.caib.ripea.core.api.service.ExpedientEnviamentService;
import es.caib.ripea.core.api.ws.callback.NotificacioEstatClient;
import es.caib.ripea.war.controller.BaseController;


/**
 * Controlador que exposa un servei REST per a la gestio de
 * notificacions.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/rest/v1/callback")
public class NotificacioCallbackController extends BaseController {

	@Autowired
	private ExpedientEnviamentService expedientEnviamentService;

	@RequestMapping(
			value = "/notificaEstat", 
			method = RequestMethod.POST,
			produces="application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody String altaEnviament(
			@RequestBody NotificacioEstatClient notificacioEstat,
			HttpServletResponse response) throws GeneralSecurityException, IOException {
		
		if (notificacioEstat.getReferenciaEnviament() != null && !notificacioEstat.getReferenciaEnviament().isEmpty()) {
			DocumentNotificacioDto notificacioActualitzada = expedientEnviamentService.notificacioUpdatePerReferencia(
					notificacioEstat.getReferenciaEnviament(),
					notificacioEstat.getEstat(),
					notificacioEstat.getData()
					);
			return notificacioActualitzada.getReferencia();
		} else {
			return null;
		}
		
	}
}
