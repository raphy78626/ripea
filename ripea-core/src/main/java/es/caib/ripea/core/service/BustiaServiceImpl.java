/**
 * 
 */
package es.caib.ripea.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.ArbreDto;
import es.caib.ripea.core.api.dto.ArbreNodeDto;
import es.caib.ripea.core.api.dto.BustiaDto;
import es.caib.ripea.core.api.dto.ContenidorDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.dto.RegistreAnotacioDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.exception.BustiaNotFoundException;
import es.caib.ripea.core.api.exception.ContenidorNotFoundException;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.RegistreNotFoundException;
import es.caib.ripea.core.api.exception.UnitatNotFoundException;
import es.caib.ripea.core.api.service.BustiaService;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.ContenidorEntity;
import es.caib.ripea.core.entity.ContenidorMovimentEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.LogTipusEnum;
import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.entity.RegistreMovimentEntity;
import es.caib.ripea.core.helper.BustiaHelper;
import es.caib.ripea.core.helper.CacheHelper;
import es.caib.ripea.core.helper.ContenidorHelper;
import es.caib.ripea.core.helper.ContenidorLogHelper;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.EmailHelper;
import es.caib.ripea.core.helper.PermisosComprovacioHelper;
import es.caib.ripea.core.helper.PermisosHelper;
import es.caib.ripea.core.helper.PermisosHelper.ObjectIdentifierExtractor;
import es.caib.ripea.core.helper.UnitatOrganitzativaHelper;
import es.caib.ripea.core.helper.UsuariHelper;
import es.caib.ripea.core.repository.BustiaRepository;
import es.caib.ripea.core.repository.ContenidorRepository;
import es.caib.ripea.core.repository.EntitatRepository;
import es.caib.ripea.core.repository.RegistreMovimentRepository;
import es.caib.ripea.core.repository.RegistreRepository;
import es.caib.ripea.core.repository.UsuariRepository;
import es.caib.ripea.core.security.ExtendedPermission;

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
	private ContenidorRepository contenidorRepository;
	@Resource
	private RegistreRepository registreRepository;
	@Resource
	private UsuariRepository usuariRepository;
	@Resource
	private RegistreMovimentRepository registreMovimentRepository;

	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private PermisosHelper permisosHelper;
	@Resource
	private PermisosComprovacioHelper permisosComprovacioHelper;
	@Resource
	private ContenidorHelper contenidorHelper;
	@Resource
	private ContenidorLogHelper contenidorLogHelper;
	@Resource
	private CacheHelper cacheHelper;
	@Resource
	private EmailHelper emailHelper;
	@Resource
	private BustiaHelper bustiaHelper;
	@Resource
	private UsuariHelper usuariHelper;
	@Resource
	private UnitatOrganitzativaHelper unitatOrganitzativaHelper;



	@Override
	@Transactional
	public BustiaDto create(
			Long entitatId,
			BustiaDto bustia) throws EntitatNotFoundException, UnitatNotFoundException {
		logger.debug("Creant una nova bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "bustia=" + bustia + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		UnitatOrganitzativaDto unitat = unitatOrganitzativaHelper.findPerEntitatAndCodi(
				entitat.getCodi(),
				bustia.getUnitatCodi());
		if (unitat == null) {
			logger.error("No s'ha trobat l'unitat administrativa (codi=" + bustia.getUnitatCodi() + ")");
			throw new UnitatNotFoundException();
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
			BustiaDto bustia) throws EntitatNotFoundException, BustiaNotFoundException {
		logger.debug("Modificant la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "bustia=" + bustia + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		BustiaEntity entity = comprovarBustia(
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
			boolean activa) throws EntitatNotFoundException, BustiaNotFoundException {
		logger.debug("Modificant propietat activa de la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "activa=" + activa + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		BustiaEntity entity = comprovarBustia(
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
			Long id) throws EntitatNotFoundException, BustiaNotFoundException {
		logger.debug("Esborrant la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		BustiaEntity bustia = comprovarBustia(
				entitat,
				id,
				false);
		if (bustia.isPerDefecte()) {
			logger.error("No es pot esborrar la bústia per defecte (bustiaId=" + id + ")");
			throw new BustiaNotFoundException();
		}
		bustiaRepository.delete(bustia);
		return toBustiaDto(bustia);
	}

	@Override
	@Transactional
	public BustiaDto marcarPerDefecte(
			Long entitatId,
			Long id) throws EntitatNotFoundException, BustiaNotFoundException {
		logger.debug("Marcant la bústia com a bústia per defecte("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		BustiaEntity bustia = comprovarBustia(
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
			Long id) throws EntitatNotFoundException {
		logger.debug("Cercant la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		BustiaEntity bustia = comprovarBustia(
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
	public List<BustiaDto> findByUnitatCodiAdmin(
			Long entitatId,
			String unitatCodi) throws EntitatNotFoundException, UnitatNotFoundException {
		logger.debug("Cercant les bústies de la unitat per admins ("
				+ "entitatId=" + entitatId + ", "
				+ "unitatCodi=" + unitatCodi + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		List<BustiaEntity> busties = bustiaRepository.findByEntitatAndUnitatCodiAndPareNotNull(entitat, unitatCodi);
		List<BustiaDto> resposta = toBustiaDto(busties);
		if (!busties.isEmpty()) {
			// Ompl els contadors de fills
			long[] counts = contenidorHelper.countFillsAmbPermisReadByContenidors(
					entitat,
					busties,
					false);
			for (int i = 0; i < resposta.size(); i++) {
				BustiaDto bustia = resposta.get(i);
				bustia.setFillsCount(new Long(counts[i]).intValue());
			}
			// Ompl els permisos
			omplirPermisosPerBusties(resposta, true);
		}
		return resposta;
	}

	@Override
	@Transactional(readOnly = true)
	public List<BustiaDto> findByUnitatCodiUsuari(
			Long entitatId,
			String unitatCodi) throws EntitatNotFoundException, UnitatNotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Cercant les bústies de la unitat per usuaris ("
				+ "entitatId=" + entitatId + ", "
				+ "unitatCodi=" + unitatCodi + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		List<BustiaEntity> busties = bustiaRepository.findByEntitatAndUnitatCodiAndPareNotNull(
				entitat,
				unitatCodi);
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
		List<BustiaDto> resposta = toBustiaDto(busties);
		if (!busties.isEmpty()) {
			// Ompl els contadors de fills i registres
			long[] countFills = contenidorHelper.countFillsAmbPermisReadByContenidors(
					entitat,
					busties,
					true);
			long[] countRegistres = contenidorHelper.countRegistresByContenidors(
					entitat,
					busties);
			for (int i = 0; i < resposta.size(); i++) {
				BustiaDto bustia = resposta.get(i);
				bustia.setFillsCount(new Long(countFills[i]).intValue());
				bustia.setRegistresCount(new Long(countRegistres[i]).intValue());
			}
			// Ompl els permisos
			omplirPermisosPerBusties(resposta, true);
		}
		return resposta;
	}

	@Override
	@Transactional
	public List<BustiaDto> findByEntitatAndActivaTrue(
			Long entitatId) throws EntitatNotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Cercant bústies actives de l'entitat ("
				+ "entitatId=" + entitatId + ", "
				+ "usuariCodi=" + auth.getName() + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		List<BustiaEntity> busties = bustiaRepository.findByEntitatAndActivaTrueAndPareNotNull(entitat);
		return toBustiaDto(busties);
	}

	@Override
	@Transactional
	public List<BustiaDto> findPermesesPerUsuari(
			Long entitatId) throws EntitatNotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Cercant bústies permeses per a l'usuari ("
				+ "entitatId=" + entitatId + ", "
				+ "usuariCodi=" + auth.getName() + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
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
		List<BustiaDto> resposta = toBustiaDto(busties);
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

	@Transactional(readOnly = true)
	@Override
	public List<ContenidorDto> findContingutPendent(
			Long entitatId,
			Long id) throws EntitatNotFoundException, BustiaNotFoundException {
		logger.debug("Consultant el contingut pendent de la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		// Comprova la bústia i que l'usuari hi tengui accés
		BustiaEntity bustia = comprovarBustia(
				entitat,
				id,
				true);
		ContenidorDto bustiaDto = contenidorHelper.toContenidorDto(
				bustia,
				false,
				true,
				true,
				false,
				false,
				false);
		return bustiaDto.getFills();
	}

	@Transactional(readOnly = true)
	@Override
	public List<RegistreAnotacioDto> findRegistrePendent(
			Long entitatId,
			Long id) throws EntitatNotFoundException, BustiaNotFoundException {
		logger.debug("Consultant els registres pendents de la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		// Comprova la bústia i que l'usuari hi tengui accés
		BustiaEntity bustia = comprovarBustia(
				entitat,
				id,
				true);
		ContenidorDto bustiaDto = contenidorHelper.toContenidorDto(
				bustia,
				false,
				true,
				true,
				false,
				false,
				false);
		return bustiaDto.getRegistres();
	}

	@Transactional(readOnly = true)
	@Override
	public RegistreAnotacioDto findOneRegistrePendent(
			Long entitatId,
			Long id,
			Long registreId) throws EntitatNotFoundException, BustiaNotFoundException {
		logger.debug("Consultant un registre pendent a la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "registreId=" + registreId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		// Comprova la bústia i que l'usuari hi tengui accés
		BustiaEntity bustia = comprovarBustia(
				entitat,
				id,
				true);
		return conversioTipusHelper.convertir(
				registreRepository.findByEntitatAndContenidorAndId(
						entitat,
						bustia,
						registreId),
				RegistreAnotacioDto.class);
	}

	@Transactional(readOnly = true)
	@Override
	public long countElementsPendentsBustiesAll(
			Long entitatId) throws EntitatNotFoundException {
		logger.debug("Consultant els elements pendents a totes les busties ("
				+ "entitatId=" + entitatId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
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
	public void refreshCountElementsPendentsBustiesAll(
			Long entitatId) throws EntitatNotFoundException {
		logger.debug("Refrescant el comptador dels elements pendents a totes les busties ("
				+ "entitatId=" + entitatId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		cacheHelper.evictElementsPendentsBustiesUsuari(
				entitat,
				auth.getName());		
	}

	@Override
	@Transactional
	public void updatePermis(
			Long entitatId,
			Long id,
			PermisDto permis) throws EntitatNotFoundException, BustiaNotFoundException {
		logger.debug("Actualitzant permis per a la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "permis=" + permis + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		BustiaEntity bustia = comprovarBustia(
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
			Long permisId) throws EntitatNotFoundException, BustiaNotFoundException {
		logger.debug("Esborrant permis per a la bústia ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "permisId=" + permisId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		BustiaEntity bustia = comprovarBustia(
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

	@Transactional(readOnly = true)
	@Override
	public ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzatives(
			Long entitatId,
			boolean nomesBustiesPermeses,
			boolean comptarElementsPendents) throws EntitatNotFoundException {
		logger.debug("Consulta de l'arbre d'unitats organitzatives ("
				+ "entitatId=" + entitatId + ", "
				+ "nomesBustiesPermeses=" + nomesBustiesPermeses + ", "
				+ "comptarElementsPendents=" + comptarElementsPendents + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		return findArbreUnitatsOrganitzatives(
				entitat,
				nomesBustiesPermeses,
				comptarElementsPendents);
	}

	@Transactional
	@Override
	public void forwardContenidor(
			Long entitatId,
			Long id,
			Long contenidorId,
			Long bustiaDestiId,
			String comentari) throws EntitatNotFoundException, BustiaNotFoundException, ContenidorNotFoundException {
		logger.debug("Reenviant contenidor ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "contenidorId=" + contenidorId + ", "
				+ "bustiaDestiId=" + bustiaDestiId + ", "
				+ "comentari=" + comentari + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		BustiaEntity bustiaOrigen = comprovarBustia(
				entitat,
				id,
				true);
		BustiaEntity bustiaDesti = comprovarBustia(
				entitat,
				bustiaDestiId,
				false);
		ContenidorEntity contenidor = comprovarContenidor(
				entitat,
				contenidorId,
				bustiaOrigen);
		// Fa l'enviament
		ContenidorMovimentEntity contenidorMoviment = contenidorHelper.ferIEnregistrarMovimentContenidor(
				contenidor,
				bustiaDesti,
				comentari);
		// Registra al log l'enviament del contenidor
		contenidorLogHelper.log(
				contenidor,
				LogTipusEnum.ELIMINACIO,
				null,
				contenidorMoviment,
				true,
				true);
		// Avisam per correu als responsables de la bústia de destí
		emailHelper.emailBustiaPendentContenidor(
				bustiaDesti,
				contenidor,
				contenidorMoviment);
	}

	@Transactional
	@Override
	public void forwardRegistre(
			Long entitatId,
			Long id,
			Long registreId,
			Long bustiaDestiId,
			String comentari) throws EntitatNotFoundException, BustiaNotFoundException, ContenidorNotFoundException {
		logger.debug("Reenviant registre ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "registreId=" + registreId + ", "
				+ "bustiaDestiId=" + bustiaDestiId + ", "
				+ "comentari=" + comentari + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		BustiaEntity bustiaOrigen = comprovarBustia(
				entitat,
				id,
				true);
		BustiaEntity bustiaDesti = comprovarBustia(
				entitat,
				bustiaDestiId,
				false);
		RegistreEntity registre = comprovarRegistre(
				entitat,
				registreId,
				bustiaOrigen);
		// Fa l'enviament
		registre.updateContenidor(bustiaDesti);
		RegistreMovimentEntity registreMoviment = RegistreMovimentEntity.getBuilder(
				registre,
				bustiaOrigen,
				bustiaDesti,
				usuariHelper.getUsuariAutenticat(),
				comentari).build();
		registreMovimentRepository.save(registreMoviment);
		// TODO Notificar el moviment a l'aplicació de registre
		// Refrescam cache usuaris bústia
		bustiaHelper.evictElementsPendentsBustia(
				entitat,
				bustiaOrigen);
		bustiaHelper.evictElementsPendentsBustia(
				entitat,
				bustiaDesti);
		// Avisam per correu als responsables de la bústia de destí
		emailHelper.emailBustiaPendentRegistre(
				bustiaDesti,
				registre);
	}



	private BustiaDto toBustiaDto(
			BustiaEntity bustia) {
		return (BustiaDto)contenidorHelper.toContenidorDto(bustia);
	}
	private List<BustiaDto> toBustiaDto(
			List<BustiaEntity> busties) {
		List<BustiaDto> resposta = new ArrayList<BustiaDto>();
		for (BustiaEntity bustia: busties)
			resposta.add(
					(BustiaDto)contenidorHelper.toContenidorDto(bustia));
		return resposta;
	}

	private BustiaEntity comprovarBustia(
			EntitatEntity entitat,
			Long bustiaId,
			boolean comprovarPermisRead) throws BustiaNotFoundException {
		BustiaEntity bustia = bustiaRepository.findOne(bustiaId);
		if (bustia == null) {
			logger.error("No s'ha trobat la bústia (bustiaId=" + bustiaId + ")");
			throw new BustiaNotFoundException();
		}
		if (!entitat.equals(bustia.getEntitat())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat de la bústia ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + bustia.getEntitat().getId() + ")");
			throw new BustiaNotFoundException();
		}
		if (comprovarPermisRead) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			boolean esPermisRead = permisosHelper.isGrantedAll(
					bustiaId,
					BustiaEntity.class,
					new Permission[] {ExtendedPermission.READ},
					auth);
			if (!esPermisRead) {
				logger.error("Aquest usuari no té permis per accedir a la bústia ("
						+ "id=" + bustiaId + ", "
						+ "usuari=" + auth.getName() + ")");
				throw new SecurityException("Sense permisos d'accés a aquesta bústia");
			}
		}
		return bustia;
	}

	private ContenidorEntity comprovarContenidor(
			EntitatEntity entitat,
			Long id,
			BustiaEntity bustiaPare) throws EntitatNotFoundException {
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
		if (bustiaPare != null) {
			if (contenidor.getPare() != null) {
				if (!contenidor.getPare().getId().equals(bustiaPare.getId())) {
					logger.error("El contenidor no està dins la bústia especificada ("
							+ "bustiaId1=" + bustiaPare.getId() + ", "
							+ "bustiaId2=" + contenidor.getPare().getId() + ")");
					throw new ContenidorNotFoundException();
				}
			} else {
				
			}
			
		}
		return contenidor;
	}

	private RegistreEntity comprovarRegistre(
			EntitatEntity entitat,
			Long id,
			BustiaEntity bustiaPare) throws RegistreNotFoundException {
		RegistreEntity registre = registreRepository.findOne(id);
		if (registre == null) {
			logger.error("No s'ha trobat l'anotació de registre (id=" + id + ")");
			throw new RegistreNotFoundException();
		}
		if (!entitat.getId().equals(registre.getEntitat().getId())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat de l'anotació de registre ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + registre.getEntitat().getId() + ")");
			throw new RegistreNotFoundException();
		}
		if (bustiaPare != null) {
			if (registre.getContenidor() != null) {
				if (!registre.getContenidor().getId().equals(bustiaPare.getId())) {
					logger.error("El registre no està dins la bústia especificada ("
							+ "bustiaId1=" + bustiaPare.getId() + ", "
							+ "bustiaId2=" + registre.getContenidor().getId() + ")");
					throw new RegistreNotFoundException();
				}
			} else {
				
			}
			
		}
		return registre;
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

	private ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzatives(
			EntitatEntity entitat,
			boolean nomesAmbBustiesPermeses,
			boolean ambContadorElementsPendents) {
		List<BustiaEntity> busties = bustiaRepository.findByEntitatAndPareNotNull(entitat);
		Set<String> bustiaUnitatCodis = null;
		if (nomesAmbBustiesPermeses) {
			bustiaUnitatCodis = new HashSet<String>();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
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
			for (BustiaEntity bustia: busties)
				bustiaUnitatCodis.add(bustia.getUnitatCodi());
		}
		// Consulta l'arbre
		ArbreDto<UnitatOrganitzativaDto> arbre = unitatOrganitzativaHelper.findUnitatsOrganitzativesPerEntitatAmbPermesos(
				entitat.getCodi(),
				bustiaUnitatCodis);
		if (ambContadorElementsPendents && !busties.isEmpty()) {
			// Consulta els contadors d'elements pendents per a totes les bústies
			long[] countFills = contenidorHelper.countFillsAmbPermisReadByContenidors(
					entitat,
					busties,
					nomesAmbBustiesPermeses);
			long[] countRegistres = contenidorHelper.countRegistresByContenidors(
					entitat,
					busties);
			// Calcula els acumulats de pendents per a cada unitat
			Map<String, Long> acumulats = new HashMap<String, Long>();
			for (int i = 0; i < busties.size(); i++) {
				BustiaEntity bustia = busties.get(i);
				Long acumulat = acumulats.get(bustia.getUnitatCodi());
				if (acumulat == null) {
					acumulats.put(
							bustia.getUnitatCodi(),
							countFills[i] + countRegistres[i]);
				} else {
					acumulats.put(
							bustia.getUnitatCodi(),
							acumulat + countFills[i] + countRegistres[i]);
				}
			}
			// Recorr l'arbre actualitzant els contadors de pendents
			for (ArbreNodeDto<UnitatOrganitzativaDto> node: arbre.toList()) {
				String unitatCodi = node.getDades().getCodi();
				Long acumulat = acumulats.get(unitatCodi);
				if (acumulat == null)
					node.setCount(new Long(0));
				else
					node.setCount(acumulat);
			}
		}
		return arbre;
	}

	private static final Logger logger = LoggerFactory.getLogger(BustiaServiceImpl.class);

}
