/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Informació de log d'una accio realitzada damunt un node.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ContingutLogDto implements Serializable {

	private Long id;
	private Date data;
	private LogTipusEnumDto tipus;
	private UsuariDto usuari;
	private ContingutMovimentDto contingutMoviment;
	private Long objecteId;
	private LogObjecteTipusEnumDto objecteTipus;
	private LogTipusEnumDto objecteLogTipus;
	private String param1;
	private String param2;
	protected Long pareId;



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public LogTipusEnumDto getTipus() {
		return tipus;
	}
	public void setTipus(LogTipusEnumDto tipus) {
		this.tipus = tipus;
	}
	public UsuariDto getUsuari() {
		return usuari;
	}
	public void setUsuari(UsuariDto usuari) {
		this.usuari = usuari;
	}
	public ContingutMovimentDto getContenidorMoviment() {
		return contingutMoviment;
	}
	public void setContenidorMoviment(ContingutMovimentDto contingutMoviment) {
		this.contingutMoviment = contingutMoviment;
	}
	public Long getObjecteId() {
		return objecteId;
	}
	public void setObjecteId(Long objecteId) {
		this.objecteId = objecteId;
	}
	public LogObjecteTipusEnumDto getObjecteTipus() {
		return objecteTipus;
	}
	public void setObjecteTipus(LogObjecteTipusEnumDto objecteTipus) {
		this.objecteTipus = objecteTipus;
	}
	public LogTipusEnumDto getObjecteLogTipus() {
		return objecteLogTipus;
	}
	public void setObjecteLogTipus(LogTipusEnumDto objecteLogTipus) {
		this.objecteLogTipus = objecteLogTipus;
	}
	public String getParam1() {
		return param1;
	}
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	public String getParam2() {
		return param2;
	}
	public void setParam2(String param2) {
		this.param2 = param2;
	}
	public Long getPareId() {
		return pareId;
	}
	public void setPareId(Long pareId) {
		this.pareId = pareId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
