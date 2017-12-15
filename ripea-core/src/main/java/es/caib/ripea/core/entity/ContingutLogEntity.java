/**
 * 
 */
package es.caib.ripea.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.api.dto.LogObjecteTipusEnumDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.audit.RipeaAuditable;

/**
 * Classe del model de dades que representa el registre d'una acció
 * feta sobre un contingut.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(name = "ipa_cont_log")
@EntityListeners(AuditingEntityListener.class)
public class ContingutLogEntity extends RipeaAuditable<Long> {

	@Column(name = "tipus", nullable = false)
	private LogTipusEnumDto tipus;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "contingut_id")
	protected ContingutEntity contingut;
	@Column(name = "objecte_id", length = 64)
	private String objecteId;
	@Column(name = "objecte_tipus")
	private LogObjecteTipusEnumDto objecteTipus;
	@Column(name = "objecte_log_tipus")
	private LogTipusEnumDto objecteLogTipus;
	@Column(name = "param1", length = 256)
	private String param1;
	@Column(name = "param2", length = 256)
	private String param2;
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "contmov_id")
	@ForeignKey(name = "ipa_contmov_contlog_fk")
	protected ContingutMovimentEntity contingutMoviment;
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "pare_id")
	@ForeignKey(name = "ipa_pare_contlog_fk")
	protected ContingutLogEntity pare;



	public LogTipusEnumDto getTipus() {
		return tipus;
	}
	public ContingutEntity getContingut() {
		return contingut;
	}
	public ContingutMovimentEntity getContingutMoviment() {
		return contingutMoviment;
	}
	public String getObjecteId() {
		return objecteId;
	}
	public LogObjecteTipusEnumDto getObjecteTipus() {
		return objecteTipus;
	}
	public LogTipusEnumDto getObjecteLogTipus() {
		return objecteLogTipus;
	}
	public String getParam1() {
		return param1;
	}
	public String getParam2() {
		return param2;
	}
	public ContingutLogEntity getPare() {
		return pare;
	}

	public void updateParams(
			String param1,
			String param2) {
		this.param1 = param1;
		this.param2 = param2;
	}

	public static Builder getBuilder(
			LogTipusEnumDto tipus,
			ContingutEntity contingut) {
		return new Builder(
				tipus,
				contingut);
	}
	public static class Builder {
		ContingutLogEntity built;
		Builder(
				LogTipusEnumDto tipus,
				ContingutEntity contingut) {
			built = new ContingutLogEntity();
			built.tipus = tipus;
			built.contingut = contingut;
		}
		public Builder objecte(Persistable<? extends Serializable> objecte) {
			built.objecteId = objecte.getId().toString();
			return this;
		}
		public Builder objecteTipus(LogObjecteTipusEnumDto objecteTipus) {
			built.objecteTipus = objecteTipus;
			return this;
		}
		public Builder objecteLogTipus(LogTipusEnumDto objecteLogTipus) {
			built.objecteLogTipus = objecteLogTipus;
			return this;
		}
		public Builder param1(String param1) {
			built.param1 = param1;
			return this;
		}
		public Builder param2(String param2) {
			built.param2 = param2;
			return this;
		}
		public Builder pare(ContingutLogEntity pare) {
			built.pare = pare;
			return this;
		}
		public Builder contingutMoviment(ContingutMovimentEntity contingutMoviment) {
			built.contingutMoviment = contingutMoviment;
			return this;
		}
		public ContingutLogEntity build() {
			return built;
		}
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
