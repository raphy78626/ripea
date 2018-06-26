/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Informació del moviment d'un contenidor.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ApuntAccioModel implements Serializable {

	private Date data;
	private String usuari;
	private String accio;
	private String param1;
	private String param2;

	DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	
	public String getData() {
		return df.format(data);
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getUsuari() {
		return usuari;
	}

	public void setUsuari(String usuari) {
		this.usuari = usuari;
	}

	public String getAccio() {
		return accio;
	}

	public void setAccio(String accio) {
		this.accio = accio;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
