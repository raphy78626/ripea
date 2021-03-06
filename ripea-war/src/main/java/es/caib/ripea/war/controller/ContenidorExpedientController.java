/**
 * 
 */
package es.caib.ripea.war.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.EscriptoriDto;
import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.service.ArxiuService;
import es.caib.ripea.core.api.service.ContenidorService;
import es.caib.ripea.core.api.service.ExpedientService;
import es.caib.ripea.core.api.service.MetaExpedientService;
import es.caib.ripea.war.command.ContenidorCommand.Create;
import es.caib.ripea.war.command.ContenidorCommand.Update;
import es.caib.ripea.war.command.ExpedientAcumularCommand;
import es.caib.ripea.war.command.ExpedientCommand;
import es.caib.ripea.war.command.ExpedientFinalitzarCommand;

/**
 * Controlador per al manteniment d'expedients dels contenidors.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/contenidor")
public class ContenidorExpedientController extends BaseUserController {

	@Autowired
	private ContenidorService contenidorService;
	@Autowired
	private ExpedientService expedientService;
	@Autowired
	private MetaExpedientService metaExpedientService;
	@Autowired
	private ArxiuService arxiuService;



	@RequestMapping(value = "/{contenidorId}/expedient/new", method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			Model model) {
		return get(request, contenidorId, null, model);
	}
	@RequestMapping(value = "/{contenidorId}/expedient/{expedientId}", method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
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
		command.setPareId(contenidorId);
		model.addAttribute(command);
		model.addAttribute(
				"metaExpedients",
				metaExpedientService.findActiveByEntitatPerCreacio(entitatActual.getId()));
		model.addAttribute(
				"arxius",
				arxiuService.findPermesosPerUsuari(entitatActual.getId()));
		return "contenidorExpedientForm";
	}
	@RequestMapping(value = "/{contenidorId}/expedient/new", method = RequestMethod.POST)
	public String postNew(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@Validated({Create.class}) ExpedientCommand command,
			BindingResult bindingResult,
			Model model) throws IOException {
		return postUpdate(
				request,
				contenidorId,
				command,
				bindingResult,
				model);
	}
	@RequestMapping(value = "/{contenidorId}/expedient/update", method = RequestMethod.POST)
	public String postUpdate(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@Validated({Update.class}) ExpedientCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			model.addAttribute(
					"arxius",
					arxiuService.findPermesosPerUsuari(entitatActual.getId()));
			model.addAttribute(
					"metaExpedients",
					metaExpedientService.findActiveByEntitatPerCreacio(entitatActual.getId()));
			return "contenidorExpedientForm";
		}
		if (command.getId() == null) {
			expedientService.create(
					entitatActual.getId(),
					contenidorId,
					command.getMetaNodeId(),
					command.getArxiuId(),
					command.getAny(),
					command.getNom(),
					command.getContingutId(),
					command.getRegistreId());
			return getModalControllerReturnValueSuccess(
					request,
					"redirect:../../../contenidor/" + contenidorId,
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
					"redirect:../../../contenidor/" + contenidorId,
					"expedient.controller.modificat.ok");
		}
	}

	@RequestMapping(value = "/{contenidorId}/expedient/{expedientId}/alliberar", method = RequestMethod.GET)
	public String expedientAlliberar(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long expedientId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		expedientService.alliberarUser(
				entitatActual.getId(),
				expedientId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../../../contenidor/" + contenidorId,
				"expedient.controller.alliberat.ok");
	}

	@RequestMapping(value = "/{contenidorId}/expedient/{expedientId}/finalitzar", method = RequestMethod.GET)
	public String expedientFinalitzarGet(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long expedientId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ExpedientFinalitzarCommand command = new ExpedientFinalitzarCommand();
		command.setId(expedientId);
		model.addAttribute(command);
		model.addAttribute(
				"expedient",
				contenidorService.getContenidorAmbContingut(
						entitatActual.getId(),
						expedientId));
		return "contenidorExpedientFinalitzarForm";
	}
	@RequestMapping(value = "/{contenidorId}/expedient/{expedientId}/finalitzar", method = RequestMethod.POST)
	public String expedientFinalitzarPost(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long expedientId,
			@Valid ExpedientFinalitzarCommand command,
			BindingResult bindingResult,
			Model model) throws IOException {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			model.addAttribute(
					"expedient",
					contenidorService.getContenidorAmbContingut(
							entitatActual.getId(),
							expedientId));
			return "contenidorExpedientFinalitzarForm";
		}
		expedientService.finalitzar(
				entitatActual.getId(),
				expedientId,
				command.getMotiu());
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../../../contenidor/" + contenidorId,
				"expedient.controller.finalitzat.ok");
	}

	@RequestMapping(value = "/{contenidorId}/expedient/{expedientId}/acumular", method = RequestMethod.GET)
	public String expedientAcumularGet(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long expedientId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ExpedientAcumularCommand command = new ExpedientAcumularCommand();
		command.setExpedientId(expedientId);
		model.addAttribute(command);
		model.addAttribute(
				"expedient",
				contenidorService.getContenidorAmbContingut(
						entitatActual.getId(),
						expedientId));
		EscriptoriDto escriptori = contenidorService.getEscriptoriPerUsuariActual(entitatActual.getId());
		model.addAttribute(
				"contenidorOrigen",
				escriptori);
		return "contenidorExpedientAcumularForm";
	}
	@RequestMapping(value = "/{contenidorId}/expedient/{expedientId}/acumular", method = RequestMethod.POST)
	public String expedientAcumularPost(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long expedientId,
			@Valid ExpedientAcumularCommand command,
			BindingResult bindingResult,
			Model model) throws IOException {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		EscriptoriDto escriptori = contenidorService.getEscriptoriPerUsuariActual(entitatActual.getId());
		if (bindingResult.hasErrors()) {
			model.addAttribute(
					"expedient",
					contenidorService.getContenidorAmbContingut(
							entitatActual.getId(),
							expedientId));
			model.addAttribute(
					"contenidorOrigen",
					escriptori);
			return "contenidorExpedientAcumularForm";
		}
		expedientService.acumular(
				entitatActual.getId(),
				command.getExpedientId(),
				command.getExpedientAcumulatId());
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../../../contenidor/" + contenidorId,
				"expedient.controller.acumulat.ok");
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(
	    		Date.class,
	    		new CustomDateEditor(
	    				new SimpleDateFormat("dd/MM/yyyy"),
	    				true));
	}

}
