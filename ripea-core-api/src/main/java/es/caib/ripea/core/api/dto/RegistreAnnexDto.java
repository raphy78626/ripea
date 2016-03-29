/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Informació d'un document d'una anotació de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class RegistreAnnexDto implements Serializable {

	private Long id;
	private String titol;
	private String fitxerNom;
	private int fitxerTamany;
	private String fitxerTipusMime;
	private String ntiTipoDocumental;
	private RegistreDocumentValidesaEnumDto validesa;
	private RegistreDocumentTipusEnumDto tipus;
	private String observacions;
	private RegistreAnnexOrigenEnumDto origen;
	private Date dataCaptura;
	private RegistreAnnexFirmaModeEnumDto firmaMode;



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
	public String getNtiTipoDocumental() {
		return ntiTipoDocumental;
	}
	public void setNtiTipoDocumental(String ntiTipoDocumental) {
		this.ntiTipoDocumental = ntiTipoDocumental;
	}
	public RegistreDocumentValidesaEnumDto getValidesa() {
		return validesa;
	}
	public void setValidesa(RegistreDocumentValidesaEnumDto validesa) {
		this.validesa = validesa;
	}
	public RegistreDocumentTipusEnumDto getTipus() {
		return tipus;
	}
	public void setTipus(RegistreDocumentTipusEnumDto tipus) {
		this.tipus = tipus;
	}
	public String getObservacions() {
		return observacions;
	}
	public void setObservacions(String observacions) {
		this.observacions = observacions;
	}
	public RegistreAnnexOrigenEnumDto getOrigen() {
		return origen;
	}
	public void setOrigen(RegistreAnnexOrigenEnumDto origen) {
		this.origen = origen;
	}
	public Date getDataCaptura() {
		return dataCaptura;
	}
	public void setDataCaptura(Date dataCaptura) {
		this.dataCaptura = dataCaptura;
	}
	public RegistreAnnexFirmaModeEnumDto getFirmaMode() {
		return firmaMode;
	}
	public void setFirmaMode(RegistreAnnexFirmaModeEnumDto firmaMode) {
		this.firmaMode = firmaMode;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
