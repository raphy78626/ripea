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
