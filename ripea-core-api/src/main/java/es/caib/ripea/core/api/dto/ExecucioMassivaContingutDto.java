/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.Date;

/**
 * DTO amb informació d'una execució massiva
 * d'expedients.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ExecucioMassivaContingutDto extends AuditoriaDto {

	public enum ExecucioMassivaEstatDto {
		ESTAT_FINALITZAT,
		ESTAT_ERROR,
		ESTAT_PENDENT,
		ESTAT_CANCELAT
	}
	
	private Date dataInici;
	private Date dataFi;
	private ExecucioMassivaEstatDto estat;
	private String error;
	private int ordre;
	private ExecucioMassivaDto execucioMassiva;
	private ContingutDto contingut;
	
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

	public ExecucioMassivaEstatDto getEstat() {
		return estat;
	}

	public void setEstat(ExecucioMassivaEstatDto estat) {
		this.estat = estat;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public int getOrdre() {
		return ordre;
	}

	public void setOrdre(int ordre) {
		this.ordre = ordre;
	}

	public ExecucioMassivaDto getExecucioMassiva() {
		return execucioMassiva;
	}

	public void setExecucioMassiva(ExecucioMassivaDto execucioMassiva) {
		this.execucioMassiva = execucioMassiva;
	}

	public ContingutDto getContingut() {
		return contingut;
	}

	public void setContingut(ContingutDto contingut) {
		this.contingut = contingut;
	}

	private static final long serialVersionUID = 4061379951434174596L;
}
