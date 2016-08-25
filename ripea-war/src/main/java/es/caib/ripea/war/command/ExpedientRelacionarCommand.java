/**
 * 
 */
package es.caib.ripea.war.command;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;

import es.caib.ripea.war.command.ExpedientRelacionarCommand.Relacionar;
import es.caib.ripea.war.validation.ExpedientRelacionar;

/**
 * Command per a relacionar expedients.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@ExpedientRelacionar(groups = {Relacionar.class})
public class ExpedientRelacionarCommand {

	@NotNull
	protected Long entitatId;
	/** Expedient sobre el que es realitza la acció de relacionar amb un altre expedient. */
	@NotNull
	protected Long expedientARelacionarId;
	/** Expedient seleccionar per a relacionar amb l'expedient sobre el que es realitza la acció. */
	@NotNull
	protected Long expedientRelacionatId;


	public Long getEntitatId() {
		return entitatId;
	}
	public void setEntitatId(Long entitatId) {
		this.entitatId = entitatId;
	}
	public Long getExpedientARelacionarId() {
		return expedientARelacionarId;
	}
	public void setExpedientARelacionarId(Long expedientId) {
		this.expedientARelacionarId = expedientId;
	}
	public Long getExpedientRelacionatId() {
		return expedientRelacionatId;
	}
	public void setExpedientRelacionatId(Long expedientRelacionatId) {
		this.expedientRelacionatId = expedientRelacionatId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public interface Relacionar {}
}
