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
 * administració.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class InteressatAdministracioEntity extends InteressatEntity {

	@Column(name = "identificador", length = 9)
	protected String identificador;



	public String getIdentificador() {
		return identificador;
	}

	public void update(
			String nom,
			String identificador) {
		this.nom = nom;
		this.identificador = identificador;
	}

	/**
	 * Obté el Builder per a crear objectes de tipus interessat-ciutada.
	 * 
	 * @param nom
	 *             El valor de l'atribut nom.
	 * @param identificador
	 *            El valor de l'atribut identificador.
	 * @param entitat
	 *            L'entitat a la qual pertany aquest interessat.
	 * @return Una nova instància del Builder.
	 */
	public static Builder getBuilder(
			String nom,
			String identificador,
			EntitatEntity entitat) {
		return new Builder(
				nom,
				identificador,
				entitat);
	}

	/**
	 * Builder per a crear noves instàncies d'aquesta classe.
	 * 
	 * @author Limit Tecnologies <limit@limit.es>
	 */
	public static class Builder {
		InteressatAdministracioEntity built;
		Builder(
				String nom,
				String identificador,
				EntitatEntity entitat) {
			built = new InteressatAdministracioEntity();
			built.nom = nom;
			built.identificador = identificador;
			built.entitat = entitat;
		}
		public InteressatAdministracioEntity build() {
			return built;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((entitat == null) ? 0 : entitat.hashCode());
		result = prime * result
				+ ((identificador == null) ? 0 : identificador.hashCode());
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
		InteressatAdministracioEntity other = (InteressatAdministracioEntity) obj;
		if (entitat == null) {
			if (other.entitat != null)
				return false;
		} else if (!entitat.equals(other.entitat))
			return false;
		if (identificador == null) {
			if (other.identificador != null)
				return false;
		} else if (!identificador.equals(other.identificador))
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
