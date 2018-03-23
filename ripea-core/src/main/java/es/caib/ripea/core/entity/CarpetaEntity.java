/**
 * 
 */
package es.caib.ripea.core.entity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.api.dto.ContingutTipusEnumDto;

/**
 * Classe del model de dades que representa una carpeta.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(name = "ipa_carpeta")
@EntityListeners(AuditingEntityListener.class)
public class CarpetaEntity extends ContingutEntity {

	public void update(
			String nom) {
		this.nom = nom;
	}

	public static Builder getBuilder(
			String nom,
			ContingutEntity pare,
			EntitatEntity entitat) {
		return new Builder(
				nom,
				pare,
				entitat);
	}
	public static class Builder {
		CarpetaEntity built;
		Builder(
				String nom,
				ContingutEntity pare,
				EntitatEntity entitat) {
			built = new CarpetaEntity();
			built.nom = nom;
			built.pare = pare;
			built.entitat = entitat;
			built.tipus = ContingutTipusEnumDto.CARPETA;
		}
		public CarpetaEntity build() {
			return built;
		}
	}

	@Override
	public String getContingutType() {
		return "carpeta";
	}
	
	private static final long serialVersionUID = -2299453443943600172L;

}
