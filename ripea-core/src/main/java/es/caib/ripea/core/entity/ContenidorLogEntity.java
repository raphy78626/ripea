/**
 * 
 */
package es.caib.ripea.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.ForeignKey;
import org.springframework.data.jpa.domain.AbstractPersistable;

import es.caib.ripea.core.api.dto.LogObjecteTipusEnumDto;
import es.caib.ripea.core.api.dto.LogTipusEnumDto;

/**
 * Classe del model de dades que representa un log de modificació
 * a un contenidor.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(name="ipa_cont_log")
public class ContenidorLogEntity extends AbstractPersistable<Long> {

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data", nullable = false)
	private Date data;
	@Column(name = "tipus", nullable = false)
	private LogTipusEnumDto tipus;
	@Column(name = "usuari_id", nullable = false)
	private String usuariId;
	@Column(name = "contenidor_id", nullable = false)
	private Long contenidorId;
	@Column(name = "contenidor_mov_id")
	private Long contenidorMovimentId;
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
	protected ContenidorLogEntity pare;
	@Version
	private long version = 0;



	public Date getData() {
		return data;
	}
	public LogTipusEnumDto getTipus() {
		return tipus;
	}
	public String getUsuariId() {
		return usuariId;
	}
	public Long getContenidorId() {
		return contenidorId;
	}
	public Long getContenidorMovimentId() {
		return contenidorMovimentId;
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
	public ContenidorLogEntity getPare() {
		return pare;
	}

	public void updateParams(
			String param1,
			String param2) {
		this.param1 = param1;
		this.param2 = param2;
	}

	/**
	 * Obté el Builder per a crear objectes de tipus ContenidorLog.
	 */
	public static Builder getBuilder(
			Date data,
			LogTipusEnumDto tipus,
			String usuariId,
			ContenidorEntity contenidor,
			ContenidorLogEntity pare,
			ContenidorMovimentEntity contenidorMoviment,
			AbstractPersistable<Long> objecte,
			LogObjecteTipusEnumDto objecteTipus,
			LogTipusEnumDto objecteLogTipus) {
		return new Builder(
				data,
				tipus,
				usuariId,
				contenidor,
				pare,
				contenidorMoviment,
				objecte,
				objecteTipus,
				objecteLogTipus);
	}

	/**
	 * Builder per a crear noves instàncies d'aquesta classe.
	 * 
	 * @author Josep Gayà
	 */
	public static class Builder {
		ContenidorLogEntity built;
		Builder(
				Date data,
				LogTipusEnumDto tipus,
				String usuariId,
				ContenidorEntity contenidor,
				ContenidorLogEntity pare,
				ContenidorMovimentEntity contenidorMoviment,
				AbstractPersistable<Long> objecte,
				LogObjecteTipusEnumDto objecteTipus,
				LogTipusEnumDto objecteLogTipus) {
			built = new ContenidorLogEntity();
			built.data = data;
			built.tipus = tipus;
			built.usuariId = usuariId;
			built.contenidorId = contenidor.getId();
			built.pare = pare;
			if (contenidorMoviment != null)
				built.contenidorMovimentId = contenidorMoviment.getId();
			if (objecte != null) {
				built.objecteId = objecte.getId();
				built.objecteTipus = objecteTipus;
				built.objecteLogTipus = objecteLogTipus;
			}
		}
		public ContenidorLogEntity build() {
			return built;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((contenidorId == null) ? 0 : contenidorId.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((pare == null) ? 0 : pare.hashCode());
		result = prime * result + ((tipus == null) ? 0 : tipus.hashCode());
		result = prime * result
				+ ((usuariId == null) ? 0 : usuariId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContenidorLogEntity other = (ContenidorLogEntity) obj;
		if (contenidorId == null) {
			if (other.contenidorId != null)
				return false;
		} else if (!contenidorId.equals(other.contenidorId))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (pare == null) {
			if (other.pare != null)
				return false;
		} else if (!pare.equals(other.pare))
			return false;
		if (tipus != other.tipus)
			return false;
		if (usuariId == null) {
			if (other.usuariId != null)
				return false;
		} else if (!usuariId.equals(other.usuariId))
			return false;
		return true;
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
