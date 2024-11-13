package ru.nsu;

import java.security.SecureRandom;

/**
 * Класс KeyGenerator предоставляет метод для генерации случайных строковых ключей.
 * Алгоритм использует генератор случайных чисел для выбора символов из заданного набора.
 */
public class KeyGenerator {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    private final SecureRandom random = new SecureRandom();

    /**
     * Генерирует случайную строку заданной длины, выбирая символы из алфавита.
     *
     * @param length длина генерируемого ключа
     * @return строка, представляющая сгенерированный ключ
     */
    public String generateKey(int length) {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = ALPHABET.charAt(random.nextInt(ALPHABET.length()));
        }

        return new String(chars);
    }
}
