/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.ContenidorDto;
import es.caib.ripea.core.api.dto.ContenidorLogDto;
import es.caib.ripea.core.api.dto.ContenidorMovimentDto;
import es.caib.ripea.core.api.dto.LogObjecteTipusEnumDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.api.dto.UsuariDto;
import es.caib.ripea.core.entity.CarpetaEntity;
import es.caib.ripea.core.entity.ContenidorEntity;
import es.caib.ripea.core.entity.ContenidorLogEntity;
import es.caib.ripea.core.entity.ContenidorMovimentEntity;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.LogObjecteTipusEnum;
import es.caib.ripea.core.entity.LogTipusEnum;
import es.caib.ripea.core.repository.ContenidorLogRepository;
import es.caib.ripea.core.repository.ContenidorMovimentRepository;
import es.caib.ripea.core.repository.ContenidorRepository;
import es.caib.ripea.core.repository.UsuariRepository;

/**
 * Utilitat per a gestionar el registre d'accions dels contenidors.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class ContenidorLogHelper {

	@Resource
	private ContenidorRepository contenidorRepository;
	@Resource
	private ContenidorLogRepository contenidorLogRepository;
	@Resource
	private ContenidorMovimentRepository contenidorMovimentRepository;
	@Resource
	private UsuariRepository usuariRepository;

	@Resource
	private ContenidorHelper contenidorHelper;
	@Resource
	private UsuariHelper usuariHelper;
	@Resource
	private ConversioTipusHelper conversioTipusHelper;



	public ContenidorLogEntity log(
			ContenidorEntity contenidor,
			LogTipusEnum tipus,
			ContenidorLogEntity contenidorLogPare,
			ContenidorMovimentEntity contenidorMoviment,
			boolean logContenidorPare,
			boolean logExpedientSuperior) {
		return log(
				contenidor,
				tipus,
				contenidorLogPare,
				contenidorMoviment,
				null,
				null,
				null,
				null,
				null,
				logContenidorPare,
				logExpedientSuperior);
	}
	public ContenidorLogEntity log(
			ContenidorEntity contenidor,
			LogTipusEnum tipus,
			ContenidorLogEntity contenidorLogPare,
			ContenidorMovimentEntity contenidorMoviment,
			String param1,
			String param2,
			boolean logContenidorPare,
			boolean logExpedientSuperior) {
		return log(
				contenidor,
				tipus,
				contenidorLogPare,
				contenidorMoviment,
				null,
				null,
				null,
				param1,
				param2,
				logContenidorPare,
				logExpedientSuperior);
	}
	public ContenidorLogEntity log(
			ContenidorEntity contenidor,
			LogTipusEnum tipus,
			ContenidorLogEntity contenidorLogPare,
			ContenidorMovimentEntity contenidorMoviment,
			AbstractPersistable<Long> objecte,
			LogObjecteTipusEnum objecteTipus,
			LogTipusEnum objecteLogTipus,
			String param1,
			String param2,
			boolean logContenidorPare,
			boolean logExpedientSuperior) {
		ContenidorLogEntity contenidorLogPareCreat = log(
				contenidor,
				tipus,
				contenidorLogPare,
				contenidorMoviment,
				objecte,
				objecteTipus,
				objecteLogTipus,
				param1,
				param2);
		if (logContenidorPare) {
			if (contenidorMoviment == null) {
				if (contenidor.getPare() != null) {
					logContenidorSuperior(
							contenidor,
							tipus,
							contenidor.getPare(),
							contenidorLogPareCreat);
				}
			} else {
				if (contenidorMoviment.getOrigen() != null) {
					logContenidorSuperior(
							contenidor,
							tipus,
							contenidorMoviment.getOrigen(),
							contenidorLogPareCreat);
				}
				if (contenidorMoviment.getDesti() != null) {
					logContenidorSuperior(
							contenidor,
							tipus,
							contenidorMoviment.getDesti(),
							contenidorLogPareCreat);
				}
			}
		}
		if (logExpedientSuperior) {
			if (contenidorMoviment == null) {
				logExpedientSuperior(
					contenidor,
					tipus,
					contenidor,
					contenidorLogPareCreat);
			} else {
				if (contenidorMoviment.getOrigen() != null) {
					logExpedientSuperior(
							contenidor,
							tipus,
							contenidorMoviment.getOrigen(),
							contenidorLogPareCreat);
				}
				if (contenidorMoviment.getDesti() != null) {
					logExpedientSuperior(
							contenidor,
							tipus,
							contenidorMoviment.getDesti(),
							contenidorLogPareCreat);
				}
			}
		}
		return contenidorLogPare;
	}

	public List<ContenidorLogDto> findLogsContenidor(
			ContenidorEntity contenidor) {
		List<ContenidorLogEntity> logs = contenidorLogRepository.findByContenidorIdOrderByDataAsc(
				contenidor.getId());
		List<ContenidorLogDto> dtos = new ArrayList<ContenidorLogDto>();
		for (ContenidorLogEntity log: logs) {
			ContenidorLogDto dto = new ContenidorLogDto();
			dto.setId(log.getId());
			dto.setData(log.getData());
			dto.setTipus(
					LogTipusEnumDto.valueOf(
							log.getTipus().name()));
			dto.setUsuari(
					conversioTipusHelper.convertir(
							usuariRepository.findOne(log.getUsuariId()),
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

	public List<ContenidorMovimentDto> findMovimentsContenidor(
			ContenidorEntity contenidor) {
		List<ContenidorMovimentEntity> moviments = contenidorMovimentRepository.findByContenidorOrderByDataAsc(
				contenidor);
		ContenidorDto contenidorDto = contenidorHelper.toContenidorDto(contenidor);
		List<ContenidorMovimentDto> dtos = new ArrayList<ContenidorMovimentDto>();
		for (ContenidorMovimentEntity moviment: moviments) {
			ContenidorMovimentDto dto = new ContenidorMovimentDto();
			dto.setId(moviment.getId());
			dto.setData(moviment.getData());
			dto.setComentari(moviment.getComentari());
			dto.setContenidor(contenidorDto);
			dto.setRemitent(
					conversioTipusHelper.convertir(
							moviment.getRemitent(),
							UsuariDto.class));
			dto.setOrigen(
					contenidorHelper.toContenidorDto(
							moviment.getOrigen()));
			dto.setDesti(
					contenidorHelper.toContenidorDto(
							moviment.getDesti()));
			dtos.add(dto);
		}
		return dtos;
	}



	private void logExpedientSuperior(
			ContenidorEntity contenidor,
			LogTipusEnum tipus,
			ContenidorEntity contenidorSuperior,
			ContenidorLogEntity contenidorLogPare) {
		ExpedientEntity expedientSuperior = contenidorHelper.getExpedientSuperior(
				contenidorSuperior,
				true);
		if (expedientSuperior != null) {
			logContenidorSuperior(
					contenidor,
					tipus,
					expedientSuperior,
					contenidorLogPare);
		}
	}
	private void logContenidorSuperior(
			ContenidorEntity contenidor,
			LogTipusEnum tipus,
			ContenidorEntity contenidorSuperior,
			ContenidorLogEntity contenidorLogPare) {
		log(	contenidorSuperior,
				LogTipusEnum.MODIFICACIO,
				contenidorLogPare,
				null,
				contenidor,
				getLogObjecteTipus(contenidor),
				tipus,
				null,
				null);
	}

	private ContenidorLogEntity log(
			ContenidorEntity contenidor,
			LogTipusEnum tipus,
			ContenidorLogEntity logPare,
			ContenidorMovimentEntity contenidorMoviment,
			AbstractPersistable<Long> objecte,
			LogObjecteTipusEnum objecteTipus,
			LogTipusEnum objecteLogTipus,
			String param1,
			String param2) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Guardant log per contenidor ("
				+ "contenidorId=" + contenidor.getId() + ", "
				+ "tipus=" + tipus + ", "
				+ "logPareId=" + ((logPare != null) ? logPare.getId() : null) + ", "
				+ "contenidorMovimentId=" + ((contenidorMoviment != null) ? contenidorMoviment.getId() : null) + ")");
		ContenidorLogEntity log = ContenidorLogEntity.getBuilder(
				new Date(),
				tipus,
				auth.getName(),
				contenidor,
				logPare,
				contenidorMoviment,
				objecte,
				objecteTipus,
				objecteLogTipus).build();
		return contenidorLogRepository.save(log);
	}

	private LogObjecteTipusEnum getLogObjecteTipus(ContenidorEntity contenidor) {
		if (contenidor instanceof ExpedientEntity) {
			return LogObjecteTipusEnum.EXPEDIENT;
		} else if (contenidor instanceof DocumentEntity) {
			return LogObjecteTipusEnum.DOCUMENT;
		} else if (contenidor instanceof CarpetaEntity) {
			return LogObjecteTipusEnum.CARPETA;
		} else {
			return LogObjecteTipusEnum.CONTENIDOR;
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(ContenidorLogHelper.class);

}
