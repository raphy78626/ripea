/**
 * 
 */
package es.caib.ripea.plugin.arxiu;

import java.util.Date;
import java.util.List;

/**
 * Estructura que representa un expedient de l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuExpedient {

	private String nodeId;
	private String titol;
	private String descripcio;

	private String eniVersio;
	private String eniIdentificador;
	private ArxiuOrigenContingut eniOrigen;
	private Date eniDataObertura;
	private String eniClassificacio;
	private ArxiuExpedientEstat eniEstat;
	private List<String> eniOrgans;
	private List<String> eniInteressats;
	private List<String> eniFirmaTipus;
	private List<String> eniFirmaCsv;
	private List<String> eniFirmaCsvDefinicio;

	private String serieDocumental;
	private String aplicacioCreacio;
	private String suport;

	private String json;

	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getTitol() {
		return titol;
	}
	public void setTitol(String titol) {
		this.titol = titol;
	}
	public String getDescripcio() {
		return descripcio;
	}
	public void setDescripcio(String descripcio) {
		this.descripcio = descripcio;
	}
	public String getEniVersio() {
		return eniVersio;
	}
	public void setEniVersio(String eniVersio) {
		this.eniVersio = eniVersio;
	}
	public String getEniIdentificador() {
		return eniIdentificador;
	}
	public void setEniIdentificador(String eniIdentificador) {
		this.eniIdentificador = eniIdentificador;
	}
	public ArxiuOrigenContingut getEniOrigen() {
		return eniOrigen;
	}
	public void setEniOrigen(ArxiuOrigenContingut eniOrigen) {
		this.eniOrigen = eniOrigen;
	}
	public Date getEniDataObertura() {
		return eniDataObertura;
	}
	public void setEniDataObertura(Date eniDataObertura) {
		this.eniDataObertura = eniDataObertura;
	}
	public String getEniClassificacio() {
		return eniClassificacio;
	}
	public void setEniClassificacio(String eniClassificacio) {
		this.eniClassificacio = eniClassificacio;
	}
	public ArxiuExpedientEstat getEniEstat() {
		return eniEstat;
	}
	public void setEniEstat(ArxiuExpedientEstat eniEstat) {
		this.eniEstat = eniEstat;
	}
	public List<String> getEniOrgans() {
		return eniOrgans;
	}
	public void setEniOrgans(List<String> eniOrgans) {
		this.eniOrgans = eniOrgans;
	}
	public List<String> getEniInteressats() {
		return eniInteressats;
	}
	public void setEniInteressats(List<String> eniInteressats) {
		this.eniInteressats = eniInteressats;
	}
	public List<String> getEniFirmaTipus() {
		return eniFirmaTipus;
	}
	public void setEniFirmaTipus(List<String> eniFirmaTipus) {
		this.eniFirmaTipus = eniFirmaTipus;
	}
	public List<String> getEniFirmaCsv() {
		return eniFirmaCsv;
	}
	public void setEniFirmaCsv(List<String> eniFirmaCsv) {
		this.eniFirmaCsv = eniFirmaCsv;
	}
	public List<String> getEniFirmaCsvDefinicio() {
		return eniFirmaCsvDefinicio;
	}
	public void setEniFirmaCsvDefinicio(List<String> eniFirmaCsvDefinicio) {
		this.eniFirmaCsvDefinicio = eniFirmaCsvDefinicio;
	}
	public String getSerieDocumental() {
		return serieDocumental;
	}
	public void setSerieDocumental(String serieDocumental) {
		this.serieDocumental = serieDocumental;
	}
	public String getAplicacioCreacio() {
		return aplicacioCreacio;
	}
	public void setAplicacioCreacio(String aplicacioCreacio) {
		this.aplicacioCreacio = aplicacioCreacio;
	}
	public String getSuport() {
		return suport;
	}
	public void setSuport(String suport) {
		this.suport = suport;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}

}
