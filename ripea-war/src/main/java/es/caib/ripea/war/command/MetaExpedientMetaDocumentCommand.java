/**
 * 
 */
package es.caib.ripea.war.command;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;

import es.caib.ripea.core.api.dto.MetaExpedientMetaDocumentDto;
import es.caib.ripea.core.api.dto.MultiplicitatEnumDto;
import es.caib.ripea.war.helper.ConversioTipusHelper;

/**
 * Command per al manteniment de les meta-documents dels
 * meta-expedients.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class MetaExpedientMetaDocumentCommand {

	private Long id;
	@NotNull
	private Long metaDocumentId;
	@NotNull
	private MultiplicitatEnumDto multiplicitat;
	private boolean readOnly;



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMetaDocumentId() {
		return metaDocumentId;
	}
	public void setMetaDocumentId(Long metaDocumentId) {
		this.metaDocumentId = metaDocumentId;
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

	public static MetaExpedientMetaDocumentCommand asCommand(MetaExpedientMetaDocumentDto dto) {
		MetaExpedientMetaDocumentCommand command = ConversioTipusHelper.convertir(
				dto,
				MetaExpedientMetaDocumentCommand.class);
		command.setId(dto.getId());
		command.setMetaDocumentId(dto.getMetaDocument().getId());
		return command;
	}
	public static MetaExpedientMetaDocumentDto asDto(MetaExpedientMetaDocumentCommand command) {
		return ConversioTipusHelper.convertir(
				command,
				MetaExpedientMetaDocumentDto.class);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
