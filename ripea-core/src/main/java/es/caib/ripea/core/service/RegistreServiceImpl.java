/**
 * 
 */
package es.caib.ripea.core.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.RegistreAnotacioDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.service.RegistreService;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.helper.BustiaHelper;
import es.caib.ripea.core.helper.ContingutHelper;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.EntityComprovarHelper;
import es.caib.ripea.core.repository.BustiaRepository;
import es.caib.ripea.core.repository.ExpedientRepository;
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
	private ContingutHelper contingutHelper;
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
			Long contingutId,
			Long registreId) throws NotFoundException {
		logger.debug("Obtenint anotació de registre ("
				+ "entitatId=" + entitatId + ", "
				+ "contingutId=" + contingutId + ", "
				+ "registreId=" + registreId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		if (contingut instanceof BustiaEntity) {
			entityComprovarHelper.comprovarBustia(
					entitat,
					contingutId,
					true);
		} else {
			// Comprova l'accés al path del contenidor pare
			contingutHelper.comprovarPermisosPathContingut(
					contingut,
					true,
					false,
					false,
					true);
		}
		RegistreEntity registre = registreRepository.findByPareAndId(
				contingut,
				registreId);
		return (RegistreAnotacioDto)contingutHelper.toContingutDto(
				registre);
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
		if (!registre.getPare().equals(bustia)) {
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
