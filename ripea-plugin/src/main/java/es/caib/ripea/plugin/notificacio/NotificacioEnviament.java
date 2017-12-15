package es.caib.ripea.plugin.notificacio;

/**
 * Informació d'un enviament per a interactuar amb el Notib.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class NotificacioEnviament {
	
	private NotificacioPersona destinatari;
	private NotificacioEntregaDeh entregaDeh;
	private NotificacioEntregaPostal entregaPostal;
	private String referencia;
    private NotificacioServeiTipusEnum serveiTipus;
    private NotificacioPersona titular;
    
    
	public NotificacioEnviament(
			NotificacioPersona destinatari,
			NotificacioEntregaDeh entregaDeh,
			NotificacioEntregaPostal entregaPostal,
			String referencia,
			NotificacioServeiTipusEnum serveiTipus,
			NotificacioPersona titular) {
		super();
		this.destinatari = destinatari;
		this.entregaDeh = entregaDeh;
		this.entregaPostal = entregaPostal;
		this.referencia = referencia;
		this.serveiTipus = serveiTipus;
		this.titular = titular;
	}
	
	
	public NotificacioPersona getDestinatari() {
		return destinatari;
	}
	public void setDestinatari(NotificacioPersona destinataris) {
		this.destinatari = destinataris;
	}
	
	public NotificacioEntregaDeh getEntregaDeh() {
		return entregaDeh;
	}
	public void setEntregaDeh(NotificacioEntregaDeh entregaDeh) {
		this.entregaDeh = entregaDeh;
	}
	
	public NotificacioEntregaPostal getEntregaPostal() {
		return entregaPostal;
	}
	public void setEntregaPostal(NotificacioEntregaPostal entregaPostal) {
		this.entregaPostal = entregaPostal;
	}
	
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	
	public NotificacioServeiTipusEnum getServeiTipus() {
		return serveiTipus;
	}
	public void setServeiTipus(NotificacioServeiTipusEnum serveiTipus) {
		this.serveiTipus = serveiTipus;
	}
	
	public NotificacioPersona getTitular() {
		return titular;
	}
	public void setTitular(NotificacioPersona titular) {
		this.titular = titular;
	}
    
    
}
