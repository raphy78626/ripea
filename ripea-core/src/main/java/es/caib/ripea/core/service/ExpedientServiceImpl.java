/**
 * 
 */
package es.caib.ripea.core.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.CarpetaTipusEnumDto;
import es.caib.ripea.core.api.dto.ContenidorDto;
import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.dto.ExpedientFiltreDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.PermissionDeniedException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.service.ExpedientService;
import es.caib.ripea.core.entity.ArxiuEntity;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.CarpetaEntity;
import es.caib.ripea.core.entity.ContenidorEntity;
import es.caib.ripea.core.entity.ContenidorLogEntity;
import es.caib.ripea.core.entity.ContenidorMovimentEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.EscriptoriEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.MetaExpedientEntity;
import es.caib.ripea.core.entity.MetaExpedientSequenciaEntity;
import es.caib.ripea.core.entity.MetaNodeEntity;
import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.entity.UsuariEntity;
import es.caib.ripea.core.helper.BustiaHelper;
import es.caib.ripea.core.helper.CacheHelper;
import es.caib.ripea.core.helper.ContenidorHelper;
import es.caib.ripea.core.helper.ContenidorLogHelper;
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
import es.caib.ripea.core.repository.ContenidorRepository;
import es.caib.ripea.core.repository.EntitatRepository;
import es.caib.ripea.core.repository.ExpedientRepository;
import es.caib.ripea.core.repository.MetaExpedientRepository;
import es.caib.ripea.core.repository.MetaExpedientSequenciaRepository;
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
	private ContenidorRepository contenidorRepository;
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
	private MetaExpedientSequenciaRepository metaExpedientSequenciaRepository;

	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private PermisosHelper permisosHelper;
	@Resource
	private ContenidorHelper contenidorHelper;
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
	private ContenidorLogHelper contenidorLogHelper;
	@Resource
	private EntityComprovarHelper entityComprovarHelper;



	@Transactional
	@Override
	public ExpedientDto create(
			Long entitatId,
			Long contenidorId,
			Long metaExpedientId,
			Long arxiuId,
			Integer any,
			String nom,
			Long contingutId,
			Long registreId) {
		logger.debug("Creant nou expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ", "
				+ "metaExpedientId=" + metaExpedientId + ", "
				+ "arxiuId=" + arxiuId + ", "
				+ "any=" + any + ", "
				+ "nom=" + nom + ", "
				+ "contingutId=" + contingutId + ", "
				+ "registreId=" + registreId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		// Comprova el meta-expedient i que l'usuari tengui permisos per a crear
		MetaExpedientEntity metaExpedient = null;
		if (metaExpedientId != null) {
			metaExpedient = comprovarMetaExpedient(
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
		ArxiuEntity arxiu = comprovarArxiu(
				entitat,
				arxiuId,
				true);
		ContenidorEntity contenidor = comprovarContenidor(
				entitat,
				contenidorId);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				contenidor);
		// Comprova l'accés al path del contenidor pare
		contenidorHelper.comprovarPermisosPathContenidor(
				contenidor,
				true,
				false,
				false,
				true);
		// Comprova que el nom sigui vàlid
		if (!contenidorHelper.isNomValid(nom)) {
			throw new ValidationException(
					"<creacio>",
					ExpedientEntity.class,
					"El nom de l'expedient no és vàlid (no pot començar amb \".\")");
		}
		// Comprova el permís de modificació de l'expedient superior
		ExpedientEntity expedientSuperior = contenidorHelper.getExpedientSuperior(
				contenidor,
				true);
		if (expedientSuperior != null) {
			contenidorHelper.comprovarPermisosContenidor(
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
				contenidor,
				entitat).build();
		expedientRepository.save(expedient);
		// Lliga el contingut al nou contenidor
		if (contingutId != null) {
			ContenidorEntity contingut = comprovarContenidor(
					entitat,
					contingutId);
			BustiaEntity bustia = bustiaRepository.findOne(contingut.getPare().getId());
			if (bustia != null) {
				bustiaHelper.evictElementsPendentsBustia(entitat, bustia);
			}
			contenidorHelper.ferIEnregistrarMovimentContenidor(
					contenidor,
					expedient,
					null);
		}
		// Lliga l'anotació de registre al nou contenidor
		if (registreId != null) {
			RegistreEntity registre = entityComprovarHelper.comprovarRegistre(
					registreId,
					null);
			bustiaHelper.evictElementsPendentsBustiaPerRegistre(
					entitat,
					registre);
			registre.updateContenidor(expedient);
		}
		// Obté la seqüència de l'expedient
		int anyExpedient;
		if (any != null)
			anyExpedient = any.intValue();
		else
			anyExpedient = Calendar.getInstance().get(Calendar.YEAR);
		MetaExpedientSequenciaEntity sequencia = metaExpedientSequenciaRepository.findByMetaExpedientAndAny(
				metaExpedient,
				anyExpedient);
		if (sequencia == null) {
			sequencia = MetaExpedientSequenciaEntity.getBuilder(
					anyExpedient,
					metaExpedient).build();
			metaExpedientSequenciaRepository.save(sequencia);
		}
		long sequenciaExpedient = sequencia.getValor();
		sequencia.incrementar();
		expedient.updateAnySequencia(
				anyExpedient,
				sequenciaExpedient);
		// Crear carpeta de nouvinguts
		CarpetaEntity nouvinguts = CarpetaEntity.getBuilder(
				CarpetaServiceImpl.CARPETA_NOUVINGUTS_NOM,
				CarpetaTipusEnumDto.NOUVINGUT,
				expedient,
				entitat).build();
		carpetaRepository.save(nouvinguts);
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
		ExpedientEntity expedient = comprovarExpedient(
				entitat,
				null,
				id);
		ArxiuEntity arxiu = comprovarArxiu(
				entitat,
				arxiuId,
				true);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				expedient);
		// Comprova l'accés al path de l'expedient
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
		// Comprova el meta-expedient
		MetaExpedientEntity metaExpedient = null;
		if (metaExpedientId != null) {
			metaExpedient = comprovarMetaExpedient(
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
		if (!contenidorHelper.isNomValid(nom)) {
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
		ExpedientEntity expedient = comprovarExpedient(
				entitat,
				null,
				id);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				expedient);
		// Comprova l'accés al path de l'expedient
		contenidorHelper.comprovarPermisosPathContenidor(
				expedient,
				true,
				false,
				false,
				true);
		// Comprova el permís de modificació de l'expedient superior
		ExpedientEntity expedientSuperior = contenidorHelper.getExpedientSuperior(
				expedient,
				false);
		if (expedientSuperior != null) {
			contenidorHelper.comprovarPermisosContenidor(
					expedientSuperior,
					false,
					true,
					false);
		}
		// Comprova el permís d'esborrar de l'expedient actual
		contenidorHelper.comprovarPermisosContenidor(
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
		ExpedientEntity expedient = comprovarExpedient(
				entitat,
				null,
				id);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path de l'expedient
		contenidorHelper.comprovarPermisosPathContenidor(
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

	@Transactional(readOnly = true)
	@Override
	public List<ContenidorDto> getContingutCarpetaNouvinguts(
			Long entitatId,
			Long id) {
		logger.debug("Consultant el contingut de la carpeta nouvinguts de l'expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = comprovarExpedient(
				entitat,
				null,
				id);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path de l'expedient
		contenidorHelper.comprovarPermisosPathContenidor(
				expedient,
				true,
				false,
				false,
				true);
		List<ContenidorEntity> fills = contenidorRepository.findByPareAndEsborrat(
				expedient,
				0,
				new Sort("createdDate"));
		for (ContenidorEntity fill: fills) {
			// Si es la carpeta de nouvinguts
			if (fill instanceof CarpetaEntity && CarpetaTipusEnumDto.NOUVINGUT.equals(((CarpetaEntity)fill).getTipus())) {
				ContenidorDto fillDto = contenidorHelper.toContenidorDto(
						fill,
						true,
						false,
						false,
						false,
						false,
						false);
				return fillDto.getFills();
			}
		}
		return new ArrayList<ContenidorDto>();
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
		ArxiuEntity arxiu = comprovarArxiu(
				entitat,
				arxiuId,
				true);
		ExpedientEntity expedient = comprovarExpedient(
				entitat,
				arxiu,
				id);
		// No s'ha de poder agafar un expedient no arrel
		ExpedientEntity expedientSuperior = contenidorHelper.getExpedientSuperior(
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
		contenidorHelper.comprovarPermisosContenidor(
				expedient,
				false,
				true,
				false);
		// Agafa l'expedient. Si l'expedient pertany a un altre usuari li pren
		ContenidorEntity arrel = contenidorHelper.findContenidorArrel(expedient);
		UsuariEntity usuariOriginal = null;
		if (arrel instanceof EscriptoriEntity)
			usuariOriginal = ((EscriptoriEntity)arrel).getUsuari();
		EscriptoriEntity escriptori = contenidorHelper.getEscriptoriPerUsuari(
				entitat,
				usuariHelper.getUsuariAutenticat());
		contenidorHelper.ferIEnregistrarMovimentContenidor(
				expedient,
				escriptori,
				null);
		// Avisa a l'usuari que li han pres
		if (usuariOriginal != null) {
			emailHelper.emailUsuariContenidorAgafatSensePermis(
					expedient,
					usuariOriginal);
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
		ArxiuEntity arxiu = comprovarArxiu(
				entitat,
				arxiuId,
				false);
		ExpedientEntity expedient = comprovarExpedient(
				entitat,
				arxiu,
				id);
		// No s'ha de poder agafar un expedient no arrel
		ExpedientEntity expedientSuperior = contenidorHelper.getExpedientSuperior(
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
		ContenidorEntity arrel = contenidorHelper.findContenidorArrel(expedient);
		UsuariEntity usuariOriginal = null;
		if (arrel instanceof EscriptoriEntity)
			usuariOriginal = ((EscriptoriEntity)arrel).getUsuari();
		EscriptoriEntity escriptori = contenidorHelper.getEscriptoriPerUsuari(
				entitat,
				usuariRepository.findOne(usuariCodi));
		contenidorHelper.ferIEnregistrarMovimentContenidor(
				expedient,
				escriptori,
				null);
		// Avisa a l'usuari que li han pres
		emailHelper.emailUsuariContenidorAgafatSensePermis(
				expedient,
				usuariOriginal);
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
		ExpedientEntity expedient = comprovarExpedient(
				entitat,
				null,
				id);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				expedient);
		contenidorHelper.ferIEnregistrarMovimentContenidor(
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
		ExpedientEntity expedient = comprovarExpedient(
				entitat,
				null,
				id);
		contenidorHelper.ferIEnregistrarMovimentContenidor(
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
	public void finalitzar(
			Long entitatId,
			Long id,
			String motiu) {
		logger.debug("Finalitzant l'expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ","
				+ "mootiu=" + motiu + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = comprovarExpedient(
				entitat,
				null,
				id);
		// Comprova que l'usuari tengui permisos d'escriptura a damunt l'expedient
		contenidorHelper.comprovarPermisosContenidor(
				expedient,
				false,
				true,
				false);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				expedient);
		// Allibera l'expedient
		contenidorHelper.ferIEnregistrarMovimentContenidor(
				expedient,
				expedient.getArxiu(),
				null);
		// Finalitza l'expedient
		expedient.updateFinalitzar(motiu);
		contenidorLogHelper.log(
				expedient,
				LogTipusEnumDto.FINALITZACIO,
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
		ExpedientEntity expedientDesti = comprovarExpedient(
				entitat,
				null,
				id);
		ExpedientEntity expedientOrigen = comprovarExpedient(
				entitat,
				null,
				acumulatId);
		CarpetaEntity carpetaNouvinguts = findCarpetaNouvinguts(expedientDesti);
		// Registra al log l'acumulació
		ContenidorLogEntity contenidorLog = contenidorLogHelper.log(
				expedientDesti,
				LogTipusEnumDto.ACUMULACIO,
				null,
				null,
				false,
				false);
		for (ContenidorEntity contenidor: expedientOrigen.getFills()) {
			ContenidorMovimentEntity contenidorMoviment = contenidorHelper.ferIEnregistrarMovimentContenidor(
					contenidor,
					carpetaNouvinguts,
					null);
			// Registra al log el moviment del node
			contenidorLogHelper.log(
					contenidor,
					LogTipusEnumDto.MOVIMENT,
					contenidorLog,
					contenidorMoviment,
					true,
					true);
		}
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
		ArxiuEntity arxiu = comprovarArxiu(
				entitat,
				filtre.getArxiuId(),
				(!accesAdmin));
		MetaExpedientEntity metaExpedient = null;
		if (filtre.getMetaExpedientId() != null) {
			metaExpedient = comprovarMetaExpedient(
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
		return (ExpedientDto)contenidorHelper.toContenidorDto(
				expedient,
				ambPathIPermisos,
				false,
				false,
				false,
				ambPathIPermisos,
				false);
	}

	private ContenidorEntity comprovarContenidor(
			EntitatEntity entitat,
			Long id) {
		ContenidorEntity contenidor = contenidorRepository.findOne(id);
		if (contenidor == null) {
			logger.error("No s'ha trobat el contenidor (contenidorId=" + id + ")");
			throw new NotFoundException(
					id,
					ContenidorEntity.class);
		}
		if (!contenidor.getEntitat().equals(entitat)) {
			logger.error("L'entitat del contenidor no coincideix ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + contenidor.getEntitat().getId() + ")");
			throw new NotFoundException(
					id,
					ContenidorEntity.class);
		}
		return contenidor;
	}
	private MetaExpedientEntity comprovarMetaExpedient(
			EntitatEntity entitat,
			Long id,
			boolean comprovarPermisCreate,
			boolean comprovarPermisRead) {
		MetaExpedientEntity metaExpedient = metaExpedientRepository.findOne(
				id);
		if (metaExpedient == null) {
			logger.error("No s'ha trobat el meta-expedient (id=" + id + ")");
			throw new NotFoundException(
					id,
					MetaExpedientEntity.class);
		}
		if (!entitat.equals(metaExpedient.getEntitat())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat del meta-expedient ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + metaExpedient.getEntitat().getId() + ")");
			throw new NotFoundException(
					id,
					MetaExpedientEntity.class);
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (comprovarPermisCreate) {
			boolean granted = permisosHelper.isGrantedAll(
					metaExpedient.getId(),
					MetaNodeEntity.class,
					new Permission[] {ExtendedPermission.CREATE},
					auth);
			if (!granted) {
				logger.error("No es tenen permisos per a crear expedients amb el meta-expedient (id=" + id + ")");
				throw new PermissionDeniedException(
						id,
						MetaExpedientEntity.class,
						auth.getName(),
						"CREATE");
			}
		}
		if (comprovarPermisRead) {
			boolean granted = permisosHelper.isGrantedAll(
					metaExpedient.getId(),
					MetaNodeEntity.class,
					new Permission[] {ExtendedPermission.READ},
					auth);
			if (!granted) {
				logger.error("No es tenen permisos per a llegir expedients amb el meta-expedient (id=" + id + ")");
				throw new PermissionDeniedException(
						id,
						MetaExpedientEntity.class,
						auth.getName(),
						"READ");
			}
		}
		return metaExpedient;
	}

	private ArxiuEntity comprovarArxiu(
			EntitatEntity entitat,
			Long id,
			boolean comprovarAcces) {
		ArxiuEntity arxiu = arxiuRepository.findOne(id);
		if (arxiu == null) {
			logger.error("No s'ha trobat l'arxiu (id=" + id + ")");
			throw new NotFoundException(
					id,
					ArxiuEntity.class);
		}
		if (!entitat.equals(arxiu.getEntitat())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat de l'arxiu ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + arxiu.getEntitat().getId() + ")");
			throw new NotFoundException(
					id,
					ArxiuEntity.class);
		}
		if (comprovarAcces) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			boolean granted = false;
			// Comprova l'accés als meta-expedients
			for (MetaExpedientEntity metaExpedient: arxiu.getMetaExpedients()) {
				if (permisosHelper.isGrantedAll(
						metaExpedient.getId(),
						MetaNodeEntity.class,
						new Permission[] {ExtendedPermission.READ},
						auth)) {
					granted = true;
					break;
				}
			}
			if (!granted) {
				logger.error("No es tenen permisos per a accedir a l'arxiu (id=" + id + ")");
				throw new PermissionDeniedException(
						id,
						ArxiuEntity.class,
						auth.getName(),
						"READ");
			}
		}
		return arxiu;
	}

	private ExpedientEntity comprovarExpedient(
			EntitatEntity entitat,
			ArxiuEntity arxiu,
			Long id)  {
		ExpedientEntity expedient = expedientRepository.findOne(id);
		if (expedient == null) {
			logger.error("No s'ha trobat l'expedient (id=" + id + ")");
			throw new NotFoundException(
					id,
					ExpedientEntity.class);
		}
		if (!entitat.getId().equals(expedient.getEntitat().getId())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat de l'expedient ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + expedient.getEntitat().getId() + ")");
			throw new NotFoundException(
					id,
					ExpedientEntity.class);
		}
		if (arxiu != null && !arxiu.equals(expedient.getArxiu())) {
			logger.error("L'arxiu de l'expedient no coincideix amb l'especificat ("
					+ "id=" + id + ", "
					+ "arxiuId1=" + arxiu.getId() + ", "
					+ "arxiuId2=" + expedient.getArxiu().getId() + ")");
			throw new NotFoundException(
					id,
					ExpedientEntity.class);
		}
		return expedient;
	}

	private CarpetaEntity findCarpetaNouvinguts(ExpedientEntity expedient) {
		return (CarpetaEntity)contenidorRepository.findByPareAndNomAndEsborrat(
				expedient,
				CarpetaServiceImpl.CARPETA_NOUVINGUTS_NOM,
				0);
	}

	private static final Logger logger = LoggerFactory.getLogger(ExpedientServiceImpl.class);

}
