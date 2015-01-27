/**
 * 
 */
package es.caib.ripea.core.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.CarpetaDto;
import es.caib.ripea.core.api.dto.CarpetaTipusEnumDto;
import es.caib.ripea.core.api.exception.CarpetaNotFoundException;
import es.caib.ripea.core.api.exception.ContenidorNotFoundException;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.ExpedientNotFoundException;
import es.caib.ripea.core.api.exception.NomInvalidException;
import es.caib.ripea.core.api.service.CarpetaService;
import es.caib.ripea.core.entity.CarpetaEntity;
import es.caib.ripea.core.entity.CarpetaTipusEnum;
import es.caib.ripea.core.entity.ContenidorEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.LogTipusEnum;
import es.caib.ripea.core.helper.ContenidorHelper;
import es.caib.ripea.core.helper.ContenidorLogHelper;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.PermisosComprovacioHelper;
import es.caib.ripea.core.helper.PermisosHelper;
import es.caib.ripea.core.repository.CarpetaRepository;
import es.caib.ripea.core.repository.ContenidorRepository;
import es.caib.ripea.core.repository.EntitatRepository;

/**
 * Implementació dels mètodes per a gestionar carpetes.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class CarpetaServiceImpl implements CarpetaService {

	public static final String CARPETA_NOUVINGUTS_NOM = ".nouvinguts";

	@Resource
	private EntitatRepository entitatRepository;
	@Resource
	private ContenidorRepository contenidorRepository;
	@Resource
	private CarpetaRepository carpetaRepository;
	//@Resource
	//private ContenidorLogRepository contenidorLogRepository;

	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private PermisosHelper permisosHelper;
	@Resource
	private ContenidorHelper contenidorHelper;
	@Resource
	private PermisosComprovacioHelper permisosComprovacioHelper;
	@Resource
	private ContenidorLogHelper contenidorLogHelper;



	@Transactional
	@Override
	public CarpetaDto create(
			Long entitatId,
			Long contenidorId,
			String nom,
			CarpetaTipusEnumDto tipus) throws EntitatNotFoundException, ContenidorNotFoundException, NomInvalidException {
		logger.debug("Creant nova carpeta ("
				+ "entitatId=" + entitatId + ", "
				+ "contenidorId=" + contenidorId + ", "
				+ "nom=" + nom + ", "
				+ "tipus=" + tipus + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		if (CarpetaTipusEnumDto.NOUVINGUT.equals(tipus)) {
			logger.error("No es pot crear una carpeta de nouvinguts");
			throw new CarpetaNotFoundException();
		}
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
			throw new NomInvalidException();
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
		}
		CarpetaEntity carpeta = CarpetaEntity.getBuilder(
				nom,
				CarpetaTipusEnum.valueOf(tipus.name()),
				contenidor,
				entitat).build();
		carpeta = carpetaRepository.save(carpeta);
		// Registra al log la creació de la carpeta
		contenidorLogHelper.log(
				carpeta,
				LogTipusEnum.CREACIO,
				null,
				null,
				true,
				true);
		return toCarpetaDto(carpeta);
	}

	@Transactional
	@Override
	public CarpetaDto update(
			Long entitatId,
			Long id,
			String nom,
			CarpetaTipusEnumDto tipus) throws EntitatNotFoundException, CarpetaNotFoundException, NomInvalidException {
		logger.debug("Actualitzant dades de la carpeta ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "nom=" + nom + ", "
				+ "tipus=" + tipus + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		CarpetaEntity carpeta = comprovarCarpeta(
				entitat,
				id);
		if (CarpetaTipusEnum.NOUVINGUT.equals(carpeta.getTipus())) {
			logger.error("No es pot modificar la carpeta de nouvinguts (id=" + id + ")");
			throw new CarpetaNotFoundException();
		}
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				carpeta);
		// Comprova l'accés al path de la carpeta
		contenidorHelper.comprovarPermisosPathContenidor(
				carpeta,
				true,
				false,
				false,
				true);
		// Comprova que el nom sigui vàlid
		if (!contenidorHelper.isNomValid(nom)) {
			throw new NomInvalidException();
		}
		// Comprova el permís de modificació de l'expedient superior
		ExpedientEntity expedientSuperior = contenidorHelper.getExpedientSuperior(
				carpeta,
				true);
		if (expedientSuperior != null) {
			contenidorHelper.comprovarPermisosContenidor(
					expedientSuperior,
					false,
					true,
					false);
		}
		carpeta.update(
				nom,
				CarpetaTipusEnum.valueOf(tipus.name()));
		// Registra al log la modificació de la carpeta
		contenidorLogHelper.log(
				carpeta,
				LogTipusEnum.MODIFICACIO,
				null,
				null,
				false,
				false);
		return toCarpetaDto(carpeta);
	}

	@Transactional
	@Override
	public CarpetaDto delete(
			Long entitatId,
			Long id) throws EntitatNotFoundException, ExpedientNotFoundException {
		logger.debug("Esborrant la carpeta ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		CarpetaEntity carpeta = comprovarCarpeta(
				entitat,
				id);
		if (CarpetaTipusEnum.NOUVINGUT.equals(carpeta.getTipus())) {
			logger.error("No es pot esborrar la carpeta de nouvinguts (id=" + id + ")");
			throw new CarpetaNotFoundException();
		}
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contenidorHelper.comprovarContenidorArrelEsEscriptoriUsuariActual(
				entitat,
				carpeta);
		// Comprova l'accés al path de la carpeta
		contenidorHelper.comprovarPermisosPathContenidor(
				carpeta,
				true,
				false,
				false,
				true);
		// Comprova el permís de modificació de l'expedient superior
		ExpedientEntity expedientSuperior = contenidorHelper.getExpedientSuperior(
				carpeta,
				false);
		if (expedientSuperior != null) {
			contenidorHelper.comprovarPermisosContenidor(
					expedientSuperior,
					false,
					true,
					false);
		}
		carpetaRepository.delete(carpeta);
		// Registra al log l'eliminació de la carpeta
		contenidorLogHelper.log(
				carpeta,
				LogTipusEnum.ELIMINACIO,
				null,
				null,
				true,
				true);
		return toCarpetaDto(carpeta);
	}

	@Transactional(readOnly = true)
	@Override
	public CarpetaDto findById(
			Long entitatId,
			Long id) throws EntitatNotFoundException, CarpetaNotFoundException {
		logger.debug("Obtenint la carpeta ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		CarpetaEntity carpeta = comprovarCarpeta(
				entitat,
				id);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path de la carpeta
		contenidorHelper.comprovarPermisosPathContenidor(
				carpeta,
				true,
				false,
				false,
				true);
		return toCarpetaDto(carpeta);
	}



	private CarpetaDto toCarpetaDto(
			CarpetaEntity carpeta) {
		return (CarpetaDto)contenidorHelper.toContenidorDto(
				carpeta,
				false,
				false,
				false,
				false,
				false,
				false);
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

	private CarpetaEntity comprovarCarpeta(
			EntitatEntity entitat,
			Long id) throws CarpetaNotFoundException {
		CarpetaEntity carpeta = carpetaRepository.findOne(id);
		if (carpeta == null) {
			logger.error("No s'ha trobat la carpeta (id=" + id + ")");
			throw new CarpetaNotFoundException();
		}
		if (!entitat.equals(carpeta.getEntitat())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat de la carpeta ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + carpeta.getEntitat().getId() + ")");
			throw new CarpetaNotFoundException();
		}
		return carpeta;
	}

	private static final Logger logger = LoggerFactory.getLogger(CarpetaServiceImpl.class);

}
