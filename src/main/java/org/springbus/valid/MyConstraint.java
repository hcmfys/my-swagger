package org.springbus.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy =MyConstraintValidator.class )
public @interface MyConstraint {
    String message() default "小松博客自定义注解";
 
    Class<?>[] groups() default {};
 
    Class<? extends Payload>[] payload() default {};
}
