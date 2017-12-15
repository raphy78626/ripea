/**
 * 
 */
package es.caib.ripea.war.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.Validator;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.fundaciobit.plugins.signature.api.FileInfoSignature;
import org.fundaciobit.plugins.signature.api.StatusSignature;
import org.fundaciobit.plugins.signature.api.StatusSignaturesSet;
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

import es.caib.ripea.core.api.dto.DocumentDto;
import es.caib.ripea.core.api.dto.DocumentEnviamentEstatEnumDto;
import es.caib.ripea.core.api.dto.DocumentNotificacioTipusEnumDto;
import es.caib.ripea.core.api.dto.DocumentPublicacioTipusEnumDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.MetaExpedientDto;
import es.caib.ripea.core.api.dto.UsuariDto;
import es.caib.ripea.core.api.service.AplicacioService;
import es.caib.ripea.core.api.service.ContingutService;
import es.caib.ripea.core.api.service.DocumentService;
import es.caib.ripea.core.api.service.ExpedientEnviamentService;
import es.caib.ripea.core.api.service.ExpedientInteressatService;
import es.caib.ripea.war.command.DocumentNotificacioCommand;
import es.caib.ripea.war.command.DocumentNotificacioCommand.Electronica;
import es.caib.ripea.war.command.DocumentPublicacioCommand;
import es.caib.ripea.war.command.PassarelaFirmaEnviarCommand;
import es.caib.ripea.war.command.PortafirmesEnviarCommand;
import es.caib.ripea.war.helper.EnumHelper;
import es.caib.ripea.war.helper.MissatgesHelper;
import es.caib.ripea.war.helper.ModalHelper;
import es.caib.ripea.war.helper.ValidationHelper;
import es.caib.ripea.war.passarelafirma.PassarelaFirmaConfig;
import es.caib.ripea.war.passarelafirma.PassarelaFirmaHelper;

/**
 * Controlador per al manteniment de documents.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping("/document")
public class DocumentController extends BaseUserController {

	@Autowired
	private AplicacioService aplicacioService;
	@Autowired
	private ContingutService contingutService;
	@Autowired
	private DocumentService documentService;
	@Autowired
	private ExpedientEnviamentService expedientEnviamentService;
	@Autowired
	private ExpedientInteressatService expedientInteressatService;

	@Autowired
	private PassarelaFirmaHelper passarelaFirmaHelper;

	@Autowired
	private Validator validator;



	@RequestMapping(value = "/{documentId}/portafirmes/upload", method = RequestMethod.GET)
	public String portafirmesUploadGet(
			HttpServletRequest request,
			@PathVariable Long documentId,
			Model model) {
		
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		DocumentDto document = documentService.findById(
				entitatActual.getId(),
				documentId);
		model.addAttribute("document", document);
		
		PortafirmesEnviarCommand command = new PortafirmesEnviarCommand();
		command.setMotiu(
				getMessage(
						request, 
						"contenidor.document.portafirmes.camp.motiu.default") +
				" [" + document.getExpedientPare().getNom() + "]");
		
		model.addAttribute(command);
		return "portafirmesForm";
	}
	@RequestMapping(value = "/{documentId}/portafirmes/upload", method = RequestMethod.POST)
	public String portafirmesUploadPost(
			HttpServletRequest request,
			@PathVariable Long documentId,
			@Valid PortafirmesEnviarCommand command,
			BindingResult bindingResult,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			emplenarModelPortafirmes(
					request,
					documentId,
					model);
			return "portafirmesForm";
		}
		documentService.portafirmesEnviar(
				entitatActual.getId(),
				documentId,
				command.getMotiu(),
				command.getPrioritat(),
				command.getDataCaducitat());
		return this.getModalControllerReturnValueSuccess(
				request,
				"redirect:../../../contingut/" + documentId,
				"document.controller.portafirmes.upload.ok");
	}

	@RequestMapping(value = "/{documentId}/portafirmes/reintentar", method = RequestMethod.GET)
	public String portafirmesReintentar(
			HttpServletRequest request,
			@PathVariable Long documentId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		documentService.portafirmesReintentar(
				entitatActual.getId(),
				documentId);
		return "redirect:../firma/info";
	}

	@RequestMapping(value = "/{documentId}/portafirmes/cancel", method = RequestMethod.GET)
	public String portafirmesCancel(
			HttpServletRequest request,
			@PathVariable Long documentId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		documentService.portafirmesCancelar(
				entitatActual.getId(),
				documentId);
		return this.getModalControllerReturnValueSuccess(
				request,
				"redirect:../../../contingut/" + documentId,
				"document.controller.portafirmes.cancel.ok");
	}

	@RequestMapping(value = "/{documentId}/portafirmes/info", method = RequestMethod.GET)
	public String portafirmesInfo(
			HttpServletRequest request,
			@PathVariable Long documentId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"portafirmes",
				documentService.portafirmesInfo(
						entitatActual.getId(),
						documentId));
		return "portafirmesInfo";
	}

	@RequestMapping(value = "/{documentId}/custodia/reintentar", method = RequestMethod.GET)
	public String custodiaReintentar(
			HttpServletRequest request,
			@PathVariable Long documentId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		documentService.portafirmesReintentar(
				entitatActual.getId(),
				documentId);
		return this.getAjaxControllerReturnValueSuccess(
				request,
				"redirect:../../../../../../../contingut/" + documentId,
				"document.controller.custodia.reintentar.ok");
	}

	@RequestMapping(value = "/{documentId}/custodia/info", method = RequestMethod.GET)
	public String custodiaInfo(
			HttpServletRequest request,
			@PathVariable Long documentId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		DocumentDto document = documentService.findById(
				entitatActual.getId(),
				documentId);
		return "redirect:" + document.getCustodiaUrl();
	}

	@RequestMapping(value = "/{documentId}/pdf", method = RequestMethod.GET)
	public String convertirPdf(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable Long documentId) throws IOException {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		FitxerDto convertit = documentService.convertirPdfPerFirmaClient(
				entitatActual.getId(),
				documentId);
		writeFileToResponse(
				convertit.getNom(),
				convertit.getContingut(),
				response);
		return null;
	}

	@RequestMapping(value = "/{documentId}/notificar", method = RequestMethod.GET)
	public String notificarGet(
			HttpServletRequest request,
			@PathVariable Long documentId,
			Model model) {
		ExpedientDto expedient = emplenarModelNotificacio(
				request,
				documentId,
				model);
		DocumentNotificacioCommand command = new DocumentNotificacioCommand();
		MetaExpedientDto metaExpedient = expedient.getMetaExpedient();
		command.setSeuAvisTitol(metaExpedient.getNotificacioAvisTitol());
		command.setSeuAvisText(metaExpedient.getNotificacioAvisText());
		command.setSeuAvisTextMobil(metaExpedient.getNotificacioAvisTextSms());
		command.setSeuOficiTitol(metaExpedient.getNotificacioOficiTitol());
		command.setSeuOficiText(metaExpedient.getNotificacioOficiText());
		model.addAttribute(command);
		return "notificacioForm";
	}

	@RequestMapping(value = "/{documentId}/notificar", method = RequestMethod.POST)
	public String notificarPost(
			HttpServletRequest request,
			@PathVariable Long documentId,
			@Validated({DocumentNotificacioCommand.Create.class}) DocumentNotificacioCommand command,
			BindingResult bindingResult,
			Model model) {
		if (DocumentNotificacioTipusEnumDto.ELECTRONICA.equals(command.getTipus())) {
			new ValidationHelper(validator).isValid(
					command,
					bindingResult,
					Electronica.class);
		}
		if (bindingResult.hasErrors()) {
			emplenarModelNotificacio(
					request,
					documentId,
					model);
			return "notificacioForm";
		}
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		expedientEnviamentService.notificacioCreate(
				entitatActual.getId(),
				documentId,
				command.getInteressatId(),
				DocumentNotificacioCommand.asDto(command));
		return this.getModalControllerReturnValueSuccess(
				request,
				"redirect:../../../contingut/" + documentId,
				"document.controller.notificacio.ok");
	}



	@RequestMapping(value = "/{documentId}/publicar", method = RequestMethod.GET)
	public String publicarGet(
			HttpServletRequest request,
			@PathVariable Long documentId,
			Model model) {
		emplenarModelPublicacio(
				request,
				documentId,
				model);
		DocumentPublicacioCommand command = new DocumentPublicacioCommand();
		command.setDocumentId(documentId);
		model.addAttribute(command);
		return "publicacioForm";
	}

	@RequestMapping(value = "/{documentId}/publicar", method = RequestMethod.POST)
	public String publicarPost(
			HttpServletRequest request,
			@PathVariable Long documentId,
			@Validated({DocumentPublicacioCommand.Create.class}) DocumentPublicacioCommand command,
			BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			emplenarModelPublicacio(
					request,
					documentId,
					model);
			return "publicacioForm";
		}
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		expedientEnviamentService.publicacioCreate(
				entitatActual.getId(),
				documentId,
				DocumentPublicacioCommand.asDto(command));
		return this.getModalControllerReturnValueSuccess(
				request,
				"redirect:../../../contingut/" + documentId,
				"document.controller.publicacio.ok");
	}

	@RequestMapping(value = "/{documentId}/firmaPassarela", method = RequestMethod.GET)
	public String firmaPassarelaGet(
			HttpServletRequest request,
			@PathVariable Long documentId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		DocumentDto document = documentService.findById(
				entitatActual.getId(),
				documentId);
		model.addAttribute("document", document);
		PassarelaFirmaEnviarCommand command = new PassarelaFirmaEnviarCommand();
		command.setMotiu(getMessage(
						request, 
						"contenidor.document.portafirmes.camp.motiu.default") +
				" [" + document.getExpedientPare().getNom() + "]");
		model.addAttribute(command);
		return "passarelaFirmaForm";
	}
	@RequestMapping(value = "/{documentId}/firmaPassarela", method = RequestMethod.POST)
	public String firmaPassarelaPost(
			HttpServletRequest request,
			@PathVariable Long documentId,
			@Valid PassarelaFirmaEnviarCommand command,
			BindingResult bindingResult,
			Model model) throws IOException {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		if (bindingResult.hasErrors()) {
			emplenarModelFirmaClient(
					request,
					documentId,
					model);
			return "passarelaFirmaForm";
		}
		if (!command.getFirma().isEmpty()) {
			String identificador = documentService.generarIdentificadorFirmaClient(
					entitatActual.getId(),
					documentId);
			documentService.processarFirmaClient(
					identificador,
					command.getFirma().getOriginalFilename(),
					command.getFirma().getBytes());
			MissatgesHelper.success(
					request,
					getMessage(
							request, 
							"document.controller.firma.passarela.final.ok"));
			return getModalControllerReturnValueSuccess(
					request, 
					"redirect:/contingut/" + documentId,
					null);
		} else {
			FitxerDto fitxerPerFirmar = documentService.convertirPdfPerFirmaClient(
					entitatActual.getId(),
					documentId);
			UsuariDto usuariActual = aplicacioService.getUsuariActual();
			String modalStr = (ModalHelper.isModal(request)) ? "/modal" : "";
			String procesFirmaUrl = passarelaFirmaHelper.iniciarProcesDeFirma(
					request,
					fitxerPerFirmar,
					usuariActual.getNif(),
					command.getMotiu(),
					(command.getLloc() != null) ? command.getLloc() : "RIPEA",
					usuariActual.getEmail(),
					LocaleContextHolder.getLocale().getLanguage(),
					modalStr + "/document/" + documentId + "/firmaPassarelaFinal",
					false);
			return "redirect:" + procesFirmaUrl;
		}
	}

	@RequestMapping(value = "/{documentId}/firmaPassarelaFinal")
	public String firmaPassarelaFinal(
			HttpServletRequest request,
			@PathVariable Long documentId,
			@RequestParam("signaturesSetId") String signaturesSetId,
			Model model) throws IOException {
		PassarelaFirmaConfig signaturesSet = passarelaFirmaHelper.finalitzarProcesDeFirma(
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
					FileInputStream fis = new FileInputStream(firmaStatus.getSignedData());
					EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
					String identificador = documentService.generarIdentificadorFirmaClient(
							entitatActual.getId(),
							documentId);
					documentService.processarFirmaClient(
							identificador,
							firmaStatus.getSignedData().getName(),
							IOUtils.toByteArray(fis));
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
		passarelaFirmaHelper.closeSignaturesSet(
				request,
				signaturesSet);
		boolean ignorarModal = false;
		String ignorarModalIdsProperty = aplicacioService.propertyPluginPassarelaFirmaIgnorarModalIds();
		if (ignorarModalIdsProperty != null && !ignorarModalIdsProperty.isEmpty()) {
			String[] ignorarModalIds = ignorarModalIdsProperty.split(",");
			for (String ignorarModalId: ignorarModalIds) {
				if (StringUtils.isNumeric(ignorarModalId)) {
					if (new Long(ignorarModalId).longValue() == signaturesSet.getPluginId().longValue()) {
						ignorarModal = true;
						break;
					}
				}
			}
		}
		if (ignorarModal) {
			return "redirect:/contingut/" + documentId;
		} else {
			return getModalControllerReturnValueSuccess(
					request, 
					"redirect:/contingut/" + documentId,
					null);
		}
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
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		DocumentDto document = documentService.findById(
				entitatActual.getId(),
				documentId);
		model.addAttribute("document", document);
	}

	private void emplenarModelFirmaClient(
			HttpServletRequest request,
			Long documentId,
			Model model) {
		emplenarModelPortafirmes(
				request,
				documentId,
				model);
	}

	private ExpedientDto emplenarModelNotificacio(
			HttpServletRequest request,
			Long documentId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		DocumentDto document = (DocumentDto)contingutService.findAmbIdUser(
				entitatActual.getId(),
				documentId,
				false,
				false);
		model.addAttribute(
				"document",
				document);
		model.addAttribute(
				"notificacioTipusEnumOptions",
				EnumHelper.getOptionsForEnum(
						DocumentNotificacioTipusEnumDto.class,
						"notificacio.tipus.enum."));
		model.addAttribute(
				"notificacioEstatEnumOptions",
				EnumHelper.getOptionsForEnum(
						DocumentEnviamentEstatEnumDto.class,
						"notificacio.estat.enum.",
						new Enum<?>[] {DocumentEnviamentEstatEnumDto.PUBLICAT}));
		model.addAttribute(
				"interessats",
				expedientInteressatService.findAmbDocumentPerNotificacio(
						entitatActual.getId(),
						documentId));
		List<DocumentDto> annexos = documentService.findAmbExpedientIPermisRead(
				entitatActual.getId(),
				document.getExpedientPare().getId());
		Iterator<DocumentDto> it = annexos.iterator();
		while (it.hasNext()) {
			DocumentDto annex = it.next();
			if (annex.getId().equals(documentId)) {
				it.remove();
				break;
			}
		}
		model.addAttribute("annexos", annexos);
		return document.getExpedientPare();
	}

	private void emplenarModelPublicacio(
			HttpServletRequest request,
			Long documentId,
			Model model) {
		EntitatDto entitatActual = getEntitatActualComprovantPermisos(request);
		model.addAttribute(
				"document",
				contingutService.findAmbIdUser(
						entitatActual.getId(),
						documentId,
						false,
						false));
		model.addAttribute(
				"publicacioTipusEnumOptions",
				EnumHelper.getOptionsForEnum(
						DocumentPublicacioTipusEnumDto.class,
						"publicacio.tipus.enum."));
		model.addAttribute(
				"publicacioEstatEnumOptions",
				EnumHelper.getOptionsForEnum(
						DocumentEnviamentEstatEnumDto.class,
						"publicacio.estat.enum.",
						new Enum<?>[] {
							DocumentEnviamentEstatEnumDto.ENVIAT_ERROR,
							DocumentEnviamentEstatEnumDto.PROCESSAT_OK,
							DocumentEnviamentEstatEnumDto.PROCESSAT_ERROR,
							DocumentEnviamentEstatEnumDto.CANCELAT}));
	}

}
