/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Informació d'un tipus de document de portafirmes.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class PortafirmesDocumentTipusDto implements Serializable {

	private long id;
	private String nom;



	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
