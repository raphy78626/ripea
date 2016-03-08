/**
 * 
 */
package es.caib.ripea.war.command;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import es.caib.ripea.core.api.dto.MetaDocumentDto;
import es.caib.ripea.core.api.dto.MetaDocumentFirmaFluxTipusEnumDto;
import es.caib.ripea.core.api.dto.MultiplicitatEnumDto;
import es.caib.ripea.war.helper.ConversioTipusHelper;
import es.caib.ripea.war.validation.CodiMetaDocumentNoRepetit;

/**
 * Command per al manteniment de meta-documents.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@CodiMetaDocumentNoRepetit(campId = "id", campCodi = "codi", campEntitatId = "entitatId")
public class MetaDocumentCommand {

	private Long id;

	@NotEmpty @Size(max=64)
	private String codi;
	@NotEmpty @Size(max=256)
	private String nom;
	@Size(max=1024)
	private String descripcio;
	private boolean globalExpedient;
	@NotNull
	private MultiplicitatEnumDto globalMultiplicitat;
	private boolean globalReadOnly;
	@Size(max=64)
	private String custodiaPolitica;
	@Size(max=64)
	private String portafirmesDocumentTipus;
	@Size(max=64)
	private String portafirmesFluxId;
	private String[] portafirmesResponsables;
	private MetaDocumentFirmaFluxTipusEnumDto portafirmesFluxTipus;
	@Size(max=64)
	private String signaturaTipusMime;
	protected MultipartFile plantilla;

	private Long entitatId;



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodi() {
		return codi;
	}
	public void setCodi(String codi) {
		this.codi = codi;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getDescripcio() {
		return descripcio;
	}
	public void setDescripcio(String descripcio) {
		this.descripcio = descripcio;
	}
	public boolean isGlobalExpedient() {
		return globalExpedient;
	}
	public void setGlobalExpedient(boolean globalExpedient) {
		this.globalExpedient = globalExpedient;
	}
	public MultiplicitatEnumDto getGlobalMultiplicitat() {
		return globalMultiplicitat;
	}
	public void setGlobalMultiplicitat(MultiplicitatEnumDto globalMultiplicitat) {
		this.globalMultiplicitat = globalMultiplicitat;
	}
	public boolean isGlobalReadOnly() {
		return globalReadOnly;
	}
	public void setGlobalReadOnly(boolean globalReadOnly) {
		this.globalReadOnly = globalReadOnly;
	}
	public String getCustodiaPolitica() {
		return custodiaPolitica;
	}
	public void setCustodiaPolitica(String custodiaPolitica) {
		this.custodiaPolitica = custodiaPolitica;
	}
	public String getPortafirmesDocumentTipus() {
		return portafirmesDocumentTipus;
	}
	public void setPortafirmesDocumentTipus(String portafirmesDocumentTipus) {
		this.portafirmesDocumentTipus = portafirmesDocumentTipus;
	}
	public String getPortafirmesFluxId() {
		return portafirmesFluxId;
	}
	public void setPortafirmesFluxId(String portafirmesFluxId) {
		this.portafirmesFluxId = portafirmesFluxId;
	}
	public String[] getPortafirmesResponsables() {
		return portafirmesResponsables;
	}
	public void setPortafirmesResponsables(String[] portafirmesResponsables) {
		this.portafirmesResponsables = portafirmesResponsables;
	}
	public MetaDocumentFirmaFluxTipusEnumDto getPortafirmesFluxTipus() {
		return portafirmesFluxTipus;
	}
	public void setPortafirmesFluxTipus(MetaDocumentFirmaFluxTipusEnumDto portafirmesFluxTipus) {
		this.portafirmesFluxTipus = portafirmesFluxTipus;
	}
	public String getSignaturaTipusMime() {
		return signaturaTipusMime;
	}
	public void setSignaturaTipusMime(String signaturaTipusMime) {
		this.signaturaTipusMime = signaturaTipusMime;
	}
	public MultipartFile getPlantilla() {
		return plantilla;
	}
	public void setPlantilla(MultipartFile plantilla) {
		this.plantilla = plantilla;
	}
	public Long getEntitatId() {
		return entitatId;
	}
	public void setEntitatId(Long entitatId) {
		this.entitatId = entitatId;
	}

	public static List<MetaDocumentCommand> toEntitatCommands(
			List<MetaDocumentDto> dtos) {
		List<MetaDocumentCommand> commands = new ArrayList<MetaDocumentCommand>();
		for (MetaDocumentDto dto: dtos) {
			commands.add(
					ConversioTipusHelper.convertir(
							dto,
							MetaDocumentCommand.class));
		}
		return commands;
	}

	public static MetaDocumentCommand asCommand(MetaDocumentDto dto) {
		return ConversioTipusHelper.convertir(
				dto,
				MetaDocumentCommand.class);
	}
	public static MetaDocumentDto asDto(MetaDocumentCommand command) {
		return ConversioTipusHelper.convertir(
				command,
				MetaDocumentDto.class);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
