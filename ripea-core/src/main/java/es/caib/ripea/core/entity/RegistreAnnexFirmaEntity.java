package es.caib.ripea.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.audit.RipeaAuditable;

@Entity
@Table(	name = "ipa_registre_annex_firma")
@EntityListeners(AuditingEntityListener.class)
public class RegistreAnnexFirmaEntity extends RipeaAuditable<Long> {
	
	@Column(name = "tipus")
	private String tipus;
	@Column(name = "perfil")
	private String perfil;
	@Column(name = "fitxer_nom", length = 80)
	private String fitxerNom;
	@Column(name = "tipus_mime", length = 30)
	private String tipusMime;
	@Column(name = "csv_regulacio")
	private String csvRegulacio;
	@Column(name = "autofirma")
	private Boolean autofirma;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "annex_id")
	@ForeignKey(name = "ipa_firma_annex_fk")
	private RegistreAnnexEntity annex;

	public String getTipus() {
		return tipus;
	}
	public String getPerfil() {
		return perfil;
	}
	public String getFitxerNom() {
		return fitxerNom;
	}
	public String getTipusMime() {
		return tipusMime;
	}
	public String getCsvRegulacio() {
		return csvRegulacio;
	}
	public boolean isAutofirma() {
		return autofirma != null && autofirma.booleanValue();
	}
	public RegistreAnnexEntity getAnnex() {
		return annex;
	}

	public static Builder getBuilder(
			String tipus,
			String perfil,
			String fitxerNom,
			String tipusMime,
			String csvRegulacio,
			boolean autofirma,
			RegistreAnnexEntity annex) {
		return new Builder(
				tipus,
				perfil,
				fitxerNom,
				tipusMime,
				csvRegulacio,
				autofirma,
				annex);
	}

	public static class Builder {
		RegistreAnnexFirmaEntity built;
		Builder(
				String tipus,
				String perfil,
				String fitxerNom,
				String tipusMime,
				String csvRegulacio,
				boolean autofirma,
				RegistreAnnexEntity annex) {
			built = new RegistreAnnexFirmaEntity();
			built.tipus = tipus;
			built.perfil = perfil;
			built.fitxerNom = fitxerNom;
			built.tipusMime = tipusMime;
			built.csvRegulacio = csvRegulacio;
			built.autofirma = autofirma;
			built.annex = annex;
		}
		public RegistreAnnexFirmaEntity build() {
			return built;
		}
	}

	private static final long serialVersionUID = -1201133969119981591L;

}
