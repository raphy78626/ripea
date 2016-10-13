/**
 * 
 */
package es.caib.ripea.core.helper;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.DocumentVersioEntity;
import es.caib.ripea.core.repository.DocumentRepository;

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
	private PluginHelper pluginHelper;



	public DocumentVersioEntity crearVersioAmbFitxerAssociat(
			DocumentEntity document,
			int versio,
			String arxiuNom,
			String arxiuContentType,
			byte[] arxiuContingut) {
		DocumentVersioEntity documentVersio;
		if (pluginHelper.isGestioDocumentalPluginActiu()) {
			String arxiuGesdocId = pluginHelper.gestioDocumentalCreate(
					arxiuNom,
					arxiuContingut);
			documentVersio = DocumentVersioEntity.getBuilder(
					document,
					versio,
					arxiuNom,
					arxiuContentType,
					arxiuContingut.length,
					arxiuGesdocId).build();
		} else {
			documentVersio = DocumentVersioEntity.getBuilder(
					document,
					versio,
					arxiuNom,
					arxiuContentType,
					arxiuContingut).build();
		}
		return documentVersio;
	}

	public void deleteFitxerAssociatSiNhiHa(
			DocumentVersioEntity documentVersio) {
		if (documentVersio.getArxiuGesdocId() != null) {
			pluginHelper.gestioDocumentalDelete(
					documentVersio.getArxiuGesdocId());
		}
	}

	public FitxerDto getFitxerAssociat(
			DocumentVersioEntity documentVersio) {
		FitxerDto fitxer;
		if (documentVersio.getArxiuGesdocId() != null) {
			fitxer = pluginHelper.gestioDocumentalGet(
					documentVersio.getArxiuGesdocId());
		} else {
			fitxer = new FitxerDto();
			fitxer.setNom(documentVersio.getArxiuNom());
			fitxer.setContingut(documentVersio.getArxiuContingut());
		}
		fitxer.setContentType(documentVersio.getArxiuContentType());
		return fitxer;
	}

}
