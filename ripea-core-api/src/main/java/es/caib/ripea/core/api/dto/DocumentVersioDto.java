/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.ArrayList;
import java.util.List;


/**
 * Informació d'una versió de document.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class DocumentVersioDto extends AuditoriaDto {

	private Long id;
	private int versio;
	private String arxiuNom;
	private String arxiuContentType;
	private long arxiuContentLength;
	private boolean custodiat;
	private String custodiaId;
	protected String custodiaUrl;

	private String portafirmesConversioArxiuNom;
	private List<PortafirmesEnviamentDto> portafirmesEnviaments;



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getVersio() {
		return versio;
	}
	public void setVersio(int versio) {
		this.versio = versio;
	}
	public String getArxiuNom() {
		return arxiuNom;
	}
	public void setArxiuNom(String arxiuNom) {
		this.arxiuNom = arxiuNom;
	}
	public String getArxiuContentType() {
		return arxiuContentType;
	}
	public void setArxiuContentType(String arxiuContentType) {
		this.arxiuContentType = arxiuContentType;
	}
	public long getArxiuContentLength() {
		return arxiuContentLength;
	}
	public void setArxiuContentLength(long arxiuContentLength) {
		this.arxiuContentLength = arxiuContentLength;
	}
	public boolean isCustodiat() {
		return custodiat;
	}
	public void setCustodiat(boolean custodiat) {
		this.custodiat = custodiat;
	}
	public String getCustodiaId() {
		return custodiaId;
	}
	public void setCustodiaId(String custodiaId) {
		this.custodiaId = custodiaId;
	}
	public String getCustodiaUrl() {
		return custodiaUrl;
	}
	public void setCustodiaUrl(String custodiaUrl) {
		this.custodiaUrl = custodiaUrl;
	}
	public String getPortafirmesConversioArxiuNom() {
		return portafirmesConversioArxiuNom;
	}
	public void setPortafirmesConversioArxiuNom(String portafirmesConversioArxiuNom) {
		this.portafirmesConversioArxiuNom = portafirmesConversioArxiuNom;
	}
	public List<PortafirmesEnviamentDto> getPortafirmesEnviaments() {
		return portafirmesEnviaments;
	}
	public void setPortafirmesEnviaments(
			List<PortafirmesEnviamentDto> portafirmesEnviaments) {
		this.portafirmesEnviaments = portafirmesEnviaments;
	}

	public void addPortafirmesEnviament(
			PortafirmesEnviamentDto portafirmesEnviament) {
		if (portafirmesEnviaments == null) {
			portafirmesEnviaments = new ArrayList<PortafirmesEnviamentDto>();
		}
		portafirmesEnviaments.add(portafirmesEnviament);
	}

	public PortafirmesEnviamentDto getPortafirmesEnviamentDarrer() {
		PortafirmesEnviamentDto darrer = null;
		if (portafirmesEnviaments != null && !portafirmesEnviaments.isEmpty()) {
			for (PortafirmesEnviamentDto portafirmesEnviament: portafirmesEnviaments) {
				if (darrer == null || portafirmesEnviament.getDataEnviament().after(darrer.getDataEnviament())) {
					darrer = portafirmesEnviament;
				}
			}
		}
		return darrer;
	}

	public PortafirmesEstatEnumDto getFirmaEstat() {
		PortafirmesEnviamentDto portafirmesEnviamentDarrer = getPortafirmesEnviamentDarrer();
		if (portafirmesEnviamentDarrer != null) {
			return portafirmesEnviamentDarrer.getPortafirmesEstat();
		}
		return null;
	}

	public boolean isFirmaEstatPendent() {
		return PortafirmesEstatEnumDto.PENDENT.equals(getFirmaEstat());
	}
	public boolean isFirmaEstatBloquejarEnviaments() {
		return	PortafirmesEstatEnumDto.PENDENT.equals(getFirmaEstat()) ||
				PortafirmesEstatEnumDto.FIRMAT.equals(getFirmaEstat());
	}
	public boolean isFirmaError() {
		PortafirmesEnviamentDto portafirmesEnviamentDarrer = getPortafirmesEnviamentDarrer();
		if (portafirmesEnviamentDarrer != null) {
			String error = portafirmesEnviamentDarrer.getErrorDescripcio();
			return error != null && !error.isEmpty();
		}
		return false;
	}

	public boolean isFirmaIntentat() {
		PortafirmesEnviamentDto portafirmesEnviamentDarrer = getPortafirmesEnviamentDarrer();
		return portafirmesEnviamentDarrer != null;
	}

	private static final long serialVersionUID = -139254994389509932L;

}
