/**
 * 
 */
package es.caib.ripea.core.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.BustiaContingutPendentTipusEnumDto;
import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.dto.ExpedientEstatEnumDto;
import es.caib.ripea.core.api.dto.ExpedientFiltreDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.service.ExpedientService;
import es.caib.ripea.core.entity.ArxiuEntity;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.ContingutLogEntity;
import es.caib.ripea.core.entity.ContingutMovimentEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.EscriptoriEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.MetaExpedientEntity;
import es.caib.ripea.core.entity.MetaNodeEntity;
import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.entity.UsuariEntity;
import es.caib.ripea.core.helper.BustiaHelper;
import es.caib.ripea.core.helper.CacheHelper;
import es.caib.ripea.core.helper.ContingutHelper;
import es.caib.ripea.core.helper.ContingutLogHelper;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.EmailHelper;
import es.caib.ripea.core.helper.EntityComprovarHelper;
import es.caib.ripea.core.helper.PaginacioHelper;
import es.caib.ripea.core.helper.PaginacioHelper.Converter;
import es.caib.ripea.core.helper.PermisosHelper;
import es.caib.ripea.core.helper.PermisosHelper.ObjectIdentifierExtractor;
import es.caib.ripea.core.helper.UsuariHelper;
import es.caib.ripea.core.repository.ArxiuRepository;
import es.caib.ripea.core.repository.BustiaRepository;
import es.caib.ripea.core.repository.CarpetaRepository;
import es.caib.ripea.core.repository.EntitatRepository;
import es.caib.ripea.core.repository.ExpedientRepository;
import es.caib.ripea.core.repository.MetaExpedientRepository;
import es.caib.ripea.core.repository.RegistreRepository;
import es.caib.ripea.core.repository.UsuariRepository;
import es.caib.ripea.core.security.ExtendedPermission;

/**
 * Implementació dels mètodes per a gestionar expedients.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class ExpedientServiceImpl implements ExpedientService {

	@Resource
	private EntitatRepository entitatRepository;
	@Resource
	private MetaExpedientRepository metaExpedientRepository;
	@Resource
	private ExpedientRepository expedientRepository;
	@Resource
	private CarpetaRepository carpetaRepository;
	@Resource
	private ArxiuRepository arxiuRepository;
	@Resource
	private UsuariRepository usuariRepository;
	@Resource
	private RegistreRepository registreRepository;
	@Resource
	private BustiaRepository bustiaRepository;

	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private PermisosHelper permisosHelper;
	@Resource
	private ContingutHelper contingutHelper;
	@Resource
	private PaginacioHelper paginacioHelper;
	@Resource
	private CacheHelper cacheHelper;
	@Resource
	private BustiaHelper bustiaHelper;
	@Resource
	private UsuariHelper usuariHelper;
	@Resource
	private EmailHelper emailHelper;
	@Resource
	private ContingutLogHelper contenidorLogHelper;
	@Resource
	private EntityComprovarHelper entityComprovarHelper;



	@Transactional
	@Override
	public ExpedientDto create(
			Long entitatId,
			Long pareId,
			Long metaExpedientId,
			Long arxiuId,
			Integer any,
			String nom,
			BustiaContingutPendentTipusEnumDto contingutPendentTipus,
			Long contingutPendentId) {
		logger.debug("Creant nou expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "pareId=" + pareId + ", "
				+ "metaExpedientId=" + metaExpedientId + ", "
				+ "arxiuId=" + arxiuId + ", "
				+ "any=" + any + ", "
				+ "nom=" + nom + ", "
				+ "contingutPendentTipus=" + contingutPendentTipus + ", "
				+ "contingutPendentId=" + contingutPendentId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		// Comprova el meta-expedient i que l'usuari tengui permisos per a crear
		MetaExpedientEntity metaExpedient = null;
		if (metaExpedientId != null) {
			metaExpedient = entityComprovarHelper.comprovarMetaExpedient(
					entitat,
					metaExpedientId,
					true,
					false);
		} else {
			throw new ValidationException(
					"<creacio>",
					ExpedientEntity.class,
					"No es pot crear un expedient sense un meta-expedient associat");			
		}
		ArxiuEntity arxiu = entityComprovarHelper.comprovarArxiu(
				entitat,
				arxiuId,
				true);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				pareId,
				null);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				contingut);
		// Comprova l'accés al path del contenidor pare
		contingutHelper.comprovarPermisosPathContingut(
				contingut,
				true,
				false,
				false,
				true);
		// Comprova que el nom sigui vàlid
		if (!contingutHelper.isNomValid(nom)) {
			throw new ValidationException(
					"<creacio>",
					ExpedientEntity.class,
					"El nom de l'expedient no és vàlid (no pot començar amb \".\")");
		}
		// Comprova el permís de modificació de l'expedient superior
		ExpedientEntity expedientSuperior = contingutHelper.getExpedientSuperior(
				contingut,
				true);
		if (expedientSuperior != null) {
			contingutHelper.comprovarPermisosContingut(
					expedientSuperior,
					false,
					true,
					false);
			// TODO Comprovar jerarquia de meta-expedients amb la cadena d'expedients
		} else {
			// TODO Comprovar que el metaExpedient és de primer nivell
		}
		// Crea l'expedient
		ExpedientEntity expedient = ExpedientEntity.getBuilder(
				nom,
				metaExpedient,
				arxiu,
				contingut,
				entitat).build();
		expedientRepository.save(expedient);
		// Calcula en número del nou expedient
		contingutHelper.calcularSequenciaExpedient(expedient, any);
		// Lliga el contingut al nou expedient
		if (contingutPendentId != null) {
			moureContingutPendentDeBustiaAExpedient(
					entitat,
					expedient,
					contingutPendentTipus,
					contingutPendentId);
		}
		// Registra al log la creació de l'expedient
		contenidorLogHelper.log(
				expedient,
				LogTipusEnumDto.CREACIO,
				null,
				null,
				true,
				true);
		return toExpedientDto(
				expedient,
				false);
	}

	@Transactional
	@Override
	public ExpedientDto update(
			Long entitatId,
			Long id,
			Long arxiuId,
			Long metaExpedientId,
			String nom) {
		logger.debug("Actualitzant dades de l'expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "arxiuId=" + arxiuId + ", "
				+ "nom=" + nom + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				id);
		ArxiuEntity arxiu = entityComprovarHelper.comprovarArxiu(
				entitat,
				arxiuId,
				true);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				expedient);
		// Comprova l'accés al path de l'expedient
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
		// Comprova el meta-expedient
		MetaExpedientEntity metaExpedient = null;
		if (metaExpedientId != null) {
			metaExpedient = entityComprovarHelper.comprovarMetaExpedient(
					entitat,
					metaExpedientId,
					true,
					false);
			cacheHelper.evictErrorsValidacioPerNode(expedient);
		} else {
			throw new ValidationException(
					"<creacio>",
					ExpedientEntity.class,
					"No es pot actualitzar un expedient sense un meta-expedient associat");			
		}
		// Comprova que el nom sigui vàlid
		if (!contingutHelper.isNomValid(nom)) {
			throw new ValidationException(
					id,
					ExpedientEntity.class,
					"El nom de l'expedient no és vàlid (no pot començar amb \".\")");
		}
		// Modifica l'expedient
		expedient.update(
				nom,
				metaExpedient,
				arxiu);
		// Registra al log la modificació de l'expedient
		contenidorLogHelper.log(
				expedient,
				LogTipusEnumDto.MODIFICACIO,
				null,
				null,
				false,
				false);
		return toExpedientDto(
				expedient,
				false);
	}

	@Transactional
	@Override
	@CacheEvict(value = "errorsValidacioNode", key = "#id")
	public ExpedientDto delete(
			Long entitatId,
			Long id) {
		logger.debug("Esborrant l'expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				id);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				expedient);
		// Comprova l'accés al path de l'expedient
		contingutHelper.comprovarPermisosPathContingut(
				expedient,
				true,
				false,
				false,
				true);
		// Comprova el permís de modificació de l'expedient superior
		ExpedientEntity expedientSuperior = contingutHelper.getExpedientSuperior(
				expedient,
				false);
		if (expedientSuperior != null) {
			contingutHelper.comprovarPermisosContingut(
					expedientSuperior,
					false,
					true,
					false);
		}
		// Comprova el permís d'esborrar de l'expedient actual
		contingutHelper.comprovarPermisosContingut(
				expedient,
				false,
				false,
				true);
		expedientRepository.delete(expedient);
		// Registra al log l'eliminació de l'expedient
		contenidorLogHelper.log(
				expedient,
				LogTipusEnumDto.ELIMINACIO,
				null,
				null,
				true,
				true);
		return toExpedientDto(
				expedient,
				false);
	}

	@Transactional(readOnly = true)
	@Override
	public ExpedientDto findById(
			Long entitatId,
			Long id) {
		logger.debug("Obtenint l'expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				id);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path de l'expedient
		contingutHelper.comprovarPermisosPathContingut(
				expedient,
				true,
				false,
				false,
				true);
		return toExpedientDto(
				expedient,
				false);
	}

	@Transactional(readOnly = true)
	@Override
	public PaginaDto<ExpedientDto> findPaginatAdmin(
			Long entitatId,
			ExpedientFiltreDto filtre,
			PaginacioParamsDto paginacioParams) {
		logger.debug("Consultant els expedients paginats per admins ("
				+ "entitatId=" + entitatId + ", "
				+ "paginacioParams=" + paginacioParams + ")");
		entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		return findByArxiuPaginat(
				entitatId,
				filtre,
				paginacioParams,
				true,
				false);
	}

	@Transactional(readOnly = true)
	@Override
	public PaginaDto<ExpedientDto> findPaginatUser(
			Long entitatId,
			ExpedientFiltreDto filtre,
			PaginacioParamsDto paginacioParams) {
		logger.debug("Consultant els expedients paginats per users ("
				+ "entitatId=" + entitatId + ", "
				+ "paginacioParams=" + paginacioParams + ")");
		entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		return findByArxiuPaginat(
				entitatId,
				filtre,
				paginacioParams,
				false,
				true);
	}

	@Transactional
	@Override
	public void agafarUser(
			Long entitatId,
			Long arxiuId,
			Long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Agafant l'expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "usuari=" + auth.getName() + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ArxiuEntity arxiu = entityComprovarHelper.comprovarArxiu(
				entitat,
				arxiuId,
				true);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				arxiu,
				id);
		// No s'ha de poder agafar un expedient no arrel
		ExpedientEntity expedientSuperior = contingutHelper.getExpedientSuperior(
				expedient,
				false);
		if (expedientSuperior != null) {
			logger.error("No es pot agafar un expedient no arrel (id=" + id + ")");
			throw new ValidationException(
					id,
					ExpedientEntity.class,
					"No es pot agafar un expedient no arrel");
		}
		// Comprova que es pugui modificar l'expedient a agafar
		contingutHelper.comprovarPermisosContingut(
				expedient,
				false,
				true,
				false);
		// Agafa l'expedient. Si l'expedient pertany a un altre usuari li pren
		ContingutEntity arrel = contingutHelper.findContingutArrel(expedient);
		UsuariEntity usuariOriginal = null;
		if (arrel instanceof EscriptoriEntity)
			usuariOriginal = ((EscriptoriEntity)arrel).getUsuari();
		UsuariEntity usuariNou = usuariHelper.getUsuariAutenticat();
		EscriptoriEntity escriptori = contingutHelper.getEscriptoriPerUsuari(
				entitat,
				usuariNou);
		contingutHelper.ferIEnregistrarMoviment(
				expedient,
				escriptori,
				null);
		// Avisa a l'usuari que li han pres
		if (usuariOriginal != null) {
			emailHelper.emailUsuariContingutAgafatSensePermis(
					expedient,
					usuariOriginal,
					usuariNou);
		}
		// Registra al log l'apropiació de l'expedient
		contenidorLogHelper.log(
				expedient,
				LogTipusEnumDto.RESERVA,
				null,
				null,
				false,
				false);
	}

	@Transactional
	@Override
	public void agafarAdmin(
			Long entitatId,
			Long arxiuId,
			Long id,
			String usuariCodi) {
		logger.debug("Agafant l'expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "arxiuId=" + arxiuId + ", "
				+ "id=" + id + ", "
				+ "usuariCodi=" + usuariCodi + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ArxiuEntity arxiu = entityComprovarHelper.comprovarArxiu(
				entitat,
				arxiuId,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				arxiu,
				id);
		// No s'ha de poder agafar un expedient no arrel
		ExpedientEntity expedientSuperior = contingutHelper.getExpedientSuperior(
				expedient,
				false);
		if (expedientSuperior != null) {
			logger.error("No es pot agafar un expedient no arrel (id=" + id + ")");
			throw new ValidationException(
					id,
					ExpedientEntity.class,
					"No es pot agafar un expedient no arrel");
		}
		// Agafa l'expedient. Si l'expedient pertany a un altre usuari li pren
		ContingutEntity arrel = contingutHelper.findContingutArrel(expedient);
		UsuariEntity usuariOriginal = null;
		if (arrel instanceof EscriptoriEntity)
			usuariOriginal = ((EscriptoriEntity)arrel).getUsuari();
		UsuariEntity usuariNou = usuariRepository.findOne(usuariCodi);
		if (usuariNou == null) {
			throw new NotFoundException(
					id,
					ArxiuEntity.class);
		}
		EscriptoriEntity escriptori = contingutHelper.getEscriptoriPerUsuari(
				entitat,
				usuariNou);
		contingutHelper.ferIEnregistrarMoviment(
				expedient,
				escriptori,
				null);
		// Avisa a l'usuari que li han pres
		emailHelper.emailUsuariContingutAgafatSensePermis(
				expedient,
				usuariOriginal,
				usuariNou);
		// Registra al log l'apropiacio de l'expedient
		contenidorLogHelper.log(
				expedient,
				LogTipusEnumDto.RESERVA,
				null,
				null,
				false,
				false);
	}

	@Transactional
	@Override
	public void alliberarUser(
			Long entitatId,
			Long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Alliberant l'expedient com a usuari ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "usuari=" + auth.getName() + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				id);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				expedient);
		contingutHelper.ferIEnregistrarMoviment(
				expedient,
				expedient.getArxiu(),
				null);
		// Registra al log l'alliberació de l'expedient
		contenidorLogHelper.log(
				expedient,
				LogTipusEnumDto.ALLIBERACIO,
				null,
				null,
				false,
				false);
	}

	@Transactional
	@Override
	public void alliberarAdmin(
			Long entitatId,
			Long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Alliberant l'expedient com a administrador ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "usuari=" + auth.getName() + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				id);
		contingutHelper.ferIEnregistrarMoviment(
				expedient,
				expedient.getArxiu(),
				null);
		// Registra al log l'alliberació de l'expedient
		contenidorLogHelper.log(
				expedient,
				LogTipusEnumDto.ALLIBERACIO,
				null,
				null,
				false,
				false);
	}

	@Transactional
	@Override
	public void tancar(
			Long entitatId,
			Long id,
			String motiu) {
		logger.debug("Tancant l'expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ","
				+ "mootiu=" + motiu + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				id);
		// Comprova que l'usuari tengui permisos d'escriptura a damunt l'expedient
		contingutHelper.comprovarPermisosContingut(
				expedient,
				false,
				true,
				false);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				expedient);
		// Allibera l'expedient
		contingutHelper.ferIEnregistrarMoviment(
				expedient,
				expedient.getArxiu(),
				null);
		// Finalitza l'expedient
		expedient.updateEstat(
				ExpedientEstatEnumDto.TANCAT,
				motiu);
		contenidorLogHelper.log(
				expedient,
				LogTipusEnumDto.TANCAMENT,
				null,
				null,
				false,
				false);
	}

	@Transactional
	@Override
	public void acumular(
			Long entitatId,
			Long id,
			Long acumulatId) {
		logger.debug("Finalitzant l'expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedientDesti = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				id);
		ExpedientEntity expedientOrigen = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				acumulatId);
		// Registra al log l'acumulació
		ContingutLogEntity contenidorLog = contenidorLogHelper.log(
				expedientDesti,
				LogTipusEnumDto.ACUMULACIO,
				null,
				null,
				false,
				false);
		for (ContingutEntity contingut: expedientOrigen.getFills()) {
			ContingutMovimentEntity contenidorMoviment = contingutHelper.ferIEnregistrarMoviment(
					contingut,
					expedientDesti,
					null);
			// Registra al log el moviment del node
			contenidorLogHelper.log(
					contingut,
					LogTipusEnumDto.MOVIMENT,
					contenidorLog,
					contenidorMoviment,
					true,
					true);
		}
	}

	@Transactional
	@Override
	public void afegirContingutBustia(
			Long entitatId,
			Long id,
			Long bustiaId,
			BustiaContingutPendentTipusEnumDto contingutTipus,
			Long contingutId) throws NotFoundException {
		logger.debug("Afegint contingut de la bústia a l'expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "bustiaId=" + bustiaId + ", "
				+ "contingutTipus=" + contingutTipus + ", "
				+ "contingutId=" + contingutId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				id);
		moureContingutPendentDeBustiaAExpedient(
				entitat,
				expedient,
				contingutTipus,
				contingutId);
	}



	private PaginaDto<ExpedientDto> findByArxiuPaginat(
			Long entitatId,
			ExpedientFiltreDto filtre,
			PaginacioParamsDto paginacioParams,
			boolean accesAdmin,
			boolean comprovarAccesMetaExpedients) {
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				(!accesAdmin),
				accesAdmin,
				false);
		ArxiuEntity arxiu = entityComprovarHelper.comprovarArxiu(
				entitat,
				filtre.getArxiuId(),
				(!accesAdmin));
		MetaExpedientEntity metaExpedient = null;
		if (filtre.getMetaExpedientId() != null) {
			metaExpedient = entityComprovarHelper.comprovarMetaExpedient(
					entitat,
					filtre.getMetaExpedientId(),
					false,
					true);
		}
		List<MetaExpedientEntity> metaExpedientsPermesos = metaExpedientRepository.findByEntitatOrderByNomAsc(
				entitat);
		if (comprovarAccesMetaExpedients) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			permisosHelper.filterGrantedAll(
					metaExpedientsPermesos,
					new ObjectIdentifierExtractor<MetaExpedientEntity>() {
						@Override
						public Long getObjectIdentifier(MetaExpedientEntity metaExpedient) {
							return metaExpedient.getId();
						}
					},
					MetaNodeEntity.class,
					new Permission[] {ExtendedPermission.READ},
					auth);
		}
		if (!metaExpedientsPermesos.isEmpty()) {
			return paginacioHelper.toPaginaDto(
					expedientRepository.findByEntitatAndArxiuFiltre(
							entitat,
							arxiu,
							metaExpedientsPermesos,
							metaExpedient == null,
							metaExpedient,
							filtre.getNom() == null || filtre.getNom().isEmpty(),
							filtre.getNom(),
							filtre.getDataCreacioInici() == null,
							filtre.getDataCreacioInici(),
							filtre.getDataCreacioFi() == null,
							filtre.getDataCreacioFi(),
							paginacioHelper.toSpringDataPageable(paginacioParams)),
					ExpedientDto.class,
					new Converter<ExpedientEntity, ExpedientDto>() {
						@Override
						public ExpedientDto convert(ExpedientEntity source) {
							return toExpedientDto(
									source,
									true);
						}
					});
		} else {
			return paginacioHelper.getPaginaDtoBuida(
					ExpedientDto.class);
		}
	}

	private ExpedientDto toExpedientDto(
			ExpedientEntity expedient,
			boolean ambPathIPermisos) {
		return (ExpedientDto)contingutHelper.toContingutDto(
				expedient,
				ambPathIPermisos,
				false,
				false,
				false,
				ambPathIPermisos,
				false);
	}

	private void moureContingutPendentDeBustiaAExpedient(
			EntitatEntity entitat,
			ExpedientEntity expedient,
			BustiaContingutPendentTipusEnumDto contingutTipus,
			Long contingutId) {
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		if (contingut instanceof RegistreEntity) {
			RegistreEntity registre = (RegistreEntity)contingut;
			if (registre.getRegla() != null) {
				throw new ValidationException(
						contingutId,
						ContingutEntity.class,
						"Aquest contingut pendent no es pot moure perquè te activat el processament automàtic mitjançant una regla (reglaId=" + registre.getRegla().getId() + ")");
			}
		}
		Long bustiaId = contingut.getPare().getId();
		contingutHelper.ferIEnregistrarMoviment(
				contingut,
				expedient,
				null);
		BustiaEntity bustia = entityComprovarHelper.comprovarBustia(
				entitat,
				bustiaId,
				true);
		bustiaHelper.evictElementsPendentsBustia(entitat, bustia);
	}

	private static final Logger logger = LoggerFactory.getLogger(ExpedientServiceImpl.class);

}
