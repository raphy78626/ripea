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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.caib.ripea.core.api.dto.ContingutTipusEnumDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.service.ContingutService;
import es.caib.ripea.core.api.service.MetaDocumentService;
import es.caib.ripea.core.api.service.MetaExpedientService;
import es.caib.ripea.core.api.service.RegistreService;
import es.caib.ripea.war.command.ContingutFiltreCommand;
import es.caib.ripea.war.command.ContingutFiltreCommand.ContenidorFiltreOpcionsEsborratEnum;
import es.caib.ripea.war.helper.DatatablesHelper;
import es.caib.ripea.war.helper.DatatablesHelper.DatatablesResponse;
import es.caib.ripea.war.helper.MissatgesHelper;
import es.caib.ripea.war.helper.RequestSessionHelper;

/**
 * Controlador per a la consulta d'arxius pels administradors.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/contingutAdmin")
public class ContingutAdminController extends BaseAdminController {

	private static final String SESSION_ATTRIBUTE_FILTRE = "ContingutAdminController.session.filtre";

	@Autowired
	private ContingutService contingutService;
	@Autowired
	private RegistreService registreService;
	@Autowired
	private MetaExpedientService metaExpedientService;
	@Autowired
	private MetaDocumentService metaDocumentService;



	@RequestMapping(method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ContingutFiltreCommand filtreCommand = getFiltreCommand(request);
		model.addAttribute(
				filtreCommand);
		if (ContingutTipusEnumDto.EXPEDIENT.equals(filtreCommand.getTipus())) {
			model.addAttribute(
					"metaNodes",
					metaExpedientService.findByEntitat(entitatActual.getId()));
		} else if (ContingutTipusEnumDto.DOCUMENT.equals(filtreCommand.getTipus())) {
			model.addAttribute(
					"metaNodes",
					metaDocumentService.findByEntitat(entitatActual.getId()));
		}
		return "contingutAdminList";
	}
	@RequestMapping(method = RequestMethod.POST)
	public String expedientPost(
			HttpServletRequest request,
			@Valid ContingutFiltreCommand filtreCommand,
			BindingResult bindingResult,
			Model model) {
		if (!bindingResult.hasErrors()) {
			RequestSessionHelper.actualitzarObjecteSessio(
					request,
					SESSION_ATTRIBUTE_FILTRE,
					filtreCommand);
		}
		return "redirect:contingutAdmin";
	}

	@RequestMapping(value = "/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesResponse datatable(
			HttpServletRequest request) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ContingutFiltreCommand filtreCommand = getFiltreCommand(request);
		return DatatablesHelper.getDatatableResponse(
				request,
				contingutService.findAdmin(
						entitatActual.getId(),
						ContingutFiltreCommand.asDto(filtreCommand),
						DatatablesHelper.getPaginacioDtoFromRequest(request)),
				"id");
	}

	@RequestMapping(value = "/{contingutId}/info", method = RequestMethod.GET)
	public String info(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"contingut",
				contingutService.findAmbIdAdmin(
						entitatActual.getId(),
						contingutId,
						true));
		return "contingutAdminInfo";
	}

	@RequestMapping(value = "/{contingutId}/log", method = RequestMethod.GET)
	public String log(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"contingut",
				contingutService.findAmbIdAdmin(
						entitatActual.getId(),
						contingutId,
						true));
		model.addAttribute(
				"logs",
				contingutService.findLogsPerContingutAdmin(
						entitatActual.getId(),
						contingutId));
		model.addAttribute(
				"moviments",
				contingutService.findMovimentsPerContingutAdmin(
						entitatActual.getId(),
						contingutId));
		return "contingutLog";
	}

	@RequestMapping(value = "/{contingutId}/undelete", method = RequestMethod.GET)
	public String undelete(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			Model model) throws IOException {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		try {
			contingutService.undelete(
					entitatActual.getId(),
					contingutId);
			return getAjaxControllerReturnValueSuccess(
					request,
					"redirect:../../esborrat",
					"contingut.admin.controller.recuperat.ok");
		} catch (ValidationException ex) {
			return getAjaxControllerReturnValueError(
					request,
					"redirect:../../esborrat",
					"contingut.admin.controller.recuperat.duplicat");
		}
	}

	@RequestMapping(value = "/{contingutId}/delete", method = RequestMethod.GET)
	public String delete(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		contingutService.deleteDefinitiu(
				entitatActual.getId(),
				contingutId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../esborrat",
				"contingut.admin.controller.esborrat.definitiu.ok");
	}

	@RequestMapping(value = "/{bustiaId}/registre/{registreId}/reintentar", method = RequestMethod.GET)
	public String reintentar(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable Long registreId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		boolean processatOk = registreService.reglaReintentarAdmin(
				entitatActual.getId(),
				bustiaId,
				registreId);
		if (processatOk) {
			MissatgesHelper.success(
					request, 
					getMessage(
							request, 
							"contingut.admin.controller.registre.reintentat.ok",
							null));
		} else {
			MissatgesHelper.error(
					request,
					getMessage(
							request, 
							"contingut.admin.controller.registre.reintentat.error",
							null));
		}
		return "redirect:../../../" + registreId + "/info";
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(
	    		Date.class,
	    		new CustomDateEditor(
	    				new SimpleDateFormat("dd/MM/yyyy"),
	    				true));
	}



	private ContingutFiltreCommand getFiltreCommand(
			HttpServletRequest request) {
		ContingutFiltreCommand filtreCommand = (ContingutFiltreCommand)RequestSessionHelper.obtenirObjecteSessio(
				request,
				SESSION_ATTRIBUTE_FILTRE);
		if (filtreCommand == null) {
			filtreCommand = new ContingutFiltreCommand();
			filtreCommand.setOpcionsEsborrat(ContenidorFiltreOpcionsEsborratEnum.NOMES_NO_ESBORRATS);
			RequestSessionHelper.actualitzarObjecteSessio(
					request,
					SESSION_ATTRIBUTE_FILTRE,
					filtreCommand);
		}
		return filtreCommand;
	}

}
