/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Informació del filtre de continguts.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ContingutMassiuFiltreDto implements Serializable {

	private ContingutTipusEnumDto tipusElement;
	private String tipusExpedient;
	private String tipusDocument;
	private String	metaDada;
	private String nom;
	private Date dataInici;
	private Date dataFi;
	
	private boolean bloquejarTipusElement;
	private boolean bloquejarMetaDada;
	private boolean bloquejarMetaExpedient;
	private boolean bloquejarMetaDocument;
	
	public ContingutTipusEnumDto getTipusElement() {
		return tipusElement;
	}

	public void setTipusElement(ContingutTipusEnumDto tipusElement) {
		this.tipusElement = tipusElement;
	}

	public String getTipusExpedient() {
		return tipusExpedient;
	}

	public void setTipusExpedient(String tipusExpedient) {
		this.tipusExpedient = tipusExpedient;
	}

	public String getTipusDocument() {
		return tipusDocument;
	}

	public void setTipusDocument(String tipusDocument) {
		this.tipusDocument = tipusDocument;
	}

	public String getMetaDada() {
		return metaDada;
	}

	public void setMetaDada(String metaDada) {
		this.metaDada = metaDada;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Date getDataInici() {
		return dataInici;
	}

	public void setDataInici(Date dataInici) {
		this.dataInici = dataInici;
	}

	public Date getDataFi() {
		return dataFi;
	}

	public void setDataFi(Date dataFi) {
		this.dataFi = dataFi;
	}

	public boolean isBloquejarTipusElement() {
		return bloquejarTipusElement;
	}

	public void setBloquejarTipusElement(boolean bloquejarTipusElement) {
		this.bloquejarTipusElement = bloquejarTipusElement;
	}

	public boolean isBloquejarMetaDada() {
		return bloquejarMetaDada;
	}

	public void setBloquejarMetaDada(boolean bloquejarMetaDada) {
		this.bloquejarMetaDada = bloquejarMetaDada;
	}

	public boolean isBloquejarMetaExpedient() {
		return bloquejarMetaExpedient;
	}

	public void setBloquejarMetaExpedient(boolean bloquejarMetaExpedient) {
		this.bloquejarMetaExpedient = bloquejarMetaExpedient;
	}

	public boolean isBloquejarMetaDocument() {
		return bloquejarMetaDocument;
	}

	public void setBloquejarMetaDocument(boolean bloquejarMetaDocument) {
		this.bloquejarMetaDocument = bloquejarMetaDocument;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
