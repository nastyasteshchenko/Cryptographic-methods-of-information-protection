package ru.nsu;

import java.security.SecureRandom;

/**
 * Класс KeyGenerator предоставляет метод для генерации 16-байтного ключа, используя генератор случайных чисел и
 * заданный алфавит символов.
 */
public class KeyGenerator {
    private static final int KEY_LENGTH = 16;
    private static final byte[] SYMBOLS = ("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ").getBytes();
    private final SecureRandom random = new SecureRandom();

    /**
     * Генерирует 16-байтный ключ, используя случайный выбор символов из алфавита.
     *
     * @return сгенерированный ключ в виде массива байтов длиной 16
     */
    public byte[] generate16BytesKey() {
        byte[] key = new byte[KEY_LENGTH];
        for (int i = 0; i < KEY_LENGTH; i++) {
            key[i] = SYMBOLS[(random.nextInt(SYMBOLS.length))];
        }
        return key;
    }
}