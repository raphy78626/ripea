/**
 * 
 */
package es.caib.ripea.war.controller;

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

import es.caib.ripea.core.api.dto.BustiaDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.EscriptoriDto;
import es.caib.ripea.core.api.dto.RegistreAnotacioDto;
import es.caib.ripea.core.api.service.ArxiuService;
import es.caib.ripea.core.api.service.BustiaService;
import es.caib.ripea.core.api.service.ContenidorService;
import es.caib.ripea.core.api.service.ExpedientService;
import es.caib.ripea.core.api.service.MetaExpedientService;
import es.caib.ripea.core.api.service.RegistreService;
import es.caib.ripea.war.command.ContenidorCommand.Create;
import es.caib.ripea.war.command.ContenidorMoureCopiarEnviarCommand;
import es.caib.ripea.war.command.ExpedientCommand;
import es.caib.ripea.war.command.RegistreRebutjarCommand;
import es.caib.ripea.war.datatable.DatatablesPagina;
import es.caib.ripea.war.helper.PaginacioHelper;

/**
 * Controlador per al manteniment de registres a les bústies.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/bustiaUser")
public class BustiaUserRegistreController extends BaseUserController {

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
	@Autowired
	private RegistreService registreService;



	@RequestMapping(value = "/{bustiaId}/pendent/registre/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesPagina<RegistreAnotacioDto> pendentRegistreDatatable(
			HttpServletRequest request,
			@PathVariable Long bustiaId) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		return PaginacioHelper.getPaginaPerDatatables(
				request,
				bustiaService.findRegistrePendent(
						entitatActual.getId(),
						bustiaId));
	}

	@RequestMapping(value = "/{bustiaId}/pendent/registre/{registreId}", method = RequestMethod.GET)
	public String registrePendentObrir(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable Long registreId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"registre",
				bustiaService.findOneRegistrePendent(
						entitatActual.getId(),
						bustiaId,
						registreId));
		return "bustiaPendentRegistre";
	}

	@RequestMapping(value = "/{bustiaId}/pendent/registre/{registreId}/nouexp", method = RequestMethod.GET)
	public String registrePendentNouexpGet(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable Long registreId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		EscriptoriDto escriptori = contenidorService.getEscriptoriPerUsuariActual(entitatActual.getId());
		ExpedientCommand command = new ExpedientCommand();
		command.setEntitatId(entitatActual.getId());
		command.setPareId(escriptori.getId());
		command.setRegistreId(registreId);
		model.addAttribute(command);
		omplirModelPerNouExpedient(
				entitatActual,
				model);
		return "bustiaPendentRegistreNouexp";
	}
	@RequestMapping(value = "/{bustiaId}/pendent/registre/{registreId}/nouexp", method = RequestMethod.POST)
	public String registrePendentNouexpPost(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable Long registreId,
			@Validated({Create.class}) ExpedientCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			omplirModelPerNouExpedient(
					entitatActual,
					model);
			return "bustiaPendentRegistreNouexp";
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
				"bustia.controller.pendent.registre.nouexp.ok");
	}

	@RequestMapping(value = "/{bustiaId}/pendent/registre/{registreId}/addexp", method = RequestMethod.GET)
	public String registrePendentAddexpGet(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable Long registreId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		EscriptoriDto escriptori = contenidorService.getEscriptoriPerUsuariActual(
				entitatActual.getId());
		model.addAttribute("contenidorOrigen", escriptori);
		ContenidorMoureCopiarEnviarCommand command = new ContenidorMoureCopiarEnviarCommand();
		command.setContenidorOrigenId(escriptori.getId());
		model.addAttribute(command);
		return "bustiaPendentRegistreAddexp";
	}
	@RequestMapping(value = "/{bustiaId}/pendent/registre/{registreId}/addexp", method = RequestMethod.POST)
	public String registrePendentAddexpPost(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable Long registreId,
			@Valid ContenidorMoureCopiarEnviarCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			EscriptoriDto escriptori = contenidorService.getEscriptoriPerUsuariActual(
					entitatActual.getId());
			model.addAttribute("contenidorOrigen", escriptori);
			return "bustiaPendentRegistreAddexp";
		}
		registreService.afegirAExpedient(
				entitatActual.getId(),
				command.getContenidorDestiId(),
				registreId);
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../../pendent",
				"bustia.controller.pendent.registre.addexp.ok");
	}

	@RequestMapping(value = "/{bustiaId}/pendent/registre/{registreId}/reenviar", method = RequestMethod.GET)
	public String registrePendentReenviarGet(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable Long registreId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		omplirModelPerReenviar(
				entitatActual,
				model,
				bustiaId);
		ContenidorMoureCopiarEnviarCommand command = new ContenidorMoureCopiarEnviarCommand();
		command.setContenidorOrigenId(bustiaId);
		model.addAttribute(command);
		return "bustiaPendentRegistreReenviar";
	}
	@RequestMapping(value = "/{bustiaId}/pendent/registre/{registreId}/reenviar", method = RequestMethod.POST)
	public String registrePendentReenviarPost(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable Long registreId,
			@Valid ContenidorMoureCopiarEnviarCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			omplirModelPerReenviar(
					entitatActual,
					model,
					bustiaId);
			return "bustiaPendentRegistreReenviar";
		}
		bustiaService.forwardRegistre(
				entitatActual.getId(),
				bustiaId,
				registreId,
				command.getContenidorDestiId(),
				command.getComentariEnviar());
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../../pendent",
				"bustia.controller.pendent.registre.reenviat.ok");
	}

	@RequestMapping(value = "/{bustiaId}/pendent/registre/{registreId}/rebutjar", method = RequestMethod.GET)
	public String registrePendentRebutjarGet(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable Long registreId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		RegistreRebutjarCommand command = new RegistreRebutjarCommand();
		command.setRegistreId(registreId);
		model.addAttribute(command);
		omplirModelPerRebutjar(
				entitatActual,
				model,
				registreId);
		return "bustiaPendentRegistreRebutjar";
	}
	@RequestMapping(value = "/{bustiaId}/pendent/registre/{registreId}/rebutjar", method = RequestMethod.POST)
	public String registrePendentRebutjarPost(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable Long registreId,
			@Valid RegistreRebutjarCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			omplirModelPerRebutjar(
					entitatActual,
					model,
					bustiaId);
			return "bustiaPendentRegistreRebutjar";
		}
		registreService.rebutjar(
				entitatActual.getId(),
				bustiaId,
				registreId,
				command.getMotiu());
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../../pendent",
				"bustia.controller.pendent.registre.rebutjat.ok");
	}



	private void omplirModelPerNouExpedient(
			EntitatDto entitatActual,
			Model model) {
		model.addAttribute(
				"metaExpedients",
				metaExpedientService.findActiveByEntitatPerCreacio(entitatActual.getId()));
		model.addAttribute(
				"arxius",
				arxiuService.findPermesosPerUsuari(entitatActual.getId()));
	}

	private void omplirModelPerReenviar(
			EntitatDto entitatActual,
			Model model,
			Long contenidorOrigenId) {
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

	private void omplirModelPerRebutjar(
			EntitatDto entitatActual,
			Model model,
			Long registreId) {
		List<BustiaDto> busties = bustiaService.findPermesesPerUsuari(
				entitatActual.getId());
		model.addAttribute(
				"busties",
				busties);
	}

}
