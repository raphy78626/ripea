/**
 * 
 */
package es.caib.ripea.core.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.ArxiuPluginDocumentContingutDto;
import es.caib.ripea.core.api.dto.ArxiuPluginInfoDto;
import es.caib.ripea.core.api.dto.ArxiuPluginNodeFillDto;
import es.caib.ripea.core.api.dto.ArxiuPluginNodeTipusEnumDto;
import es.caib.ripea.core.api.dto.ContingutComentariDto;
import es.caib.ripea.core.api.dto.ContingutDto;
import es.caib.ripea.core.api.dto.ContingutFiltreDto;
import es.caib.ripea.core.api.dto.ContingutLogDetallsDto;
import es.caib.ripea.core.api.dto.ContingutLogDto;
import es.caib.ripea.core.api.dto.ContingutMassiuFiltreDto;
import es.caib.ripea.core.api.dto.ContingutMovimentDto;
import es.caib.ripea.core.api.dto.ContingutTipusEnumDto;
import es.caib.ripea.core.api.dto.DocumentEstatEnumDto;
import es.caib.ripea.core.api.dto.DocumentNtiEstadoElaboracionEnumDto;
import es.caib.ripea.core.api.dto.DocumentNtiOrigenEnumDto;
import es.caib.ripea.core.api.dto.DocumentNtiTipoDocumentalEnumDto;
import es.caib.ripea.core.api.dto.DocumentTipusEnumDto;
import es.caib.ripea.core.api.dto.EscriptoriDto;
import es.caib.ripea.core.api.dto.ExpedientEstatEnumDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.LogObjecteTipusEnumDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.dto.ValidacioErrorDto;
import es.caib.ripea.core.api.exception.ConteDocumentsDefinitiusException;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.service.ContingutService;
import es.caib.ripea.core.entity.CarpetaEntity;
import es.caib.ripea.core.entity.ContingutComentariEntity;
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.ContingutMovimentEntity;
import es.caib.ripea.core.entity.DadaEntity;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.EscriptoriEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.MetaDadaEntity;
import es.caib.ripea.core.entity.MetaNodeEntity;
import es.caib.ripea.core.entity.MetaNodeMetaDadaEntity;
import es.caib.ripea.core.entity.NodeEntity;
import es.caib.ripea.core.entity.UsuariEntity;
import es.caib.ripea.core.helper.CacheHelper;
import es.caib.ripea.core.helper.ContingutHelper;
import es.caib.ripea.core.helper.ContingutLogHelper;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.DocumentHelper;
import es.caib.ripea.core.helper.EntityComprovarHelper;
import es.caib.ripea.core.helper.HibernateHelper;
import es.caib.ripea.core.helper.PaginacioHelper;
import es.caib.ripea.core.helper.PaginacioHelper.Converter;
import es.caib.ripea.core.helper.PluginHelper;
import es.caib.ripea.core.helper.PropertiesHelper;
import es.caib.ripea.core.helper.UsuariHelper;
import es.caib.ripea.core.repository.ContingutComentariRepository;
import es.caib.ripea.core.repository.ContingutRepository;
import es.caib.ripea.core.repository.DadaRepository;
import es.caib.ripea.core.repository.EscriptoriRepository;
import es.caib.ripea.core.repository.MetaDadaRepository;
import es.caib.ripea.core.repository.MetaNodeMetaDadaRepository;
import es.caib.ripea.core.repository.MetaNodeRepository;
import es.caib.ripea.core.repository.UsuariRepository;
import es.caib.ripea.plugin.arxiu.ArxiuCarpeta;
import es.caib.ripea.plugin.arxiu.ArxiuContingutTipusEnum;
import es.caib.ripea.plugin.arxiu.ArxiuDocument;
import es.caib.ripea.plugin.arxiu.ArxiuDocumentContingut;
import es.caib.ripea.plugin.arxiu.ArxiuExpedient;
import es.caib.ripea.plugin.arxiu.ArxiuFill;

/**
 * Implementació dels mètodes per a gestionar continguts.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class ContingutServiceImpl implements ContingutService {

	@Resource
	private UsuariRepository usuariRepository;
	@Resource
	private EscriptoriRepository escriptoriRepository;
	@Resource
	private ContingutRepository contingutRepository;
	@Resource
	private MetaNodeMetaDadaRepository metaNodeMetaDadaRepository;
	@Resource
	private MetaDadaRepository metaDadaRepository;
	@Resource
	private DadaRepository dadaRepository;
	@Resource
	private MetaNodeRepository metaNodeRepository;
	@Resource
	private ContingutComentariRepository contingutComentariRepository;

	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	PaginacioHelper paginacioHelper;
	@Resource
	private CacheHelper cacheHelper;
	@Resource
	private ContingutHelper contingutHelper;
	@Resource
	private DocumentHelper documentHelper;
	@Resource
	private ContingutLogHelper contingutLogHelper;
	@Resource
	private UsuariHelper usuariHelper;
	@Resource
	private PluginHelper pluginHelper;
	@Resource
	private EntityComprovarHelper entityComprovarHelper;



	@Transactional
	@Override
	public ContingutDto rename(
			Long entitatId,
			Long contingutId,
			String nom) {
		logger.debug("Canviant el nom del contingut ("
				+ "entitatId=" + entitatId + ", "
				+ "contingutId=" + contingutId + ", "
				+ "nom=" + nom + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		// Comprova que el contingut arrel és l'escriptori de l'usuari actual
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				contingut);
		// Comprova l'accés al path del contingut
		contingutHelper.comprovarPermisosPathContingut(
				contingut,
				false,
				false,
				true,
				true);
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
		}
		if (!contingutHelper.isNomValid(nom)) {
			throw new ValidationException(
					contingutId,
					ContingutEntity.class,
					"El nom del contingut no és vàlid (no pot començar amb \".\")");
		}
		// Canvia el nom del contingut
		contingut.update(nom);
		// Registra al log la modificació del contingut
		contingutLogHelper.log(
				contingut,
				LogTipusEnumDto.MODIFICACIO,
				nom,
				null,
				true,
				true);
		return contingutHelper.toContingutDto(
				contingut,
				true,
				true,
				true,
				false,
				false,
				false,
				false);
	}

	@Transactional
	@Override
	public void dadaSave(
			Long entitatId,
			Long contingutId,
			Map<String, Object> valors) throws NotFoundException {
		logger.debug("Guardant dades del node (" +
				"entitatId=" + entitatId + ", " +
				"contingutId=" + contingutId + ", " +
				"valors=" + valors + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		if (!(contingut instanceof NodeEntity)) {
			throw new ValidationException(
					contingutId,
					ContingutEntity.class,
					"El contingut no és un node");
		}
		// Comprova que el contingut arrel és l'escriptori de l'usuari actual
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				contingut);
		// Comprova el permis d'escriptura al node
		NodeEntity node = entityComprovarHelper.comprovarNode(
				entitat,
				contingutId,
				false,
				true,
				false);
		// Esborra les dades no especificades
		for (DadaEntity dada: dadaRepository.findByNode(node)) {
			if (!valors.keySet().contains(dada.getMetaDada().getCodi())) {
				dadaRepository.delete(dada);
			}
		}
		// Obté les metaDades del node (globals i específiques)
		List<MetaNodeMetaDadaEntity> metaNodeMetaDades = metaNodeMetaDadaRepository.findByMetaNodeAndActivaTrue(node.getMetaNode());
		List<MetaDadaEntity> metaDadesGlobals = null;
		if (node instanceof ExpedientEntity) {
			metaDadesGlobals = metaDadaRepository.findByEntitatAndGlobalExpedientTrueAndActivaTrueOrderByIdAsc(entitat);
		} else if (node instanceof DocumentEntity) {
			metaDadesGlobals = metaDadaRepository.findByEntitatAndGlobalDocumentTrueAndActivaTrueOrderByIdAsc(entitat);
		}
		// Modifica les dades existents
		for (String dadaCodi: valors.keySet()) {
			nodeDadaGuardar(
					node,
					dadaCodi,
					valors.get(dadaCodi),
					metaNodeMetaDades,
					metaDadesGlobals);
			
		}
		cacheHelper.evictErrorsValidacioPerNode(node);
	}

	@Transactional
	@Override
	@CacheEvict(value = "errorsValidacioNode", key = "#contingutId")
	public ContingutDto deleteReversible(
			Long entitatId,
			Long contingutId) throws IOException {
		logger.debug("Esborrant el contingut ("
				+ "entitatId=" + entitatId + ", "
				+ "contingutId=" + contingutId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		// Comprova que el contingut arrel és l'escriptori de l'usuari actual
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				contingut);
		// Comprova l'accés al path del contingut
		contingutHelper.comprovarPermisosPathContingut(
				contingut,
				true,
				false,
				false,
				true);
		// Comprova el permís de modificació de l'expedient superior
		ExpedientEntity expedientSuperior = contingutHelper.getExpedientSuperior(
				contingut,
				false,
				false,
				false);
		if (expedientSuperior != null) {
			contingutHelper.comprovarPermisosContingut(
					expedientSuperior,
					false,
					true,
					false);
		}
		// Comprova el permís d'esborrar del contingut
		contingutHelper.comprovarPermisosContingut(
				contingut,
				false,
				false,
				true);
		ContingutDto dto = contingutHelper.toContingutDto(
				contingut,
				true,
				false,
				false,
				false,
				false,
				false,
				false);
		// Comprova que el contingut no estigui esborrat
		if (contingut.getEsborrat() > 0) {
			logger.error("Aquest contingut ja està esborrat (contingutId=" + contingutId + ")");
			throw new ValidationException(
					contingutId,
					ContingutEntity.class,
					"Aquest contingut ja està esborrat");
		}
		// Valida si conté documents definitius
		if (conteDocumentsDefinitius(contingut)) {
			throw new ConteDocumentsDefinitiusException(
					contingutId,
					ContingutEntity.class);
		}
		// Marca el contingut i tots els seus fills com a esborrats
		//  de forma recursiva
		marcarEsborrat(contingut);
		// Si el contingut és un document guarda una còpia del fitxer esborrat
		// per a poder recuperar-lo posteriorment
		if (contingut instanceof DocumentEntity) {
			DocumentEntity document = (DocumentEntity)contingut;
			if (DocumentTipusEnumDto.DIGITAL.equals(document.getDocumentTipus())) {
				fitxerDocumentEsborratGuardar((DocumentEntity)contingut);
			}
		}
		// Propaga l'acció a l'arxiu
		contingutHelper.arxiuPropagarEliminacio(
				contingut,
				expedientSuperior);
		return dto;
	}

	@Transactional
	@Override
	@CacheEvict(value = "errorsValidacioNode", key = "#contingutId")
	public ContingutDto deleteDefinitiu(
			Long entitatId,
			Long contingutId) {
		logger.debug("Esborrant el contingut ("
				+ "entitatId=" + entitatId + ", "
				+ "contingutId=" + contingutId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		// Esborra definitivament el contingut
		ContingutDto dto = contingutHelper.toContingutDto(
				contingut,
				true,
				false,
				false,
				false,
				false,
				false,
				false);
		if (contingut.getPare() != null) {
			contingut.getPare().getFills().remove(contingut);
		}
		contingutRepository.delete(contingut);
		// Propaga l'acció a l'arxiu
		ExpedientEntity expedientSuperior = contingutHelper.getExpedientSuperior(
				contingut,
				false,
				false,
				false);
		contingutHelper.arxiuPropagarEliminacio(
				contingut,
				expedientSuperior);
		// Registra al log l'eliminació definitiva del contingut
		contingutLogHelper.log(
				contingut,
				LogTipusEnumDto.ELIMINACIODEF,
				null,
				null,
				true,
				true);
		return dto;
	}

	@Transactional
	@Override
	public ContingutDto undelete(
			Long entitatId,
			Long contingutId) throws IOException {
		logger.debug("Recuperant el contingut ("
				+ "entitatId=" + entitatId + ", "
				+ "contingutId=" + contingutId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		// No es comproven permisos perquè això només ho pot fer l'administrador
		if (contingut.getEsborrat() == 0) {
			logger.error("Aquest contingut no està esborrat (contingutId=" + contingutId + ")");
			throw new ValidationException(
					contingutId,
					ContingutEntity.class,
					"Aquest contingut no està esborrat");
		}
		if (contingut.getPare() == null) {
			logger.error("Aquest contingut no te pare (contingutId=" + contingutId + ")");
			throw new ValidationException(
					contingutId,
					ContingutEntity.class,
					"Aquest contingut no te pare");
		}
		boolean nomDuplicat = contingutRepository.findByPareAndNomAndEsborrat(
				contingut.getPare(),
				contingut.getNom(),
				0) != null;
		if (nomDuplicat) {
			throw new ValidationException(
					contingutId,
					ContingutEntity.class,
					"Ja existeix un altre contingut amb el mateix nom dins el mateix pare");
		}
		// Recupera el contingut esborrat
		contingut.updateEsborrat(0);
		ContingutDto dto = contingutHelper.toContingutDto(
				contingut,
				true,
				false,
				false,
				false,
				false,
				false,
				false);
		// Registra al log la recuperació del contingut
		contingutLogHelper.log(
				contingut,
				LogTipusEnumDto.RECUPERACIO,
				null,
				null,
				true,
				true);
		// Propaga l'acció a l'arxiu
		ExpedientEntity expedientSuperior = contingutHelper.getExpedientSuperior(
				contingut,
				false,
				false,
				false);
		FitxerDto fitxer = null;
		if (contingut instanceof DocumentEntity) {
			DocumentEntity document = (DocumentEntity)contingut;
			if (DocumentTipusEnumDto.DIGITAL.equals(document.getDocumentTipus())) {
				fitxer = fitxerDocumentEsborratLlegir((DocumentEntity)contingut);
			}
		}
		contingutHelper.arxiuPropagarModificacio(
				contingut,
				expedientSuperior,
				fitxer);
		if (fitxer != null) {
			fitxerDocumentEsborratEsborrar((DocumentEntity)contingut);
		}
		return dto;
	}

	@Transactional
	@Override
	public ContingutDto move(
			Long entitatId,
			Long contingutOrigenId,
			Long contingutDestiId) {
		logger.debug("Movent el contingut ("
				+ "entitatId=" + entitatId + ", "
				+ "contingutOrigenId=" + contingutOrigenId + ", "
				+ "contingutDestiId=" + contingutDestiId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContingutEntity contingutOrigen = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutOrigenId,
				null);
		if (!(contingutOrigen instanceof DocumentEntity) && !(contingutOrigen instanceof CarpetaEntity)) {
			throw new ValidationException(
					contingutOrigenId,
					contingutOrigen.getClass(),
					"Només es poden moure documents i carpetes");
		}
		// Comprova que el contingutOrigen arrel és l'escriptori de l'usuari actual
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				contingutOrigen);
		// Comprova l'accés al path del contingutOrigen
		contingutHelper.comprovarPermisosPathContingut(
				contingutOrigen,
				true,
				false,
				false,
				true);
		// Comprova que el contingutDesti arrel és l'escriptori de l'usuari actual
		ContingutEntity contingutDesti = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutDestiId,
				null);
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				contingutDesti);
		// Comprova l'accés al path del contingutDesti
		contingutHelper.comprovarPermisosPathContingut(
				contingutDesti,
				false,
				true,
				false,
				true);
		// Comprova que el nom no sigui duplicat
		boolean nomDuplicat = contingutRepository.findByPareAndNomAndEsborrat(
				contingutDesti,
				contingutOrigen.getNom(),
				0) != null;
		if (nomDuplicat) {
			throw new ValidationException(
					contingutOrigenId,
					ContingutEntity.class,
					"Ja existeix un altre contingut amb el mateix nom dins el contingut destí ("
							+ "contingutDestiId=" + contingutDestiId + ")");
		}
		// Comprova que la sèrie documental sigui la mateixa
		// TODO comprovació sèrie documental
		// Realitza el moviment del contingut
		ContingutMovimentEntity contingutMoviment = contingutHelper.ferIEnregistrarMoviment(
				contingutOrigen,
				contingutDesti,
				null);
		contingutLogHelper.log(
				contingutOrigen,
				LogTipusEnumDto.MOVIMENT,
				contingutMoviment,
				true,
				true);
		ContingutDto dto = contingutHelper.toContingutDto(
				contingutOrigen,
				true,
				false,
				false,
				false,
				false,
				false,
				false);
		contingutHelper.arxiuPropagarMoviment(
				contingutOrigen,
				contingutDesti);
		return dto;
	}

	@Transactional
	@Override
	public ContingutDto copy(
			Long entitatId,
			Long contingutOrigenId,
			Long contingutDestiId,
			boolean recursiu) {
		logger.debug("Copiant el contingut ("
				+ "entitatId=" + entitatId + ", "
				+ "contingutOrigenId=" + contingutOrigenId + ", "
				+ "contingutDestiId=" + contingutDestiId + ", "
				+ "recursiu=" + recursiu + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContingutEntity contingutOrigen = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutOrigenId,
				null);
		if (!(contingutOrigen instanceof DocumentEntity) && !(contingutOrigen instanceof CarpetaEntity)) {
			throw new ValidationException(
					contingutOrigenId,
					contingutOrigen.getClass(),
					"Només es poden copiar documents i carpetes");
		}
		// Comprova que el contingutOrigen arrel és l'escriptori de l'usuari actual
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				contingutOrigen);
		// Comprova l'accés al path del contingutOrigen
		contingutHelper.comprovarPermisosPathContingut(
				contingutOrigen,
				true,
				false,
				false,
				true);
		ContingutEntity contingutDesti = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutDestiId,
				null);
		// Comprova que el contingutDesti arrel és l'escriptori de l'usuari actual
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				contingutDesti);
		// Comprova l'accés al path del contingutDesti
		contingutHelper.comprovarPermisosPathContingut(
				contingutDesti,
				false,
				true,
				false,
				true);
		// Comprova que el nom no sigui duplicat
		boolean nomDuplicat = contingutRepository.findByPareAndNomAndEsborrat(
				contingutDesti,
				contingutOrigen.getNom(),
				0) != null;
		if (nomDuplicat) {
			throw new ValidationException(
					contingutOrigenId,
					ContingutEntity.class,
					"Ja existeix un altre contingut amb el mateix nom dins el contingut destí ("
							+ "contingutDestiId=" + contingutDestiId + ")");
		}
		// Comprova que la sèrie documental sigui la mateixa
		// TODO comprovació sèrie documental
		// Realitza la còpia del contingut
		ContingutEntity contingutCopia = copiarContingut(
				entitat,
				contingutOrigen,
				contingutDesti,
				recursiu);
		contingutLogHelper.log(
				contingutCopia,
				LogTipusEnumDto.COPIA,
				null,
				null,
				true,
				true);
		ContingutDto dto = contingutHelper.toContingutDto(
				contingutOrigen,
				true,
				false,
				false,
				false,
				false,
				false,
				false);
		contingutHelper.arxiuPropagarCopia(
				contingutOrigen,
				contingutDesti);
		return dto;
	}

	@Transactional
	@Override
	public EscriptoriDto getEscriptoriPerUsuariActual(
			Long entitatId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Obtenint escriptori ("
				+ "entitatId=" + entitatId + ", "
				+ "usuariCodi=" + auth.getName() + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		EscriptoriEntity escriptori = contingutHelper.getEscriptoriPerUsuari(
				entitat,
				usuariHelper.getUsuariAutenticat());
		return (EscriptoriDto)contingutHelper.toContingutDto(
				escriptori,
				true,
				false,
				false,
				false,
				false,
				false,
				false);
	}

	@Transactional(readOnly = true)
	@Override
	public ContingutDto findAmbIdUser(
			Long entitatId,
			Long contingutId,
			boolean ambFills,
			boolean ambVersions) {
		logger.debug("Obtenint contingut amb id per usuari ("
				+ "entitatId=" + entitatId + ", "
				+ "contingutId=" + contingutId + ", "
				+ "ambFills=" + ambFills + ", "
				+ "ambVersions=" + ambVersions + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		if (contingut instanceof ExpedientEntity) {
			contingutHelper.comprovarPermisosContingut(
					contingut,
					true,
					false,
					false);
		} else {
			contingutHelper.comprovarPermisosPathContingut(
					contingut,
					true,
					false,
					false,
					true);
		}
		return contingutHelper.toContingutDto(
				contingut,
				true,
				ambFills,
				ambFills,
				true,
				true,
				false,
				ambVersions);
	}

	@Transactional(readOnly = true)
	@Override
	public ContingutDto findAmbIdAdmin(
			Long entitatId,
			Long contingutId,
			boolean ambFills) {
		logger.debug("Obtenint contingut amb id per admin ("
				+ "entitatId=" + entitatId + ", "
				+ "contingutId=" + contingutId + ", "
				+ "ambFills=" + ambFills + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		return contingutHelper.toContingutDto(
				contingut,
				true,
				ambFills,
				ambFills,
				true,
				true,
				false,
				true);
	}

	@Transactional(readOnly = true)
	@Override
	public ContingutDto getContingutAmbFillsPerPath(
			Long entitatId,
			String path) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Obtenint contingut amb fills donat el seu path ("
				+ "entitatId=" + entitatId + ", "
				+ "path=" + path + ", "
				+ "usuariCodi=" + auth.getName() + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		EscriptoriEntity escriptori = escriptoriRepository.findByEntitatAndUsuari(
				entitat,
				usuariHelper.getUsuariAutenticat());
		ContingutEntity contingutActual = escriptori;
		if (!path.isEmpty() && !path.equals("/")) {
			String[] pathParts;
			if (path.startsWith("/")) {
				pathParts = path.substring(1).split("/");
			} else {
				pathParts = path.split("/");
			}
			for (String pathPart: pathParts) {
				Long idActual = contingutActual.getId();
				contingutActual = contingutRepository.findByPareAndNomAndEsborrat(
						contingutActual,
						pathPart,
						0);
				if (contingutActual == null) {
					logger.error("No s'ha trobat el contingut (pareId=" + idActual + ", nom=" + pathPart + ")");
					throw new NotFoundException(
							"(pareId=" + idActual + ", nom=" + pathPart + ")",
							ContingutEntity.class);
				}
				// Si el contingut actual és un document ens aturam
				// perquè el següent element del path serà la darrera
				// versió i no la trobaría com a contingut.
				if (contingutActual instanceof DocumentEntity)
					break;
			}
		}
		// Comprova que el contingut arrel és l'escriptori de l'usuari actual
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				contingutActual);
		// Comprova l'accés al path del contingut
		contingutHelper.comprovarPermisosPathContingut(
				contingutActual,
				true,
				false,
				false,
				true);
		return contingutHelper.toContingutDto(
				contingutActual,
				true,
				true,
				true,
				true,
				true,
				false,
				true);
	}

	@Transactional(readOnly = true)
	@Override
	public List<ValidacioErrorDto> findErrorsValidacio(
			Long entitatId,
			Long contingutId) {
		logger.debug("Obtenint errors de validació del contingut ("
				+ "entitatId=" + entitatId + ", "
				+ "contingutId=" + contingutId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		// Comprova l'accés al path del contingut
		contingutHelper.comprovarPermisosPathContingut(
				contingut,
				true,
				false,
				false,
				true);
		if (contingut instanceof NodeEntity) {
			NodeEntity node = (NodeEntity)contingut;
			return cacheHelper.findErrorsValidacioPerNode(node);
		} else {
			logger.error("El contingut no és cap node (contingutId=" + contingutId + ")");
			throw new ValidationException(
					contingutId,
					ContingutEntity.class,
					"El contingut no és un node");
		}
	}

	@Transactional(readOnly = true)
	@Override
	public List<ContingutLogDto> findLogsPerContingutAdmin(
			Long entitatId,
			Long contingutId) {
		logger.debug("Obtenint registre d'accions pel contingut usuari admin ("
				+ "entitatId=" + entitatId + ", "
				+ "nodeId=" + contingutId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		return contingutLogHelper.findLogsContingut(contingut);
	}

	@Transactional(readOnly = true)
	@Override
	public List<ContingutLogDto> findLogsPerContingutUser(
			Long entitatId,
			Long contingutId) {
		logger.debug("Obtenint registre d'accions pel contingut usuari normal ("
				+ "entitatId=" + entitatId + ", "
				+ "nodeId=" + contingutId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		// Comprova que l'usuari tengui accés al contingut
		contingutHelper.comprovarPermisosPathContingut(
				contingut,
				false,
				false,
				false,
				true);
		return contingutLogHelper.findLogsContingut(contingut);
	}

	@Transactional(readOnly = true)
	@Override
	public ContingutLogDetallsDto findLogDetallsPerContingutAdmin(
			Long entitatId,
			Long contingutId,
			Long contingutLogId) {
		logger.debug("Obtenint registre d'accions pel contingut usuari normal ("
				+ "entitatId=" + entitatId + ", "
				+ "nodeId=" + contingutId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		return contingutLogHelper.findLogDetalls(
				contingut,
				contingutLogId);
	}

	@Transactional(readOnly = true)
	@Override
	public ContingutLogDetallsDto findLogDetallsPerContingutUser(
			Long entitatId,
			Long contingutId,
			Long contingutLogId) {
		logger.debug("Obtenint registre d'accions pel contingut usuari normal ("
				+ "entitatId=" + entitatId + ", "
				+ "nodeId=" + contingutId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		// Comprova que l'usuari tengui accés al contingut
		contingutHelper.comprovarPermisosPathContingut(
				contingut,
				false,
				false,
				false,
				true);
		return contingutLogHelper.findLogDetalls(
				contingut,
				contingutLogId);
	}

	@Transactional(readOnly = true)
	@Override
	public List<ContingutMovimentDto> findMovimentsPerContingutAdmin(
			Long entitatId,
			Long contingutId) {
		logger.debug("Obtenint registre de moviments pel contingut usuari admin ("
				+ "entitatId=" + entitatId + ", "
				+ "nodeId=" + contingutId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		return contingutLogHelper.findMovimentsContingut(contingut);
	}

	

	@Transactional(readOnly = true)
	@Override
	public List<ContingutMovimentDto> findMovimentsPerContingutUser(
			Long entitatId,
			Long contingutId) {
		logger.debug("Obtenint registre de moviments pel contingut usuari normal ("
				+ "entitatId=" + entitatId + ", "
				+ "nodeId=" + contingutId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		// Comprova que l'usuari tengui accés al contingut
		contingutHelper.comprovarPermisosPathContingut(
				contingut,
				false,
				false,
				false,
				true);
		return contingutLogHelper.findMovimentsContingut(contingut);
	}

	@Transactional(readOnly = true)
	@Override
	public PaginaDto<ContingutDto> findAdmin(
			Long entitatId,
			ContingutFiltreDto filtre,
			PaginacioParamsDto paginacioParams) {
		logger.debug("Consulta de continguts per usuari admin ("
				+ "entitatId=" + entitatId + ", "
				+ "filtre=" + filtre + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaNodeEntity metaNode = null;
		if (filtre.getMetaNodeId() != null) {
			metaNode = metaNodeRepository.findOne(filtre.getMetaNodeId());
			if (metaNode == null) {
				throw new NotFoundException(
						filtre.getMetaNodeId(),
						MetaNodeEntity.class);
			}
		}
		boolean tipusArxiu = true;
		boolean tipusBustia = true;
		boolean tipusCarpeta = true;
		boolean tipusDocument = true;
		boolean tipusEscriptori = false;
		boolean tipusExpedient = true;
		boolean tipusRegistre = true;
		if (filtre.getTipus() != null) {
			tipusArxiu = false;
			tipusBustia = false;
			tipusCarpeta = false;
			tipusDocument = false;
			tipusExpedient = false;
			tipusRegistre = false;
			switch (filtre.getTipus()) {
			case ARXIU:
				tipusArxiu = true;
				break;
			case BUSTIA:
				tipusBustia = true;
				break;
			case CARPETA:
				tipusCarpeta = true;
				break;
			case DOCUMENT:
				tipusDocument = true;
				break;
			case ESCRIPTORI:
				break;
			case EXPEDIENT:
				tipusExpedient = true;
				break;
			case REGISTRE:
				tipusRegistre = true;
				break;
			}
		}
		Date dataInici = filtre.getDataCreacioInici();
		if (dataInici != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dataInici);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			dataInici = cal.getTime();
		}
		Date dataFi = filtre.getDataCreacioFi();
		if (dataFi != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dataFi);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
			dataFi = cal.getTime();
		}
		return paginacioHelper.toPaginaDto(
				contingutRepository.findByFiltrePaginat(
						entitat,
						tipusArxiu,
						tipusBustia,
						tipusCarpeta,
						tipusDocument,
						tipusEscriptori,
						tipusExpedient,
						tipusRegistre,
						(filtre.getNom() == null),
						filtre.getNom(),
						(metaNode == null),
						metaNode,
						(dataInici == null),
						dataInici,
						(dataFi == null),
						dataFi,
						filtre.isMostrarEsborrats(),
						filtre.isMostrarNoEsborrats(),
						paginacioHelper.toSpringDataPageable(paginacioParams)),
				ContingutDto.class,
				new Converter<ContingutEntity, ContingutDto>() {
					@Override
					public ContingutDto convert(ContingutEntity source) {
						return contingutHelper.toContingutDto(
								source,
								false,
								false,
								false,
								false,
								true,
								false,
								false);
					}
				});
	}

	@Transactional(readOnly = true)
	@Override
	public PaginaDto<ContingutDto> findEsborrats(
			Long entitatId,
			String nom,
			String usuariCodi,
			Date dataInici,
			Date dataFi,
			PaginacioParamsDto paginacioParams) {
		logger.debug("Obtenint elements esborrats ("
				+ "entitatId=" + entitatId + ", "
				+ "nom=" + nom + ", "
				+ "usuariCodi=" + usuariCodi + ", "
				+ "dataInici=" + dataInici + ", "
				+ "dataFi=" + dataFi + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		UsuariEntity usuari = null;
		if (usuariCodi != null && !usuariCodi.isEmpty()) {
			usuari = usuariRepository.findOne(usuariCodi);
			if (usuari == null) {
				logger.error("No s'ha trobat l'usuari (codi=" + usuariCodi + ")");
				throw new NotFoundException(
						usuariCodi,
						UsuariEntity.class);
			}
		}
		if (dataInici != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dataInici);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			dataInici = cal.getTime();
		}
		if (dataFi != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dataFi);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
			dataFi = cal.getTime();
		}
		return paginacioHelper.toPaginaDto(
				contingutRepository.findEsborratsByFiltrePaginat(
						entitat,
						(nom == null),
						(nom != null) ? '%' + nom + '%' : nom,
						(usuari == null),
						usuari,
						(dataInici == null),
						dataInici,
						(dataFi == null),
						dataFi,
						paginacioHelper.toSpringDataPageable(paginacioParams)),
				ContingutDto.class,
				new Converter<ContingutEntity, ContingutDto>() {
					@Override
					public ContingutDto convert(ContingutEntity source) {
						return contingutHelper.toContingutDto(
								source,
								false,
								false,
								false,
								false,
								false,
								false,
								false);
					}
				});
	}

	@Transactional(readOnly = true)
	@Override
	public ArxiuPluginInfoDto getArxiuInfo(
			Long entitatId,
			Long contingutId) {
		logger.debug("Obtenint informació de l'arxiu pel contingut ("
				+ "entitatId=" + entitatId + ", "
				+ "contingutId=" + contingutId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		if (contingut instanceof ExpedientEntity) {
			ArxiuExpedient arxiuExpedient = pluginHelper.arxiuExpedientConsultar(
					(ExpedientEntity)contingut);
			ArxiuPluginInfoDto arxiuInfo = new ArxiuPluginInfoDto();
			arxiuInfo.setNodeId(arxiuExpedient.getNodeId());
			arxiuInfo.setNom(arxiuExpedient.getTitol());
			arxiuInfo.setDescripcio(arxiuExpedient.getDescripcio());
			arxiuInfo.setSerieDocumental(arxiuExpedient.getSerieDocumental());
			arxiuInfo.setAplicacio(arxiuExpedient.getAplicacio());
			arxiuInfo.setEniVersio(arxiuExpedient.getEniVersio());
			arxiuInfo.setEniIdentificador(arxiuExpedient.getEniIdentificador());
			if (arxiuExpedient.getEniOrigen() != null) {
				switch (arxiuExpedient.getEniOrigen()) {
				case CIUTADA:
					arxiuInfo.setEniOrigen(DocumentNtiOrigenEnumDto.O0);
					break;
				case ADMINISTRACIO:
					arxiuInfo.setEniOrigen(DocumentNtiOrigenEnumDto.O1);
					break;
				}
			}
			arxiuInfo.setEniDataObertura(arxiuExpedient.getEniDataObertura());
			arxiuInfo.setEniClassificacio(arxiuExpedient.getEniClassificacio());
			if (arxiuExpedient.getEniEstat() != null) {
				switch (arxiuExpedient.getEniEstat()) {
				case OBERT:
					arxiuInfo.setEniEstat(ExpedientEstatEnumDto.OBERT);
					break;
				case TANCAT:
					arxiuInfo.setEniEstat(ExpedientEstatEnumDto.TANCAT);
					break;
				case INDEX_REMISSIO:
					arxiuInfo.setEniEstat(ExpedientEstatEnumDto.INDEX_REMISSIO);
					break;
				}
			}
			arxiuInfo.setEniInteressats(arxiuExpedient.getEniInteressats());
			arxiuInfo.setEniFirmaTipus(arxiuExpedient.getEniFirmaTipus());
			arxiuInfo.setEniFirmaCsv(arxiuExpedient.getEniFirmaCsv());
			arxiuInfo.setEniFirmaCsvDefinicio(arxiuExpedient.getEniFirmaCsvDefinicio());
			arxiuInfo.setEniOrgans(arxiuExpedient.getEniOrgans());
			arxiuInfo.setMetadades(arxiuExpedient.getMetadades());
			arxiuInfo.setAspectes(arxiuExpedient.getAspectes());
			arxiuInfo.setFills(arxiuConvertirFills(arxiuExpedient.getFills()));
			arxiuInfo.setCodiFontPeticio(arxiuExpedient.getCodiFontPeticio());
			arxiuInfo.setCodiFontResposta(arxiuExpedient.getCodiFontResposta());
			return arxiuInfo;
		} else if (contingut instanceof DocumentEntity) {
			ArxiuPluginInfoDto arxiuInfo = new ArxiuPluginInfoDto();
			ArxiuDocument arxiuDocument = pluginHelper.arxiuDocumentConsultar(
					contingut,
					null,
					null,
					true);
			arxiuInfo.setNodeId(arxiuDocument.getNodeId());
			arxiuInfo.setNom(arxiuDocument.getTitol());
			arxiuInfo.setDescripcio(arxiuDocument.getDescripcio());
			arxiuInfo.setSerieDocumental(arxiuDocument.getSerieDocumental());
			arxiuInfo.setAplicacio(arxiuDocument.getAplicacio());
			arxiuInfo.setDocumentVersioId(arxiuDocument.getVersioId());
			arxiuInfo.setDocumentVersioNodeId(arxiuDocument.getVersioNodeId());
			arxiuInfo.setEniVersio(arxiuDocument.getEniVersio());
			arxiuInfo.setEniIdentificador(arxiuDocument.getEniIdentificador());
			if (arxiuDocument.getEniOrigen() != null) {
				switch (arxiuDocument.getEniOrigen()) {
				case CIUTADA:
					arxiuInfo.setEniOrigen(DocumentNtiOrigenEnumDto.O0);
					break;
				case ADMINISTRACIO:
					arxiuInfo.setEniOrigen(DocumentNtiOrigenEnumDto.O1);
					break;
				}
			}
			arxiuInfo.setEniDataCaptura(arxiuDocument.getEniDataCaptura());
			if (arxiuDocument.getEniEstatElaboracio() != null) {
				switch (arxiuDocument.getEniEstatElaboracio()) {
				case ORIGINAL:
					arxiuInfo.setEniEstatElaboracio(DocumentNtiEstadoElaboracionEnumDto.EE01);
					break;
				case COPIA_AUTENTICA_FORMAT:
					arxiuInfo.setEniEstatElaboracio(DocumentNtiEstadoElaboracionEnumDto.EE02);
					break;
				case COPIA_AUTENTICA_PAPER:
					arxiuInfo.setEniEstatElaboracio(DocumentNtiEstadoElaboracionEnumDto.EE03);
					break;
				case COPIA_AUTENTICA_PARCIAL:
					arxiuInfo.setEniEstatElaboracio(DocumentNtiEstadoElaboracionEnumDto.EE04);
					break;
				case ALTRES:
					arxiuInfo.setEniEstatElaboracio(DocumentNtiEstadoElaboracionEnumDto.EE99);
					break;
				}
			}
			if (arxiuDocument.getEniTipusDocumental() != null) {
				switch (arxiuDocument.getEniTipusDocumental()) {
				case RESOLUCIO:
					arxiuInfo.setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto.TD01);
					break;
				case ACORD:
					arxiuInfo.setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto.TD02);
					break;
				case CONTRACTE:
					arxiuInfo.setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto.TD03);
					break;
				case CONVENI:
					arxiuInfo.setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto.TD04);
					break;
				case DECLARACIO:
					arxiuInfo.setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto.TD05);
					break;
				case COMUNICACIO:
					arxiuInfo.setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto.TD06);
					break;
				case NOTIFICACIO:
					arxiuInfo.setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto.TD07);
					break;
				case PUBLICACIO:
					arxiuInfo.setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto.TD08);
					break;
				case JUSTIFICANT_RECEPCIO:
					arxiuInfo.setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto.TD09);
					break;
				case ACTA:
					arxiuInfo.setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto.TD10);
					break;
				case CERTIFICAT:
					arxiuInfo.setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto.TD11);
					break;
				case DILIGENCIA:
					arxiuInfo.setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto.TD12);
					break;
				case INFORME:
					arxiuInfo.setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto.TD13);
					break;
				case SOLICITUD:
					arxiuInfo.setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto.TD14);
					break;
				case DENUNCIA:
					arxiuInfo.setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto.TD15);
					break;
				case ALEGACIO:
					arxiuInfo.setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto.TD16);
					break;
				case RECURSOS:
					arxiuInfo.setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto.TD17);
					break;
				case COMUNICACIO_CIUTADA:
					arxiuInfo.setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto.TD18);
					break;
				case FACTURA:
					arxiuInfo.setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto.TD19);
					break;
				case ALTRES_INCAUTATS:
					arxiuInfo.setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto.TD20);
					break;
				case ALTRES:
					arxiuInfo.setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto.TD99);
					break;
				}
			}
			if (arxiuDocument.getEniFormatNom() != null)
				arxiuInfo.setEniFormatNom(arxiuDocument.getEniFormatNom().getValor());
			if (arxiuDocument.getEniFormatExtensio() != null)
				arxiuInfo.setEniFormatExtensio(arxiuDocument.getEniFormatExtensio().getValor());
			arxiuInfo.setEniOrgans(arxiuDocument.getEniOrgans());
			arxiuInfo.setEniDocumentOrigenId(arxiuDocument.getEniDocumentOrigenId());
			arxiuInfo.setEniFirmaTipus(arxiuDocument.getEniFirmaTipus());
			arxiuInfo.setEniFirmaCsv(arxiuDocument.getEniFirmaCsv());
			arxiuInfo.setEniFirmaCsvDefinicio(arxiuDocument.getEniFirmaCsvDefinicio());
			arxiuInfo.setMetadades(arxiuDocument.getMetadades());
			arxiuInfo.setAspectes(arxiuDocument.getAspectes());
			if (arxiuDocument.getContinguts() != null) {
				List<ArxiuPluginDocumentContingutDto> continguts = new ArrayList<ArxiuPluginDocumentContingutDto>();
				for (ArxiuDocumentContingut arxiuContingut: arxiuDocument.getContinguts()) {
					ArxiuPluginDocumentContingutDto dto = new ArxiuPluginDocumentContingutDto();
					if (arxiuContingut.getTipus() != null) {
						dto.setTipus(arxiuContingut.getTipus().toString());
					}
					dto.setContentType(arxiuContingut.getContentType());
					continguts.add(dto);
				}
				arxiuInfo.setContinguts(continguts);
			}
			arxiuInfo.setCodiFontPeticio(arxiuDocument.getCodiFontPeticio());
			arxiuInfo.setCodiFontResposta(arxiuDocument.getCodiFontResposta());
			return arxiuInfo;
		} else if (contingut instanceof CarpetaEntity) {
			ArxiuPluginInfoDto arxiuInfo = new ArxiuPluginInfoDto();
			ArxiuCarpeta arxiuCarpeta = pluginHelper.arxiuCarpetaConsultar(
					(CarpetaEntity)contingut);
			arxiuInfo.setNodeId(arxiuCarpeta.getNodeId());
			arxiuInfo.setNom(arxiuCarpeta.getNom());
			arxiuInfo.setDescripcio(arxiuCarpeta.getDescripcio());
			arxiuInfo.setSerieDocumental(arxiuCarpeta.getSerieDocumental());
			arxiuInfo.setAplicacio(arxiuCarpeta.getAplicacio());
			arxiuInfo.setMetadades(arxiuCarpeta.getMetadades());
			arxiuInfo.setAspectes(arxiuCarpeta.getAspectes());
			arxiuInfo.setFills(arxiuConvertirFills(arxiuCarpeta.getFills()));
			arxiuInfo.setCodiFontPeticio(arxiuCarpeta.getCodiFontPeticio());
			arxiuInfo.setCodiFontResposta(arxiuCarpeta.getCodiFontResposta());
			return arxiuInfo;
		} else {
			throw new ValidationException(
					contingutId,
					ContingutEntity.class,
					"El plugin d'arxiu no està actiu");
		}
	}

	@Transactional(readOnly = true)
	@Override
	public FitxerDto exportacioEni(
			Long entitatId,
			Long contingutId) {
		logger.debug("Exportant document a format ENI (" +
				"entitatId=" + entitatId + ", " +
				"contingutId=" + contingutId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		// Comprova l'accés al path del document
		contingutHelper.comprovarPermisosPathContingut(
				contingut,
				true,
				false,
				false,
				true);
		String exportacio;
		if (contingut instanceof ExpedientEntity) {
			exportacio = pluginHelper.arxiuExpedientExportar(
					(ExpedientEntity)contingut);
		} else if (contingut instanceof DocumentEntity) {
			exportacio = pluginHelper.arxiuDocumentExportar(
					(DocumentEntity)contingut);
		} else {
			throw new ValidationException(
					contingutId,
					ContingutEntity.class,
					"El contingut a exportar ha de ser un expedient o un document");
		}
		FitxerDto fitxer = new FitxerDto();
		fitxer.setNom("exportacio_ENI.xml");
		fitxer.setContentType("application/xml");
		fitxer.setContingut(exportacio.getBytes());
		return fitxer;
	}

	@Transactional(readOnly = true)
	@Override
	public List<ContingutComentariDto> findComentarisPerContingut(
			Long entitatId,
			Long contingutId) {
		logger.debug("Obtenint els comentaris pel contingut de bustia ("
				+ "entitatId=" + entitatId + ", "
				+ "nodeId=" + contingutId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		// Comprova que l'usuari tengui accés al contingut
		contingutHelper.comprovarPermisosPathContingut(
				contingut,
				false,
				false,
				false,
				true);
		
		
		return conversioTipusHelper.convertirList(
				contingutComentariRepository.findByContingutOrderByCreatedDateAsc(contingut), 
				ContingutComentariDto.class);
	}
	
	@Transactional
	@Override
	public boolean publicarComentariPerContingut(
			Long entitatId,
			Long contingutId,
			String text) {
		logger.debug("Obtenint els comentaris pel contingut de bustia ("
				+ "entitatId=" + entitatId + ", "
				+ "nodeId=" + contingutId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		// Comprova que l'usuari tengui accés al contingut
		contingutHelper.comprovarPermisosPathContingut(
				contingut,
				false,
				false,
				false,
				true);
		
		//truncam a 1024 caracters
		if (text.length() > 1024)
			text = text.substring(0, 1024);
		
		ContingutComentariEntity comentari = ContingutComentariEntity.getBuilder(
				contingut, 
				text).build();
		
		contingutComentariRepository.save(comentari);
		
		return true;
	}
	
	@Transactional
	@Override
	public boolean marcarProcessat(
			Long entitatId,
			Long contingutId,
			String text) {
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		
		contingut.updateEsborrat(1);
		
		return publicarComentariPerContingut(
				entitatId,
				contingutId,
				text);
	}
	
	@Transactional(readOnly = true)
	@Override
	public PaginaDto<ContingutDto> contingutMassiuFindByDatatable(
			Long entitatId, 
			ContingutMassiuFiltreDto filtre,
			PaginacioParamsDto paginacioParams) throws NotFoundException {
		
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		
		Date dataInici = filtre.getDataInici();
		if (dataInici != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dataInici);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			dataInici = cal.getTime();
		}
		Date dataFi = filtre.getDataFi();
		if (dataFi != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dataFi);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
			dataFi = cal.getTime();
		}
		boolean tipusArxiu = true;
		boolean tipusBustia = true;
		boolean tipusCarpeta = true;
		boolean tipusDocument = true;
		boolean tipusEscriptori = false;
		boolean tipusExpedient = true;
		boolean tipusRegistre = true;
		
		if (filtre.getTipusElement() != null) {
			tipusArxiu = false;
			tipusBustia = false;
			tipusCarpeta = false;
			tipusDocument = false;
			tipusExpedient = false;
			tipusRegistre = false;
			switch (filtre.getTipusElement()) {
			case ARXIU:
				tipusArxiu = true;
				break;
			case BUSTIA:
				tipusBustia = true;
				break;
			case CARPETA:
				tipusCarpeta = true;
				break;
			case DOCUMENT:
				tipusDocument = true;
				break;
			case ESCRIPTORI:
				break;
			case EXPEDIENT:
				tipusExpedient = true;
				break;
			case REGISTRE:
				tipusRegistre = true;
				break;
			}
		}
		
		Long idMetaNode = null;
		MetaNodeEntity metaNode = null;
		
		if (filtre.getTipusElement() == ContingutTipusEnumDto.EXPEDIENT && filtre.getTipusExpedient() != null && !filtre.getTipusExpedient().isEmpty())
			idMetaNode = Long.parseLong(filtre.getTipusExpedient());
		else if (filtre.getTipusElement() == ContingutTipusEnumDto.DOCUMENT && filtre.getTipusDocument() != null && !filtre.getTipusDocument().isEmpty())
			idMetaNode = Long.parseLong(filtre.getTipusDocument());
		
		if (idMetaNode != null) {
			metaNode = metaNodeRepository.findOne(idMetaNode);
			if (metaNode == null) {
				throw new NotFoundException(
						idMetaNode,
						MetaNodeEntity.class);
			}
		}
		
//		List<ContingutEntity>preLlistat = contingutRepository.findContingutMassiuByFiltrePaginat(
//						entitat,
//						tipusArxiu,
//						tipusBustia,
//						tipusCarpeta,
//						tipusDocument,
//						tipusEscriptori,
//						tipusExpedient,
//						tipusRegistre,
//						(filtre.getNom() == null),
//						filtre.getNom(),
//						(metaNode == null),
//						metaNode,
//						(dataInici == null),
//						dataInici,
//						(dataFi == null),
//						dataFi,
//						false,
//						true);
		
		return paginacioHelper.toPaginaDto(
				contingutRepository.findContingutMassiuByFiltrePaginat(
						entitat,
						tipusArxiu,
						tipusBustia,
						tipusCarpeta,
						tipusDocument,
						tipusEscriptori,
						tipusExpedient,
						tipusRegistre,
						(filtre.getNom() == null),
						filtre.getNom(),
						(metaNode == null),
						metaNode,
						(dataInici == null),
						dataInici,
						(dataFi == null),
						dataFi,
						false,
						true,
						paginacioHelper.toSpringDataPageable(paginacioParams)),
				ContingutDto.class,
				new Converter<ContingutEntity, ContingutDto>() {
					@Override
					public ContingutDto convert(ContingutEntity source) {
						return contingutHelper.toContingutDto(
								source,
								false,
								false,
								false,
								false,
								true,
								false,
								false);
					}
				});
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Long> findIdsMassiusAmbFiltre(
			Long entitatId,
			ContingutMassiuFiltreDto filtre) throws NotFoundException {
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
				filtre);
	}
	
	private List<Long> findIdsAmbFiltrePaginat(
			Long entitatId,
			ContingutMassiuFiltreDto filtre) {
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		
		Date dataInici = filtre.getDataInici();
		if (dataInici != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dataInici);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			dataInici = cal.getTime();
		}
		Date dataFi = filtre.getDataFi();
		if (dataFi != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dataFi);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
			dataFi = cal.getTime();
		}
		boolean tipusArxiu = true;
		boolean tipusBustia = true;
		boolean tipusCarpeta = true;
		boolean tipusDocument = true;
		boolean tipusEscriptori = false;
		boolean tipusExpedient = true;
		boolean tipusRegistre = true;
		
		if (filtre.getTipusElement() != null) {
			tipusArxiu = false;
			tipusBustia = false;
			tipusCarpeta = false;
			tipusDocument = false;
			tipusExpedient = false;
			tipusRegistre = false;
			switch (filtre.getTipusElement()) {
			case ARXIU:
				tipusArxiu = true;
				break;
			case BUSTIA:
				tipusBustia = true;
				break;
			case CARPETA:
				tipusCarpeta = true;
				break;
			case DOCUMENT:
				tipusDocument = true;
				break;
			case ESCRIPTORI:
				break;
			case EXPEDIENT:
				tipusExpedient = true;
				break;
			case REGISTRE:
				tipusRegistre = true;
				break;
			}
		}
		
		Long idMetaNode = null;
		MetaNodeEntity metaNode = null;
		
		if (filtre.getTipusElement() == ContingutTipusEnumDto.EXPEDIENT && filtre.getTipusExpedient() != null && !filtre.getTipusExpedient().isEmpty())
			idMetaNode = Long.parseLong(filtre.getTipusExpedient());
		else if (filtre.getTipusElement() == ContingutTipusEnumDto.DOCUMENT && filtre.getTipusDocument() != null && !filtre.getTipusDocument().isEmpty())
			idMetaNode = Long.parseLong(filtre.getTipusDocument());
		
		if (idMetaNode != null) {
			metaNode = metaNodeRepository.findOne(idMetaNode);
			if (metaNode == null) {
				throw new NotFoundException(
						idMetaNode,
						MetaNodeEntity.class);
			}
		}
		return contingutRepository.findIdMassiuByEntitatAndFiltre(
				entitat,
				tipusArxiu,
				tipusBustia,
				tipusCarpeta,
				tipusDocument,
				tipusEscriptori,
				tipusExpedient,
				tipusRegistre,
				(filtre.getNom() == null),
				filtre.getNom(),
				(metaNode == null),
				metaNode,
				(dataInici == null),
				dataInici,
				(dataFi == null),
				dataFi,
				false,
				true);
	}

	private ContingutEntity copiarContingut(
			EntitatEntity entitat,
			ContingutEntity contingutOrigen,
			ContingutEntity contingutDesti,
			boolean recursiu) {
		ContingutEntity creat = null;
		if (contingutOrigen instanceof CarpetaEntity) {
			CarpetaEntity carpetaOrigen = (CarpetaEntity)contingutOrigen;
			CarpetaEntity carpetaNova = CarpetaEntity.getBuilder(
					carpetaOrigen.getNom(),
					contingutDesti,
					entitat).build();
			creat = contingutRepository.save(carpetaNova);
		} else if (contingutOrigen instanceof DocumentEntity) {
			DocumentEntity documentOrigen = (DocumentEntity)contingutOrigen;
			creat = documentHelper.crearNouDocument(
					documentOrigen.getDocumentTipus(),
					documentOrigen.getNom(),
					documentOrigen.getData(),
					documentOrigen.getDataCaptura(),
					documentOrigen.getNtiOrgano(),
					documentOrigen.getNtiOrigen(),
					documentOrigen.getNtiEstadoElaboracion(),
					documentOrigen.getNtiTipoDocumental(),
					documentOrigen.getExpedient(),
					documentOrigen.getMetaDocument(),
					contingutDesti,
					entitat,
					documentOrigen.getUbicacio());
		}
		if (creat != null) {
			if (creat instanceof NodeEntity) {
				NodeEntity nodeOrigen = (NodeEntity)contingutOrigen;
				NodeEntity nodeDesti = (NodeEntity)creat;
				for (DadaEntity dada: dadaRepository.findByNode(nodeOrigen)) {
					DadaEntity dadaNova = DadaEntity.getBuilder(
							dada.getMetaDada(),
							nodeDesti,
							dada.getValor(),
							dada.getOrdre()).build();
					dadaRepository.save(dadaNova);
				}
			}
			if (recursiu) {
				for (ContingutEntity fill: contingutOrigen.getFills()) {
					if (fill instanceof CarpetaEntity || fill instanceof DocumentEntity) {
						copiarContingut(
								entitat,
								fill,
								creat,
								recursiu);
					}
				}
			}
		}
		return creat;
	}

	private void nodeDadaGuardar(
			NodeEntity node,
			String dadaCodi,
			Object dadaValor,
			List<MetaNodeMetaDadaEntity> metaNodeMetaDades,
			List<MetaDadaEntity> metaDadesGlobals) {
		MetaDadaEntity metaDada = null;
		for (MetaNodeMetaDadaEntity metaNodeMetaDada: metaNodeMetaDades) {
			if (metaNodeMetaDada.getMetaDada().getCodi().equals(dadaCodi)) {
				metaDada = metaNodeMetaDada.getMetaDada();
				break;
			}
		}
		if (metaDada == null) {
			for (MetaDadaEntity metaDadaGlobal: metaDadesGlobals) {
				if (metaDadaGlobal.getCodi().equals(dadaCodi)) {
					metaDada = metaDadaGlobal;
					break;
				}
			}
		}
		if (metaDada == null) {
			throw new ValidationException(
					node.getId(),
					NodeEntity.class,
					"No s'ha trobat la metaDada amb el codi " + dadaCodi);
		}
		List<DadaEntity> dades = dadaRepository.findByNodeAndMetaDadaOrderByOrdreAsc(
				node,
				metaDada);
		Object[] valors = (dadaValor instanceof Object[]) ? (Object[])dadaValor : new Object[] {dadaValor};
		// Esborra els valors nulls
		List<Object> valorsSenseNull = new ArrayList<Object>();
		for (Object o: valors) {
			if (o != null)
				valorsSenseNull.add(o);
		}
		// Esborra les dades ja creades que sobren
		if (dades.size() > valorsSenseNull.size()) {
			for (int i = valorsSenseNull.size(); i < dades.size(); i++) {
				dadaRepository.delete(dades.get(i));
			}
		}
		// Modifica o crea les dades
		for (int i = 0; i < valorsSenseNull.size(); i++) {
			DadaEntity dada = (i < dades.size()) ? dades.get(i) : null;
			if (dada != null) {
				dada.update(
						valorsSenseNull.get(i),
						i);
				contingutLogHelper.log(
						node,
						LogTipusEnumDto.MODIFICACIO,
						dada,
						LogObjecteTipusEnumDto.DADA,
						LogTipusEnumDto.MODIFICACIO,
						dadaCodi,
						dada.getValorComString(),
						false,
						false);
			} else {
				dada = DadaEntity.getBuilder(
						metaDada,
						node,
						valorsSenseNull.get(i),
						i).build();
				dadaRepository.save(dada);
				contingutLogHelper.log(
						node,
						LogTipusEnumDto.MODIFICACIO,
						dada,
						LogObjecteTipusEnumDto.DADA,
						LogTipusEnumDto.CREACIO,
						dadaCodi,
						dada.getValorComString(),
						false,
						false);
			}
		}
	}

	private boolean conteDocumentsDefinitius(ContingutEntity contingut) {
		boolean conteDefinitius = false;
		ContingutEntity deproxied = HibernateHelper.deproxy(contingut);
		if (deproxied instanceof ExpedientEntity || deproxied instanceof CarpetaEntity) {
			for (ContingutEntity contingutFill: contingut.getFills()) {
				conteDefinitius = conteDocumentsDefinitius(contingutFill);
				if (conteDefinitius)
					break;
			}
		} else if (deproxied instanceof DocumentEntity) {
			DocumentEntity document = (DocumentEntity)deproxied;
			conteDefinitius = !DocumentEstatEnumDto.REDACCIO.equals(document.getEstat());
		}
		return conteDefinitius;
	}

	private void marcarEsborrat(ContingutEntity contingut) {
		for (ContingutEntity contingutFill: contingut.getFills()) {
			marcarEsborrat(contingutFill);
		}
		List<ContingutEntity> continguts = contingutRepository.findByPareAndNomOrderByEsborratAsc(
				contingut.getPare(),
				contingut.getNom());
		// Per evitar errors de restricció única violada hem de
		// posar al camp esborrat un nombre != 0 i que sigui diferent
		// dels altres fills esborrats amb el mateix nom.
		int index = 1;
		for (ContingutEntity c: continguts) {
			if (c.getEsborrat() > 0) {
				if (index < c.getEsborrat()) {
					break;
				}
				index++;
			}
		}
		contingut.updateEsborrat(continguts.size() + 1);
		contingutLogHelper.log(
				contingut,
				LogTipusEnumDto.ELIMINACIO,
				null,
				null,
				true,
				true);
	}

	private List<ArxiuPluginNodeFillDto> arxiuConvertirFills(
			List<ArxiuFill> arxiuFills) {
		List<ArxiuPluginNodeFillDto> fills = null;
		if (arxiuFills != null) {
			fills = new ArrayList<ArxiuPluginNodeFillDto>();
			for (ArxiuFill fill: arxiuFills) {
				ArxiuPluginNodeFillDto arxiuFill = new ArxiuPluginNodeFillDto();
				arxiuFill.setNodeId(fill.getNodeId());
				arxiuFill.setNom(fill.getNom());
				switch (fill.getTipus()) {
				case EXPEDIENT:
					arxiuFill.setTipus(ArxiuPluginNodeTipusEnumDto.EXPEDIENT);
					break;
				case DOCUMENT:
					arxiuFill.setTipus(ArxiuPluginNodeTipusEnumDto.DOCUMENT);
					break;
				case CARPETA:
					arxiuFill.setTipus(ArxiuPluginNodeTipusEnumDto.CARPETA);
					break;
				case DOCUMENT_MIGRAT:
					arxiuFill.setTipus(ArxiuPluginNodeTipusEnumDto.DOCUMENT_MIGRAT);
					break;
				}
				fills.add(arxiuFill);
			}
		}
		return fills;
	}

	private void fitxerDocumentEsborratGuardar(
			DocumentEntity document) throws IOException {
		File fContent = new File(getBaseDir() + "/" + document.getId());
		fContent.getParentFile().mkdirs();
		FileOutputStream outContent = new FileOutputStream(fContent);
		FitxerDto fitxer = documentHelper.getFitxerAssociat(
				document,
				null);
		outContent.write(fitxer.getContingut());
		outContent.close();
	}

	private FitxerDto fitxerDocumentEsborratLlegir(
			DocumentEntity document) throws IOException {
		File fContent = new File(getBaseDir() + "/" + document.getId());
		fContent.getParentFile().mkdirs();
		if (fContent.exists()) {
			FileInputStream inContent = new FileInputStream(fContent);
			byte fileContent[] = new byte[(int)fContent.length()];
			inContent.read(fileContent);
			inContent.close();
			ArxiuDocumentContingut contingut = new ArxiuDocumentContingut(
					ArxiuContingutTipusEnum.CONTINGUT,
					null,
					fileContent);
			List<ArxiuDocumentContingut> continguts = new ArrayList<ArxiuDocumentContingut>();
			continguts.add(contingut);
			FitxerDto fitxer = new FitxerDto();
			fitxer.setNom(document.getFitxerNom());
			fitxer.setContentType(document.getFitxerContentType());
			fitxer.setContingut(fileContent);
			return fitxer;
		} else {
			return null;
		}
	}

	private void fitxerDocumentEsborratEsborrar(
			DocumentEntity document) {
		File fContent = new File(getBaseDir() + "/" + document.getId());
		fContent.getParentFile().mkdirs();
		fContent.delete();
	}

	private String getBaseDir() {
		return PropertiesHelper.getProperties().getProperty("es.caib.ripea.app.data.dir") + "/esborrats-tmp";
	}

	private static final Logger logger = LoggerFactory.getLogger(ContingutServiceImpl.class);



	

}
