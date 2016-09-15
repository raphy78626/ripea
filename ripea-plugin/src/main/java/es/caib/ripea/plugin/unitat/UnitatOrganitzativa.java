/**
 * 
 */
package es.caib.ripea.plugin.unitat;

import java.io.Serializable;
import java.util.Date;

/**
 * Informació d'una unitat organitzativa.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class UnitatOrganitzativa implements Serializable {

	private String codi;
	private String denominacio;
	private String nifCif;
	private String nivellAdministracio;
	private String tipusEntitatPublica;
	private String tipusUnitatOrganica;
	private String poder;
	private String sigles;
	private String codiUnitatSuperior;
	private String codiUnitatArrel;
	private Long nivellJerarquic;
	private Date dataCreacioOficial;
	private Date dataSupressioOficial;
	private Date dataExtincioFuncional;
	private Date dataAnulacio;
	private String estat; // V: Vigente, E: Extinguido, A: Anulado, T: Transitorio

	private String codiPais;
	private String codiComunitat;
	private String codiProvincia;
	private String codiPostal;
	private String nomLocalitat;
	private String adressa;


	public UnitatOrganitzativa(
			String codi,
			String denominacio,
			String nifCif,
			Date dataCreacioOficial,
			String estat) {
		this.codi = codi;
		this.denominacio = denominacio;
		this.nifCif = nifCif;
		this.dataCreacioOficial = dataCreacioOficial;
		this.estat = estat;
	}
	public UnitatOrganitzativa(
			String codi,
			String denominacio,
			String nifCif,
			Date dataCreacioOficial,
			String estat,
			String codiUnitatSuperior,
			String codiUnitatArrel) {
		this.codi = codi;
		this.denominacio = denominacio;
		this.nifCif = nifCif;
		this.dataCreacioOficial = dataCreacioOficial;
		this.estat = estat;
		this.codiUnitatSuperior = codiUnitatSuperior;
		this.codiUnitatArrel = codiUnitatArrel;
	}
	public UnitatOrganitzativa(
			String codi,
			String denominacio,
			String nifCif,
			Date dataCreacioOficial,
			String estat,
			String codiUnitatSuperior,
			String codiUnitatArrel,
			Long codiPais,
			Long codiComunitat,
			Long codiProvincia,
			String codiPostal,
			String nomLocalitat,
			String adressa) {
		this.codi = codi;
		this.denominacio = denominacio;
		this.nifCif = nifCif;
		this.dataCreacioOficial = dataCreacioOficial;
		this.estat = estat;
		this.codiUnitatSuperior = codiUnitatSuperior;
		this.codiUnitatArrel = codiUnitatArrel;
		this.codiPais = codiPais != null? codiPais.toString() : "";
		this.codiComunitat = codiComunitat != null? codiComunitat.toString() : "";
		this.codiProvincia = codiProvincia != null? codiProvincia.toString() : "";
		this.codiPostal = codiPostal;
		this.nomLocalitat = nomLocalitat;
		this.adressa = adressa;
	}

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
	public String getNivellAdministracio() {
		return nivellAdministracio;
	}
	public void setNivellAdministracio(String nivellAdministracio) {
		this.nivellAdministracio = nivellAdministracio;
	}
	public String getTipusEntitatPublica() {
		return tipusEntitatPublica;
	}
	public void setTipusEntitatPublica(String tipusEntitatPublica) {
		this.tipusEntitatPublica = tipusEntitatPublica;
	}
	public String getTipusUnitatOrganica() {
		return tipusUnitatOrganica;
	}
	public void setTipusUnitatOrganica(String tipusUnitatOrganica) {
		this.tipusUnitatOrganica = tipusUnitatOrganica;
	}
	public String getPoder() {
		return poder;
	}
	public void setPoder(String poder) {
		this.poder = poder;
	}
	public String getSigles() {
		return sigles;
	}
	public void setSigles(String sigles) {
		this.sigles = sigles;
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
	public Long getNivellJerarquic() {
		return nivellJerarquic;
	}
	public void setNivellJerarquic(Long nivellJerarquic) {
		this.nivellJerarquic = nivellJerarquic;
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
	public String getCodiPais() {
		return codiPais;
	}
	public void setCodiPais(String codiPais) {
		this.codiPais = codiPais;
	}
	public String getCodiComunitat() {
		return codiComunitat;
	}
	public void setCodiComunitat(String codiComunitat) {
		this.codiComunitat = codiComunitat;
	}
	public String getCodiProvincia() {
		return codiProvincia;
	}
	public void setCodiProvincia(String codiProvincia) {
		this.codiProvincia = codiProvincia;
	}
	public String getCodiPostal() {
		return codiPostal;
	}
	public void setCodiPostal(String codiPostal) {
		this.codiPostal = codiPostal;
	}
	public String getNomLocalitat() {
		return nomLocalitat;
	}
	public void setNomLocalitat(String nomLocalitat) {
		this.nomLocalitat = nomLocalitat;
	}
	public String getAdressa() {
		return adressa;
	}
	public void setAdressa(String adressa) {
		this.adressa = adressa;
	}

	private static final long serialVersionUID = -5602898182576627524L;

}
