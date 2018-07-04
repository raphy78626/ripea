/**
 * 
 */
package es.caib.ripea.plugin.distribucio;

import java.util.Date;
import java.util.List;

import es.caib.ripea.core.api.registre.RegistreAnnexElaboracioEstatEnum;
import es.caib.ripea.core.api.registre.RegistreAnnexNtiTipusDocumentEnum;
import es.caib.ripea.core.api.registre.RegistreAnnexOrigenEnum;

/**
 * Classe que representa un annex d'una anotació de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class DistribucioRegistreAnnex {

	private String id;
	private Date dataCaptura;
	private String gesdocDocumentId;
	private String origenCiutadaAdmin;
	private String ntiTipusDocument;
	private String sicresTipusDocument;
	private String ntiElaboracioEstat;
	private String titol;
	private String fitxerNom;
	private int fitxerTamany;
	private String fitxerTipusMime;
	private String fitxerArxiuUuid;
	private List<DistribucioRegistreFirma> firmes;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitol() {
		return titol;
	}
	public void setTitol(String titol) {
		this.titol = titol;
	}
	public String getFitxerNom() {
		return fitxerNom;
	}
	public void setFitxerNom(String fitxerNom) {
		this.fitxerNom = fitxerNom;
	}
	public int getFitxerTamany() {
		return fitxerTamany;
	}
	public void setFitxerTamany(int fitxerTamany) {
		this.fitxerTamany = fitxerTamany;
	}
	public String getFitxerTipusMime() {
		return fitxerTipusMime;
	}
	public void setFitxerTipusMime(String fitxerTipusMime) {
		this.fitxerTipusMime = fitxerTipusMime;
	}
	public String getFitxerArxiuUuid() {
		return fitxerArxiuUuid;
	}
	public void setFitxerArxiuUuid(String fitxerArxiuUuid) {
		this.fitxerArxiuUuid = fitxerArxiuUuid;
	}
	
	public RegistreAnnexOrigenEnum getOrigenCiutadaAdmin() {
		return RegistreAnnexOrigenEnum.valorAsEnum(this.origenCiutadaAdmin);
	}
	public RegistreAnnexNtiTipusDocumentEnum getNtiTipusDocument() {
		return RegistreAnnexNtiTipusDocumentEnum.valorAsEnum(this.ntiTipusDocument);
	}
	public RegistreAnnexElaboracioEstatEnum getNtiElaboracioEstat() {
		return RegistreAnnexElaboracioEstatEnum.valorAsEnum(this.ntiElaboracioEstat);
	}

	public Date getDataCaptura() {
		return dataCaptura;
	}
	public void setDataCaptura(Date dataCaptura) {
		this.dataCaptura = dataCaptura;
	}
	public String getGesdocDocumentId() {
		return gesdocDocumentId;
	}
	public void setGesdocDocumentId(String gesdocDocumentId) {
		this.gesdocDocumentId = gesdocDocumentId;
	}
	public String getSicresTipusDocument() {
		return sicresTipusDocument;
	}
	public void setSicresTipusDocument(String sicresTipusDocument) {
		this.sicresTipusDocument = sicresTipusDocument;
	}
	public List<DistribucioRegistreFirma> getFirmes() {
		return firmes;
	}
	public void setFirmes(List<DistribucioRegistreFirma> firmes) {
		this.firmes = firmes;
	}
}
