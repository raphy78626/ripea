/**
 * 
 */
package es.caib.ripea.plugin.notib;

import java.util.Date;

/**
 * Dades resultants de fer una notificació telemàtica.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class NotibNotificacioResultat {

	private Date referenciaData;
	private String referencia;
	
	
	public Date getReferenciaData() {
		return referenciaData;
	}
	public void setReferenciaData(Date referenciaData) {
		this.referenciaData = referenciaData;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

}
