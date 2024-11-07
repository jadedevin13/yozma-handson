package org.devin.yozma.qa.platform.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AnswersValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnswersConstraint {
    String message() default "Invalid question configuration";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}