package es.caib.ripea.war.passarelafirma;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.fundaciobit.plugins.signature.api.CommonInfoSignature;
import org.fundaciobit.plugins.signature.api.FileInfoSignature;
import org.fundaciobit.plugins.signatureweb.api.ISignatureWebPlugin;
import org.fundaciobit.plugins.signature.api.ITimeStampGenerator;
import org.fundaciobit.plugins.signature.api.PdfVisibleSignature;
import org.fundaciobit.plugins.signature.api.PolicyInfoSignature;
import org.fundaciobit.plugins.signature.api.SecureVerificationCodeStampInfo;
import org.fundaciobit.plugins.signature.api.SignaturesTableHeader;
import org.fundaciobit.plugins.signature.api.StatusSignaturesSet;
import org.fundaciobit.plugins.utils.PluginsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.service.AplicacioService;

/**
 * Classes s'ajuda per a les accions de la passarel·la de firma.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class PassarelaFirmaHelper {

	public static final String CONTEXTWEB = "/firmapassarela";

	@Autowired
	private AplicacioService aplicacioService;

	private Map<String, PassarelaFirmaSignaturesSet> signaturesSetsMap = new HashMap<String, PassarelaFirmaSignaturesSet>();
	private long lastCheckFirmesCaducades = 0;



	public String iniciarProcesDeFirma(
			HttpServletRequest request,
			FitxerDto fitxerPerFirmar,
			String destinatariNif,
			String motiu,
			String llocFirma,
			String emailFirmant,
			String idiomaCodi,
			String urlFinal,
			boolean navegadorSuportaJava) throws IOException {
		PassarelaFirmaSignaturesSet signaturesSet = getSignaturesSetFirmaPassarela(
				request,
				fitxerPerFirmar,
				destinatariNif,
				motiu,
				llocFirma,
				emailFirmant,
				idiomaCodi,
				urlFinal,
				navegadorSuportaJava);
		final String signaturesSetId = signaturesSet.getSignaturesSetID();
		startSignatureProcess(signaturesSet);
		return CONTEXTWEB + "/selectsignmodule/" + signaturesSetId;
	}

	public List<PassarelaFirmaPlugin> getAllPlugins(
			HttpServletRequest request,
			String signaturesSetId) throws Exception {
		PassarelaFirmaSignaturesSet signaturesSet = getSignaturesSet(request, signaturesSetId);
		List<PassarelaFirmaPlugin> plugins = getAllPluginsFromProperties();
		if (plugins == null || plugins.size() == 0) {
			String msg = "S'ha produit un error llegint els plugins o no se n'han definit.";
			throw new Exception(msg);
		}
		List<PassarelaFirmaPlugin> pluginsFiltered = new ArrayList<PassarelaFirmaPlugin>();
		ISignatureWebPlugin signaturePlugin;
		for (PassarelaFirmaPlugin pluginDeFirma: plugins) {
			// 1.- Es pot instanciar el plugin ?
			signaturePlugin = getInstanceByPluginId(pluginDeFirma.getPluginId());
			if (signaturePlugin == null) {
				throw new Exception("No s'ha pogut instanciar el plugin amb id " + pluginDeFirma.getPluginId());
			}
			// 2.- Passa el filtre ...
			if (signaturePlugin.filter(request, signaturesSet)) {
				pluginsFiltered.add(pluginDeFirma);
			} else {
				log.debug("Exclos plugin [" + pluginDeFirma.getNom() + "]: NO PASSA FILTRE");
			}
		}
		return pluginsFiltered;
	}

	public PassarelaFirmaSignaturesSet finalitzarProcesDeFirma(
			HttpServletRequest request,
			String signaturesSetId) {
		PassarelaFirmaSignaturesSet pss = getSignaturesSet(request, signaturesSetId);
		// Check pss is null
		if (pss == null) {
			String msg = "moduldefirma.caducat: " + signaturesSetId;
			throw new RuntimeException(msg);
		}
		StatusSignaturesSet sss = pss.getStatusSignaturesSet();
		if (sss.getStatus() == StatusSignaturesSet.STATUS_INITIALIZING
				|| sss.getStatus() == StatusSignaturesSet.STATUS_IN_PROGRESS) {
			// Vull presuposar que si i que el mòdul de firma s'ha oblidat
			// d'indicar aquest fet ???
			sss.setStatus(StatusSignaturesSet.STATUS_FINAL_OK);
		}
		return pss;
	}

	public String signDocuments(
			HttpServletRequest request,
			String signaturesSetId) throws Exception {
		PassarelaFirmaSignaturesSet signaturesSet = getSignaturesSet(request, signaturesSetId);
		Long pluginId = signaturesSet.getPluginId();
		// El plugin existeix?
		ISignatureWebPlugin signaturePlugin;
		signaturePlugin = getInstanceByPluginId(pluginId);
		if (signaturePlugin == null) {
			String msg = "plugin.signatureweb.noexist: " + String.valueOf(pluginId);
			throw new Exception(msg);
		}
		String urlToPluginWebPage;
		urlToPluginWebPage = signaturePlugin.signDocuments(
				request,
				getRequestPluginBasePath(
						getAbsoluteControllerBase(
								request,
								PassarelaFirmaHelper.CONTEXTWEB),
						signaturesSetId,
						-1),
				getRequestPluginBasePath(
						getRelativeControllerBase(
								request,
								PassarelaFirmaHelper.CONTEXTWEB),
						signaturesSetId,
						-1),
				signaturesSet);
		return urlToPluginWebPage;
	}

	public void requestPlugin(
			HttpServletRequest request,
			HttpServletResponse response,
			String signaturesSetId,
			int signatureIndex,
			String query) throws Exception {
		PassarelaFirmaSignaturesSet ss = getSignaturesSet(request, signaturesSetId);
		long pluginId = ss.getPluginId();
		ISignatureWebPlugin signaturePlugin;
		try {
			signaturePlugin = getInstanceByPluginId(pluginId);
		} catch (Exception e) {
			String msg = "plugin.signatureweb.noexist: " + String.valueOf(pluginId);
			throw new Exception(msg);
		}
		if (signaturePlugin == null) {
			String msg = "plugin.signatureweb.noexist: " + String.valueOf(pluginId);
			throw new Exception(msg);
		}
		String absoluteRequestPluginBasePath = getRequestPluginBasePath(
				getAbsoluteControllerBase(
						request,
						PassarelaFirmaHelper.CONTEXTWEB),
				signaturesSetId,
				signatureIndex);
		String relativeRequestPluginBasePath = getRequestPluginBasePath(
				getRelativeControllerBase(
						request,
						PassarelaFirmaHelper.CONTEXTWEB),
				signaturesSetId,
				signatureIndex);
		if ("POST".equals(request.getMethod())) {
			signaturePlugin.requestPOST(
					absoluteRequestPluginBasePath,
					relativeRequestPluginBasePath,
					query,
					signaturesSetId,
					signatureIndex,
					request,
					response);
		} else {
			signaturePlugin.requestGET(
					absoluteRequestPluginBasePath,
					relativeRequestPluginBasePath,
					query,
					signaturesSetId,
					signatureIndex,
					request,
					response);
		}
	}

	/**
	 * Fa neteja
	 * 
	 * @param signaturesSetId
	 * @return
	 */
	public PassarelaFirmaSignaturesSet getSignaturesSet(
			HttpServletRequest request,
			String signaturesSetId) {
		// Fer net peticions caducades SignaturesSet.getExpiryDate()
		// Check si existeix algun proces de firma caducat s'ha d'esborrar
		// Com a mínim cada minut es revisa si hi ha caducats
		Long now = System.currentTimeMillis();
		final long un_minut_en_ms = 60 * 60 * 1000;
		if (now + un_minut_en_ms > lastCheckFirmesCaducades) {
			lastCheckFirmesCaducades = now;
			List<PassarelaFirmaSignaturesSet> keysToDelete = new ArrayList<PassarelaFirmaSignaturesSet>();
			Set<String> ids = signaturesSetsMap.keySet();
			for (String id : ids) {
				PassarelaFirmaSignaturesSet ss = signaturesSetsMap.get(id);
				if (now > ss.getExpiryDate().getTime()) {
					keysToDelete.add(ss);
					SimpleDateFormat sdf = new SimpleDateFormat();
					log.debug("Tancant Signature SET amb id = " + id + " a causa de que està caducat " + "( ARA: "
							+ sdf.format(new Date(now)) + " | CADUCITAT: " + sdf.format(ss.getExpiryDate()) + ")");
				}
			}
			if (keysToDelete.size() != 0) {
				synchronized (signaturesSetsMap) {

					for (PassarelaFirmaSignaturesSet pss : keysToDelete) {
						closeSignaturesSet(request, pss);
					}
				}
			}
		}
		return signaturesSetsMap.get(signaturesSetId);
	}

	// -------------------------------------------------------------------------
	// -------------------------------------------------------------------------
	// ----------------------------- U T I L I T A T S ----------------------
	// -------------------------------------------------------------------------
	// -------------------------------------------------------------------------

	private PassarelaFirmaSignaturesSet getSignaturesSetFirmaPassarela(
			HttpServletRequest request,
			FitxerDto fitxerPerFirmar,
			String destinatariNif,
			String motiu,
			String llocFirma,
			String emailFirmant,
			String idiomaCodi,
			String urlFinal,
			boolean navegadorSuportaJava) throws IOException {
		long signaturaId = generateUniqueSignaturesSetId();
		String signaturesSetId = new Long(signaturaId).toString();
		Calendar caducitat = Calendar.getInstance();
		caducitat.add(Calendar.MINUTE, 40);
		CommonInfoSignature commonInfoSignature;
		final String urlFinalCalculada = getRelativeControllerBase(request, CONTEXTWEB) + "/final/" + signaturesSetId;
		// TODO Veure manual de MiniApplet
		final String filtreCertificats = "filters.1=nonexpired:";
		// TODO Definir politica de Firma (opcional)
		PolicyInfoSignature pis = null;
		commonInfoSignature = new CommonInfoSignature(
				idiomaCodi,
				filtreCertificats,
				request.getUserPrincipal().getName(),
				destinatariNif,
				pis);
		File filePerFirmar = getFitxerAFirmarPath(signaturaId);
		FileUtils.writeByteArrayToFile(
				filePerFirmar,
				fitxerPerFirmar.getContingut());
		FileInfoSignature fis = getFileInfoSignature(
				signaturesSetId,
				filePerFirmar, // File amb el fitxer a firmar
				fitxerPerFirmar.getContentType(), // Tipus mime del fitxer a firmar
				fitxerPerFirmar.getNom(), // Nom del fitxer a firmar
				0, // posició taula firmes: 0, 1, -1 (sense, primera pag., darrera pag.)
				null, // SignaturesTableHeader 
				motiu,
				llocFirma,
				emailFirmant,
				1, // Nombre de firmes (nomes en suporta una)
				idiomaCodi,
				FileInfoSignature.SIGN_TYPE_PADES,
				FileInfoSignature.SIGN_ALGORITHM_SHA1,
				FileInfoSignature.SIGN_MODE_IMPLICIT,
				false, // userRequiresTimeStamp,
				null, // timeStampGenerator,
				null); // svcsi
		PassarelaFirmaSignaturesSet signaturesSet = new PassarelaFirmaSignaturesSet(
				signaturesSetId,
				caducitat.getTime(),
				commonInfoSignature,
				new FileInfoSignature[] {fis},
				urlFinalCalculada, urlFinal);
		return signaturesSet;
	}

	private void startSignatureProcess(PassarelaFirmaSignaturesSet signaturesSet) {
		synchronized (signaturesSetsMap) {
			final String signaturesSetId = signaturesSet.getSignaturesSetID();
			signaturesSetsMap.put(signaturesSetId, signaturesSet);
		}
	}

	private long generateUniqueSignaturesSetId() {
		long id;
		synchronized (PassarelaFirmaHelper.CONTEXTWEB) {
			id = (System.currentTimeMillis() * 1000000L) + System.nanoTime() % 1000000L;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
		}
		return id;
	}

	private static final String AUTOFIRMA = "AUTOFIRMA";
	private static final String autofirmaBasePath;
	static {
		String tempDir = System.getProperty("java.io.tmpdir");
		final File base = new File(tempDir, AUTOFIRMA);
		base.mkdirs();
		autofirmaBasePath = base.getAbsolutePath();
	}
	private File getFitxerAFirmarPath(long id) {
		File f = new File(
				autofirmaBasePath + File.separatorChar + id,
				String.valueOf(id) + "_original");
		f.getParentFile().mkdirs();
		return f;
	}

	private FileInfoSignature getFileInfoSignature(
			String signatureId,
			File fileToSign,
			String mimeType,
			String idname,
			int locationSignTableId,
			SignaturesTableHeader signaturesTableHeader,
			String reason,
			String location,
			String signerEmail,
			int signNumber,
			String languageSign,
			String signType,
			String signAlgorithm,
			int signModeUncheck,
			boolean userRequiresTimeStamp,
			ITimeStampGenerator timeStampGenerator,
			SecureVerificationCodeStampInfo csvStampInfo) {
		final int signMode = ((signModeUncheck == FileInfoSignature.SIGN_MODE_IMPLICIT)
				? FileInfoSignature.SIGN_MODE_IMPLICIT : FileInfoSignature.SIGN_MODE_EXPLICIT);
		PdfVisibleSignature pdfInfoSignature = null;
		if (FileInfoSignature.SIGN_TYPE_PADES.equals(signType)) {
			// PDF Visible
			pdfInfoSignature = new PdfVisibleSignature();
			if (locationSignTableId != FileInfoSignature.SIGNATURESTABLELOCATION_WITHOUT) {
				// No tenim generadors en aquest APP
				pdfInfoSignature.setRubricGenerator(null);
				pdfInfoSignature.setPdfRubricRectangle(null);
			}
		} else if (FileInfoSignature.SIGN_TYPE_CADES.equals(signType)) {
		} else if (FileInfoSignature.SIGN_TYPE_XADES.equals(signType)) {
		} else {
			// TODO Traduir
			throw new RuntimeException("Tipus de firma no suportada: " + signType);
		}
		if (FileInfoSignature.SIGN_ALGORITHM_SHA1.equals(signAlgorithm)
				|| FileInfoSignature.SIGN_ALGORITHM_SHA256.equals(signAlgorithm)
				|| FileInfoSignature.SIGN_ALGORITHM_SHA384.equals(signAlgorithm)
				|| FileInfoSignature.SIGN_ALGORITHM_SHA512.equals(signAlgorithm)) {
			// OK
		} else {
			// TODO Traduir
			throw new RuntimeException("Tipus d'algorisme no suportat " + signAlgorithm);
		}
		FileInfoSignature fis = new FileInfoSignature(
				signatureId,
				fileToSign,
				mimeType,
				idname,
				reason,
				location,
				signerEmail,
				signNumber,
				languageSign,
				signType,
				signAlgorithm,
				signMode,
				locationSignTableId,
				signaturesTableHeader,
				pdfInfoSignature,
				csvStampInfo,
				userRequiresTimeStamp,
				timeStampGenerator);
		return fis;
	}

	private void closeSignaturesSet(HttpServletRequest request, PassarelaFirmaSignaturesSet pss) {
		Long pluginId = pss.getPluginId();
		final String signaturesSetId = pss.getSignaturesSetID();
		if (pluginId == null) {
			// Encara no s'ha asignat plugin al signatureset
		} else {
			ISignatureWebPlugin signaturePlugin = null;
			try {
				signaturePlugin = getInstanceByPluginId(pluginId);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return;
			}
			if (signaturePlugin == null) {
				log.error("plugin.signatureweb.noexist: " + String.valueOf(pluginId));
			}
			try {
				signaturePlugin.closeSignaturesSet(request, signaturesSetId);
			} catch (Exception e) {
				log.error("Error borrant dades d'un SignaturesSet " + signaturesSetId + ": " + e.getMessage(), e);
			}
		}
		signaturesSetsMap.remove(signaturesSetId);
	}

	private List<PassarelaFirmaPlugin> plugins;
	private static final String PROPERTIES_BASE = "es.caib.ripea.plugin.passarelafirma.";
	private List<PassarelaFirmaPlugin> getAllPluginsFromProperties() {
		if (plugins == null) {
			String idsStr = aplicacioService.propertyGet(
					PROPERTIES_BASE + "ids");
			String[] ids = idsStr.split(",");
			plugins = new ArrayList<PassarelaFirmaPlugin>();
			for (String id: ids) {
				String base = PROPERTIES_BASE + id + ".";
				String nom = aplicacioService.propertyGet(base + "nom");
				log.debug("Plugin a carregar NOM[" + base + "nom" + "]: " + nom);
				String classe = aplicacioService.propertyGet(base + "class");
				String descripcioCurta = aplicacioService.propertyGet(base + "desc");
				Map<String, String> pluginProperties = aplicacioService.propertyFindByPrefix(base);
				Properties pluginPropertiesProcessat = new Properties();
				for (String property: pluginProperties.keySet()) {
					if (property.startsWith(base)) {
						String value = aplicacioService.propertyGet(property);
						String nomFinal = property.substring(base.length());
						pluginPropertiesProcessat.put(
								PROPERTIES_BASE + nomFinal,
								value);
					}
				}
				log.debug(" -------------  PLUGIN " + id + "------------------");
				log.debug("nom: " + nom);
				log.debug("descripcioCurta: " + descripcioCurta);
				log.debug("classe: " + classe);
				log.debug("properties: " + pluginPropertiesProcessat);
				plugins.add(
						new PassarelaFirmaPlugin(
								new Long(id),
								nom,
								descripcioCurta,
								classe,
								pluginPropertiesProcessat));
			}
		}
		return plugins;
	}

	private Map<Long, ISignatureWebPlugin> instancesCache = new HashMap<Long, ISignatureWebPlugin>();
	private Map<Long, PassarelaFirmaPlugin> pluginsCache = new HashMap<Long, PassarelaFirmaPlugin>();
	private ISignatureWebPlugin getInstanceByPluginId(
			long pluginId) throws Exception {
		ISignatureWebPlugin instance = instancesCache.get(pluginId);
		if (instance == null) {
			PassarelaFirmaPlugin plugin = getPluginFromCache(pluginId);
			if (plugin == null) {
				plugin = getPluginById(pluginId);
				if (plugin == null) {
					return null;
				}
				addPluginToCache(pluginId, plugin);
			}
			instance = (ISignatureWebPlugin)PluginsManager.instancePluginByClassName(plugin.getClasse(),
					PROPERTIES_BASE,
					plugin.getProperties());
			if (instance == null) {
				throw new Exception("plugin.donotinstantiate: " + plugin.getNom() + " (" + plugin.getClasse() + ")");
			}
			instancesCache.put(pluginId, instance);
		}
		return instance;
	}
	private void addPluginToCache(
			Long pluginId,
			PassarelaFirmaPlugin pluginInstance) {
		synchronized (pluginsCache) {
			pluginsCache.put(pluginId, pluginInstance);
		}
	}
	private PassarelaFirmaPlugin getPluginFromCache(
			Long pluginId) {
		synchronized (pluginsCache) {
			return pluginsCache.get(pluginId);
		}
	}
	private PassarelaFirmaPlugin getPluginById(long pluginId) {
		for (PassarelaFirmaPlugin plugin: getAllPluginsFromProperties()) {
			if (plugin.getPluginId() == pluginId) {
				return plugin;
			}
		}
		return null;
	}

	private String getRequestPluginBasePath(
			String base,
			String signaturesSetId,
			int signatureIndex) {
		String absoluteRequestPluginBasePath = base + "/requestPlugin/" + signaturesSetId + "/" + signatureIndex;
		return absoluteRequestPluginBasePath;
	}
	private String getRelativeControllerBase(
			HttpServletRequest request,
			String webContext) {
		return request.getContextPath() + webContext;
	}
	private String getAbsoluteControllerBase(
			HttpServletRequest request,
			String webContext) {
		return	request.getScheme() + "://" +
				request.getServerName() + ":" +
				request.getServerPort() +
				request.getContextPath() +
				webContext;
	}

	private static Logger log = LoggerFactory.getLogger(PassarelaFirmaHelper.class);

}
