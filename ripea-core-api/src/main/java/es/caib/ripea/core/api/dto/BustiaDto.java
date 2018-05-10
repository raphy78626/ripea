/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;




/**
 * Informació d'una bústia.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class BustiaDto extends ContingutDto {

	private String unitatCodi;
	private String unitatConselleriaCodi;
	private boolean activa;
	private boolean perDefecte;
	private UnitatOrganitzativaDto unitatOrganitzativa;

	private List<PermisDto> permisos;
	private boolean usuariActualRead;
	private UnitatOrganitzativaDto unitat;
	private String pareNom;



	public UnitatOrganitzativaDto getUnitatOrganitzativa() {
		return unitatOrganitzativa;
	}
	public void setUnitatOrganitzativa(UnitatOrganitzativaDto unitatOrganitzativa) {
		this.unitatOrganitzativa = unitatOrganitzativa;
	}
	public String getUnitatCodi() {
		return unitatCodi;
	}
	public void setUnitatCodi(String unitatCodi) {
		this.unitatCodi = unitatCodi;
	}
	public String getUnitatConselleriaCodi() {
		return unitatConselleriaCodi;
	}
	public void setUnitatConselleriaCodi(String unitatConselleriaCodi) {
		this.unitatConselleriaCodi = unitatConselleriaCodi;
	}
	public boolean isActiva() {
		return activa;
	}
	public void setActiva(boolean activa) {
		this.activa = activa;
	}
	public boolean isPerDefecte() {
		return perDefecte;
	}
	public void setPerDefecte(boolean perDefecte) {
		this.perDefecte = perDefecte;
	}
	public List<PermisDto> getPermisos() {
		return permisos;
	}
	public void setPermisos(List<PermisDto> permisos) {
		this.permisos = permisos;
	}
	public boolean isUsuariActualRead() {
		return usuariActualRead;
	}
	public void setUsuariActualRead(boolean usuariActualRead) {
		this.usuariActualRead = usuariActualRead;
	}
	public UnitatOrganitzativaDto getUnitat() {
		return unitat;
	}
	public void setUnitat(UnitatOrganitzativaDto unitat) {
		this.unitat = unitat;
	}

	public String getPareNom() {
		return pareNom;
	}
	public void setPareNom(String pareNom) {
		this.pareNom = pareNom;
	}
	public int getPermisosCount() {
		if  (permisos == null)
			return 0;
		else
			return permisos.size();
	}

	private static final String SEPARADOR_NOM_UNITAT = " / ";
	public String getNomAmbUnitat() {
		if (unitat != null) {
			if (unitat.getDenominacio() != null)
				return unitat.getDenominacio() + SEPARADOR_NOM_UNITAT + nom;
			else if (unitat.getCodi() != null)
				return unitat.getCodi() + SEPARADOR_NOM_UNITAT + nom;
			else
				return "[?]" + SEPARADOR_NOM_UNITAT + nom;
		} else {
			return nom;
		}
	}

	protected CarpetaDto copiarContenidor(ContingutDto original) {
		CarpetaDto copia = new CarpetaDto();
		copia.setId(original.getId());
		copia.setNom(original.getNom());
		return copia;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
