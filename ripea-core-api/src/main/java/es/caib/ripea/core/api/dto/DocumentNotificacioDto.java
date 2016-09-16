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
	private Date dataRecepcio;
	private String registreNumero;
	private String destinatariDocumentTipus;
	private String destinatariDocumentNum;
	private String destinatariNom;
	private String destinatariLlinatge1;
	private String destinatariLlinatge2;
	private String destinatariEmail;
	private boolean destinatariRepresentant;
	

	public DocumentNotificacioTipusEnumDto getTipus() {
		return tipus;
	}
	public void setTipus(DocumentNotificacioTipusEnumDto tipus) {
		this.tipus = tipus;
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
	public String getDestinatariDocumentTipus() {
		return destinatariDocumentTipus;
	}
	public void setDestinatariDocumentTipus(String destinatariDocumentTipus) {
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

	public String getNomSencerRepresentantAmbDocument() {
		if (destinatariDocumentNum != null) {
			return destinatariDocumentNum + " - " + getNomSencerRepresentant();
		} else {
			return getNomSencerRepresentant();
		}
	}
	public String getNomSencerRepresentant() {
		StringBuilder sb = new StringBuilder();
		sb.append(getNomSencer());
		if (destinatariRepresentant) {
			sb.append(" (representant)");
		} else {
			sb.append(" (interessat)");
		}
		return sb.toString();
	}
	public String getNomSencerAmbDocument() {
		if (destinatariDocumentNum != null) {
			return destinatariDocumentNum + " - " + getNomSencer();
		} else {
			return getNomSencer();
		}
	}
	public String getNomSencer() {
		StringBuilder sb = new StringBuilder();
		sb.append(destinatariNom);
		if (destinatariLlinatge1 != null) {
			sb.append(" ");
			sb.append(destinatariLlinatge1);
			if (destinatariLlinatge2 != null) {
				sb.append(" ");
				sb.append(destinatariLlinatge2);
			}
		}
		if (destinatariRepresentant) {
			sb.append(" (representant)");
		} else {
			sb.append(" ()");
		}
		return sb.toString();
	}

	private static final long serialVersionUID = -139254994389509932L;

}
