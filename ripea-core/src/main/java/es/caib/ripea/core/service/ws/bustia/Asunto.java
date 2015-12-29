/**
 * 
 */
package es.caib.ripea.core.service.ws.bustia;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Dades de l'assumpte segons l'estàndard SICRES_3.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Asunto {

	@XmlElement(required = true)
	private String resumen;
	@XmlElement(required = false)
	private String codigoAsuntoSegunDestino;
	@XmlElement(required = false)
	private String referenciaExterna;
	@XmlElement(required = false)
	private String numeroExpediente;

	public String getResumen() {
		return resumen;
	}
	public void setResumen(String resumen) {
		this.resumen = resumen;
	}
	public String getCodigoAsuntoSegunDestino() {
		return codigoAsuntoSegunDestino;
	}
	public void setCodigoAsuntoSegunDestino(String codigoAsuntoSegunDestino) {
		this.codigoAsuntoSegunDestino = codigoAsuntoSegunDestino;
	}
	public String getReferenciaExterna() {
		return referenciaExterna;
	}
	public void setReferenciaExterna(String referenciaExterna) {
		this.referenciaExterna = referenciaExterna;
	}
	public String getNumeroExpediente() {
		return numeroExpediente;
	}
	public void setNumeroExpediente(String numeroExpediente) {
		this.numeroExpediente = numeroExpediente;
	}

}
