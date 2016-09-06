/**
 * 
 */
package es.caib.ripea.war.command;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import es.caib.ripea.core.api.dto.DocumentDto;
import es.caib.ripea.core.api.dto.DocumentTipusEnumDto;
import es.caib.ripea.war.helper.ConversioTipusHelper;
import es.caib.ripea.war.validation.ArxiuNoBuit;

/**
 * Command per al manteniment de documents.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class DocumentCommand extends ContenidorCommand {

	@NotNull(groups = {CreateDigital.class, CreateFisic.class, UpdateDigital.class, UpdateFisic.class})
	protected DocumentTipusEnumDto documentTipus = DocumentTipusEnumDto.DIGITAL;
	@NotEmpty(groups = {CreateFisic.class, UpdateFisic.class})
	@Size(groups = {CreateDigital.class, CreateFisic.class, UpdateDigital.class, UpdateFisic.class}, max=255)
	protected String ubicacio;
	protected Long metaNodeId;
	@NotNull(groups = {CreateDigital.class, CreateFisic.class, UpdateDigital.class, UpdateFisic.class})
	protected Date data;
	@ArxiuNoBuit(groups = {CreateDigital.class})
	protected MultipartFile arxiu;



	public DocumentTipusEnumDto getDocumentTipus() {
		return documentTipus;
	}
	public void setDocumentTipus(DocumentTipusEnumDto documentTipus) {
		this.documentTipus = documentTipus;
	}
	public String getUbicacio() {
		return ubicacio;
	}
	public void setUbicacio(String ubicacio) {
		this.ubicacio = ubicacio;
	}
	public Long getMetaNodeId() {
		return metaNodeId;
	}
	public void setMetaNodeId(Long metaNodeId) {
		this.metaNodeId = metaNodeId;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public MultipartFile getArxiu() {
		return arxiu;
	}
	public void setArxiu(MultipartFile arxiu) {
		this.arxiu = arxiu;
	}

	public static DocumentCommand asCommand(DocumentDto dto) {
		DocumentCommand command = ConversioTipusHelper.convertir(
				dto,
				DocumentCommand.class);
		if (dto.getPare() != null)
			command.setPareId(dto.getPare().getId());
		if (dto.getMetaNode() != null)
			command.setMetaNodeId(dto.getMetaNode().getId());
		return command;
	}
	public static DocumentDto asDto(DocumentCommand command) {
		return ConversioTipusHelper.convertir(
				command,
				DocumentDto.class);
	}

	public interface CreateDigital {}
	public interface UpdateDigital {}
	public interface CreateFisic {}
	public interface UpdateFisic {}

}
