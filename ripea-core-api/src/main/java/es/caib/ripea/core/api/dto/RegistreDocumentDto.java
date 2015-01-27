/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Informació d'un document d'una anotació de registre.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class RegistreDocumentDto implements Serializable {

	private String fitxerNom;
	private String identificador;
	private RegistreDocumentValidesaEnumDto validesa;
	private RegistreDocumentTipusEnumDto tipus;
	private String gestioDocumentalId;
	private String indentificadorDocumentFirmat;
	private String observacions;

	public String getFitxerNom() {
		return fitxerNom;
	}
	public void setFitxerNom(String fitxerNom) {
		this.fitxerNom = fitxerNom;
	}
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public RegistreDocumentValidesaEnumDto getValidesa() {
		return validesa;
	}
	public void setValidesa(RegistreDocumentValidesaEnumDto validesa) {
		this.validesa = validesa;
	}
	public RegistreDocumentTipusEnumDto getTipus() {
		return tipus;
	}
	public void setTipus(RegistreDocumentTipusEnumDto tipus) {
		this.tipus = tipus;
	}
	public String getGestioDocumentalId() {
		return gestioDocumentalId;
	}
	public void setGestioDocumentalId(String gestioDocumentalId) {
		this.gestioDocumentalId = gestioDocumentalId;
	}
	
	public String getIndentificadorDocumentFirmat() {
		return indentificadorDocumentFirmat;
	}
	public void setIndentificadorDocumentFirmat(String indentificadorDocumentFirmat) {
		this.indentificadorDocumentFirmat = indentificadorDocumentFirmat;
	}
	public String getObservacions() {
		return observacions;
	}
	public void setObservacions(String observacions) {
		this.observacions = observacions;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
