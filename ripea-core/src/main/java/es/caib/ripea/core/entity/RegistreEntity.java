/**
 * 
 */
package es.caib.ripea.core.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.ForeignKey;

import es.caib.ripea.core.audit.RipeaAuditable;
import es.caib.ripea.core.audit.RipeaAuditingEntityListener;

/**
 * Classe del model de dades que representa una anotació al
 * registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(	name = "ipa_registre",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {
						"entitat_id",
						"accio",
						"tipus",
						"entitat_codi",
						"numero",
						"data"})})
@EntityListeners(RipeaAuditingEntityListener.class)
public class RegistreEntity extends RipeaAuditable<Long> {

	@Column(name = "accio", nullable = false)
	private RegistreAccioEnum accio;
	@Column(name = "tipus", nullable = false)
	private RegistreTipusEnum tipus;
	@Column(name = "entitat_codi", length = 21, nullable = false)
	private String entitatCodi;
	@Column(name = "entitat_nom", length = 80)
	private String entitatNom;
	@Column(name = "numero", length = 20, nullable = false)
	private String numero;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data", nullable = false)
	private Date data;
	@Column(name = "assumpte_resum", length = 240, nullable = false)
	private String assumpteResum;
	@Column(name = "assumpte_codi", length = 16)
	private String assumpteCodi;
	@Column(name = "assumpte_referencia", length = 16)
	private String assumpteReferencia;
	@Column(name = "assumpte_numexp", length = 80)
	private String assumpteNumExpedient;
	@Column(name = "transport_tipus")
	private RegistreTransportTipusEnum transportTipus;
	@Column(name = "transport_num", length = 20)
	private String transportNumero;
	@Column(name = "usuari_nom", length = 80)
	private String usuariNom;
	@Column(name = "usuari_contacte", length = 160)
	private String usuariContacte;
	@Column(name = "aplicacio_emissora", length = 4)
	private String aplicacioEmissora;
	@Column(name = "documentacio_fis")
	private RegistreDocumentacioFisicaTipusEnum documentacioFisica;
	@Column(name = "observacions", length = 50)
	private String observacions;
	@Column(name = "prova")
	private boolean prova;
	@Column(name = "motiu_rebuig", length = 1024)
	private String motiuRebuig;
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "contenidor_id")
	@ForeignKey(name = "ipa_contenidor_registre_fk")
	private ContenidorEntity contenidor;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "entitat_id")
	@ForeignKey(name = "ipa_entitat_registre_fk")
	protected EntitatEntity entitat;
	@OneToMany(
			mappedBy = "registre",
			fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private List<RegistreInteressatEntity> interessats = new ArrayList<RegistreInteressatEntity>();
	@OneToMany(
			mappedBy = "registre",
			fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private List<RegistreDocumentEntity> documents = new ArrayList<RegistreDocumentEntity>();
	@Version
	private long version = 0;



	public RegistreAccioEnum getAccio() {
		return accio;
	}
	public RegistreTipusEnum getTipus() {
		return tipus;
	}
	public String getEntitatCodi() {
		return entitatCodi;
	}
	public String getEntitatNom() {
		return entitatNom;
	}
	public String getNumero() {
		return numero;
	}
	public Date getData() {
		return data;
	}
	public String getAssumpteResum() {
		return assumpteResum;
	}
	public String getAssumpteCodi() {
		return assumpteCodi;
	}
	public String getAssumpteReferencia() {
		return assumpteReferencia;
	}
	public String getAssumpteNumExpedient() {
		return assumpteNumExpedient;
	}
	public RegistreTransportTipusEnum getTransportTipus() {
		return transportTipus;
	}
	public String getTransportNumero() {
		return transportNumero;
	}
	public String getUsuariNom() {
		return usuariNom;
	}
	public String getUsuariContacte() {
		return usuariContacte;
	}
	public String getAplicacioEmissora() {
		return aplicacioEmissora;
	}
	public RegistreDocumentacioFisicaTipusEnum getDocumentacioFisica() {
		return documentacioFisica;
	}
	public String getObservacions() {
		return observacions;
	}
	public boolean isProva() {
		return prova;
	}
	public ContenidorEntity getContenidor() {
		return contenidor;
	}
	public EntitatEntity getEntitat() {
		return entitat;
	}
	public List<RegistreInteressatEntity> getInteressats() {
		return interessats;
	}
	public List<RegistreDocumentEntity> getDocuments() {
		return documents;
	}
	public long getVersion() {
		return version;
	}

	public void updateContenidor(ContenidorEntity contenidor) {
		this.contenidor = contenidor;
	}

	public void updateMotiuRebuig(String motiuRebuig) {
		this.motiuRebuig = motiuRebuig;
	}

	public void addDocument(RegistreDocumentEntity document) {
		this.documents.add(document);
	}
	public void addInteressat(RegistreInteressatEntity interessat) {
		this.interessats.add(interessat);
	}

	/**
	 * Obté el Builder per a crear objectes de tipus registre.
	 * 
	 * @param accio
	 *            El valor de l'atribut accio.
	 * @param tipus
	 *            El valor de l'atribut tipus.
	 * @param entitatCodi
	 *            El valor de l'atribut entitatCodi.
	 * @param entitatNom
	 *            El valor de l'atribut entitatNom.
	 * @param numero
	 *            El valor de l'atribut numero.
	 * @param data
	 *            El valor de l'atribut data.
	 * @param assumpteResum
	 *            El valor de l'atribut assumpteResum.
	 * @param assumpteCodi
	 *            El valor de l'atribut assumpteCodi.
	 * @param assumpteReferencia
	 *            El valor de l'atribut assumpteReferencia.
	 * @param assumpteNumExpedient
	 *            El valor de l'atribut assumpteNumExpedient.
	 * @param transportTipus
	 *            El valor de l'atribut transportTipus.
	 * @param transportNumero
	 *            El valor de l'atribut transportNumero.
	 * @param usuariNom
	 *            El valor de l'atribut usuariNom.
	 * @param usuariContacte
	 *            El valor de l'atribut usuariContacte.
	 * @param aplicacioEmissora
	 *            El valor de l'atribut aplicacioEmissora.
	 * @param documentacioFisica
	 *            El valor de l'atribut documentacioFisica.
	 * @param observacions
	 *            El valor de l'atribut observacions.
	 * @param entitat
	 *            El valor de l'atribut entitat.
	 * @param prova
	 *            El valor de l'atribut prova.
	 * @return Una nova instància del Builder.
	 */
	public static Builder getBuilder(
			RegistreAccioEnum accio,
			RegistreTipusEnum tipus,
			String entitatCodi,
			String entitatNom,
			String numero,
			Date data,
			String assumpteResum,
			String assumpteCodi,
			String assumpteReferencia,
			String assumpteNumExpedient,
			RegistreTransportTipusEnum transportTipus,
			String transportNumero,
			String usuariNom,
			String usuariContacte,
			String aplicacioEmissora,
			RegistreDocumentacioFisicaTipusEnum documentacioFisica,
			String observacions,
			EntitatEntity entitat,
			boolean prova) {
		return new Builder(
				accio,
				tipus,
				entitatCodi,
				entitatNom,
				numero,
				data,
				assumpteResum,
				assumpteCodi,
				assumpteReferencia,
				assumpteNumExpedient,
				transportTipus,
				transportNumero,
				usuariNom,
				usuariContacte,
				aplicacioEmissora,
				documentacioFisica,
				observacions,
				entitat,
				prova);
	}

	/**
	 * Builder per a crear noves instàncies d'aquesta classe.
	 * 
	 * @author Limit Tecnologies <limit@limit.es>
	 */
	public static class Builder {
		RegistreEntity built;
		Builder(
				RegistreAccioEnum accio,
				RegistreTipusEnum tipus,
				String entitatCodi,
				String entitatNom,
				String numero,
				Date data,
				String assumpteResum,
				String assumpteCodi,
				String assumpteReferencia,
				String assumpteNumExpedient,
				RegistreTransportTipusEnum transportTipus,
				String transportNumero,
				String usuariNom,
				String usuariContacte,
				String aplicacioEmissora,
				RegistreDocumentacioFisicaTipusEnum documentacioFisica,
				String observacions,
				EntitatEntity entitat,
				boolean prova) {
			built = new RegistreEntity();
			built.accio = accio;
			built.tipus = tipus;
			built.entitatCodi = entitatCodi;
			built.entitatNom = entitatNom;
			built.numero = numero;
			built.data = data;
			built.assumpteResum = assumpteResum;
			built.assumpteCodi = assumpteCodi;
			built.assumpteReferencia = assumpteReferencia;
			built.assumpteNumExpedient = assumpteNumExpedient;
			built.transportTipus = transportTipus;
			built.transportNumero = transportNumero;
			built.usuariNom = usuariNom;
			built.usuariContacte = usuariContacte;
			built.aplicacioEmissora = aplicacioEmissora;
			built.documentacioFisica = documentacioFisica;
			built.observacions = observacions;
			built.entitat = entitat;
			built.prova = prova;
		}
		public RegistreEntity build() {
			return built;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((accio == null) ? 0 : accio.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((entitat == null) ? 0 : entitat.hashCode());
		result = prime * result
				+ ((entitatCodi == null) ? 0 : entitatCodi.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((tipus == null) ? 0 : tipus.hashCode());
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
		RegistreEntity other = (RegistreEntity) obj;
		if (accio != other.accio)
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (entitat == null) {
			if (other.entitat != null)
				return false;
		} else if (!entitat.equals(other.entitat))
			return false;
		if (entitatCodi == null) {
			if (other.entitatCodi != null)
				return false;
		} else if (!entitatCodi.equals(other.entitatCodi))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		if (tipus != other.tipus)
			return false;
		return true;
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
