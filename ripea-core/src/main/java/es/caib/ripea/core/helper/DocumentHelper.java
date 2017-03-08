/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.DocumentEstatEnumDto;
import es.caib.ripea.core.api.dto.DocumentNtiEstadoElaboracionEnumDto;
import es.caib.ripea.core.api.dto.DocumentNtiOrigenEnumDto;
import es.caib.ripea.core.api.dto.DocumentNtiTipoDocumentalEnumDto;
import es.caib.ripea.core.api.dto.DocumentTipusEnumDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.exception.SistemaExternException;
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.DocumentVersioEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.MetaDocumentEntity;
import es.caib.ripea.core.repository.DocumentRepository;
import es.caib.ripea.core.repository.DocumentVersioRepository;
import es.caib.ripea.plugin.arxiu.ArxiuDocument;

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
	private DocumentVersioRepository documentVersioRepository;

	@Resource
	private ContingutHelper contingutHelper;
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
			DocumentNtiOrigenEnumDto ntiOrigen,
			DocumentNtiEstadoElaboracionEnumDto ntiEstadoElaboracion,
			DocumentNtiTipoDocumentalEnumDto ntiTipoDocumental,
			ExpedientEntity expedient,
			MetaDocumentEntity metaDocument,
			ContingutEntity pare,
			EntitatEntity entitat,
			String ubicacio,
			FitxerDto fitxer) {
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
		if (DocumentTipusEnumDto.DIGITAL.equals(documentTipus)) {
			int versio = 1;
			DocumentVersioEntity documentVersio = crearVersioAmbFitxerAssociat(
					documentCreat,
					versio,
					fitxer.getNom(),
					fitxer.getContentType(),
					fitxer.getContingut());
			documentVersioRepository.save(documentVersio);
			documentCreat.updateVersioDarrera(documentVersio);
		}
		if (expedient != null) {
			cacheHelper.evictErrorsValidacioPerNode(expedient);
		}
		return documentCreat;
	}

	public DocumentVersioEntity crearVersioAmbFitxerAssociat(
			DocumentEntity document,
			int versio,
			String fitxerNom,
			String fitxerContentType,
			byte[] fitxerContingut) {
		DocumentVersioEntity documentVersio;
		if (pluginHelper.isArxiuPluginActiu()) {
			documentVersio = DocumentVersioEntity.getBuilder(
					document,
					versio,
					fitxerNom,
					fitxerContentType,
					fitxerContingut.length).build();
		} else {
			documentVersio = DocumentVersioEntity.getBuilder(
					document,
					versio,
					fitxerNom,
					fitxerContentType,
					fitxerContingut).build();
		}
		return documentVersio;
	}

	public FitxerDto getFitxerAssociat(
			DocumentEntity document) {
		return getFitxerAssociat(document, null);
	}
	public FitxerDto getFitxerAssociat(
			DocumentEntity document,
			Integer versio) {
		FitxerDto fitxer = null;
		DocumentVersioEntity documentVersio;
		if (versio == null) {
			documentVersio = document.getVersioDarrera();
		} else {
			documentVersio = documentVersioRepository.findByDocumentAndVersio(
					document,
					versio);
		}
		if (document.getArxiuUuid() != null) {
			if (pluginHelper.isArxiuPluginActiu()) {
				ArxiuDocument arxiuDocument = pluginHelper.arxiuDocumentConsultar(
						document,
						true);
				fitxer = new FitxerDto();
				fitxer.setNom(documentVersio.getArxiuNom());
				fitxer.setContentType(arxiuDocument.getContingut().getContentType());
				fitxer.setContingut(arxiuDocument.getContingut().getContingut());
			} else {
				throw new SistemaExternException(
						IntegracioHelper.INTCODI_ARXIU,
						"S'està intentant consultar el contingut d'un document de l'arxiu i el plugin d'accés a l'arxiu no esta configurat");
			}
		} else {
			fitxer = new FitxerDto();
			fitxer.setNom(documentVersio.getArxiuNom());
			fitxer.setContentType(documentVersio.getArxiuContentType());
			fitxer.setContingut(documentVersio.getArxiuContingut());
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

}
