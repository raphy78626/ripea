/**
 * 
 */
package es.caib.ripea.plugin.caib.arxiu.client;

import java.util.Date;
import java.util.List;

import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.Content;
import es.caib.arxiudigital.apirest.constantes.EstadosElaboracion;
import es.caib.arxiudigital.apirest.constantes.ExtensionesFichero;
import es.caib.arxiudigital.apirest.constantes.FormatosFichero;
import es.caib.arxiudigital.apirest.constantes.OrigenesContenido;
import es.caib.arxiudigital.apirest.constantes.TiposDocumentosENI;

/**
 * Estructura que representa un document de l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuDocument {

	private String nodeId;
	private String titol;
	private String descripcio;

	private String eniVersio; // eni:v_nti
	private String eniIdentificador; // eni:id
	private OrigenesContenido eniOrigen; // eni:origen
	private Date eniDataCaptura; // eni:fecha_inicio
	private EstadosElaboracion eniEstatElaboracio; // eni:estado_elaboracion
	private TiposDocumentosENI eniTipusDocumental; // eni:tipo_doc_ENI
	private FormatosFichero eniFormatNom; // eni:nombre_formato
	private ExtensionesFichero eniFormatExtensio; // eni:nombre_formato
	private List<String> eniOrgans; // eni:organo
	private List<String> eniFirmaTipus; // eni:tipoFirma
	private List<String> eniFirmaCsv; // eni:csv
	private List<String> eniFirmaCsvDefinicio; // eni:def_csv
	private String eniDocumentOrigenId; // eni:id_origen

	private String serieDocumental; // eni:cod_clasificacion
	private String aplicacioCreacio; // eni:app_tramite_exp
	private String suport; // eni:soporte

	private List<Content> continguts;

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
	public Date getEniDataCaptura() {
		return eniDataCaptura;
	}
	public void setEniDataCaptura(Date eniDataCaptura) {
		this.eniDataCaptura = eniDataCaptura;
	}
	public EstadosElaboracion getEniEstatElaboracio() {
		return eniEstatElaboracio;
	}
	public void setEniEstatElaboracio(EstadosElaboracion eniEstatElaboracio) {
		this.eniEstatElaboracio = eniEstatElaboracio;
	}
	public TiposDocumentosENI getEniTipusDocumental() {
		return eniTipusDocumental;
	}
	public void setEniTipusDocumental(TiposDocumentosENI eniTipusDocumental) {
		this.eniTipusDocumental = eniTipusDocumental;
	}
	public FormatosFichero getEniFormatNom() {
		return eniFormatNom;
	}
	public void setEniFormatNom(FormatosFichero eniFormatNom) {
		this.eniFormatNom = eniFormatNom;
	}
	public ExtensionesFichero getEniFormatExtensio() {
		return eniFormatExtensio;
	}
	public void setEniFormatExtensio(ExtensionesFichero eniFormatExtensio) {
		this.eniFormatExtensio = eniFormatExtensio;
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
	public List<Content> getContinguts() {
		return continguts;
	}
	public void setContinguts(List<Content> continguts) {
		this.continguts = continguts;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}

}
