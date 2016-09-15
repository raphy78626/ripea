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
 * Constraint de validació que controla que no es doni d'alta dues
 * vegades el mateix interesat.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=InteressatDocumentValidator.class)
public @interface InteressatDocument {

	String message() default "Número de document incorrecte";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
