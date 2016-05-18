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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.api.registre.RegistreTipusEnum;
import es.caib.ripea.core.audit.RipeaAuditable;

/**
 * Classe del model de dades que representa una anotació al
 * registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(	name = "ipa_registre",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "ipa_reg_mult_uk",
						columnNames = {
								"tipus",
								"unitat_adm",
								"numero",
								"data",
								"oficina",
								"llibre"})})
@EntityListeners(AuditingEntityListener.class)
public class RegistreEntity extends RipeaAuditable<Long> {

	@Column(name = "tipus", length = 1, nullable = false)
	private String tipus;
	@Column(name = "unitat_adm", length = 21, nullable = false)
	private String unitatAdministrativa;
	@Column(name = "numero", nullable = false)
	private int numero;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data", nullable = false)
	private Date data;
	@Column(name = "identificador", length = 100, nullable = false)
	private String identificador;
	@Column(name = "entitat_codi", length = 21, nullable = false)
	private String entitatCodi;
	@Column(name = "entitat_desc", length = 100)
	private String entitatDescripcio;
	@Column(name = "oficina_codi", length = 21, nullable = false)
	private String oficinaCodi;
	@Column(name = "oficina_desc", length = 100)
	private String oficinaDescripcio;
	@Column(name = "llibre_codi", length = 4, nullable = false)
	private String llibreCodi;
	@Column(name = "llibre_desc", length = 100)
	private String llibreDescripcio;
	@Column(name = "extracte", length = 240)
	private String extracte;
	@Column(name = "assumpte_tipus_codi", length = 16, nullable = false)
	private String assumpteTipusCodi;
	@Column(name = "assumpte_tipus_desc", length = 100)
	private String assumpteTipusDescripcio;
	@Column(name = "assumpte_codi", length = 16)
	private String assumpteCodi;
	@Column(name = "assumpte_desc", length = 100)
	private String assumpteDescripcio;
	@Column(name = "referencia", length = 16)
	private String referencia;
	@Column(name = "expedient_num", length = 80)
	private String expedientNumero;
	@Column(name = "idioma_codi", length = 2, nullable = false)
	private String idiomaCodi;
	@Column(name = "idioma_desc", length = 100)
	private String idiomaDescripcio;
	@Column(name = "transport_tipus_codi", length = 2)
	private String transportTipusCodi;
	@Column(name = "transport_tipus_desc", length = 100)
	private String transportTipusDescripcio;
	@Column(name = "transport_num", length = 20)
	private String transportNumero;
	@Column(name = "usuari_codi", length = 20)
	private String usuariCodi;
	@Column(name = "usuari_nom", length = 80)
	private String usuariNom;
	@Column(name = "usuari_contacte", length = 160)
	private String usuariContacte;
	@Column(name = "aplicacio_codi", length = 20)
	private String aplicacioCodi;
	@Column(name = "aplicacio_versio", length = 15)
	private String aplicacioVersio;
	@Column(name = "docfis_codi", length = 1)
	private String documentacioFisicaCodi;
	@Column(name = "docfis_desc", length = 100)
	private String documentacioFisicaDescripcio;
	@Column(name = "observacions", length = 50)
	private String observacions;
	@Column(name = "exposa", length = 4000)
	private String exposa;
	@Column(name = "solicita", length = 4000)
	private String solicita;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_processat")
	private Date dataProcessat;
	@Column(name = "motiu_rebuig", length = 1024)
	private String motiuRebuig;
	@OneToMany(
			mappedBy = "registre",
			fetch = FetchType.EAGER,
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private List<RegistreInteressatEntity> interessats = new ArrayList<RegistreInteressatEntity>();
	@OneToMany(
			mappedBy = "registre",
			fetch = FetchType.EAGER,
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private List<RegistreAnnexEntity> annexos = new ArrayList<RegistreAnnexEntity>();
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "contenidor_id")
	@ForeignKey(name = "ipa_contenidor_registre_fk")
	private ContenidorEntity contenidor;
	@Version
	private long version = 0;



	public RegistreTipusEnum getTipus() {
		return RegistreTipusEnum.valorAsEnum(tipus);
	}
	public String getUnitatAdministrativa() {
		return unitatAdministrativa;
	}
	public int getNumero() {
		return numero;
	}
	public Date getData() {
		return data;
	}
	public String getIdentificador() {
		return identificador;
	}
	public String getEntitatCodi() {
		return entitatCodi;
	}
	public String getEntitatDescripcio() {
		return entitatDescripcio;
	}
	public String getOficinaCodi() {
		return oficinaCodi;
	}
	public String getOficinaDescripcio() {
		return oficinaDescripcio;
	}
	public String getLlibreCodi() {
		return llibreCodi;
	}
	public String getLlibreDescripcio() {
		return llibreDescripcio;
	}
	public String getExtracte() {
		return extracte;
	}
	public String getAssumpteTipusCodi() {
		return assumpteTipusCodi;
	}
	public String getAssumpteTipusDescripcio() {
		return assumpteTipusDescripcio;
	}
	public String getAssumpteCodi() {
		return assumpteCodi;
	}
	public String getAssumpteDescripcio() {
		return assumpteDescripcio;
	}
	public String getReferencia() {
		return referencia;
	}
	public String getExpedientNumero() {
		return expedientNumero;
	}
	public String getIdiomaCodi() {
		return idiomaCodi;
	}
	public String getIdiomaDescripcio() {
		return idiomaDescripcio;
	}
	public String getTransportTipusCodi() {
		return transportTipusCodi;
	}
	public String getTransportTipusDescripcio() {
		return transportTipusDescripcio;
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
	public String getAplicacioCodi() {
		return aplicacioCodi;
	}
	public String getAplicacioVersio() {
		return aplicacioVersio;
	}
	public String getDocumentacioFisicaCodi() {
		return documentacioFisicaCodi;
	}
	public String getDocumentacioFisicaDescripcio() {
		return documentacioFisicaDescripcio;
	}
	public String getObservacions() {
		return observacions;
	}
	public String getExposa() {
		return exposa;
	}
	public String getSolicita() {
		return solicita;
	}
	public Date getDataProcessat() {
		return dataProcessat;
	}
	public String getMotiuRebuig() {
		return motiuRebuig;
	}
	public List<RegistreInteressatEntity> getInteressats() {
		return interessats;
	}
	public List<RegistreAnnexEntity> getAnnexos() {
		return annexos;
	}
	public ContenidorEntity getContenidor() {
		return contenidor;
	}
	public void updateContenidor(ContenidorEntity contenidor) {
		this.contenidor = contenidor;
	}
	public void updateMotiuRebuig(String motiuRebuig) {
		this.motiuRebuig = motiuRebuig;
	}

	public static Builder getBuilder(
			RegistreTipusEnum tipus,
			String unitatAdministrativa,
			int numero,
			Date data,
			String identificador,
			String oficinaCodi,
			String llibreCodi,
			String assumpteTipusCodi,
			String idiomaCodi,
			ContenidorEntity contenidor) {
		return new Builder(
				tipus,
				unitatAdministrativa,
				numero,
				data,
				identificador,
				oficinaCodi,
				llibreCodi,
				assumpteTipusCodi,
				idiomaCodi,
				contenidor);
	}

	/**
	 * Builder per a crear noves instàncies d'aquesta classe.
	 * 
	 * @author Limit Tecnologies <limit@limit.es>
	 */
	public static class Builder {
		RegistreEntity built;
		Builder(
				RegistreTipusEnum tipus,
				String unitatAdministrativa,
				int numero,
				Date data,
				String identificador,
				String oficinaCodi,
				String llibreCodi,
				String assumpteTipusCodi,
				String idiomaCodi,
				ContenidorEntity contenidor) {
			built = new RegistreEntity();
			built.tipus = tipus.getValor();
			built.unitatAdministrativa = unitatAdministrativa;
			built.numero = numero;
			built.data = data;
			built.identificador = identificador;
			built.oficinaCodi = oficinaCodi;
			built.llibreCodi = llibreCodi;
			built.assumpteTipusCodi = assumpteTipusCodi;
			built.idiomaCodi = idiomaCodi;
			built.contenidor = contenidor;
		}
		public Builder entitatCodi(String entitatCodi) {
			built.entitatCodi = entitatCodi;
			return this;
		}
		public Builder entitatDescripcio(String entitatDescripcio) {
			built.entitatDescripcio = entitatDescripcio;
			return this;
		}
		public Builder oficinaDescripcio(String oficinaDescripcio) {
			built.oficinaDescripcio = oficinaDescripcio;
			return this;
		}
		public Builder llibreDescripcio(String llibreDescripcio) {
			built.llibreDescripcio = llibreDescripcio;
			return this;
		}
		public Builder extracte(String extracte) {
			built.extracte = extracte;
			return this;
		}
		public Builder assumpteTipusDescripcio(String assumpteTipusDescripcio) {
			built.assumpteTipusDescripcio = assumpteTipusDescripcio;
			return this;
		}
		public Builder assumpteCodi(String assumpteCodi) {
			built.assumpteCodi = assumpteCodi;
			return this;
		}
		public Builder assumpteDescripcio(String assumpteDescripcio) {
			built.assumpteDescripcio = assumpteDescripcio;
			return this;
		}
		public Builder referencia(String referencia) {
			built.referencia = referencia;
			return this;
		}
		public Builder expedientNumero(String expedientNumero) {
			built.expedientNumero = expedientNumero;
			return this;
		}
		public Builder idiomaDescripcio(String idiomaDescripcio) {
			built.idiomaDescripcio = idiomaDescripcio;
			return this;
		}
		public Builder transportTipusCodi(String transportTipusCodi) {
			built.transportTipusCodi = transportTipusCodi;
			return this;
		}
		public Builder transportTipusDescripcio(String transportTipusDescripcio) {
			built.transportTipusDescripcio = transportTipusDescripcio;
			return this;
		}
		public Builder transportNumero(String transportNumero) {
			built.transportNumero = transportNumero;
			return this;
		}
		public Builder usuariCodi(String usuariCodi) {
			built.usuariCodi = usuariCodi;
			return this;
		}
		public Builder usuariNom(String usuariNom) {
			built.usuariNom = usuariNom;
			return this;
		}
		public Builder usuariContacte(String usuariContacte) {
			built.usuariContacte = usuariContacte;
			return this;
		}
		public Builder aplicacioCodi(String aplicacioCodi) {
			built.aplicacioCodi = aplicacioCodi;
			return this;
		}
		public Builder aplicacioVersio(String aplicacioVersio) {
			built.aplicacioVersio = aplicacioVersio;
			return this;
		}
		public Builder documentacioFisicaCodi(String documentacioFisicaCodi) {
			built.documentacioFisicaCodi = documentacioFisicaCodi;
			return this;
		}
		public Builder documentacioFisicaDescripcio(String documentacioFisicaDescripcio) {
			built.documentacioFisicaDescripcio = documentacioFisicaDescripcio;
			return this;
		}
		public Builder observacions(String observacions) {
			built.observacions = observacions;
			return this;
		}
		public Builder exposa(String exposa) {
			built.exposa = exposa;
			return this;
		}
		public Builder solicita(String solicita) {
			built.solicita = solicita;
			return this;
		}
		public Builder dataProcessat(Date dataProcessat) {
			built.dataProcessat = dataProcessat;
			return this;
		}
		public Builder motiuRebuig(String motiuRebuig) {
			built.motiuRebuig = motiuRebuig;
			return this;
		}
		public RegistreEntity build() {
			return built;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((identificador == null) ? 0 : identificador.hashCode());
		result = prime * result + ((llibreCodi == null) ? 0 : llibreCodi.hashCode());
		result = prime * result + ((oficinaCodi == null) ? 0 : oficinaCodi.hashCode());
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
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (identificador == null) {
			if (other.identificador != null)
				return false;
		} else if (!identificador.equals(other.identificador))
			return false;
		if (llibreCodi == null) {
			if (other.llibreCodi != null)
				return false;
		} else if (!llibreCodi.equals(other.llibreCodi))
			return false;
		if (oficinaCodi == null) {
			if (other.oficinaCodi != null)
				return false;
		} else if (!oficinaCodi.equals(other.oficinaCodi))
			return false;
		if (tipus == null) {
			if (other.tipus != null)
				return false;
		} else if (!tipus.equals(other.tipus))
			return false;
		return true;
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
