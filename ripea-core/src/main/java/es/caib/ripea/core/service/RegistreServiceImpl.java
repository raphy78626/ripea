/**
 * 
 */
package es.caib.ripea.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.AnnexArxiuTipusEnumDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.api.dto.RegistreAnnexDetallDto;
import es.caib.ripea.core.api.dto.RegistreAnotacioDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ScheduledTaskException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.registre.RegistreProcesEstatEnum;
import es.caib.ripea.core.api.service.RegistreService;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.RegistreAnnexEntity;
import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.helper.BustiaHelper;
import es.caib.ripea.core.helper.ContingutHelper;
import es.caib.ripea.core.helper.ContingutLogHelper;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.EntityComprovarHelper;
import es.caib.ripea.core.helper.RegistreHelper;
import es.caib.ripea.core.helper.ReglaHelper;
import es.caib.ripea.core.repository.BustiaRepository;
import es.caib.ripea.core.repository.ExpedientRepository;
import es.caib.ripea.core.repository.RegistreAnnexRepository;
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
	private RegistreAnnexRepository registreAnnexRepository;

	@Resource
	private ContingutHelper contingutHelper;
	@Resource
	private ContingutLogHelper contingutLogHelper;
	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private EntityComprovarHelper entityComprovarHelper;
	@Resource
	private ReglaHelper reglaHelper;
	@Resource
	private BustiaHelper bustiaHelper;
	@Resource
	private RegistreHelper registreHelper;



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

	@Override
	@Transactional
	@Async
	@Scheduled(fixedRateString = "${config:es.caib.ripea.tasca.regla.pendent.periode.execucio}")
	public void reglaAplicarPendents() {
		logger.debug("Aplicant regles a les anotacions pendents");
		List<RegistreEntity> pendents = registreRepository.findByReglaNotNullAndProcesEstatOrderByCreatedDateAsc(
				RegistreProcesEstatEnum.PENDENT);
		logger.debug("Aplicant regles a " + pendents.size() + " registres pendents");
		if (!pendents.isEmpty()) {
			for (RegistreEntity pendent: pendents) {
				reglaAplicar(pendent);
			}
		} else {
			logger.debug("No hi ha registres pendents de processar");
		}
	}

	@Override
	@Transactional
	public boolean reglaReintentarAdmin(
			Long entitatId,
			Long bustiaId,
			Long registreId) {
		logger.debug("Reintentant aplicació de regla a anotació pendent (" +
				"entitatId=" + entitatId + ", " +
				"bustiaId=" + bustiaId + ", " +
				"registreId=" + registreId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		BustiaEntity bustia = entityComprovarHelper.comprovarBustia(
				entitat,
				bustiaId,
				false);
		RegistreEntity anotacio = entityComprovarHelper.comprovarRegistre(
				registreId,
				bustia);
		if (	RegistreProcesEstatEnum.PENDENT.equals(anotacio.getProcesEstat()) ||
				RegistreProcesEstatEnum.ERROR.equals(anotacio.getProcesEstat())) {
			return reglaAplicar(anotacio);
		} else {
			throw new ValidationException(
					anotacio.getId(),
					RegistreEntity.class,
					"L'estat de l'anotació no és PENDENT o ERROR");
		}
	}

	@Override
	@Transactional
	public boolean reglaReintentarUser(
			Long entitatId,
			Long bustiaId,
			Long registreId) {
		logger.debug("Reintentant aplicació de regla a anotació pendent (" +
				"entitatId=" + entitatId + ", " +
				"bustiaId=" + bustiaId + ", " +
				"registreId=" + registreId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		BustiaEntity bustia = entityComprovarHelper.comprovarBustia(
				entitat,
				bustiaId,
				true);
		RegistreEntity anotacio = entityComprovarHelper.comprovarRegistre(
				registreId,
				bustia);
		if (	RegistreProcesEstatEnum.PENDENT.equals(anotacio.getProcesEstat()) ||
				RegistreProcesEstatEnum.ERROR.equals(anotacio.getProcesEstat())) {
			return reglaAplicar(anotacio);
		} else {
			throw new ValidationException(
					anotacio.getId(),
					RegistreEntity.class,
					"L'estat de l'anotació no és PENDENT o ERROR");
		}
	}
	
	@Transactional(readOnly = true)
	@Override
	public FitxerDto getArxiuAnnex(
			Long annexId, 
			AnnexArxiuTipusEnumDto tipus) {
		RegistreAnnexEntity annex = registreAnnexRepository.findOne(annexId);
		String nomArxiu = annex.getId().toString();
		String contentType = "";
		String nomFinal = "";
		if (tipus == AnnexArxiuTipusEnumDto.DOCUMENT) {
			nomArxiu += "_d";
			contentType = annex.getFitxerTipusMime();
			nomFinal = annex.getFitxerNom();
		} else {
			nomArxiu += "_f";
			contentType = annex.getFirmaFitxerTipusMime();
			nomFinal = annex.getFirmaFitxerNom();
		}
		byte[] contingutArxiu = registreHelper.getAnnexArxiuContingut(nomArxiu + "." + contentType);
			
		FitxerDto arxiu = new FitxerDto();
		arxiu.setNom(nomFinal);
		arxiu.setContentType(contentType);
		arxiu.setContingut(contingutArxiu);
		return arxiu;
	}

	@Transactional(readOnly = true)
	@Override
	public List<RegistreAnnexDetallDto> getAnnexosAmbArxiu(
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

		List<RegistreAnnexDetallDto> annexos = new ArrayList<RegistreAnnexDetallDto>();
		for (RegistreAnnexEntity annexEntity: registre.getAnnexos()) {
			RegistreAnnexDetallDto annex = conversioTipusHelper.convertir(annexEntity, RegistreAnnexDetallDto.class);
			registreHelper.comprovarDocumentsFirmesPerAnnex(annex);
			annexos.add(annex);
		}
		
		return annexos;
	}

	private boolean reglaAplicar(
			RegistreEntity anotacio) {
		contingutLogHelper.log(
				anotacio,
				LogTipusEnumDto.PROCESSAMENT,
				null,
				null,
				false,
				false);
		logger.debug("Aplicant regla a anotació de registre (" +
				"anotacioId=" + anotacio.getId() + ", " +
				"anotacioNum=" + anotacio.getIdentificador() + ", " +
				"reglaId=" + anotacio.getRegla().getId() + ", " +
				"reglaTipus=" + anotacio.getRegla().getTipus().name() + ", " +
				"metaExpedient=" + anotacio.getRegla().getMetaExpedient() + ", " +
				"arxiu=" + anotacio.getRegla().getArxiu() + ", " +
				"bustia=" + anotacio.getRegla().getBustia() + ")");
		try {
			reglaHelper.aplicar(anotacio.getId());
			logger.debug("Processament anotació OK (id=" + anotacio.getId() + ", núm.=" + anotacio.getIdentificador() + ")");
			return true;
		} catch (Exception ex) {
			String procesError;
			if (ex instanceof ScheduledTaskException) {
				procesError = ex.getMessage();
			} else {
				procesError = ExceptionUtils.getStackTrace(ExceptionUtils.getRootCause(ex));
			}
			reglaHelper.actualitzarEstatError(
					anotacio.getId(),
					procesError);
			logger.debug("Processament anotació ERROR (" +
					"id=" + anotacio.getId() + ", " +
					"núm.=" + anotacio.getIdentificador() + "): " +
					procesError);
			return false;
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(RegistreServiceImpl.class);

}
