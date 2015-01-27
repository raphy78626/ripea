/**
 * 
 */
package es.caib.ripea.core.helper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import es.caib.ripea.core.api.dto.ArbreDto;
import es.caib.ripea.core.api.dto.ArbreNodeDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;


/**
 * Helper per a operacions amb unitats organitzatives.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class UnitatOrganitzativaHelper {

	@Resource
	private CacheHelper cacheHelper;



	public UnitatOrganitzativaDto findPerEntitatAndCodi(
			String entitatCodi,
			String unitatCodi) {
		ArbreDto<UnitatOrganitzativaDto> arbre = cacheHelper.findUnitatsOrganitzativesPerEntitat(entitatCodi);
		for (UnitatOrganitzativaDto unitat: arbre.toDadesList()) {
			if (unitat.getCodi().equals(unitatCodi)) {
				return unitat;
			}
		}
		return null;
	}

	public ArbreDto<UnitatOrganitzativaDto> findUnitatsOrganitzativesPerEntitatAmbPermesos(
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

	public UnitatOrganitzativaDto findConselleriaPerUnitatOrganitzativa(
			String entitatCodi,
			String unitatCodi) {
		ArbreDto<UnitatOrganitzativaDto> arbre = cacheHelper.findUnitatsOrganitzativesPerEntitat(entitatCodi).clone();
		UnitatOrganitzativaDto unitatConselleria = null;
		for (ArbreNodeDto<UnitatOrganitzativaDto> node: arbre.toList()) {
			UnitatOrganitzativaDto uo = node.getDades();
			if (uo.getCodi().equals(unitatCodi)) {
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

}
