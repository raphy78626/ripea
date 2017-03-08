/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu.client;

import java.util.Date;
import java.util.List;

import es.caib.arxiudigital.apirest.constantes.EstadosExpediente;
import es.caib.arxiudigital.apirest.constantes.OrigenesContenido;

/**
 * Estructura que representa un expedient de l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuFile {

	private String nodeId;
	private String titol;
	private String descripcio;

	private String eniVersio; // eni:v_nti
	private String eniIdentificador; // eni:id
	private OrigenesContenido eniOrigen; // eni:origen
	private List<String> eniOrgans; // eni:organo
	private Date eniDataObertura; // eni:fecha_inicio
	private String eniClassificacio; // eni:id_tramite
	private EstadosExpediente eniEstat; // eni:estado_exp
	private List<String> eniInteressats; // eni:interesados_exp
	private List<String> eniFirmaTipus; // eni:tipoFirma
	private List<String> eniFirmaCsv; // eni:csv
	private List<String> eniFirmaCsvDefinicio; // eni:def_csv

	private String serieDocumental; // eni:cod_clasificacion
	private String aplicacioCreacio; // eni:app_tramite_exp
	private String suport; // eni:soporte

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
	public OrigenesContenido getEniOrigen() {
		return eniOrigen;
	}
	public void setEniOrigen(OrigenesContenido eniOrigen) {
		this.eniOrigen = eniOrigen;
	}
	public List<String> getEniOrgans() {
		return eniOrgans;
	}
	public void setEniOrgans(List<String> eniOrgans) {
		this.eniOrgans = eniOrgans;
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
	public EstadosExpediente getEniEstat() {
		return eniEstat;
	}
	public void setEniEstat(EstadosExpediente eniEstat) {
		this.eniEstat = eniEstat;
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
