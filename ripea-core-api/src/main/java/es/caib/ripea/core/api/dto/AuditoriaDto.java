/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Informació d'auditoria.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class AuditoriaDto implements Serializable {

	private UsuariDto createdBy;
	private Date createdDate;
	private UsuariDto lastModifiedBy;
	private Date lastModifiedDate;

	public UsuariDto getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(UsuariDto createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public UsuariDto getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(UsuariDto lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	private static final long serialVersionUID = -139254994389509932L;

}
