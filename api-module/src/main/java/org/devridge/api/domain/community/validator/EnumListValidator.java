package org.devridge.api.domain.community.validator;

import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.devridge.api.domain.community.entity.ProjectRole;

public class EnumListValidator implements ConstraintValidator<ValidateEnumList, List<String>> {

    @Override
    public boolean isValid(List<String> roles, ConstraintValidatorContext context) {
        if (roles == null || roles.isEmpty()) {
            return true;
        }

        for (String role : roles) {
            if (!ProjectRole.isValidRole(role)) {
                context.buildConstraintViolationWithTemplate("유효하지 않은 역할이 포함되어 있습니다: " + role)
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }

}
