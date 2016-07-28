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
public class InteressatPersonaJuridicaDto extends InteressatDto {

	private String llinatges;
	private String nif;



	public String getLlinatges() {
		return llinatges;
	}
	public void setLlinatges(String llinatges) {
		this.llinatges = llinatges;
	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getLlinatgesComaNom() {
		return llinatges + ", " + nom;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
