/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu.client;

import java.util.List;

import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.Metadata;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.SummaryInfoNode;
import es.caib.arxiudigital.apirest.constantes.Aspectos;

/**
 * Estructura que representa un expedient de l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuFile extends ArxiuContingut {

	private List<SummaryInfoNode> fills;

	public ArxiuFile(
			String nodeId,
			String titol,
			List<SummaryInfoNode> fills,
			List<Metadata> metadades,
			List<Aspectos> aspectes,
			String json) {
		super(
				nodeId,
				titol,
				metadades,
				aspectes,
				json);
		this.fills = fills;
	}

	public List<SummaryInfoNode> getFills() {
		return fills;
	}

}
