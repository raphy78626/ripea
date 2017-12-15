/**
 * 
 */
package es.caib.ripea.core.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.service.ExecucioMassivaService;
import es.caib.ripea.core.api.service.SegonPlaService;
import es.caib.ripea.core.entity.ExecucioMassivaContingutEntity;
import es.caib.ripea.core.entity.ExecucioMassivaEntity.ExecucioMassivaTipus;
import es.caib.ripea.core.helper.PropertiesHelper;
import es.caib.ripea.core.repository.ExecucioMassivaContingutRepository;
import es.caib.ripea.core.repository.ExecucioMassivaRepository;

/**
 * Implementació dels mètodes per a gestionar documents.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class SegonPlaServiceImpl implements SegonPlaService {
	
	@Autowired
	private ExecucioMassivaService execucioMassivaService;
	@Resource
	private ExecucioMassivaRepository execucioMassivaRepository;
	@Resource
	private ExecucioMassivaContingutRepository execucioMassivaContingutRepository;
	
	private static Map<Long, String> errorsMassiva = new HashMap<Long, String>();
	
	@Override
	@Scheduled(fixedDelayString = "${config:es.caib.ripea.segonpla.massives.periode.comprovacio}")
	public void comprovarExecucionsMassives() {
		boolean active = true;
		Long ultimaExecucioMassiva = null;
		
		int timeBetweenExecutions = 500;
		try {
			timeBetweenExecutions = Integer.parseInt(
					PropertiesHelper.getProperties().getProperty("es.caib.ripea.plugin.arxiu.class"));
		} catch (Exception ex) {}
		
		while (active) {
			try {
				Long cmasiu_id = execucioMassivaService.getExecucionsMassivesActiva(ultimaExecucioMassiva);
				if (cmasiu_id != null) {
					try {
						execucioMassivaService.executarExecucioMassiva(cmasiu_id);
					}
					catch (Exception e) {
						// recuperem l'error de la aplicació
						String errMsg = getError(cmasiu_id);
						if (errMsg == null || "".equals(errMsg))
							errMsg = e.getMessage();
						execucioMassivaService.generaInformeError(cmasiu_id, errMsg);
					}
					ExecucioMassivaContingutEntity emc = execucioMassivaContingutRepository.findOne(cmasiu_id);
					if (emc == null)
						throw new NotFoundException(cmasiu_id, ExecucioMassivaContingutEntity.class);
					ultimaExecucioMassiva = emc.getExecucioMassiva().getId();
					execucioMassivaService.actualitzaUltimaOperacio(emc.getId());
				} else {
					active = false;
				}
				Thread.sleep(timeBetweenExecutions);
			} catch (Exception e) {
				logger.error("La execució de execucions massives ha estat interromput");
				active = false;
			}
		}
	}
	
	public static void saveError(Long execucioMassivaContingutId, Throwable error, ExecucioMassivaTipus tipus) {
		StringWriter out = new StringWriter();
		error.printStackTrace(new PrintWriter(out));
		errorsMassiva.put(execucioMassivaContingutId, out.toString());
	}
	
	private static String getError(Long operacioMassivaId) {
		String error = errorsMassiva.get(operacioMassivaId);
		errorsMassiva.remove(operacioMassivaId);
		return error;
	}

	private static final Logger logger = LoggerFactory.getLogger(SegonPlaServiceImpl.class);

}
