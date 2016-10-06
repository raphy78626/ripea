/**
 * 
 */
package es.caib.ripea.plugin.unitat;

import java.util.List;

import es.caib.ripea.plugin.SistemaExternException;


/**
 * Plugin per a obtenir l'arbre d'unitats organitzatives.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface UnitatsOrganitzativesPlugin {

	/**
	 * Retorna la llista d'unitats organitzatives filles donada
	 * una unitat pare.
	 * 
	 * @param pareCodi
	 *            Codi de la unitat pare.
	 * @return La llista d'unitats organitzatives.
	 * @throws SistemaExternException
	 *            Si es produeix un error al consultar les unitats organitzatives.
	 */
	public List<UnitatOrganitzativa> findAmbPare(
			String pareCodi) throws SistemaExternException;

	/**
	 * Retorna l'unitat organitzativa donat el seu codi.
	 * 
	 * @param codi
	 *            Codi de l'unitat organitzativa.
	 * @return La unitat organitzativa.
	 * @throws SistemaExternException
	 *            Si es produeix un error al consultar les unitats organitzatives.
	 */
	public UnitatOrganitzativa findAmbCodi(
			String codi) throws SistemaExternException;
	
	/**
	 * Retorna la llista d'unitats organitzatives filles donat un filtre.
	 * 
	 * @param codiUnitat
	 *            Codi de la unitat.
	 * @param denominacioUnitat
	 *            Denominació de la unitat de la unitat
	 * @param codiNivellAdministracio
	 *            Nivell de administració de la unitat.
	 * @param codiComunitat
	 *            Codi de la comunitat de la unitat.
	 * @param ambOficines
	 *            Indica si les unitats retornades tenen oficines.
	 * @param esUnitatArrel
	 *            Indica si les unitats retornades són unitats arrel.
	 * @param codiProvincia
	 *            Codi de la provincia de la unitat.
	 * @param codiLocalitat
	 *            Codi de la localitat de la unitat.
	 *            
	 * @return La llista d'unitats organitzatives.
	 * @throws SistemaExternException
	 *            Si es produeix un error al consultar les unitats organitzatives.
	 */
	public List<UnitatOrganitzativa> cercaUnitats(
			String codiUnitat,
			String denominacioUnitat,
			Long codiNivellAdministracio,
			Long codiComunitat,
			Boolean ambOficines,
			Boolean esUnitatArrel,
			Long codiProvincia,
			String codiLocalitat) throws SistemaExternException;

}
