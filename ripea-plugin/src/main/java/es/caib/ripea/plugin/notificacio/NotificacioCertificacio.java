package es.caib.ripea.plugin.notificacio;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Informació certificació de resposta per interactuar amb el Notib.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class NotificacioCertificacio {
	
	protected String contingutBase64;
    protected String csv;
    protected XMLGregorianCalendar data;
    protected String hash;
    protected String metadades;
    protected String origen;
    protected int tamany;
    protected String tipusMime;
    
    
	public NotificacioCertificacio(
			String contingutBase64,
			String csv,
			XMLGregorianCalendar data,
			String hash,
			String metadades,
			String origen,
			int tamany,
			String tipusMime) {
		super();
		this.contingutBase64 = contingutBase64;
		this.csv = csv;
		this.data = data;
		this.hash = hash;
		this.metadades = metadades;
		this.origen = origen;
		this.tamany = tamany;
		this.tipusMime = tipusMime;
	}


	public String getContingutBase64() {
		return contingutBase64;
	}
	public void setContingutBase64(String contingutBase64) {
		this.contingutBase64 = contingutBase64;
	}
	
	public String getCsv() {
		return csv;
	}
	public void setCsv(String csv) {
		this.csv = csv;
	}
	
	public XMLGregorianCalendar getData() {
		return data;
	}
	public void setData(XMLGregorianCalendar data) {
		this.data = data;
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
	
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	
	public int getTamany() {
		return tamany;
	}
	public void setTamany(int tamany) {
		this.tamany = tamany;
	}
	
	public String getTipusMime() {
		return tipusMime;
	}
	public void setTipusMime(String tipusMime) {
		this.tipusMime = tipusMime;
	}
	
}
