/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Informació d'un MetaDocument.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class MetaDocumentDto extends MetaNodeAmbMetaDadesDto implements Serializable {

	private boolean globalExpedient;
	private MultiplicitatEnumDto globalMultiplicitat;
	private boolean globalReadOnly;
	private boolean firmaAppletActiva;
	private String signaturaTipusMime;
	private boolean firmaPortafirmesActiva;
	private String portafirmesDocumentTipus;
	private String portafirmesFluxId;
	private String custodiaPolitica;
	private String plantillaNom;
	private String plantillaContentType;

	public boolean isGlobalExpedient() {
		return globalExpedient;
	}
	public void setGlobalExpedient(boolean globalExpedient) {
		this.globalExpedient = globalExpedient;
	}
	public MultiplicitatEnumDto getGlobalMultiplicitat() {
		return globalMultiplicitat;
	}
	public void setGlobalMultiplicitat(MultiplicitatEnumDto globalMultiplicitat) {
		this.globalMultiplicitat = globalMultiplicitat;
	}
	public boolean isGlobalReadOnly() {
		return globalReadOnly;
	}
	public void setGlobalReadOnly(boolean globalReadOnly) {
		this.globalReadOnly = globalReadOnly;
	}
	public boolean isFirmaAppletActiva() {
		return firmaAppletActiva;
	}
	public void setFirmaAppletActiva(boolean firmaAppletActiva) {
		this.firmaAppletActiva = firmaAppletActiva;
	}
	public String getSignaturaTipusMime() {
		return signaturaTipusMime;
	}
	public void setSignaturaTipusMime(String signaturaTipusMime) {
		this.signaturaTipusMime = signaturaTipusMime;
	}
	public boolean isFirmaPortafirmesActiva() {
		return firmaPortafirmesActiva;
	}
	public void setFirmaPortafirmesActiva(boolean firmaPortafirmesActiva) {
		this.firmaPortafirmesActiva = firmaPortafirmesActiva;
	}
	public String getPortafirmesDocumentTipus() {
		return portafirmesDocumentTipus;
	}
	public void setPortafirmesDocumentTipus(String portafirmesDocumentTipus) {
		this.portafirmesDocumentTipus = portafirmesDocumentTipus;
	}
	public String getPortafirmesFluxId() {
		return portafirmesFluxId;
	}
	public void setPortafirmesFluxId(String portafirmesFluxId) {
		this.portafirmesFluxId = portafirmesFluxId;
	}
	public String getCustodiaPolitica() {
		return custodiaPolitica;
	}
	public void setCustodiaPolitica(String custodiaPolitica) {
		this.custodiaPolitica = custodiaPolitica;
	}
	public String getPlantillaNom() {
		return plantillaNom;
	}
	public void setPlantillaNom(String plantillaNom) {
		this.plantillaNom = plantillaNom;
	}
	public String getPlantillaContentType() {
		return plantillaContentType;
	}
	public void setPlantillaContentType(String plantillaContentType) {
		this.plantillaContentType = plantillaContentType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaDocumentDto other = (MetaDocumentDto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	private static final long serialVersionUID = -139254994389509932L;

}
