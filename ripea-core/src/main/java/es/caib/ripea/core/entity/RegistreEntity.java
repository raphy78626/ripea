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

import es.caib.ripea.core.api.dto.RegistreDocumentacioFisicaTipusEnumDto;
import es.caib.ripea.core.api.dto.RegistreTipusEnumDto;
import es.caib.ripea.core.api.dto.RegistreTransportTipusEnumDto;
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
				@UniqueConstraint(
						name = "ipa_reg_mult_uk",
						columnNames = {
								"tipus",
								"identificador",
								"oficina",
								"llibre",
								"data"})})
@EntityListeners(RipeaAuditingEntityListener.class)
public class RegistreEntity extends RipeaAuditable<Long> {

	@Column(name = "tipus", length = 1, nullable = false)
	private String tipus;
	@Column(name = "numero", nullable = false)
	private int numero;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data")
	private Date data;
	@Column(name = "identificador")
	private String identificador;
	@Column(name = "oficina", length = 21, nullable = false)
	private String oficina;
	@Column(name = "llibre", length = 4, nullable = false)
	private String llibre;
	@Column(name = "extracte", length = 240)
	private String extracte;
	@Column(name = "assumpte_tipus", length = 16, nullable = false)
	private String assumpteTipus;
	@Column(name = "assumpte_codi", length = 16)
	private String assumpteCodi;
	@Column(name = "assumpte_ref", length = 16)
	private String assumpteReferencia;
	@Column(name = "assumpte_numexp", length = 80)
	private String assumpteNumExpedient;
	@Column(name = "idioma", length = 2, nullable = false)
	private String idioma;
	@Column(name = "transport_tipus", length = 2)
	private String transportTipus;
	@Column(name = "transport_num", length = 20)
	private String transportNumero;
	@Column(name = "usuari_nom", length = 80, nullable = false)
	private String usuariNom;
	@Column(name = "usuari_contacte", length = 160)
	private String usuariContacte;
	@Column(name = "aplicacio_codi", length = 20)
	private String aplicacioCodi;
	@Column(name = "aplicacio_versio", length = 15)
	private String aplicacioVersio;
	@Column(name = "documentacio_fis", length = 1, nullable = false)
	private String documentacioFisica;
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
			fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private List<RegistreInteressatEntity> interessats = new ArrayList<RegistreInteressatEntity>();
	@OneToMany(
			mappedBy = "registre",
			fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private List<RegistreAnnexEntity> annexos = new ArrayList<RegistreAnnexEntity>();
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "contenidor_id")
	@ForeignKey(name = "ipa_contenidor_registre_fk")
	private ContenidorEntity contenidor;
	@Version
	private long version = 0;



	public RegistreTipusEnumDto getTipus() {
		return RegistreTipusEnumDto.valorAsEnum(tipus);
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
	public String getOficina() {
		return oficina;
	}
	public String getLlibre() {
		return llibre;
	}
	public String getExtracte() {
		return extracte;
	}
	public String getAssumpteTipus() {
		return assumpteTipus;
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
	public String getIdioma() {
		return idioma;
	}
	public RegistreTransportTipusEnumDto getTransportTipus() {
		return RegistreTransportTipusEnumDto.valorAsEnum(transportTipus);
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
	public RegistreDocumentacioFisicaTipusEnumDto getDocumentacioFisica() {
		return RegistreDocumentacioFisicaTipusEnumDto.valorAsEnum(documentacioFisica);
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
			RegistreTipusEnumDto tipus,
			int numero,
			Date data,
			String identificador,
			String oficina,
			String llibre,
			String assumpteTipus,
			String idioma,
			String usuariNom,
			String documentacioFisica) {
		return new Builder(
				tipus,
				numero,
				data,
				identificador,
				oficina,
				llibre,
				assumpteTipus,
				idioma,
				usuariNom,
				documentacioFisica);
	}

	/**
	 * Builder per a crear noves instàncies d'aquesta classe.
	 * 
	 * @author Limit Tecnologies <limit@limit.es>
	 */
	public static class Builder {
		RegistreEntity built;
		Builder(
				RegistreTipusEnumDto tipus,
				int numero,
				Date data,
				String identificador,
				String oficina,
				String llibre,
				String assumpteTipus,
				String idioma,
				String usuariNom,
				String documentacioFisica) {
			built = new RegistreEntity();
			built.tipus = tipus.getValor();
			built.numero = numero;
			built.data = data;
			built.identificador = identificador;
			built.oficina = oficina;
			built.llibre = llibre;
			built.assumpteTipus = assumpteTipus;
			built.idioma = idioma;
			built.usuariNom = usuariNom;
			built.documentacioFisica = documentacioFisica;
		}
		public Builder extracte(String extracte) {
			built.extracte = extracte;
			return this;
		}
		public Builder assumpteCodi(String assumpteCodi) {
			built.assumpteCodi = assumpteCodi;
			return this;
		}
		public Builder assumpteReferencia(String assumpteReferencia) {
			built.assumpteReferencia = assumpteReferencia;
			return this;
		}
		public Builder assumpteNumExpedient(String assumpteNumExpedient) {
			built.assumpteNumExpedient = assumpteNumExpedient;
			return this;
		}
		public Builder transportTipus(RegistreTransportTipusEnumDto transportTipus) {
			built.transportTipus = transportTipus.getValor();
			return this;
		}
		public Builder transportNumero(String transportNumero) {
			built.transportNumero = transportNumero;
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
		public Builder contenidor(ContenidorEntity contenidor) {
			built.contenidor = contenidor;
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
		result = prime * result + ((llibre == null) ? 0 : llibre.hashCode());
		result = prime * result + ((oficina == null) ? 0 : oficina.hashCode());
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
		if (llibre == null) {
			if (other.llibre != null)
				return false;
		} else if (!llibre.equals(other.llibre))
			return false;
		if (oficina == null) {
			if (other.oficina != null)
				return false;
		} else if (!oficina.equals(other.oficina))
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
