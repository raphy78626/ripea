/**
 * 
 */
package es.caib.ripea.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.MunicipiDto;
import es.caib.ripea.core.api.dto.PaginaDto;
import es.caib.ripea.core.api.dto.PaginacioParamsDto;
import es.caib.ripea.core.api.dto.ProvinciaDto;
import es.caib.ripea.core.api.dto.TipusViaDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaFiltreDto;
import es.caib.ripea.core.api.service.UnitatOrganitzativaService;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.entity.UnitatOrganitzativaEntity;
import es.caib.ripea.core.helper.CacheHelper;
import es.caib.ripea.core.helper.ConversioTipusHelper;
import es.caib.ripea.core.helper.EntityComprovarHelper;
import es.caib.ripea.core.helper.PaginacioHelper;
import es.caib.ripea.core.helper.PaginacioHelper.Converter;
import es.caib.ripea.core.helper.PluginHelper;
import es.caib.ripea.core.helper.UnitatOrganitzativaHelper;
import es.caib.ripea.core.repository.EntitatRepository;
import es.caib.ripea.core.repository.UnitatOrganitzativaRepository;
import es.caib.ripea.plugin.SistemaExternException;

/**
 * Implementació del servei de gestió d'entitats.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class UnitatOrganitzativaServiceImpl implements UnitatOrganitzativaService {

	@Resource
	private CacheHelper cacheHelper;
	@Resource
	private PluginHelper pluginHelper;
	@Resource
	private EntityComprovarHelper entityComprovarHelper;
	@Resource
	private PaginacioHelper paginacioHelper;
	@Resource
	private ConversioTipusHelper conversioTipusHelper;
	@Resource
	private UnitatOrganitzativaRepository unitatOrganitzativaRepository;
	@Resource
	private EntitatRepository entitatRepository;
	@Resource
	private UnitatOrganitzativaHelper unitatOrganitzativaHelper;
	

	@Override
	@Transactional
	public void synchronize(EntitatDto entitatActual) {

		List<UnitatOrganitzativaDto> unitatsOrganitzativesPerEntitat = new ArrayList<UnitatOrganitzativaDto>();
		try {
			unitatsOrganitzativesPerEntitat = unitatOrganitzativaHelper.findUnitatsOrganitzativesPerEntitatFromPlugin(entitatActual.getCodi());
		} catch (SistemaExternException e) {
			e.printStackTrace();
		}

		for (UnitatOrganitzativaDto unitatOrganitzativa : unitatsOrganitzativesPerEntitat) {
			create(entitatActual.getId(), unitatOrganitzativa);
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public PaginaDto<UnitatOrganitzativaDto> findAmbFiltre(
			Long entitatId,
			UnitatOrganitzativaFiltreDto filtre,
			PaginacioParamsDto paginacioParams) {
		logger.debug("Cercant les unitats organitzatives segons el filtre ("
				+ "entitatId=" + entitatId + ", "
				+ "filtre=" + filtre + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		
		Map<String, String[]> mapeigPropietatsOrdenacio = new HashMap<String, String[]>();

		
		PaginaDto<UnitatOrganitzativaDto> resultPagina =  paginacioHelper.toPaginaDto(
				unitatOrganitzativaRepository.findByEntitatAndUnitatCodiAndUnitatDenominacioFiltrePaginat(
						entitat.getUnitatArrel(),
						filtre.getCodi() == null || filtre.getCodi().isEmpty(), 
						filtre.getCodi(),
						filtre.getDenominacio() == null || filtre.getDenominacio().isEmpty(), 
						filtre.getDenominacio(),
						paginacioHelper.toSpringDataPageable(paginacioParams, mapeigPropietatsOrdenacio)),
				UnitatOrganitzativaDto.class,
				new Converter<UnitatOrganitzativaEntity, UnitatOrganitzativaDto>() {
					@Override
					public UnitatOrganitzativaDto convert(UnitatOrganitzativaEntity source) {
						return conversioTipusHelper.convertir(
								source,
								UnitatOrganitzativaDto.class);
					}
				});
		return resultPagina;
	}
	
	
	@Transactional
	public void create(
			Long entitatId,
			UnitatOrganitzativaDto unitatOrganitzativa) {
		logger.debug("Creant una nova unitat organitzativa ("
				+ "entitatId=" + entitatId + ", "
				+ "unitatOrganitzativa=" + unitatOrganitzativa + ")");
		EntitatEntity entitat = entityComprovarHelper.comprovarEntitat(
				entitatId,
				false,
				true,
				false);
		
		UnitatOrganitzativaEntity entity = UnitatOrganitzativaEntity.getBuilder(
				unitatOrganitzativa.getCodi(),
				unitatOrganitzativa.getDenominacio(),
				unitatOrganitzativa.getNifCif(),
				unitatOrganitzativa.getCodiUnitatSuperior(),
				unitatOrganitzativa.getCodiUnitatArrel(),
				unitatOrganitzativa.getDataCreacioOficial(),
				unitatOrganitzativa.getDataSupressioOficial(),
				unitatOrganitzativa.getDataExtincioFuncional(),
				unitatOrganitzativa.getDataAnulacio(),
				unitatOrganitzativa.getEstat(), 
				unitatOrganitzativa.getCodiPais(),
				unitatOrganitzativa.getCodiComunitat(),
				unitatOrganitzativa.getCodiProvincia(),
				unitatOrganitzativa.getCodiPostal(),
				unitatOrganitzativa.getNomLocalitat(),
				unitatOrganitzativa.getLocalitat(),
				unitatOrganitzativa.getAdressa(),
				unitatOrganitzativa.getTipusVia(),
				unitatOrganitzativa.getNomVia(),
				unitatOrganitzativa.getNumVia(),
				false).build();
		unitatOrganitzativaRepository.save(entity);
		
	}
	

	
	@Override
	@Transactional(readOnly = true)
	public List<UnitatOrganitzativaDto> findByEntitat(
			String entitatCodi) { 
		EntitatEntity entitat = entitatRepository.findByCodi(entitatCodi);
		return conversioTipusHelper.convertirList(
				unitatOrganitzativaRepository.findByCodiUnitatArrel(entitat.getUnitatArrel()),
				UnitatOrganitzativaDto.class);
//		return cacheHelper.findUnitatsOrganitzativesPerEntitat(entitatCodi).toDadesList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public UnitatOrganitzativaDto findById(
			Long id) {
		UnitatOrganitzativaDto unitat = conversioTipusHelper.convertir(
				unitatOrganitzativaRepository.findOne(id),
				UnitatOrganitzativaDto.class);
		
		if (unitat != null) {
			unitat.setAdressa(
					getAdressa(
							unitat.getTipusVia(), 
							unitat.getNomVia(), 
							unitat.getNumVia()));
			if (unitat.getCodiPais() != null && !"".equals(unitat.getCodiPais())) {
				unitat.setCodiPais(("000" + unitat.getCodiPais()).substring(unitat.getCodiPais().length()));
			}
			if (unitat.getCodiComunitat() != null && !"".equals(unitat.getCodiComunitat())) {
				unitat.setCodiComunitat(("00" + unitat.getCodiComunitat()).substring(unitat.getCodiComunitat().length()));
			}
			if ((unitat.getCodiProvincia() == null || "".equals(unitat.getCodiProvincia())) && 
					unitat.getCodiComunitat() != null && !"".equals(unitat.getCodiComunitat())) {
				List<ProvinciaDto> provincies = cacheHelper.findProvinciesPerComunitat(unitat.getCodiComunitat());
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
	
	

	@Override
	@Transactional(readOnly = true)
	public UnitatOrganitzativaDto findByCodi(
			String unitatOrganitzativaCodi) {
		UnitatOrganitzativaDto unitat = conversioTipusHelper.convertir(
				unitatOrganitzativaRepository.findByCodi(unitatOrganitzativaCodi),
				UnitatOrganitzativaDto.class);
		
//		UnitatOrganitzativaDto unitat = pluginHelper.unitatsOrganitzativesFindByCodi(
//				unitatOrganitzativaCodi);
		if (unitat != null) {
			unitat.setAdressa(
					getAdressa(
							unitat.getTipusVia(), 
							unitat.getNomVia(), 
							unitat.getNumVia()));
			if (unitat.getCodiPais() != null && !"".equals(unitat.getCodiPais())) {
				unitat.setCodiPais(("000" + unitat.getCodiPais()).substring(unitat.getCodiPais().length()));
			}
			if (unitat.getCodiComunitat() != null && !"".equals(unitat.getCodiComunitat())) {
				unitat.setCodiComunitat(("00" + unitat.getCodiComunitat()).substring(unitat.getCodiComunitat().length()));
			}
			if ((unitat.getCodiProvincia() == null || "".equals(unitat.getCodiProvincia())) && 
					unitat.getCodiComunitat() != null && !"".equals(unitat.getCodiComunitat())) {
				List<ProvinciaDto> provincies = cacheHelper.findProvinciesPerComunitat(unitat.getCodiComunitat());
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

	@Override
	public List<UnitatOrganitzativaDto> findByFiltre(
			String codiDir3, 
			String denominacio,
			String nivellAdm, 
			String comunitat, 
			String provincia, 
			String localitat, 
			Boolean arrel) {
		return pluginHelper.unitatsOrganitzativesFindByFiltre(
				codiDir3, 
				denominacio,
				nivellAdm, 
				comunitat, 
				provincia, 
				localitat, 
				arrel);
	}



	private String getAdressa(
			Long tipusVia,
			String nomVia,
			String numVia) {
		String adressa = "";
		if (tipusVia != null) {
			List<TipusViaDto> tipus = cacheHelper.findTipusVia();
			for (TipusViaDto tvia: tipus) {
				if (tvia.getCodi().equals(tipusVia)) {
					adressa = tvia.getDescripcio() + " ";
					break;
				}
			}
		}
		adressa += nomVia;
		if (numVia != null) {
			adressa += ", " + numVia;
		}
		return adressa;
	}

	private MunicipiDto findMunicipiAmbNom(
			String provinciaCodi,
			String municipiNom) {
		MunicipiDto municipi = null;
		List<MunicipiDto> municipis = cacheHelper.findMunicipisPerProvincia(provinciaCodi);
		if (municipis != null) {
			for (MunicipiDto mun: municipis) {
				if (mun.getNom().equalsIgnoreCase(municipiNom)) { 
					municipi = mun;
					break;
				}
			}
		}
		return municipi;
	}

	private static final Logger logger = LoggerFactory.getLogger(UnitatOrganitzativaServiceImpl.class);

}
