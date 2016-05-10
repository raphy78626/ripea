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
import javax.validation.Valid;

import org.fundaciobit.plugins.signatureweb.api.FileInfoSignature;
import org.fundaciobit.plugins.signatureweb.api.StatusSignature;
import org.fundaciobit.plugins.signatureweb.api.StatusSignaturesSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.caib.ripea.core.api.dto.ContenidorDto;
import es.caib.ripea.core.api.dto.DocumentDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.MetaDocumentDto;
import es.caib.ripea.core.api.dto.UsuariDto;
import es.caib.ripea.core.api.service.AplicacioService;
import es.caib.ripea.core.api.service.ContenidorService;
import es.caib.ripea.core.api.service.DocumentService;
import es.caib.ripea.core.api.service.MetaDocumentService;
import es.caib.ripea.war.command.ContenidorCommand.Create;
import es.caib.ripea.war.command.ContenidorCommand.Update;
import es.caib.ripea.war.command.DocumentCommand;
import es.caib.ripea.war.command.FirmaAppletCommand;
import es.caib.ripea.war.command.PassarelaFirmaEnviarCommand;
import es.caib.ripea.war.command.PortafirmesEnviarCommand;
import es.caib.ripea.war.helper.MissatgesHelper;
import es.caib.ripea.war.helper.ModalHelper;
import es.caib.ripea.war.passarelafirma.PassarelaFirmaHelper;
import es.caib.ripea.war.passarelafirma.PassarelaFirmaSignaturesSet;

/**
 * Controlador per al manteniment de documents dels contenidors.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/contenidor")
public class ContenidorDocumentController extends BaseUserController {

	@Autowired
	private AplicacioService aplicacioService;
	@Autowired
	private ContenidorService contenidorService;
	@Autowired
	private DocumentService documentService;
	@Autowired
	private MetaDocumentService metaDocumentService;

	@Autowired
	private PassarelaFirmaHelper passarelaFirmaHelper;



	@RequestMapping(value = "/{contenidorId}/document/new", method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			Model model) {
		return get(request, contenidorId, null, model);
	}
	@RequestMapping(value = "/{contenidorId}/document/{documentId}", method = RequestMethod.GET)
	public String get(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
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
							contenidorId));
		}
		command.setEntitatId(entitatActual.getId());
		command.setPareId(contenidorId);
		model.addAttribute(command);
		return "contenidorDocumentForm";
	}
	@RequestMapping(value = "/{contenidorId}/document/new", method = RequestMethod.POST)
	public String postNew(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@Validated({Create.class}) DocumentCommand command,
			BindingResult bindingResult,
			Model model) throws IOException {
		if (bindingResult.hasErrors()) {
			EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
			model.addAttribute(
					"metaDocuments",
					metaDocumentService.findActiveByEntitatAndContenidorPerCreacio(
							entitatActual.getId(),
							contenidorId));
			return "contenidorDocumentForm";
		}
		return postUpdate(
				request,
				contenidorId,
				command,
				bindingResult,
				model);
	}
	@RequestMapping(value = "/{contenidorId}/document/update", method = RequestMethod.POST)
	public String postUpdate(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@Validated({Update.class}) DocumentCommand command,
			BindingResult bindingResult,
			Model model) throws IOException {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			model.addAttribute(
					"metaDocuments",
					metaDocumentService.findActiveByEntitatAndContenidorPerModificacio(
							entitatActual.getId(),
							contenidorId));
			return "contenidorDocumentForm";
		}
		if (command.getId() == null) {
			documentService.create(
					entitatActual.getId(),
					contenidorId,
					command.getMetaNodeId(),
					command.getNom(),
					command.getData(),
					command.getArxiu().getOriginalFilename(),
					command.getArxiu().getContentType(),
					command.getArxiu().getBytes());
			return getModalControllerReturnValueSuccess(
					request,
					"redirect:../../../contenidor/" + contenidorId,
					"document.controller.creat.ok");
		} else {
			if (command.getArxiu() == null || command.getArxiu().isEmpty()) {
				documentService.update(
						entitatActual.getId(),
						command.getId(),
						command.getMetaNodeId(),
						command.getNom(),
						command.getData(),
						null,
						null,
						null);
			} else {
				documentService.update(
						entitatActual.getId(),
						command.getId(),
						command.getMetaNodeId(),
						command.getNom(),
						command.getData(),
						command.getArxiu().getOriginalFilename(),
						command.getArxiu().getContentType(),
						command.getArxiu().getBytes());
			}
			return getModalControllerReturnValueSuccess(
					request,
					"redirect:../contenidor/" + command.getPareId(),
					"document.controller.modificat.ok");
		}
	}

	@RequestMapping(value = "/{contenidorId}/document/{documentId}/descarregar", method = RequestMethod.GET)
	public String descarregar(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable Long contenidorId,
			@PathVariable Long documentId) throws IOException {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		ContenidorDto contenidor = contenidorService.getContenidorAmbContingut(
				entitatActual.getId(),
				documentId);
		if (contenidor instanceof DocumentDto) {
			DocumentDto document = (DocumentDto)contenidor;
			FitxerDto fitxer = documentService.descarregar(
					entitatActual.getId(),
					documentId,
					document.getDarreraVersio().getVersio());
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
		if (contenidor.getPare() != null)
			return "redirect:../../contenidor/" + contenidorId;
		else
			return "redirect:../../escriptori";
	}

	@RequestMapping(value = "/{contenidorId}/document/{documentId}/versio/{versio}/descarregar", method = RequestMethod.GET)
	public String descarregarVersio(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable Long contenidorId,
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

	@RequestMapping(value = "/{contenidorId}/metaDocument/{metaDocumentId}", method = RequestMethod.GET)
	@ResponseBody
	public MetaDocumentDto metaDocumentInfo(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long metaDocumentId) throws IOException {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		List<MetaDocumentDto> metaDocuments = metaDocumentService.findActiveByEntitatAndContenidorPerCreacio(
				entitatActual.getId(),
				contenidorId);
		for (MetaDocumentDto metaDocument: metaDocuments) {
			if (metaDocument.getId().equals(metaDocumentId))
				return metaDocument;
		}
		return null;
	}

	@RequestMapping(value = "/{contenidorId}/metaDocument/{metaDocumentId}/plantilla", method = RequestMethod.GET)
	public String metaDocumentPlantilla(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable Long contenidorId,
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

	@RequestMapping(value = "/{contenidorId}/document/{documentId}/versio/{versio}/portafirmes/upload", method = RequestMethod.GET)
	public String portafirmesUploadGet(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long documentId,
			@PathVariable int versio,
			Model model) {
		emplenarModelPortafirmes(
				request,
				documentId,
				versio,
				model);
		model.addAttribute(new PortafirmesEnviarCommand());
		return "portafirmesEnviarForm";
	}
	@RequestMapping(value = "/{contenidorId}/document/{documentId}/versio/{versio}/portafirmes/upload", method = RequestMethod.POST)
	public String portafirmesUploadPost(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long documentId,
			@PathVariable int versio,
			@Valid PortafirmesEnviarCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			emplenarModelPortafirmes(
					request,
					documentId,
					versio,
					model);
			return "portafirmesEnviarForm";
		}
		documentService.portafirmesEnviar(
				entitatActual.getId(),
				documentId,
				versio,
				command.getMotiu(),
				command.getPrioritat(),
				command.getDataCaducitat());
		return this.getModalControllerReturnValueSuccess(
				request,
				"redirect:../../../../../../../contenidor/" + documentId,
				"document.controller.portafirmes.upload.ok");
	}

	@RequestMapping(value = "/{contenidorId}/document/{documentId}/versio/{versio}/portafirmes/cancel", method = RequestMethod.GET)
	public String portafirmesCancel(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long documentId,
			@PathVariable int versio,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		documentService.portafirmesCancelar(
				entitatActual.getId(),
				documentId,
				versio);
		return this.getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../../../../../../contenidor/" + documentId,
				"document.controller.portafirmes.cancel.ok");
	}

	@RequestMapping(value = "/{contenidorId}/document/{documentId}/versio/{versio}/custodia/reintentar", method = RequestMethod.GET)
	public String custodiaReintentar(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long documentId,
			@PathVariable int versio,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		documentService.custodiaPortafirmesReintentar(
				entitatActual.getId(),
				documentId,
				versio);
		return this.getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../../../../../../contenidor/" + documentId,
				"document.controller.custodia.reintentar.ok");
	}

	@RequestMapping(value = "/{contenidorId}/document/{documentId}/versio/{versio}/custodia/esborrar", method = RequestMethod.GET)
	public String custodiaEsborrar(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long documentId,
			@PathVariable int versio,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		documentService.custodiaEsborrar(
				entitatActual.getId(),
				documentId,
				versio);
		return this.getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../../../../../../contenidor/" + documentId,
				"document.controller.custodia.esborrar.ok");
	}

	@RequestMapping(value = "/{contenidorId}/document/{documentId}/versio/{versio}/pdf", method = RequestMethod.GET)
	public String convertirPdf(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable Long contenidorId,
			@PathVariable Long documentId,
			@PathVariable int versio) throws IOException {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		FitxerDto convertit = documentService.convertirPdfPerFirma(
				entitatActual.getId(),
				documentId,
				versio);
		writeFileToResponse(
				convertit.getNom(),
				convertit.getContingut(),
				response);
		return null;
	}

	@RequestMapping(value = "/{contenidorId}/document/{documentId}/versio/{versio}/firmaApplet", method = RequestMethod.GET)
	public String firmaAppletGet(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long documentId,
			@PathVariable int versio,
			Model model) {
		String identificador = emplenarModelFirmaApplet(
				request,
				documentId,
				versio,
				model);
		model.addAttribute(
				new FirmaAppletCommand(identificador));
		return "firmaAppletForm";
	}

	@RequestMapping(value = "/{contenidorId}/document/{documentId}/versio/{versio}/firmaPassarela", method = RequestMethod.GET)
	public String firmaPassarelaGet(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long documentId,
			@PathVariable int versio,
			Model model) {
		emplenarModelPassarela(
				request,
				documentId,
				versio,
				model);
		model.addAttribute(new PassarelaFirmaEnviarCommand());
		return "passarelaFirmaForm";
	}
	@RequestMapping(value = "/{contenidorId}/document/{documentId}/versio/{versio}/firmaPassarela", method = RequestMethod.POST)
	public String firmaPassarelaPost(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long documentId,
			@PathVariable int versio,
			@Valid PassarelaFirmaEnviarCommand command,
			BindingResult bindingResult,
			Model model) throws IOException {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			emplenarModelPassarela(
					request,
					documentId,
					versio,
					model);
			return "passarelaFirmaForm";
		}
		FitxerDto fitxerPerFirmar = documentService.convertirPdfPerFirma(
				entitatActual.getId(),
				documentId,
				versio);
		UsuariDto usuariActual = aplicacioService.getUsuariActual();
		String modalStr = (ModalHelper.isModal(request)) ? "/modal" : "";
		String procesFirmaUrl = passarelaFirmaHelper.iniciarProcesDeFirma(
				request,
				fitxerPerFirmar,
				usuariActual.getNif(),
				command.getMotiu(),
				command.getLloc(),
				usuariActual.getEmail(),
				LocaleContextHolder.getLocale().getLanguage(),
				modalStr + "/contenidor/" + contenidorId + "/document/" + documentId + "/versio/" + versio + "/firmaPassarelaFinal",
				false);
		return "redirect:" + procesFirmaUrl;
	}

	@RequestMapping(value = "/{contenidorId}/document/{documentId}/versio/{versio}/firmaPassarelaFinal")
	public String firmaPassarelaFinal(
			HttpServletRequest request,
			@PathVariable Long contenidorId,
			@PathVariable Long documentId,
			@PathVariable int versio,
			@RequestParam("signaturesSetId") String signaturesSetId,
			Model model) {
		PassarelaFirmaSignaturesSet signaturesSet = passarelaFirmaHelper.finalitzarProcesDeFirma(
				request,
				signaturesSetId);
		StatusSignaturesSet status = signaturesSet.getStatusSignaturesSet();
		switch (status.getStatus()) {
		case StatusSignaturesSet.STATUS_FINAL_OK:
			FileInfoSignature firmaInfo = signaturesSet.getFileInfoSignatureArray()[0];
			StatusSignature firmaStatus = firmaInfo.getStatusSignature();
			if (firmaStatus.getStatus() == StatusSignature.STATUS_FINAL_OK) {
				if (firmaStatus.getSignedData() == null || !firmaStatus.getSignedData().exists()) {
					firmaStatus.setStatus(StatusSignature.STATUS_FINAL_ERROR);
					String msg = "L'estat indica que ha finalitzat correctament però el fitxer firmat o no s'ha definit o no existeix";
					firmaStatus.setErrorMsg(msg);
					MissatgesHelper.error(
							request,
							getMessage(
									request, 
									"document.controller.firma.passarela.final.ok.nofile"));
				} else {
					System.out.println(">>> fitxer firmat: " + firmaStatus.getSignedData().getAbsolutePath());
					MissatgesHelper.success(
							request,
							getMessage(
									request, 
									"document.controller.firma.passarela.final.ok"));
				}
			} else {
				MissatgesHelper.error(
						request,
						getMessage(
								request, 
								"document.controller.firma.passarela.final.ok.statuserr"));
			}
			break;
		case StatusSignaturesSet.STATUS_FINAL_ERROR:
			MissatgesHelper.error(
					request,
					getMessage(
							request, 
							"document.controller.firma.passarela.final.error",
							new Object[] {status.getErrorMsg()}));
			break;
		case StatusSignaturesSet.STATUS_CANCELLED:
			MissatgesHelper.warning(
					request,
					getMessage(
							request, 
							"document.controller.firma.passarela.final.cancel"));
			break;
		default:
			MissatgesHelper.warning(
					request,
					getMessage(
							request, 
							"document.controller.firma.passarela.final.desconegut"));
		}
		return getModalControllerReturnValueSuccess(
				request, 
				"redirect:/contenidor/" + contenidorId,
				null);
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(
	    		Date.class,
	    		new CustomDateEditor(
	    				new SimpleDateFormat("dd/MM/yyyy"),
	    				true));
	}



	private void emplenarModelPortafirmes(
			HttpServletRequest request,
			Long documentId,
			int versio,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"document",
				documentService.findById(
						entitatActual.getId(),
						documentId));
		model.addAttribute(
				"documentVersio",
				documentService.findVersio(
						entitatActual.getId(),
						documentId,
						versio));
	}
	private void emplenarModelPassarela(
			HttpServletRequest request,
			Long documentId,
			int versio,
			Model model) {
		emplenarModelPortafirmes(
				request,
				documentId,
				versio,
				model);
	}
	private String emplenarModelFirmaApplet(
			HttpServletRequest request,
			Long documentId,
			int versio,
			Model model) {
		emplenarModelPortafirmes(
				request,
				documentId,
				versio,
				model);
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		String identificador = documentService.generarIdentificadorFirmaClient(
				entitatActual.getId(),
				documentId,
				versio);
		model.addAttribute(
				"identificadorFirmaApplet",
				identificador);
		return identificador;
	}
	
}
