/**
 * 
 */
package es.caib.ripea.war.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.caib.ripea.core.api.dto.ArxiuDto;
import es.caib.ripea.core.api.dto.BustiaContingutPendentDto;
import es.caib.ripea.core.api.dto.BustiaContingutPendentTipusEnumDto;
import es.caib.ripea.core.api.dto.BustiaDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.EscriptoriDto;
import es.caib.ripea.core.api.service.ArxiuService;
import es.caib.ripea.core.api.service.BustiaService;
import es.caib.ripea.core.api.service.ContenidorService;
import es.caib.ripea.core.api.service.ExpedientService;
import es.caib.ripea.core.api.service.MetaExpedientService;
import es.caib.ripea.war.command.ContenidorCommand.Create;
import es.caib.ripea.war.command.ContenidorMoureCopiarEnviarCommand;
import es.caib.ripea.war.command.ExpedientCommand;
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
	@Autowired
	private ExpedientService expedientService;
	@Autowired
	private MetaExpedientService metaExpedientService;


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
		bustiaService.contingutPendentBustiesAllCount(
				entitatActual.getId());
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../../pendent",
				null);
	}

	@RequestMapping(value = "/{bustiaId}/pendent/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesPagina<BustiaContingutPendentDto> pendentContingutDatatable(
			HttpServletRequest request,
			@PathVariable Long bustiaId) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		return PaginacioHelper.getPaginaPerDatatables(
				request,
				bustiaService.contingutPendentFindByBustiaId(
						entitatActual.getId(),
						bustiaId));
	}

	@RequestMapping(value = "/{bustiaId}/pendent/{contingutTipus}/{contingutId}/nouexp", method = RequestMethod.GET)
	public String bustiaPendentNouexpGet(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable BustiaContingutPendentTipusEnumDto contingutTipus,
			@PathVariable Long contingutId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		EscriptoriDto escriptori = contenidorService.getEscriptoriPerUsuariActual(entitatActual.getId());
		ExpedientCommand command = new ExpedientCommand();
		command.setEntitatId(entitatActual.getId());
		command.setPareId(escriptori.getId());
		model.addAttribute(command);
		omplirModelPerNouExpedient(
				entitatActual,
				model);
		return "bustiaPendentContingutNouexp";
	}
	@RequestMapping(value = "/{bustiaId}/pendent/{contingutTipus}/{contingutId}/nouexp", method = RequestMethod.POST)
	public String bustiaPendentNouexpPost(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable BustiaContingutPendentTipusEnumDto contingutTipus,
			@PathVariable Long contingutId,
			@Validated({Create.class}) ExpedientCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			omplirModelPerNouExpedient(
					entitatActual,
					model);
			return "bustiaPendentContingutNouexp";
		}
		EscriptoriDto escriptori = contenidorService.getEscriptoriPerUsuariActual(entitatActual.getId());
		expedientService.create(
				entitatActual.getId(),
				escriptori.getId(),
				command.getMetaNodeId(),
				command.getArxiuId(),
				null,
				command.getNom(),
				contingutTipus,
				contingutId);
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../../pendent",
				"bustia.controller.pendent.contingut.nouexp.ok");
	}

	@RequestMapping(value = "/{bustiaId}/pendent/{contingutTipus}/{contingutId}/addexp", method = RequestMethod.GET)
	public String bustiaPendentAddexpGet(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable BustiaContingutPendentTipusEnumDto contingutTipus,
			@PathVariable Long contingutId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		omplirModelPerAfegirAExpedient(
				entitatActual,
				model,
				contingutId);
		return "bustiaPendentContingutAddexp";
	}
	@RequestMapping(value = "/{bustiaId}/pendent/{contingutTipus}/{contingutId}/addexp", method = RequestMethod.POST)
	public String bustiaPendentAddexpPost(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable BustiaContingutPendentTipusEnumDto contingutTipus,
			@PathVariable Long contingutId,
			@Valid ContenidorMoureCopiarEnviarCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			omplirModelPerAfegirAExpedient(
					entitatActual,
					model,
					contingutId);
			return "bustiaPendentContingutAddexp";
		}
		expedientService.afegirContingutBustia(
				entitatActual.getId(),
				command.getContenidorDestiId(),
				bustiaId,
				contingutTipus,
				contingutId);
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../../pendent",
				"bustia.controller.pendent.contingut.addexp.ok");
	}

	@RequestMapping(value = "/{bustiaId}/pendent/{contingutTipus}/{contingutId}/reenviar", method = RequestMethod.GET)
	public String bustiaPendentReenviarGet(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable BustiaContingutPendentTipusEnumDto contingutTipus,
			@PathVariable Long contingutId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		omplirModelPerReenviar(
				entitatActual,
				bustiaId,
				contingutTipus,
				contingutId,
				model);
		ContenidorMoureCopiarEnviarCommand command = new ContenidorMoureCopiarEnviarCommand();
		command.setContenidorOrigenId(bustiaId);
		model.addAttribute(command);
		return "bustiaPendentContingutReenviar";
	}
	@RequestMapping(value = "/{bustiaId}/pendent/{contingutTipus}/{contingutId}/reenviar", method = RequestMethod.POST)
	public String bustiaPendentReenviarPost(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable BustiaContingutPendentTipusEnumDto contingutTipus,
			@PathVariable Long contingutId,
			@Valid ContenidorMoureCopiarEnviarCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			omplirModelPerReenviar(
					entitatActual,
					bustiaId,
					contingutTipus,
					contingutId,
					model);
			return "bustiaPendentContingutReenviar";
		}
		bustiaService.contingutPendentReenviar(
				entitatActual.getId(),
				bustiaId,
				command.getContenidorDestiId(),
				contingutTipus,
				contingutId,
				command.getComentariEnviar());
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../../pendent",
				"bustia.controller.pendent.contingut.reenviat.ok");
	}



	private void omplirModelPerNouExpedient(
			EntitatDto entitatActual,
			Model model) {
		model.addAttribute(
				"metaExpedients",
				metaExpedientService.findActiveByEntitatPerCreacio(entitatActual.getId()));
		model.addAttribute(
				"arxius",
				new ArrayList<ArxiuDto>());
	}

	private void omplirModelPerAfegirAExpedient(
			EntitatDto entitatActual,
			Model model,
			Long contenidorOrigenId) {
		EscriptoriDto escriptori = contenidorService.getEscriptoriPerUsuariActual(entitatActual.getId());
		model.addAttribute(
				"contenidorDesti",
				escriptori);
		ContenidorMoureCopiarEnviarCommand command = new ContenidorMoureCopiarEnviarCommand();
		command.setContenidorOrigenId(contenidorOrigenId);
		model.addAttribute(command);
	}

	private void omplirModelPerReenviar(
			EntitatDto entitatActual,
			Long bustiaId,
			BustiaContingutPendentTipusEnumDto contingutTipus,
			Long contingutId,
			Model model) {
		model.addAttribute(
				"contingutPendent",
				bustiaService.contingutPendentFindOne(
						entitatActual.getId(),
						bustiaId,
						contingutTipus,
						contingutId));
		List<BustiaDto> busties = bustiaService.findByEntitatAndActivaTrue(
				entitatActual.getId());
		model.addAttribute(
				"busties",
				busties);
		model.addAttribute(
				"arbreUnitatsOrganitzatives",
				bustiaService.findArbreUnitatsOrganitzatives(
						entitatActual.getId(),
						true,
						false,
						true));
	}

}
