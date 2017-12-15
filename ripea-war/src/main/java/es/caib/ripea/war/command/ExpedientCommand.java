/**
 * 
 */
package es.caib.ripea.war.command;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;

import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.war.helper.ConversioTipusHelper;

/**
 * Command per al manteniment d'expedients.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ExpedientCommand extends ContenidorCommand {

	@NotNull(groups = {Create.class, Update.class})
	protected Long metaNodeId;
	@NotNull(groups = {Create.class, Update.class})
	protected Long arxiuId;
	private String tancatMotiu;
	private int any;
	private long sequencia;


	public Long getMetaNodeId() {
		return metaNodeId;
	}
	public void setMetaNodeId(Long metaNodeId) {
		this.metaNodeId = metaNodeId;
	}
	public Long getArxiuId() {
		return arxiuId;
	}
	public void setArxiuId(Long arxiuId) {
		this.arxiuId = arxiuId;
	}
	public String getTancatMotiu() {
		return tancatMotiu;
	}
	public void setTancatMotiu(String tancatMotiu) {
		this.tancatMotiu = tancatMotiu;
	}
	public int getAny() {
		return any;
	}
	public void setAny(int any) {
		this.any = any;
	}
	public long getSequencia() {
		return sequencia;
	}
	public void setSequencia(long sequencia) {
		this.sequencia = sequencia;
	}

	public static ExpedientCommand asCommand(ExpedientDto dto) {
		ExpedientCommand command = ConversioTipusHelper.convertir(
				dto,
				ExpedientCommand.class);
		if (dto.getPare() != null)
			command.setPareId(dto.getPare().getId());
		if (dto.getMetaNode() != null)
			command.setMetaNodeId(dto.getMetaNode().getId());
		if (dto.getArxiu() != null)
			command.setArxiuId(dto.getArxiu().getId());
		return command;
	}
	public static ExpedientDto asDto(ExpedientCommand command) {
		return ConversioTipusHelper.convertir(
				command,
				ExpedientDto.class);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
