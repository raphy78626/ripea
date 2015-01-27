/**
 * 
 */
package es.caib.ripea.war.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

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

import es.caib.ripea.core.api.dto.CarpetaDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.service.ArxiuService;
import es.caib.ripea.core.api.service.CarpetaService;
import es.caib.ripea.core.api.service.ContenidorService;
import es.caib.ripea.war.command.CarpetaCommand;
import es.caib.ripea.war.command.ContenidorCommand.Create;
import es.caib.ripea.war.command.ContenidorCommand.Update;

/**
 * Controlador per al manteniment de carpetes dels contenidors.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/contenidor")
public class ContenidorCarpetaController extends BaseUserController {

	@Autowired
	private ContenidorService contenidorService;
	@Autowired
	private CarpetaService carpetaService;
	@Autowired
	private ArxiuService arxiuService;



	@RequestMapping(value = "/{contenidorId}/carpeta/new", method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			Model model) {
		return get(request, contenidorId, null, model);
	}
	@RequestMapping(value = "/{contenidorId}/carpeta/{carpetaId}", method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long carpetaId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		CarpetaDto carpeta = null;
		if (carpetaId != null) {
			carpeta = carpetaService.findById(
					entitatActual.getId(),
					carpetaId);
		}
		CarpetaCommand command = null;
		if (carpeta != null)
			command = CarpetaCommand.asCommand(carpeta);
		else
			command = new CarpetaCommand();
		command.setEntitatId(entitatActual.getId());
		command.setPareId(contenidorId);
		model.addAttribute(command);
		return "contenidorCarpetaForm";
	}

	@RequestMapping(value = "/{contenidorId}/carpeta/new", method = RequestMethod.POST)
	public String postNew(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@Validated({Create.class}) CarpetaCommand command,
			BindingResult bindingResult,
			Model model) {
		return postUpdate(
				request,
				contenidorId,
				command,
				bindingResult,
				model);
	}
	@RequestMapping(value = "/{contenidorId}/carpeta/update", method = RequestMethod.POST)
	public String postUpdate(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@Validated({Update.class}) CarpetaCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			return "contenidorCarpetaForm";
		}
		if (command.getId() == null) {
			carpetaService.create(
					entitatActual.getId(),
					contenidorId,
					command.getNom(),
					command.getTipus());
			return getModalControllerReturnValueSuccess(
					request,
					"redirect:../../../contenidor/" + contenidorId,
					"carpeta.controller.creada.ok");
		} else {
			carpetaService.update(
					entitatActual.getId(),
					command.getId(),
					command.getNom(),
					command.getTipus());
			return getModalControllerReturnValueSuccess(
					request,
					"redirect:../../../contenidor/" + command.getPareId(),
					"carpeta.controller.modificada.ok");
		}
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
