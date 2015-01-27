/**
 * 
 */
package es.caib.ripea.war.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controlador per a les pàgines d'utilitat.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping(value = "/util")
public class UtilController {

	@RequestMapping(value = "/modalTancar", method = RequestMethod.GET)
	public String modalTancar() {
		return "util/modalTancar";
	}
	@RequestMapping(value = "/ajaxOk", method = RequestMethod.GET)
	public String ajaxOk() {
		return "util/ajaxOk";
	}
	@RequestMapping(value = "/alertes", method = RequestMethod.GET)
	public String get() {
		return "util/alertes";
	}

}
