/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.ArbreDto;
import es.caib.ripea.core.api.dto.ArbreNodeDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.core.api.exception.NotFoundException;
import es.caib.ripea.core.entity.BustiaEntity;
import es.caib.ripea.core.entity.EntitatEntity;
import es.caib.ripea.core.helper.PermisosHelper.ObjectIdentifierExtractor;
import es.caib.ripea.core.repository.BustiaRepository;
import es.caib.ripea.core.security.ExtendedPermission;

/**
 * Mètodes comuns per a gestionar bústies.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class BustiaHelper {

	@Resource
	private BustiaRepository bustiaRepository;

	@Resource
	private CacheHelper cacheHelper;
	@Resource
	private ContingutHelper contenidorHelper;
	@Resource
	private PermisosHelper permisosHelper;
	@Resource
	private UnitatOrganitzativaHelper unitatOrganitzativaHelper;



	public ArbreDto<UnitatOrganitzativaDto> findArbreUnitatsOrganitzatives(
			EntitatEntity entitat,
			boolean nomesAmbBusties,
			boolean nomesAmbBustiesPermeses,
			boolean ambContadorElementsPendents) {
		List<BustiaEntity> busties = bustiaRepository.findByEntitatAndPareNotNull(entitat);
		Set<String> bustiaUnitatCodis = null;
		if (nomesAmbBusties) {
			bustiaUnitatCodis = new HashSet<String>();
			if (nomesAmbBustiesPermeses) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
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
						auth);
			}
			for (BustiaEntity bustia: busties)
				bustiaUnitatCodis.add(bustia.getUnitatCodi());
		}
		// Consulta l'arbre
		ArbreDto<UnitatOrganitzativaDto> arbre = unitatOrganitzativaHelper.findUnitatsOrganitzativesPerEntitatAmbPermesos(
				entitat.getCodi(),
				bustiaUnitatCodis);
		if (ambContadorElementsPendents && !busties.isEmpty()) {
			// Consulta els contadors d'elements pendents per a totes les bústies
			long[] countContenidors = contenidorHelper.countFillsAmbPermisReadByContinguts(
					entitat,
					busties,
					nomesAmbBustiesPermeses);
			// Calcula els acumulats de pendents per a cada unitat
			Map<String, Long> acumulats = new HashMap<String, Long>();
			for (int i = 0; i < busties.size(); i++) {
				BustiaEntity bustia = busties.get(i);
				Long acumulat = acumulats.get(bustia.getUnitatCodi());
				if (acumulat == null) {
					acumulats.put(
							bustia.getUnitatCodi(),
							countContenidors[i]);
				} else {
					acumulats.put(
							bustia.getUnitatCodi(),
							acumulat + countContenidors[i]);
				}
			}
			// Calcula el nombre de nivells de l'arbre
			int nivellsCount = 0;
			for (ArbreNodeDto<UnitatOrganitzativaDto> node: arbre.toList()) {
				if (node.getNivell() > nivellsCount) {
					nivellsCount = node.getNivell();
				}
			}
			// Recorr l'arbre per nivells en ordre invers per a actualitzar
			// el contador d'elements pendents de cada node i dels seus pares
			for (int nivell = nivellsCount; nivell > 0; nivell--) {
				for (ArbreNodeDto<UnitatOrganitzativaDto> node: arbre.toList()) {
					if (node.getNivell() == nivell) {
						// Actualitza el contador del node actual
						String unitatCodi = node.getDades().getCodi();
						Long acumulat = acumulats.get(unitatCodi);
						if (acumulat != null) {
							node.addCount(acumulat);
							// Actualitza els contadors dels pares
							ArbreNodeDto<UnitatOrganitzativaDto> pare = node.getPare();
							while (pare != null) {
								pare.addCount(node.getCount());
								pare = pare.getPare();
							}
						}
					}
				}
			}
		}
		return arbre;
	}

	/** Troba la bústica per defecte per a la unitat administrativa. Si la unitat no té
	 * bústia, llavors mira la 1a de les unitats organitzatives superiors que tingui bústia,
	 * si cap en té llavors crea la bústia per a la unitat organitzativa arrel.
	 * 
	 * @param entitat
	 * @param unitat
	 * @return
	 */
	public BustiaEntity findBustiaPerDefecte(
			EntitatEntity entitat,
			UnitatOrganitzativaDto unitat) {
		
		BustiaEntity bustia = null;
		String unitatOrganitzativaCodi;
		String unitatSuperiorCodi;
		String unitatArrelCodi = unitat.getCodiUnitatArrel();		
		// Busca la bústia mentre no es trobi la per defecte en la unitat o les seves superiors fins la arrel
		do {
			// Guarda el codi de la unitat actual i de la immediatament superior
			unitatOrganitzativaCodi = unitat.getCodi();
			unitatSuperiorCodi = unitat.getCodiUnitatSuperior();
			// Busca la bústia per defecte per la unitat actual
			bustia = bustiaRepository.findByEntitatAndUnitatCodiAndPerDefecteTrue(
					entitat,
					unitatOrganitzativaCodi);
			// Si la bústia no existeix continua mirant amb la unitat superior
			if (bustia == null) {
				unitat = unitatOrganitzativaHelper.findPerEntitatAndCodi(
						entitat.getCodi(),
						unitatSuperiorCodi);	
				if (unitat == null)
					throw new NotFoundException(unitatSuperiorCodi, UnitatOrganitzativaDto.class);
			}
		} while(bustia == null && unitatArrelCodi.compareTo(unitatOrganitzativaCodi) != 0);		
		
		// Si la bústia no existeix la crea per a la unitat superior
		if (bustia == null) {
			// Cerca la bústia superior
			BustiaEntity bustiaPare = bustiaRepository.findByEntitatAndUnitatCodiAndPareNull(
					entitat,
					unitatOrganitzativaCodi);
			// Si la bústia superior no existeix la crea
			if (bustiaPare == null) {
				bustiaPare = bustiaRepository.save(
						BustiaEntity.getBuilder(
								entitat,
								unitat.getDenominacio(),
								unitatOrganitzativaCodi,
								null).build());
			}
			// Crea la nova bústia
			BustiaEntity entity = BustiaEntity.getBuilder(
					entitat,
					unitat.getDenominacio() + " (default)",
					unitatOrganitzativaCodi,
					bustiaPare).build();
			entity.updatePerDefecte(true);
			bustia = bustiaRepository.save(entity);
		}
		return bustia;
	}

	public void evictElementsPendentsBustia(
			EntitatEntity entitat,
			BustiaEntity bustia) {
		Set<String> usuaris = contenidorHelper.findUsuarisAmbPermisReadPerContenidor(bustia);
		if (usuaris != null) {
			for (String usuari: usuaris)
				cacheHelper.evictElementsPendentsBustiesUsuari(
						entitat,
						usuari);
		}
	}

}
