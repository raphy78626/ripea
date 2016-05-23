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
import es.caib.ripea.core.api.dto.BustiaDto;
import es.caib.ripea.core.api.dto.ContenidorDto;
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
import es.caib.ripea.war.helper.PaginacioHelper;

/**
 * Controlador per a gentionar el contingut pendent a les bústies.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/bustiaUser")
public class BustiaUserContingutController extends BaseUserController {

	@Autowired
	private BustiaService bustiaService;
	@Autowired
	private ContenidorService contenidorService;
	@Autowired
	private ExpedientService expedientService;
	@Autowired
	private MetaExpedientService metaExpedientService;
	@Autowired
	private ArxiuService arxiuService;



	@RequestMapping(value = "/{bustiaId}/pendent/contingut/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesPagina<ContenidorDto> pendentContingutDatatable(
			HttpServletRequest request,
			@PathVariable Long bustiaId) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		return PaginacioHelper.getPaginaPerDatatables(
				request,
				bustiaService.findContingutPendent(
						entitatActual.getId(),
						bustiaId));
	}

	@RequestMapping(value = "/{bustiaId}/pendent/contingut/{contenidorId}/agafar", method = RequestMethod.GET)
	public String pendentAfegirEscriptori(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable Long contenidorId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		EscriptoriDto escriptori = contenidorService.getEscriptoriPerUsuariActual(entitatActual.getId());
		contenidorService.receive(
				entitatActual.getId(),
				bustiaId,
				contenidorId,
				escriptori.getId());
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../pendent",
				"bustia.controller.pendent.contingut.agafat");
	}

	@RequestMapping(value = "/{bustiaId}/pendent/contingut/{contingutId}/nouexp", method = RequestMethod.GET)
	public String registrePendentNouexpGet(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable Long contingutId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		EscriptoriDto escriptori = contenidorService.getEscriptoriPerUsuariActual(entitatActual.getId());
		ExpedientCommand command = new ExpedientCommand();
		command.setEntitatId(entitatActual.getId());
		command.setPareId(escriptori.getId());
		command.setContingutId(contingutId);
		model.addAttribute(command);
		omplirModelPerNouExpedient(
				entitatActual,
				command.getMetaNodeId(),
				model);
		return "bustiaPendentContingutNouexp";
	}
	@RequestMapping(value = "/{bustiaId}/pendent/contingut/{contingutId}/nouexp", method = RequestMethod.POST)
	public String registrePendentNouexpPost(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable Long contingutId,
			@Validated({Create.class}) ExpedientCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			omplirModelPerNouExpedient(
					entitatActual,
					command.getMetaNodeId(),
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
				command.getContingutId(),
				command.getRegistreId());
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../../pendent",
				"bustia.controller.pendent.contingut.nouexp.ok");
	}

	@RequestMapping(value = "/{bustiaId}/pendent/contingut/{contingutId}/addexp", method = RequestMethod.GET)
	public String registrePendentAddexpGet(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable Long contingutId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		omplirModelPerAfegirAExpedient(
				entitatActual,
				model,
				contingutId);
		return "bustiaPendentContingutAddexp";
	}
	@RequestMapping(value = "/{bustiaId}/pendent/contingut/{contingutId}/addexp", method = RequestMethod.POST)
	public String registrePendentAddexpPost(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
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
		contenidorService.receive(
				entitatActual.getId(),
				bustiaId,
				command.getContenidorOrigenId(),
				command.getContenidorDestiId());
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../../pendent",
				"bustia.controller.pendent.contingut.addexp.ok");
	}

	@RequestMapping(value = "/{bustiaId}/pendent/contingut/{contingutId}/reenviar", method = RequestMethod.GET)
	public String registrePendentReenviarGet(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable Long contingutId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		omplirModelPerReenviar(
				entitatActual,
				contingutId,
				model);
		ContenidorMoureCopiarEnviarCommand command = new ContenidorMoureCopiarEnviarCommand();
		command.setContenidorOrigenId(bustiaId);
		model.addAttribute(command);
		return "bustiaPendentContingutReenviar";
	}
	@RequestMapping(value = "/{bustiaId}/pendent/contingut/{contingutId}/reenviar", method = RequestMethod.POST)
	public String registrePendentReenviarPost(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable Long contingutId,
			@Valid ContenidorMoureCopiarEnviarCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			omplirModelPerReenviar(
					entitatActual,
					contingutId,
					model);
			return "bustiaPendentContingutReenviar";
		}
		contenidorService.send(
				entitatActual.getId(),
				contingutId,
				command.getContenidorDestiId(),
				command.getComentariEnviar());
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../../pendent",
				"bustia.controller.pendent.contingut.reenviat.ok");
	}



	private void omplirModelPerNouExpedient(
			EntitatDto entitatActual,
			Long metaExpedientId,
			Model model) {
		model.addAttribute(
				"metaExpedients",
				metaExpedientService.findActiveByEntitatPerCreacio(entitatActual.getId()));
		if(metaExpedientId != null) {
			model.addAttribute(
					"arxius",
					arxiuService.findPermesosPerUsuariIMetaExpedient(
							entitatActual.getId(),
							metaExpedientId));
		} else {
			model.addAttribute(
					"arxius",
					new ArrayList<ArxiuDto>());
		}
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
			Long contenidorOrigenId,
			Model model) {
		ContenidorDto contenidorOrigen = contenidorService.getContenidorAmbContingut(
				entitatActual.getId(),
				contenidorOrigenId);
		model.addAttribute(
				"contenidorOrigen",
				contenidorOrigen);
		List<BustiaDto> busties = bustiaService.findByEntitatAndActivaTrue(
				entitatActual.getId());
		model.addAttribute(
				"busties",
				busties);
		model.addAttribute(
				"arbreUnitatsOrganitzatives",
				bustiaService.findArbreUnitatsOrganitzatives(
						entitatActual.getId(),
						false,
						true));
	}

}
