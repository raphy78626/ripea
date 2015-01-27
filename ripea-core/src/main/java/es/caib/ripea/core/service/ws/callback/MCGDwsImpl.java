/**
 * 
 */
package es.caib.ripea.core.service.ws.callback;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.IntegracioAccioTipusEnumDto;
import es.caib.ripea.core.api.service.DocumentService;
import es.caib.ripea.core.helper.IntegracioHelper;

/**
 * Implementació dels mètodes per al servei de callback del portafirmes.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
@WebService(
		name = "MCGDws",
		serviceName = "MCGDwsService",
		portName = "MCGDwsServicePort",
		endpointInterface = "es.caib.ripea.core.service.ws.callback.MCGDws",
		targetNamespace = "http://www.indra.es/portafirmasmcgdws/mcgdws")
public class MCGDwsImpl implements MCGDws {

	@Resource
	private DocumentService documentService;
	@Resource
	private IntegracioHelper integracioHelper;


	@Override
	public CallbackResponse callback(
			CallbackRequest callbackRequest) {
		int documentId = callbackRequest.getApplication().getDocument().getId();
		Integer estat = callbackRequest.getApplication().getDocument().getAttributes().getState();
		logger.debug("Rebuda petició al callback de portafirmes (" +
				"documentId:" + documentId + ", " +
				"estat:" + estat + ")");
		String accioDescripcio = "Petició rebuda al callback";
		Map<String, String> accioParams = new HashMap<String, String>();
		accioParams.put("documentId", new Integer(documentId).toString());
		accioParams.put("estat", estat.toString());
		CallbackResponse callbackResponse = new CallbackResponse();
		try {
			Exception ex = documentService.portafirmesCallback(
					documentId,
					estat);
			if (ex == null) {
				integracioHelper.addAccioOk(
						IntegracioHelper.INTCODI_CALLBACK,
						accioDescripcio,
						accioParams,
						IntegracioAccioTipusEnumDto.RECEPCIO);
				callbackResponse.setReturn(1);
			} else {
				logger.error(
						"Error al processar petició rebuda al callback de portafirmes (" +
						"documentId:" + documentId + ", " +
						"estat:" + estat + ")",
						ex);
				String errorDescripcio = "Error al processar petició rebuda al callback de portafirmes";
				integracioHelper.addAccioError(
						IntegracioHelper.INTCODI_CALLBACK,
						accioDescripcio,
						accioParams,
						IntegracioAccioTipusEnumDto.RECEPCIO,
						errorDescripcio,
						ex);
				callbackResponse.setReturn(-1);
			}
		} catch (Exception ex) {
			logger.error(
					"Error al processar petició rebuda al callback de portafirmes (" +
					"documentId:" + documentId + ", " +
					"estat:" + estat + ")",
					ex);
			String errorDescripcio = "Error al processar petició rebuda al callback de portafirmes";
			integracioHelper.addAccioError(
					IntegracioHelper.INTCODI_CALLBACK,
					accioDescripcio,
					accioParams,
					IntegracioAccioTipusEnumDto.RECEPCIO,
					errorDescripcio,
					ex);
			callbackResponse.setReturn(-1);
		}
		return callbackResponse;
	}

	private static final Logger logger = LoggerFactory.getLogger(MCGDwsImpl.class);
			
}
