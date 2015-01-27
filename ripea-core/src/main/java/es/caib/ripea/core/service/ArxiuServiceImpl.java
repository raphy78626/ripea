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
import es.caib.ripea.core.api.dto.ArxiuDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.exception.ArxiuNotFoundException;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.UnitatNotFoundException;
import es.caib.ripea.core.api.service.ArxiuService;
import es.caib.ripea.core.entity.ArxiuEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.MetaExpedientEntity;
import es.caib.ripea.core.entity.MetaNodeEntity;
import es.caib.ripea.core.helper.CacheHelper;
import es.caib.ripea.core.helper.ContenidorHelper;
import es.caib.ripea.core.helper.PermisosComprovacioHelper;
import es.caib.ripea.core.helper.PermisosHelper;
import es.caib.ripea.core.helper.PermisosHelper.ObjectIdentifierExtractor;
import es.caib.ripea.core.helper.UnitatOrganitzativaHelper;
import es.caib.ripea.core.repository.ArxiuRepository;
import es.caib.ripea.core.repository.EntitatRepository;
import es.caib.ripea.core.repository.ExpedientRepository;
import es.caib.ripea.core.repository.MetaExpedientRepository;
import es.caib.ripea.core.security.ExtendedPermission;

/**
 * Implementació dels mètodes per a gestionar arxius.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class ArxiuServiceImpl implements ArxiuService {

	@Resource
	private ArxiuRepository arxiuRepository;
	@Resource
	private EntitatRepository entitatRepository;
	@Resource
	private ExpedientRepository expedientRepository;
	@Resource
	private MetaExpedientRepository metaExpedientRepository;

	@Resource
	private ContenidorHelper contenidorHelper;
	@Resource
	private PermisosHelper permisosHelper;
	@Resource
	private PermisosComprovacioHelper permisosComprovacioHelper;
	@Resource
	private CacheHelper cacheHelper;
	@Resource
	private UnitatOrganitzativaHelper unitatOrganitzativaHelper;



	@Override
	@Transactional
	public ArxiuDto create(
			Long entitatId,
			ArxiuDto arxiu) throws EntitatNotFoundException, UnitatNotFoundException {
		logger.debug("Creant un nou arxiu ("
				+ "entitatId=" + entitatId + ", "
				+ "arxiuViu=" + arxiu + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		UnitatOrganitzativaDto unitat = unitatOrganitzativaHelper.findPerEntitatAndCodi(
				entitat.getCodi(),
				arxiu.getUnitatCodi());
		if (unitat == null) {
			logger.error("No s'ha trobat l'unitat administrativa (codi=" + arxiu.getUnitatCodi() + ")");
			throw new UnitatNotFoundException();
		}
		// Cerca l'arxiu superior
		ArxiuEntity arxiuPare = arxiuRepository.findByEntitatAndUnitatCodiAndPareNull(
				entitat,
				arxiu.getUnitatCodi());
		if (arxiuPare == null) {
			// Si l'arxiu superior no existeix el crea
			arxiuPare = ArxiuEntity.getBuilder(
					entitat,
					unitat.getDenominacio(),
					arxiu.getUnitatCodi(),
					null).build();
			arxiuRepository.save(arxiuPare);
		}
		ArxiuEntity entity = ArxiuEntity.getBuilder(
				entitat,
				arxiu.getNom(),
				arxiu.getUnitatCodi(),
				arxiuPare).build();
		return toArxiuDto(
				arxiuRepository.save(entity));
	}

	@Override
	@Transactional
	public ArxiuDto update(
			Long entitatId,
			ArxiuDto arxiu) throws EntitatNotFoundException, ArxiuNotFoundException {
		logger.debug("Modificant l'arxiu ("
				+ "entitatId=" + entitatId + ", "
				+ "arxiu=" + arxiu + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ArxiuEntity entity = comprovarArxiu(
				entitat,
				arxiu.getId());
		entity.update(
				arxiu.getNom());
		return toArxiuDto(entity);
	}

	@Override
	@Transactional
	public ArxiuDto updateActiu(
			Long entitatId,
			Long id,
			boolean actiu) throws EntitatNotFoundException, ArxiuNotFoundException {
		logger.debug("Modificant propietat actiu de l'arxiu ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "actiu=" + actiu + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ArxiuEntity entity = comprovarArxiu(
				entitat,
				id);
		entity.updateActiu(actiu);
		return toArxiuDto(entity);
	}

	@Override
	@Transactional
	public ArxiuDto delete(
			Long entitatId,
			Long id) throws EntitatNotFoundException, ArxiuNotFoundException {
		logger.debug("Esborrant l'arxiu ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ArxiuEntity arxiu = comprovarArxiu(
				entitat,
				id);
		arxiuRepository.delete(arxiu);
		return toArxiuDto(arxiu);
	}

	@Override
	@Transactional(readOnly = true)
	public ArxiuDto findById(
			Long entitatId,
			Long id) throws EntitatNotFoundException {
		logger.debug("Cercant l'arxiu ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ArxiuEntity arxiu = comprovarArxiu(
				entitat,
				id);
		ArxiuDto resposta = toArxiuDto(arxiu);
		// Ompl els contadors d'expedients
		List<Object[]> countExpedients = expedientRepository.countByArxiu(
				entitat);
		for (Object[] reg: countExpedients) {
			Long arxiuId = (Long)reg[0];
			if (arxiuId.equals(arxiu.getId())) {
				Long count = (Long)reg[1];
				resposta.setExpedientsCount(count.intValue());
				break;
			}
		}
		// Ompl els permisos
		List<ArxiuDto> llista = new ArrayList<ArxiuDto>();
		llista.add(resposta);
		omplirPermisosPerArxius(llista, true);
		return resposta;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ArxiuDto> findByUnitatCodiAdmin(
			Long entitatId,
			String unitatCodi) throws EntitatNotFoundException, UnitatNotFoundException {
		logger.debug("Cercant els arxius de la unitat per admins ("
				+ "entitatId=" + entitatId + ", "
				+ "unitatCodi=" + unitatCodi + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		List<ArxiuDto> resposta = toArxiuDto(
				arxiuRepository.findByEntitatAndUnitatCodiAndPareNotNull(
						entitat,
						unitatCodi));
		// Ompl els contadors d'expedients
		List<Object[]> countExpedients = expedientRepository.countByArxiu(
				entitat);
		for (ArxiuDto arxiuViu: resposta) {
			for (Object[] reg: countExpedients) {
				Long arxiuId = (Long)reg[0];
				if (arxiuId.equals(arxiuViu.getId())) {
					Long count = (Long)reg[1];
					arxiuViu.setExpedientsCount(count.intValue());
					break;
				}
			}
		}
		// Ompl els permisos
		omplirPermisosPerArxius(resposta, true);
		return resposta;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ArxiuDto> findByUnitatCodiUsuari(
			Long entitatId,
			String unitatCodi) throws EntitatNotFoundException, UnitatNotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Cercant els arxius vius de la unitat per usuaris ("
				+ "entitatId=" + entitatId + ", "
				+ "unitatCodi=" + unitatCodi + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		List<ArxiuDto> resposta = toArxiuDto(
				arxiuRepository.findByEntitatAndUnitatCodiAndPareNotNull(
						entitat,
						unitatCodi));
		// Filtra la llista d'arxius segons els permisos
		permisosHelper.filterGrantedAll(
				resposta,
				new ObjectIdentifierExtractor<ArxiuDto>() {
					@Override
					public Long getObjectIdentifier(ArxiuDto arxiu) {
						return arxiu.getId();
					}
				},
				ArxiuEntity.class,
				new Permission[] {ExtendedPermission.READ},
				auth);
		// Ompl els contadors d'expedients
		List<Object[]> countExpedients = countExpedientsPermesosAgrupatsPerArxiu(entitat);
		for (ArxiuDto arxiu: resposta) {
			int expedientsCount = 0;
			if (countExpedients != null) {
				for (Object[] reg: countExpedients) {
					Long arxiuId = (Long)reg[0];
					if (arxiuId.equals(arxiu.getId())) {
						Long count = (Long)reg[1];
						expedientsCount = count.intValue();
						break;
					}
				}
			}
			arxiu.setExpedientsCount(expedientsCount);
		}
		// Ompl els permisos
		omplirPermisosPerArxius(resposta, true);
		return resposta;
	}

	@Override
	@Transactional
	public List<ArxiuDto> findPermesosPerUsuari(
			Long entitatId) throws EntitatNotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Cercan arxius permesos per a l'usuari ("
				+ "entitatId=" + entitatId + ", "
				+ "usuariCodi=" + auth.getName() + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		List<ArxiuEntity> arxius = arxiuRepository.findByEntitatAndPareNotNull(
				entitat);
		// Filtra la llista d'arxius segons els permisos
		permisosHelper.filterGrantedAll(
				arxius,
				new ObjectIdentifierExtractor<ArxiuEntity>() {
					@Override
					public Long getObjectIdentifier(ArxiuEntity arxiu) {
						return arxiu.getId();
					}
				},
				ArxiuEntity.class,
				new Permission[] {ExtendedPermission.READ},
				auth);
		// Converteix a DTO
		List<ArxiuDto> resposta = toArxiuDto(arxius);
		// Ompl els permisos
		omplirPermisosPerArxius(resposta, true);
		// Ompl les unitats
		List<UnitatOrganitzativaDto> unitats = cacheHelper.findUnitatsOrganitzativesPerEntitat(
				entitat.getCodi()).toDadesList();
		for (ArxiuDto arxiu: resposta) {
			for (UnitatOrganitzativaDto unitat: unitats) {
				if (unitat.getCodi().equals(arxiu.getUnitatCodi())) {
					arxiu.setUnitat(unitat);
					break;
				}
			}
		}
		return resposta;
	}

	@Override
	@Transactional
	public void updatePermis(
			Long entitatId,
			Long id,
			PermisDto permis) throws EntitatNotFoundException, ArxiuNotFoundException {
		logger.debug("Actualitzant permis per a l'arxiu ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "permis=" + permis + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		comprovarArxiu(
				entitat,
				id);
		permisosHelper.updatePermis(
				id,
				ArxiuEntity.class,
				permis);
	}

	@Override
	@Transactional
	public void deletePermis(
			Long entitatId,
			Long id,
			Long permisId) throws EntitatNotFoundException, ArxiuNotFoundException {
		logger.debug("Esborrant permis per a l'arxiu ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "permisId=" + permisId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		comprovarArxiu(
				entitat,
				id);
		permisosHelper.deletePermis(
				id,
				ArxiuEntity.class,
				permisId);
	}

	@Transactional(readOnly = true)
	@Override
	public ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzativesAdmin(
			Long entitatId) throws EntitatNotFoundException {
		logger.debug("Consulta de l'arbre d'unitats organitzatives per administrador ("
				+ "entitatId=" + entitatId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		return findArbreUnitatsOrganitzatives(
				entitat,
				false,
				false);
	}

	@Transactional(readOnly = true)
	@Override
	public ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzativesUser(
			Long entitatId) throws EntitatNotFoundException {
		logger.debug("Consulta de l'arbre d'unitats organitzatives per usuari ("
				+ "entitatId=" + entitatId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		return findArbreUnitatsOrganitzatives(
				entitat,
				true,
				true);
	}



	private ArxiuDto toArxiuDto(
			ArxiuEntity bustia) {
		return (ArxiuDto)contenidorHelper.toContenidorDto(bustia);
	}
	private List<ArxiuDto> toArxiuDto(
			List<ArxiuEntity> arxius) {
		List<ArxiuDto> resposta = new ArrayList<ArxiuDto>();
		for (ArxiuEntity arxiu: arxius)
			resposta.add(
					(ArxiuDto)contenidorHelper.toContenidorDto(arxiu));
		return resposta;
	}

	private ArxiuEntity comprovarArxiu(
			EntitatEntity entitat,
			Long arxiuId) throws ArxiuNotFoundException {
		ArxiuEntity arxiu = arxiuRepository.findOne(arxiuId);
		if (arxiu == null) {
			logger.error("No s'ha trobat l'arxiu (arxiuId=" + arxiuId + ")");
			throw new ArxiuNotFoundException();
		}
		if (!entitat.equals(arxiu.getEntitat())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat de l'arxiu ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + arxiu.getEntitat().getId() + ")");
			throw new ArxiuNotFoundException();
		}
		return arxiu;
	}

	private void omplirPermisosPerArxius(
			List<? extends ArxiuDto> arxius,
			boolean ambLlistaPermisos) {
		// Filtra les entitats per saber els permisos per a l'usuari actual
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<ArxiuDto> arxiusRead = new ArrayList<ArxiuDto>();
		arxiusRead.addAll(arxius);
		permisosHelper.filterGrantedAll(
				arxiusRead,
				new ObjectIdentifierExtractor<ArxiuDto>() {
					public Long getObjectIdentifier(ArxiuDto arxiu) {
						return arxiu.getId();
					}
				},
				ArxiuEntity.class,
				new Permission[] {ExtendedPermission.READ},
				auth);
		for (ArxiuDto arxiu: arxius) {
			arxiu.setUsuariActualRead(
					arxiusRead.contains(arxiu));
		}
		// Obté els permisos per a tots els arxius només amb una consulta
		if (ambLlistaPermisos) {
			List<Long> ids = new ArrayList<Long>();
			for (ArxiuDto arxiu: arxius)
				ids.add(arxiu.getId());
			Map<Long, List<PermisDto>> permisos = permisosHelper.findPermisos(
					ids,
					ArxiuEntity.class);
			for (ArxiuDto arxiu: arxius)
				arxiu.setPermisos(permisos.get(arxiu.getId()));
		}
	}

	private ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzatives(
			EntitatEntity entitat,
			boolean nomesAmbArxiusPermesos,
			boolean ambContadorExpedientsPendents) {
		List<ArxiuEntity> arxius = arxiuRepository.findByEntitatAndPareNotNull(entitat);
		Set<String> arxiuUnitatCodis = null;
		if (nomesAmbArxiusPermesos) {
			arxiuUnitatCodis = new HashSet<String>();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			permisosHelper.filterGrantedAll(
					arxius,
					new ObjectIdentifierExtractor<ArxiuEntity>() {
						@Override
						public Long getObjectIdentifier(ArxiuEntity arxiu) {
							return arxiu.getId();
						}
					},
					ArxiuEntity.class,
					new Permission[] {ExtendedPermission.READ},
					auth);
			for (ArxiuEntity arxiu: arxius)
				arxiuUnitatCodis.add(arxiu.getUnitatCodi());
		}
		// Consulta l'arbre
		ArbreDto<UnitatOrganitzativaDto> arbre = unitatOrganitzativaHelper.findUnitatsOrganitzativesPerEntitatAmbPermesos(
				entitat.getCodi(),
				arxiuUnitatCodis);
		if (ambContadorExpedientsPendents) {
			// Consulta els contadors d'expedients per a tots els arxius
			List<Object[]> countExpedients = countExpedientsPermesosAgrupatsPerArxiu(entitat);
			// Calcula els acumulats de pendents per a cada unitat
			Map<String, Long> acumulats = new HashMap<String, Long>();
			for (Object[] registreCount: countExpedients) {
				Long arxiuId = (Long)registreCount[0];
				for (ArxiuEntity arxiu: arxius) {
					if (arxiu.getId().equals(arxiuId)) {
						Long count = (Long)registreCount[1];
						Long acumulat = acumulats.get(arxiu.getUnitatCodi());
						acumulats.put(
								arxiu.getUnitatCodi(),
								(acumulat == null) ? count : acumulat + count);
						break;
					}
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

	private List<Object[]> countExpedientsPermesosAgrupatsPerArxiu(
			EntitatEntity entitat) {
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
		List<Object[]> countExpedients = null;
		if (metaExpedientsPermesos.size() > 0) {
			// Si se passa una llista buida la consulta dona error
			countExpedients = expedientRepository.countPermesosByArxiu(
					entitat,
					metaExpedientsPermesos);
		}
		return countExpedients;
	}

	private static final Logger logger = LoggerFactory.getLogger(ArxiuServiceImpl.class);

}
