/**
 * 
 */
package es.caib.ripea.core.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import es.caib.ripea.core.api.dto.DocumentEnviamentDto;
import es.caib.ripea.core.api.dto.DocumentEnviamentEstatEnumDto;
import es.caib.ripea.core.api.dto.DocumentNotificacioDto;
import es.caib.ripea.core.api.dto.DocumentNotificacioTipusEnumDto;
import es.caib.ripea.core.api.dto.DocumentPublicacioDto;
import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.dto.ExpedientEstatEnumDto;
import es.caib.ripea.core.api.dto.ExpedientFiltreDto;
import es.caib.ripea.core.api.dto.LogObjecteTipusEnumDto;
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
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.DocumentNotificacioEntity;
import es.caib.ripea.core.entity.DocumentPublicacioEntity;
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
import es.caib.ripea.core.repository.DocumentNotificacioRepository;
import es.caib.ripea.core.repository.DocumentPublicacioRepository;
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
	private DocumentNotificacioRepository documentNotificacioRepository;
	@Resource
	private DocumentPublicacioRepository documentPublicacioRepository;

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
	private ContingutLogHelper contingutLogHelper;
	@Resource
	private EntityComprovarHelper entityComprovarHelper;

	private Map<String, String[]> ordenacioMap;
	
	public ExpedientServiceImpl() {
		this.ordenacioMap = new HashMap<String, String[]>();
		ordenacioMap.put("numero", new String[] {"any", "sequencia"});
	}

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
		ExpedientEntity expedient = contingutHelper.crearNouExpedient(
				nom,
				metaExpedient,
				arxiu,
				contingut,
				entitat,
				"1.0",
				arxiu.getUnitatCodi(),
				new Date(),
				any);
		// Lliga el contingut al nou expedient
		if (contingutPendentId != null) {
			moureContingutPendentDeBustiaAExpedient(
					entitat,
					expedient,
					contingutPendentTipus,
					contingutPendentId);
		}
		// Registra al log la creació de l'expedient
		contingutLogHelper.log(
				expedient,
				LogTipusEnumDto.CREACIO,
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
		contingutLogHelper.log(
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
		contingutLogHelper.log(
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
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				id);
		if (expedient.getArxiu() != null)
			entityComprovarHelper.comprovarArxiu(
				entitat,
				expedient.getArxiu().getId(),
				true);
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
		contingutLogHelper.log(
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
		contingutLogHelper.log(
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
		contingutLogHelper.log(
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
		contingutLogHelper.log(
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
		contingutLogHelper.log(
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
		ContingutLogEntity contenidorLog = contingutLogHelper.log(
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
			contingutLogHelper.log(
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
		ArxiuEntity arxiu = null;
		List<ArxiuEntity> arxiusPermesos = null;
		if (filtre.getArxiuId() != null) {
			arxiu = entityComprovarHelper.comprovarArxiu(
				entitat,
				filtre.getArxiuId(),
				(!accesAdmin));
		} else {
			arxiusPermesos = this.getArxiusPermesos(entitat);
		}
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
							arxiu == null,
							arxiu,
							arxiusPermesos == null,
							arxiusPermesos,
							metaExpedientsPermesos,
							metaExpedient == null,
							metaExpedient,
							filtre.getNumero() == null || "".equals(filtre.getNumero().trim()),
							filtre.getNumero(),
							filtre.getNom() == null || filtre.getNom().isEmpty(),
							filtre.getNom(),
							filtre.getDataCreacioInici() == null,
							filtre.getDataCreacioInici(),
							filtre.getDataCreacioFi() == null,
							filtre.getDataCreacioFi(),
							filtre.getDataTancatInici() == null,
							filtre.getDataTancatInici(),
							filtre.getDataTancatFi() == null,
							filtre.getDataTancatFi(),
							filtre.getEstat() == null,
							filtre.getEstat(),
							paginacioHelper.toSpringDataPageable(
									paginacioParams,
									ordenacioMap)),
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

	private List<ArxiuEntity> getArxiusPermesos(EntitatEntity entitat) {
		List<ArxiuEntity> arxius = arxiuRepository.findByEntitatAndPareNotNull(entitat);
		List<ArxiuEntity> resultat = new ArrayList<ArxiuEntity>();
		Permission[] permisos = new Permission[] {ExtendedPermission.READ};
		// Filtra els meta-expedients dels arxius en el que l'usuari tingui permisos de lectura
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// Afegeix les unitats dels arxius que estiguin relacionats amb algun meta-expedient amb permisos
		for (ArxiuEntity arxiu: arxius) {
			boolean granted = false;
			// Comprova l'accés als meta-expedients
			for (MetaExpedientEntity metaExpedient: arxiu.getMetaExpedients()) {
				if (permisosHelper.isGrantedAll(
						metaExpedient.getId(),
						MetaNodeEntity.class,
						permisos,
						auth)) {
					granted = true;
					break;
				}
			}
			if (granted)
				resultat.add(arxiu);
		}
		return resultat;
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

	@Transactional
	@Override
	public void relacioCreate(
			Long entitatId,
			Long id,
			Long relacionatId) {
		logger.debug("Relacionant l'expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "relacionatId=" + relacionatId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient= entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				id);
		ExpedientEntity relacionat = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				relacionatId);
		expedient.addRelacionat(relacionat);
		contingutLogHelper.log(
				expedient,
				LogTipusEnumDto.MODIFICACIO,
				null,
				null,
				relacionat,
				LogObjecteTipusEnumDto.RELACIO,
				LogTipusEnumDto.CREACIO,
				relacionatId.toString(),
				null,
				false,
				false);
	}

	@Transactional
	@Override
	public boolean relacioDelete(
			Long entitatId,
			Long id,
			Long relacionatId) {
		logger.debug("Esborrant la relació de l'expedient amb un altre expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "relacionatId=" + relacionatId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ExpedientEntity expedient = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				id);
		ExpedientEntity relacionat = entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				relacionatId);
		boolean trobat = true;
		if (expedient.getRelacionatsAmb().contains(relacionat)) {
			expedient.removeRelacionat(relacionat);
		} else if (relacionat.getRelacionatsAmb().contains(expedient)) {
			relacionat.removeRelacionat(expedient);
		} else {
			trobat = false;
		}
		if (trobat) {
			contingutLogHelper.log(
					expedient,
					LogTipusEnumDto.MODIFICACIO,
					null,
					null,
					relacionat,
					LogObjecteTipusEnumDto.RELACIO,
					LogTipusEnumDto.ELIMINACIO,
					relacionatId.toString(),
					null,
					false,
					false);
		}
		return trobat;
	}

	@Transactional(readOnly = true)
	@Override
	public List<ExpedientDto> relacioFindAmbExpedient(
			Long entitatId,
			Long expedientId) {
		logger.debug("Obtenint la llista d'expedients relacionats (" +
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
		List<ExpedientEntity> relacionats = new ArrayList<ExpedientEntity>();
		relacionats.addAll(expedient.getRelacionatsAmb());
		relacionats.addAll(expedient.getRelacionatsPer());
		Collections.sort(
				relacionats, 
				new Comparator<ExpedientEntity>() {
				    @Override
				    public int compare(ExpedientEntity e1, ExpedientEntity e2) {
				        return e1.getNom().compareTo(e2.getNom());
				    }
				});
		List<ExpedientDto> relacionatsDto = new ArrayList<ExpedientDto>();
		for (ExpedientEntity e: relacionats)
			relacionatsDto.add(toExpedientDto(e, false));		
		return relacionatsDto;
	}

	@Transactional
	@Override
	public DocumentNotificacioDto notificacioCreate(
			Long entitatId,
			Long expedientId,
			DocumentNotificacioDto notificacio) {
		logger.debug("Creant una notificació de l'expedient (" +
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
				expedientId);
		DocumentEntity document = entityComprovarHelper.comprovarDocument(
				entitat,
				expedient,
				notificacio.getDocument().getId(),
				false,
				false,
				false);
		// TODO Enviament de la notificació amb el plugin
		String sistemaExternId = "???";
		DocumentNotificacioEntity entity = DocumentNotificacioEntity.getBuilder(
				expedient,
				DocumentEnviamentEstatEnumDto.ENVIAT_OK,
				notificacio.getAssumpte(),
				new Date(),
				sistemaExternId,
				document,
				notificacio.getTipus(),
				notificacio.getDestinatariDocumentTipus(),
				notificacio.getDestinatariDocumentNum(),
				notificacio.getDestinatariNom(),
				notificacio.getDestinatariLlinatge1(),
				notificacio.getDestinatariLlinatge2(),
				notificacio.isDestinatariRepresentant()).
				build();
		contingutLogHelper.log(
				expedient,
				LogTipusEnumDto.MODIFICACIO,
				null,
				null,
				entity,
				LogObjecteTipusEnumDto.NOTIFICACIO,
				LogTipusEnumDto.CREACIO,
				notificacio.getAssumpte(),
				notificacio.getNomSencerRepresentantAmbDocument(),
				false,
				false);
		return conversioTipusHelper.convertir(
				documentNotificacioRepository.save(entity),
				DocumentNotificacioDto.class);
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
				expedientId);
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
		entity.update(
				notificacio.getAssumpte(),
				notificacio.getObservacions(),
				notificacio.getDestinatariDocumentTipus(),
				notificacio.getDestinatariDocumentNum(),
				notificacio.getDestinatariNom(),
				notificacio.getDestinatariLlinatge1(),
				notificacio.getDestinatariLlinatge2(),
				notificacio.isDestinatariRepresentant());
		contingutLogHelper.log(
				expedient,
				LogTipusEnumDto.MODIFICACIO,
				null,
				null,
				entity,
				LogObjecteTipusEnumDto.NOTIFICACIO,
				LogTipusEnumDto.CREACIO,
				notificacio.getAssumpte(),
				notificacio.getNomSencerRepresentantAmbDocument(),
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
				expedientId);
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

	@Transactional(readOnly = true)
	@Override
	public List<DocumentEnviamentDto> notificacioFindByExpedientId(
			Long entitatId,
			Long expedientId) {
		logger.debug("Obtenint la llista de notificacions de l'expedient (" +
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
		return conversioTipusHelper.convertirList(
				documentNotificacioRepository.findByExpedientOrderByDestinatariDocumentNumAscDataEnviamentAsc(
						expedient),
				DocumentEnviamentDto.class);
	}

	@Transactional
	@Override
	public DocumentPublicacioDto publicacioCreate(
			Long entitatId,
			Long expedientId,
			DocumentPublicacioDto publicacio) {
		logger.debug("Creant una publicació de l'expedient (" +
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
				expedientId);
		DocumentEntity document = entityComprovarHelper.comprovarDocument(
				entitat,
				expedient,
				publicacio.getDocument().getId(),
				false,
				false,
				false);
		DocumentPublicacioEntity entity = DocumentPublicacioEntity.getBuilder(
				expedient,
				DocumentEnviamentEstatEnumDto.ENVIAT_OK,
				publicacio.getAssumpte(),
				new Date(),
				document,
				publicacio.getTipus()).
				build();
		contingutLogHelper.log(
				expedient,
				LogTipusEnumDto.MODIFICACIO,
				null,
				null,
				entity,
				LogObjecteTipusEnumDto.PUBLICACIO,
				LogTipusEnumDto.CREACIO,
				publicacio.getAssumpte(),
				publicacio.getTipus().name(),
				false,
				false);
		return conversioTipusHelper.convertir(
				documentPublicacioRepository.save(entity),
				DocumentPublicacioDto.class);
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
				expedientId);
		DocumentPublicacioEntity entity = entityComprovarHelper.comprovarPublicacio(
				expedient,
				null,
				publicacio.getId());
		entity.update(
				publicacio.getAssumpte(),
				publicacio.getObservacions(),
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
		entityComprovarHelper.comprovarExpedient(
				entitat,
				null,
				expedientId);
		DocumentPublicacioEntity publicacio = documentPublicacioRepository.findOne(
				publicacioId);
		if (publicacio == null) {
			throw new NotFoundException(
					publicacioId,
					DocumentNotificacioEntity.class);
		}
		if (!DocumentNotificacioTipusEnumDto.MANUAL.equals(publicacio.getTipus())) {
			throw new ValidationException(
					publicacioId,
					DocumentNotificacioEntity.class,
					"No es pot esborrar una notificació que no te el tipus " + DocumentNotificacioTipusEnumDto.MANUAL);
		}
		documentPublicacioRepository.delete(publicacio);
		return conversioTipusHelper.convertir(
				publicacio,
				DocumentPublicacioDto.class);
	}

	@Transactional(readOnly = true)
	@Override
	public List<DocumentPublicacioDto> publicacioFindByExpedientId(
			Long entitatId,
			Long expedientId) {
		logger.debug("Obtenint la llista de publicacions de l'expedient (" +
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
		return conversioTipusHelper.convertirList(
				documentPublicacioRepository.findByExpedientOrderByDataEnviamentAsc(
						expedient),
				DocumentPublicacioDto.class);
	}

	private static final Logger logger = LoggerFactory.getLogger(ExpedientServiceImpl.class);

}
