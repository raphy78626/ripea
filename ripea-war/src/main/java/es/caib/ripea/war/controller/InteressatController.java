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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.InteressatDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.service.DadesExternesService;
import es.caib.ripea.core.api.service.InteressatService;
import es.caib.ripea.war.command.InteressatCommand;
import es.caib.ripea.war.command.InteressatCommand.Administracio;
import es.caib.ripea.war.command.InteressatCommand.PersonaFisica;
import es.caib.ripea.war.command.InteressatCommand.PersonaJuridica;
import es.caib.ripea.war.helper.ValidationHelper;

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
	@Autowired
	private DadesExternesService dadesExternesService;

	@Autowired(required = true)
	private javax.validation.Validator validator;

	@RequestMapping(value = "/{expedientId}/interessat/new", method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		InteressatCommand interessatCommand = new InteressatCommand();
		interessatCommand.setEntitatId(entitatActual.getId());
		model.addAttribute("interessatCommand", interessatCommand);
		model.addAttribute("expedientId", expedientId);
		model.addAttribute("unitatsOrganitzatives", interessatService.findUnitatsOrganitzativesByEntitat(entitatActual.getCodi()));
		model.addAttribute("paisos", dadesExternesService.findPaisos());
		model.addAttribute("provincies", dadesExternesService.findProvincies());
		return "expedientInteressatForm";
	}

	@RequestMapping(value = "/{expedientId}/interessat/{interessatId}", method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@PathVariable Long interessatId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		InteressatDto interessatDto = interessatService.findById(entitatActual.getId(), interessatId); 
		InteressatCommand interessatCommand = InteressatCommand.asCommand(interessatDto);
//		interessatCommand.setEntitatId(entitatActual.getId());
		model.addAttribute("interessatCommand", interessatCommand);
		model.addAttribute("expedientId", expedientId);
		model.addAttribute("unitatsOrganitzatives", interessatService.findUnitatsOrganitzativesByEntitat(entitatActual.getCodi()));
		model.addAttribute("paisos", dadesExternesService.findPaisos());
		model.addAttribute("provincies", dadesExternesService.findProvincies());
		
		if (interessatDto.getProvincia() != null) {
			model.addAttribute("municipis", dadesExternesService.findMunicipisPerProvincia(interessatDto.getProvincia()));
		}
		return "expedientInteressatForm";
	}
	
	@RequestMapping(value="/{expedientId}/interessat", method = RequestMethod.POST)
	public String postCiutada(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@ModelAttribute InteressatCommand interessatCommand,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		
		List<Class<?>> grups = new ArrayList<Class<?>>();
		if (interessatCommand.getTipus() != null) {
			switch (interessatCommand.getTipus()) {
			case PERSONA_FISICA:
				grups.add(PersonaFisica.class);
				break;
			case PERSONA_JURIDICA:
				grups.add(PersonaJuridica.class);
				break;
			case ADMINISTRACIO:
				grups.add(Administracio.class);
				break;
			}
		}
		new ValidationHelper(validator).isValid(
				interessatCommand,
				bindingResult,
				grups.toArray(new Class[grups.size()]));
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("expedientId", expedientId);
			model.addAttribute("unitatsOrganitzatives", interessatService.findUnitatsOrganitzativesByEntitat(entitatActual.getCodi()));
			model.addAttribute("paisos", dadesExternesService.findPaisos());
			return "expedientInteressatForm";
		}
		
		InteressatDto interessatDto = null;
		switch (interessatCommand.getTipus()) {
		case PERSONA_FISICA:
			interessatDto = InteressatCommand.asPersonaFisicaDto(interessatCommand);
			break;
		case PERSONA_JURIDICA:
			interessatDto = InteressatCommand.asPersonaJuridicaDto(interessatCommand);
			break;
		case ADMINISTRACIO:
			interessatDto = InteressatCommand.asAdministracioDto(interessatCommand);
			break;
		}
		
		if (interessatCommand.getId() == null) {
			interessatService.create(
					entitatActual.getId(),
					expedientId,
					interessatDto);	
		} else {
			interessatService.update(
					entitatActual.getId(),
					expedientId,
					interessatDto);
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
		interessatService.delete(
				entitatActual.getId(),
				expedientId,
				interessatId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../../../contenidor/" + expedientId,
				"interessat.controller.eliminat.ok");
	}
	
	@RequestMapping(value = "/organ/{codi}", method = RequestMethod.GET)
	@ResponseBody
	public UnitatOrganitzativaDto getByCodi(
			HttpServletRequest request,
			@PathVariable String codi,
			Model model) {
		return interessatService.findUnitatsOrganitzativesByCodi(codi);
	}

}
