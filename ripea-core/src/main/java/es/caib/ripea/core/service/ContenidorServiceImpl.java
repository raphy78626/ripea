/**
 * 
 */
package es.caib.ripea.core.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import es.caib.ripea.core.api.dto.ContenidorDto;
import es.caib.ripea.core.api.dto.ContenidorFiltreDto;
import es.caib.ripea.core.api.dto.ContenidorLogDto;
import es.caib.ripea.core.api.dto.ContenidorMovimentDto;
import es.caib.ripea.core.api.dto.ContenidorTipusEnumDto;
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
import es.caib.ripea.core.api.service.ContenidorService;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.CarpetaEntity;
import es.caib.ripea.core.entity.ContenidorEntity;
import es.caib.ripea.core.entity.ContenidorMovimentEntity;
import es.caib.ripea.core.entity.DadaEntity;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.DocumentVersioEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.EscriptoriEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.MetaDadaEntity;
import es.caib.ripea.core.entity.MetaNodeMetaDadaEntity;
import es.caib.ripea.core.entity.NodeEntity;
import es.caib.ripea.core.entity.UsuariEntity;
import es.caib.ripea.core.helper.BustiaHelper;
import es.caib.ripea.core.helper.CacheHelper;
import es.caib.ripea.core.helper.ContenidorHelper;
import es.caib.ripea.core.helper.ContenidorLogHelper;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.EmailHelper;
import es.caib.ripea.core.helper.EntityComprovarHelper;
import es.caib.ripea.core.helper.MetaNodeHelper;
import es.caib.ripea.core.helper.PaginacioHelper;
import es.caib.ripea.core.helper.PaginacioHelper.Converter;
import es.caib.ripea.core.helper.PermisosHelper;
import es.caib.ripea.core.helper.UsuariHelper;
import es.caib.ripea.core.repository.BustiaRepository;
import es.caib.ripea.core.repository.ContenidorLogRepository;
import es.caib.ripea.core.repository.ContenidorRepository;
import es.caib.ripea.core.repository.DadaRepository;
import es.caib.ripea.core.repository.DocumentVersioRepository;
import es.caib.ripea.core.repository.EntitatRepository;
import es.caib.ripea.core.repository.EscriptoriRepository;
import es.caib.ripea.core.repository.MetaDadaRepository;
import es.caib.ripea.core.repository.MetaNodeMetaDadaRepository;
import es.caib.ripea.core.repository.UsuariRepository;

/**
 * Implementació dels mètodes per a gestionar contenidors.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class ContenidorServiceImpl implements ContenidorService {

	@Resource
	private EntitatRepository entitatRepository;
	@Resource
	private UsuariRepository usuariRepository;
	@Resource
	private EscriptoriRepository escriptoriRepository;
	@Resource
	private ContenidorRepository contenidorRepository;
	@Resource
	private ContenidorLogRepository contenidorLogRepository;
	@Resource
	private MetaDadaRepository metaDadaRepository;
	@Resource
	private DadaRepository dadaRepository;
	@Resource
	private MetaNodeMetaDadaRepository metaNodeMetaDadaRepository;
	@Resource
	private DocumentVersioRepository documentVersioRepository;
	@Resource
	private BustiaRepository bustiaRepository;

	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	PaginacioHelper paginacioHelper;
	@Resource
	private PermisosHelper permisosHelper;
	@Resource
	private CacheHelper cacheHelper;
	@Resource
	private MetaNodeHelper metaNodeHelper;
	@Resource
	private ContenidorHelper contenidorHelper;
	@Resource
	private ContenidorLogHelper contenidorLogHelper;
	@Resource
	private EmailHelper emailHelper;
	@Resource
	private BustiaHelper bustiaHelper;
	@Resource
	private UsuariHelper usuariHelper;
	@Resource
	private EntityComprovarHelper entityComprovarHelper;



	@Transactional
	@Override
	public ContenidorDto rename(
			Long entitatId,
			Long contenidorId,
			String nom) {
		logger.debug("Renombrant contenidor ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ", "
				+ "nom=" + nom + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidor = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorId,
				null);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				contenidor);
		// Comprova l'accés al path del contenidor
		contenidorHelper.comprovarPermisosPathContenidor(
				contenidor,
				false,
				false,
				true,
				true);
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
		}
		if (!contenidorHelper.isNomValid(nom)) {
			throw new ValidationException(
					contenidorId,
					ContenidorEntity.class,
					"El nom del contenidor no és vàlid (no pot començar amb \".\")");
		}
		// Canvia el nom del contenidor
		String nomAnterior = contenidor.getNom();
		contenidor.update(nom);
		// Registra al log la modificació del contenidor
		contenidorLogHelper.log(
				contenidor,
				LogTipusEnumDto.MODIFICACIO,
				null,
				null,
				"Canvi de nom",
				nomAnterior,
				true,
				true);
		return contenidorHelper.toContenidorDto(
				contenidor,
				true,
				true,
				true,
				false,
				false,
				false);
	}

	@Transactional
	@Override
	@CacheEvict(value = "errorsValidacioNode", key = "#contenidorId")
	public DadaDto dadaCreate(
			Long entitatId,
			Long contenidorId,
			Long metaDadaId,
			Object valor) {
		logger.debug("Creant nova dada ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ", "
				+ "metaDadaId=" + metaDadaId + ", "
				+ "valor=" + valor + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidor = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorId,
				null);
		if (contenidor instanceof NodeEntity) {
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
			}
			NodeEntity node = (NodeEntity)contenidor;
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
							contenidorId,
							ContenidorEntity.class,
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
							contenidorId,
							ContenidorEntity.class,
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
			contenidorLogHelper.log(
					contenidor,
					LogTipusEnumDto.MODIFICACIO,
					null,
					null,
					dada,
					LogObjecteTipusEnumDto.DADA,
					LogTipusEnumDto.CREACIO,
					metaDada.getCodi(),
					dada.getValor(),
					true,
					true);
			return conversioTipusHelper.convertir(
					dada,
					DadaDto.class);
		} else {
			logger.error("El contenidor no és un node (contenidorId=" + contenidorId + ")");
			throw new ValidationException(
					contenidorId,
					ContenidorEntity.class,
					"El contenidor no és un node");
		}
	}

	@Transactional
	@Override
	public DadaDto dadaUpdate(
			Long entitatId,
			Long contenidorId,
			Long dadaId,
			Object valor) {
		logger.debug("Modificant dada ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ", "
				+ "dadaId=" + dadaId + ", "
				+ "valor=" + valor + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidor = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorId,
				null);
		if (contenidor instanceof NodeEntity) {
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
			}
			NodeEntity node = (NodeEntity)contenidor;
			DadaEntity dada = entityComprovarHelper.comprovarDada(
					node,
					dadaId);
			dada.update(
					getDadaValorPerGuardar(
							dada.getMetaDada(),
							valor));
			// Registra al log la modificació de la dada
			contenidorLogHelper.log(
					contenidor,
					LogTipusEnumDto.MODIFICACIO,
					null,
					null,
					dada,
					LogObjecteTipusEnumDto.DADA,
					LogTipusEnumDto.MODIFICACIO,
					dada.getMetaDada().getCodi(),
					dada.getValor(),
					true,
					true);
			return conversioTipusHelper.convertir(
					dada,
					DadaDto.class);
		} else {
			logger.error("El contenidor no és un node (contenidorId=" + contenidorId + ")");
			throw new ValidationException(
					contenidorId,
					ContenidorEntity.class,
					"El contenidor no és un node");
		}
	}

	@Transactional
	@Override
	@CacheEvict(value = "errorsValidacioNode", key = "#contenidorId")
	public DadaDto dadaDelete(
			Long entitatId,
			Long contenidorId,
			Long dadaId) {
		logger.debug("Esborrant la dada ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ", "
				+ "dadaId=" + dadaId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidor = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorId,
				null);
		if (contenidor instanceof NodeEntity) {
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
			}
			NodeEntity node = (NodeEntity)contenidor;
			DadaEntity dada = entityComprovarHelper.comprovarDada(
					node,
					dadaId);
			dadaRepository.delete(dada);
			// Refresca la validesa del node
			cacheHelper.evictErrorsValidacioPerNode(node);
			// Registra al log la eliminació de la dada
			contenidorLogHelper.log(
					contenidor,
					LogTipusEnumDto.MODIFICACIO,
					null,
					null,
					dada,
					LogObjecteTipusEnumDto.DADA,
					LogTipusEnumDto.ELIMINACIO,
					dada.getMetaDada().getCodi(),
					null,
					true,
					true);
			return conversioTipusHelper.convertir(
					dada,
					DadaDto.class);
		} else {
			logger.error("El contenidor no és un node (contenidorId=" + contenidorId + ")");
			throw new ValidationException(
					contenidorId,
					ContenidorEntity.class,
					"El contenidor no és un node");
		}
	}

	@Transactional(readOnly = true)
	@Override
	public DadaDto dadaFindById(
			Long entitatId,
			Long contenidorId,
			Long dadaId) {
		logger.debug("Obtenint la dada ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ", "
				+ "dadaId=" + dadaId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidor = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorId,
				null);
		if (contenidor instanceof NodeEntity) {
			// Per a consultes no es comprova el contenidor arrel
			// Comprova l'accés al path del contenidor pare
			contenidorHelper.comprovarPermisosPathContenidor(
					contenidor,
					true,
					false,
					false,
					true);
			NodeEntity node = (NodeEntity)contenidor;
			DadaEntity dada = entityComprovarHelper.comprovarDada(
					node,
					dadaId);
			return conversioTipusHelper.convertir(
					dada,
					DadaDto.class);
		} else {
			logger.error("El contenidor no és un node (contenidorId=" + contenidorId + ")");
			throw new ValidationException(
					contenidorId,
					ContenidorEntity.class,
					"El contenidor no és un node");
		}
	}

	@Transactional
	@Override
	public ContenidorDto deleteReversible(
			Long entitatId,
			Long contenidorId) {
		logger.debug("Esborrant el contenidor ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidor = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorId,
				null);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				contenidor);
		// Comprova l'accés al path del contenidor
		contenidorHelper.comprovarPermisosPathContenidor(
				contenidor,
				true,
				false,
				false,
				true);
		// Comprova el permís de modificació de l'expedient superior
		ExpedientEntity expedientSuperior = contenidorHelper.getExpedientSuperior(
				contenidor,
				false);
		if (expedientSuperior != null) {
			contenidorHelper.comprovarPermisosContenidor(
					expedientSuperior,
					false,
					true,
					false);
		}
		// Comprova el permís d'esborrar del contenidor
		contenidorHelper.comprovarPermisosContenidor(
				contenidor,
				false,
				false,
				true);
		ContenidorDto dto = contenidorHelper.toContenidorDto(
				contenidor,
				true,
				false,
				false,
				false,
				false,
				false);
		// Comprova que el contenidor no estigui esborrat
		if (contenidor.getEsborrat() > 0) {
			logger.error("Aquest contenidor ja està esborrat (contenidorId=" + contenidorId + ")");
			throw new ValidationException(
					contenidorId,
					ContenidorEntity.class,
					"Aquest contenidor ja està esborrat");
		}
		// Marca el contenidor com a esborrat
		List<ContenidorEntity> contenidors = contenidorRepository.findByPareAndNomOrderByEsborratAsc(
				contenidor.getPare(),
				contenidor.getNom());
		int index = 1;
		for (ContenidorEntity c: contenidors) {
			if (c.getEsborrat() > 0) {
				if (index < c.getEsborrat()) {
					break;
				}
				index++;
			}
		}
		contenidor.updateEsborrat(contenidors.size() + 1);
		// Registra al log l'eliminació del contenidor
		contenidorLogHelper.log(
				contenidor,
				LogTipusEnumDto.ELIMINACIO,
				null,
				null,
				true,
				true);
		return dto;
	}

	@Transactional
	@Override
	@CacheEvict(value = "errorsValidacioNode", key = "#contenidorId")
	public ContenidorDto deleteDefinitiu(
			Long entitatId,
			Long contenidorId) {
		logger.debug("Esborrant el contenidor ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ContenidorEntity contenidor = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorId,
				null);
		// Comprova que el contenidor estigui esborrat
		if (contenidor.getEsborrat() == 0) {
			logger.error("Aquest contenidor no està esborrat (contenidorId=" + contenidorId + ")");
			throw new ValidationException(
					contenidorId,
					ContenidorEntity.class,
					"Aquest contenidor no està esborrat");
		}
		// Esborra definitivament el contenidor
		ContenidorDto dto = contenidorHelper.toContenidorDto(
				contenidor,
				true,
				false,
				false,
				false,
				false,
				false);
		if (contenidor.getPare() != null) {
			contenidor.getPare().getFills().remove(contenidor);
		}
		// Registra al log l'eliminació definitiva del contenidor
		contenidorLogHelper.log(
				contenidor,
				LogTipusEnumDto.ELIMINACIODEF,
				null,
				null,
				true,
				true);
		return dto;
	}

	@Transactional
	@Override
	public ContenidorDto undelete(
			Long entitatId,
			Long contenidorId) {
		logger.debug("Recuperant el contenidor ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ContenidorEntity contenidor = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorId,
				null);
		// No es comproven permisos perquè això només ho pot fer l'administrador
		if (contenidor.getEsborrat() == 0) {
			logger.error("Aquest contenidor no està esborrat (contenidorId=" + contenidorId + ")");
			throw new ValidationException(
					contenidorId,
					ContenidorEntity.class,
					"Aquest contenidor no està esborrat");
		}
		if (contenidor.getPare() == null) {
			logger.error("Aquest contenidor no te pare (contenidorId=" + contenidorId + ")");
			throw new ValidationException(
					contenidorId,
					ContenidorEntity.class,
					"Aquest contenidor no te pare");
		}
		boolean nomDuplicat = contenidorRepository.findByPareAndNomAndEsborrat(
				contenidor.getPare(),
				contenidor.getNom(),
				0) != null;
		if (nomDuplicat) {
			throw new ValidationException(
					contenidorId,
					ContenidorEntity.class,
					"Ja existeix un altre contenidor amb el mateix nom dins el mateix pare");
		}
		contenidor.updateEsborrat(0);
		// Registra al log la recuperació del contenidor
		contenidorLogHelper.log(
				contenidor,
				LogTipusEnumDto.RECUPERACIO,
				null,
				null,
				true,
				true);
		return contenidorHelper.toContenidorDto(
				contenidor,
				true,
				false,
				false,
				false,
				false,
				false);
	}

	@Transactional
	@Override
	public ContenidorDto move(
			Long entitatId,
			Long contenidorOrigenId,
			Long contenidorDestiId) {
		logger.debug("Movent el contenidor ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorOrigenId=" + contenidorOrigenId + ", "
				+ "contenidorDestiId=" + contenidorDestiId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidorOrigen = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorOrigenId,
				null);
		if (contenidorOrigen instanceof ExpedientEntity) {
			logger.error("Els expedients no es poden moure ("
					+ "entitatId=" + entitatId + ", "
					+ "expedientId=" + contenidorOrigenId + ")");
			throw new ValidationException(
					contenidorOrigenId,
					ExpedientEntity.class,
					"Els expedients no es poden moure");
		}
		// Comprova que el contenidorOrigen arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				contenidorOrigen);
		// Comprova l'accés al path del contenidorOrigen
		contenidorHelper.comprovarPermisosPathContenidor(
				contenidorOrigen,
				true,
				false,
				false,
				true);
		ContenidorEntity contenidorDesti = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorDestiId,
				null);
		// Comprova que el contenidorDesti arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				contenidorDesti);
		// Comprova l'accés al path del contenidorDesti
		contenidorHelper.comprovarPermisosPathContenidor(
				contenidorDesti,
				true,
				false,
				false,
				true);
		// Comprova que el nom no sigui duplicat
		boolean nomDuplicat = contenidorRepository.findByPareAndNomAndEsborrat(
				contenidorDesti,
				contenidorOrigen.getNom(),
				0) != null;
		if (nomDuplicat) {
			throw new ValidationException(
					contenidorOrigenId,
					ContenidorEntity.class,
					"Ja existeix un altre contenidor amb el mateix nom dins el contenidor destí ("
							+ "contenidorDestiId=" + contenidorDestiId + ")");
		}
		ContenidorMovimentEntity contenidorMoviment = contenidorHelper.ferIEnregistrarMovimentContenidor(
				contenidorOrigen,
				contenidorDesti,
				null);
		// Registra al log el moviment del node
		contenidorLogHelper.log(
				contenidorOrigen,
				LogTipusEnumDto.MOVIMENT,
				null,
				contenidorMoviment,
				true,
				true);
		return contenidorHelper.toContenidorDto(
				contenidorOrigen,
				true,
				false,
				false,
				false,
				false,
				false);
	}

	@Transactional
	@Override
	public ContenidorDto copy(
			Long entitatId,
			Long contenidorOrigenId,
			Long contenidorDestiId,
			boolean recursiu) {
		logger.debug("Copiant el contenidor ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorOrigenId=" + contenidorOrigenId + ", "
				+ "contenidorDestiId=" + contenidorDestiId + ", "
				+ "recursiu=" + recursiu + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidorOrigen = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorOrigenId,
				null);
		if (contenidorOrigen instanceof ExpedientEntity) {
			logger.error("Els expedients no es poden copiar ("
					+ "entitatId=" + entitatId + ", "
					+ "expedientId=" + contenidorOrigenId + ")");
			throw new ValidationException(
					contenidorOrigenId,
					ExpedientEntity.class,
					"Els expedients no es poden copiar");
		}
		// Comprova que el contenidorOrigen arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				contenidorOrigen);
		// Comprova l'accés al path del contenidorOrigen
		contenidorHelper.comprovarPermisosPathContenidor(
				contenidorOrigen,
				true,
				false,
				false,
				true);
		ContenidorEntity contenidorDesti = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorDestiId,
				null);
		// Comprova que el contenidorDesti arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				contenidorDesti);
		// Comprova l'accés al path del contenidorDesti
		contenidorHelper.comprovarPermisosPathContenidor(
				contenidorDesti,
				true,
				false,
				false,
				true);
		// Comprova si pertanyen al mateix expedient o si no pertanyen a cap
		ExpedientEntity expedientSuperiorOrigen = contenidorHelper.getExpedientSuperior(
				contenidorOrigen,
				true);
		ExpedientEntity expedientSuperiorDesti = contenidorHelper.getExpedientSuperior(
				contenidorDesti,
				true);
		if (	(expedientSuperiorOrigen != null && expedientSuperiorDesti == null) ||
				(expedientSuperiorOrigen == null && expedientSuperiorDesti != null)) {
			logger.error("Els contenidors origen i destí no pertanyen al mateix expedient ("
					+ "contenidorOrigenId=" + contenidorOrigenId + ", "
					+ "contenidorDestiId=" + contenidorDestiId + ")");
			throw new SecurityException("Els contenidors origen i destí no pertanyen al mateix expedient ("
					+ "contenidorOrigenId=" + contenidorOrigenId + ", "
					+ "contenidorDestiId=" + contenidorDestiId + ")");
		}
		// Comprova que el nom no sigui duplicat
		boolean nomDuplicat = contenidorRepository.findByPareAndNomAndEsborrat(
				contenidorDesti,
				contenidorOrigen.getNom(),
				0) != null;
		if (nomDuplicat) {
			throw new ValidationException(
					contenidorOrigenId,
					ContenidorEntity.class,
					"Ja existeix un altre contenidor amb el mateix nom dins el contenidor destí ("
							+ "contenidorDestiId=" + contenidorDestiId + ")");
		}
		// Fa la còpia del contenidor
		ContenidorEntity contenidorCopia = copiarContenidor(
				entitat,
				contenidorOrigen,
				contenidorDesti,
				recursiu);
		// Registra al log la còpia del node
		contenidorLogHelper.log(
				contenidorCopia,
				LogTipusEnumDto.COPIA,
				null,
				null,
				true,
				true);
		return contenidorHelper.toContenidorDto(
				contenidorOrigen,
				true,
				false,
				false,
				false,
				false,
				false);
	}

	@Transactional
	@Override
	public ContenidorDto send(
			Long entitatId,
			Long contenidorId,
			Long bustiaId,
			String comentari) {
		logger.debug("Enviant contenidor a bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ", "
				+ "bustiaId=" + bustiaId + ","
				+ "comentari=" + comentari + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidor = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorId,
				null);
		// Comprova que el contenidorOrigen arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				contenidor);
		// Comprova que aquest contenidor no pertanyi a cap expedient
		ExpedientEntity expedientActual = contenidorHelper.getExpedientSuperior(
				contenidor,
				true);
		if (expedientActual != null) {
			logger.error("No es pot enviar un node que pertany a un expedient");
			throw new ValidationException(
					contenidorId,
					ContenidorEntity.class,
					"No es pot enviar un node que pertany a un expedient");
		}
		// Comprova l'accés al path del contenidorOrigen
		contenidorHelper.comprovarPermisosPathContenidor(
				contenidor,
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
		ContenidorMovimentEntity contenidorMoviment = contenidorHelper.ferIEnregistrarMovimentContenidor(
				contenidor,
				bustia,
				comentari);
		// Registra al log l'enviament del contenidor
		contenidorLogHelper.log(
				contenidor,
				LogTipusEnumDto.ENVIAMENT,
				null,
				contenidorMoviment,
				true,
				true);
		// Avisam per correu als responsables de la bústia de destí
		emailHelper.emailBustiaPendentContenidor(
				bustia,
				contenidor,
				contenidorMoviment);
		// Refrescam cache usuaris bústia de destí
		bustiaHelper.evictElementsPendentsBustia(
				entitat,
				bustia);
		return contenidorHelper.toContenidorDto(
				contenidor,
				true,
				false,
				false,
				false,
				false,
				false);
	}

	@Override
	@Transactional
	public ContenidorDto receive(
			Long entitatId,
			Long bustiaId,
			Long contenidorOrigenId,
			Long contenidorDestiId) {
		logger.debug("Processant contenidor pendent ("
				+ "entitatId=" + entitatId + ", "
				+ "bustiaId=" + bustiaId + ", "
				+ "contenidorOrigenId=" + contenidorOrigenId + ", "
				+ "contenidorDestiId=" + contenidorDestiId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		// Comprova la bústia
		BustiaEntity bustia = entityComprovarHelper.comprovarBustia(
				entitat,
				bustiaId,
				true);
		// Comprova el contenidor origen
		ContenidorEntity contenidorOrigen = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorOrigenId,
				null);
		if (!bustia.equals(contenidorOrigen.getPare())) {
			logger.error("El contenidor no és fill de la bústia especificada ("
					+ "bustiaId=" + bustiaId + ", "
					+ "contenidorOrigenId=" + contenidorOrigenId + ")");
			throw new ValidationException(
					contenidorOrigenId,
					ContenidorEntity.class,
					"El contenidor no és fill de la bústia especificada (bustiaId=" + bustiaId + ")");
		}
		// Comprova el contenidor destí
		ContenidorEntity contenidorDesti = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorDestiId,
				null);
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				contenidorDesti);
		// Reb el contingut pendent movent-lo a l'escriptori de l'usuari actual
		ContenidorMovimentEntity contenidorMoviment = contenidorHelper.ferIEnregistrarMovimentContenidor(
				contenidorOrigen,
				contenidorDesti,
				null);
		// Registra al log el processament del contenidor
		contenidorLogHelper.log(
				contenidorOrigen,
				LogTipusEnumDto.PROCESSAMENT,
				null,
				contenidorMoviment,
				true,
				true);
		// Refrescam cache usuaris bústia
		bustiaHelper.evictElementsPendentsBustia(
				entitat,
				bustia);
		return contenidorHelper.toContenidorDto(
				contenidorOrigen,
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
		EscriptoriEntity escriptori = contenidorHelper.getEscriptoriPerUsuari(
				entitat,
				usuariHelper.getUsuariAutenticat());
		return (EscriptoriDto)contenidorHelper.toContenidorDto(
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
	public ContenidorDto getContenidorSenseContingut(
			Long entitatId,
			Long contenidorId) {
		logger.debug("Obtenint contenidor sense contingut ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContenidorEntity contenidor = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorId,
				null);
		contenidorHelper.comprovarPermisosPathContenidor(
				contenidor,
				true,
				false,
				false,
				true);
		return contenidorHelper.toContenidorDto(
				contenidor,
				true,
				false,
				false,
				true,
				true,
				false);
	}

	@Transactional(readOnly = true)
	@Override
	public ContenidorDto getContenidorAmbContingut(
			Long entitatId,
			Long contenidorId) {
		logger.debug("Obtenint contingut del contenidor ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContenidorEntity contenidor = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorId,
				null);
		contenidorHelper.comprovarPermisosPathContenidor(
				contenidor,
				true,
				false,
				false,
				true);
		return contenidorHelper.toContenidorDto(
				contenidor,
				true,
				true,
				true,
				true,
				true,
				false);
	}

	@Transactional(readOnly = true)
	@Override
	public ContenidorDto getContenidorAmbContingutPerPath(
			Long entitatId,
			String path) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Obtenint contingut del contenidor ("
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
		ContenidorEntity contenidorActual = escriptori;
		if (!path.isEmpty() && !path.equals("/")) {
			String[] pathParts;
			if (path.startsWith("/")) {
				pathParts = path.substring(1).split("/");
			} else {
				pathParts = path.split("/");
			}
			for (String pathPart: pathParts) {
				Long idActual = contenidorActual.getId();
				contenidorActual = contenidorRepository.findByPareAndNomAndEsborrat(
						contenidorActual,
						pathPart,
						0);
				if (contenidorActual == null) {
					logger.error("No s'ha trobat el contenidor (pareId=" + idActual + ", nom=" + pathPart + ")");
					throw new NotFoundException(
							"(pareId=" + idActual + ", nom=" + pathPart + ")",
							ContenidorEntity.class);
				}
				// Si el contenidor actual és un document ens aturam
				// perquè el següent element del path serà la darrera
				// versió i no la trobaría com a contenidor.
				if (contenidorActual instanceof DocumentEntity)
					break;
			}
		}
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				contenidorActual);
		// Comprova l'accés al path del contenidor
		contenidorHelper.comprovarPermisosPathContenidor(
				contenidorActual,
				true,
				false,
				false,
				true);
		return contenidorHelper.toContenidorDto(
				contenidorActual,
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
			Long contenidorId) {
		logger.debug("Obtenint errors de validació del contenidor ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidor = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorId,
				null);
		// Comprova l'accés al path del contenidor
		contenidorHelper.comprovarPermisosPathContenidor(
				contenidor,
				true,
				false,
				false,
				true);
		if (contenidor instanceof NodeEntity) {
			NodeEntity node = (NodeEntity)contenidor;
			return cacheHelper.findErrorsValidacioPerNode(node);
		} else {
			logger.error("El contenidor no és cap node (contenidorId=" + contenidorId + ")");
			throw new ValidationException(
					contenidorId,
					ContenidorEntity.class,
					"El contenidor no és un node");
		}
	}

	@Transactional(readOnly = true)
	@Override
	public List<ContenidorLogDto> findLogsPerContenidorAdmin(
			Long entitatId,
			Long contenidorId) {
		logger.debug("Obtenint registre d'accions pel contenidor usuari admin ("
				+ "entitatId=" + entitatId + ", "
				+ "nodeId=" + contenidorId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContenidorEntity contenidor = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorId,
				null);
		return contenidorLogHelper.findLogsContenidor(contenidor);
	}

	@Transactional(readOnly = true)
	@Override
	public List<ContenidorLogDto> findLogsPerContenidorUser(
			Long entitatId,
			Long contenidorId) {
		logger.debug("Obtenint registre d'accions pel contenidor usuari normal ("
				+ "entitatId=" + entitatId + ", "
				+ "nodeId=" + contenidorId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContenidorEntity contenidor = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorId,
				null);
		// Comprova que l'usuari tengui accés al contenidor
		contenidorHelper.comprovarPermisosPathContenidor(
				contenidor,
				false,
				false,
				false,
				true);
		return contenidorLogHelper.findLogsContenidor(contenidor);
	}

	@Transactional(readOnly = true)
	@Override
	public List<ContenidorMovimentDto> findMovimentsPerContenidorAdmin(
			Long entitatId,
			Long contenidorId) {
		logger.debug("Obtenint registre de moviments pel contenidor usuari admin ("
				+ "entitatId=" + entitatId + ", "
				+ "nodeId=" + contenidorId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContenidorEntity contenidor = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorId,
				null);
		return contenidorLogHelper.findMovimentsContenidor(contenidor);
	}

	@Transactional(readOnly = true)
	@Override
	public List<ContenidorMovimentDto> findMovimentsPerContenidorUser(
			Long entitatId,
			Long contenidorId) {
		logger.debug("Obtenint registre de moviments pel contenidor usuari normal ("
				+ "entitatId=" + entitatId + ", "
				+ "nodeId=" + contenidorId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContenidorEntity contenidor = entityComprovarHelper.comprovarContenidor(
				entitat,
				contenidorId,
				null);
		// Comprova que l'usuari tengui accés al contenidor
		contenidorHelper.comprovarPermisosPathContenidor(
				contenidor,
				false,
				false,
				false,
				true);
		return contenidorLogHelper.findMovimentsContenidor(contenidor);
	}

	@Transactional(readOnly = true)
	@Override
	public PaginaDto<ContenidorDto> findAdmin(
			Long entitatId,
			ContenidorFiltreDto filtre,
			PaginacioParamsDto paginacioParams) {
		logger.debug("Condulta de contenidors per usuari admin ("
				+ "entitatId=" + entitatId + ", "
				+ "filtre=" + filtre + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		List<Class<?>> tipusPermesos = new ArrayList<Class<?>>();
		if (filtre.getTipus() == null) {
			tipusPermesos.add(ExpedientEntity.class);
			tipusPermesos.add(DocumentEntity.class);
			tipusPermesos.add(CarpetaEntity.class);
		} else if (filtre.getTipus().equals(ContenidorTipusEnumDto.EXPEDIENT)) {
			tipusPermesos.add(ExpedientEntity.class);
		} else if (filtre.getTipus().equals(ContenidorTipusEnumDto.DOCUMENT)) {
			tipusPermesos.add(DocumentEntity.class);
		} else if (filtre.getTipus().equals(ContenidorTipusEnumDto.CARPETA)) {
			tipusPermesos.add(CarpetaEntity.class);
		}
		return paginacioHelper.toPaginaDto(
				contenidorRepository.findByFiltrePaginat(
						entitat,
						(filtre.getTipus() == null || filtre.getTipus().equals(ContenidorTipusEnumDto.EXPEDIENT)),
						(filtre.getTipus() == null || filtre.getTipus().equals(ContenidorTipusEnumDto.DOCUMENT)),
						(filtre.getTipus() == null || filtre.getTipus().equals(ContenidorTipusEnumDto.CARPETA)),
						(filtre.getNom() == null),
						(filtre.getNom() != null) ? '%' + filtre.getNom() + '%' : null,
						(filtre.getDataCreacioInici() == null),
						filtre.getDataCreacioInici(),
						(filtre.getDataCreacioFi() == null),
						filtre.getDataCreacioFi(),
						filtre.isMostrarEsborrats(),
						filtre.isMostrarNoEsborrats(),
						paginacioHelper.toSpringDataPageable(paginacioParams)),
				ContenidorDto.class,
				new Converter<ContenidorEntity, ContenidorDto>() {
					@Override
					public ContenidorDto convert(ContenidorEntity source) {
						return contenidorHelper.toContenidorDto(
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
	public PaginaDto<ContenidorDto> findEsborrats(
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
				contenidorRepository.findEsborratsByFiltrePaginat(
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
				ContenidorDto.class,
				new Converter<ContenidorEntity, ContenidorDto>() {
					@Override
					public ContenidorDto convert(ContenidorEntity source) {
						return contenidorHelper.toContenidorDto(
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



	private ContenidorEntity copiarContenidor(
			EntitatEntity entitat,
			ContenidorEntity contenidorOrigen,
			ContenidorEntity contenidorDesti,
			boolean recursiu) {
		ContenidorEntity creat = null;
		if (contenidorOrigen instanceof CarpetaEntity) {
			CarpetaEntity carpetaOrigen = (CarpetaEntity)contenidorOrigen;
			CarpetaEntity carpetaNova = CarpetaEntity.getBuilder(
					carpetaOrigen.getNom(),
					carpetaOrigen.getTipus(),
					contenidorDesti,
					entitat).build();
			creat = contenidorRepository.save(carpetaNova);
		} else if (contenidorOrigen instanceof DocumentEntity) {
			DocumentEntity documentOrigen = (DocumentEntity)contenidorOrigen;
			DocumentEntity documentNou = DocumentEntity.getBuilder(
					documentOrigen.getNom(),
					documentOrigen.getData(),
					documentOrigen.getExpedient(),
					documentOrigen.getMetaDocument(),
					contenidorDesti,
					entitat).build();
			creat = contenidorRepository.save(documentNou);
			DocumentVersioEntity documentVersio = documentVersioRepository.findByDocumentAndVersio(
					documentOrigen,
					documentOrigen.getDarreraVersio());
			int versio = 1;
			DocumentVersioEntity documentVersioNova = DocumentVersioEntity.getBuilder(
					documentNou,
					versio,
					documentVersio.getArxiuNom(),
					documentVersio.getArxiuContentType(),
					documentVersio.getArxiuContingut()).build();
			documentVersioRepository.save(documentVersioNova);
			documentNou.updateDarreraVersio(versio);
		} else if (contenidorOrigen instanceof ExpedientEntity) {
			ExpedientEntity expedientOrigen = (ExpedientEntity)contenidorOrigen;
			ExpedientEntity expedientNou = ExpedientEntity.getBuilder(
					expedientOrigen.getNom(),
					expedientOrigen.getMetaExpedient(),
					expedientOrigen.getArxiu(),
					contenidorDesti,
					entitat).build();
			creat = contenidorRepository.save(expedientNou);
		}
		if (creat != null) {
			if (creat instanceof NodeEntity) {
				NodeEntity nodeOrigen = (NodeEntity)contenidorOrigen;
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

	private static final Logger logger = LoggerFactory.getLogger(ContenidorServiceImpl.class);

}
