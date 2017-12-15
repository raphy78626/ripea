package es.caib.ripea.war.passarelafirma;

import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.fundaciobit.plugins.signature.api.StatusSignaturesSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

	public static final boolean stepSelectionWhenOnlyOnePlugin = true;

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
				log.debug("Seleccionant automàticament plugin de firma (" +
						"signaturesSetId = " + signaturesSetId + ")");
				return "redirect:" +
						PassarelaFirmaHelper.CONTEXTWEB + "/showsignaturemodule/" +
						pluginID + "/" + signaturesSetId;
			}
		}
		// Si cap modul compleix llavors mostrar missatge
		if (pluginsFiltered.size() == 0) {
			String msg = "No existeix cap mòdul de firma que passi els filtres";
			PassarelaFirmaConfig pfss = passarelaFirmaHelper.getSignaturesSet(
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
			log.debug("Cap plugin de firma disponible (" +
					"signaturesSetId = " + signaturesSetId + ")");
			return "redirect:" + pfss.getUrlFinal();
		}
		model.addAttribute("signaturesSetId", signaturesSetId);
		model.addAttribute("plugins", pluginsFiltered);
		log.debug("Pantalla de selecció del plugin de firma (" +
				"signaturesSetId = " + signaturesSetId + ")");
		return "passarelaFirmaSeleccio";
	}

	@RequestMapping(value = "/showsignaturemodule/{pluginId}/{signaturesSetId}")
	public RedirectView showSignatureModule(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("pluginId") Long pluginId,
			@PathVariable("signaturesSetId") String signaturesSetId) throws Exception {
		PassarelaFirmaConfig pfss = passarelaFirmaHelper.getSignaturesSet(
				request,
				signaturesSetId);
		pfss.setPluginId(pluginId);
		String pluginUrl = passarelaFirmaHelper.getPluginUrl(
				request,
				signaturesSetId);
		log.debug("Mostrant mòdul de signatura (" +
				"pluginId = " + pluginId + ", " +
				"signaturesSetId = " + signaturesSetId + ", " +
				"pluginUrl = " + pluginUrl + ")");
		return new RedirectView(pluginUrl, false);
	}

	private static final String REQUEST_PLUGIN_MAPPING = "/requestPlugin/{signaturesSetId}/{signatureIndex}/**";
	@RequestMapping(value = REQUEST_PLUGIN_MAPPING)
	public void requestPlugin(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable String signaturesSetId,
			@PathVariable int signatureIndex) throws Exception {
		String servletPath = request.getServletPath();
		int indexBarra = StringUtils.ordinalIndexOf(
				servletPath,
				"/",
				StringUtils.countMatches(
						PassarelaFirmaHelper.CONTEXTWEB + REQUEST_PLUGIN_MAPPING,
						"/"));
		String query = servletPath.substring(indexBarra + 1);
		log.debug("Gestió de la petició al plugin (" +
				"signaturesSetId = " + signaturesSetId + ", " +
				"signatureIndex = " + signatureIndex + ", " +
				"requestUri = " + request.getRequestURI() + ")");
		passarelaFirmaHelper.requestPlugin(
				request,
				response,
				signaturesSetId,
				signatureIndex,
				query);
	}

	@RequestMapping(value = "/final/{signaturesSetId}")
	public String finalProcesDeFirma(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("signaturesSetId") String signaturesSetId) throws Exception {
		PassarelaFirmaConfig pss = passarelaFirmaHelper.finalitzarProcesDeFirma(
				request,
				signaturesSetId);
		log.debug("Final del procés de firma (" +
				"signaturesSetId = " + signaturesSetId + ")");
		return "redirect:" + pss.getUrlFinalRipea() + "?signaturesSetId=" + signaturesSetId;
	}



	protected static Logger log = LoggerFactory.getLogger(PassarelaFirmaController.class);

}
