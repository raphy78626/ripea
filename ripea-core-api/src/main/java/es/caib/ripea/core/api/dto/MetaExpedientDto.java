/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Informació d'un MetaExpedient.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class MetaExpedientDto extends MetaNodeAmbMetaDadesDto implements Serializable {

	private String classificacio;
	private Long pareId;
	private List<MetaExpedientMetaDocumentDto> metaDocuments;
	private int arxiusCount;


	public String getClassificacio() {
		return classificacio;
	}
	public void setClassificacio(String classificacio) {
		this.classificacio = classificacio;
	}
	public Long getPareId() {
		return pareId;
	}
	public void setPareId(Long pareId) {
		this.pareId = pareId;
	}
	public List<MetaExpedientMetaDocumentDto> getMetaDocuments() {
		return metaDocuments;
	}
	public void setMetaDocuments(List<MetaExpedientMetaDocumentDto> metaDocuments) {
		this.metaDocuments = metaDocuments;
	}

	public int getMetaDocumentsCount() {
		if  (metaDocuments == null)
			return 0;
		else
			return metaDocuments.size();
	}
	public void setArxiusCount(int arxiusCount) {
		this.arxiusCount = arxiusCount;
	}
	public int getArxiusCount() {
		return this.arxiusCount;
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
		MetaExpedientDto other = (MetaExpedientDto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	private static final long serialVersionUID = -139254994389509932L;

}
