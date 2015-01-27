/**
 * 
 */
package es.caib.ripea.war.command;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;

import es.caib.ripea.core.api.dto.MetaNodeMetaDadaDto;
import es.caib.ripea.core.api.dto.MultiplicitatEnumDto;
import es.caib.ripea.war.helper.ConversioTipusHelper;

/**
 * Command per al manteniment de les meta-dades dels
 * meta-nodes.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class MetaNodeMetaDadaCommand {

	private Long id;
	@NotNull
	private Long metaDadaId;
	@NotNull
	private MultiplicitatEnumDto multiplicitat;
	boolean readOnly;



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMetaDadaId() {
		return metaDadaId;
	}
	public void setMetaDadaId(Long metaDadaId) {
		this.metaDadaId = metaDadaId;
	}
	public MultiplicitatEnumDto getMultiplicitat() {
		return multiplicitat;
	}
	public void setMultiplicitat(MultiplicitatEnumDto multiplicitat) {
		this.multiplicitat = multiplicitat;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public static MetaNodeMetaDadaCommand asCommand(MetaNodeMetaDadaDto dto) {
		MetaNodeMetaDadaCommand command = ConversioTipusHelper.convertir(
				dto,
				MetaNodeMetaDadaCommand.class);
		command.setId(dto.getId());
		command.setMetaDadaId(dto.getMetaDada().getId());
		return command;
	}
	public static MetaNodeMetaDadaDto asDto(MetaNodeMetaDadaCommand command) {
		return ConversioTipusHelper.convertir(
				command,
				MetaNodeMetaDadaDto.class);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
