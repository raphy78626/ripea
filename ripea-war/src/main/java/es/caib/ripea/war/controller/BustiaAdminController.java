/**
 * 
 */
package es.caib.ripea.war.controller;

import java.util.ArrayList;
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
import es.caib.ripea.core.api.service.BustiaService;
import es.caib.ripea.war.command.BustiaCommand;
import es.caib.ripea.war.datatable.DatatablesPagina;
import es.caib.ripea.war.helper.PaginacioHelper;

/**
 * Controlador per al manteniment de bústies.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/bustiaAdmin")
public class BustiaAdminController extends BaseAdminController {

	@Autowired
	private BustiaService bustiaService;



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
				bustiaService.findArbreUnitatsOrganitzatives(
						entitatActual.getId(),
						false,
						false));
		model.addAttribute("unitatCodi", unitatCodi);
		return "bustiaAdminList";
	}
	@RequestMapping(value = "/unitat/{unitatCodi}/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesPagina<BustiaDto> datatable(
			HttpServletRequest request,
			@PathVariable String unitatCodi,
			Model model) {
		if (!"null".equalsIgnoreCase(unitatCodi)) {
			EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
			List<BustiaDto> busties = bustiaService.findByUnitatCodiAdmin(
					entitatActual.getId(),
					unitatCodi);
			return PaginacioHelper.getPaginaPerDatatables(
					request,
					busties);
		} else {
			return PaginacioHelper.getPaginaPerDatatables(
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
		BustiaCommand command = (BustiaCommand)model.asMap().get("bustiaCommand");
		command.setUnitatCodi(unitatCodi);
		return vista;
	}
	@RequestMapping(value = "/{bustiaId}", method = RequestMethod.GET)
	public String formGet(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		BustiaDto bustia = null;
		if (bustiaId != null)
			bustia = bustiaService.findById(
					entitatActual.getId(),
					bustiaId);
		BustiaCommand command = null;
		if (bustia != null)
			command = BustiaCommand.asCommand(bustia);
		else
			command = new BustiaCommand();
		model.addAttribute(command);
		command.setEntitatId(entitatActual.getId());
		return "bustiaAdminForm";
	}
	@RequestMapping(method = RequestMethod.POST)
	public String save(
			HttpServletRequest request,
			@Valid BustiaCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			return "bustiaAdminForm";
		}
		if (command.getId() != null) {
			bustiaService.update(
					entitatActual.getId(),
					BustiaCommand.asDto(command));
			return getModalControllerReturnValueSuccess(
					request,
					"redirect:bustiaAdmin",
					"bustia.controller.modificat.ok");
		} else {
			bustiaService.create(
					entitatActual.getId(),
					BustiaCommand.asDto(command));
			return getModalControllerReturnValueSuccess(
					request,
					"redirect:bustiaAdmin",
					"bustia.controller.creat.ok");
		}
	}
	@RequestMapping(value = "/{bustiaId}/new", method = RequestMethod.GET)
	public String getNewAmbPare(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		BustiaCommand command = new BustiaCommand();
		command.setPareId(bustiaId);
		command.setEntitatId(entitatActual.getId());
		model.addAttribute(command);
		return "bustiaAdminForm";
	}

	@RequestMapping(value = "/{bustiaId}/enable", method = RequestMethod.GET)
	public String enable(
			HttpServletRequest request,
			@PathVariable Long bustiaId) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		bustiaService.updateActiva(
				entitatActual.getId(),
				bustiaId,
				true);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../bustiaAdmin",
				"bustia.controller.activat.ok");
	}
	@RequestMapping(value = "/{bustiaId}/disable", method = RequestMethod.GET)
	public String disable(
			HttpServletRequest request,
			@PathVariable Long bustiaId) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		bustiaService.updateActiva(
				entitatActual.getId(),
				bustiaId,
				false);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../bustiaAdmin",
				"bustia.controller.desactivat.ok");
	}

	@RequestMapping(value = "/{bustiaId}/default", method = RequestMethod.GET)
	public String defecte(
			HttpServletRequest request,
			@PathVariable Long bustiaId) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		bustiaService.marcarPerDefecte(
				entitatActual.getId(),
				bustiaId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../bustiaAdmin",
				"bustia.controller.defecte.ok");
	}

	@RequestMapping(value = "/{bustiaId}/delete", method = RequestMethod.GET)
	public String delete(
			HttpServletRequest request,
			@PathVariable Long bustiaId) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		bustiaService.delete(
				entitatActual.getId(),
				bustiaId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../bustiaAdmin",
				"bustia.controller.esborrat.ok");
	}

}
