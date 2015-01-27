/**
 * 
 */
package es.caib.ripea.core.entity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

import es.caib.ripea.core.audit.RipeaAuditingEntityListener;

/**
 * Classe del model de dades que representa un escriptori.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(name = "ipa_escriptori")
@EntityListeners(RipeaAuditingEntityListener.class)
public class EscriptoriEntity extends ContenidorEntity {

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name="usuari_id")
	@ForeignKey(name="ipa_usuari_escriptori_fk")
	protected UsuariEntity usuari;



	public UsuariEntity getUsuari() {
		return usuari;
	}

	/**
	 * Obté el Builder per a crear objectes de tipus escriptori.
	 * 
	 * @param usuari
	 *            L'usuari al qual pertany aquest escriptori.
	 * @param entitat
	 *            L'entitat a la qual pertany aquest escriptori.
	 * @return Una nova instància del Builder.
	 */
	public static Builder getBuilder(
			UsuariEntity usuari,
			EntitatEntity entitat) {
		return new Builder(
				usuari,
				entitat);
	}

	/**
	 * Builder per a crear noves instàncies d'aquesta classe.
	 * 
	 * @author Limit Tecnologies <limit@limit.es>
	 */
	public static class Builder {
		EscriptoriEntity built;
		Builder(
				UsuariEntity usuari,
				EntitatEntity entitat) {
			built = new EscriptoriEntity();
			built.usuari = usuari;
			built.entitat = entitat;
			built.nom = "[" + entitat.getCodi() + "]" + usuari.getCodi();
			built.tipusContenidor = ContenidorTipusEnum.ESCRIPTORI;
		}
		public EscriptoriEntity build() {
			return built;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((entitat == null) ? 0 : entitat.hashCode());
		result = prime * result + ((usuari == null) ? 0 : usuari.hashCode());
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
		EscriptoriEntity other = (EscriptoriEntity) obj;
		if (entitat == null) {
			if (other.entitat != null)
				return false;
		} else if (!entitat.equals(other.entitat))
			return false;
		if (usuari == null) {
			if (other.usuari != null)
				return false;
		} else if (!usuari.equals(other.usuari))
			return false;
		return true;
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
