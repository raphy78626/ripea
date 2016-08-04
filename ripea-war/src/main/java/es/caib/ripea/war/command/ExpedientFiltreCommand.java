/**
 * 
 */
package es.caib.ripea.war.command;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import es.caib.ripea.core.api.dto.ExpedientEstatEnumDto;
import es.caib.ripea.core.api.dto.ExpedientFiltreDto;
import es.caib.ripea.war.helper.ConversioTipusHelper;

/**
 * Command per al filtre d'expedients dels arxius.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ExpedientFiltreCommand {

	public enum ExpedientFiltreOpcionsEstatEnum {
		OBERTS,
		TANCATS,
		TOTS
	};

	private Long arxiuId;
	private Long metaExpedientId;
	private String nom;
	private Date dataCreacioInici;
	private Date dataCreacioFi;
	/** Formació de la cadena sequencia/any */
	private String numero;
	/** Tipus d'expedient */
	private ExpedientFiltreOpcionsEstatEnum estatFiltre;
	private Date dataTancatInici;
	private Date dataTancatFi;



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

	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public ExpedientFiltreOpcionsEstatEnum getEstatFiltre() {
		return estatFiltre;
	}
	public void setEstatFiltre(ExpedientFiltreOpcionsEstatEnum estat) {
		this.estatFiltre = estat;
	}
	public Date getDataTancatInici() {
		return dataTancatInici;
	}
	public void setDataTancatInici(Date dataTancatInici) {
		this.dataTancatInici = dataTancatInici;
	}
	public Date getDataTancatFi() {
		return dataTancatFi;
	}
	public void setDataTancatFi(Date dataTancatFi) {
		this.dataTancatFi = dataTancatFi;
	}
	
	public static ExpedientFiltreCommand asCommand(ExpedientFiltreDto dto) {
		ExpedientFiltreCommand command = ConversioTipusHelper.convertir(
				dto,
				ExpedientFiltreCommand.class);
		if (dto.getEstat() != null)
			switch(dto.getEstat()) {
			case OBERT:
				command.setEstatFiltre(ExpedientFiltreOpcionsEstatEnum.OBERTS);
				break;
			case TANCAT:
				command.setEstatFiltre(ExpedientFiltreOpcionsEstatEnum.TANCATS);
				break;
			default:
				command.setEstatFiltre(null);
			}
		else
			command.setEstatFiltre(null);
		return command;
	}
	
	public static ExpedientFiltreDto asDto(ExpedientFiltreCommand command) {
		ExpedientFiltreDto dto = ConversioTipusHelper.convertir(
				command,
				ExpedientFiltreDto.class);
		if (command.getEstatFiltre() != null) 
			switch (command.getEstatFiltre()) {
			case OBERTS:
				dto.setEstat(ExpedientEstatEnumDto.OBERT);
				break;
			case TANCATS:
				dto.setEstat(ExpedientEstatEnumDto.TANCAT);
				break;
			case TOTS:
				dto.setEstat(null);
				break;
			default:
				break;
			}
		else
			dto.setEstat(null);
		return dto;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
