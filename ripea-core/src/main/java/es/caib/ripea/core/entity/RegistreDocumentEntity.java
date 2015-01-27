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
 * Classe del model de dades que representa un document
 * d'una anotació al registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(	name = "ipa_registre_doc",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {
						"registre_id",
						"identificador"})})
@EntityListeners(RipeaAuditingEntityListener.class)
public class RegistreDocumentEntity extends RipeaAuditable<Long> {

	@Column(name = "fitxer_nom", length = 80, nullable = false)
	private String fitxerNom;
	@Column(name = "identificador", length = 50, nullable = false)
	private String identificador;
	@Column(name = "validesa")
	private RegistreDocumentValidesaEnum validesa;
	@Column(name = "tipus", nullable = false)
	private RegistreDocumentTipusEnum tipus;
	@Column(name = "gesdoc_id", length = 100)
	private String gestioDocumentalId;
	/*@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "certificat")
	private String certificat;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "firma")
	private String firma;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "timestamp")
	private String timestamp;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "validacio_ocsp")
	private String validacioOcsp;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "hash", nullable = false)
	private String hash;
	@Column(name = "tipus_mime", length = 20)
	private String tipusMime;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "fitxer_contingut")
	private String fitxerContingut;*/
	@Column(name = "docfirmat_id", length = 50)
	private String indentificadorDocumentFirmat;
	@Column(name = "observacions", length = 50)
	private String observacions;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "registre_id")
	@ForeignKey(name = "ipa_document_registre_fk")
	private RegistreEntity registre;
	@Version
	private long version = 0;



	/**
	 * Obté el Builder per a crear objectes de tipus arxiu.
	 * 
	 * @param fitxerNom
	 *            El valor de l'atribut fitxerNom.
	 * @param identificador
	 *            El valor de l'atribut identificador.
	 * @param validesa
	 *            El valor de l'atribut validesa.
	 * @param tipus
	 *            El valor de l'atribut tipus.
	 * @param gestioDocumentalId
	 *            El valor de l'atribut gestioDocumentalId.
	 * @param certificat
	 *            El valor de l'atribut certificat.
	 * @param firma
	 *            El valor de l'atribut firma.
	 * @param timestamp
	 *            El valor de l'atribut timestamp.
	 * @param validacioOcsp
	 *            El valor de l'atribut validacioOcsp.
	 * @param hash
	 *            El valor de l'atribut hash.
	 * @param tipusMime
	 *            El valor de l'atribut tipusMime.
	 * @param fitxerContingut
	 *            El valor de l'atribut fitxerContingut.
	 * @param indentificadorDocumentFirmat
	 *            El valor de l'atribut indentificadorDocumentFirmat.
	 * @param observacions
	 *            El valor de l'atribut observacions.
	 * @param registre
	 *            El valor de l'atribut registre.
	 * @return Una nova instància del Builder.
	 */
	public static Builder getBuilder(
			String fitxerNom,
			String identificador,
			RegistreDocumentValidesaEnum validesa,
			RegistreDocumentTipusEnum tipus,
			String gestioDocumentalId,
			/*String certificat,
			String firma,
			String timestamp,
			String validacioOcsp,
			String hash,
			String tipusMime,
			String fitxerContingut,*/
			String indentificadorDocumentFirmat,
			String observacions,
			RegistreEntity registre) {
		return new Builder(
				fitxerNom,
				identificador,
				validesa,
				tipus,
				gestioDocumentalId,
				/*certificat,
				firma,
				timestamp,
				validacioOcsp,
				hash,
				tipusMime,
				fitxerContingut,*/
				indentificadorDocumentFirmat,
				observacions,
				registre);
	}

	/**
	 * Builder per a crear noves instàncies d'aquesta classe.
	 * 
	 * @author Limit Tecnologies <limit@limit.es>
	 */
	public static class Builder {
		RegistreDocumentEntity built;
		Builder(
				String fitxerNom,
				String identificador,
				RegistreDocumentValidesaEnum validesa,
				RegistreDocumentTipusEnum tipus,
				String gestioDocumentalId,
				/*String certificat,
				String firma,
				String timestamp,
				String validacioOcsp,
				String hash,
				String tipusMime,
				String fitxerContingut,*/
				String indentificadorDocumentFirmat,
				String observacions,
				RegistreEntity registre) {
			built = new RegistreDocumentEntity();
			built.fitxerNom = fitxerNom;
			built.identificador = identificador;
			built.validesa = validesa;
			built.tipus = tipus;
			built.gestioDocumentalId = gestioDocumentalId;
			/*built.certificat = certificat;
			built.firma = firma;
			built.timestamp = timestamp;
			built.validacioOcsp = validacioOcsp;
			built.hash = hash;
			built.tipusMime = tipusMime;
			built.fitxerContingut = fitxerContingut;*/
			built.indentificadorDocumentFirmat = indentificadorDocumentFirmat;
			built.observacions = observacions;
			built.registre = registre;
		}
		public RegistreDocumentEntity build() {
			return built;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((identificador == null) ? 0 : identificador.hashCode());
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
		RegistreDocumentEntity other = (RegistreDocumentEntity) obj;
		if (identificador == null) {
			if (other.identificador != null)
				return false;
		} else if (!identificador.equals(other.identificador))
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
