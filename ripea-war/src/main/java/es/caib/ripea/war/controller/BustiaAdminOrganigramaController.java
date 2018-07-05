package es.caib.ripea.war.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.caib.ripea.core.api.dto.BustiaDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.service.BustiaService;
import es.caib.ripea.core.helper.UnitatOrganitzativaHelper;
import es.caib.ripea.war.command.BustiaCommand;
import es.caib.ripea.war.command.BustiaFiltreCommand;
import es.caib.ripea.war.command.BustiaFiltreOrganigramaCommand;
import es.caib.ripea.war.helper.RequestSessionHelper;

/**
 * Controlador per al manteniment de bústies.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/bustiaAdminOrganigrama")
public class BustiaAdminOrganigramaController extends BaseAdminController {
	
	private static final String SESSION_ATTRIBUTE_FILTRE = "BustiaAdminOrganigramaController.session.filtre";

	@Autowired
	private BustiaService bustiaService;
	
	@Autowired
	private UnitatOrganitzativaHelper unitatOrganitzativaHelper;



	@RequestMapping(method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			Model model,
			Long bustiaId) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		BustiaFiltreOrganigramaCommand bustiaFiltreOrganigramaCommand = getFiltreOrganigramaCommand(request);
		model.addAttribute("bustiaFiltreOrganigramaCommand", bustiaFiltreOrganigramaCommand);
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
		BustiaDto bustia = null;
		if (bustiaId != null)
			bustia = bustiaService.findById(
					entitatActual.getId(),
					bustiaId);
		BustiaCommand command = null;
		if (bustia != null)
			command = BustiaCommand.asCommand(bustia);
		else
			command = new BustiaCommand();
		model.addAttribute("bustiaCommand", command);
		command.setEntitatId(entitatActual.getId());
		return "bustiaAdminOrganigrama";
	}
	
	


	private BustiaFiltreOrganigramaCommand getFiltreOrganigramaCommand(
			HttpServletRequest request) {
		BustiaFiltreOrganigramaCommand bustiaFiltreCommand = (BustiaFiltreOrganigramaCommand)RequestSessionHelper.obtenirObjecteSessio(
				request,
				SESSION_ATTRIBUTE_FILTRE);
		if (bustiaFiltreCommand == null) {
			bustiaFiltreCommand = new BustiaFiltreOrganigramaCommand();
			RequestSessionHelper.actualitzarObjecteSessio(
					request,
					SESSION_ATTRIBUTE_FILTRE,
					bustiaFiltreCommand);
		}
		return bustiaFiltreCommand;
	}
	
	

	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newGet(
			HttpServletRequest request,
			Model model) {
		
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		
		BustiaCommand command = new BustiaCommand();
		command.setEntitatId(entitatActual.getId());
		model.addAttribute(command);
		model.addAttribute("isOrganigrama", true);

		return "bustiaAdminForm";
	}
	
	
	
	@RequestMapping(value = "/{bustiaId}", method = RequestMethod.GET)
	public BustiaDto bustiaGet(
			HttpServletRequest request,
			@PathVariable Long bustiaId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		BustiaDto bustia = null;

			
		bustia = bustiaService.findById(
					entitatActual.getId(),
					bustiaId);
			
		// setting last historicos to the unitat of this bustia
		bustia.setUnitatOrganitzativa(unitatOrganitzativaHelper.getLastHistoricos(bustia.getUnitatOrganitzativa()));
				
		// getting all the busties connected with old unitat excluding the one you are currently in 
		List<BustiaDto> bustiesOfOldUnitat = bustiaService.findAmbUnitatCodiAdmin(entitatActual.getId(), bustia.getUnitatOrganitzativa().getCodi());
		List<BustiaDto> bustiesOfOldUnitatWithoutCurrent = new ArrayList<BustiaDto>();
		for(BustiaDto bustiaI: bustiesOfOldUnitat){
			if(!bustiaI.getId().equals(bustia.getId())){
				bustiesOfOldUnitatWithoutCurrent.add(bustiaI);
			}
		}
		return bustia;
	}
	

	
	
	
	
	

	
	
	
//	@RequestMapping(value = "/{bustiaId}", method = RequestMethod.GET)
//	public String formGet(
//			HttpServletRequest request,
//			@PathVariable Long bustiaId,
//			Model model) {
//		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
//		BustiaDto bustia = null;
//
//			
//		bustia = bustiaService.findById(
//					entitatActual.getId(),
//					bustiaId);
//			
//		// setting last historicos to the unitat of this bustia
//		bustia.setUnitatOrganitzativa(unitatOrganitzativaHelper.getLastHistoricos(bustia.getUnitatOrganitzativa()));
//			
//		
//		// getting all the busties connected with old unitat excluding the one you are currently in 
//		List<BustiaDto> bustiesOfOldUnitat = bustiaService.findAmbUnitatCodiAdmin(entitatActual.getId(), bustia.getUnitatOrganitzativa().getCodi());
//		List<BustiaDto> bustiesOfOldUnitatWithoutCurrent = new ArrayList<BustiaDto>();
//		for(BustiaDto bustiaI: bustiesOfOldUnitat){
//			if(!bustiaI.getId().equals(bustia.getId())){
//				bustiesOfOldUnitatWithoutCurrent.add(bustiaI);
//			}
//		}
//		model.addAttribute("bustiesOfOldUnitatWithoutCurrent", bustiesOfOldUnitatWithoutCurrent);
//		model.addAttribute(bustia);	
//		
//
//		BustiaCommand command = BustiaCommand.asCommand(bustia);
//
//		return "bustiaAdminForm";
//	}
	
	



	
	
	
}
