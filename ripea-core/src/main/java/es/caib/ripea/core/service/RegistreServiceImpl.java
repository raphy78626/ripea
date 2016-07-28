/**
 * 
 */
package es.caib.ripea.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.RegistreAnotacioDto;
import es.caib.ripea.core.api.dto.RegistreMovimentDto;
import es.caib.ripea.core.api.dto.UsuariDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.service.RegistreService;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.ContenidorEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.entity.RegistreMovimentEntity;
import es.caib.ripea.core.helper.BustiaHelper;
import es.caib.ripea.core.helper.ContenidorHelper;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.EntityComprovarHelper;
import es.caib.ripea.core.repository.BustiaRepository;
import es.caib.ripea.core.repository.ExpedientRepository;
import es.caib.ripea.core.repository.RegistreMovimentRepository;
import es.caib.ripea.core.repository.RegistreRepository;

/**
 * Implementació dels mètodes per a gestionar anotacions
 * de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class RegistreServiceImpl implements RegistreService {

	@Resource
	private RegistreRepository registreRepository;
	@Resource
	private ExpedientRepository expedientRepository;
	@Resource
	private BustiaRepository bustiaRepository;
	@Resource
	private RegistreMovimentRepository registreMovimentRepository;

	@Resource
	private ContenidorHelper contenidorHelper;
	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private EntityComprovarHelper entityComprovarHelper;
	@Resource
	private BustiaHelper bustiaHelper;



	@Transactional(readOnly = true)
	@Override
	public RegistreAnotacioDto findOne(
			Long entitatId,
			Long contenidorId,
			Long registreId) throws NotFoundException {
		logger.debug("Obtenint anotació de registre ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ", "
				+ "registreId=" + registreId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidor = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorId,
				null);
		if (contenidor instanceof BustiaEntity) {
			entityComprovarHelper.comprovarBustia(
					entitat,
					contenidorId,
					true);
		} else {
			// Comprova l'accés al path del contenidor pare
			contenidorHelper.comprovarPermisosPathContenidor(
					contenidor,
					true,
					false,
					false,
					true);
		}
		return conversioTipusHelper.convertir(
				registreRepository.findByContenidorAndId(
						contenidor,
						registreId),
				RegistreAnotacioDto.class);
	}

	@Transactional(readOnly = true)
	@Override
	public List<RegistreMovimentDto> findMoviments(
			Long entitatId,
			Long registreId) {
		logger.debug("Obtenint moviments d'una anotació de registre ("
				+ "entitatId=" + entitatId + ", "
				+ "registreId=" + registreId + ")");
		entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		RegistreEntity registre = entityComprovarHelper.comprovarRegistre(
				registreId,
				null);
		List<RegistreMovimentEntity> moviments = registreMovimentRepository.findByRegistreOrderByDataAsc(
				registre);
		List<RegistreMovimentDto> dtos = new ArrayList<RegistreMovimentDto>();
		for (RegistreMovimentEntity moviment: moviments) {
			RegistreMovimentDto dto = new RegistreMovimentDto();
			dto.setId(moviment.getId());
			dto.setData(moviment.getData());
			dto.setComentari(moviment.getComentari());
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

	@Transactional
	@Override
	public void rebutjar(
			Long entitatId,
			Long bustiaId,
			Long registreId,
			String motiu) {
		logger.debug("Rebutjar anotació de registre a la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "bustiaId=" + bustiaId + ", "
				+ "registreId=" + registreId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		BustiaEntity bustia = entityComprovarHelper.comprovarBustia(
				entitat,
				bustiaId,
				true);
		RegistreEntity registre = entityComprovarHelper.comprovarRegistre(
				registreId,
				null);
		if (!registre.getContenidor().equals(bustia)) {
			logger.error("No s'ha trobat el registre a dins la bústia (" +
					"bustiaId=" + bustiaId + "," +
					"registreId=" + registreId + ")");
			throw new ValidationException(
					registreId,
					RegistreEntity.class,
					"La bústia especificada (id=" + bustiaId + ") no coincideix amb la bústia de l'anotació de registre");
		}
		registre.updateRebuig(motiu);
		// Refrescam cache usuaris bústia
		bustiaHelper.evictElementsPendentsBustia(
				entitat,
				bustia);
	}

	private static final Logger logger = LoggerFactory.getLogger(RegistreServiceImpl.class);

}
