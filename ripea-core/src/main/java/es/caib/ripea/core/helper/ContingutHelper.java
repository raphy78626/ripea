/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import es.caib.plugins.arxiu.api.ContingutArxiu;
import es.caib.ripea.core.api.dto.ArxiuDto;
import es.caib.ripea.core.api.dto.BustiaDto;
import es.caib.ripea.core.api.dto.CarpetaDto;
import es.caib.ripea.core.api.dto.ContingutDto;
import es.caib.ripea.core.api.dto.DadaDto;
import es.caib.ripea.core.api.dto.DocumentDto;
import es.caib.ripea.core.api.dto.DocumentVersioDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.EscriptoriDto;
import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.dto.FitxerDto;
import es.caib.ripea.core.api.dto.MetaDocumentDto;
import es.caib.ripea.core.api.dto.MetaExpedientDto;
import es.caib.ripea.core.api.dto.MetaNodeDto;
import es.caib.ripea.core.api.dto.NodeDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.dto.RegistreAnotacioDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.dto.UsuariDto;
import es.caib.ripea.core.api.exception.PermissionDeniedException;
import es.caib.ripea.core.api.exception.ValidationException;
import es.caib.ripea.core.api.registre.RegistreInteressat;
import es.caib.ripea.core.entity.ArxiuEntity;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.CarpetaEntity;
import es.caib.ripea.core.entity.ContingutEntity;
import es.caib.ripea.core.entity.ContingutMovimentEntity;
import es.caib.ripea.core.entity.DadaEntity;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.EscriptoriEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.MetaExpedientEntity;
import es.caib.ripea.core.entity.MetaExpedientSequenciaEntity;
import es.caib.ripea.core.entity.MetaNodeEntity;
import es.caib.ripea.core.entity.NodeEntity;
import es.caib.ripea.core.entity.RegistreEntity;
import es.caib.ripea.core.entity.UnitatOrganitzativaEntity;
import es.caib.ripea.core.entity.UsuariEntity;
import es.caib.ripea.core.helper.PermisosHelper.ObjectIdentifierExtractor;
import es.caib.ripea.core.repository.ContingutMovimentRepository;
import es.caib.ripea.core.repository.ContingutRepository;
import es.caib.ripea.core.repository.DadaRepository;
import es.caib.ripea.core.repository.DocumentRepository;
import es.caib.ripea.core.repository.EscriptoriRepository;
import es.caib.ripea.core.repository.ExpedientRepository;
import es.caib.ripea.core.repository.MetaDadaRepository;
import es.caib.ripea.core.repository.MetaDocumentRepository;
import es.caib.ripea.core.repository.MetaExpedientMetaDocumentRepository;
import es.caib.ripea.core.repository.MetaExpedientSequenciaRepository;
import es.caib.ripea.core.repository.MetaNodeMetaDadaRepository;
import es.caib.ripea.core.repository.MetaNodeRepository;
import es.caib.ripea.core.repository.NodeRepository;
import es.caib.ripea.core.repository.RegistreRepository;
import es.caib.ripea.core.security.ExtendedPermission;
import es.caib.ripea.plugin.usuari.DadesUsuari;

/**
 * Utilitat per a gestionar contenidors.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class ContingutHelper {

	@Resource
	private ContingutRepository contingutRepository;
	@Resource
	private EscriptoriRepository escriptoriRepository;
	@Resource
	private NodeRepository nodeRepository;
	@Resource
	private DadaRepository dadaRepository;
	@Resource
	private DocumentRepository documentRepository;
	@Resource
	private ExpedientRepository expedientRepository;
	@Resource
	private MetaDadaRepository metaDadaRepository;
	@Resource
	private MetaDocumentRepository metaDocumentRepository;
	@Resource
	private MetaNodeMetaDadaRepository metaNodeMetaDadaRepository;
	@Resource
	private MetaExpedientMetaDocumentRepository metaExpedientMetaDocumentRepository;
	@Resource
	private MetaNodeRepository metaNodeRepository;
	@Resource
	private RegistreRepository registreRepository;
	@Resource
	private ContingutMovimentRepository contenidorMovimentRepository;
	@Resource
	private MetaExpedientSequenciaRepository metaExpedientSequenciaRepository;

	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private MetaNodeHelper metaNodeHelper;
	@Resource
	private DocumentHelper documentHelper;
	@Resource
	private PluginHelper pluginHelper;
	@Resource
	private PermisosHelper permisosHelper;
	@Resource
	private CacheHelper cacheHelper;
	@Resource
	private UsuariHelper usuariHelper;
	@Resource
	private UnitatOrganitzativaHelper unitatOrganitzativaHelper;



	public ContingutDto toContingutDto(
			ContingutEntity contingut) {
		return toContingutDto(
				contingut,
				false,
				false,
				false,
				false,
				false,
				false,
				false);
	}
	public ContingutDto toContingutDto(
			ContingutEntity contingut,
			boolean ambPermisos,
			boolean ambFills,
			boolean filtrarFillsSegonsPermisRead,
			boolean ambDades,
			boolean ambPath,
			boolean pathNomesFinsExpedientArrel,
			boolean ambVersions) {
		ContingutDto resposta = null;
		MetaNodeDto metaNode = null;
		// Crea el contenidor del tipus correcte
		ContingutEntity deproxied = HibernateHelper.deproxy(contingut);
		if (deproxied instanceof EscriptoriEntity) {
			EscriptoriDto dto = new EscriptoriDto();
			resposta = dto;
		} else if (deproxied instanceof ExpedientEntity) {
			ExpedientEntity expedient = (ExpedientEntity)deproxied;
			ExpedientDto dto = new ExpedientDto();
			dto.setEstat(expedient.getEstat());
			dto.setTancatData(expedient.getTancatData());
			dto.setTancatMotiu(expedient.getTancatMotiu());
			dto.setAny(expedient.getAny());
			dto.setSequencia(expedient.getSequencia());
			dto.setCodi(expedient.getCodi());
			dto.setNtiVersion(expedient.getNtiVersion());
			dto.setNtiIdentificador(expedient.getNtiIdentificador());
			dto.setNtiOrgano(expedient.getNtiOrgano());
			dto.setNtiOrganoDescripcio(expedient.getNtiOrgano());
			dto.setNtiFechaApertura(expedient.getNtiFechaApertura());
			dto.setNtiClasificacionSia(expedient.getNtiClasificacionSia());
			dto.setSistraPublicat(expedient.isSistraPublicat());
			dto.setSistraUnitatAdministrativa(expedient.getSistraUnitatAdministrativa());
			dto.setSistraClau(expedient.getSistraClau());
			dto.setNumero(expedient.getNumero());
			dto.setAgafatPer(
					conversioTipusHelper.convertir(
							expedient.getAgafatPer(),
							UsuariDto.class));
			dto.setArxiu((ArxiuDto)toContingutDto(expedient.getArxiu()));
			metaNode = conversioTipusHelper.convertir(
					expedient.getMetaNode(),
					MetaExpedientDto.class);
			dto.setMetaNode(metaNode);
			dto.setValid(
					cacheHelper.findErrorsValidacioPerNode(expedient).isEmpty());
			dto.setAmbRegistresSenseLlegir(false);
			for(ContingutEntity c: expedient.getFills()) {
				if(c instanceof RegistreEntity) {
					Boolean llegida = ((RegistreEntity) c).getLlegida();
					if(llegida != null && !llegida) {
						dto.setAmbRegistresSenseLlegir(true);
						break;
					}
				}
			}
			resposta = dto;
		} else if (deproxied instanceof DocumentEntity) {
			DocumentEntity document = (DocumentEntity)deproxied;
			DocumentDto dto = new DocumentDto();
			dto.setDocumentTipus(document.getDocumentTipus());
			dto.setEstat(document.getEstat());
			dto.setUbicacio(document.getUbicacio());
			dto.setData(document.getData());
			dto.setCustodiaData(document.getCustodiaData());
			dto.setCustodiaId(document.getCustodiaId());
			if (document.getFitxerNom() != null) {
				dto.setFitxerNom(document.getFitxerNom());
				dto.setFitxerNomEnviamentPortafirmes(
						pluginHelper.conversioConvertirPdfArxiuNom(document.getFitxerNom()));
				dto.setFitxerContentType(document.getFitxerContentType());
				//dto.setFitxerContingut(document.getFitxerContingut());
			}
			dto.setDataCaptura(document.getDataCaptura());
			dto.setVersioDarrera(document.getVersioDarrera());
			dto.setVersioCount(document.getVersioCount());
			if (ambVersions && pluginHelper.isArxiuPluginActiu() && pluginHelper.arxiuSuportaVersionsDocuments() && document.getEsborrat() == 0) {
				List<ContingutArxiu> arxiuVersions = pluginHelper.arxiuDocumentObtenirVersions(document);
				if (arxiuVersions != null) {
					List<DocumentVersioDto> versions = new ArrayList<DocumentVersioDto>();
					for (ContingutArxiu arxiuVersio: arxiuVersions) {
						DocumentVersioDto versio = new DocumentVersioDto();
						versio.setArxiuUuid(arxiuVersio.getIdentificador());
						versio.setId(arxiuVersio.getVersio());
						versions.add(versio);
					}
					dto.setVersions(versions);
				}
			}
			dto.setNtiVersion(document.getNtiVersion());
			dto.setNtiIdentificador(document.getNtiIdentificador());
			dto.setNtiOrgano(document.getNtiOrgano());
			dto.setNtiOrganoDescripcio(document.getNtiOrgano());
			dto.setNtiOrigen(document.getNtiOrigen());
			dto.setNtiEstadoElaboracion(document.getNtiEstadoElaboracion());
			dto.setNtiTipoDocumental(document.getNtiTipoDocumental());
			dto.setNtiIdDocumentoOrigen(document.getNtiIdDocumentoOrigen());
			dto.setNtiTipoFirma(document.getNtiTipoFirma());
			dto.setNtiCsv(document.getNtiCsv());
			dto.setNtiCsvRegulacion(document.getNtiCsvRegulacion());
			metaNode = conversioTipusHelper.convertir(
					document.getMetaNode(),
					MetaDocumentDto.class);
			dto.setMetaNode(metaNode);
			dto.setValid(
					cacheHelper.findErrorsValidacioPerNode(document).isEmpty());
			resposta = dto;
		} else if (deproxied instanceof CarpetaEntity) {
			CarpetaDto dto = new CarpetaDto();
			resposta = dto;
		} else if (deproxied instanceof ArxiuEntity) {
			ArxiuEntity arxiu = (ArxiuEntity)deproxied;
			ArxiuDto dto = new ArxiuDto();
			dto.setUnitatCodi(arxiu.getUnitatCodi());
			dto.setActiu(arxiu.isActiu());
			resposta = dto;
		} else if (deproxied instanceof BustiaEntity) {
			BustiaEntity bustia = (BustiaEntity)deproxied;
			BustiaDto dto = new BustiaDto();
			dto.setUnitatCodi(bustia.getUnitatCodi());
			dto.setActiva(bustia.isActiva());
			dto.setPerDefecte(bustia.isPerDefecte());
			UnitatOrganitzativaDto unitatConselleria = unitatOrganitzativaHelper.findConselleria(
					bustia.getEntitat().getCodi(),
					bustia.getUnitatCodi());
			if (unitatConselleria != null)
				dto.setUnitatConselleriaCodi(unitatConselleria.getCodi());
//===============================================================================================================
//			UnitatOrganitzativaDto unitatOrganitzativa = unitatOrganitzativaHelper.findPerEntitatAndCodi(
//					bustia.getEntitat().getCodi(),
//					bustia.getUnitatCodi());
//			dto.setUnitat(unitatOrganitzativa);
//===============================================================================================================
			UnitatOrganitzativaEntity unitatEntity = bustia.getUnitatOrganitzativa();
			UnitatOrganitzativaDto unitatDto = conversioTipusHelper.convertir(
					unitatEntity,
					UnitatOrganitzativaDto.class);
			
			unitatDto = UnitatOrganitzativaHelper.assignAltresUnitatsFusionades(unitatEntity, unitatDto);
			dto.setUnitatOrganitzativa(unitatDto);
			resposta = dto;
		} else if (deproxied instanceof RegistreEntity) {
			RegistreEntity registre = (RegistreEntity)deproxied;
			RegistreAnotacioDto dto = conversioTipusHelper.convertir(
					registre,
					RegistreAnotacioDto.class);
			dto.setLlegida(registre.getLlegida() == null || registre.getLlegida());
			resposta = dto;
		}
		resposta.setId(contingut.getId());
		resposta.setNom(contingut.getNom());
		resposta.setEsborrat(contingut.getEsborrat());
		resposta.setArxiuUuid(contingut.getArxiuUuid());
		resposta.setArxiuDataActualitzacio(contingut.getArxiuDataActualitzacio());
		resposta.setEntitat(
				conversioTipusHelper.convertir(
						contingut.getEntitat(),
							EntitatDto.class));
		if (contingut.getDarrerMoviment() != null) {
			ContingutMovimentEntity darrerMoviment = contingut.getDarrerMoviment();
			resposta.setDarrerMovimentUsuari(
					conversioTipusHelper.convertir(
							darrerMoviment.getRemitent(),
							UsuariDto.class));
			resposta.setDarrerMovimentData(darrerMoviment.getCreatedDate().toDate());
			resposta.setDarrerMovimentComentari(darrerMoviment.getComentari());
		}
		if (ambPermisos && metaNode != null) {
			// Omple els permisos
			metaNodeHelper.omplirPermisosPerMetaNode(metaNode, false);
		}
		if (resposta != null) {
			// Omple la informació d'auditoria
			resposta.setCreatedBy(
					conversioTipusHelper.convertir(
							contingut.getCreatedBy(),
							UsuariDto.class));
			resposta.setCreatedDate(contingut.getCreatedDate().toDate());
			resposta.setLastModifiedBy(
					conversioTipusHelper.convertir(
							contingut.getLastModifiedBy(),
							UsuariDto.class));
			resposta.setLastModifiedDate(contingut.getLastModifiedDate().toDate());
		}
		if (resposta != null) {
			if (ambPath) {
				// Calcula el path
				List<ContingutDto> path = getPathContingutComDto(
						contingut,
						ambPermisos,
						pathNomesFinsExpedientArrel);
				resposta.setPath(path);
			}
			if (ambFills) {
				// Cerca els nodes fills
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				List<ContingutDto> contenidorDtos = new ArrayList<ContingutDto>();
				List<ContingutEntity> fills = contingutRepository.findByPareAndEsborrat(
						contingut,
						0,
						new Sort("createdDate"));
				if (filtrarFillsSegonsPermisRead) {
					// Filtra els fills que no tenen permis de lectura
					Iterator<ContingutEntity> it = fills.iterator();
					while (it.hasNext()) {
						ContingutEntity c = it.next();
						if (c instanceof NodeEntity) {
							NodeEntity n = (NodeEntity)c;
							if (n.getMetaNode() != null && !permisosHelper.isGrantedAll(
									n.getMetaNode().getId(),
									MetaNodeEntity.class,
									new Permission[] {ExtendedPermission.READ},
									auth)) {
								it.remove();
							}
						}
					}
				}
				List<ContingutDto> fillPath = null;
				if (ambPath) {
					fillPath = new ArrayList<ContingutDto>();
					if (resposta.getPath() != null)
						fillPath.addAll(resposta.getPath());
					fillPath.add(toContingutDto(
							contingut,
							false,
							false,
							false,
							false,
							false,
							false,
							false));
				}
				for (ContingutEntity fill: fills) {
					if (fill.getEsborrat() == 0) {
						ContingutDto fillDto = toContingutDto(
								fill,
								ambPermisos,
								false,
								false,
								false,
								false,
								false,
								false);
						// Configura el pare de cada fill
						fillDto.setPath(fillPath);
						contenidorDtos.add(fillDto);
					}
				}
				resposta.setFills(contenidorDtos);
			}
			if (ambDades && contingut instanceof NodeEntity) {
				NodeEntity node = (NodeEntity)contingut;
				List<DadaEntity> dades = dadaRepository.findByNode(node);
				((NodeDto)resposta).setDades(
						conversioTipusHelper.convertirList(
								dades,
								DadaDto.class));
				for (int i = 0; i < dades.size(); i++) {
					((NodeDto)resposta).getDades().get(i).setValor(dades.get(i).getValor());
				}
			}
		}
		return resposta;
	}

	public void comprovarPermisosContingut(
			ContingutEntity contingut,
			boolean comprovarPermisRead,
			boolean comprovarPermisWrite,
			boolean comprovarPermisDelete) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// Comprova els permisos del contenidor actual
		if (contingut instanceof NodeEntity) {
			// Si el contenidor és de tipus node
			NodeEntity node = (NodeEntity)contingut;
			if (node.getMetaNode() != null) {
				if (comprovarPermisRead) {
					boolean granted = permisosHelper.isGrantedAll(
							node.getMetaNode().getId(),
							MetaNodeEntity.class,
							new Permission[] {ExtendedPermission.READ},
							auth);
					if (!granted) {
						logger.debug("No te permisos per a llegir el node ("
								+ "id=" + node.getId() + ", "
								+ "usuari=" + auth.getName() + ")");
						throw new SecurityException("Sense permisos per accedir al node ("
								+ "id=" + node.getId() + ", "
								+ "usuari=" + auth.getName() + ")");
					}
				}
				if (comprovarPermisWrite) {
					boolean granted = permisosHelper.isGrantedAll(
							node.getMetaNode().getId(),
							MetaNodeEntity.class,
							new Permission[] {ExtendedPermission.WRITE},
							auth);
					if (!granted) {
						logger.debug("No te permisos per a modificar el node ("
								+ "id=" + node.getId() + ", "
								+ "usuari=" + auth.getName() + ")");
						throw new SecurityException("Sense permisos per a modificar el node ("
								+ "id=" + node.getId() + ", "
								+ "usuari=" + auth.getName() + ")");
					}
				}
				if (comprovarPermisDelete) {
					boolean granted = permisosHelper.isGrantedAll(
							node.getMetaNode().getId(),
							MetaNodeEntity.class,
							new Permission[] {ExtendedPermission.DELETE},
							auth);
					if (!granted) {
						logger.debug("No te permisos per a esborrar el node ("
								+ "id=" + node.getId() + ", "
								+ "usuari=" + auth.getName() + ")");
						throw new SecurityException("Sense permisos per a esborrar el node ("
								+ "id=" + node.getId() + ", "
								+ "usuari=" + auth.getName() + ")");
					}
				}
			}
		} else if (contingut instanceof EscriptoriEntity) {
			EscriptoriEntity escriptori = (EscriptoriEntity)contingut;
			if (comprovarPermisRead) {
				boolean granted = escriptori.getUsuari().getCodi().equals(auth.getName());
				if (!granted) {
					logger.debug("Sense permisos per a accedir a l'escriptori ("
							+ "id=" + escriptori.getId() + ", "
							+ "usuari=" + auth.getName() + ")");
					throw new SecurityException("Sense permisos per accedir a l'escriptori ("
							+ "id=" + escriptori.getId() + ", "
							+ "usuari=" + auth.getName() + ")");
				}
			}
		} else if (contingut instanceof ArxiuEntity) {
			ArxiuEntity arxiu = (ArxiuEntity)contingut;
			if (arxiu.getPare() != null && comprovarPermisRead) {
				// Comprova que algun dels seus meta-expedients associants tingui el permís de lectura
				boolean granted = false;
				for(MetaExpedientEntity metaExpedient : arxiu.getMetaExpedients()) {
					granted = permisosHelper.isGrantedAll(
							metaExpedient.getId(),
							MetaNodeEntity.class,
							new Permission[] {ExtendedPermission.READ},
							auth);
					if(granted)
						break;
				}
				if (!granted) {
					logger.debug("Sense permisos per a accedir a l'arxiu ("
							+ "id=" + arxiu.getId() + ", "
							+ "usuari=" + auth.getName() + ")");
					throw new SecurityException("Sense permisos per accedir a l'arxiu ("
							+ "id=" + arxiu.getId() + ", "
							+ "usuari=" + auth.getName() + ")");
				}
			}
		} else if (contingut instanceof BustiaEntity) {
			BustiaEntity bustia = (BustiaEntity)contingut;
			if (bustia.getPare() != null && comprovarPermisRead) {
				boolean granted = permisosHelper.isGrantedAll(
						bustia.getId(),
						BustiaEntity.class,
						new Permission[] {ExtendedPermission.READ},
						auth);
				if (!granted) {
					logger.debug("Sense permisos per a accedir a la bústia ("
							+ "id=" + bustia.getId() + ", "
							+ "usuari=" + auth.getName() + ")");
					throw new SecurityException("Sense permisos per accedir a la bústia ("
							+ "id=" + bustia.getId() + ", "
							+ "usuari=" + auth.getName() + ")");
				}
			}
		}
	}
	public void comprovarPermisosPathContingut(
			ContingutEntity contingut,
			boolean comprovarPermisRead,
			boolean comprovarPermisWrite,
			boolean comprovarPermisDelete,
			boolean incloureActual) {
		List<ContingutEntity> path = getPathContingut(contingut);
		if (path != null) {
			// Dels contenidors del path només comprova el permis read
			for (ContingutEntity contingutPath: path) {
				// Si el contingut està agafat per un altre usuari no es
				// comproven els permisos de l'escriptori perquè òbviament
				// els altres usuaris no hi tendran accés.
				if (!(contingutPath instanceof EscriptoriEntity)) {
					comprovarPermisosContingut(
							contingutPath,
							true,
							false,
							false);
				}
			}
		}
		if (incloureActual) {
			// Del contenidor en qüestió comprova tots els permisos
			comprovarPermisosContingut(
					contingut,
					comprovarPermisRead,
					comprovarPermisWrite,
					comprovarPermisDelete);
		}
	}
	public void comprovarPermisosFinsExpedientArrel(
			ContingutEntity contingut,
			boolean comprovarPermisRead,
			boolean comprovarPermisWrite,
			boolean comprovarPermisDelete,
			boolean incloureActual) {
		List<ContingutEntity> path = getPathContingut(contingut);
		if (incloureActual || path != null) {
			if (incloureActual) {
				if (path == null)
					path = new ArrayList<ContingutEntity>();
				path.add(contingut);
			}
			boolean expedientArrelTrobat = false;
			for (ContingutEntity contingutPath: path) {
				if (!expedientArrelTrobat && contingutPath instanceof ExpedientEntity)
					expedientArrelTrobat = true;
				if (expedientArrelTrobat) {
					comprovarPermisosContingut(
							contingutPath,
							comprovarPermisRead,
							comprovarPermisWrite,
							comprovarPermisDelete);
				}
			}
		}
	}

	public void comprovarContingutArrelEsEscriptoriUsuariActual(
			EntitatEntity entitat,
			ContingutEntity contingut) {
		// Comprova que el contenidor arrel és un escriptori
		// i que pertany a l'usuari actual.
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		EscriptoriEntity escriptori = null;
		Object deproxied = HibernateHelper.deproxy(contingut);
		if (deproxied instanceof EscriptoriEntity) {
			escriptori = (EscriptoriEntity)deproxied;
		} else {
			List<ContingutEntity> path = getPathContingut(contingut);
			if (path != null) {
				deproxied = HibernateHelper.deproxy(path.get(0));
				if (deproxied instanceof EscriptoriEntity)
					escriptori = (EscriptoriEntity)deproxied;
			}
		}
		if (	escriptori == null ||
				escriptori.getUsuari() == null ||
				!escriptori.getUsuari().getCodi().equals(auth.getName()) ||
				!escriptori.getEntitat().equals(entitat)) {
			throw new SecurityException("El contingut no està situat a l'escriptori de l'usuari actual ("
						+ "id=" + contingut.getId() + ", "
						+ "entitatId=" + entitat.getId() + ", "
						+ "usuari=" + auth.getName() + ")");
		}
	}

	public ExpedientEntity getExpedientSuperior(
			ContingutEntity contingut,
			boolean incloureActual,
			boolean comprovarPermisRead,
			boolean comprovarPermisWrite) {
		ExpedientEntity expedientSuperior = null;
		if (incloureActual && contingut instanceof ExpedientEntity) {
			expedientSuperior = (ExpedientEntity)contingut;
		} else {
			List<ContingutEntity> path = getPathContingut(contingut);
			// Si el contenidor és arrel el path retorna null i s'ha de comprovar
			if (path != null) {
				List<ContingutEntity> pathInvers = new ArrayList<ContingutEntity>(path);
				Collections.reverse(pathInvers);
				for (ContingutEntity contenidorPath: pathInvers) {
					if (contenidorPath instanceof ExpedientEntity) {
						expedientSuperior = (ExpedientEntity)contenidorPath;
						break;
					}
				}
			}
		}
		if (expedientSuperior != null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (comprovarPermisRead) {
				boolean granted = permisosHelper.isGrantedAll(
						expedientSuperior.getMetaExpedient().getId(),
						MetaNodeEntity.class,
						new Permission[] {ExtendedPermission.READ},
						auth);
				if (!granted) {
					throw new PermissionDeniedException(
							expedientSuperior.getMetaExpedient().getId(),
							MetaExpedientEntity.class,
							auth.getName(),
							"READ");
				}
			}
			if (comprovarPermisWrite) {
				boolean granted = permisosHelper.isGrantedAll(
						expedientSuperior.getMetaExpedient().getId(),
						MetaNodeEntity.class,
						new Permission[] {ExtendedPermission.WRITE},
						auth);
				if (!granted) {
					throw new PermissionDeniedException(
							expedientSuperior.getMetaExpedient().getId(),
							MetaExpedientEntity.class,
							auth.getName(),
							"WRITE");
				}
			}
		}
		return expedientSuperior;
	}

	public void calcularSequenciaExpedient(
			ExpedientEntity expedient,
			Integer any) {
		int anyExpedient;
		if (any != null)
			anyExpedient = any.intValue();
		else
			anyExpedient = Calendar.getInstance().get(Calendar.YEAR);
		MetaExpedientSequenciaEntity sequencia = metaExpedientSequenciaRepository.findByMetaExpedientAndAny(
				expedient.getMetaExpedient(),
				anyExpedient);
		if (sequencia == null) {
			sequencia = MetaExpedientSequenciaEntity.getBuilder(
					anyExpedient,
					expedient.getMetaExpedient()).build();
			metaExpedientSequenciaRepository.save(sequencia);
		}
		long sequenciaExpedient = sequencia.getValor();
		sequencia.incrementar();
		expedient.updateAnySequenciaCodi(
				anyExpedient,
				sequenciaExpedient,
				expedient.getMetaExpedient().getCodi());
	}

	public ExpedientEntity crearNouExpedient(
			String nom,
			MetaExpedientEntity metaExpedient,
			ArxiuEntity arxiu,
			ContingutEntity pare,
			EntitatEntity entitat,
			String ntiVersion,
			String ntiOrgano,
			Date ntiFechaApertura,
			Integer any) {
		ExpedientEntity expedientCrear = ExpedientEntity.getBuilder(
				nom,
				metaExpedient,
				arxiu,
				pare,
				entitat,
				"1.0",
				ntiOrgano,
				ntiFechaApertura,
				metaExpedient.getClassificacioSia()).build();
		// Calcula en número del nou expedient
		calcularSequenciaExpedient(
				expedientCrear,
				any);
		ExpedientEntity expedientCreat = expedientRepository.save(expedientCrear);
		// Calcula l'identificador del nou expedient
		calcularIdentificadorExpedient(
				expedientCreat,
				entitat.getUnitatArrel(),
				any);
		return expedientCreat;
	}

	public void calcularIdentificadorExpedient(
			ExpedientEntity expedient,
			String organCodi,
			Integer any) {
		int anyExpedient;
		if (any != null)
			anyExpedient = any.intValue();
		else
			anyExpedient = Calendar.getInstance().get(Calendar.YEAR);
		String ntiIdentificador = "ES_" + organCodi + "_" + anyExpedient + "_EXP_RIP" + String.format("%027d", expedient.getId());
		expedient.updateNtiIdentificador(ntiIdentificador);
	}

	public EscriptoriEntity getEscriptoriPerUsuari(
			EntitatEntity entitat,
			UsuariEntity usuari) {
		EscriptoriEntity escriptori = escriptoriRepository.findByEntitatAndUsuari(
				entitat,
				usuari);
		if (escriptori == null) {
			logger.debug("No s'ha trobat l'escriptori i se'n crea un de nou");
			escriptori = escriptoriRepository.save(
					EscriptoriEntity.getBuilder(
							usuari,
							entitat).build());
			pluginHelper.arxiuEscriptoriCrear(escriptori);
		}
		return escriptori;
	}

	public long[] countFillsAmbPermisReadByContinguts(
			EntitatEntity entitat,
			List<? extends ContingutEntity> continguts,
			boolean comprovarPermisos) {
		long[] resposta = new long[continguts.size()];
		if (!continguts.isEmpty()) {
			List<Object[]> countFillsTotals = contingutRepository.countByPares(
					entitat,
					continguts);
			List<Object[]> countNodesTotals = nodeRepository.countByPares(
					entitat,
					continguts);
			List<Object[]> countNodesByPares;
			if (comprovarPermisos) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				List<MetaNodeEntity> metaNodesPermesos = metaNodeRepository.findByEntitat(entitat);
				permisosHelper.filterGrantedAll(
						metaNodesPermesos,
						new ObjectIdentifierExtractor<MetaNodeEntity>() {
							public Long getObjectIdentifier(MetaNodeEntity metaNode) {
								return metaNode.getId();
							}
						},
						MetaNodeEntity.class,
						new Permission[] {ExtendedPermission.READ},
						auth);
				if (!metaNodesPermesos.isEmpty()) {
					countNodesByPares = nodeRepository.countAmbPermisReadByPares(
							entitat,
							continguts,
							metaNodesPermesos);
				} else {
					countNodesByPares = new ArrayList<Object[]>();
				}
			} else {
				countNodesByPares = nodeRepository.countByPares(
						entitat,
						continguts);
			}
			for (int i = 0; i < continguts.size(); i++) {
				ContingutEntity contingut = continguts.get(i);
				Long total = getCountByContingut(
						contingut,
						countFillsTotals);
				Long totalNodes = getCountByContingut(
						contingut,
						countNodesTotals);
				Long totalNodesPermisRead = getCountByContingut(
						contingut,
						countNodesByPares);
				resposta[i] = total - totalNodes + totalNodesPermisRead;
				
			}
		}
		return resposta;
	}

	public Set<String> findUsuarisAmbPermisReadPerContenidor(
			ContingutEntity contingut) {
		List<PermisDto> permisos = new ArrayList<PermisDto>();
		if (contingut instanceof BustiaEntity) {
			permisos = permisosHelper.findPermisos(
					contingut.getId(),
					BustiaEntity.class);
		} else if (contingut instanceof NodeEntity) {
			NodeEntity node = (NodeEntity)contingut;
			permisos = permisosHelper.findPermisos(
					node.getMetaNode().getId(),
					BustiaEntity.class);
		}
		Set<String> usuaris = new HashSet<String>();
		for (PermisDto permis: permisos) {
			switch (permis.getPrincipalTipus()) {
			case USUARI:
				usuaris.add(permis.getPrincipalNom());
				break;
			case ROL:
				List<DadesUsuari> usuarisGrup = pluginHelper.dadesUsuariFindAmbGrup(
						permis.getPrincipalNom());
				if (usuarisGrup != null) {
					for (DadesUsuari usuariGrup: usuarisGrup) {
						usuaris.add(usuariGrup.getCodi());
					}
				}
				break;
			}
		}
		return usuaris;
	}

	public ContingutMovimentEntity ferIEnregistrarMoviment(
			ContingutEntity contingut,
			ContingutEntity desti,
			String comentari) {
		UsuariEntity usuariAutenticat = usuariHelper.getUsuariAutenticat();
		if (usuariAutenticat == null && contingut.getDarrerMoviment() != null)
			usuariHelper.generarUsuariAutenticat(
					contingut.getDarrerMoviment().getRemitent().getCodi(), 
					true);
		
		ContingutMovimentEntity contenidorMoviment = ContingutMovimentEntity.getBuilder(
				contingut,
				contingut.getPare(),
				desti,
				usuariHelper.getUsuariAutenticat(),
				comentari).build();
		contingut.updateDarrerMoviment(
				contenidorMovimentRepository.save(contenidorMoviment));
		contingut.updatePare(desti);
		return contenidorMoviment;
	}

	public ContingutEntity findContingutArrel(
			ContingutEntity contingut) {
		ContingutEntity contingutActual = contingut;
		while (contingutActual != null && contingutActual.getPare() != null) {
			contingutActual = contingutActual.getPare();
		}
		return contingutRepository.findOne(contingutActual.getId());
	}

	public boolean isNomValid(String nom) {
		return !nom.startsWith(".");
	}

	public void arxiuPropagarModificacio(
			ContingutEntity contingut,
			ExpedientEntity expedientSuperior,
			FitxerDto fitxer) {
		String serieDocumental = null;
		if (expedientSuperior != null) {
			serieDocumental = expedientSuperior.getMetaExpedient().getSerieDocumental();
		}
		if (pluginHelper.isArxiuPluginActiu()) {
			if (contingut instanceof ExpedientEntity) {
				pluginHelper.arxiuExpedientActualitzar(
						(ExpedientEntity)contingut);
			} else if (contingut instanceof DocumentEntity) {
				pluginHelper.arxiuDocumentActualitzar(
						(DocumentEntity)contingut,
						fitxer,
						contingut.getPare(),
						serieDocumental);
				documentHelper.actualitzarVersionsDocument((DocumentEntity)contingut);
			} else if (contingut instanceof CarpetaEntity) {
				pluginHelper.arxiuCarpetaActualitzar(
						(CarpetaEntity)contingut,
						contingut.getPare());
			} else {
				throw new ValidationException(
						contingut.getId(),
						ContingutEntity.class,
						"El contingut que es vol propagar a l'arxiu no és del tipus expedient, document o carpeta");
			}
		}
	}

	public void arxiuPropagarEliminacio(
			ContingutEntity contingut,
			ExpedientEntity expedientSuperior) {
		if (contingut.getArxiuUuid() != null) {
			if (pluginHelper.isArxiuPluginActiu()) {
				if (contingut instanceof ExpedientEntity) {
					pluginHelper.arxiuExpedientEsborrar(
							(ExpedientEntity)contingut);
				} else if (contingut instanceof DocumentEntity) {
					pluginHelper.arxiuDocumentEsborrar(
							(DocumentEntity)contingut);
				} else if (contingut instanceof CarpetaEntity) {
					pluginHelper.arxiuCarpetaEsborrar(
							(CarpetaEntity)contingut);
				} else {
					throw new ValidationException(
							contingut.getId(),
							ContingutEntity.class,
							"El contingut que es vol esborrar de l'arxiu no és del tipus expedient, document o carpeta");
				}
			} else {
				throw new ValidationException(
						contingut.getId(),
						ContingutEntity.class,
						"S'ha d'esborrar un contingut de l'arxiu però el plugin no està habilitat");
			}
		}
	}

	public void arxiuPropagarCopia(
			ContingutEntity contingut,
			ContingutEntity desti) {
		if (contingut instanceof DocumentEntity) {
			pluginHelper.arxiuDocumentCopiar(
					(DocumentEntity)contingut,
					desti.getArxiuUuid());
		} else if (contingut instanceof CarpetaEntity) {
			pluginHelper.arxiuCarpetaCopiar(
					(CarpetaEntity)contingut,
					desti.getArxiuUuid());
		}
	}

	public void arxiuPropagarMoviment(
			ContingutEntity contingut,
			ContingutEntity desti) {
		if (contingut instanceof DocumentEntity) {
			pluginHelper.arxiuDocumentMoure(
					(DocumentEntity)contingut,
					desti.getArxiuUuid());
		} else if (contingut instanceof CarpetaEntity) {
			pluginHelper.arxiuCarpetaMoure(
					(CarpetaEntity)contingut,
					desti.getArxiuUuid());
		}
	}



	private List<ContingutEntity> getPathContingut(
			ContingutEntity contingut) {
		List<ContingutEntity> path = null;
		ContingutEntity contingutActual = contingut;
		while (contingutActual != null && contingutActual.getPare() != null) {
			if (path == null)
				path = new ArrayList<ContingutEntity>();
			ContingutEntity c = contingutRepository.findOne(contingutActual.getPare().getId());
			path.add(c);
			contingutActual = c;
		}
		if (path != null) {
			Collections.reverse(path);
		}
		return path;
	}

	public List<ContingutDto> getPathContingutComDto(
			ContingutEntity contingut,
			boolean ambPermisos,
			boolean nomesFinsExpedientArrel) {
		List<ContingutEntity> path = getPathContingut(contingut);
		List<ContingutDto> pathDto = null;
		if (path != null) {
			pathDto = new ArrayList<ContingutDto>();
			boolean expedientArrelTrobat = !nomesFinsExpedientArrel;
			for (ContingutEntity contingutPath: path) {
				if (!expedientArrelTrobat && contingutPath instanceof ExpedientEntity)
					expedientArrelTrobat = true;
				if (expedientArrelTrobat) {
					pathDto.add(
						toContingutDto(
								contingutPath,
								ambPermisos,
								false,
								false,
								false,
								false,
								false,
								false));
				}
			}
		}
		return pathDto;
	}
	
	public void tractarInteressats(List<RegistreInteressat> interessats) {
		ListIterator<RegistreInteressat> iter = interessats.listIterator();
		while(iter.hasNext()){
		    if(iter.next().getRepresentat() != null){
		        iter.remove();
		    }
		}
	}

	private Long getCountByContingut(
			ContingutEntity contingut,
			List<Object[]> counts) {
		for (Object[] count: counts) {
			Long contingutId = (Long)count[0];
			if (contingutId.equals(contingut.getId())) {
				return (Long)count[1];
			}
		}
		return new Long(0);
	}

	private static final Logger logger = LoggerFactory.getLogger(ContingutHelper.class);

}
