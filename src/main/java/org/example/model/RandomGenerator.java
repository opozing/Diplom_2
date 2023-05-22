package org.example.model;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomGenerator {

    public static String getRandom() {
        String random = RandomStringUtils.randomAlphabetic(10);
        return random;
    }
}
