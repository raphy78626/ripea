/**
 * 
 */
package es.caib.ripea.plugin.notib;

import java.util.Date;

/**
 * Informació sobre el justificant d'una notificació telemàtica.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class NotibNotificacioEstat {

	public enum ZonaperJustificantEstat {
		PENDENT,
	    ENTREGADA,
	    REBUTJADA;
	};

	private Date data;
	private ZonaperJustificantEstat estat;



	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public ZonaperJustificantEstat getEstat() {
		return estat;
	}
	public void setEstat(ZonaperJustificantEstat estat) {
		this.estat = estat;
	}

}
