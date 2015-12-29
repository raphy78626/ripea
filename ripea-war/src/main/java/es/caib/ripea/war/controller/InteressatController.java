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

import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.InteressatAdministracioDto;
import es.caib.ripea.core.api.dto.InteressatCiutadaDto;
import es.caib.ripea.core.api.service.InteressatService;
import es.caib.ripea.war.command.InteressatCommand;
import es.caib.ripea.war.command.InteressatCommand.Administracio;
import es.caib.ripea.war.command.InteressatCommand.Ciutada;
import es.caib.ripea.war.command.InteressatCommand.ComprovarAdministracio;
import es.caib.ripea.war.command.InteressatCommand.ComprovarCiutada;
import es.caib.ripea.war.helper.AlertHelper;

/**
 * Controlador per als interessats dels expedients.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/expedient")
public class InteressatController extends BaseUserController {

	@Autowired
	private InteressatService interessatService;



	@RequestMapping(value = "/{expedientId}/interessat/new", method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		InteressatCommand command = new InteressatCommand();
		command.setEntitatId(entitatActual.getId());
		model.addAttribute(command);
		InteressatCommand emptyCommand = new InteressatCommand();
		emptyCommand.setEntitatId(entitatActual.getId());
		model.addAttribute("emptyCommand", emptyCommand);
		model.addAttribute("expedientId", expedientId);
		return "expedientInteressatForm";
	}

	@RequestMapping(value="/{expedientId}/interessatComprovarCiutada", method = RequestMethod.POST)
	public String postComprovarCiutada(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@Validated({ComprovarCiutada.class}) InteressatCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		InteressatCommand emptyCommand = new InteressatCommand();
		emptyCommand.setEntitatId(entitatActual.getId());
		model.addAttribute("emptyCommand", emptyCommand);
		model.addAttribute("expedientId", expedientId);
		if (!bindingResult.hasErrors()) {
			List<InteressatCiutadaDto> interessats = interessatService.findByFiltreCiutada(
					entitatActual.getId(),
					null,
					command.getNif(),
					null);
			model.addAttribute("interessats", interessats);
			command.setComprovat(true);
			if (!interessats.isEmpty())
				command.setId(interessats.get(0).getId());
			AlertHelper.info(
					request, 
					getMessage(
							request, 
							(interessats.size() > 0) ? "interessat.controller.trobats.ciutada" : "interessat.controller.no.trobats.ciutada"));
		}
		return "expedientInteressatForm";
	}
	@RequestMapping(value="/{expedientId}/interessatComprovarAdministracio", method = RequestMethod.POST)
	public String postComprovarAdministracio(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@Validated({ComprovarAdministracio.class}) InteressatCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		InteressatCommand emptyCommand = new InteressatCommand();
		emptyCommand.setEntitatId(entitatActual.getId());
		model.addAttribute("emptyCommand", emptyCommand);
		model.addAttribute("expedientId", expedientId);
		if (!bindingResult.hasErrors()) {
			List<InteressatAdministracioDto> interessats = interessatService.findByFiltreAdministracio(
					entitatActual.getId(),
					null,
					command.getIdentificador());
			command.setComprovat(true);
			if (!interessats.isEmpty())
				command.setId(interessats.get(0).getId());
			model.addAttribute("interessats", interessats);
			AlertHelper.info(
					request, 
					getMessage(
							request, 
							(interessats.size() > 0) ? "interessat.controller.trobats.administracio" : "interessat.controller.no.trobats.administracio"));
		}
		return "expedientInteressatForm";
	}

	@RequestMapping(value="/{expedientId}/interessatCiutada", method = RequestMethod.POST)
	public String postCiutada(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@Validated({Ciutada.class}) InteressatCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			InteressatCommand emptyCommand = new InteressatCommand();
			emptyCommand.setEntitatId(entitatActual.getId());
			model.addAttribute("emptyCommand", emptyCommand);
			model.addAttribute("expedientId", expedientId);
			if (command.isComprovat()) {
				List<InteressatCiutadaDto> interessats = interessatService.findByFiltreCiutada(
						entitatActual.getId(),
						null,
						command.getNif(),
						null);
				model.addAttribute("interessats", interessats);
			}
			return "expedientInteressatForm";
		}
		if (command.getId() == null) {
			interessatService.create(
					entitatActual.getId(),
					expedientId,
					InteressatCommand.asCiutadaDto(command));
		} else {
			interessatService.addToExpedient(
					entitatActual.getId(),
					expedientId,
					command.getId());
		}
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../contenidor/" + expedientId,
				"interessat.controller.afegit.ok");
	}
	@RequestMapping(value="/{expedientId}/interessatAdministracio", method = RequestMethod.POST)
	public String postAdministracio(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@Validated({Administracio.class}) InteressatCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			InteressatCommand emptyCommand = new InteressatCommand();
			emptyCommand.setEntitatId(entitatActual.getId());
			model.addAttribute("emptyCommand", emptyCommand);
			model.addAttribute("expedientId", expedientId);
			if (command.isComprovat()) {
				List<InteressatAdministracioDto> interessats = interessatService.findByFiltreAdministracio(
						entitatActual.getId(),
						null,
						command.getIdentificador());
				model.addAttribute("interessats", interessats);
			}
			return "expedientInteressatForm";
		}
		if (command.getId() == null) {
			interessatService.create(
					entitatActual.getId(),
					expedientId,
					InteressatCommand.asAdministracioDto(command));
		} else {
			interessatService.addToExpedient(
					entitatActual.getId(),
					expedientId,
					command.getId());
		}
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../contenidor/" + expedientId,
				"interessat.controller.afegit.ok");
	}

	@RequestMapping(value = "/{expedientId}/interessat/{interessatId}/delete", method = RequestMethod.GET)
	public String deleteFromExpedient(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@PathVariable Long interessatId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		interessatService.removeFromExpedient(
				entitatActual.getId(),
				expedientId,
				interessatId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../../../contenidor/" + expedientId,
				"interessat.controller.eliminat.ok");
	}

}
