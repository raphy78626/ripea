package es.caib.ripea.plugin.notificacio;

/**
 * Informació de la entrega deh per a interactuar amb el Notib.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class NotificacioEntregaDeh {
	
	private String procedimentCodi;
	private boolean obligat;
	
	public NotificacioEntregaDeh(
			String procedimentCodi,
			boolean obligat) {
		super();
		this.procedimentCodi = procedimentCodi;
		this.obligat = obligat;
	}

	public String getProcedimentCodi() {
		return procedimentCodi;
	}
	public void setProcedimentCodi(String procedimentCodi) {
		this.procedimentCodi = procedimentCodi;
	}
	
	public boolean isObligat() {
		return obligat;
	}
	public void setObligat(boolean obligat) {
		this.obligat = obligat;
	}
    
	
}
