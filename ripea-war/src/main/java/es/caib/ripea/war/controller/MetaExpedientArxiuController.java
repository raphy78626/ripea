/**
 * 
 */
package es.caib.ripea.war.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.caib.ripea.core.api.dto.ArxiuDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.MetaExpedientDto;
import es.caib.ripea.core.api.service.ArxiuService;
import es.caib.ripea.core.api.service.MetaExpedientService;
import es.caib.ripea.war.command.MetaExpedientArxiuCommand;
import es.caib.ripea.war.command.MetaExpedientArxiuCommand.Create;
import es.caib.ripea.war.datatable.DatatablesPagina;
import es.caib.ripea.war.helper.PaginacioHelper;

/**
 * Controlador per al manteniment d'arxius per a un meta-expedient.
 * Permet associar arxius a un meta-expedient.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/metaExpedient")
public class MetaExpedientArxiuController extends BaseAdminController {

	@Autowired
	private ArxiuService arxiuService;
	@Autowired
	private MetaExpedientService metaExpedientService;



	@RequestMapping(value = "/{metaExpedientId}/arxiu", method = RequestMethod.GET)
	public String metaExpedient(
			HttpServletRequest request,
			@PathVariable Long metaExpedientId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"metaExpedient",
				metaExpedientService.findById(entitatActual.getId(), metaExpedientId));
		return "metaExpedientArxiu";
	}
	@RequestMapping(value = "/{metaExpedientId}/arxiu/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesPagina<ArxiuDto> datatable(
			HttpServletRequest request,
			@PathVariable Long metaExpedientId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		List<ArxiuDto> arxius = metaExpedientService.findArxiusMetaExpedient(entitatActual.getId(), metaExpedientId);
		return PaginacioHelper.getPaginaPerDatatables(
				request,
				arxius);
	}

	/** Mètode Ajax per obtenir tots els arxius per a un node. */
	@RequestMapping(value = "/{metaExpedientId}/unitat/{unitatCodi}/arxiu/findAll", method = RequestMethod.GET)
	@ResponseBody
	public List<ArxiuDto> findAllArxius(
			HttpServletRequest request,
			@PathVariable Long metaExpedientId,
			@PathVariable String unitatCodi,
			Model model) {
		
		if (!"null".equalsIgnoreCase(unitatCodi)) {
			EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
			List<ArxiuDto> arxius = arxiuService.findByUnitatCodiAdmin(
					entitatActual.getId(),
					unitatCodi);
			return arxius;
		} else {
			return new ArrayList<ArxiuDto>();
		}
	}


	@RequestMapping(value = "/{metaExpedientId}/arxiu/new", method = RequestMethod.GET)
	public String getNew(
			HttpServletRequest request,
			@PathVariable Long metaExpedientId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		
		MetaExpedientDto metaExpedient = metaExpedientService.findById(entitatActual.getId(), metaExpedientId);
		model.addAttribute("metaExpedient", metaExpedient);

		// Arbre d'unitats organtizatives
		model.addAttribute(
				"arbreUnitatsOrganitzatives",
				arxiuService.findArbreUnitatsOrganitzativesAdmin(
						entitatActual.getId(),
						true));
		model.addAttribute(new MetaExpedientArxiuCommand());

		return "metaExpedientArxiuForm";
	}

	@RequestMapping(value = "/{metaExpedientId}/arxiu", method = RequestMethod.POST)
	public String save(
			HttpServletRequest request,
			@PathVariable Long metaExpedientId,
			@Validated({Create.class}) MetaExpedientArxiuCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		MetaExpedientDto metaExpedient = metaExpedientService.findById(entitatActual.getId(), metaExpedientId);
		if (bindingResult.hasErrors()) {
			model.addAttribute(
					"metaExpedient",
					metaExpedient);
			
			// Arbre d'unitats organtizatives
			model.addAttribute(
					"arbreUnitatsOrganitzatives",
					arxiuService.findArbreUnitatsOrganitzativesAdmin(
							entitatActual.getId(),
							true));			
			
			return "metaExpedientArxiuForm";
		}
		// Comprova si la relació ja existia
		ArxiuDto arxiu = arxiuService.findById(entitatActual.getId(), command.getArxiuId());
		List<ArxiuDto> arxius = metaExpedientService.findArxiusMetaExpedient(entitatActual.getId(), metaExpedientId);
		if(arxius.contains(arxiu)) {
			return getModalControllerReturnValueSuccess(
					request,
					"redirect:../../metaExpedient/" + metaExpedientId + "/arxiu",
					"metaexpedient.controller.arxiu.afegit.existent");
		}
		// Afegeix la relació
		metaExpedientService.addArxiu(
					entitatActual.getId(),
					metaExpedientId,
					command.getArxiuId());
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../metaExpedient/" + metaExpedientId + "/arxiu",
				"metaexpedient.controller.arxiu.afegit.ok");
	}

	@RequestMapping(value = "/{metaExpedientId}/arxiu/{arxiuId}/delete", method = RequestMethod.GET)
	public String delete(
			HttpServletRequest request,
			@PathVariable Long metaExpedientId,
			@PathVariable Long arxiuId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		metaExpedientService.removeArxiu(
				entitatActual.getId(),
				metaExpedientId,
				arxiuId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../../../metaExpedient/" + arxiuId + "/arxiu",
				"metaexpedient.controller.arxiu.esborrat.ok");
	}

}
