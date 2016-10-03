/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;

/**
 * Objecte que representa una província provinent d'una font externa.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ProvinciaDto implements Serializable {

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

	private static final long serialVersionUID = -5902192920575301790L;

}
