/**
 * 
 */
package es.caib.ripea.core.service.ws.bustia;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Dades de l'origen segons l'estàndard SICRES_3.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Origen {

	@XmlElement(required = true)
	private String codigoEntidadRegistralOrigen;
	@XmlElement(required = false)
	private String decodificacionEntidadRegistralOrigen;
	@XmlElement(required = true)
	private String numeroRegistroEntrada;
	@XmlElement(required = true)
	private String fechaHoraEntrada;
	@XmlElement(required = false)
	private String timestampEntrada;
	@XmlElement(required = false)
	private String codigoUnidadTramitacionOrigen;
	@XmlElement(required = false)
	private String decodificacionUnidadTramitacionOrigen;

	public String getCodigoEntidadRegistralOrigen() {
		return codigoEntidadRegistralOrigen;
	}
	public void setCodigoEntidadRegistralOrigen(String codigoEntidadRegistralOrigen) {
		this.codigoEntidadRegistralOrigen = codigoEntidadRegistralOrigen;
	}
	public String getDecodificacionEntidadRegistralOrigen() {
		return decodificacionEntidadRegistralOrigen;
	}
	public void setDecodificacionEntidadRegistralOrigen(
			String decodificacionEntidadRegistralOrigen) {
		this.decodificacionEntidadRegistralOrigen = decodificacionEntidadRegistralOrigen;
	}
	public String getNumeroRegistroEntrada() {
		return numeroRegistroEntrada;
	}
	public void setNumeroRegistroEntrada(String numeroRegistroEntrada) {
		this.numeroRegistroEntrada = numeroRegistroEntrada;
	}
	public String getFechaHoraEntrada() {
		return fechaHoraEntrada;
	}
	public void setFechaHoraEntrada(String fechaHoraEntrada) {
		this.fechaHoraEntrada = fechaHoraEntrada;
	}
	public String getTimestampEntrada() {
		return timestampEntrada;
	}
	public void setTimestampEntrada(String timestampEntrada) {
		this.timestampEntrada = timestampEntrada;
	}
	public String getCodigoUnidadTramitacionOrigen() {
		return codigoUnidadTramitacionOrigen;
	}
	public void setCodigoUnidadTramitacionOrigen(
			String codigoUnidadTramitacionOrigen) {
		this.codigoUnidadTramitacionOrigen = codigoUnidadTramitacionOrigen;
	}
	public String getDecodificacionUnidadTramitacionOrigen() {
		return decodificacionUnidadTramitacionOrigen;
	}
	public void setDecodificacionUnidadTramitacionOrigen(
			String decodificacionUnidadTramitacionOrigen) {
		this.decodificacionUnidadTramitacionOrigen = decodificacionUnidadTramitacionOrigen;
	}

}
