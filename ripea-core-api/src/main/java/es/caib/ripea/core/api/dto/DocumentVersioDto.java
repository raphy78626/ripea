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

	public DocumentFirmaEstatEnumDto getFirmaEstat() {
		PortafirmesEnviamentDto portafirmesEnviamentDarrer = getPortafirmesEnviamentDarrer();
		if (portafirmesEnviamentDarrer != null) {
			switch (portafirmesEnviamentDarrer.getPortafirmesEstat()) {
			case PENDENT:
				return DocumentFirmaEstatEnumDto.PFIRMA_PENDENT;
			case REBUTJAT:
				return DocumentFirmaEstatEnumDto.PFIRMA_REBUTJAT;
			case FIRMAT:
				if (getCustodiaId() != null)
					return DocumentFirmaEstatEnumDto.PFIRMA_CUSTODIAT;
				else
					return DocumentFirmaEstatEnumDto.PFIRMA_FIRMAT;
			case CANCELAT:
				return DocumentFirmaEstatEnumDto.PFIRMA_CANCELAT;
			case ERROR_CUSTODIA:
				return DocumentFirmaEstatEnumDto.CUSTODIA_ERROR;
			}
		}
		return DocumentFirmaEstatEnumDto.SENSE_FIRMA;
	}

	public boolean isFirmaEstatCustodiat() {
		DocumentFirmaEstatEnumDto firmaEstat = getFirmaEstat();
		return	firmaEstat.equals(DocumentFirmaEstatEnumDto.PFIRMA_CUSTODIAT) ||
				firmaEstat.equals(DocumentFirmaEstatEnumDto.APPLET_CUSTODIAT);
	}
	public boolean isFirmaEstatPortafirmesPendent() {
		DocumentFirmaEstatEnumDto firmaEstat = getFirmaEstat();
		return firmaEstat.equals(DocumentFirmaEstatEnumDto.PFIRMA_PENDENT);
	}
	public boolean isFirmaEstatPortafirmesBloquejat() {
		DocumentFirmaEstatEnumDto firmaEstat = getFirmaEstat();
		return	firmaEstat.equals(DocumentFirmaEstatEnumDto.PFIRMA_PENDENT) ||
				firmaEstat.equals(DocumentFirmaEstatEnumDto.PFIRMA_FIRMAT) ||
				firmaEstat.equals(DocumentFirmaEstatEnumDto.PFIRMA_CUSTODIAT) ||
				firmaEstat.equals(DocumentFirmaEstatEnumDto.CUSTODIA_ERROR);
	}
	public boolean isFirmaEstatCustodiaError() {
		DocumentFirmaEstatEnumDto firmaEstat = getFirmaEstat();
		return firmaEstat.equals(DocumentFirmaEstatEnumDto.CUSTODIA_ERROR);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
