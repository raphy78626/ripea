/**
 * 
 */
package es.caib.ripea.war.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.caib.ripea.core.api.dto.ContingutDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.dto.RegistreAnotacioDto;
import es.caib.ripea.core.api.service.BustiaService;
import es.caib.ripea.core.api.service.ContingutService;
import es.caib.ripea.core.api.service.RegistreService;
import es.caib.ripea.war.datatable.DatatablesPagina;
import es.caib.ripea.war.helper.MissatgesHelper;
import es.caib.ripea.war.helper.PaginacioHelper;

/**
 * Controlador per la gestió d'anotacions de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/contenidor")
public class ContenidorRegistreController extends BaseUserController {

	@Autowired
	private ContingutService contingutService;
	@Autowired
	private RegistreService registreService;
	@Autowired
	private BustiaService bustiaService;



	@RequestMapping(value = "/{contenidorId}/registre/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesPagina<RegistreAnotacioDto> datatable(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		List<RegistreAnotacioDto> registres = null;
		ContingutDto contenidor = contingutService.findAmbIdUser(
				entitatActual.getId(),
				contenidorId,
				true);
		if (contenidor instanceof ExpedientDto) {
			ExpedientDto expedient = (ExpedientDto)contenidor;
			registres = expedient.getFillsRegistres();
		} else {
			registres = new ArrayList<RegistreAnotacioDto>();
		}
		return PaginacioHelper.getPaginaPerDatatables(request, registres);
	}

	@RequestMapping(value = "/{contenidorId}/registre/{registreId}", method = RequestMethod.GET)
	public String info(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long registreId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"registre",
				registreService.findOne(
						entitatActual.getId(),
						contenidorId,
						registreId));
		return "registreDetall";
	}

	@RequestMapping(value = "/{contenidorId}/registre/{registreId}/log", method = RequestMethod.GET)
	public String log(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long registreId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"contenidor",
				contingutService.findAmbIdUser(
						entitatActual.getId(),
						contenidorId,
						true));
		model.addAttribute(
				"registre",
				registreService.findOne(
						entitatActual.getId(),
						contenidorId,
						registreId));
		model.addAttribute(
				"moviments",
				contingutService.findMovimentsPerContingutUser(
						entitatActual.getId(),
						registreId));
		return "registreLog";
	}

	@RequestMapping(value = "/{contenidorId}/registre/{registreId}/reintentar", method = RequestMethod.GET)
	public String reintentar(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long registreId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		boolean processatOk = bustiaService.registreReglaReintentarUser(
				entitatActual.getId(),
				contenidorId,
				registreId);
		if (processatOk) {
			return getModalControllerReturnValueSuccess(
					request,
					"redirect:../../../" + contenidorId,
					"contenidor.controller.registre.reintentat.ok");
		} else {
			MissatgesHelper.error(
					request,
					getMessage(
							request, 
							"contenidor.controller.registre.reintentat.error",
							null));
			return "redirect:../" + registreId;
		}
	}

}
