/**
 * 
 */
package es.caib.ripea.core.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import es.caib.ripea.core.api.service.ExecucioMassivaService;
import es.caib.ripea.core.api.service.SegonPlaService;
import es.caib.ripea.core.helper.PropertiesHelper;
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
					ExecucioMassivaExpedient ome = execucioMassivaExpedientRepository.findOne(cmasiu_id);
					if (ome == null)
						throw new NoTrobatException(ExecucioMassivaExpedient.class, cmasiu_id);
					ultimaExecucioMassiva = ome.getExecucioMassiva().getId();
					execucioMassivaService.actualitzaUltimaOperacio(ome.getId());
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

	private static final Logger logger = LoggerFactory.getLogger(SegonPlaServiceImpl.class);

}
