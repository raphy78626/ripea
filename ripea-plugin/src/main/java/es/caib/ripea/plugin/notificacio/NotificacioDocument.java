package es.caib.ripea.plugin.notificacio;

/**
 * Informació d'un document adjunt a una notificació..
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class NotificacioDocument {
	
	private String arxiuNom;
    private String contingutBase64;
    private boolean generarCsv;
    private String hash;
    private String metadades;
    private boolean normalitzat;
    private String url;
    
    
	public NotificacioDocument(String arxiuNom, String contingutBase64, boolean generarCsv, String hash,
			String metadades, boolean normalitzat, String url) {
		super();
		this.arxiuNom = arxiuNom;
		this.contingutBase64 = contingutBase64;
		this.generarCsv = generarCsv;
		this.hash = hash;
		this.metadades = metadades;
		this.normalitzat = normalitzat;
		this.url = url;
	}


	public String getArxiuNom() {
		return arxiuNom;
	}
	public void setArxiuNom(String arxiuNom) {
		this.arxiuNom = arxiuNom;
	}
	
	public String getContingutBase64() {
		return contingutBase64;
	}
	public void setContingutBase64(String contingutBase64) {
		this.contingutBase64 = contingutBase64;
	}
	
	public boolean isGenerarCsv() {
		return generarCsv;
	}
	public void setGenerarCsv(boolean generarCsv) {
		this.generarCsv = generarCsv;
	}
	
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	public String getMetadades() {
		return metadades;
	}
	public void setMetadades(String metadades) {
		this.metadades = metadades;
	}
	
	public boolean isNormalitzat() {
		return normalitzat;
	}
	public void setNormalitzat(boolean normalitzat) {
		this.normalitzat = normalitzat;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
    
}
