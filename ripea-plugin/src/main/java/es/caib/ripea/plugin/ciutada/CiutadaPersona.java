/**
 * 
 */
package es.caib.ripea.plugin.ciutada;

/**
 * Informació d'una persona per a interactuar amb la zona personal.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class CiutadaPersona {

	private String nif;
	private String nom;
	private String llinatge1;
	private String llinatge2;
	private String paisCodi;
	private String paisNom;
	private String provinciaCodi;
	private String provinciaNom;
	private String localitatCodi;
	private String localitatNom;



	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
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
	public String getPaisCodi() {
		return paisCodi;
	}
	public void setPaisCodi(String paisCodi) {
		this.paisCodi = paisCodi;
	}
	public String getPaisNom() {
		return paisNom;
	}
	public void setPaisNom(String paisNom) {
		this.paisNom = paisNom;
	}
	public String getProvinciaCodi() {
		return provinciaCodi;
	}
	public void setProvinciaCodi(String provinciaCodi) {
		this.provinciaCodi = provinciaCodi;
	}
	public String getProvinciaNom() {
		return provinciaNom;
	}
	public void setProvinciaNom(String provinciaNom) {
		this.provinciaNom = provinciaNom;
	}
	public String getLocalitatCodi() {
		return localitatCodi;
	}
	public void setLocalitatCodi(String localitatCodi) {
		this.localitatCodi = localitatCodi;
	}
	public String getLocalitatNom() {
		return localitatNom;
	}
	public void setLocalitatNom(String localitatNom) {
		this.localitatNom = localitatNom;
	}
	public String getLlinatges() {
		if (llinatge2 == null) {
			return llinatge1;
		} else {
			return llinatge1 + " " + llinatge2;
		}
	}
	public String getLlinatgesComaNom() {
		return getLlinatges() + ", " + getNom();
	}

}
