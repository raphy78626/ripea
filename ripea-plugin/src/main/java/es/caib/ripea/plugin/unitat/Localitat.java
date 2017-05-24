/**
 * 
 */
package es.caib.ripea.plugin.unitat;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Informació d'una unitat organitzativa.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Localitat implements Serializable, Comparable<Localitat> {

	private String id;
	private String nombre;
	private String codigoEntidadGeografica;


	public Localitat() {
		
	}
	
	public Localitat(
			String id,
			String nombre,
			String codigoEntidadGeografica) {
		this.id = id;
		this.nombre = nombre;
		this.codigoEntidadGeografica = codigoEntidadGeografica;
	}

	private static final long serialVersionUID = -5602898182576627524L;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodigoEntidadGeografica() {
		return codigoEntidadGeografica;
	}

	public void setCodigoEntidadGeografica(String codigoEntidadGeografica) {
		this.codigoEntidadGeografica = codigoEntidadGeografica;
	}

	@Override
	public int compareTo(Localitat o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
