/**
 * 
 */
package es.caib.ripea.plugin.arxiu;

import java.util.List;
import java.util.Map;

/**
 * Estructura que representa un contingut genèric de l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public abstract class ArxiuContingut {

	protected String nodeId;
	protected String titol;
	protected Map<String, Object> metadades;
	protected List<String> aspectes;
	protected String json;

	private String descripcio;
	private String serieDocumental;
	private String aplicacio;

	public ArxiuContingut(
			String nodeId,
			String titol,
			Map<String, Object> metadades,
			List<String> aspectes,
			String json) {
		this.nodeId = nodeId;
		this.titol = titol;
		this.metadades = metadades;
		this.aspectes = aspectes;
		this.json = json;
	}

	public String getNodeId() {
		return nodeId;
	}
	public String getTitol() {
		return titol;
	}
	public Map<String, Object> getMetadades() {
		return metadades;
	}
	public List<String> getAspectes() {
		return aspectes;
	}
	public String getJson() {
		return json;
	}
	public String getDescripcio() {
		return descripcio;
	}
	public void setDescripcio(String descripcio) {
		this.descripcio = descripcio;
	}
	public String getSerieDocumental() {
		return serieDocumental;
	}
	public void setSerieDocumental(String serieDocumental) {
		this.serieDocumental = serieDocumental;
	}
	public String getAplicacio() {
		return aplicacio;
	}
	public void setAplicacio(String aplicacio) {
		this.aplicacio = aplicacio;
	}

}
