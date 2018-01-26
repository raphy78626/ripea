/**
 * 
 */
package es.caib.ripea.core.helper;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.AlertaDto;
import es.caib.ripea.core.api.service.AlertaService;

/**
 * Mètodes comuns per a gestionar les alertes.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class AlertaHelper {
	
	@Resource
	private AlertaService alertaService;
	
	
	public void crearAlerta(
			Long expedientId,
			String text,
			Exception e) {
		AlertaDto alerta = new AlertaDto();
		alerta.setText(text);
		if(e != null) {
			alerta.setError(ExceptionUtils.getStackTrace(e).substring(0, 2048));
		}
		alerta.setLlegida(false);
		alerta.setContingutId(expedientId);
		alertaService.create(alerta);
	}

}
