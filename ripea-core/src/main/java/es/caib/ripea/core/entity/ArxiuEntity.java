/**
 * 
 */
package es.caib.ripea.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import es.caib.ripea.core.audit.RipeaAuditingEntityListener;

/**
 * Classe del model de dades que representa un arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(name = "ipa_arxiu")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@EntityListeners(RipeaAuditingEntityListener.class)
public class ArxiuEntity  extends ContenidorEntity {

	@Column(name = "unitat_codi", length = 9, nullable = false)
	protected String unitatCodi;
	@Column(name = "actiu")
	protected boolean actiu;



	public String getUnitatCodi() {
		return unitatCodi;
	}
	public boolean isActiu() {
		return actiu;
	}

	public void update(String nom) {
		this.nom = nom;
	}
	public void updateActiu(boolean actiu) {
		this.actiu = actiu;
	}

	/**
	 * Obté el Builder per a crear objectes de tipus arxiu.
	 * 
	 * @param entitat
	 *            El valor de l'atribut entitat.
	 * @param nom
	 *            El valor de l'atribut nom.
	 * @param unitatCodi
	 *            El codi de l'unitat a la qual pertany l'arxiu.
	 * @param pare
	 *            L'arxiu pare.
	 * @param entitat
	 *            L'entitat a la qual pertany aquest arxiu.
	 * @return Una nova instància del Builder.
	 */
	public static Builder getBuilder(
			EntitatEntity entitat,
			String nom,
			String unitatCodi,
			ArxiuEntity pare) {
		return new Builder(
				entitat,
				nom,
				unitatCodi,
				pare);
	}

	/**
	 * Builder per a crear noves instàncies d'aquesta classe.
	 * 
	 * @author Limit Tecnologies <limit@limit.es>
	 */
	public static class Builder {
		ArxiuEntity built;
		Builder(
				EntitatEntity entitat,
				String nom,
				String unitatCodi,
				ArxiuEntity pare) {
			built = new ArxiuEntity();
			built.entitat = entitat;
			built.nom = nom;
			built.unitatCodi = unitatCodi;
			built.pare = pare;
			built.actiu = true;
			built.tipusContenidor = ContenidorTipusEnum.ARXIU;
		}
		public ArxiuEntity build() {
			return built;
		}
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
