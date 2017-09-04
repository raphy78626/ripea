package es.caib.ripea.core.api.ws.callback;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import es.caib.notib.ws.notificacio.CertificacioArxiuTipusEnum;
import es.caib.notib.ws.notificacio.CertificacioTipusEnum;

/**
 * Informació sobre l'estat d'una notificació.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@XmlRootElement
@JsonAutoDetect
public class NotificacioCertificacioClient {
	
	
	private CertificacioTipusEnum tipus;
	private CertificacioArxiuTipusEnum arxiuTipus;
	private byte[] arxiuContingut;
	private String numSeguiment;
	private Date dataActualitzacio;
	
	
	public NotificacioCertificacioClient() {}
	
	public NotificacioCertificacioClient(
			CertificacioTipusEnum tipus, 
			CertificacioArxiuTipusEnum arxiuTipus,
			byte[] arxiuContingut, 
			String numSeguiment, 
			Date dataActualitzacio) {
		
		super();
		this.tipus = tipus;
		this.arxiuTipus = arxiuTipus;
		this.arxiuContingut = arxiuContingut;
		this.numSeguiment = numSeguiment;
		this.dataActualitzacio = dataActualitzacio;
	}
	
	
	public CertificacioTipusEnum getTipus() {
		return tipus;
	}
	public void setTipus(CertificacioTipusEnum tipus) {
		this.tipus = tipus;
	}
	public CertificacioArxiuTipusEnum getArxiuTipus() {
		return arxiuTipus;
	}
	public void setArxiuTipus(CertificacioArxiuTipusEnum arxiuTipus) {
		this.arxiuTipus = arxiuTipus;
	}
	public byte[] getArxiuContingut() {
		return arxiuContingut;
	}
	public void setArxiuContingut(byte[] arxiuContingut) {
		this.arxiuContingut = arxiuContingut;
	}
	public String getNumSeguiment() {
		return numSeguiment;
	}
	public void setNumSeguiment(String numSeguiment) {
		this.numSeguiment = numSeguiment;
	}
	public Date getDataActualitzacio() {
		return dataActualitzacio;
	}
	public void setDataActualitzacio(Date dataActualitzacio) {
		this.dataActualitzacio = dataActualitzacio;
	}
	
	
}
