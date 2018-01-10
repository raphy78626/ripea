/**
 * 
 */
package es.caib.ripea.core.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.plugins.arxiu.api.ContingutArxiu;
import es.caib.plugins.arxiu.api.Document;
import es.caib.ripea.core.api.dto.ArbreDto;
import es.caib.ripea.core.api.dto.BackofficeTipusEnumDto;
import es.caib.ripea.core.api.dto.BustiaContingutDto;
import es.caib.ripea.core.api.dto.BustiaContingutFiltreEstatEnumDto;
import es.caib.ripea.core.api.dto.BustiaContingutPendentTipusEnumDto;
import es.caib.ripea.core.api.dto.BustiaDto;
import es.caib.ripea.core.api.dto.BustiaFiltreDto;
import es.caib.ripea.core.api.dto.BustiaUserFiltreDto;
import es.caib.ripea.core.api.dto.ContingutDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.dto.ReglaTipusEnumDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.exception.BustiaServiceException;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.registre.RegistreAnotacio;
import es.caib.ripea.core.api.registre.RegistreProcesEstatEnum;
import es.caib.ripea.core.api.registre.RegistreTipusEnum;
import es.caib.ripea.core.api.service.BustiaService;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.ContingutMovimentEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.RegistreAnnexEntity;
import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.entity.ReglaEntity;
import es.caib.ripea.core.helper.BustiaHelper;
import es.caib.ripea.core.helper.CacheHelper;
import es.caib.ripea.core.helper.ContingutHelper;
import es.caib.ripea.core.helper.ContingutLogHelper;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.EmailHelper;
import es.caib.ripea.core.helper.EntityComprovarHelper;
import es.caib.ripea.core.helper.HibernateHelper;
import es.caib.ripea.core.helper.PaginacioHelper;
import es.caib.ripea.core.helper.PaginacioHelper.Converter;
import es.caib.ripea.core.helper.PermisosHelper;
import es.caib.ripea.core.helper.PermisosHelper.ObjectIdentifierExtractor;
import es.caib.ripea.core.helper.PluginHelper;
import es.caib.ripea.core.helper.PropertiesHelper;
import es.caib.ripea.core.helper.RegistreHelper;
import es.caib.ripea.core.helper.ReglaHelper;
import es.caib.ripea.core.helper.UnitatOrganitzativaHelper;
import es.caib.ripea.core.helper.UsuariHelper;
import es.caib.ripea.core.helper.XmlHelper;
import es.caib.ripea.core.repository.BustiaRepository;
import es.caib.ripea.core.repository.ContingutComentariRepository;
import es.caib.ripea.core.repository.ContingutRepository;
import es.caib.ripea.core.repository.EntitatRepository;
import es.caib.ripea.core.repository.ExpedientRepository;
import es.caib.ripea.core.repository.RegistreRepository;
import es.caib.ripea.core.repository.ReglaRepository;
import es.caib.ripea.core.security.ExtendedPermission;
import es.caib.ripea.plugin.registre.RegistreAnotacioResposta;

/**
 * Implementació dels mètodes per a gestionar bústies.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class BustiaServiceImpl implements BustiaService {

	@Resource
	private BustiaRepository bustiaRepository;
	@Resource
	private EntitatRepository entitatRepository;
	@Resource
	private RegistreRepository registreRepository;
	@Resource
	private ReglaRepository reglaRepository;
	@Resource
	private ExpedientRepository expedientRepository;
	@Resource
	private ContingutRepository contingutRepository;
	@Resource
	private ContingutComentariRepository contingutComentariRepository;

	@Resource
	private PermisosHelper permisosHelper;
	@Resource
	private PluginHelper pluginHelper;
	@Resource
	private ContingutHelper contingutHelper;
	@Resource
	private ContingutLogHelper contingutLogHelper;
	@Resource
	private CacheHelper cacheHelper;
	@Resource
	private BustiaHelper bustiaHelper;
	@Resource
	private EmailHelper emailHelper;
	@Resource
	private UsuariHelper usuariHelper;
	@Resource
	private ReglaHelper reglaHelper;
	@Resource
	private RegistreHelper registreHelper;
	@Resource
	private EntityComprovarHelper entityComprovarHelper;
	@Resource
	private UnitatOrganitzativaHelper unitatOrganitzativaHelper;
	@Resource
	private PaginacioHelper paginacioHelper;
	@Resource
	private ConversioTipusHelper conversioTipusHelper;

	/*private Map<String, String[]> ordenacioMap;

	public BustiaServiceImpl() {
		this.ordenacioMap = new HashMap<String, String[]>();
		ordenacioMap.put("pareNom", new String[] {"pare.nom"});
	}*/

	@Override
	@Transactional
	public BustiaDto create(
			Long entitatId,
			BustiaDto bustia) {
		logger.debug("Creant una nova bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "bustia=" + bustia + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		UnitatOrganitzativaDto unitat = unitatOrganitzativaHelper.findPerEntitatAndCodi(
				entitat.getCodi(),
				bustia.getUnitatCodi());
		if (unitat == null) {
			logger.error("No s'ha trobat la unitat administrativa (codi=" + bustia.getUnitatCodi() + ")");
			throw new NotFoundException(
					bustia.getUnitatCodi(),
					UnitatOrganitzativaDto.class);
		}
		// Cerca la bústia superior
		BustiaEntity bustiaPare = bustiaRepository.findByEntitatAndUnitatCodiAndPareNull(
				entitat,
				bustia.getUnitatCodi());
		// Si la bústia superior no existeix la crea
		if (bustiaPare == null) {
			bustiaPare = bustiaRepository.save(
					BustiaEntity.getBuilder(
							entitat,
							unitat.getDenominacio(),
							bustia.getUnitatCodi(),
							null).build());
		}
		// Crea la nova bústia
		BustiaEntity entity = BustiaEntity.getBuilder(
				entitat,
				bustia.getNom(),
				bustia.getUnitatCodi(),
				bustiaPare).build();
		bustiaRepository.save(entity);
		// Registra al log la creació de la bústia
		contingutLogHelper.logCreacio(
				entity,
				false,
				false);
		// Si no hi ha cap bústia per defecte a dins l'unitat configura
		// la bústia actual com a bústia per defecte
		BustiaEntity bustiaPerDefecte = bustiaRepository.findByEntitatAndUnitatCodiAndPerDefecteTrue(
				entitat,
				bustia.getUnitatCodi());
		if (bustiaPerDefecte == null) {
			entity.updatePerDefecte(true);
			contingutLogHelper.log(
					entity,
					LogTipusEnumDto.PER_DEFECTE,
					"true",
					null,
					false,
					false);
		}
		return toBustiaDto(
				entity,
				false,
				false);
	}

	@Override
	@Transactional
	public BustiaDto update(
			Long entitatId,
			BustiaDto bustia) {
		logger.debug("Modificant la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "bustia=" + bustia + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		BustiaEntity entity = entityComprovarHelper.comprovarBustia(
				entitat,
				bustia.getId(),
				false);
		String nomOriginal = entity.getNom();
		entity.update(
				bustia.getNom());
		// Registra al log la modificació de la bústia
		contingutLogHelper.log(
				entity,
				LogTipusEnumDto.MODIFICACIO,
				(!nomOriginal.equals(entity.getNom())) ? entity.getNom() : null,
				null,
				false,
				false);
		return toBustiaDto(
				entity,
				false,
				false);
	}

	@Override
	@Transactional
	public BustiaDto updateActiva(
			Long entitatId,
			Long id,
			boolean activa) {
		logger.debug("Modificant propietat activa de la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "activa=" + activa + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		BustiaEntity entity = entityComprovarHelper.comprovarBustia(
				entitat,
				id,
				false);
		entity.updateActiva(activa);
		// Registra al log la modificació de la bústia
		contingutLogHelper.log(
				entity,
				activa ? LogTipusEnumDto.ACTIVACIO : LogTipusEnumDto.DESACTIVACIO,
				null,
				false,
				false);
		return toBustiaDto(
				entity,
				false,
				false);
	}

	@Override
	@Transactional
	public BustiaDto delete(
			Long entitatId,
			Long id) {
		logger.debug("Esborrant la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		BustiaEntity bustia = entityComprovarHelper.comprovarBustia(
				entitat,
				id,
				false);
		if (bustia.isPerDefecte()) {
			String missatgeError = "No es pot esborrar la bústia per defecte (" +
					"bustiaId=" + id + ", " +
					"unitatOrganitzativaCodi=" + bustia.getUnitatCodi() + ")";
			logger.error(missatgeError);
			throw new ValidationException(
					id,
					BustiaEntity.class,
					missatgeError);
		}
		bustiaRepository.delete(bustia);
		// Registra al log l'eliminació de la bústia
		contingutLogHelper.log(
				bustia,
				LogTipusEnumDto.ELIMINACIO,
				null,
				null,
				true,
				true);
		return toBustiaDto(
				bustia,
				false,
				false);
	}

	@Override
	@Transactional
	public BustiaDto marcarPerDefecte(
			Long entitatId,
			Long id) {
		logger.debug("Marcant la bústia com a bústia per defecte("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		BustiaEntity bustia = entityComprovarHelper.comprovarBustia(
				entitat,
				id,
				false);
		List<BustiaEntity> bustiesMateixaUnitat = bustiaRepository.findByEntitatAndUnitatCodiAndPareNotNull(
				entitat,
				bustia.getUnitatCodi());
		for (BustiaEntity bu: bustiesMateixaUnitat) {
			if (bu.isPerDefecte()) {
				// Registra al log la modificació de la bústia
				contingutLogHelper.log(
						bu,
						LogTipusEnumDto.PER_DEFECTE,
						"false",
						null,
						false,
						false);
			}
			bu.updatePerDefecte(false);
		}
		// Registra al log la modificació de la bústia
		contingutLogHelper.log(
				bustia,
				LogTipusEnumDto.PER_DEFECTE,
				"true",
				null,
				false,
				false);
		bustia.updatePerDefecte(true);
		return toBustiaDto(
				bustia,
				false,
				false);
	}

	@Override
	@Transactional(readOnly = true)
	public BustiaDto findById(
			Long entitatId,
			Long id) {
		logger.debug("Cercant la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		BustiaEntity bustia = entityComprovarHelper.comprovarBustia(
				entitat,
				id,
				false);
		BustiaDto resposta = toBustiaDto(
				bustia,
				false,
				false);
		// Ompl els permisos
		List<BustiaDto> llista = new ArrayList<BustiaDto>();
		llista.add(resposta);
		omplirPermisosPerBusties(llista, true);
		return resposta;
	}

	@Override
	@Transactional(readOnly = true)
	public List<BustiaDto> findAmbUnitatCodiAdmin(
			Long entitatId,
			String unitatCodi) {
		logger.debug("Cercant les bústies de la unitat per admins ("
				+ "entitatId=" + entitatId + ", "
				+ "unitatCodi=" + unitatCodi + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		List<BustiaEntity> busties = bustiaRepository.findByEntitatAndUnitatCodiAndPareNotNull(entitat, unitatCodi);
		List<BustiaDto> resposta = toBustiaDto(
				busties,
				true,
				false);
		omplirPermisosPerBusties(resposta, true);
		return resposta;
	}

	@Override
	@Transactional(readOnly = true)
	public PaginaDto<BustiaDto> findAmbFiltreAdmin(
			Long entitatId,
			BustiaFiltreDto filtre,
			PaginacioParamsDto paginacioParams) {
		logger.debug("Cercant les bústies segons el filtre ("
				+ "entitatId=" + entitatId + ", "
				+ "filtre=" + filtre + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		PaginaDto<BustiaDto> resultPagina =  paginacioHelper.toPaginaDto(
				bustiaRepository.findByEntitatAndUnitatCodiAndBustiaNomFiltrePaginat(
						entitat,
						filtre.getUnitatCodi() == null || filtre.getUnitatCodi().isEmpty(), 
						filtre.getUnitatCodi(),
						filtre.getNom() == null || filtre.getNom().isEmpty(), 
						filtre.getNom(),
						paginacioHelper.toSpringDataPageable(paginacioParams)),
				BustiaDto.class,
				new Converter<BustiaEntity, BustiaDto>() {
					@Override
					public BustiaDto convert(BustiaEntity source) {
						return toBustiaDto(
								source,
								false,
								true);
					}
				});
		omplirPermisosPerBusties(resultPagina.getContingut(), true);
		return resultPagina;
	}

	@Override
	@Transactional
	public List<BustiaDto> findActivesAmbEntitat(
			Long entitatId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Cercant bústies actives de l'entitat ("
				+ "entitatId=" + entitatId + ", "
				+ "usuariCodi=" + auth.getName() + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				false);
		List<BustiaEntity> busties = bustiaRepository.findByEntitatAndActivaTrueAndPareNotNull(entitat);
		return toBustiaDto(
				busties,
				false,
				false);
	}

	@Override
	@Transactional(readOnly = true)
	public PaginaDto<BustiaDto> findPermesesPerUsuari(
			Long entitatId,
			PaginacioParamsDto paginacioParams) {
		logger.debug("Consulta de busties permeses per un usuari ("
				+ "entitatId=" + entitatId + ", "
				+ "paginacioParams=" + paginacioParams + ")");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		// Obté la llista d'id's amb permisos per a l'usuari
		List<BustiaEntity> busties = bustiaRepository.findByEntitatAndActivaTrueAndPareNotNull(entitat);
		// Filtra la llista de bústies segons els permisos
		permisosHelper.filterGrantedAll(
				busties,
				new ObjectIdentifierExtractor<BustiaEntity>() {
					@Override
					public Long getObjectIdentifier(BustiaEntity bustia) {
						return bustia.getId();
					}
				},
				BustiaEntity.class,
				new Permission[] {ExtendedPermission.READ},
				auth);
		if (busties.isEmpty()) {
			return paginacioHelper.getPaginaDtoBuida(BustiaDto.class);
		}
		List<Long> bustiaIds = new ArrayList<Long>();
		for (BustiaEntity bustia: busties) {
			bustiaIds.add(bustia.getId());
		}
		// Realitza la consulta
		Page<BustiaEntity> pagina = bustiaRepository.findByEntitatAndIdsAndFiltrePaginat(
				entitat,
				bustiaIds,
				paginacioParams.getFiltre() == null,
				paginacioParams.getFiltre(),
				paginacioHelper.toSpringDataPageable(
						paginacioParams));
		return paginacioHelper.toPaginaDto(
				pagina,
				BustiaDto.class,
				new Converter<BustiaEntity, BustiaDto>() {
					@Override
					public BustiaDto convert(BustiaEntity source) {
						return toBustiaDto(
								source,
								true,
								true);
					}
				});
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<BustiaDto> findPermesesPerUsuari(
			Long entitatId) {
		logger.debug("Consulta de busties permeses per un usuari ("
				+ "entitatId=" + entitatId + ")");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		// Obté la llista d'id's amb permisos per a l'usuari
		List<BustiaEntity> busties = bustiaRepository.findByEntitatAndActivaTrueAndPareNotNull(entitat);
		// Filtra la llista de bústies segons els permisos
		permisosHelper.filterGrantedAll(
				busties,
				new ObjectIdentifierExtractor<BustiaEntity>() {
					@Override
					public Long getObjectIdentifier(BustiaEntity bustia) {
						return bustia.getId();
					}
				},
				BustiaEntity.class,
				new Permission[] {ExtendedPermission.READ},
				auth);
		List<BustiaDto> bustiesRetorn = toBustiaDto(busties, true, true);
		
		return bustiesRetorn;
	}

	@Transactional
	@Override
	public ContingutDto enviarContingut(
			Long entitatId,
			Long bustiaId,
			Long contingutId,
			String comentari) {
		logger.debug("Enviant contingut a bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "bustiaId=" + bustiaId + ", "
				+ "contingutId=" + contingutId + ","
				+ "comentari=" + comentari + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		// Comprova que el contingutOrigen arrel és l'escriptori de l'usuari actual
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				contingut);
		// Comprova que aquest contingut no pertanyi a cap expedient
		ExpedientEntity expedientActual = contingutHelper.getExpedientSuperior(
				contingut,
				true,
				false,
				false);
		if (expedientActual != null) {
			logger.error("No es pot enviar un node que pertany a un expedient");
			throw new ValidationException(
					contingutId,
					ContingutEntity.class,
					"No es pot enviar un node que pertany a un expedient");
		}
		// Comprova l'accés al path del contingutOrigen
		contingutHelper.comprovarPermisosPathContingut(
				contingut,
				true,
				false,
				false,
				true);
		// Comprova la bústia
		BustiaEntity bustia = entityComprovarHelper.comprovarBustia(
				entitat,
				bustiaId,
				true);
		// Fa l'enviament
		ContingutMovimentEntity contingutMoviment = contingutHelper.ferIEnregistrarMoviment(
				contingut,
				bustia,
				comentari);
		// Registra al log l'enviament del contingut
		contingutLogHelper.log(
				contingut,
				LogTipusEnumDto.ENVIAMENT,
				contingutMoviment,
				true,
				true);
		// Avisam per correu als responsables de la bústia de destí
		emailHelper.emailBustiaPendentContingut(
				bustia,
				contingut,
				contingutMoviment);
		// Refrescam cache usuaris bústia de destí
		bustiaHelper.evictElementsPendentsBustia(
				entitat,
				bustia);
		return contingutHelper.toContingutDto(
				contingut,
				true,
				false,
				false,
				false,
				false,
				false,
				false);
	}

	@Transactional
	@Override
	public void registreAnotacioCrear(
			String entitatUnitatCodi,
			RegistreTipusEnum tipus,
			String unitatOrganitzativa,
			RegistreAnotacio anotacio) {
		logger.debug("Creant anotació de registre a la bústia ("
				+ "entitatUnitatCodi=" + entitatUnitatCodi + ", "
				+ "tipus=" + tipus + ", "
				+ "unitatOrganitzativa=" + unitatOrganitzativa + ","
				+ "anotacio=" + anotacio.getIdentificador() + ")");
		EntitatEntity entitatPerUnitat = entitatRepository.findByUnitatArrel(entitatUnitatCodi);
		if (entitatPerUnitat == null) {
			throw new NotFoundException(
					entitatUnitatCodi,
					EntitatEntity.class);
		}
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatPerUnitat.getId(),
				false,
				false,
				false);
		BustiaEntity bustia = bustiaHelper.findBustiaDesti(
				entitat,
				unitatOrganitzativa);
		RegistreEntity registreRepetit = registreRepository.findByEntitatCodiAndLlibreCodiAndRegistreTipusAndNumeroAndData(
				anotacio.getEntitatCodi(),
				anotacio.getLlibreCodi(),
				RegistreTipusEnum.ENTRADA.getValor(),
				anotacio.getNumero(),
				anotacio.getData());
		if (registreRepetit != null) {
			throw new ValidationException(
					"Aquesta anotació ja ha estat donada d'alta a l'aplicació (" +
					"entitatCodi=" + anotacio.getEntitatCodi() + ", " +
					"llibreCodi=" + anotacio.getLlibreCodi() + ", " +
					"tipus=" + RegistreTipusEnum.ENTRADA.getValor() + ", " +
					"numero=" + anotacio.getNumero() + ", " +
					"data=" + anotacio.getData() + ")");
		}
		
		if (anotacio.getJustificant() != null && anotacio.getJustificant().getFitxerArxiuUuid() == null) {
			throw new ValidationException(
					"El justificant adjuntat no conté un uuid (" +
					"entitatCodi=" + anotacio.getEntitatCodi() + ", " +
					"llibreCodi=" + anotacio.getLlibreCodi() + ", " +
					"tipus=" + RegistreTipusEnum.ENTRADA.getValor() + ", " +
					"numero=" + anotacio.getNumero() + ", " +
					"data=" + anotacio.getData() + ")");
		}
		
		ReglaEntity reglaAplicable = reglaHelper.findAplicable(
				entitat,
				unitatOrganitzativa,
				anotacio);
		RegistreEntity anotacioEntity = registreHelper.toRegistreEntity(
				entitat,
				tipus,
				unitatOrganitzativa,
				anotacio,
				reglaAplicable);
		registreRepository.saveAndFlush(anotacioEntity);
		
		if (anotacioEntity.getAnnexos() != null && anotacioEntity.getAnnexos().size() > 0) {
			ContingutArxiu expedientCreat = crearExpedientArxiuTemporal(anotacioEntity, bustia);
			anotacioEntity.updateExpedientArxiuUuid(expedientCreat.getIdentificador());
			registreRepository.saveAndFlush(anotacioEntity);
			processarAnnexos(anotacioEntity, bustia, expedientCreat);
		}
		
		contingutLogHelper.log(
				anotacioEntity,
				LogTipusEnumDto.CREACIO,
				anotacioEntity.getNom(),
				null,
				false,
				false);
		contingutHelper.ferIEnregistrarMoviment(
				anotacioEntity,
				bustia,
				null);
		bustiaHelper.evictElementsPendentsBustia(
				bustia.getEntitat(),
				bustia);
	}

	@Transactional
	@Override
	public void registreAnotacioCrear(
			String entitatUnitatCodi,
			String registreReferencia) {
		RegistreAnotacioResposta resposta = pluginHelper.registreEntradaConsultar(
				registreReferencia,
				entitatUnitatCodi);
		registreAnotacioCrear(
				entitatUnitatCodi,
				resposta.getTipus(),
				resposta.getUnitatAdministrativa(),
				resposta.getRegistreAnotacio());
	}

	@Transactional(readOnly = true)
	@Override
	public PaginaDto<BustiaContingutDto> contingutPendentFindByDatatable(
			Long entitatId,
			List<BustiaDto> bustiesUsuari,
			BustiaUserFiltreDto filtre,
			PaginacioParamsDto paginacioParams) {
		logger.debug("Consultant el contingut de l'usuari ("
				+ "entitatId=" + entitatId + ", "
				+ "bustiaId=" + filtre.getBustia() + ", "
				+ "contingutDescripcio=" + filtre.getContingutDescripcio() + ", "
				+ "remitent=" + filtre.getRemitent() + ", "
				+ "dataRecepcioInici=" + filtre.getDataRecepcioInici() + ", "
				+ "dataRecepcioFi=" + filtre.getDataRecepcioFi() + ", "
				+ "estatContingut=" + filtre.getEstatContingut() + ", "
				+ "paginacioParams=" + paginacioParams + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		// Comprova la bústia i que l'usuari hi tengui accés
		BustiaEntity bustia = null;
		if (filtre.getBustia() != null && !filtre.getBustia().isEmpty())
			bustia = entityComprovarHelper.comprovarBustia(
					entitat,
					new Long(filtre.getBustia()),
					true);
		
		List<ContingutEntity> pares = new ArrayList<ContingutEntity>();
		if (bustiesUsuari != null && !bustiesUsuari.isEmpty())
			for (BustiaDto bustiaUsuari: bustiesUsuari) {
				pares.add(
						entityComprovarHelper.comprovarBustia(
						entitat,
						new Long(bustiaUsuari.getId()),
						true));
			}
		else
			if (bustia != null)
				pares.add(bustia);
		
		Map<String, String[]> mapeigOrdenacio = new HashMap<String, String[]>();
		mapeigOrdenacio.put(
				"recepcioData",
				new String[] {"darrerMoviment.createdDate"});
		mapeigOrdenacio.put(
				"remitent",
				new String[] {"darrerMoviment.remitent.nom"});
		mapeigOrdenacio.put(
				"comentari",
				new String[] {"darrerMoviment.comentari"});
		
		Page<ContingutEntity> pagina = contingutRepository.findBustiaPendentByPareAndFiltre(
				(bustia == null),
				bustia,
				pares,
				filtre.getContingutDescripcio() == null || filtre.getContingutDescripcio().isEmpty(),
				filtre.getContingutDescripcio(),
				filtre.getRemitent() == null || filtre.getRemitent().isEmpty(),
				filtre.getRemitent(),
				(filtre.getDataRecepcioInici() == null),
				filtre.getDataRecepcioInici(),
				(filtre.getDataRecepcioFi() == null),
				filtre.getDataRecepcioFi(),
				(filtre.getEstatContingut() == null),
				(filtre.getEstatContingut() != null ? filtre.getEstatContingut().ordinal() : 1),
				paginacioParams.getFiltre() == null || paginacioParams.getFiltre().isEmpty(),
				paginacioParams.getFiltre(),
				paginacioHelper.toSpringDataPageable(
						paginacioParams,
						mapeigOrdenacio));
		return paginacioHelper.toPaginaDto(
				pagina,
				BustiaContingutDto.class,
				new Converter<ContingutEntity, BustiaContingutDto>() {
					@Override
					public BustiaContingutDto convert(ContingutEntity source) {
						return toBustiaContingutDto(source);
					}
				});
	}

	@Transactional(readOnly = true)
	@Override
	public BustiaContingutDto contingutPendentFindOne(
			Long entitatId,
			Long bustiaId,
			Long contingutId) {
		logger.debug("Consultant un contingut pendent de la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "bustiaId=" + bustiaId + ", "
				+ "contingutId=" + contingutId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		// Comprova la bústia i que l'usuari hi tengui accés
		BustiaEntity bustia = entityComprovarHelper.comprovarBustia(
				entitat,
				bustiaId,
				true);
		ContingutEntity contingutPendent = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				bustia);
		return toBustiaContingutDto(contingutPendent);
	}

	@Transactional(readOnly = true)
	@Override
	public long contingutPendentBustiesAllCount(
			Long entitatId) {
		logger.debug("Consultant els elements pendents a totes les busties ("
				+ "entitatId=" + entitatId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return cacheHelper.countElementsPendentsBustiesUsuari(
				entitat,
				auth.getName());
	}

	@Transactional
	@Override
	public void contingutPendentReenviar(
			Long entitatId,
			Long bustiaOrigenId,
			Long bustiaDestiId,
			Long contingutId,
			String comentari) throws NotFoundException {
		logger.debug("Reenviant contingut pendent de la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "bustiaOrigenId=" + bustiaOrigenId + ", "
				+ "bustiaDestiId=" + bustiaDestiId + ", "
				+ "contingutId=" + contingutId + ", "
				+ "comentari=" + comentari + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		BustiaEntity bustiaOrigen = entityComprovarHelper.comprovarBustia(
				entitat,
				bustiaOrigenId,
				true);
		BustiaEntity bustiaDesti = entityComprovarHelper.comprovarBustia(
				entitat,
				bustiaDestiId,
				false);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				bustiaOrigen);
		if (contingut instanceof RegistreEntity) {
			RegistreEntity registre = (RegistreEntity)contingut;
			if (registre.getRegla() != null) {
				throw new ValidationException(
						contingutId,
						ContingutEntity.class,
						"Aquest contingut pendent no es pot moure perquè te activat el processament automàtic mitjançant una regla (reglaId=" + registre.getRegla().getId() + ")");
			}
		}
		ContingutMovimentEntity contingutMoviment = contingutHelper.ferIEnregistrarMoviment(
				contingut,
				bustiaDesti,
				comentari);
		// Registra al log l'enviament del contingut
		contingutLogHelper.log(
				contingut,
				LogTipusEnumDto.REENVIAMENT,
				contingutMoviment,
				true,
				true);
		// Avisam per correu als responsables de la bústia de destí
		emailHelper.emailBustiaPendentContingut(
				bustiaDesti,
				contingut,
				contingutMoviment);
		// Refrescam cache d'elements pendents de les bústies
		bustiaHelper.evictElementsPendentsBustia(
				entitat,
				bustiaOrigen);
		bustiaHelper.evictElementsPendentsBustia(
				entitat,
				bustiaDesti);
	}

	@Transactional(readOnly = true)
	@Override
	public ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzatives(
			Long entitatId,
			boolean nomesBusties,
			boolean nomesBustiesPermeses,
			boolean comptarElementsPendents) {
		logger.debug("Consulta de l'arbre d'unitats organitzatives ("
				+ "entitatId=" + entitatId + ", "
				+ "nomesBusties=" + nomesBusties + ", "
				+ "nomesBustiesPermeses=" + nomesBustiesPermeses + ", "
				+ "comptarElementsPendents=" + comptarElementsPendents + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		return bustiaHelper.findArbreUnitatsOrganitzatives(
				entitat,
				nomesBusties,
				nomesBustiesPermeses,
				comptarElementsPendents);
	}

	@Override
	@Transactional
	public void updatePermis(
			Long entitatId,
			Long id,
			PermisDto permis) {
		logger.debug("Actualitzant permis per a la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "permis=" + permis + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		BustiaEntity bustia = entityComprovarHelper.comprovarBustia(
				entitat,
				id,
				false);
		permisosHelper.updatePermis(
				id,
				BustiaEntity.class,
				permis);
		bustiaHelper.evictElementsPendentsBustia(
				entitat,
				bustia);
	}

	@Override
	@Transactional
	public void deletePermis(
			Long entitatId,
			Long id,
			Long permisId) {
		logger.debug("Esborrant permis per a la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "permisId=" + permisId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		BustiaEntity bustia = entityComprovarHelper.comprovarBustia(
				entitat,
				id,
				false);
		permisosHelper.deletePermis(
				id,
				BustiaEntity.class,
				permisId);
		bustiaHelper.evictElementsPendentsBustia(
				entitat,
				bustia);
	}



	private BustiaDto toBustiaDto(
			BustiaEntity bustia,
			boolean ambFills,
			boolean filtrarFillsSegonsPermisRead) {
		return (BustiaDto)contingutHelper.toContingutDto(
				bustia,
				false,
				ambFills,
				filtrarFillsSegonsPermisRead,
				false,
				true,
				false,
				false);
	}
	private List<BustiaDto> toBustiaDto(
			List<BustiaEntity> busties,
			boolean ambFills,
			boolean filtrarFillsSegonsPermisRead) {
		List<BustiaDto> resposta = new ArrayList<BustiaDto>();
		for (BustiaEntity bustia: busties) {
			resposta.add(
					toBustiaDto(
							bustia,
							ambFills,
							filtrarFillsSegonsPermisRead));
		}
		return resposta;
	}

	private void omplirPermisosPerBusties(
			List<? extends BustiaDto> busties,
			boolean ambLlistaPermisos) {
		// Filtra les entitats per saber els permisos per a l'usuari actual
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<BustiaDto> bustiesRead = new ArrayList<BustiaDto>();
		bustiesRead.addAll(busties);
		permisosHelper.filterGrantedAll(
				bustiesRead,
				new ObjectIdentifierExtractor<BustiaDto>() {
					public Long getObjectIdentifier(BustiaDto bustia) {
						return bustia.getId();
					}
				},
				BustiaEntity.class,
				new Permission[] {ExtendedPermission.READ},
				auth);
		for (BustiaDto bustia: busties) {
			bustia.setUsuariActualRead(
					bustiesRead.contains(bustia));
		}
		// Obté els permisos per a totes les bústies només amb una consulta
		if (ambLlistaPermisos) {
			List<Long> ids = new ArrayList<Long>();
			for (BustiaDto bustia: busties)
				ids.add(bustia.getId());
			Map<Long, List<PermisDto>> permisos = permisosHelper.findPermisos(
					ids,
					BustiaEntity.class);
			for (BustiaDto bustia: busties)
				bustia.setPermisos(permisos.get(bustia.getId()));
		}
	}

	private BustiaContingutDto toBustiaContingutDto(
			ContingutEntity contingut) {
		Object deproxied = HibernateHelper.deproxy(contingut);
		BustiaContingutDto bustiaContingut = new BustiaContingutDto();
		bustiaContingut.setId(contingut.getId());
		bustiaContingut.setNom(contingut.getNom());
		
		List<ContingutDto> path = contingutHelper.getPathContingutComDto(
				contingut,
				false,
				false);
		
		bustiaContingut.setPath(path);
		
		BustiaDto pare = toBustiaDto((BustiaEntity)(contingut.getPare()), false, false);
		bustiaContingut.setPareId(pare.getId());
		if (contingut.getEsborrat() < 2)
			bustiaContingut.setEstatContingut(BustiaContingutFiltreEstatEnumDto.values()[contingut.getEsborrat()]);
		if (deproxied instanceof RegistreEntity) {
			RegistreEntity anotacio = (RegistreEntity)contingut;
			bustiaContingut.setTipus(BustiaContingutPendentTipusEnumDto.REGISTRE);
			bustiaContingut.setRecepcioData(anotacio.getCreatedDate().toDate());
			if (RegistreProcesEstatEnum.ERROR.equals(anotacio.getProcesEstat())) {
				bustiaContingut.setError(true);
			}
			bustiaContingut.setProcesAutomatic(anotacio.getRegla() != null);
		} else if (deproxied instanceof ExpedientEntity) {
			bustiaContingut.setTipus(BustiaContingutPendentTipusEnumDto.EXPEDIENT);
		} else if (deproxied instanceof ExpedientEntity) {
			bustiaContingut.setTipus(BustiaContingutPendentTipusEnumDto.DOCUMENT);
		}
		if (contingut.getDarrerMoviment() != null) {
			if (contingut.getDarrerMoviment().getRemitent() != null)
				bustiaContingut.setRemitent(contingut.getDarrerMoviment().getRemitent().getNom());
			if (contingut.getDarrerMoviment().getCreatedDate() != null)
				bustiaContingut.setRecepcioData(contingut.getDarrerMoviment().getCreatedDate().toDate());
			bustiaContingut.setComentari(contingut.getDarrerMoviment().getComentari());
		}
		
		bustiaContingut.setNumComentaris(contingutComentariRepository.countByContingut(contingut));
		
		return bustiaContingut;
	}

	private ContingutArxiu crearExpedientArxiuTemporal(RegistreEntity anotacio, BustiaEntity bustia) {
		
			ContingutArxiu expedientCreat = pluginHelper.arxiuExpedientPerAnotacioCrear(
					anotacio, 
					bustia);
			
			return expedientCreat;
	}
	
	private void processarAnnexos(RegistreEntity anotacio, BustiaEntity bustia, ContingutArxiu expedientCreat) {
		try {
			for (RegistreAnnexEntity annex: anotacio.getAnnexos()) {
				//si tenim contingut de fitxer i també referència del registre, hem de tornar una excepció
				if (annex.getFitxerArxiuUuid() == null && annex.getFitxerContingut() == null) {
					throw new ValidationException(
							"S'ha d'especificar o bé la referència del document o el contingut del document"
							+ " per l'annex [" + annex.getTitol() + "]");
				} else {
					guardarDocumentAnnex (annex, bustia, expedientCreat);
				}
				// Si l'anotació té una regla sistra llavors intenta extreure la informació dels identificadors
				// del tràmit i del procediment de l'annex
				if (anotacio.getRegla() != null 
						&& anotacio.getRegla().getTipus() == ReglaTipusEnumDto.BACKOFFICE
						&& anotacio.getRegla().getBackofficeTipus() == BackofficeTipusEnumDto.SISTRA) {
					if (annex.getFitxerNom().equals("DatosPropios.xml")
							|| annex.getFitxerNom().equals("Asiento.xml"))
						processarAnnexSistra(anotacio, annex);
				}
			}
		} catch (Exception ex) {
			try {
				eliminarContingutExistent(anotacio);
			} catch (Exception ex2) {
				logger.error(
						"Error al eliminar els continguts relacionats amb els annexos de l'anotació de registre",
						ex2);
			}
			throw new BustiaServiceException(
					"Error al processar els annexos de l'anotació de registre",
					ex);
		}
	}

	/*
	 * Mètode privat per obrir el document annex de tipus sistra i extreure'n
	 * informació per a l'anotació de registre. La informació que es pot extreure
	 * depén del document:
	 * - Asiento.xml: ASIENTO_REGISTRAL.DATOS_ASUNTO.IDENTIFICADOR_TRAMITE (VARCHAR2(20))
	 * - DatosPropios.xml: DATOS_PROPIOS.INSTRUCCIONES.IDENTIFICADOR_PROCEDIMIENTO (VARCHAR2(100))
	 * 
	 * @param anotacio 
	 * 			Anotació del registre
	 * @param annex
	 * 			Document annex amb el contingut per a llegir.
	 */
	private void processarAnnexSistra(RegistreEntity anotacio, RegistreAnnexEntity annex) {
		try {
			org.w3c.dom.Document doc = XmlHelper.getDocumentFromContent(annex.getFitxerContingut());
			if (annex.getFitxerNom().equals("DatosPropios.xml")) {
				String identificadorProcediment = XmlHelper.getNodeValue(doc.getDocumentElement(), "INSTRUCCIONES.IDENTIFICADOR_PROCEDIMIENTO");
				anotacio.updateIdentificadorProcedimentSistra(identificadorProcediment);
			} else if (annex.getFitxerNom().equals("Asiento.xml")) {
				String identificadorTramit = XmlHelper.getNodeValue(doc.getDocumentElement(), "DATOS_ASUNTO.IDENTIFICADOR_TRAMITE");
				anotacio.updateIdentificadorTramitSistra(identificadorTramit);
			}		
		} catch (Exception e) {
			logger.error(
					"Error processant l'annex per l'anotació amb regla backoffice SISTRA " + annex.getFitxerNom(),
					e);
		}
	}

	private void guardarDocumentAnnex(
			RegistreAnnexEntity annex,
			BustiaEntity bustia,
			ContingutArxiu expedientCreat) throws IOException {
		byte[] contingut = null;
		if (annex.getFitxerContingut() != null) {
			contingut = annex.getFitxerContingut();
		} else {
			Document arxiuDocument = pluginHelper.arxiuDocumentConsultar(
					bustia,
					annex.getFitxerArxiuUuid(),
					null,
					true);
			if (arxiuDocument.getContingut() != null) {
				contingut = arxiuDocument.getContingut().getContingut();
			} else {
				throw new ValidationException(
						"No s'ha trobat cap contingut per l'annex (" +
						"uuid=" + annex.getFitxerArxiuUuid() + ")");
			}
		}
		
		FitxerDto fitxer = new FitxerDto();
		fitxer.setNom(annex.getFitxerNom());
		fitxer.setContingut(contingut);
		fitxer.setContentType(annex.getFitxerTipusMime());
		fitxer.setTamany(contingut.length);
		
		String uuidDocument = pluginHelper.arxiuDocumentAnnexCrear(annex, bustia, fitxer, expedientCreat);
		annex.updateFitxerArxiuUuid(uuidDocument);
	}

	private void eliminarContingutExistent(
			RegistreEntity anotacio) throws IOException {
		String pathName = PropertiesHelper.getProperties().getProperty("es.caib.ripea.bustia.contingut.documents.dir");
		for (RegistreAnnexEntity annex: anotacio.getAnnexos()) {
			File doc = new File(pathName + "/" + annex.getId() + "_d." + annex.getFitxerTipusMime());
			if (doc != null)
				Files.deleteIfExists(doc.toPath());
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(BustiaServiceImpl.class);

}
