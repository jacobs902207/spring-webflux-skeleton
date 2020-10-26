package proejct.spring.skeleton.common.annotation.validator;


import proejct.spring.skeleton.common.annotation.EnumValidate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<EnumValidate, String> {
    private EnumValidate annotation;

    @Override
    public void initialize(final EnumValidate constraintAnnotation) {
        annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        Object[] enumValues = annotation.enumClass().getEnumConstants();

        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if (value.equals(enumValue.toString())) {
                    return true;
                }
            }
        }

        return false;
    }
}