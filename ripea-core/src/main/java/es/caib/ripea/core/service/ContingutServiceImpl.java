/**
 * 
 */
package es.caib.ripea.core.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.ContingutDto;
import es.caib.ripea.core.api.dto.ContingutFiltreDto;
import es.caib.ripea.core.api.dto.ContingutLogDetallsDto;
import es.caib.ripea.core.api.dto.ContingutLogDto;
import es.caib.ripea.core.api.dto.ContingutMovimentDto;
import es.caib.ripea.core.api.dto.DadaDto;
import es.caib.ripea.core.api.dto.EscriptoriDto;
import es.caib.ripea.core.api.dto.LogObjecteTipusEnumDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.api.dto.MetaDadaTipusEnumDto;
import es.caib.ripea.core.api.dto.MultiplicitatEnumDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.dto.ValidacioErrorDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.service.ContingutService;
import es.caib.ripea.core.entity.CarpetaEntity;
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
import es.caib.ripea.core.helper.PaginacioHelper;
import es.caib.ripea.core.helper.PaginacioHelper.Converter;
import es.caib.ripea.core.helper.UsuariHelper;
import es.caib.ripea.core.repository.ContingutRepository;
import es.caib.ripea.core.repository.DadaRepository;
import es.caib.ripea.core.repository.DocumentVersioRepository;
import es.caib.ripea.core.repository.EscriptoriRepository;
import es.caib.ripea.core.repository.MetaNodeRepository;
import es.caib.ripea.core.repository.UsuariRepository;

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
	private DadaRepository dadaRepository;
	@Resource
	private DocumentVersioRepository documentVersioRepository;
	@Resource
	private MetaNodeRepository metaNodeRepository;

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
	private EntityComprovarHelper entityComprovarHelper;



	@Transactional
	@Override
	public ContingutDto rename(
			Long entitatId,
			Long contingutId,
			String nom) {
		logger.debug("Renombrant contingut ("
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
				false);
	}

	@Transactional
	@Override
	@CacheEvict(value = "errorsValidacioNode", key = "#contingutId")
	public DadaDto dadaCreate(
			Long entitatId,
			Long contingutId,
			Long metaDadaId,
			Object valor) {
		logger.debug("Creant nova dada ("
				+ "entitatId=" + entitatId + ", "
				+ "contingutId=" + contingutId + ", "
				+ "metaDadaId=" + metaDadaId + ", "
				+ "valor=" + valor + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		if (contingut instanceof NodeEntity) {
			// Comprova que el contingut arrel és l'escriptori de l'usuari actual
			contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
					entitat,
					contingut);
			// Comprova l'accés al path del contingut pare
			contingutHelper.comprovarPermisosPathContingut(
					contingut,
					true,
					false,
					false,
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
			NodeEntity node = (NodeEntity)contingut;
			MetaDadaEntity metaDada = entityComprovarHelper.comprovarMetaDada(
					entitat,
					metaDadaId);
			List<DadaEntity> dades = dadaRepository.findByNodeAndMetaDada(node, metaDada);
			boolean global = 
					(node instanceof ExpedientEntity && metaDada.isGlobalExpedient()) ||
					(node instanceof DocumentEntity && metaDada.isGlobalDocument());
			if (!global) {
				MetaNodeMetaDadaEntity metaNodeMetaDada = entityComprovarHelper.comprovarMetaNodeMetaDada(
						entitat,
						node.getMetaNode(),
						metaDada);
				// Comprova que no s'afegeixin més dades de les permeses
				if (dades.size() > 0 && (metaNodeMetaDada.getMultiplicitat().equals(MultiplicitatEnumDto.M_0_1) || metaNodeMetaDada.getMultiplicitat().equals(MultiplicitatEnumDto.M_1))) {
					throw new ValidationException(
							contingutId,
							ContingutEntity.class,
							"La multiplicitat del meta-node no permet afegir més dades d'aquest tipus (" +
							"metaNodeId=" + metaNodeMetaDada.getMetaNode().getId() + ", " +
							"metaNodeCodi=" + metaNodeMetaDada.getMetaNode().getCodi() + ", " +
							"metaNodeTipus=" + metaNodeMetaDada.getMetaNode().getClass().getName() + ", " +
							"metaDadaId=" + metaDada.getId() + ", " +
							"metaDadaCodi=" + metaDada.getCodi() + ", " +
							"multiplicitat=" + metaNodeMetaDada.getMultiplicitat() + ")");
				}
			} else {
				if (dades.size() > 0 && (metaDada.getGlobalMultiplicitat().equals(MultiplicitatEnumDto.M_0_1) || metaDada.getGlobalMultiplicitat().equals(MultiplicitatEnumDto.M_1))) {
					throw new ValidationException(
							contingutId,
							ContingutEntity.class,
							"La multiplicitat global no permet afegir més dades d'aquest tipus (" +
							"metaDadaId=" + metaDada.getId() + ", " +
							"metaDadaCodi=" + metaDada.getCodi() + ", " +
							"multiplicitat=" + metaDada.getGlobalMultiplicitat() + ")");
				}
			}
			int ordre = dades.size();
			DadaEntity dada = DadaEntity.getBuilder(
					metaDada,
					node,
					getDadaValorPerGuardar(
							metaDada,
							valor),
					ordre).build();
			dadaRepository.save(dada);
			// Refresca la validesa del node
			cacheHelper.evictErrorsValidacioPerNode(node);
			// Registra al log la creació de la dada
			contingutLogHelper.log(
					contingut,
					LogTipusEnumDto.MODIFICACIO,
					dada,
					LogObjecteTipusEnumDto.DADA,
					LogTipusEnumDto.CREACIO,
					metaDada.getCodi(),
					dada.getValor(),
					false,
					false);
			return conversioTipusHelper.convertir(
					dada,
					DadaDto.class);
		} else {
			logger.error("El contingut no és un node (contingutId=" + contingutId + ")");
			throw new ValidationException(
					contingutId,
					ContingutEntity.class,
					"El contingut no és un node");
		}
	}

	@Transactional
	@Override
	public DadaDto dadaUpdate(
			Long entitatId,
			Long contingutId,
			Long dadaId,
			Object valor) {
		logger.debug("Modificant dada ("
				+ "entitatId=" + entitatId + ", "
				+ "contingutId=" + contingutId + ", "
				+ "dadaId=" + dadaId + ", "
				+ "valor=" + valor + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		if (contingut instanceof NodeEntity) {
			// Comprova que el contingut arrel és l'escriptori de l'usuari actual
			contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
					entitat,
					contingut);
			// Comprova l'accés al path del contingut pare
			contingutHelper.comprovarPermisosPathContingut(
					contingut,
					true,
					false,
					false,
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
			NodeEntity node = (NodeEntity)contingut;
			DadaEntity dada = entityComprovarHelper.comprovarDada(
					node,
					dadaId);
			dada.update(
					getDadaValorPerGuardar(
							dada.getMetaDada(),
							valor));
			// Registra al log la modificació de la dada
			contingutLogHelper.log(
					contingut,
					LogTipusEnumDto.MODIFICACIO,
					dada,
					LogObjecteTipusEnumDto.DADA,
					LogTipusEnumDto.MODIFICACIO,
					dada.getMetaDada().getCodi(),
					dada.getValor(),
					false,
					false);
			return conversioTipusHelper.convertir(
					dada,
					DadaDto.class);
		} else {
			logger.error("El contingut no és un node (contingutId=" + contingutId + ")");
			throw new ValidationException(
					contingutId,
					ContingutEntity.class,
					"El contingut no és un node");
		}
	}

	@Transactional
	@Override
	@CacheEvict(value = "errorsValidacioNode", key = "#contingutId")
	public DadaDto dadaDelete(
			Long entitatId,
			Long contingutId,
			Long dadaId) {
		logger.debug("Esborrant la dada ("
				+ "entitatId=" + entitatId + ", "
				+ "contingutId=" + contingutId + ", "
				+ "dadaId=" + dadaId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		if (contingut instanceof NodeEntity) {
			// Comprova que el contingut arrel és l'escriptori de l'usuari actual
			contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
					entitat,
					contingut);
			// Comprova l'accés al path del contingut pare
			contingutHelper.comprovarPermisosPathContingut(
					contingut,
					true,
					false,
					false,
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
			NodeEntity node = (NodeEntity)contingut;
			DadaEntity dada = entityComprovarHelper.comprovarDada(
					node,
					dadaId);
			dadaRepository.delete(dada);
			// Refresca la validesa del node
			cacheHelper.evictErrorsValidacioPerNode(node);
			// Registra al log la eliminació de la dada
			contingutLogHelper.log(
					contingut,
					LogTipusEnumDto.MODIFICACIO,
					dada,
					LogObjecteTipusEnumDto.DADA,
					LogTipusEnumDto.ELIMINACIO,
					dada.getMetaDada().getCodi(),
					dada.getValor(),
					false,
					false);
			return conversioTipusHelper.convertir(
					dada,
					DadaDto.class);
		} else {
			logger.error("El contingut no és un node (contingutId=" + contingutId + ")");
			throw new ValidationException(
					contingutId,
					ContingutEntity.class,
					"El contingut no és un node");
		}
	}

	@Transactional(readOnly = true)
	@Override
	public DadaDto dadaFindById(
			Long entitatId,
			Long contingutId,
			Long dadaId) {
		logger.debug("Obtenint la dada ("
				+ "entitatId=" + entitatId + ", "
				+ "contingutId=" + contingutId + ", "
				+ "dadaId=" + dadaId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
				null);
		if (contingut instanceof NodeEntity) {
			// Per a consultes no es comprova el contingut arrel
			// Comprova l'accés al path del contingut pare
			contingutHelper.comprovarPermisosPathContingut(
					contingut,
					true,
					false,
					false,
					true);
			NodeEntity node = (NodeEntity)contingut;
			DadaEntity dada = entityComprovarHelper.comprovarDada(
					node,
					dadaId);
			return conversioTipusHelper.convertir(
					dada,
					DadaDto.class);
		} else {
			logger.error("El contingut no és un node (contingutId=" + contingutId + ")");
			throw new ValidationException(
					contingutId,
					ContingutEntity.class,
					"El contingut no és un node");
		}
	}

	@Transactional
	@Override
	public ContingutDto deleteReversible(
			Long entitatId,
			Long contingutId) {
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
				false);
		// Comprova que el contingut no estigui esborrat
		if (contingut.getEsborrat() > 0) {
			logger.error("Aquest contingut ja està esborrat (contingutId=" + contingutId + ")");
			throw new ValidationException(
					contingutId,
					ContingutEntity.class,
					"Aquest contingut ja està esborrat");
		}
		// Marca el contingut com a esborrat
		List<ContingutEntity> continguts = contingutRepository.findByPareAndNomOrderByEsborratAsc(
				contingut.getPare(),
				contingut.getNom());
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
		// Registra al log l'eliminació del contingut
		contingutLogHelper.log(
				contingut,
				LogTipusEnumDto.ELIMINACIO,
				null,
				null,
				true,
				true);
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
		// Comprova que el contingut estigui esborrat
		if (contingut.getEsborrat() == 0) {
			logger.error("Aquest contingut no està esborrat (contingutId=" + contingutId + ")");
			throw new ValidationException(
					contingutId,
					ContingutEntity.class,
					"Aquest contingut no està esborrat");
		}
		// Esborra definitivament el contingut
		ContingutDto dto = contingutHelper.toContingutDto(
				contingut,
				true,
				false,
				false,
				false,
				false,
				false);
		if (contingut.getPare() != null) {
			contingut.getPare().getFills().remove(contingut);
		}
		//TODO: es comenta perquè no es pot afegir contingut amb FK sobre quelcom que ja no existeix
//		// Registra al log l'eliminació definitiva del contingut
//		contingutLogHelper.log(
//				contingut,
//				LogTipusEnumDto.ELIMINACIODEF,
//				null,
//				null,
//				true,
//				true);
		return dto;
	}

	@Transactional
	@Override
	public ContingutDto undelete(
			Long entitatId,
			Long contingutId) {
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
		contingut.updateEsborrat(0);
		// Registra al log la recuperació del contingut
		contingutLogHelper.log(
				contingut,
				LogTipusEnumDto.RECUPERACIO,
				null,
				null,
				true,
				true);
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
		if (contingutOrigen instanceof ExpedientEntity) {
			logger.error("Els expedients no es poden moure ("
					+ "entitatId=" + entitatId + ", "
					+ "expedientId=" + contingutOrigenId + ")");
			throw new ValidationException(
					contingutOrigenId,
					ExpedientEntity.class,
					"Els expedients no es poden moure");
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
				true,
				false,
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
		ContingutMovimentEntity contingutMoviment = contingutHelper.ferIEnregistrarMoviment(
				contingutOrigen,
				contingutDesti,
				null);
		// Registra al log el moviment del node
		contingutLogHelper.log(
				contingutOrigen,
				LogTipusEnumDto.MOVIMENT,
				contingutMoviment,
				true,
				true);
		return contingutHelper.toContingutDto(
				contingutOrigen,
				true,
				false,
				false,
				false,
				false,
				false);
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
		if (contingutOrigen instanceof ExpedientEntity) {
			logger.error("Els expedients no es poden copiar ("
					+ "entitatId=" + entitatId + ", "
					+ "expedientId=" + contingutOrigenId + ")");
			throw new ValidationException(
					contingutOrigenId,
					ExpedientEntity.class,
					"Els expedients no es poden copiar");
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
				true,
				false,
				false,
				true);
		// Comprova si pertanyen al mateix expedient o si no pertanyen a cap
		ExpedientEntity expedientSuperiorOrigen = contingutHelper.getExpedientSuperior(
				contingutOrigen,
				true,
				false,
				false);
		ExpedientEntity expedientSuperiorDesti = contingutHelper.getExpedientSuperior(
				contingutDesti,
				true,
				false,
				false);
		if (	(expedientSuperiorOrigen != null && expedientSuperiorDesti == null) ||
				(expedientSuperiorOrigen == null && expedientSuperiorDesti != null)) {
			logger.error("Els continguts origen i destí no pertanyen al mateix expedient ("
					+ "contingutOrigenId=" + contingutOrigenId + ", "
					+ "contingutDestiId=" + contingutDestiId + ")");
			throw new SecurityException("Els continguts origen i destí no pertanyen al mateix expedient ("
					+ "contingutOrigenId=" + contingutOrigenId + ", "
					+ "contingutDestiId=" + contingutDestiId + ")");
		}
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
		// Fa la còpia del contingut
		ContingutEntity contingutCopia = copiarContingut(
				entitat,
				contingutOrigen,
				contingutDesti,
				recursiu);
		// Registra al log la còpia del node
		contingutLogHelper.log(
				contingutCopia,
				LogTipusEnumDto.COPIA,
				null,
				null,
				true,
				true);
		return contingutHelper.toContingutDto(
				contingutOrigen,
				true,
				false,
				false,
				false,
				false,
				false);
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
				false);
	}

	@Transactional(readOnly = true)
	@Override
	public ContingutDto findAmbIdUser(
			Long entitatId,
			Long contingutId,
			boolean ambFills) {
		logger.debug("Obtenint contingut amb id per usuari ("
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
		contingutHelper.comprovarPermisosPathContingut(
				contingut,
				true,
				false,
				false,
				true);
		return contingutHelper.toContingutDto(
				contingut,
				true,
				ambFills,
				ambFills,
				true,
				true,
				false);
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
				false);
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
				false);
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
								false);
					}
				});
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
					carpetaOrigen.getCarpetaTipus(),
					contingutDesti,
					entitat).build();
			creat = contingutRepository.save(carpetaNova);
		} else if (contingutOrigen instanceof DocumentEntity) {
			DocumentEntity documentOrigen = (DocumentEntity)contingutOrigen;
			creat = contingutHelper.crearNouDocument(
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
					documentOrigen.getUbicacio(),
					documentHelper.getFitxerAssociat(
							documentOrigen.getVersioDarrera()));
		} else if (contingutOrigen instanceof ExpedientEntity) {
			ExpedientEntity expedientOrigen = (ExpedientEntity)contingutOrigen;
			creat = contingutHelper.crearNouExpedient(
					expedientOrigen.getNom(),
					expedientOrigen.getMetaExpedient(),
					expedientOrigen.getArxiu(),
					contingutDesti,
					entitat,
					expedientOrigen.getNtiVersion(),
					expedientOrigen.getNtiOrgano(),
					new Date(),
					null);
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
				// TODO copiar contingut recursivament
			}
		}
		return creat;
	}

	private String getDadaValorPerGuardar(
			MetaDadaEntity metaDada,
			Object valor) {
		if (metaDada.getTipus().equals(MetaDadaTipusEnumDto.TEXT)) {
			if (valor instanceof String) {
				return (String)valor;
			} else {
				throw new RuntimeException();
			}
		} else if (metaDada.getTipus().equals(MetaDadaTipusEnumDto.DATA)) {
			if (valor instanceof Date) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				return sdf.format((Date)valor);
			} else {
				throw new RuntimeException();
			}
		} else if (metaDada.getTipus().equals(MetaDadaTipusEnumDto.SENCER)) {
			if (valor instanceof Long) {
				return ((Long)valor).toString();
			} else {
				throw new RuntimeException();
			}
		} else if (metaDada.getTipus().equals(MetaDadaTipusEnumDto.FLOTANT)) {
			if (valor instanceof Double) {
				return ((Double)valor).toString();
			} else {
				throw new RuntimeException();
			}
		} else if (metaDada.getTipus().equals(MetaDadaTipusEnumDto.IMPORT)) {
			if (valor instanceof BigDecimal) {
				return ((BigDecimal)valor).toString();
			} else {
				throw new RuntimeException();
			}
		} else if (metaDada.getTipus().equals(MetaDadaTipusEnumDto.BOOLEA)) {
			if (valor instanceof Boolean) {
				return ((Boolean)valor).toString();
			} else {
				throw new RuntimeException();
			}
		}
		throw new RuntimeException();
	}

	private static final Logger logger = LoggerFactory.getLogger(ContingutServiceImpl.class);

}
