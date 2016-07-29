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

	protected String raoSocial;

	public String getRaoSocial() {
		return raoSocial;
	}
	public void setRaoSocial(String raoSocial) {
		this.raoSocial = raoSocial;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
