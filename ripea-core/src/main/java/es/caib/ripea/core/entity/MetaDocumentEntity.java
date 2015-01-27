/**
 * 
 */
package es.caib.ripea.core.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

import es.caib.ripea.core.audit.RipeaAuditingEntityListener;

/**
 * Classe del model de dades que representa un meta-document.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Entity
@Table(name = "ipa_metadocument")
@EntityListeners(RipeaAuditingEntityListener.class)
public class MetaDocumentEntity extends MetaNodeEntity {

	@Column(name = "global_expedient")
	private boolean globalExpedient;
	@Column(name = "global_multiplicitat")
	@Enumerated(EnumType.STRING)
	private MultiplicitatEnum globalMultiplicitat;
	@Column(name = "global_readonly")
	private boolean globalReadOnly;
	@Column(name = "firma_applet")
	private boolean firmaAppletActiva;
	@Column(name = "signatura_tipmime", length = 64)
	private String signaturaTipusMime;
	@Column(name = "firma_pfirma")
	private boolean firmaPortafirmesActiva;
	@Column(name = "portafirmes_doctip", length = 64)
	private String portafirmesDocumentTipus;
	@Column(name = "portafirmes_fluxid", length = 64)
	private String portafirmesFluxId;
	@Column(name = "custodia_politica", length = 64)
	private String custodiaPolitica;
	@Column(name = "plantilla_nom", length = 256)
	private String plantillaNom;
	@Column(name = "plantilla_content_type", length = 256)
	private String plantillaContentType;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "plantilla_contingut")
	private byte[] plantillaContingut;



	public boolean isGlobalExpedient() {
		return globalExpedient;
	}
	public MultiplicitatEnum getGlobalMultiplicitat() {
		return globalMultiplicitat;
	}
	public boolean isGlobalReadOnly() {
		return globalReadOnly;
	}
	public boolean isFirmaAppletActiva() {
		return firmaAppletActiva;
	}
	public String getSignaturaTipusMime() {
		return signaturaTipusMime;
	}
	public boolean isFirmaPortafirmesActiva() {
		return firmaPortafirmesActiva;
	}
	public String getPortafirmesDocumentTipus() {
		return portafirmesDocumentTipus;
	}
	public String getPortafirmesFluxId() {
		return portafirmesFluxId;
	}
	public String getCustodiaPolitica() {
		return custodiaPolitica;
	}
	public String getPlantillaNom() {
		return plantillaNom;
	}
	public String getPlantillaContentType() {
		return plantillaContentType;
	}
	public byte[] getPlantillaContingut() {
		return plantillaContingut;
	}

	public void update(
			String codi,
			String nom,
			String descripcio,
			boolean globalExpedient,
			MultiplicitatEnum globalMultiplicitat,
			boolean globalReadOnly,
			String custodiaPolitica,
			String portafirmesDocumentTipus,
			String portafirmesFluxId,
			String signaturaTipusMime) {
		update(
				codi,
				nom,
				descripcio);
		this.globalExpedient = globalExpedient;
		this.globalMultiplicitat = globalMultiplicitat;
		this.globalReadOnly = globalReadOnly;
		this.custodiaPolitica = custodiaPolitica;
		this.portafirmesDocumentTipus = portafirmesDocumentTipus;
		this.portafirmesFluxId = portafirmesFluxId;
		this.signaturaTipusMime = signaturaTipusMime;
	}

	public void updatePlantilla(
			String plantillaNom,
			String plantillaContentType,
			byte[] plantillaContingut) {
		this.plantillaNom = plantillaNom;
		this.plantillaContentType = plantillaContentType;
		this.plantillaContingut = plantillaContingut;
	}

	/**
	 * Obté el Builder per a crear objectes de tipus meta-document.
	 * 
	 * @param codi
	 *            El valor de l'atribut codi.
	 * @param nom
	 *            El valor de l'atribut nom.
	 * @param descripcio
	 *            El valor de l'atribut descripcio.
	 * @param globalExpedient
	 *            El valor de l'atribut globalExpedient.
	 * @param globalDocument
	 *            El valor de l'atribut globalDocument.
	 * @param globalMultiplicitat
	 *            El valor de l'atribut globalMultiplicitat.
	 * @param globalReadOnly
	 *            El valor de l'atribut globalReadOnly.
	 * @param custodiaPolitica
	 *            El valor de l'atribut custodiaPolitica.
	 * @param portafirmesDocumentTipus
	 *            El valor de l'atribut portafirmesDocumentTipus.
	 * @param portafirmesFluxId
	 *            El valor de l'atribut portafirmesFluxId.
	 * @param signaturaTipusMime
	 *            El valor de l'atribut signaturaTipusMime.
	 * @param entitat
	 *            L'entitat a la qual pertany aquest meta-document.
	 * @return Una nova instància del Builder.
	 */
	public static Builder getBuilder(
			String codi,
			String nom,
			String descripcio,
			boolean globalExpedient,
			MultiplicitatEnum globalMultiplicitat,
			boolean globalReadOnly,
			String custodiaPolitica,
			String portafirmesDocumentTipus,
			String portafirmesFluxId,
			String signaturaTipusMime,
			EntitatEntity entitat) {
		return new Builder(
				codi,
				nom,
				descripcio,
				globalExpedient,
				globalMultiplicitat,
				globalReadOnly,
				custodiaPolitica,
				portafirmesDocumentTipus,
				portafirmesFluxId,
				signaturaTipusMime,
				entitat);
	}

	/**
	 * Builder per a crear noves instàncies d'aquesta classe.
	 * 
	 * @author Limit Tecnologies <limit@limit.es>
	 */
	public static class Builder {
		MetaDocumentEntity built;
		Builder(
				String codi,
				String nom,
				String descripcio,
				boolean globalExpedient,
				MultiplicitatEnum globalMultiplicitat,
				boolean globalReadOnly,
				String custodiaPolitica,
				String portafirmesDocumentTipus,
				String portafirmesFluxId,
				String signaturaTipusMime,
				EntitatEntity entitat) {
			built = new MetaDocumentEntity();
			built.codi = codi;
			built.nom = nom;
			built.descripcio = descripcio;
			built.globalExpedient = globalExpedient;
			built.globalMultiplicitat = globalMultiplicitat;
			built.globalReadOnly = globalReadOnly;
			built.custodiaPolitica = custodiaPolitica;
			built.portafirmesDocumentTipus = portafirmesDocumentTipus;
			built.portafirmesFluxId = portafirmesFluxId;
			built.signaturaTipusMime = signaturaTipusMime;
			built.entitat = entitat;
			built.tipus = MetaNodeTipusEnum.DOCUMENT;
		}
		public MetaDocumentEntity build() {
			return built;
		}
	}

	private static final long serialVersionUID = -2299453443943600172L;

}
