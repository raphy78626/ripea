/**
 * 
 */
package es.caib.ripea.plugin.registre;

import java.util.Date;


/**
 * Classe que representa un annex d'una anotació de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class RegistreAnnex {

	private String titol;
	private String fitxerNom;
	private byte[] fitxerContingut;
	private String fitxerTipusMime;
	private String ntiTipoDocumental;
	private String validesa;
	private String tipus;
	private String observacions;
	private String origen;
	private Date dataCaptura;
	private Integer firmaMode;
	private String firmaFitxerNom;
	private byte[] firmaFitxerContingut;
	private String firmaFitxerTipusMime;
	private String firmaCsv;



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
	public byte[] getFitxerContingut() {
		return fitxerContingut;
	}
	public void setFitxerContingut(byte[] fitxerContingut) {
		this.fitxerContingut = fitxerContingut;
	}
	public String getFitxerTipusMime() {
		return fitxerTipusMime;
	}
	public void setFitxerTipusMime(String fitxerTipusMime) {
		this.fitxerTipusMime = fitxerTipusMime;
	}
	public String getNtiTipoDocumental() {
		return ntiTipoDocumental;
	}
	public void setNtiTipoDocumental(String ntiTipoDocumental) {
		this.ntiTipoDocumental = ntiTipoDocumental;
	}
	public String getValidesa() {
		return validesa;
	}
	public void setValidesa(String validesa) {
		this.validesa = validesa;
	}
	public String getTipus() {
		return tipus;
	}
	public void setTipus(String tipus) {
		this.tipus = tipus;
	}
	public String getObservacions() {
		return observacions;
	}
	public void setObservacions(String observacions) {
		this.observacions = observacions;
	}
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	public Date getDataCaptura() {
		return dataCaptura;
	}
	public void setDataCaptura(Date dataCaptura) {
		this.dataCaptura = dataCaptura;
	}
	public Integer getFirmaMode() {
		return firmaMode;
	}
	public void setFirmaMode(Integer firmaMode) {
		this.firmaMode = firmaMode;
	}
	public String getFirmaFitxerNom() {
		return firmaFitxerNom;
	}
	public void setFirmaFitxerNom(String firmaFitxerNom) {
		this.firmaFitxerNom = firmaFitxerNom;
	}
	public byte[] getFirmaFitxerContingut() {
		return firmaFitxerContingut;
	}
	public void setFirmaFitxerContingut(byte[] firmaFitxerContingut) {
		this.firmaFitxerContingut = firmaFitxerContingut;
	}
	public String getFirmaFitxerTipusMime() {
		return firmaFitxerTipusMime;
	}
	public void setFirmaFitxerTipusMime(String firmaFitxerTipusMime) {
		this.firmaFitxerTipusMime = firmaFitxerTipusMime;
	}
	public String getFirmaCsv() {
		return firmaCsv;
	}
	public void setFirmaCsv(String firmaCsv) {
		this.firmaCsv = firmaCsv;
	}

}