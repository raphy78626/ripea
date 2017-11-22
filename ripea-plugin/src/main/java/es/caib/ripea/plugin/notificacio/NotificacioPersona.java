package es.caib.ripea.plugin.notificacio;

/**
 * Informació d'una persona per a interactuar amb el Notib.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class NotificacioPersona {
	
	private String nom;
    private String llinatge1;
    private String llinatge2;
    private String nif;
    private String email;
    private String telefon;
    
    
	public NotificacioPersona(
			String nom,
			String llinatge1,
			String llinatge2,
			String nif,
			String email,
			String telefon) {
		super();
		this.nom = nom;
		this.llinatge1 = llinatge1;
		this.llinatge2 = llinatge2;
		this.nif = nif;
		this.email = email;
		this.telefon = telefon;
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
	
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
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
    
	
    
}
