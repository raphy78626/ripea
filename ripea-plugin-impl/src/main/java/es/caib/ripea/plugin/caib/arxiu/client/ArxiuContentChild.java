/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu.client;

/**
 * Estructura que representa un contingut d'un document de l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuContentChild {

	private String nodeId;
	private String name;
	private ArxiuContentChildTypeEnum tipus;

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
	public ArxiuContentChildTypeEnum getTipus() {
		return tipus;
	}
	public void setTipus(ArxiuContentChildTypeEnum tipus) {
		this.tipus = tipus;
	}

}
