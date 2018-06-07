/**
 * 
 */
package es.caib.ripea.core.helper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.ArbreDto;
import es.caib.ripea.core.api.dto.ArbreNodeDto;
import es.caib.ripea.core.api.dto.TipusTranscissioEnumDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.exception.SistemaExternException;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.UnitatOrganitzativaEntity;
import es.caib.ripea.core.repository.EntitatRepository;
import es.caib.ripea.core.repository.UnitatOrganitzativaRepository;
import es.caib.ripea.plugin.unitat.UnitatOrganitzativa;


/**
 * Helper per a operacions amb unitats organitzatives.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class UnitatOrganitzativaHelper {

	@Resource
	private CacheHelper cacheHelper;
	@Resource
	private EntityComprovarHelper entityComprovarHelper;
	@Resource
	private PaginacioHelper paginacioHelper;
	@Resource
	private PluginHelper pluginHelper;
	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private UnitatOrganitzativaRepository unitatOrganitzativaRepository;
	@Resource
	private EntitatRepository entitatRepository;
	
	
	
	
	public void sincronizarOActualizar(Long entidadId, Timestamp fechaActualizacion, Timestamp fechaSincronizacion)
			throws SistemaExternException {

		EntitatEntity entitat = entitatRepository.getOne(entidadId);
//
//		try {
//			UnitatOrganitzativa unidadPadreWS = pluginHelper.getUnitatsOrganitzativesPlugin()
//					.obtenerUnidad(entitat.getUnitatArrel(), fechaActualizacion, fechaSincronizacion);
//			if (unidadPadreWS != null) {
//
//				List<UnitatOrganitzativa> arbol = pluginHelper.getUnitatsOrganitzativesPlugin()
//						.obtenerArbolUnidades(entitat.getUnitatArrel(), fechaActualizacion, fechaSincronizacion);
//
//				UnitatOrganitzativaEntity unitatPadre = sincronizarUnitat(unidadPadreWS, entidadId);
//				sincronizarHistoricosUnitat(unitatPadre, unidadPadreWS);
//
//				if (arbol.size() > 0) {
//					for (UnitatOrganitzativa unidadWS : arbol) {
//						UnitatOrganitzativaEntity unitat = sincronizarUnitat(unidadWS, entidadId);
//						sincronizarHistoricosUnitat(unitat, unidadWS);
//					}
//				}
//								
				List<UnitatOrganitzativaEntity> obsoleteUnitats = unitatOrganitzativaRepository
						.findByCodiUnitatArrelAndEstatNotV(entitat.getUnitatArrel());

				//setting type of transition
				for (UnitatOrganitzativaEntity obsoleteUnitat : obsoleteUnitats) {
					
					if (obsoleteUnitat.getNovaList().size() > 1) {
						obsoleteUnitat.setTipusTranscissio(TipusTranscissioEnumDto.DIVISIO);
					} else {
						if(obsoleteUnitat.getNovaList().size() == 1){
							
							if(obsoleteUnitat.getNovaList().get(0).getAntigaList().size()>1){
								obsoleteUnitat.setTipusTranscissio(TipusTranscissioEnumDto.FUSIO);
							} else if(obsoleteUnitat.getNovaList().get(0).getAntigaList().size()==1){
								obsoleteUnitat.setTipusTranscissio(TipusTranscissioEnumDto.SUBSTITUCIO);
							}
						}
					}
				}
				
				
				
//			} else {
//				throw new SistemaExternException("No s'han trobat la unitat pare (entidadId=" + entidadId + ")");
//			}
//		} catch (Exception ex) {
//			throw new SistemaExternException(
//					"No s'han pogut consultar les unitats organitzatives via WS (" + "entidadId=" + entidadId + ")",
//					ex);
//		}

	}
	
	
	public void sincronizarHistoricosUnitat(
			UnitatOrganitzativaEntity unitat,
			UnitatOrganitzativa unidadWS){
		
		List<UnitatOrganitzativaEntity> historicosUO = new ArrayList<UnitatOrganitzativaEntity>();
		for (String historicoCodi : unidadWS.getHistoricosUO()) {
			historicosUO.add(unitatOrganitzativaRepository.findByCodi(historicoCodi));
		}
		unitat.setNovaList(historicosUO);
	}
	
	

	/**
	 * This method creates new (if it doesnt already exists) or updates existing unidad in database with the given unitatWS  
	 * 
	 * @param unidadWS
	 * @param entidadId
	 * @throws Exception
	 */
	public UnitatOrganitzativaEntity sincronizarUnitat(UnitatOrganitzativa unitatWS, Long entidadId) throws Exception {


		UnitatOrganitzativaEntity unitat = null;

		if (unitatWS != null) {
			// checks if unitat already exists in databse
			unitat = unitatOrganitzativaRepository.findByCodi(unitatWS.getCodi());
			//if not it creates a new one
			if (unitat == null) {
				unitat = new UnitatOrganitzativaEntity();
			}

			unitat.update(
					unitatWS.getCodi(),
					unitatWS.getDenominacio(),
					unitatWS.getNifCif(),
					unitatWS.getEstat(),
					unitatWS.getCodiUnitatSuperior(),
					unitatWS.getCodiUnitatArrel(),
					unitatWS.getCodiPais(),
					unitatWS.getCodiComunitat(),
					unitatWS.getCodiProvincia(),
					unitatWS.getCodiPostal(),
					unitatWS.getNomLocalitat(),
					unitatWS.getTipusVia(),
					unitatWS.getNomVia(),
					unitatWS.getNumVia());
			// Guardamos el Unitat
			unitat = unitatOrganitzativaRepository.save(unitat);
		}

		return unitat;
	}
	
	public static UnitatOrganitzativaDto assignAltresUnitatsFusionades(
			UnitatOrganitzativaEntity unitatOrganitzativaEntity, UnitatOrganitzativaDto unitatOrganitzativaDto) {
		if (unitatOrganitzativaEntity != null && unitatOrganitzativaDto != null) {
			Map<String, String> altresUnitatsFusionades = new HashMap<String, String>();
			if (unitatOrganitzativaEntity.getTipusTranscissio() == TipusTranscissioEnumDto.FUSIO) {
				List<UnitatOrganitzativaEntity> antigaUnitats = unitatOrganitzativaEntity.getNovaList().get(0)
						.getAntigaList();
				for (UnitatOrganitzativaEntity unitatEntity : antigaUnitats) {
					if (unitatOrganitzativaEntity.getCodi() != unitatEntity.getCodi()) {
						altresUnitatsFusionades.put(unitatEntity.getCodi(), unitatEntity.getDenominacio());
					}
				}
			}
			unitatOrganitzativaDto.setAltresUnitatsFusionades(altresUnitatsFusionades);
			return unitatOrganitzativaDto;
		} else
			return null;

	}
	
    
	
	public UnitatOrganitzativaDto toUnitatOrganitzativaDto(
			UnitatOrganitzativaEntity source) {
		return conversioTipusHelper.convertir(
				source, 
				UnitatOrganitzativaDto.class);
	}
	
	public UnitatOrganitzativaDto findPerEntitatAndCodi(
			String entitatCodi,
			String unitatOrganitzativaCodi) {
		
		EntitatEntity entitat = entitatRepository.findByCodi(entitatCodi);
		
		return toUnitatOrganitzativaDto(unitatOrganitzativaRepository.findByCodiUnitatArrelAndCodi(
				entitat.getUnitatArrel(),
				unitatOrganitzativaCodi));
	}

//	public UnitatOrganitzativaDto findPerEntitatAndCodi(
//			String entitatCodi,
//			String unitatOrganitzativaCodi) {
//		ArbreDto<UnitatOrganitzativaDto> arbre = cacheHelper.findUnitatsOrganitzativesPerEntitat(entitatCodi);
//		for (UnitatOrganitzativaDto unitat: arbre.toDadesList()) {
//			if (unitat.getCodi().equals(unitatOrganitzativaCodi)) {
//				return unitat;
//			}
//		}
//		return null;
//	}
	
	
//	public UnitatOrganitzativaDto findAmbCodi(
//			String unitatOrganitzativaCodi) {
//		return pluginHelper.unitatsOrganitzativesFindByCodi(
//				unitatOrganitzativaCodi);
//	}

	public UnitatOrganitzativaDto findAmbCodi(
			String unitatOrganitzativaCodi) {
		return toUnitatOrganitzativaDto(unitatOrganitzativaRepository.findByCodi(
				unitatOrganitzativaCodi));
	}

	public List<UnitatOrganitzativaDto> findPath(
			String entitatCodi,
			String unitatOrganitzativaCodi) {
		ArbreDto<UnitatOrganitzativaDto> arbre = cacheHelper.findUnitatsOrganitzativesPerEntitat(entitatCodi);
		List<UnitatOrganitzativaDto> superiors = new ArrayList<UnitatOrganitzativaDto>();
		String codiActual = unitatOrganitzativaCodi;
		do {
			UnitatOrganitzativaDto unitatActual = cercaDinsArbreAmbCodi(
					arbre,
					codiActual);
			if (unitatActual != null) {
				superiors.add(unitatActual);
				codiActual = unitatActual.getCodiUnitatSuperior();
			} else {
				codiActual = null;
			}
		} while (codiActual != null);
		return superiors;
	}

	public ArbreDto<UnitatOrganitzativaDto> findPerEntitatAmbCodisPermesos(
			String entitatCodi,
			Set<String> unitatCodiPermesos) {
		ArbreDto<UnitatOrganitzativaDto> arbre = cacheHelper.findUnitatsOrganitzativesPerEntitat(entitatCodi).clone();
		if (unitatCodiPermesos != null) {
			// Calcula els nodes a "salvar" afegint els nodes permesos
			// i tots els seus pares.
			List<ArbreNodeDto<UnitatOrganitzativaDto>> nodes = arbre.toList();
			Set<String> unitatCodiSalvats = new HashSet<String>();
			for (ArbreNodeDto<UnitatOrganitzativaDto> node: nodes) {
				if (unitatCodiPermesos.contains(node.dades.getCodi())) {
					unitatCodiSalvats.add(node.dades.getCodi());
					ArbreNodeDto<UnitatOrganitzativaDto> pare = node.getPare();
					while (pare != null) {
						unitatCodiSalvats.add(pare.dades.getCodi());
						pare = pare.getPare();
					}
				}
			}
			// Esborra els nodes no "salvats"
			for (ArbreNodeDto<UnitatOrganitzativaDto> node: nodes) {
				if (!unitatCodiSalvats.contains(node.dades.getCodi())) {
					if (node.getPare() != null)
						node.getPare().removeFill(node);
					else
						arbre.setArrel(null);
				}
					
			}
		}
		return arbre;
	}
	
	public List<UnitatOrganitzativaDto> findUnitatsOrganitzativesPerEntitatFromPlugin(String entitatCodi)
			throws SistemaExternException {

		EntitatEntity entitat = entitatRepository.findByCodi(entitatCodi);
		return pluginHelper.unitatsOrganitzativesFindListByPare(entitat.getUnitatArrel());
	}
	

	public UnitatOrganitzativaDto findConselleria(
			String entitatCodi,
			String unitatOrganitzativaCodi) {
		ArbreDto<UnitatOrganitzativaDto> arbre = cacheHelper.findUnitatsOrganitzativesPerEntitat(entitatCodi).clone();
		UnitatOrganitzativaDto unitatConselleria = null;
		for (ArbreNodeDto<UnitatOrganitzativaDto> node: arbre.toList()) {
			UnitatOrganitzativaDto uo = node.getDades();
			if (uo.getCodi().equals(unitatOrganitzativaCodi)) {
				ArbreNodeDto<UnitatOrganitzativaDto> nodeActual = node;
				while (nodeActual.getNivell() > 1) {
					nodeActual = nodeActual.getPare();
				}
				if (nodeActual.getNivell() == 1)
					unitatConselleria = nodeActual.getDades();
				break;
			}
		}
		return unitatConselleria;
	}



	private UnitatOrganitzativaDto cercaDinsArbreAmbCodi(
			ArbreDto<UnitatOrganitzativaDto> arbre,
			String unitatOrganitzativaCodi) {
		UnitatOrganitzativaDto trobada = null;
		for (UnitatOrganitzativaDto unitat: arbre.toDadesList()) {
			if (unitat.getCodi().equals(unitatOrganitzativaCodi)) {
				trobada = unitat;
				break;
			}
		}
		return trobada;
	}

}
