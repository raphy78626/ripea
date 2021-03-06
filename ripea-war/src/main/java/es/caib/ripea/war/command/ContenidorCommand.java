/**
 * 
 */
package es.caib.ripea.war.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Command per al manteniment de contenidors.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ContenidorCommand {

	protected Long id;
	@NotNull(groups = {Create.class, Update.class})
	protected Long entitatId;
	@NotNull(groups = {Create.class, Update.class})
	protected Long pareId;
	@NotEmpty(groups = {Create.class, Update.class}) @Size(max=256)
	protected String nom;



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEntitatId() {
		return entitatId;
	}
	public void setEntitatId(Long entitatId) {
		this.entitatId = entitatId;
	}
	public Long getPareId() {
		return pareId;
	}
	public void setPareId(Long pareId) {
		this.pareId = pareId;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public interface Create {}
	public interface Update {}

}
