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
	private DocumentNotificacioEnviamentTipusEnumDto tipusEnviament;
	private Date dataRecepcio;
	private String registreNumero;
	private InteressatDocumentTipusEnumDto destinatariDocumentTipus;
	private String destinatariDocumentNum;
	private String destinatariNom;
	private String destinatariLlinatge1;
	private String destinatariLlinatge2;
	private String destinatariPaisCodi;
	private String destinatariProvinciaCodi;
	private String destinatariMunicipiCodi;
	private String destinatariEmail;
	private boolean destinatariRepresentant;
	private String avisTitol;
	private String avisText;
	private String avisTextSms;
	private String oficiTitol;
	private String oficiText;
	private InteressatIdiomaEnumDto idioma;
	private Date enviamentData;
	private Integer enviamentCount;
	private boolean enviamentError;
	private String enviamentErrorDescripcio;
	private Date processamentData;
	private Integer processamentCount;
	private boolean processamentError;
	private String processamentErrorDescripcio;
	private String concepte;
	private String referencia;


	public DocumentNotificacioTipusEnumDto getTipus() {
		return tipus;
	}
	public void setTipus(DocumentNotificacioTipusEnumDto tipus) {
		this.tipus = tipus;
	}
	public DocumentNotificacioEnviamentTipusEnumDto getTipusEnviament() {
		return tipusEnviament;
	}
	public void setTipusEnviament(DocumentNotificacioEnviamentTipusEnumDto tipusEnviament) {
		this.tipusEnviament = tipusEnviament;
	}
	public Date getDataRecepcio() {
		return dataRecepcio;
	}
	public void setDataRecepcio(Date dataRecepcio) {
		this.dataRecepcio = dataRecepcio;
	}
	public String getRegistreNumero() {
		return registreNumero;
	}
	public void setRegistreNumero(String registreNumero) {
		this.registreNumero = registreNumero;
	}
	public InteressatDocumentTipusEnumDto getDestinatariDocumentTipus() {
		return destinatariDocumentTipus;
	}
	public void setDestinatariDocumentTipus(InteressatDocumentTipusEnumDto destinatariDocumentTipus) {
		this.destinatariDocumentTipus = destinatariDocumentTipus;
	}
	public String getDestinatariDocumentNum() {
		return destinatariDocumentNum;
	}
	public void setDestinatariDocumentNum(String destinatariDocumentNum) {
		this.destinatariDocumentNum = destinatariDocumentNum;
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
	public String getAvisTitol() {
		return avisTitol;
	}
	public void setAvisTitol(String avisTitol) {
		this.avisTitol = avisTitol;
	}
	public String getAvisText() {
		return avisText;
	}
	public void setAvisText(String avisText) {
		this.avisText = avisText;
	}
	public String getAvisTextSms() {
		return avisTextSms;
	}
	public void setAvisTextSms(String avisTextSms) {
		this.avisTextSms = avisTextSms;
	}
	public String getOficiTitol() {
		return oficiTitol;
	}
	public void setOficiTitol(String oficiTitol) {
		this.oficiTitol = oficiTitol;
	}
	public String getOficiText() {
		return oficiText;
	}
	public void setOficiText(String oficiText) {
		this.oficiText = oficiText;
	}
	public InteressatIdiomaEnumDto getIdioma() {
		return idioma;
	}
	public void setIdioma(InteressatIdiomaEnumDto idioma) {
		this.idioma = idioma;
	}
	public Date getEnviamentData() {
		return enviamentData;
	}
	public void setEnviamentData(Date enviamentData) {
		this.enviamentData = enviamentData;
	}
	public Integer getEnviamentCount() {
		return enviamentCount;
	}
	public void setEnviamentCount(Integer enviamentCount) {
		this.enviamentCount = enviamentCount;
	}
	public boolean isEnviamentError() {
		return enviamentError;
	}
	public void setEnviamentError(boolean enviamentError) {
		this.enviamentError = enviamentError;
	}
	public String getEnviamentErrorDescripcio() {
		return enviamentErrorDescripcio;
	}
	public void setEnviamentErrorDescripcio(String enviamentErrorDescripcio) {
		this.enviamentErrorDescripcio = enviamentErrorDescripcio;
	}
	public Date getProcessamentData() {
		return processamentData;
	}
	public void setProcessamentData(Date processamentData) {
		this.processamentData = processamentData;
	}
	public Integer getProcessamentCount() {
		return processamentCount;
	}
	public void setProcessamentCount(Integer processamentCount) {
		this.processamentCount = processamentCount;
	}
	public boolean isProcessamentError() {
		return processamentError;
	}
	public void setProcessamentError(boolean processamentError) {
		this.processamentError = processamentError;
	}
	public String getProcessamentErrorDescripcio() {
		return processamentErrorDescripcio;
	}
	public void setProcessamentErrorDescripcio(String processamentErrorDescripcio) {
		this.processamentErrorDescripcio = processamentErrorDescripcio;
	}
	public String getDestinatariNomSencerRepresentantAmbDocument() {
		if (destinatariDocumentNum != null) {
			return destinatariDocumentNum + " - " + getDestinatariNomSencerRepresentant();
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
		if (destinatariDocumentNum != null) {
			return destinatariDocumentNum + " - " + getDestinatariNomSencer();
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

	public String getConcepte() {
		return concepte;
	}
	public void setConcepte(String concepte) {
		this.concepte = concepte;
	}

	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	private static final long serialVersionUID = -139254994389509932L;

}
