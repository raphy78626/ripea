/**
 * 
 */
package es.caib.ripea.war.command;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import es.caib.ripea.core.api.dto.ExpedientFiltreDto;
import es.caib.ripea.war.helper.ConversioTipusHelper;

/**
 * Command per al filtre d'expedients dels arxius.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ExpedientFiltreCommand {

	private Long arxiuId;
	private Long metaExpedientId;
	private String nom;
	private Date dataCreacioInici;
	private Date dataCreacioFi;



	public Long getArxiuId() {
		return arxiuId;
	}
	public void setArxiuId(Long arxiuId) {
		this.arxiuId = arxiuId;
	}
	public Long getMetaExpedientId() {
		return metaExpedientId;
	}
	public void setMetaExpedientId(Long metaExpedientId) {
		this.metaExpedientId = metaExpedientId;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Date getDataCreacioInici() {
		return dataCreacioInici;
	}
	public void setDataCreacioInici(Date dataCreacioInici) {
		this.dataCreacioInici = dataCreacioInici;
	}
	public Date getDataCreacioFi() {
		return dataCreacioFi;
	}
	public void setDataCreacioFi(Date dataCreacioFi) {
		this.dataCreacioFi = dataCreacioFi;
	}

	public static ExpedientFiltreCommand asCommand(ExpedientFiltreDto dto) {
		return ConversioTipusHelper.convertir(
				dto,
				ExpedientFiltreCommand.class);
	}
	public static ExpedientFiltreDto asDto(ExpedientFiltreCommand command) {
		return ConversioTipusHelper.convertir(
				command,
				ExpedientFiltreDto.class);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
