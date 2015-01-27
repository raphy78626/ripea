/**
 * 
 */
package es.caib.ripea.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.MetaExpedientDto;
import es.caib.ripea.core.api.dto.MetaExpedientMetaDocumentDto;
import es.caib.ripea.core.api.dto.MetaNodeMetaDadaDto;
import es.caib.ripea.core.api.dto.MultiplicitatEnumDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.MetaDadaNotFoundException;
import es.caib.ripea.core.api.exception.MetaDocumentNotFoundException;
import es.caib.ripea.core.api.exception.MetaExpedientNotFoundException;
import es.caib.ripea.core.api.service.MetaExpedientService;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.MetaDadaEntity;
import es.caib.ripea.core.entity.MetaDocumentEntity;
import es.caib.ripea.core.entity.MetaExpedientEntity;
import es.caib.ripea.core.entity.MetaExpedientMetaDocumentEntity;
import es.caib.ripea.core.entity.MetaNodeEntity;
import es.caib.ripea.core.entity.MetaNodeMetaDadaEntity;
import es.caib.ripea.core.entity.MultiplicitatEnum;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.MetaNodeHelper;
import es.caib.ripea.core.helper.PaginacioHelper;
import es.caib.ripea.core.helper.PermisosComprovacioHelper;
import es.caib.ripea.core.helper.PermisosHelper;
import es.caib.ripea.core.helper.PermisosHelper.ObjectIdentifierExtractor;
import es.caib.ripea.core.repository.EntitatRepository;
import es.caib.ripea.core.repository.MetaDadaRepository;
import es.caib.ripea.core.repository.MetaDocumentRepository;
import es.caib.ripea.core.repository.MetaExpedientMetaDocumentRepository;
import es.caib.ripea.core.repository.MetaExpedientRepository;
import es.caib.ripea.core.repository.MetaNodeMetaDadaRepository;
import es.caib.ripea.core.security.ExtendedPermission;

/**
 * Implementació del servei de gestió de meta-expedients.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class MetaExpedientServiceImpl implements MetaExpedientService {

	@Resource
	private MetaExpedientRepository metaExpedientRepository;
	@Resource
	private EntitatRepository entitatRepository;
	@Resource
	private MetaDadaRepository metaDadaRepository;
	@Resource
	private MetaDocumentRepository metaDocumentRepository;
	@Resource
	private MetaExpedientMetaDocumentRepository metaExpedientMetaDocumentRepository;
	@Resource
	private MetaNodeMetaDadaRepository metaNodeMetaDadaRepository;

	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private MetaNodeHelper metaNodeHelper;
	@Resource
	private PaginacioHelper paginacioHelper;
	@Resource
	private PermisosHelper permisosHelper;
	@Resource
	private PermisosComprovacioHelper permisosComprovacioHelper;



	@Transactional
	@Override
	public MetaExpedientDto create(
			Long entitatId,
			MetaExpedientDto metaExpedient) throws EntitatNotFoundException, MetaExpedientNotFoundException {
		logger.debug("Creant un nou meta-expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "metaExpedient=" + metaExpedient + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaExpedientEntity metaExpedientPare = null;
		if (metaExpedient.getPareId() != null) {
			metaExpedientPare = comprovarMetaExpedient(
					entitat,
					metaExpedient.getPareId());
		}
		MetaExpedientEntity entity = MetaExpedientEntity.getBuilder(
				metaExpedient.getCodi(),
				metaExpedient.getNom(),
				metaExpedient.getDescripcio(),
				metaExpedient.getClassificacio(),
				entitat,
				metaExpedientPare).build();
		return conversioTipusHelper.convertir(
				metaExpedientRepository.save(entity),
				MetaExpedientDto.class);
	}

	@Transactional
	@Override
	public MetaExpedientDto update(
			Long entitatId,
			MetaExpedientDto metaExpedient) throws EntitatNotFoundException, MetaExpedientNotFoundException {
		logger.debug("Actualitzant meta-expedient existent ("
				+ "entitatId=" + entitatId + ", "
				+ "metaExpedient=" + metaExpedient + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaExpedientEntity metaExpedientEntitiy = comprovarMetaExpedient(
				entitat,
				metaExpedient.getId());
		MetaExpedientEntity metaExpedientPare = null;
		if (metaExpedient.getPareId() != null) {
			metaExpedientPare = comprovarMetaExpedient(
					entitat,
					metaExpedient.getPareId());
		}
		metaExpedientEntitiy.update(
				metaExpedient.getCodi(),
				metaExpedient.getNom(),
				metaExpedient.getDescripcio(),
				metaExpedient.getClassificacio(),
				metaExpedientPare);
		return conversioTipusHelper.convertir(
				metaExpedientEntitiy,
				MetaExpedientDto.class);
	}

	@Transactional
	@Override
	public MetaExpedientDto updateActiu(
			Long entitatId,
			Long id,
			boolean actiu) throws EntitatNotFoundException {
		logger.debug("Actualitzant propietat activa d'un meta-expedient existent ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "actiu=" + actiu + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaExpedientEntity metaExpedient = comprovarMetaExpedient(
				entitat,
				id);
		metaExpedient.updateActiu(actiu);
		return conversioTipusHelper.convertir(
				metaExpedient,
				MetaExpedientDto.class);
	}

	@Transactional
	@Override
	public MetaExpedientDto delete(
			Long entitatId,
			Long id) throws EntitatNotFoundException, MetaExpedientNotFoundException {
		logger.debug("Esborrant meta-expedient (id=" + id +  ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaExpedientEntity metaExpedient = comprovarMetaExpedient(
				entitat,
				id);
		metaExpedientRepository.delete(metaExpedient);
		return conversioTipusHelper.convertir(
				metaExpedient,
				MetaExpedientDto.class);
	}

	@Transactional(readOnly = true)
	@Override
	public MetaExpedientDto findById(
			Long entitatId,
			Long id) throws EntitatNotFoundException {
		logger.debug("Consulta del meta-expedient ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaExpedientEntity metaExpedient = comprovarMetaExpedient(entitat, id);
		MetaExpedientDto resposta = conversioTipusHelper.convertir(
				metaExpedient,
				MetaExpedientDto.class);
		if (resposta != null) {
			metaNodeHelper.omplirMetaDadesPerMetaNode(resposta);
			metaNodeHelper.omplirPermisosPerMetaNode(resposta, false);
			omplirMetaDocumentsPerMetaExpedient(
					metaExpedient,
					resposta);
		}
		return resposta;
	}

	@Transactional(readOnly = true)
	@Override
	public MetaExpedientDto findByEntitatCodi(
			Long entitatId,
			String codi) throws EntitatNotFoundException {
		logger.debug("Consulta del meta-expedient per entitat i codi ("
				+ "entitatId=" + entitatId + ", "
				+ "codi=" + codi + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaExpedientEntity metaExpedient = metaExpedientRepository.findByEntitatAndCodi(
				entitat,
				codi);
		MetaExpedientDto resposta = conversioTipusHelper.convertir(
				metaExpedient,
				MetaExpedientDto.class);
		if (resposta != null) {
			metaNodeHelper.omplirMetaDadesPerMetaNode(resposta);
			metaNodeHelper.omplirPermisosPerMetaNode(resposta, false);
			omplirMetaDocumentsPerMetaExpedient(
					metaExpedient,
					resposta);
		}
		return resposta;
	}

	@Transactional(readOnly = true)
	@Override
	public PaginaDto<MetaExpedientDto> findByEntitatPaginat(
			Long entitatId,
			PaginacioParamsDto paginacioParams) throws EntitatNotFoundException {
		logger.debug("Consulta paginada dels meta-expedients de l'entitat (entitatId=" + entitatId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		PaginaDto<MetaExpedientDto> resposta;
		if (paginacioHelper.esPaginacioActivada(paginacioParams)) {
			resposta = paginacioHelper.toPaginaDto(
					metaExpedientRepository.findByEntitat(
							entitat,
							paginacioHelper.toSpringDataPageable(paginacioParams)),
					MetaExpedientDto.class);
		} else {
			resposta = paginacioHelper.toPaginaDto(
					metaExpedientRepository.findByEntitat(
							entitat,
							paginacioHelper.toSpringDataSort(paginacioParams)),
					MetaExpedientDto.class);
		}
		metaNodeHelper.omplirMetaDadesPerMetaNodes(resposta.getContingut());
		omplirMetaDocumentsPerMetaExpedients(resposta.getContingut());
		metaNodeHelper.omplirPermisosPerMetaNodes(
				resposta.getContingut(),
				true);
		return resposta;
	}

	@Transactional
	@Override
	public void metaDadaCreate(
			Long entitatId,
			Long id,
			Long metaDadaId,
			MultiplicitatEnumDto multiplicitat,
			boolean readOnly) throws EntitatNotFoundException, MetaExpedientNotFoundException, MetaDadaNotFoundException {
		logger.debug("Afegint meta-dada al meta-expedient ("
				+ "entitatId=" + entitatId +  ", "
				+ "id=" + id +  ", "
				+ "metaDadaId=" + metaDadaId +  ","
				+ "multiplicitat=" + multiplicitat +  ", "
				+ "readOnly=" + readOnly +  ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaExpedientEntity metaExpedient = comprovarMetaExpedient(
				entitat,
				id);
		MetaDadaEntity metaDada = comprovarMetaDada(
				entitat,
				metaDadaId);
		metaExpedient.metaDadaAdd(
				metaDada,
				MultiplicitatEnum.valueOf(multiplicitat.name()),
				readOnly);
	}

	@Transactional
	@Override
	public void metaDadaUpdate(
			Long entitatId,
			Long id,
			Long metaNodeMetaDadaId,
			MultiplicitatEnumDto multiplicitat,
			boolean readOnly) throws EntitatNotFoundException, MetaExpedientNotFoundException, MetaDadaNotFoundException {
		logger.debug("Actualitzant meta-dada del meta-expedient ("
				+ "entitatId=" + entitatId +  ", "
				+ "id=" + id +  ", "
				+ "metaNodeMetaDadaId=" + metaNodeMetaDadaId +  ","
				+ "multiplicitat=" + multiplicitat +  ", "
				+ "readOnly=" + readOnly +  ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaExpedientEntity metaExpedient = comprovarMetaExpedient(
				entitat,
				id);
		MetaNodeMetaDadaEntity metaNodeMetaDada = comprovarMetaNodeMetaDada(
				entitat,
				metaExpedient,
				metaNodeMetaDadaId);
		metaNodeMetaDada.update(
				MultiplicitatEnum.valueOf(multiplicitat.name()),
				readOnly);
	}

	@Transactional
	@Override
	public void metaDadaDelete(
			Long entitatId,
			Long id,
			Long metaExpedientMetaDadaId) throws EntitatNotFoundException, MetaExpedientNotFoundException, MetaDadaNotFoundException {
		logger.debug("Esborrant meta-dada al meta-expedient ("
				+ "entitatId=" + entitatId +  ", "
				+ "id=" + id +  ", "
				+ "metaExpedientMetaDadaId=" + metaExpedientMetaDadaId +  ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaExpedientEntity metaExpedient = comprovarMetaExpedient(
				entitat,
				id);
		MetaNodeMetaDadaEntity metaExpedientMetaDada = comprovarMetaNodeMetaDada(
				entitat,
				metaExpedient,
				metaExpedientMetaDadaId);
		metaExpedient.metaDadaDelete(metaExpedientMetaDada);
		metaNodeHelper.reordenarMetaDades(metaExpedient);
	}

	@Transactional
	@Override
	public void metaDadaMove(
			Long entitatId,
			Long id,
			Long metaExpedientMetaDadaId,
			int posicio) throws EntitatNotFoundException, MetaExpedientNotFoundException, MetaDadaNotFoundException {
		logger.debug("Movent meta-dada al meta-expedient ("
				+ "entitatId=" + entitatId +  ", "
				+ "id=" + id +  ", "
				+ "metaExpedientMetaDadaId=" + metaExpedientMetaDadaId +  ", "
				+ "posicio=" + posicio +  ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaExpedientEntity metaExpedient = comprovarMetaExpedient(
				entitat,
				id);
		MetaNodeMetaDadaEntity metaExpedientMetaDada = comprovarMetaNodeMetaDada(
				entitat,
				metaExpedient,
				metaExpedientMetaDadaId);
		metaNodeHelper.moureMetaNodeMetaDada(
				metaExpedient,
				metaExpedientMetaDada,
				posicio);
	}

	@Transactional(readOnly = true)
	@Override
	public MetaNodeMetaDadaDto findMetaDada(
			Long entitatId,
			Long id,
			Long metaNodeMetaDadaId) throws EntitatNotFoundException, MetaExpedientNotFoundException, MetaDadaNotFoundException {
		logger.debug("Cercant la meta-dada del meta-expedient ("
				+ "entitatId=" + entitatId +  ", "
				+ "id=" + id +  ", "
				+ "metaNodeMetaDadaId=" + metaNodeMetaDadaId +  ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaExpedientEntity metaExpedient = comprovarMetaExpedient(
				entitat,
				id);
		MetaNodeMetaDadaEntity metaNodeMetaDada = comprovarMetaNodeMetaDada(
				entitat,
				metaExpedient,
				metaNodeMetaDadaId);
		return conversioTipusHelper.convertir(
				metaNodeMetaDada,
				MetaNodeMetaDadaDto.class);
	}

	@Transactional
	@Override
	public void metaDocumentCreate(
			Long entitatId,
			Long id,
			Long metaDocumentId,
			MultiplicitatEnumDto multiplicitat,
			boolean readOnly) throws EntitatNotFoundException, MetaExpedientNotFoundException, MetaDocumentNotFoundException {
		logger.debug("Afegint meta-document al meta-expedient ("
				+ "entitatId=" + entitatId +  ", "
				+ "id=" + id +  ", "
				+ "metaDocumentId=" + metaDocumentId +  ", "
				+ "multiplicitat=" + multiplicitat +  ", "
				+ "readOnly=" + readOnly +  ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaExpedientEntity metaExpedient = comprovarMetaExpedient(
				entitat,
				id);
		MetaDocumentEntity metaDocument = comprovarMetaDocument(
				entitat,
				metaDocumentId);
		metaExpedient.metaDocumentAdd(
				metaDocument,
				MultiplicitatEnum.valueOf(multiplicitat.toString()),
				readOnly);
	}

	@Transactional
	@Override
	public void metaDocumentUpdate(
			Long entitatId,
			Long id,
			Long metaExpedientMetaDocumentId,
			MultiplicitatEnumDto multiplicitat,
			boolean readOnly) throws EntitatNotFoundException, MetaExpedientNotFoundException, MetaDadaNotFoundException {
		logger.debug("Actualitzant meta-document del meta-expedient ("
				+ "entitatId=" + entitatId +  ", "
				+ "id=" + id +  ", "
				+ "metaNodeMetaDadaId=" + metaExpedientMetaDocumentId +  ","
				+ "multiplicitat=" + multiplicitat +  ", "
				+ "readOnly=" + readOnly +  ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaExpedientEntity metaExpedient = comprovarMetaExpedient(
				entitat,
				id);
		MetaExpedientMetaDocumentEntity metaExpedientMetaDocument = comprovarMetaExpedientMetaDocument(
				entitat,
				metaExpedient,
				metaExpedientMetaDocumentId);
		metaExpedientMetaDocument.update(
				MultiplicitatEnum.valueOf(multiplicitat.name()),
				readOnly);
	}

	@Transactional
	@Override
	public void metaDocumentDelete(
			Long entitatId,
			Long id,
			Long metaExpedientMetaDocumentId) throws EntitatNotFoundException, MetaExpedientNotFoundException, MetaDocumentNotFoundException {
		logger.debug("Afegint meta-document al meta-expedient ("
				+ "entitatId=" + entitatId +  ", "
				+ "id=" + id +  ", "
				+ "metaExpedientMetaDocumentId=" + metaExpedientMetaDocumentId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaExpedientEntity metaExpedient = comprovarMetaExpedient(
				entitat,
				id);
		MetaExpedientMetaDocumentEntity metaExpedientMetaDocument = comprovarMetaExpedientMetaDocument(
				entitat,
				metaExpedient,
				metaExpedientMetaDocumentId);
		metaExpedient.metaDocumentDelete(metaExpedientMetaDocument);
		reordenarMetaDocuments(metaExpedient);
	}

	@Transactional
	@Override
	public void metaDocumentMove(
			Long entitatId,
			Long id,
			Long metaExpedientMetaDocumentId,
			int posicio) throws EntitatNotFoundException, MetaExpedientNotFoundException, MetaDocumentNotFoundException {
		logger.debug("Movent meta-document al meta-expedient ("
				+ "entitatId=" + entitatId +  ", "
				+ "id=" + id +  ", "
				+ "metaExpedientMetaDocumentId=" + metaExpedientMetaDocumentId +  ", "
				+ "posicio=" + posicio +  ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaExpedientEntity metaExpedient = comprovarMetaExpedient(
				entitat,
				id);
		MetaExpedientMetaDocumentEntity metaExpedientMetaDocument = comprovarMetaExpedientMetaDocument(
				entitat,
				metaExpedient,
				metaExpedientMetaDocumentId);
		moureMetaDocument(
				metaExpedient,
				metaExpedientMetaDocument,
				posicio);
	}

	@Transactional(readOnly = true)
	@Override
	public MetaExpedientMetaDocumentDto findMetaDocument(
			Long entitatId,
			Long id,
			Long metaExpedientMetaDocumentId) throws EntitatNotFoundException, MetaExpedientNotFoundException, MetaDocumentNotFoundException {
		logger.debug("Cercant el meta-document del meta-expedient ("
				+ "entitatId=" + entitatId +  ", "
				+ "id=" + id +  ", "
				+ "metaExpedientMetaDocumentId=" + metaExpedientMetaDocumentId +  ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaExpedientEntity metaExpedient = comprovarMetaExpedient(
				entitat,
				id);
		MetaExpedientMetaDocumentEntity metaExpedientMetaDocument = comprovarMetaExpedientMetaDocument(
				entitat,
				metaExpedient,
				metaExpedientMetaDocumentId);
		return conversioTipusHelper.convertir(
				metaExpedientMetaDocument,
				MetaExpedientMetaDocumentDto.class);
	}

	@Transactional
	@Override
	public List<PermisDto> findPermis(
			Long entitatId,
			Long id) throws EntitatNotFoundException, MetaExpedientNotFoundException {
		logger.debug("Consulta dels permisos del meta-expedient ("
				+ "entitatId=" + entitatId +  ", "
				+ "id=" + id +  ")"); 
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		comprovarMetaExpedient(
				entitat,
				id);
		return permisosHelper.findPermisos(
				id,
				MetaNodeEntity.class);
	}

	@Transactional
	@Override
	public void updatePermis(
			Long entitatId,
			Long id,
			PermisDto permis) throws EntitatNotFoundException, MetaExpedientNotFoundException {
		logger.debug("Modificació del permis del meta-expedient ("
				+ "entitatId=" + entitatId +  ", "
				+ "id=" + id + ", "
				+ "permis=" + permis + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		comprovarMetaExpedient(
				entitat,
				id);
		permisosHelper.updatePermis(
				id,
				MetaNodeEntity.class,
				permis);
	}

	@Transactional
	@Override
	public void deletePermis(
			Long entitatId,
			Long id,
			Long permisId) throws EntitatNotFoundException, MetaExpedientNotFoundException {
		logger.debug("Eliminació del permis del meta-expedient ("
				+ "entitatId=" + entitatId +  ", "
				+ "id=" + id + ", "
				+ "permisId=" + permisId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		comprovarMetaExpedient(
				entitat,
				id);
		permisosHelper.deletePermis(
				id,
				MetaNodeEntity.class,
				permisId);
	}

	@Transactional(readOnly = true)
	@Override
	public List<MetaExpedientDto> findByEntitat(
			Long entitatId) throws EntitatNotFoundException {
		logger.debug("Consulta de meta-expedients de l'entitat ("
				+ "entitatId=" + entitatId +  ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		List<MetaExpedientEntity> metaExpedients = metaExpedientRepository.findByEntitatOrderByNomAsc(entitat);
		return conversioTipusHelper.convertirList(
				metaExpedients,
				MetaExpedientDto.class);
	}

	@Transactional(readOnly = true)
	@Override
	public List<MetaExpedientDto> findActiveByEntitatPerCreacio(
			Long entitatId) throws EntitatNotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Consulta de meta-expedients actius de l'entitat amb el permis CREATE ("
				+ "entitatId=" + entitatId +  ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		List<MetaExpedientEntity> metaExpedients = metaExpedientRepository.findByEntitatAndActiuTrueOrderByNomAsc(entitat);
		permisosHelper.filterGrantedAll(
				metaExpedients,
				new ObjectIdentifierExtractor<MetaNodeEntity>() {
					public Long getObjectIdentifier(MetaNodeEntity metaNode) {
						return metaNode.getId();
					}
				},
				MetaNodeEntity.class,
				new Permission[] {ExtendedPermission.CREATE},
				auth);
		return conversioTipusHelper.convertirList(
				metaExpedients,
				MetaExpedientDto.class);
	}

	@Transactional(readOnly = true)
	@Override
	public List<MetaExpedientDto> findByEntitatPerLectura(
			Long entitatId) throws EntitatNotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("Consulta de meta-expedients de l'entitat amb el permis READ ("
				+ "entitatId=" + entitatId +  ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		List<MetaExpedientEntity> metaExpedients = metaExpedientRepository.findByEntitatOrderByNomAsc(entitat);
		permisosHelper.filterGrantedAll(
				metaExpedients,
				new ObjectIdentifierExtractor<MetaNodeEntity>() {
					public Long getObjectIdentifier(MetaNodeEntity metaNode) {
						return metaNode.getId();
					}
				},
				MetaNodeEntity.class,
				new Permission[] {ExtendedPermission.READ},
				auth);
		return conversioTipusHelper.convertirList(
				metaExpedients,
				MetaExpedientDto.class);
	}



	private MetaExpedientEntity comprovarMetaExpedient(
			EntitatEntity entitat,
			Long id) throws MetaExpedientNotFoundException {
		MetaExpedientEntity metaExpedient = metaExpedientRepository.findOne(
				id);
		if (metaExpedient == null) {
			logger.error("No s'ha trobat el meta-expedient (id=" + id + ")");
			throw new MetaExpedientNotFoundException();
		}
		if (!entitat.equals(metaExpedient.getEntitat())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat del meta-expedient ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + metaExpedient.getEntitat().getId() + ")");
			throw new MetaExpedientNotFoundException();
		}
		return metaExpedient;
	}
	private MetaDadaEntity comprovarMetaDada(
			EntitatEntity entitat,
			Long metaDadaId) throws MetaDadaNotFoundException {
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
			MetaExpedientEntity metaExpedient,
			Long metaNodeMetaDadaId) throws MetaDadaNotFoundException {
		MetaNodeMetaDadaEntity metaNodeMetaDada = metaNodeMetaDadaRepository.findOne(metaNodeMetaDadaId);
		if (metaNodeMetaDada == null) {
			logger.error("No s'ha trobat la meta-dada del meta-node (metaNodeMetaDadaId=" + metaNodeMetaDadaId + ")");
			throw new MetaDadaNotFoundException();
		}
		if (!metaNodeMetaDada.getMetaNode().equals(metaExpedient)) {
			logger.error("El meta-node de la meta-dada no coincideix amb el meta-expedient ("
					+ "metaNodeId1=" + metaExpedient.getId() + ", "
					+ "metaNodeId2=" + metaNodeMetaDada.getMetaNode().getId() + ")");
			throw new MetaDadaNotFoundException();
		}
		return metaNodeMetaDada;
	}
	private MetaDocumentEntity comprovarMetaDocument(
			EntitatEntity entitat,
			Long metaDocumentId) throws MetaDocumentNotFoundException {
		MetaDocumentEntity metaDocument = metaDocumentRepository.findOne(metaDocumentId);
		if (metaDocument == null) {
			logger.error("No s'ha trobat el meta-document (metaDocumentId=" + metaDocumentId + ")");
			throw new MetaDocumentNotFoundException();
		}
		if (!entitat.equals(metaDocument.getEntitat())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat del meta-document ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + metaDocument.getEntitat().getId() + ")");
			throw new MetaDocumentNotFoundException();
		}
		return metaDocument;
	}
	private MetaExpedientMetaDocumentEntity comprovarMetaExpedientMetaDocument(
			EntitatEntity entitat,
			MetaExpedientEntity metaExpedient,
			Long metaExpedientMetaDocumentId) throws MetaDadaNotFoundException {
		MetaExpedientMetaDocumentEntity metaExpedientMetaDocument = metaExpedientMetaDocumentRepository.findOne(metaExpedientMetaDocumentId);
		if (metaExpedientMetaDocument == null) {
			logger.error("No s'ha trobat el meta-document del meta-expedient (metaExpedientMetaDocumentId=" + metaExpedientMetaDocumentId + ")");
			throw new MetaDocumentNotFoundException();
		}
		if (!metaExpedientMetaDocument.getMetaExpedient().equals(metaExpedient)) {
			logger.error("El meta-expedient del meta-document no coincideix amb el meta-expedient ("
					+ "metaNodeId1=" + metaExpedient.getId() + ", "
					+ "metaNodeId2=" + metaExpedientMetaDocument.getMetaExpedient().getId() + ")");
			throw new MetaDocumentNotFoundException();
		}
		return metaExpedientMetaDocument;
	}

	private void omplirMetaDocumentsPerMetaExpedients(
			List<MetaExpedientDto> metaExpedients) {
		List<Long> metaExpedientIds = new ArrayList<Long>();
		for (MetaExpedientDto metaExpedient: metaExpedients)
			metaExpedientIds.add(metaExpedient.getId());
		List<MetaExpedientMetaDocumentEntity> metaExpedientMetaDocuments = null;
		// Si passam una llista buida dona un error a la consulta.
		if (metaExpedientIds.size() > 0)
			metaExpedientMetaDocuments = metaExpedientMetaDocumentRepository.findByMetaExpedientIdIn(metaExpedientIds);
		for (MetaExpedientDto metaExpedient: metaExpedients) {
			List<MetaExpedientMetaDocumentDto> metaDocuments = new ArrayList<MetaExpedientMetaDocumentDto>();
			if (metaExpedientMetaDocuments != null) {
				for (MetaExpedientMetaDocumentEntity metaExpedientMetaDocument: metaExpedientMetaDocuments) {
					if (metaExpedientMetaDocument.getMetaExpedient().getId().equals(metaExpedient.getId())) {
						metaDocuments.add(conversioTipusHelper.convertir(
								metaExpedientMetaDocument,
								MetaExpedientMetaDocumentDto.class));
					}
				}
			}
			metaExpedient.setMetaDocuments(metaDocuments);
		}
	}
	private void omplirMetaDocumentsPerMetaExpedient(
			MetaExpedientEntity metaExpedient,
			MetaExpedientDto dto) {
		List<MetaExpedientMetaDocumentEntity> metaExpedientMetaDocuments = metaExpedientMetaDocumentRepository.findByMetaExpedient(
				metaExpedient);
		List<MetaExpedientMetaDocumentDto> metaDocuments = new ArrayList<MetaExpedientMetaDocumentDto>();
		for (MetaExpedientMetaDocumentEntity metaExpedientMetaDocument: metaExpedientMetaDocuments) {
			metaDocuments.add(conversioTipusHelper.convertir(
					metaExpedientMetaDocument,
					MetaExpedientMetaDocumentDto.class));
		}
		dto.setMetaDocuments(metaDocuments);
	}
	private void moureMetaDocument(
			MetaExpedientEntity metaExpedient,
			MetaExpedientMetaDocumentEntity metaExpedientMetaDocument,
			int posicio) {
		List<MetaExpedientMetaDocumentEntity> metaExpedientMetaDocuments = metaExpedientMetaDocumentRepository.findByMetaExpedient(
				metaExpedient);
		int indexOrigen = -1;
		for (MetaExpedientMetaDocumentEntity memd: metaExpedientMetaDocuments) {
			if (memd.getId().equals(metaExpedientMetaDocument.getId())) {
				indexOrigen = memd.getOrdre();
				break;
			}
		}
		metaExpedientMetaDocuments.add(
				posicio,
				metaExpedientMetaDocuments.remove(indexOrigen));
		for (int i = 0; i < metaExpedientMetaDocuments.size(); i++)
			metaExpedientMetaDocuments.get(i).updateOrdre(i);
	}
	private void reordenarMetaDocuments(
			MetaExpedientEntity metaExpedient) {
		List<MetaExpedientMetaDocumentEntity> metaExpedientMetaDocuments = metaExpedientMetaDocumentRepository.findByMetaExpedient(
				metaExpedient);
		int ordre = 0;
		for (MetaExpedientMetaDocumentEntity metaExpedientMetaDocument: metaExpedientMetaDocuments)
			metaExpedientMetaDocument.updateOrdre(ordre++);
	}

	private static final Logger logger = LoggerFactory.getLogger(MetaExpedientServiceImpl.class);

}
