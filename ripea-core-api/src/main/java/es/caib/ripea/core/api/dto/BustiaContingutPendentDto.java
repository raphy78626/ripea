/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Informació sobre contingut pendent d'una bústia.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class BustiaContingutPendentDto implements Serializable {

	private Long id;
	private BustiaContingutPendentTipusEnumDto tipus;
	private String nom;
	private String remitent;
	private Date recepcioData;
	private String comentari;
	private boolean procesAutomatic;
	private boolean error;



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public BustiaContingutPendentTipusEnumDto getTipus() {
		return tipus;
	}
	public void setTipus(BustiaContingutPendentTipusEnumDto tipus) {
		this.tipus = tipus;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getRemitent() {
		return remitent;
	}
	public void setRemitent(String remitent) {
		this.remitent = remitent;
	}
	public Date getRecepcioData() {
		return recepcioData;
	}
	public void setRecepcioData(Date recepcioData) {
		this.recepcioData = recepcioData;
	}
	public String getComentari() {
		return comentari;
	}
	public void setComentari(String comentari) {
		this.comentari = comentari;
	}
	public boolean isProcesAutomatic() {
		return procesAutomatic;
	}
	public void setProcesAutomatic(boolean procesAutomatic) {
		this.procesAutomatic = procesAutomatic;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
