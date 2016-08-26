/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Informació d'un interessat.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public abstract class InteressatDto implements Serializable {

	protected Long id;
	protected InteressatTipusEnumDto tipus;
	protected InteressatDocumentTipusEnumDto documentTipus;
	protected String documentNum;
	protected String pais;
	protected String provincia;
	protected String municipi;
	protected String adresa;
	protected String codiPostal;
	protected String email;
	protected String telefon;
	protected String observacions;
	protected IndiomaEnumDto notificacioIdioma;
	protected Boolean notificacioAutoritzat;
	protected Long representantId;
	protected String identificador;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public abstract InteressatTipusEnumDto getTipus();
	public void setTipus(InteressatTipusEnumDto tipus) {
		this.tipus = tipus;
	}
	public InteressatDocumentTipusEnumDto getDocumentTipus() {
		return documentTipus;
	}
	public void setDocumentTipus(InteressatDocumentTipusEnumDto documentTipus) {
		this.documentTipus = documentTipus;
	}
	public String getDocumentNum() {
		return documentNum;
	}
	public void setDocumentNum(String documentNum) {
		this.documentNum = documentNum;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getMunicipi() {
		return municipi;
	}
	public void setMunicipi(String municipi) {
		this.municipi = municipi;
	}
	public String getAdresa() {
		return adresa;
	}
	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}
	public String getCodiPostal() {
		return codiPostal;
	}
	public void setCodiPostal(String codiPostal) {
		this.codiPostal = codiPostal;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefon() {
		return telefon;
	}
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	public String getObservacions() {
		return observacions;
	}
	public void setObservacions(String observacions) {
		this.observacions = observacions;
	}
	public IndiomaEnumDto getNotificacioIdioma() {
		return notificacioIdioma;
	}
	public void setNotificacioIdioma(IndiomaEnumDto notificacioIdioma) {
		this.notificacioIdioma = notificacioIdioma;
	}
	public Boolean getNotificacioAutoritzat() {
		return notificacioAutoritzat;
	}
	public void setNotificacioAutoritzat(Boolean notificacioAutoritzat) {
		this.notificacioAutoritzat = notificacioAutoritzat;
	}
	public Long getRepresentantId() {
		return representantId;
	}
	public void setRepresentantId(Long representantId) {
		this.representantId = representantId;
	}
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public boolean isPersonaFisica() {
		return this instanceof InteressatPersonaFisicaDto;
	}
	public boolean isPersonaJuridica() {
		return this instanceof InteressatPersonaJuridicaDto;
	}
	public boolean isAdministracio() {
		return this instanceof InteressatAdministracioDto;
	}

	private static final long serialVersionUID = -139254994389509932L;

}
