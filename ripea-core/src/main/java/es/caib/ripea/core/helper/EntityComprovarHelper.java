/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.api.exception.PermissionDeniedException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.entity.ArxiuEntity;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.CarpetaEntity;
import es.caib.ripea.core.entity.ContenidorEntity;
import es.caib.ripea.core.entity.DadaEntity;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.InteressatEntity;
import es.caib.ripea.core.entity.MetaDadaEntity;
import es.caib.ripea.core.entity.MetaDocumentEntity;
import es.caib.ripea.core.entity.MetaExpedientEntity;
import es.caib.ripea.core.entity.MetaExpedientMetaDocumentEntity;
import es.caib.ripea.core.entity.MetaNodeEntity;
import es.caib.ripea.core.entity.MetaNodeMetaDadaEntity;
import es.caib.ripea.core.entity.NodeEntity;
import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.repository.ArxiuRepository;
import es.caib.ripea.core.repository.BustiaRepository;
import es.caib.ripea.core.repository.CarpetaRepository;
import es.caib.ripea.core.repository.ContenidorRepository;
import es.caib.ripea.core.repository.DadaRepository;
import es.caib.ripea.core.repository.DocumentRepository;
import es.caib.ripea.core.repository.EntitatRepository;
import es.caib.ripea.core.repository.ExpedientRepository;
import es.caib.ripea.core.repository.InteressatRepository;
import es.caib.ripea.core.repository.MetaDadaRepository;
import es.caib.ripea.core.repository.MetaDocumentRepository;
import es.caib.ripea.core.repository.MetaExpedientMetaDocumentRepository;
import es.caib.ripea.core.repository.MetaExpedientRepository;
import es.caib.ripea.core.repository.MetaNodeMetaDadaRepository;
import es.caib.ripea.core.repository.NodeRepository;
import es.caib.ripea.core.repository.RegistreRepository;
import es.caib.ripea.core.security.ExtendedPermission;


/**
 * Helper per a la comprovació de l'existencia d'entitats de base de dades.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class EntityComprovarHelper {

	@Resource
	private EntitatRepository entitatRepository;
	@Resource
	private MetaDocumentRepository metaDocumentRepository;
	@Resource
	private MetaExpedientRepository metaExpedientRepository;
	@Resource
	private MetaExpedientMetaDocumentRepository metaExpedientMetaDocumentRepository;
	@Resource
	private MetaDadaRepository metaDadaRepository;
	@Resource
	private MetaNodeMetaDadaRepository metaNodeMetaDadaRepository;
	@Resource
	private NodeRepository nodeRepository;
	@Resource
	private ContenidorRepository contenidorRepository;
	@Resource
	private CarpetaRepository carpetaRepository;
	@Resource
	private ExpedientRepository expedientRepository;
	@Resource
	private DocumentRepository documentRepository;
	@Resource
	private DadaRepository dadaRepository;
	@Resource
	private BustiaRepository bustiaRepository;
	@Resource
	private ArxiuRepository arxiuRepository;
	@Resource
	private RegistreRepository registreRepository;
	@Resource
	private InteressatRepository interessatRepository;

	@Resource
	private PermisosHelper permisosHelper;



	public EntitatEntity comprovarEntitat(
			Long entitatId,
			boolean comprovarPermisUsuari,
			boolean comprovarPermisAdmin,
			boolean comprovarPermisUsuariOrAdmin) throws NotFoundException {
		EntitatEntity entitat = entitatRepository.findOne(entitatId);
		if (entitat == null) {
			logger.error("No s'ha trobat l'entitat (entitatId=" + entitatId + ")");
			throw new NotFoundException(
					entitatId,
					EntitatEntity.class);
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (comprovarPermisUsuari) {
			boolean esLectorEntitat = permisosHelper.isGrantedAll(
					entitatId,
					EntitatEntity.class,
					new Permission[] {ExtendedPermission.READ},
					auth);
			if (!esLectorEntitat) {
				logger.error("Aquest usuari no té permisos d'accés sobre l'entitat ("
						+ "id=" + entitatId + ", "
						+ "usuari=" + auth.getName() + ")");
				throw new PermissionDeniedException(
						entitatId,
						EntitatEntity.class,
						auth.getName(),
						"READ");
			}
		}
		if (comprovarPermisAdmin) {
			boolean esAdministradorEntitat = permisosHelper.isGrantedAll(
					entitatId,
					EntitatEntity.class,
					new Permission[] {ExtendedPermission.ADMINISTRATION},
					auth);
			if (!esAdministradorEntitat) {
				logger.error("Aquest usuari no té permisos d'administrador sobre l'entitat ("
						+ "id=" + entitatId + ", "
						+ "usuari=" + auth.getName() + ")");
				throw new PermissionDeniedException(
						entitatId,
						EntitatEntity.class,
						auth.getName(),
						"ADMINISTRATION");
			}
		}
		if (comprovarPermisUsuariOrAdmin) {
			boolean esAdministradorOLectorEntitat = permisosHelper.isGrantedAny(
					entitatId,
					EntitatEntity.class,
					new Permission[] {
						ExtendedPermission.ADMINISTRATION,
						ExtendedPermission.READ},
					auth);
			if (!esAdministradorOLectorEntitat) {
				logger.error("Aquest usuari no té permisos d'administrador o d'accés sobre l'entitat ("
						+ "id=" + entitatId + ", "
						+ "usuari=" + auth.getName() + ")");
				throw new PermissionDeniedException(
						entitatId,
						EntitatEntity.class,
						auth.getName(),
						"ADMINISTRATION || READ");
			}
		}
		return entitat;
	}

	public MetaDocumentEntity comprovarMetaDocument(
			EntitatEntity entitat,
			Long id,
			boolean comprovarPermisCreate) {
		MetaDocumentEntity metaDocument = metaDocumentRepository.findOne(
				id);
		if (metaDocument == null) {
			logger.error("No s'ha trobat el meta-expedient (id=" + id + ")");
			throw new NotFoundException(
					id,
					MetaDocumentEntity.class);
		}
		if (!entitat.equals(metaDocument.getEntitat())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat del meta-expedient ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + metaDocument.getEntitat().getId() + ")");
			throw new ValidationException(
					id,
					MetaDocumentEntity.class,
					"L'entitat especificada (id=" + entitat.getId() + ") no coincideix amb l'entitat del meta-expedient");
		}
		if (comprovarPermisCreate) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			boolean granted = permisosHelper.isGrantedAll(
					metaDocument.getId(),
					MetaNodeEntity.class,
					new Permission[] {ExtendedPermission.CREATE},
					auth);
			if (!granted) {
				logger.error("No es tenen permisos per a crear documents amb el meta-document (id=" + id + ")");
				throw new PermissionDeniedException(
						id,
						MetaDocumentEntity.class,
						auth.getName(),
						"CREATE");
			}
		}
		return metaDocument;
	}

	public MetaExpedientEntity comprovarMetaExpedient(
			EntitatEntity entitat,
			Long id) {
		MetaExpedientEntity metaExpedient = metaExpedientRepository.findOne(
				id);
		if (metaExpedient == null) {
			logger.error("No s'ha trobat el meta-expedient (id=" + id + ")");
			throw new NotFoundException(
					id,
					MetaExpedientEntity.class);
		}
		if (!entitat.equals(metaExpedient.getEntitat())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat del meta-expedient ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + metaExpedient.getEntitat().getId() + ")");
			throw new ValidationException(
					id,
					MetaExpedientEntity.class,
					"L'entitat especificada (id=" + entitat.getId() + ") no coincideix amb l'entitat del meta-expedient");
		}
		return metaExpedient;
	}

	public MetaExpedientMetaDocumentEntity comprovarMetaExpedientMetaDocument(
			EntitatEntity entitat,
			MetaExpedientEntity metaExpedient,
			Long metaExpedientMetaDocumentId) {
		MetaExpedientMetaDocumentEntity metaExpedientMetaDocument = metaExpedientMetaDocumentRepository.findOne(metaExpedientMetaDocumentId);
		if (metaExpedientMetaDocument == null) {
			logger.error("No s'ha trobat el meta-document del meta-expedient (metaExpedientMetaDocumentId=" + metaExpedientMetaDocumentId + ")");
			throw new NotFoundException(
					metaExpedientMetaDocumentId,
					MetaExpedientMetaDocumentEntity.class);
		}
		if (!metaExpedientMetaDocument.getMetaExpedient().equals(metaExpedient)) {
			logger.error("El meta-expedient del meta-document no coincideix amb el meta-expedient ("
					+ "metaNodeId1=" + metaExpedient.getId() + ", "
					+ "metaNodeId2=" + metaExpedientMetaDocument.getMetaExpedient().getId() + ")");
			throw new ValidationException(
					metaExpedientMetaDocumentId,
					MetaExpedientMetaDocumentEntity.class,
					"El meta-expedient especificat (id=" + metaExpedient.getId() + ") no coincideix amb el meta-expedient del meta-document");
		}
		return metaExpedientMetaDocument;
	}

	public MetaDadaEntity comprovarMetaDada(
			EntitatEntity entitat,
			Long metaDadaId) {
		MetaDadaEntity metaDada = metaDadaRepository.findOne(metaDadaId);
		if (metaDada == null) {
			logger.error("No s'ha trobat la meta-dada (metaDadaId=" + metaDadaId + ")");
			throw new NotFoundException(
					metaDadaId,
					MetaDadaEntity.class);
		}
		if (!entitat.equals(metaDada.getEntitat())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat de la meta-dada ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + metaDada.getEntitat().getId() + ")");
			throw new ValidationException(
					metaDadaId,
					MetaDadaEntity.class,
					"L'entitat especificada (id=" + entitat.getId() + ") no coincideix amb l'entitat de la meta-dada");
		}
		return metaDada;
	}

	public MetaNodeMetaDadaEntity comprovarMetaNodeMetaDada(
			EntitatEntity entitat,
			MetaNodeEntity metaNode,
			Long metaNodeMetaDadaId) {
		MetaNodeMetaDadaEntity metaNodeMetaDada = metaNodeMetaDadaRepository.findOne(metaNodeMetaDadaId);
		if (metaNodeMetaDada == null) {
			logger.error("No s'ha trobat la meta-dada del meta-node (metaNodeMetaDadaId=" + metaNodeMetaDadaId + ")");
			throw new NotFoundException(
					metaNodeMetaDadaId,
					MetaDadaEntity.class);
		}
		if (!metaNodeMetaDada.getMetaNode().equals(metaNode)) {
			logger.error("El meta-node de la meta-dada no coincideix amb el meta-document ("
					+ "metaNodeId1=" + metaNode.getId() + ", "
					+ "metaNodeId2=" + metaNodeMetaDada.getMetaNode().getId() + ")");
			throw new ValidationException(
					metaNodeMetaDadaId,
					MetaDadaEntity.class,
					"El meta-node especificat (id=" + metaNode.getId() + ") no coincideix amb el meta-node de la meta-dada");
		}
		return metaNodeMetaDada;
	}

	public MetaNodeMetaDadaEntity comprovarMetaNodeMetaDada(
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
			throw new NotFoundException(
					"(metaNodeId=" + metaNode.getId() + ", metaDadaId=" + metaDada.getId() + ")",
					MetaNodeMetaDadaEntity.class);
		}
		return metaNodeMetaDada;
	}

	public ContenidorEntity comprovarContenidor(
			EntitatEntity entitat,
			Long id,
			BustiaEntity bustiaPare) {
		ContenidorEntity contenidor = contenidorRepository.findOne(id);
		if (contenidor == null) {
			logger.error("No s'ha trobat el contenidor (contenidorId=" + id + ")");
			throw new NotFoundException(
					id,
					ContenidorEntity.class);
		}
		if (!contenidor.getEntitat().equals(entitat)) {
			logger.error("L'entitat del contenidor no coincideix ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + contenidor.getEntitat().getId() + ")");
			throw new ValidationException(
					id,
					ContenidorEntity.class,
					"L'entitat especificada (id=" + entitat.getId() + ") no coincideix amb l'entitat del contenidor");
		}
		if (bustiaPare != null) {
			if (contenidor.getPare() != null) {
				if (!contenidor.getPare().getId().equals(bustiaPare.getId())) {
					logger.error("El contenidor no està dins la bústia especificada ("
							+ "bustiaId1=" + bustiaPare.getId() + ", "
							+ "bustiaId2=" + contenidor.getPare().getId() + ")");
					throw new ValidationException(
							id,
							ContenidorEntity.class,
							"La bústia especificada (id=" + bustiaPare.getId() + ") no coincideix amb la bústia del contenidor");
				}
			}
		}
		return contenidor;
	}

	public NodeEntity comprovarNode(
			EntitatEntity entitat,
			Long nodeId) {
		NodeEntity node = nodeRepository.findOne(nodeId);
		if (node == null) {
			logger.error("No s'ha trobat el node (nodeId=" + nodeId + ")");
			throw new NotFoundException(
					nodeId,
					NodeEntity.class);
		}
		if (!entitat.equals(node.getEntitat())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat del node ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + node.getEntitat().getId() + ")");
			throw new ValidationException(
					nodeId,
					NodeEntity.class,
					"L'entitat especificada (id=" + entitat.getId() + ") no coincideix amb l'entitat del node");
		}
		return node;
	}

	public CarpetaEntity comprovarCarpeta(
			EntitatEntity entitat,
			Long id) {
		CarpetaEntity carpeta = carpetaRepository.findOne(id);
		if (carpeta == null) {
			logger.error("No s'ha trobat la carpeta (id=" + id + ")");
			throw new NotFoundException(
					id,
					CarpetaEntity.class);
		}
		if (!entitat.equals(carpeta.getEntitat())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat de la carpeta ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + carpeta.getEntitat().getId() + ")");
			throw new ValidationException(
					id,
					CarpetaEntity.class,
					"L'entitat especificada (id=" + entitat.getId() + ") no coincideix amb l'entitat de la carpeta");
		}
		return carpeta;
	}

	public ExpedientEntity comprovarExpedient(
			EntitatEntity entitat,
			ArxiuEntity arxiu,
			Long id) {
		ExpedientEntity expedient = expedientRepository.findOne(id);
		if (expedient == null) {
			logger.error("No s'ha trobat l'expedient (id=" + id + ")");
			throw new NotFoundException(
					id,
					ExpedientEntity.class);
		}
		if (!entitat.getId().equals(expedient.getEntitat().getId())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat de l'expedient ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + expedient.getEntitat().getId() + ")");
			throw new ValidationException(
					id,
					ExpedientEntity.class,
					"L'entitat especificada (id=" + entitat.getId() + ") no coincideix amb l'entitat de l'expedient");
		}
		if (arxiu != null && !arxiu.equals(expedient.getArxiu())) {
			logger.error("L'arxiu de l'expedient no coincideix amb l'especificat ("
					+ "id=" + id + ""
					+ "arxiuId1=" + arxiu.getId() + ", "
					+ "arxiuId2=" + expedient.getArxiu().getId() + ")");
			throw new ValidationException(
					id,
					ExpedientEntity.class,
					"L'arxiu especificat (id=" + arxiu.getId() + ") no coincideix amb l'arxiu de l'expedient");
		}
		return expedient;
	}

	public DocumentEntity comprovarDocument(
			EntitatEntity entitat,
			Long documentId,
			boolean comprovarPermisRead,
			boolean comprovarPermisWrite,
			boolean comprovarPermisDelete) {
		DocumentEntity document = documentRepository.findOne(documentId);
		if (document == null) {
			logger.error("No s'ha trobat el document (documentId=" + documentId + ")");
			throw new NotFoundException(
					documentId,
					DocumentEntity.class);
		}
		if (!document.getEntitat().equals(entitat)) {
			logger.error("L'entitat del document no coincideix ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + document.getEntitat().getId() + ")");
			throw new ValidationException(
					documentId,
					DocumentEntity.class,
					"L'entitat especificada (id=" + entitat.getId() + ") no coincideix amb l'entitat del document");
		}
		if (document.getMetaDocument() != null && (comprovarPermisRead || comprovarPermisWrite || comprovarPermisDelete)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			List<Permission> permisos = new ArrayList<Permission>();
			StringBuilder permisosStr = new StringBuilder();
			if (comprovarPermisRead) {
				permisos.add(ExtendedPermission.READ);
				if (permisosStr.length() > 0)
					permisosStr.append(" && ");
				permisosStr.append("READ");
			}
			if (comprovarPermisWrite) {
				permisos.add(ExtendedPermission.WRITE);
				if (permisosStr.length() > 0)
					permisosStr.append(" && ");
				permisosStr.append("WRITE");
			}
			if (comprovarPermisDelete) {
				permisos.add(ExtendedPermission.DELETE);
				if (permisosStr.length() > 0)
					permisosStr.append(" && ");
				permisosStr.append("DELETE");
			}
			boolean granted = permisosHelper.isGrantedAll(
					document.getMetaDocument().getId(),
					MetaNodeEntity.class,
					permisos.toArray(new Permission[permisos.size()]),
					auth);
			if (!granted) {
				logger.error("No es tenen els permisos requerits pel document (documentId=" + documentId + ")");
				throw new PermissionDeniedException(
						documentId,
						DocumentEntity.class,
						auth.getName(),
						permisosStr.toString());
			}
		}
		return document;
	}

	public DadaEntity comprovarDada(
			NodeEntity node,
			Long dadaId) {
		DadaEntity dada = dadaRepository.findOne(dadaId);
		if (dada == null) {
			logger.error("No s'ha trobat la dada (dadaId=" + dadaId + ")");
			throw new NotFoundException(
					dadaId,
					DadaEntity.class);
		}
		if (!dada.getNode().equals(node)) {
			logger.error("L'usuari no te els permisos requerits ("
					+ "nodeId1=" + node.getId() + ","
					+ "nodeId2=" + dada.getNode().getId() + ")");
			throw new ValidationException(
					dadaId,
					DadaEntity.class,
					"El node especificat (id=" + node.getId() + ") no coincideix amb el node de la dada");
		}
		return dada;
	}

	public BustiaEntity comprovarBustia(
			EntitatEntity entitat,
			Long bustiaId,
			boolean comprovarPermisRead) {
		BustiaEntity bustia = bustiaRepository.findOne(bustiaId);
		if (bustia == null) {
			logger.error("No s'ha trobat la bústia (bustiaId=" + bustiaId + ")");
			throw new NotFoundException(
					bustiaId,
					BustiaEntity.class);
		}
		if (!entitat.equals(bustia.getEntitat())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat de la bústia ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + bustia.getEntitat().getId() + ")");
			throw new ValidationException(
					bustiaId,
					BustiaEntity.class,
					"L'entitat especificada (id=" + entitat.getId() + ") no coincideix amb l'entitat de la bústia");
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
				throw new PermissionDeniedException(
						bustiaId,
						BustiaEntity.class,
						auth.getName(),
						"READ");
			}
		}
		return bustia;
	}

	public ArxiuEntity comprovarArxiu(
			EntitatEntity entitat,
			Long arxiuId) throws NotFoundException {
		ArxiuEntity arxiu = arxiuRepository.findOne(arxiuId);
		if (arxiu == null) {
			logger.error("No s'ha trobat l'arxiu (arxiuId=" + arxiuId + ")");
			throw new NotFoundException(
					arxiuId,
					ArxiuEntity.class);
		}
		if (!entitat.equals(arxiu.getEntitat())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat de l'arxiu ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + arxiu.getEntitat().getId() + ")");
			throw new ValidationException(
					arxiuId,
					ArxiuEntity.class,
					"L'entitat especificada (id=" + entitat.getId() + ") no coincideix amb l'entitat de l'arxiu");
		}
		return arxiu;
	}

	public RegistreEntity comprovarRegistre(
			EntitatEntity entitat,
			Long id,
			BustiaEntity bustiaPare) {
		RegistreEntity registre = registreRepository.findOne(id);
		if (registre == null) {
			logger.error("No s'ha trobat l'anotació de registre (id=" + id + ")");
			throw new NotFoundException(
					id,
					RegistreEntity.class);
		}
		if (!entitat.getId().equals(registre.getEntitat().getId())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat de l'anotació de registre ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + registre.getEntitat().getId() + ")");
			throw new ValidationException(
					id,
					RegistreEntity.class,
					"L'entitat especificada (id=" + entitat.getId() + ") no coincideix amb l'entitat de l'anotació de registre");
		}
		if (bustiaPare != null) {
			if (registre.getContenidor() != null) {
				if (!registre.getContenidor().getId().equals(bustiaPare.getId())) {
					logger.error("El registre no està dins la bústia especificada ("
							+ "bustiaId1=" + bustiaPare.getId() + ", "
							+ "bustiaId2=" + registre.getContenidor().getId() + ")");
					throw new ValidationException(
							id,
							RegistreEntity.class,
							"La bústia especificada (id=" + bustiaPare.getId() + ") no coincideix amb la bústia de l'anotació de registre");
				}
			}
		}
		return registre;
	}

	public InteressatEntity comprovarInteressat(
			EntitatEntity entitat,
			Long id) {
		InteressatEntity interessat = interessatRepository.findOne(id);
		if (interessat == null) {
			logger.error("No s'ha trobat l'interessat (id=" + id + ")");
			throw new NotFoundException(
					id,
					InteressatEntity.class);
		}
		if (!entitat.getId().equals(interessat.getEntitat().getId())) {
			logger.error("L'entitat especificada no coincideix amb l'entitat de l'interessat ("
					+ "entitatId1=" + entitat.getId() + ", "
					+ "entitatId2=" + interessat.getEntitat().getId() + ")");
			throw new ValidationException(
					id,
					InteressatEntity.class,
					"L'entitat especificada (id=" + entitat.getId() + ") no coincideix amb l'entitat de l'interessat");
		}
		return interessat;
	}

	private static final Logger logger = LoggerFactory.getLogger(EntityComprovarHelper.class);

}
