/**
 * 
 */
package es.caib.ripea.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.DocumentEnviamentDto;
import es.caib.ripea.core.api.dto.DocumentEnviamentEstatEnumDto;
import es.caib.ripea.core.api.dto.DocumentEstatEnumDto;
import es.caib.ripea.core.api.dto.DocumentNotificacioDto;
import es.caib.ripea.core.api.dto.DocumentNotificacioTipusEnumDto;
import es.caib.ripea.core.api.dto.DocumentPublicacioDto;
import es.caib.ripea.core.api.dto.InteressatIdiomaEnumDto;
import es.caib.ripea.core.api.dto.LogObjecteTipusEnumDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.service.ExpedientEnviamentService;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.DocumentEnviamentEntity;
import es.caib.ripea.core.entity.DocumentNotificacioEntity;
import es.caib.ripea.core.entity.DocumentPublicacioEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.InteressatEntity;
import es.caib.ripea.core.entity.InteressatPersonaFisicaEntity;
import es.caib.ripea.core.entity.InteressatPersonaJuridicaEntity;
import es.caib.ripea.core.helper.ContingutHelper;
import es.caib.ripea.core.helper.ContingutLogHelper;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.EntityComprovarHelper;
import es.caib.ripea.core.helper.ExpedientHelper;
import es.caib.ripea.core.repository.DocumentEnviamentRepository;
import es.caib.ripea.core.repository.DocumentNotificacioRepository;
import es.caib.ripea.core.repository.DocumentPublicacioRepository;

/**
 * Implementació dels mètodes per a gestionar expedients.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class ExpedientEnviamentServiceImpl implements ExpedientEnviamentService {

	@Resource
	private DocumentEnviamentRepository documentEnviamentRepository;
	@Resource
	private DocumentNotificacioRepository documentNotificacioRepository;
	@Resource
	private DocumentPublicacioRepository documentPublicacioRepository;

	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private ContingutHelper contingutHelper;
	@Resource
	private ExpedientHelper expedientHelper;
	@Resource
	private ContingutLogHelper contingutLogHelper;
	@Resource
	private EntityComprovarHelper entityComprovarHelper;



	@Transactional
	@Override
	public DocumentNotificacioDto notificacioCreate(
			Long entitatId,
			Long documentId,
			Long interessatId,
			DocumentNotificacioDto notificacio) {
		logger.debug("Creant una notificació de l'expedient (" +
				"entitatId=" + entitatId + ", " +
				"documentId=" + documentId + ", " +
				"interessatId=" + interessatId + ", " +
				"notificacio=" + notificacio + ")");
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
		if (!DocumentEstatEnumDto.CUSTODIAT.equals(document.getEstat())) {
			throw new ValidationException(
					documentId,
					DocumentEntity.class,
					"El document no està custodiat");
		}
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
		if (	DocumentNotificacioTipusEnumDto.ELECTRONICA.equals(notificacio.getTipus()) &&
				!expedient.getMetaExpedient().isNotificacioActiva()) {
			throw new ValidationException(
					documentId,
					DocumentEntity.class,
					"El document pertany a un expedient que no te activades les notificacions electròniques");
		}
		InteressatEntity interessat = entityComprovarHelper.comprovarInteressat(
				expedient,
				interessatId);
		if (interessat == null) {
			throw new ValidationException(
					interessatId,
					InteressatEntity.class,
					"L'interessat no existeix o no pertany a l'expedient(" +
					"expedientId=" + expedient.getId() + ", " +
					"interessatId=" + interessatId + ")");
		}
		if (	DocumentNotificacioTipusEnumDto.ELECTRONICA.equals(notificacio.getTipus()) &&
				!interessat.isNotificacioAutoritzat()) {
			throw new ValidationException(
					interessatId,
					InteressatEntity.class,
					"L'interessat no ha donat el consentiment per a les notificacions electròniques (" +
					"expedientId=" + expedient.getId() + ", " +
					"interessatId=" + interessatId + ")");
		}
		String nom;
		String llinatge1 = null;
		String llinatge2 = null;
		if (interessat instanceof InteressatPersonaFisicaEntity) {
			InteressatPersonaFisicaEntity interessatPf = (InteressatPersonaFisicaEntity)interessat;
			nom = interessatPf.getNom();
			llinatge1 = interessatPf.getLlinatge1();
			llinatge2 = interessatPf.getLlinatge2();
		} else if (interessat instanceof InteressatPersonaJuridicaEntity) {
			InteressatPersonaJuridicaEntity interessatPj = (InteressatPersonaJuridicaEntity)interessat;
			nom = interessatPj.getRaoSocial();
		} else {
			throw new ValidationException(
					interessatId,
					InteressatEntity.class,
					"L'interessat ha de ser una persona física o jurídica(" +
					"interessatId=" + interessatId + ", " +
					"interessatClass=" + interessat.getClass().getName() + ")");
		}
		InteressatIdiomaEnumDto notificacioIdioma = interessat.getPreferenciaIdioma();
		if (notificacioIdioma == null) {
			notificacioIdioma = InteressatIdiomaEnumDto.CA;
		}
		DocumentNotificacioEntity notificacioEntity = DocumentNotificacioEntity.getBuilder(
				expedient,
				(notificacio.getEstat() != null) ? notificacio.getEstat() : DocumentEnviamentEstatEnumDto.PENDENT,
				notificacio.getAssumpte(),
				new Date(),
				document,
				notificacio.getTipus(),
				interessat.getDocumentTipus(),
				interessat.getDocumentNum(),
				nom,
				llinatge1,
				llinatge2,
				interessat.getPais(),
				interessat.getProvincia(),
				interessat.getMunicipi(),
				interessat.isEsRepresentant(),
				notificacioIdioma).
				observacions(notificacio.getObservacions()).
				unitatAdministrativa(expedient.getMetaExpedient().getUnitatAdministrativa()).
				organCodi(expedient.getMetaExpedient().getNotificacioOrganCodi()).
				llibreCodi(expedient.getMetaExpedient().getNotificacioLlibreCodi()).
				avisTitol(notificacio.getAvisTitol()).
				avisText(notificacio.getAvisText()).
				avisTextSms(notificacio.getAvisTextSms()).
				oficiTitol(notificacio.getOficiTitol()).
				oficiText(notificacio.getOficiText()).
				destinatariEmail(interessat.getEmail()).
				// annexos().
				build();
		if (DocumentNotificacioTipusEnumDto.ELECTRONICA.equals(notificacio.getTipus())) {
			expedientHelper.ciutadaNotificacioEnviar(
					notificacioEntity,
					interessat);
		}
		DocumentNotificacioDto dto = conversioTipusHelper.convertir(
				documentNotificacioRepository.save(notificacioEntity),
				DocumentNotificacioDto.class);
		contingutLogHelper.log(
				expedient,
				LogTipusEnumDto.MODIFICACIO,
				null,
				null,
				notificacioEntity,
				LogObjecteTipusEnumDto.NOTIFICACIO,
				LogTipusEnumDto.CREACIO,
				dto.getAssumpte(),
				dto.getDestinatariNomSencerRepresentantAmbDocument(),
				false,
				false);
		return dto;
	}

	@Transactional
	@Override
	public DocumentNotificacioDto notificacioUpdate(
			Long entitatId,
			Long expedientId,
			DocumentNotificacioDto notificacio) {
		logger.debug("Modificant una notificació de l'expedient (" +
				"entitatId=" + entitatId + ", " +
				"expedientId=" + expedientId + ", " +
				"notificacio=" + notificacio + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				expedientId,
				false,
				true,
				false);
		DocumentNotificacioEntity entity = entityComprovarHelper.comprovarNotificacio(
				expedient,
				null,
				notificacio.getId());
		if (!DocumentNotificacioTipusEnumDto.MANUAL.equals(entity.getTipus())) {
			throw new ValidationException(
					notificacio.getId(),
					DocumentNotificacioEntity.class,
					"No es pot modificar una notificació que no te el tipus " + DocumentNotificacioTipusEnumDto.MANUAL);
		}
		DocumentEnviamentEstatEnumDto estat = entity.getEstat();
		if (DocumentNotificacioTipusEnumDto.MANUAL.equals(entity.getTipus())) {
			estat = notificacio.getEstat();
		}
		entity.update(
				estat,
				notificacio.getAssumpte(),
				notificacio.getObservacions(),
				notificacio.getDestinatariDocumentTipus(),
				notificacio.getDestinatariDocumentNum(),
				notificacio.getDestinatariNom(),
				notificacio.getDestinatariLlinatge1(),
				notificacio.getDestinatariLlinatge2(),
				notificacio.isDestinatariRepresentant(),
				entity.getUnitatAdministrativa(),
				entity.getOrganCodi(),
				entity.getLlibreCodi(),
				notificacio.getAvisTitol(),
				notificacio.getAvisText(),
				notificacio.getAvisTextSms(),
				notificacio.getOficiTitol(),
				notificacio.getOficiText(),
				notificacio.getIdioma());
		contingutLogHelper.log(
				expedient,
				LogTipusEnumDto.MODIFICACIO,
				null,
				null,
				entity,
				LogObjecteTipusEnumDto.NOTIFICACIO,
				LogTipusEnumDto.CREACIO,
				notificacio.getAssumpte(),
				notificacio.getDestinatariNomSencerRepresentantAmbDocument(),
				false,
				false);
		return conversioTipusHelper.convertir(
				entity,
				DocumentNotificacioDto.class);
	}

	@Transactional
	@Override
	public DocumentNotificacioDto notificacioDelete(
			Long entitatId,
			Long expedientId,
			Long notificacioId) {
		logger.debug("Esborrant una notificació de l'expedient (" +
				"entitatId=" + entitatId + ", " +
				"expedientId=" + expedientId + ", " +
				"notificacioId=" + notificacioId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				expedientId,
				false,
				true,
				false);
		DocumentNotificacioEntity notificacio = entityComprovarHelper.comprovarNotificacio(
				expedient,
				null,
				notificacioId);
		if (!DocumentNotificacioTipusEnumDto.MANUAL.equals(notificacio.getTipus())) {
			throw new ValidationException(
					notificacioId,
					DocumentNotificacioEntity.class,
					"No es pot esborrar una notificació que no te el tipus " + DocumentNotificacioTipusEnumDto.MANUAL);
		}
		documentNotificacioRepository.delete(notificacio);
		return conversioTipusHelper.convertir(
				notificacio,
				DocumentNotificacioDto.class);
	}

	@Transactional
	@Override
	public boolean notificacioRetry(
			Long entitatId,
			Long expedientId,
			Long notificacioId) {
		logger.debug("Reintentant el processament d'una notificació electrònica de l'expedient (" +
				"entitatId=" + entitatId + ", " +
				"expedientId=" + expedientId + ", " +
				"notificacioId=" + notificacioId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				expedientId,
				false,
				true,
				false);
		DocumentNotificacioEntity notificacio = entityComprovarHelper.comprovarNotificacio(
				expedient,
				null,
				notificacioId);
		if (!DocumentNotificacioTipusEnumDto.ELECTRONICA.equals(notificacio.getTipus())) {
			throw new ValidationException(
					notificacioId,
					DocumentNotificacioEntity.class,
					"Només es pot reintentar el processament d'una notificació del tipus " + DocumentNotificacioTipusEnumDto.ELECTRONICA);
		}
		if (	DocumentEnviamentEstatEnumDto.PENDENT.equals(notificacio.getEstat()) ||
				DocumentEnviamentEstatEnumDto.ENVIAT_ERROR.equals(notificacio.getEstat())) {
			InteressatEntity destinatari = InteressatPersonaFisicaEntity.getBuilder(
					notificacio.getDestinatariNom(),
					notificacio.getDestinatariLlinatge1(),
					notificacio.getDestinatariLlinatge2(),
					notificacio.getDestinatariDocumentTipus(),
					notificacio.getDestinatariDocumentNum(),
					notificacio.getDestinatariPaisCodi(),
					notificacio.getDestinatariProvinciaCodi(),
					notificacio.getDestinatariMunicipiCodi(),
					null,
					null,
					notificacio.getDestinatariEmail(),
					null,
					null,
					null,
					true,
					null,
					null).build();
			return expedientHelper.ciutadaNotificacioEnviar(
					notificacio,
					destinatari);
		} else if (
				DocumentEnviamentEstatEnumDto.ENVIAT_OK.equals(notificacio.getEstat()) ||
				DocumentEnviamentEstatEnumDto.PROCESSAT_ERROR.equals(notificacio.getEstat())) {
			return expedientHelper.ciutadaNotificacioComprovarEstat(
					notificacio);
		} else {
			// Si arriba aquí vol dir que no hi ha res a reintentar
			return true;
		}
	}

	@Transactional(readOnly = true)
	@Override
	public DocumentNotificacioDto notificacioFindAmbId(
			Long entitatId,
			Long expedientId,
			Long notificacioId) {
		logger.debug("Consulta d'una notificació de l'expedient (" +
				"entitatId=" + entitatId + ", " +
				"expedientId=" + expedientId + ", " +
				"notificacioId=" + notificacioId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				expedientId,
				false,
				true,
				false);
		DocumentNotificacioEntity notificacio = entityComprovarHelper.comprovarNotificacio(
				expedient,
				null,
				notificacioId);
		return conversioTipusHelper.convertir(
				notificacio,
				DocumentNotificacioDto.class);
	}

	@Override
	@Transactional
	@Async
	@Scheduled(fixedRateString = "${config:es.caib.ripea.tasca.notificacio.pendent.periode.execucio}")
	public void notificacioProcessarPendents() {
		logger.debug("Aplicant regles a les notificacions pendents");
		List<DocumentNotificacioEntity> pendents = documentNotificacioRepository.findByEstatAndTipus(
				DocumentEnviamentEstatEnumDto.ENVIAT_OK,
				DocumentNotificacioTipusEnumDto.ELECTRONICA);
		logger.debug("Aplicant regles a " + pendents.size() + " notificacions pendents");
		if (!pendents.isEmpty()) {
			for (DocumentNotificacioEntity pendent: pendents) {
				expedientHelper.ciutadaNotificacioComprovarEstat(
						pendent);
			}
		} else {
			logger.debug("No hi ha notificacions pendents de processar");
		}
	}

	@Transactional
	@Override
	public DocumentPublicacioDto publicacioCreate(
			Long entitatId,
			Long documentId,
			DocumentPublicacioDto publicacio) {
		logger.debug("Creant una publicació de l'expedient (" +
				"entitatId=" + entitatId + ", " +
				"documentId=" + documentId + ", " +
				"publicacio=" + publicacio + ")");
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
		DocumentPublicacioEntity publicacioEntity = DocumentPublicacioEntity.getBuilder(
				expedient,
				DocumentEnviamentEstatEnumDto.ENVIAT_OK,
				publicacio.getAssumpte(),
				new Date(),
				document,
				publicacio.getTipus()).
				build();
		DocumentPublicacioDto dto = conversioTipusHelper.convertir(
				documentPublicacioRepository.save(publicacioEntity),
				DocumentPublicacioDto.class);
		contingutLogHelper.log(
				expedient,
				LogTipusEnumDto.MODIFICACIO,
				null,
				null,
				publicacioEntity,
				LogObjecteTipusEnumDto.PUBLICACIO,
				LogTipusEnumDto.CREACIO,
				publicacio.getAssumpte(),
				publicacio.getTipus().name(),
				false,
				false);
		return dto;
	}

	@Transactional
	@Override
	public DocumentPublicacioDto publicacioUpdate(
			Long entitatId,
			Long expedientId,
			DocumentPublicacioDto publicacio) {
		logger.debug("Modificant una publicació de l'expedient (" +
				"entitatId=" + entitatId + ", " +
				"expedientId=" + expedientId + ", " +
				"publicacio=" + publicacio + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				expedientId,
				false,
				true,
				false);
		DocumentPublicacioEntity entity = entityComprovarHelper.comprovarPublicacio(
				expedient,
				null,
				publicacio.getId());
		entity.update(
				publicacio.getAssumpte(),
				publicacio.getObservacions(),
				publicacio.getTipus(),
				publicacio.getDataPublicacio());
		contingutLogHelper.log(
				expedient,
				LogTipusEnumDto.MODIFICACIO,
				null,
				null,
				entity,
				LogObjecteTipusEnumDto.PUBLICACIO,
				LogTipusEnumDto.MODIFICACIO,
				publicacio.getAssumpte(),
				publicacio.getTipus().name(),
				false,
				false);
		return conversioTipusHelper.convertir(
				entity,
				DocumentPublicacioDto.class);
	}

	@Transactional
	@Override
	public DocumentPublicacioDto publicacioDelete(
			Long entitatId,
			Long expedientId,
			Long publicacioId) {
		logger.debug("Esborrant una publicació de l'expedient (" +
				"entitatId=" + entitatId + ", " +
				"expedientId=" + expedientId + ", " +
				"publicacioId=" + publicacioId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				expedientId,
				false,
				true,
				false);
		DocumentPublicacioEntity publicacio = entityComprovarHelper.comprovarPublicacio(
				expedient,
				null,
				publicacioId);
		documentPublicacioRepository.delete(publicacio);
		contingutLogHelper.log(
				expedient,
				LogTipusEnumDto.MODIFICACIO,
				null,
				null,
				publicacio,
				LogObjecteTipusEnumDto.PUBLICACIO,
				LogTipusEnumDto.ELIMINACIO,
				null,
				null,
				false,
				false);
		return conversioTipusHelper.convertir(
				publicacio,
				DocumentPublicacioDto.class);
	}

	@Transactional(readOnly = true)
	@Override
	public DocumentPublicacioDto publicacioFindAmbId(
			Long entitatId,
			Long expedientId,
			Long publicacioId) {
		logger.debug("Consulta d'una publicació de l'expedient (" +
				"entitatId=" + entitatId + ", " +
				"expedientId=" + expedientId + ", " +
				"publicacioId=" + publicacioId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				expedientId,
				false,
				true,
				false);
		DocumentPublicacioEntity publicacio = entityComprovarHelper.comprovarPublicacio(
				expedient,
				null,
				publicacioId);
		return conversioTipusHelper.convertir(
				publicacio,
				DocumentPublicacioDto.class);
	}

	@Transactional(readOnly = true)
	@Override
	public List<DocumentEnviamentDto> enviamentFindAmbExpedient(
			Long entitatId,
			Long expedientId) {
		logger.debug("Obtenint la llista d'enviaments de l'expedient (" +
				"entitatId=" + entitatId + ", " +
				"expedientId=" + expedientId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				expedientId);
		List<DocumentEnviamentEntity> enviaments = documentEnviamentRepository.findByExpedientOrderByDataEnviamentAsc(
				expedient);
		List<DocumentEnviamentDto> resposta = new ArrayList<DocumentEnviamentDto>();
		for (DocumentEnviamentEntity enviament: enviaments) {
			if (enviament instanceof DocumentNotificacioEntity) {
				resposta.add(
						conversioTipusHelper.convertir(
								enviament,
								DocumentNotificacioDto.class));
			} else if (enviament instanceof DocumentPublicacioEntity) {
				resposta.add(
						conversioTipusHelper.convertir(
								enviament,
								DocumentPublicacioDto.class));
			}
		}
		return resposta;
	}

	private static final Logger logger = LoggerFactory.getLogger(ExpedientEnviamentServiceImpl.class);

}
