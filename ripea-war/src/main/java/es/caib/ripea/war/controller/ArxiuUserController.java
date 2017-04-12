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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.caib.ripea.core.api.dto.ArxiuDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.service.ArxiuService;
import es.caib.ripea.core.api.service.ContingutService;
import es.caib.ripea.core.api.service.ExpedientService;
import es.caib.ripea.core.api.service.MetaExpedientService;
import es.caib.ripea.war.command.ExpedientFiltreCommand;
import es.caib.ripea.war.datatable.DatatablesPagina;
import es.caib.ripea.war.helper.PaginacioHelper;
import es.caib.ripea.war.helper.RequestSessionHelper;

/**
 * Controlador per al manteniment d'arxius.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/arxiuUser")
public class ArxiuUserController extends BaseUserController {

	private static final String SESSION_ATTRIBUTE_FILTRE = "ArxiuUserController.session.filtre";

	@Autowired
	private ArxiuService arxiuService;
	@Autowired
	private ContingutService contenidorService;
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
				arxiuService.findArbreUnitatsOrganitzativesUser(
						entitatActual.getId()));
		model.addAttribute("unitatCodi", unitatCodi);
		return "arxiuUserList";
	}
	@RequestMapping(value = "/unitat/{unitatCodi}/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesPagina<ArxiuDto> datatable(
			HttpServletRequest request,
			@PathVariable String unitatCodi,
			Model model) {
		if (!"null".equalsIgnoreCase(unitatCodi)) {
			EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
			List<ArxiuDto> arxius = arxiuService.findByUnitatCodiUsuari(
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
	}

	@RequestMapping(value = "/{arxiuId}/expedient", method = RequestMethod.GET)
	public String expedientGet(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"arxiu",
				contenidorService.findAmbIdUser(
						entitatActual.getId(),
						arxiuId,
						false,
						false));
		model.addAttribute(
				"metaExpedients",
				metaExpedientService.findAmbEntitatPerLectura(entitatActual.getId()));
		model.addAttribute(
				getFiltreCommand(request, arxiuId));
		return "arxiuUserExpedientList";
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
				expedientService.findAmbFiltreUser(
						entitatActual.getId(),
						ExpedientFiltreCommand.asDto(filtreCommand),
						PaginacioHelper.getPaginacioDtoFromDatatable(
								request,
								null)));
	}

	@RequestMapping(value = "/{arxiuId}/expedient", method = RequestMethod.POST)
	public String expedientPost(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			@Valid ExpedientFiltreCommand filtreCommand,
			BindingResult bindingResult,
			Model model) {
		getEntitatActualComprovantPermisos(request);
		if (!bindingResult.hasErrors()) {
			RequestSessionHelper.actualitzarObjecteSessio(
					request,
					SESSION_ATTRIBUTE_FILTRE,
					filtreCommand);
		}
		return "redirect:../../arxiuUser/" + arxiuId + "/expedient";
	}

	@RequestMapping(value = "/{arxiuId}/expedient/netejar", method = RequestMethod.GET)
	public String expedientNetejar(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			Model model) {
		getEntitatActualComprovantPermisos(request);
		return "redirect:../expedient";
	}

	@RequestMapping(value = "/{arxiuId}/expedient/{expedientId}/agafar", method = RequestMethod.GET)
	public String expedientAgafar(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			@PathVariable Long expedientId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		expedientService.agafarUser(
				entitatActual.getId(),
				expedientId);
		return getAjaxControllerReturnValueSuccess(
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
		expedientService.alliberarUser(
				entitatActual.getId(),
				expedientId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../expedient",
				"arxiu.expedient.controller.alliberat.ok");
	}

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
