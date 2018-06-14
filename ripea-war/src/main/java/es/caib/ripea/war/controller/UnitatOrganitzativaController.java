/**
 * 
 */
package es.caib.ripea.war.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.caib.ripea.core.api.dto.BustiaDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.service.BustiaService;
import es.caib.ripea.core.api.service.UnitatOrganitzativaService;
import es.caib.ripea.core.entity.EntitatEntity;
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
	@Autowired
	private BustiaService bustiaService;


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
		unitatOrganitzativaService.synchronize(entitatActual.getId());

		return "redirect:/unitatOrganitzativa";
	}
	
	
	@RequestMapping(value = "/mostrarArbre", method = RequestMethod.GET)
	public String mostrarArbre(
			HttpServletRequest request,
			Model model) {
		
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		
		model.addAttribute(
				"arbreUnitatsOrganitzatives",
				unitatOrganitzativaService.findTree(entitatActual.getId()));
		
		return "unitatArbre";
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
	
	
	
	@RequestMapping(value = "/{unitatId}/unitatTransicioInfo", method = RequestMethod.GET)
	public String formGet(
			HttpServletRequest request,
			@PathVariable Long unitatId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		UnitatOrganitzativaDto unitat = null;
		if (unitatId != null)
			unitat = unitatOrganitzativaService.findById(
					unitatId);
		model.addAttribute(unitat);
		
		
		// getting all the busties connected with old unitat excluding the one you are currently in 
		List<BustiaDto> bustiesOfOldUnitat = bustiaService.findAmbUnitatCodiAdmin(entitatActual.getId(), unitat.getCodi());
		model.addAttribute("bustiesOfOldUnitat", bustiesOfOldUnitat);	

	
		return "unitatTransicioInfo";
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
