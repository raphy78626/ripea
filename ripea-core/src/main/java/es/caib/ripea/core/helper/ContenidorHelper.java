/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.ArxiuDto;
import es.caib.ripea.core.api.dto.BustiaDto;
import es.caib.ripea.core.api.dto.CarpetaDto;
import es.caib.ripea.core.api.dto.CarpetaTipusEnumDto;
import es.caib.ripea.core.api.dto.ContenidorDto;
import es.caib.ripea.core.api.dto.DadaDto;
import es.caib.ripea.core.api.dto.DocumentDto;
import es.caib.ripea.core.api.dto.DocumentVersioDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.EscriptoriDto;
import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.dto.MetaDocumentDto;
import es.caib.ripea.core.api.dto.MetaExpedientDto;
import es.caib.ripea.core.api.dto.MetaNodeDto;
import es.caib.ripea.core.api.dto.NodeDto;
import es.caib.ripea.core.api.dto.PermisDto;
import es.caib.ripea.core.api.dto.RegistreAnotacioDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.dto.UsuariDto;
import es.caib.ripea.core.entity.ArxiuEntity;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.CarpetaEntity;
import es.caib.ripea.core.entity.ContenidorEntity;
import es.caib.ripea.core.entity.ContenidorMovimentEntity;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.EscriptoriEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.MetaNodeEntity;
import es.caib.ripea.core.entity.NodeEntity;
import es.caib.ripea.core.entity.UsuariEntity;
import es.caib.ripea.core.helper.PermisosHelper.ObjectIdentifierExtractor;
import es.caib.ripea.core.repository.ContenidorMovimentRepository;
import es.caib.ripea.core.repository.ContenidorRepository;
import es.caib.ripea.core.repository.DadaRepository;
import es.caib.ripea.core.repository.DocumentRepository;
import es.caib.ripea.core.repository.DocumentVersioRepository;
import es.caib.ripea.core.repository.EscriptoriRepository;
import es.caib.ripea.core.repository.MetaDadaRepository;
import es.caib.ripea.core.repository.MetaDocumentRepository;
import es.caib.ripea.core.repository.MetaExpedientMetaDocumentRepository;
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
public class ContenidorHelper {

	@Resource
	private ContenidorRepository contenidorRepository;
	@Resource
	private EscriptoriRepository escriptoriRepository;
	@Resource
	private NodeRepository nodeRepository;
	@Resource
	private DadaRepository dadaRepository;
	@Resource
	private DocumentRepository documentRepository;
	@Resource
	private DocumentVersioRepository documentVersioRepository;
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
	private ContenidorMovimentRepository contenidorMovimentRepository;

	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private MetaNodeHelper metaNodeHelper;
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



	public ContenidorDto toContenidorDto(
			ContenidorEntity contenidor) {
		return toContenidorDto(
				contenidor,
				false,
				false,
				false,
				false,
				false,
				false);
	}
	public ContenidorDto toContenidorDto(
			ContenidorEntity contenidor,
			boolean ambPermisos,
			boolean ambFills,
			boolean filtrarFillsSegonsPermisRead,
			boolean ambDades,
			boolean ambPath,
			boolean pathNomesFinsExpedientArrel) {
		ContenidorDto resposta = null;
		MetaNodeDto metaNode = null;
		// Crea el contenidor del tipus correcte
		Object deproxied = HibernateHelper.deproxy(contenidor);
		if (deproxied instanceof EscriptoriEntity) {
			EscriptoriDto dto = new EscriptoriDto();
			resposta = dto;
		} else if (deproxied instanceof ExpedientEntity) {
			ExpedientEntity expedient = (ExpedientEntity)deproxied;
			ExpedientDto dto = new ExpedientDto();
			dto.setObert(expedient.isObert());
			dto.setTancatMotiu(expedient.getTancatMotiu());
			dto.setAny(expedient.getAny());
			dto.setSequencia(expedient.getSequencia());
			dto.setArxiu((ArxiuDto)toContenidorDto(expedient.getArxiu()));
			metaNode = conversioTipusHelper.convertir(
					expedient.getMetaNode(),
					MetaExpedientDto.class);
			dto.setMetaNode(metaNode);
			dto.setValid(
					cacheHelper.findErrorsValidacioPerNode(expedient).isEmpty());
			resposta = dto;
		} else if (deproxied instanceof CarpetaEntity) {
			CarpetaEntity carpeta = (CarpetaEntity)deproxied;
			CarpetaDto dto = new CarpetaDto();
			dto.setTipus(CarpetaTipusEnumDto.valueOf(carpeta.getTipus().name()));
			resposta = dto;
		} else if (deproxied instanceof DocumentEntity) {
			DocumentEntity document = (DocumentEntity)deproxied;
			DocumentDto dto = new DocumentDto();
			dto.setData(document.getData());
			dto.setDarreraVersio(
					conversioTipusHelper.convertir(
							documentVersioRepository.findByDocumentAndVersio(
									document,
									document.getDarreraVersio()),
							DocumentVersioDto.class));
			metaNode = conversioTipusHelper.convertir(
					document.getMetaNode(),
					MetaDocumentDto.class);
			dto.setMetaNode(metaNode);
			dto.setValid(
					cacheHelper.findErrorsValidacioPerNode(document).isEmpty());
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
			UnitatOrganitzativaDto unitatConselleria = unitatOrganitzativaHelper.findConselleriaPerUnitatOrganitzativa(
					bustia.getEntitat().getCodi(),
					bustia.getUnitatCodi());
			if (unitatConselleria != null)
				dto.setUnitatConselleriaCodi(unitatConselleria.getCodi());
			resposta = dto;
		}
		resposta.setId(contenidor.getId());
		resposta.setNom(contenidor.getNom());
		resposta.setEsborrat(contenidor.getEsborrat());
		resposta.setEntitat(
				conversioTipusHelper.convertir(
							contenidor.getEntitat(),
							EntitatDto.class));
		if (contenidor.getDarrerMoviment() != null) {
			ContenidorMovimentEntity darrerMoviment = contenidor.getDarrerMoviment();
			resposta.setDarrerMovimentUsuari(
					conversioTipusHelper.convertir(
							darrerMoviment.getRemitent(),
							UsuariDto.class));
			resposta.setDarrerMovimentData(darrerMoviment.getData());
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
							contenidor.getCreatedBy(),
							UsuariDto.class));
			resposta.setCreatedDate(contenidor.getCreatedDate().toDate());
			resposta.setLastModifiedBy(
					conversioTipusHelper.convertir(
							contenidor.getLastModifiedBy(),
							UsuariDto.class));
			resposta.setLastModifiedDate(contenidor.getLastModifiedDate().toDate());
		}
		if (resposta != null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (ambPath) {
				// Calcula el path
				List<ContenidorDto> path = getPathContenidorComDto(
						contenidor,
						ambPermisos,
						pathNomesFinsExpedientArrel);
				resposta.setPath(path);
			}
			if (ambFills) {
				// Cerca els nodes fills
				List<ContenidorDto> contenidorDtos = new ArrayList<ContenidorDto>();
				List<ContenidorEntity> fills = contenidorRepository.findByPareAndEsborrat(
						contenidor,
						0,
						new Sort("createdDate"));
				if (filtrarFillsSegonsPermisRead) {
					// Filtra els fills que no tenen permis de lectura
					Iterator<ContenidorEntity> it = fills.iterator();
					while (it.hasNext()) {
						ContenidorEntity c = it.next();
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
				List<ContenidorDto> fillPath = null;
				if (ambPath) {
					fillPath = new ArrayList<ContenidorDto>();
					if (resposta.getPath() != null)
						fillPath.addAll(resposta.getPath());
					fillPath.add(toContenidorDto(
							contenidor,
							false,
							false,
							false,
							false,
							false,
							false));
				}
				for (ContenidorEntity fill: fills) {
					boolean esCarpetaNouvinguts = fill instanceof CarpetaEntity && CarpetaTipusEnumDto.NOUVINGUT.equals(((CarpetaEntity)fill).getTipus());
					if (fill.getEsborrat() == 0 && !esCarpetaNouvinguts) {
						ContenidorDto fillDto = toContenidorDto(
								fill,
								ambPermisos,
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
				// Incorpora les anotacions de registre
				resposta.setRegistres(
						conversioTipusHelper.convertirList(
								registreRepository.findByContenidorAndMotiuRebuigNullOrderByDataAsc(
										contenidor),
								RegistreAnotacioDto.class));
			}
			if (ambDades) {
				// Omple les dades
				if (contenidor instanceof NodeEntity) {
					NodeEntity node = (NodeEntity)contenidor;
					((NodeDto)resposta).setDades(
							conversioTipusHelper.convertirList(
									dadaRepository.findByNode(node),
									DadaDto.class));
				}
			}
		}
		return resposta;
	}

	public void comprovarPermisosContenidor(
			ContenidorEntity contenidor,
			boolean comprovarPermisRead,
			boolean comprovarPermisWrite,
			boolean comprovarPermisDelete) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// Comprova els permisos del contenidor actual
		if (contenidor instanceof NodeEntity) {
			// Si el contenidor és de tipus node
			NodeEntity node = (NodeEntity)contenidor;
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
		} else if (contenidor instanceof EscriptoriEntity) {
			EscriptoriEntity escriptori = (EscriptoriEntity)contenidor;
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
		} else if (contenidor instanceof ArxiuEntity) {
			ArxiuEntity arxiu = (ArxiuEntity)contenidor;
			if (arxiu.getPare() != null && comprovarPermisRead) {
				boolean granted = permisosHelper.isGrantedAll(
						arxiu.getId(),
						ArxiuEntity.class,
						new Permission[] {ExtendedPermission.READ},
						auth);
				if (!granted) {
					logger.debug("Sense permisos per a accedir a l'arxiu ("
							+ "id=" + arxiu.getId() + ", "
							+ "usuari=" + auth.getName() + ")");
					throw new SecurityException("Sense permisos per accedir a l'arxiu ("
							+ "id=" + arxiu.getId() + ", "
							+ "usuari=" + auth.getName() + ")");
				}
			}
		} else if (contenidor instanceof BustiaEntity) {
			BustiaEntity bustia = (BustiaEntity)contenidor;
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
	public void comprovarPermisosPathContenidor(
			ContenidorEntity contenidor,
			boolean comprovarPermisRead,
			boolean comprovarPermisWrite,
			boolean comprovarPermisDelete,
			boolean incloureActual) {
		List<ContenidorEntity> path = getPathContenidor(contenidor);
		if (path != null) {
			// Dels contenidors del path només comprova el permis read
			for (ContenidorEntity contenidorPath: path) {
				comprovarPermisosContenidor(
						contenidorPath,
						true,
						false,
						false);
			}
		}
		if (incloureActual) {
			// Del contenidor en qüestió comprova tots els permisos
			comprovarPermisosContenidor(
					contenidor,
					comprovarPermisRead,
					comprovarPermisWrite,
					comprovarPermisDelete);
		}
	}
	public void comprovarPermisosFinsExpedientArrel(
			ContenidorEntity contenidor,
			boolean comprovarPermisRead,
			boolean comprovarPermisWrite,
			boolean comprovarPermisDelete,
			boolean incloureActual) {
		List<ContenidorEntity> path = getPathContenidor(contenidor);
		if (incloureActual || path != null) {
			if (incloureActual) {
				if (path == null)
					path = new ArrayList<ContenidorEntity>();
				path.add(contenidor);
			}
			boolean expedientArrelTrobat = false;
			for (ContenidorEntity contenidorPath: path) {
				if (!expedientArrelTrobat && contenidorPath instanceof ExpedientEntity)
					expedientArrelTrobat = true;
				if (expedientArrelTrobat) {
					comprovarPermisosContenidor(
							contenidorPath,
							comprovarPermisRead,
							comprovarPermisWrite,
							comprovarPermisDelete);
				}
			}
		}
	}

	public void comprovarContenidorArrelEsEscriptoriUsuariActual(
			EntitatEntity entitat,
			ContenidorEntity contenidor) {
		// Comprova que el contenidor arrel és un escriptori
		// i que pertany a l'usuari actual.
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		EscriptoriEntity escriptori = null;
		Object deproxied = HibernateHelper.deproxy(contenidor);
		if (deproxied instanceof EscriptoriEntity) {
			escriptori = (EscriptoriEntity)deproxied;
		} else {
			List<ContenidorEntity> path = getPathContenidor(contenidor);
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
			throw new SecurityException("El contenidor no pertany a l'escriptori ("
						+ "id=" + contenidor.getId() + ", "
						+ "entitatId=" + entitat.getId() + ", "
						+ "usuari=" + auth.getName() + ")");
		}
	}

	public ExpedientEntity getExpedientSuperior(
			ContenidorEntity contenidor,
			boolean incloureActual) {
		ExpedientEntity expedientSuperior = null;
		if (incloureActual && contenidor instanceof ExpedientEntity) {
			expedientSuperior = (ExpedientEntity)contenidor;
		} else {
			List<ContenidorEntity> path = getPathContenidor(contenidor);
			// Si el contenidor és arrel el path retorna null i s'ha de comprovar
			if (path != null) {
				List<ContenidorEntity> pathInvers = new ArrayList<ContenidorEntity>(path);
				Collections.reverse(pathInvers);
				for (ContenidorEntity contenidorPath: pathInvers) {
					if (contenidorPath instanceof ExpedientEntity) {
						expedientSuperior = (ExpedientEntity)contenidorPath;
						break;
					}
				}
			}
		}
		return expedientSuperior;
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
		}
		return escriptori;
	}

	public long[] countFillsAmbPermisReadByContenidors(
			EntitatEntity entitat,
			List<? extends ContenidorEntity> contenidors,
			boolean comprovarPermisos) {
		long[] resposta = new long[contenidors.size()];
		if (!contenidors.isEmpty()) {
			List<Object[]> countFillsTotals = contenidorRepository.countByPares(
					entitat,
					contenidors);
			List<Object[]> countNodesTotals = nodeRepository.countByPares(
					entitat,
					contenidors);
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
							contenidors,
							metaNodesPermesos);
				} else {
					countNodesByPares = new ArrayList<Object[]>();
				}
			} else {
				countNodesByPares = nodeRepository.countByPares(
						entitat,
						contenidors);
			}
			for (int i = 0; i < contenidors.size(); i++) {
				ContenidorEntity contenidor = contenidors.get(i);
				Long total = getCountByContenidor(
						contenidor,
						countFillsTotals);
				Long totalNodes = getCountByContenidor(
						contenidor,
						countNodesTotals);
				Long totalNodesPermisRead = getCountByContenidor(
						contenidor,
						countNodesByPares);
				resposta[i] = total - totalNodes + totalNodesPermisRead;
				
			}
		}
		return resposta;
	}

	public long[] countRegistresByContenidors(
			EntitatEntity entitat,
			List<? extends ContenidorEntity> contenidors) {
		List<Object[]> countRegistres = registreRepository.countByContenidorsAndNotRebutjat(
				contenidors);
		long[] resposta = new long[contenidors.size()];
		for (int i = 0; i < contenidors.size(); i++) {
			ContenidorEntity contenidor = contenidors.get(i);
			resposta[i] = getCountByContenidor(
					contenidor,
					countRegistres);
		}
		return resposta;
	}

	public Set<String> findUsuarisAmbPermisReadPerContenidor(
			ContenidorEntity contenidor) {
		List<PermisDto> permisos = null;
		if (contenidor instanceof BustiaEntity) {
			permisos = permisosHelper.findPermisos(
					contenidor.getId(),
					BustiaEntity.class);
		} else if (contenidor instanceof NodeEntity) {
			NodeEntity node = (NodeEntity)contenidor;
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
				List<DadesUsuari> usuarisGrup = pluginHelper.dadesUsuariConsultarAmbGrup(
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

	public ContenidorMovimentEntity ferIEnregistrarMovimentContenidor(
			ContenidorEntity contenidor,
			ContenidorEntity desti,
			String comentari) {
		ContenidorMovimentEntity contenidorMoviment = ContenidorMovimentEntity.getBuilder(
				contenidor,
				contenidor.getPare(),
				desti,
				usuariHelper.getUsuariAutenticat(),
				comentari).build();
		contenidor.updateDarrerMoviment(
				contenidorMovimentRepository.save(contenidorMoviment));
		contenidor.updatePare(desti);
		return contenidorMoviment;
	}

	public ContenidorEntity findContenidorArrel(
			ContenidorEntity contenidor) {
		ContenidorEntity contenidorActual = contenidor;
		while (contenidorActual != null && contenidorActual.getPare() != null) {
			contenidorActual = contenidorActual.getPare();
		}
		return contenidorRepository.findOne(contenidorActual.getId());
	}

	public boolean isNomValid(String nom) {
		return !nom.startsWith(".");
	}



	private List<ContenidorEntity> getPathContenidor(
			ContenidorEntity contenidor) {
		List<ContenidorEntity> path = null;
		ContenidorEntity contenidorActual = contenidor;
		while (contenidorActual != null && contenidorActual.getPare() != null) {
			if (path == null)
				path = new ArrayList<ContenidorEntity>();
			ContenidorEntity c = contenidorRepository.findOne(contenidorActual.getPare().getId());
			path.add(c);
			contenidorActual = c;
		}
		if (path != null) {
			Collections.reverse(path);
		}
		return path;
	}

	private List<ContenidorDto> getPathContenidorComDto(
			ContenidorEntity contenidor,
			boolean ambPermisos,
			boolean nomesFinsExpedientArrel) {
		List<ContenidorEntity> path = getPathContenidor(contenidor);
		List<ContenidorDto> pathDto = null;
		if (path != null) {
			pathDto = new ArrayList<ContenidorDto>();
			boolean expedientArrelTrobat = !nomesFinsExpedientArrel;
			for (ContenidorEntity contenidorPath: path) {
				if (!expedientArrelTrobat && contenidorPath instanceof ExpedientEntity)
					expedientArrelTrobat = true;
				if (expedientArrelTrobat) {
					pathDto.add(
						toContenidorDto(
								contenidorPath,
								ambPermisos,
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

	private Long getCountByContenidor(
			ContenidorEntity contenidor,
			List<Object[]> counts) {
		for (Object[] count: counts) {
			Long contenidorId = (Long)count[0];
			if (contenidorId.equals(contenidor.getId())) {
				return (Long)count[1];
			}
		}
		return new Long(0);
	}

	private static final Logger logger = LoggerFactory.getLogger(ContenidorHelper.class);

}
