/**
 * 
 */
package es.caib.ripea.war.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador per al manteniment de registres a les bústies.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/bustiaUser")
public class BustiaUserRegistreController extends BaseUserController {

	/*@Autowired
	private BustiaService bustiaService;
	@Autowired
	private ContingutService contenidorService;
	@Autowired
	private ExpedientService expedientService;
	@Autowired
	private MetaExpedientService metaExpedientService;
	@Autowired
	private ArxiuService arxiuService;
	@Autowired
	private RegistreService registreService;*/



	/*@RequestMapping(value = "/{bustiaId}/pendent/registre/{registreId}", method = RequestMethod.GET)
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
	}*/

	/*@RequestMapping(value = "/{bustiaId}/pendent/registre/{registreId}/nouexp", method = RequestMethod.GET)
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
		model.addAttribute(command);
		omplirModelPerNouExpedient(
				entitatActual,
				command.getMetaNodeId(),
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
					command.getMetaNodeId(),
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
				null,
				null);
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
		ContingutMoureCopiarEnviarCommand command = new ContingutMoureCopiarEnviarCommand();
		command.setOrigenId(escriptori.getId());
		model.addAttribute(command);
		return "bustiaPendentRegistreAddexp";
	}
	@RequestMapping(value = "/{bustiaId}/pendent/registre/{registreId}/addexp", method = RequestMethod.POST)
	public String registrePendentAddexpPost(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable Long registreId,
			@Valid ContingutMoureCopiarEnviarCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			EscriptoriDto escriptori = contenidorService.getEscriptoriPerUsuariActual(
					entitatActual.getId());
			model.addAttribute("contenidorOrigen", escriptori);
			return "bustiaPendentRegistreAddexp";
		}
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
		ContingutMoureCopiarEnviarCommand command = new ContingutMoureCopiarEnviarCommand();
		command.setOrigenId(bustiaId);
		model.addAttribute(command);
		return "bustiaPendentRegistreReenviar";
	}
	@RequestMapping(value = "/{bustiaId}/pendent/registre/{registreId}/reenviar", method = RequestMethod.POST)
	public String registrePendentReenviarPost(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			@PathVariable Long registreId,
			@Valid ContingutMoureCopiarEnviarCommand command,
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
			Long metaExpedientId,
			Model model) {
		model.addAttribute(
				"metaExpedients",
				metaExpedientService.findActiusAmbEntitatPerCreacio(entitatActual.getId()));
		if (metaExpedientId != null) {
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

	private void omplirModelPerReenviar(
			EntitatDto entitatActual,
			Model model,
			Long contenidorOrigenId) {
		List<BustiaDto> busties = bustiaService.findActivesAmbEntitat(
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

	private void omplirModelPerRebutjar(
			EntitatDto entitatActual,
			Model model,
			Long registreId) {
		List<BustiaDto> busties = bustiaService.findPermesesPerUsuari(
				entitatActual.getId());
		model.addAttribute(
				"busties",
				busties);
	}*/

}
