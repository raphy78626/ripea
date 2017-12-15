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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.caib.ripea.core.api.dto.ComunitatDto;
import es.caib.ripea.core.api.dto.LocalitatDto;
import es.caib.ripea.core.api.dto.MunicipiDto;
import es.caib.ripea.core.api.dto.NivellAdministracioDto;
import es.caib.ripea.core.api.dto.ProvinciaDto;
import es.caib.ripea.core.api.dto.ProvinciaRw3Dto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.service.DadesExternesService;
import es.caib.ripea.core.api.service.UnitatsOrganitzativesService;
import es.caib.ripea.war.command.UnitatsFiltreCommand;
import es.caib.ripea.war.helper.DatatablesHelper;
import es.caib.ripea.war.helper.DatatablesHelper.DatatablesResponse;
import es.caib.ripea.war.helper.RequestSessionHelper;

/**
 * Controlador per a les consultes ajax dels usuaris normals.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/unitatajax") // No podem posar "/ajaxuser" per mor del AjaxInterceptor
public class AjaxUnitatsController extends BaseAdminController {
	
	private static final String SESSION_ATTRIBUTE_FILTRE = "AjaxUnitatsController.session.filtre";

	@Autowired
	private DadesExternesService dadesExternesService;
	@Autowired
	private UnitatsOrganitzativesService unitatsOrganitzativesService;

	@RequestMapping(value = "/unitat", method = RequestMethod.POST)
	@ResponseBody
	public UnitatOrganitzativaDto getByCodi(
			HttpServletRequest request,
			@RequestParam String codi,
			Model model) {
		return unitatsOrganitzativesService.findUnitatOrganitzativaByCodi(codi);
	}

	@RequestMapping(value = "/unitats", method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			Model model) {
		UnitatsFiltreCommand unitatsFiltreCommand = getFiltreCommand(request);
		unitatsFiltreCommand.setNivellAdministracio("3");
		unitatsFiltreCommand.setComunitat("4");
		unitatsFiltreCommand.setProvincia("7");
		model.addAttribute("unitatsFiltreCommand", unitatsFiltreCommand);
		List<NivellAdministracioDto> nivellsAdmin = dadesExternesService.findNivellAdministracions();
		List<ComunitatDto> comunitats = dadesExternesService.findComunitats();
		List<ProvinciaDto> provincies = dadesExternesService.findProvinciesPerComunitat(unitatsFiltreCommand.getComunitat());
		List<MunicipiDto> localitats = dadesExternesService.findMunicipisPerProvincia(unitatsFiltreCommand.getProvincia());
		model.addAttribute("nivellsAdmin", nivellsAdmin);
		model.addAttribute("comunitats", comunitats);
		model.addAttribute("provincies", provincies);
		model.addAttribute("localitats", localitats);
		
		return "unitatSearch";
	}
	
	@RequestMapping(value = "/unitats/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesResponse datatable(
			HttpServletRequest request) {
		UnitatsFiltreCommand unitatsFiltreCommand = getFiltreCommand(request);
		return DatatablesHelper.getDatatableResponse(
				request,
				unitatsOrganitzativesService.findUnitatsOrganitzativesPerDatatable(
						UnitatsFiltreCommand.asDto(unitatsFiltreCommand), 
						null),
				"codigo");
	}
	
	@RequestMapping(value = "/unitats", method = RequestMethod.POST)
	public String unitatsPost(
			HttpServletRequest request,
			@Valid UnitatsFiltreCommand unitatsFiltreCommand,
			BindingResult bindingResult,
			Model model) {
		if (!bindingResult.hasErrors()) {
			RequestSessionHelper.actualitzarObjecteSessio(
					request,
					SESSION_ATTRIBUTE_FILTRE,
					unitatsFiltreCommand);
		}
		return "redirect:/unitatajax/unitats";
	}
	
	@RequestMapping(value = "/localitats", method = RequestMethod.POST)
	@ResponseBody
	public List<LocalitatDto> getLocalitats(
			HttpServletRequest request,
			@RequestParam String codiProvincia,
			Model model) {
		
		return unitatsOrganitzativesService.findLocalitatsPerProvincia(codiProvincia);
	}
	
	@RequestMapping(value = "/provincies", method = RequestMethod.POST)
	@ResponseBody
	public List<ProvinciaRw3Dto> getProvincies(
			HttpServletRequest request,
			@RequestParam String codiComunitat,
			Model model) {
		
		return unitatsOrganitzativesService.findProvinciesPerComunitat(codiComunitat);
	}
	
	private UnitatsFiltreCommand getFiltreCommand(
			HttpServletRequest request) {
		UnitatsFiltreCommand unitatsFiltreCommand = (UnitatsFiltreCommand)RequestSessionHelper.obtenirObjecteSessio(
				request,
				SESSION_ATTRIBUTE_FILTRE);
		if (unitatsFiltreCommand == null) {
			unitatsFiltreCommand = new UnitatsFiltreCommand();
			RequestSessionHelper.actualitzarObjecteSessio(
					request,
					SESSION_ATTRIBUTE_FILTRE,
					unitatsFiltreCommand);
		}
		return unitatsFiltreCommand;
	}
}
