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
public class ProvinciaRw3 implements Serializable, Comparable<ProvinciaRw3> {

	private String id;
	private String nombre;


	public ProvinciaRw3() {
		
	}
	
	public ProvinciaRw3(
			String id,
			String nombre,
			String codigoEntidadGeografica) {
		this.id = id;
		this.nombre = nombre;
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

	@Override
	public int compareTo(ProvinciaRw3 o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
