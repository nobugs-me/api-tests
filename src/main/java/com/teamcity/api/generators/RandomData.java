package com.teamcity.api.generators;

import org.apache.commons.lang3.RandomStringUtils;

public final class RandomData {

    private static final int LENGTH = 10;
    private static final String TEST_PREFIX = "test_";

    private RandomData() {
    }

    public static String getString() {
        return TEST_PREFIX + RandomStringUtils.randomAlphabetic(LENGTH);
    }

    public static String getString(int length) {
        // Генерируем строку кастомной длины, учитывая то, что test_ уже занимает 5 символов.
        // Строка меньше 10 символов не может получиться
        return TEST_PREFIX + RandomStringUtils.randomAlphabetic(Math.max(length - TEST_PREFIX.length(), LENGTH));
    }

}
