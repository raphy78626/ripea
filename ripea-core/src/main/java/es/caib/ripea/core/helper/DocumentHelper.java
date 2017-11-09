/**
 * 
 */
package es.caib.ripea.core.helper;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sun.jersey.core.util.Base64;

import es.caib.plugins.arxiu.api.ContingutArxiu;
import es.caib.plugins.arxiu.api.Document;
import es.caib.ripea.core.api.dto.DocumentEstatEnumDto;
import es.caib.ripea.core.api.dto.DocumentNtiEstadoElaboracionEnumDto;
import es.caib.ripea.core.api.dto.DocumentNtiTipoDocumentalEnumDto;
import es.caib.ripea.core.api.dto.DocumentTipusEnumDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.api.dto.NtiOrigenEnumDto;
import es.caib.ripea.core.api.exception.SistemaExternException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.DocumentPortafirmesEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.MetaDocumentEntity;
import es.caib.ripea.core.repository.DocumentRepository;
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
		if (pluginHelper.isArxiuPluginActiu()) {
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
				Document arxiuDocument = pluginHelper.arxiuDocumentConsultar(
						document,
						null,
						versio,
						true);
				fitxer = new FitxerDto();
				fitxer.setContentType(document.getFitxerContentType());
				fitxer.setNom(document.getFitxerNom());
				byte[] contingut = null;
				if (arxiuDocument.getContingut() != null) {
					contingut = arxiuDocument.getContingut().getContingut();
				} else {
					throw new ValidationException(
							"El document no no te cap contingut associat a dins l'arxiu (" +
							"documentId=" + document.getId() + ", " +
							"arxiuUuid=" + document.getArxiuUuid() + ")");
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
		UUID uuid = UUID.randomUUID();
		ByteBuffer bb = ByteBuffer.wrap(new byte[20]);
		bb.putLong(uuid.getMostSignificantBits());
		bb.putLong(uuid.getLeastSignificantBits());
		Random rand = new Random(System.currentTimeMillis());
		bb.putInt(rand.nextInt());
		String identificador = "ES_" + organCodi + "_" + any + "_" +  new String(Base64.encode(bb.array()));
		document.updateNtiIdentificador(identificador);
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
						fitxer);
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
				List<ContingutArxiu> versions = pluginHelper.arxiuDocumentObtenirVersions(
						document);
				if (versions != null) {
					ContingutArxiu darreraVersio = null;
					Float versioNum = null;
					for (ContingutArxiu versio: versions) {
						if (versioNum == null || new Float(versio.getVersio()).floatValue() > versioNum.floatValue()) {
							versioNum = new Float(versio.getVersio());
							darreraVersio = versio;
						}
					}
					document.updateVersio(
							darreraVersio.getVersio(),
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
