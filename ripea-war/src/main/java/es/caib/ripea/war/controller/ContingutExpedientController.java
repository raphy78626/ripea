/**
 * 
 */
package es.caib.ripea.war.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;

import es.caib.ripea.core.api.dto.ArxiuDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.EscriptoriDto;
import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.service.ArxiuService;
import es.caib.ripea.core.api.service.ContingutService;
import es.caib.ripea.core.api.service.ExpedientService;
import es.caib.ripea.core.api.service.MetaExpedientService;
import es.caib.ripea.war.command.ContenidorCommand.Create;
import es.caib.ripea.war.command.ContenidorCommand.Update;
import es.caib.ripea.war.command.ExpedientAcumularCommand;
import es.caib.ripea.war.command.ExpedientCommand;
import es.caib.ripea.war.command.ExpedientFiltreCommand;
import es.caib.ripea.war.command.ExpedientFiltreCommand.ExpedientFiltreOpcionsEstatEnum;
import es.caib.ripea.war.command.ExpedientRelacionarCommand;
import es.caib.ripea.war.command.ExpedientRelacionarCommand.Relacionar;
import es.caib.ripea.war.command.ExpedientTancarCommand;
import es.caib.ripea.war.helper.DatatablesHelper;
import es.caib.ripea.war.helper.DatatablesHelper.DatatablesResponse;
import es.caib.ripea.war.helper.MissatgesHelper;

/**
 * Controlador per al manteniment d'expedients.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/contingut")
public class ContingutExpedientController extends BaseUserController {

	@Autowired
	private ContingutService contingutService;
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

	@RequestMapping(value = "/{contingutId}/expedient/{expedientId}/alliberar", method = RequestMethod.GET)
	public String expedientAlliberar(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			@PathVariable Long expedientId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		expedientService.alliberarUser(
				entitatActual.getId(),
				expedientId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../../../contingut/" + contingutId,
				"expedient.controller.alliberat.ok");
	}

	@RequestMapping(value = "/{contingutId}/expedient/{expedientId}/tancar", method = RequestMethod.GET)
	public String expedientTancarGet(
			HttpServletRequest request,
			@PathVariable Long contingutId,
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
		return "contingutExpedientTancarForm";
	}
	@RequestMapping(value = "/{contingutId}/expedient/{expedientId}/tancar", method = RequestMethod.POST)
	public String expedientTancarPost(
			HttpServletRequest request,
			@PathVariable Long contingutId,
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
			return "contingutExpedientTancarForm";
		}
		expedientService.tancar(
				entitatActual.getId(),
				expedientId,
				command.getMotiu());
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../../../contingut/" + contingutId,
				"expedient.controller.tancar.ok");
	}

	@RequestMapping(value = "/{contingutId}/expedient/{expedientId}/relacionar", method = RequestMethod.GET)
	public String expedientRelacionarGet(
			HttpServletRequest request,
			@PathVariable Long contingutId,
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
		model.addAttribute("contingutId", contingutId);
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
		return "contingutExpedientRelacionarForm";
	}
	@RequestMapping(value = "/{contingutId}/expedient/{expedientId}/relacionar", method = RequestMethod.POST)
	public String expedientRelacionarPost(
			HttpServletRequest request,
			@PathVariable Long contingutId,
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
			
			return "contingutExpedientRelacionarForm";
		}
		expedientService.relacioCreate(
				entitatActual.getId(),
				command.getExpedientId(),
				command.getRelacionatId());
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../../../contingut/" + contingutId,
				"expedient.controller.relacionat.ok");
	}
	@RequestMapping(value = "/{contingutId}/expedient/{expedientId}/relacio/{expedientRelacionatId}/delete", method = RequestMethod.GET)
	public String expedientRelacioDelete(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			@PathVariable Long expedientId,
			@PathVariable Long expedientRelacionatId) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (expedientService.relacioDelete(
				entitatActual.getId(),
				expedientId,
				expedientRelacionatId)) {
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
		return "redirect:/contenidor/" + expedientId;
	}

	@RequestMapping(value = "/{contingutId}/expedient/{expedientId}/relacionar/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesResponse datatable(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			ExpedientFiltreCommand filtre) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		return DatatablesHelper.getDatatableResponse(
				request,
				expedientService.findPaginatUser(
						entitatActual.getId(), 
						ExpedientFiltreCommand.asDto(filtre), 
						DatatablesHelper.getPaginacioDtoFromRequest(request)));		
	}

	@RequestMapping(value = "/{contingutId}/expedient/{expedientId}/acumular", method = RequestMethod.GET)
	public String expedientAcumularGet(
			HttpServletRequest request,
			@PathVariable Long contingutId,
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
		return "contingutExpedientAcumularForm";
	}
	@RequestMapping(value = "/{contingutId}/expedient/{expedientId}/acumular", method = RequestMethod.POST)
	public String expedientAcumularPost(
			HttpServletRequest request,
			@PathVariable Long contingutId,
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
			return "contingutExpedientAcumularForm";
		}
		expedientService.acumular(
				entitatActual.getId(),
				command.getExpedientId(),
				command.getExpedientAcumulatId());
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../../../contingut/" + contingutId,
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
