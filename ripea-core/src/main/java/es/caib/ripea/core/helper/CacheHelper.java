/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.ArbreDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.MetaDadaDto;
import es.caib.ripea.core.api.dto.MetaDocumentDto;
import es.caib.ripea.core.api.dto.MultiplicitatEnumDto;
import es.caib.ripea.core.api.dto.MunicipiDto;
import es.caib.ripea.core.api.dto.PaisDto;
import es.caib.ripea.core.api.dto.ProvinciaDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.dto.ValidacioErrorDto;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.DadaEntity;
import es.caib.ripea.core.entity.DocumentEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.ExpedientEntity;
import es.caib.ripea.core.entity.MetaDadaEntity;
import es.caib.ripea.core.entity.MetaDocumentEntity;
import es.caib.ripea.core.entity.MetaExpedientMetaDocumentEntity;
import es.caib.ripea.core.entity.MetaNodeMetaDadaEntity;
import es.caib.ripea.core.entity.NodeEntity;
import es.caib.ripea.core.helper.PermisosHelper.ObjectIdentifierExtractor;
import es.caib.ripea.core.repository.BustiaRepository;
import es.caib.ripea.core.repository.DadaRepository;
import es.caib.ripea.core.repository.DocumentRepository;
import es.caib.ripea.core.repository.EntitatRepository;
import es.caib.ripea.core.repository.MetaDadaRepository;
import es.caib.ripea.core.repository.MetaDocumentRepository;
import es.caib.ripea.core.repository.MetaExpedientMetaDocumentRepository;
import es.caib.ripea.core.repository.MetaNodeMetaDadaRepository;
import es.caib.ripea.core.security.ExtendedPermission;
import es.caib.ripea.plugin.usuari.DadesUsuari;

/**
 * Utilitat per a accedir a les caches. Els mètodes cacheables es
 * defineixen aquí per evitar la impossibilitat de fer funcionar
 * l'anotació @Cacheable als mètodes privats degut a limitacions
 * AOP.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class CacheHelper {

	@Resource
	private EntitatRepository entitatRepository;
	@Resource
	private DadaRepository dadaRepository;
	@Resource
	private DocumentRepository documentRepository;
	@Resource
	private MetaDadaRepository metaDadaRepository;
	@Resource
	private MetaDocumentRepository metaDocumentRepository;
	@Resource
	private MetaNodeMetaDadaRepository metaNodeMetaDadaRepository;
	@Resource
	private MetaExpedientMetaDocumentRepository metaExpedientMetaDocumentRepository;
	@Resource
	private BustiaRepository bustiaRepository;

	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private ContingutHelper contenidorHelper;
	@Resource
	private PermisosHelper permisosHelper;
	@Resource
	private PermisosEntitatHelper permisosEntitatHelper;
	@Resource
	private PluginHelper pluginHelper;
	@Resource
	private UsuariHelper usuariHelper;
	@Resource
	private DadesExternesHelper dadesExternesHelper;

	private Map<String, Set<String>> usuarisElementsPendentsPerEntitat;



	@Cacheable(value = "entitatsUsuari", key="#usuariCodi")
	public List<EntitatDto> findEntitatsAccessiblesUsuari(String usuariCodi) {
		logger.debug("Consulta entitats accessibles (usuariCodi=" + usuariCodi + ")");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<EntitatEntity> entitats = entitatRepository.findByActiva(true);
		permisosHelper.filterGrantedAny(
				entitats,
				new ObjectIdentifierExtractor<EntitatEntity>() {
					public Long getObjectIdentifier(EntitatEntity entitat) {
						return entitat.getId();
					}
				},
				EntitatEntity.class,
				new Permission[] {
					ExtendedPermission.READ,
					ExtendedPermission.ADMINISTRATION},
				auth);
		List<EntitatDto> resposta = conversioTipusHelper.convertirList(
				entitats,
				EntitatDto.class);
		permisosEntitatHelper.omplirPermisosPerEntitats(
				resposta,
				false);
		return resposta;
	}
	@CacheEvict(value = "entitatsUsuari", key="#usuariCodi")
	public void evictEntitatsAccessiblesUsuari(String usuariCodi) {
	}

	@Cacheable(value = "errorsValidacioNode", key = "#node.id")
	public List<ValidacioErrorDto> findErrorsValidacioPerNode(
			NodeEntity node) {
		logger.debug("Consulta dels errors de validació pel node (nodeId=" + node.getId() + ")");
		List<ValidacioErrorDto> errors = new ArrayList<ValidacioErrorDto>();
		List<DadaEntity> dades = dadaRepository.findByNode(node);
		// Valida dades globals
		List<MetaDadaEntity> metaDadesGlobals = null;
		if (node instanceof ExpedientEntity)
			metaDadesGlobals = metaDadaRepository.findByEntitatAndGlobalExpedientTrueAndActivaTrueOrderByNomAsc(
					node.getEntitat());
		else
			metaDadesGlobals = metaDadaRepository.findByEntitatAndGlobalDocumentTrueAndActivaTrueOrderByNomAsc(
					node.getEntitat());
		if (metaDadesGlobals != null) {
			for (MetaDadaEntity metaDada: metaDadesGlobals) {
				if (metaDada.getGlobalMultiplicitat().equals(MultiplicitatEnumDto.M_1) || metaDada.getGlobalMultiplicitat().equals(MultiplicitatEnumDto.M_1_N)) {
					boolean trobada = false;
					for (DadaEntity dada: dades) {
						if (dada.getMetaDada().equals(metaDada)) {
							trobada = true;
							break;
						}
					}
					if (!trobada)
						errors.add(
								crearValidacioError(
										metaDada,
										metaDada.getGlobalMultiplicitat()));
				}
			}
		}
		// Valida dades específiques del meta-node
		List<MetaNodeMetaDadaEntity> metaNodeMetaDades = metaNodeMetaDadaRepository.findByMetaNodeAndActivaTrue(node.getMetaNode());
		for (MetaNodeMetaDadaEntity metaNodeMetaDada: metaNodeMetaDades) {
			if (metaNodeMetaDada.getMultiplicitat().equals(MultiplicitatEnumDto.M_1) || metaNodeMetaDada.getMultiplicitat().equals(MultiplicitatEnumDto.M_1_N)) {
				boolean trobada = false;
				for (DadaEntity dada: dades) {
					if (dada.getMetaDada() != null && dada.getMetaDada().equals(metaNodeMetaDada.getMetaDada())) {
						trobada = true;
						break;
					}
				}
				if (!trobada)
					errors.add(
							crearValidacioError(
									metaNodeMetaDada.getMetaDada(),
									metaNodeMetaDada.getMultiplicitat()));
			}
		}
		if (node instanceof ExpedientEntity) {
			ExpedientEntity expedient = (ExpedientEntity)node;
			List<DocumentEntity> documents = documentRepository.findByExpedient(expedient);
			// Valida documents globals
			List<MetaDocumentEntity> metaDocumentsGlobals = metaDocumentRepository.findByEntitatAndGlobalExpedientTrueAndActiuTrueOrderByNomAsc(
					node.getEntitat());
			for (MetaDocumentEntity metaDocument: metaDocumentsGlobals) {
				if (metaDocument.getGlobalMultiplicitat().equals(MultiplicitatEnumDto.M_1) || metaDocument.getGlobalMultiplicitat().equals(MultiplicitatEnumDto.M_1_N)) {
					boolean trobat = false;
					for (DocumentEntity document: documents) {
						if (document.getMetaDocument() != null && document.getMetaDocument().equals(metaDocument)) {
							trobat = true;
							break;
						}
					}
					if (!trobat)
						errors.add(
								crearValidacioError(
										metaDocument,
										metaDocument.getGlobalMultiplicitat()));
				}
			}
			// Valida documents específics del meta-node
			List<MetaExpedientMetaDocumentEntity> metaExpedientMetaDocuments = metaExpedientMetaDocumentRepository.findByMetaExpedient(expedient.getMetaExpedient());
			for (MetaExpedientMetaDocumentEntity metaExpedientMetaDocument: metaExpedientMetaDocuments) {
				if (metaExpedientMetaDocument.getMultiplicitat().equals(MultiplicitatEnumDto.M_1) || metaExpedientMetaDocument.getMultiplicitat().equals(MultiplicitatEnumDto.M_1_N)) {
					boolean trobat = false;
					for (DocumentEntity document: documents) {
						if (document.getMetaDocument() != null && document.getMetaDocument().equals(metaExpedientMetaDocument.getMetaDocument())) {
							trobat = true;
							break;
						}
					}
					if (!trobat)
						errors.add(
								crearValidacioError(
										metaExpedientMetaDocument.getMetaDocument(),
										metaExpedientMetaDocument.getMultiplicitat()));
				}
			}
		}
		return errors;
	}
	@CacheEvict(value = "errorsValidacioNode", key = "#node.id")
	public void evictErrorsValidacioPerNode(
			NodeEntity node) {
	}

	@Cacheable(value = "usuariAmbCodi", key="#usuariCodi")
	public DadesUsuari findUsuariAmbCodi(
			String usuariCodi) {
		return pluginHelper.dadesUsuariConsultarAmbCodi(
				usuariCodi);
	}

	@Cacheable(value = "unitatsOrganitzatives", key="#entitatCodi")
	public ArbreDto<UnitatOrganitzativaDto> findUnitatsOrganitzativesPerEntitat(
			String entitatCodi) {
		EntitatEntity entitat = entitatRepository.findByCodi(entitatCodi);
		return pluginHelper.unitatsOrganitzativesFindArbreByPare(
				entitat.getUnitatArrel());
	}
	@CacheEvict(value = "unitatsOrganitzatives", key="#entitatCodi")
	public void evictUnitatsOrganitzativesPerEntitat(
			String entitatCodi) {
	}

	@Cacheable(value = "unitatOrganitzativa", key="#organCodi")
	public UnitatOrganitzativaDto findUnitatOrganitzativaPerCodi(
			String organCodi) {
		UnitatOrganitzativaDto unitat = pluginHelper.unitatsOrganitzativesFindByCodi(organCodi);
		if (unitat != null) {
			if (unitat.getCodiPais() != null && !"".equals(unitat.getCodiPais()))
				unitat.setCodiPais(("000" + unitat.getCodiPais()).substring(unitat.getCodiPais().length()));
			if(unitat.getCodiComunitat() != null && !"".equals(unitat.getCodiComunitat()))
				unitat.setCodiComunitat(("00" + unitat.getCodiComunitat()).substring(unitat.getCodiComunitat().length()));
			
			if ((unitat.getCodiProvincia() == null || "".equals(unitat.getCodiProvincia())) && 
					unitat.getCodiComunitat() != null && !"".equals(unitat.getCodiComunitat())) {
				List<ProvinciaDto> provincies = findProvinciesPerComunitat(unitat.getCodiComunitat());
				if (provincies != null && provincies.size() == 1) {
					unitat.setCodiProvincia(provincies.get(0).getCodi());
				}		
			}
			if (unitat.getCodiProvincia() != null && !"".equals(unitat.getCodiProvincia())) {
				unitat.setCodiProvincia(("00" + unitat.getCodiProvincia()).substring(unitat.getCodiProvincia().length()));
				
				if (unitat.getLocalitat() == null && unitat.getNomLocalitat() != null) {
					MunicipiDto municipi = findMunicipiAmbNom(
							unitat.getCodiProvincia(), 
							unitat.getNomLocalitat());
					if (municipi != null)
						unitat.setLocalitat(municipi.getCodi());
					else
						logger.error("UNITAT ORGANITZATIVA. No s'ha trobat la localitat amb el nom: '" + unitat.getNomLocalitat() + "'");
				}
			}
		}
		return unitat;
	}

	@Cacheable(value = "elementsPendentsBustiesUsuari", key="{#entitat.id, #usuariCodi}")
	public long countElementsPendentsBustiesUsuari(
			EntitatEntity entitat,
			String usuariCodi) {
		// Consulta les bústies de l'usuari a l'entitat
		List<BustiaEntity> busties = bustiaRepository.findByEntitatAndActivaTrueAndPareNotNull(
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
				usuariHelper.generarUsuariAutenticat(usuariCodi, false));
		long count = 0;
		if (!busties.isEmpty()) {
			// Ompl els contadors de fills i registres
			long[] countFills = contenidorHelper.countFillsAmbPermisReadByContinguts(
					entitat,
					busties,
					true);
			for (long c: countFills)
				count += c;
			/*long[] countRegistres = contenidorHelper.countRegistresByContinguts(
					entitat,
					busties);
			for (long c: countRegistres)
				count += c;*/
		}
		// Afegeix l'usuari a l'entitat
		afegirUsuariElementsPendentsPerEntitat(
				entitat,
				usuariCodi);
		return count;
	}
	@CacheEvict(value = "elementsPendentsBustiesUsuari", key="{#entitat.id, #usuariCodi}")
	public void evictElementsPendentsBustiesUsuari(
			EntitatEntity entitat,
			String usuariCodi) {
	}

	@Cacheable(value = "paisos")
	public List<PaisDto> findPaisos() {
		return conversioTipusHelper.convertirList(
				pluginHelper.dadesExternesPaisosFindAll(),
				PaisDto.class);
	}

	@Cacheable(value = "provincies")
	public List<ProvinciaDto> findProvincies() {
		return conversioTipusHelper.convertirList(
				pluginHelper.dadesExternesProvinciesFindAll(),
				ProvinciaDto.class);
	}

	@Cacheable(value = "provinciesPerComunitat", key="#comunitatCodi")
	public List<ProvinciaDto> findProvinciesPerComunitat(String comunitatCodi) {
		return conversioTipusHelper.convertirList(
				pluginHelper.dadesExternesProvinciesFindAmbComunitat(comunitatCodi),
				ProvinciaDto.class);
	}

	@Cacheable(value = "municipisPerProvincia", key="#provinciaCodi")
	public List<MunicipiDto> findMunicipisPerProvincia(String provinciaCodi) {
		return conversioTipusHelper.convertirList(
				pluginHelper.dadesExternesMunicipisFindAmbProvincia(provinciaCodi),
				MunicipiDto.class);
	}

	@Cacheable(value = "municipisAmbNom", key="#municipiNom")
	public MunicipiDto findMunicipiAmbNom(String provinciaCodi, String nom) {
		MunicipiDto municipi = null;
		List<MunicipiDto> municipis = findMunicipisPerProvincia(provinciaCodi);
		if (municipis != null) {
			for (MunicipiDto mun: municipis) {
												
				if (	mun.getNom().equals(nom) || 
						(mun.getNom().equals("Palma") && nom.equals("Palma de Mallorca")) || 						// Excepció: Palma
						(mun.getNom().equals("Maó") && nom.equals("Maó-Mahón")) ||									// Excepció: Maó
						(mun.getNom().equals("Santa Eulalia del Río") && nom.equals("Santa Eulària des Riu")) ||	// Excepció: Santa Eulària des Riu
						(mun.getNom().equals("Deyá") && nom.equals("Deià"))											// Excepció: Deià
				) { 
					municipi = mun;
					break;
				}
			}
		}
		return municipi;
	}

	private ValidacioErrorDto crearValidacioError(
			MetaDadaEntity metaDada,
			MultiplicitatEnumDto multiplicitat) {
		return new ValidacioErrorDto(
				conversioTipusHelper.convertir(
						metaDada,
						MetaDadaDto.class),
				MultiplicitatEnumDto.valueOf(multiplicitat.toString()));
	}
	private ValidacioErrorDto crearValidacioError(
			MetaDocumentEntity metaDocument,
			MultiplicitatEnumDto multiplicitat) {
		return new ValidacioErrorDto(
				conversioTipusHelper.convertir(
						metaDocument,
						MetaDocumentDto.class),
				MultiplicitatEnumDto.valueOf(multiplicitat.toString()));
	}

	private void afegirUsuariElementsPendentsPerEntitat(
			EntitatEntity entitat,
			String usuariCodi) {
		String entitatCodi = entitat.getCodi();
		if (usuarisElementsPendentsPerEntitat == null) {
			usuarisElementsPendentsPerEntitat = new HashMap<String, Set<String>>();
		}
		Set<String> usuaris = usuarisElementsPendentsPerEntitat.get(entitatCodi);
		if (usuaris == null) {
			usuaris = new HashSet<String>();
			usuarisElementsPendentsPerEntitat.put(
					entitatCodi,
					usuaris);
		}
		usuaris.add(usuariCodi);
	}

	private static final Logger logger = LoggerFactory.getLogger(CacheHelper.class);

}
