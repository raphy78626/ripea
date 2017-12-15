/**
 * 
 */
package es.caib.ripea.war.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.caib.ripea.core.api.dto.ArxiuDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.service.ArxiuService;
import es.caib.ripea.core.api.service.ExpedientService;
import es.caib.ripea.core.api.service.MetaExpedientService;
import es.caib.ripea.war.command.ContenidorCommand.Create;
import es.caib.ripea.war.command.ContenidorCommand.Update;
import es.caib.ripea.war.command.ExpedientCommand;

/**
 * Controlador per al manteniment d'expedients.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/contingut")
public class ContingutExpedientController extends BaseUserController {

	@Autowired
	private ExpedientService expedientService;
	@Autowired
	private MetaExpedientService metaExpedientService;
	@Autowired
	private ArxiuService arxiuService;

	@RequestMapping(value = "/{contingutId}/expedient/new", method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			Model model) {
		return get(request, contingutId, null, model);
	}
	@RequestMapping(value = "/{contingutId}/expedient/{expedientId}", method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			@PathVariable Long expedientId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ExpedientDto expedient = null;
		if (expedientId != null) {
			expedient = expedientService.findById(
					entitatActual.getId(),
					expedientId);
		}
		ExpedientCommand command = null;
		if (expedient != null) {
			command = ExpedientCommand.asCommand(expedient);
		} else {
			command = new ExpedientCommand();
			command.setAny(Calendar.getInstance().get(Calendar.YEAR));
		}
		command.setEntitatId(entitatActual.getId());
		command.setPareId(contingutId);
		model.addAttribute(command);
		model.addAttribute(
				"metaExpedients",
				metaExpedientService.findActiusAmbEntitatPerCreacio(entitatActual.getId()));
		if (command.getMetaNodeId() != null) {	
			model.addAttribute(
					"arxius",
					arxiuService.findPermesosPerUsuariIMetaExpedient(
							entitatActual.getId(),
							command.getMetaNodeId()));
		} else {
			model.addAttribute("arxius", new ArrayList<ArxiuDto>());
		}
		return "contingutExpedientForm";
	}
	@RequestMapping(value = "/{contingutId}/expedient/new", method = RequestMethod.POST)
	public String postNew(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			@Validated({Create.class}) ExpedientCommand command,
			BindingResult bindingResult,
			Model model) throws IOException {
		return postUpdate(
				request,
				contingutId,
				command,
				bindingResult,
				model);
	}
	@RequestMapping(value = "/{contingutId}/expedient/update", method = RequestMethod.POST)
	public String postUpdate(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			@Validated({Update.class}) ExpedientCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			model.addAttribute(
					"metaExpedients",
					metaExpedientService.findActiusAmbEntitatPerCreacio(entitatActual.getId()));
			if(command.getMetaNodeId() != null) {				
				model.addAttribute(
						"arxius",
						arxiuService.findPermesosPerUsuariIMetaExpedient(
								entitatActual.getId(),
								command.getMetaNodeId()));
			} else {
				model.addAttribute("arxius", new ArrayList<ArxiuDto>());
			}
			return "contingutExpedientForm";
		}
		if (command.getId() == null) {
			expedientService.create(
					entitatActual.getId(),
					contingutId,
					command.getMetaNodeId(),
					command.getArxiuId(),
					command.getAny(),
					command.getNom(),
					null,
					null);
			return getModalControllerReturnValueSuccess(
					request,
					"redirect:../../../contingut/" + contingutId,
					"expedient.controller.creat.ok");
		} else {
			expedientService.update(
					entitatActual.getId(),
					command.getId(),
					command.getArxiuId(),
					command.getMetaNodeId(),
					command.getNom());
			return getModalControllerReturnValueSuccess(
					request,
					"redirect:../../../contingut/" + contingutId,
					"expedient.controller.modificat.ok");
		}
	}

}
