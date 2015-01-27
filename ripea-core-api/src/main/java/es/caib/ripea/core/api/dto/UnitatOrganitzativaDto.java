/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Informació d'una unitat organitzativa.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class UnitatOrganitzativaDto implements Serializable {

	private String codi;
	private String denominacio;
	private String nifCif;
	private String codiUnitatSuperior;
	private String codiUnitatArrel;
	private Date dataCreacioOficial;
	private Date dataSupressioOficial;
	private Date dataExtincioFuncional;
	private Date dataAnulacio;
	private String estat; // V: Vigente, E: Extinguido, A: Anulado, T: Transitorio



	public String getCodi() {
		return codi;
	}
	public void setCodi(String codi) {
		this.codi = codi;
	}
	public String getDenominacio() {
		return denominacio;
	}
	public void setDenominacio(String denominacio) {
		this.denominacio = denominacio;
	}
	public String getNifCif() {
		return nifCif;
	}
	public void setNifCif(String nifCif) {
		this.nifCif = nifCif;
	}
	public String getCodiUnitatSuperior() {
		return codiUnitatSuperior;
	}
	public void setCodiUnitatSuperior(String codiUnitatSuperior) {
		this.codiUnitatSuperior = codiUnitatSuperior;
	}
	public String getCodiUnitatArrel() {
		return codiUnitatArrel;
	}
	public void setCodiUnitatArrel(String codiUnitatArrel) {
		this.codiUnitatArrel = codiUnitatArrel;
	}
	public String getEstat() {
		return estat;
	}
	public void setEstat(String estat) {
		this.estat = estat;
	}
	public Date getDataCreacioOficial() {
		return dataCreacioOficial;
	}
	public void setDataCreacioOficial(Date dataCreacioOficial) {
		this.dataCreacioOficial = dataCreacioOficial;
	}
	public Date getDataSupressioOficial() {
		return dataSupressioOficial;
	}
	public void setDataSupressioOficial(Date dataSupressioOficial) {
		this.dataSupressioOficial = dataSupressioOficial;
	}
	public Date getDataExtincioFuncional() {
		return dataExtincioFuncional;
	}
	public void setDataExtincioFuncional(Date dataExtincioFuncional) {
		this.dataExtincioFuncional = dataExtincioFuncional;
	}
	public Date getDataAnulacio() {
		return dataAnulacio;
	}
	public void setDataAnulacio(Date dataAnulacio) {
		this.dataAnulacio = dataAnulacio;
	}

	private static final long serialVersionUID = -5602898182576627524L;

}
