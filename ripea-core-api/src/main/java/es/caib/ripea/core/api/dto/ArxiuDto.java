/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Informació d'un arxiu.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuDto extends ContenidorDto {

	private String unitatCodi;
	private boolean actiu;

	private List<PermisDto> permisos;
	private boolean usuariActualRead;
	private int expedientsCount;
	private UnitatOrganitzativaDto unitat;
	private List<MetaExpedientDto> metaExpedients;
	private int metaExpedientsCount;

	public String getUnitatCodi() {
		return unitatCodi;
	}
	public void setUnitatCodi(String unitatCodi) {
		this.unitatCodi = unitatCodi;
	}
	public boolean isActiu() {
		return actiu;
	}
	public void setActiu(boolean actiu) {
		this.actiu = actiu;
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
	public int getExpedientsCount() {
		return expedientsCount;
	}
	public void setExpedientsCount(int expedientsCount) {
		this.expedientsCount = expedientsCount;
	}
	public UnitatOrganitzativaDto getUnitat() {
		return unitat;
	}
	public void setUnitat(UnitatOrganitzativaDto unitat) {
		this.unitat = unitat;
	}

	public int getPermisosCount() {
		if  (permisos == null)
			return 0;
		else
			return permisos.size();
	}
	public int getMetaExpedientsCount() {
		return metaExpedientsCount;
	}
	/**
	 * @return the metaExpedients
	 */
	public List<MetaExpedientDto> getMetaExpedients() {
		if(metaExpedients == null){
			this.metaExpedients = new ArrayList<MetaExpedientDto>();
		}
		return metaExpedients;
	}
	/**
	 * @param metaExpedients the metaExpedients to set
	 */
	public void setMetaExpedients(List<MetaExpedientDto> metaExpedients) {
		this.metaExpedients = metaExpedients;
	}
	public void setMetaExpedientsCount(int metaExpedientsCount) {
		this.metaExpedientsCount = metaExpedientsCount;
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

	protected CarpetaDto copiarContenidor(ContenidorDto original) {
		CarpetaDto copia = new CarpetaDto();
		copia.setId(original.getId());
		copia.setNom(original.getNom());
		return copia;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArxiuDto other = (ArxiuDto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	private static final long serialVersionUID = -139254994389509932L;

}
