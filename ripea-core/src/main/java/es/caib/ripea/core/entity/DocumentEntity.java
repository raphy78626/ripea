/**
 * 
 */
package es.caib.ripea.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;

import es.caib.ripea.core.audit.RipeaAuditingEntityListener;

/**
 * Classe del model de dades que representa un document.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(name = "ipa_document")
@EntityListeners(RipeaAuditingEntityListener.class)
public class DocumentEntity extends NodeEntity {

	@ManyToOne(optional = true)
	@JoinColumn(name = "expedient_id")
	@ForeignKey(name = "ipa_expedient_document_fk")
	protected ExpedientEntity expedient;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data")
	protected Date data;
	@Column(name = "darrera_versio", nullable = false)
	protected int darreraVersio;



	public ExpedientEntity getExpedient() {
		return expedient;
	}
	public Date getData() {
		return data;
	}
	public int getDarreraVersio() {
		return darreraVersio;
	}

	public MetaDocumentEntity getMetaDocument() {
		return (MetaDocumentEntity)getMetaNode();
	}

	public void update(
			MetaDocumentEntity metaDocument,
			String nom,
			Date data) {
		this.metaNode = metaDocument;
		this.nom = nom;
		this.data = data;
	}
	public void updateDarreraVersio(int darreraVersio) {
		this.darreraVersio = darreraVersio;
	}

	/**
	 * Obté el Builder per a crear objectes de tipus document.
	 * 
	 * @param nom
	 *            El valor de l'atribut nom.
	 * @param data
	 *            El valor de l'atribut data.
	 * @param expedient
	 *            El valor de l'atribut expedient.
	 * @param metaNode
	 *            El meta-node al qual pertany aquest document.
	 * @param contenidor
	 *            El contenidor al qual pertany aquest document.
	 * @param entitat
	 *            L'entitat a la qual pertany aquest document.
	 * @return Una nova instància del Builder.
	 */
	public static Builder getBuilder(
			String nom,
			Date data,
			ExpedientEntity expedient,
			MetaNodeEntity metaNode,
			ContenidorEntity contenidor,
			EntitatEntity entitat) {
		return new Builder(
				nom,
				data,
				expedient,
				metaNode,
				contenidor,
				entitat);
	}

	/**
	 * Builder per a crear noves instàncies d'aquesta classe.
	 * 
	 * @author Limit Tecnologies <limit@limit.es>
	 */
	public static class Builder {
		DocumentEntity built;
		Builder(
				String nom,
				Date data,
				ExpedientEntity expedient,
				MetaNodeEntity metaNode,
				ContenidorEntity pare,
				EntitatEntity entitat) {
			built = new DocumentEntity();
			built.nom = nom;
			built.data = data;
			built.expedient = expedient;
			built.metaNode = metaNode;
			built.pare = pare;
			built.entitat = entitat;
			built.darreraVersio = 0;
			built.tipusContenidor = ContenidorTipusEnum.CONTINGUT;
		}
		public DocumentEntity build() {
			return built;
		}
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
