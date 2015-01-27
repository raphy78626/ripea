/**
 * 
 */
package es.caib.ripea.core.api.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Informació d'una dada.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class DadaDto extends AuditoriaDto {

	private Long id;
	private MetaDadaDto metaDada;
	protected String valor;
	protected int ordre;



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public MetaDadaDto getMetaDada() {
		return metaDada;
	}
	public void setMetaDada(MetaDadaDto metaDada) {
		this.metaDada = metaDada;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getValor() {
		return valor;
	}
	public int getOrdre() {
		return ordre;
	}
	public void setOrdre(int ordre) {
		this.ordre = ordre;
	}

	public String getValorMostrar() {
		if (metaDada != null) {
			if (metaDada.getTipus().equals(MetaDadaTipusEnumDto.TEXT)) {
				return valor;
			} else if (metaDada.getTipus().equals(MetaDadaTipusEnumDto.DATA)) {
				return valor;
			} else if (metaDada.getTipus().equals(MetaDadaTipusEnumDto.SENCER)) {
				return valor.toString();
			} else if (metaDada.getTipus().equals(MetaDadaTipusEnumDto.FLOTANT)) {
				return valor.toString();
			} else if (metaDada.getTipus().equals(MetaDadaTipusEnumDto.IMPORT)) {
				return valor.toString();
			} else if (metaDada.getTipus().equals(MetaDadaTipusEnumDto.BOOLEA)) {
				return new Boolean(valor).toString();
			} else {
				return valor;
			}
		} else {
			return valor;
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
