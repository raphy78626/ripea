/**
 * 
 */
package es.caib.ripea.war.command;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import es.caib.ripea.core.api.dto.ContenidorFiltreDto;
import es.caib.ripea.core.api.dto.ContenidorTipusEnumDto;
import es.caib.ripea.war.helper.ConversioTipusHelper;

/**
 * Command per al filtre de la consulta de contenidors
 * dels usuaris administradors.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ContenidorFiltreCommand {

	public enum ContenidorFiltreOpcionsEsborratEnum {
		NOMES_NO_ESBORRATS,
		NOMES_ESBORRATS,
		ESBORRATS_I_NO_ESBORRATS
	};

	private String nom;
	private ContenidorTipusEnumDto tipus;
	private Long metaNodeId;
	private Date dataCreacioInici;
	private Date dataCreacioFi;
	private String usuariCreacio;
	private ContenidorFiltreOpcionsEsborratEnum opcionsEsborrat;



	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public ContenidorTipusEnumDto getTipus() {
		return tipus;
	}
	public void setTipus(ContenidorTipusEnumDto tipus) {
		this.tipus = tipus;
	}
	public Long getMetaNodeId() {
		return metaNodeId;
	}
	public void setMetaNodeId(Long metaNodeId) {
		this.metaNodeId = metaNodeId;
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
	public String getUsuariCreacio() {
		return usuariCreacio;
	}
	public void setUsuariCreacio(String usuariCreacio) {
		this.usuariCreacio = usuariCreacio;
	}
	public ContenidorFiltreOpcionsEsborratEnum getOpcionsEsborrat() {
		return opcionsEsborrat;
	}
	public void setOpcionsEsborrat(
			ContenidorFiltreOpcionsEsborratEnum opcionsEsborrat) {
		this.opcionsEsborrat = opcionsEsborrat;
	}

	public static ContenidorFiltreCommand asCommand(ContenidorFiltreDto dto) {
		ContenidorFiltreCommand command = ConversioTipusHelper.convertir(
				dto,
				ContenidorFiltreCommand.class);
		return command;
	}
	public static ContenidorFiltreDto asDto(ContenidorFiltreCommand command) {
		ContenidorFiltreDto dto = ConversioTipusHelper.convertir(
				command,
				ContenidorFiltreDto.class);
		dto.setMostrarEsborrats(
				ContenidorFiltreOpcionsEsborratEnum.ESBORRATS_I_NO_ESBORRATS.equals(command.getOpcionsEsborrat()) ||
				ContenidorFiltreOpcionsEsborratEnum.NOMES_ESBORRATS.equals(command.getOpcionsEsborrat()));
		dto.setMostrarNoEsborrats(
				ContenidorFiltreOpcionsEsborratEnum.ESBORRATS_I_NO_ESBORRATS.equals(command.getOpcionsEsborrat()) ||
				ContenidorFiltreOpcionsEsborratEnum.NOMES_NO_ESBORRATS.equals(command.getOpcionsEsborrat()));
		return dto;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
