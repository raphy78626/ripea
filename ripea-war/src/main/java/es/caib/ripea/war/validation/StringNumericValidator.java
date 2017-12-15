/**
 * 
 */
package es.caib.ripea.war.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Valida que l'arxiu no estigui buit.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class StringNumericValidator implements ConstraintValidator<StringNumeric, String> {
	
	@Override
	public void initialize(final StringNumeric constraintAnnotation) {
	}

	@Override
	public boolean isValid(
			final String value,
			final ConstraintValidatorContext context) {
		return value == null || StringUtils.isNumeric(value);
	}

}
