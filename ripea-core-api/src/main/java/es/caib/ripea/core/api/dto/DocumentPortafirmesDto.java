/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.Arrays;
import java.util.Date;

/**
 * Informació de l'enviament d'un document al portafirmes.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class DocumentPortafirmesDto extends DocumentEnviamentDto {

	private PortafirmesPrioritatEnumDto prioritat;
	private Date dataCaducitat;
	private String documentTipus;
	private String[] responsables;
	private MetaDocumentFirmaFluxTipusEnumDto fluxTipus;
	private String fluxId;
	private String portafirmesId;
	private Date enviamentData;
	private Integer enviamentCount;
	private boolean enviamentError;
	private String enviamentErrorDescripcio;
	private Date processamentData;
	private Integer processamentCount;
	private boolean processamentError;
	private String processamentErrorDescripcio;



	public PortafirmesPrioritatEnumDto getPrioritat() {
		return prioritat;
	}
	public void setPrioritat(PortafirmesPrioritatEnumDto prioritat) {
		this.prioritat = prioritat;
	}
	public Date getDataCaducitat() {
		return dataCaducitat;
	}
	public void setDataCaducitat(Date dataCaducitat) {
		this.dataCaducitat = dataCaducitat;
	}
	public String getDocumentTipus() {
		return documentTipus;
	}
	public void setDocumentTipus(String documentTipus) {
		this.documentTipus = documentTipus;
	}
	public String[] getResponsables() {
		return responsables;
	}
	public void setResponsables(String[] responsables) {
		this.responsables = responsables;
	}
	public MetaDocumentFirmaFluxTipusEnumDto getFluxTipus() {
		return fluxTipus;
	}
	public void setFluxTipus(MetaDocumentFirmaFluxTipusEnumDto fluxTipus) {
		this.fluxTipus = fluxTipus;
	}
	public String getFluxId() {
		return fluxId;
	}
	public void setFluxId(String fluxId) {
		this.fluxId = fluxId;
	}
	public String getPortafirmesId() {
		return portafirmesId;
	}
	public void setPortafirmesId(String portafirmesId) {
		this.portafirmesId = portafirmesId;
	}
	public Date getEnviamentData() {
		return enviamentData;
	}
	public void setEnviamentData(Date enviamentData) {
		this.enviamentData = enviamentData;
	}
	public Integer getEnviamentCount() {
		return enviamentCount;
	}
	public void setEnviamentCount(Integer enviamentCount) {
		this.enviamentCount = enviamentCount;
	}
	public boolean isEnviamentError() {
		return enviamentError;
	}
	public void setEnviamentError(boolean enviamentError) {
		this.enviamentError = enviamentError;
	}
	public String getEnviamentErrorDescripcio() {
		return enviamentErrorDescripcio;
	}
	public void setEnviamentErrorDescripcio(String enviamentErrorDescripcio) {
		this.enviamentErrorDescripcio = enviamentErrorDescripcio;
	}
	public Date getProcessamentData() {
		return processamentData;
	}
	public void setProcessamentData(Date processamentData) {
		this.processamentData = processamentData;
	}
	public Integer getProcessamentCount() {
		return processamentCount;
	}
	public void setProcessamentCount(Integer processamentCount) {
		this.processamentCount = processamentCount;
	}
	public boolean isProcessamentError() {
		return processamentError;
	}
	public void setProcessamentError(boolean processamentError) {
		this.processamentError = processamentError;
	}
	public String getProcessamentErrorDescripcio() {
		return processamentErrorDescripcio;
	}
	public void setProcessamentErrorDescripcio(String processamentErrorDescripcio) {
		this.processamentErrorDescripcio = processamentErrorDescripcio;
	}

	public String getDestinatari() {
		return Arrays.toString(responsables);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
