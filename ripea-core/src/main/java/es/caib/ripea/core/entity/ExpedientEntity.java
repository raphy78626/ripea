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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
	@Column(name = "obert")
	protected boolean obert;
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
	@Column(name = "sistra_clau")
	protected String sistraClau;



	public ArxiuEntity getArxiu() {
		return arxiu;
	}
	public Set<InteressatEntity> getInteressats() {
		return interessats;
	}
	public boolean isObert() {
		return obert;
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
	public void updateFinalitzar(
			String tancatMotiu) {
		this.tancatMotiu = tancatMotiu;
		this.obert = false;
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

	/**
	 * Obté el Builder per a crear objectes de tipus expedient.
	 * 
	 * @param nom
	 *            El valor de l'atribut nom.
	 * @param metaNode
	 *            arxiu meta-node al qual pertany aquest expedient.
	 * @param arxiu
	 *            L'arxiu al qual pertany aquest expedient.
	 * @param contenidor
	 *            El contenidor al qual pertany aquest expedient.
	 * @param entitat
	 *            L'entitat a la qual pertany aquest expedient.
	 * @return Una nova instància del Builder.
	 */
	public static Builder getBuilder(
			String nom,
			MetaNodeEntity metaNode,
			ArxiuEntity arxiu,
			ContenidorEntity contenidor,
			EntitatEntity entitat) {
		return new Builder(
				nom,
				metaNode,
				arxiu,
				contenidor,
				entitat);
	}

	/**
	 * Builder per a crear noves instàncies d'aquesta classe.
	 * 
	 * @author Limit Tecnologies <limit@limit.es>
	 */
	public static class Builder {
		ExpedientEntity built;
		Builder(
				String nom,
				MetaNodeEntity metaNode,
				ArxiuEntity arxiu,
				ContenidorEntity pare,
				EntitatEntity entitat) {
			built = new ExpedientEntity();
			built.nom = nom;
			built.metaNode = metaNode;
			built.arxiu = arxiu;
			built.pare = pare;
			built.entitat = entitat;
			built.obert = true;
			built.tipusContenidor = ContenidorTipusEnum.CONTINGUT;
		}
		public ExpedientEntity build() {
			return built;
		}
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
