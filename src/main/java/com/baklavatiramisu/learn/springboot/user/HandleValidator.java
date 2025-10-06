package com.baklavatiramisu.learn.springboot.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HandleValidator implements ConstraintValidator<HandleValidation, String> {
    @Override
    public void initialize(HandleValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String handle, ConstraintValidatorContext constraintValidatorContext) {
        return handle.matches("^[a-zA-Z0-9_]+$");
    }
}
