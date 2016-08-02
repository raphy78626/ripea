/**
 * 
 */
package es.caib.ripea.core.api.dto;


/**
 * Informació d'un document.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class CarpetaDto extends ContingutDto {

	private CarpetaTipusEnumDto tipus;



	public CarpetaTipusEnumDto getTipus() {
		return tipus;
	}
	public void setTipus(CarpetaTipusEnumDto tipus) {
		this.tipus = tipus;
	}
	// Per evitar errors en el cercador de contenedors per admins
	public CarpetaTipusEnumDto getMetaNode() {
		return null;
	}

	protected CarpetaDto copiarContenidor(ContingutDto original) {
		CarpetaDto copia = new CarpetaDto();
		copia.setId(original.getId());
		copia.setNom(original.getNom());
		return copia;
	}

	private static final long serialVersionUID = -139254994389509932L;

}
