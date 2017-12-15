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
import es.caib.ripea.core.api.dto.NotificacioEntregaPostalViaTipusEnumDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.service.ExpedientInteressatService;
import es.caib.ripea.core.entity.DocumentEntity;
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
import es.caib.ripea.core.helper.HibernateHelper;
import es.caib.ripea.core.helper.PermisosHelper;
import es.caib.ripea.core.helper.PluginHelper;
import es.caib.ripea.core.repository.EntitatRepository;
import es.caib.ripea.core.repository.ExpedientRepository;
import es.caib.ripea.core.repository.InteressatRepository;
import es.caib.ripea.plugin.notificacio.NotificacioEntregaPostalViaTipusEnum;

/**
 * Implementació dels mètodes per a gestionar la versió de l'aplicació.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class ExpedientInteressatServiceImpl implements ExpedientInteressatService {

	@Resource
	private EntitatRepository entitatRepository;
	@Resource
	private ExpedientRepository expedientRepository;
	@Resource
	private InteressatRepository interessatRepository;

	@Resource
	private CacheHelper cacheHelper;
	@Resource
	private PluginHelper pluginHelper;
	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private ContingutHelper contingutHelper;
	@Resource
	private ContingutLogHelper contingutLogHelper;
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
		return create(entitatId, expedientId, null, interessat);
	}

	@Transactional
	@Override
	public InteressatDto create(
			Long entitatId,
			Long expedientId,
			Long interessatId,
			InteressatDto interessat) {
		if (interessatId != null) {
			logger.debug("Creant nou representant ("
					+ "entitatId=" + entitatId + ", "
					+ "expedientId=" + expedientId + ", "
					+ "interessatId=" + interessatId + ", "
					+ "interessat=" + interessat + ")");
		} else {
			logger.debug("Creant nou interessat ("
					+ "entitatId=" + entitatId + ", "
					+ "expedientId=" + expedientId + ", "
					+ "interessat=" + interessat + ")");
		}
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				expedientId);
		InteressatEntity pare = null;
		if (interessatId != null) {
			pare = interessatRepository.findOne(interessatId);
			if (pare == null) {
				throw new NotFoundException(
						interessatId,
						InteressatEntity.class);
			}
		}
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				expedient);
		// Comprova l'accés al path del contenidor pare
		contingutHelper.comprovarPermisosPathContingut(
				expedient,
				true,
				false,
				false,
				true);
		// Comprova el permís de modificació de l'expedient
		contingutHelper.comprovarPermisosContingut(
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
					interessatPersonaFisicaDto.getEmail(),
					interessatPersonaFisicaDto.getTelefon(),
					interessatPersonaFisicaDto.getObservacions(),
					interessatPersonaFisicaDto.getPreferenciaIdioma(),
					interessatPersonaFisicaDto.getNotificacioAutoritzat(),
					expedient,
					null).
					adresa(
							interessatPersonaFisicaDto.getDomiciliTipusEnum(),
							interessatPersonaFisicaDto.getDomiciliApartatCorreus(),
							interessatPersonaFisicaDto.getDomiciliBloc(),
							interessatPersonaFisicaDto.getDomiciliCie(),
							interessatPersonaFisicaDto.getDomiciliCodiPostal(),
							interessatPersonaFisicaDto.getDomiciliComplement(),
							interessatPersonaFisicaDto.getDomiciliEscala(),
							interessatPersonaFisicaDto.getDomiciliLinea1(),
							interessatPersonaFisicaDto.getDomiciliLinea2(),
							interessatPersonaFisicaDto.getDomiciliMunicipiCodiIne(),
							interessatPersonaFisicaDto.getDomiciliNumeracioTipus(),
							interessatPersonaFisicaDto.getDomiciliNumeracioNumero(),
							interessatPersonaFisicaDto.getDomiciliPaisCodiIso(),
							interessatPersonaFisicaDto.getDomiciliPlanta(),
							interessatPersonaFisicaDto.getDomiciliPoblacio(),
							interessatPersonaFisicaDto.getDomiciliPorta(),
							interessatPersonaFisicaDto.getDomiciliPortal(),
							interessatPersonaFisicaDto.getDomiciliProvinciaCodi(),
							interessatPersonaFisicaDto.getDomiciliNumeracioPuntKm(),
							interessatPersonaFisicaDto.getDomiciliViaNom(),
							NotificacioEntregaPostalViaTipusEnum.valueOf(
									interessatPersonaFisicaDto.getDomiciliViaTipus().name())).
					build();
		} else if (interessat.isPersonaJuridica()) {
			InteressatPersonaJuridicaDto interessatPersonaJuridicaDto = (InteressatPersonaJuridicaDto)interessat;
			interessatEntity = InteressatPersonaJuridicaEntity.getBuilder(
					interessatPersonaJuridicaDto.getRaoSocial(),
					interessatPersonaJuridicaDto.getDocumentTipus(),
					interessatPersonaJuridicaDto.getDocumentNum(),
					interessatPersonaJuridicaDto.getEmail(),
					interessatPersonaJuridicaDto.getTelefon(),
					interessatPersonaJuridicaDto.getObservacions(),
					interessatPersonaJuridicaDto.getPreferenciaIdioma(),
					interessatPersonaJuridicaDto.getNotificacioAutoritzat(),
					expedient,
					null).
					adresa(
							interessatPersonaJuridicaDto.getDomiciliTipusEnum(),
							interessatPersonaJuridicaDto.getDomiciliApartatCorreus(),
							interessatPersonaJuridicaDto.getDomiciliBloc(),
							interessatPersonaJuridicaDto.getDomiciliCie(),
							interessatPersonaJuridicaDto.getDomiciliCodiPostal(),
							interessatPersonaJuridicaDto.getDomiciliComplement(),
							interessatPersonaJuridicaDto.getDomiciliEscala(),
							interessatPersonaJuridicaDto.getDomiciliLinea1(),
							interessatPersonaJuridicaDto.getDomiciliLinea2(),
							interessatPersonaJuridicaDto.getDomiciliMunicipiCodiIne(),
							interessatPersonaJuridicaDto.getDomiciliNumeracioTipus(),
							interessatPersonaJuridicaDto.getDomiciliNumeracioNumero(),
							interessatPersonaJuridicaDto.getDomiciliPaisCodiIso(),
							interessatPersonaJuridicaDto.getDomiciliPlanta(),
							interessatPersonaJuridicaDto.getDomiciliPoblacio(),
							interessatPersonaJuridicaDto.getDomiciliPorta(),
							interessatPersonaJuridicaDto.getDomiciliPortal(),
							interessatPersonaJuridicaDto.getDomiciliProvinciaCodi(),
							interessatPersonaJuridicaDto.getDomiciliNumeracioPuntKm(),
							interessatPersonaJuridicaDto.getDomiciliViaNom(),
							NotificacioEntregaPostalViaTipusEnum.valueOf(
									interessatPersonaJuridicaDto.getDomiciliViaTipus().name())).
					build();
		} else {
			InteressatAdministracioDto interessatAdministracioDto = (InteressatAdministracioDto)interessat;
			UnitatOrganitzativaDto unitat = findUnitatsOrganitzativesByCodi(interessatAdministracioDto.getOrganCodi());
			interessatEntity = InteressatAdministracioEntity.getBuilder(
					unitat.getCodi(),
					unitat.getDenominacio(),
					interessatAdministracioDto.getDocumentTipus(),
					interessatAdministracioDto.getDocumentNum(),
					interessatAdministracioDto.getEmail(),
					interessatAdministracioDto.getTelefon(),
					interessatAdministracioDto.getObservacions(),
					interessatAdministracioDto.getPreferenciaIdioma(),
					interessatAdministracioDto.getNotificacioAutoritzat(),
					expedient,
					null).
					adresa(
							interessatAdministracioDto.getDomiciliTipusEnum(),
							interessatAdministracioDto.getDomiciliApartatCorreus(),
							interessatAdministracioDto.getDomiciliBloc(),
							interessatAdministracioDto.getDomiciliCie(),
							interessatAdministracioDto.getDomiciliCodiPostal(),
							interessatAdministracioDto.getDomiciliComplement(),
							interessatAdministracioDto.getDomiciliEscala(),
							interessatAdministracioDto.getDomiciliLinea1(),
							interessatAdministracioDto.getDomiciliLinea2(),
							interessatAdministracioDto.getDomiciliMunicipiCodiIne(),
							interessatAdministracioDto.getDomiciliNumeracioTipus(),
							interessatAdministracioDto.getDomiciliNumeracioNumero(),
							interessatAdministracioDto.getDomiciliPaisCodiIso(),
							interessatAdministracioDto.getDomiciliPlanta(),
							interessatAdministracioDto.getDomiciliPoblacio(),
							interessatAdministracioDto.getDomiciliPorta(),
							interessatAdministracioDto.getDomiciliPortal(),
							interessatAdministracioDto.getDomiciliProvinciaCodi(),
							interessatAdministracioDto.getDomiciliNumeracioPuntKm(),
							interessatAdministracioDto.getDomiciliViaNom(),
							NotificacioEntregaPostalViaTipusEnum.valueOf(
									interessatAdministracioDto.getDomiciliViaTipus().name())).
					build();
		}
		if (pare != null)
			interessatEntity.setEsRepresentant(true);
		interessatEntity = interessatRepository.save(interessatEntity);
		if (pare != null)
			pare.setRepresentant(interessatEntity);
		expedient.addInteressat(interessatEntity);
		// Registra al log la creació de l'interessat
		contingutLogHelper.log(
				expedient,
				LogTipusEnumDto.MODIFICACIO,
				interessatEntity,
				LogObjecteTipusEnumDto.INTERESSAT,
				LogTipusEnumDto.CREACIO,
				interessatEntity.getIdentificador(),
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
		return update(entitatId, expedientId, null, interessat);
	}

	@Transactional
	@Override
	public InteressatDto update(
			Long entitatId,
			Long expedientId,
			Long interessatId,
			InteressatDto interessat) {
		if (interessatId != null) {
			logger.debug("Modificant un representant ("
					+ "entitatId=" + entitatId + ", "
					+ "expedientId=" + expedientId + ", "
					+ "interessatId=" + interessatId + ", "
					+ "interessat=" + interessat + ")");
		} else {
			logger.debug("Modificant un interessat ("
					+ "entitatId=" + entitatId + ", "
					+ "expedientId=" + expedientId + ", "
					+ "interessat=" + interessat + ")");
		}
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				expedientId);
		InteressatEntity pare = null;
		if (interessatId != null) {
			pare = interessatRepository.findOne(interessatId);
			if (pare == null || 
				pare.getRepresentant() == null || 
				!pare.getRepresentant().getId().equals(interessat.getId())) {
				throw new NotFoundException(
						interessatId,
						InteressatEntity.class);
			}
		}
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				expedient);
		// Comprova l'accés al path del contenidor pare
		contingutHelper.comprovarPermisosPathContingut(
				expedient,
				true,
				false,
				false,
				true);
		// Comprova el permís de modificació de l'expedient
		contingutHelper.comprovarPermisosContingut(
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
					interessatPersonaFisicaDto.getEmail(),
					interessatPersonaFisicaDto.getTelefon(),
					interessatPersonaFisicaDto.getObservacions(),
					interessatPersonaFisicaDto.getPreferenciaIdioma(),
					interessatPersonaFisicaDto.getNotificacioAutoritzat());
			interessatEntity.updateAdresa(
					interessatPersonaFisicaDto.getDomiciliTipusEnum(),
					interessatPersonaFisicaDto.getDomiciliApartatCorreus(),
					interessatPersonaFisicaDto.getDomiciliBloc(),
					interessatPersonaFisicaDto.getDomiciliCie(),
					interessatPersonaFisicaDto.getDomiciliCodiPostal(),
					interessatPersonaFisicaDto.getDomiciliComplement(),
					interessatPersonaFisicaDto.getDomiciliEscala(),
					interessatPersonaFisicaDto.getDomiciliLinea1(),
					interessatPersonaFisicaDto.getDomiciliLinea2(),
					interessatPersonaFisicaDto.getDomiciliMunicipiCodiIne(),
					interessatPersonaFisicaDto.getDomiciliNumeracioTipus(),
					interessatPersonaFisicaDto.getDomiciliNumeracioNumero(),
					interessatPersonaFisicaDto.getDomiciliPaisCodiIso(),
					interessatPersonaFisicaDto.getDomiciliPlanta(),
					interessatPersonaFisicaDto.getDomiciliPoblacio(),
					interessatPersonaFisicaDto.getDomiciliPorta(),
					interessatPersonaFisicaDto.getDomiciliPortal(),
					interessatPersonaFisicaDto.getDomiciliProvinciaCodi(),
					interessatPersonaFisicaDto.getDomiciliNumeracioPuntKm(),
					interessatPersonaFisicaDto.getDomiciliViaNom(),
					NotificacioEntregaPostalViaTipusEnum.valueOf(
							interessatPersonaFisicaDto.getDomiciliViaTipus().name()));
		} else if (interessat.isPersonaJuridica()) {
			InteressatPersonaJuridicaDto interessatPersonaJuridicaDto = (InteressatPersonaJuridicaDto)interessat;
			interessatEntity = interessatRepository.findPersonaJuridicaById(interessat.getId());
			((InteressatPersonaJuridicaEntity)interessatEntity).update(
					interessatPersonaJuridicaDto.getRaoSocial(),
					interessatPersonaJuridicaDto.getDocumentTipus(),
					interessatPersonaJuridicaDto.getDocumentNum(),
					interessatPersonaJuridicaDto.getEmail(),
					interessatPersonaJuridicaDto.getTelefon(),
					interessatPersonaJuridicaDto.getObservacions(),
					interessatPersonaJuridicaDto.getPreferenciaIdioma(),
					interessatPersonaJuridicaDto.getNotificacioAutoritzat());
			interessatEntity.updateAdresa(
					interessatPersonaJuridicaDto.getDomiciliTipusEnum(),
					interessatPersonaJuridicaDto.getDomiciliApartatCorreus(),
					interessatPersonaJuridicaDto.getDomiciliBloc(),
					interessatPersonaJuridicaDto.getDomiciliCie(),
					interessatPersonaJuridicaDto.getDomiciliCodiPostal(),
					interessatPersonaJuridicaDto.getDomiciliComplement(),
					interessatPersonaJuridicaDto.getDomiciliEscala(),
					interessatPersonaJuridicaDto.getDomiciliLinea1(),
					interessatPersonaJuridicaDto.getDomiciliLinea2(),
					interessatPersonaJuridicaDto.getDomiciliMunicipiCodiIne(),
					interessatPersonaJuridicaDto.getDomiciliNumeracioTipus(),
					interessatPersonaJuridicaDto.getDomiciliNumeracioNumero(),
					interessatPersonaJuridicaDto.getDomiciliPaisCodiIso(),
					interessatPersonaJuridicaDto.getDomiciliPlanta(),
					interessatPersonaJuridicaDto.getDomiciliPoblacio(),
					interessatPersonaJuridicaDto.getDomiciliPorta(),
					interessatPersonaJuridicaDto.getDomiciliPortal(),
					interessatPersonaJuridicaDto.getDomiciliProvinciaCodi(),
					interessatPersonaJuridicaDto.getDomiciliNumeracioPuntKm(),
					interessatPersonaJuridicaDto.getDomiciliViaNom(),
					NotificacioEntregaPostalViaTipusEnum.valueOf(
							interessatPersonaJuridicaDto.getDomiciliViaTipus().name()));
		} else {
			InteressatAdministracioDto interessatAdministracioDto = (InteressatAdministracioDto)interessat;
			interessatEntity = interessatRepository.findAdministracioById(interessat.getId());
			UnitatOrganitzativaDto unitat = findUnitatsOrganitzativesByCodi(interessatAdministracioDto.getOrganCodi());
			((InteressatAdministracioEntity)interessatEntity).update(
					unitat.getCodi(),
					unitat.getDenominacio(),
					interessatAdministracioDto.getDocumentTipus(),
					interessatAdministracioDto.getDocumentNum(),
					interessatAdministracioDto.getEmail(),
					interessatAdministracioDto.getTelefon(),
					interessatAdministracioDto.getObservacions(),
					interessatAdministracioDto.getPreferenciaIdioma(),
					interessatAdministracioDto.getNotificacioAutoritzat());
			interessatEntity.updateAdresa(
					interessatAdministracioDto.getDomiciliTipusEnum(),
					interessatAdministracioDto.getDomiciliApartatCorreus(),
					interessatAdministracioDto.getDomiciliBloc(),
					interessatAdministracioDto.getDomiciliCie(),
					interessatAdministracioDto.getDomiciliCodiPostal(),
					interessatAdministracioDto.getDomiciliComplement(),
					interessatAdministracioDto.getDomiciliEscala(),
					interessatAdministracioDto.getDomiciliLinea1(),
					interessatAdministracioDto.getDomiciliLinea2(),
					interessatAdministracioDto.getDomiciliMunicipiCodiIne(),
					interessatAdministracioDto.getDomiciliNumeracioTipus(),
					interessatAdministracioDto.getDomiciliNumeracioNumero(),
					interessatAdministracioDto.getDomiciliPaisCodiIso(),
					interessatAdministracioDto.getDomiciliPlanta(),
					interessatAdministracioDto.getDomiciliPoblacio(),
					interessatAdministracioDto.getDomiciliPorta(),
					interessatAdministracioDto.getDomiciliPortal(),
					interessatAdministracioDto.getDomiciliProvinciaCodi(),
					interessatAdministracioDto.getDomiciliNumeracioPuntKm(),
					interessatAdministracioDto.getDomiciliViaNom(),
					NotificacioEntregaPostalViaTipusEnum.valueOf(
							interessatAdministracioDto.getDomiciliViaTipus().name()));
		}
		interessatEntity = interessatRepository.save(interessatEntity);
		// Registra al log la modificació de l'interessat
		contingutLogHelper.log(
				expedient,
				LogTipusEnumDto.MODIFICACIO,
				interessatEntity,
				LogObjecteTipusEnumDto.INTERESSAT,
				LogTipusEnumDto.MODIFICACIO,
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
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				expedient);
		// Comprova l'accés al path del contenidor pare
		contingutHelper.comprovarPermisosPathContingut(
				expedient,
				true,
				false,
				false,
				true);
		// Comprova el permís de modificació de l'expedient
		contingutHelper.comprovarPermisosContingut(
				expedient,
				false,
				true,
				false);
		InteressatEntity interessat = interessatRepository.findOne(interessatId);
		if (interessat != null) {
			interessatRepository.delete(interessat);
			//expedient.deleteInteressat(interessat);
			// Registra al log la baixa de l'interessat
			contingutLogHelper.log(
					expedient,
					LogTipusEnumDto.MODIFICACIO,
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

	@Transactional
	@Override
	public void delete(
			Long entitatId,
			Long expedientId,
			Long interessatId,
			Long representantId) {
		logger.debug("Esborrant interessat de l'expedient  ("
				+ "entitatId=" + entitatId + ", "
				+ "expedientId=" + expedientId + ", "
				+ "interessatId=" + interessatId + ", "
				+ "representantId=" + representantId + ")");
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
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				expedient);
		// Comprova l'accés al path del contenidor pare
		contingutHelper.comprovarPermisosPathContingut(
				expedient,
				true,
				false,
				false,
				true);
		// Comprova el permís de modificació de l'expedient
		contingutHelper.comprovarPermisosContingut(
				expedient,
				false,
				true,
				false);
		InteressatEntity interessat = interessatRepository.findOne(interessatId);
		if (interessat != null) {
			if (interessat.getRepresentant() != null && 
				interessat.getRepresentant().getId().equals(representantId)) {
				InteressatEntity representant = interessatRepository.findOne(representantId);
			
				interessat.setRepresentant(null);
				interessatRepository.delete(representant);
				//expedient.deleteInteressat(interessat);
				// Registra al log la baixa de l'interessat
				contingutLogHelper.log(
						expedient,
						LogTipusEnumDto.MODIFICACIO,
						interessat,
						LogObjecteTipusEnumDto.INTERESSAT,
						LogTipusEnumDto.ELIMINACIO,
						null,
						null,
						false,
						false);
			} else {
				logger.error("No s'ha trobat el representant de l'interessat ("
						+ "expedientId=" + expedientId + ", "
						+ "interessatId=" + interessatId + ", "
						+ "representantId=" + representantId + ")");
				throw new ValidationException(
						representantId,
						InteressatEntity.class,
						"No s'ha trobat el representant de l'interessat (interessatId=" + interessatId + ")");
			}
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

	@Transactional(readOnly = true)
	@Override
	public InteressatDto findById(Long id) {
		logger.debug("Consulta de l'interessat ("
				+ "id=" + id + ")");
		InteressatEntity interessat = entityComprovarHelper.comprovarInteressat(
				null,
				id);
		return conversioTipusHelper.convertir(
				interessat,
				InteressatDto.class);
	}

	@Transactional(readOnly = true)
	@Override
	public InteressatDto findRepresentantById(
			Long interessatId,
			Long id) {
		logger.debug("Consulta de l'interessat ("
				+ "id=" + id + ")");
		InteressatEntity interessat = entityComprovarHelper.comprovarInteressat(
				null,
				interessatId);
		if (interessat.getRepresentantId() == null || !interessat.getRepresentantId().equals(id)) {
			throw new ValidationException(
					id,
					InteressatEntity.class,
					"El representant especificat (id=" + id + ") no pertany a l'interessat (id=" + interessatId + ")");
		}
		InteressatEntity representant = HibernateHelper.deproxy(interessat.getRepresentant());
		return conversioTipusHelper.convertir(
				representant,
				InteressatDto.class);
	}

	@Transactional(readOnly = true)
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
		List<InteressatEntity> interessats = interessatRepository.findByExpedientAndNotRepresentant(
				expedient);
//		List<InteressatDto> resposta = conversioTipusHelper.convertirList(
//				interessats, 
//				InteressatDto.class);
		List<InteressatDto> resposta = new ArrayList<InteressatDto>();
		for (InteressatEntity interessat: interessats) {
			if (interessat instanceof InteressatPersonaFisicaEntity)
				resposta.add(conversioTipusHelper.convertir(
						interessat,
						InteressatPersonaFisicaDto.class));
			else if (interessat instanceof InteressatPersonaJuridicaEntity)
				resposta.add(conversioTipusHelper.convertir(
						interessat,
						InteressatPersonaJuridicaDto.class));
			else if (interessat instanceof InteressatAdministracioEntity)
				resposta.add(conversioTipusHelper.convertir(
						interessat,
						InteressatAdministracioDto.class));
		}
		return resposta;
	}

	@Transactional(readOnly = true)
	@Override
	public long countByExpedient(
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
		return interessatRepository.countByExpedient(
				expedient);
	}

	@Transactional(readOnly = true)
	@Override
	public List<InteressatDto> findAmbDocumentPerNotificacio(
			Long entitatId,
			Long documentId) {
		logger.debug("Consulta interessats del document per notificacions ("
				+ "entitatId=" + entitatId + ", "
				+ "documentId=" + documentId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		DocumentEntity document = entityComprovarHelper.comprovarDocument(
				entitat,
				null,
				documentId,
				false,
				false,
				false);
		ExpedientEntity expedient = contingutHelper.getExpedientSuperior(
				document,
				false,
				false,
				true);
		if (expedient == null) {
			throw new ValidationException(
					documentId,
					DocumentEntity.class,
					"El document no pertany a cap expedient");
		}
		List<InteressatEntity> interessats = interessatRepository.findByExpedientPerNotificacions(
				expedient);
		List<InteressatDto> resposta = new ArrayList<InteressatDto>();
		for (InteressatEntity interessat: interessats) {
			if (interessat instanceof InteressatPersonaFisicaEntity)
				resposta.add(conversioTipusHelper.convertir(
						interessat,
						InteressatPersonaFisicaDto.class));
			else if (interessat instanceof InteressatPersonaJuridicaEntity)
				resposta.add(conversioTipusHelper.convertir(
						interessat,
						InteressatAdministracioDto.class));
		}
		return resposta;
	}

	@Transactional(readOnly = true)
	@Override
	public List<InteressatPersonaFisicaDto> findByFiltrePersonaFisica(
			String documentNum,
			String nom,
			String llinatge1,
			String llinatge2) {
		logger.debug("Consulta interessats de tipus ciutadà ("
				+ "nom=" + nom + ", "
				+ "documentNum=" + documentNum + ", "
				+ "llinatge1=" + llinatge1 + ", "
				+ "llinatge2=" + llinatge2 + ")");
		return conversioTipusHelper.convertirList(
				interessatRepository.findByFiltrePersonaFisica(
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
			String documentNum,
			String raoSocial) {
		logger.debug("Consulta interessats de tipus ciutadà ("
				+ "raoSocial=" + raoSocial + ", "
				+ "documentNum=" + documentNum + ")");
		return conversioTipusHelper.convertirList(
				interessatRepository.findByFiltrePersonaJuridica(
						documentNum == null,
						documentNum,
						raoSocial == null,
						raoSocial),
				InteressatPersonaJuridicaDto.class);
	}

	@Transactional(readOnly = true)
	@Override
	public List<InteressatAdministracioDto> findByFiltreAdministracio(
			String organCodi) {
		logger.debug("Consulta interessats de tipus administració ("
				+ "organCodi=" + organCodi + ")");
		List<InteressatAdministracioEntity> administracions = interessatRepository.findByFiltreAdministracio(
				organCodi == null,
				organCodi);
		return conversioTipusHelper.convertirList(
				administracions,
				InteressatAdministracioDto.class);
	}

	@Override
	public List<UnitatOrganitzativaDto> findUnitatsOrganitzativesByEntitat(String entitatCodi) {
		return cacheHelper.findUnitatsOrganitzativesPerEntitat(entitatCodi).toDadesList();
	}

	@Override
	public UnitatOrganitzativaDto findUnitatsOrganitzativesByCodi(String codi) {
		try {
			return cacheHelper.findUnitatOrganitzativaPerCodi(codi);
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<UnitatOrganitzativaDto> findUnitatsOrganitzativesByFiltre(
			String codiDir3, 
			String denominacio,
			String nivellAdm, 
			String comunitat, 
			String provincia, 
			String localitat, 
			Boolean arrel) {
		return pluginHelper.unitatsOrganitzativesFindByFiltre(
				codiDir3, 
				denominacio,
				nivellAdm, 
				comunitat, 
				provincia, 
				localitat, 
				arrel);
	}



	private static final Logger logger = LoggerFactory.getLogger(ExpedientInteressatServiceImpl.class);

}
