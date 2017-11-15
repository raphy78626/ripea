/**
 * 
 */
package es.caib.ripea.core.api.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Informació del filtre d'expedients.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ExpedientFiltreDto implements Serializable {

	private Long arxiuId;
	private Long metaExpedientId;
	private String nom;
	private Date dataCreacioInici;
	private Date dataCreacioFi;
	private String numero;
	private ExpedientEstatEnumDto estat;
	private Date dataTancatInici;
	private Date dataTancatFi;
	private boolean meusExpedients;


	public Long getArxiuId() {
		return arxiuId;
	}
	public void setArxiuId(Long arxiuId) {
		this.arxiuId = arxiuId;
	}
	public Long getMetaExpedientId() {
		return metaExpedientId;
	}
	public void setMetaExpedientId(Long metaExpedientId) {
		this.metaExpedientId = metaExpedientId;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Date getDataCreacioInici() {
		return dataCreacioInici;
	}
	public void setDataCreacioInici(Date dataCreacioInici) {
		this.dataCreacioInici = dataCreacioInici;
	}
	public Date getDataCreacioFi() {
		return dataCreacioFi;
	}
	public void setDataCreacioFi(Date dataCreacioFi) {
		this.dataCreacioFi = dataCreacioFi;
	}

	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public ExpedientEstatEnumDto getEstat() {
		return estat;
	}
	public void setEstat(ExpedientEstatEnumDto estat) {
		this.estat = estat;
	}
	public Date getDataTancatInici() {
		return dataTancatInici;
	}
	public void setDataTancatInici(Date dataTancatInici) {
		this.dataTancatInici = dataTancatInici;
	}
	public Date getDataTancatFi() {
		return dataTancatFi;
	}
	public void setDataTancatFi(Date dataTancatFi) {
		this.dataTancatFi = dataTancatFi;
	}
	public boolean isMeusExpedients() {
		return meusExpedients;
	}
	public void setMeusExpedients(boolean meusExpedients) {
		this.meusExpedients = meusExpedients;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private static final long serialVersionUID = -139254994389509932L;

}
