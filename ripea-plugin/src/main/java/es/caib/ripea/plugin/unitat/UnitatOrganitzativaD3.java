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
public class UnitatOrganitzativaD3 implements Serializable, Comparable<UnitatOrganitzativaD3> {
	
	private String codigo;
	private String denominacion;
	private String raiz;
	private String localidad;
	
	@Override
	public int compareTo(UnitatOrganitzativaD3 o) {
		return denominacion.compareToIgnoreCase(o.getDenominacion());
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	public String getRaiz() {
		return raiz;
	}

	public void setRaiz(String raiz) {
		this.raiz = raiz;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	private static final long serialVersionUID = 4293950393461807359L;
}
