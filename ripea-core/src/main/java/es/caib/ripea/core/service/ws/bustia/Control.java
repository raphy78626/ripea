/**
 * 
 */
package es.caib.ripea.core.service.ws.bustia;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Dades de control segons l'estàndard SICRES_3.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Control {

	@XmlElement(required = false)
	private String tipoTransporteEntrada;
	@XmlElement(required = false)
	private String numeroTransporteEntrada;
	@XmlElement(required = false)
	private String nombreUsuario;
	@XmlElement(required = false)
	private String contactoUsuario;
	@XmlElement(required = true)
	private String identificadorIntercambio;
	@XmlElement(required = false)
	private String aplicacionVersionEmisora;
	@XmlElement(required = true)
	private String tipoAnotacion;
	@XmlElement(required = false)
	private String descripcionTipoAnotacion;
	@XmlElement(required = true)
	private String tipoRegistro;
	@XmlElement(required = true)
	private String documentacionFisica;
	@XmlElement(required = false)
	private String observacionesApunte;
	@XmlElement(required = true)
	private String indicadorPrueba;
	@XmlElement(required = true)
	private String codigoEntidadRegistralInicio;
	@XmlElement(required = false)
	private String decodificacionEntidadRegistralInicio;

	public String getTipoTransporteEntrada() {
		return tipoTransporteEntrada;
	}
	public void setTipoTransporteEntrada(String tipoTransporteEntrada) {
		this.tipoTransporteEntrada = tipoTransporteEntrada;
	}
	public String getNumeroTransporteEntrada() {
		return numeroTransporteEntrada;
	}
	public void setNumeroTransporteEntrada(String numeroTransporteEntrada) {
		this.numeroTransporteEntrada = numeroTransporteEntrada;
	}
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public String getContactoUsuario() {
		return contactoUsuario;
	}
	public void setContactoUsuario(String contactoUsuario) {
		this.contactoUsuario = contactoUsuario;
	}
	public String getIdentificadorIntercambio() {
		return identificadorIntercambio;
	}
	public void setIdentificadorIntercambio(String identificadorIntercambio) {
		this.identificadorIntercambio = identificadorIntercambio;
	}
	public String getAplicacionVersionEmisora() {
		return aplicacionVersionEmisora;
	}
	public void setAplicacionVersionEmisora(String aplicacionVersionEmisora) {
		this.aplicacionVersionEmisora = aplicacionVersionEmisora;
	}
	public String getTipoAnotacion() {
		return tipoAnotacion;
	}
	public void setTipoAnotacion(String tipoAnotacion) {
		this.tipoAnotacion = tipoAnotacion;
	}
	public String getDescripcionTipoAnotacion() {
		return descripcionTipoAnotacion;
	}
	public void setDescripcionTipoAnotacion(String descripcionTipoAnotacion) {
		this.descripcionTipoAnotacion = descripcionTipoAnotacion;
	}
	public String getTipoRegistro() {
		return tipoRegistro;
	}
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	public String getDocumentacionFisica() {
		return documentacionFisica;
	}
	public void setDocumentacionFisica(String documentacionFisica) {
		this.documentacionFisica = documentacionFisica;
	}
	public String getObservacionesApunte() {
		return observacionesApunte;
	}
	public void setObservacionesApunte(String observacionesApunte) {
		this.observacionesApunte = observacionesApunte;
	}
	public String getIndicadorPrueba() {
		return indicadorPrueba;
	}
	public void setIndicadorPrueba(String indicadorPrueba) {
		this.indicadorPrueba = indicadorPrueba;
	}
	public String getCodigoEntidadRegistralInicio() {
		return codigoEntidadRegistralInicio;
	}
	public void setCodigoEntidadRegistralInicio(String codigoEntidadRegistralInicio) {
		this.codigoEntidadRegistralInicio = codigoEntidadRegistralInicio;
	}
	public String getDecodificacionEntidadRegistralInicio() {
		return decodificacionEntidadRegistralInicio;
	}
	public void setDecodificacionEntidadRegistralInicio(
			String decodificacionEntidadRegistralInicio) {
		this.decodificacionEntidadRegistralInicio = decodificacionEntidadRegistralInicio;
	}

}
