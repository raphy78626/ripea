/**
 * 
 */
package es.caib.ripea.plugin.notib;

/**
 * Dades resultants de crear un expedient a la zona personal.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class NotibExpedientInformacio {

	private String identificador;
	private String clau;

	public NotibExpedientInformacio(String identificador, String clau) {
		super();
		this.identificador = identificador;
		this.clau = clau;
	}

	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public String getClau() {
		return clau;
	}
	public void setClau(String clau) {
		this.clau = clau;
	}

}
