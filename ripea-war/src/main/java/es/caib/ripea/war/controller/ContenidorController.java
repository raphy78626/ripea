/**
 * 
 */
package es.caib.ripea.war.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.caib.ripea.core.api.dto.BustiaDto;
import es.caib.ripea.core.api.dto.ContingutDto;
import es.caib.ripea.core.api.dto.DadaDto;
import es.caib.ripea.core.api.dto.DocumentDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.dto.MetaDadaDto;
import es.caib.ripea.core.api.dto.MetaDadaTipusEnumDto;
import es.caib.ripea.core.api.dto.NodeDto;
import es.caib.ripea.core.api.dto.RegistreAnotacioDto;
import es.caib.ripea.core.api.service.BustiaService;
import es.caib.ripea.core.api.service.ContingutService;
import es.caib.ripea.core.api.service.DocumentService;
import es.caib.ripea.core.api.service.ExpedientService;
import es.caib.ripea.core.api.service.InteressatService;
import es.caib.ripea.core.api.service.MetaDadaService;
import es.caib.ripea.core.api.service.MetaDocumentService;
import es.caib.ripea.core.api.service.MetaExpedientService;
import es.caib.ripea.core.api.service.RegistreService;
import es.caib.ripea.war.command.ContenidorMoureCopiarEnviarCommand;
import es.caib.ripea.war.command.DadaCommand;
import es.caib.ripea.war.command.DadaCommand.DadaTipusBoolea;
import es.caib.ripea.war.command.DadaCommand.DadaTipusData;
import es.caib.ripea.war.command.DadaCommand.DadaTipusFlotant;
import es.caib.ripea.war.command.DadaCommand.DadaTipusImport;
import es.caib.ripea.war.command.DadaCommand.DadaTipusSencer;
import es.caib.ripea.war.command.DadaCommand.DadaTipusText;
import es.caib.ripea.war.datatable.DatatablesPagina;
import es.caib.ripea.war.helper.PaginacioHelper;
import es.caib.ripea.war.helper.SessioHelper;
import es.caib.ripea.war.helper.ValidationHelper;

/**
 * Controlador per al manteniment de contenidors.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
public class ContenidorController extends BaseUserController {

	private static final String CONTENIDOR_VISTA_ICONES = "icones";
	private static final String CONTENIDOR_VISTA_LLISTAT = "llistat";

	@Autowired
	private ContingutService contenidorService;
	@Autowired
	private MetaExpedientService metaExpedientService;
	@Autowired
	private MetaDocumentService metaDocumentService;
	@Autowired
	private DocumentService documentService;
	@Autowired
	private InteressatService interessatService;
	@Autowired
	private ExpedientService expedientService;
	@Autowired
	private MetaDadaService metaDadaService;
	@Autowired
	private BustiaService bustiaService;
	@Autowired
	private RegistreService registreService;

	@Autowired(required = true)
	private javax.validation.Validator validator;



	@RequestMapping(value = "/contenidor/{contenidorId}", method = RequestMethod.GET)
	public String contingutGet(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ContingutDto contingut = contenidorService.getContingutAmbFills(
				entitatActual.getId(),
				contenidorId);
		omplirModelPerMostrarContingut(
				request,
				entitatActual,
				contingut,
				model);
		return "contenidorContingut";
	}

	@RequestMapping(value = "/contenidor/{contenidorId}/dada/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesPagina<DadaDto> dadaDatatable(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		List<DadaDto> dades = null;
		ContingutDto contenidor = contenidorService.getContingutAmbFills(
				entitatActual.getId(),
				contenidorId);
		if (contenidor instanceof NodeDto) {
			NodeDto node = (NodeDto)contenidor;
			dades = node.getDades();
		} else {
			dades = new ArrayList<DadaDto>();
		}
		return PaginacioHelper.getPaginaPerDatatables(request, dades);
	}

	@RequestMapping(value = "/contenidor/{contenidorId}/dada/new", method = RequestMethod.GET)
	public String dadaNewGet(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			Model model) throws Exception {
		return dadaGet(
				request,
				contenidorId,
				null,
				model);
	}
	@RequestMapping(value = "/contenidor/{contenidorId}/dada/{dadaId}", method = RequestMethod.GET)
	public String dadaGet(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long dadaId,
			Model model) throws Exception {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ContingutDto contenidor = contenidorService.getContingutAmbFills(
				entitatActual.getId(),
				contenidorId);
		DadaDto dada = null;
		if (contenidor instanceof NodeDto) {
			NodeDto node = (NodeDto)contenidor;
			if (dadaId != null) {
				dada = contenidorService.dadaFindById(
						entitatActual.getId(),
						contenidorId,
						dadaId);
				List<MetaDadaDto> metaDades = new ArrayList<MetaDadaDto>();
				metaDades.add(dada.getMetaDada());
				model.addAttribute("metaDades", metaDades);
			} else {
				model.addAttribute(
						"metaDades",
						metaDadaService.findByNodePerCreacio(
								entitatActual.getId(),
								node.getId()));
			}
		}
		DadaCommand command;
		if (dada != null) {
			command = DadaCommand.asCommand(dada);
		} else {
			command = new DadaCommand();
			command.setId(dadaId);
		}
		command.setEntitatId(entitatActual.getId());
		command.setNodeId(contenidorId);
		model.addAttribute(command);
		return "contenidorDadaForm";
	}
	@RequestMapping(value = "/contenidor/{contenidorId}/dada/new", method = RequestMethod.POST)
	public String dadaNewPost(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@ModelAttribute DadaCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		List<Class<?>> grups = new ArrayList<Class<?>>();
		if (command.getMetaDadaId() != null) {
			List<MetaDadaDto> metaDades = metaDadaService.findByNodePerCreacio(
					entitatActual.getId(),
					contenidorId);
			for (MetaDadaDto metaDada: metaDades) {
				if (command.getMetaDadaId().equals(metaDada.getId())) {
					if (metaDada.getTipus().equals(MetaDadaTipusEnumDto.TEXT))
						grups.add(DadaTipusText.class);
					if (metaDada.getTipus().equals(MetaDadaTipusEnumDto.DATA))
						grups.add(DadaTipusData.class);
					if (metaDada.getTipus().equals(MetaDadaTipusEnumDto.SENCER))
						grups.add(DadaTipusSencer.class);
					if (metaDada.getTipus().equals(MetaDadaTipusEnumDto.FLOTANT))
						grups.add(DadaTipusFlotant.class);
					if (metaDada.getTipus().equals(MetaDadaTipusEnumDto.IMPORT))
						grups.add(DadaTipusImport.class);
					if (metaDada.getTipus().equals(MetaDadaTipusEnumDto.BOOLEA))
						grups.add(DadaTipusBoolea.class);
					break;
				}
			}
		}
		new ValidationHelper(validator).isValid(
				command,
				bindingResult,
				grups.toArray(new Class[grups.size()]));
		if (bindingResult.hasErrors()) {
			ContingutDto contenidor = contenidorService.getContingutAmbFills(
					entitatActual.getId(),
					contenidorId);
			if (contenidor instanceof NodeDto) {
				NodeDto node = (NodeDto)contenidor;
				model.addAttribute(
						"metaDades",
						metaDadaService.findByNodePerCreacio(
								entitatActual.getId(),
								node.getId()));
			}
			return "contenidorDadaForm";
		}
		if (command.getId() != null) {
			contenidorService.dadaUpdate(
					entitatActual.getId(),
					contenidorId,
					command.getId(),
					command.getValor());
			return getModalControllerReturnValueSuccess(
					request,
					"redirect:../../../contenidor/" + contenidorId,
					"contenidor.controller.dada.modificada.ok");
		} else {
			contenidorService.dadaCreate(
					entitatActual.getId(),
					contenidorId,
					command.getMetaDadaId(),
					command.getValor());
			return getModalControllerReturnValueSuccess(
					request,
					"redirect:../../../contenidor/" + contenidorId,
					"contenidor.controller.dada.creada.ok");
		}
	}

	@RequestMapping(value = "/contenidor/{contenidorId}/dada/{dadaId}/delete", method = RequestMethod.GET)
	public String dadaDelete(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long dadaId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		contenidorService.dadaDelete(
				entitatActual.getId(),
				contenidorId,
				dadaId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../../../contenidor/" + contenidorId,
				"contenidor.controller.dada.esborrada.ok");
	}

	@RequestMapping(value = "/contenidor/{contenidorId}/llistaMetaDadesCrear", method = RequestMethod.GET)
	@ResponseBody
	public List<MetaDadaDto> llistaMetas(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ContingutDto contenidor = contenidorService.getContingutAmbFills(
				entitatActual.getId(),
				contenidorId);
		if (contenidor instanceof NodeDto)
			return metaDadaService.findByNodePerCreacio(
					entitatActual.getId(),
					contenidorId);
		else
			return new ArrayList<MetaDadaDto>();
	}

	@RequestMapping(value = "/contenidor/{contenidorId}/delete", method = RequestMethod.GET)
	public String delete(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ContingutDto contenidor = contenidorService.getContingutAmbFills(
				entitatActual.getId(),
				contenidorId);
		contenidorService.deleteReversible(
				entitatActual.getId(),
				contenidorId);
		return getAjaxControllerReturnValueSuccess(
				request,
				(contenidor.getPare() != null) ? "redirect:../../contenidor/" + contenidor.getPare().getId() : "redirect:../../escriptori",
				"contenidor.controller.element.esborrat.ok");
	}

	@RequestMapping(value = "/contenidor/{contenidorId}/canviVista/icones", method = RequestMethod.GET)
	public String canviVistaLlistat(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			Model model) {
		getEntitatActualComprovantPermisos(request);
		SessioHelper.updateContenidorVista(
				request,
				CONTENIDOR_VISTA_ICONES);
		return "redirect:../../" + contenidorId;
	}
	@RequestMapping(value = "/contenidor/{contenidorId}/canviVista/llistat", method = RequestMethod.GET)
	public String canviVistaIcones(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			Model model) {
		getEntitatActualComprovantPermisos(request);
		SessioHelper.updateContenidorVista(
				request,
				CONTENIDOR_VISTA_LLISTAT);
		return "redirect:../../" + contenidorId;
	}

	@RequestMapping(value = "/contenidor/{contenidorOrigenId}/moure", method = RequestMethod.GET)
	public String moureForm(
			HttpServletRequest request,
			@PathVariable Long contenidorOrigenId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		omplirModelPerMoureOCopiar(
				entitatActual,
				contenidorOrigenId,
				model);
		ContenidorMoureCopiarEnviarCommand command = new ContenidorMoureCopiarEnviarCommand();
		command.setContenidorOrigenId(contenidorOrigenId);
		model.addAttribute(command);
		return "contenidorMoureForm";
	}
	@RequestMapping(value = "/contenidor/{contenidorOrigenId}/moure", method = RequestMethod.POST)
	public String moure(
			HttpServletRequest request,
			@PathVariable Long contenidorOrigenId,
			@Valid ContenidorMoureCopiarEnviarCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			omplirModelPerMoureOCopiar(
					entitatActual,
					contenidorOrigenId,
					model);
			return "contenidorMoureForm";
		}
		contenidorService.move(
				entitatActual.getId(),
				contenidorOrigenId,
				command.getContenidorDestiId());
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../" + contenidorOrigenId,
				"contenidor.controller.element.mogut.ok");
	}
	@RequestMapping(value = "/contenidor/{contenidorOrigenId}/moure/{contenidorDestiId}", method = RequestMethod.GET)
	public String moureDragDrop(
			HttpServletRequest request,
			@PathVariable Long contenidorOrigenId,
			@PathVariable Long contenidorDestiId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ContingutDto contenidorOrigen = contenidorService.getContingutAmbFills(
				entitatActual.getId(),
				contenidorOrigenId);
		contenidorService.move(
				entitatActual.getId(),
				contenidorOrigenId,
				contenidorDestiId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../" + contenidorOrigen.getPare().getId(),
				"contenidor.controller.element.mogut.ok");
	}

	@RequestMapping(value = "/contenidor/{contenidorOrigenId}/copiar", method = RequestMethod.GET)
	public String copiarForm(
			HttpServletRequest request,
			@PathVariable Long contenidorOrigenId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		omplirModelPerMoureOCopiar(
				entitatActual,
				contenidorOrigenId,
				model);
		ContenidorMoureCopiarEnviarCommand command = new ContenidorMoureCopiarEnviarCommand();
		command.setContenidorOrigenId(contenidorOrigenId);
		model.addAttribute(command);
		return "contenidorCopiarForm";
	}
	@RequestMapping(value = "/contenidor/{contenidorOrigenId}/copiar", method = RequestMethod.POST)
	public String copiar(
			HttpServletRequest request,
			@PathVariable Long contenidorOrigenId,
			@Valid ContenidorMoureCopiarEnviarCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			omplirModelPerMoureOCopiar(
					entitatActual,
					contenidorOrigenId,
					model);
			return "contenidorCopiarForm";
		}
		ContingutDto contenidorCreat = contenidorService.copy(
				entitatActual.getId(),
				contenidorOrigenId,
				command.getContenidorDestiId(),
				true);
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../" + contenidorCreat.getId(),
				"contenidor.controller.element.copiat.ok");
	}
	@RequestMapping(value = "/contenidor/{contenidorOrigenId}/enviar", method = RequestMethod.GET)
	public String enviarForm(
			HttpServletRequest request,
			@PathVariable Long contenidorOrigenId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		omplirModelPerEnviar(
				entitatActual,
				contenidorOrigenId,
				model);
		ContenidorMoureCopiarEnviarCommand command = new ContenidorMoureCopiarEnviarCommand();
		command.setContenidorOrigenId(contenidorOrigenId);
		model.addAttribute(command);
		return "contenidorEnviarForm";
	}
	@RequestMapping(value = "/contenidor/{contenidorOrigenId}/enviar", method = RequestMethod.POST)
	public String enviar(
			HttpServletRequest request,
			@PathVariable Long contenidorOrigenId,
			@Valid ContenidorMoureCopiarEnviarCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			omplirModelPerEnviar(
					entitatActual,
					contenidorOrigenId,
					model);
			return "contenidorEnviarForm";
		}
		bustiaService.enviarContingut(
				entitatActual.getId(),
				command.getContenidorDestiId(),
				contenidorOrigenId,
				command.getComentariEnviar());
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../" + contenidorOrigenId,
				"contenidor.controller.element.enviat.ok");
	}

	@RequestMapping(value = "/contenidor/{contenidorId}/errors", method = RequestMethod.GET)
	public String errors(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"contenidor",
				contenidorService.getContingutAmbFills(
						entitatActual.getId(),
						contenidorId));
		model.addAttribute(
				"errors",
				contenidorService.findErrorsValidacio(
						entitatActual.getId(),
						contenidorId));
		return "contenidorErrors";
	}

	@RequestMapping(value = "/contenidor/{contenidorId}/registre/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesPagina<RegistreAnotacioDto> registreDatatable(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		List<RegistreAnotacioDto> registres = null;
		ContingutDto contenidor = contenidorService.getContingutAmbFills(
				entitatActual.getId(),
				contenidorId);
		if (contenidor instanceof ExpedientDto) {
			ExpedientDto expedient = (ExpedientDto)contenidor;
			registres = expedient.getFillsRegistres();
		} else {
			registres = new ArrayList<RegistreAnotacioDto>();
		}
		return PaginacioHelper.getPaginaPerDatatables(request, registres);
	}
	@RequestMapping(value = "/contenidor/{contenidorId}/registre/{registreId}", method = RequestMethod.GET)
	public String registreInfo(
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
	@RequestMapping(value = "/contenidor/{contenidorId}/registre/{registreId}/log", method = RequestMethod.GET)
	public String registreLog(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long registreId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"contenidor",
				contenidorService.getContingutAmbFills(
						entitatActual.getId(),
						contenidorId));
		model.addAttribute(
				"registre",
				registreService.findOne(
						entitatActual.getId(),
						contenidorId,
						registreId));
		model.addAttribute(
				"moviments",
				contenidorService.findMovimentsPerContingutUser(
						entitatActual.getId(),
						registreId));
		return "registreLog";
	}

	@RequestMapping(value = "/contenidor/{contenidorId}/log", method = RequestMethod.GET)
	public String log(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"contenidor",
				contenidorService.getContingutAmbFills(
						entitatActual.getId(),
						contenidorId));
		model.addAttribute(
				"logs",
				contenidorService.findLogsPerContingutUser(
						entitatActual.getId(),
						contenidorId));
		model.addAttribute(
				"moviments",
				contenidorService.findMovimentsPerContingutUser(
						entitatActual.getId(),
						contenidorId));
		return "contenidorLog";
	}

	@RequestMapping(value = "/contenidor/{contenidorId}/nti")
	public String nti(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"contenidor",
				contenidorService.getContingutAmbFills(
						entitatActual.getId(),
						contenidorId));
		return "contenidorNti";
	}



	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(
	    		Date.class,
	    		new CustomDateEditor(
	    				new SimpleDateFormat("dd/MM/yyyy"),
	    				true));
	}



	private void omplirModelPerMostrarContingut(
			HttpServletRequest request,
			EntitatDto entitatActual,
			ContingutDto contingut,
			Model model) {
		model.addAttribute("contenidor", contingut);
		model.addAttribute(
				"metaExpedients",
				metaExpedientService.findActiusAmbEntitatPerCreacio(entitatActual.getId()));
		model.addAttribute(
				"metaDocuments",
				metaDocumentService.findActiveByEntitatAndContenidorPerCreacio(
						entitatActual.getId(),
						contingut.getId()));
		if (contingut instanceof ExpedientDto) {
			model.addAttribute(
					"interessats",
					interessatService.findByExpedient(
							entitatActual.getId(),
							contingut.getId()));
			model.addAttribute("relacionats", expedientService.relacioFindAmbExpedient(
						entitatActual.getId(),
						contingut.getId()));			
		}
		if (contingut instanceof DocumentDto) {
			model.addAttribute(
					"documentVersions",
					documentService.findVersionsByDocument(
							entitatActual.getId(),
							contingut.getId()));
		}
		String contenidorVista = SessioHelper.getContenidorVista(request);
		if (contenidorVista == null)
			contenidorVista = CONTENIDOR_VISTA_ICONES;
		model.addAttribute(
				"vistaIcones",
				new Boolean(CONTENIDOR_VISTA_ICONES.equals(contenidorVista)));
		model.addAttribute(
				"vistaLlistat",
				new Boolean(CONTENIDOR_VISTA_LLISTAT.equals(contenidorVista)));
	}

	private void omplirModelPerMoureOCopiar(
			EntitatDto entitatActual,
			Long contenidorOrigenId,
			Model model) {
		ContingutDto contingutOrigen = contenidorService.getContingutAmbFills(
				entitatActual.getId(),
				contenidorOrigenId);
		model.addAttribute(
				"contenidorOrigen",
				contingutOrigen);
	}

	private void omplirModelPerEnviar(
			EntitatDto entitatActual,
			Long contenidorOrigenId,
			Model model) {
		ContingutDto contingutOrigen = contenidorService.getContingutAmbFills(
				entitatActual.getId(),
				contenidorOrigenId);
		model.addAttribute(
				"contenidorOrigen",
				contingutOrigen);
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

}
