/**
 * 
 */
package es.caib.ripea.core.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.ExecucioMassivaDto;
import es.caib.ripea.core.api.dto.ExecucioMassivaDto.ExecucioMassivaTipusDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.service.ExecucioMassivaService;
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.ExecucioMassivaContingutEntity;
import es.caib.ripea.core.entity.ExecucioMassivaEntity;
import es.caib.ripea.core.entity.ExecucioMassivaEntity.ExecucioMassivaTipus;
import es.caib.ripea.core.repository.ContingutRepository;
import es.caib.ripea.core.repository.ExecucioMassivaRepository;

/**
 * Implementació dels mètodes per a gestionar documents.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class ExecucioMassivaServiceImpl implements ExecucioMassivaService {

	@Resource
	private ContingutRepository contingutRepository;
	@Resource
	private ExecucioMassivaRepository execucioMassivaRepository;

	@Transactional
	@Override
	public void crearExecucioMassiva(ExecucioMassivaDto dto) throws NotFoundException, ValidationException {
		ExecucioMassivaEntity execucioMassiva = null;
		
		Date dataInici;
		if (dto.getDataInici() == null) {
			dataInici = new Date();
		} else {
			dataInici = dto.getDataInici();
		}
		
		if (dto.getTipus() == ExecucioMassivaTipusDto.PORTASIGNATURES) {
			execucioMassiva = ExecucioMassivaEntity.getBuilder(
					ExecucioMassivaTipus.valueOf(dto.getTipus().toString()),
					dataInici,
					dto.getMotiu(), 
					dto.getPrioritat(), 
					dto.getDataCaducitat(), 
					false).build();
		}
		
		int ordre = 0;
		for (Long contingutId: dto.getContingutIds()) {
			ContingutEntity contingut = contingutRepository.findOne(contingutId);
			ExecucioMassivaContingutEntity emc = ExecucioMassivaContingutEntity.getBuilder(
					execucioMassiva, 
					contingut, 
					ordre++).build();
			
			execucioMassiva.addContingut(emc);
		}
		
		execucioMassivaRepository.save(execucioMassiva);
	}
	
	private void enviarPortafirmes(ExecucioMassivaContingutEntity ome) throws Exception {
//		Expedient exp = ome.getExpedient();
//		try {
//			ome.setDataInici(new Date());
//			Object param2 = deserialize(ome.getExecucioMassiva().getParam2());
//			String script = "";
//			if (param2 instanceof Object[]) {
//				script = (String)((Object[])param2)[0];
//			} else {
//				script = (String)param2;
//			}
//			expedientService.procesScriptExec(
//					exp.getId(),
//					exp.getProcessInstanceId(),
//					script);
//			ome.setEstat(ExecucioMassivaEstat.ESTAT_FINALITZAT);
//			ome.setDataFi(new Date());
//			execucioMassivaExpedientRepository.save(ome);
//		} catch (Exception ex) {
//			logger.error("OPERACIO:" + ome.getId() + ". No s'ha pogut executar l'script", ex);
//			throw ex;
//		}
	}

}
