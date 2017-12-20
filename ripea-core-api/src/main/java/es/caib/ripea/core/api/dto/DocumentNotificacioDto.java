/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.Date;

/**
 * Informació d'una notificació d'un document a un ciutadà.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class DocumentNotificacioDto extends DocumentEnviamentDto {
	
	private DocumentNotificacioTipusEnumDto tipus;
	
	private Date caducitat;
	private String emisorDir3Codi;
	private Date enviamentDataProgramada;
	private NotificacioEnviamentTipusEnumDto enviamentTipus;
	
	private InteressatDocumentTipusEnumDto destinatariDocumentTipus;
	private String destinatariNom;
	private String destinatariLlinatge1;
	private String destinatariLlinatge2;
	private String destinatariNif;
	private String destinatariTelefon;
	private String destinatariEmail;
	private boolean destinatariRepresentant;
	private String destinatariPaisCodi;
	private String destinatariProvinciaCodi;
	private String destinatariMunicipiCodi;
	
	private boolean dehObligat;
	private String dehNif;
	
	private String notificaReferencia;
	private ServeiTipusEnumDto serveiTipus;
    
	private String titularNom;
	private String titularLlinatge1;
	private String titularLlinatge2;
	private String titularNif;
	private String titularTelefon;
	private String titularEmail;
	
	private String pagadorCieCodiDir3;
	private Date pagadorCieDataVigencia;
	
	private String pagadorCorreusCodiDir3;
	private String pagadorCorreusContracteNum;
	private String pagadorCorreusCodiClientFacturacio;
	private Date pagadorCorreusDataVigencia;
	
	private String seuExpedientSerieDocumental;
	private String seuExpedientUnitatOrganitzativa;
	private String seuExpedientIdentificadorEni;
	private String seuExpedientTitol;
	private String seuRegistreOficina;
	private String seuRegistreLlibre;
	private InteressatIdiomaEnumDto seuIdioma;
	private String seuAvisTitol;
	private String seuAvisText;
	private String seuAvisTextMobil;
	private String seuOficiTitol;
	private String seuOficiText;
	
	private String procedimentCodiSia;
	private Integer retardPostal;
	
	private String documentHash;
	private boolean documentNormalitzat;
	private boolean documentGenerarCsv;
	
	
	public DocumentNotificacioTipusEnumDto getTipus() {
		return tipus;
	}
	public void setTipus(DocumentNotificacioTipusEnumDto tipus) {
		this.tipus = tipus;
	}
	
	public Date getCaducitat() {
		return caducitat;
	}
	public void setCaducitat(Date caducitat) {
		this.caducitat = caducitat;
	}
	
	public String getEmisorDir3Codi() {
		return emisorDir3Codi;
	}
	public void setEmisorDir3Codi(String emisorDir3Codi) {
		this.emisorDir3Codi = emisorDir3Codi;
	}
	
	public Date getEnviamentDataProgramada() {
		return enviamentDataProgramada;
	}
	public void setEnviamentDataProgramada(Date enviamentDataProgramada) {
		this.enviamentDataProgramada = enviamentDataProgramada;
	}
	
	public NotificacioEnviamentTipusEnumDto getEnviamentTipus() {
		return enviamentTipus;
	}
	public void setEnviamentTipus(NotificacioEnviamentTipusEnumDto enviamentTipus) {
		this.enviamentTipus = enviamentTipus;
	}
	
	public InteressatDocumentTipusEnumDto getDestinatariDocumentTipus() {
		return destinatariDocumentTipus;
	}
	public void setDestinatariDocumentTipus(InteressatDocumentTipusEnumDto destinatariDocumentTipus) {
		this.destinatariDocumentTipus = destinatariDocumentTipus;
	}
	
	public String getDestinatariNom() {
		return destinatariNom;
	}
	public void setDestinatariNom(String destinatariNom) {
		this.destinatariNom = destinatariNom;
	}
	
	public String getDestinatariLlinatge1() {
		return destinatariLlinatge1;
	}
	public void setDestinatariLlinatge1(String destinatariLlinatge1) {
		this.destinatariLlinatge1 = destinatariLlinatge1;
	}
	
	public String getDestinatariLlinatge2() {
		return destinatariLlinatge2;
	}
	public void setDestinatariLlinatge2(String destinatariLlinatge2) {
		this.destinatariLlinatge2 = destinatariLlinatge2;
	}
	
	public String getDestinatariNif() {
		return destinatariNif;
	}
	public void setDestinatariNif(String destinatariNif) {
		this.destinatariNif = destinatariNif;
	}
	
	public String getDestinatariTelefon() {
		return destinatariTelefon;
	}
	public void setDestinatariTelefon(String destinatariTelefon) {
		this.destinatariTelefon = destinatariTelefon;
	}
	
	public String getDestinatariEmail() {
		return destinatariEmail;
	}
	public void setDestinatariEmail(String destinatariEmail) {
		this.destinatariEmail = destinatariEmail;
	}
	
	public boolean isDestinatariRepresentant() {
		return destinatariRepresentant;
	}
	public void setDestinatariRepresentant(boolean destinatariRepresentant) {
		this.destinatariRepresentant = destinatariRepresentant;
	}
	
	public String getDestinatariPaisCodi() {
		return destinatariPaisCodi;
	}
	public void setDestinatariPaisCodi(String destinatariPaisCodi) {
		this.destinatariPaisCodi = destinatariPaisCodi;
	}
	
	public String getDestinatariProvinciaCodi() {
		return destinatariProvinciaCodi;
	}
	public void setDestinatariProvinciaCodi(String destinatariProvinciaCodi) {
		this.destinatariProvinciaCodi = destinatariProvinciaCodi;
	}
	
	public String getDestinatariMunicipiCodi() {
		return destinatariMunicipiCodi;
	}
	public void setDestinatariMunicipiCodi(String destinatariMunicipiCodi) {
		this.destinatariMunicipiCodi = destinatariMunicipiCodi;
	}
	
	public boolean isDehObligat() {
		return dehObligat;
	}
	public void setDehObligat(boolean dehObligat) {
		this.dehObligat = dehObligat;
	}
	
	public String getDehNif() {
		return dehNif;
	}
	public void setDehNif(String dehNif) {
		this.dehNif = dehNif;
	}
	
	public String getNotificaReferencia() {
		return notificaReferencia;
	}
	public void setNotificaReferencia(String notificaReferencia) {
		this.notificaReferencia = notificaReferencia;
	}
	
	public ServeiTipusEnumDto getServeiTipus() {
		return serveiTipus;
	}
	public void setServeiTipus(ServeiTipusEnumDto serveiTipus) {
		this.serveiTipus = serveiTipus;
	}
	
	public String getTitularNom() {
		return titularNom;
	}
	public void setTitularNom(String titularNom) {
		this.titularNom = titularNom;
	}
	
	public String getTitularLlinatge1() {
		return titularLlinatge1;
	}
	public void setTitularLlinatge1(String titularLlinatge1) {
		this.titularLlinatge1 = titularLlinatge1;
	}
	
	public String getTitularLlinatge2() {
		return titularLlinatge2;
	}
	public void setTitularLlinatge2(String titularLlinatge2) {
		this.titularLlinatge2 = titularLlinatge2;
	}
	
	public String getTitularNif() {
		return titularNif;
	}
	public void setTitularNif(String titularNif) {
		this.titularNif = titularNif;
	}
	
	public String getTitularTelefon() {
		return titularTelefon;
	}
	public void setTitularTelefon(String titularTelefon) {
		this.titularTelefon = titularTelefon;
	}
	
	public String getTitularEmail() {
		return titularEmail;
	}
	public void setTitularEmail(String titularEmail) {
		this.titularEmail = titularEmail;
	}
	
	public String getPagadorCieCodiDir3() {
		return pagadorCieCodiDir3;
	}
	public void setPagadorCieCodiDir3(String pagadorCieCodiDir3) {
		this.pagadorCieCodiDir3 = pagadorCieCodiDir3;
	}
	
	public Date getPagadorCieDataVigencia() {
		return pagadorCieDataVigencia;
	}
	public void setPagadorCieDataVigencia(Date pagadorCieDataVigencia) {
		this.pagadorCieDataVigencia = pagadorCieDataVigencia;
	}
	
	public String getPagadorCorreusCodiDir3() {
		return pagadorCorreusCodiDir3;
	}
	public void setPagadorCorreusCodiDir3(String pagadorCorreusCodiDir3) {
		this.pagadorCorreusCodiDir3 = pagadorCorreusCodiDir3;
	}
	
	public String getPagadorCorreusContracteNum() {
		return pagadorCorreusContracteNum;
	}
	public void setPagadorCorreusContracteNum(String pagadorCorreusContracteNum) {
		this.pagadorCorreusContracteNum = pagadorCorreusContracteNum;
	}
	
	public String getPagadorCorreusCodiClientFacturacio() {
		return pagadorCorreusCodiClientFacturacio;
	}
	public void setPagadorCorreusCodiClientFacturacio(String pagadorCorreusCodiClientFacturacio) {
		this.pagadorCorreusCodiClientFacturacio = pagadorCorreusCodiClientFacturacio;
	}
	
	public Date getPagadorCorreusDataVigencia() {
		return pagadorCorreusDataVigencia;
	}
	public void setPagadorCorreusDataVigencia(Date pagadorCorreusDataVigencia) {
		this.pagadorCorreusDataVigencia = pagadorCorreusDataVigencia;
	}
	
	public String getSeuExpedientSerieDocumental() {
		return seuExpedientSerieDocumental;
	}
	public void setSeuExpedientSerieDocumental(String seuExpedientSerieDocumental) {
		this.seuExpedientSerieDocumental = seuExpedientSerieDocumental;
	}
	
	public String getSeuExpedientUnitatOrganitzativa() {
		return seuExpedientUnitatOrganitzativa;
	}
	public void setSeuExpedientUnitatOrganitzativa(String seuExpedientUnitatOrganitzativa) {
		this.seuExpedientUnitatOrganitzativa = seuExpedientUnitatOrganitzativa;
	}
	
	public String getSeuExpedientIdentificadorEni() {
		return seuExpedientIdentificadorEni;
	}
	public void setSeuExpedientIdentificadorEni(String seuExpedientIdentificadorEni) {
		this.seuExpedientIdentificadorEni = seuExpedientIdentificadorEni;
	}
	
	public String getSeuExpedientTitol() {
		return seuExpedientTitol;
	}
	public void setSeuExpedientTitol(String seuExpedientTitol) {
		this.seuExpedientTitol = seuExpedientTitol;
	}
	
	public String getSeuRegistreOficina() {
		return seuRegistreOficina;
	}
	public void setSeuRegistreOficina(String seuRegistreOficina) {
		this.seuRegistreOficina = seuRegistreOficina;
	}
	
	public String getSeuRegistreLlibre() {
		return seuRegistreLlibre;
	}
	public void setSeuRegistreLlibre(String seuRegistreLlibre) {
		this.seuRegistreLlibre = seuRegistreLlibre;
	}
	
	public InteressatIdiomaEnumDto getSeuIdioma() {
		return seuIdioma;
	}
	public void setSeuIdioma(InteressatIdiomaEnumDto seuIdioma) {
		this.seuIdioma = seuIdioma;
	}
	
	public String getSeuAvisTitol() {
		return seuAvisTitol;
	}
	public void setSeuAvisTitol(String seuAvisTitol) {
		this.seuAvisTitol = seuAvisTitol;
	}
	
	public String getSeuAvisText() {
		return seuAvisText;
	}
	public void setSeuAvisText(String seuAvisText) {
		this.seuAvisText = seuAvisText;
	}
	
	public String getSeuAvisTextMobil() {
		return seuAvisTextMobil;
	}
	public void setSeuAvisTextMobil(String seuAvisTextMobil) {
		this.seuAvisTextMobil = seuAvisTextMobil;
	}
	
	public String getSeuOficiTitol() {
		return seuOficiTitol;
	}
	public void setSeuOficiTitol(String seuOficiTitol) {
		this.seuOficiTitol = seuOficiTitol;
	}
	
	public String getSeuOficiText() {
		return seuOficiText;
	}
	public void setSeuOficiText(String seuOficiText) {
		this.seuOficiText = seuOficiText;
	}
	
	public String getProcedimentCodiSia() {
		return procedimentCodiSia;
	}
	public void setProcedimentCodiSia(String procedimentCodiSia) {
		this.procedimentCodiSia = procedimentCodiSia;
	}
	
	public Integer getRetardPostal() {
		return retardPostal;
	}
	public void setRetardPostal(Integer retardPostal) {
		this.retardPostal = retardPostal;
	}
	
	public String getDocumentHash() {
		return documentHash;
	}
	public void setDocumentHash(String documentHash) {
		this.documentHash = documentHash;
	}
	
	public boolean isDocumentNormalitzat() {
		return documentNormalitzat;
	}
	public void setDocumentNormalitzat(boolean documentNormalitzat) {
		this.documentNormalitzat = documentNormalitzat;
	}
	
	public boolean isDocumentGenerarCsv() {
		return documentGenerarCsv;
	}
	public void setDocumentGenerarCsv(boolean documentGenerarCsv) {
		this.documentGenerarCsv = documentGenerarCsv;
	}
	
	
	
	public String getDestinatariNomSencerRepresentantAmbDocument() {
		if (destinatariNif != null) {
			return destinatariNif + " - " + getDestinatariNomSencerRepresentant();
		} else {
			return getDestinatariNomSencerRepresentant();
		}
	}
	public String getDestinatariNomSencerRepresentant() {
		StringBuilder sb = new StringBuilder();
		sb.append(getDestinatariNomSencer());
		if (destinatariRepresentant) {
			sb.append(" (representant)");
		} else {
			sb.append(" (interessat)");
		}
		return sb.toString();
	}
	public String getDestinatariNomSencerAmbDocument() {
		if (destinatariNif != null) {
			return destinatariNif + " - " + getDestinatariNomSencer();
		} else {
			return getDestinatariNomSencer();
		}
	}
	public String getDestinatariNomSencer() {
		StringBuilder sb = new StringBuilder();
		if (destinatariNom != null)
			sb.append(destinatariNom);
		if (destinatariLlinatge1 != null) {
			sb.append(" ");
			sb.append(destinatariLlinatge1);
			if (destinatariLlinatge2 != null) {
				sb.append(" ");
				sb.append(destinatariLlinatge2);
			}
		}
		return sb.toString();
	}

	public String getDestinatari() {
		return getDestinatariNomSencerAmbDocument();
	}

	private static final long serialVersionUID = -139254994389509932L;

}
