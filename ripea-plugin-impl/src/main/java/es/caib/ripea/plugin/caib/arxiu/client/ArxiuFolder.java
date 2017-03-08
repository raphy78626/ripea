/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu.client;

/**
 * Estructura que representa una carpeta de l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuFolder {

	private String nodeId;
	private String name;

	private String json;

	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}

}
