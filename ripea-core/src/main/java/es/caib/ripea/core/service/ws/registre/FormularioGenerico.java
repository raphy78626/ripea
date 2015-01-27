/**
 * 
 */
package es.caib.ripea.core.service.ws.registre;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 * Dades de formulari genèric segons l'estàndard SICRES_3.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FormularioGenerico {

	@XmlElement(required = true)
	private String expone;
	@XmlElement(required = true)
	private String solicita;

	public String getExpone() {
		return expone;
	}
	public void setExpone(String expone) {
		this.expone = expone;
	}
	public String getSolicita() {
		return solicita;
	}
	public void setSolicita(String solicita) {
		this.solicita = solicita;
	}

}
