/**
 * 
 */
package es.caib.ripea.war.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import es.caib.ripea.core.api.dto.DocumentPublicacioDto;
import es.caib.ripea.core.api.dto.DocumentPublicacioTipusEnumDto;
import es.caib.ripea.war.helper.ConversioTipusHelper;

/**
 * Command per a gestionar les publicacions de documents.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class DocumentPublicacioCommand {

	private Long id;
	@NotNull(groups = {Create.class})
	private Long documentId;
	@NotNull
	private DocumentPublicacioTipusEnumDto tipus;
	@NotEmpty @Size(max=64)
	private String assumpte;
	@Size(max=256)
	private String observacions;



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDocumentId() {
		return documentId;
	}
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}
	public DocumentPublicacioTipusEnumDto getTipus() {
		return tipus;
	}
	public void setTipus(DocumentPublicacioTipusEnumDto tipus) {
		this.tipus = tipus;
	}
	public String getAssumpte() {
		return assumpte;
	}
	public void setAssumpte(String assumpte) {
		this.assumpte = assumpte;
	}
	public String getObservacions() {
		return observacions;
	}
	public void setObservacions(String observacions) {
		this.observacions = observacions;
	}

	public static DocumentPublicacioCommand asCommand(DocumentPublicacioDto dto) {
		return ConversioTipusHelper.convertir(
				dto,
				DocumentPublicacioCommand.class);
	}
	public static DocumentPublicacioDto asDto(DocumentPublicacioCommand command) {
		return ConversioTipusHelper.convertir(
				command,
				DocumentPublicacioDto.class);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public interface Create {}

}
