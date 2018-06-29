/**
 * 
 */
package es.caib.ripea.war.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
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
import es.caib.ripea.core.api.service.UnitatOrganitzativaService;
import es.caib.ripea.war.command.UnitatOrganitzativaFiltreCommand;
import es.caib.ripea.war.helper.DatatablesHelper;
import es.caib.ripea.war.helper.DatatablesHelper.DatatablesResponse;
import es.caib.ripea.war.helper.RequestSessionHelper;

/**
 * Controlador per al manteniment de unitats organitzatives.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/unitatOrganitzativa")
public class UnitatOrganitzativaController extends BaseAdminController{
	
	private static final String SESSION_ATTRIBUTE_FILTRE = "UnitatOrganitzativaController.session.filtre";

	@Autowired
	private UnitatOrganitzativaService unitatOrganitzativaService;
	
	@Autowired
	private BustiaService bustiaService;


	@RequestMapping(method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			Model model) {
		UnitatOrganitzativaFiltreCommand unitatOrganitzativaFiltreCommand = getFiltreCommand(request);
		model.addAttribute("unitatOrganitzativaFiltreCommand", unitatOrganitzativaFiltreCommand);
		return "unitatOrganitzativaList";
	}
	
	@RequestMapping(value = "/synchronizeGet", method = RequestMethod.GET)
	public String synchronizeGet(
			HttpServletRequest request,
			Model model) {
		
	
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		
		
		MultiMap splitMap = new MultiHashMap();
		MultiMap mergeOrSubstMap = new MultiHashMap();
		MultiMap mergeMap = new MultiHashMap();
		MultiMap substMap = new MultiHashMap();
		List<UnitatOrganitzativaDto> unitatsVigents = new ArrayList<>();
		
		boolean isFirstSincronization = unitatOrganitzativaService.isFirstSincronization(entitatActual.getId());
		
		if(isFirstSincronization){
			
		} else {
			List<UnitatOrganitzativaDto> unitatsVigentObsoleteDto = unitatOrganitzativaService
					.predictSynchronization(entitatActual.getId());


			// differentiate between split and (subst/merge)
			for (UnitatOrganitzativaDto vigentObsolete : unitatsVigentObsoleteDto) {
				if (vigentObsolete.getLastHistoricosUnitats().size() > 1) {
					for (UnitatOrganitzativaDto hist : vigentObsolete.getLastHistoricosUnitats()) {
						splitMap.put(vigentObsolete, hist);
					}
				} else if (vigentObsolete.getLastHistoricosUnitats().size() == 1) {
					// check if the map already contains key with this codi
					UnitatOrganitzativaDto mergeOrSubstKeyWS = vigentObsolete.getLastHistoricosUnitats().get(0);
					UnitatOrganitzativaDto keyWithTheSameCodi = null;
					Set<UnitatOrganitzativaDto> keysMergeOrSubst = mergeOrSubstMap.keySet();
					for (UnitatOrganitzativaDto mergeOrSubstKeyMap : keysMergeOrSubst) {
						if (mergeOrSubstKeyMap.getCodi().equals(mergeOrSubstKeyWS.getCodi())) {
							keyWithTheSameCodi = mergeOrSubstKeyMap;
						}
					}
					// if it contains already key with the same codi, assign
					// found key
					if (keyWithTheSameCodi != null) {
						mergeOrSubstMap.put(keyWithTheSameCodi, vigentObsolete);
					} else {
						mergeOrSubstMap.put(mergeOrSubstKeyWS, vigentObsolete);
					}
				}
			}


			// differantiate between substitution and merge
			Set<UnitatOrganitzativaDto> keysMergeOrSubst = mergeOrSubstMap.keySet();
			for (UnitatOrganitzativaDto mergeOrSubstKey : keysMergeOrSubst) {
				List<UnitatOrganitzativaDto> values = (List<UnitatOrganitzativaDto>) mergeOrSubstMap
						.get(mergeOrSubstKey);
				if (values.size() > 1) {
					for (UnitatOrganitzativaDto value : values) {
						mergeMap.put(mergeOrSubstKey, value);
					}
				} else {
					substMap.put(mergeOrSubstKey, values.get(0));
				}
			}

			// getting list of unitats with only properties changed
			unitatsVigents = unitatOrganitzativaService
					.getVigentsFromWebService(entitatActual.getId());

			// Set<UnitatOrganitzativaDto> keysSplit = splitMap.keySet();
			// System.out.println("SPLITS: ");
			// for (UnitatOrganitzativaDto splitKey : keysSplit) {
			// System.out.println(splitMap.get(splitKey));
			// }
			//
			// Set<UnitatOrganitzativaDto> mergeKeys = mergeMap.keySet();
			// System.out.println("MERGES: ");
			// for (UnitatOrganitzativaDto mergeKey : mergeKeys) {
			// System.out.println(mergeKey.getCodi() + " "+mergeKey+": "+
			// mergeOrSubstMap.get(mergeKey));
			// }
			//
			// Set<UnitatOrganitzativaDto> substKeys = substMap.keySet();
			// System.out.println("SUBSTUTIONS: ");
			// for (UnitatOrganitzativaDto substKey : substKeys) {
			// System.out.println(substKey.getCodi() + " "+substKey+": "+
			// mergeOrSubstMap.get(substKey));
			// }
		}
		
		

		model.addAttribute("splitMap", splitMap);
		model.addAttribute("mergeMap", mergeMap);
		model.addAttribute("substMap", substMap);
		model.addAttribute("unitatsVigents", unitatsVigents);
		model.addAttribute("isFirstSincronization", isFirstSincronization);
		
		
		return "synchronizationPrediction";
	}
	
	@RequestMapping(value = "/saveSynchronize", method = RequestMethod.POST)
	public String synchronizePost(
			HttpServletRequest request) {
		
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		unitatOrganitzativaService.synchronize(entitatActual.getId());

		return getModalControllerReturnValueSuccess(
				request,
				"redirect:unitatOrganitzativa",
				"unitat.controller.synchronize.ok");
	}
	
	
	
	
	@RequestMapping(value = "/mostrarArbre", method = RequestMethod.GET)
	public String mostrarArbre(
			HttpServletRequest request,
			Model model) {
		
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		
		model.addAttribute(
				"arbreUnitatsOrganitzatives",
				unitatOrganitzativaService.findTree(entitatActual.getId()));
		
		return "unitatArbre";
	}
	
	
	
	@RequestMapping(method = RequestMethod.POST)
	public String bustiaPost(
			HttpServletRequest request,
			@Valid UnitatOrganitzativaFiltreCommand filtreCommand,
			BindingResult bindingResult,
			Model model) {
		if (!bindingResult.hasErrors()) {
			RequestSessionHelper.actualitzarObjecteSessio(
					request,
					SESSION_ATTRIBUTE_FILTRE,
					filtreCommand);
		}
		return "redirect:unitatOrganitzativa";
	}
	
	
	
	@RequestMapping(value = "/{unitatId}/unitatTransicioInfo", method = RequestMethod.GET)
	public String formGet(
			HttpServletRequest request,
			@PathVariable Long unitatId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		UnitatOrganitzativaDto unitat = null;
		if (unitatId != null)
			unitat = unitatOrganitzativaService.findById(
					unitatId);
		model.addAttribute(unitat);
		
		
		// getting all the busties connected with old unitat excluding the one you are currently in 
		List<BustiaDto> bustiesOfOldUnitat = bustiaService.findAmbUnitatCodiAdmin(entitatActual.getId(), unitat.getCodi());
		model.addAttribute("bustiesOfOldUnitat", bustiesOfOldUnitat);	

	
		return "unitatTransicioInfo";
	}
	
	

	@RequestMapping(value = "/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesResponse datatable(
			HttpServletRequest request) {

		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		UnitatOrganitzativaFiltreCommand unitatOrganitzativaFiltreCommand = getFiltreCommand(request);
		return DatatablesHelper.getDatatableResponse(
				request,
				unitatOrganitzativaService.findAmbFiltre(
						entitatActual.getId(),
						UnitatOrganitzativaFiltreCommand.asDto(unitatOrganitzativaFiltreCommand),
						DatatablesHelper.getPaginacioDtoFromRequest(request)),
				"id");
	}
	
	private UnitatOrganitzativaFiltreCommand getFiltreCommand(
			HttpServletRequest request) {
		UnitatOrganitzativaFiltreCommand unitatOrganitzativaFiltreCommand = (UnitatOrganitzativaFiltreCommand)RequestSessionHelper.obtenirObjecteSessio(
				request,
				SESSION_ATTRIBUTE_FILTRE);
		if (unitatOrganitzativaFiltreCommand == null) {
			unitatOrganitzativaFiltreCommand = new UnitatOrganitzativaFiltreCommand();
			RequestSessionHelper.actualitzarObjecteSessio(
					request,
					SESSION_ATTRIBUTE_FILTRE,
					unitatOrganitzativaFiltreCommand);
		}
		return unitatOrganitzativaFiltreCommand;
	}


}
