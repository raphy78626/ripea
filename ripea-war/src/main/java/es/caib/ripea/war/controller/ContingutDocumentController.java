/**
 * 
 */
package es.caib.ripea.war.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.caib.ripea.core.api.dto.ContingutDto;
import es.caib.ripea.core.api.dto.DocumentDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.MetaDocumentDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.service.ContingutService;
import es.caib.ripea.core.api.service.DocumentService;
import es.caib.ripea.core.api.service.MetaDocumentService;
import es.caib.ripea.war.command.DocumentCommand;
import es.caib.ripea.war.command.DocumentCommand.CreateDigital;
import es.caib.ripea.war.command.DocumentCommand.CreateFisic;
import es.caib.ripea.war.command.DocumentCommand.UpdateDigital;
import es.caib.ripea.war.command.DocumentCommand.UpdateFisic;
import es.caib.ripea.war.helper.MissatgesHelper;

/**
 * Controlador per al manteniment de documents.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/contingut")
public class ContingutDocumentController extends BaseUserController {

	@Autowired
	private ContingutService contingutService;
	@Autowired
	private DocumentService documentService;
	@Autowired
	private MetaDocumentService metaDocumentService;



	@RequestMapping(value = "/{contingutId}/document/new", method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			Model model) {
		return get(request, contingutId, null, model);
	}
	@RequestMapping(value = "/{contingutId}/document/{documentId}", method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			@PathVariable Long documentId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		DocumentDto document = null;
		if (documentId != null) {
			document = documentService.findById(
					entitatActual.getId(),
					documentId);
		}
		DocumentCommand command = null;
		if (document != null) {
			command = DocumentCommand.asCommand(document);
			model.addAttribute(
					"metaDocuments",
					metaDocumentService.findActiveByEntitatAndContenidorPerModificacio(
							entitatActual.getId(),
							documentId));
		} else {
			command = new DocumentCommand();
			command.setData(new Date());
			model.addAttribute(
					"metaDocuments",
					metaDocumentService.findActiveByEntitatAndContenidorPerCreacio(
							entitatActual.getId(),
							contingutId));
		}
		command.setEntitatId(entitatActual.getId());
		command.setPareId(contingutId);
		model.addAttribute(command);
		return "contingutDocumentForm";
	}
	@RequestMapping(value = "/{contingutId}/document/digital/new", method = RequestMethod.POST)
	public String postDigitalNew(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			@Validated({CreateDigital.class}) DocumentCommand command,
			BindingResult bindingResult,
			Model model) throws IOException {
		if (bindingResult.hasErrors()) {
			EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
			model.addAttribute(
					"metaDocuments",
					metaDocumentService.findActiveByEntitatAndContenidorPerCreacio(
							entitatActual.getId(),
							contingutId));
			return "contingutDocumentForm";
		}
		return createUpdateDocument(
				request,
				contingutId,
				command,
				bindingResult,
				model);
	}
	@RequestMapping(value = "/{contingutId}/document/digital/update", method = RequestMethod.POST)
	public String postDigitalUpdate(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			@Validated({UpdateDigital.class}) DocumentCommand command,
			BindingResult bindingResult,
			Model model) throws IOException {
		if (bindingResult.hasErrors()) {
			EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
			model.addAttribute(
					"metaDocuments",
					metaDocumentService.findActiveByEntitatAndContenidorPerModificacio(
							entitatActual.getId(),
							contingutId));
			return "contingutDocumentForm";
		}
		return createUpdateDocument(
				request,
				contingutId,
				command,
				bindingResult,
				model);
	}
	@RequestMapping(value = "/{contingutId}/document/fisic/new", method = RequestMethod.POST)
	public String postFisicNew(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			@Validated({CreateFisic.class}) DocumentCommand command,
			BindingResult bindingResult,
			Model model) throws IOException {
		if (bindingResult.hasErrors()) {
			EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
			model.addAttribute(
					"metaDocuments",
					metaDocumentService.findActiveByEntitatAndContenidorPerCreacio(
							entitatActual.getId(),
							contingutId));
			return "contingutDocumentForm";
		}
		return createUpdateDocument(
				request,
				contingutId,
				command,
				bindingResult,
				model);
	}
	@RequestMapping(value = "/{contingutId}/document/fisic/update", method = RequestMethod.POST)
	public String postFisicUpdate(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			@Validated({UpdateFisic.class}) DocumentCommand command,
			BindingResult bindingResult,
			Model model) throws IOException {
		if (bindingResult.hasErrors()) {
			EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
			model.addAttribute(
					"metaDocuments",
					metaDocumentService.findActiveByEntitatAndContenidorPerModificacio(
							entitatActual.getId(),
							contingutId));
			return "contingutDocumentForm";
		}
		return createUpdateDocument(
				request,
				contingutId,
				command,
				bindingResult,
				model);
	}

	@RequestMapping(value = "/{contingutId}/document/{documentId}/descarregar/{versio}", method = RequestMethod.GET)
	public String descarregar(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable Long contingutId,
			@PathVariable Long documentId,
			@PathVariable int versio) throws IOException {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ContingutDto contingut = contingutService.findAmbIdUser(
				entitatActual.getId(),
				documentId,
				true);
		if (contingut instanceof DocumentDto) {
			FitxerDto fitxer = documentService.descarregar(
					entitatActual.getId(),
					documentId,
					versio);
			writeFileToResponse(
					fitxer.getNom(),
					fitxer.getContingut(),
					response);
			return null;
		}
		MissatgesHelper.error(
				request, 
				getMessage(
						request, 
						"document.controller.descarregar.error"));
		if (contingut.getPare() != null)
			return "redirect:../../contingut/" + contingutId;
		else
			return "redirect:../../escriptori";
	}

	@RequestMapping(value = "/{contingutId}/document/{documentId}/versio/{versio}/descarregar", method = RequestMethod.GET)
	public String descarregarVersio(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable Long contingutId,
			@PathVariable Long documentId,
			@PathVariable int versio) throws IOException {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		FitxerDto fitxer = documentService.descarregar(
				entitatActual.getId(),
				documentId,
				versio);
		writeFileToResponse(
				fitxer.getNom(),
				fitxer.getContingut(),
				response);
		return null;
	}

	@RequestMapping(value = "/{contingutId}/metaDocument/{metaDocumentId}", method = RequestMethod.GET)
	@ResponseBody
	public MetaDocumentDto metaDocumentInfo(
			HttpServletRequest request,
			@PathVariable Long contingutId,
			@PathVariable Long metaDocumentId) throws IOException {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		List<MetaDocumentDto> metaDocuments = metaDocumentService.findActiveByEntitatAndContenidorPerCreacio(
				entitatActual.getId(),
				contingutId);
		for (MetaDocumentDto metaDocument: metaDocuments) {
			if (metaDocument.getId().equals(metaDocumentId))
				return metaDocument;
		}
		return null;
	}

	@RequestMapping(value = "/{contingutId}/metaDocument/{metaDocumentId}/plantilla", method = RequestMethod.GET)
	public String metaDocumentPlantilla(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable Long contingutId,
			@PathVariable Long metaDocumentId) throws IOException {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		FitxerDto plantilla = metaDocumentService.getPlantilla(
				entitatActual.getId(),
				metaDocumentId);
		writeFileToResponse(
				plantilla.getNom(),
				plantilla.getContingut(),
				response);
		return null;
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(
	    		Date.class,
	    		new CustomDateEditor(
	    				new SimpleDateFormat("dd/MM/yyyy"),
	    				true));
	}



	private String createUpdateDocument(
			HttpServletRequest request,
			Long contingutId,
			DocumentCommand command,
			BindingResult bindingResult,
			Model model) throws NotFoundException, ValidationException, IOException {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (command.getId() == null) {
			documentService.create(
					entitatActual.getId(),
					contingutId,
					command.getDocumentTipus(),
					command.getMetaNodeId(),
					command.getNom(),
					command.getData(),
					command.getArxiu().getOriginalFilename(),
					command.getArxiu().getContentType(),
					command.getArxiu().getBytes(),
					command.getUbicacio());
			return getModalControllerReturnValueSuccess(
					request,
					"redirect:../../../contingut/" + contingutId,
					"document.controller.creat.ok");
		} else {
			if (command.getArxiu() == null || command.getArxiu().isEmpty()) {
				documentService.update(
						entitatActual.getId(),
						command.getId(),
						command.getDocumentTipus(),
						command.getMetaNodeId(),
						command.getNom(),
						command.getData(),
						null,
						null,
						null,
						command.getUbicacio());
			} else {
				documentService.update(
						entitatActual.getId(),
						command.getId(),
						command.getDocumentTipus(),
						command.getMetaNodeId(),
						command.getNom(),
						command.getData(),
						command.getArxiu().getOriginalFilename(),
						command.getArxiu().getContentType(),
						command.getArxiu().getBytes(),
						command.getUbicacio());
			}
			return getModalControllerReturnValueSuccess(
					request,
					"redirect:../contingut/" + command.getPareId(),
					"document.controller.modificat.ok");
		}
	}

}
