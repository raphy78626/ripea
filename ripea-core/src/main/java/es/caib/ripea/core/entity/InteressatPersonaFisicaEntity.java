/**
 * 
 */
package es.caib.ripea.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Classe del model de dades que representa un interessat de tipus
 * ciutadà.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class InteressatPersonaFisicaEntity extends InteressatEntity {
	
//	nom: 					nom de l’interessat.
//	llinatge1: 				primer llinatge de l’interessat.
//	llinatge2: 				segon llinatge de l’interessat.

	@Column(name = "nom", length = 40)
	protected String nom;
	@Column(name = "llinatges", length = 256)
	protected String llinatges;
	@Column(name = "nif", length = 9)
	protected String nif;



	public String getLlinatges() {
		return llinatges;
	}
	public String getNif() {
		return nif;
	}

	public void update(
			String nom,
			String llinatges,
			String nif) {
		this.nom = nom;
		this.llinatges = llinatges;
		this.nif = nif;
	}

	/**
	 * Obté el Builder per a crear objectes de tipus interessat-ciutada.
	 * 
	 * @param nom
	 *             El valor de l'atribut nom.
	 * @param llinatges
	 *            El valor de l'atribut llinatges.
	 * @param nif
	 *            El valor de l'atribut nif.
	 * @param entitat
	 *            L'entitat a la qual pertany aquest interessat.
	 * @return Una nova instància del Builder.
	 */
	public static Builder getBuilder(
			String nom,
			String llinatges,
			String nif,
			EntitatEntity entitat) {
		return new Builder(
				nom,
				llinatges,
				nif,
				entitat);
	}

	/**
	 * Builder per a crear noves instàncies d'aquesta classe.
	 * 
	 * @author Limit Tecnologies <limit@limit.es>
	 */
	public static class Builder {
		InteressatPersonaFisicaEntity built;
		Builder(
				String nom,
				String llinatges,
				String nif,
				EntitatEntity entitat) {
			built = new InteressatPersonaFisicaEntity();
			built.nom = nom;
			built.llinatges = llinatges;
			built.nif = nif;
			built.entitat = entitat;
		}
		public InteressatPersonaFisicaEntity build() {
			return built;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((entitat == null) ? 0 : entitat.hashCode());
		result = prime * result
				+ ((llinatges == null) ? 0 : llinatges.hashCode());
		result = prime * result + ((nif == null) ? 0 : nif.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
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
		InteressatPersonaFisicaEntity other = (InteressatPersonaFisicaEntity) obj;
		if (entitat == null) {
			if (other.entitat != null)
				return false;
		} else if (!entitat.equals(other.entitat))
			return false;
		if (llinatges == null) {
			if (other.llinatges != null)
				return false;
		} else if (!llinatges.equals(other.llinatges))
			return false;
		if (nif == null) {
			if (other.nif != null)
				return false;
		} else if (!nif.equals(other.nif))
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		return true;
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
