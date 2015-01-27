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
 * Classe del model de dades que representa una dada.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(	name = "ipa_dada",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {
						"metadada_id",
						"node_id",
						"ordre"})})
@EntityListeners(RipeaAuditingEntityListener.class)
public class DadaEntity extends RipeaAuditable<Long> {

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "metadada_id")
	@ForeignKey(name = "ipa_metadada_dada_fk")
	protected MetaDadaEntity metaDada;
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "node_id")
	@ForeignKey(name = "ipa_node_dada_fk")
	protected NodeEntity node;
	@Column(name = "valor", length = 256, nullable = false)
	protected String valor;
	@Column(name = "ordre")
	protected int ordre;
	@Version
	private long version = 0;



	public MetaDadaEntity getMetaDada() {
		return metaDada;
	}
	public NodeEntity getNode() {
		return node;
	}
	public String getValor() {
		return valor;
	}
	public int getOrdre() {
		return ordre;
	}

	public void update(
			String valor) {
		this.valor = valor;
	}

	/**
	 * Obté el Builder per a crear objectes de tipus meta-dada.
	 * 
	 * @param metaDada
	 *            La meta-dada a la qual pertany aquesta dada.
	 * @param node
	 *            El node al qual pertany aquesta dada.
	 * @param valor
	 *           El valor de l'atribut valor.
	 * @param ordre
	 *            El valor de l'atribut ordre.
	 * @return Una nova instància del Builder.
	 */
	public static Builder getBuilder(
			MetaDadaEntity metaDada,
			NodeEntity node,
			String valor,
			int ordre) {
		return new Builder(
				metaDada,
				node,
				valor,
				ordre);
	}

	/**
	 * Builder per a crear noves instàncies d'aquesta classe.
	 * 
	 * @author Limit Tecnologies <limit@limit.es>
	 */
	public static class Builder {
		DadaEntity built;
		Builder(
				MetaDadaEntity metaDada,
				NodeEntity node,
				String valor,
				int ordre) {
			built = new DadaEntity();
			built.metaDada = metaDada;
			built.node = node;
			built.valor = valor;
			built.ordre = ordre;
		}
		public DadaEntity build() {
			return built;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((metaDada == null) ? 0 : metaDada.hashCode());
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		result = prime * result + ordre;
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
		DadaEntity other = (DadaEntity) obj;
		if (metaDada == null) {
			if (other.metaDada != null)
				return false;
		} else if (!metaDada.equals(other.metaDada))
			return false;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		if (ordre != other.ordre)
			return false;
		return true;
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
