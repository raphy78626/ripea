/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu.client;

import java.util.List;

import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.Content;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.Metadata;
import es.caib.arxiudigital.apirest.constantes.Aspectos;

/**
 * Estructura que representa un document de l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuDocument extends ArxiuContingut {

	private List<Content> continguts;

	public ArxiuDocument(
			String nodeId,
			String titol,
			List<Content> continguts,
			List<Metadata> metadades,
			List<Aspectos> aspectes,
			String json) {
		super(
				nodeId,
				titol,
				metadades,
				aspectes,
				json);
		this.continguts = continguts;
	}

	public List<Content> getContinguts() {
		return continguts;
	}

}
