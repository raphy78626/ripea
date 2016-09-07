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
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.service.CarpetaService;
import es.caib.ripea.core.entity.CarpetaEntity;
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.helper.ContingutHelper;
import es.caib.ripea.core.helper.ContingutLogHelper;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.EntityComprovarHelper;
import es.caib.ripea.core.helper.PermisosHelper;
import es.caib.ripea.core.repository.CarpetaRepository;
import es.caib.ripea.core.repository.EntitatRepository;

/**
 * Implementació dels mètodes per a gestionar carpetes.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class CarpetaServiceImpl implements CarpetaService {

	@Resource
	private EntitatRepository entitatRepository;
	@Resource
	private CarpetaRepository carpetaRepository;

	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private PermisosHelper permisosHelper;
	@Resource
	private ContingutHelper contingutHelper;
	@Resource
	private EntityComprovarHelper entityComprovarHelper;
	@Resource
	private ContingutLogHelper contenidorLogHelper;



	@Transactional
	@Override
	public CarpetaDto create(
			Long entitatId,
			Long contingutId,
			String nom,
			CarpetaTipusEnumDto tipus) {
		logger.debug("Creant nova carpeta ("
				+ "entitatId=" + entitatId + ", "
				+ "contingutId=" + contingutId + ", "
				+ "nom=" + nom + ", "
				+ "tipus=" + tipus + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContingutEntity contingut = entityComprovarHelper.comprovarContingut(
				entitat,
				contingutId,
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
					CarpetaEntity.class,
					"El nom de la carpeta no és vàlid (no pot començar amb \".\")");
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
		}
		CarpetaEntity carpeta = CarpetaEntity.getBuilder(
				nom,
				tipus,
				contingut,
				entitat).build();
		carpeta = carpetaRepository.save(carpeta);
		System.out.println(">>> Creacio CARPETA " + carpeta.getId() + " al pare " + carpeta.getPare().getId());
		// Registra al log la creació de la carpeta
		contenidorLogHelper.log(
				carpeta,
				LogTipusEnumDto.CREACIO,
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
			CarpetaTipusEnumDto tipus) {
		logger.debug("Actualitzant dades de la carpeta ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "nom=" + nom + ", "
				+ "tipus=" + tipus + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		CarpetaEntity carpeta = entityComprovarHelper.comprovarCarpeta(
				entitat,
				id);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				carpeta);
		// Comprova l'accés al path de la carpeta
		contingutHelper.comprovarPermisosPathContingut(
				carpeta,
				true,
				false,
				false,
				true);
		// Comprova que el nom sigui vàlid
		if (!contingutHelper.isNomValid(nom)) {
			throw new ValidationException(
					id,
					CarpetaEntity.class,
					"El nom de la carpeta no és vàlid (no pot començar amb \".\")");
		}
		// Comprova el permís de modificació de l'expedient superior
		ExpedientEntity expedientSuperior = contingutHelper.getExpedientSuperior(
				carpeta,
				true);
		if (expedientSuperior != null) {
			contingutHelper.comprovarPermisosContingut(
					expedientSuperior,
					false,
					true,
					false);
		}
		carpeta.update(
				nom,
				tipus);
		// Registra al log la modificació de la carpeta
		contenidorLogHelper.log(
				carpeta,
				LogTipusEnumDto.MODIFICACIO,
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
			Long id) {
		logger.debug("Esborrant la carpeta ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		CarpetaEntity carpeta = entityComprovarHelper.comprovarCarpeta(
				entitat,
				id);
		// Comprova que el contenidor arrel és l'escriptori de l'usuari actual
		contingutHelper.comprovarContingutArrelEsEscriptoriUsuariActual(
				entitat,
				carpeta);
		// Comprova l'accés al path de la carpeta
		contingutHelper.comprovarPermisosPathContingut(
				carpeta,
				true,
				false,
				false,
				true);
		// Comprova el permís de modificació de l'expedient superior
		ExpedientEntity expedientSuperior = contingutHelper.getExpedientSuperior(
				carpeta,
				false);
		if (expedientSuperior != null) {
			contingutHelper.comprovarPermisosContingut(
					expedientSuperior,
					false,
					true,
					false);
		}
		carpetaRepository.delete(carpeta);
		// Registra al log l'eliminació de la carpeta
		contenidorLogHelper.log(
				carpeta,
				LogTipusEnumDto.ELIMINACIO,
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
			Long id) {
		logger.debug("Obtenint la carpeta ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		CarpetaEntity carpeta = entityComprovarHelper.comprovarCarpeta(
				entitat,
				id);
		// Per a consultes no es comprova el contenidor arrel
		// Comprova l'accés al path de la carpeta
		contingutHelper.comprovarPermisosPathContingut(
				carpeta,
				true,
				false,
				false,
				true);
		return toCarpetaDto(carpeta);
	}



	private CarpetaDto toCarpetaDto(
			CarpetaEntity carpeta) {
		return (CarpetaDto)contingutHelper.toContingutDto(
				carpeta,
				false,
				false,
				false,
				false,
				false,
				false);
	}

	private static final Logger logger = LoggerFactory.getLogger(CarpetaServiceImpl.class);

}
