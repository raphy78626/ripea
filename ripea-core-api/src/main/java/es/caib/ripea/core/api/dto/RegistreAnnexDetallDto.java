/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe que representa una anotació de registre amb id.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class RegistreAnnexDetallDto implements Serializable {

	private Long id;
	private String titol;
	private String fitxerNom;
	private int fitxerTamany;
	private String fitxerTipusMime;
	private Date dataCaptura;
	private String localitzacio;
	private String origenCiutadaAdmin;
	private String ntiTipusDocument;
	private String sicresTipusDocument;
	private String ntiElaboracioEstat;
	private String observacions;
	private String firmaFitxerNom;
	private Integer firmaFitxerTamany;
	private Integer firmaMode;
	private String firmaCsv;
	private String firmaFitxerTipusMime;
	private String timestamp;
	private String validacioOCSP;
	private boolean ambDocument;
	private boolean ambFirma;
	
	private static final long serialVersionUID = -8656873728034274066L;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getFitxerTipusMime() {
		return fitxerTipusMime;
	}

	public void setFitxerTipusMime(String fitxerTipusMime) {
		this.fitxerTipusMime = fitxerTipusMime;
	}

	public Date getDataCaptura() {
		return dataCaptura;
	}

	public void setDataCaptura(Date dataCaptura) {
		this.dataCaptura = dataCaptura;
	}

	public String getLocalitzacio() {
		return localitzacio;
	}

	public void setLocalitzacio(String localitzacio) {
		this.localitzacio = localitzacio;
	}

	public String getOrigenCiutadaAdmin() {
		return origenCiutadaAdmin;
	}

	public void setOrigenCiutadaAdmin(String origenCiutadaAdmin) {
		this.origenCiutadaAdmin = origenCiutadaAdmin;
	}

	public String getNtiTipusDocument() {
		return ntiTipusDocument;
	}

	public void setNtiTipusDocument(String ntiTipusDocument) {
		this.ntiTipusDocument = ntiTipusDocument;
	}

	public String getSicresTipusDocument() {
		return sicresTipusDocument;
	}

	public void setSicresTipusDocument(String sicresTipusDocument) {
		this.sicresTipusDocument = sicresTipusDocument;
	}

	public String getNtiElaboracioEstat() {
		return ntiElaboracioEstat;
	}

	public void setNtiElaboracioEstat(String ntiElaboracioEstat) {
		this.ntiElaboracioEstat = ntiElaboracioEstat;
	}

	public String getObservacions() {
		return observacions;
	}

	public void setObservacions(String observacions) {
		this.observacions = observacions;
	}

	public String getFirmaFitxerNom() {
		return firmaFitxerNom;
	}

	public void setFirmaFitxerNom(String firmaFitxerNom) {
		this.firmaFitxerNom = firmaFitxerNom;
	}

	public Integer getFirmaMode() {
		return firmaMode;
	}

	public void setFirmaMode(Integer firmaMode) {
		this.firmaMode = firmaMode;
	}

	public String getFirmaCsv() {
		return firmaCsv;
	}

	public void setFirmaCsv(String firmaCsv) {
		this.firmaCsv = firmaCsv;
	}

	public String getFirmaFitxerTipusMime() {
		return firmaFitxerTipusMime;
	}

	public void setFirmaFitxerTipusMime(String firmaFitxerTipusMime) {
		this.firmaFitxerTipusMime = firmaFitxerTipusMime;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getValidacioOCSP() {
		return validacioOCSP;
	}

	public void setValidacioOCSP(String validacioOCSP) {
		this.validacioOCSP = validacioOCSP;
	}

	public boolean isAmbDocument() {
		return ambDocument;
	}

	public void setAmbDocument(boolean ambDocument) {
		this.ambDocument = ambDocument;
	}

	public boolean isAmbFirma() {
		return ambFirma;
	}

	public void setAmbFirma(boolean ambFirma) {
		this.ambFirma = ambFirma;
	}

	public int getFitxerTamany() {
		return fitxerTamany;
	}

	public void setFitxerTamany(int fitxerTamany) {
		this.fitxerTamany = fitxerTamany;
	}

	public Integer getFirmaFitxerTamany() {
		return firmaFitxerTamany;
	}

	public void setFirmaFitxerTamany(Integer firmaFitxerTamany) {
		this.firmaFitxerTamany = firmaFitxerTamany;
	}

}
