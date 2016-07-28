/**
 * 
 */
package es.caib.ripea.core.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.dto.ReglaDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.service.ReglaService;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ReglaEntity;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.EntityComprovarHelper;
import es.caib.ripea.core.helper.PaginacioHelper;
import es.caib.ripea.core.repository.EntitatRepository;
import es.caib.ripea.core.repository.ReglaRepository;

/**
 * Implementació dels mètodes per a gestionar regles.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class ReglaServiceImpl implements ReglaService {

	@Resource
	private EntitatRepository entitatRepository;
	@Resource
	private ReglaRepository reglaRepository;

	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private PaginacioHelper paginacioHelper;
	@Resource
	private EntityComprovarHelper entityComprovarHelper;



	@Override
	@Transactional
	public ReglaDto create(
			Long entitatId,
			ReglaDto regla) {
		logger.debug("Creant una nova regla ("
				+ "entitatId=" + entitatId + ", "
				+ "regla=" + regla + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		int ordre = reglaRepository.countByEntitat(entitat);
		ReglaEntity entity = ReglaEntity.getBuilder(
				entitat,
				regla.getNom(),
				regla.getTipus(),
				regla.getAssumpteCodi(),
				regla.getUnitatCodi(),
				ordre).build();
		return conversioTipusHelper.convertir(
				reglaRepository.save(entity),
				ReglaDto.class);
	}

	@Override
	@Transactional
	public ReglaDto update(
			Long entitatId,
			ReglaDto regla) throws NotFoundException {
		logger.debug("Modificant la regla ("
				+ "entitatId=" + entitatId + ", "
				+ "regla=" + regla + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ReglaEntity entity = entityComprovarHelper.comprovarRegla(
				entitat,
				regla.getId());
		entity.update(
				regla.getNom(),
				regla.getDescripcio(),
				regla.getTipus(),
				regla.getAssumpteCodi(),
				regla.getUnitatCodi());
		return conversioTipusHelper.convertir(
				reglaRepository.save(entity),
				ReglaDto.class);
	}

	@Override
	@Transactional
	public ReglaDto updateActiva(
			Long entitatId,
			Long reglaId,
			boolean activa) throws NotFoundException {
		logger.debug("Modificant propietat activa de la regla ("
				+ "entitatId=" + entitatId + ", "
				+ "reglaId=" + reglaId + ", "
				+ "activa=" + activa + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ReglaEntity regla = entityComprovarHelper.comprovarRegla(
				entitat,
				reglaId);
		regla.updateActiva(activa);
		return conversioTipusHelper.convertir(
				reglaRepository.save(regla),
				ReglaDto.class);
	}

	@Override
	@Transactional
	public ReglaDto delete(
			Long entitatId,
			Long reglaId) throws NotFoundException {
		logger.debug("Esborrant la regla ("
				+ "entitatId=" + entitatId + ", "
				+ "reglaId=" + reglaId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ReglaEntity regla = entityComprovarHelper.comprovarRegla(
				entitat,
				reglaId);
		reglaRepository.delete(regla);
		return conversioTipusHelper.convertir(
				regla,
				ReglaDto.class);
	}

	@Override
	@Transactional
	public ReglaDto moveUp(
			Long entitatId,
			Long reglaId) throws NotFoundException {
		logger.debug("Movent la regla per amunt ("
				+ "entitatId=" + entitatId + ", "
				+ "reglaId=" + reglaId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ReglaEntity regla = entityComprovarHelper.comprovarRegla(
				entitat,
				reglaId);
		canviPosicio(
				regla,
				regla.getOrdre() - 1);
		return conversioTipusHelper.convertir(
				regla,
				ReglaDto.class);
	}

	@Override
	@Transactional
	public ReglaDto moveDown(
			Long entitatId,
			Long reglaId) throws NotFoundException {
		logger.debug("Movent la regla per avall ("
				+ "entitatId=" + entitatId + ", "
				+ "reglaId=" + reglaId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ReglaEntity regla = entityComprovarHelper.comprovarRegla(
				entitat,
				reglaId);
		canviPosicio(
				regla,
				regla.getOrdre() + 1);
		return conversioTipusHelper.convertir(
				regla,
				ReglaDto.class);
	}

	@Override
	@Transactional
	public ReglaDto moveTo(
			Long entitatId,
			Long reglaId,
			int posicio) throws NotFoundException {
		logger.debug("Movent la regla a la posició especificada ("
				+ "entitatId=" + entitatId + ", "
				+ "reglaId=" + reglaId + ", "
				+ "posicio=" + posicio + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ReglaEntity regla = entityComprovarHelper.comprovarRegla(
				entitat,
				reglaId);
		canviPosicio(
				regla,
				posicio);
		return conversioTipusHelper.convertir(
				regla,
				ReglaDto.class);
	}

	@Override
	@Transactional(readOnly = true)
	public ReglaDto findOne(
			Long entitatId,
			Long reglaId) {
		logger.debug("Cercant la regla ("
				+ "entitatId=" + entitatId + ", "
				+ "reglaId=" + reglaId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ReglaEntity regla = entityComprovarHelper.comprovarRegla(
				entitat,
				reglaId);
		return conversioTipusHelper.convertir(
				regla,
				ReglaDto.class);
	}

	@Override
	@Transactional(readOnly = true)
	public PaginaDto<ReglaDto> findAmbEntitatPaginat(
			Long entitatId,
			PaginacioParamsDto paginacioParams) {
		logger.debug("Consulta de regles amb paginació ("
				+ "entitatId=" + entitatId + ", "
				+ "paginacioParams=" + paginacioParams + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		return paginacioHelper.toPaginaDto(
				reglaRepository.findByEntitatAndFiltrePaginat(
						entitat,
						paginacioParams.getFiltre() == null,
						paginacioParams.getFiltre(),
						paginacioHelper.toSpringDataPageable(paginacioParams)),
				ReglaDto.class);
	}



	private void canviPosicio(
			ReglaEntity regla,
			int posicio) {
		List<ReglaEntity> regles = reglaRepository.findByEntitatOrderByOrdreAsc(
				regla.getEntitat());
		if (posicio >= 0 && posicio < regles.size()) {
			if (posicio < regla.getOrdre()) {
				for (ReglaEntity reg: regles) {
					if (reg.getOrdre() >= posicio && reg.getOrdre() < regla.getOrdre()) {
						reg.updateOrdre(reg.getOrdre() + 1);
					}
				}
			} else if (posicio > regla.getOrdre()) {
				for (ReglaEntity reg: regles) {
					if (reg.getOrdre() > regla.getOrdre() && reg.getOrdre() <= posicio) {
						reg.updateOrdre(reg.getOrdre() - 1);
					}
				}
			}
			regla.updateOrdre(posicio);
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(ReglaServiceImpl.class);

}