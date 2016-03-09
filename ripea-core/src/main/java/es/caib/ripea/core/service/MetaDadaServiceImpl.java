/**
 * 
 */
package es.caib.ripea.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.MetaDadaDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.service.MetaDadaService;
import es.caib.ripea.core.entity.DadaEntity;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.MetaDadaEntity;
import es.caib.ripea.core.entity.MetaDadaTipusEnum;
import es.caib.ripea.core.entity.MetaNodeMetaDadaEntity;
import es.caib.ripea.core.entity.MultiplicitatEnum;
import es.caib.ripea.core.entity.NodeEntity;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.EntityComprovarHelper;
import es.caib.ripea.core.helper.PaginacioHelper;
import es.caib.ripea.core.helper.PermisosHelper;
import es.caib.ripea.core.repository.DadaRepository;
import es.caib.ripea.core.repository.EntitatRepository;
import es.caib.ripea.core.repository.MetaDadaRepository;
import es.caib.ripea.core.repository.MetaNodeMetaDadaRepository;
import es.caib.ripea.core.repository.NodeRepository;

/**
 * Implementació del servei de gestió de meta-dades.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class MetaDadaServiceImpl implements MetaDadaService {

	@Resource
	private MetaDadaRepository metaDadaRepository;
	@Resource
	private EntitatRepository entitatRepository;
	@Resource
	private NodeRepository nodeRepository;
	@Resource
	private MetaNodeMetaDadaRepository metaNodeMetaDadaRepository;
	@Resource
	private DadaRepository dadaRepository;

	@Resource
	ConversioTipusHelper conversioTipusHelper;
	@Resource
	PaginacioHelper paginacioHelper;
	@Resource
	private PermisosHelper permisosHelper;
	@Resource
	private EntityComprovarHelper entityComprovarHelper;



	@Transactional
	@Override
	public MetaDadaDto create(
			Long entitatId,
			MetaDadaDto metaDada) {
		logger.debug("Creant una nova meta-dada ("
				+ "entitatId=" + entitatId + ", "
				+ "metaDada=" + metaDada + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaDadaEntity entity = MetaDadaEntity.getBuilder(
				metaDada.getCodi(),
				metaDada.getNom(),
				metaDada.getDescripcio(),
				MetaDadaTipusEnum.valueOf(metaDada.getTipus().name()),
				metaDada.isGlobalExpedient(),
				metaDada.isGlobalDocument(),
				MultiplicitatEnum.valueOf(metaDada.getGlobalMultiplicitat().name()),
				metaDada.isGlobalReadOnly(),
				entitat).build();
		return conversioTipusHelper.convertir(
				metaDadaRepository.save(entity),
				MetaDadaDto.class);
	}

	@Transactional
	@Override
	public MetaDadaDto update(
			Long entitatId,
			MetaDadaDto metaDada) {
		logger.debug("Actualitzant meta-dada existent ("
				+ "entitatId=" + entitatId + ", "
				+ "metaDada=" + metaDada + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaDadaEntity entity = entityComprovarHelper.comprovarMetaDada(entitat, metaDada.getId());
		entity.update(
				metaDada.getCodi(),
				metaDada.getNom(),
				metaDada.getDescripcio(),
				MetaDadaTipusEnum.valueOf(metaDada.getTipus().name()),
				metaDada.isGlobalExpedient(),
				metaDada.isGlobalDocument(),
				MultiplicitatEnum.valueOf(metaDada.getGlobalMultiplicitat().name()),
				metaDada.isGlobalReadOnly(),
				entitat);
		return conversioTipusHelper.convertir(
				entity,
				MetaDadaDto.class);
	}

	@Transactional
	@Override
	public MetaDadaDto updateActiva(
			Long entitatId,
			Long id,
			boolean activa) {
		logger.debug("Actualitzant propietat activa de la meta-dada ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ","
				+ "activa=" + activa + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaDadaEntity metaDada = entityComprovarHelper.comprovarMetaDada(entitat, id);
		metaDada.updateActiva(activa);
		return conversioTipusHelper.convertir(
				metaDada,
				MetaDadaDto.class);
	}

	@Transactional
	@Override
	public MetaDadaDto delete(
			Long entitatId,
			Long id) {
		logger.debug("Esborrant meta-dada (id=" + id +  ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaDadaEntity metaDada = entityComprovarHelper.comprovarMetaDada(entitat, id);
		metaDadaRepository.delete(metaDada);
		return conversioTipusHelper.convertir(
				metaDada,
				MetaDadaDto.class);
	}

	@Transactional(readOnly=true)
	@Override
	public MetaDadaDto findById(
			Long entitatId,
			Long id) {
		logger.debug("Consulta de la meta-dada (id=" + id + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaDadaEntity metaDada = entityComprovarHelper.comprovarMetaDada(entitat, id);
		return conversioTipusHelper.convertir(
				metaDada,
				MetaDadaDto.class);
	}

	@Transactional(readOnly=true)
	@Override
	public MetaDadaDto findByEntitatCodi(
			Long entitatId,
			String codi) {
		logger.debug("Consulta de la meta-dada per entitat i codi ("
				+ "entitatId=" + entitatId + ", "
				+ "codi=" + codi + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		return conversioTipusHelper.convertir(
				metaDadaRepository.findByEntitatAndCodi(entitat, codi),
				MetaDadaDto.class);
	}

	@Transactional(readOnly=true)
	@Override
	public PaginaDto<MetaDadaDto> findAllByEntitatPaginat(
			Long entitatId,
			PaginacioParamsDto paginacioParams) {
		logger.debug("Consulta paginada de les meta-dades de l'entitat (entitatId=" + entitatId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		PaginaDto<MetaDadaDto> resposta;
		if (paginacioHelper.esPaginacioActivada(paginacioParams)) {
			resposta = paginacioHelper.toPaginaDto(
					metaDadaRepository.findByEntitat(
							entitat,
							paginacioHelper.toSpringDataPageable(paginacioParams)),
					MetaDadaDto.class);
		} else {
			resposta = paginacioHelper.toPaginaDto(
					metaDadaRepository.findByEntitat(
							entitat,
							paginacioHelper.toSpringDataSort(paginacioParams)),
					MetaDadaDto.class);
		}
		return resposta;
	}

	@Transactional(readOnly=true)
	@Override
	public List<MetaDadaDto> findActiveByEntitat(
			Long entitatId,
			boolean incloureGlobalsExpedient,
			boolean incloureGlobalsDocument) {
		logger.debug("Consulta de les meta-dades de l'entitat (entitatId=" + entitatId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		return conversioTipusHelper.convertirList(
				metaDadaRepository.findByEntitatAndActivaTrueAndGlobalsOrderByNomAsc(
						entitat,
						incloureGlobalsExpedient,
						incloureGlobalsDocument),
				MetaDadaDto.class);
	}

	@Transactional(readOnly=true)
	@Override
	public List<MetaDadaDto> findByNodePerCreacio(
			Long entitatId,
			Long nodeId) {
		logger.debug("Consulta de les meta-dades candidates a afegir pel node ("
				+ "entitatId=" + entitatId + ", "
				+ "nodeId=" + nodeId + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		NodeEntity node = entityComprovarHelper.comprovarNode(entitat, nodeId);
		List<MetaDadaEntity> metaDades = new ArrayList<MetaDadaEntity>();
		// De les meta-dades actives pel meta-node només deixa les que encara
		// es poden afegir al node especificat segons la multiplicitat.
		List<MetaNodeMetaDadaEntity> metaNodeMetaDades = metaNodeMetaDadaRepository.findByMetaNodeAndActivaTrue(node.getMetaNode());
		List<DadaEntity> dades = dadaRepository.findByNode(node);
		for (MetaNodeMetaDadaEntity metaNodeMetaDada: metaNodeMetaDades) {
			boolean afegir = true;
			for (DadaEntity dada: dades) {
				if (dada.getMetaDada().equals(metaNodeMetaDada.getMetaDada())) {
					if (metaNodeMetaDada.getMultiplicitat().equals(MultiplicitatEnum.M_0_1) || metaNodeMetaDada.getMultiplicitat().equals(MultiplicitatEnum.M_1))
						afegir = false;
					break;
				}
			}
			if (afegir)
				metaDades.add(metaNodeMetaDada.getMetaDada());
		}
		// Afegeix les meta-dades globals actives
		List<MetaDadaEntity> metaDadesGlobals = null;
		if (node instanceof ExpedientEntity) {
			metaDadesGlobals = metaDadaRepository.findByEntitatAndGlobalExpedientTrueAndActivaTrueOrderByNomAsc(
							entitat);
		}
		if (node instanceof DocumentEntity) {
			metaDadesGlobals = metaDadaRepository.findByEntitatAndGlobalDocumentTrueAndActivaTrueOrderByNomAsc(
							entitat);
		}
		if (metaDadesGlobals != null) {
			for (MetaDadaEntity metaDada: metaDadesGlobals) {
				boolean afegir = true;
				for (DadaEntity dada: dades) {
					if (dada.getMetaDada().equals(metaDada)) {
						if (metaDada.getGlobalMultiplicitat().equals(MultiplicitatEnum.M_0_1) || metaDada.getGlobalMultiplicitat().equals(MultiplicitatEnum.M_1))
							afegir = false;
						break;
					}
				}
				if (afegir)
					metaDades.add(metaDada);
			}
		}
		return conversioTipusHelper.convertirList(
				metaDades,
				MetaDadaDto.class);
	}



	private static final Logger logger = LoggerFactory.getLogger(MetaDadaServiceImpl.class);

}
