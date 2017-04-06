/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;

/**
 * Informació d'un node fill emmagatzemat amb el plugin d'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuPluginNodeFillDto implements Serializable {

	private String nodeId;
	private String nom;
	private ArxiuPluginNodeFillTipusEnumDto tipus;



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
	public ArxiuPluginNodeFillTipusEnumDto getTipus() {
		return tipus;
	}
	public void setTipus(ArxiuPluginNodeFillTipusEnumDto tipus) {
		this.tipus = tipus;
	}

	private static final long serialVersionUID = -2124829280908976623L;

}
