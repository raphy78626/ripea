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

	private RegistreInteressatDocumentTipusEnumDto documentTipus;
	private String documentNum;
	private String nom;
	private String llinatge1;
	private String llinatge2;
	private String raoSocial;
	private String pais;
	private String provincia;
	private String municipi;
	private String adresa;
	private String codiPostal;
	private String email;
	private String telefon;
	private String emailHabilitat;
	private RegistreInteressatCanalEnumDto canalPreferent;
	private RegistreInteressatDocumentTipusEnumDto representantDocumentTipus;
	private String representantDocumentNum;
	private String representantNom;
	private String representantLlinatge1;
	private String representantLlinatge2;
	private String representantRaoSocial;
	private String representantPais;
	private String representantProvincia;
	private String representantMunicipi;
	private String representantAdresa;
	private String representantCodiPostal;
	private String representantEmail;
	private String representantTelefon;
	private String representantEmailHabilitat;
	private RegistreInteressatCanalEnumDto representantCanalPreferent;
	private String observacions;

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
	public RegistreInteressatDocumentTipusEnumDto getRepresentantDocumentTipus() {
		return representantDocumentTipus;
	}
	public void setRepresentantDocumentTipus(
			RegistreInteressatDocumentTipusEnumDto representantDocumentTipus) {
		this.representantDocumentTipus = representantDocumentTipus;
	}
	public String getRepresentantDocumentNum() {
		return representantDocumentNum;
	}
	public void setRepresentantDocumentNum(String representantDocumentNum) {
		this.representantDocumentNum = representantDocumentNum;
	}
	public String getRepresentantNom() {
		return representantNom;
	}
	public void setRepresentantNom(String representantNom) {
		this.representantNom = representantNom;
	}
	public String getRepresentantLlinatge1() {
		return representantLlinatge1;
	}
	public void setRepresentantLlinatge1(String representantLlinatge1) {
		this.representantLlinatge1 = representantLlinatge1;
	}
	public String getRepresentantLlinatge2() {
		return representantLlinatge2;
	}
	public void setRepresentantLlinatge2(String representantLlinatge2) {
		this.representantLlinatge2 = representantLlinatge2;
	}
	public String getRepresentantRaoSocial() {
		return representantRaoSocial;
	}
	public void setRepresentantRaoSocial(String representantRaoSocial) {
		this.representantRaoSocial = representantRaoSocial;
	}
	public String getRepresentantPais() {
		return representantPais;
	}
	public void setRepresentantPais(String representantPais) {
		this.representantPais = representantPais;
	}
	public String getRepresentantProvincia() {
		return representantProvincia;
	}
	public void setRepresentantProvincia(String representantProvincia) {
		this.representantProvincia = representantProvincia;
	}
	public String getRepresentantMunicipi() {
		return representantMunicipi;
	}
	public void setRepresentantMunicipi(String representantMunicipi) {
		this.representantMunicipi = representantMunicipi;
	}
	public String getRepresentantAdresa() {
		return representantAdresa;
	}
	public void setRepresentantAdresa(String representantAdresa) {
		this.representantAdresa = representantAdresa;
	}
	public String getRepresentantCodiPostal() {
		return representantCodiPostal;
	}
	public void setRepresentantCodiPostal(String representantCodiPostal) {
		this.representantCodiPostal = representantCodiPostal;
	}
	public String getRepresentantEmail() {
		return representantEmail;
	}
	public void setRepresentantEmail(String representantEmail) {
		this.representantEmail = representantEmail;
	}
	public String getRepresentantTelefon() {
		return representantTelefon;
	}
	public void setRepresentantTelefon(String representantTelefon) {
		this.representantTelefon = representantTelefon;
	}
	public String getRepresentantEmailHabilitat() {
		return representantEmailHabilitat;
	}
	public void setRepresentantEmailHabilitat(String representantEmailHabilitat) {
		this.representantEmailHabilitat = representantEmailHabilitat;
	}
	public RegistreInteressatCanalEnumDto getRepresentantCanalPreferent() {
		return representantCanalPreferent;
	}
	public void setRepresentantCanalPreferent(
			RegistreInteressatCanalEnumDto representantCanalPreferent) {
		this.representantCanalPreferent = representantCanalPreferent;
	}
	public String getObservacions() {
		return observacions;
	}
	public void setObservacions(String observacions) {
		this.observacions = observacions;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
