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
	private RegistreAccioEnumDto accio;
	private RegistreTipusEnumDto tipus;
	private String entitatCodi;
	private String entitatNom;
	private String numero;
	private Date data;
	private String assumpteResum;
	private String assumpteCodi;
	private String assumpteReferencia;
	private String assumpteNumExpedient;
	private RegistreTransportTipusEnumDto transportTipus;
	private String transportNumero;
	private String usuariNom;
	private String usuariContacte;
	private String aplicacioEmissora;
	private RegistreDocumentacioFisicaTipusEnumDto documentacioFisica;
	private String observacions;
	private boolean prova;

	private List<RegistreInteressatDto> interessats;
	private List<RegistreDocumentDto> annexos;



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public RegistreAccioEnumDto getAccio() {
		return accio;
	}
	public void setAccio(RegistreAccioEnumDto accio) {
		this.accio = accio;
	}
	public RegistreTipusEnumDto getTipus() {
		return tipus;
	}
	public void setTipus(RegistreTipusEnumDto tipus) {
		this.tipus = tipus;
	}
	public String getEntitatCodi() {
		return entitatCodi;
	}
	public void setEntitatCodi(String entitatCodi) {
		this.entitatCodi = entitatCodi;
	}
	public String getEntitatNom() {
		return entitatNom;
	}
	public void setEntitatNom(String entitatNom) {
		this.entitatNom = entitatNom;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getAssumpteResum() {
		return assumpteResum;
	}
	public void setAssumpteResum(String assumpteResum) {
		this.assumpteResum = assumpteResum;
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
	public String getAplicacioEmissora() {
		return aplicacioEmissora;
	}
	public void setAplicacioEmissora(String aplicacioEmissora) {
		this.aplicacioEmissora = aplicacioEmissora;
	}
	public RegistreDocumentacioFisicaTipusEnumDto getDocumentacioFisica() {
		return documentacioFisica;
	}
	public void setDocumentacioFisica(
			RegistreDocumentacioFisicaTipusEnumDto documentacioFisica) {
		this.documentacioFisica = documentacioFisica;
	}
	public String getObservacions() {
		return observacions;
	}
	public void setObservacions(String observacions) {
		this.observacions = observacions;
	}
	public boolean isProva() {
		return prova;
	}
	public void setProva(boolean prova) {
		this.prova = prova;
	}
	public List<RegistreInteressatDto> getInteressats() {
		return interessats;
	}
	public void setInteressats(List<RegistreInteressatDto> interessats) {
		this.interessats = interessats;
	}
	public List<RegistreDocumentDto> getAnnexos() {
		return annexos;
	}
	public void setAnnexos(List<RegistreDocumentDto> annexos) {
		this.annexos = annexos;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
