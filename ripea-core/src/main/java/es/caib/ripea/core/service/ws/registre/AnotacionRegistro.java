/**
 * 
 */
package es.caib.ripea.core.service.ws.registre;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Anotació de registre seguint l'estàndard SICRES_3.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AnotacionRegistro {

	@XmlElement(required = true)
	private String accion;
	@XmlElement(required = true)
	private Origen origen;
	@XmlElement(required = true)
	private Destino destino;
	@XmlElement(required = true)
	private List<Interesado> interesados;
	@XmlElement(required = true)
	private Asunto asunto;
	@XmlElement(required = false)
	private List<Anexo> anexos;
	@XmlElement(required = true)
	private Control control;
	@XmlElement(required = false)
	private FormularioGenerico formularioGenerico;



	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	public Origen getOrigen() {
		return origen;
	}
	public void setOrigen(Origen origen) {
		this.origen = origen;
	}
	public Destino getDestino() {
		return destino;
	}
	public void setDestino(Destino destino) {
		this.destino = destino;
	}
	public List<Interesado> getInteresados() {
		return interesados;
	}
	public void setInteresados(List<Interesado> interesados) {
		this.interesados = interesados;
	}
	public Asunto getAsunto() {
		return asunto;
	}
	public void setAsunto(Asunto asunto) {
		this.asunto = asunto;
	}
	public List<Anexo> getAnexos() {
		return anexos;
	}
	public void setAnexos(List<Anexo> anexos) {
		this.anexos = anexos;
	}
	public Control getControl() {
		return control;
	}
	public void setControl(Control control) {
		this.control = control;
	}
	public FormularioGenerico getFormularioGenerico() {
		return formularioGenerico;
	}
	public void setFormularioGenerico(FormularioGenerico formularioGenerico) {
		this.formularioGenerico = formularioGenerico;
	}

}
