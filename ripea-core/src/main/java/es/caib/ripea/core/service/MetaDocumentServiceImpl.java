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

import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.MetaDocumentDto;
import es.caib.ripea.core.api.dto.MetaNodeMetaDadaDto;
import es.caib.ripea.core.api.dto.MultiplicitatEnumDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.dto.PortafirmesDocumentTipusDto;
import es.caib.ripea.core.api.exception.ContenidorNotFoundException;
import es.caib.ripea.core.api.exception.EntitatNotFoundException;
import es.caib.ripea.core.api.exception.MetaDadaNotFoundException;
import es.caib.ripea.core.api.exception.MetaDocumentNotFoundException;
import es.caib.ripea.core.api.exception.MetaExpedientNotFoundException;
import es.caib.ripea.core.api.service.MetaDocumentService;
import es.caib.ripea.core.entity.ContenidorEntity;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.MetaDadaEntity;
import es.caib.ripea.core.entity.MetaDocumentEntity;
import es.caib.ripea.core.entity.MetaExpedientMetaDocumentEntity;
import es.caib.ripea.core.entity.MetaNodeEntity;
import es.caib.ripea.core.entity.MetaNodeMetaDadaEntity;
import es.caib.ripea.core.entity.MultiplicitatEnum;
import es.caib.ripea.core.helper.ContenidorHelper;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.MetaNodeHelper;
import es.caib.ripea.core.helper.PaginacioHelper;
import es.caib.ripea.core.helper.PermisosComprovacioHelper;
import es.caib.ripea.core.helper.PermisosHelper;
import es.caib.ripea.core.helper.PermisosHelper.ObjectIdentifierExtractor;
import es.caib.ripea.core.helper.PluginHelper;
import es.caib.ripea.core.repository.ContenidorRepository;
import es.caib.ripea.core.repository.DocumentRepository;
import es.caib.ripea.core.repository.EntitatRepository;
import es.caib.ripea.core.repository.MetaDadaRepository;
import es.caib.ripea.core.repository.MetaDocumentRepository;
import es.caib.ripea.core.repository.MetaExpedientMetaDocumentRepository;
import es.caib.ripea.core.repository.MetaNodeMetaDadaRepository;
import es.caib.ripea.core.security.ExtendedPermission;

/**
 * Implementació del servei de gestió de meta-documents.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class MetaDocumentServiceImpl implements MetaDocumentService {

	@Resource
	private MetaDocumentRepository metaDocumentRepository;
	@Resource
	private EntitatRepository entitatRepository;
	@Resource
	private MetaDadaRepository metaDadaRepository;
	@Resource
	private MetaNodeMetaDadaRepository metaNodeMetaDadaRepository;
	@Resource
	private ContenidorRepository contenidorRepository;
	@Resource
	private MetaExpedientMetaDocumentRepository metaExpedientMetaDocumentRepository;
	@Resource
	private DocumentRepository documentRepository;

	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private MetaNodeHelper metaNodeHelper;
	@Resource
	private ContenidorHelper contenidorHelper;
	@Resource
	private PaginacioHelper paginacioHelper;
	@Resource
	private PermisosHelper permisosHelper;
	@Resource
	private PluginHelper pluginHelper;
	@Resource
	private PermisosComprovacioHelper permisosComprovacioHelper;



	@Transactional
	@Override
	public MetaDocumentDto create(
			Long entitatId,
			MetaDocumentDto metaDocument,
			String plantillaNom,
			String plantillaContentType,
			byte[] plantillaContingut) throws EntitatNotFoundException {
		logger.debug("Creant un nou meta-document ("
				+ "entitatId=" + entitatId + ", "
				+ "metaDocument=" + metaDocument + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaDocumentEntity entity = MetaDocumentEntity.getBuilder(
				entitat,
				metaDocument.getCodi(),
				metaDocument.getNom()).
				descripcio(metaDocument.getDescripcio()).
				globalExpedient(metaDocument.isGlobalExpedient()).
				globalMultiplicitat(MultiplicitatEnum.valueOf(metaDocument.getGlobalMultiplicitat().name())).
				globalReadOnly(metaDocument.isGlobalReadOnly()).
				custodiaPolitica(metaDocument.getCustodiaPolitica()).
				portafirmesDocumentTipus(metaDocument.getPortafirmesDocumentTipus()).
				portafirmesFluxId(metaDocument.getPortafirmesFluxId()).
				portafirmesResponsables(metaDocument.getPortafirmesResponsables()).
				portafirmesFluxTipus(metaDocument.getPortafirmesFluxTipus()).
				signaturaTipusMime(metaDocument.getSignaturaTipusMime()).
				build();
		if (plantillaContingut != null) {
			entity.updatePlantilla(
					plantillaNom,
					plantillaContentType,
					plantillaContingut);
		}
		return conversioTipusHelper.convertir(
				metaDocumentRepository.save(entity),
				MetaDocumentDto.class);
	}

	@Transactional
	@Override
	public MetaDocumentDto update(
			Long entitatId,
			MetaDocumentDto metaDocument,
			String plantillaNom,
			String plantillaContentType,
			byte[] plantillaContingut) throws EntitatNotFoundException, MetaDocumentNotFoundException {
		logger.debug("Actualitzant meta-document existent ("
				+ "entitatId=" + entitatId + ", "
				+ "metaDocument=" + metaDocument + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaDocumentEntity metaDocumentEntitiy = comprovarMetaDocument(
				entitat,
				metaDocument.getId());
		metaDocumentEntitiy.update(
				metaDocument.getCodi(),
				metaDocument.getNom(),
				metaDocument.getDescripcio(),
				metaDocument.isGlobalExpedient(),
				MultiplicitatEnum.valueOf(metaDocument.getGlobalMultiplicitat().name()),
				metaDocument.isGlobalReadOnly(),
				metaDocument.getCustodiaPolitica(),
				metaDocument.getPortafirmesDocumentTipus(),
				metaDocument.getPortafirmesFluxId(),
				metaDocument.getPortafirmesResponsables(),
				metaDocument.getPortafirmesFluxTipus(),
				metaDocument.getSignaturaTipusMime());
		if (plantillaContingut != null) {
			metaDocumentEntitiy.updatePlantilla(
					plantillaNom,
					plantillaContentType,
					plantillaContingut);
		}
		return conversioTipusHelper.convertir(
				metaDocumentEntitiy,
				MetaDocumentDto.class);
	}

	@Transactional
	@Override
	public MetaDocumentDto updateActiu(
			Long entitatId,
			Long id,
			boolean actiu) throws EntitatNotFoundException {
		logger.debug("Actualitzant propietat activa d'un meta-document existent ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ", "
				+ "actiu=" + actiu + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaDocumentEntity metaDocument = comprovarMetaDocument(
				entitat,
				id);
		metaDocument.updateActiu(actiu);
		return conversioTipusHelper.convertir(
				metaDocument,
				MetaDocumentDto.class);
	}

	@Transactional
	@Override
	public MetaDocumentDto delete(
			Long entitatId,
			Long id) throws EntitatNotFoundException, MetaDocumentNotFoundException {
		logger.debug("Esborrant meta-document (id=" + id +  ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaDocumentEntity metaDocumentEntitiy = comprovarMetaDocument(
				entitat,
				id);
		metaDocumentRepository.delete(metaDocumentEntitiy);
		return conversioTipusHelper.convertir(
				metaDocumentEntitiy,
				MetaDocumentDto.class);
	}

	@Transactional(readOnly=true)
	@Override
	public MetaDocumentDto findById(
			Long entitatId,
			Long id) throws EntitatNotFoundException {
		logger.debug("Consulta del meta-document ("
				+ "entitatId=" + entitatId + ", "
				+ "id=" + id + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaDocumentEntity metaDocumentEntitiy = comprovarMetaDocument(
				entitat,
				id);
		MetaDocumentDto resposta = conversioTipusHelper.convertir(
				metaDocumentEntitiy,
				MetaDocumentDto.class);
		if (resposta != null) {
			metaNodeHelper.omplirMetaDadesPerMetaNode(resposta);
			metaNodeHelper.omplirPermisosPerMetaNode(resposta, false);
		}
		return resposta;
	}

	@Transactional(readOnly=true)
	@Override
	public MetaDocumentDto findByEntitatCodi(
			Long entitatId,
			String codi) throws EntitatNotFoundException {
		logger.debug("Consulta del meta-document per entitat i codi ("
				+ "entitatId=" + entitatId + ", "
				+ "codi=" + codi + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaDocumentDto resposta = conversioTipusHelper.convertir(
				metaDocumentRepository.findByEntitatAndCodi(entitat, codi),
				MetaDocumentDto.class);
		if (resposta != null) {
			metaNodeHelper.omplirMetaDadesPerMetaNode(resposta);
			metaNodeHelper.omplirPermisosPerMetaNode(resposta, false);
		}
		return resposta;
	}

	@Transactional(readOnly=true)
	@Override
	public PaginaDto<MetaDocumentDto> findByEntitatPaginat(
			Long entitatId,
			PaginacioParamsDto paginacioParams) throws EntitatNotFoundException {
		logger.debug("Consulta paginada dels meta-documents de l'entitat (entitatId=" + entitatId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		PaginaDto<MetaDocumentDto> resposta;
		if (paginacioHelper.esPaginacioActivada(paginacioParams)) {
			resposta = paginacioHelper.toPaginaDto(
					metaDocumentRepository.findByEntitat(
							entitat,
							paginacioHelper.toSpringDataPageable(paginacioParams)),
					MetaDocumentDto.class);
		} else {
			resposta = paginacioHelper.toPaginaDto(
					metaDocumentRepository.findByEntitat(
							entitat,
							paginacioHelper.toSpringDataSort(paginacioParams)),
					MetaDocumentDto.class);
		}
		metaNodeHelper.omplirMetaDadesPerMetaNodes(resposta.getContingut());
		metaNodeHelper.omplirPermisosPerMetaNodes(
				resposta.getContingut(),
				true);
		return resposta;
	}

	@Transactional(readOnly=true)
	@Override
	public List<MetaDocumentDto> findByEntitat(
			Long entitatId) throws EntitatNotFoundException {
		logger.debug("Consulta de tots els meta-documents de l'entitat ("
				+ "entitatId=" + entitatId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		return conversioTipusHelper.convertirList(
				metaDocumentRepository.findByEntitatOrderByNomAsc(
						entitat),
						MetaDocumentDto.class);
	}

	@Transactional(readOnly=true)
	@Override
	public List<MetaDocumentDto> findByEntitatAndActiveTrue(
			Long entitatId,
			boolean incloureGlobalsExpedient) throws EntitatNotFoundException {
		logger.debug("Consulta dels meta-documents actius de l'entitat ("
				+ "entitatId=" + entitatId + ", "
				+ "incloureGlobalsExpedient=" + incloureGlobalsExpedient + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		return conversioTipusHelper.convertirList(
				metaDocumentRepository.findByEntitatAndActiuTrueAndGlobalsOrderByNomAsc(
						entitat,
						incloureGlobalsExpedient),
						MetaDocumentDto.class);
	}

	@Transactional(readOnly=true)
	@Override
	public FitxerDto getPlantilla(
			Long entitatId,
			Long id) throws EntitatNotFoundException, MetaDocumentNotFoundException {
		logger.debug("Obtenint plantilla del meta-document (" +
				"entitatId=" + entitatId + ", " +
				"id=" + id +  ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				false,
				true);
		MetaDocumentEntity metaDocumentEntitiy = comprovarMetaDocument(
				entitat,
				id);
		FitxerDto fitxer = new FitxerDto();
		fitxer.setNom(metaDocumentEntitiy.getPlantillaNom());
		fitxer.setContentType(metaDocumentEntitiy.getPlantillaContentType());
		fitxer.setContingut(metaDocumentEntitiy.getPlantillaContingut());
		return fitxer;
	}

	@Transactional
	@Override
	public void metaDadaCreate(
			Long entitatId,
			Long id,
			Long metaDadaId,
			MultiplicitatEnumDto multiplicitat,
			boolean readOnly) throws EntitatNotFoundException, MetaDocumentNotFoundException, MetaDadaNotFoundException {
		logger.debug("Afegint meta-dada al meta-document ("
				+ "entitatId=" + entitatId +  ", "
				+ "id=" + id +  ", "
				+ "metaDadaId=" + metaDadaId +  ", "
				+ "readOnly=" + readOnly +  ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaDocumentEntity metaDocument = comprovarMetaDocument(
				entitat,
				id);
		MetaDadaEntity metaDada = comprovarMetaDada(
				entitat,
				metaDadaId);
		metaDocument.metaDadaAdd(
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
		logger.debug("Actualitzant meta-dada del meta-document ("
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
		MetaDocumentEntity metaDocument = comprovarMetaDocument(
				entitat,
				id);
		MetaNodeMetaDadaEntity metaNodeMetaDada = comprovarMetaNodeMetaDada(
				entitat,
				metaDocument,
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
			Long metaDocumentMetaDadaId) throws EntitatNotFoundException, MetaDocumentNotFoundException, MetaDadaNotFoundException {
		logger.debug("Afegint meta-dada al meta-document ("
				+ "entitatId=" + entitatId +  ", "
				+ "id=" + id +  ", "
				+ "metaDocumentMetaDadaId=" + metaDocumentMetaDadaId +  ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaDocumentEntity metaDocument = comprovarMetaDocument(
				entitat,
				id);
		MetaNodeMetaDadaEntity metaDocumentMetaDada = comprovarMetaNodeMetaDada(
				entitat,
				metaDocument,
				metaDocumentMetaDadaId);
		metaDocument.metaDadaDelete(metaDocumentMetaDada);
		metaNodeHelper.reordenarMetaDades(metaDocument);
	}

	@Transactional
	@Override
	public void metaDadaMove(
			Long entitatId,
			Long id,
			Long metaDocumentMetaDadaId,
			int posicio) throws EntitatNotFoundException, MetaDocumentNotFoundException, MetaDadaNotFoundException {
		logger.debug("Movent meta-dada al meta-document ("
				+ "entitatId=" + entitatId +  ", "
				+ "id=" + id +  ", "
				+ "metaDocumentMetaDadaId=" + metaDocumentMetaDadaId +  ", "
				+ "posicio=" + posicio +  ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		MetaDocumentEntity metaDocument = comprovarMetaDocument(
				entitat,
				id);
		MetaNodeMetaDadaEntity metaDocumentMetaDada = comprovarMetaNodeMetaDada(
				entitat,
				metaDocument,
				metaDocumentMetaDadaId);
		metaNodeHelper.moureMetaNodeMetaDada(
				metaDocument,
				metaDocumentMetaDada,
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
		MetaDocumentEntity metaDocument = comprovarMetaDocument(
				entitat,
				id);
		MetaNodeMetaDadaEntity metaNodeMetaDada = comprovarMetaNodeMetaDada(
				entitat,
				metaDocument,
				metaNodeMetaDadaId);
		return conversioTipusHelper.convertir(
				metaNodeMetaDada,
				MetaNodeMetaDadaDto.class);
	}

	@Transactional
	@Override
	public List<PermisDto> findPermis(
			Long entitatId,
			Long id) throws EntitatNotFoundException, MetaDocumentNotFoundException {
		logger.debug("Consulta dels permisos del meta-document ("
				+ "entitatId=" + entitatId +  ", "
				+ "id=" + id +  ")"); 
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		comprovarMetaDocument(
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
			PermisDto permis) throws EntitatNotFoundException, MetaDocumentNotFoundException {
		logger.debug("Modificació del permis del meta-document ("
				+ "entitatId=" + entitatId +  ", "
				+ "id=" + id + ", "
				+ "permis=" + permis + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		comprovarMetaDocument(
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
			Long permisId) throws EntitatNotFoundException, MetaDocumentNotFoundException {
		logger.debug("Eliminació del permis del meta-document ("
				+ "entitatId=" + entitatId +  ", "
				+ "id=" + id + ", "
				+ "permisId=" + permisId + ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		comprovarMetaDocument(
				entitat,
				id);
		permisosHelper.deletePermis(
				id,
				MetaNodeEntity.class,
				permisId);
	}

	@Transactional(readOnly = true)
	@Override
	public List<MetaDocumentDto> findActiveByEntitatAndContenidorPerCreacio(
			Long entitatId,
			Long contenidorId) throws EntitatNotFoundException, ContenidorNotFoundException {
		logger.debug("Consulta de meta-documents actius per a crear ("
				+ "entitatId=" + entitatId +  ", "
				+ "contenidorId=" + contenidorId +  ")");
		EntitatEntity entitat = permisosComprovacioHelper.comprovarEntitat(
				entitatId,
				true,
				false,
				false);
		ContenidorEntity contenidor = comprovarContenidor(entitat, contenidorId);
		ExpedientEntity expedientSuperior = contenidorHelper.getExpedientSuperior(
				contenidor,
				true);
		// Només es poden crear documents amb meta-document a dins els expedients
		List<MetaDocumentEntity> metaDocuments;
		if (expedientSuperior != null) {
			metaDocuments = new ArrayList<MetaDocumentEntity>();
			// Dels meta-documents actius pel meta-expedient només deixa els que
			// encara es poden afegir segons la multiplicitat.
			List<MetaExpedientMetaDocumentEntity> metaExpedientMetaDocuments = metaExpedientMetaDocumentRepository.findByMetaExpedientAndMetaDocumentActiuTrueOrderByMetaDocumentNomAsc(
					expedientSuperior.getMetaExpedient());
			List<DocumentEntity> documents = documentRepository.findByExpedient(
					expedientSuperior);
			for (MetaExpedientMetaDocumentEntity metaExpedientMetaDocument: metaExpedientMetaDocuments) {
				boolean afegir = true;
				for (DocumentEntity document: documents) {
					if (document.getMetaNode() != null && document.getMetaNode().equals(metaExpedientMetaDocument.getMetaDocument())) {
						if (metaExpedientMetaDocument.getMultiplicitat().equals(MultiplicitatEnum.M_0_1) || metaExpedientMetaDocument.getMultiplicitat().equals(MultiplicitatEnum.M_1))
							afegir = false;
						break;
					}
				}
				if (afegir)
					metaDocuments.add(metaExpedientMetaDocument.getMetaDocument());
			}
			// ...comprova que es tengui el permis CREATE
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			permisosHelper.filterGrantedAll(
					metaDocuments,
					new ObjectIdentifierExtractor<MetaNodeEntity>() {
						public Long getObjectIdentifier(MetaNodeEntity metaNode) {
							return metaNode.getId();
						}
					},
					MetaNodeEntity.class,
					new Permission[] {ExtendedPermission.CREATE},
					auth);
			// Afegeix els meta-documents globals actius
			metaDocuments.addAll(
					metaDocumentRepository.findByEntitatAndGlobalExpedientTrueAndActiuTrueOrderByNomAsc(
							entitat));
		} else {
			// Consulta els metadocuments de l'entitat...
			metaDocuments = metaDocumentRepository.findByEntitatAndActiuTrueOrderByNomAsc(entitat);
			// ...i comprova que es tengui el permis CREATE
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			permisosHelper.filterGrantedAll(
					metaDocuments,
					new ObjectIdentifierExtractor<MetaNodeEntity>() {
						public Long getObjectIdentifier(MetaNodeEntity metaNode) {
							return metaNode.getId();
						}
					},
					MetaNodeEntity.class,
					new Permission[] {ExtendedPermission.CREATE},
					auth);
			
		}
		return conversioTipusHelper.convertirList(
				metaDocuments,
				MetaDocumentDto.class);
	}

	@Transactional(readOnly = true)
	@Override
	public List<PortafirmesDocumentTipusDto> findPortafirmesDocumentTipus() {
		return pluginHelper.portafirmesFindDocumentTipus();
	}



	private MetaDocumentEntity comprovarMetaDocument(
			EntitatEntity entitat,
			Long id) {
		MetaDocumentEntity metaDocument = metaDocumentRepository.findOne(
				id);
		if (metaDocument == null) {
			logger.error("No s'ha trobat el meta-expedient (id=" + id + ")");
			throw new MetaExpedientNotFoundException();
		}
		if (!entitat.equals(metaDocument.getEntitat())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat del meta-expedient ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + metaDocument.getEntitat().getId() + ")");
			throw new MetaExpedientNotFoundException();
		}
		return metaDocument;
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
			MetaDocumentEntity metaDocument,
			Long metaNodeMetaDadaId) throws MetaDadaNotFoundException {
		MetaNodeMetaDadaEntity metaNodeMetaDada = metaNodeMetaDadaRepository.findOne(metaNodeMetaDadaId);
		if (metaNodeMetaDada == null) {
			logger.error("No s'ha trobat la meta-dada del meta-node (metaNodeMetaDadaId=" + metaNodeMetaDadaId + ")");
			throw new MetaDadaNotFoundException();
		}
		if (!metaNodeMetaDada.getMetaNode().equals(metaDocument)) {
			logger.error("El meta-node de la meta-dada no coincideix amb el meta-document ("
					+ "metaNodeId1=" + metaDocument.getId() + ", "
					+ "metaNodeId2=" + metaNodeMetaDada.getMetaNode().getId() + ")");
			throw new MetaDadaNotFoundException();
		}
		return metaNodeMetaDada;
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

	private static final Logger logger = LoggerFactory.getLogger(MetaDocumentServiceImpl.class);

}
