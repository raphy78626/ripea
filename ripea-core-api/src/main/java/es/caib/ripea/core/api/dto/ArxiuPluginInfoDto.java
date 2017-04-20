/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Informació d'un contingut emmagatzemat amb el plugin d'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuPluginInfoDto implements Serializable {

	private String nodeId;
	private String nom;
	private String descripcio;
	private String serieDocumental;
	private String aplicacio;

	private String documentVersioId;
	private String documentVersioNodeId;

	private String eniVersio;
	private String eniIdentificador;
	private DocumentNtiOrigenEnumDto eniOrigen;
	private Date eniDataObertura;
	private String eniClassificacio;
	private ExpedientEstatEnumDto eniEstat;
	private List<String> eniInteressats;
	private Date eniDataCaptura;
	private DocumentNtiEstadoElaboracionEnumDto eniEstatElaboracio;
	private DocumentNtiTipoDocumentalEnumDto eniTipusDocumental;
	private String eniFormatNom;
	private String eniFormatExtensio;
	private String eniDocumentOrigenId;
	private List<String> eniOrgans;
	private List<String> eniFirmaTipus;
	private List<String> eniFirmaCsv;
	private List<String> eniFirmaCsvDefinicio;

	protected Map<String, Object> metadades;
	protected List<String> aspectes;
	protected List<ArxiuPluginNodeFillDto> fills;

	private String codiFontPeticio;
	private String codiFontResposta;



	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
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
	public String getSerieDocumental() {
		return serieDocumental;
	}
	public void setSerieDocumental(String serieDocumental) {
		this.serieDocumental = serieDocumental;
	}
	public String getAplicacio() {
		return aplicacio;
	}
	public void setAplicacio(String aplicacio) {
		this.aplicacio = aplicacio;
	}
	public String getDocumentVersioId() {
		return documentVersioId;
	}
	public void setDocumentVersioId(String documentVersioId) {
		this.documentVersioId = documentVersioId;
	}
	public String getDocumentVersioNodeId() {
		return documentVersioNodeId;
	}
	public void setDocumentVersioNodeId(String documentVersioNodeId) {
		this.documentVersioNodeId = documentVersioNodeId;
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
	public DocumentNtiOrigenEnumDto getEniOrigen() {
		return eniOrigen;
	}
	public void setEniOrigen(DocumentNtiOrigenEnumDto eniOrigen) {
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
	public ExpedientEstatEnumDto getEniEstat() {
		return eniEstat;
	}
	public void setEniEstat(ExpedientEstatEnumDto eniEstat) {
		this.eniEstat = eniEstat;
	}
	public List<String> getEniInteressats() {
		return eniInteressats;
	}
	public void setEniInteressats(List<String> eniInteressats) {
		this.eniInteressats = eniInteressats;
	}
	public Date getEniDataCaptura() {
		return eniDataCaptura;
	}
	public void setEniDataCaptura(Date eniDataCaptura) {
		this.eniDataCaptura = eniDataCaptura;
	}
	public DocumentNtiEstadoElaboracionEnumDto getEniEstatElaboracio() {
		return eniEstatElaboracio;
	}
	public void setEniEstatElaboracio(DocumentNtiEstadoElaboracionEnumDto eniEstatElaboracio) {
		this.eniEstatElaboracio = eniEstatElaboracio;
	}
	public DocumentNtiTipoDocumentalEnumDto getEniTipusDocumental() {
		return eniTipusDocumental;
	}
	public void setEniTipusDocumental(DocumentNtiTipoDocumentalEnumDto eniTipusDocumental) {
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
	public String getEniDocumentOrigenId() {
		return eniDocumentOrigenId;
	}
	public void setEniDocumentOrigenId(String eniDocumentOrigenId) {
		this.eniDocumentOrigenId = eniDocumentOrigenId;
	}
	public List<String> getEniOrgans() {
		return eniOrgans;
	}
	public void setEniOrgans(List<String> eniOrgans) {
		this.eniOrgans = eniOrgans;
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
	public Map<String, Object> getMetadades() {
		return metadades;
	}
	public void setMetadades(Map<String, Object> metadades) {
		this.metadades = metadades;
	}
	public List<String> getAspectes() {
		return aspectes;
	}
	public void setAspectes(List<String> aspectes) {
		this.aspectes = aspectes;
	}
	public List<ArxiuPluginNodeFillDto> getFills() {
		return fills;
	}
	public void setFills(List<ArxiuPluginNodeFillDto> fills) {
		this.fills = fills;
	}

	public String getCodiFontPeticio() {
		return codiFontPeticio;
	}
	public void setCodiFontPeticio(String codiFontPeticio) {
		this.codiFontPeticio = codiFontPeticio;
	}
	public String getCodiFontResposta() {
		return codiFontResposta;
	}
	public void setCodiFontResposta(String codiFontResposta) {
		this.codiFontResposta = codiFontResposta;
	}

	private static final long serialVersionUID = -2124829280908976623L;

}
