package com.baklavatiramisu.learn.springjpa.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HandleValidator.class)
public @interface HandleValidation {
    String message() default "The user handle is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
