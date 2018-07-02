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
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.service.ArxiuService;
import es.caib.ripea.core.entity.ArxiuEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.MetaExpedientEntity;
import es.caib.ripea.core.entity.MetaNodeEntity;
import es.caib.ripea.core.helper.CacheHelper;
import es.caib.ripea.core.helper.ContingutHelper;
import es.caib.ripea.core.helper.ContingutLogHelper;
import es.caib.ripea.core.helper.EntityComprovarHelper;
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
	private ContingutHelper contenidorHelper;
	@Resource
	private PermisosHelper permisosHelper;
	@Resource
	private CacheHelper cacheHelper;
	@Resource
	private ContingutLogHelper contingutLogHelper;
	@Resource
	private EntityComprovarHelper entityComprovarHelper;
	@Resource
	private UnitatOrganitzativaHelper unitatOrganitzativaHelper;



	@Override
	@Transactional
	public ArxiuDto create(
			Long entitatId,
			ArxiuDto arxiu) {
		logger.debug("Creant un nou arxiu ("
				+ "entitatId=" + entitatId + ", "
				+ "arxiuViu=" + arxiu + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		UnitatOrganitzativaDto unitat = unitatOrganitzativaHelper.findPerEntitatAndCodi(
				entitat.getCodi(),
				arxiu.getUnitatCodi());
		if (unitat == null) {
			logger.error("No s'ha trobat l'unitat administrativa (codi=" + arxiu.getUnitatCodi() + ")");
			throw new NotFoundException(
					arxiu.getUnitatCodi(),
					UnitatOrganitzativaDto.class);
		}
		// Cerca l'arxiu superior
		ArxiuEntity arxiuPare = arxiuRepository.findByEntitatAndUnitatCodiAndPareNull(
				entitat,
				arxiu.getUnitatCodi());
		if (arxiuPare == null) {
			// Si l'arxiu superior no existeix el crea
			arxiuPare = arxiuRepository.save(
					ArxiuEntity.getBuilder(
							entitat,
							unitat.getDenominacio(),
							arxiu.getUnitatCodi(),
							null).build());
		}
		ArxiuEntity entity = ArxiuEntity.getBuilder(
				entitat,
				arxiu.getNom(),
				arxiu.getUnitatCodi(),
				arxiuPare).build();
		arxiuRepository.save(entity);
		// Registra al log la creació de l'arxiu
		contingutLogHelper.logCreacio(
				entity,
				false,
				false);
		return toArxiuDto(entity);
	}

	@Override
	@Transactional
	public ArxiuDto update(
			Long entitatId,
			ArxiuDto arxiu) {
		logger.debug("Modificant l'arxiu ("
				+ "entitatId=" + entitatId + ", "
				+ "arxiu=" + arxiu + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ArxiuEntity entity = entityComprovarHelper.comprovarArxiu(
				entitat,
				arxiu.getId(),
				false);
		String nomOriginal = entity.getNom();
		entity.update(
				arxiu.getNom());
		// Registra al log la modificació de l'arxiu
		contingutLogHelper.log(
				entity,
				LogTipusEnumDto.MODIFICACIO,
				(!nomOriginal.equals(entity.getNom())) ? entity.getNom() : null,
				null,
				false,
				false);
		return toArxiuDto(entity);
	}

	@Override
	@Transactional
	public ArxiuDto updateActiu(
			Long entitatId,
			Long id,
			boolean actiu) throws NotFoundException {
		logger.debug("Modificant propietat actiu de l'arxiu ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "actiu=" + actiu + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ArxiuEntity entity = entityComprovarHelper.comprovarArxiu(
				entitat,
				id,
				false);
		entity.updateActiu(actiu);
		// Registra al log la modificació de l'arxiu
		contingutLogHelper.log(
				entity,
				actiu ? LogTipusEnumDto.ACTIVACIO : LogTipusEnumDto.DESACTIVACIO,
				null,
				false,
				false);
		return toArxiuDto(entity);
	}

	@Override
	@Transactional
	public ArxiuDto delete(
			Long entitatId,
			Long id) throws NotFoundException {
		logger.debug("Esborrant l'arxiu ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ArxiuEntity arxiu = entityComprovarHelper.comprovarArxiu(
				entitat,
				id,
				false);
		arxiuRepository.delete(arxiu);
		// Registra al log l'eliminació de l'arxiu
		contingutLogHelper.log(
				arxiu,
				LogTipusEnumDto.ELIMINACIO,
				null,
				null,
				true,
				true);
		return toArxiuDto(arxiu);
	}

	@Override
	@Transactional(readOnly = true)
	public ArxiuDto findById(
			Long entitatId,
			Long id) throws NotFoundException {
		logger.debug("Cercant l'arxiu ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		ArxiuEntity arxiu = entityComprovarHelper.comprovarArxiu(
				entitat,
				id,
				false);
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
		if (arxiu.getMetaExpedients() != null) {
			resposta.setMetaExpedientsCount(arxiu.getMetaExpedients().size());
		}
		return resposta;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ArxiuDto> findByUnitatCodiAdmin(
			Long entitatId,
			String unitatCodi) throws NotFoundException {
		logger.debug("Cercant els arxius de la unitat per admins ("
				+ "entitatId=" + entitatId + ", "
				+ "unitatCodi=" + unitatCodi + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
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
		// Omple els comptador de meta-expedients 
		List<Object[]> countMetaExpedients = arxiuRepository.countMetaExpedients(
				entitat); 
		for (ArxiuDto arxiuViu: resposta) {
			for (Object[] reg: countMetaExpedients) {
				Long arxiuId = (Long)reg[0];
				if (arxiuId.equals(arxiuViu.getId())) {
					Integer count = (Integer)reg[1];
					arxiuViu.setMetaExpedientsCount(count.intValue());
					break;
				}
			}
		}

		return resposta;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ArxiuDto> findByUnitatCodiUsuari(
			Long entitatId,
			String unitatCodi) throws NotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Cercant els arxius vius de la unitat per usuaris ("
				+ "entitatId=" + entitatId + ", "
				+ "unitatCodi=" + unitatCodi + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		List<ArxiuDto> resposta = new ArrayList<ArxiuDto>();
		List<ArxiuEntity> arxius = arxiuRepository.findByEntitatAndUnitatCodiAndPareNotNull(
				entitat,
				unitatCodi);
		boolean granted;
		for(ArxiuEntity arxiu : arxius) {
			// Comprova l'accés als meta-expedients
			granted = false;
			for (MetaExpedientEntity metaExpedient: arxiu.getMetaExpedients()) {
				if (permisosHelper.isGrantedAll(
						metaExpedient.getId(),
						MetaNodeEntity.class,
						new Permission[] {ExtendedPermission.READ},
						auth)) {
					granted = true;
					break;
				}
			}
			if (granted) 
				resposta.add(toArxiuDto(arxiu));
		}
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
		return resposta;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ArxiuDto> findPermesosPerUsuariIMetaExpedient(
			Long entitatId,
			Long metaExpedientId) throws NotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Cercan arxius permesos per a l'usuari i el meta-expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "metaExpedientId=" + entitatId + ", "
				+ "usuariCodi=" + auth.getName() + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		MetaExpedientEntity metaExpedient = entityComprovarHelper.comprovarMetaExpedient(
				entitat,
				metaExpedientId,
				false,
				false);
		return toArxiuDto(metaExpedient.getArxius());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ArxiuDto> findPermesosPerUsuari(
			Long entitatId) throws NotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Cercan arxius permesos per a l'usuari ("
				+ "entitatId=" + entitatId + ", "
				+ "usuariCodi=" + auth.getName() + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		List<ArxiuEntity> arxius = arxiuRepository.findByEntitatAndPareNotNull(entitat);
		List<ArxiuEntity> arxiusPermesos = new ArrayList<ArxiuEntity>();
		// Afegeix les unitats dels arxius que estiguin relacionats amb algun meta-expedient amb permisos
		for (ArxiuEntity arxiu: arxius) {
			// Comprova l'accés als meta-expedients
			for (MetaExpedientEntity metaExpedient: arxiu.getMetaExpedients()) {
				if (permisosHelper.isGrantedAll(
						metaExpedient.getId(),
						MetaNodeEntity.class,
						new Permission[] {ExtendedPermission.READ},
						auth)) {
					arxiusPermesos.add(arxiu);
					break;
				}
			}
		}
		return toArxiuDto(arxiusPermesos);
	}

	@Transactional(readOnly = true)
	@Override
	public ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzativesAdmin(
			Long entitatId,
			boolean nomesAmbArxiusPermesos,
			boolean nomesQueContinguinArxius) throws NotFoundException {
		logger.debug("Consulta de l'arbre d'unitats organitzatives per administrador ("
				+ "entitatId=" + entitatId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		return findArbreUnitatsOrganitzatives(
				entitat,
				nomesAmbArxiusPermesos,
				nomesQueContinguinArxius,
				false);
	}

	@Transactional(readOnly = true)
	@Override
	public ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzativesUser(
			Long entitatId) throws NotFoundException {
		logger.debug("Consulta de l'arbre d'unitats organitzatives per usuari ("
				+ "entitatId=" + entitatId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		return findArbreUnitatsOrganitzatives(
				entitat,
				true,
				false,
				true);
	}

	@Override
	@Transactional
	public void addMetaExpedient(
			Long entitatId, 
			Long id, 
			Long metaExpedientId) throws NotFoundException {
		logger.debug("Afegint la relació amb meta-expedient per a l'arxiu ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "metaExpedientId=" + metaExpedientId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ArxiuEntity arxiu = entityComprovarHelper.comprovarArxiu(
				entitat,
				id,
				false);
		MetaExpedientEntity metaExpedient = entityComprovarHelper.comprovarMetaExpedient(
				entitat, 
				metaExpedientId,
				false,
				false);
		if (!arxiu.getMetaExpedients().contains(metaExpedient)) {
			arxiu.getMetaExpedients().add(metaExpedient);
			arxiuRepository.saveAndFlush(arxiu);	
		}
	}

	@Override
	@Transactional
	public void removeMetaExpedient(
			Long entitatId, 
			Long id, 
			Long metaExpedientId) throws NotFoundException {
		logger.debug("Esborrant relació amb meta-expedient per a l'arxiu ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "metaExpedientId=" + metaExpedientId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		ArxiuEntity arxiu = entityComprovarHelper.comprovarArxiu(
				entitat,
				id,
				false);
		MetaExpedientEntity metaExpedient = entityComprovarHelper.comprovarMetaExpedient(
				entitat, 
				metaExpedientId,
				false,
				false);
		arxiu.getMetaExpedients().remove(metaExpedient);
		arxiuRepository.saveAndFlush(arxiu);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ArxiuDto> findAmbMetaExpedient(
			Long entitatId, 
			Long id) {
		logger.debug("Consulta els arxius del meta-expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaExpedientEntity metaExpedient = entityComprovarHelper.comprovarMetaExpedient(
				entitat,
				id,
				false,
				false);
		List<ArxiuDto> resposta = new ArrayList<ArxiuDto>();
		for (ArxiuEntity arxiu: metaExpedient.getArxius()) {
			resposta.add(toArxiuDto(arxiu));
		}
		return resposta;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ArxiuDto> findAmbMetaExpedientPerCreacio(
			Long entitatId, 
			Long id) {
		logger.debug("Consulta els arxius del meta-expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		MetaExpedientEntity metaExpedient = entityComprovarHelper.comprovarMetaExpedient(
				entitat,
				id,
				true,
				false);
		return toArxiuDto(metaExpedient.getArxius());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ArxiuDto> findActiusAmbEntitat(
			Long entitatId) {
		logger.debug("Consulta els arxius actius de l'entitat ("
				+ "entitatId=" + entitatId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		return toArxiuDto(arxiuRepository.findByEntitatAndActiuTrue(entitat));
	}



	private ArxiuDto toArxiuDto(
			ArxiuEntity arxiu) {
		return (ArxiuDto)contenidorHelper.toContingutDto(arxiu);
	}
	private List<ArxiuDto> toArxiuDto(
			List<ArxiuEntity> arxius) {
		List<ArxiuDto> resposta = new ArrayList<ArxiuDto>();
		for (ArxiuEntity arxiu: arxius)
			resposta.add(
					(ArxiuDto)contenidorHelper.toContingutDto(arxiu));
		return resposta;
	}

	private ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzatives(
			EntitatEntity entitat,
			boolean nomesAmbArxiusPermesos,
			boolean nomesQueContinguinArxius,
			boolean ambContadorExpedientsPendents) {
		List<ArxiuEntity> arxius = arxiuRepository.findByEntitatAndPareNotNull(entitat);
		Set<String> arxiuUnitatCodis = null;
		if (nomesAmbArxiusPermesos ) {
			arxiuUnitatCodis = new HashSet<String>();
			// Filtra els meta-expedients dels arxius en el que l'usuari tingui permisos de lectura
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			// Afegeix les unitats dels arxius que estiguin relacionats amb algun meta-expedient amb permisos
			for (ArxiuEntity arxiu: arxius) {
				boolean granted = false;
				// Comprova l'accés als meta-expedients
				for (MetaExpedientEntity metaExpedient: arxiu.getMetaExpedients()) {
					if (permisosHelper.isGrantedAll(
							metaExpedient.getId(),
							MetaNodeEntity.class,
							new Permission[] {ExtendedPermission.READ},
							auth)) {
						granted = true;
						break;
					}
				}
				if (granted)
					arxiuUnitatCodis.add(arxiu.getUnitatCodi());
			}
		} else if (nomesQueContinguinArxius) {
			arxiuUnitatCodis = new HashSet<String>();
			for (ArxiuEntity arxiu: arxius) {
				arxiuUnitatCodis.add(arxiu.getUnitatCodi());
			}
		}
		// Consulta l'arbre
		ArbreDto<UnitatOrganitzativaDto> arbre = unitatOrganitzativaHelper.findPerUnitatArrelAmbCodisPermesos(
				entitat.getUnitatArrel(),
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
		List<Object[]> countExpedients;
		if (metaExpedientsPermesos.size() > 0) {
			// Si se passa una llista buida la consulta dona error
			countExpedients = expedientRepository.countPermesosByArxiu(
					entitat,
					metaExpedientsPermesos);
		} else {
			return new ArrayList<Object[]>();
		}
		return countExpedients;
	}

	private static final Logger logger = LoggerFactory.getLogger(ArxiuServiceImpl.class);

}
