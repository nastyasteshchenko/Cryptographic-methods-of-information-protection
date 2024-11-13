package ru.nsu;

import java.security.SecureRandom;

public class KeyGenerator {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    private final SecureRandom random = new SecureRandom();

    /**
     * Генерирует ключ заданного размера для шифрования шифром Вернама, используя буквы русского и английского алфавитов.
     * @param length длина ключа.
     * @return сгенерированный ключ указанной длины.
     */
    public String generateKey(int length) {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = ALPHABET.charAt(random.nextInt(ALPHABET.length()));
        }

        return new String(chars);
    }
}
