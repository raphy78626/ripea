/**
 * 
 */
package es.caib.ripea.core.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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

	@Column(name = "clasif_sia", length = 6, nullable = false)
	private String classificacioSia;
	@Column(name = "clasif_doc", length = 30, nullable = false)
	private String classificacioDocumental;
	@Column(name = "unitat_adm", length = 9)
	private String unitatAdministrativa;
	@Column(name = "notif_activa", nullable = false)
	private boolean notificacioActiva;
	@Column(name = "notif_organ_codi", length = 9)
	private String notificacioOrganCodi;
	@Column(name = "notif_llibre_codi", length = 4)
	private String notificacioLlibreCodi;
	@Column(name = "notif_avis_titol", length = 256)
	private String notificacioAvisTitol;
	@Column(name = "notif_avis_text", length = 1024)
	private String notificacioAvisText;
	@Column(name = "notif_avis_textsms", length = 200)
	private String notificacioAvisTextSms;
	@Column(name = "notif_ofici_titol", length = 1024)
	private String notificacioOficiTitol;
	@Column(name = "notif_ofici_text", length = 256)
	private String notificacioOficiText;
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
	@ManyToMany(
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY)
	@JoinTable(
			name = "ipa_metaexpedient_arxiu",
			joinColumns = {@JoinColumn(name = "metaexpedient_id")},
			inverseJoinColumns = {@JoinColumn(name = "arxiu_id")})
	protected List<ArxiuEntity> arxius;
	
	public String getClassificacioSia() {
		return classificacioSia;
	}
	public MetaExpedientEntity getPare() {
		return pare;
	}
	public List<ArxiuEntity> getArxius() {
		return arxius;
	}
	public String getUnitatAdministrativa() {
		return unitatAdministrativa;
	}
	public String getClassificacioDocumental() {
		return classificacioDocumental;
	}
	public boolean isNotificacioActiva() {
		return notificacioActiva;
	}
	public String getNotificacioOrganCodi() {
		return notificacioOrganCodi;
	}
	public String getNotificacioLlibreCodi() {
		return notificacioLlibreCodi;
	}
	public String getNotificacioAvisTitol() {
		return notificacioAvisTitol;
	}
	public String getNotificacioAvisText() {
		return notificacioAvisText;
	}
	public String getNotificacioAvisTextSms() {
		return notificacioAvisTextSms;
	}
	public String getNotificacioOficiTitol() {
		return notificacioOficiTitol;
	}
	public String getNotificacioOficiText() {
		return notificacioOficiText;
	}
	public Set<MetaExpedientMetaDocumentEntity> getMetaDocuments() {
		return metaDocuments;
	}
	public void update(
			String codi,
			String nom,
			String descripcio,
			String classificacioDocumental,
			String classificacioSia,
			String unitatAdministrativa,
			boolean notificacioActiva,
			String notificacioOrganCodi,
			String notificacioLlibreCodi,
			String notificacioAvisTitol,
			String notificacioAvisText,
			String notificacioAvisTextSms,
			String notificacioOficiTitol,
			String notificacioOficiText,
			MetaExpedientEntity pare) {
		super.update(
				codi,
				nom,
				descripcio);
		this.classificacioDocumental = classificacioDocumental;
		this.classificacioSia = classificacioSia;
		this.unitatAdministrativa = unitatAdministrativa;
		this.notificacioActiva = notificacioActiva;
		this.notificacioOrganCodi = notificacioOrganCodi;
		this.notificacioLlibreCodi = notificacioLlibreCodi;
		this.notificacioAvisTitol = notificacioAvisTitol;
		this.notificacioAvisText = notificacioAvisText;
		this.notificacioAvisTextSms = notificacioAvisTextSms;
		this.notificacioOficiTitol = notificacioOficiTitol;
		this.notificacioOficiText = notificacioOficiText;
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

	public static Builder getBuilder(
			String codi,
			String nom,
			String descripcio,
			String classificacioDocumental,
			String classificacioSia,
			String unitatAdministrativa,
			EntitatEntity entitat,
			MetaExpedientEntity pare,
			boolean notificacioActiva) {
		return new Builder(
				codi,
				nom,
				descripcio,
				classificacioDocumental,
				classificacioSia,
				unitatAdministrativa,
				entitat,
				pare,
				notificacioActiva);
	}

	public static class Builder {
		MetaExpedientEntity built;
		Builder(
				String codi,
				String nom,
				String descripcio,
				String classificacioDocumental,
				String classificacioSia,
				String unitatAdministrativa,
				EntitatEntity entitat,
				MetaExpedientEntity pare,
				boolean notificacioActiva) {
			built = new MetaExpedientEntity();
			built.codi = codi;
			built.nom = nom;
			built.descripcio = descripcio;
			built.classificacioDocumental = classificacioDocumental;
			built.classificacioSia = classificacioSia;
			built.unitatAdministrativa = unitatAdministrativa;
			built.entitat = entitat;
			built.tipus = MetaNodeTipusEnum.EXPEDIENT;
			built.pare = pare;
			built.notificacioActiva = notificacioActiva;
		}
		public Builder classificacioDocumental(String classificacioDocumental) {
			built.classificacioDocumental = classificacioDocumental;
			return this;
		}
		public Builder notificacioOrganCodi(String notificacioOrganCodi) {
			built.notificacioOrganCodi = notificacioOrganCodi;
			return this;
		}
		public Builder notificacioLlibreCodi(String notificacioLlibreCodi) {
			built.notificacioLlibreCodi = notificacioLlibreCodi;
			return this;
		}
		public Builder notificacioAvisTitol(String notificacioAvisTitol) {
			built.notificacioAvisTitol = notificacioAvisTitol;
			return this;
		}
		public Builder notificacioAvisText(String notificacioAvisText) {
			built.notificacioAvisText = notificacioAvisText;
			return this;
		}
		public Builder notificacioAvisTextSms(String notificacioAvisTextSms) {
			built.notificacioAvisTextSms = notificacioAvisTextSms;
			return this;
		}
		public Builder notificacioOficiTitol(String notificacioOficiTitol) {
			built.notificacioOficiTitol = notificacioOficiTitol;
			return this;
		}
		public Builder notificacioOficiText(String notificacioOficiText) {
			built.notificacioOficiText = notificacioOficiText;
			return this;
		}
		public MetaExpedientEntity build() {
			return built;
		}
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
