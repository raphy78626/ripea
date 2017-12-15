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

	private String classificacioDocumental;
	private String classificacioSia;
	private String unitatAdministrativa;
	private boolean notificacioActiva;
	private String notificacioOrganCodi;
	private String notificacioLlibreCodi;
	private String notificacioAvisTitol;
	private String notificacioAvisText;
	private String notificacioAvisTextSms;
	private String notificacioOficiTitol;
	private String notificacioOficiText;
	private Long pareId;
	private List<MetaExpedientMetaDocumentDto> metaDocuments;
	private int arxiusCount;



	public String getClassificacioDocumental() {
		return classificacioDocumental;
	}
	public void setClassificacioDocumental(String classificacioDocumental) {
		this.classificacioDocumental = classificacioDocumental;
	}
	public String getClassificacioSia() {
		return classificacioSia;
	}
	public void setClassificacioSia(String classificacioSia) {
		this.classificacioSia = classificacioSia;
	}
	public String getUnitatAdministrativa() {
		return unitatAdministrativa;
	}
	public void setUnitatAdministrativa(String unitatAdministrativa) {
		this.unitatAdministrativa = unitatAdministrativa;
	}
	public boolean isNotificacioActiva() {
		return notificacioActiva;
	}
	public void setNotificacioActiva(boolean notificacioActiva) {
		this.notificacioActiva = notificacioActiva;
	}
	public String getNotificacioOrganCodi() {
		return notificacioOrganCodi;
	}
	public void setNotificacioOrganCodi(String notificacioOrganCodi) {
		this.notificacioOrganCodi = notificacioOrganCodi;
	}
	public String getNotificacioLlibreCodi() {
		return notificacioLlibreCodi;
	}
	public void setNotificacioLlibreCodi(String notificacioLlibreCodi) {
		this.notificacioLlibreCodi = notificacioLlibreCodi;
	}
	public String getNotificacioAvisTitol() {
		return notificacioAvisTitol;
	}
	public void setNotificacioAvisTitol(String notificacioAvisTitol) {
		this.notificacioAvisTitol = notificacioAvisTitol;
	}
	public String getNotificacioAvisText() {
		return notificacioAvisText;
	}
	public void setNotificacioAvisText(String notificacioAvisText) {
		this.notificacioAvisText = notificacioAvisText;
	}
	public String getNotificacioAvisTextSms() {
		return notificacioAvisTextSms;
	}
	public void setNotificacioAvisTextSms(String notificacioAvisTextSms) {
		this.notificacioAvisTextSms = notificacioAvisTextSms;
	}
	public String getNotificacioOficiTitol() {
		return notificacioOficiTitol;
	}
	public void setNotificacioOficiTitol(String notificacioOficiTitol) {
		this.notificacioOficiTitol = notificacioOficiTitol;
	}
	public String getNotificacioOficiText() {
		return notificacioOficiText;
	}
	public void setNotificacioOficiText(String notificacioOficiText) {
		this.notificacioOficiText = notificacioOficiText;
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
