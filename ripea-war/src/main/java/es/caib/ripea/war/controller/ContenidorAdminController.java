/**
 * 
 */
package es.caib.ripea.war.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.caib.ripea.core.api.dto.ContenidorDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.service.ContenidorService;
import es.caib.ripea.war.command.ContenidorFiltreCommand;
import es.caib.ripea.war.command.ContenidorFiltreCommand.ContenidorFiltreOpcionsEsborratEnum;
import es.caib.ripea.war.datatable.DatatablesPagina;
import es.caib.ripea.war.helper.PaginacioHelper;
import es.caib.ripea.war.helper.RequestSessionHelper;

/**
 * Controlador per a la consulta d'arxius pels administradors.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/contenidorAdmin")
public class ContenidorAdminController extends BaseAdminController {

	private static final String SESSION_ATTRIBUTE_FILTRE = "ContenidorAdminController.session.filtre";

	@Autowired
	private ContenidorService contenidorService;



	@RequestMapping(method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			Model model) {
		getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				getFiltreCommand(request));
		return "contenidorAdminList";
	}
	@RequestMapping(method = RequestMethod.POST)
	public String expedientPost(
			HttpServletRequest request,
			@Valid ContenidorFiltreCommand filtreCommand,
			BindingResult bindingResult,
			Model model) {
		if (!bindingResult.hasErrors()) {
			RequestSessionHelper.actualitzarObjecteSessio(
					request,
					SESSION_ATTRIBUTE_FILTRE,
					filtreCommand);
		}
		return "redirect:contenidorAdmin";
	}
	@RequestMapping(value = "/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesPagina<ContenidorDto> datatable(
			HttpServletRequest request) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ContenidorFiltreCommand filtreCommand = getFiltreCommand(request);
		return PaginacioHelper.getPaginaPerDatatables(
				request,
				contenidorService.findAdmin(
						entitatActual.getId(),
						ContenidorFiltreCommand.asDto(filtreCommand),
						PaginacioHelper.getPaginacioDtoFromDatatable(
								request,
								null)));
	}

	@RequestMapping(value = "/{contenidorId}/undelete", method = RequestMethod.GET)
	public String undelete(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		try {
			contenidorService.undelete(
					entitatActual.getId(),
					contenidorId);
			return getAjaxControllerReturnValueSuccess(
					request,
					"redirect:../../esborrat",
					"contenidor.admin.controller.recuperat.ok");
		} catch (ValidationException ex) {
			return getAjaxControllerReturnValueError(
					request,
					"redirect:../../esborrat",
					"contenidor.admin.controller.recuperat.duplicat");
		}
	}

	@RequestMapping(value = "/{contenidorId}/delete", method = RequestMethod.GET)
	public String delete(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		contenidorService.deleteDefinitiu(
				entitatActual.getId(),
				contenidorId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../esborrat",
				"contenidor.admin.controller.esborrat.definitiu.ok");
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(
	    		Date.class,
	    		new CustomDateEditor(
	    				new SimpleDateFormat("dd/MM/yyyy"),
	    				true));
	}



	private ContenidorFiltreCommand getFiltreCommand(
			HttpServletRequest request) {
		ContenidorFiltreCommand filtreCommand = (ContenidorFiltreCommand)RequestSessionHelper.obtenirObjecteSessio(
				request,
				SESSION_ATTRIBUTE_FILTRE);
		if (filtreCommand == null) {
			filtreCommand = new ContenidorFiltreCommand();
			filtreCommand.setOpcionsEsborrat(ContenidorFiltreOpcionsEsborratEnum.NOMES_NO_ESBORRATS);
			RequestSessionHelper.actualitzarObjecteSessio(
					request,
					SESSION_ATTRIBUTE_FILTRE,
					filtreCommand);
		}
		return filtreCommand;
	}

}
