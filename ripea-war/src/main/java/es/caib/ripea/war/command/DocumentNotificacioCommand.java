/**
 * 
 */
package es.caib.ripea.war.command;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import es.caib.ripea.core.api.dto.DocumentDto;
import es.caib.ripea.core.api.dto.DocumentEnviamentEstatEnumDto;
import es.caib.ripea.core.api.dto.DocumentNotificacioDto;
import es.caib.ripea.core.api.dto.DocumentNotificacioTipusEnumDto;
import es.caib.ripea.war.helper.ConversioTipusHelper;

/**
 * Command per a gestionar les notificacions de documents.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class DocumentNotificacioCommand {

	private Long id;
	@NotNull(groups = {Create.class})
	private Long documentId;
	@NotNull(groups = {Create.class, Update.class})
	private Long interessatId;
	@NotNull
	private DocumentNotificacioTipusEnumDto tipus;
	private DocumentEnviamentEstatEnumDto estat;
	@NotEmpty(groups = {Create.class, Update.class})
	@Size(max = 64, groups = {Create.class, Update.class})
	private String assumpte;
	@Size(groups = {Create.class, Update.class}, max = 256)
	private String observacions;
	@NotEmpty(groups = {Electronica.class}) @Size(max = 256)
	private String avisTitol;
	@NotEmpty(groups = {Electronica.class}) @Size(max = 1024)
	private String avisText;
	@Size(max = 200)
	private String avisTextSms;
	@NotEmpty(groups = {Electronica.class}) @Size(max = 256)
	private String oficiTitol;
	@NotEmpty(groups = {Electronica.class}) @Size(max = 1024)
	private String oficiText;
	private List<Long> annexos;



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
	public Long getInteressatId() {
		return interessatId;
	}
	public void setInteressatId(Long interessatId) {
		this.interessatId = interessatId;
	}
	public DocumentNotificacioTipusEnumDto getTipus() {
		return tipus;
	}
	public void setTipus(DocumentNotificacioTipusEnumDto tipus) {
		this.tipus = tipus;
	}
	public DocumentEnviamentEstatEnumDto getEstat() {
		return estat;
	}
	public void setEstat(DocumentEnviamentEstatEnumDto estat) {
		this.estat = estat;
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
	public String getAvisTitol() {
		return avisTitol;
	}
	public void setAvisTitol(String avisTitol) {
		this.avisTitol = avisTitol;
	}
	public String getAvisText() {
		return avisText;
	}
	public void setAvisText(String avisText) {
		this.avisText = avisText;
	}
	public String getAvisTextSms() {
		return avisTextSms;
	}
	public void setAvisTextSms(String avisTextSms) {
		this.avisTextSms = avisTextSms;
	}
	public String getOficiTitol() {
		return oficiTitol;
	}
	public void setOficiTitol(String oficiTitol) {
		this.oficiTitol = oficiTitol;
	}
	public String getOficiText() {
		return oficiText;
	}
	public void setOficiText(String oficiText) {
		this.oficiText = oficiText;
	}
	public List<Long> getAnnexos() {
		return annexos;
	}
	public void setAnnexos(List<Long> annexos) {
		this.annexos = annexos;
	}

	public static DocumentNotificacioCommand asCommand(DocumentNotificacioDto dto) {
		return ConversioTipusHelper.convertir(
				dto,
				DocumentNotificacioCommand.class);
	}
	public static DocumentNotificacioDto asDto(DocumentNotificacioCommand command) {
		DocumentNotificacioDto dto = ConversioTipusHelper.convertir(
				command,
				DocumentNotificacioDto.class);
		if (command.getAnnexos() != null) {
			List<DocumentDto> annexos = new ArrayList<DocumentDto>();
			for (Long annexId: command.getAnnexos()) {
				DocumentDto annex = new DocumentDto();
				annex.setId(annexId);
				annexos.add(annex);
			}
			dto.setAnnexos(annexos);
		}
		return dto;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public interface Electronica {}
	public interface Create {}
	public interface Update {}

}
