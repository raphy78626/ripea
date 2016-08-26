/**
 * 
 */
package es.caib.ripea.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.api.dto.IndiomaEnumDto;
import es.caib.ripea.core.api.dto.InteressatDocumentTipusEnumDto;

/**
 * Classe del model de dades que representa un interessat de tipus administració pública.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class InteressatAdministracioEntity extends InteressatEntity {

//	CAMP					TIPUS INTERESSAT	DESCRIPCIÓ
//	------------------------------------------------------------------------------------------------------------------------------------
//	organCodi: 				ADMINISTRACIÓ		codi DIR3 de l’òrgan en cas de que l’interessat sigui del tipus administració pública.
	
	@Column(name = "organ_codi", length = 9)
	protected String organCodi;

	public String getOrganCodi() {
		return organCodi;
	}
	public void setOrganCodi(String organCodi) {
		this.organCodi = organCodi;
	}

	@Override
	public void updateIdentificador() {
		this.identificador = this.organCodi;
	}
	
	public void update(
			String organCodi,
			InteressatDocumentTipusEnumDto documentTipus,
			String documentNum,
			String pais,
			String provincia,
			String municipi,
			String adresa,
			String codiPostal,
			String email,
			String telefon,
			String observacions,
			IndiomaEnumDto notificacioIdioma,
			Boolean notificacioAutoritzat) {
		this.organCodi = organCodi;
		this.documentTipus = documentTipus;
		this.documentNum = documentNum;
		this.pais = pais;
		this.provincia =  provincia;
		this.municipi =  municipi;
		this.adresa =  adresa;
		this.codiPostal =  codiPostal;
		this.email =  email;
		this.telefon =  telefon;
		this.observacions =  observacions;
		this.notificacioIdioma =  notificacioIdioma;
		this.notificacioAutoritzat =  notificacioAutoritzat;
	}

	/**
	 * Obté el Builder per a crear objectes de tipus interessat-administració pública.
	 * 
	 * @param organCodi El codi DIR3 de l'òrgan de l'administració pública.
	 * @param documentTipus	El tipus de document de l'òrgan de l'administració pública.
	 * @param documentNum	El número de document de l'òrgan de l'administració pública.
	 * @param pais	El país de residència de l'òrgan de l'administració pública.
	 * @param provincia	La província de residència de l'òrgan de l'administració pública.
	 * @param municipi	El municipi de residència de l'òrgan de l'administració pública.
	 * @param adresa	L'adreça de residència de l'òrgan de l'administració pública.
	 * @param codiPostal	El codi postal de la residència de l'òrgan de l'administració pública.
	 * @param email	El correu electrònic de l'òrgan de l'administració pública.
	 * @param telefon	El telèfon de l'òrgan de l'administració pública.
	 * @param observacions	Camp per introduir observacions sobre l'òrgan de l'administració pública.
	 * @param notificacioIdioma	Idioma en que l'òrgan de l'administració pública desitja rebre les notificacions.
	 * @param notificacioAutoritzat	Camp per indicar si l'òrgan de l'administració pública ha autoritzat la recepció de notificacions en format electrònic.
	 * @param expedient	Expedient on està vinculat l'òrgan de l'administració pública.
	 * @param representant	Representant de l'òrgan de l'administració pública.
	 * @param entitat	Entitat a la qual pertany aquest òrgan de l'administració pública.
	 * @return
	 */
	public static Builder getBuilder(
			String organCodi,
			InteressatDocumentTipusEnumDto documentTipus,
			String documentNum,
			String pais,
			String provincia,
			String municipi,
			String adresa,
			String codiPostal,
			String email,
			String telefon,
			String observacions,
			IndiomaEnumDto notificacioIdioma,
			Boolean notificacioAutoritzat,
			ExpedientEntity expedient,
			InteressatEntity representant,
			EntitatEntity entitat) {
		return new Builder(
				organCodi,
				documentTipus,
				documentNum,
				pais,
				provincia,
				municipi,
				adresa,
				codiPostal,
				email,
				telefon,
				observacions,
				notificacioIdioma,
				notificacioAutoritzat,
				expedient,
				representant,
				entitat);
	}

	/**
	 * Builder per a crear noves instàncies d'aquesta classe.
	 * 
	 * @author Limit Tecnologies <limit@limit.es>
	 */
	public static class Builder {
		InteressatAdministracioEntity built;
		Builder(
				String organCodi,
				InteressatDocumentTipusEnumDto documentTipus,
				String documentNum,
				String pais,
				String provincia,
				String municipi,
				String adresa,
				String codiPostal,
				String email,
				String telefon,
				String observacions,
				IndiomaEnumDto notificacioIdioma,
				Boolean notificacioAutoritzat,
				ExpedientEntity expedient,
				InteressatEntity representant,
				EntitatEntity entitat) {
			built = new InteressatAdministracioEntity();
			built.organCodi = organCodi;
			built.documentTipus = documentTipus;
			built.documentNum = documentNum;
			built.pais = pais;
			built.provincia =  provincia;
			built.municipi =  municipi;
			built.adresa =  adresa;
			built.codiPostal =  codiPostal;
			built.email =  email;
			built.telefon =  telefon;
			built.observacions =  observacions;
			built.notificacioIdioma =  notificacioIdioma;
			built.notificacioAutoritzat =  notificacioAutoritzat;
			built.expedient =  expedient;
			built.representant =  representant;
			built.entitat = entitat;
		}
		public InteressatAdministracioEntity build() {
			return built;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((documentNum == null) ? 0 : documentNum.hashCode());
		result = prime * result + ((documentTipus == null) ? 0 : documentTipus.hashCode());
		result = prime * result + ((expedient == null) ? 0 : expedient.hashCode());
		result = prime * result + ((organCodi == null) ? 0 : organCodi.hashCode());
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
		InteressatAdministracioEntity other = (InteressatAdministracioEntity) obj;
		if (documentNum == null) {
			if (other.documentNum != null)
				return false;
		} else if (!documentNum.equals(other.documentNum))
			return false;
		if (documentTipus != other.documentTipus)
			return false;
		if (expedient == null) {
			if (other.expedient != null)
				return false;
		} else if (!expedient.equals(other.expedient))
			return false;
		if (organCodi == null) {
			if (other.organCodi != null)
				return false;
		} else if (!organCodi.equals(other.organCodi))
			return false;
		return true;
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
