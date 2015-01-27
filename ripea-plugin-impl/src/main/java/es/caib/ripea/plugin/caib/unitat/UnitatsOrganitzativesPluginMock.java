/**
 * 
 */
package es.caib.ripea.plugin.caib.unitat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.caib.ripea.plugin.SistemaExternException;
import es.caib.ripea.plugin.unitat.UnitatOrganitzativa;
import es.caib.ripea.plugin.unitat.UnitatsOrganitzativesPlugin;

/**
 * Implementació de proves del plugin d'unitats organitzatives.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class UnitatsOrganitzativesPluginMock implements UnitatsOrganitzativesPlugin {

	@Override
	public List<UnitatOrganitzativa> findAmbPare(
			String pareCodi) throws SistemaExternException {
		List<UnitatOrganitzativa> unitats = new ArrayList<UnitatOrganitzativa>();
		UnitatOrganitzativa unitatArrel = new UnitatOrganitzativa(
				"LIM000000",
				"Limit Tecnologies",
				"12345678Z",
				new Date(0),
				"V");
		unitats.add(unitatArrel);
		UnitatOrganitzativa unitat1 = new UnitatOrganitzativa(
				"LIM000001",
				"Departament de programari",
				"12345678Z",
				new Date(0),
				"V",
				"LIM000000",
				"LIM000000");
		unitats.add(unitat1);
		UnitatOrganitzativa unitat2 = new UnitatOrganitzativa(
				"LIM000002",
				"Departament de comunicacions i taller",
				"12345678Z",
				new Date(0),
				"V",
				"LIM000000",
				"LIM000000");
		unitats.add(unitat2);
		UnitatOrganitzativa unitat3 = new UnitatOrganitzativa(
				"LIM000003",
				"Departament comercial",
				"12345678Z",
				new Date(0),
				"V",
				"LIM000000",
				"LIM000000");
		unitats.add(unitat3);
		UnitatOrganitzativa unitat4 = new UnitatOrganitzativa(
				"LIM000004",
				"Secció de programació Java",
				"12345678Z",
				new Date(0),
				"V",
				"LIM000001",
				"LIM000000");
		unitats.add(unitat4);
		UnitatOrganitzativa unitat5 = new UnitatOrganitzativa(
				"LIM000005",
				"Secció de programació Forms/Oracle",
				"12345678Z",
				new Date(0),
				"V",
				"LIM000001",
				"LIM000000");
		unitats.add(unitat5);
		UnitatOrganitzativa unitat6 = new UnitatOrganitzativa(
				"LIM000006",
				"Secció de programació Web/PHP",
				"12345678Z",
				new Date(0),
				"V",
				"LIM000001",
				"LIM000000");
		unitats.add(unitat6);
		UnitatOrganitzativa unitat7 = new UnitatOrganitzativa(
				"LIM000007",
				"Departament d'administració",
				"12345678Z",
				new Date(0),
				"V",
				"LIM000000",
				"LIM000000");
		unitats.add(unitat7);
		return unitats;
	}

	@Override
	public UnitatOrganitzativa findAmbCodi(
			String codi) throws SistemaExternException {
		List<UnitatOrganitzativa> unitats = findAmbPare(null);
		for (UnitatOrganitzativa unitat: unitats) {
			if (unitat.getCodi().equals(codi))
				return unitat;
		}
		return null;
	}

}
