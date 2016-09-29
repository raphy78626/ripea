/**
 * 
 */
package es.caib.ripea.war.command;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import es.caib.ripea.core.api.dto.MetaExpedientDto;
import es.caib.ripea.war.helper.ConversioTipusHelper;
import es.caib.ripea.war.validation.CodiMetaExpedientNoRepetit;

/**
 * Command per al manteniment de meta-expedients.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@CodiMetaExpedientNoRepetit(campId = "id", campCodi = "codi", campEntitatId = "entitatId")
public class MetaExpedientCommand {

	private Long id;

	@NotEmpty @Size(max = 64)
	private String codi;
	@NotEmpty @Size(max = 256)
	private String nom;
	@Size(max=1024)
	private String descripcio;
	@NotEmpty @Size(max = 30)
	private String classificacioDocumental;
	@NotEmpty @Size(max = 6)
	private String classificacioSia;
	@Size(max = 9)
	private String unitatAdministrativa;
	private boolean notificacioActiva;
	@Size(max = 9)
	private String notificacioOrganCodi;
	@Size(max = 4)
	private String notificacioLlibreCodi;
	@Size(max = 256)
	private String notificacioAvisTitol;
	@Size(max = 1024)
	private String notificacioAvisText;
	@Size(max = 200)
	private String notificacioAvisTextSms;
	@Size(max = 256)
	private String notificacioOficiTitol;
	@Size(max = 1024)
	private String notificacioOficiText;
	private Long pareId;
	private Long entitatId;



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodi() {
		return codi;
	}
	public void setCodi(String codi) {
		this.codi = codi;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getDescripcio() {
		return descripcio;
	}
	public void setDescripcio(String descripcio) {
		this.descripcio = descripcio;
	}
	public String getClassificacioDocumental() {
		return classificacioDocumental;
	}
	public void setClassificacioDocumental(String classificacioDocumental) {
		this.classificacioDocumental = classificacioDocumental;
	}
	public String getClassificacioSia() {
		return classificacioSia;
	}
	public void setClassificacioSia(String classificacioSia) {
		this.classificacioSia = classificacioSia;
	}
	public String getUnitatAdministrativa() {
		return unitatAdministrativa;
	}
	public void setUnitatAdministrativa(String unitatAdministrativa) {
		this.unitatAdministrativa = unitatAdministrativa;
	}
	public boolean isNotificacioActiva() {
		return notificacioActiva;
	}
	public void setNotificacioActiva(boolean notificacioActiva) {
		this.notificacioActiva = notificacioActiva;
	}
	public String getNotificacioOrganCodi() {
		return notificacioOrganCodi;
	}
	public void setNotificacioOrganCodi(String notificacioOrganCodi) {
		this.notificacioOrganCodi = notificacioOrganCodi;
	}
	public String getNotificacioLlibreCodi() {
		return notificacioLlibreCodi;
	}
	public void setNotificacioLlibreCodi(String notificacioLlibreCodi) {
		this.notificacioLlibreCodi = notificacioLlibreCodi;
	}
	public String getNotificacioAvisTitol() {
		return notificacioAvisTitol;
	}
	public void setNotificacioAvisTitol(String notificacioAvisTitol) {
		this.notificacioAvisTitol = notificacioAvisTitol;
	}
	public String getNotificacioAvisText() {
		return notificacioAvisText;
	}
	public void setNotificacioAvisText(String notificacioAvisText) {
		this.notificacioAvisText = notificacioAvisText;
	}
	public String getNotificacioAvisTextSms() {
		return notificacioAvisTextSms;
	}
	public void setNotificacioAvisTextSms(String notificacioAvisTextSms) {
		this.notificacioAvisTextSms = notificacioAvisTextSms;
	}
	public String getNotificacioOficiTitol() {
		return notificacioOficiTitol;
	}
	public void setNotificacioOficiTitol(String notificacioOficiTitol) {
		this.notificacioOficiTitol = notificacioOficiTitol;
	}
	public String getNotificacioOficiText() {
		return notificacioOficiText;
	}
	public void setNotificacioOficiText(String notificacioOficiText) {
		this.notificacioOficiText = notificacioOficiText;
	}
	public Long getPareId() {
		return pareId;
	}
	public void setPareId(Long pareId) {
		this.pareId = pareId;
	}
	public Long getEntitatId() {
		return entitatId;
	}
	public void setEntitatId(Long entitatId) {
		this.entitatId = entitatId;
	}

	public static List<MetaExpedientCommand> toEntitatCommands(
			List<MetaExpedientDto> dtos) {
		List<MetaExpedientCommand> commands = new ArrayList<MetaExpedientCommand>();
		for (MetaExpedientDto dto: dtos) {
			commands.add(
					ConversioTipusHelper.convertir(
							dto,
							MetaExpedientCommand.class));
		}
		return commands;
	}

	public static MetaExpedientCommand asCommand(MetaExpedientDto dto) {
		return ConversioTipusHelper.convertir(
				dto,
				MetaExpedientCommand.class);
	}
	public static MetaExpedientDto asDto(MetaExpedientCommand command) {
		return ConversioTipusHelper.convertir(
				command,
				MetaExpedientDto.class);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
