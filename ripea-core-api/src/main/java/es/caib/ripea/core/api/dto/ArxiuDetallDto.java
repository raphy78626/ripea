/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Informació d'un contingut emmagatzemada a l'arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuDetallDto extends ArxiuContingutDto {

	private String eniVersio;
	private String eniIdentificador;
	private NtiOrigenEnumDto eniOrigen;
	private Date eniDataObertura;
	private String eniClassificacio;
	private ExpedientEstatEnumDto eniEstat;
	private List<String> eniOrgans;
	private List<String> eniInteressats;
	private Date eniDataCaptura;
	private DocumentNtiEstadoElaboracionEnumDto eniEstatElaboracio;
	private DocumentNtiTipoDocumentalEnumDto eniTipusDocumental;
	private String eniFormat;
	private String eniExtensio;
	private String eniDocumentOrigenId;
	private String serieDocumental;

	private Map<String, Object> metadadesAddicionals;

	private String contingutTipusMime;
	private String contingutArxiuNom;
	
	private List<FirmaDto> firmes;
	private List<ArxiuContingutDto> fills;

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
	public NtiOrigenEnumDto getEniOrigen() {
		return eniOrigen;
	}
	public void setEniOrigen(NtiOrigenEnumDto eniOrigen) {
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
	public String getEniFormat() {
		return eniFormat;
	}
	public void setEniFormat(String eniFormat) {
		this.eniFormat = eniFormat;
	}
	public String getEniExtensio() {
		return eniExtensio;
	}
	public void setEniExtensio(String eniExtensio) {
		this.eniExtensio = eniExtensio;
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
	public Map<String, Object> getMetadadesAddicionals() {
		return metadadesAddicionals;
	}
	public void setMetadadesAddicionals(Map<String, Object> metadadesAddicionals) {
		this.metadadesAddicionals = metadadesAddicionals;
	}
	public String getContingutTipusMime() {
		return contingutTipusMime;
	}
	public void setContingutTipusMime(String contingutTipusMime) {
		this.contingutTipusMime = contingutTipusMime;
	}
	public String getContingutArxiuNom() {
		return contingutArxiuNom;
	}
	public void setContingutArxiuNom(String contingutArxiuNom) {
		this.contingutArxiuNom = contingutArxiuNom;
	}
	public List<FirmaDto> getFirmes() {
		return firmes;
	}
	public void setFirmes(List<FirmaDto> firmes) {
		this.firmes = firmes;
	}
	public List<ArxiuContingutDto> getFills() {
		return fills;
	}
	public void setFills(List<ArxiuContingutDto> fills) {
		this.fills = fills;
	}

	private static final long serialVersionUID = -2124829280908976623L;

}
