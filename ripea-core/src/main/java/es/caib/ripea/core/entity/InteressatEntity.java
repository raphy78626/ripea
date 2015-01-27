/**
 * 
 */
package es.caib.ripea.core.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.ForeignKey;

import es.caib.ripea.core.audit.RipeaAuditable;
import es.caib.ripea.core.audit.RipeaAuditingEntityListener;

/**
 * Classe del model de dades que representa un interessat.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(	name = "ipa_interessat",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {
						"entitat_id",
						"nif",
						"nom",
						"llinatges",
						"identificador"})})
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@EntityListeners(RipeaAuditingEntityListener.class)
public abstract class InteressatEntity extends RipeaAuditable<Long> {

	@Column(name = "nom", length = 256, nullable = false)
	protected String nom;
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "entitat_id")
	@ForeignKey(name = "ipa_entitat_interessat_fk")
	protected EntitatEntity entitat;
	@ManyToMany(
			mappedBy = "interessats",
			fetch = FetchType.LAZY)
	protected Set<ExpedientEntity> expedients = new HashSet<ExpedientEntity>();
	@Version
	private long version = 0;



	public String getNom() {
		return nom;
	}
	public EntitatEntity getEntitat() {
		return entitat;
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
