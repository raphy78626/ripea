/**
 * 
 */
package es.caib.ripea.war.controller;

import java.io.IOException;

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

import es.caib.ripea.core.api.dto.DocumentEnviamentEstatEnumDto;
import es.caib.ripea.core.api.dto.DocumentNotificacioTipusEnumDto;
import es.caib.ripea.core.api.dto.DocumentPublicacioTipusEnumDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.service.ExpedientEnviamentService;
import es.caib.ripea.core.api.service.ExpedientInteressatService;
import es.caib.ripea.war.command.DocumentNotificacioCommand;
import es.caib.ripea.war.command.DocumentPublicacioCommand;
import es.caib.ripea.war.helper.DatatablesHelper;
import es.caib.ripea.war.helper.DatatablesHelper.DatatablesResponse;
import es.caib.ripea.war.helper.EnumHelper;
import es.caib.ripea.war.helper.MissatgesHelper;

/**
 * Controlador per als enviaments dels expedients.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/expedient")
public class ExpedientEnviamentController extends BaseUserController {

	@Autowired
	private ExpedientEnviamentService expedientEnviamentService;
	@Autowired
	private ExpedientInteressatService expedientInteressatService;



	@RequestMapping(value = "/{expedientId}/enviament/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesResponse enviamentDatatable(
			HttpServletRequest request,
			@PathVariable Long expedientId) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		return DatatablesHelper.getDatatableResponse(
				request,
				expedientEnviamentService.enviamentFindAmbExpedient(
						entitatActual.getId(), 
						expedientId));		
	}

	@RequestMapping(value = "/{expedientId}/notificacio/{notificacioId}/info")
	public String notificacioInfo(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@PathVariable Long notificacioId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"notificacio",
				expedientEnviamentService.notificacioFindAmbId(
						entitatActual.getId(),
						expedientId,
						notificacioId));
		return "notificacioInfo";
	}

	@RequestMapping(value = "/{expedientId}/notificacio/{notificacioId}", method = RequestMethod.GET)
	public String notificacioUpdateGet(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@PathVariable Long notificacioId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		emplenarModelNotificacio(
				request,
				expedientId,
				model);
		DocumentNotificacioCommand command = DocumentNotificacioCommand.asCommand(
				expedientEnviamentService.notificacioFindAmbId(
						entitatActual.getId(),
						expedientId,
						notificacioId));
		model.addAttribute(command);
		return "notificacioForm";
	}
	@RequestMapping(value = "/{expedientId}/notificacio/{notificacioId}", method = RequestMethod.POST)
	public String notificacioUpdatePost(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@PathVariable Long notificacioId,
			@Valid DocumentNotificacioCommand command,
			BindingResult bindingResult,
			Model model) throws IOException {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			emplenarModelNotificacio(
					request,
					expedientId,
					model);
			return "notificacioForm";
		}
		expedientEnviamentService.notificacioUpdate(
				entitatActual.getId(),
				expedientId,
				DocumentNotificacioCommand.asDto(command));
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../contingut/" + expedientId,
				"expedient.controller.notificacio.modificada.ok");
	}

	@RequestMapping(value = "/{expedientId}/notificacio/{notificacioId}/delete", method = RequestMethod.GET)
	public String notificacioDelete(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@PathVariable Long notificacioId) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		expedientEnviamentService.notificacioDelete(
				entitatActual.getId(),
				expedientId,
				notificacioId);
		return this.getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../../../contingut/" + expedientId,
				"expedient.controller.notificacio.esborrada.ok");
	}

	@RequestMapping(value = "/{expedientId}/notificacio/{notificacioId}/reintentar")
	public String notificacioReintentar(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@PathVariable Long notificacioId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		boolean fetaAmbExit = expedientEnviamentService.notificacioRetry(
				entitatActual.getId(),
				expedientId,
				notificacioId);
		if (fetaAmbExit) {
			MissatgesHelper.success(
					request, 
					getMessage(
							request, 
							"expedient.controller.notificacio.reintent.ok"));
		} else {
			MissatgesHelper.error(
					request, 
					getMessage(
							request, 
							"expedient.controller.notificacio.reintent.error"));
		}
		return "redirect:info";
	}

	@RequestMapping(value = "/{expedientId}/publicacio/{publicacioId}/info")
	public String publicacioInfo(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@PathVariable Long publicacioId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"publicacio",
				expedientEnviamentService.publicacioFindAmbId(
						entitatActual.getId(),
						expedientId,
						publicacioId));
		return "publicacioInfo";
	}

	@RequestMapping(value = "/{expedientId}/publicacio/{publicacioId}", method = RequestMethod.GET)
	public String publicacioUpdateGet(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@PathVariable Long publicacioId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		emplenarModelPublicacio(
				request,
				model);
		DocumentPublicacioCommand command = DocumentPublicacioCommand.asCommand(
				expedientEnviamentService.publicacioFindAmbId(
						entitatActual.getId(),
						expedientId,
						publicacioId));
		model.addAttribute(command);
		return "publicacioForm";
	}
	@RequestMapping(value = "/{expedientId}/publicacio/{publicacioId}", method = RequestMethod.POST)
	public String publicacioUpdatePost(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@PathVariable Long publicacioId,
			@Valid DocumentPublicacioCommand command,
			BindingResult bindingResult,
			Model model) throws IOException {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			emplenarModelPublicacio(
					request,
					model);
			return "publicacioForm";
		}
		expedientEnviamentService.publicacioUpdate(
				entitatActual.getId(),
				expedientId,
				DocumentPublicacioCommand.asDto(command));
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../contingut/" + expedientId,
				"expedient.controller.publicacio.modificada.ok");
	}

	@RequestMapping(value = "/{expedientId}/publicacio/{publicacioId}/delete", method = RequestMethod.GET)
	public String publicacioDelete(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@PathVariable Long publicacioId) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		expedientEnviamentService.publicacioDelete(
				entitatActual.getId(),
				expedientId,
				publicacioId);
		return this.getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../../../contingut/" + expedientId,
				"expedient.controller.publicacio.esborrada.ok");
	}


	private void emplenarModelNotificacio(
			HttpServletRequest request,
			Long expedientId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"notificacioTipusEnumOptions",
				EnumHelper.getOptionsForEnum(
						DocumentNotificacioTipusEnumDto.class,
						"notificacio.tipus.enum."));
		model.addAttribute(
				"enviamentEstatEnumOptions",
				EnumHelper.getOptionsForEnum(
						DocumentEnviamentEstatEnumDto.class,
						"enviament.estat.enum."));
		model.addAttribute(
				"interessats",
				expedientInteressatService.findByExpedient(
						entitatActual.getId(),
						expedientId));
		model.addAttribute(
				"expedientId",
				expedientId);
	}

	private void emplenarModelPublicacio(
			HttpServletRequest request,
			Model model) {
		model.addAttribute(
				"publicacioTipusEnumOptions",
				EnumHelper.getOptionsForEnum(
						DocumentPublicacioTipusEnumDto.class,
						"publicacio.tipus.enum."));
	}

}
