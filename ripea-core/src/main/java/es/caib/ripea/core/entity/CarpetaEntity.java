/**
 * 
 */
package es.caib.ripea.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.api.dto.CarpetaTipusEnumDto;

/**
 * Classe del model de dades que representa una carpeta.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(name = "ipa_carpeta")
@EntityListeners(AuditingEntityListener.class)
public class CarpetaEntity extends ContenidorEntity {

	@Column(name = "tipus", nullable = false)
	@Enumerated(EnumType.STRING)
	protected CarpetaTipusEnumDto tipus;



	public CarpetaTipusEnumDto getTipus() {
		return tipus;
	}

	public void update(
			String nom,
			CarpetaTipusEnumDto tipus) {
		this.nom = nom;
		this.tipus = tipus;
	}

	/**
	 * Obté el Builder per a crear objectes de tipus carpeta.
	 * 
	 * @param nom
	 *            El valor de l'atribut nom.
	 * @param nom
	 *            El valor de l'atribut tipus.
	 * @param metaNode
	 *            El meta-node al qual pertany aquesta carpeta.
	 * @param pare
	 *            El contenidor al qual pertany aquesta carpeta.
	 * @param entitat
	 *            L'entitat a la qual pertany aquesta carpeta.
	 * @return Una nova instància del Builder.
	 */
	public static Builder getBuilder(
			String nom,
			CarpetaTipusEnumDto tipus,
			ContenidorEntity pare,
			EntitatEntity entitat) {
		return new Builder(
				nom,
				tipus,
				pare,
				entitat);
	}

	/**
	 * Builder per a crear noves instàncies d'aquesta classe.
	 * 
	 * @author Limit Tecnologies <limit@limit.es>
	 */
	public static class Builder {
		CarpetaEntity built;
		Builder(
				String nom,
				CarpetaTipusEnumDto tipus,
				ContenidorEntity pare,
				EntitatEntity entitat) {
			built = new CarpetaEntity();
			built.nom = nom;
			built.tipus = tipus;
			built.pare = pare;
			built.entitat = entitat;
			built.tipusContenidor = ContenidorTipusEnum.CONTINGUT;
		}
		public CarpetaEntity build() {
			return built;
		}
	}

	private static final long serialVersionUID = -2299453443943600172L;

}