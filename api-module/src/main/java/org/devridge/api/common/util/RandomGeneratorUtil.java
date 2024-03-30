package org.devridge.api.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RandomGeneratorUtil {

    private final int bound;
    private final int initialValue;

    public RandomGeneratorUtil(@Value("${devridge.email.bound}") int bound,
                               @Value("${devridge.email.initialValue}") int initialValue) {
        this.bound = bound;
        this.initialValue = initialValue;
    }

    public int generateFourDigitNumber() {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextInt(bound) + initialValue;
    }
}
