/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Informació d'una anotació de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class RegistreAnotacioDto implements Serializable {

	private Long id;
	private RegistreTipusEnumDto tipus;
	private String unitatAdministrativa;
	private int numero;
	private Date data;
	private String identificador;
	private String oficina;
	private String llibre;
	private String extracte;
	private String assumpteTipus;
	public String assumpteCodi;
	public String assumpteReferencia;
	public String assumpteNumExpedient;
	public String idioma;
	public RegistreTransportTipusEnumDto transportTipus;
	public String transportNumero;
	public String usuariNom;
	public String usuariContacte;
	public String aplicacioCodi;
	public String aplicacioVersio;
	public RegistreDocumentacioFisicaTipusEnumDto documentacioFisica;
	public String observacions;
	public String exposa;
	public String solicita;
	public Date dataProcessat;
	public String motiuRebuig;

	private List<RegistreInteressatDto> interessats;
	private List<RegistreAnnexDto> annexos;



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public RegistreTipusEnumDto getTipus() {
		return tipus;
	}
	public void setTipus(RegistreTipusEnumDto tipus) {
		this.tipus = tipus;
	}
	public String getUnitatAdministrativa() {
		return unitatAdministrativa;
	}
	public void setUnitatAdministrativa(String unitatAdministrativa) {
		this.unitatAdministrativa = unitatAdministrativa;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public String getOficina() {
		return oficina;
	}
	public void setOficina(String oficina) {
		this.oficina = oficina;
	}
	public String getLlibre() {
		return llibre;
	}
	public void setLlibre(String llibre) {
		this.llibre = llibre;
	}
	public String getExtracte() {
		return extracte;
	}
	public void setExtracte(String extracte) {
		this.extracte = extracte;
	}
	public String getAssumpteTipus() {
		return assumpteTipus;
	}
	public void setAssumpteTipus(String assumpteTipus) {
		this.assumpteTipus = assumpteTipus;
	}
	public String getAssumpteCodi() {
		return assumpteCodi;
	}
	public void setAssumpteCodi(String assumpteCodi) {
		this.assumpteCodi = assumpteCodi;
	}
	public String getAssumpteReferencia() {
		return assumpteReferencia;
	}
	public void setAssumpteReferencia(String assumpteReferencia) {
		this.assumpteReferencia = assumpteReferencia;
	}
	public String getAssumpteNumExpedient() {
		return assumpteNumExpedient;
	}
	public void setAssumpteNumExpedient(String assumpteNumExpedient) {
		this.assumpteNumExpedient = assumpteNumExpedient;
	}
	public String getIdioma() {
		return idioma;
	}
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	public RegistreTransportTipusEnumDto getTransportTipus() {
		return transportTipus;
	}
	public void setTransportTipus(RegistreTransportTipusEnumDto transportTipus) {
		this.transportTipus = transportTipus;
	}
	public String getTransportNumero() {
		return transportNumero;
	}
	public void setTransportNumero(String transportNumero) {
		this.transportNumero = transportNumero;
	}
	public String getUsuariNom() {
		return usuariNom;
	}
	public void setUsuariNom(String usuariNom) {
		this.usuariNom = usuariNom;
	}
	public String getUsuariContacte() {
		return usuariContacte;
	}
	public void setUsuariContacte(String usuariContacte) {
		this.usuariContacte = usuariContacte;
	}
	public String getAplicacioCodi() {
		return aplicacioCodi;
	}
	public void setAplicacioCodi(String aplicacioCodi) {
		this.aplicacioCodi = aplicacioCodi;
	}
	public String getAplicacioVersio() {
		return aplicacioVersio;
	}
	public void setAplicacioVersio(String aplicacioVersio) {
		this.aplicacioVersio = aplicacioVersio;
	}
	public RegistreDocumentacioFisicaTipusEnumDto getDocumentacioFisica() {
		return documentacioFisica;
	}
	public void setDocumentacioFisica(RegistreDocumentacioFisicaTipusEnumDto documentacioFisica) {
		this.documentacioFisica = documentacioFisica;
	}
	public String getObservacions() {
		return observacions;
	}
	public void setObservacions(String observacions) {
		this.observacions = observacions;
	}
	public String getExposa() {
		return exposa;
	}
	public void setExposa(String exposa) {
		this.exposa = exposa;
	}
	public String getSolicita() {
		return solicita;
	}
	public void setSolicita(String solicita) {
		this.solicita = solicita;
	}
	public Date getDataProcessat() {
		return dataProcessat;
	}
	public void setDataProcessat(Date dataProcessat) {
		this.dataProcessat = dataProcessat;
	}
	public String getMotiuRebuig() {
		return motiuRebuig;
	}
	public void setMotiuRebuig(String motiuRebuig) {
		this.motiuRebuig = motiuRebuig;
	}
	public List<RegistreInteressatDto> getInteressats() {
		return interessats;
	}
	public void setInteressats(List<RegistreInteressatDto> interessats) {
		this.interessats = interessats;
	}
	public List<RegistreAnnexDto> getAnnexos() {
		return annexos;
	}
	public void setAnnexos(List<RegistreAnnexDto> annexos) {
		this.annexos = annexos;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
