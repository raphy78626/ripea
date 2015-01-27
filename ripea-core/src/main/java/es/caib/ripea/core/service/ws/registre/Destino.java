/**
 * 
 */
package es.caib.ripea.core.service.ws.registre;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Dades del destí segons l'estàndard SICRES_3.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Destino {

	@XmlElement(required = true)
	private String codigoEntidadRegistralDestino;
	@XmlElement(required = false)
	private String decodificacionEntidadRegistralDestino;
	@XmlElement(required = false)
	private String codigoUnidadTramitacionDestino;
	@XmlElement(required = false)
	private String decodificacionUnidadTramitacionDestino;

	public String getCodigoEntidadRegistralDestino() {
		return codigoEntidadRegistralDestino;
	}
	public void setCodigoEntidadRegistralDestino(
			String codigoEntidadRegistralDestino) {
		this.codigoEntidadRegistralDestino = codigoEntidadRegistralDestino;
	}
	public String getDecodificacionEntidadRegistralDestino() {
		return decodificacionEntidadRegistralDestino;
	}
	public void setDecodificacionEntidadRegistralDestino(
			String decodificacionEntidadRegistralDestino) {
		this.decodificacionEntidadRegistralDestino = decodificacionEntidadRegistralDestino;
	}
	public String getCodigoUnidadTramitacionDestino() {
		return codigoUnidadTramitacionDestino;
	}
	public void setCodigoUnidadTramitacionDestino(
			String codigoUnidadTramitacionDestino) {
		this.codigoUnidadTramitacionDestino = codigoUnidadTramitacionDestino;
	}
	public String getDecodificacionUnidadTramitacionDestino() {
		return decodificacionUnidadTramitacionDestino;
	}
	public void setDecodificacionUnidadTramitacionDestino(
			String decodificacionUnidadTramitacionDestino) {
		this.decodificacionUnidadTramitacionDestino = decodificacionUnidadTramitacionDestino;
	}

}
