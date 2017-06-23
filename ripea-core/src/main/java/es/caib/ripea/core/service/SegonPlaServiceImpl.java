/**
 * 
 */
package es.caib.ripea.core.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import es.caib.ripea.core.api.service.SegonPlaService;
import es.caib.ripea.core.entity.ExecucioMassivaEntity;
import es.caib.ripea.core.repository.ExecucioMassivaRepository;

/**
 * Implementació dels mètodes per a gestionar documents.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class SegonPlaServiceImpl implements SegonPlaService {
	
	@Resource
	private ExecucioMassivaRepository execucioMassivaRepository;
	
	@Override
	@Scheduled(fixedDelayString = "${config:es.caib.ripea.segonpla.massives.periode.comprovacio}")
	public void comprovarExecucionsMassives() {
		
		System.out.println("comprovant si hi ha execucions massives pendents");
		
		List<ExecucioMassivaEntity> execucionsMassives = execucioMassivaRepository.findAll();
		
		System.out.println("Hi ha " + execucionsMassives.size());
		
		
		boolean active = true;
		Long ultimaExecucioMassiva = null;
		
		int timeBetweenExecutions = 500;
//		try {
//			timeBetweenExecutions = Integer.parseInt(
//					GlobalProperties.getInstance().getProperty("app.massiu.periode.execucions")); 
//		} catch (Exception ex) {}
//		
//		while (active) {
//			try {
//				Long ome_id = execucioMassivaService.getExecucionsMassivesActiva(ultimaExecucioMassiva);
//				if (ome_id != null) {
//					try {
//						execucioMassivaService.executarExecucioMassiva(ome_id);
//					}
//					catch (Exception e) {
//						// recuperem l'error de la aplicació
//						String errMsg = getError(ome_id);
//						if (errMsg == null || "".equals(errMsg))
//							errMsg = e.getMessage();
//						execucioMassivaService.generaInformeError(ome_id, errMsg);
//					}
//					ExecucioMassivaExpedient ome = execucioMassivaExpedientRepository.findOne(ome_id);
//					if (ome == null)
//						throw new NoTrobatException(ExecucioMassivaExpedient.class, ome_id);
//					ultimaExecucioMassiva = ome.getExecucioMassiva().getId();
//					execucioMassivaService.actualitzaUltimaOperacio(ome.getId());
//				} else {
//					active = false;
//				}
//				Thread.sleep(timeBetweenExecutions);
//			} catch (Exception e) {
//				logger.error("La execució de execucions massives ha estat interromput");
//				active = false;
//			}
//		}
	}

	private static final Logger logger = LoggerFactory.getLogger(SegonPlaServiceImpl.class);

}
