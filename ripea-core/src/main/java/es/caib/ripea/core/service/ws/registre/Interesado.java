/**
 * 
 */
package es.caib.ripea.core.service.ws.registre;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Dades d'un interessat segons l'estàndard SICRES_3.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Interesado {

	@XmlElement(required = false)
	private String tipoDocumentoIdentificacionInteresado;
	@XmlElement(required = false)
	private String documentoIdentificacionInteresado;
	@XmlElement(required = false)
	private String razonSocialInteresado;
	@XmlElement(required = false)
	private String nombreInteresado;
	@XmlElement(required = false)
	private String primerApellidoInteresado;
	@XmlElement(required = false)
	private String segundoApellidoInteresado;
	@XmlElement(required = false)
	private String tipoDocumentoIdentificacionRepresentante;
	@XmlElement(required = false)
	private String documentoIdentificacionRepresentante;
	@XmlElement(required = false)
	private String razonSocialRepresentante;
	@XmlElement(required = false)
	private String nombreRepresentante;
	@XmlElement(required = false)
	private String primerApellidoRepresentante;
	@XmlElement(required = false)
	private String segundoApellidoRepresentante;
	@XmlElement(required = false)
	private String paisInteresado;
	@XmlElement(required = false)
	private String provinciaInteresado;
	@XmlElement(required = false)
	private String municipioInteresado;
	@XmlElement(required = false)
	private String direccionInteresado;
	@XmlElement(required = false)
	private String codigoPostalInteresado;
	@XmlElement(required = false)
	private String correoElectronicoInteresado;
	@XmlElement(required = false)
	private String telefonoContactoInteresado;
	@XmlElement(required = false)
	private String direccionElectronicaHabilitadaInteresado;
	@XmlElement(required = false)
	private String canalPreferenteComunicacionInteresado;
	@XmlElement(required = false)
	private String paisRepresentante;
	@XmlElement(required = false)
	private String provinciaRepresentante;
	@XmlElement(required = false)
	private String municipioRepresentante;
	@XmlElement(required = false)
	private String direccionRepresentante;
	@XmlElement(required = false)
	private String codigoPostalRepresentante;
	@XmlElement(required = false)
	private String correoElectronicoRepresentante;
	@XmlElement(required = false)
	private String telefonoContactoRepresentante;
	@XmlElement(required = false)
	private String direccionElectronicaHabilitadaRepresentante;
	@XmlElement(required = false)
	private String canalPreferenteComunicacionRepresentante;
	@XmlElement(required = false)
	private String observaciones;

	public String getTipoDocumentoIdentificacionInteresado() {
		return tipoDocumentoIdentificacionInteresado;
	}
	public void setTipoDocumentoIdentificacionInteresado(
			String tipoDocumentoIdentificacionInteresado) {
		this.tipoDocumentoIdentificacionInteresado = tipoDocumentoIdentificacionInteresado;
	}
	public String getDocumentoIdentificacionInteresado() {
		return documentoIdentificacionInteresado;
	}
	public void setDocumentoIdentificacionInteresado(
			String documentoIdentificacionInteresado) {
		this.documentoIdentificacionInteresado = documentoIdentificacionInteresado;
	}
	public String getRazonSocialInteresado() {
		return razonSocialInteresado;
	}
	public void setRazonSocialInteresado(String razonSocialInteresado) {
		this.razonSocialInteresado = razonSocialInteresado;
	}
	public String getNombreInteresado() {
		return nombreInteresado;
	}
	public void setNombreInteresado(String nombreInteresado) {
		this.nombreInteresado = nombreInteresado;
	}
	public String getPrimerApellidoInteresado() {
		return primerApellidoInteresado;
	}
	public void setPrimerApellidoInteresado(String primerApellidoInteresado) {
		this.primerApellidoInteresado = primerApellidoInteresado;
	}
	public String getSegundoApellidoInteresado() {
		return segundoApellidoInteresado;
	}
	public void setSegundoApellidoInteresado(String segundoApellidoInteresado) {
		this.segundoApellidoInteresado = segundoApellidoInteresado;
	}
	public String getTipoDocumentoIdentificacionRepresentante() {
		return tipoDocumentoIdentificacionRepresentante;
	}
	public void setTipoDocumentoIdentificacionRepresentante(
			String tipoDocumentoIdentificacionRepresentante) {
		this.tipoDocumentoIdentificacionRepresentante = tipoDocumentoIdentificacionRepresentante;
	}
	public String getDocumentoIdentificacionRepresentante() {
		return documentoIdentificacionRepresentante;
	}
	public void setDocumentoIdentificacionRepresentante(
			String documentoIdentificacionRepresentante) {
		this.documentoIdentificacionRepresentante = documentoIdentificacionRepresentante;
	}
	public String getRazonSocialRepresentante() {
		return razonSocialRepresentante;
	}
	public void setRazonSocialRepresentante(String razonSocialRepresentante) {
		this.razonSocialRepresentante = razonSocialRepresentante;
	}
	public String getNombreRepresentante() {
		return nombreRepresentante;
	}
	public void setNombreRepresentante(String nombreRepresentante) {
		this.nombreRepresentante = nombreRepresentante;
	}
	public String getPrimerApellidoRepresentante() {
		return primerApellidoRepresentante;
	}
	public void setPrimerApellidoRepresentante(String primerApellidoRepresentante) {
		this.primerApellidoRepresentante = primerApellidoRepresentante;
	}
	public String getSegundoApellidoRepresentante() {
		return segundoApellidoRepresentante;
	}
	public void setSegundoApellidoRepresentante(String segundoApellidoRepresentante) {
		this.segundoApellidoRepresentante = segundoApellidoRepresentante;
	}
	public String getPaisInteresado() {
		return paisInteresado;
	}
	public void setPaisInteresado(String paisInteresado) {
		this.paisInteresado = paisInteresado;
	}
	public String getProvinciaInteresado() {
		return provinciaInteresado;
	}
	public void setProvinciaInteresado(String provinciaInteresado) {
		this.provinciaInteresado = provinciaInteresado;
	}
	public String getMunicipioInteresado() {
		return municipioInteresado;
	}
	public void setMunicipioInteresado(String municipioInteresado) {
		this.municipioInteresado = municipioInteresado;
	}
	public String getDireccionInteresado() {
		return direccionInteresado;
	}
	public void setDireccionInteresado(String direccionInteresado) {
		this.direccionInteresado = direccionInteresado;
	}
	public String getCodigoPostalInteresado() {
		return codigoPostalInteresado;
	}
	public void setCodigoPostalInteresado(String codigoPostalInteresado) {
		this.codigoPostalInteresado = codigoPostalInteresado;
	}
	public String getCorreoElectronicoInteresado() {
		return correoElectronicoInteresado;
	}
	public void setCorreoElectronicoInteresado(String correoElectronicoInteresado) {
		this.correoElectronicoInteresado = correoElectronicoInteresado;
	}
	public String getTelefonoContactoInteresado() {
		return telefonoContactoInteresado;
	}
	public void setTelefonoContactoInteresado(String telefonoContactoInteresado) {
		this.telefonoContactoInteresado = telefonoContactoInteresado;
	}
	public String getDireccionElectronicaHabilitadaInteresado() {
		return direccionElectronicaHabilitadaInteresado;
	}
	public void setDireccionElectronicaHabilitadaInteresado(
			String direccionElectronicaHabilitadaInteresado) {
		this.direccionElectronicaHabilitadaInteresado = direccionElectronicaHabilitadaInteresado;
	}
	public String getCanalPreferenteComunicacionInteresado() {
		return canalPreferenteComunicacionInteresado;
	}
	public void setCanalPreferenteComunicacionInteresado(
			String canalPreferenteComunicacionInteresado) {
		this.canalPreferenteComunicacionInteresado = canalPreferenteComunicacionInteresado;
	}
	public String getPaisRepresentante() {
		return paisRepresentante;
	}
	public void setPaisRepresentante(String paisRepresentante) {
		this.paisRepresentante = paisRepresentante;
	}
	public String getProvinciaRepresentante() {
		return provinciaRepresentante;
	}
	public void setProvinciaRepresentante(String provinciaRepresentante) {
		this.provinciaRepresentante = provinciaRepresentante;
	}
	public String getMunicipioRepresentante() {
		return municipioRepresentante;
	}
	public void setMunicipioRepresentante(String municipioRepresentante) {
		this.municipioRepresentante = municipioRepresentante;
	}
	public String getDireccionRepresentante() {
		return direccionRepresentante;
	}
	public void setDireccionRepresentante(String direccionRepresentante) {
		this.direccionRepresentante = direccionRepresentante;
	}
	public String getCodigoPostalRepresentante() {
		return codigoPostalRepresentante;
	}
	public void setCodigoPostalRepresentante(String codigoPostalRepresentante) {
		this.codigoPostalRepresentante = codigoPostalRepresentante;
	}
	public String getCorreoElectronicoRepresentante() {
		return correoElectronicoRepresentante;
	}
	public void setCorreoElectronicoRepresentante(
			String correoElectronicoRepresentante) {
		this.correoElectronicoRepresentante = correoElectronicoRepresentante;
	}
	public String getTelefonoContactoRepresentante() {
		return telefonoContactoRepresentante;
	}
	public void setTelefonoContactoRepresentante(
			String telefonoContactoRepresentante) {
		this.telefonoContactoRepresentante = telefonoContactoRepresentante;
	}
	public String getDireccionElectronicaHabilitadaRepresentante() {
		return direccionElectronicaHabilitadaRepresentante;
	}
	public void setDireccionElectronicaHabilitadaRepresentante(
			String direccionElectronicaHabilitadaRepresentante) {
		this.direccionElectronicaHabilitadaRepresentante = direccionElectronicaHabilitadaRepresentante;
	}
	public String getCanalPreferenteComunicacionRepresentante() {
		return canalPreferenteComunicacionRepresentante;
	}
	public void setCanalPreferenteComunicacionRepresentante(
			String canalPreferenteComunicacionRepresentante) {
		this.canalPreferenteComunicacionRepresentante = canalPreferenteComunicacionRepresentante;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

}
