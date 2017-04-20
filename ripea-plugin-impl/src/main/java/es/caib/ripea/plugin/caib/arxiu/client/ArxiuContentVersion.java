/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu.client;

import java.util.Date;

/**
 * Estructura que representa una versió de document de l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuContentVersion {

	private String id;
	private Date data;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}

}
