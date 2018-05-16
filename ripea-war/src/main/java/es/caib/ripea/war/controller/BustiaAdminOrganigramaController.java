package es.caib.ripea.war.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.caib.ripea.core.api.dto.BustiaDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.service.BustiaService;
import es.caib.ripea.war.command.BustiaCommand;
import es.caib.ripea.war.command.BustiaFiltreCommand;
import es.caib.ripea.war.helper.RequestSessionHelper;

/**
 * Controlador per al manteniment de bústies.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/bustiaAdminOrganigrama")
public class BustiaAdminOrganigramaController extends BaseAdminController {
	
	private static final String SESSION_ATTRIBUTE_FILTRE = "BustiaAdminOrganigramaController.session.filtre";

	@Autowired
	private BustiaService bustiaService;


	@RequestMapping(method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			Model model,
			Long bustiaId) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		BustiaFiltreCommand bustiaFiltreCommand = getFiltreCommand(request);
		model.addAttribute("bustiaFiltreCommand", bustiaFiltreCommand);
		List<BustiaDto> busties = bustiaService.findActivesAmbEntitat(
				entitatActual.getId());
		model.addAttribute(
				"busties",
				busties);
		model.addAttribute(
				"arbreUnitatsOrganitzatives",
				bustiaService.findArbreUnitatsOrganitzatives(
						entitatActual.getId(),
						true,
						false,
						true));
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
		model.addAttribute("bustiaCommand", command);
		command.setEntitatId(entitatActual.getId());
		return "bustiaAdminOrganigrama";
	}

//	@RequestMapping(method = RequestMethod.POST)
//	public String bustiaPost(
//			HttpServletRequest request,
//			@Valid BustiaFiltreCommand filtreCommand,
//			BindingResult bindingResult,
//			Model model) {
//		if (!bindingResult.hasErrors()) {
//			RequestSessionHelper.actualitzarObjecteSessio(
//					request,
//					SESSION_ATTRIBUTE_FILTRE,
//					filtreCommand);
//		}
//		return "redirect:bustiaAdmin";
//	}
//
//	@RequestMapping(value = "/new", method = RequestMethod.GET)
//	public String newGet(
//			HttpServletRequest request,
//			Model model) {
//		String vista = formGet(request, null, model);
////		BustiaCommand command = (BustiaCommand)model.asMap().get("bustiaCommand");
////		command.setUnitatCodi(unitatCodi);
//		return vista;
//	}
//	@RequestMapping(value = "/{bustiaId}", method = RequestMethod.GET)
//	public String formGet(
//			HttpServletRequest request,
//			@PathVariable Long bustiaId,
//			Model model) {
//		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
//		BustiaDto bustia = null;
//		if (bustiaId != null)
//			bustia = bustiaService.findById(
//					entitatActual.getId(),
//					bustiaId);
//		BustiaCommand command = null;
//		if (bustia != null)
//			command = BustiaCommand.asCommand(bustia);
//		else
//			command = new BustiaCommand();
//		model.addAttribute(command);
//		command.setEntitatId(entitatActual.getId());
//		return "bustiaAdminForm";
//	}
//	
//	@RequestMapping(value = "/new", method = RequestMethod.POST)
//	public String save(
//			HttpServletRequest request,
//			@Valid BustiaCommand command,
//			BindingResult bindingResult,
//			Model model) {
//		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
//		if (bindingResult.hasErrors()) {
//			return "bustiaAdminForm";
//		}
//		if (command.getId() != null) {
//			bustiaService.update(
//					entitatActual.getId(),
//					BustiaCommand.asDto(command));
//			return getModalControllerReturnValueSuccess(
//					request,
//					"redirect:bustiaAdmin",
//					"bustia.controller.modificat.ok");
//		} else {
//			bustiaService.create(
//					entitatActual.getId(),
//					BustiaCommand.asDto(command));
//			return getModalControllerReturnValueSuccess(
//					request,
//					"redirect:bustiaAdmin",
//					"bustia.controller.creat.ok");
//		}
//	}
//	
//	@RequestMapping(value = "/{bustiaId}/new", method = RequestMethod.GET)
//	public String getNewAmbPare(
//			HttpServletRequest request,
//			@PathVariable Long bustiaId,
//			Model model) {
//		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
//		BustiaCommand command = new BustiaCommand();
//		command.setPareId(bustiaId);
//		command.setEntitatId(entitatActual.getId());
//		model.addAttribute(command);
//		return "bustiaAdminForm";
//	}
//
//	@RequestMapping(value = "/{bustiaId}/enable", method = RequestMethod.GET)
//	public String enable(
//			HttpServletRequest request,
//			@PathVariable Long bustiaId) {
//		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
//		bustiaService.updateActiva(
//				entitatActual.getId(),
//				bustiaId,
//				true);
//		return getAjaxControllerReturnValueSuccess(
//				request,
//				"redirect:../../bustiaAdmin",
//				"bustia.controller.activat.ok");
//	}
//	@RequestMapping(value = "/{bustiaId}/disable", method = RequestMethod.GET)
//	public String disable(
//			HttpServletRequest request,
//			@PathVariable Long bustiaId) {
//		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
//		bustiaService.updateActiva(
//				entitatActual.getId(),
//				bustiaId,
//				false);
//		return getAjaxControllerReturnValueSuccess(
//				request,
//				"redirect:../../bustiaAdmin",
//				"bustia.controller.desactivat.ok");
//	}
//
//	@RequestMapping(value = "/{bustiaId}/default", method = RequestMethod.GET)
//	public String defecte(
//			HttpServletRequest request,
//			@PathVariable Long bustiaId) {
//		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
//		bustiaService.marcarPerDefecte(
//				entitatActual.getId(),
//				bustiaId);
//		return getAjaxControllerReturnValueSuccess(
//				request,
//				"redirect:../../bustiaAdmin",
//				"bustia.controller.defecte.ok");
//	}
//
//	@RequestMapping(value = "/{bustiaId}/delete", method = RequestMethod.GET)
//	public String delete(
//			HttpServletRequest request,
//			@PathVariable Long bustiaId) {
//		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
//		try {
//			bustiaService.delete(
//					entitatActual.getId(),
//					bustiaId);
//			return getAjaxControllerReturnValueSuccess(
//					request,
//					"redirect:../../bustiaAdmin",
//					"bustia.controller.esborrat.ok");
//		} catch (RuntimeException ve) {
//			return getAjaxControllerReturnValueError(
//					request,
//					"redirect:../../bustiaAdmin",
//					"bustia.controller.esborrat.error.validacio",
//					new Object[] {ve.getMessage()});			
//		}
//	}
//
//	@RequestMapping(value = "/findAmbEntitat", method = RequestMethod.GET)
//	@ResponseBody
//	public List<BustiaDto> findAmbEntitat(
//			HttpServletRequest request,
//			Model model) {
//		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
//		return bustiaService.findActivesAmbEntitat(
//				entitatActual.getId());
//	}

	private BustiaFiltreCommand getFiltreCommand(
			HttpServletRequest request) {
		BustiaFiltreCommand bustiaFiltreCommand = (BustiaFiltreCommand)RequestSessionHelper.obtenirObjecteSessio(
				request,
				SESSION_ATTRIBUTE_FILTRE);
		if (bustiaFiltreCommand == null) {
			bustiaFiltreCommand = new BustiaFiltreCommand();
			RequestSessionHelper.actualitzarObjecteSessio(
					request,
					SESSION_ATTRIBUTE_FILTRE,
					bustiaFiltreCommand);
		}
		return bustiaFiltreCommand;
	}
}
