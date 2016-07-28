/**
 * 
 */
package es.caib.ripea.war.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import es.caib.ripea.core.api.dto.InteressatAdministracioDto;
import es.caib.ripea.core.api.dto.InteressatPersonaFisicaDto;
import es.caib.ripea.core.api.service.InteressatService;
import es.caib.ripea.war.command.InteressatCommand;

/**
 * Constraint de validació que controla que no es repeteixi
 * l'interessat.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class InteressatNoRepetitValidator implements ConstraintValidator<InteressatNoRepetit, Object> {

	private String campId;
	private String campEntitatId;
	private String campNom;
	private String campNif;
	private String campLlinatges;
	private String campIdentificador;
	private String campTipus;

	@Autowired
	private InteressatService interessatService;



	@Override
	public void initialize(final InteressatNoRepetit constraintAnnotation) {
		this.campId = constraintAnnotation.campId();
		this.campEntitatId = constraintAnnotation.campEntitatId();
		this.campNom = constraintAnnotation.campNom();
		this.campNif = constraintAnnotation.campNif();
		this.campLlinatges = constraintAnnotation.campLlinatges();
		this.campIdentificador = constraintAnnotation.campIdentificador();
		this.campTipus = constraintAnnotation.campTipus();
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext context) {
		try {
			final String id = BeanUtils.getSimpleProperty(value, campId);
			if (id != null && !id.isEmpty())
				return true;
			final Long entitatId = new Long(BeanUtils.getSimpleProperty(value, campEntitatId));
			final String nom = BeanUtils.getProperty(value, campNom);
			final String nif = BeanUtils.getProperty(value, campNif);
			final String llinatges = BeanUtils.getProperty(value, campLlinatges);
			final String identificador = BeanUtils.getProperty(value, campIdentificador);
			final String tipus = BeanUtils.getSimpleProperty(value, campTipus);
			boolean existeix;
			if (InteressatCommand.TIPUS_CIUTADA.equals(tipus)) {
				List<InteressatPersonaFisicaDto> interessats = interessatService.findByFiltreCiutada(
						entitatId,
						nom,
						nif,
						llinatges);
				existeix = interessats.size() > 0;
			} else if (InteressatCommand.TIPUS_ADMINISTRACIO.equals(tipus)) {
				List<InteressatAdministracioDto> interessats = interessatService.findByFiltreAdministracio(
						entitatId,
						nom,
						identificador);
				existeix = interessats.size() > 0;
			} else {
				throw new RuntimeException("No s'ha pogut comprovar si l'interessat ja està donat d'alta: tipus desconegut");
			}
			return !existeix;
		} catch (final Exception ex) {
        	LOGGER.error("Error al comprovar si l'interessat ja està donat d'alta", ex);
        	return false;
        }
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(InteressatNoRepetitValidator.class);

}
