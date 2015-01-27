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

import es.caib.ripea.core.api.dto.BustiaDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.service.BustiaService;
import es.caib.ripea.core.api.service.ContenidorService;
import es.caib.ripea.war.datatable.DatatablesPagina;
import es.caib.ripea.war.helper.ElementsPendentsBustiaHelper;
import es.caib.ripea.war.helper.PaginacioHelper;

/**
 * Controlador per al manteniment de bústies.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/bustiaUser")
public class BustiaUserController extends BaseUserController {

	@Autowired
	private BustiaService bustiaService;
	@Autowired
	private ContenidorService contenidorService;



	@RequestMapping(method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			Model model) {
		return get(request, null, model);
	}
	@RequestMapping(value = "/unitat/{unitatCodi}", method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			@PathVariable String unitatCodi,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"arbreUnitatsOrganitzatives",
				bustiaService.findArbreUnitatsOrganitzatives(
						entitatActual.getId(),
						true,
						true));
		model.addAttribute("unitatCodi", unitatCodi);
		return "bustiaUserList";
	}
	@RequestMapping(value = "/unitat/{unitatCodi}/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesPagina<BustiaDto> datatable(
			HttpServletRequest request,
			@PathVariable String unitatCodi,
			Model model) {
		if (!"null".equalsIgnoreCase(unitatCodi)) {
			EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
			List<BustiaDto> busties = bustiaService.findByUnitatCodiUsuari(
					entitatActual.getId(),
					unitatCodi);
			return PaginacioHelper.getPaginaPerDatatables(
					request,
					busties);
		} else {
			return PaginacioHelper.getPaginaPerDatatables(
					request,
					new ArrayList<BustiaDto>());
		}
	}

	@RequestMapping(value = "/{bustiaId}/pendent", method = RequestMethod.GET)
	public String pendent(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"bustia",
				contenidorService.getContenidorSenseContingut(entitatActual.getId(), bustiaId));
		return "bustiaPendentList";
	}

	@RequestMapping(value = "/{bustiaId}/pendent/count", method = RequestMethod.GET)
	@ResponseBody
	public long pendentCount(
			HttpServletRequest request,
			@PathVariable Long bustiaId) {
		getEntitatActualComprovantPermisos(request);
		return ElementsPendentsBustiaHelper.getCount(request);
	}

	@RequestMapping(value = "/{bustiaId}/pendent/refresh", method = RequestMethod.GET)
	public String pendentRefresh(
			HttpServletRequest request,
			@PathVariable Long bustiaId) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		bustiaService.refreshCountElementsPendentsBustiesAll(
				entitatActual.getId());
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../../pendent",
				null);
	}

}
