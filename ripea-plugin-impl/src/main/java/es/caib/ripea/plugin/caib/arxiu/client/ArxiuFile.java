/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu.client;

import java.util.List;
import java.util.Map;

/**
 * Estructura que representa un expedient de l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuFile extends ArxiuContent {

	private List<ArxiuContentChild> children;

	public ArxiuFile(
			String nodeId,
			String titol,
			List<ArxiuContentChild> children,
			Map<String, Object> metadata,
			List<String> aspects) {
		super(
				nodeId,
				titol,
				metadata,
				aspects);
		this.children = children;
	}

	public List<ArxiuContentChild> getChildren() {
		return children;
	}

}
