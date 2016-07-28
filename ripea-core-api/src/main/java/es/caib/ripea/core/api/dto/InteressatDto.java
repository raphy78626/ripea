/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Informació d'un interessat.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public abstract class InteressatDto implements Serializable {

	protected Long id;
	protected String nom;



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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

	public boolean isCiutada() {
		return this instanceof InteressatPersonaFisicaDto;
	}
	public boolean isAdministracio() {
		return this instanceof InteressatAdministracioDto;
	}

	private static final long serialVersionUID = -139254994389509932L;

}
