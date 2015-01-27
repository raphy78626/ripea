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

import es.caib.ripea.core.audit.RipeaAuditable;
import es.caib.ripea.core.audit.RipeaAuditingEntityListener;

/**
 * Classe del model de dades que representa un moviment
 * d'una anotació de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(	name = "ipa_registre_mov",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {
						"registre_id",
						"desti_id",
						"data"})})
@Inheritance(strategy=InheritanceType.JOINED)
@EntityListeners(RipeaAuditingEntityListener.class)
public class RegistreMovimentEntity extends RipeaAuditable<Long> {

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "registre_id")
	@ForeignKey(name = "ipa_registre_regmov_fk")
	protected RegistreEntity registre;
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "origen_id")
	@ForeignKey(name = "ipa_origen_regmov_fk")
	protected ContenidorEntity origen;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "desti_id")
	@ForeignKey(name = "ipa_desti_regmov_fk")
	protected ContenidorEntity desti;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data", nullable = false)
	protected Date data;
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "remitent_id")
	@ForeignKey(name = "ipa_remitent_regmov_fk")
	protected UsuariEntity remitent;
	@Column(name = "comentari", length = 256)
	protected String comentari;
	@Version
	private long version = 0;



	public RegistreEntity getRegistre() {
		return registre;
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
	 * @param registre
	 *            El valor de l'atribut registre.
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
			RegistreEntity registre,
			ContenidorEntity origen,
			ContenidorEntity desti,
			UsuariEntity remitent,
			String comentari) {
		return new Builder(
				registre,
				origen,
				desti,
				remitent,
				comentari);
	}

	/**
	 * Obté el Builder per a crear objectes de tipus ContenidorMoviment.
	 * 
	 * @param registre
	 *            El valor de l'atribut registre.
	 * @param desti
	 *            El valor de l'atribut desti.
	 * @param remitent
	 *            El valor de l'atribut remitent.
	 * @param comentari
	 *            El valor de l'atribut comentari.
	 * @return Una nova instància del Builder.
	 */
	public static Builder getBuilder(
			RegistreEntity registre,
			ContenidorEntity desti,
			UsuariEntity remitent,
			String comentari) {
		return new Builder(
				registre,
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
		RegistreMovimentEntity built;
		Builder(
				RegistreEntity registre,
				ContenidorEntity origen,
				ContenidorEntity desti,
				UsuariEntity remitent,
				String comentari) {
			built = new RegistreMovimentEntity();
			built.registre = registre;
			built.origen = origen;
			built.desti = desti;
			built.remitent = remitent;
			built.comentari = comentari;
			built.data = new Date();
		}
		public RegistreMovimentEntity build() {
			return built;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((desti == null) ? 0 : desti.hashCode());
		result = prime * result + ((registre == null) ? 0 : registre.hashCode());
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
		RegistreMovimentEntity other = (RegistreMovimentEntity) obj;
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
		if (registre == null) {
			if (other.registre != null)
				return false;
		} else if (!registre.equals(other.registre))
			return false;
		return true;
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
