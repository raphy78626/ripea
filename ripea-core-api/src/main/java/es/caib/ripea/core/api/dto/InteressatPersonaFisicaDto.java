/**
 * 
 */
package es.caib.ripea.core.api.dto;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Informació d'un interessat de tipus ciutadà.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class InteressatPersonaFisicaDto extends InteressatDto {

	protected String nom;
	protected String llinatge1;
	protected String llinatge2;

	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getLlinatge1() {
		return llinatge1;
	}
	public void setLlinatge1(String llinatge1) {
		this.llinatge1 = llinatge1;
	}
	public String getLlinatge2() {
		return llinatge2;
	}
	public void setLlinatge2(String llinatge2) {
		this.llinatge2 = llinatge2;
	}

	public String getLlinatgesComaNom() {
		return llinatge1 + (llinatge2 != null ? " " + llinatge2 : "") + ", " + nom;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
