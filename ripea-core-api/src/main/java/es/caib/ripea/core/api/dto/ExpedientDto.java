/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.util.Date;



/**
 * Informació d'un expedient.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ExpedientDto extends NodeDto {

	public ArxiuDto arxiu;
	private ExpedientEstatEnumDto estat;
	private Date tancatData;
	private String tancatMotiu;
	private int any;
	private long sequencia;
	private String codi;
	private String ntiVersion;
	private String ntiIdentificador;
	private String ntiOrgano;
	private String ntiOrganoDescripcio;
	private Date ntiFechaApertura;
	private String ntiClasificacionSia;
	private boolean sistraPublicat;
	private String sistraUnitatAdministrativa;
	private String sistraClau;
	private UsuariDto agafatPer;

	private String numero;


	public ArxiuDto getArxiu() {
		return arxiu;
	}
	public void setArxiu(ArxiuDto arxiu) {
		this.arxiu = arxiu;
	}
	public ExpedientEstatEnumDto getEstat() {
		return estat;
	}
	public void setEstat(ExpedientEstatEnumDto estat) {
		this.estat = estat;
	}
	public Date getTancatData() {
		return tancatData;
	}
	public void setTancatData(Date tancatData) {
		this.tancatData = tancatData;
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
	public String getCodi() {
		return codi;
	}
	public void setCodi(String codi) {
		this.codi = codi;
	}
	public String getNtiVersion() {
		return ntiVersion;
	}
	public void setNtiVersion(String ntiVersion) {
		this.ntiVersion = ntiVersion;
	}
	public String getNtiIdentificador() {
		return ntiIdentificador;
	}
	public void setNtiIdentificador(String ntiIdentificador) {
		this.ntiIdentificador = ntiIdentificador;
	}
	public String getNtiOrgano() {
		return ntiOrgano;
	}
	public void setNtiOrgano(String ntiOrgano) {
		this.ntiOrgano = ntiOrgano;
	}
	public String getNtiOrganoDescripcio() {
		return ntiOrganoDescripcio;
	}
	public void setNtiOrganoDescripcio(String ntiOrganoDescripcio) {
		this.ntiOrganoDescripcio = ntiOrganoDescripcio;
	}
	public Date getNtiFechaApertura() {
		return ntiFechaApertura;
	}
	public void setNtiFechaApertura(Date ntiFechaApertura) {
		this.ntiFechaApertura = ntiFechaApertura;
	}
	public String getNtiClasificacionSia() {
		return ntiClasificacionSia;
	}
	public void setNtiClasificacionSia(String ntiClasificacionSia) {
		this.ntiClasificacionSia = ntiClasificacionSia;
	}
	public boolean isSistraPublicat() {
		return sistraPublicat;
	}
	public void setSistraPublicat(boolean sistraPublicat) {
		this.sistraPublicat = sistraPublicat;
	}
	public String getSistraUnitatAdministrativa() {
		return sistraUnitatAdministrativa;
	}
	public void setSistraUnitatAdministrativa(String sistraUnitatAdministrativa) {
		this.sistraUnitatAdministrativa = sistraUnitatAdministrativa;
	}
	public String getSistraClau() {
		return sistraClau;
	}
	public void setSistraClau(String sistraClau) {
		this.sistraClau = sistraClau;
	}
	public UsuariDto getAgafatPer() {
		return agafatPer;
	}
	public void setAgafatPer(UsuariDto agafatPer) {
		this.agafatPer = agafatPer;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}

	public MetaExpedientDto getMetaExpedient() {
		return (MetaExpedientDto)getMetaNode();
	}

	/*public String getNtiVersionUrl() {
		return "http://administracionelectronica.gob.es/ENI/XSD/V" + ntiVersion + "/expediente-e";
	}
	public String getNtiClasificacion() {
		if (ntiClasificacionSia != null) {
			return ntiClasificacionSia;
		} else {
			if (getMetaNode() != null) {
				MetaExpedientDto metaExpedient = (MetaExpedientDto)getMetaNode();
				if (metaExpedient.getClassificacioSia() != null) {
					return metaExpedient.getClassificacioSia();
				} else {
					return ntiOrgano + "_PRO_RIP" + String.format("%027d", metaExpedient.getId());
				}
			} else {
				return ntiOrgano + "_PRO_RIP" + String.format("%027d", 0);
			}
		}
	}
	public String getNtiEstado() {
		return ExpedientEstatEnumDto.OBERT.equals(estat) ? "Abierto" : "Cerrado";
	}*/
	public boolean isAgafat() {
		return agafatPer != null;
	}
	
	protected ExpedientDto copiarContenidor(ContingutDto original) {
		ExpedientDto copia = new ExpedientDto();
		copia.setId(original.getId());
		copia.setNom(original.getNom());
		return copia;
	}

	private static final long serialVersionUID = -139254994389509932L;

}
