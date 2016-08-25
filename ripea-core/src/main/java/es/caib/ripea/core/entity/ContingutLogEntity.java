/**
 * 
 */
package es.caib.ripea.core.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.ForeignKey;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.api.dto.LogObjecteTipusEnumDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;
import es.caib.ripea.core.audit.RipeaAuditable;

/**
 * Classe del model de dades que representa un log de modificació
 * a un contenidor.
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
	@ForeignKey(name = "ipa_contingut_contlog_fk")
	protected ContingutEntity contingut;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "contmov_id")
	@ForeignKey(name = "ipa_contmov_contlog_fk")
	protected ContingutMovimentEntity contingutMoviment;
	@Column(name = "objecte_id")
	private Long objecteId;
	@Column(name = "objecte_tipus")
	private LogObjecteTipusEnumDto objecteTipus;
	@Column(name = "objecte_log_tipus")
	private LogTipusEnumDto objecteLogTipus;
	@Column(name = "param1", length = 256)
	private String param1;
	@Column(name = "param2", length = 256)
	private String param2;
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "pare_id")
	@ForeignKey(name = "ipa_pare_contlog_fk")
	protected ContingutLogEntity pare;
	@Version
	private long version = 0;
	
	/** Per propagar l'esborrat en els logs.
	 * TODO: s'ha de repensar per a que els logs es conservin i deixin esobrrar definitivament l'entitat.
	 */
	@OneToMany(mappedBy = "pare", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
	protected Set<ContingutLogEntity> fills;




	public LogTipusEnumDto getTipus() {
		return tipus;
	}
	public ContingutEntity getContingut() {
		return contingut;
	}
	public ContingutMovimentEntity getContingutMoviment() {
		return contingutMoviment;
	}
	public Long getObjecteId() {
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
			ContingutEntity contingut,
			ContingutLogEntity pare,
			ContingutMovimentEntity contingutMoviment,
			AbstractPersistable<Long> objecte,
			LogObjecteTipusEnumDto objecteTipus,
			LogTipusEnumDto objecteLogTipus) {
		return new Builder(
				tipus,
				contingut,
				pare,
				contingutMoviment,
				objecte,
				objecteTipus,
				objecteLogTipus);
	}
	public static class Builder {
		ContingutLogEntity built;
		Builder(
				LogTipusEnumDto tipus,
				ContingutEntity contingut,
				ContingutLogEntity pare,
				ContingutMovimentEntity contingutMoviment,
				AbstractPersistable<Long> objecte,
				LogObjecteTipusEnumDto objecteTipus,
				LogTipusEnumDto objecteLogTipus) {
			built = new ContingutLogEntity();
			built.tipus = tipus;
			built.contingut = contingut;
			built.pare = pare;
			built.contingutMoviment = contingutMoviment;
			if (objecte != null) {
				built.objecteId = objecte.getId();
				built.objecteTipus = objecteTipus;
				built.objecteLogTipus = objecteLogTipus;
			}
		}
		public ContingutLogEntity build() {
			return built;
		}
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
