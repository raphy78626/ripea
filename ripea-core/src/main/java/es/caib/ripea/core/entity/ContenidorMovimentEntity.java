/**
 * 
 */
package es.caib.ripea.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.ForeignKey;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.audit.RipeaAuditable;

/**
 * Classe del model de dades que representa l'enviament
 * d'un contenidor.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(	name = "ipa_cont_mov",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {
						"contenidor_id",
						"desti_id",
						"data"})})
@Inheritance(strategy=InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
public class ContenidorMovimentEntity extends RipeaAuditable<Long> {

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "contenidor_id")
	@ForeignKey(name = "ipa_contenidor_contmov_fk")
	protected ContenidorEntity contenidor;
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "origen_id")
	@ForeignKey(name = "ipa_origen_contmov_fk")
	protected ContenidorEntity origen;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "desti_id")
	@ForeignKey(name = "ipa_desti_contmov_fk")
	protected ContenidorEntity desti;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data", nullable = false)
	protected Date data;
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "remitent_id")
	@ForeignKey(name = "ipa_remitent_contmov_fk")
	protected UsuariEntity remitent;
	@Column(name = "comentari", length = 256)
	protected String comentari;
	@Version
	private long version = 0;



	public ContenidorEntity getContenidor() {
		return contenidor;
	}
	public ContenidorEntity getOrigen() {
		return origen;
	}
	public ContenidorEntity getDesti() {
		return desti;
	}
	public Date getData() {
		return data;
	}
	public UsuariEntity getRemitent() {
		return remitent;
	}
	public String getComentari() {
		return comentari;
	}

	/**
	 * Obté el Builder per a crear objectes de tipus ContenidorMoviment.
	 * 
	 * @param contenidor
	 *            El valor de l'atribut contenidor.
	 * @param origen
	 *            El valor de l'atribut origen.
	 * @param desti
	 *            El valor de l'atribut desti.
	 * @param remitent
	 *            El valor de l'atribut remitent.
	 * @param comentari
	 *            El valor de l'atribut comentari.
	 * @return Una nova instància del Builder.
	 */
	public static Builder getBuilder(
			ContenidorEntity contenidor,
			ContenidorEntity origen,
			ContenidorEntity desti,
			UsuariEntity remitent,
			String comentari) {
		return new Builder(
				contenidor,
				origen,
				desti,
				remitent,
				comentari);
	}

	/**
	 * Obté el Builder per a crear objectes de tipus ContenidorMoviment.
	 * 
	 * @param contenidor
	 *            El valor de l'atribut contenidor.
	 * @param desti
	 *            El valor de l'atribut desti.
	 * @param remitent
	 *            El valor de l'atribut remitent.
	 * @param comentari
	 *            El valor de l'atribut comentari.
	 * @return Una nova instància del Builder.
	 */
	public static Builder getBuilder(
			ContenidorEntity contenidor,
			ContenidorEntity desti,
			UsuariEntity remitent,
			String comentari) {
		return new Builder(
				contenidor,
				null,
				desti,
				remitent,
				comentari);
	}

	/**
	 * Builder per a crear noves instàncies d'aquesta classe.
	 * 
	 * @author Limit Tecnologies <limit@limit.es>
	 */
	public static class Builder {
		ContenidorMovimentEntity built;
		Builder(
				ContenidorEntity contenidor,
				ContenidorEntity origen,
				ContenidorEntity desti,
				UsuariEntity remitent,
				String comentari) {
			built = new ContenidorMovimentEntity();
			built.contenidor = contenidor;
			built.origen = origen;
			built.desti = desti;
			built.remitent = remitent;
			built.comentari = comentari;
			built.data = new Date();
		}
		public ContenidorMovimentEntity build() {
			return built;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((desti == null) ? 0 : desti.hashCode());
		result = prime * result + ((contenidor == null) ? 0 : contenidor.hashCode());
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
		ContenidorMovimentEntity other = (ContenidorMovimentEntity) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (desti == null) {
			if (other.desti != null)
				return false;
		} else if (!desti.equals(other.desti))
			return false;
		if (contenidor == null) {
			if (other.contenidor != null)
				return false;
		} else if (!contenidor.equals(other.contenidor))
			return false;
		return true;
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
