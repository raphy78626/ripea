/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.Date;
import java.util.List;

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
	private String fitxerNom;
	private String fitxerNomEnviamentPortafirmes;
	private String fitxerContentType;
	private byte[] fitxerContingut;
	private Date dataCaptura;
	private String ntiVersion;
	private String ntiIdentificador;
	private String ntiOrgano;
	private String ntiOrganoDescripcio;
	private NtiOrigenEnumDto ntiOrigen;
	private DocumentNtiEstadoElaboracionEnumDto ntiEstadoElaboracion;
	private DocumentNtiTipoDocumentalEnumDto ntiTipoDocumental;
	private String ntiIdDocumentoOrigen;
	private DocumentNtiTipoFirmaEnumDto ntiTipoFirma;
	private String ntiCsv;
	private String ntiCsvRegulacion;
	private String versioDarrera;
	private int versioCount;
	private List<DocumentVersioDto> versions;

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
	public String getFitxerNom() {
		return fitxerNom;
	}
	public void setFitxerNom(String fitxerNom) {
		this.fitxerNom = fitxerNom;
	}
	public String getFitxerNomEnviamentPortafirmes() {
		return fitxerNomEnviamentPortafirmes;
	}
	public void setFitxerNomEnviamentPortafirmes(String fitxerNomEnviamentPortafirmes) {
		this.fitxerNomEnviamentPortafirmes = fitxerNomEnviamentPortafirmes;
	}
	public String getFitxerContentType() {
		return fitxerContentType;
	}
	public void setFitxerContentType(String fitxerContentType) {
		this.fitxerContentType = fitxerContentType;
	}
	public byte[] getFitxerContingut() {
		return fitxerContingut;
	}
	public void setFitxerContingut(byte[] fitxerContingut) {
		this.fitxerContingut = fitxerContingut;
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
	public String getNtiOrganoDescripcio() {
		return ntiOrganoDescripcio;
	}
	public void setNtiOrganoDescripcio(String ntiOrganoDescripcio) {
		this.ntiOrganoDescripcio = ntiOrganoDescripcio;
	}
	public NtiOrigenEnumDto getNtiOrigen() {
		return ntiOrigen;
	}
	public void setNtiOrigen(NtiOrigenEnumDto ntiOrigen) {
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
	public List<DocumentVersioDto> getVersions() {
		return versions;
	}
	public void setVersions(List<DocumentVersioDto> versions) {
		this.versions = versions;
	}

	public MetaDocumentDto getMetaDocument() {
		return (MetaDocumentDto)getMetaNode();
	}

	public String getNtiVersionUrl() {
		return "http://administracionelectronica.gob.es/ENI/XSD/V" + ntiVersion + "/expediente-e";
	}

	public boolean isFirmat() {
		return DocumentEstatEnumDto.FIRMAT.equals(estat);
	}
	public boolean isCustodiat() {
		return DocumentEstatEnumDto.CUSTODIAT.equals(estat);
	}

	protected DocumentDto copiarContenidor(ContingutDto original) {
		DocumentDto copia = new DocumentDto();
		copia.setId(original.getId());
		copia.setNom(original.getNom());
		return copia;
	}

	private static final long serialVersionUID = -139254994389509932L;

}
