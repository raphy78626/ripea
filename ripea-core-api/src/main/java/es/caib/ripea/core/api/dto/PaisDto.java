/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;

/**
 * Objecte que representa un pais provinent d'una font externa.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class PaisDto implements Serializable {

	private int codiNumeric;
	private String codi2Char;
	private String codi3Char;
	private String nom;

	public int getCodiNumeric() {
		return codiNumeric;
	}

	public void setCodiNumeric(int codiNumeric) {
		this.codiNumeric = codiNumeric;
	}

	public String getCodi2Char() {
		return codi2Char;
	}

	public void setCodi2Char(String codi2Char) {
		this.codi2Char = codi2Char;
	}

	public String getCodi3Char() {
		return codi3Char;
	}

	public void setCodi3Char(String codi3Char) {
		this.codi3Char = codi3Char;
	}

	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}

	private static final long serialVersionUID = -8710022935513464199L;

}
