/**
 * 
 */
package es.caib.ripea.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.namespace.QName;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.ArbreDto;
import es.caib.ripea.core.api.dto.BustiaContingutPendentDto;
import es.caib.ripea.core.api.dto.BustiaContingutPendentTipusEnumDto;
import es.caib.ripea.core.api.dto.BustiaDto;
import es.caib.ripea.core.api.dto.ContingutDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.dto.RegistreAnotacioDto;
import es.caib.ripea.core.api.dto.ReglaTipusEnumDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.registre.RegistreAnnex;
import es.caib.ripea.core.api.registre.RegistreAnnexElaboracioEstatEnum;
import es.caib.ripea.core.api.registre.RegistreAnnexNtiTipusDocumentEnum;
import es.caib.ripea.core.api.registre.RegistreAnnexOrigenEnum;
import es.caib.ripea.core.api.registre.RegistreAnnexSicresTipusDocumentEnum;
import es.caib.ripea.core.api.registre.RegistreAnotacio;
import es.caib.ripea.core.api.registre.RegistreInteressat;
import es.caib.ripea.core.api.registre.RegistreInteressatCanalEnum;
import es.caib.ripea.core.api.registre.RegistreInteressatDocumentTipusEnum;
import es.caib.ripea.core.api.registre.RegistreInteressatTipusEnum;
import es.caib.ripea.core.api.registre.RegistreProcesEstatEnum;
import es.caib.ripea.core.api.registre.RegistreTipusEnum;
import es.caib.ripea.core.api.service.BustiaService;
import es.caib.ripea.core.api.service.ws.RipeaBackofficeResultatProces;
import es.caib.ripea.core.api.service.ws.RipeaBackofficeWsService;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.ContingutMovimentEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.RegistreAnnexEntity;
import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.entity.RegistreInteressatEntity;
import es.caib.ripea.core.entity.ReglaEntity;
import es.caib.ripea.core.helper.BustiaHelper;
import es.caib.ripea.core.helper.CacheHelper;
import es.caib.ripea.core.helper.ContingutHelper;
import es.caib.ripea.core.helper.ContingutLogHelper;
import es.caib.ripea.core.helper.EmailHelper;
import es.caib.ripea.core.helper.EntityComprovarHelper;
import es.caib.ripea.core.helper.PaginacioHelper;
import es.caib.ripea.core.helper.PaginacioHelper.Converter;
import es.caib.ripea.core.helper.PermisosHelper;
import es.caib.ripea.core.helper.PermisosHelper.ObjectIdentifierExtractor;
import es.caib.ripea.core.helper.PluginHelper;
import es.caib.ripea.core.helper.UnitatOrganitzativaHelper;
import es.caib.ripea.core.helper.UsuariHelper;
import es.caib.ripea.core.helper.WsClientHelper;
import es.caib.ripea.core.repository.BustiaRepository;
import es.caib.ripea.core.repository.EntitatRepository;
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
	private EntityComprovarHelper entityComprovarHelper;
	@Resource
	private UnitatOrganitzativaHelper unitatOrganitzativaHelper;
	@Resource
	private PaginacioHelper paginacioHelper;



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
		// Si no hi ha cap bústia per defecte a dins l'unitat configura
		// la bústia actual com a bústia per defecte
		BustiaEntity bustiaPerDefecte = bustiaRepository.findByEntitatAndUnitatCodiAndPerDefecteTrue(
				entitat,
				bustia.getUnitatCodi());
		if (bustiaPerDefecte == null) {
			entity.updatePerDefecte(true);
		}
		return toBustiaDto(
				bustiaRepository.save(entity));
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
		entity.update(
				bustia.getNom());
		return toBustiaDto(entity);
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
		return toBustiaDto(entity);
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
			logger.error("No es pot esborrar la bústia per defecte (bustiaId=" + id + ")");
			throw new NotFoundException(
					id,
					BustiaEntity.class);
		}
		bustiaRepository.delete(bustia);
		return toBustiaDto(bustia);
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
		for (BustiaEntity bu: bustiesMateixaUnitat)
			bu.updatePerDefecte(false);
		bustia.updatePerDefecte(true);
		return toBustiaDto(bustia);
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
		BustiaDto resposta = toBustiaDto(bustia);
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
		return toBustiaDto(
				busties,
				true,
				false);
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
	public PaginaDto<BustiaDto> findAmbEntitatPaginat(
			Long entitatId,
			PaginacioParamsDto paginacioParams) {
		logger.debug("Consulta de busties amb paginació ("
				+ "entitatId=" + entitatId + ", "
				+ "paginacioParams=" + paginacioParams + ")");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		// Obté la llista d'id's amb permisos per a l'usuari
		List<BustiaEntity> busties = bustiaRepository.findByEntitatAndPareNotNull(entitat);
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
		List<Long> bustiaIds = new ArrayList<Long>();
		for (BustiaEntity bustia : busties) {
			bustiaIds.add(bustia.getId());
		}
		// Realitza la consulta
		Map<String, String> mapeigPropietatsOrdenacio = new HashMap<String, String>();
		mapeigPropietatsOrdenacio.put("pareNom", "pare.nom");
		Page<BustiaEntity> pagina = bustiaRepository.findByEntitatAndIdsAndFiltrePaginat(
				entitat,
				bustiaIds,
				paginacioParams.getFiltre() == null,
				paginacioParams.getFiltre(),
				paginacioHelper.toSpringDataPageable(
						paginacioParams,
						mapeigPropietatsOrdenacio));
		return paginacioHelper.toPaginaDto(
				pagina,
				BustiaDto.class,
				new Converter<BustiaEntity, BustiaDto>() {
					@Override
					public BustiaDto convert(BustiaEntity source) {
						return (BustiaDto)contingutHelper.toContingutDto(
								source,
								false,
								true,
								true,
								false,
								false,
								false);
					}
				});					
	}

	@Override
	@Transactional
	public List<BustiaDto> findPermesesPerUsuari(
			Long entitatId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Cercant bústies permeses per a l'usuari ("
				+ "entitatId=" + entitatId + ", "
				+ "usuariCodi=" + auth.getName() + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		List<BustiaEntity> busties = bustiaRepository.findByEntitatAndPareNotNull(
				entitat);
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
		// Converteix a DTO
		List<BustiaDto> resposta = toBustiaDto(
				busties,
				false,
				false);
		// Ompl els permisos
		omplirPermisosPerBusties(resposta, true);
		// Ompl les unitats
		List<UnitatOrganitzativaDto> unitats = cacheHelper.findUnitatsOrganitzativesPerEntitat(
				entitat.getCodi()).toDadesList();
		for (BustiaDto bustia: resposta) {
			for (UnitatOrganitzativaDto unitat: unitats) {
				if (unitat.getCodi().equals(bustia.getUnitatCodi())) {
					bustia.setUnitat(unitat);
					break;
				}
			}
		}
		return resposta;
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
				true);
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
				null,
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
				false);
	}

	@Transactional
	@Override
	public void registreAnotacioCrear(
			String entitatUnitatCodi,
			RegistreTipusEnum tipus,
			String unitatAdministrativa,
			RegistreAnotacio anotacio) {
		logger.debug("Creant anotació de registre a la bústia ("
				+ "entitatUnitatCodi=" + entitatUnitatCodi + ", "
				+ "tipus=" + tipus + ", "
				+ "unitatAdministrativa=" + unitatAdministrativa + ","
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
		UnitatOrganitzativaDto unitat = unitatOrganitzativaHelper.findPerEntitatAndCodi(
				entitat.getCodi(),
				unitatAdministrativa);
		if (unitat == null) {
			throw new NotFoundException(
					unitatAdministrativa,
					UnitatOrganitzativaDto.class);
		}
		BustiaEntity bustia = bustiaHelper.findAndCreateIfNotExistsBustiaPerDefectePerUnitatAdministrativa(
				entitat,
				unitat);
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
		ReglaEntity reglaAplicable = findReglaAplicable(
				entitat,
				unitatAdministrativa,
				anotacio);
		RegistreEntity anotacioEntity = toRegistreEntity(
				tipus,
				unitatAdministrativa,
				anotacio,
				bustia,
				reglaAplicable);
		registreRepository.save(anotacioEntity);
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
	public List<BustiaContingutPendentDto> contingutPendentFindByBustiaId(
			Long entitatId,
			Long bustiaId) {
		logger.debug("Consultant el contingut pendent de la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "bustiaId=" + bustiaId + ")");
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
		ContingutDto bustiaDto = contingutHelper.toContingutDto(
				bustia,
				false,
				true,
				true,
				false,
				false,
				false);
		List<BustiaContingutPendentDto> pendents = new ArrayList<BustiaContingutPendentDto>();
		for (ContingutDto fill: bustiaDto.getFills()) {
			boolean afegir = true;
			if (fill.isRegistre()) {
				RegistreAnotacioDto anotacio = (RegistreAnotacioDto)fill;
				if (!RegistreProcesEstatEnum.NO_PROCES.equals(anotacio.getProcesEstat()) && !RegistreProcesEstatEnum.ERROR.equals(anotacio.getProcesEstat())) {
					afegir = false;
				}
			}
			if (afegir) {
				pendents.add(
						toContingutPendentDto(fill));
			}
		}
		return pendents;
	}

	@Transactional(readOnly = true)
	@Override
	public BustiaContingutPendentDto contingutPendentFindOne(
			Long entitatId,
			Long bustiaId,
			BustiaContingutPendentTipusEnumDto contingutTipus,
			Long contingutId) {
		logger.debug("Consultant un contingut pendent de la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "bustiaId=" + bustiaId + ", "
				+ "contingutTipus=" + contingutTipus + ", "
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
		ContingutDto bustiaDto = contingutHelper.toContingutDto(
				bustia,
				false,
				true,
				true,
				false,
				false,
				false);
		for (ContingutDto fill: bustiaDto.getFills()) {
			if (fill.getId().equals(contingutId)) {
				return toContingutPendentDto(fill);
			}
		}
		throw new NotFoundException(
				contingutTipus.toString() + " [" + contingutId + "]",
				BustiaContingutPendentDto.class);
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
			BustiaContingutPendentTipusEnumDto contingutTipus,
			Long contingutId,
			String comentari) throws NotFoundException {
		logger.debug("Reenviant contingut pendent de la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "bustiaOrigenId=" + bustiaOrigenId + ", "
				+ "bustiaDestiId=" + bustiaDestiId + ", "
				+ "contingutTipus=" + contingutTipus + ", "
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
		if (contingutTipus != BustiaContingutPendentTipusEnumDto.REGISTRE) {
			ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
					entitat,
					contingutId,
					bustiaOrigen);
			ContingutMovimentEntity contingutMoviment = contingutHelper.ferIEnregistrarMoviment(
					contingut,
					bustiaDesti,
					comentari);
			// Registra al log l'enviament del contingut
			contingutLogHelper.log(
					contingut,
					LogTipusEnumDto.MOVIMENT,
					null,
					contingutMoviment,
					true,
					true);
			// Avisam per correu als responsables de la bústia de destí
			emailHelper.emailBustiaPendentContingut(
					bustiaDesti,
					contingut,
					contingutMoviment);
		} else {
			RegistreEntity registre = entityComprovarHelper.comprovarRegistre(
					contingutId,
					bustiaOrigen);
			// Fa l'enviament
			registre.updatePare(bustiaDesti);
			contingutHelper.ferIEnregistrarMoviment(
					registre.getPare(),
					bustiaDesti,
					null);
			// TODO Notificar el moviment a l'aplicació de registre
			// Avisam per correu als responsables de la bústia de destí
			emailHelper.emailBustiaPendentRegistre(
					bustiaDesti,
					registre);
		}
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

	@Override
	@Transactional
	@Async
	@Scheduled(fixedRate=10000)
	public void registreReglaAplicarPendents() {
		logger.debug("Aplicant regles a les anotacions pendents");
		List<RegistreEntity> pendents = registreRepository.findByReglaNotNullAndProcesEstatOrderByCreatedDateAsc(
				RegistreProcesEstatEnum.PENDENT);
		if (!pendents.isEmpty()) {
			logger.debug("Aplicant regles a " + pendents.size() + " registres pendents");
			for (RegistreEntity pendent: pendents) {
				ReglaEntity regla = pendent.getRegla();
				String error = null;
				try {
					switch (regla.getTipus()) {
					case BACKOFFICE:
						RipeaBackofficeWsService backofficeClient = new WsClientHelper<RipeaBackofficeWsService>().generarClientWs(
								regla.getBackofficeUrl(),
								new QName(
										"http://www.caib.es/ripea/ws/backoffice",
										"RipeaBackofficeService"),
								regla.getBackofficeUsuari(),
								regla.getBackofficeContrasenya(),
								null,
								RipeaBackofficeWsService.class);
						RipeaBackofficeResultatProces resultat = backofficeClient.processarAnotacio(
								fromRegistreEntity(pendent));
						if (resultat.isError()) {
							error = "[" + resultat.getErrorCodi() + "] " + resultat.getErrorDescripcio();
						}
						break;
					case BUSTIA:
						// TODO enviar a bústia
						break;
					case EXP_CREAR:
						// TODO crear expedient
						break;
					case EXP_AFEGIR:
						// TODO afegir a expedient existent
						break;
					default:
						error = "Tipus de regla desconegut (" + regla.getTipus() + ")";
						break;
					}
				} catch (Exception ex) {
					error = ExceptionUtils.getStackTrace(ex);
				}
				if (error != null) {
					if (ReglaTipusEnumDto.BACKOFFICE.equals(regla.getTipus())) {
						Integer intentsRegla = regla.getBackofficeIntents();
						if (intentsRegla == null) {
							pendent.updateProces(
									null,
									RegistreProcesEstatEnum.ERROR,
									error);
						} else {
							Integer intentsPendent = pendent.getProcesIntents();
							if (intentsPendent != null && intentsPendent.intValue() >= intentsRegla.intValue() - 1) {
								pendent.updateProces(
										null,
										RegistreProcesEstatEnum.ERROR,
										error);
							} else {
								pendent.updateProces(
										null,
										RegistreProcesEstatEnum.PENDENT,
										error);
							}
						}
					} else {
						pendent.updateProces(
								null,
								RegistreProcesEstatEnum.ERROR,
								error);
					}
				} else {
					pendent.updateProces(
							new Date(),
							RegistreProcesEstatEnum.PROCESSAT,
							error);
				}
			}
		} else {
			logger.debug("No hi ha registres pendents de processar");
		}
	}



	private BustiaDto toBustiaDto(
			BustiaEntity bustia) {
		return (BustiaDto)contingutHelper.toContingutDto(bustia);
	}
	private List<BustiaDto> toBustiaDto(
			List<BustiaEntity> busties,
			boolean ambFills,
			boolean filtrarFillsSegonsPermisRead) {
		List<BustiaDto> resposta = new ArrayList<BustiaDto>();
		for (BustiaEntity bustia: busties)
			resposta.add(
					(BustiaDto)contingutHelper.toContingutDto(
							bustia,
							false,
							ambFills,
							filtrarFillsSegonsPermisRead,
							false,
							false,
							false));
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

	private BustiaContingutPendentDto toContingutPendentDto(
			ContingutDto contingut) {
		BustiaContingutPendentDto pendent = new BustiaContingutPendentDto();
		pendent.setId(contingut.getId());
		pendent.setDescripcio(contingut.getNom());
		if (contingut.isRegistre()) {
			RegistreAnotacioDto anotacio = (RegistreAnotacioDto)contingut;
			pendent.setTipus(BustiaContingutPendentTipusEnumDto.REGISTRE);
			pendent.setNumero(anotacio.getIdentificador());
			pendent.setDescripcio(anotacio.getExtracte());
			pendent.setRemitent("REGWEB");
			pendent.setRecepcioData(anotacio.getCreatedDate());
			if (RegistreProcesEstatEnum.ERROR.equals(anotacio.getProcesEstat())) {
				pendent.setError(true);
			}
		} else {
			if (contingut.isExpedient()) {
				pendent.setTipus(BustiaContingutPendentTipusEnumDto.EXPEDIENT);
			}
			if (contingut.isDocument()) {
				pendent.setTipus(BustiaContingutPendentTipusEnumDto.DOCUMENT);
			}
		}
		if (contingut.getDarrerMovimentUsuari() != null)
			pendent.setRemitent(contingut.getDarrerMovimentUsuari().getNom());
		if (contingut.getDarrerMovimentData() != null)
			pendent.setRecepcioData(contingut.getDarrerMovimentData());
		if (contingut.getDarrerMovimentComentari() != null)
			pendent.setComentari(contingut.getDarrerMovimentComentari());
		return pendent;
	}

	private RegistreEntity toRegistreEntity(
			RegistreTipusEnum tipus,
			String unitatAdministrativa,
			RegistreAnotacio anotacio,
			BustiaEntity bustia,
			ReglaEntity regla) {
		RegistreEntity entity = RegistreEntity.getBuilder(
				tipus,
				unitatAdministrativa,
				anotacio.getNumero(),
				anotacio.getData(),
				anotacio.getIdentificador(),
				anotacio.getOficinaCodi(),
				anotacio.getLlibreCodi(),
				anotacio.getAssumpteTipusCodi(),
				anotacio.getIdiomaCodi(),
				(regla != null) ? RegistreProcesEstatEnum.PENDENT : RegistreProcesEstatEnum.NO_PROCES,
				bustia).
		entitatCodi(anotacio.getEntitatCodi()).
		entitatDescripcio(anotacio.getEntitatDescripcio()).
		oficinaDescripcio(anotacio.getOficinaDescripcio()).
		llibreDescripcio(anotacio.getLlibreDescripcio()).
		extracte(anotacio.getExtracte()).
		assumpteTipusDescripcio(anotacio.getAssumpteTipusDescripcio()).
		assumpteCodi(anotacio.getAssumpteCodi()).
		assumpteDescripcio(anotacio.getAssumpteDescripcio()).
		referencia(anotacio.getReferencia()).
		expedientNumero(anotacio.getExpedientNumero()).
		idiomaDescripcio(anotacio.getIdiomaDescripcio()).
		transportTipusCodi(anotacio.getTransportTipusCodi()).
		transportTipusDescripcio(anotacio.getTransportTipusDescripcio()).
		transportNumero(anotacio.getTransportNumero()).
		usuariCodi(anotacio.getUsuariCodi()).
		usuariNom(anotacio.getUsuariNom()).
		usuariContacte(anotacio.getUsuariContacte()).
		aplicacioCodi(anotacio.getAplicacioCodi()).
		aplicacioVersio(anotacio.getAplicacioVersio()).
		documentacioFisicaCodi(anotacio.getDocumentacioFisicaCodi()).
		documentacioFisicaDescripcio(anotacio.getDocumentacioFisicaDescripcio()).
		observacions(anotacio.getObservacions()).
		exposa(anotacio.getExposa()).
		solicita(anotacio.getSolicita()).
		regla(regla).
		build();
		if (anotacio.getInteressats() != null) {
			for (RegistreInteressat registreInteressat: anotacio.getInteressats()) {
				entity.getInteressats().add(
						toInteressatEntity(
								registreInteressat,
								entity));
			}
		}
		if (anotacio.getAnnexos() != null) {
			for (RegistreAnnex registreAnnex: anotacio.getAnnexos()) {
				entity.getAnnexos().add(
						toAnnexEntity(
								registreAnnex,
								entity));
			}
		}
		return entity;
	}
	private RegistreInteressatEntity toInteressatEntity(
			RegistreInteressat registreInteressat,
			RegistreEntity registre) {
		RegistreInteressatTipusEnum interessatTipus = RegistreInteressatTipusEnum.valorAsEnum(registreInteressat.getTipus());
		RegistreInteressatEntity.Builder interessatBuilder;
		switch (interessatTipus) {
		case PERSONA_FIS:
			interessatBuilder = RegistreInteressatEntity.getBuilder(
					interessatTipus,
					RegistreInteressatDocumentTipusEnum.valorAsEnum(registreInteressat.getDocumentTipus()),
					registreInteressat.getDocumentNum(),
					registreInteressat.getNom(),
					registreInteressat.getLlinatge1(),
					registreInteressat.getLlinatge2(),
					registre);
			break;
		default: // PERSONA_JUR o ADMINISTRACIO
			interessatBuilder = RegistreInteressatEntity.getBuilder(
					interessatTipus,
					RegistreInteressatDocumentTipusEnum.valorAsEnum(registreInteressat.getDocumentTipus()),
					registreInteressat.getDocumentNum(),
					registreInteressat.getRaoSocial(),
					registre);
			break;
		}
		RegistreInteressatEntity interessatEntity = interessatBuilder.
		pais(registreInteressat.getPais()).
		provincia(registreInteressat.getProvincia()).
		municipi(registreInteressat.getMunicipi()).
		adresa(registreInteressat.getAdresa()).
		codiPostal(registreInteressat.getCodiPostal()).
		email(registreInteressat.getEmail()).
		telefon(registreInteressat.getTelefon()).
		emailHabilitat(registreInteressat.getEmailHabilitat()).
		canalPreferent(
				RegistreInteressatCanalEnum.valorAsEnum(
						registreInteressat.getCanalPreferent())).
		observacions(registreInteressat.getObservacions()).
		build();
		if (registreInteressat.getRepresentant() != null) {
			RegistreInteressat representant = registreInteressat.getRepresentant();
			interessatEntity.updateRepresentant(
					RegistreInteressatTipusEnum.valorAsEnum(representant.getTipus()),
					RegistreInteressatDocumentTipusEnum.valorAsEnum(representant.getDocumentTipus()),
					representant.getDocumentNum(),
					representant.getNom(),
					representant.getLlinatge1(),
					representant.getLlinatge2(),
					representant.getRaoSocial(),
					representant.getPais(),
					representant.getProvincia(),
					representant.getMunicipi(),
					representant.getAdresa(),
					representant.getCodiPostal(),
					representant.getEmail(),
					representant.getTelefon(),
					representant.getEmailHabilitat(),
					RegistreInteressatCanalEnum.valorAsEnum(representant.getCanalPreferent()));
		}
		return interessatEntity;
	}
	private RegistreAnnexEntity toAnnexEntity(
			RegistreAnnex registreAnnex,
			RegistreEntity registre) {
		RegistreAnnexEntity annexEntity = RegistreAnnexEntity.getBuilder(
				registreAnnex.getTitol(),
				registreAnnex.getFitxerNom(),
				registreAnnex.getFitxerTamany(),
				registreAnnex.getFitxerGestioDocumentalId(),
				registreAnnex.getDataCaptura(),
				RegistreAnnexOrigenEnum.valorAsEnum(registreAnnex.getOrigenCiutadaAdmin()),
				RegistreAnnexNtiTipusDocumentEnum.valorAsEnum(registreAnnex.getNtiTipusDocument()),
				RegistreAnnexSicresTipusDocumentEnum.valorAsEnum(registreAnnex.getSicresTipusDocument()),
				registre).
				fitxerTipusMime(registreAnnex.getFitxerTipusMime()).
				localitzacio(registreAnnex.getLocalitzacio()).
				ntiElaboracioEstat(RegistreAnnexElaboracioEstatEnum.valorAsEnum(registreAnnex.getNtiElaboracioEstat())).
				firmaCsv(registreAnnex.getFirmaCsv()).
				observacions(registreAnnex.getObservacions()).
				build();
		return annexEntity;
	}
	
	private RegistreAnotacio fromRegistreEntity(
			RegistreEntity entity) {
		RegistreAnotacio anotacio = new RegistreAnotacio();
		anotacio.setNumero(entity.getNumero());
		anotacio.setData(entity.getData());
		anotacio.setIdentificador(entity.getIdentificador());
		anotacio.setEntitatCodi(entity.getEntitatCodi());
		anotacio.setEntitatDescripcio(entity.getEntitatDescripcio());
		anotacio.setOficinaCodi(entity.getOficinaCodi());
		anotacio.setOficinaDescripcio(entity.getOficinaDescripcio());
		anotacio.setLlibreCodi(entity.getLlibreCodi());
		anotacio.setLlibreDescripcio(entity.getLlibreDescripcio());
		anotacio.setExtracte(entity.getExtracte());
		anotacio.setAssumpteTipusCodi(entity.getAssumpteTipusCodi());
		anotacio.setAssumpteTipusDescripcio(entity.getAssumpteTipusDescripcio());
		anotacio.setAssumpteCodi(entity.getAssumpteCodi());
		anotacio.setAssumpteDescripcio(entity.getAssumpteDescripcio());
		anotacio.setReferencia(entity.getReferencia());
		anotacio.setExpedientNumero(entity.getExpedientNumero());
		anotacio.setIdiomaCodi(entity.getIdiomaCodi());
		anotacio.setIdiomaDescripcio(entity.getIdiomaDescripcio());
		anotacio.setTransportTipusCodi(entity.getTransportTipusCodi());
		anotacio.setTransportTipusDescripcio(entity.getTransportTipusDescripcio());
		anotacio.setTransportNumero(entity.getTransportNumero());
		anotacio.setUsuariCodi(entity.getUsuariCodi());
		anotacio.setUsuariNom(entity.getUsuariNom());
		anotacio.setUsuariContacte(entity.getUsuariContacte());
		anotacio.setAplicacioCodi(entity.getAplicacioCodi());
		anotacio.setAplicacioVersio(entity.getAplicacioVersio());
		anotacio.setDocumentacioFisicaCodi(entity.getDocumentacioFisicaCodi());
		anotacio.setDocumentacioFisicaDescripcio(entity.getDocumentacioFisicaDescripcio());
		anotacio.setObservacions(entity.getObservacions());
		anotacio.setExposa(entity.getExposa());
		anotacio.setSolicita(entity.getSolicita());
		if (!entity.getInteressats().isEmpty()) {
			List<RegistreInteressat> interessats = new ArrayList<RegistreInteressat>();
			for (RegistreInteressatEntity interessat: entity.getInteressats()) {
				interessats.add(
						fromInteressatEntity(
								interessat));
			}
			anotacio.setInteressats(interessats);
		}
		if (!entity.getAnnexos().isEmpty()) {
			List<RegistreAnnex> annexos = new ArrayList<RegistreAnnex>();
			for (RegistreAnnexEntity annex: entity.getAnnexos()) {
				annexos.add(
						fromAnnexEntity(
								annex,
								anotacio));
			}
			anotacio.setAnnexos(annexos);
		}
		return anotacio;
	}
	private RegistreInteressat fromInteressatEntity(
			RegistreInteressatEntity interessatEntity) {
		RegistreInteressat interessat = new RegistreInteressat();
		if (interessatEntity.getTipus() != null)
			interessat.setTipus(interessatEntity.getTipus().getValor());
		if (interessatEntity.getDocumentTipus() != null)
			interessat.setDocumentTipus(interessatEntity.getDocumentTipus().getValor());
		interessat.setDocumentNum(interessatEntity.getDocumentNum());
		interessat.setNom(interessatEntity.getNom());
		interessat.setLlinatge1(interessatEntity.getLlinatge1());
		interessat.setLlinatge2(interessatEntity.getLlinatge2());
		interessat.setRaoSocial(interessatEntity.getRaoSocial());
		interessat.setPais(interessatEntity.getPais());
		interessat.setProvincia(interessatEntity.getProvincia());
		interessat.setMunicipi(interessatEntity.getMunicipi());
		interessat.setAdresa(interessatEntity.getAdresa());
		interessat.setCodiPostal(interessatEntity.getCodiPostal());
		interessat.setEmail(interessatEntity.getEmail());
		interessat.setTelefon(interessatEntity.getTelefon());
		interessat.setEmailHabilitat(interessatEntity.getEmailHabilitat());
		if (interessatEntity.getCanalPreferent() != null)
			interessat.setCanalPreferent(interessatEntity.getCanalPreferent().getValor());
		interessat.setObservacions(interessatEntity.getObservacions());
		if (interessatEntity.getRepresentant() != null) {
			interessat.setRepresentant(
					fromInteressatEntity(interessatEntity.getRepresentant()));
		}
		return interessat;
	}
	private RegistreAnnex fromAnnexEntity(
			RegistreAnnexEntity annexEntity,
			RegistreAnotacio registre) {
		RegistreAnnex annex = new RegistreAnnex();
		annex.setTitol(annexEntity.getTitol());
		annex.setFitxerNom(annexEntity.getFitxerNom());
		annex.setFitxerTamany(annexEntity.getFitxerTamany());
		annex.setFitxerTipusMime(annexEntity.getFitxerTipusMime());
		annex.setFirmaFitxerGestioDocumentalId(annexEntity.getFirmaFitxerGestioDocumentalId());
		annex.setDataCaptura(annexEntity.getDataCaptura());
		annex.setLocalitzacio(annexEntity.getLocalitzacio());
		if (annexEntity.getOrigenCiutadaAdmin() != null)
			annex.setOrigenCiutadaAdmin(annexEntity.getOrigenCiutadaAdmin().getValor());
		if (annexEntity.getNtiTipusDocument() != null)
			annex.setNtiTipusDocument(annexEntity.getNtiTipusDocument().getValor());
		if (annexEntity.getSicresTipusDocument() != null)
			annex.setSicresTipusDocument(annexEntity.getSicresTipusDocument().getValor());
		if (annexEntity.getNtiElaboracioEstat() != null)
			annex.setNtiElaboracioEstat(annexEntity.getNtiElaboracioEstat().getValor());
		annex.setObservacions(annexEntity.getObservacions());
		annex.setFirmaMode(annexEntity.getFirmaMode());
		annex.setFirmaFitxerNom(annexEntity.getFirmaFitxerNom());
		annex.setFirmaFitxerTamany(annexEntity.getFirmaFitxerTamany());
		annex.setFirmaFitxerTipusMime(annexEntity.getFitxerTipusMime());
		annex.setFirmaFitxerGestioDocumentalId(annexEntity.getFitxerGestioDocumentalId());
		annex.setFirmaCsv(annexEntity.getFirmaCsv());
		annex.setTimestamp(annexEntity.getTimestamp());
		annex.setValidacioOCSP(annexEntity.getValidacioOCSP());
		return annex;
	}

	private ReglaEntity findReglaAplicable(
			EntitatEntity entitat,
			String unitatAdministrativa,
			RegistreAnotacio anotacio) {
		List<ReglaEntity> regles = reglaRepository.findByEntitatAndActivaTrueOrderByOrdreAsc(entitat);
		ReglaEntity reglaAplicable = null;
		for (ReglaEntity regla: regles) {
			if (regla.getUnitatCodi() == null || regla.getUnitatCodi().equals(unitatAdministrativa)) {
				if (anotacio.getAssumpteTipusCodi() != null && anotacio.getAssumpteTipusCodi().equals(regla.getAssumpteCodi())) {
					reglaAplicable = regla;
					break;
				}
			}
		}
		return reglaAplicable;
	}

	private static final Logger logger = LoggerFactory.getLogger(BustiaServiceImpl.class);

}
