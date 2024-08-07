package com.bloomberg.fxdeals.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.lang.reflect.Field;

public class SortFieldValidator implements ConstraintValidator<ValidSortField, String> {
    private Set<String> fieldNames;

    @Override
    public void initialize(ValidSortField constraintAnnotation) {
        Class<?> entityClass = constraintAnnotation.entityClass();
        fieldNames = Arrays.stream(entityClass.getDeclaredFields())
                .map(field -> field.getName().toLowerCase())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return fieldNames.contains(value.toLowerCase());
    }
}
