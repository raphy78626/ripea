/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.text.SimpleDateFormat;



/**
 * Informació d'un expedient.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ExpedientDto extends NodeDto {

	public ArxiuDto arxiu;
	public boolean obert;
	private String tancatMotiu;
	private int any;
	private long sequencia;



	public ArxiuDto getArxiu() {
		return arxiu;
	}
	public void setArxiu(ArxiuDto arxiu) {
		this.arxiu = arxiu;
	}
	public boolean isObert() {
		return obert;
	}
	public void setObert(boolean obert) {
		this.obert = obert;
	}
	public String getTancatMotiu() {
		return tancatMotiu;
	}
	public void setTancatMotiu(String tancatMotiu) {
		this.tancatMotiu = tancatMotiu;
	}
	public int getAny() {
		return any;
	}
	public void setAny(int any) {
		this.any = any;
	}
	public long getSequencia() {
		return sequencia;
	}
	public void setSequencia(long sequencia) {
		this.sequencia = sequencia;
	}

	public MetaExpedientDto getMetaExpedient() {
		return (MetaExpedientDto)getMetaNode();
	}

	public String getNtiVersion() {
		return "1.0";
	}
	public String getNtiVersionUrl() {
		return "http://administracionelectronica.gob.es/ENI/XSD/v1.0/expediente-e";
	}
	public String getNtiIdentificador() {
		return "ES_" + getNtiOrgano() + "_" + getAny() + "_EXP_RIP" + String.format("%027d", getId());
	}
	public String getNtiOrgano() {
		if (arxiu == null)
			return null;
		return arxiu.getUnitatCodi();
	}
	public String getNtiFechaApertura() {
		if (getCreatedDate() != null)
			return new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss").format(getCreatedDate());
		else
			return null;
	}
	public String getNtiClasificacion() {
		if (getMetaNode() != null) {
			MetaExpedientDto metaExpedient = (MetaExpedientDto)getMetaNode();
			if (metaExpedient.getClassificacio() != null) {
				return metaExpedient.getClassificacio();
			} else {
				return getNtiOrgano() + "_PRO_RIP" + String.format("%027d", metaExpedient.getId());
			}
		} else {
			return getNtiOrgano() + "_PRO_RIP" + String.format("%027d", 0);
		}
	}
	public String getNtiEstado() {
		return obert ? "Abierto" : "Cerrado";
	}

	protected ExpedientDto copiarContenidor(ContenidorDto original) {
		ExpedientDto copia = new ExpedientDto();
		copia.setId(original.getId());
		copia.setNom(original.getNom());
		return copia;
	}

	private static final long serialVersionUID = -139254994389509932L;

}
