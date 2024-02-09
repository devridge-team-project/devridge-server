package org.devridge.api.domain.qna.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OptionValidator implements ConstraintValidator<ValidateSortOption, String> {

    private ValidateSortOption validateSortOption;

    @Override
    public void initialize(ValidateSortOption constraintAnnotation) {
        this.validateSortOption = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Enum<?>[] enumValues = this.validateSortOption.enumClass().getEnumConstants();

        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if (
                        value.equals(enumValue.toString())
                                || this.validateSortOption.ignoreCase() && value.equalsIgnoreCase(enumValue.toString())
                ) {
                    return true;
                }
            }
        }

        return false;
    }
}