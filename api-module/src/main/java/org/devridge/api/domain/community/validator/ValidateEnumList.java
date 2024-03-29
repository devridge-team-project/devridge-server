package org.devridge.api.domain.community.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {EnumListValidator.class})
public @interface ValidateEnumList {

    String message() default "EnumList에 없는 값입니다.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
    Class<? extends Enum<?>> enumClass();
}
