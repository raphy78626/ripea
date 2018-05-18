/**
 * 
 */
package es.caib.ripea.core.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jopendocument.dom.spreadsheet.SpreadSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Persistable;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.BustiaContingutPendentTipusEnumDto;
import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.dto.ExpedientEstatEnumDto;
import es.caib.ripea.core.api.dto.ExpedientFiltreDto;
import es.caib.ripea.core.api.dto.ExpedientSelectorDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.LogObjecteTipusEnumDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.registre.RegistreProcesEstatEnum;
import es.caib.ripea.core.api.registre.RegistreProcesEstatSistraEnum;
import es.caib.ripea.core.api.service.ExpedientService;
import es.caib.ripea.core.entity.ArxiuEntity;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.ContingutMovimentEntity;
import es.caib.ripea.core.entity.DadaEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.MetaDadaEntity;
import es.caib.ripea.core.entity.MetaExpedientEntity;
import es.caib.ripea.core.entity.MetaNodeEntity;
import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.entity.UsuariEntity;
import es.caib.ripea.core.helper.BustiaHelper;
import es.caib.ripea.core.helper.CacheHelper;
import es.caib.ripea.core.helper.ContingutHelper;
import es.caib.ripea.core.helper.ContingutLogHelper;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.CsvHelper;
import es.caib.ripea.core.helper.EmailHelper;
import es.caib.ripea.core.helper.EntityComprovarHelper;
import es.caib.ripea.core.helper.MessageHelper;
import es.caib.ripea.core.helper.PaginacioHelper;
import es.caib.ripea.core.helper.PaginacioHelper.Converter;
import es.caib.ripea.core.helper.PermisosHelper;
import es.caib.ripea.core.helper.PermisosHelper.ObjectIdentifierExtractor;
import es.caib.ripea.core.helper.PluginHelper;
import es.caib.ripea.core.helper.UsuariHelper;
import es.caib.ripea.core.repository.AlertaRepository;
import es.caib.ripea.core.repository.ArxiuRepository;
import es.caib.ripea.core.repository.BustiaRepository;
import es.caib.ripea.core.repository.CarpetaRepository;
import es.caib.ripea.core.repository.ContingutRepository;
import es.caib.ripea.core.repository.DadaRepository;
import es.caib.ripea.core.repository.DocumentNotificacioRepository;
import es.caib.ripea.core.repository.DocumentPublicacioRepository;
import es.caib.ripea.core.repository.EntitatRepository;
import es.caib.ripea.core.repository.ExpedientRepository;
import es.caib.ripea.core.repository.InteressatRepository;
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
	private DadaRepository dadaRepository;
	@Resource
	private UsuariRepository usuariRepository;
	@Resource
	private RegistreRepository registreRepository;
	@Resource
	private BustiaRepository bustiaRepository;
	@Resource
	private InteressatRepository interessatRepository;
	@Resource
	private DocumentNotificacioRepository documentNotificacioRepository;
	@Resource
	private DocumentPublicacioRepository documentPublicacioRepository;
	@Resource
	private AlertaRepository alertaRepository;
	@Resource
	private ContingutRepository contingutRepository;

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
	private PluginHelper pluginHelper;
	@Resource
	private CsvHelper csvHelper;
	@Resource
	private ContingutLogHelper contingutLogHelper;
	@Resource
	private EntityComprovarHelper entityComprovarHelper;
	@Resource
	private MessageHelper messageHelper;

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
				true,
				false,
				false);
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
		contingutLogHelper.logCreacio(
				expedient,
				false,
				false);
		ExpedientDto dto = toExpedientDto(
				expedient,
				true);
		contingutHelper.arxiuPropagarModificacio(
				expedient,
				null,
				null);
		return dto;
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
		String nomOriginal = expedient.getNom();
		// Modifica l'expedient
		expedient.update(
				nom,
				metaExpedient,
				arxiu);
		// Registra al log la modificació de l'expedient
		contingutLogHelper.log(
				expedient,
				LogTipusEnumDto.MODIFICACIO,
				(!nomOriginal.equals(expedient.getNom())) ? expedient.getNom() : null,
				null,
				false,
				false);
		ExpedientDto dto = toExpedientDto(
				expedient,
				true);
		contingutHelper.arxiuPropagarModificacio(
				expedient,
				null,
				null);
		return dto;
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
				true);
	}

	@Transactional(readOnly = true)
	@Override
	public PaginaDto<ExpedientDto> findAmbFiltreAdmin(
			Long entitatId,
			ExpedientFiltreDto filtre,
			PaginacioParamsDto paginacioParams) {
		logger.debug("Consultant els expedients segons el filtre per admins ("
				+ "entitatId=" + entitatId + ", "
				+ "filtre=" + filtre + ", "
				+ "paginacioParams=" + paginacioParams + ")");
		entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		return findAmbFiltrePaginat(
				entitatId,
				filtre,
				paginacioParams,
				true,
				false);
	}

	@Transactional(readOnly = true)
	@Override
	public PaginaDto<ExpedientDto> findAmbFiltreUser(
			Long entitatId,
			ExpedientFiltreDto filtre,
			PaginacioParamsDto paginacioParams) {
		logger.debug("Consultant els expedients segons el filtre per usuaris ("
				+ "entitatId=" + entitatId + ", "
				+ "filtre=" + filtre + ", "
				+ "paginacioParams=" + paginacioParams + ")");
		entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		return findAmbFiltrePaginat(
				entitatId,
				filtre,
				paginacioParams,
				false,
				true);
	}

	@Transactional(readOnly = true)
	@Override
	public List<ExpedientSelectorDto> findPerUserAndTipus(
			Long entitatId,
			Long metaExpedientId) {
		logger.debug("Consultant els expedients segons el tipus per usuaris ("
				+ "entitatId=" + entitatId + ", "
				+ "metaExpedientId=" + metaExpedientId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		MetaExpedientEntity metaExpedient = null;
		if (metaExpedientId != null) {
			metaExpedient = entityComprovarHelper.comprovarMetaExpedient(
					entitat,
					metaExpedientId,
					false,
					true);
		}
		List<MetaExpedientEntity> metaExpedientsPermesos = metaExpedientRepository.findByEntitatOrderByNomAsc(
				entitat);
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
		if (!metaExpedientsPermesos.isEmpty()) {
			return conversioTipusHelper.convertirList(
					expedientRepository.findByEntitatAndMetaExpedientOrderByNomAsc(
							entitat, 
							metaExpedientsPermesos,
							metaExpedient == null,
							metaExpedient), 
					ExpedientSelectorDto.class);
		} else {
			return new ArrayList<ExpedientSelectorDto>();
		}
	}

	@Transactional(readOnly = true)
	@Override
	public List<Long> findIdsAmbFiltre(
			Long entitatId,
			ExpedientFiltreDto filtre) throws NotFoundException {
		logger.debug("Consultant els ids d'expedient segons el filtre ("
				+ "entitatId=" + entitatId + ", "
				+ "filtre=" + filtre + ")");
		entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		return findIdsAmbFiltrePaginat(
				entitatId,
				filtre,
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
				false,
				false,
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
		UsuariEntity usuariOriginal = expedient.getAgafatPer();
		UsuariEntity usuariNou = usuariHelper.getUsuariAutenticat();
		/*ContingutEntity arrel = contingutHelper.findContingutArrel(expedient);
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
				null);*/
		expedient.updateAgafatPer(usuariNou);
		// Avisa a l'usuari que li han pres
		if (usuariOriginal != null) {
			emailHelper.emailUsuariContingutAgafatSensePermis(
					expedient,
					usuariOriginal,
					usuariNou);
		}
		// Registra al log l'accó d'agafar
		contingutLogHelper.log(
				expedient,
				LogTipusEnumDto.AGAFAR,
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
				false,
				false,
				false);
		if (expedientSuperior != null) {
			logger.error("No es pot agafar un expedient no arrel (id=" + id + ")");
			throw new ValidationException(
					id,
					ExpedientEntity.class,
					"No es pot agafar un expedient no arrel");
		}
		// Agafa l'expedient. Si l'expedient pertany a un altre usuari li pren
		UsuariEntity usuariOriginal = expedient.getAgafatPer();
		UsuariEntity usuariNou = usuariHelper.getUsuariAutenticat();
		/*ContingutEntity arrel = contingutHelper.findContingutArrel(expedient);
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
				null);*/
		expedient.updateAgafatPer(usuariNou);
		// Avisa a l'usuari que li han pres
		emailHelper.emailUsuariContingutAgafatSensePermis(
				expedient,
				usuariOriginal,
				usuariNou);
		// Registra al log l'apropiacio de l'expedient
		contingutLogHelper.log(
				expedient,
				LogTipusEnumDto.AGAFAR,
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
		/*/ Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				expedient);
		contingutHelper.ferIEnregistrarMoviment(
				expedient,
				expedient.getArxiu(),
				null);*/
		expedient.updateAgafatPer(null);
		// Registra al log l'alliberació de l'expedient
		contingutLogHelper.log(
				expedient,
				LogTipusEnumDto.ALLIBERAR,
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
		/*contingutHelper.ferIEnregistrarMoviment(
				expedient,
				expedient.getArxiu(),
				null);*/
		expedient.updateAgafatPer(null);
		// Registra al log l'alliberació de l'expedient
		contingutLogHelper.log(
				expedient,
				LogTipusEnumDto.ALLIBERAR,
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
				+ "motiu=" + motiu + ")");
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
		if (pluginHelper.isArxiuPluginActiu()) {
			pluginHelper.arxiuExpedientTancar(expedient);
		}
	}

	@Transactional
	@Override
	public void reobrir(
			Long entitatId,
			Long id) {
		logger.debug("Reobrint l'expedient ("
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
				ExpedientEstatEnumDto.OBERT,
				null);
		contingutLogHelper.log(
				expedient,
				LogTipusEnumDto.REOBERTURA,
				null,
				null,
				false,
				false);
		if (pluginHelper.isArxiuPluginActiu()) {
			pluginHelper.arxiuExpedientReobrir(expedient);
		}
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
		contingutLogHelper.log(
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



	private PaginaDto<ExpedientDto> findAmbFiltrePaginat(
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
		if (filtre.getArxiuId() != null) {
			arxiu = entityComprovarHelper.comprovarArxiu(
				entitat,
				filtre.getArxiuId(),
				(!accesAdmin));
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
			UsuariEntity agafatPer = null;
			if (filtre.isMeusExpedients()) {
				agafatPer = usuariHelper.getUsuariAutenticat();
			}
			Map<String, String[]> ordenacioMap = new HashMap<String, String[]>();
			ordenacioMap.put("numero", new String[] {"codi", "any", "sequencia"});
			Page<ExpedientEntity> paginaExpedients = expedientRepository.findByEntitatAndFiltre(
					entitat,
					arxiu == null,
					arxiu,
					metaExpedientsPermesos,
					metaExpedient == null,
					metaExpedient,
					filtre.getNumero() == null || "".equals(filtre.getNumero().trim()),
					filtre.getNumero() == null ? "" : filtre.getNumero(),
					filtre.getNom() == null || filtre.getNom().isEmpty(),
					filtre.getNom() == null ? "" : filtre.getNom(),
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
					agafatPer == null,
					agafatPer,
					filtre.getSearch() == null,
					filtre.getSearch() == null ? "" : filtre.getSearch(),
					filtre.getTipusId() == null,
					filtre.getTipusId(),
					paginacioHelper.toSpringDataPageable(
							paginacioParams,
							ordenacioMap));
			PaginaDto<ExpedientDto> result = paginacioHelper.toPaginaDto(
					paginaExpedients,
					ExpedientDto.class,
					new Converter<ExpedientEntity, ExpedientDto>() {
						@Override
						public ExpedientDto convert(ExpedientEntity source) {
							return toExpedientDto(
									source,
									true);
						}
					});
			for(ExpedientDto e : result) {
				boolean enAlerta = alertaRepository.countByLlegidaAndContingutId(
						false,
						e.getId()
						) > 0;
						
				List<ContingutEntity> continguts = contingutRepository.findRegistresByPareId(e.getId());
				if(!continguts.isEmpty() && alertaRepository.countByLlegidaAndContinguts(
						false,
						continguts
						) > 0) enAlerta = true;
				
				e.setAlerta(enAlerta);
			}
			return result;
		} else {
			return paginacioHelper.getPaginaDtoBuida(
					ExpedientDto.class);
		}
	}

	private List<Long> findIdsAmbFiltrePaginat(
			Long entitatId,
			ExpedientFiltreDto filtre,
			boolean accesAdmin,
			boolean comprovarAccesMetaExpedients) {
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				(!accesAdmin),
				accesAdmin,
				false);
		ArxiuEntity arxiu = null;
		if (filtre.getArxiuId() != null) {
			arxiu = entityComprovarHelper.comprovarArxiu(
				entitat,
				filtre.getArxiuId(),
				(!accesAdmin));
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
			return expedientRepository.findIdByEntitatAndFiltre(
					entitat,
					arxiu == null,
					arxiu,
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
					filtre.getEstat());
		} else {
			return new ArrayList<Long>();
		}
	}

	private ExpedientDto toExpedientDto(
			ExpedientEntity expedient,
			boolean ambPathIPermisos) {
		
		ExpedientDto expedientDto = (ExpedientDto) contingutHelper.toContingutDto(
				expedient,
				ambPathIPermisos,
				false,
				false,
				false,
				ambPathIPermisos,
				false,
				false);
		expedientDto.setAmbRegistresSenseLlegir(false);
		for(ContingutEntity contingut: expedient.getFills()) {
			if(contingut instanceof RegistreEntity) {
				Boolean llegida = ((RegistreEntity) contingut).getLlegida();
				if(llegida != null && !llegida) {
					expedientDto.setAmbRegistresSenseLlegir(true);
					break;
				}
			}
		}
		return expedientDto;
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
			registre.updateLlegida(false);
			if (registre.getRegla() != null && (RegistreProcesEstatEnum.PENDENT == registre.getProcesEstat() || RegistreProcesEstatSistraEnum.PENDENT == registre.getProcesEstatSistra())) {
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

	@SuppressWarnings("serial")
	@Transactional
	@Override
	public void relacioCreate(
			Long entitatId,
			final Long id,
			final Long relacionatId) {
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
				new Persistable<String>() {
					@Override
					public String getId() {
						return id + "#" + relacionatId;
					}
					@Override
					public boolean isNew() {
						return false;
					}
				},
				LogObjecteTipusEnumDto.RELACIO,
				LogTipusEnumDto.CREACIO,
				id.toString(),
				relacionatId.toString(),
				false,
				false);
	}

	@SuppressWarnings("serial")
	@Transactional
	@Override
	public boolean relacioDelete(
			Long entitatId,
			final Long id,
			final Long relacionatId) {
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
					new Persistable<String>() {
						@Override
						public String getId() {
							return id + "#" + relacionatId;
						}
						@Override
						public boolean isNew() {
							return false;
						}
					},
					LogObjecteTipusEnumDto.RELACIO,
					LogTipusEnumDto.ELIMINACIO,
					id.toString(),
					relacionatId.toString(),
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

	@Transactional(readOnly = true)
	@Override
	public FitxerDto exportacio(
			Long entitatId,
			Long metaExpedientId,
			Collection<Long> expedientIds,
			String format) throws IOException {
		logger.debug("Exportant informació dels expedients (" +
				"entitatId=" + entitatId + ", " +
				"metaExpedientId=" + metaExpedientId + ", " +
				"expedientIds=" + expedientIds + ", " +
				"format=" + format + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		MetaExpedientEntity metaExpedient = entityComprovarHelper.comprovarMetaExpedient(
				entitat,
				metaExpedientId,
				false,
				true);
		List<ExpedientEntity> expedients = expedientRepository.findByEntitatAndAndMetaNodeAndIdInOrderByIdAsc(
				entitat,
				metaExpedient,
				expedientIds);
		List<MetaDadaEntity> metaDades = dadaRepository.findDistinctMetaDadaByNodeIdInOrderByMetaDadaCodiAsc(expedientIds);
		List<DadaEntity> dades = dadaRepository.findByNodeIdInOrderByNodeIdAscMetaDadaCodiAsc(expedientIds);
		int numColumnes = 5 + metaDades.size();
		String[] columnes = new String[numColumnes];
		columnes[0] = messageHelper.getMessage("expedient.service.exportacio.numero");
		columnes[1] = messageHelper.getMessage("expedient.service.exportacio.titol");
		columnes[2] = messageHelper.getMessage("expedient.service.exportacio.estat");
		columnes[3] = messageHelper.getMessage("expedient.service.exportacio.datcre");
		columnes[4] = messageHelper.getMessage("expedient.service.exportacio.idnti");
		for (int i = 0; i < metaDades.size(); i++) {
			MetaDadaEntity metaDada = metaDades.get(i);
			columnes[5 + i] = metaDada.getNom() + " (" + metaDada.getCodi() + ")";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		List<String[]> files = new ArrayList<String[]>();
		int dadesIndex = 0;
		for (ExpedientEntity expedient: expedients) {
			String[] fila = new String[numColumnes];
			fila[0] = expedient.getNumero();
			fila[1] = expedient.getNom();
			fila[2] = expedient.getEstat().name();
			fila[3] = sdf.format(expedient.getCreatedDate().toDate());
			fila[4] = expedient.getNtiIdentificador();
			if (!dades.isEmpty()) {
				DadaEntity dadaActual = dades.get(dadesIndex);
				if (dadaActual.getNode().getId().equals(expedient.getId())) {
					for (int i = 0; i < metaDades.size(); i++) {
						MetaDadaEntity metaDada = metaDades.get(i);
						int dadesIndexIncrement = 1;
						while (dadaActual.getNode().getId().equals(expedient.getId())) {
							if (dadaActual.getMetaDada().getCodi().equals(metaDada.getCodi())) {
								break;
							}
							dadaActual = dades.get(dadesIndex + dadesIndexIncrement++);
						}
						if (dadaActual.getMetaDada().getCodi().equals(metaDada.getCodi())) {
							fila[5 + i] = dadaActual.getValorComString();
						}
					}
				}
			}
			files.add(fila);
		}
		FitxerDto fitxer = new FitxerDto();
		if ("ODS".equalsIgnoreCase(format)) {
			Object[][] filesArray = files.toArray(new Object[files.size()][numColumnes]);
			TableModel model = new DefaultTableModel(filesArray, columnes);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			SpreadSheet.createEmpty(model).getPackage().save(baos);
			fitxer.setNom("exportacio.ods");
			fitxer.setContentType("application/vnd.oasis.opendocument.spreadsheet");
			fitxer.setContingut(baos.toByteArray());
		} else if ("CSV".equalsIgnoreCase(format)) {
			fitxer.setNom("exportacio.csv");
			fitxer.setContentType("text/csv");
			StringBuilder sb = new StringBuilder();
			csvHelper.afegirLinia(sb, columnes, ';');
			for (String[] fila: files) {
				csvHelper.afegirLinia(sb, fila, ';');
			}
			fitxer.setContingut(sb.toString().getBytes());
		} else {
			throw new ValidationException("Format de fitxer no suportat: " + format);
		}
		return fitxer;
	}

	private static final Logger logger = LoggerFactory.getLogger(ExpedientServiceImpl.class);

}
