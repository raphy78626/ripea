/**
 * 
 */
package es.caib.ripea.plugin.arxiu;

/**
 * Estructura que representa un document de l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuCarpeta {

	private String nodeId;
	private String nom;

	private String json;

	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}

}
