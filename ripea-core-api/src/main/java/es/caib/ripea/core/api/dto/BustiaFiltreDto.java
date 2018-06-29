/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Informació del filtre de continguts.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class BustiaFiltreDto implements Serializable {

	private String unitatCodi;
	private String nom;
	
	// if the obsolete is true we look for the busties of extinguished or anulated unitats  
	private Boolean unitatObsoleta;
	
	public Boolean getUnitatObsoleta() {
		return unitatObsoleta;
	}
	public void setUnitatObsoleta(Boolean unitatObsoleta) {
		this.unitatObsoleta = unitatObsoleta;
	}
	public String getUnitatCodi() {
		return unitatCodi;
	}
	public void setUnitatCodi(String unitatCodi) {
		this.unitatCodi = unitatCodi;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
