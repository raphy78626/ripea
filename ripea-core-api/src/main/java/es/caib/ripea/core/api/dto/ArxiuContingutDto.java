/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;

/**
 * Informació d'un contingut emmagatzemada a l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuContingutDto implements Serializable {

	private String identificador;
	private String nom;
	private ArxiuContingutTipusEnumDto tipus;

	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public ArxiuContingutTipusEnumDto getTipus() {
		return tipus;
	}
	public void setTipus(ArxiuContingutTipusEnumDto tipus) {
		this.tipus = tipus;
	}

	private static final long serialVersionUID = -2124829280908976623L;

}
