/**
 * 
 */
package es.caib.ripea.war.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Constraint de validació que controla que un String tengui valor numeric.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=StringNumericValidator.class)
public @interface StringNumeric {

	String message() default "Aquest camp ha d'esser numèric";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
