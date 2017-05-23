/**
 * 
 */
package es.caib.ripea.war.command;

import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Command per a enviar documents a la passarela de firma.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class PassarelaFirmaEnviarCommand {

	@NotEmpty @Size(max=256)
	private String motiu;
	@Size(max=256)
	private String lloc;

	public String getMotiu() {
		return motiu;
	}
	public void setMotiu(String motiu) {
		this.motiu = motiu;
	}
	public String getLloc() {
		return lloc;
	}
	public void setLloc(String lloc) {
		this.lloc = lloc;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
