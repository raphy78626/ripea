/**
 * 
 */
package es.caib.ripea.plugin.registre;

import java.util.Date;
import java.util.List;


/**
 * Classe que representa una anotació de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class RegistreAnotacio {

	public enum RegistreTipusEnum {
		ENTRADA,
		SORTIDA;
	}

	private RegistreTipusEnum tipus;
	private String unitatAdministrativa;
	private int numero;
	private Date data;
	private String identificador;
	private String oficina;
	private String llibre;
	private String extracte;
	private String assumpteTipus;
	private String assumpteCodi;
	private String assumpteReferencia;
	private String assumpteNumExpedient;
	private String idioma;
	private String transportTipus;
	private String transportNumero;
	private String usuariNom;
	private String usuariContacte;
	private String aplicacioCodi;
	private String aplicacioVersio;
	private String documentacioFisica;
	private String observacions;
	private String exposa;
	private String solicita;
	private List<RegistreInteressat> interessats;
	private List<RegistreAnnex> annexos;

	public RegistreTipusEnum getTipus() {
		return tipus;
	}
	public void setTipus(RegistreTipusEnum tipus) {
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
	public String getTransportTipus() {
		return transportTipus;
	}
	public void setTransportTipus(String transportTipus) {
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
	public String getDocumentacioFisica() {
		return documentacioFisica;
	}
	public void setDocumentacioFisica(String documentacioFisica) {
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
	public List<RegistreInteressat> getInteressats() {
		return interessats;
	}
	public void setInteressats(List<RegistreInteressat> interessats) {
		this.interessats = interessats;
	}
	public List<RegistreAnnex> getAnnexos() {
		return annexos;
	}
	public void setAnnexos(List<RegistreAnnex> annexos) {
		this.annexos = annexos;
	}

	public boolean isEntrada() {
		return RegistreTipusEnum.ENTRADA.equals(tipus);
	}
	public boolean isSortida() {
		return RegistreTipusEnum.SORTIDA.equals(tipus);
	}

}
