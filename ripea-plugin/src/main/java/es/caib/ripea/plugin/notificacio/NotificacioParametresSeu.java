package es.caib.ripea.plugin.notificacio;

/**
 * Informació que empra Notib per interactuar amb la Seu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class NotificacioParametresSeu {
	
	private String avisText;
    private String avisTextMobil;
    private String avisTitol;
    private String expedientIdentificadorEni;
    private String expedientSerieDocumental;
    private String expedientTitol;
    private String expedientUnitatOrganitzativa;
    private String idioma;
    private String oficiText;
    private String oficiTitol;
    private String registreLlibre;
    private String registreOficina;
    
    
	public NotificacioParametresSeu(
			String avisText,
			String avisTextMobil,
			String avisTitol,
			String expedientIdentificadorEni,
			String expedientSerieDocumental,
			String expedientTitol,
			String expedientUnitatOrganitzativa,
			String idioma,
			String oficiText,
			String oficiTitol,
			String registreLlibre,
			String registreOficina) {
		super();
		this.avisText = avisText;
		this.avisTextMobil = avisTextMobil;
		this.avisTitol = avisTitol;
		this.expedientIdentificadorEni = expedientIdentificadorEni;
		this.expedientSerieDocumental = expedientSerieDocumental;
		this.expedientTitol = expedientTitol;
		this.expedientUnitatOrganitzativa = expedientUnitatOrganitzativa;
		this.idioma = idioma;
		this.oficiText = oficiText;
		this.oficiTitol = oficiTitol;
		this.registreLlibre = registreLlibre;
		this.registreOficina = registreOficina;
	}


	public String getAvisText() {
		return avisText;
	}
	public void setAvisText(String avisText) {
		this.avisText = avisText;
	}
	
	public String getAvisTextMobil() {
		return avisTextMobil;
	}
	public void setAvisTextMobil(String avisTextMobil) {
		this.avisTextMobil = avisTextMobil;
	}
	
	public String getAvisTitol() {
		return avisTitol;
	}
	public void setAvisTitol(String avisTitol) {
		this.avisTitol = avisTitol;
	}
	
	public String getExpedientIdentificadorEni() {
		return expedientIdentificadorEni;
	}
	public void setExpedientIdentificadorEni(String expedientIdentificadorEni) {
		this.expedientIdentificadorEni = expedientIdentificadorEni;
	}
	
	public String getExpedientSerieDocumental() {
		return expedientSerieDocumental;
	}
	public void setExpedientSerieDocumental(String expedientSerieDocumental) {
		this.expedientSerieDocumental = expedientSerieDocumental;
	}
	
	public String getExpedientTitol() {
		return expedientTitol;
	}
	public void setExpedientTitol(String expedientTitol) {
		this.expedientTitol = expedientTitol;
	}
	
	public String getExpedientUnitatOrganitzativa() {
		return expedientUnitatOrganitzativa;
	}
	public void setExpedientUnitatOrganitzativa(String expedientUnitatOrganitzativa) {
		this.expedientUnitatOrganitzativa = expedientUnitatOrganitzativa;
	}
	
	public String getIdioma() {
		return idioma;
	}
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	
	public String getOficiText() {
		return oficiText;
	}
	public void setOficiText(String oficiText) {
		this.oficiText = oficiText;
	}
	
	public String getOficiTitol() {
		return oficiTitol;
	}
	public void setOficiTitol(String oficiTitol) {
		this.oficiTitol = oficiTitol;
	}
	
	public String getRegistreLlibre() {
		return registreLlibre;
	}
	public void setRegistreLlibre(String registreLlibre) {
		this.registreLlibre = registreLlibre;
	}
	
	public String getRegistreOficina() {
		return registreOficina;
	}
	public void setRegistreOficina(String registreOficina) {
		this.registreOficina = registreOficina;
	}
    
	
}
