/**
 * 
 */
package es.caib.ripea.core.api.dto;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Informació d'un interessat de tipus administració.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class InteressatAdministracioDto extends InteressatDto {

	private String identificador;



	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
