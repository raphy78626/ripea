/**
 * 
 */
package es.caib.ripea.war.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.caib.ripea.core.api.dto.ArxiuDto;
import es.caib.ripea.core.api.dto.BustiaDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.service.ArxiuService;
import es.caib.ripea.core.api.service.ExpedientService;
import es.caib.ripea.core.api.service.MetaExpedientService;
import es.caib.ripea.war.command.ArxiuCommand;
import es.caib.ripea.war.command.ArxiuExpedientAgafarCommand;
import es.caib.ripea.war.command.ExpedientFiltreCommand;
import es.caib.ripea.war.datatable.DatatablesPagina;
import es.caib.ripea.war.helper.DatatablesHelper;
import es.caib.ripea.war.helper.DatatablesHelper.DatatablesResponse;
import es.caib.ripea.war.helper.PaginacioHelper;
import es.caib.ripea.war.helper.RequestSessionHelper;

/**
 * Controlador per al manteniment d'arxius.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/arxiuAdmin")
public class ArxiuAdminController extends BaseAdminController {

	private static final String SESSION_ATTRIBUTE_FILTRE = "ArxiuAdminController.session.filtre";

	@Autowired
	private ArxiuService arxiuService;
	@Autowired
	private ExpedientService expedientService;
	@Autowired
	private MetaExpedientService metaExpedientService;



	@RequestMapping(method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			Model model) {
		return get(request, null, model);
	}
	@RequestMapping(value = "/unitat/{unitatCodi}", method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			@PathVariable String unitatCodi,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"arbreUnitatsOrganitzatives",
				arxiuService.findArbreUnitatsOrganitzativesAdmin(
						entitatActual.getId(),
						false));
		model.addAttribute("unitatCodi", unitatCodi);
		return "arxiuAdminList";
	}

	/*@RequestMapping(value = "/unitat/{unitatCodi}/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesPagina<ArxiuDto> datatable(
			HttpServletRequest request,
			@PathVariable String unitatCodi,
			Model model) {
		if (!"null".equalsIgnoreCase(unitatCodi)) {
			EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
			List<ArxiuDto> arxius = arxiuService.findByUnitatCodiAdmin(
					entitatActual.getId(),
					unitatCodi);
			return PaginacioHelper.getPaginaPerDatatables(
					request,
					arxius);
		} else {
			return PaginacioHelper.getPaginaPerDatatables(
					request,
					new ArrayList<ArxiuDto>());
		}
	}*/
	@RequestMapping(value = "/unitat/{unitatCodi}/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesResponse datatable(
			HttpServletRequest request,
			@PathVariable String unitatCodi) {
		if (!"null".equalsIgnoreCase(unitatCodi)) {
			EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
			List<ArxiuDto> busties = arxiuService.findByUnitatCodiAdmin(
					entitatActual.getId(),
					unitatCodi);
			return DatatablesHelper.getDatatableResponse(
					request,
					busties);
		} else {
			return DatatablesHelper.getDatatableResponse(
					request,
					new ArrayList<BustiaDto>());
		}
	}

	@RequestMapping(value = "/unitat/{unitatCodi}/new", method = RequestMethod.GET)
	public String newGet(
			HttpServletRequest request,
			@PathVariable String unitatCodi,
			Model model) {
		String vista = formGet(request, null, model);
		ArxiuCommand command = (ArxiuCommand)model.asMap().get("arxiuCommand");
		command.setUnitatCodi(unitatCodi);
		return vista;
	}
	@RequestMapping(value = "/{arxiuId}", method = RequestMethod.GET)
	public String formGet(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ArxiuDto arxiu = null;
		if (arxiuId != null)
			arxiu = arxiuService.findById(
					entitatActual.getId(),
					arxiuId);
		ArxiuCommand command = null;
		if (arxiu != null)
			command = ArxiuCommand.asCommand(arxiu);
		else
			command = new ArxiuCommand();
		model.addAttribute(command);
		command.setEntitatId(entitatActual.getId());
		return "arxiuAdminForm";
	}
	@RequestMapping(method = RequestMethod.POST)
	public String save(
			HttpServletRequest request,
			@Valid ArxiuCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			return "arxiuAdminForm";
		}
		if (command.getId() != null) {
			arxiuService.update(
					entitatActual.getId(),
					ArxiuCommand.asDto(command));
			return getModalControllerReturnValueSuccess(
					request,
					"redirect:arxiuAdmin",
					"arxiu.controller.modificat.ok");
		} else {
			arxiuService.create(
					entitatActual.getId(),
					ArxiuCommand.asDto(command));
			return getModalControllerReturnValueSuccess(
					request,
					"redirect:arxiuAdmin",
					"arxiu.controller.creat.ok");
		}
	}
	@RequestMapping(value = "/{arxiuId}/new", method = RequestMethod.GET)
	public String getNewAmbPare(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ArxiuCommand command = new ArxiuCommand();
		command.setPareId(arxiuId);
		command.setEntitatId(entitatActual.getId());
		model.addAttribute(command);
		return "arxiuAdminForm";
	}

	@RequestMapping(value = "/{arxiuId}/enable", method = RequestMethod.GET)
	public String enable(
			HttpServletRequest request,
			@PathVariable Long arxiuId) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		arxiuService.updateActiu(
				entitatActual.getId(),
				arxiuId,
				true);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../arxiuAdmin",
				"arxiu.controller.activat.ok");
	}
	@RequestMapping(value = "/{arxiuId}/disable", method = RequestMethod.GET)
	public String disable(
			HttpServletRequest request,
			@PathVariable Long arxiuId) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		arxiuService.updateActiu(
				entitatActual.getId(),
				arxiuId,
				false);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../arxiuAdmin",
				"arxiu.controller.desactivat.ok");
	}

	@RequestMapping(value = "/{arxiuId}/delete", method = RequestMethod.GET)
	public String delete(
			HttpServletRequest request,
			@PathVariable Long arxiuId) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		arxiuService.delete(
				entitatActual.getId(),
				arxiuId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../arxiuAdmin",
				"arxiu.controller.esborrat.ok");
	}

	@RequestMapping(value = "/{arxiuId}/expedient", method = RequestMethod.GET)
	public String expedientGet(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"arxiu",
				arxiuService.findById(entitatActual.getId(), arxiuId));
		model.addAttribute(
				"metaExpedients",
				metaExpedientService.findByEntitat(entitatActual.getId()));
		model.addAttribute(
				getFiltreCommand(request, arxiuId));
		return "arxiuAdminExpedientList";
	}
	@RequestMapping(value = "/{arxiuId}/expedient", method = RequestMethod.POST)
	public String expedientPost(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			@Valid ExpedientFiltreCommand filtreCommand,
			BindingResult bindingResult,
			Model model) {
		if (!bindingResult.hasErrors()) {
			RequestSessionHelper.actualitzarObjecteSessio(
					request,
					SESSION_ATTRIBUTE_FILTRE,
					filtreCommand);
		}
		return "redirect:../../arxiuAdmin/" + arxiuId + "/expedient";
	}
	@RequestMapping(value = "/{arxiuId}/expedient/filtre", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void filtre(
			HttpServletRequest request,
			@Valid ExpedientFiltreCommand filtreCommand,
			BindingResult bindingResult,
			@RequestParam(value = "accio", required = false) String accio) {
		if ("netejar".equals(accio)) {
			RequestSessionHelper.esborrarObjecteSessio(
					request,
					SESSION_ATTRIBUTE_FILTRE);
		} else {
			RequestSessionHelper.actualitzarObjecteSessio(
					request,
					SESSION_ATTRIBUTE_FILTRE,
					filtreCommand);
		}
	}

	@RequestMapping(value = "/{arxiuId}/expedient/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesPagina<ExpedientDto> datatable(
			HttpServletRequest request,
			@PathVariable Long arxiuId) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ExpedientFiltreCommand filtreCommand = getFiltreCommand(request, arxiuId);
		return PaginacioHelper.getPaginaPerDatatables(
				request,
				expedientService.findPaginatAdmin(
						entitatActual.getId(),
						ExpedientFiltreCommand.asDto(filtreCommand),
						PaginacioHelper.getPaginacioDtoFromDatatable(
								request,
								null)));
	}

	@RequestMapping(value = "/{arxiuId}/expedient/netejar", method = RequestMethod.GET)
	public String expedientNetejar(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			Model model) {
		getEntitatActualComprovantPermisos(request);
		RequestSessionHelper.esborrarObjecteSessio(
				request,
				SESSION_ATTRIBUTE_FILTRE);
		return "redirect:../expedient";
	}

	@RequestMapping(value = "/{arxiuId}/expedient/{expedientId}/agafar", method = RequestMethod.GET)
	public String expedientAgafarGet(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			@PathVariable Long expedientId,
			Model model) {
		getEntitatActualComprovantPermisos(request);
		model.addAttribute(new ArxiuExpedientAgafarCommand(
				arxiuId,
				expedientId));
		return "arxiuAdminExpedientAgafarForm";
	}
	@RequestMapping(value = "/{arxiuId}/expedient/{expedientId}/agafar", method = RequestMethod.POST)
	public String expedientAgafarPost(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			@PathVariable Long expedientId,
			@Valid ArxiuExpedientAgafarCommand command,
			BindingResult bindingResult) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			return "arxiuAdminExpedientAgafarForm";
		}
		expedientService.agafarAdmin(
				entitatActual.getId(),
				arxiuId,
				expedientId,
				command.getUsuariCodi());
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../expedient",
				"arxiu.expedient.controller.agafat.ok");
	}

	@RequestMapping(value = "/{arxiuId}/expedient/{expedientId}/alliberar", method = RequestMethod.GET)
	public String expedientAlliberar(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			@PathVariable Long expedientId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		expedientService.alliberarAdmin(
				entitatActual.getId(),
				expedientId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../expedient",
				"arxiu.expedient.controller.alliberat.ok");
	}

	/*@RequestMapping(value = "/arbre", method = RequestMethod.GET)
	public String arbre(
			HttpServletRequest request,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"arbreUnitatsOrganitzatives",
				entitatService.findArbreUnitatsOrganitzatives(
						entitatActual.getId()));
		return "arxiuAdminArbre";
	}*/

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(
	    		Date.class,
	    		new CustomDateEditor(
	    				new SimpleDateFormat("dd/MM/yyyy"),
	    				true));
	}



	private ExpedientFiltreCommand getFiltreCommand(
			HttpServletRequest request,
			Long arxiuId) {
		ExpedientFiltreCommand filtreCommand = (ExpedientFiltreCommand)RequestSessionHelper.obtenirObjecteSessio(
				request,
				SESSION_ATTRIBUTE_FILTRE);
		if (filtreCommand == null || !filtreCommand.getArxiuId().equals(arxiuId)) {
			filtreCommand = new ExpedientFiltreCommand();
			filtreCommand.setArxiuId(arxiuId);
			RequestSessionHelper.actualitzarObjecteSessio(
					request,
					SESSION_ATTRIBUTE_FILTRE,
					filtreCommand);
		}
		return filtreCommand;
	}

}
