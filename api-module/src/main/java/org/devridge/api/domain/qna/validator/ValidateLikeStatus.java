package org.devridge.api.domain.qna.validator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Documented
@Target({ FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { LikeStatusValidator.class })
public @interface ValidateLikeStatus {

    Class<? extends Enum<?>> enumClass();
    String message() default "추천/비추천 상태를 정해진 상수에 따라 입력해주세요.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean ignoreCase() default true;
}
