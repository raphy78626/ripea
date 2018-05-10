/**
 * 
 */
package es.caib.ripea.war.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.service.UnitatOrganitzativaService;
import es.caib.ripea.war.command.UnitatOrganitzativaFiltreCommand;
import es.caib.ripea.war.helper.DatatablesHelper;
import es.caib.ripea.war.helper.DatatablesHelper.DatatablesResponse;
import es.caib.ripea.war.helper.RequestSessionHelper;

/**
 * Controlador per al manteniment de unitats organitzatives.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/unitatOrganitzativa")
public class UnitatOrganitzativaController extends BaseAdminController{
	
	private static final String SESSION_ATTRIBUTE_FILTRE = "UnitatOrganitzativaController.session.filtre";

	@Autowired
	private UnitatOrganitzativaService unitatOrganitzativaService;


	@RequestMapping(method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			Model model) {
		UnitatOrganitzativaFiltreCommand unitatOrganitzativaFiltreCommand = getFiltreCommand(request);
		model.addAttribute("unitatOrganitzativaFiltreCommand", unitatOrganitzativaFiltreCommand);
		return "unitatOrganitzativaList";
	}
	
	@RequestMapping(value = "/synchronize", method = RequestMethod.GET)
	public String synchronize(
			HttpServletRequest request) {
		
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		unitatOrganitzativaService.synchronize(entitatActual);

		return "unitatOrganitzativaList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String bustiaPost(
			HttpServletRequest request,
			@Valid UnitatOrganitzativaFiltreCommand filtreCommand,
			BindingResult bindingResult,
			Model model) {
		if (!bindingResult.hasErrors()) {
			RequestSessionHelper.actualitzarObjecteSessio(
					request,
					SESSION_ATTRIBUTE_FILTRE,
					filtreCommand);
		}
		return "redirect:unitatOrganitzativa";
	}

	@RequestMapping(value = "/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesResponse datatable(
			HttpServletRequest request) {

		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		UnitatOrganitzativaFiltreCommand unitatOrganitzativaFiltreCommand = getFiltreCommand(request);
		return DatatablesHelper.getDatatableResponse(
				request,
				unitatOrganitzativaService.findAmbFiltre(
						entitatActual.getId(),
						UnitatOrganitzativaFiltreCommand.asDto(unitatOrganitzativaFiltreCommand),
						DatatablesHelper.getPaginacioDtoFromRequest(request)),
				"id");
	}
	
	private UnitatOrganitzativaFiltreCommand getFiltreCommand(
			HttpServletRequest request) {
		UnitatOrganitzativaFiltreCommand unitatOrganitzativaFiltreCommand = (UnitatOrganitzativaFiltreCommand)RequestSessionHelper.obtenirObjecteSessio(
				request,
				SESSION_ATTRIBUTE_FILTRE);
		if (unitatOrganitzativaFiltreCommand == null) {
			unitatOrganitzativaFiltreCommand = new UnitatOrganitzativaFiltreCommand();
			RequestSessionHelper.actualitzarObjecteSessio(
					request,
					SESSION_ATTRIBUTE_FILTRE,
					unitatOrganitzativaFiltreCommand);
		}
		return unitatOrganitzativaFiltreCommand;
	}


}
