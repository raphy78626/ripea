/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Informació del moviment d'una anotació de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class RegistreMovimentDto implements Serializable {

	private Long id;
	private ContenidorDto origen;
	private ContenidorDto desti;
	private Date data;
	private UsuariDto remitent;
	private String comentari;



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ContenidorDto getOrigen() {
		return origen;
	}
	public void setOrigen(ContenidorDto origen) {
		this.origen = origen;
	}
	public ContenidorDto getDesti() {
		return desti;
	}
	public void setDesti(ContenidorDto desti) {
		this.desti = desti;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public UsuariDto getRemitent() {
		return remitent;
	}
	public void setRemitent(UsuariDto remitent) {
		this.remitent = remitent;
	}
	public String getComentari() {
		return comentari;
	}
	public void setComentari(String comentari) {
		this.comentari = comentari;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
