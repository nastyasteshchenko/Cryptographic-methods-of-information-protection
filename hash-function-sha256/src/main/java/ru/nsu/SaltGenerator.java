package ru.nsu;

import java.security.SecureRandom;

/**
 * Класс для генерации случайной соли.
 * Соль используется для улучшения безопасности хеширования.
 * Генерирует случайные байты заданной длины с использованием криптографически безопасного генератора случайных чисел.
 */
public class SaltGenerator {
    private static final int SALT_LENGTH = 16;
    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Генерирует случайную соль криптографически безопасным способом.
     * Результат представляет собой массив байт заданной длины.
     *
     * @return Сгенерированная соль в виде массива байт.
     */
    public byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        return salt;
    }
}
