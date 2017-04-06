/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu.client;

import java.util.List;

import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.SummaryInfoNode;

/**
 * Estructura que representa una carpeta de l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuFolder extends ArxiuContingut {

	private List<SummaryInfoNode> fills;

	public ArxiuFolder(
			String nodeId,
			String titol,
			List<SummaryInfoNode> fills,
			String json) {
		super(
				nodeId,
				titol,
				null,
				null,
				json);
		this.fills = fills;
	}

	public List<SummaryInfoNode> getFills() {
		return fills;
	}

	public String getNom() {
		return titol;
	}

}
