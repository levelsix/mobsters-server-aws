package com.lvl6.mobsters.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Pattern(regexp="^\\p{XDigit}{8}+-\\p{XDigit}{4}+-\\p{XDigit}{4}+-\\p{XDigit}{4}+-\\p{XDigit}{12}+$")
@Size(min=36, max=36)
@NotNull
@ReportAsSingleViolation
@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface DynamoID {
	String message() default "{com.lvl6.mobsters.validation.constraints.valid_uuid}";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}
