package es.caib.ripea.core.api.ws.callback;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import es.caib.notib.ws.notificacio.NotificacioEstatEnum;


/**
 * Informació sobre l'estat d'una notificació.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@XmlRootElement
@JsonAutoDetect
public class NotificacioEstatClient {
	
	
	private NotificacioEstatEnum estat;
	private Date data;
	private String receptorNom;
	private String receptorNif;
	private String origen;
	private String numSeguiment;
	private String referenciaEnviament;
	
	
	public NotificacioEstatClient() {}

	public NotificacioEstatClient(
			NotificacioEstatEnum estat,
			Date data,
			String receptorNom,
			String receptorNif,
			String origen,
			String numSeguiment,
			String referenciaEnviament) {
		
		super();
		this.estat = estat;
		this.data = data;
		this.receptorNom = receptorNom;
		this.receptorNif = receptorNif;
		this.origen = origen;
		this.numSeguiment = numSeguiment;
		this.referenciaEnviament = referenciaEnviament;
	}
	public NotificacioEstatEnum getEstat() {
		return estat;
	}
	public void setEstat(NotificacioEstatEnum estat) {
		this.estat = estat;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getReceptorNom() {
		return receptorNom;
	}
	public void setReceptorNom(String receptorNom) {
		this.receptorNom = receptorNom;
	}
	public String getReceptorNif() {
		return receptorNif;
	}
	public void setReceptorNif(String receptorNif) {
		this.receptorNif = receptorNif;
	}
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	public String getNumSeguiment() {
		return numSeguiment;
	}
	public void setNumSeguiment(String numSeguiment) {
		this.numSeguiment = numSeguiment;
	}
	public String getReferenciaEnviament() {
		return referenciaEnviament;
	}
	public void setReferenciaEnviament(String referenciaEnviament) {
		this.referenciaEnviament = referenciaEnviament;
	}
}
