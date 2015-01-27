/**
 * 
 */
package es.caib.ripea.war.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Command per a copiar, moure o enviar contenidors.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ContenidorMoureCopiarEnviarCommand {

	@NotNull
	protected Long contenidorOrigenId;
	@NotNull
	protected Long contenidorDestiId;
	@Size(max=256)
	protected String comentariEnviar;



	public Long getContenidorOrigenId() {
		return contenidorOrigenId;
	}
	public void setContenidorOrigenId(Long contenidorOrigenId) {
		this.contenidorOrigenId = contenidorOrigenId;
	}
	public Long getContenidorDestiId() {
		return contenidorDestiId;
	}
	public void setContenidorDestiId(Long contenidorDestiId) {
		this.contenidorDestiId = contenidorDestiId;
	}
	public String getComentariEnviar() {
		return comentariEnviar;
	}
	public void setComentariEnviar(String comentariEnviar) {
		this.comentariEnviar = comentariEnviar;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
