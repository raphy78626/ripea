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

	@Column(name = "tipus", nullable = false)
	@Enumerated(EnumType.STRING)
	private CarpetaTipusEnumDto carpetaTipus;



	public CarpetaTipusEnumDto getCarpetaTipus() {
		return carpetaTipus;
	}

	public void update(
			String nom,
			CarpetaTipusEnumDto carpetaTipus) {
		this.nom = nom;
		this.carpetaTipus = carpetaTipus;
	}

	public static Builder getBuilder(
			String nom,
			CarpetaTipusEnumDto tipus,
			ContingutEntity pare,
			EntitatEntity entitat) {
		return new Builder(
				nom,
				tipus,
				pare,
				entitat);
	}
	public static class Builder {
		CarpetaEntity built;
		Builder(
				String nom,
				CarpetaTipusEnumDto carpetaTipus,
				ContingutEntity pare,
				EntitatEntity entitat) {
			built = new CarpetaEntity();
			built.nom = nom;
			built.carpetaTipus = carpetaTipus;
			built.pare = pare;
			built.entitat = entitat;
			built.tipus = ContingutTipusEnumDto.CARPETA;
		}
		public CarpetaEntity build() {
			return built;
		}
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
