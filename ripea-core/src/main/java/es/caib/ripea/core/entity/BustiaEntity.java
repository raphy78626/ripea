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
@Table(name = "ipa_bustia")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@EntityListeners(RipeaAuditingEntityListener.class)
public class BustiaEntity extends ContenidorEntity {

	@Column(name = "unitat_codi", length = 9, nullable = false)
	protected String unitatCodi;
	@Column(name = "per_defecte")
	protected boolean perDefecte;
	@Column(name = "activa")
	protected boolean activa;



	public String getUnitatCodi() {
		return unitatCodi;
	}
	public boolean isPerDefecte() {
		return perDefecte;
	}
	public boolean isActiva() {
		return activa;
	}

	public void updatePerDefecte(boolean perDefecte) {
		this.perDefecte = perDefecte;
	}
	public void updateActiva(boolean activa) {
		this.activa = activa;
	}

	/**
	 * Obté el Builder per a crear objectes de tipus bústia.
	 * 
	 * @param entitat
	 *            El valor de l'atribut entitat.
	 * @param nom
	 *            El valor de l'atribut nom.
	 * @param unitatCodi
	 *            El codi de l'unitat a la qual pertany l'arxiu.
	 * @param pare
	 *            La bústia pare.
	 * @param entitat
	 *            L'entitat a la qual pertany aquesta bústia.
	 * @return Una nova instància del Builder.
	 */
	public static Builder getBuilder(
			EntitatEntity entitat,
			String nom,
			String unitatCodi,
			BustiaEntity pare) {
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
		BustiaEntity built;
		Builder(
				EntitatEntity entitat,
				String nom,
				String unitatCodi,
				BustiaEntity pare) {
			built = new BustiaEntity();
			built.entitat = entitat;
			built.nom = nom;
			built.unitatCodi = unitatCodi;
			built.pare = pare;
			built.activa = true;
			built.tipusContenidor = ContenidorTipusEnum.BUSTIA;
		}
		public BustiaEntity build() {
			return built;
		}
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
