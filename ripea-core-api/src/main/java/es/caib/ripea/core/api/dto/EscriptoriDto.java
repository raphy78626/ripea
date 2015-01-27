/**
 * 
 */
package es.caib.ripea.core.api.dto;


/**
 * Informació d'un escriptori d'un usuari.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class EscriptoriDto extends ContenidorDto {

	private static final long serialVersionUID = -139254994389509932L;

	protected EscriptoriDto copiarContenidor(ContenidorDto original) {
		EscriptoriDto copia = new EscriptoriDto();
		copia.setId(original.getId());
		copia.setNom(original.getNom());
		return copia;
	}

}
