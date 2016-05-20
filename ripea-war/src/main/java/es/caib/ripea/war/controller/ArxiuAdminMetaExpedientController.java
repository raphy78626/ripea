/**
 * 
 */
package es.caib.ripea.war.controller;

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
import es.caib.ripea.war.command.ArxiuMetaExpedientCommand;
import es.caib.ripea.war.command.ArxiuMetaExpedientCommand.Create;
import es.caib.ripea.war.datatable.DatatablesPagina;
import es.caib.ripea.war.helper.PaginacioHelper;

/**
 * Controlador per al manteniment de meta-expedients de l'entitat
 * actual per a l'usuari administrador.
 * Permet associar meta-expedients a un arxiu
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/arxiuAdmin")
public class ArxiuAdminMetaExpedientController extends BaseAdminController {

	@Autowired
	private ArxiuService arxiuService;
	@Autowired
	private MetaExpedientService metaExpedientService;



	@RequestMapping(value = "/{arxiuId}/metaExpedient", method = RequestMethod.GET)
	public String metaExpedient(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"arxiu",
				arxiuService.findById(entitatActual.getId(), arxiuId));
		return "arxiuAdminMetaExpedient";
	}
	@RequestMapping(value = "/{arxiuId}/metaExpedient/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesPagina<MetaExpedientDto> datatable(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		return PaginacioHelper.getPaginaPerDatatables(
				request,
				arxiuService.findById(entitatActual.getId(), arxiuId).getMetaExpedients());
	}

	@RequestMapping(value = "/{arxiuId}/metaExpedient/new", method = RequestMethod.GET)
	public String getNew(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		
		ArxiuDto arxiu = arxiuService.findById(entitatActual.getId(), arxiuId);
		model.addAttribute("arxiu", arxiu);
		// Consulta els meta-expedients en els que té permisos
		List<MetaExpedientDto> metaExpedients = metaExpedientService.findActiveByEntitatPerCreacio(entitatActual.getId());
		model.addAttribute(
				"metaExpedients",
				metaExpedients
				);
		model.addAttribute(new ArxiuMetaExpedientCommand());

		return "arxiuAdminMetaExpedientForm";
	}

	@RequestMapping(value = "/{arxiuId}/metaExpedient", method = RequestMethod.POST)
	public String save(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			@Validated({Create.class}) ArxiuMetaExpedientCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ArxiuDto arxiu = arxiuService.findById(entitatActual.getId(), arxiuId);
		if (bindingResult.hasErrors()) {
			model.addAttribute(
					"arxiu",
					arxiu);
			// Consulta els meta-expedients en els que té permisos
			List<MetaExpedientDto> metaExpedients = metaExpedientService.findActiveByEntitatPerCreacio(entitatActual.getId());
			model.addAttribute(
					"metaExpedients",
					metaExpedients
					);
			return "arxiuAdminMetaExpedientForm";
		}
		// Comprova si la relació ja existeix
		MetaExpedientDto metaExpedient = metaExpedientService.findById(entitatActual.getId(), command.getMetaExpedientId());
		if(arxiu.getMetaExpedients().contains(metaExpedient)) {
			return getModalControllerReturnValueSuccess(
					request,
					"redirect:../../arxiuAdmin/" + arxiuId + "/metaExpedient",
					"arxiu.controller.metaexpedient.afegit.existent");
		}
		// Afegeix la relació
		arxiuService.addMetaExpedient(
				entitatActual.getId(),
				arxiuId,
				command.getMetaExpedientId());
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../arxiuAdmin/" + arxiuId + "/metaExpedient",
				"arxiu.controller.metaexpedient.afegit.ok");
	}

	@RequestMapping(value = "/{arxiuId}/metaExpedient/{metaExpedientId}/delete", method = RequestMethod.GET)
	public String delete(
			HttpServletRequest request,
			@PathVariable Long arxiuId,
			@PathVariable Long metaExpedientId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		arxiuService.removeMetaExpedient(
				entitatActual.getId(),
				arxiuId,
				metaExpedientId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../../../arxiuAdmin/" + arxiuId + "/metaExpedient",
				"arxiu.controller.metaexpedient.esborrat.ok");
	}

}
