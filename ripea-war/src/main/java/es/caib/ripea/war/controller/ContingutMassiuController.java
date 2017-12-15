/**
 * 
 */
package es.caib.ripea.war.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.caib.ripea.core.api.dto.ContingutTipusEnumDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.EscriptoriDto;
import es.caib.ripea.core.api.dto.ExecucioMassivaContingutDto;
import es.caib.ripea.core.api.dto.ExecucioMassivaDto;
import es.caib.ripea.core.api.dto.ExecucioMassivaDto.ExecucioMassivaTipusDto;
import es.caib.ripea.core.api.dto.ExpedientSelectorDto;
import es.caib.ripea.core.api.dto.UsuariDto;
import es.caib.ripea.core.api.service.AplicacioService;
import es.caib.ripea.core.api.service.ContingutService;
import es.caib.ripea.core.api.service.ExecucioMassivaService;
import es.caib.ripea.core.api.service.ExpedientService;
import es.caib.ripea.core.api.service.MetaDocumentService;
import es.caib.ripea.core.api.service.MetaExpedientService;
import es.caib.ripea.war.command.ContingutMassiuFiltreCommand;
import es.caib.ripea.war.command.PortafirmesEnviarCommand;
import es.caib.ripea.war.helper.DatatablesHelper;
import es.caib.ripea.war.helper.DatatablesHelper.DatatablesResponse;
import es.caib.ripea.war.helper.RequestSessionHelper;
import es.caib.ripea.war.helper.RolHelper;

/**
 * Controlador per al manteniment de bústies.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/massiu")
public class ContingutMassiuController extends BaseUserOAdminController {
	
	private static final String SESSION_ATTRIBUTE_FILTRE = "ContingutMassiuController.session.filtre";
	private static final String SESSION_ATTRIBUTE_SELECCIO = "ContingutMassiuController.session.seleccio";

	@Autowired
	private ContingutService contingutService;
	@Autowired
	private ContingutService contenidorService;
	@Autowired
	private MetaExpedientService metaExpedientService;
	@Autowired
	private MetaDocumentService metaDocumentService;
	@Autowired
	private ExecucioMassivaService execucioMassivaService;
	@Autowired
	private ExpedientService expedientService;
	@Autowired
	private AplicacioService aplicacioService;

	@RequestMapping(value = "/portafirmes", method = RequestMethod.GET)
	public String getDocuments(
			HttpServletRequest request,
			Model model) {
		
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		EscriptoriDto escriptori = contenidorService.getEscriptoriPerUsuariActual(entitatActual.getId());
		
		ContingutMassiuFiltreCommand filtreCommand = getFiltreCommand(request);
		filtreCommand.setTipusElement(ContingutTipusEnumDto.DOCUMENT);
		filtreCommand.setBloquejarTipusElement(true);
		filtreCommand.setBloquejarMetaDada(true);
		filtreCommand.setBloquejarMetaExpedient(false);
		
		model.addAttribute(
				"seleccio",
				RequestSessionHelper.obtenirObjecteSessio(
						request,
						SESSION_ATTRIBUTE_SELECCIO));
		model.addAttribute(
				"titolMassiu",
				getMessage(request, "accio.massiva.titol.portafirmes"));
		model.addAttribute(
				"botoMassiu",
				getMessage(request, "accio.massiva.boto.crear.portafirmes"));
		model.addAttribute(
				filtreCommand);
		model.addAttribute(
				"metaExpedients",
				metaExpedientService.findActiusAmbEntitatPerCreacio(entitatActual.getId()));
		model.addAttribute(
				"metaDocuments",
				metaDocumentService.findActiveByEntitatAndContenidorPerCreacio(
						entitatActual.getId(),
						escriptori.getId()));
		
		List<ExpedientSelectorDto> expedients = new ArrayList<ExpedientSelectorDto>();
		if (filtreCommand.getTipusExpedient() != null)
			expedients = expedientService.findPerUserAndTipus(entitatActual.getId(), filtreCommand.getTipusExpedient());
		
		model.addAttribute(
				"expedients",
				expedients);
		
		return "contingutMassiuList";
	}
	
	@RequestMapping(value = "/portafirmes", method = RequestMethod.POST)
	public String bustiaPost(
			HttpServletRequest request,
			@Valid ContingutMassiuFiltreCommand filtreCommand,
			BindingResult bindingResult,
			Model model) {
		if (!bindingResult.hasErrors()) {
			RequestSessionHelper.actualitzarObjecteSessio(
					request,
					SESSION_ATTRIBUTE_FILTRE,
					filtreCommand);
		}
		
		filtreCommand.setTipusElement(ContingutTipusEnumDto.DOCUMENT);
		filtreCommand.setBloquejarTipusElement(true);
		filtreCommand.setBloquejarMetaDada(true);
		filtreCommand.setBloquejarMetaExpedient(false);
		
		return "redirect:/massiu/portafirmes";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/crear/portafirmes", method = RequestMethod.GET)
	public String getCrearPortafirmes(
			HttpServletRequest request,
			Model model) {
		
		Set<Long> seleccio = (Set<Long>)RequestSessionHelper.obtenirObjecteSessio(
				request,
				SESSION_ATTRIBUTE_SELECCIO);
		
		if (seleccio == null || seleccio.isEmpty()) {
			return getModalControllerReturnValueError(
					request,
					"redirect:/massiu/portafirmes",
					"accio.massiva.seleccio.buida");
		}
		
		getEntitatActualComprovantPermisos(request);
		
		PortafirmesEnviarCommand command = new PortafirmesEnviarCommand();
		model.addAttribute(command);
		
		return "enviarPortafirmes";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/crear/portafirmes", method = RequestMethod.POST)
	public String postCrearPortafirmes(
			HttpServletRequest request,
			@Valid PortafirmesEnviarCommand filtreCommand,
			BindingResult bindingResult,
			Model model) {
		
		if (bindingResult.hasErrors()) {
			return "enviarPortafirmes";
		}
		
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		
		Set<Long> seleccio = (Set<Long>)RequestSessionHelper.obtenirObjecteSessio(
				request,
				SESSION_ATTRIBUTE_SELECCIO);
		
		ExecucioMassivaDto dto = new ExecucioMassivaDto();
		dto.setTipus(ExecucioMassivaTipusDto.PORTASIGNATURES);
		dto.setDataInici(filtreCommand.getDataInici());
		dto.setEnviarCorreu(filtreCommand.isEnviarCorreu());
		dto.setMotiu(filtreCommand.getMotiu());
		dto.setPrioritat(filtreCommand.getPrioritat());
		dto.setDataCaducitat(filtreCommand.getDataCaducitat());
		dto.setContingutIds(new ArrayList<Long>(seleccio));

		execucioMassivaService.crearExecucioMassiva(entitatActual.getId(), dto);
		
		RequestSessionHelper.esborrarObjecteSessio(request, SESSION_ATTRIBUTE_SELECCIO);
		
		return getModalControllerReturnValueSuccess(
				request,
				"redirect:../../../massiu/portafirmes",
				"accio.massiva.creat.ok");
	}

	@RequestMapping(value = "/datatable", method = RequestMethod.GET)
	@ResponseBody
	public DatatablesResponse datatable(
			HttpServletRequest request) {
		
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ContingutMassiuFiltreCommand contingutMassiuFiltreCommand = getFiltreCommand(request);
		
		return DatatablesHelper.getDatatableResponse(
				request,
				 contingutService.documentMassiuFindByDatatable(
							entitatActual.getId(), 
							ContingutMassiuFiltreCommand.asDto(contingutMassiuFiltreCommand),
							DatatablesHelper.getPaginacioDtoFromRequest(request)),
				 "id",
				 SESSION_ATTRIBUTE_SELECCIO);
		
	}
	
	@RequestMapping(value = "/expedients/{metaExpedientId}", method = RequestMethod.GET)
	@ResponseBody
	public List<ExpedientSelectorDto> findAll(
			HttpServletRequest request,
			@PathVariable Long metaExpedientId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		
		List<ExpedientSelectorDto> expedients = new ArrayList<ExpedientSelectorDto>();
		if (metaExpedientId != null)
			expedients = expedientService.findPerUserAndTipus(entitatActual.getId(), metaExpedientId);
		return expedients;
	}
	
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	@ResponseBody
	public int select(
			HttpServletRequest request,
			@RequestParam(value="ids[]", required = false) Long[] ids) {
		@SuppressWarnings("unchecked")
		Set<Long> seleccio = (Set<Long>)RequestSessionHelper.obtenirObjecteSessio(
				request,
				SESSION_ATTRIBUTE_SELECCIO);
		if (seleccio == null) {
			seleccio = new HashSet<Long>();
			RequestSessionHelper.actualitzarObjecteSessio(
					request,
					SESSION_ATTRIBUTE_SELECCIO,
					seleccio);
		}
		if (ids != null) {
			for (Long id: ids) {
				seleccio.add(id);
			}
		} else {
			EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
			ContingutMassiuFiltreCommand filtreCommand = getFiltreCommand(request);
			seleccio.addAll(
					contingutService.findIdsMassiusAmbFiltre(
							entitatActual.getId(),
							ContingutMassiuFiltreCommand.asDto(filtreCommand)));
		}
		return seleccio.size();
	}
	@RequestMapping(value = "/deselect", method = RequestMethod.GET)
	@ResponseBody
	public int deselect(
			HttpServletRequest request,
			@RequestParam(value="ids[]", required = false) Long[] ids) {
		@SuppressWarnings("unchecked")
		Set<Long> seleccio = (Set<Long>)RequestSessionHelper.obtenirObjecteSessio(
				request,
				SESSION_ATTRIBUTE_SELECCIO);
		if (seleccio == null) {
			seleccio = new HashSet<Long>();
			RequestSessionHelper.actualitzarObjecteSessio(
					request,
					SESSION_ATTRIBUTE_SELECCIO,
					seleccio);
		}
		if (ids != null) {
			for (Long id: ids) {
				seleccio.remove(id);
			}
		} else {
			seleccio.clear();
		}
		return seleccio.size();
	}
	
	
	@RequestMapping(value = "/consulta/{pagina}", method = RequestMethod.GET)
	public String getConsultaExecucions(
			HttpServletRequest request,
			@PathVariable int pagina,
			Model model) {
		
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		
		pagina = (pagina < 0 ? 0 : pagina);
		
		List<ExecucioMassivaDto> execucionsMassives = new ArrayList<ExecucioMassivaDto>();
		
		UsuariDto usuariActual = null;
		if (RolHelper.isUsuariActualAdministrador(request)) {
			model.addAttribute(
					"titolConsulta",
					getMessage(request, "accio.massiva.consulta.titol.gobal"));
		} else if (RolHelper.isUsuariActualUsuari(request)) {
			usuariActual = aplicacioService.getUsuariActual();
			model.addAttribute(
					"titolConsulta",
					getMessage(request, "accio.massiva.consulta.titol.usuari", new String[]{usuariActual.getNom()}));
		}
		
		execucionsMassives = execucioMassivaService.findExecucionsMassivesPerUsuari(entitatActual.getId(), usuariActual,pagina);
		
		if (execucionsMassives.size() < 8)
			model.addAttribute("sumador", 0);
		else
			model.addAttribute("sumador", 1);
		
		model.addAttribute("pagina",pagina);
		model.addAttribute("execucionsMassives", execucionsMassives);
		
		return "consultaExecucionsMassives";
	}
	
	@RequestMapping(value = "/consultaContingut/{execucioMassivaId}", method = RequestMethod.GET)
	@ResponseBody
	public List<ExecucioMassivaContingutDto> getConsultaContinguts(
			HttpServletRequest request,
			@PathVariable Long execucioMassivaId) {
		
		if (RolHelper.isUsuariActualUsuari(request)) 
			getEntitatActualComprovantPermisos(request);
		
		return execucioMassivaService.findContingutPerExecucioMassiva(execucioMassivaId);
		
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(
	    		Date.class,
	    		new CustomDateEditor(
	    				new SimpleDateFormat("dd/MM/yyyy"),
	    				true));
	}
	
	private ContingutMassiuFiltreCommand getFiltreCommand(
			HttpServletRequest request) {
		ContingutMassiuFiltreCommand filtreCommand = (ContingutMassiuFiltreCommand)RequestSessionHelper.obtenirObjecteSessio(
				request,
				SESSION_ATTRIBUTE_FILTRE);
		if (filtreCommand == null) {
			filtreCommand = new ContingutMassiuFiltreCommand();
			RequestSessionHelper.actualitzarObjecteSessio(
					request,
					SESSION_ATTRIBUTE_FILTRE,
					filtreCommand);
		}
		return filtreCommand;
	}
	
	

}
