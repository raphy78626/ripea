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
import javax.validation.Validator;

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
import es.caib.ripea.core.api.dto.DocumentEnviamentEstatEnumDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.dto.InteressatDto;
import es.caib.ripea.core.api.dto.InteressatTipusEnumDto;
import es.caib.ripea.core.api.dto.MetaDadaDto;
import es.caib.ripea.core.api.dto.MetaDadaTipusEnumDto;
import es.caib.ripea.core.api.dto.NodeDto;
import es.caib.ripea.core.api.dto.RegistreAnotacioDto;
import es.caib.ripea.core.api.registre.RegistreTipusEnum;
import es.caib.ripea.core.api.service.BustiaService;
import es.caib.ripea.core.api.service.ContingutService;
import es.caib.ripea.core.api.service.DocumentService;
import es.caib.ripea.core.api.service.ExpedientInteressatService;
import es.caib.ripea.core.api.service.ExpedientService;
import es.caib.ripea.core.api.service.MetaDadaService;
import es.caib.ripea.core.api.service.MetaDocumentService;
import es.caib.ripea.core.api.service.MetaExpedientService;
import es.caib.ripea.core.api.service.RegistreService;
import es.caib.ripea.war.command.ContingutMoureCopiarEnviarCommand;
import es.caib.ripea.war.command.DadaCommand;
import es.caib.ripea.war.command.DadaCommand.DadaTipusBoolea;
import es.caib.ripea.war.command.DadaCommand.DadaTipusData;
import es.caib.ripea.war.command.DadaCommand.DadaTipusFlotant;
import es.caib.ripea.war.command.DadaCommand.DadaTipusImport;
import es.caib.ripea.war.command.DadaCommand.DadaTipusSencer;
import es.caib.ripea.war.command.DadaCommand.DadaTipusText;
import es.caib.ripea.war.helper.DatatablesHelper;
import es.caib.ripea.war.helper.DatatablesHelper.DatatablesResponse;
import es.caib.ripea.war.helper.EnumHelper;
import es.caib.ripea.war.helper.MissatgesHelper;
import es.caib.ripea.war.helper.SessioHelper;
import es.caib.ripea.war.helper.ValidationHelper;

/**
 * Controlador per a la gestió de contenidors i mètodes compartits entre
 * diferents tipus de contingut.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
public class ContingutController extends BaseUserController {

	private static final String CONTENIDOR_VISTA_ICONES = "icones";
	private static final String CONTENIDOR_VISTA_LLISTAT = "llistat";

	@Autowired
	private ContingutService contingutService;
	@Autowired
	private MetaExpedientService metaExpedientService;
	@Autowired
	private MetaDocumentService metaDocumentService;
	@Autowired
	private DocumentService documentService;
	@Autowired
	private ExpedientInteressatService interessatService;
	@Autowired
	private ExpedientService expedientService;
	@Autowired
	private MetaDadaService metaDadaService;
	@Autowired
	private BustiaService bustiaService;
	@Autowired
	private RegistreService registreService;

	@Autowired
	private Validator validator;



	@RequestMapping(value = "/contingut/{contingutId}", method = RequestMethod.GET)
	public String contingutGet(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ContingutDto contingut = contingutService.findAmbIdUser(
				entitatActual.getId(),
				contingutId,
				true);
		omplirModelPerMostrarContingut(
				request,
				entitatActual,
				contingut,
				model);
		return "contingut";
	}

	@RequestMapping(value = "/contingut/{contingutId}/dada/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesResponse dadaDatatable(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		List<DadaDto> dades = null;
		ContingutDto contingut = contingutService.findAmbIdUser(
				entitatActual.getId(),
				contingutId,
				true);
		if (contingut instanceof NodeDto) {
			NodeDto node = (NodeDto)contingut;
			dades = node.getDades();
		}
		return DatatablesHelper.getDatatableResponse(
				request,
				dades);
	}

	@RequestMapping(value = "/contingut/{contingutId}/dada/new", method = RequestMethod.GET)
	public String dadaNewGet(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			Model model) throws Exception {
		return dadaGet(
				request,
				contingutId,
				null,
				model);
	}
	@RequestMapping(value = "/contingut/{contingutId}/dada/{dadaId}", method = RequestMethod.GET)
	public String dadaGet(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			@PathVariable Long dadaId,
			Model model) throws Exception {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ContingutDto contingut = contingutService.findAmbIdUser(
				entitatActual.getId(),
				contingutId,
				true);
		DadaDto dada = null;
		if (contingut instanceof NodeDto) {
			NodeDto node = (NodeDto)contingut;
			if (dadaId != null) {
				dada = contingutService.dadaFindById(
						entitatActual.getId(),
						contingutId,
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
		command.setNodeId(contingutId);
		model.addAttribute(command);
		return "contingutDadaForm";
	}
	@RequestMapping(value = "/contingut/{contingutId}/dada/new", method = RequestMethod.POST)
	public String dadaNewPost(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			@ModelAttribute DadaCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		List<Class<?>> grups = new ArrayList<Class<?>>();
		if (command.getMetaDadaId() != null) {
			List<MetaDadaDto> metaDades = metaDadaService.findByNodePerCreacio(
					entitatActual.getId(),
					contingutId);
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
			ContingutDto contingut = contingutService.findAmbIdUser(
					entitatActual.getId(),
					contingutId,
					true);
			if (contingut instanceof NodeDto) {
				NodeDto node = (NodeDto)contingut;
				model.addAttribute(
						"metaDades",
						metaDadaService.findByNodePerCreacio(
								entitatActual.getId(),
								node.getId()));
			}
			return "contingutDadaForm";
		}
		if (command.getId() != null) {
			contingutService.dadaUpdate(
					entitatActual.getId(),
					contingutId,
					command.getId(),
					command.getValor());
			return getModalControllerReturnValueSuccess(
					request,
					"redirect:../../../contingut/" + contingutId,
					"contingut.controller.dada.modificada.ok");
		} else {
			contingutService.dadaCreate(
					entitatActual.getId(),
					contingutId,
					command.getMetaDadaId(),
					command.getValor());
			return getModalControllerReturnValueSuccess(
					request,
					"redirect:../../../contingut/" + contingutId,
					"contingut.controller.dada.creada.ok");
		}
	}

	@RequestMapping(value = "/contingut/{contingutId}/dada/{dadaId}/delete", method = RequestMethod.GET)
	public String dadaDelete(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			@PathVariable Long dadaId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		contingutService.dadaDelete(
				entitatActual.getId(),
				contingutId,
				dadaId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../../../contingut/" + contingutId,
				"contingut.controller.dada.esborrada.ok");
	}

	@RequestMapping(value = "/contingut/{contingutId}/llistaMetaDadesCrear", method = RequestMethod.GET)
	@ResponseBody
	public List<MetaDadaDto> llistaMetas(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ContingutDto contingut = contingutService.findAmbIdUser(
				entitatActual.getId(),
				contingutId,
				true);
		if (contingut instanceof NodeDto)
			return metaDadaService.findByNodePerCreacio(
					entitatActual.getId(),
					contingutId);
		else
			return new ArrayList<MetaDadaDto>();
	}

	@RequestMapping(value = "/contingut/{contingutId}/delete", method = RequestMethod.GET)
	public String delete(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ContingutDto contingut = contingutService.findAmbIdUser(
				entitatActual.getId(),
				contingutId,
				true);
		contingutService.deleteReversible(
				entitatActual.getId(),
				contingutId);
		return getAjaxControllerReturnValueSuccess(
				request,
				(contingut.getPare() != null) ? "redirect:../../contingut/" + contingut.getPare().getId() : "redirect:../../escriptori",
				"contingut.controller.element.esborrat.ok");
	}

	@RequestMapping(value = "/contingut/{contingutId}/canviVista/icones", method = RequestMethod.GET)
	public String canviVistaLlistat(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			Model model) {
		getEntitatActualComprovantPermisos(request);
		SessioHelper.updateContenidorVista(
				request,
				CONTENIDOR_VISTA_ICONES);
		return "redirect:../../" + contingutId;
	}
	@RequestMapping(value = "/contingut/{contingutId}/canviVista/llistat", method = RequestMethod.GET)
	public String canviVistaIcones(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			Model model) {
		getEntitatActualComprovantPermisos(request);
		SessioHelper.updateContenidorVista(
				request,
				CONTENIDOR_VISTA_LLISTAT);
		return "redirect:../../" + contingutId;
	}

	@RequestMapping(value = "/contingut/{contingutOrigenId}/moure", method = RequestMethod.GET)
	public String moureForm(
			HttpServletRequest request,
			@PathVariable Long contingutOrigenId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		omplirModelPerMoureOCopiar(
				entitatActual,
				contingutOrigenId,
				model);
		ContingutMoureCopiarEnviarCommand command = new ContingutMoureCopiarEnviarCommand();
		command.setOrigenId(contingutOrigenId);
		model.addAttribute(command);
		return "contingutMoureForm";
	}
	@RequestMapping(value = "/contingut/{contingutOrigenId}/moure", method = RequestMethod.POST)
	public String moure(
			HttpServletRequest request,
			@PathVariable Long contingutOrigenId,
			@Valid ContingutMoureCopiarEnviarCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			omplirModelPerMoureOCopiar(
					entitatActual,
					contingutOrigenId,
					model);
			return "contingutMoureForm";
		}
		contingutService.move(
				entitatActual.getId(),
				contingutOrigenId,
				command.getDestiId());
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../" + contingutOrigenId,
				"contingut.controller.element.mogut.ok");
	}
	@RequestMapping(value = "/contingut/{contingutOrigenId}/moure/{contingutDestiId}", method = RequestMethod.GET)
	public String moureDragDrop(
			HttpServletRequest request,
			@PathVariable Long contingutOrigenId,
			@PathVariable Long contingutDestiId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ContingutDto contingutOrigen = contingutService.findAmbIdUser(
				entitatActual.getId(),
				contingutOrigenId,
				true);
		contingutService.move(
				entitatActual.getId(),
				contingutOrigenId,
				contingutDestiId);
		return getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../" + contingutOrigen.getPare().getId(),
				"contingut.controller.element.mogut.ok");
	}

	@RequestMapping(value = "/contingut/{contingutOrigenId}/copiar", method = RequestMethod.GET)
	public String copiarForm(
			HttpServletRequest request,
			@PathVariable Long contingutOrigenId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		omplirModelPerMoureOCopiar(
				entitatActual,
				contingutOrigenId,
				model);
		ContingutMoureCopiarEnviarCommand command = new ContingutMoureCopiarEnviarCommand();
		command.setOrigenId(contingutOrigenId);
		model.addAttribute(command);
		return "contingutCopiarForm";
	}
	@RequestMapping(value = "/contingut/{contingutOrigenId}/copiar", method = RequestMethod.POST)
	public String copiar(
			HttpServletRequest request,
			@PathVariable Long contingutOrigenId,
			@Valid ContingutMoureCopiarEnviarCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			omplirModelPerMoureOCopiar(
					entitatActual,
					contingutOrigenId,
					model);
			return "contingutCopiarForm";
		}
		ContingutDto contingutCreat = contingutService.copy(
				entitatActual.getId(),
				contingutOrigenId,
				command.getDestiId(),
				true);
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../" + contingutCreat.getId(),
				"contingut.controller.element.copiat.ok");
	}
	@RequestMapping(value = "/contingut/{contingutOrigenId}/enviar", method = RequestMethod.GET)
	public String enviarForm(
			HttpServletRequest request,
			@PathVariable Long contingutOrigenId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		omplirModelPerEnviar(
				entitatActual,
				contingutOrigenId,
				model);
		ContingutMoureCopiarEnviarCommand command = new ContingutMoureCopiarEnviarCommand();
		command.setOrigenId(contingutOrigenId);
		model.addAttribute(command);
		return "contingutEnviarForm";
	}
	@RequestMapping(value = "/contingut/{contingutOrigenId}/enviar", method = RequestMethod.POST)
	public String enviar(
			HttpServletRequest request,
			@PathVariable Long contingutOrigenId,
			@Valid ContingutMoureCopiarEnviarCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			omplirModelPerEnviar(
					entitatActual,
					contingutOrigenId,
					model);
			return "contingutEnviarForm";
		}
		bustiaService.enviarContingut(
				entitatActual.getId(),
				command.getDestiId(),
				contingutOrigenId,
				command.getComentariEnviar());
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../" + contingutOrigenId,
				"contingut.controller.element.enviat.ok");
	}

	@RequestMapping(value = "/contingut/{contingutId}/errors", method = RequestMethod.GET)
	public String errors(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"contingut",
				contingutService.findAmbIdUser(
						entitatActual.getId(),
						contingutId,
						true));
		model.addAttribute(
				"errors",
				contingutService.findErrorsValidacio(
						entitatActual.getId(),
						contingutId));
		return "contingutErrors";
	}

	@RequestMapping(value = "/contingut/{contingutId}/registre/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesResponse registreDatatable(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		List<RegistreAnotacioDto> registres = null;
		ContingutDto contingut = contingutService.findAmbIdUser(
				entitatActual.getId(),
				contingutId,
				true);
		if (contingut instanceof ExpedientDto) {
			ExpedientDto expedient = (ExpedientDto)contingut;
			registres = expedient.getFillsRegistres();
		}
		return DatatablesHelper.getDatatableResponse(
				request,
				registres);
	}
	@RequestMapping(value = "/contingut/{contingutId}/registre/{registreId}", method = RequestMethod.GET)
	public String registreInfo(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			@PathVariable Long registreId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"registre",
				registreService.findOne(
						entitatActual.getId(),
						contingutId,
						registreId));
		return "registreDetall";
	}
	@RequestMapping(value = "/contingut/{contingutId}/registre/{registreId}/reintentar", method = RequestMethod.GET)
	public String reintentar(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			@PathVariable Long registreId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		boolean processatOk = registreService.reglaReintentarUser(
				entitatActual.getId(),
				contingutId,
				registreId);
		if (processatOk) {
			return getModalControllerReturnValueSuccess(
					request,
					"redirect:../../../" + contingutId,
					"contenidor.controller.registre.reintentat.ok");
		} else {
			MissatgesHelper.error(
					request,
					getMessage(
							request, 
							"contenidor.controller.registre.reintentat.error",
							null));
			return "redirect:../" + registreId;
		}
	}
	/*@RequestMapping(value = "/contingut/{contingutId}/registre/{registreId}/log", method = RequestMethod.GET)
	public String registreLog(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			@PathVariable Long registreId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"contingut",
				contingutService.findAmbIdUser(
						entitatActual.getId(),
						contingutId,
						true));
		model.addAttribute(
				"registre",
				registreService.findOne(
						entitatActual.getId(),
						contingutId,
						registreId));
		model.addAttribute(
				"moviments",
				contingutService.findMovimentsPerContingutUser(
						entitatActual.getId(),
						registreId));
		return "registreLog";
	}*/
	
	@RequestMapping(value = "/contingut/{contingutId}/interessat/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesResponse interessatDatatable(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		List<InteressatDto> interessats = null;
		ContingutDto contingut = contingutService.findAmbIdUser(
				entitatActual.getId(),
				contingutId,
				true);
		if (contingut instanceof ExpedientDto) {
			interessats = interessatService.findByExpedient(
					entitatActual.getId(),
					contingutId);
		}
		return DatatablesHelper.getDatatableResponse(
				request,
				interessats);
	}
	

	@RequestMapping(value = "/contingut/{contingutId}/log", method = RequestMethod.GET)
	public String log(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"contingut",
				contingutService.findAmbIdUser(
						entitatActual.getId(),
						contingutId,
						true));
		model.addAttribute(
				"logs",
				contingutService.findLogsPerContingutUser(
						entitatActual.getId(),
						contingutId));
		model.addAttribute(
				"moviments",
				contingutService.findMovimentsPerContingutUser(
						entitatActual.getId(),
						contingutId));
		return "contingutLog";
	}

	@RequestMapping(value = "/contingut/{contingutId}/nti")
	public String nti(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"contingut",
				contingutService.findAmbIdUser(
						entitatActual.getId(),
						contingutId,
						true));
		return "contingutNti";
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
		model.addAttribute("contingut", contingut);
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
		String contingutVista = SessioHelper.getContenidorVista(request);
		if (contingutVista == null)
			contingutVista = CONTENIDOR_VISTA_ICONES;
		model.addAttribute(
				"vistaIcones",
				new Boolean(CONTENIDOR_VISTA_ICONES.equals(contingutVista)));
		model.addAttribute(
				"vistaLlistat",
				new Boolean(CONTENIDOR_VISTA_LLISTAT.equals(contingutVista)));
		model.addAttribute(
				"registreTipusEnumOptions",
				EnumHelper.getOptionsForEnum(
						RegistreTipusEnum.class,
						"registre.anotacio.tipus.enum."));
		model.addAttribute(
				"enviamentEstatEnumOptions",
				EnumHelper.getOptionsForEnum(
						DocumentEnviamentEstatEnumDto.class,
						"enviament.estat.enum."));
		model.addAttribute(
				"interessatTipusEnumOptions",
				EnumHelper.getOptionsForEnum(
						InteressatTipusEnumDto.class,
						"interessat.tipus.enum."));
	}

	private void omplirModelPerMoureOCopiar(
			EntitatDto entitatActual,
			Long contingutOrigenId,
			Model model) {
		ContingutDto contingutOrigen = contingutService.findAmbIdUser(
				entitatActual.getId(),
				contingutOrigenId,
				true);
		model.addAttribute(
				"contingutOrigen",
				contingutOrigen);
	}

	private void omplirModelPerEnviar(
			EntitatDto entitatActual,
			Long contingutOrigenId,
			Model model) {
		ContingutDto contingutOrigen = contingutService.findAmbIdUser(
				entitatActual.getId(),
				contingutOrigenId,
				true);
		model.addAttribute(
				"contingutOrigen",
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
