/**
 * 
 */
package es.caib.ripea.core.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.api.dto.MultiplicitatEnumDto;

/**
 * Classe del model de dades que representa un meta-expedient.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(name = "ipa_metaexpedient")
@EntityListeners(AuditingEntityListener.class)
public class MetaExpedientEntity extends MetaNodeEntity {

	@Column(name = "classificacio", length = 30)
	private String classificacio;
	@ManyToOne(
			optional = true,
			fetch = FetchType.EAGER)
	@JoinColumn(name = "pare_id")
	@ForeignKey(name = "ipa_pare_metaexp_fk")
	private MetaExpedientEntity pare;
	@OneToMany(
			mappedBy = "metaExpedient",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private Set<MetaExpedientMetaDocumentEntity> metaDocuments = new HashSet<MetaExpedientMetaDocumentEntity>();



	public String getClassificacio() {
		return classificacio;
	}
	public MetaExpedientEntity getPare() {
		return pare;
	}

	public void update(
			String codi,
			String nom,
			String descripcio,
			String classificacio,
			MetaExpedientEntity pare) {
		super.update(
				codi,
				nom,
				descripcio);
		this.classificacio = classificacio;
		this.pare = pare;
	}

	public void metaDocumentAdd(
			MetaDocumentEntity metaDocument,
			MultiplicitatEnumDto multiplicitat,
			boolean readOnly) {
		MetaExpedientMetaDocumentEntity metaExpedientMetaDocument = MetaExpedientMetaDocumentEntity.getBuilder(
				this,
				metaDocument,
				multiplicitat,
				readOnly,
				metaDocuments.size()).build();
		metaDocuments.add(metaExpedientMetaDocument);
	}
	public void metaDocumentDelete(MetaExpedientMetaDocumentEntity metaExpedientMetaDocument) {
		Iterator<MetaExpedientMetaDocumentEntity> it = metaDocuments.iterator();
		while (it.hasNext()) {
			MetaExpedientMetaDocumentEntity memd = it.next();
			if (memd.getId().equals(metaExpedientMetaDocument.getId()))
				it.remove();
		}
	}

	/**
	 * Obté el Builder per a crear objectes de tipus meta-expedient.
	 * 
	 * @param codi
	 *            El valor de l'atribut codi.
	 * @param nom
	 *            El valor de l'atribut nom.
	 * @param descripcio
	 *            El valor de l'atribut descripcio.
	 * @param classificacio
	 *            El valor de l'atribut classificacio.
	 * @param entitat
	 *            L'entitat a la qual pertany aquest meta-expedient.
	 * @param entitat
	 *            El meta-expedient pare d'aquest meta-expedient.
	 * @return Una nova instància del Builder.
	 */
	public static Builder getBuilder(
			String codi,
			String nom,
			String descripcio,
			String classificacio,
			EntitatEntity entitat,
			MetaExpedientEntity pare) {
		return new Builder(
				codi,
				nom,
				descripcio,
				classificacio,
				entitat,
				pare);
	}

	/**
	 * Builder per a crear noves instàncies d'aquesta classe.
	 * 
	 * @author Limit Tecnologies <limit@limit.es>
	 */
	public static class Builder {
		MetaExpedientEntity built;
		Builder(
				String codi,
				String nom,
				String descripcio,
				String classificacio,
				EntitatEntity entitat,
				MetaExpedientEntity pare) {
			built = new MetaExpedientEntity();
			built.codi = codi;
			built.nom = nom;
			built.descripcio = descripcio;
			built.classificacio = classificacio;
			built.entitat = entitat;
			built.tipus = MetaNodeTipusEnum.EXPEDIENT;
			built.pare = pare;
		}
		public MetaExpedientEntity build() {
			return built;
		}
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
