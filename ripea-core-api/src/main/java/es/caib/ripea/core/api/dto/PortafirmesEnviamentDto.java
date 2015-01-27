/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;
import java.util.Date;


/**
 * Dades d'un enviament a portafirmes.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class PortafirmesEnviamentDto implements Serializable {

	private String motiu;
	private PortafirmesPrioritatEnumDto prioritat;
	private Date dataEnviament;
	private Date dataCaducitat;
	private long portafirmesId;
	private PortafirmesEstatEnumDto portafirmesEstat;
	private Date callbackDarrer;
	private int callbackCount;
	private String errorDescripcio;



	public String getMotiu() {
		return motiu;
	}
	public void setMotiu(String motiu) {
		this.motiu = motiu;
	}
	public PortafirmesPrioritatEnumDto getPrioritat() {
		return prioritat;
	}
	public void setPrioritat(PortafirmesPrioritatEnumDto prioritat) {
		this.prioritat = prioritat;
	}
	public Date getDataEnviament() {
		return dataEnviament;
	}
	public void setDataEnviament(Date dataEnviament) {
		this.dataEnviament = dataEnviament;
	}
	public Date getDataCaducitat() {
		return dataCaducitat;
	}
	public void setDataCaducitat(Date dataCaducitat) {
		this.dataCaducitat = dataCaducitat;
	}
	public long getPortafirmesId() {
		return portafirmesId;
	}
	public void setPortafirmesId(long portafirmesId) {
		this.portafirmesId = portafirmesId;
	}
	public PortafirmesEstatEnumDto getPortafirmesEstat() {
		return portafirmesEstat;
	}
	public void setPortafirmesEstat(PortafirmesEstatEnumDto portafirmesEstat) {
		this.portafirmesEstat = portafirmesEstat;
	}
	public Date getCallbackDarrer() {
		return callbackDarrer;
	}
	public void setCallbackDarrer(Date callbackDarrer) {
		this.callbackDarrer = callbackDarrer;
	}
	public int getCallbackCount() {
		return callbackCount;
	}
	public void setCallbackCount(int callbackCount) {
		this.callbackCount = callbackCount;
	}
	public String getErrorDescripcio() {
		return errorDescripcio;
	}
	public void setErrorDescripcio(String errorDescripcio) {
		this.errorDescripcio = errorDescripcio;
	}

	private static final long serialVersionUID = -139254994389509932L;

}
