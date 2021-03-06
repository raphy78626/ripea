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
import org.springframework.security.acls.model.Permission;
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
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.dto.ValidacioErrorDto;
import es.caib.ripea.core.api.exception.BustiaNotFoundException;
import es.caib.ripea.core.api.exception.ContenidorNomDuplicatException;
import es.caib.ripea.core.api.exception.ContenidorNotFoundException;
import es.caib.ripea.core.api.exception.DadaNotFoundException;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.MetaDadaNotFoundException;
import es.caib.ripea.core.api.exception.MultiplicitatException;
import es.caib.ripea.core.api.exception.UsuariNotFoundException;
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
import es.caib.ripea.core.entity.LogObjecteTipusEnum;
import es.caib.ripea.core.entity.LogTipusEnum;
import es.caib.ripea.core.entity.MetaDadaEntity;
import es.caib.ripea.core.entity.MetaDadaTipusEnum;
import es.caib.ripea.core.entity.MetaNodeEntity;
import es.caib.ripea.core.entity.MetaNodeMetaDadaEntity;
import es.caib.ripea.core.entity.MultiplicitatEnum;
import es.caib.ripea.core.entity.NodeEntity;
import es.caib.ripea.core.entity.UsuariEntity;
import es.caib.ripea.core.helper.BustiaHelper;
import es.caib.ripea.core.helper.CacheHelper;
import es.caib.ripea.core.helper.ContenidorHelper;
import es.caib.ripea.core.helper.ContenidorLogHelper;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.EmailHelper;
import es.caib.ripea.core.helper.MetaNodeHelper;
import es.caib.ripea.core.helper.PaginacioHelper;
import es.caib.ripea.core.helper.PaginacioHelper.Converter;
import es.caib.ripea.core.helper.PermisosComprovacioHelper;
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
import es.caib.ripea.core.security.ExtendedPermission;

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
	private PermisosComprovacioHelper permisosComprovacioHelper;



	@Transactional
	@Override
	public ContenidorDto rename(
			Long entitatId,
			Long contenidorId,
			String nom) throws EntitatNotFoundException, ContenidorNotFoundException {
		logger.debug("Renombrant contenidor ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ", "
				+ "nom=" + nom + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidor = comprovarContenidor(
				entitat,
				contenidorId);
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
		// Canvia el nom del contenidor
		String nomAnterior = contenidor.getNom();
		contenidor.update(nom);
		// Registra al log la modificació del contenidor
		contenidorLogHelper.log(
				contenidor,
				LogTipusEnum.MODIFICACIO,
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
			Object valor) throws EntitatNotFoundException, ContenidorNotFoundException, MetaDadaNotFoundException, MultiplicitatException {
		logger.debug("Creant nova dada ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ", "
				+ "metaDadaId=" + metaDadaId + ", "
				+ "valor=" + valor + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidor = comprovarContenidor(
				entitat,
				contenidorId);
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
			MetaDadaEntity metaDada = comprovarMetaDada(
					entitat,
					metaDadaId);
			List<DadaEntity> dades = dadaRepository.findByNodeAndMetaDada(node, metaDada);
			boolean global = 
					(node instanceof ExpedientEntity && metaDada.isGlobalExpedient()) ||
					(node instanceof DocumentEntity && metaDada.isGlobalDocument());
			if (!global) {
				MetaNodeMetaDadaEntity metaNodeMetaDada = comprovarMetaNodeMetaDada(
						entitat,
						node.getMetaNode(),
						metaDada);
				// Comprova que no s'afegeixin més dades de les permeses
				if (dades.size() > 0 && (metaNodeMetaDada.getMultiplicitat().equals(MultiplicitatEnum.M_0_1) || metaNodeMetaDada.getMultiplicitat().equals(MultiplicitatEnum.M_1))) {
					throw new MultiplicitatException();
				}
			} else {
				if (dades.size() > 0 && (metaDada.getGlobalMultiplicitat().equals(MultiplicitatEnum.M_0_1) || metaDada.getGlobalMultiplicitat().equals(MultiplicitatEnum.M_1))) {
					throw new MultiplicitatException();
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
					LogTipusEnum.MODIFICACIO,
					null,
					null,
					dada,
					LogObjecteTipusEnum.DADA,
					LogTipusEnum.CREACIO,
					metaDada.getCodi(),
					dada.getValor(),
					true,
					true);
			return conversioTipusHelper.convertir(
					dada,
					DadaDto.class);
		} else {
			logger.error("El contenidor no és un node (contenidorId=" + contenidorId + ")");
			throw new ContenidorNotFoundException();
		}
	}

	@Transactional
	@Override
	public DadaDto dadaUpdate(
			Long entitatId,
			Long contenidorId,
			Long dadaId,
			Object valor) throws EntitatNotFoundException, ContenidorNotFoundException, DadaNotFoundException {
		logger.debug("Modificant dada ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ", "
				+ "dadaId=" + dadaId + ", "
				+ "valor=" + valor + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidor = comprovarContenidor(
				entitat,
				contenidorId);
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
			DadaEntity dada = comprovarDada(
					node,
					dadaId);
			dada.update(
					getDadaValorPerGuardar(
							dada.getMetaDada(),
							valor));
			// Registra al log la modificació de la dada
			contenidorLogHelper.log(
					contenidor,
					LogTipusEnum.MODIFICACIO,
					null,
					null,
					dada,
					LogObjecteTipusEnum.DADA,
					LogTipusEnum.MODIFICACIO,
					dada.getMetaDada().getCodi(),
					dada.getValor(),
					true,
					true);
			return conversioTipusHelper.convertir(
					dada,
					DadaDto.class);
		} else {
			logger.error("El contenidor no és un node (contenidorId=" + contenidorId + ")");
			throw new ContenidorNotFoundException();
		}
	}

	@Transactional
	@Override
	@CacheEvict(value = "errorsValidacioNode", key = "#contenidorId")
	public DadaDto dadaDelete(
			Long entitatId,
			Long contenidorId,
			Long dadaId) throws EntitatNotFoundException, ContenidorNotFoundException, DadaNotFoundException {
		logger.debug("Esborrant la dada ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ", "
				+ "dadaId=" + dadaId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidor = comprovarContenidor(
				entitat,
				contenidorId);
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
			DadaEntity dada = comprovarDada(
					node,
					dadaId);
			dadaRepository.delete(dada);
			// Refresca la validesa del node
			cacheHelper.evictErrorsValidacioPerNode(node);
			// Registra al log la eliminació de la dada
			contenidorLogHelper.log(
					contenidor,
					LogTipusEnum.MODIFICACIO,
					null,
					null,
					dada,
					LogObjecteTipusEnum.DADA,
					LogTipusEnum.ELIMINACIO,
					dada.getMetaDada().getCodi(),
					null,
					true,
					true);
			return conversioTipusHelper.convertir(
					dada,
					DadaDto.class);
		} else {
			logger.error("El contenidor no és un node (contenidorId=" + contenidorId + ")");
			throw new ContenidorNotFoundException();
		}
	}

	@Transactional(readOnly = true)
	@Override
	public DadaDto dadaFindById(
			Long entitatId,
			Long contenidorId,
			Long dadaId) throws EntitatNotFoundException, ContenidorNotFoundException {
		logger.debug("Obtenint la dada ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ", "
				+ "dadaId=" + dadaId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidor = comprovarContenidor(
				entitat,
				contenidorId);
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
			DadaEntity dada = comprovarDada(
					node,
					dadaId);
			return conversioTipusHelper.convertir(
					dada,
					DadaDto.class);
		} else {
			logger.error("El contenidor no és un node (contenidorId=" + contenidorId + ")");
			throw new ContenidorNotFoundException();
		}
	}

	@Transactional
	@Override
	public ContenidorDto deleteReversible(
			Long entitatId,
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException {
		logger.debug("Esborrant el contenidor ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidor = comprovarContenidor(
				entitat,
				contenidorId);
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
			throw new ContenidorNotFoundException();
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
				LogTipusEnum.ELIMINACIO,
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
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException {
		logger.debug("Esborrant el contenidor ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ContenidorEntity contenidor = comprovarContenidor(
				entitat,
				contenidorId);
		// Comprova que el contenidor estigui esborrat
		if (contenidor.getEsborrat() == 0) {
			logger.error("Aquest contenidor no està esborrat (contenidorId=" + contenidorId + ")");
			throw new ContenidorNotFoundException();
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
				LogTipusEnum.ELIMINACIODEF,
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
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException, ContenidorNomDuplicatException {
		logger.debug("Recuperant el contenidor ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ContenidorEntity contenidor = comprovarContenidor(
				entitat,
				contenidorId);
		// No es comproven permisos perquè això només ho pot fer l'administrador
		if (contenidor.getEsborrat() == 0) {
			logger.error("Aquest contenidor no està esborrat (contenidorId=" + contenidorId + ")");
			throw new ContenidorNotFoundException();
		}
		if (contenidor.getPare() == null) {
			logger.error("Aquest contenidor no te pare (contenidorId=" + contenidorId + ")");
			throw new ContenidorNotFoundException();
		}
		boolean nomDuplicat = contenidorRepository.findByPareAndNomAndEsborrat(
				contenidor.getPare(),
				contenidor.getNom(),
				0) != null;
		if (nomDuplicat) {
			logger.error("Ja existeix un altre contenidor amb el mateix nom dins el mateix pare (contenidorId=" + contenidorId + ")");
			throw new ContenidorNomDuplicatException();
		}
		contenidor.updateEsborrat(0);
		// Registra al log la recuperació del contenidor
		contenidorLogHelper.log(
				contenidor,
				LogTipusEnum.RECUPERACIO,
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
			Long contenidorDestiId) throws EntitatNotFoundException, ContenidorNotFoundException, ContenidorNomDuplicatException {
		logger.debug("Movent el contenidor ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorOrigenId=" + contenidorOrigenId + ", "
				+ "contenidorDestiId=" + contenidorDestiId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidorOrigen = comprovarContenidor(
				entitat,
				contenidorOrigenId);
		if (contenidorOrigen instanceof ExpedientEntity) {
			logger.error("Els expedients no es poden moure ("
					+ "entitatId=" + entitatId + ", "
					+ "expedientId=" + contenidorOrigenId + ")");
			throw new ContenidorNotFoundException();
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
		ContenidorEntity contenidorDesti = comprovarContenidor(
				entitat,
				contenidorDestiId);
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
			logger.error("Ja existeix un altre contenidor amb el mateix nom dins el contenidor destí ("
					+ "contenidorOrigenId=" + contenidorOrigenId + ", "
					+ "contenidorDestiId=" + contenidorDestiId + ")");
			throw new ContenidorNomDuplicatException();
		}
		ContenidorMovimentEntity contenidorMoviment = contenidorHelper.ferIEnregistrarMovimentContenidor(
				contenidorOrigen,
				contenidorDesti,
				null);
		// Registra al log el moviment del node
		contenidorLogHelper.log(
				contenidorOrigen,
				LogTipusEnum.MOVIMENT,
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
			boolean recursiu) throws EntitatNotFoundException, ContenidorNotFoundException, ContenidorNomDuplicatException {
		logger.debug("Copiant el contenidor ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorOrigenId=" + contenidorOrigenId + ", "
				+ "contenidorDestiId=" + contenidorDestiId + ", "
				+ "recursiu=" + recursiu + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidorOrigen = comprovarContenidor(
				entitat,
				contenidorOrigenId);
		if (contenidorOrigen instanceof ExpedientEntity) {
			logger.error("Els expedients no es poden copiar ("
					+ "entitatId=" + entitatId + ", "
					+ "expedientId=" + contenidorOrigenId + ")");
			throw new ContenidorNotFoundException();
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
		ContenidorEntity contenidorDesti = comprovarContenidor(
				entitat,
				contenidorDestiId);
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
			logger.error("Ja existeix un altre contenidor amb el mateix nom dins el contenidor destí ("
					+ "contenidorOrigenId=" + contenidorOrigenId + ", "
					+ "contenidorDestiId=" + contenidorDestiId + ")");
			throw new ContenidorNomDuplicatException();
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
				LogTipusEnum.COPIA,
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
			String comentari) throws EntitatNotFoundException, ContenidorNotFoundException, BustiaNotFoundException {
		logger.debug("Enviant contenidor a bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ", "
				+ "bustiaId=" + bustiaId + ","
				+ "comentari=" + comentari + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidor = comprovarContenidor(
				entitat,
				contenidorId);
		// Comprova que el contenidorOrigen arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				contenidor);
		// Comprova que aquest contenidor no pertanyi a cap expedient
		ExpedientEntity expedientActual = contenidorHelper.getExpedientSuperior(
				contenidor,
				true);
		if (expedientActual != null) {
			logger.error("No es pot enviar un expedient o un contenidor que pertany a un expedient ("
					+ "entitatId=" + entitatId + ", "
					+ "contenidorId=" + contenidorId + ", "
					+ "bustiaId=" + bustiaId + ")");
			throw new ContenidorNotFoundException();
		}
		// Comprova l'accés al path del contenidorOrigen
		contenidorHelper.comprovarPermisosPathContenidor(
				contenidor,
				true,
				false,
				false,
				true);
		// Comprova la bústia
		BustiaEntity bustia = comprovarBustia(
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
				LogTipusEnum.ENVIAMENT,
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
			Long contenidorDestiId) throws EntitatNotFoundException, BustiaNotFoundException, ContenidorNotFoundException {
		logger.debug("Processant contenidor pendent ("
				+ "entitatId=" + entitatId + ", "
				+ "bustiaId=" + bustiaId + ", "
				+ "contenidorOrigenId=" + contenidorOrigenId + ", "
				+ "contenidorDestiId=" + contenidorDestiId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		// Comprova la bústia
		BustiaEntity bustia = comprovarBustia(
				entitat,
				bustiaId,
				true);
		// Comprova el contenidor origen
		ContenidorEntity contenidorOrigen = comprovarContenidor(
				entitat,
				contenidorOrigenId);
		if (!bustia.equals(contenidorOrigen.getPare())) {
			logger.error("El contenidor no és fill de la bústia especificada ("
					+ "bustiaId=" + bustiaId + ", "
					+ "contenidorOrigenId=" + contenidorOrigenId + ")");
			throw new ContenidorNotFoundException();
		}
		// Comprova el contenidor destí
		ContenidorEntity contenidorDesti = comprovarContenidor(
				entitat,
				contenidorDestiId);
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
				LogTipusEnum.PROCESSAMENT,
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
			Long entitatId) throws EntitatNotFoundException, UsuariNotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Obtenint escriptori ("
				+ "entitatId=" + entitatId + ", "
				+ "usuariCodi=" + auth.getName() + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
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
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException {
		logger.debug("Obtenint contenidor sense contingut ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContenidorEntity contenidor = comprovarContenidor(
				entitat,
				contenidorId);
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
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException {
		logger.debug("Obtenint contingut del contenidor ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContenidorEntity contenidor = comprovarContenidor(
				entitat,
				contenidorId);
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
			String path) throws EntitatNotFoundException, ContenidorNotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Obtenint contingut del contenidor ("
				+ "entitatId=" + entitatId + ", "
				+ "path=" + path + ", "
				+ "usuariCodi=" + auth.getName() + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
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
					throw new ContenidorNotFoundException();
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
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException {
		logger.debug("Obtenint errors de validació del contenidor ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidor = comprovarContenidor(
				entitat,
				contenidorId);
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
			throw new ContenidorNotFoundException();
		}
	}

	@Transactional(readOnly = true)
	@Override
	public List<ContenidorLogDto> findLogsPerContenidorAdmin(
			Long entitatId,
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException {
		logger.debug("Obtenint registre d'accions pel contenidor usuari admin ("
				+ "entitatId=" + entitatId + ", "
				+ "nodeId=" + contenidorId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContenidorEntity contenidor = comprovarContenidor(
				entitat,
				contenidorId);
		return contenidorLogHelper.findLogsContenidor(contenidor);
	}

	@Transactional(readOnly = true)
	@Override
	public List<ContenidorLogDto> findLogsPerContenidorUser(
			Long entitatId,
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException {
		logger.debug("Obtenint registre d'accions pel contenidor usuari normal ("
				+ "entitatId=" + entitatId + ", "
				+ "nodeId=" + contenidorId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContenidorEntity contenidor = comprovarContenidor(
				entitat,
				contenidorId);
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
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException {
		logger.debug("Obtenint registre de moviments pel contenidor usuari admin ("
				+ "entitatId=" + entitatId + ", "
				+ "nodeId=" + contenidorId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContenidorEntity contenidor = comprovarContenidor(
				entitat,
				contenidorId);
		return contenidorLogHelper.findMovimentsContenidor(contenidor);
	}

	@Transactional(readOnly = true)
	@Override
	public List<ContenidorMovimentDto> findMovimentsPerContenidorUser(
			Long entitatId,
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException {
		logger.debug("Obtenint registre de moviments pel contenidor usuari normal ("
				+ "entitatId=" + entitatId + ", "
				+ "nodeId=" + contenidorId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ContenidorEntity contenidor = comprovarContenidor(
				entitat,
				contenidorId);
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
			PaginacioParamsDto paginacioParams) throws EntitatNotFoundException {
		logger.debug("Condulta de contenidors per usuari admin ("
				+ "entitatId=" + entitatId + ", "
				+ "filtre=" + filtre + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
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
			PaginacioParamsDto paginacioParams) throws EntitatNotFoundException, UsuariNotFoundException {
		logger.debug("Obtenint elements esborrats ("
				+ "entitatId=" + entitatId + ", "
				+ "nom=" + nom + ", "
				+ "usuariCodi=" + usuariCodi + ", "
				+ "dataInici=" + dataInici + ", "
				+ "dataFi=" + dataFi + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		UsuariEntity usuari = null;
		if (usuariCodi != null && !usuariCodi.isEmpty()) {
			usuari = usuariRepository.findOne(usuariCodi);
			if (usuari == null) {
				logger.error("No s'ha trobat l'usuari (codi=" + usuariCodi + ")");
				throw new UsuariNotFoundException();
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



	private ContenidorEntity comprovarContenidor(
			EntitatEntity entitat,
			Long id) throws EntitatNotFoundException {
		ContenidorEntity contenidor = contenidorRepository.findOne(id);
		if (contenidor == null) {
			logger.error("No s'ha trobat el contenidor (contenidorId=" + id + ")");
			throw new ContenidorNotFoundException();
		}
		if (!contenidor.getEntitat().equals(entitat)) {
			logger.error("L'entitat del contenidor no coincideix ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + contenidor.getEntitat().getId() + ")");
			throw new ContenidorNotFoundException();
		}
		return contenidor;
	}

	private MetaDadaEntity comprovarMetaDada(
			EntitatEntity entitat,
			Long metaDadaId) {
		MetaDadaEntity metaDada = metaDadaRepository.findOne(metaDadaId);
		if (metaDada == null) {
			logger.error("No s'ha trobat la meta-dada (metaDadaId=" + metaDadaId + ")");
			throw new MetaDadaNotFoundException();
		}
		if (!entitat.equals(metaDada.getEntitat())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat de la meta-dada ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + metaDada.getEntitat().getId() + ")");
			throw new MetaDadaNotFoundException();
		}
		return metaDada;
	}

	private MetaNodeMetaDadaEntity comprovarMetaNodeMetaDada(
			EntitatEntity entitat,
			MetaNodeEntity metaNode,
			MetaDadaEntity metaDada) {
		MetaNodeMetaDadaEntity metaNodeMetaDada = metaNodeMetaDadaRepository.findByMetaNodeIdAndMetaDada(
				metaNode.getId(),
				metaDada);
		if (metaNodeMetaDada == null) {
			logger.error("No s'ha trobat la meta-dada pel meta-node ("
					+ "metaNodeId=" + metaNode.getId() + ","
					+ "metaDadaId=" + metaDada.getId() + ")");
			throw new MetaDadaNotFoundException();
		}
		return metaNodeMetaDada;
	}

	private DadaEntity comprovarDada(
			NodeEntity node,
			Long dadaId) {
		DadaEntity dada = dadaRepository.findOne(dadaId);
		if (dada == null) {
			logger.error("No s'ha trobat la dada (dadaId=" + dadaId + ")");
			throw new DadaNotFoundException();
		}
		if (!dada.getNode().equals(node)) {
			logger.error("Els nodes no coincideixen("
					+ "nodeId1=" + node.getId() + ","
					+ "nodeId2=" + dada.getNode().getId() + ")");
			throw new DadaNotFoundException();
		}
		return dada;
	}
	
	private BustiaEntity comprovarBustia(
			EntitatEntity entitat,
			Long bustiaId,
			boolean comprovarPermisRead) {
		BustiaEntity bustia = bustiaRepository.findOne(bustiaId);
		if (bustia == null) {
			logger.error("No s'ha trobat la bustia (bustiaId=" + bustiaId + ")");
			throw new BustiaNotFoundException();
		}
		if (!bustia.getEntitat().equals(entitat)) {
			logger.error("L'entitat especificada no coincideix amb l'entitat de la bústia ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + bustia.getEntitat().getId() + ")");
			throw new BustiaNotFoundException();
		}
		if (comprovarPermisRead) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			boolean esAdministradorEntitat = permisosHelper.isGrantedAll(
					bustiaId,
					BustiaEntity.class,
					new Permission[] {ExtendedPermission.READ},
					auth);
			if (!esAdministradorEntitat) {
				logger.error("Aquest usuari no té permisos d'accés a la bústia ("
						+ "id=" + bustiaId + ", "
						+ "usuari=" + auth.getName() + ")");
				throw new SecurityException("Sense permisos d'accés sobre aquesta bústia");
			}
		}
		return bustia;
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
		if (metaDada.getTipus().equals(MetaDadaTipusEnum.TEXT)) {
			if (valor instanceof String) {
				return (String)valor;
			} else {
				throw new RuntimeException();
			}
		} else if (metaDada.getTipus().equals(MetaDadaTipusEnum.DATA)) {
			if (valor instanceof Date) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				return sdf.format((Date)valor);
			} else {
				throw new RuntimeException();
			}
		} else if (metaDada.getTipus().equals(MetaDadaTipusEnum.SENCER)) {
			if (valor instanceof Long) {
				return ((Long)valor).toString();
			} else {
				throw new RuntimeException();
			}
		} else if (metaDada.getTipus().equals(MetaDadaTipusEnum.FLOTANT)) {
			if (valor instanceof Double) {
				return ((Double)valor).toString();
			} else {
				throw new RuntimeException();
			}
		} else if (metaDada.getTipus().equals(MetaDadaTipusEnum.IMPORT)) {
			if (valor instanceof BigDecimal) {
				return ((BigDecimal)valor).toString();
			} else {
				throw new RuntimeException();
			}
		} else if (metaDada.getTipus().equals(MetaDadaTipusEnum.BOOLEA)) {
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
