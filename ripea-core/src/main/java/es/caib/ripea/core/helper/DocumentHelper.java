/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.DocumentEstatEnumDto;
import es.caib.ripea.core.api.dto.DocumentNtiEstadoElaboracionEnumDto;
import es.caib.ripea.core.api.dto.NtiOrigenEnumDto;
import es.caib.ripea.core.api.dto.DocumentNtiTipoDocumentalEnumDto;
import es.caib.ripea.core.api.dto.DocumentTipusEnumDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.api.exception.SistemaExternException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.DocumentPortafirmesEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.MetaDocumentEntity;
import es.caib.ripea.core.repository.DocumentRepository;
import es.caib.ripea.plugin.arxiu.ArxiuContingutTipusEnum;
import es.caib.ripea.plugin.arxiu.ArxiuDocument;
import es.caib.ripea.plugin.arxiu.ArxiuDocumentContingut;
import es.caib.ripea.plugin.arxiu.ArxiuDocumentVersio;
import es.caib.ripea.plugin.portafirmes.PortafirmesDocument;
import es.caib.ripea.plugin.portafirmes.PortafirmesPrioritatEnum;

/**
 * Mètodes per a gestionar els arxius associats a un document
 * tenint en compte que hi pot haver configurat (o no) un plugin
 * de gestió documental.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class DocumentHelper {

	@Resource
	private DocumentRepository documentRepository;

	@Resource
	private ContingutHelper contingutHelper;
	@Resource
	private ContingutLogHelper contingutLogHelper;
	@Resource
	private PluginHelper pluginHelper;
	@Resource
	private CacheHelper cacheHelper;



	public DocumentEntity crearNouDocument(
			DocumentTipusEnumDto documentTipus,
			String nom,
			Date data,
			Date dataCaptura,
			String ntiOrgano,
			NtiOrigenEnumDto ntiOrigen,
			DocumentNtiEstadoElaboracionEnumDto ntiEstadoElaboracion,
			DocumentNtiTipoDocumentalEnumDto ntiTipoDocumental,
			ExpedientEntity expedient,
			MetaDocumentEntity metaDocument,
			ContingutEntity pare,
			EntitatEntity entitat,
			String ubicacio) {
		DocumentEntity documentCrear = DocumentEntity.getBuilder(
				documentTipus,
				DocumentEstatEnumDto.REDACCIO,
				nom,
				data,
				dataCaptura,
				"1.0",
				ntiOrgano,
				ntiOrigen,
				ntiEstadoElaboracion,
				ntiTipoDocumental,
				expedient,
				metaDocument,
				pare,
				entitat).
				ubicacio(ubicacio).
				build();
		DocumentEntity documentCreat = documentRepository.save(documentCrear);
		calcularIdentificadorDocument(
				documentCreat,
				entitat.getUnitatArrel());
		if (expedient != null) {
			cacheHelper.evictErrorsValidacioPerNode(expedient);
		}
		return documentCreat;
	}

	public void actualitzarFitxerDocument(
			DocumentEntity document,
			FitxerDto fitxer) {
		if (pluginHelper.isArxiuPluginActiu() && pluginHelper.arxiuPotGestionarDocuments()) {
			document.updateFitxer(
					fitxer.getNom(),
					fitxer.getContentType(),
					null);
		} else {
			document.updateFitxer(
					fitxer.getNom(),
					fitxer.getContentType(),
					fitxer.getContingut());
		}
	}

	public FitxerDto getFitxerAssociat(
			DocumentEntity document) {
		return getFitxerAssociat(document, null);
	}
	public FitxerDto getFitxerAssociat(
			DocumentEntity document,
			String versio) {
		FitxerDto fitxer = null;
		if (document.getArxiuUuid() != null) {
			if (pluginHelper.isArxiuPluginActiu()) {
				ArxiuDocument arxiuDocument = pluginHelper.arxiuDocumentConsultar(
						document,
						null,
						versio,
						true);
				fitxer = new FitxerDto();
				fitxer.setContentType(document.getFitxerContentType());
				fitxer.setNom(document.getFitxerNom());
				byte[] contingut = null;
				if (arxiuDocument.getContinguts() != null) {
					ArxiuContingutTipusEnum arxiuTipus = ArxiuContingutTipusEnum.CONTINGUT;
					if (versio == null && DocumentEstatEnumDto.CUSTODIAT.equals(document.getEstat())) {
						arxiuTipus = ArxiuContingutTipusEnum.FIRMA;
					}
					for (ArxiuDocumentContingut arxiuContingut: arxiuDocument.getContinguts()) {
						if (arxiuTipus.equals(arxiuContingut.getTipus())) {
							contingut = arxiuContingut.getContingut();
							break;
						}
					}
				}
				if (contingut == null) {
					throw new ValidationException(
							"No s'ha trobat cap contingut pel document de l'arxiu(" +
							"uuid=" + document.getArxiuUuid() + ")");
				}
				fitxer.setContingut(contingut);
			} else {
				throw new SistemaExternException(
						IntegracioHelper.INTCODI_ARXIU,
						"S'està intentant obtenir l'arxiu associat a un document pujat a l'arxiu i el plugin d'arxiu no està activat");
			}
		} else {
			fitxer = new FitxerDto();
			fitxer.setNom(document.getFitxerNom());
			fitxer.setContentType(document.getFitxerContentType());
			fitxer.setContingut(document.getFitxerContingut());
		}
		if (versio == null && DocumentEstatEnumDto.CUSTODIAT.equals(document.getEstat())) {
			fitxer.setNom(
					pluginHelper.conversioConvertirPdfArxiuNom(
							document.getFitxerNom()));
		}
		return fitxer;
	}

	public void calcularIdentificadorDocument(
			DocumentEntity document,
			String organCodi) {
		int any = Calendar.getInstance().get(Calendar.YEAR);
		String ntiIdentificador = "ES_" + organCodi + "_" + any + "_RIP" + String.format("%027d", document.getId());
		document.updateNtiIdentificador(ntiIdentificador);
	}

	public String getExtensioArxiu(String arxiuNom) {
		int indexPunt = arxiuNom.lastIndexOf(".");
		if (indexPunt != -1 && indexPunt < arxiuNom.length() - 1) {
			return arxiuNom.substring(indexPunt + 1);
		} else {
			return null;
		}
	}



	public SistemaExternException portafirmesEnviar(
			DocumentPortafirmesEntity documentPortafirmes) {
		DocumentEntity document = documentPortafirmes.getDocument();
		if (pluginHelper.portafirmesEnviarDocumentEstampat() && document.getCustodiaCsv() == null) {
			try {
				String csv = pluginHelper.arxiuDocumentGenerarCsv(document);
				document.updateInformacioCustodia(
						document.getCustodiaData(),
						document.getCustodiaId(),
						csv);
				contingutLogHelper.log(
						documentPortafirmes.getDocument(),
						LogTipusEnumDto.ARXIU_CSV,
						csv,
						null,
						false,
						false);
			} catch (SistemaExternException ex) {
				Throwable rootCause = ExceptionUtils.getRootCause(ex);
				if (rootCause == null) rootCause = ex;
				documentPortafirmes.updateEnviament(
						true,
						true,
						ExceptionUtils.getStackTrace(rootCause),
						null);
				return ex;
			}
		}
		try {
			String portafirmesId = pluginHelper.portafirmesUpload(
					document,
					documentPortafirmes.getAssumpte(),
					PortafirmesPrioritatEnum.valueOf(documentPortafirmes.getPrioritat().name()),
					documentPortafirmes.getDataCaducitat(),
					documentPortafirmes.getDocumentTipus(),
					documentPortafirmes.getResponsables(),
					documentPortafirmes.getFluxTipus(),
					documentPortafirmes.getFluxId(),
					null);
			documentPortafirmes.updateEnviament(
					true,
					false,
					null,
					portafirmesId);
			return null;
		} catch (SistemaExternException ex) {
			Throwable rootCause = ExceptionUtils.getRootCause(ex);
			if (rootCause == null) rootCause = ex;
			documentPortafirmes.updateEnviament(
					true,
					true,
					ExceptionUtils.getStackTrace(rootCause),
					null);
			return ex;
		}
	}

	public boolean portafirmesProcessarFirma(
			DocumentPortafirmesEntity documentPortafirmes) {
		DocumentEntity document = documentPortafirmes.getDocument();
		document.updateEstat(
				DocumentEstatEnumDto.FIRMAT);
		PortafirmesDocument portafirmesDocument = null;
		try {
			portafirmesDocument = pluginHelper.portafirmesDownload(
					documentPortafirmes);
		} catch (SistemaExternException ex) {
			Throwable rootCause = ExceptionUtils.getRootCause(ex);
			if (rootCause == null) rootCause = ex;
			documentPortafirmes.updateProcessament(
					true,
					true,
					ExceptionUtils.getStackTrace(rootCause),
					false);
			return false;
		}
		if (portafirmesDocument.isCustodiat()) {
			// El document ja ha estat custodiat pel portafirmes
			document.updateInformacioCustodia(
					new Date(),
					portafirmesDocument.getCustodiaId(),
					portafirmesDocument.getCustodiaUrl());
			documentPortafirmes.updateProcessament(
					true,
					false,
					null,
					false);
			return true;
		} else {
			// Envia el document a custòdia
			try {
				FitxerDto fitxer = new FitxerDto();
				fitxer.setNom(portafirmesDocument.getArxiuNom());
				fitxer.setContingut(portafirmesDocument.getArxiuContingut());
				fitxer.setContentType("application/pdf");
				documentPortafirmes.updateProcessament(
						true,
						false,
						null,
						false);
				String custodiaDocumentId = pluginHelper.arxiuDocumentGuardarPdfFirmat(
						document,
						fitxer,
						document.getMetaDocument().getPortafirmesCustodiaTipus());
				document.updateInformacioCustodia(
						new Date(),
						custodiaDocumentId,
						document.getCustodiaCsv());
				actualitzarVersionsDocument(document);
				contingutLogHelper.log(
						documentPortafirmes.getDocument(),
						LogTipusEnumDto.ARXIU_CUSTODIAT,
						custodiaDocumentId,
						null,
						false,
						false);
				return true;
			} catch (SistemaExternException ex) {
				Throwable rootCause = ExceptionUtils.getRootCause(ex);
				if (rootCause == null) rootCause = ex;
				documentPortafirmes.updateProcessament(
						true,
						true,
						ExceptionUtils.getStackTrace(rootCause),
						false);
				return false;
			}
		}
	}

	public void actualitzarVersionsDocument(
			DocumentEntity document) {
		if (pluginHelper.arxiuSuportaVersionsDocuments()) {
			try {
				List<ArxiuDocumentVersio> versions = pluginHelper.arxiuDocumentObtenirVersions(
						document);
				if (versions != null) {
					ArxiuDocumentVersio darreraVersio = null;
					Date versioData = null;
					for (ArxiuDocumentVersio versio: versions) {
						if (versioData == null || versio.getData().after(versioData)) {
							versioData = versio.getData();
							darreraVersio = versio;
						}
					}
					document.updateVersio(
							darreraVersio.getId(),
							versions.size());
				}
			} catch (Exception ex) {
				logger.error(
						"Error al actualitzar les versions del document (" + 
						"entitatId=" + document.getEntitat().getId() + ", " +
						"documentId=" + document.getId() + ", " +
						"documentTitol=" + document.getNom() + ")",
						ex);
			}
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(DocumentHelper.class);

}
