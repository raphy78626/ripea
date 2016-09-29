/**
 * 
 */
package es.caib.ripea.war.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.ResponseBody;

import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.EscriptoriDto;
import es.caib.ripea.core.api.service.ArxiuService;
import es.caib.ripea.core.api.service.ContingutService;
import es.caib.ripea.core.api.service.ExpedientService;
import es.caib.ripea.core.api.service.MetaExpedientService;
import es.caib.ripea.war.command.ExpedientAcumularCommand;
import es.caib.ripea.war.command.ExpedientFiltreCommand;
import es.caib.ripea.war.command.ExpedientFiltreCommand.ExpedientFiltreOpcionsEstatEnum;
import es.caib.ripea.war.command.ExpedientRelacionarCommand;
import es.caib.ripea.war.command.ExpedientRelacionarCommand.Relacionar;
import es.caib.ripea.war.command.ExpedientTancarCommand;
import es.caib.ripea.war.helper.DatatablesHelper;
import es.caib.ripea.war.helper.DatatablesHelper.DatatablesResponse;
import es.caib.ripea.war.helper.MissatgesHelper;

/**
 * Controlador per al llistat d'expedients dels usuaris.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/expedient")
public class ExpedientController extends BaseUserController {

	@Autowired
	private ContingutService contingutService;
	@Autowired
	private ExpedientService expedientService;
	@Autowired
	private MetaExpedientService metaExpedientService;
	@Autowired
	private ArxiuService arxiuService;



	@RequestMapping(value = "/{expedientId}/agafar", method = RequestMethod.GET)
	public String agafar(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		expedientService.agafarUser(
				entitatActual.getId(),
				expedientId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../contingut/" + expedientId,
				"expedient.controller.agafat.ok");
	}

	@RequestMapping(value = "/{expedientId}/alliberar", method = RequestMethod.GET)
	public String expedientAlliberar(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		expedientService.alliberarUser(
				entitatActual.getId(),
				expedientId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../contingut/" + expedientId,
				"expedient.controller.alliberat.ok");
	}

	@RequestMapping(value = "/{expedientId}/tancar", method = RequestMethod.GET)
	public String expedientTancarGet(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ExpedientTancarCommand command = new ExpedientTancarCommand();
		command.setId(expedientId);
		model.addAttribute(command);
		model.addAttribute(
				"expedient",
				contingutService.findAmbIdUser(
						entitatActual.getId(),
						expedientId,
						true));
		return "expedientTancarForm";
	}
	@RequestMapping(value = "/{expedientId}/tancar", method = RequestMethod.POST)
	public String expedientTancarPost(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@Valid ExpedientTancarCommand command,
			BindingResult bindingResult,
			Model model) throws IOException {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			model.addAttribute(
					"expedient",
					contingutService.findAmbIdUser(
							entitatActual.getId(),
							expedientId,
							true));
			return "expedientTancarForm";
		}
		expedientService.tancar(
				entitatActual.getId(),
				expedientId,
				command.getMotiu());
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../contingut/" + expedientId,
				"expedient.controller.tancar.ok");
	}

	@RequestMapping(value = "/{expedientId}/relacionar", method = RequestMethod.GET)
	public String expedientRelacionarGet(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ExpedientRelacionarCommand command = new ExpedientRelacionarCommand();
		command.setEntitatId(entitatActual.getId());
		command.setExpedientId(expedientId);
		model.addAttribute(command);
		model.addAttribute(
				"expedient",
				contingutService.findAmbIdUser(
						entitatActual.getId(),
						expedientId,
						true));
		EscriptoriDto escriptori = contingutService.getEscriptoriPerUsuariActual(entitatActual.getId());
		model.addAttribute(
				"contenidorOrigen",
				escriptori);
		model.addAttribute("expedientId", expedientId);
		ExpedientFiltreCommand filtre = new ExpedientFiltreCommand();
		filtre.setEstatFiltre(ExpedientFiltreOpcionsEstatEnum.TOTS);
		model.addAttribute(filtre);
		model.addAttribute(
				"arxius",
				arxiuService.findPermesosPerUsuari(
						entitatActual.getId()));
		model.addAttribute(
				"metaExpedients",
				metaExpedientService.findAmbEntitatPerLectura(
						entitatActual.getId()));
		return "expedientRelacionarForm";
	}
	@RequestMapping(value = "/{expedientId}/relacionar", method = RequestMethod.POST)
	public String expedientRelacionarPost(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			ExpedientFiltreCommand filtre,
			@Validated(Relacionar.class) ExpedientRelacionarCommand command,
			BindingResult bindingResult,
			Model model) throws IOException {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		EscriptoriDto escriptori = contingutService.getEscriptoriPerUsuariActual(entitatActual.getId());
		if (bindingResult.hasErrors()) {
			model.addAttribute(
					"expedient",
					contingutService.findAmbIdUser(
							entitatActual.getId(),
							expedientId,
							true));
			model.addAttribute(
					"contenidorOrigen",
					escriptori);
			model.addAttribute(filtre);
			return "expedientRelacionarForm";
		}
		expedientService.relacioCreate(
				entitatActual.getId(),
				command.getExpedientId(),
				command.getRelacionatId());
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:/../../contingut/" + expedientId,
				"expedient.controller.relacionat.ok");
	}
	@RequestMapping(value = "/{expedientId}/relacio/{relacionatId}/delete", method = RequestMethod.GET)
	public String expedientRelacioDelete(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@PathVariable Long relacionatId) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (expedientService.relacioDelete(
				entitatActual.getId(),
				expedientId,
				relacionatId)) {
			MissatgesHelper.success(
					request, 
					getMessage(
							request, 
							"expedient.controller.relacio.esborrada.ok"));
		} else {
			MissatgesHelper.error(
					request, 
					getMessage(
							request, 
							"expedient.controller.relacio.esborrada.error"));
		}
		return "redirect:/contingut/" + expedientId;
	}

	@RequestMapping(value = "/{expedientId}/relacio/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesResponse relacioDatatable(
			HttpServletRequest request,
			ExpedientFiltreCommand filtre) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		return DatatablesHelper.getDatatableResponse(
				request,
				expedientService.findPaginatUser(
						entitatActual.getId(), 
						ExpedientFiltreCommand.asDto(filtre), 
						DatatablesHelper.getPaginacioDtoFromRequest(request)));		
	}

	@RequestMapping(value = "/{expedientId}/acumular", method = RequestMethod.GET)
	public String expedientAcumularGet(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ExpedientAcumularCommand command = new ExpedientAcumularCommand();
		command.setExpedientId(expedientId);
		model.addAttribute(command);
		model.addAttribute(
				"expedient",
				contingutService.findAmbIdUser(
						entitatActual.getId(),
						expedientId,
						true));
		EscriptoriDto escriptori = contingutService.getEscriptoriPerUsuariActual(entitatActual.getId());
		model.addAttribute(
				"contingutOrigen",
				escriptori);
		return "expedientAcumularForm";
	}
	@RequestMapping(value = "/{expedientId}/acumular", method = RequestMethod.POST)
	public String expedientAcumularPost(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@Valid ExpedientAcumularCommand command,
			BindingResult bindingResult,
			Model model) throws IOException {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		EscriptoriDto escriptori = contingutService.getEscriptoriPerUsuariActual(entitatActual.getId());
		if (bindingResult.hasErrors()) {
			model.addAttribute(
					"expedient",
					contingutService.findAmbIdUser(
							entitatActual.getId(),
							expedientId,
							true));
			model.addAttribute(
					"contingutOrigen",
					escriptori);
			return "expedientAcumularForm";
		}
		expedientService.acumular(
				entitatActual.getId(),
				command.getExpedientId(),
				command.getExpedientAcumulatId());
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../contingut/" + expedientId,
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
