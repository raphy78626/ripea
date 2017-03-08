/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * Informació d'un contingut emmagatzemat amb el plugin d'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuPluginInfoDto implements Serializable {

	private String nodeId;

	private String versioId;
	private String versioNodeId;
	private String nom;
	private String descripcio;

	private String eniVersio;
	private String eniIdentificador;
	private String eniOrigen;
	private Date eniDataInicial;
	private String eniEstatElaboracio;
	private String eniTipusDocumental;
	private String eniFormatNom;
	private String eniFormatExtensio;
	private String eniClassificacio;
	private String eniEstat;
	private List<String> eniOrgans;
	private List<String> eniInteressats;
	private List<String> eniFirmaTipus;
	private List<String> eniFirmaCsv;
	private List<String> eniFirmaCsvDefinicio;
	private String eniDocumentOrigenId;

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
	public String getVersioId() {
		return versioId;
	}
	public void setVersioId(String versioId) {
		this.versioId = versioId;
	}
	public String getVersioNodeId() {
		return versioNodeId;
	}
	public void setVersioNodeId(String versioNodeId) {
		this.versioNodeId = versioNodeId;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
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
	public String getEniOrigen() {
		return eniOrigen;
	}
	public void setEniOrigen(String eniOrigen) {
		this.eniOrigen = eniOrigen;
	}
	public Date getEniDataInicial() {
		return eniDataInicial;
	}
	public void setEniDataInicial(Date eniDataInicial) {
		this.eniDataInicial = eniDataInicial;
	}
	public String getEniEstatElaboracio() {
		return eniEstatElaboracio;
	}
	public void setEniEstatElaboracio(String eniEstatElaboracio) {
		this.eniEstatElaboracio = eniEstatElaboracio;
	}
	public String getEniTipusDocumental() {
		return eniTipusDocumental;
	}
	public void setEniTipusDocumental(String eniTipusDocumental) {
		this.eniTipusDocumental = eniTipusDocumental;
	}
	public String getEniFormatNom() {
		return eniFormatNom;
	}
	public void setEniFormatNom(String eniFormatNom) {
		this.eniFormatNom = eniFormatNom;
	}
	public String getEniFormatExtensio() {
		return eniFormatExtensio;
	}
	public void setEniFormatExtensio(String eniFormatExtensio) {
		this.eniFormatExtensio = eniFormatExtensio;
	}
	public String getEniClassificacio() {
		return eniClassificacio;
	}
	public void setEniClassificacio(String eniClassificacio) {
		this.eniClassificacio = eniClassificacio;
	}
	public String getEniEstat() {
		return eniEstat;
	}
	public void setEniEstat(String eniEstat) {
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
	public String getEniDocumentOrigenId() {
		return eniDocumentOrigenId;
	}
	public void setEniDocumentOrigenId(String eniDocumentOrigenId) {
		this.eniDocumentOrigenId = eniDocumentOrigenId;
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

	private static final long serialVersionUID = -2124829280908976623L;

}
