/**
 * 
 */
package es.caib.ripea.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.ForeignKey;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.api.registre.RegistreAnnexElaboracioEstatEnum;
import es.caib.ripea.core.api.registre.RegistreAnnexNtiTipusDocumentEnum;
import es.caib.ripea.core.api.registre.RegistreAnnexOrigenEnum;
import es.caib.ripea.core.api.registre.RegistreAnnexSicresTipusDocumentEnum;
import es.caib.ripea.core.audit.RipeaAuditable;

/**
 * Classe del model de dades que representa un document
 * d'una anotació al registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(	name = "ipa_registre_annex",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "ipa_reganx_mult_uk",
						columnNames = {
								"registre_id",
								"titol",
								"fitxer_nom",
								"fitxer_tamany",
								"fitxer_gesdoc_id",
								"data_captura",
								"origen_ciuadm",
								"nti_tipus_doc",
								"sicres_tipus_doc"})})
@EntityListeners(AuditingEntityListener.class)
public class RegistreAnnexEntity extends RipeaAuditable<Long> {

	@Column(name = "titol", length = 200, nullable = false)
	private String titol;
	@Column(name = "fitxer_nom", length = 80, nullable = false)
	private String fitxerNom;
	@Column(name = "fitxer_tamany", nullable = false)
	private int fitxerTamany;
	@Column(name = "fitxer_mime", length = 30)
	private String fitxerTipusMime;
	@Column(name = "fitxer_gesdoc_id", length = 100, nullable = false)
	private String fitxerGestioDocumentalId;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_captura", nullable = false)
	private Date dataCaptura;
	@Column(name = "localitzacio", length = 80)
	private String localitzacio;
	@Column(name = "origen_ciuadm", length = 1, nullable = false)
	private String origenCiutadaAdmin;
	@Column(name = "nti_tipus_doc", length = 4, nullable = false)
	private String ntiTipusDocument;
	@Column(name = "sicres_tipus_doc", length = 2, nullable = false)
	private String sicresTipusDocument;
	@Column(name = "nti_estat_elab", length = 2)
	private String estatElaboracio;
	@Column(name = "firma_csv", length = 50)
	private String firmaCsv;
	@Column(name = "observacions", length = 50)
	private String observacions;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "registre_id")
	@ForeignKey(name = "ipa_annex_registre_fk")
	private RegistreEntity registre;
	@Version
	private long version = 0;



	public String getTitol() {
		return titol;
	}
	public String getFitxerNom() {
		return fitxerNom;
	}
	public int getFitxerTamany() {
		return fitxerTamany;
	}
	public String getFitxerTipusMime() {
		return fitxerTipusMime;
	}
	public String getFitxerGestioDocumentalId() {
		return fitxerGestioDocumentalId;
	}
	public Date getDataCaptura() {
		return dataCaptura;
	}
	public String getLocalitzacio() {
		return localitzacio;
	}
	public RegistreAnnexOrigenEnum getOrigenCiutadaAdmin() {
		return RegistreAnnexOrigenEnum.valorAsEnum(origenCiutadaAdmin);
	}
	public RegistreAnnexNtiTipusDocumentEnum getNtiTipusDocument() {
		return RegistreAnnexNtiTipusDocumentEnum.valorAsEnum(ntiTipusDocument);
	}
	public RegistreAnnexSicresTipusDocumentEnum getSicresTipusDocument() {
		return RegistreAnnexSicresTipusDocumentEnum.valorAsEnum(sicresTipusDocument);
	}
	public RegistreAnnexElaboracioEstatEnum getEstatElaboracio() {
		return RegistreAnnexElaboracioEstatEnum.valorAsEnum(estatElaboracio);
	}
	public String getFirmaCsv() {
		return firmaCsv;
	}
	public String getObservacions() {
		return observacions;
	}
	public RegistreEntity getRegistre() {
		return registre;
	}

	public static Builder getBuilder(
			String titol,
			String fitxerNom,
			int fitxerTamany,
			String fitxerGestioDocumentalId,
			Date dataCaptura,
			RegistreAnnexOrigenEnum origenCiutadaAdmin,
			RegistreAnnexNtiTipusDocumentEnum ntiTipusDocument,
			RegistreAnnexSicresTipusDocumentEnum sicresTipusDocument,
			RegistreEntity registre) {
		return new Builder(
				titol,
				fitxerNom,
				fitxerTamany,
				fitxerGestioDocumentalId,
				dataCaptura,
				origenCiutadaAdmin,
				ntiTipusDocument,
				sicresTipusDocument,
				registre);
	}
	public static class Builder {
		RegistreAnnexEntity built;
		Builder(
				String titol,
				String fitxerNom,
				int fitxerTamany,
				String fitxerGestioDocumentalId,
				Date dataCaptura,
				RegistreAnnexOrigenEnum origenCiutadaAdmin,
				RegistreAnnexNtiTipusDocumentEnum ntiTipusDocument,
				RegistreAnnexSicresTipusDocumentEnum sicresTipusDocument,
				RegistreEntity registre) {
			built = new RegistreAnnexEntity();
			built.titol = titol;
			built.fitxerNom = fitxerNom;
			built.fitxerTamany = fitxerTamany;
			built.fitxerGestioDocumentalId = fitxerGestioDocumentalId;
			built.dataCaptura = dataCaptura;
			if (origenCiutadaAdmin != null)
				built.origenCiutadaAdmin = origenCiutadaAdmin.getValor();
			if (ntiTipusDocument != null)
				built.ntiTipusDocument = ntiTipusDocument.getValor();
			if (sicresTipusDocument != null)
				built.sicresTipusDocument = sicresTipusDocument.getValor();
			built.registre = registre;
		}
		public Builder fitxerTipusMime(String fitxerTipusMime) {
			built.fitxerTipusMime = fitxerTipusMime;
			return this;
		}
		public Builder localitzacio(String localitzacio) {
			built.localitzacio = localitzacio;
			return this;
		}
		public Builder estatElaboracio(RegistreAnnexElaboracioEstatEnum estatElaboracio) {
			if (estatElaboracio != null)
				built.estatElaboracio = estatElaboracio.getValor();
			return this;
		}
		public Builder firmaCsv(String firmaCsv) {
			built.firmaCsv = firmaCsv;
			return this;
		}
		public Builder observacions(String observacions) {
			built.observacions = observacions;
			return this;
		}
		public RegistreAnnexEntity build() {
			return built;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((dataCaptura == null) ? 0 : dataCaptura.hashCode());
		result = prime * result + ((fitxerGestioDocumentalId == null) ? 0 : fitxerGestioDocumentalId.hashCode());
		result = prime * result + ((fitxerNom == null) ? 0 : fitxerNom.hashCode());
		result = prime * result + fitxerTamany;
		result = prime * result + ((ntiTipusDocument == null) ? 0 : ntiTipusDocument.hashCode());
		result = prime * result + ((origenCiutadaAdmin == null) ? 0 : origenCiutadaAdmin.hashCode());
		result = prime * result + ((registre == null) ? 0 : registre.hashCode());
		result = prime * result + ((sicresTipusDocument == null) ? 0 : sicresTipusDocument.hashCode());
		result = prime * result + ((titol == null) ? 0 : titol.hashCode());
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
		RegistreAnnexEntity other = (RegistreAnnexEntity) obj;
		if (dataCaptura == null) {
			if (other.dataCaptura != null)
				return false;
		} else if (!dataCaptura.equals(other.dataCaptura))
			return false;
		if (fitxerGestioDocumentalId == null) {
			if (other.fitxerGestioDocumentalId != null)
				return false;
		} else if (!fitxerGestioDocumentalId.equals(other.fitxerGestioDocumentalId))
			return false;
		if (fitxerNom == null) {
			if (other.fitxerNom != null)
				return false;
		} else if (!fitxerNom.equals(other.fitxerNom))
			return false;
		if (fitxerTamany != other.fitxerTamany)
			return false;
		if (ntiTipusDocument == null) {
			if (other.ntiTipusDocument != null)
				return false;
		} else if (!ntiTipusDocument.equals(other.ntiTipusDocument))
			return false;
		if (origenCiutadaAdmin == null) {
			if (other.origenCiutadaAdmin != null)
				return false;
		} else if (!origenCiutadaAdmin.equals(other.origenCiutadaAdmin))
			return false;
		if (registre == null) {
			if (other.registre != null)
				return false;
		} else if (!registre.equals(other.registre))
			return false;
		if (sicresTipusDocument == null) {
			if (other.sicresTipusDocument != null)
				return false;
		} else if (!sicresTipusDocument.equals(other.sicresTipusDocument))
			return false;
		if (titol == null) {
			if (other.titol != null)
				return false;
		} else if (!titol.equals(other.titol))
			return false;
		return true;
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
