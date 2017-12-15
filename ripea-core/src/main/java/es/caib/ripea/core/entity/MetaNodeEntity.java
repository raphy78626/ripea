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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.ForeignKey;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.api.dto.MultiplicitatEnumDto;
import es.caib.ripea.core.audit.RipeaAuditable;

/**
 * Classe del model de dades que representa un meta-node.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(	name = "ipa_metanode",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {
						"entitat_id",
						"codi",
						"tipus"})})
@Inheritance(strategy=InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
public abstract class MetaNodeEntity extends RipeaAuditable<Long> {

	@Column(name = "codi", length = 64, nullable = false)
	protected String codi;
	@Column(name = "nom", length = 256, nullable = false)
	protected String nom;
	@Column(name = "descripcio", length = 1024)
	protected String descripcio;
	@Column(name = "tipus", nullable = false)
	@Enumerated(EnumType.STRING)
	protected MetaNodeTipusEnum tipus;
	@Column(name = "actiu")
	protected boolean actiu = true;
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "entitat_id")
	@ForeignKey(name = "ipa_entitat_metanode_fk")
	protected EntitatEntity entitat;
	@OneToMany(
			mappedBy = "metaNode",
			fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	@OrderBy("ordre asc")
	private Set<MetaNodeMetaDadaEntity> metaDades = new HashSet<MetaNodeMetaDadaEntity>();
	@OneToMany(
			mappedBy = "metaNode",
			fetch = FetchType.LAZY)
	protected Set<NodeEntity> nodes = new HashSet<NodeEntity>();
	@Version
	private long version = 0;



	public String getCodi() {
		return codi;
	}
	public String getNom() {
		return nom;
	}
	public String getDescripcio() {
		return descripcio;
	}
	public EntitatEntity getEntitat() {
		return entitat;
	}
	public Set<MetaNodeMetaDadaEntity> getMetaDades() {
		return metaDades;
	}
	public Set<NodeEntity> getNodes() {
		return nodes;
	}
	public boolean isActiu() {
		return actiu;
	}

	protected void update(
			String codi,
			String nom,
			String descripcio) {
		this.codi = codi;
		this.nom = nom;
		this.descripcio = descripcio;
	}
	public void updateActiu(
			boolean actiu) {
		this.actiu = actiu;
	}

	public void metaDadaAdd(
			MetaDadaEntity metaDada,
			MultiplicitatEnumDto multiplicitat,
			boolean readOnly) {
		MetaNodeMetaDadaEntity metaNodeMetaDada = MetaNodeMetaDadaEntity.getBuilder(
				this,
				metaDada,
				multiplicitat,
				readOnly,
				metaDades.size()).build();
		metaDades.add(metaNodeMetaDada);
	}
	public void metaDadaDelete(MetaNodeMetaDadaEntity metaNodeMetaDada) {
		Iterator<MetaNodeMetaDadaEntity> it = metaDades.iterator();
		while (it.hasNext()) {
			MetaNodeMetaDadaEntity mnmd = it.next();
			if (mnmd.getId().equals(metaNodeMetaDada.getId()))
				it.remove();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((codi == null) ? 0 : codi.hashCode());
		result = prime * result + ((entitat == null) ? 0 : entitat.hashCode());
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
		MetaNodeEntity other = (MetaNodeEntity) obj;
		if (codi == null) {
			if (other.codi != null)
				return false;
		} else if (!codi.equals(other.codi))
			return false;
		if (entitat == null) {
			if (other.entitat != null)
				return false;
		} else if (!entitat.equals(other.entitat))
			return false;
		if (tipus != other.tipus)
			return false;
		return true;
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
