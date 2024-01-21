package org.devridge.api.domain.qna.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LikeStatusValidator implements ConstraintValidator<ValidateLikeStatus, String> {

    private ValidateLikeStatus validateLikeStatus;

    @Override
    public void initialize(ValidateLikeStatus constraintAnnotation) {
        this.validateLikeStatus = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Enum<?>[] enumValues = this.validateLikeStatus.enumClass().getEnumConstants();

        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if (
                    value.equals(enumValue.toString())
                    || this.validateLikeStatus.ignoreCase() && value.equalsIgnoreCase(enumValue.toString())
                ) {
                    return true;
                }
            }
        }

        return false;
    }
}
