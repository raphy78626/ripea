/**
 * 
 */
package es.caib.ripea.core.service.ws.bustia;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.service.BustiaService;
import es.caib.ripea.core.api.service.ws.BustiaV0WsService;

/**
 * Implementació dels mètodes per al servei de bústia de RIPEA.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
@WebService(
		name = "BustiaV0",
		serviceName = "BustiaV0Service",
		portName = "BustiaV0ServicePort",
		endpointInterface = "es.caib.ripea.core.api.service.ws.BustiaV0WsService",
		targetNamespace = "http://www.caib.es/ripea/ws/v0/bustia")
public class BustiaV0WsServiceImpl implements BustiaV0WsService {

	@Resource
	private BustiaService bustiaService;



	@Override
	public void enviarContingut(
			String entitat,
			String unitatAdministrativa,
			BustiaContingutTipus tipus,
			String referencia) {
		logger.debug("Processant enviament al servei web de bústia (" +
				"entitat:" + entitat + ", " +
				"unitatAdministrativa:" + unitatAdministrativa + ", " +
				"tipus:" + tipus + ", " +
				"referencia:" + referencia + ")");
		if (BustiaContingutTipus.EXPEDIENT.equals(tipus)) {
			throw new ValidationException(
					"Els enviaments de tipus EXPEDIENT encara no estan suportats");
		} else if (BustiaContingutTipus.DOCUMENT.equals(tipus)) {
			throw new ValidationException(
					"Els enviaments de tipus DOCUMENT encara no estan suportats");
		} else if (BustiaContingutTipus.REGISTRE_ENTRADA.equals(tipus)) {
			bustiaService.registreAnotacioCrear(
					entitat,
					referencia);
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(BustiaV0WsServiceImpl.class);

}
