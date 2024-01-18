package org.devridge.api.domain.qna.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;

@Documented
@Target({ PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { OptionValidator.class })
public @interface ValidateSortOption {

    Class<? extends Enum<?>> enumClass();
    String message() default "정렬 옵션은 조회순, 최신순입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean ignoreCase() default true;
}
