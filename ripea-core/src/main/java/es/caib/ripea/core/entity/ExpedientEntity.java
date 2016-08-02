/**
 * 
 */
package es.caib.ripea.core.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.api.dto.ContingutTipusEnumDto;
import es.caib.ripea.core.api.dto.ExpedientEstatEnumDto;

/**
 * Classe del model de dades que representa un expedient.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(name = "ipa_expedient")
@EntityListeners(AuditingEntityListener.class)
public class ExpedientEntity extends NodeEntity {

	@ManyToOne(
			optional = false,
			fetch = FetchType.EAGER)
	@JoinColumn(name = "arxiu_id")
	@ForeignKey(name = "ipa_arxiu_expedient_fk")
	protected ArxiuEntity arxiu;
	@ManyToMany(
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY)
	@JoinTable(
			name = "ipa_expedient_interessat",
			joinColumns = {@JoinColumn(name = "expedient_id")},
			inverseJoinColumns = {@JoinColumn(name = "interessat_id")})
	protected Set<InteressatEntity> interessats = new HashSet<InteressatEntity>();
	@Column(name = "estat", nullable = false)
	protected ExpedientEstatEnumDto estat;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tancat_data")
	protected Date tancatData;
	@Column(name = "tancat_motiu", length = 1024)
	protected String tancatMotiu;
	@Column(name = "anio")
	protected int any;
	@Column(name = "sequencia")
	protected long sequencia;
	@Column(name = "sistra_publicat")
	protected boolean sistraPublicat;
	@Column(name = "sistra_unitat_adm")
	protected Integer sistraUnitatAdministrativa;
	@Column(name = "sistra_clau", length = 100)
	protected String sistraClau;



	public ArxiuEntity getArxiu() {
		return arxiu;
	}
	public Set<InteressatEntity> getInteressats() {
		return interessats;
	}
	public ExpedientEstatEnumDto getEstat() {
		return estat;
	}
	public Date getTancatData() {
		return tancatData;
	}
	public String getTancatMotiu() {
		return tancatMotiu;
	}
	public int getAny() {
		return any;
	}
	public long getSequencia() {
		return sequencia;
	}
	public boolean isSistraPublicat() {
		return sistraPublicat;
	}
	public Integer getSistraUnitatAdministrativa() {
		return sistraUnitatAdministrativa;
	}
	public String getSistraClau() {
		return sistraClau;
	}
	public MetaExpedientEntity getMetaExpedient() {
		return (MetaExpedientEntity)getMetaNode();
	}

	public void update(
			String nom,
			MetaExpedientEntity metaExpedient,
			ArxiuEntity arxiu) {
		this.nom = nom;
		this.metaNode = metaExpedient;
		this.arxiu = arxiu;
	}
	public void updateAnySequencia(
			int any,
			long sequencia) {
		this.any = any;
		this.sequencia = sequencia;
	}
	public void updateEstat(
			ExpedientEstatEnumDto estat,
			String tancatMotiu) {
		this.estat = estat;
		this.tancatMotiu = tancatMotiu;
		if (ExpedientEstatEnumDto.TANCAT.equals(estat))
			this.tancatData = new Date();
	}

	public void addInteressat(InteressatEntity interessat) {
		interessats.add(interessat);
	}
	public void deleteInteressat(InteressatEntity interessat) {
		Iterator<InteressatEntity> it = interessats.iterator();
		while (it.hasNext()) {
			InteressatEntity ie = it.next();
			if (ie.equals(interessat))
				it.remove();
		}
	}

	public static Builder getBuilder(
			String nom,
			MetaNodeEntity metaNode,
			ArxiuEntity arxiu,
			ContingutEntity pare,
			EntitatEntity entitat) {
		return new Builder(
				nom,
				metaNode,
				arxiu,
				pare,
				entitat);
	}

	public static class Builder {
		ExpedientEntity built;
		Builder(
				String nom,
				MetaNodeEntity metaNode,
				ArxiuEntity arxiu,
				ContingutEntity pare,
				EntitatEntity entitat) {
			built = new ExpedientEntity();
			built.nom = nom;
			built.metaNode = metaNode;
			built.arxiu = arxiu;
			built.pare = pare;
			built.entitat = entitat;
			built.estat = ExpedientEstatEnumDto.OBERT;
			built.tipus = ContingutTipusEnumDto.EXPEDIENT;
		}
		public ExpedientEntity build() {
			return built;
		}
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
