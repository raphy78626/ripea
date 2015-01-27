/**
 * 
 */
package es.caib.ripea.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.ForeignKey;

import es.caib.ripea.core.audit.RipeaAuditable;
import es.caib.ripea.core.audit.RipeaAuditingEntityListener;

/**
 * Classe del model de dades que representa un interessat
 * d'una anotació al registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(	name = "ipa_registre_inter",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {
						"registre_id",
						"doc_tipus",
						"doc_num",
						"nom",
						"llinatge1",
						"llinatge2",
						"rao_social"})})
@EntityListeners(RipeaAuditingEntityListener.class)
public class RegistreInteressatEntity extends RipeaAuditable<Long> {

	@Column(name = "doc_tipus")
	private RegistreInteressatDocumentTipusEnum documentTipus;
	@Column(name = "doc_num", length = 17)
	private String documentNum;
	@Column(name = "nom", length = 30)
	private String nom;
	@Column(name = "llinatge1", length = 30)
	private String llinatge1;
	@Column(name = "llinatge2", length = 30)
	private String llinatge2;
	@Column(name = "rao_social", length = 80)
	private String raoSocial;
	@Column(name = "pais", length = 4)
	private String pais;
	@Column(name = "provincia", length = 2)
	private String provincia;
	@Column(name = "municipi", length = 5)
	private String municipi;
	@Column(name = "adresa", length = 160)
	private String adresa;
	@Column(name = "codi_postal", length = 5)
	private String codiPostal;
	@Column(name = "email", length = 160)
	private String email;
	@Column(name = "telefon", length = 20)
	private String telefon;
	@Column(name = "email_hab", length = 160)
	private String emailHabilitat;
	@Column(name = "canal_pref")
	private RegistreInteressatCanalEnum canalPreferent;
	@Column(name = "rep_doc_tipus")
	private RegistreInteressatDocumentTipusEnum representantDocumentTipus;
	@Column(name = "rep_doc_num", length = 17)
	private String representantDocumentNum;
	@Column(name = "rep_nom", length = 30)
	private String representantNom;
	@Column(name = "rep_llinatge1", length = 30)
	private String representantLlinatge1;
	@Column(name = "rep_llinatge2", length = 30)
	private String representantLlinatge2;
	@Column(name = "rep_rao_social", length = 80)
	private String representantRaoSocial;
	@Column(name = "rep_pais", length = 4)
	private String representantPais;
	@Column(name = "rep_provincia", length = 2)
	private String representantProvincia;
	@Column(name = "rep_municipi", length = 5)
	private String representantMunicipi;
	@Column(name = "rep_adresa", length = 160)
	private String representantAdresa;
	@Column(name = "rep_codi_postal", length = 5)
	private String representantCodiPostal;
	@Column(name = "rep_email", length = 160)
	private String representantEmail;
	@Column(name = "rep_telefon", length = 20)
	private String representantTelefon;
	@Column(name = "rep_email_hab", length = 160)
	private String representantEmailHabilitat;
	@Column(name = "rep_canal_pref")
	private RegistreInteressatCanalEnum representantCanalPreferent;
	@Column(name = "observacions", length = 160)
	private String observacions;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "registre_id")
	@ForeignKey(name = "ipa_interessat_registre_fk")
	protected RegistreEntity registre;
	@Version
	private long version = 0;



	public RegistreInteressatDocumentTipusEnum getDocumentTipus() {
		return documentTipus;
	}
	public String getDocumentNum() {
		return documentNum;
	}
	public String getNom() {
		return nom;
	}
	public String getLlinatge1() {
		return llinatge1;
	}
	public String getLlinatge2() {
		return llinatge2;
	}
	public String getRaoSocial() {
		return raoSocial;
	}
	public String getPais() {
		return pais;
	}
	public String getProvincia() {
		return provincia;
	}
	public String getMunicipi() {
		return municipi;
	}
	public String getAdresa() {
		return adresa;
	}
	public String getCodiPostal() {
		return codiPostal;
	}
	public String getEmail() {
		return email;
	}
	public String getTelefon() {
		return telefon;
	}
	public String getEmailHabilitat() {
		return emailHabilitat;
	}
	public RegistreInteressatCanalEnum getCanalPreferent() {
		return canalPreferent;
	}
	public RegistreInteressatDocumentTipusEnum getRepresentantDocumentTipus() {
		return representantDocumentTipus;
	}
	public String getRepresentantDocumentNum() {
		return representantDocumentNum;
	}
	public String getRepresentantNom() {
		return representantNom;
	}
	public String getRepresentantLlinatge1() {
		return representantLlinatge1;
	}
	public String getRepresentantLlinatge2() {
		return representantLlinatge2;
	}
	public String getRepresentantRaoSocial() {
		return representantRaoSocial;
	}
	public String getRepresentantPais() {
		return representantPais;
	}
	public String getRepresentantProvincia() {
		return representantProvincia;
	}
	public String getRepresentantMunicipi() {
		return representantMunicipi;
	}
	public String getRepresentantAdresa() {
		return representantAdresa;
	}
	public String getRepresentantCodiPostal() {
		return representantCodiPostal;
	}
	public String getRepresentantEmail() {
		return representantEmail;
	}
	public String getRepresentantTelefon() {
		return representantTelefon;
	}
	public String getRepresentantEmailHabilitat() {
		return representantEmailHabilitat;
	}
	public RegistreInteressatCanalEnum getRepresentantCanalPreferent() {
		return representantCanalPreferent;
	}
	public String getObservacions() {
		return observacions;
	}
	public long getVersion() {
		return version;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void updateDadesRepresentant(
			RegistreInteressatDocumentTipusEnum documentTipus,
			String documentNum,
			String nom,
			String llinatge1,
			String llinatge2,
			String raoSocial,
			String pais,
			String provincia,
			String municipi,
			String adresa,
			String codiPostal,
			String email,
			String telefon,
			String emailHabilitat,
			RegistreInteressatCanalEnum canalPreferent) {
		this.representantDocumentTipus = documentTipus;
		this.representantDocumentNum = documentNum;
		this.representantNom = nom;
		this.representantLlinatge1 = llinatge1;
		this.representantLlinatge2 = llinatge2;
		this.representantRaoSocial = raoSocial;
		this.representantPais = pais;
		this.representantProvincia = provincia;
		this.representantMunicipi = municipi;
		this.representantAdresa = adresa;
		this.representantCodiPostal = codiPostal;
		this.representantEmail = email;
		this.representantTelefon = telefon;
		this.representantEmailHabilitat = emailHabilitat;
		this.representantCanalPreferent = canalPreferent;
	}

	/**
	 * Obté el Builder per a crear objectes de tipus interessat d'una
	 * anotació de registre.
	 * 
	 * @param documentNum
	 *            El valor de l'atribut documentNum.
	 * @param nom
	 *            El valor de l'atribut nom.
	 * @param llinatge1
	 *            El valor de l'atribut llinatge1.
	 * @param llinatge2
	 *            El valor de l'atribut llinatge2.
	 * @param raoSocial
	 *            El valor de l'atribut raoSocial.
	 * @param pais
	 *            El valor de l'atribut pais.
	 * @param provincia
	 *            El valor de l'atribut provincia.
	 * @param municipi
	 *            El valor de l'atribut municipi.
	 * @param adresa
	 *            El valor de l'atribut adresa.
	 * @param codiPostal
	 *            El valor de l'atribut codiPostal.
	 * @param email
	 *            El valor de l'atribut email.
	 * @param telefon
	 *            El valor de l'atribut telefon.
	 * @param emailHabilitat
	 *            El valor de l'atribut emailHabilitat.
	 * @param canalPreferent
	 *            El valor de l'atribut canalPreferent.
	 * @param observacions
	 *            El valor de l'atribut observacions.
	 * @param registre
	 *            El valor de l'atribut registre.
	 * @return Una nova instància del Builder.
	 */
	public static Builder getBuilder(
			RegistreInteressatDocumentTipusEnum documentTipus,
			String documentNum,
			String nom,
			String llinatge1,
			String llinatge2,
			String raoSocial,
			String pais,
			String provincia,
			String municipi,
			String adresa,
			String codiPostal,
			String email,
			String telefon,
			String emailHabilitat,
			RegistreInteressatCanalEnum canalPreferent,
			String observacions,
			RegistreEntity registre) {
		return new Builder(
				documentTipus,
				documentNum,
				nom,
				llinatge1,
				llinatge2,
				raoSocial,
				pais,
				provincia,
				municipi,
				adresa,
				codiPostal,
				email,
				telefon,
				emailHabilitat,
				canalPreferent,
				observacions,
				registre);
	}

	/**
	 * Builder per a crear noves instàncies d'aquesta classe.
	 * 
	 * @author Limit Tecnologies <limit@limit.es>
	 */
	public static class Builder {
		RegistreInteressatEntity built;
		Builder(
				RegistreInteressatDocumentTipusEnum documentTipus,
				String documentNum,
				String nom,
				String llinatge1,
				String llinatge2,
				String raoSocial,
				String pais,
				String provincia,
				String municipi,
				String adresa,
				String codiPostal,
				String email,
				String telefon,
				String emailHabilitat,
				RegistreInteressatCanalEnum canalPreferent,
				String observacions,
				RegistreEntity registre) {
			built = new RegistreInteressatEntity();
			built.documentTipus = documentTipus;
			built.documentNum = documentNum;
			built.nom = nom;
			built.llinatge1 = llinatge1;
			built.llinatge2 = llinatge2;
			built.raoSocial = raoSocial;
			built.pais = pais;
			built.provincia = provincia;
			built.municipi = municipi;
			built.adresa = adresa;
			built.codiPostal = codiPostal;
			built.email = email;
			built.telefon = telefon;
			built.emailHabilitat = emailHabilitat;
			built.canalPreferent = canalPreferent;
		}
		public RegistreInteressatEntity build() {
			return built;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((documentNum == null) ? 0 : documentNum.hashCode());
		result = prime * result
				+ ((documentTipus == null) ? 0 : documentTipus.hashCode());
		result = prime * result
				+ ((llinatge1 == null) ? 0 : llinatge1.hashCode());
		result = prime * result
				+ ((llinatge2 == null) ? 0 : llinatge2.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		result = prime * result
				+ ((raoSocial == null) ? 0 : raoSocial.hashCode());
		result = prime * result
				+ ((registre == null) ? 0 : registre.hashCode());
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
		RegistreInteressatEntity other = (RegistreInteressatEntity) obj;
		if (documentNum == null) {
			if (other.documentNum != null)
				return false;
		} else if (!documentNum.equals(other.documentNum))
			return false;
		if (documentTipus != other.documentTipus)
			return false;
		if (llinatge1 == null) {
			if (other.llinatge1 != null)
				return false;
		} else if (!llinatge1.equals(other.llinatge1))
			return false;
		if (llinatge2 == null) {
			if (other.llinatge2 != null)
				return false;
		} else if (!llinatge2.equals(other.llinatge2))
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		if (raoSocial == null) {
			if (other.raoSocial != null)
				return false;
		} else if (!raoSocial.equals(other.raoSocial))
			return false;
		if (registre == null) {
			if (other.registre != null)
				return false;
		} else if (!registre.equals(other.registre))
			return false;
		return true;
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
