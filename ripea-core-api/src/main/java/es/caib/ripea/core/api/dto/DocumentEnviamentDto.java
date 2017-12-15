/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.Date;
import java.util.List;



/**
 * Informació d'un enviament d'un document.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public abstract class DocumentEnviamentDto extends AuditoriaDto {

	private Long id;
	private DocumentEnviamentEstatEnumDto estat;
	private String assumpte;
	private Date dataEnviament;
	private String observacions;
	private DocumentDto document;
	private List<DocumentDto> annexos;



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public DocumentEnviamentEstatEnumDto getEstat() {
		return estat;
	}
	public void setEstat(DocumentEnviamentEstatEnumDto estat) {
		this.estat = estat;
	}
	public String getAssumpte() {
		return assumpte;
	}
	public void setAssumpte(String assumpte) {
		this.assumpte = assumpte;
	}
	public Date getDataEnviament() {
		return dataEnviament;
	}
	public void setDataEnviament(Date dataEnviament) {
		this.dataEnviament = dataEnviament;
	}
	public String getObservacions() {
		return observacions;
	}
	public void setObservacions(String observacions) {
		this.observacions = observacions;
	}
	public DocumentDto getDocument() {
		return document;
	}
	public void setDocument(DocumentDto document) {
		this.document = document;
	}
	public List<DocumentDto> getAnnexos() {
		return annexos;
	}
	public void setAnnexos(List<DocumentDto> annexos) {
		this.annexos = annexos;
	}

	public boolean isNotificacio() {
		return this instanceof DocumentNotificacioDto;
	}
	public boolean isPublicacio() {
		return this instanceof DocumentPublicacioDto;
	}

	public abstract String getDestinatari();

	private static final long serialVersionUID = -139254994389509932L;

}
