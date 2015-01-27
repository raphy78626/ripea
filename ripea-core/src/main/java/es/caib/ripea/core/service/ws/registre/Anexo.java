/**
 * 
 */
package es.caib.ripea.core.service.ws.registre;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Dades d'un annex segons l'estàndard SICRES_3 modificat per a
 * enviar un id de la gestió documental en comptes del contingut de
 * l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Anexo {

	@XmlElement(required = true)
	private String nombreFichero;
	@XmlElement(required = true)
	private String identificador;
	@XmlElement(required = false)
	private String validez;
	@XmlElement(required = true)
	private String tipo;
	@XmlElement(required = false)
	private String gestionDocumentalId;
	@XmlElement(required = false)
	private String identificadorDocumentoFirmado;
	@XmlElement(required = false)
	private String observaciones;

	public String getNombreFichero() {
		return nombreFichero;
	}
	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public String getValidez() {
		return validez;
	}
	public void setValidez(String validez) {
		this.validez = validez;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getGestionDocumentalId() {
		return gestionDocumentalId;
	}
	public void setGestionDocumentalId(String gestionDocumentalId) {
		this.gestionDocumentalId = gestionDocumentalId;
	}
	public String getIdentificadorDocumentoFirmado() {
		return identificadorDocumentoFirmado;
	}
	public void setIdentificadorDocumentoFirmado(
			String identificadorDocumentoFirmado) {
		this.identificadorDocumentoFirmado = identificadorDocumentoFirmado;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

}
