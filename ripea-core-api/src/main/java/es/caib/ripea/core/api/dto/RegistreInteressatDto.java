/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Informació d'un interessat d'una anotació de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class RegistreInteressatDto implements Serializable {

	public Long id;
	public RegistreInteressatTipusEnumDto tipus;
	public RegistreInteressatDocumentTipusEnumDto documentTipus;
	public String documentNum;
	public String nom;
	public String llinatge1;
	public String llinatge2;
	public String raoSocial;
	public String pais;
	public String provincia;
	public String municipi;
	public String adresa;
	public String codiPostal;
	public String email;
	public String telefon;
	public String emailHabilitat;
	public RegistreInteressatCanalEnumDto canalPreferent;
	public String observacions;
	public RegistreInteressatDto representant;



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public RegistreInteressatTipusEnumDto getTipus() {
		return tipus;
	}
	public void setTipus(RegistreInteressatTipusEnumDto tipus) {
		this.tipus = tipus;
	}
	public RegistreInteressatDocumentTipusEnumDto getDocumentTipus() {
		return documentTipus;
	}
	public void setDocumentTipus(
			RegistreInteressatDocumentTipusEnumDto documentTipus) {
		this.documentTipus = documentTipus;
	}
	public String getDocumentNum() {
		return documentNum;
	}
	public void setDocumentNum(String documentNum) {
		this.documentNum = documentNum;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getLlinatge1() {
		return llinatge1;
	}
	public void setLlinatge1(String llinatge1) {
		this.llinatge1 = llinatge1;
	}
	public String getLlinatge2() {
		return llinatge2;
	}
	public void setLlinatge2(String llinatge2) {
		this.llinatge2 = llinatge2;
	}
	public String getRaoSocial() {
		return raoSocial;
	}
	public void setRaoSocial(String raoSocial) {
		this.raoSocial = raoSocial;
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
	public String getEmailHabilitat() {
		return emailHabilitat;
	}
	public void setEmailHabilitat(String emailHabilitat) {
		this.emailHabilitat = emailHabilitat;
	}
	public RegistreInteressatCanalEnumDto getCanalPreferent() {
		return canalPreferent;
	}
	public void setCanalPreferent(RegistreInteressatCanalEnumDto canalPreferent) {
		this.canalPreferent = canalPreferent;
	}
	public String getObservacions() {
		return observacions;
	}
	public void setObservacions(String observacions) {
		this.observacions = observacions;
	}
	public RegistreInteressatDto getRepresentant() {
		return representant;
	}
	public void setRepresentant(RegistreInteressatDto representant) {
		this.representant = representant;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
