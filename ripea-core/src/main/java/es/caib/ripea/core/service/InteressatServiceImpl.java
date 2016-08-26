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

import es.caib.ripea.core.api.dto.InteressatAdministracioDto;
import es.caib.ripea.core.api.dto.InteressatDto;
import es.caib.ripea.core.api.dto.InteressatPersonaFisicaDto;
import es.caib.ripea.core.api.dto.InteressatPersonaJuridicaDto;
import es.caib.ripea.core.api.dto.LogObjecteTipusEnumDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.service.InteressatService;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.InteressatAdministracioEntity;
import es.caib.ripea.core.entity.InteressatEntity;
import es.caib.ripea.core.entity.InteressatPersonaFisicaEntity;
import es.caib.ripea.core.entity.InteressatPersonaJuridicaEntity;
import es.caib.ripea.core.helper.CacheHelper;
import es.caib.ripea.core.helper.ContingutHelper;
import es.caib.ripea.core.helper.ContingutLogHelper;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.EntityComprovarHelper;
import es.caib.ripea.core.helper.PermisosHelper;
import es.caib.ripea.core.repository.EntitatRepository;
import es.caib.ripea.core.repository.ExpedientRepository;
import es.caib.ripea.core.repository.InteressatRepository;

/**
 * Implementació dels mètodes per a gestionar la versió de l'aplicació.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class InteressatServiceImpl implements InteressatService {

	@Resource
	private EntitatRepository entitatRepository;
	@Resource
	private ExpedientRepository expedientRepository;
	@Resource
	private InteressatRepository interessatRepository;

	@Resource
	private CacheHelper cacheHelper;
	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private ContingutHelper contenidorHelper;
	@Resource
	private ContingutLogHelper contenidorLogHelper;
	@Resource
	private PermisosHelper permisosHelper;
	@Resource
	private EntityComprovarHelper entityComprovarHelper;



	@Transactional
	@Override
	public InteressatDto create(
			Long entitatId,
			Long expedientId,
			InteressatDto interessat) {
		logger.debug("Creant nou interessat ("
				+ "entitatId=" + entitatId + ", "
				+ "expedientId=" + expedientId + ", "
				+ "interessat=" + interessat + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				expedientId);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				expedient);
		// Comprova l'accés al path del contenidor pare
		contenidorHelper.comprovarPermisosPathContingut(
				expedient,
				true,
				false,
				false,
				true);
		// Comprova el permís de modificació de l'expedient
		contenidorHelper.comprovarPermisosContingut(
				expedient,
				false,
				true,
				false);
		InteressatEntity interessatEntity = null;
		if (interessat.isPersonaFisica()) {
			InteressatPersonaFisicaDto interessatPersonaFisicaDto = (InteressatPersonaFisicaDto)interessat;
			interessatEntity = InteressatPersonaFisicaEntity.getBuilder(
					interessatPersonaFisicaDto.getNom(),
					interessatPersonaFisicaDto.getLlinatge1(),
					interessatPersonaFisicaDto.getLlinatge2(),
					interessatPersonaFisicaDto.getDocumentTipus(),
					interessatPersonaFisicaDto.getDocumentNum(),
					interessatPersonaFisicaDto.getPais(),
					interessatPersonaFisicaDto.getProvincia(),
					interessatPersonaFisicaDto.getMunicipi(),
					interessatPersonaFisicaDto.getAdresa(),
					interessatPersonaFisicaDto.getCodiPostal(),
					interessatPersonaFisicaDto.getEmail(),
					interessatPersonaFisicaDto.getTelefon(),
					interessatPersonaFisicaDto.getObservacions(),
					interessatPersonaFisicaDto.getNotificacioIdioma(),
					interessatPersonaFisicaDto.getNotificacioAutoritzat(),
					expedient,
					null,
					entitat).build();
		} else if (interessat.isPersonaJuridica()) {
			InteressatPersonaJuridicaDto interessatPersonaJuridicaDto = (InteressatPersonaJuridicaDto)interessat;
			interessatEntity = InteressatPersonaJuridicaEntity.getBuilder(
					interessatPersonaJuridicaDto.getRaoSocial(),
					interessatPersonaJuridicaDto.getDocumentTipus(),
					interessatPersonaJuridicaDto.getDocumentNum(),
					interessatPersonaJuridicaDto.getPais(),
					interessatPersonaJuridicaDto.getProvincia(),
					interessatPersonaJuridicaDto.getMunicipi(),
					interessatPersonaJuridicaDto.getAdresa(),
					interessatPersonaJuridicaDto.getCodiPostal(),
					interessatPersonaJuridicaDto.getEmail(),
					interessatPersonaJuridicaDto.getTelefon(),
					interessatPersonaJuridicaDto.getObservacions(),
					interessatPersonaJuridicaDto.getNotificacioIdioma(),
					interessatPersonaJuridicaDto.getNotificacioAutoritzat(),
					expedient,
					null,
					entitat).build();
		} else {
			InteressatAdministracioDto interessatAdministracioDto = (InteressatAdministracioDto)interessat;
			interessatEntity = InteressatAdministracioEntity.getBuilder(
					interessatAdministracioDto.getOrganCodi(),
					interessatAdministracioDto.getDocumentTipus(),
					interessatAdministracioDto.getDocumentNum(),
					interessatAdministracioDto.getPais(),
					interessatAdministracioDto.getProvincia(),
					interessatAdministracioDto.getMunicipi(),
					interessatAdministracioDto.getAdresa(),
					interessatAdministracioDto.getCodiPostal(),
					interessatAdministracioDto.getEmail(),
					interessatAdministracioDto.getTelefon(),
					interessatAdministracioDto.getObservacions(),
					interessatAdministracioDto.getNotificacioIdioma(),
					interessatAdministracioDto.getNotificacioAutoritzat(),
					expedient,
					null,
					entitat).build();
		}
		interessatEntity = interessatRepository.save(interessatEntity);
		expedient.addInteressat(interessatEntity);
		// Registra al log la creació de l'interessat
		contenidorLogHelper.log(
				expedient,
				LogTipusEnumDto.MODIFICACIO,
				null,
				null,
				interessatEntity,
				LogObjecteTipusEnumDto.INTERESSAT,
				LogTipusEnumDto.CREACIO,
				null,
				null,
				false,
				false);
		if (interessat instanceof InteressatPersonaFisicaDto) {
			return conversioTipusHelper.convertir(
					interessatRepository.save(interessatEntity),
					InteressatPersonaFisicaDto.class);
		} else if (interessat instanceof InteressatPersonaJuridicaDto) {
			return conversioTipusHelper.convertir(
					interessatRepository.save(interessatEntity),
					InteressatPersonaJuridicaDto.class);
		} else {
			return conversioTipusHelper.convertir(
					interessatRepository.save(interessatEntity),
					InteressatAdministracioDto.class);
		}
	}
	
	@Transactional
	@Override
	public InteressatDto update(
			Long entitatId,
			Long expedientId,
			InteressatDto interessat) {
		logger.debug("Modificant un interessat ("
				+ "entitatId=" + entitatId + ", "
				+ "expedientId=" + expedientId + ", "
				+ "interessat=" + interessat + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				expedientId);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				expedient);
		// Comprova l'accés al path del contenidor pare
		contenidorHelper.comprovarPermisosPathContingut(
				expedient,
				true,
				false,
				false,
				true);
		// Comprova el permís de modificació de l'expedient
		contenidorHelper.comprovarPermisosContingut(
				expedient,
				false,
				true,
				false);
		
		InteressatEntity interessatEntity = null;
		
		if (interessat.isPersonaFisica()) {
			InteressatPersonaFisicaDto interessatPersonaFisicaDto = (InteressatPersonaFisicaDto)interessat;
			interessatEntity = interessatRepository.findPersonaFisicaById(interessat.getId());
			((InteressatPersonaFisicaEntity)interessatEntity).update(
					interessatPersonaFisicaDto.getNom(),
					interessatPersonaFisicaDto.getLlinatge1(),
					interessatPersonaFisicaDto.getLlinatge2(),
					interessatPersonaFisicaDto.getDocumentTipus(),
					interessatPersonaFisicaDto.getDocumentNum(),
					interessatPersonaFisicaDto.getPais(),
					interessatPersonaFisicaDto.getProvincia(),
					interessatPersonaFisicaDto.getMunicipi(),
					interessatPersonaFisicaDto.getAdresa(),
					interessatPersonaFisicaDto.getCodiPostal(),
					interessatPersonaFisicaDto.getEmail(),
					interessatPersonaFisicaDto.getTelefon(),
					interessatPersonaFisicaDto.getObservacions(),
					interessatPersonaFisicaDto.getNotificacioIdioma(),
					interessatPersonaFisicaDto.getNotificacioAutoritzat());
		} else if (interessat.isPersonaJuridica()) {
			InteressatPersonaJuridicaDto interessatPersonaJuridicaDto = (InteressatPersonaJuridicaDto)interessat;
			interessatEntity = interessatRepository.findPersonaJuridicaById(interessat.getId());
			((InteressatPersonaJuridicaEntity)interessatEntity).update(
					interessatPersonaJuridicaDto.getRaoSocial(),
					interessatPersonaJuridicaDto.getDocumentTipus(),
					interessatPersonaJuridicaDto.getDocumentNum(),
					interessatPersonaJuridicaDto.getPais(),
					interessatPersonaJuridicaDto.getProvincia(),
					interessatPersonaJuridicaDto.getMunicipi(),
					interessatPersonaJuridicaDto.getAdresa(),
					interessatPersonaJuridicaDto.getCodiPostal(),
					interessatPersonaJuridicaDto.getEmail(),
					interessatPersonaJuridicaDto.getTelefon(),
					interessatPersonaJuridicaDto.getObservacions(),
					interessatPersonaJuridicaDto.getNotificacioIdioma(),
					interessatPersonaJuridicaDto.getNotificacioAutoritzat());
		} else {
			InteressatAdministracioDto interessatAdministracioDto = (InteressatAdministracioDto)interessat;
			interessatEntity = interessatRepository.findAdministracioById(interessat.getId());
			((InteressatAdministracioEntity)interessatEntity).update(
					interessatAdministracioDto.getOrganCodi(),
					interessatAdministracioDto.getDocumentTipus(),
					interessatAdministracioDto.getDocumentNum(),
					interessatAdministracioDto.getPais(),
					interessatAdministracioDto.getProvincia(),
					interessatAdministracioDto.getMunicipi(),
					interessatAdministracioDto.getAdresa(),
					interessatAdministracioDto.getCodiPostal(),
					interessatAdministracioDto.getEmail(),
					interessatAdministracioDto.getTelefon(),
					interessatAdministracioDto.getObservacions(),
					interessatAdministracioDto.getNotificacioIdioma(),
					interessatAdministracioDto.getNotificacioAutoritzat());
		}
		interessatEntity = interessatRepository.save(interessatEntity);
		// Registra al log la creació de l'interessat
		contenidorLogHelper.log(
				expedient,
				LogTipusEnumDto.MODIFICACIO,
				null,
				null,
				interessatEntity,
				LogObjecteTipusEnumDto.INTERESSAT,
				LogTipusEnumDto.CREACIO,
				null,
				null,
				false,
				false);
		if (interessat instanceof InteressatPersonaFisicaDto) {
			return conversioTipusHelper.convertir(
					interessatRepository.save(interessatEntity),
					InteressatPersonaFisicaDto.class);
		} else if (interessat instanceof InteressatPersonaJuridicaDto) {
			return conversioTipusHelper.convertir(
					interessatRepository.save(interessatEntity),
					InteressatPersonaJuridicaDto.class);
		} else {
			return conversioTipusHelper.convertir(
					interessatRepository.save(interessatEntity),
					InteressatAdministracioDto.class);
		}
	}

	@Transactional
	@Override
	public void delete(
			Long entitatId,
			Long expedientId,
			Long interessatId) {
		logger.debug("Esborrant interessat de l'expedient  ("
				+ "entitatId=" + entitatId + ", "
				+ "expedientId=" + expedientId + ", "
				+ "interessatId=" + interessatId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				expedientId);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				expedient);
		// Comprova l'accés al path del contenidor pare
		contenidorHelper.comprovarPermisosPathContingut(
				expedient,
				true,
				false,
				false,
				true);
		// Comprova el permís de modificació de l'expedient
		contenidorHelper.comprovarPermisosContingut(
				expedient,
				false,
				true,
				false);
		InteressatEntity interessat = interessatRepository.findOne(interessatId);
		if (interessat != null) {
			interessatRepository.delete(interessat);
			//expedient.deleteInteressat(interessat);
			// Registra al log la baixa de l'interessat
			contenidorLogHelper.log(
					expedient,
					LogTipusEnumDto.MODIFICACIO,
					null,
					null,
					interessat,
					LogObjecteTipusEnumDto.INTERESSAT,
					LogTipusEnumDto.ELIMINACIO,
					null,
					null,
					false,
					false);
		} else {
			logger.error("No s'ha trobat l'interessat a l'expedient ("
					+ "expedientId=" + expedientId + ", "
					+ "interessatId=" + interessatId + ")");
			throw new ValidationException(
					interessatId,
					InteressatEntity.class,
					"No s'ha trobat l'interessat a l'expedient (expedientId=" + expedientId + ")");
		}
	}
	
//	@Transactional
//	@Override
//	public void addToExpedient(
//			Long entitatId,
//			Long expedientId,
//			Long id) {
//		logger.debug("Afegint interessat a l'expedient ("
//				+ "entitatId=" + entitatId + ", "
//				+ "expedientId=" + expedientId + ", "
//				+ "id=" + id + ")");
//		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
//				entitatId,
//				true,
//				false,
//				false);
//		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
//				entitat,
//				null,
//				expedientId);
//		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
//		contenidorHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
//				entitat,
//				expedient);
//		// Comprova l'accés al path del contenidor pare
//		contenidorHelper.comprovarPermisosPathContingut(
//				expedient,
//				true,
//				false,
//				false,
//				true);
//		// Comprova el permís de modificació de l'expedient
//		contenidorHelper.comprovarPermisosContingut(
//				expedient,
//				false,
//				true,
//				false);
//		InteressatEntity interessat = entityComprovarHelper.comprovarInteressat(
//				entitat,
//				id);
//		expedient.addInteressat(interessat);
//		// Registra al log la creació de l'interessat
//		contenidorLogHelper.log(
//				expedient,
//				LogTipusEnumDto.MODIFICACIO,
//				null,
//				null,
//				interessat,
//				LogObjecteTipusEnumDto.INTERESSAT,
//				LogTipusEnumDto.CREACIO,
//				null,
//				null,
//				false,
//				false);
//	}
//	@Transactional
//	@Override
//	public void removeFromExpedient(
//			Long entitatId,
//			Long expedientId,
//			Long id) {
//		logger.debug("Esborrant interessat de l'expedient  ("
//				+ "entitatId=" + entitatId + ", "
//				+ "expedientId=" + expedientId + ", "
//				+ "id=" + id + ")");
//		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
//				entitatId,
//				true,
//				false,
//				false);
//		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
//				entitat,
//				null,
//				expedientId);
//		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
//		contenidorHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
//				entitat,
//				expedient);
//		// Comprova l'accés al path del contenidor pare
//		contenidorHelper.comprovarPermisosPathContingut(
//				expedient,
//				true,
//				false,
//				false,
//				true);
//		// Comprova el permís de modificació de l'expedient
//		contenidorHelper.comprovarPermisosContingut(
//				expedient,
//				false,
//				true,
//				false);
//		InteressatEntity interessat = entityComprovarHelper.comprovarInteressat(
//				entitat,
//				id);
//		List<InteressatEntity> interessats = interessatRepository.findByEntitatAndExpedient(
//				entitat,
//				expedient);
//		boolean trobat = false;
//		for (InteressatEntity i: interessats) {
//			if (i.getId().equals(interessat.getId())) {
//				trobat = true;
//				break;
//			}
//		}
//		if (trobat) {
//			expedient.deleteInteressat(interessat);
//			// Registra al log la baixa de l'interessat
//			contenidorLogHelper.log(
//					expedient,
//					LogTipusEnumDto.MODIFICACIO,
//					null,
//					null,
//					interessat,
//					LogObjecteTipusEnumDto.INTERESSAT,
//					LogTipusEnumDto.ELIMINACIO,
//					null,
//					null,
//					false,
//					false);
//		} else {
//			logger.error("No s'ha trobat l'interessat a l'expedient ("
//					+ "expedientId=" + expedientId + ", "
//					+ "id=" + id + ")");
//			throw new ValidationException(
//					id,
//					InteressatEntity.class,
//					"No s'ha trobat l'interessat a l'expedient (expedientId=" + expedientId + ")");
//		}
//	}

	@Transactional(readOnly=true)
	@Override
	public InteressatDto findById(
			Long entitatId,
			Long id) {
		logger.debug("Consulta de l'interessat ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		InteressatEntity interessat = entityComprovarHelper.comprovarInteressat(
				entitat,
				id);
		return conversioTipusHelper.convertir(
				interessat,
				InteressatDto.class);
	}

	@Transactional(readOnly=true)
	@Override
	public List<InteressatDto> findByExpedient(
			Long entitatId,
			Long expedientId) {
		logger.debug("Consulta interessats de l'expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "expedientId=" + expedientId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				expedientId);
		List<InteressatEntity> interessats = interessatRepository.findByEntitatAndExpedient(
				entitat,
				expedient);
		List<InteressatDto> resposta = new ArrayList<InteressatDto>();
		for (InteressatEntity interessat: interessats) {
			if (interessat instanceof InteressatPersonaFisicaEntity)
				resposta.add(conversioTipusHelper.convertir(
						interessat,
						InteressatPersonaFisicaDto.class));
			else if (interessat instanceof InteressatAdministracioEntity)
				resposta.add(conversioTipusHelper.convertir(
						interessat,
						InteressatAdministracioDto.class));
		}
		return resposta;
	}
	
	@Transactional(readOnly=true)
	@Override
	public Long countByExpedient(
			Long entitatId,
			Long expedientId) {
		logger.debug("Consulta interessats de l'expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "expedientId=" + expedientId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				expedientId);
		Long interessatcount = interessatRepository.countByEntitatAndExpedient(
				entitat,
				expedient);
		if (interessatcount == null)
			interessatcount = 0L;
		return interessatcount;
	}

	@Transactional(readOnly = true)
	@Override
	public List<InteressatPersonaFisicaDto> findByFiltrePersonaFisica(
			Long entitatId,
			String documentNum,
			String nom,
			String llinatge1,
			String llinatge2) {
		logger.debug("Consulta interessats de tipus ciutadà ("
				+ "entitatId=" + entitatId + ", "
				+ "nom=" + nom + ", "
				+ "documentNum=" + documentNum + ", "
				+ "llinatge1=" + llinatge1 + ", "
				+ "llinatge2=" + llinatge2 + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		return conversioTipusHelper.convertirList(
				interessatRepository.findByFiltrePersonaFisica(
						entitat,
						nom == null,
						nom,
						documentNum == null,
						documentNum,
						llinatge1 == null,
						llinatge1,
						llinatge2 == null,
						llinatge2),
				InteressatPersonaFisicaDto.class);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<InteressatPersonaJuridicaDto> findByFiltrePersonaJuridica(
			Long entitatId,
			String documentNum,
			String raoSocial) {
		logger.debug("Consulta interessats de tipus ciutadà ("
				+ "entitatId=" + entitatId + ", "
				+ "raoSocial=" + raoSocial + ", "
				+ "documentNum=" + documentNum + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		return conversioTipusHelper.convertirList(
				interessatRepository.findByFiltrePersonaJuridica(
						entitat, 
						documentNum == null,
						documentNum,
						raoSocial == null,
						raoSocial),
				InteressatPersonaJuridicaDto.class);
	}

	@Transactional(readOnly = true)
	@Override
	public List<InteressatAdministracioDto> findByFiltreAdministracio(
			Long entitatId,
			String organCodi) {
		logger.debug("Consulta interessats de tipus administració ("
				+ "entitatId=" + entitatId + ", "
				+ "organCodi=" + organCodi + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		List<InteressatAdministracioEntity> administracions = interessatRepository.findByFiltreAdministracio(
				entitat,
				organCodi == null,
				organCodi);
		return conversioTipusHelper.convertirList(
				administracions,
				InteressatAdministracioDto.class);
	}



	private static final Logger logger = LoggerFactory.getLogger(InteressatServiceImpl.class);



	@Override
	public List<UnitatOrganitzativaDto> findUnitatsOrganitzativesByEntitat(String entitatCodi) {
		return cacheHelper.findUnitatsOrganitzativesPerEntitat(entitatCodi).toDadesList();
	}

}
