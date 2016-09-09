/**
 * 
 */
package es.caib.ripea.war.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import es.caib.ripea.core.api.dto.IndiomaEnumDto;
import es.caib.ripea.core.api.dto.InteressatAdministracioDto;
import es.caib.ripea.core.api.dto.InteressatDocumentTipusEnumDto;
import es.caib.ripea.core.api.dto.InteressatDto;
import es.caib.ripea.core.api.dto.InteressatPersonaFisicaDto;
import es.caib.ripea.core.api.dto.InteressatPersonaJuridicaDto;
import es.caib.ripea.core.api.dto.InteressatTipusEnumDto;
import es.caib.ripea.war.command.InteressatCommand.Administracio;
import es.caib.ripea.war.command.InteressatCommand.PersonaFisica;
import es.caib.ripea.war.command.InteressatCommand.PersonaJuridica;
import es.caib.ripea.war.helper.ConversioTipusHelper;
import es.caib.ripea.war.validation.DocumentIdentitat;
import es.caib.ripea.war.validation.InteressatNoRepetit;
import es.caib.ripea.war.validation.InteressatPais;

/**
 * Command per al manteniment d'interessats.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@InteressatNoRepetit(groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class})
@InteressatPais(groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class})
public class InteressatCommand  {

//	public static final String TIPUS_CIUTADA = "C";
//	public static final String TIPUS_ADMINISTRACIO = "A";

	protected Long id;
	@NotNull(groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class})
	protected Long entitatId;
	@NotEmpty(groups = {PersonaFisica.class})
	@Size(max = 30, groups = {PersonaFisica.class}, message = "max.size")
	protected String nom;
	@NotEmpty(groups = {PersonaFisica.class})
	@Size(max = 30, groups = {PersonaFisica.class}, message = "max.size")
	protected String llinatge1;
	@Size(max = 30, groups = {PersonaFisica.class}, message = "max.size")
	protected String llinatge2;
	@NotEmpty(groups = {PersonaJuridica.class})
	@Size(max = 80, groups = {PersonaJuridica.class}, message = "max.size")
	protected String raoSocial;
	@NotEmpty(groups = {Administracio.class})
	@Size(max = 9, groups = {Administracio.class}, message = "max.size")
	protected String organCodi;
	
	@NotNull(groups = {PersonaFisica.class, PersonaJuridica.class})
	protected InteressatDocumentTipusEnumDto documentTipus;
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	@DocumentIdentitat(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 9, groups = {PersonaFisica.class, PersonaJuridica.class}, message = "max.size")
	protected String documentNum;
	
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 4, groups={PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String pais;
	@Size(max = 2, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String provincia;
	@Size(max = 5, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String municipi;
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 160, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String adresa;
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 5, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String codiPostal;
	@Size(max = 160, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String email;
	@Size(max = 20, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String telefon;
	@Size(max = 160, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String observacions;
	protected IndiomaEnumDto notificacioIdioma;
	protected Boolean notificacioAutoritzat;

	@NotNull
	protected InteressatTipusEnumDto tipus;
	protected boolean comprovat = false;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEntitatId() {
		return entitatId;
	}
	public void setEntitatId(Long entitatId) {
		this.entitatId = entitatId;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getLlinatge1() {
		return llinatge1;
	}
	public void setLlinatge1(String llinatge1) {
		this.llinatge1 = llinatge1;
	}
	public String getLlinatge2() {
		return llinatge2;
	}
	public void setLlinatge2(String llinatge2) {
		this.llinatge2 = llinatge2;
	}
	public String getRaoSocial() {
		return raoSocial;
	}
	public void setRaoSocial(String raoSocial) {
		this.raoSocial = raoSocial;
	}
	public String getOrganCodi() {
		return organCodi;
	}
	public void setOrganCodi(String organCodi) {
		this.organCodi = organCodi;
	}
	public InteressatDocumentTipusEnumDto getDocumentTipus() {
		return documentTipus;
	}
	public void setDocumentTipus(InteressatDocumentTipusEnumDto documentTipus) {
		this.documentTipus = documentTipus;
	}
	public String getDocumentNum() {
		return documentNum;
	}
	public void setDocumentNum(String documentNum) {
		this.documentNum = documentNum;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getMunicipi() {
		return municipi;
	}
	public void setMunicipi(String municipi) {
		this.municipi = municipi;
	}
	public String getAdresa() {
		return adresa;
	}
	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}
	public String getCodiPostal() {
		return codiPostal;
	}
	public void setCodiPostal(String codiPostal) {
		this.codiPostal = codiPostal;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefon() {
		return telefon;
	}
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	public String getObservacions() {
		return observacions;
	}
	public void setObservacions(String observacions) {
		this.observacions = observacions;
	}
	public IndiomaEnumDto getNotificacioIdioma() {
		return notificacioIdioma;
	}
	public void setNotificacioIdioma(IndiomaEnumDto notificacioIdioma) {
		this.notificacioIdioma = notificacioIdioma;
	}
	public Boolean getNotificacioAutoritzat() {
		return notificacioAutoritzat;
	}
	public void setNotificacioAutoritzat(Boolean notificacioAutoritzat) {
		this.notificacioAutoritzat = notificacioAutoritzat;
	}
	public InteressatTipusEnumDto getTipus() {
		return tipus;
	}
	public void setTipus(InteressatTipusEnumDto tipus) {
		this.tipus = tipus;
	}
	public boolean isComprovat() {
		return comprovat;
	}
	public void setComprovat(boolean comprovat) {
		this.comprovat = comprovat;
	}

	public static InteressatCommand asCommand(InteressatDto dto) {
		InteressatCommand command = ConversioTipusHelper.convertir(
				dto,
				InteressatCommand.class);
		return command;
	}
	public static InteressatPersonaFisicaDto asPersonaFisicaDto(InteressatCommand command) {
		return ConversioTipusHelper.convertir(
				command,
				InteressatPersonaFisicaDto.class);
	}
	public static InteressatPersonaJuridicaDto asPersonaJuridicaDto(InteressatCommand command) {
		return ConversioTipusHelper.convertir(
				command,
				InteressatPersonaJuridicaDto.class);
	}
	public static InteressatAdministracioDto asAdministracioDto(InteressatCommand command) {
		return ConversioTipusHelper.convertir(
				command,
				InteressatAdministracioDto.class);
	}

	public boolean isPersonaFisica() {
		if (tipus == null)
			return true;
		return InteressatTipusEnumDto.PERSONA_FISICA.equals(tipus);
	}
	public boolean isPersonaJuridica() {
		return InteressatTipusEnumDto.PERSONA_JURIDICA.equals(tipus);
	}
	public boolean isAdministracio() {
		return InteressatTipusEnumDto.ADMINISTRACIO.equals(tipus);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public interface PersonaFisica {}
	public interface PersonaJuridica {}
	public interface Administracio {}

}
