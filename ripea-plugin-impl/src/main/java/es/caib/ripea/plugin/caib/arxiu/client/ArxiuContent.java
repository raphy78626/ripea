/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu.client;

import java.util.List;
import java.util.Map;

/**
 * Estructura que representa un contingut genèric de l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public abstract class ArxiuContent {

	protected String nodeId;
	protected String name;
	protected Map<String, Object> metadata;
	protected List<String> aspects;

	public ArxiuContent(
			String nodeId,
			String name,
			Map<String, Object> metadata,
			List<String> aspects) {
		this.nodeId = nodeId;
		this.name = name;
		this.metadata = metadata;
		this.aspects = aspects;
	}

	public String getNodeId() {
		return nodeId;
	}
	public String getName() {
		return name;
	}
	public Map<String, Object> getMetadata() {
		return metadata;
	}
	public List<String> getAspects() {
		return aspects;
	}

}
