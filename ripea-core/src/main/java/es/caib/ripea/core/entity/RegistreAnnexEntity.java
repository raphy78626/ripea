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

import es.caib.ripea.core.api.dto.RegistreAnnexFirmaModeEnumDto;
import es.caib.ripea.core.api.dto.RegistreAnnexOrigenEnumDto;
import es.caib.ripea.core.api.dto.RegistreDocumentTipusEnumDto;
import es.caib.ripea.core.api.dto.RegistreDocumentValidesaEnumDto;
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
								"tipus"})})
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
	@Column(name = "nti_tipo", length = 4, nullable = false)
	private String ntiTipoDocumental;
	@Column(name = "validesa")
	private String validesa;
	@Column(name = "tipus", nullable = false)
	private String tipus;
	@Column(name = "observacions", length = 50)
	private String observacions;
	@Column(name = "origen", length = 1)
	private String origen;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_captura", nullable = false)
	private Date dataCaptura;
	@Column(name = "firma_mode", length = 1)
	private Integer firmaMode;
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
	public String getNtiTipoDocumental() {
		return ntiTipoDocumental;
	}
	public RegistreDocumentValidesaEnumDto getValidesa() {
		return RegistreDocumentValidesaEnumDto.valorAsEnum(validesa);
	}
	public RegistreDocumentTipusEnumDto getTipus() {
		return RegistreDocumentTipusEnumDto.valorAsEnum(tipus);
	}
	public String getObservacions() {
		return observacions;
	}
	public RegistreAnnexOrigenEnumDto getOrigen() {
		return RegistreAnnexOrigenEnumDto.valorAsEnum(origen);
	}
	public Date getDataCaptura() {
		return dataCaptura;
	}
	public RegistreAnnexFirmaModeEnumDto getFirmaMode() {
		return RegistreAnnexFirmaModeEnumDto.valorAsEnum(firmaMode);
	}
	public RegistreEntity getRegistre() {
		return registre;
	}

	public static Builder getBuilder(
			String titol,
			String fitxerNom,
			int fitxerTamany,
			String ntiTipoDocumental,
			RegistreDocumentTipusEnumDto tipus,
			RegistreAnnexOrigenEnumDto origen,
			Date dataCaptura,
			RegistreEntity registre) {
		return new Builder(
				titol,
				fitxerNom,
				fitxerTamany,
				ntiTipoDocumental,
				tipus,
				origen,
				dataCaptura,
				registre);
	}
	public static class Builder {
		RegistreAnnexEntity built;
		Builder(
				String titol,
				String fitxerNom,
				int fitxerTamany,
				String ntiTipoDocumental,
				RegistreDocumentTipusEnumDto tipus,
				RegistreAnnexOrigenEnumDto origen,
				Date dataCaptura,
				RegistreEntity registre) {
			built = new RegistreAnnexEntity();
			built.titol = titol;
			built.fitxerNom = fitxerNom;
			built.fitxerTamany = fitxerTamany;
			built.ntiTipoDocumental = ntiTipoDocumental;
			if (tipus != null)
				built.tipus = tipus.getValor();
			if (origen != null)
				built.origen = origen.getValor();
			built.dataCaptura = dataCaptura;
			built.registre = registre;
		}
		public Builder fitxerTipusMime(String fitxerTipusMime) {
			built.fitxerTipusMime = fitxerTipusMime;
			return this;
		}
		public Builder validesa(RegistreDocumentValidesaEnumDto validesa) {
			if (validesa != null)
				built.validesa = validesa.getValor();
			return this;
		}
		public Builder observacions(String observacions) {
			built.observacions = observacions;
			return this;
		}
		public Builder firmaMode(RegistreAnnexFirmaModeEnumDto firmaMode) {
			if (firmaMode != null)
				built.firmaMode = firmaMode.getValor();
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
		result = prime * result + ((fitxerNom == null) ? 0 : fitxerNom.hashCode());
		result = prime * result + fitxerTamany;
		result = prime * result + ((registre == null) ? 0 : registre.hashCode());
		result = prime * result + ((tipus == null) ? 0 : tipus.hashCode());
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
		if (fitxerNom == null) {
			if (other.fitxerNom != null)
				return false;
		} else if (!fitxerNom.equals(other.fitxerNom))
			return false;
		if (fitxerTamany != other.fitxerTamany)
			return false;
		if (registre == null) {
			if (other.registre != null)
				return false;
		} else if (!registre.equals(other.registre))
			return false;
		if (tipus != other.tipus)
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
