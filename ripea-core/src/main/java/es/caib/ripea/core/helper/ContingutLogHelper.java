/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.ContingutDto;
import es.caib.ripea.core.api.dto.ContingutLogDto;
import es.caib.ripea.core.api.dto.ContingutMovimentDto;
import es.caib.ripea.core.api.dto.LogObjecteTipusEnumDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.api.dto.UsuariDto;
import es.caib.ripea.core.entity.CarpetaEntity;
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.ContingutLogEntity;
import es.caib.ripea.core.entity.ContingutMovimentEntity;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.repository.ContingutLogRepository;
import es.caib.ripea.core.repository.ContingutMovimentRepository;
import es.caib.ripea.core.repository.ContingutRepository;

/**
 * Utilitat per a gestionar el registre d'accions dels contenidors.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class ContingutLogHelper {

	@Resource
	private ContingutRepository contingutRepository;
	@Resource
	private ContingutLogRepository contingutLogRepository;
	@Resource
	private ContingutMovimentRepository contingutMovimentRepository;

	@Resource
	private ContingutHelper contenidorHelper;
	@Resource
	private UsuariHelper usuariHelper;
	@Resource
	private ConversioTipusHelper conversioTipusHelper;



	public ContingutLogEntity log(
			ContingutEntity contingut,
			LogTipusEnumDto tipus,
			ContingutLogEntity contingutLogPare,
			ContingutMovimentEntity contingutMoviment,
			boolean logContenidorPare,
			boolean logExpedientSuperior) {
		return log(
				contingut,
				tipus,
				contingutLogPare,
				contingutMoviment,
				null,
				null,
				null,
				null,
				null,
				logContenidorPare,
				logExpedientSuperior);
	}
	public ContingutLogEntity log(
			ContingutEntity contingut,
			LogTipusEnumDto tipus,
			ContingutLogEntity contingutLogPare,
			ContingutMovimentEntity contingutMoviment,
			String param1,
			String param2,
			boolean logContenidorPare,
			boolean logExpedientSuperior) {
		return log(
				contingut,
				tipus,
				contingutLogPare,
				contingutMoviment,
				null,
				null,
				null,
				param1,
				param2,
				logContenidorPare,
				logExpedientSuperior);
	}
	public ContingutLogEntity log(
			ContingutEntity contingut,
			LogTipusEnumDto tipus,
			ContingutLogEntity contingutLogPare,
			ContingutMovimentEntity contingutMoviment,
			AbstractPersistable<Long> objecte,
			LogObjecteTipusEnumDto objecteTipus,
			LogTipusEnumDto objecteLogTipus,
			String param1,
			String param2,
			boolean logContenidorPare,
			boolean logExpedientSuperior) {
		ContingutLogEntity contenidorLogPareCreat = log(
				contingut,
				tipus,
				contingutLogPare,
				contingutMoviment,
				objecte,
				objecteTipus,
				objecteLogTipus,
				param1,
				param2);
		if (logContenidorPare) {
			if (contingutMoviment == null) {
				if (contingut.getPare() != null) {
					logContingutSuperior(
							contingut,
							tipus,
							contingut.getPare(),
							contenidorLogPareCreat);
				}
			} else {
				if (contingutMoviment.getOrigen() != null) {
					logContingutSuperior(
							contingut,
							tipus,
							contingutMoviment.getOrigen(),
							contenidorLogPareCreat);
				}
				if (contingutMoviment.getDesti() != null) {
					logContingutSuperior(
							contingut,
							tipus,
							contingutMoviment.getDesti(),
							contenidorLogPareCreat);
				}
			}
		}
		if (logExpedientSuperior) {
			if (contingutMoviment == null) {
				logExpedientSuperior(
					contingut,
					tipus,
					contingut,
					contenidorLogPareCreat);
			} else {
				if (contingutMoviment.getOrigen() != null) {
					logExpedientSuperior(
							contingut,
							tipus,
							contingutMoviment.getOrigen(),
							contenidorLogPareCreat);
				}
				if (contingutMoviment.getDesti() != null) {
					logExpedientSuperior(
							contingut,
							tipus,
							contingutMoviment.getDesti(),
							contenidorLogPareCreat);
				}
			}
		}
		return contingutLogPare;
	}

	public List<ContingutLogDto> findLogsContingut(
			ContingutEntity contingut) {
		List<ContingutLogEntity> logs = contingutLogRepository.findByContingutOrderByCreatedDateAsc(
				contingut);
		List<ContingutLogDto> dtos = new ArrayList<ContingutLogDto>();
		for (ContingutLogEntity log: logs) {
			ContingutLogDto dto = new ContingutLogDto();
			dto.setId(log.getId());
			dto.setData(log.getCreatedDate().toDate());
			dto.setTipus(
					LogTipusEnumDto.valueOf(
							log.getTipus().name()));
			dto.setUsuari(
					conversioTipusHelper.convertir(
							log.getCreatedBy(),
							UsuariDto.class));
			//dto.setContenidorMoviment(contenidorMoviment);
			if (log.getObjecteId() != null) {
				dto.setObjecteId(log.getObjecteId());
				dto.setObjecteTipus(
						LogObjecteTipusEnumDto.valueOf(
								log.getObjecteTipus().name()));
				dto.setObjecteLogTipus(
						LogTipusEnumDto.valueOf(
								log.getObjecteLogTipus().name()));
			}
			dto.setParam1(log.getParam1());
			dto.setParam2(log.getParam2());
			if (log.getPare() != null)
				dto.setPareId(log.getPare().getId());
			dtos.add(dto);
		}
		return dtos;
	}

	public List<ContingutMovimentDto> findMovimentsContingut(
			ContingutEntity contingut) {
		List<ContingutMovimentEntity> moviments = contingutMovimentRepository.findByContingutOrderByCreatedDateAsc(
				contingut);
		ContingutDto contingutDto = contenidorHelper.toContingutDto(contingut);
		List<ContingutMovimentDto> dtos = new ArrayList<ContingutMovimentDto>();
		for (ContingutMovimentEntity moviment: moviments) {
			ContingutMovimentDto dto = new ContingutMovimentDto();
			dto.setId(moviment.getId());
			dto.setData(moviment.getCreatedDate().toDate());
			dto.setComentari(moviment.getComentari());
			dto.setContingut(contingutDto);
			dto.setRemitent(
					conversioTipusHelper.convertir(
							moviment.getRemitent(),
							UsuariDto.class));
			if (moviment.getOrigen() != null) {
				dto.setOrigen(
						contenidorHelper.toContingutDto(
								moviment.getOrigen()));
			}
			dto.setDesti(
					contenidorHelper.toContingutDto(
							moviment.getDesti()));
			dtos.add(dto);
		}
		return dtos;
	}



	private void logExpedientSuperior(
			ContingutEntity contingut,
			LogTipusEnumDto tipus,
			ContingutEntity contingutSuperior,
			ContingutLogEntity contenidorLogPare) {
		ExpedientEntity expedientSuperior = contenidorHelper.getExpedientSuperior(
				contingutSuperior,
				true);
		if (expedientSuperior != null) {
			logContingutSuperior(
					contingut,
					tipus,
					expedientSuperior,
					contenidorLogPare);
		}
	}
	private void logContingutSuperior(
			ContingutEntity contingut,
			LogTipusEnumDto tipus,
			ContingutEntity contingutSuperior,
			ContingutLogEntity contenidorLogPare) {
		log(	contingutSuperior,
				LogTipusEnumDto.MODIFICACIO,
				contenidorLogPare,
				null,
				contingut,
				getLogObjecteTipus(contingut),
				tipus,
				null,
				null);
	}

	private ContingutLogEntity log(
			ContingutEntity contingut,
			LogTipusEnumDto tipus,
			ContingutLogEntity logPare,
			ContingutMovimentEntity contingutMoviment,
			AbstractPersistable<Long> objecte,
			LogObjecteTipusEnumDto objecteTipus,
			LogTipusEnumDto objecteLogTipus,
			String param1,
			String param2) {
		logger.debug("Guardant log per contenidor ("
				+ "contingutId=" + contingut.getId() + ", "
				+ "tipus=" + tipus + ", "
				+ "logPareId=" + ((logPare != null) ? logPare.getId() : null) + ", "
				+ "contingutMovimentId=" + ((contingutMoviment != null) ? contingutMoviment.getId() : null) + ")");
		ContingutLogEntity log = ContingutLogEntity.getBuilder(
				tipus,
				contingut,
				logPare,
				contingutMoviment,
				objecte,
				objecteTipus,
				objecteLogTipus).build();
		return contingutLogRepository.save(log);
	}

	private LogObjecteTipusEnumDto getLogObjecteTipus(
			ContingutEntity contingut) {
		if (contingut instanceof ExpedientEntity) {
			return LogObjecteTipusEnumDto.EXPEDIENT;
		} else if (contingut instanceof DocumentEntity) {
			return LogObjecteTipusEnumDto.DOCUMENT;
		} else if (contingut instanceof CarpetaEntity) {
			return LogObjecteTipusEnumDto.CARPETA;
		} else {
			return LogObjecteTipusEnumDto.CONTENIDOR;
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(ContingutLogHelper.class);

}
