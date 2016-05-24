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
	private Date recepcioData;
	private String numero;
	private String descripcio;
	private String remitent;
	private String comentari;



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
	public Date getRecepcioData() {
		return recepcioData;
	}
	public void setRecepcioData(Date recepcioData) {
		this.recepcioData = recepcioData;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getDescripcio() {
		return descripcio;
	}
	public void setDescripcio(String descripcio) {
		this.descripcio = descripcio;
	}
	public String getRemitent() {
		return remitent;
	}
	public void setRemitent(String remitent) {
		this.remitent = remitent;
	}
	public String getComentari() {
		return comentari;
	}
	public void setComentari(String comentari) {
		this.comentari = comentari;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
