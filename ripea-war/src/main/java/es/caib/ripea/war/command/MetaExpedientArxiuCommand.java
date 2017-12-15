/**
 * 
 */
package es.caib.ripea.war.command;

import javax.validation.constraints.NotNull;

import es.caib.ripea.core.api.dto.ArxiuDto;
/**
 * Command per a la relació metaExpedient-arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class MetaExpedientArxiuCommand {

	@NotNull(groups = {Create.class, Update.class})
	private Long arxiuId;

	public Long getArxiuId() {
		return arxiuId;
	}
	public void setArxiuId(Long arxiuId) {
		this.arxiuId = arxiuId;
	}
	
	public static MetaExpedientArxiuCommand asCommand(ArxiuDto dto) {
		MetaExpedientArxiuCommand command = new MetaExpedientArxiuCommand();
		command.setArxiuId(dto.getId());
		return command;
	}
	public static ArxiuDto asDto(MetaExpedientArxiuCommand command) {
		ArxiuDto dto = new ArxiuDto();
		dto.setId(command.getArxiuId());
		return dto;
	}

	public interface Create {}
	public interface Update {}
}
