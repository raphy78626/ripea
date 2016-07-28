/**
 * 
 */
package es.caib.ripea.war.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import es.caib.ripea.core.api.dto.InteressatAdministracioDto;
import es.caib.ripea.core.api.dto.InteressatPersonaFisicaDto;
import es.caib.ripea.core.api.dto.InteressatDto;
import es.caib.ripea.war.command.InteressatCommand.Administracio;
import es.caib.ripea.war.command.InteressatCommand.Ciutada;
import es.caib.ripea.war.helper.ConversioTipusHelper;
import es.caib.ripea.war.validation.DocumentIdentitat;
import es.caib.ripea.war.validation.InteressatNoRepetit;

/**
 * Command per al manteniment d'interessats.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@InteressatNoRepetit(
		groups = {Ciutada.class, Administracio.class},
		campId = "id",
		campEntitatId = "entitatId",
		campNom = "nom",
		campNif = "nif",
		campLlinatges = "llinatges",
		campIdentificador = "identificador",
		campTipus = "tipus")
public class InteressatCommand  {

	public static final String TIPUS_CIUTADA = "C";
	public static final String TIPUS_ADMINISTRACIO = "A";

	protected Long id;
	@NotNull(groups = {ComprovarCiutada.class, ComprovarAdministracio.class, Ciutada.class, Administracio.class})
	protected Long entitatId;
	@NotEmpty(groups = {Ciutada.class, Administracio.class})
	@Size(max = 256, groups = {Ciutada.class, Administracio.class})
	protected String nom;
	@NotEmpty(groups = {Ciutada.class})
	@Size(max = 256, groups = {Ciutada.class})
	protected String llinatges;
	@NotEmpty(groups = {ComprovarCiutada.class, Ciutada.class})
	@DocumentIdentitat(groups = {ComprovarCiutada.class, Ciutada.class})
	@Size(max = 9, groups = {ComprovarCiutada.class, Ciutada.class})
	protected String nif;
	@NotEmpty(groups = {ComprovarAdministracio.class, Administracio.class})
	@Size(max = 9, groups = {ComprovarAdministracio.class, Administracio.class})
	protected String identificador;

	protected String tipus;
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
	public String getLlinatges() {
		return llinatges;
	}
	public void setLlinatges(String llinatges) {
		this.llinatges = llinatges;
	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public String getTipus() {
		return tipus;
	}
	public void setTipus(String tipus) {
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
	public static InteressatPersonaFisicaDto asCiutadaDto(InteressatCommand command) {
		return ConversioTipusHelper.convertir(
				command,
				InteressatPersonaFisicaDto.class);
	}
	public static InteressatAdministracioDto asAdministracioDto(InteressatCommand command) {
		return ConversioTipusHelper.convertir(
				command,
				InteressatAdministracioDto.class);
	}

	public boolean isCiutada() {
		if (tipus == null)
			return true;
		return TIPUS_CIUTADA.equals(tipus);
	}
	public boolean isAdministracio() {
		return TIPUS_ADMINISTRACIO.equals(tipus);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public interface Ciutada {}
	public interface Administracio {}
	public interface ComprovarCiutada {}
	public interface ComprovarAdministracio {}

}
