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

import es.caib.notib.domini.NotificacioCanviClient;
import es.caib.ripea.core.api.service.DocumentEnviamentService;

/**
 * Controlador per a les peticions al servei REST de Notib
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/rest/notib")
public class NotibWsController {

	@Autowired
	private DocumentEnviamentService documentEnviamentService;

	@RequestMapping(method = RequestMethod.GET)
	public String get() {
		return "restNotib";
	}

	@RequestMapping(value = "/notificaCanvi", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void enviarContingutPost(
			@RequestParam("notificacioCanvi") NotificacioCanviClient notificacioCanvi) {
		documentEnviamentService.notificacioActualitzarEstat(
				notificacioCanvi.getIdentificador(), 
				notificacioCanvi.getReferenciaEnviament());
	}
	
//	public ResponseEntity<Void> canvi(@RequestBody NotificacioCanviClient notificacioCanvi, UriComponentsBuilder ucBuilder) {
//		 
//    	System.out.println("Rebut: \n"
//				+ "\t\t identificador: " + notificacioCanvi.getIdentificador() + "\n"
//				+ "\t\t referencia: " + notificacioCanvi.getReferenciaEnviament());
//    	
//        return new ResponseEntity<Void>(HttpStatus.OK);
//    }

}
