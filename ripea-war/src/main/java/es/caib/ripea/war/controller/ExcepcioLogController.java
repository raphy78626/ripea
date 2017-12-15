/**
 * 
 */
package es.caib.ripea.war.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.caib.ripea.core.api.service.AplicacioService;
import es.caib.ripea.war.helper.DatatablesHelper;
import es.caib.ripea.war.helper.DatatablesHelper.DatatablesResponse;

/**
 * Controlador per a la consulta del log d'excepcions.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/excepcio")
public class ExcepcioLogController extends BaseUserController {

	@Autowired
	private AplicacioService aplicacioService;



	@RequestMapping(method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			Model model) {
		return "excepcio";
	}

	@RequestMapping(value = "/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesResponse datatable(
			HttpServletRequest request) {
		DatatablesResponse dtr = DatatablesHelper.getDatatableResponse(
				request,
				aplicacioService.excepcioFindAll());
		return dtr;
	}

	@RequestMapping(value = "/{index}", method = RequestMethod.GET)
	public String detall(
			HttpServletRequest request,
			@PathVariable Long index,
			Model model) {
		model.addAttribute("excepcio", aplicacioService.excepcioFindOne(index));
		return "excepcioDetall";
	}

}
