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
import es.caib.ripea.core.api.dto.InteressatCiutadaDto;
import es.caib.ripea.core.api.dto.InteressatDto;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.ExpedientNotFoundException;
import es.caib.ripea.core.api.exception.InteressatNotFoundException;
import es.caib.ripea.core.api.service.InteressatService;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.InteressatAdministracioEntity;
import es.caib.ripea.core.entity.InteressatCiutadaEntity;
import es.caib.ripea.core.entity.InteressatEntity;
import es.caib.ripea.core.entity.LogObjecteTipusEnum;
import es.caib.ripea.core.entity.LogTipusEnum;
import es.caib.ripea.core.helper.ContenidorHelper;
import es.caib.ripea.core.helper.ContenidorLogHelper;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.PermisosComprovacioHelper;
import es.caib.ripea.core.helper.PermisosHelper;
import es.caib.ripea.core.repository.ContenidorLogRepository;
import es.caib.ripea.core.repository.ContenidorRepository;
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
	private ContenidorRepository contenidorRepository;
	@Resource
	private ExpedientRepository expedientRepository;
	@Resource
	private InteressatRepository interessatRepository;
	@Resource
	private ContenidorLogRepository contenidorLogRepository;

	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private ContenidorHelper contenidorHelper;
	@Resource
	private ContenidorLogHelper contenidorLogHelper;
	@Resource
	private PermisosHelper permisosHelper;
	@Resource
	private PermisosComprovacioHelper permisosComprovacioHelper;



	@Transactional
	@Override
	public InteressatDto create(
			Long entitatId,
			Long expedientId,
			InteressatDto interessat) throws EntitatNotFoundException, ExpedientNotFoundException {
		logger.debug("Creant nou interessat ("
				+ "entitatId=" + entitatId + ", "
				+ "expedientId=" + expedientId + ", "
				+ "interessat=" + interessat + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = comprovarExpedient(entitat, expedientId);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				expedient);
		// Comprova l'accés al path del contenidor pare
		contenidorHelper.comprovarPermisosPathContenidor(
				expedient,
				true,
				false,
				false,
				true);
		// Comprova el permís de modificació de l'expedient
		contenidorHelper.comprovarPermisosContenidor(
				expedient,
				false,
				true,
				false);
		InteressatEntity interessatEntity = null;
		if (interessat instanceof InteressatCiutadaDto) {
			InteressatCiutadaDto interessatCiutadaDto = (InteressatCiutadaDto)interessat;
			interessatEntity = InteressatCiutadaEntity.getBuilder(
					interessatCiutadaDto.getNom(),
					interessatCiutadaDto.getLlinatges(),
					interessatCiutadaDto.getNif(),
					entitat).build();
		} else {
			InteressatAdministracioDto interessatAdministracioDto = (InteressatAdministracioDto)interessat;
			interessatEntity = InteressatAdministracioEntity.getBuilder(
					interessatAdministracioDto.getNom(),
					interessatAdministracioDto.getIdentificador(),
					entitat).build();
		}
		interessatEntity = interessatRepository.save(interessatEntity);
		expedient.addInteressat(interessatEntity);
		// Registra al log la creació de l'interessat
		contenidorLogHelper.log(
				expedient,
				LogTipusEnum.MODIFICACIO,
				null,
				null,
				interessatEntity,
				LogObjecteTipusEnum.INTERESSAT,
				LogTipusEnum.CREACIO,
				null,
				null,
				false,
				false);
		if (interessat instanceof InteressatCiutadaDto) {
			return conversioTipusHelper.convertir(
					interessatRepository.save(interessatEntity),
					InteressatCiutadaDto.class);
		} else {
			return conversioTipusHelper.convertir(
					interessatRepository.save(interessatEntity),
					InteressatAdministracioDto.class);
		}
	}

	@Transactional
	@Override
	public void addToExpedient(
			Long entitatId,
			Long expedientId,
			Long id) throws EntitatNotFoundException, ExpedientNotFoundException, InteressatNotFoundException {
		logger.debug("Afegint interessat a l'expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "expedientId=" + expedientId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = comprovarExpedient(entitat, expedientId);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				expedient);
		// Comprova l'accés al path del contenidor pare
		contenidorHelper.comprovarPermisosPathContenidor(
				expedient,
				true,
				false,
				false,
				true);
		// Comprova el permís de modificació de l'expedient
		contenidorHelper.comprovarPermisosContenidor(
				expedient,
				false,
				true,
				false);
		InteressatEntity interessat = comprovarInteressat(
				entitat,
				id);
		expedient.addInteressat(interessat);
		// Registra al log la creació de l'interessat
		contenidorLogHelper.log(
				expedient,
				LogTipusEnum.MODIFICACIO,
				null,
				null,
				interessat,
				LogObjecteTipusEnum.INTERESSAT,
				LogTipusEnum.CREACIO,
				null,
				null,
				false,
				false);
	}
	@Transactional
	@Override
	public void removeFromExpedient(
			Long entitatId,
			Long expedientId,
			Long id) throws EntitatNotFoundException, ExpedientNotFoundException, InteressatNotFoundException {
		logger.debug("Esborrant interessat de l'expedient  ("
				+ "entitatId=" + entitatId + ", "
				+ "expedientId=" + expedientId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = comprovarExpedient(entitat, expedientId);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				expedient);
		// Comprova l'accés al path del contenidor pare
		contenidorHelper.comprovarPermisosPathContenidor(
				expedient,
				true,
				false,
				false,
				true);
		// Comprova el permís de modificació de l'expedient
		contenidorHelper.comprovarPermisosContenidor(
				expedient,
				false,
				true,
				false);
		InteressatEntity interessat = comprovarInteressat(
				entitat,
				id);
		List<InteressatEntity> interessats = interessatRepository.findByEntitatAndExpedient(
				entitat,
				expedient);
		boolean trobat = false;
		for (InteressatEntity i: interessats) {
			if (i.getId().equals(interessat.getId())) {
				trobat = true;
				break;
			}
		}
		if (trobat) {
			expedient.deleteInteressat(interessat);
			// Registra al log la baixa de l'interessat
			contenidorLogHelper.log(
					expedient,
					LogTipusEnum.MODIFICACIO,
					null,
					null,
					interessat,
					LogObjecteTipusEnum.INTERESSAT,
					LogTipusEnum.ELIMINACIO,
					null,
					null,
					false,
					false);
		} else {
			logger.error("No s'ha trobat l'interessat a l'expedient ("
					+ "expedientId=" + expedientId + ", "
					+ "id=" + id + ")");
			throw new InteressatNotFoundException();
		}
	}

	@Transactional(readOnly=true)
	@Override
	public InteressatDto findById(
			Long entitatId,
			Long id) throws EntitatNotFoundException, InteressatNotFoundException {
		logger.debug("Consulta de l'interessat ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		InteressatEntity interessat = comprovarInteressat(
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
			Long expedientId) throws EntitatNotFoundException, ExpedientNotFoundException {
		logger.debug("Consulta interessats de l'expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "expedientId=" + expedientId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = comprovarExpedient(entitat, expedientId);
		List<InteressatEntity> interessats = interessatRepository.findByEntitatAndExpedient(
				entitat,
				expedient);
		List<InteressatDto> resposta = new ArrayList<InteressatDto>();
		for (InteressatEntity interessat: interessats) {
			if (interessat instanceof InteressatCiutadaEntity)
				resposta.add(conversioTipusHelper.convertir(
						interessat,
						InteressatCiutadaDto.class));
			else if (interessat instanceof InteressatAdministracioEntity)
				resposta.add(conversioTipusHelper.convertir(
						interessat,
						InteressatAdministracioDto.class));
		}
		return resposta;
	}

	@Transactional(readOnly = true)
	@Override
	public List<InteressatCiutadaDto> findByFiltreCiutada(
			Long entitatId,
			String nom,
			String nif,
			String llinatges) throws EntitatNotFoundException {
		logger.debug("Consulta interessats de tipus ciutadà ("
				+ "entitatId=" + entitatId + ", "
				+ "nom=" + nom + ", "
				+ "nif=" + nif + ", "
				+ "llinatges=" + llinatges + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		return conversioTipusHelper.convertirList(
				interessatRepository.findByFiltreCiutada(
						entitat,
						nom == null,
						nom,
						nif == null,
						nif,
						llinatges == null,
						llinatges),
				InteressatCiutadaDto.class);
	}

	@Transactional(readOnly = true)
	@Override
	public List<InteressatAdministracioDto> findByFiltreAdministracio(
			Long entitatId,
			String nom,
			String identificador) throws EntitatNotFoundException {
		logger.debug("Consulta interessats de tipus administració ("
				+ "entitatId=" + entitatId + ", "
				+ "nom=" + nom + ", "
				+ "identificador=" + identificador + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		List<InteressatAdministracioEntity> administracions = interessatRepository.findByFiltreAdministracio(
				entitat,
				nom == null,
				nom,
				identificador == null,
				identificador);
		return conversioTipusHelper.convertirList(
				administracions,
				InteressatAdministracioDto.class);
	}



	private ExpedientEntity comprovarExpedient(
			EntitatEntity entitat,
			Long id) throws ExpedientNotFoundException {
		ExpedientEntity expedient = expedientRepository.findOne(id);
		if (expedient == null) {
			logger.error("No s'ha trobat l'expedient (id=" + id + ")");
			throw new ExpedientNotFoundException();
		}
		if (!entitat.getId().equals(expedient.getEntitat().getId())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat de l'expedient ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + expedient.getEntitat().getId() + ")");
			throw new ExpedientNotFoundException();
		}
		return expedient;
	}

	private InteressatEntity comprovarInteressat(
			EntitatEntity entitat,
			Long id) throws InteressatNotFoundException {
		InteressatEntity interessat = interessatRepository.findOne(id);
		if (interessat == null) {
			logger.error("No s'ha trobat l'interessat (id=" + id + ")");
			throw new InteressatNotFoundException();
		}
		if (!entitat.getId().equals(interessat.getEntitat().getId())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat de l'interessat ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + interessat.getEntitat().getId() + ")");
			throw new InteressatNotFoundException();
		}
		return interessat;
	}

	private static final Logger logger = LoggerFactory.getLogger(InteressatServiceImpl.class);

}
