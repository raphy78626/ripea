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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.caib.ripea.core.api.dto.DomiciliTipusEnumDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.InteressatDto;
import es.caib.ripea.core.api.dto.MunicipiDto;
import es.caib.ripea.core.api.dto.ProvinciaDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.service.DadesExternesService;
import es.caib.ripea.core.api.service.ExpedientInteressatService;
import es.caib.ripea.war.command.InteressatCommand;
import es.caib.ripea.war.command.InteressatCommand.Administracio;
import es.caib.ripea.war.command.InteressatCommand.ApartatCorreus;
import es.caib.ripea.war.command.InteressatCommand.NacionalEstranger;
import es.caib.ripea.war.command.InteressatCommand.PersonaFisica;
import es.caib.ripea.war.command.InteressatCommand.PersonaJuridica;
import es.caib.ripea.war.command.InteressatCommand.SenseNormalitzar;
import es.caib.ripea.war.helper.MissatgesHelper;
import es.caib.ripea.war.helper.ValidationHelper;

/**
 * Controlador per als interessats dels expedients.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/expedient")
public class ExpedientInteressatController extends BaseUserController {

	@Autowired
	private ExpedientInteressatService expedientInteressatService;
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
		interessatCommand.setDomiciliPaisCodiIso("724");
		model.addAttribute("interessatCommand", interessatCommand);
		model.addAttribute("expedientId", expedientId);
		ompleModel(request, model, entitatActual.getCodi());
		return "expedientInteressatForm";
	}

	@RequestMapping(value = "/{expedientId}/interessat/{interessatId}", method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@PathVariable Long interessatId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		InteressatDto interessatDto = expedientInteressatService.findById(interessatId); 
		InteressatCommand interessatCommand = InteressatCommand.asCommand(interessatDto);
		interessatCommand.setEntitatId(entitatActual.getId());
		model.addAttribute("interessatCommand", interessatCommand);
		model.addAttribute("expedientId", expedientId);
		ompleModel(request, model, entitatActual.getCodi());
		if (interessatDto.getDomiciliProvinciaCodi() != null) {
			model.addAttribute("municipis", dadesExternesService.findMunicipisPerProvincia(interessatDto.getDomiciliProvinciaCodi()));
		}
		if (interessatDto.isAdministracio()) {
			List<UnitatOrganitzativaDto> unitats = new ArrayList<UnitatOrganitzativaDto>();
			try {
				UnitatOrganitzativaDto unitat = expedientInteressatService.findUnitatsOrganitzativesByCodi(interessatCommand.getOrganCodi());
				unitats.add(unitat);
			} catch (Exception e) {
				MissatgesHelper.warning(request, getMessage(request, "interessat.controller.unitat.error"));
			}
			model.addAttribute("unitatsOrganitzatives", unitats);
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
		
		List<Class<?>> grupsDomiciliTipus = new ArrayList<Class<?>>();
		if (interessatCommand.getDomiciliTipusEnum() != null) {
			DomiciliTipusEnumDto domiciliTipusEnum = DomiciliTipusEnumDto.valueOf(interessatCommand.getDomiciliTipusEnum());
			switch (domiciliTipusEnum) {
				case NACIONAL_ESTRANGER:
					grupsDomiciliTipus.add(NacionalEstranger.class);
					break;
				case APARTAT_CORREUS:
					grupsDomiciliTipus.add(ApartatCorreus.class);
					break;
				case SENSE_NORMALITZAR:
					grupsDomiciliTipus.add(SenseNormalitzar.class);
					break;
			}
		}
		new ValidationHelper(validator).isValid(
				interessatCommand,
				bindingResult,
				grupsDomiciliTipus.toArray(new Class[grupsDomiciliTipus.size()]));
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("expedientId", expedientId);
			ompleModel(request, model, entitatActual.getCodi());
			if (interessatCommand.getDomiciliProvinciaCodi() != null) {
				model.addAttribute("municipis", dadesExternesService.findMunicipisPerProvincia(interessatCommand.getDomiciliProvinciaCodi()));
			}
			model.addAttribute("interessatCommand", interessatCommand);
			model.addAttribute("netejar", false);
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
		
		String msgKey = "interessat.controller.afegit.ok";
		if (interessatCommand.getId() == null) {
			expedientInteressatService.create(
					entitatActual.getId(),
					expedientId,
					interessatDto);	
		} else {
			expedientInteressatService.update(
					entitatActual.getId(),
					expedientId,
					interessatDto);
			msgKey = "interessat.controller.modificat.ok";
		}
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../contenidor/" + expedientId,
				msgKey);
	}

	@RequestMapping(value = "/{expedientId}/interessat/{interessatId}/delete", method = RequestMethod.GET)
	public String deleteFromExpedient(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@PathVariable Long interessatId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		expedientInteressatService.delete(
				entitatActual.getId(),
				expedientId,
				interessatId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../../../contenidor/" + expedientId,
				"interessat.controller.eliminat.ok");
	}
	
	// REPRESENTANT
	//////////////////////////////////////////////////////////////////
	
	@RequestMapping(value = "/{expedientId}/interessat/{interessatId}/representant/new", method = RequestMethod.GET)
	public String getRepresentant(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@PathVariable Long interessatId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		InteressatCommand interessatCommand = new InteressatCommand();
		interessatCommand.setEntitatId(entitatActual.getId());
		interessatCommand.setDomiciliPaisCodiIso("724");
		model.addAttribute("interessatCommand", interessatCommand);
		model.addAttribute("expedientId", expedientId);
		model.addAttribute("esRepresentant", true);
		model.addAttribute("interessatId", interessatId);
		ompleModel(request, model, entitatActual.getCodi());
		return "expedientInteressatForm";
	}

	@RequestMapping(value = "/{expedientId}/interessat/{interessatId}/representant/{representantId}", method = RequestMethod.GET)
	public String getRepresentant(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@PathVariable Long interessatId,
			@PathVariable Long representantId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		InteressatDto representantDto = expedientInteressatService.findRepresentantById(
				interessatId, 
				representantId);
//		InteressatDto representantDto = interessatService.findById(entitatActual.getId(), representantId);
		InteressatCommand interessatCommand = InteressatCommand.asCommand(representantDto);
		interessatCommand.setEntitatId(entitatActual.getId());
		model.addAttribute("interessatCommand", interessatCommand);
		model.addAttribute("expedientId", expedientId);
		model.addAttribute("esRepresentant", true);
		model.addAttribute("interessatId", interessatId);
		ompleModel(request, model, entitatActual.getCodi());
		if (representantDto.getDomiciliProvinciaCodi() != null) {
			model.addAttribute("municipis", dadesExternesService.findMunicipisPerProvincia(representantDto.getDomiciliProvinciaCodi()));
		}
		return "expedientInteressatForm";
	}
	
	@RequestMapping(value="/{expedientId}/interessat/{interessatId}/representant", method = RequestMethod.POST)
	public String postRepresentant(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@PathVariable Long interessatId,
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
			ompleModel(request, model, entitatActual.getCodi());
			if (interessatCommand.getDomiciliProvinciaCodi() != null) {
				model.addAttribute("municipis", dadesExternesService.findMunicipisPerProvincia(interessatCommand.getDomiciliProvinciaCodi()));
			}
			model.addAttribute("esRepresentant", true);
			model.addAttribute("interessatId", interessatId);
			model.addAttribute("interessatCommand", interessatCommand);
			model.addAttribute("netejar", false);
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
		
		String msgKey = "interessat.controller.representant.afegit.ok";
		if (interessatCommand.getId() == null) {
			expedientInteressatService.create(
					entitatActual.getId(),
					expedientId,
					interessatId,
					interessatDto);	
		} else {
			expedientInteressatService.update(
					entitatActual.getId(),
					expedientId,
					interessatId,
					interessatDto);
			msgKey = "interessat.controller.representant.modificat.ok";
		}
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../contenidor/" + expedientId,
				msgKey);
	}

	@RequestMapping(value = "/{expedientId}/interessat/{interessatId}/representant/{representantId}/delete", method = RequestMethod.GET)
	public String deleteRepresentant(
			HttpServletRequest request,
			@PathVariable Long expedientId,
			@PathVariable Long interessatId,
			@PathVariable Long representantId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		expedientInteressatService.delete(
				entitatActual.getId(),
				expedientId,
				interessatId,
				representantId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../../../../../contenidor/" + expedientId,
				"interessat.controller.representant.eliminat.ok");
		
	}
	
	
	
	@RequestMapping(value = "/organ/{codi}", method = RequestMethod.GET)
	@ResponseBody
	public UnitatOrganitzativaDto getByCodi(
			HttpServletRequest request,
			@PathVariable String codi,
			Model model) {
		return expedientInteressatService.findUnitatsOrganitzativesByCodi(codi);
	}
	
	@RequestMapping(value = "/provincies/{codiComunitat}", method = RequestMethod.GET)
	@ResponseBody
	public List<ProvinciaDto> getProvincieByCodiComunitat(
			HttpServletRequest request,
			@PathVariable String codiComunitat,
			Model model) {
		return dadesExternesService.findProvinciesPerComunitat(codiComunitat);
	}
	
	@RequestMapping(value = "/municipis/{codiProvincia}", method = RequestMethod.GET)
	@ResponseBody
	public List<MunicipiDto> getMunicipisByCodiProvincia(
			HttpServletRequest request,
			@PathVariable String codiProvincia,
			Model model) {
		return dadesExternesService.findMunicipisPerProvincia(codiProvincia);
	}
	
	@RequestMapping(value = "/organs", method = RequestMethod.GET)
	@ResponseBody
	public List<UnitatOrganitzativaDto> getOrgansEntitat(
			HttpServletRequest request,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		return expedientInteressatService.findUnitatsOrganitzativesByEntitat(entitatActual.getCodi());
	}
	
	@RequestMapping(value = "/organ/filtre", method = RequestMethod.POST)
	@ResponseBody
	public List<UnitatOrganitzativaDto> getOrgansFiltrats(
			HttpServletRequest request,
			@RequestParam(value = "codiDir3", required = false) String codiDir3,
			@RequestParam(value = "denominacio", required = false) String denominacio,
			@RequestParam(value = "nivellAdm", required = false) String nivellAdm,
			@RequestParam(value = "comunitat", required = false) String comunitat,
			@RequestParam(value = "provincia", required = false) String provincia,
			@RequestParam(value = "localitat", required = false) String localitat,
			@RequestParam(value = "arrel", required = false) String arrel,
			Model model) {
		return expedientInteressatService.findUnitatsOrganitzativesByFiltre(
				codiDir3,
				denominacio,
				nivellAdm,
				comunitat,
				provincia,
				localitat,
				"true".equals(arrel));
	}
	
	private void ompleModel(HttpServletRequest request, Model model, String entitatActualCodi) {
//		try {
//			model.addAttribute("unitatsOrganitzatives", interessatService.findUnitatsOrganitzativesByEntitat(entitatActualCodi));
//		} catch (Exception e) {
//			MissatgesHelper.warning(request, getMessage(request, "interessat.controller.unitat.error"));
//		}
		try {
			model.addAttribute("paisos", dadesExternesService.findPaisos());
		} catch (Exception e) {
			MissatgesHelper.warning(request, getMessage(request, "interessat.controller.paisos.error"));
		}
		try {
			model.addAttribute("comunitats", dadesExternesService.findComunitats());
		} catch (Exception e) {
			MissatgesHelper.warning(request, getMessage(request, "interessat.controller.comunitats.error"));
		}
		try {
			model.addAttribute("provincies", dadesExternesService.findProvincies());
		} catch (Exception e) {
			MissatgesHelper.warning(request, getMessage(request, "interessat.controller.provincies.error"));
		}
		try {
			model.addAttribute("nivellAdministracions", dadesExternesService.findNivellAdministracions());
		} catch (Exception e) {
			MissatgesHelper.warning(request, getMessage(request, "interessat.controller.nivell.administracio.error"));
		}
	}

}
