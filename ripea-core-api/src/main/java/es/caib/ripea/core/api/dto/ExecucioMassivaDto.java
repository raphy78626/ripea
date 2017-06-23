/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DTO amb informació d'una execució massiva
 * de continguts.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ExecucioMassivaDto extends AuditoriaDto {

	public enum ExecucioMassivaTipusDto {
		PORTASIGNATURES
	}

	private ExecucioMassivaTipusDto tipus;
	private Date dataInici;
	private Date dataFi;
	
//	Paràmetres enviament portafirmes
	private String motiu;
	private PortafirmesPrioritatEnumDto prioritat = PortafirmesPrioritatEnumDto.NORMAL;
	private Date dataCaducitat;
//////////////////////////////////////	
	
	private Boolean enviarCorreu;
	private List<Long> contingutIds = new ArrayList<Long>();
	
	public ExecucioMassivaTipusDto getTipus() {
		return tipus;
	}

	public void setTipus(ExecucioMassivaTipusDto tipus) {
		this.tipus = tipus;
	}

	public Date getDataInici() {
		return dataInici;
	}

	public void setDataInici(Date dataInici) {
		this.dataInici = dataInici;
	}

	public Date getDataFi() {
		return dataFi;
	}

	public void setDataFi(Date dataFi) {
		this.dataFi = dataFi;
	}

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

	public Boolean getEnviarCorreu() {
		return enviarCorreu;
	}

	public void setEnviarCorreu(Boolean enviarCorreu) {
		this.enviarCorreu = enviarCorreu;
	}


	public List<Long> getContingutIds() {
		return contingutIds;
	}

	public void setContingutIds(List<Long> contingutIds) {
		this.contingutIds = contingutIds;
	}

	private static final long serialVersionUID = 4061379951434174596L;
}
