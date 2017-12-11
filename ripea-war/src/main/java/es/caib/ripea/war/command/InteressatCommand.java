/**
 * 
 */
package es.caib.ripea.war.command;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import es.caib.ripea.core.api.dto.InteressatIdiomaEnumDto;
import es.caib.ripea.core.api.dto.InteressatAdministracioDto;
import es.caib.ripea.core.api.dto.InteressatDocumentTipusEnumDto;
import es.caib.ripea.core.api.dto.InteressatDto;
import es.caib.ripea.core.api.dto.InteressatPersonaFisicaDto;
import es.caib.ripea.core.api.dto.InteressatPersonaJuridicaDto;
import es.caib.ripea.core.api.dto.InteressatTipusEnumDto;
import es.caib.ripea.plugin.notificacio.NotificacioEntregaPostalViaTipusEnum;
import es.caib.ripea.war.command.InteressatCommand.Administracio;
import es.caib.ripea.war.command.InteressatCommand.PersonaFisica;
import es.caib.ripea.war.command.InteressatCommand.PersonaJuridica;
import es.caib.ripea.war.helper.ConversioTipusHelper;
import es.caib.ripea.war.validation.InteressatDocument;
import es.caib.ripea.war.validation.InteressatNoRepetit;
import es.caib.ripea.war.validation.InteressatPais;

/**
 * Command per al manteniment d'interessats.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@InteressatNoRepetit(groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class})
@InteressatPais(groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class})
@InteressatDocument(groups = {PersonaFisica.class, PersonaJuridica.class})
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
//	@DocumentIdentitat(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 9, groups = {PersonaFisica.class, PersonaJuridica.class}, message = "max.size")
	protected String documentNum;
	
	@Size(max = 160, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String email;
	@Size(max = 20, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String telefon;
	@Size(max = 160, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String observacions;
	protected InteressatIdiomaEnumDto preferenciaIdioma;
	protected Boolean notificacioAutoritzat;

	@NotNull
	protected InteressatTipusEnumDto tipus;
	protected boolean comprovat = false;
	
	// Camps de filtre (No s'utilitzen al fer submit)
	protected String filtreCodiDir3;
	protected String filtreDenominacio;
	protected String filtreNivellAdministracio;
	protected String filtreComunitat;
	protected String filtreProvincia;
	protected String filtreLocalitat;
	protected Boolean filtreArrel;
	
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 10, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String domiciliApartatCorreus;
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 50, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String domiciliBloc;
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	protected Integer domiciliCie;
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 10, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String domiciliCodiPostal;
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 250, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String domiciliComplement;
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 50, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String domiciliEscala;
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 50, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String domiciliLinea1;
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 50, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String domiciliLinea2;
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 5, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String domiciliMunicipiCodiIne;
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 10, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String domiciliNumeracioNumero;
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 3, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String domiciliPaisCodiIso; // ISO-3166
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 50, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String domiciliPlanta;
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 30, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String domiciliPoblacio;
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 50, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String domiciliPorta;
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 50, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String domiciliPortal;
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 2, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String domiciliProvinciaCodi;
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 10, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String domiciliNumeracioPuntKm;
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	@Size(max = 100, groups = {PersonaFisica.class, PersonaJuridica.class, Administracio.class}, message = "max.size")
	protected String domiciliViaNom;
	@NotEmpty(groups = {PersonaFisica.class, PersonaJuridica.class})
	protected NotificacioEntregaPostalViaTipusEnum domiciliViaTipus;
	
	

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
	public InteressatIdiomaEnumDto getPreferenciaIdioma() {
		return preferenciaIdioma;
	}
	public void setPreferenciaIdioma(InteressatIdiomaEnumDto preferenciaIdioma) {
		this.preferenciaIdioma = preferenciaIdioma;
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

	public String getFiltreCodiDir3() {
		return filtreCodiDir3;
	}
	public void setFiltreCodiDir3(String filtreCodiDir3) {
		this.filtreCodiDir3 = filtreCodiDir3;
	}
	public String getFiltreDenominacio() {
		return filtreDenominacio;
	}
	public void setFiltreDenominacio(String filtreDenominacio) {
		this.filtreDenominacio = filtreDenominacio;
	}
	public String getFiltreNivellAdministracio() {
		return filtreNivellAdministracio;
	}
	public void setFiltreNivellAdministracio(String filtreNivellAdministracio) {
		this.filtreNivellAdministracio = filtreNivellAdministracio;
	}
	public String getFiltreComunitat() {
		return filtreComunitat;
	}
	public void setFiltreComunitat(String filtreComunitat) {
		this.filtreComunitat = filtreComunitat;
	}
	public String getFiltreProvincia() {
		return filtreProvincia;
	}
	public void setFiltreProvincia(String filtreProvincia) {
		this.filtreProvincia = filtreProvincia;
	}
	public String getFiltreLocalitat() {
		return filtreLocalitat;
	}
	public void setFiltreLocalitat(String filtreLocalitat) {
		this.filtreLocalitat = filtreLocalitat;
	}
	public Boolean getFiltreArrel() {
		return filtreArrel;
	}
	public void setFiltreArrel(Boolean filtreArrel) {
		this.filtreArrel = filtreArrel;
	}
	
	public String getDomiciliApartatCorreus() {
		return domiciliApartatCorreus;
	}
	public void setDomiciliApartatCorreus(String domiciliApartatCorreus) {
		this.domiciliApartatCorreus = domiciliApartatCorreus;
	}
	public String getDomiciliBloc() {
		return domiciliBloc;
	}
	public void setDomiciliBloc(String domiciliBloc) {
		this.domiciliBloc = domiciliBloc;
	}
	public Integer getDomiciliCie() {
		return domiciliCie;
	}
	public void setDomiciliCie(Integer domiciliCie) {
		this.domiciliCie = domiciliCie;
	}
	public String getDomiciliCodiPostal() {
		return domiciliCodiPostal;
	}
	public void setDomiciliCodiPostal(String domiciliCodiPostal) {
		this.domiciliCodiPostal = domiciliCodiPostal;
	}
	public String getDomiciliComplement() {
		return domiciliComplement;
	}
	public void setDomiciliComplement(String domiciliComplement) {
		this.domiciliComplement = domiciliComplement;
	}
	public String getDomiciliEscala() {
		return domiciliEscala;
	}
	public void setDomiciliEscala(String domiciliEscala) {
		this.domiciliEscala = domiciliEscala;
	}
	public String getDomiciliLinea1() {
		return domiciliLinea1;
	}
	public void setDomiciliLinea1(String domiciliLinea1) {
		this.domiciliLinea1 = domiciliLinea1;
	}
	public String getDomiciliLinea2() {
		return domiciliLinea2;
	}
	public void setDomiciliLinea2(String domiciliLinea2) {
		this.domiciliLinea2 = domiciliLinea2;
	}
	public String getDomiciliMunicipiCodiIne() {
		return domiciliMunicipiCodiIne;
	}
	public void setDomiciliMunicipiCodiIne(String domiciliMunicipiCodiIne) {
		this.domiciliMunicipiCodiIne = domiciliMunicipiCodiIne;
	}
	public String getDomiciliNumeracioNumero() {
		return domiciliNumeracioNumero;
	}
	public void setDomiciliNumeracioNumero(String domiciliNumeracioNumero) {
		this.domiciliNumeracioNumero = domiciliNumeracioNumero;
	}
	public String getDomiciliPaisCodiIso() {
		return domiciliPaisCodiIso;
	}
	public void setDomiciliPaisCodiIso(String domiciliPaisCodiIso) {
		this.domiciliPaisCodiIso = domiciliPaisCodiIso;
	}
	public String getDomiciliPlanta() {
		return domiciliPlanta;
	}
	public void setDomiciliPlanta(String domiciliPlanta) {
		this.domiciliPlanta = domiciliPlanta;
	}
	public String getDomiciliPoblacio() {
		return domiciliPoblacio;
	}
	public void setDomiciliPoblacio(String domiciliPoblacio) {
		this.domiciliPoblacio = domiciliPoblacio;
	}
	public String getDomiciliPorta() {
		return domiciliPorta;
	}
	public void setDomiciliPorta(String domiciliPorta) {
		this.domiciliPorta = domiciliPorta;
	}
	public String getDomiciliPortal() {
		return domiciliPortal;
	}
	public void setDomiciliPortal(String domiciliPortal) {
		this.domiciliPortal = domiciliPortal;
	}
	public String getDomiciliProvinciaCodi() {
		return domiciliProvinciaCodi;
	}
	public void setDomiciliProvinciaCodi(String domiciliProvinciaCodi) {
		this.domiciliProvinciaCodi = domiciliProvinciaCodi;
	}
	public String getDomiciliNumeracioPuntKm() {
		return domiciliNumeracioPuntKm;
	}
	public void setDomiciliNumeracioPuntKm(String domiciliNumeracioPuntKm) {
		this.domiciliNumeracioPuntKm = domiciliNumeracioPuntKm;
	}
	public String getDomiciliViaNom() {
		return domiciliViaNom;
	}
	public void setDomiciliViaNom(String domiciliViaNom) {
		this.domiciliViaNom = domiciliViaNom;
	}
	public NotificacioEntregaPostalViaTipusEnum getDomiciliViaTipus() {
		return domiciliViaTipus;
	}
	public void setDomiciliViaTipus(NotificacioEntregaPostalViaTipusEnum domiciliViaTipus) {
		this.domiciliViaTipus = domiciliViaTipus;
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
