/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.Date;

/**
 * Informació d'un document.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class DocumentDto extends NodeDto {

	protected DocumentTipusEnumDto documentTipus;
	protected DocumentEstatEnumDto estat;
	protected String ubicacio;
	private Date data;
	private Date custodiaData;
	private String custodiaId;
	private String custodiaUrl;
	private Date dataCaptura;
	private String ntiVersion;
	private String ntiIdentificador;
	private String ntiOrgano;
	private DocumentNtiOrigenEnumDto ntiOrigen;
	private DocumentNtiEstadoElaboracionEnumDto ntiEstadoElaboracion;
	private DocumentNtiTipoDocumentalEnumDto ntiTipoDocumental;
	private String ntiIdDocumentoOrigen;
	private DocumentNtiTipoFirmaEnumDto ntiTipoFirma;
	private String ntiCsv;
	private String ntiCsvRegulacion;
	private String versioDarrera;
	private int versioCount;
	private String[] versions;

	public DocumentTipusEnumDto getDocumentTipus() {
		return documentTipus;
	}
	public void setDocumentTipus(DocumentTipusEnumDto documentTipus) {
		this.documentTipus = documentTipus;
	}
	public DocumentEstatEnumDto getEstat() {
		return estat;
	}
	public void setEstat(DocumentEstatEnumDto estat) {
		this.estat = estat;
	}
	public String getUbicacio() {
		return ubicacio;
	}
	public void setUbicacio(String ubicacio) {
		this.ubicacio = ubicacio;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public Date getCustodiaData() {
		return custodiaData;
	}
	public void setCustodiaData(Date custodiaData) {
		this.custodiaData = custodiaData;
	}
	public String getCustodiaId() {
		return custodiaId;
	}
	public void setCustodiaId(String custodiaId) {
		this.custodiaId = custodiaId;
	}
	public String getCustodiaUrl() {
		return custodiaUrl;
	}
	public void setCustodiaUrl(String custodiaUrl) {
		this.custodiaUrl = custodiaUrl;
	}
	public Date getDataCaptura() {
		return dataCaptura;
	}
	public void setDataCaptura(Date dataCaptura) {
		this.dataCaptura = dataCaptura;
	}
	public String getNtiVersion() {
		return ntiVersion;
	}
	public void setNtiVersion(String ntiVersion) {
		this.ntiVersion = ntiVersion;
	}
	public String getNtiIdentificador() {
		return ntiIdentificador;
	}
	public void setNtiIdentificador(String ntiIdentificador) {
		this.ntiIdentificador = ntiIdentificador;
	}
	public String getNtiOrgano() {
		return ntiOrgano;
	}
	public void setNtiOrgano(String ntiOrgano) {
		this.ntiOrgano = ntiOrgano;
	}
	public DocumentNtiOrigenEnumDto getNtiOrigen() {
		return ntiOrigen;
	}
	public void setNtiOrigen(DocumentNtiOrigenEnumDto ntiOrigen) {
		this.ntiOrigen = ntiOrigen;
	}
	public DocumentNtiEstadoElaboracionEnumDto getNtiEstadoElaboracion() {
		return ntiEstadoElaboracion;
	}
	public void setNtiEstadoElaboracion(DocumentNtiEstadoElaboracionEnumDto ntiEstadoElaboracion) {
		this.ntiEstadoElaboracion = ntiEstadoElaboracion;
	}
	public DocumentNtiTipoDocumentalEnumDto getNtiTipoDocumental() {
		return ntiTipoDocumental;
	}
	public void setNtiTipoDocumental(DocumentNtiTipoDocumentalEnumDto ntiTipoDocumental) {
		this.ntiTipoDocumental = ntiTipoDocumental;
	}
	public String getNtiIdDocumentoOrigen() {
		return ntiIdDocumentoOrigen;
	}
	public void setNtiIdDocumentoOrigen(String ntiIdDocumentoOrigen) {
		this.ntiIdDocumentoOrigen = ntiIdDocumentoOrigen;
	}
	public DocumentNtiTipoFirmaEnumDto getNtiTipoFirma() {
		return ntiTipoFirma;
	}
	public void setNtiTipoFirma(DocumentNtiTipoFirmaEnumDto ntiTipoFirma) {
		this.ntiTipoFirma = ntiTipoFirma;
	}
	public String getNtiCsv() {
		return ntiCsv;
	}
	public void setNtiCsv(String ntiCsv) {
		this.ntiCsv = ntiCsv;
	}
	public String getNtiCsvRegulacion() {
		return ntiCsvRegulacion;
	}
	public void setNtiCsvRegulacion(String ntiCsvRegulacion) {
		this.ntiCsvRegulacion = ntiCsvRegulacion;
	}
	public String getVersioDarrera() {
		return versioDarrera;
	}
	public void setVersioDarrera(String versioDarrera) {
		this.versioDarrera = versioDarrera;
	}
	public int getVersioCount() {
		return versioCount;
	}
	public void setVersioCount(int versioCount) {
		this.versioCount = versioCount;
	}
	public String[] getVersions() {
		return versions;
	}
	public void setVersions(String[] versions) {
		this.versions = versions;
	}

	public MetaDocumentDto getMetaDocument() {
		return (MetaDocumentDto)getMetaNode();
	}

	public String getNtiVersionUrl() {
		return "http://administracionelectronica.gob.es/ENI/XSD/V" + ntiVersion + "/expediente-e";
	}

	protected DocumentDto copiarContenidor(ContingutDto original) {
		DocumentDto copia = new DocumentDto();
		copia.setId(original.getId());
		copia.setNom(original.getNom());
		return copia;
	}

	private static final long serialVersionUID = -139254994389509932L;

}
