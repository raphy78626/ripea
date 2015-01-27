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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.caib.ripea.core.api.dto.ArxiuDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.service.ArxiuService;
import es.caib.ripea.war.command.PermisCommand;
import es.caib.ripea.war.datatable.DatatablesPagina;
import es.caib.ripea.war.helper.PaginacioHelper;

/**
 * Controlador per al manteniment de permisos de l'entitat
 * actual per a l'usuari administrador.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/arxiuAdmin")
public class ArxiuAdminPermisController extends BaseAdminController {

	@Autowired
	private ArxiuService arxiuService;



	@RequestMapping(value = "/{arxiuId}/permis", method = RequestMethod.GET)
	public String permis(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"arxiu",
				arxiuService.findById(entitatActual.getId(), arxiuId));
		return "arxiuAdminPermis";
	}
	@RequestMapping(value = "/{arxiuId}/permis/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesPagina<PermisDto> datatable(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		return PaginacioHelper.getPaginaPerDatatables(
				request,
				arxiuService.findById(entitatActual.getId(), arxiuId).getPermisos());
	}

	@RequestMapping(value = "/{arxiuId}/permis/new", method = RequestMethod.GET)
	public String getNew(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			Model model) {
		return get(request, arxiuId, null, model);
	}
	@RequestMapping(value = "/{arxiuId}/permis/{permisId}", method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			@PathVariable Long permisId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ArxiuDto arxiu = arxiuService.findById(entitatActual.getId(), arxiuId);
		model.addAttribute("arxiu", arxiu);
		PermisDto permis = null;
		if (permisId != null) {
			for (PermisDto p: arxiu.getPermisos()) {
				if (p.getId().equals(permisId)) {
					permis = p;
					break;
				}
			}
		}
		if (permis != null)
			model.addAttribute(PermisCommand.asCommand(permis));
		else
			model.addAttribute(new PermisCommand());
		return "arxiuAdminPermisForm";
	}

	@RequestMapping(value = "/{arxiuId}/permis", method = RequestMethod.POST)
	public String save(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			@Valid PermisCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			model.addAttribute(
					"arxiu",
					arxiuService.findById(entitatActual.getId(), arxiuId));
			return "arxiuAdminPermisForm";
		}
		arxiuService.updatePermis(
				entitatActual.getId(),
				arxiuId,
				PermisCommand.asDto(command));
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../arxiuAdmin/" + arxiuId + "/permis",
				"arxiu.controller.permis.modificat.ok");
	}

	@RequestMapping(value = "/{arxiuId}/permis/{permisId}/delete", method = RequestMethod.GET)
	public String delete(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			@PathVariable Long permisId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		arxiuService.deletePermis(
				entitatActual.getId(),
				arxiuId,
				permisId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../../../arxiuAdmin/" + arxiuId + "/permis",
				"arxiu.controller.permis.esborrat.ok");
	}

}
