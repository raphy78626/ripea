/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;

/**
 * Objecte que representa un municipi provinent d'una font externa.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class MunicipiDto implements Serializable {

	private String codi;
	private String nom;

	public String getCodi() {
		return codi;
	}
	public void setCodi(String codi) {
		this.codi = codi;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}

	private static final long serialVersionUID = -6781006082031161827L;

}
