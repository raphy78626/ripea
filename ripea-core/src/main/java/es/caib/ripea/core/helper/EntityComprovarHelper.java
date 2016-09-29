/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

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
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.DadaEntity;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.DocumentNotificacioEntity;
import es.caib.ripea.core.entity.DocumentPublicacioEntity;
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
import es.caib.ripea.core.entity.ReglaEntity;
import es.caib.ripea.core.helper.PermisosHelper.ObjectIdentifierExtractor;
import es.caib.ripea.core.repository.ArxiuRepository;
import es.caib.ripea.core.repository.BustiaRepository;
import es.caib.ripea.core.repository.CarpetaRepository;
import es.caib.ripea.core.repository.ContingutRepository;
import es.caib.ripea.core.repository.DadaRepository;
import es.caib.ripea.core.repository.DocumentNotificacioRepository;
import es.caib.ripea.core.repository.DocumentPublicacioRepository;
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
import es.caib.ripea.core.repository.ReglaRepository;
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
	private ContingutRepository contingutRepository;
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
	private ReglaRepository reglaRepository;
	@Resource
	private InteressatRepository interessatRepository;
	@Resource
	private DocumentNotificacioRepository documentNotificacioRepository;
	@Resource
	private DocumentPublicacioRepository documentPublicacioRepository;

	@Resource
	private PermisosHelper permisosHelper;



	public EntitatEntity comprovarEntitat(
			Long entitatId,
			boolean comprovarPermisUsuari,
			boolean comprovarPermisAdmin,
			boolean comprovarPermisUsuariOrAdmin) throws NotFoundException {
		EntitatEntity entitat = entitatRepository.findOne(entitatId);
		if (entitat == null) {
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
			throw new NotFoundException(
					id,
					MetaDocumentEntity.class);
		}
		if (!entitat.equals(metaDocument.getEntitat())) {
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
			Long id,
			boolean comprovarPermisCreate,
			boolean comprovarPermisRead) {
		MetaExpedientEntity metaExpedient = metaExpedientRepository.findOne(
				id);
		if (metaExpedient == null) {
			throw new NotFoundException(
					id,
					MetaExpedientEntity.class);
		}
		if (!entitat.equals(metaExpedient.getEntitat())) {
			throw new ValidationException(
					id,
					MetaExpedientEntity.class,
					"L'entitat especificada (id=" + entitat.getId() + ") no coincideix amb l'entitat del meta-expedient");
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (comprovarPermisCreate) {
			boolean granted = permisosHelper.isGrantedAll(
					metaExpedient.getId(),
					MetaNodeEntity.class,
					new Permission[] {ExtendedPermission.CREATE},
					auth);
			if (!granted) {
				throw new PermissionDeniedException(
						id,
						MetaDocumentEntity.class,
						auth.getName(),
						"CREATE");
			}
		}
		if (comprovarPermisRead) {
			boolean granted = permisosHelper.isGrantedAll(
					metaExpedient.getId(),
					MetaNodeEntity.class,
					new Permission[] {ExtendedPermission.READ},
					auth);
			if (!granted) {
				throw new PermissionDeniedException(
						id,
						MetaExpedientEntity.class,
						auth.getName(),
						"READ");
			}
		}
		return metaExpedient;
	}

	public MetaExpedientMetaDocumentEntity comprovarMetaExpedientMetaDocument(
			EntitatEntity entitat,
			MetaExpedientEntity metaExpedient,
			Long metaExpedientMetaDocumentId) {
		MetaExpedientMetaDocumentEntity metaExpedientMetaDocument = metaExpedientMetaDocumentRepository.findOne(metaExpedientMetaDocumentId);
		if (metaExpedientMetaDocument == null) {
			throw new NotFoundException(
					metaExpedientMetaDocumentId,
					MetaExpedientMetaDocumentEntity.class);
		}
		if (!metaExpedientMetaDocument.getMetaExpedient().equals(metaExpedient)) {
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
			throw new NotFoundException(
					metaDadaId,
					MetaDadaEntity.class);
		}
		if (!entitat.equals(metaDada.getEntitat())) {
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
			throw new NotFoundException(
					metaNodeMetaDadaId,
					MetaDadaEntity.class);
		}
		if (!metaNodeMetaDada.getMetaNode().equals(metaNode)) {
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
			throw new NotFoundException(
					"(metaNodeId=" + metaNode.getId() + ", metaDadaId=" + metaDada.getId() + ")",
					MetaNodeMetaDadaEntity.class);
		}
		return metaNodeMetaDada;
	}

	public ContingutEntity comprovarContingut(
			EntitatEntity entitat,
			Long id,
			BustiaEntity bustiaPare) {
		ContingutEntity contingut = contingutRepository.findOne(id);
		if (contingut == null) {
			throw new NotFoundException(
					id,
					ContingutEntity.class);
		}
		if (!contingut.getEntitat().equals(entitat)) {
			throw new ValidationException(
					id,
					ContingutEntity.class,
					"L'entitat especificada (id=" + entitat.getId() + ") no coincideix amb l'entitat del contingut");
		}
		if (bustiaPare != null) {
			if (contingut.getPare() != null) {
				if (!contingut.getPare().getId().equals(bustiaPare.getId())) {
					throw new ValidationException(
							id,
							ContingutEntity.class,
							"La bústia especificada (id=" + bustiaPare.getId() + ") no coincideix amb la bústia del contingut");
				}
			}
		}
		return contingut;
	}

	public NodeEntity comprovarNode(
			EntitatEntity entitat,
			Long nodeId) {
		NodeEntity node = nodeRepository.findOne(nodeId);
		if (node == null) {
			throw new NotFoundException(
					nodeId,
					NodeEntity.class);
		}
		if (!entitat.equals(node.getEntitat())) {
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
			throw new NotFoundException(
					id,
					CarpetaEntity.class);
		}
		if (!entitat.equals(carpeta.getEntitat())) {
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
			Long expedientId) {
		return comprovarExpedient(
				entitat,
				 arxiu,
				expedientId,
				false,
				false,
				false);
	}
	public ExpedientEntity comprovarExpedient(
			EntitatEntity entitat,
			ArxiuEntity arxiu,
			Long expedientId,
			boolean comprovarPermisRead,
			boolean comprovarPermisWrite,
			boolean comprovarPermisDelete) {
		ExpedientEntity expedient = expedientRepository.findOne(expedientId);
		if (expedient == null) {
			throw new NotFoundException(
					expedientId,
					ExpedientEntity.class);
		}
		if (!entitat.getId().equals(expedient.getEntitat().getId())) {
			throw new ValidationException(
					expedientId,
					ExpedientEntity.class,
					"L'entitat especificada (id=" + entitat.getId() + ") no coincideix amb l'entitat de l'expedient");
		}
		if (arxiu != null && !arxiu.equals(expedient.getArxiu())) {
			throw new ValidationException(
					expedientId,
					ExpedientEntity.class,
					"L'arxiu especificat (id=" + arxiu.getId() + ") no coincideix amb l'arxiu de l'expedient");
		}
		if (comprovarPermisRead || comprovarPermisWrite || comprovarPermisDelete) {
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
					expedient.getMetaExpedient().getId(),
					MetaNodeEntity.class,
					permisos.toArray(new Permission[permisos.size()]),
					auth);
			if (!granted) {
				throw new PermissionDeniedException(
						expedientId,
						DocumentEntity.class,
						auth.getName(),
						permisosStr.toString());
			}
		}
		return expedient;
	}

	public DocumentEntity comprovarDocument(
			EntitatEntity entitat,
			ExpedientEntity expedient,
			Long documentId,
			boolean comprovarPermisRead,
			boolean comprovarPermisWrite,
			boolean comprovarPermisDelete) {
		DocumentEntity document = documentRepository.findOne(documentId);
		if (document == null) {
			throw new NotFoundException(
					documentId,
					DocumentEntity.class);
		}
		if (!document.getEntitat().equals(entitat)) {
			throw new ValidationException(
					documentId,
					DocumentEntity.class,
					"L'entitat especificada (id=" + entitat.getId() + ") no coincideix amb l'entitat del document");
		}
		if (expedient != null && !document.getExpedient().equals(expedient)) {
			throw new ValidationException(
					documentId,
					DocumentEntity.class,
					"L'expedient especificat (id=" + expedient.getId() + ") no coincideix amb l'entitat del document (id=" + document.getExpedient().getId() + ")");
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
			throw new NotFoundException(
					dadaId,
					DadaEntity.class);
		}
		if (!dada.getNode().equals(node)) {
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
			throw new NotFoundException(
					bustiaId,
					BustiaEntity.class);
		}
		if (!entitat.equals(bustia.getEntitat())) {
			throw new ValidationException(
					bustiaId,
					BustiaEntity.class,
					"L'entitat especificada (id=" + entitat.getId() + ") no coincideix amb l'entitat de la bústia (id=" + bustia.getEntitat().getId() + ")");
		}
		if (comprovarPermisRead) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			boolean esPermisRead = permisosHelper.isGrantedAll(
					bustiaId,
					BustiaEntity.class,
					new Permission[] {ExtendedPermission.READ},
					auth);
			if (!esPermisRead) {
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
			Long arxiuId,
			boolean comprovarAcces) throws NotFoundException {
		ArxiuEntity arxiu = arxiuRepository.findOne(arxiuId);
		if (arxiu == null) {
			throw new NotFoundException(
					arxiuId,
					ArxiuEntity.class);
		}
		if (!entitat.equals(arxiu.getEntitat())) {
			throw new ValidationException(
					arxiuId,
					ArxiuEntity.class,
					"L'entitat especificada (id=" + entitat.getId() + ") no coincideix amb l'entitat de l'arxiu");
		}
		if (comprovarAcces) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			List<MetaExpedientEntity> metaExpedientsPermesos = new ArrayList<MetaExpedientEntity>();
			metaExpedientsPermesos.addAll(arxiu.getMetaExpedients());
			permisosHelper.filterGrantedAny(
					metaExpedientsPermesos,
					new ObjectIdentifierExtractor<MetaNodeEntity>() {
						public Long getObjectIdentifier(MetaNodeEntity obj) {
							return obj.getId();
						}
					},
					MetaNodeEntity.class,
					new Permission[] {ExtendedPermission.READ},
					auth);
			if (metaExpedientsPermesos.isEmpty()) {
				throw new PermissionDeniedException(
						arxiuId,
						ArxiuEntity.class,
						auth.getName(),
						"READ");
			}
		}
		return arxiu;
	}

	public RegistreEntity comprovarRegistre(
			Long id,
			BustiaEntity bustiaPare) {
		RegistreEntity registre = registreRepository.findOne(id);
		if (registre == null) {
			throw new NotFoundException(
					id,
					RegistreEntity.class);
		}
		if (bustiaPare != null) {
			if (registre.getPare() != null) {
				if (!registre.getPare().getId().equals(bustiaPare.getId())) {
					throw new ValidationException(
							id,
							RegistreEntity.class,
							"La bústia especificada (id=" + bustiaPare.getId() + ") no coincideix amb la bústia de l'anotació de registre");
				}
			}
		}
		return registre;
	}

	public ReglaEntity comprovarRegla(
			EntitatEntity entitat,
			Long reglaId) {
		ReglaEntity regla = reglaRepository.findOne(reglaId);
		if (regla == null) {
			throw new NotFoundException(
					reglaId,
					ReglaEntity.class);
		}
		if (!regla.getEntitat().equals(entitat)) {
			throw new ValidationException(
					reglaId,
					ReglaEntity.class,
					"La regla especificada (id=" + entitat.getId() + ") no coincideix amb l'entitat de la regla");
		}
		return regla;
	}

	public InteressatEntity comprovarInteressat(
			ExpedientEntity expedient,
			Long interessatId) {
		InteressatEntity interessat = interessatRepository.findOne(interessatId);
		if (interessat == null) {
			throw new NotFoundException(
					interessatId,
					InteressatEntity.class);
		}
		if (expedient != null && !interessat.getExpedient().equals(expedient)) {
			throw new ValidationException(
					interessatId,
					InteressatEntity.class,
					"L'expedient especificat (id=" + expedient.getId() + ") no coincideix amb l'expedeint de l'interessat (id=" + interessat.getExpedient().getId() + ")");
		}
		return interessat;
	}

	public DocumentNotificacioEntity comprovarNotificacio(
			ExpedientEntity expedient,
			DocumentEntity document,
			Long notificacioId) {
		DocumentNotificacioEntity notificacio = documentNotificacioRepository.findOne(
				notificacioId);
		if (notificacio == null) {
			throw new NotFoundException(
					notificacioId,
					DocumentNotificacioEntity.class);
		}
		if (!notificacio.getExpedient().equals(expedient)) {
			throw new ValidationException(
					notificacioId,
					DocumentNotificacioEntity.class,
					"L'expedient especificat (id=" + expedient.getId() + ") no coincideix amb l'expedient de la notificació (id=" + notificacio.getExpedient().getId() + ")");
		}
		if (document != null && !notificacio.getDocument().equals(document)) {
			throw new ValidationException(
					notificacioId,
					DocumentNotificacioEntity.class,
					"El document especificat (id=" + document.getId() + ") no coincideix amb el document de la notificació (id=" + notificacio.getDocument().getId() + ")");
		}
		return notificacio;
	}

	public DocumentPublicacioEntity comprovarPublicacio(
			ExpedientEntity expedient,
			DocumentEntity document,
			Long publicacioId) {
		DocumentPublicacioEntity publicacio = documentPublicacioRepository.findOne(
				publicacioId);
		if (publicacio == null) {
			throw new NotFoundException(
					publicacioId,
					DocumentNotificacioEntity.class);
		}
		if (!publicacio.getExpedient().equals(expedient)) {
			throw new ValidationException(
					publicacioId,
					DocumentPublicacioEntity.class,
					"L'expedient especificat (id=" + expedient.getId() + ") no coincideix amb l'expedient de la publicació (id=" + publicacio.getExpedient().getId() + ")");
		}
		if (document != null && !publicacio.getDocument().equals(document)) {
			throw new ValidationException(
					publicacioId,
					DocumentPublicacioEntity.class,
					"El document especificat (id=" + document.getId() + ") no coincideix amb el document de la publicació (id=" + publicacio.getDocument().getId() + ")");
		}
		return publicacio;
	}

}
