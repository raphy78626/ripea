/**
 * 
 */
package es.caib.ripea.core.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.api.dto.ContingutTipusEnumDto;

/**
 * Classe del model de dades que representa un arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(name = "ipa_arxiu")
@EntityListeners(AuditingEntityListener.class)
public class ArxiuEntity extends ContingutEntity {

	@Column(name = "unitat_codi", length = 9, nullable = false)
	protected String unitatCodi;
	@Column(name = "actiu")
	protected boolean actiu;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "ipa_metaexpedient_arxiu",
			joinColumns = {@JoinColumn(name = "arxiu_id")},
			inverseJoinColumns = {@JoinColumn(name = "metaexpedient_id")})
	protected List<MetaExpedientEntity> metaExpedients = new ArrayList<MetaExpedientEntity>();
		
	public String getUnitatCodi() {
		return unitatCodi;
	}
	public boolean isActiu() {
		return actiu;
	}

	public List<MetaExpedientEntity> getMetaExpedients() {
		return metaExpedients;
	}
	public void setMetaExpedients(List<MetaExpedientEntity> metaExpedients) {
		this.metaExpedients = metaExpedients;
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
			built.tipus = ContingutTipusEnumDto.ARXIU;
		}
		public ArxiuEntity build() {
			return built;
		}
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
