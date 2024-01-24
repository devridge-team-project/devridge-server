package org.devridge.api.util;

import java.security.SecureRandom;

public class RandomGeneratorUtil {

    public static int generateFourDigitNumber() {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextInt(9990) + 10;
    }
}
