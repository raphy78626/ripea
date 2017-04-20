/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu.client;

import java.util.List;

/**
 * Estructura que representa una carpeta de l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuFolder extends ArxiuContent {

	private List<ArxiuContentChild> children;

	public ArxiuFolder(
			String nodeId,
			String name,
			List<ArxiuContentChild> children) {
		super(
				nodeId,
				name,
				null,
				null);
		this.children = children;
	}

	public List<ArxiuContentChild> getChildren() {
		return children;
	}

}
