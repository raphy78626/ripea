/**
 * 
 */
package es.caib.ripea.core.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.ForeignKey;

import es.caib.ripea.core.audit.RipeaAuditable;
import es.caib.ripea.core.audit.RipeaAuditingEntityListener;

/**
 * Classe del model de dades que representa un contenidor.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(	name = "ipa_contenidor",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "ipa_contenidor_mult_uk",
						columnNames = {
								"entitat_id",
								"nom",
								"pare_id",
								"tipus_cont",
								"esborrat"})})
@Inheritance(strategy=InheritanceType.JOINED)
@EntityListeners(RipeaAuditingEntityListener.class)
public abstract class ContenidorEntity extends RipeaAuditable<Long> {

	@Column(name = "nom", length = 256, nullable = false)
	protected String nom;
	@Column(name = "tipus_cont", nullable = false)
	protected ContenidorTipusEnum tipusContenidor;
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "pare_id")
	@ForeignKey(name = "ipa_pare_contenidor_fk")
	protected ContenidorEntity pare;
	@OneToMany(
			mappedBy = "pare",
			fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	protected Set<ContenidorEntity> fills = new HashSet<ContenidorEntity>();
	@Column(name = "esborrat")
	protected int esborrat = 0;
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "contmov_id")
	@ForeignKey(name = "ipa_contmov_contenidor_fk")
	protected ContenidorMovimentEntity darrerMoviment;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "entitat_id")
	@ForeignKey(name = "ipa_entitat_contenidor_fk")
	protected EntitatEntity entitat;
	@Version
	private long version = 0;



	public String getNom() {
		return nom;
	}
	public ContenidorTipusEnum getTipusContenidor() {
		return tipusContenidor;
	}
	public EntitatEntity getEntitat() {
		return entitat;
	}
	public ContenidorEntity getPare() {
		return pare;
	}
	public Set<ContenidorEntity> getFills() {
		return fills;
	}
	public int getEsborrat() {
		return esborrat;
	}
	public ContenidorMovimentEntity getDarrerMoviment() {
		return darrerMoviment;
	}

	public void update(String nom) {
		this.nom = nom;
	}
	public void updatePare(ContenidorEntity pare) {
		this.pare = pare;
	}
	public void updateEsborrat(int esborrat) {
		this.esborrat = esborrat;
	}
	public void updateDarrerMoviment(ContenidorMovimentEntity darrerMoviment) {
		this.darrerMoviment = darrerMoviment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + esborrat;
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		result = prime * result + ((pare == null) ? 0 : pare.hashCode());
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
		ContenidorEntity other = (ContenidorEntity) obj;
		if (esborrat != other.esborrat)
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		if (pare == null) {
			if (other.pare != null)
				return false;
		} else if (!pare.equals(other.pare))
			return false;
		return true;
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
