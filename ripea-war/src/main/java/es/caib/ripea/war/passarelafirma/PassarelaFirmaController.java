package es.caib.ripea.war.passarelafirma;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.fundaciobit.plugins.signatureweb.api.IUploadedFile;
import org.fundaciobit.plugins.signatureweb.api.StatusSignaturesSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.view.RedirectView;

import es.caib.ripea.war.helper.MissatgesHelper;

/**
 * Controller per a les accions de la passarel·la de firma.
 *
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
@RequestMapping(value = PassarelaFirmaHelper.CONTEXTWEB)
public class PassarelaFirmaController {

	public static final boolean stepSelectionWhenOnlyOnePlugin = false;

	@Autowired
	private PassarelaFirmaHelper passarelaFirmaHelper;



	@RequestMapping(value = "/selectsignmodule/{signaturesSetId}")
	public String selectSignModules(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("signaturesSetId") String signaturesSetId,
			Model model) throws Exception {
		List<PassarelaFirmaPlugin> pluginsFiltered = passarelaFirmaHelper.getAllPlugins(
				request,
				signaturesSetId);
		// Si només hi ha un mòdul de firma llavors anar a firmar directament
		if (stepSelectionWhenOnlyOnePlugin) {
			if (pluginsFiltered.size() == 1) {
				PassarelaFirmaPlugin modul = pluginsFiltered.get(0);
				long pluginID = modul.getPluginId();
				return "redirect:" +
						PassarelaFirmaHelper.CONTEXTWEB + "/showsignaturemodule/" +
						pluginID + "/" + signaturesSetId;
			}
		}
		// Si cap modul compleix llavors mostrar missatge
		if (pluginsFiltered.size() == 0) {
			String msg = "No existeix cap mòdul de firma que passi els filtres";
			PassarelaFirmaSignaturesSet pfss = passarelaFirmaHelper.getSignaturesSet(
					request,
					signaturesSetId);
			if (pfss == null) {
				MissatgesHelper.error(request, msg);
			} else {
				StatusSignaturesSet sss = pfss.getStatusSignaturesSet();
				sss.setErrorMsg(msg);
				sss.setErrorException(null);
				sss.setStatus(StatusSignaturesSet.STATUS_FINAL_ERROR);
			}
			return "redirect:" + pfss.getUrlFinal();
		}
		model.addAttribute("signaturesSetId", signaturesSetId);
		model.addAttribute("plugins", pluginsFiltered);
		return "passarelaFirmaSeleccio";
	}

	@RequestMapping(value = "/showsignaturemodule/{pluginId}/{signaturesSetID}")
	public RedirectView showSignatureModule(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("pluginId") Long pluginId,
			@PathVariable("signaturesSetID") String signaturesSetID) throws Exception {
		// Assignar plugin Elegit
		PassarelaFirmaSignaturesSet pfss = passarelaFirmaHelper.getSignaturesSet(
				request,
				signaturesSetID);
		pfss.setPluginId(pluginId);
		String urlToPluginWebPage = passarelaFirmaHelper.signDocuments(
				request,
				signaturesSetID);
		return new RedirectView(urlToPluginWebPage, false);
	}

	private static final String REQUEST_PLUGIN_MAPPING = "/requestPlugin/{signaturesSetID}/{signatureIndex}/**";
	@RequestMapping(value = REQUEST_PLUGIN_MAPPING)
	public void requestPlugin(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable String signaturesSetID,
			@PathVariable int signatureIndex) throws Exception {
		Map<String, IUploadedFile> uploadedFiles = getMultipartFiles(request);
		String servletPath = request.getServletPath();
		int indexBarra = StringUtils.ordinalIndexOf(
				servletPath,
				"/",
				StringUtils.countMatches(
						PassarelaFirmaHelper.CONTEXTWEB + REQUEST_PLUGIN_MAPPING,
						"/"));
		String query = servletPath.substring(indexBarra + 1);
		passarelaFirmaHelper.requestPlugin(
				request,
				response,
				signaturesSetID,
				signatureIndex,
				query,
				uploadedFiles);
	}

	@RequestMapping(value = "/final/{signaturesSetId}")
	public String finalProcesDeFirma(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("signaturesSetId") String signaturesSetId) throws Exception {
		PassarelaFirmaSignaturesSet pss = passarelaFirmaHelper.finalitzarProcesDeFirma(
				request,
				signaturesSetId);
		return "redirect:" + pss.getUrlFinal() + "?signaturesSetId=" + signaturesSetId;
	}

	/*@RequestMapping(value = "/error")
	public ModelAndView errorProcesDeFirma(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("URL_FINAL") String urlFinal) throws Exception {
		ModelAndView mav = new ModelAndView(PassarelaFirmaHelper.VISTA_FINAL);
		mav.addObject("URL_FINAL", urlFinal);
		return mav;
	}*/



	private Map<String, IUploadedFile> getMultipartFiles(HttpServletRequest request) {
		Map<String, IUploadedFile> uploadedFiles;
		uploadedFiles = new HashMap<String, IUploadedFile>();
		if (request instanceof MultipartHttpServletRequest) {
			try {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				Map<String, MultipartFile> files = multipartRequest.getFileMap();
				if (log.isDebugEnabled()) {
					log.debug("getMultipartFiles::multipartRequest.getFileMap() = " + files);
					log.debug("getMultipartFiles::multipartRequest.getFileMap().size() = " + files.size());
				}
				for (String name : files.keySet()) {
					MultipartFile mpf = files.get(name);
					if (log.isDebugEnabled()) {
						log.debug("getMultipartFiles::KEY[" + name + "] = len:" + mpf.getSize());
					}
					if (mpf.isEmpty() || mpf.getSize() == 0) {
						continue;
					}
					uploadedFiles.put(name, new SignatureWebUploadedFile(mpf));
				}
			} catch (Throwable e) {
				log.error("Error processant fitxers pujats en la petició web: " + e.getMessage(), e);
			}
		}
		return uploadedFiles;
	}

	private static class SignatureWebUploadedFile implements IUploadedFile {
		protected final MultipartFile mpf;
		/**
		 * @param mpf
		 */
		public SignatureWebUploadedFile(MultipartFile mpf) {
			super();
			this.mpf = mpf;
		}
		@Override
		public String getFormFieldName() {
			return mpf.getName();
		}
		@Override
		public String getOriginalFilename() {
			return mpf.getOriginalFilename();
		}
		@Override
		public String getContentType() {
			return mpf.getContentType();
		}
		@Override
		public boolean isEmpty() {
			return mpf.isEmpty();
		}
		@Override
		public long getSize() {
			return mpf.getSize();
		}
		@Override
		public byte[] getBytes() throws IOException {
			return mpf.getBytes();
		}
		@Override
		public InputStream getInputStream() throws IOException {
			return mpf.getInputStream();
		}
		@Override
		public void transferTo(File dest) throws IOException, IllegalStateException {
			mpf.transferTo(dest);
		}
	}

	protected static Logger log = LoggerFactory.getLogger(PassarelaFirmaController.class);

}
