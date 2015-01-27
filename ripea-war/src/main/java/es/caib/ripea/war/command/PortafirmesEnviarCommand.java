/**
 * 
 */
package es.caib.ripea.war.command;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import es.caib.ripea.core.api.dto.PortafirmesPrioritatEnumDto;

/**
 * Command per a enviar documents al portafirmes.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class PortafirmesEnviarCommand {

	@NotEmpty @Size(max=256)
	private String motiu;
	@NotNull
	private PortafirmesPrioritatEnumDto prioritat = PortafirmesPrioritatEnumDto.NORMAL;
	@NotNull
	private Date dataCaducitat;



	public String getMotiu() {
		return motiu;
	}
	public void setMotiu(String motiu) {
		this.motiu = motiu;
	}
	public PortafirmesPrioritatEnumDto getPrioritat() {
		return prioritat;
	}
	public void setPrioritat(PortafirmesPrioritatEnumDto prioritat) {
		this.prioritat = prioritat;
	}
	public Date getDataCaducitat() {
		return dataCaducitat;
	}
	public void setDataCaducitat(Date dataCaducitat) {
		this.dataCaducitat = dataCaducitat;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
