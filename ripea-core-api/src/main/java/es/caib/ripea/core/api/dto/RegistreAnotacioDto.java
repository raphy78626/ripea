/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.Date;

import es.caib.ripea.core.api.registre.RegistreAnotacio;
import es.caib.ripea.core.api.registre.RegistreTipusEnum;

/**
 * Classe que representa una anotació de registre amb id.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class RegistreAnotacioDto extends RegistreAnotacio {

	private Long id;
	private RegistreTipusEnum tipus;
	private String unitatAdministrativa;
	private UsuariDto createdBy;
	private Date createdDate;
	private UsuariDto lastModifiedBy;
	private Date lastModifiedDate;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public RegistreTipusEnum getTipus() {
		return tipus;
	}
	public void setTipus(RegistreTipusEnum tipus) {
		this.tipus = tipus;
	}
	public String getUnitatAdministrativa() {
		return unitatAdministrativa;
	}
	public void setUnitatAdministrativa(String unitatAdministrativa) {
		this.unitatAdministrativa = unitatAdministrativa;
	}
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

}
