/**
 * 
 */
package es.caib.ripea.core.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.RegistreAnotacioDto;
import es.caib.ripea.core.api.dto.RegistreDocumentDto;
import es.caib.ripea.core.api.dto.RegistreInteressatDto;
import es.caib.ripea.core.api.exception.BustiaNotFoundException;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.ExpedientNotFoundException;
import es.caib.ripea.core.api.exception.RegistreNotFoundException;
import es.caib.ripea.core.api.service.RegistreService;
import es.caib.ripea.core.entity.ArxiuEntity;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.RegistreAccioEnum;
import es.caib.ripea.core.entity.RegistreDocumentEntity;
import es.caib.ripea.core.entity.RegistreDocumentTipusEnum;
import es.caib.ripea.core.entity.RegistreDocumentValidesaEnum;
import es.caib.ripea.core.entity.RegistreDocumentacioFisicaTipusEnum;
import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.entity.RegistreInteressatCanalEnum;
import es.caib.ripea.core.entity.RegistreInteressatDocumentTipusEnum;
import es.caib.ripea.core.entity.RegistreInteressatEntity;
import es.caib.ripea.core.entity.RegistreMovimentEntity;
import es.caib.ripea.core.entity.RegistreTipusEnum;
import es.caib.ripea.core.entity.RegistreTransportTipusEnum;
import es.caib.ripea.core.helper.BustiaHelper;
import es.caib.ripea.core.helper.CacheHelper;
import es.caib.ripea.core.helper.EmailHelper;
import es.caib.ripea.core.helper.PermisosComprovacioHelper;
import es.caib.ripea.core.helper.PermisosHelper;
import es.caib.ripea.core.helper.UsuariHelper;
import es.caib.ripea.core.repository.BustiaRepository;
import es.caib.ripea.core.repository.ExpedientRepository;
import es.caib.ripea.core.repository.RegistreDocumentRepository;
import es.caib.ripea.core.repository.RegistreInteressatRepository;
import es.caib.ripea.core.repository.RegistreMovimentRepository;
import es.caib.ripea.core.repository.RegistreRepository;
import es.caib.ripea.core.security.ExtendedPermission;

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
	private RegistreInteressatRepository registreInteressatRepository;
	@Resource
	private RegistreDocumentRepository registreDocumentRepository;
	@Resource
	private ExpedientRepository expedientRepository;
	@Resource
	private BustiaRepository bustiaRepository;
	@Resource
	private RegistreMovimentRepository registreMovimentRepository;

	@Resource
	private PermisosComprovacioHelper permisosComprovacioHelper;
	@Resource
	private PermisosHelper permisosHelper;
	@Resource
	private EmailHelper emailHelper;
	@Resource
	private CacheHelper cacheHelper;
	@Resource
	private BustiaHelper bustiaHelper;
	@Resource
	private UsuariHelper usuariHelper;



	@Transactional
	@Override
	public void create(
			Long entitatId,
			RegistreAnotacioDto registre) throws EntitatNotFoundException, BustiaNotFoundException {
		logger.debug("Creant nova anotació de registre ("
				+ "entitatId=" + entitatId + ", "
				+ "registre=" + registre + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				false);
		// Cercam la bústia destinatària de l'anotació
		BustiaEntity bustia = bustiaRepository.findByEntitatAndUnitatCodiAndPerDefecteTrue(
				entitat,
				registre.getEntitatCodi());
		if (bustia == null) {
			logger.error("No s'ha trobat la bústia per defecte (" +
					"entitatId=" + entitatId + "," +
					"unitatCodi=" + registre.getEntitatCodi() + ")");
			throw new BustiaNotFoundException();
		}
		// Donam d'alta el registre
		RegistreEntity registreEntity = RegistreEntity.getBuilder(
				(registre.getAccio() != null) ? RegistreAccioEnum.valueOf(registre.getAccio().name()) : null,
				(registre.getTipus() != null) ? RegistreTipusEnum.valueOf(registre.getTipus().name()) : null,
				registre.getEntitatCodi(),
				registre.getEntitatNom(),
				registre.getNumero(),
				registre.getData(),
				registre.getAssumpteResum(),
				registre.getAssumpteCodi(),
				registre.getAssumpteReferencia(),
				registre.getAssumpteNumExpedient(),
				(registre.getTransportTipus() != null) ? RegistreTransportTipusEnum.valueOf(registre.getTransportTipus().name()) : null,
				registre.getTransportNumero(),
				registre.getUsuariNom(),
				registre.getUsuariContacte(),
				registre.getAplicacioEmissora(),
				(registre.getDocumentacioFisica() != null) ? RegistreDocumentacioFisicaTipusEnum.valueOf(registre.getDocumentacioFisica().name()) : null,
				registre.getObservacions(),
				entitat,
				registre.isProva()).build();
		registreEntity.updateContenidor(bustia);
		registreRepository.save(registreEntity);
		// Donam d'alta els seus interessats
		if (registre.getInteressats() != null) {
			for (RegistreInteressatDto interessat: registre.getInteressats()) {
				RegistreInteressatEntity interessatEntity = RegistreInteressatEntity.getBuilder(
						(interessat.getDocumentTipus() != null) ? RegistreInteressatDocumentTipusEnum.valueOf(interessat.getDocumentTipus().name()) : null,
						interessat.getDocumentNum(),
						interessat.getNom(),
						interessat.getLlinatge1(),
						interessat.getLlinatge2(),
						interessat.getRaoSocial(),
						interessat.getPais(),
						interessat.getProvincia(),
						interessat.getMunicipi(),
						interessat.getAdresa(),
						interessat.getCodiPostal(),
						interessat.getEmail(),
						interessat.getTelefon(),
						interessat.getEmailHabilitat(),
						(interessat.getCanalPreferent() != null) ? RegistreInteressatCanalEnum.valueOf(interessat.getCanalPreferent().name()) : null,
						interessat.getObservacions(),
						registreEntity).build();
				if (interessat.getRepresentantNom() != null || interessat.getRepresentantDocumentNum() != null) {
					interessatEntity.updateDadesRepresentant(
							(interessat.getRepresentantDocumentTipus() != null) ? RegistreInteressatDocumentTipusEnum.valueOf(interessat.getRepresentantDocumentTipus().name()) : null,
							interessat.getRepresentantDocumentNum(),
							interessat.getRepresentantNom(),
							interessat.getRepresentantLlinatge1(),
							interessat.getRepresentantLlinatge2(),
							interessat.getRepresentantRaoSocial(),
							interessat.getRepresentantPais(),
							interessat.getRepresentantProvincia(),
							interessat.getRepresentantMunicipi(),
							interessat.getRepresentantAdresa(),
							interessat.getRepresentantCodiPostal(),
							interessat.getRepresentantEmail(),
							interessat.getRepresentantTelefon(),
							interessat.getRepresentantEmailHabilitat(),
							(interessat.getRepresentantCanalPreferent() != null) ? RegistreInteressatCanalEnum.valueOf(interessat.getRepresentantCanalPreferent().name()) : null);
				}
				registreInteressatRepository.save(interessatEntity);
			}
		}
		// Donam d'alta els seus annexos
		if (registre.getAnnexos() != null) {
			for (RegistreDocumentDto annex: registre.getAnnexos()) {
				RegistreDocumentEntity documentEntity = RegistreDocumentEntity.getBuilder(
						annex.getFitxerNom(),
						annex.getIdentificador(),
						(annex.getValidesa() != null) ? RegistreDocumentValidesaEnum.valueOf(annex.getValidesa().name()) : null,
						(annex.getTipus() != null) ? RegistreDocumentTipusEnum.valueOf(annex.getTipus().name()) : null,
						annex.getGestioDocumentalId(),
						annex.getIndentificadorDocumentFirmat(),
						annex.getObservacions(),
						registreEntity).build();
				registreDocumentRepository.save(documentEntity);
			}
		}
		// Registra el moviment
		RegistreMovimentEntity registreMoviment = RegistreMovimentEntity.getBuilder(
				registreEntity,
				bustia,
				usuariHelper.getUsuariAutenticat(),
				null).build();
		registreMovimentRepository.save(registreMoviment);
		// Refrescam cache usuaris bústia
		bustiaHelper.evictElementsPendentsBustia(
				entitat,
				bustia);
		// Avisam per correu als responsables
		emailHelper.emailBustiaPendentRegistre(
				bustia,
				registreEntity);
	}

	@Transactional
	@Override
	public void afegirAExpedient(
			Long entitatId,
			Long expedientId,
			Long registreId) throws EntitatNotFoundException, ExpedientNotFoundException, RegistreNotFoundException {
		logger.debug("Afegir anotació de registre a l'expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "expedientId=" + expedientId + ", "
				+ "registreId=" + registreId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = comprovarExpedient(
				entitat,
				null,
				expedientId);
		RegistreEntity registre = comprovarRegistre(
				entitat,
				registreId);
		bustiaHelper.evictElementsPendentsBustiaPerRegistre(
				entitat,
				registre);
		registre.updateContenidor(expedient);
	}

	@Transactional
	@Override
	public void rebutjar(
			Long entitatId,
			Long bustiaId,
			Long registreId,
			String motiu) throws EntitatNotFoundException, BustiaNotFoundException, RegistreNotFoundException {
		logger.debug("Rebutjar anotació de registre a la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "bustiaId=" + bustiaId + ", "
				+ "registreId=" + registreId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		BustiaEntity bustia = comprovarBustia(
				entitat,
				bustiaId,
				true);
		RegistreEntity registre = comprovarRegistre(
				entitat,
				registreId);
		if (!registre.getContenidor().equals(bustia)) {
			logger.error("No s'ha trobat el registre a dins la bústia (" +
					"bustiaId=" + bustiaId + "," +
					"registreId=" + registreId + ")");
			throw new RegistreNotFoundException();
		}
		registre.updateMotiuRebuig(motiu);
		// Refrescam cache usuaris bústia
		bustiaHelper.evictElementsPendentsBustia(
				entitat,
				bustia);
	}



	private ExpedientEntity comprovarExpedient(
			EntitatEntity entitat,
			ArxiuEntity arxiu,
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
		if (arxiu != null && !arxiu.equals(expedient.getArxiu())) {
			logger.error("L'arxiu de l'expedient no coincideix amb l'especificat ("
					+ "id=" + id + ""
					+ "arxiuId1=" + arxiu.getId() + ", "
					+ "arxiuId2=" + expedient.getArxiu().getId() + ")");
			throw new ExpedientNotFoundException();
		}
		return expedient;
	}

	private BustiaEntity comprovarBustia(
			EntitatEntity entitat,
			Long bustiaId,
			boolean comprovarPermisRead) throws BustiaNotFoundException {
		BustiaEntity bustia = bustiaRepository.findOne(bustiaId);
		if (bustia == null) {
			logger.error("No s'ha trobat la bústia (bustiaId=" + bustiaId + ")");
			throw new BustiaNotFoundException();
		}
		if (!entitat.equals(bustia.getEntitat())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat de la bústia ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + bustia.getEntitat().getId() + ")");
			throw new BustiaNotFoundException();
		}
		if (comprovarPermisRead) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			boolean esPermisRead = permisosHelper.isGrantedAll(
					bustiaId,
					BustiaEntity.class,
					new Permission[] {ExtendedPermission.READ},
					auth);
			if (!esPermisRead) {
				logger.error("Aquest usuari no té permis per accedir a la bústia ("
						+ "id=" + bustiaId + ", "
						+ "usuari=" + auth.getName() + ")");
				throw new SecurityException("Sense permisos d'accés a aquesta bústia");
			}
		}
		return bustia;
	}

	private RegistreEntity comprovarRegistre(
			EntitatEntity entitat,
			Long id) throws RegistreNotFoundException {
		RegistreEntity registre = registreRepository.findOne(id);
		if (registre == null) {
			logger.error("No s'ha trobat l'anotació de registre (id=" + id + ")");
			throw new RegistreNotFoundException();
		}
		if (!entitat.getId().equals(registre.getEntitat().getId())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat de l'anotació de registre ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + registre.getEntitat().getId() + ")");
			throw new RegistreNotFoundException();
		}
		return registre;
	}

	private static final Logger logger = LoggerFactory.getLogger(RegistreServiceImpl.class);

}
