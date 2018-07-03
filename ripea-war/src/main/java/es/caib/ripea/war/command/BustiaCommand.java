/**
 * 
 */
package es.caib.ripea.war.command;

import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import es.caib.ripea.core.api.dto.BustiaDto;
import es.caib.ripea.core.api.dto.UnitatOrganitzativaDto;
import es.caib.ripea.war.helper.ConversioTipusHelper;

/**
 * Command per al manteniment de bústies.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class BustiaCommand {

	private Long id;
	@NotEmpty @Size(max=256)
	private String nom;
	private String unitatCodi;
	private Long unitatId;
	private Long pareId;
	private Long entitatId;


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getUnitatCodi() {
		return unitatCodi;
	}
	public void setUnitatCodi(String unitatCodi) {
		this.unitatCodi = unitatCodi;
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

	public Long getUnitatId() {
		return unitatId;
	}
	public void setUnitatId(Long unitatId) {
		this.unitatId = unitatId;
	}
	
	public static BustiaCommand asCommand(BustiaDto dto) {
		BustiaCommand command = ConversioTipusHelper.convertir(
				dto, 
				BustiaCommand.class);
		if (dto.getUnitatOrganitzativa() != null)
			command.setUnitatId(
					dto.getUnitatOrganitzativa().getId());
		return command;
	}
	
	public static BustiaDto asDto(BustiaCommand command) {
		BustiaDto bustiaDto = ConversioTipusHelper.convertir(
				command,
				BustiaDto.class);
		UnitatOrganitzativaDto unitatOrganitzativaDto = new UnitatOrganitzativaDto();
		unitatOrganitzativaDto.setId(command.getUnitatId());
		bustiaDto.setUnitatOrganitzativa(unitatOrganitzativaDto);
		return bustiaDto;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
